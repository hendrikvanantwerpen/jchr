package be.kuleuven.jchr.compiler.CHRIntermediateForm.init;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import be.kuleuven.jchr.annotations.JCHR_Declare;
import be.kuleuven.jchr.annotations.JCHR_Init;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.IArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.constant.StringArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumentable.BuiltInMethod;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.Arguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.EmptyArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.AmbiguityException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.GenericType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.variables.Variable;
import be.kuleuven.jchr.util.Annotations;
import be.kuleuven.jchr.util.comparing.Comparison;



public class InitialisatorMethod 
    extends BuiltInMethod<InitialisatorMethod>
    implements IInitialisator<InitialisatorMethod> {
    
    private int identifierIndex = -1;
    
    public InitialisatorMethod() {
        // NOP
    }
    
    public InitialisatorMethod(Method method) {
        super(method);
    }
    
    @Override
    public void init(GenericType base, Method method) {     
        super.init(base, method);
        if (method.isAnnotationPresent(JCHR_Init.class))
            setIdentifierIndex(method.getAnnotation(JCHR_Init.class).identifier() + 1);
        else
            setIdentifierIndex(-1);
    }

    public boolean isConstructor() {
        return false;
    }

    public IType getType() {
        return getReturnType();
    }
    
    public Comparison compareTo(IInitialisator other) {
        return Initialisator.compare(this, other);
    }

    protected static InitialisatorMethod getInitialisatorMethodFrom(GenericType base, InitialisatorMethod best, Class<?> raw, IType type)
    throws AmbiguityException {
        return getInitialisatorMethodFrom(base, best, Annotations.getMarkedMethods(raw, JCHR_Init.class), type);
    }
    
    protected static InitialisatorMethod getInitialisatorMethodFrom(GenericType base, InitialisatorMethod best, List<Method> methods, IType type)
    throws AmbiguityException {
        InitialisatorMethod temp = new InitialisatorMethod();
        boolean ambiguous = false;
        
        for (Method method : methods) {
            temp.init(method);
            
            if (temp.isValidInitialisatorFrom(type) && temp.getType().isAssignableTo(base).isMatch()) {
                if (best == null)
                    best = temp.clone();
                else {                    
                    switch (temp.compareTo( (IInitialisator)best)) {
                        case BETTER:
                            ambiguous = false;
                            best = temp.clone();
                        break;
                        
                        case EQUAL:
                        case AMBIGUOUS:
                            ambiguous = true;
                        break;
                    }
                }
            }
        }
        
        if (ambiguous) throw new AmbiguityException();

        return best;
    }
    
    public static InitialisatorMethod getWrapperInitialisatorMethodFrom(GenericType base, IType type) 
    throws AmbiguityException {
        return getInitialisatorMethodFrom(base, null, getWrapperInitialisatorMethods(base), type);
    }
    
    public static List<Method> getWrapperInitialisatorMethods(GenericType base) {
        if (!base.isImmutableJavaWrapper()) throw new IllegalArgumentException();
        final List<Method> result = new ArrayList<Method>();
        for (Method method : base.getRawType().getMethods())
            if (isWrapperInititialisatorMethod(method))
                result.add(method);
        return result;
    }
    public static boolean isWrapperInititialisatorMethod(Method method) {
        return GenericType.getInstance(method.getDeclaringClass()).isImmutableJavaWrapper() && method.getName().equals("valueOf");
    }
    
    public static InitialisatorMethod getDeclaratorInitialisatorMethod(GenericType base) 
    throws AmbiguityException {
        InitialisatorMethod result = null;
        JCHR_Declare declare = base.getRawType().getAnnotation(JCHR_Declare.class);
        if (declare != null) try {
            InitialisatorMethod temp = new InitialisatorMethod();
            
            String id = declare.factoryClass();
            if (id.length() > 0) {
                for (Method method : Class.forName(id).getMethods()) {
                    if (method.isAnnotationPresent(JCHR_Declare.class)) {
                        temp.init(method);
                        
                        if (temp.getType().isAssignableTo(base).isMatch()) {
                            if (result == null)
                                result = new InitialisatorMethod(method);
                            else {                            
                                if (temp.usesIdentifier() && !result.usesIdentifier())
                                    result = temp.clone();
                                else if (!temp.usesIdentifier() && result.usesIdentifier())
                                    continue;
                                else
                                    throw new AmbiguityException();
                            }
                        }
                    }
                }
            }
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Unable to find factory class");
            cnfe.printStackTrace();
        }
        
        return result;
    }    
    
    public static InitialisatorMethod getInitialisatorMethodFrom(GenericType base, IType type) 
    throws AmbiguityException {
        InitialisatorMethod result = null;
        
        JCHR_Init init = base.getRawType().getAnnotation(JCHR_Init.class);
        if (init != null) try {
            String id = init.factoryClass();
            if (id.length() > 0)
                result = getInitialisatorMethodFrom(base, null, Class.forName(id), type);
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Unable to find factory class: ");
            cnfe.printStackTrace();
        }
        
        return getInitialisatorMethodFrom(base, result, base.getRawType(), type);
    }

    public int getIdentifierIndex() {
        return identifierIndex;
    }
    public boolean usesIdentifier() {
        return getIdentifierIndex() >= 0;
    }
    protected void setIdentifierIndex(int identifierIndex) {
        this.identifierIndex = identifierIndex;
    }
    
    public IInitialisatorInvocation<InitialisatorMethod> getInstance(IArgument argument, String identifier) {
        IArguments arguments = new Arguments(3 /* foert, is al beter dan de standaard */);
        arguments.addArgument(argument);
        
        if (usesIdentifier()) {
            if (identifier == null) 
                identifier = Variable.createIdentifier();
            arguments.addArgumentAt(getIdentifierIndex(), new StringArgument(identifier));
        }
        
        setImplicitArgumentOf(arguments);
        return getInstance(arguments);
    }    
    public IInitialisatorInvocation<InitialisatorMethod> getInstance(IArgument argument) {
        return getInstance(argument, null);
    }
    
    public InitialisatorMethodInvocation getInstance(IArguments arguments) {
        return new InitialisatorMethodInvocation(this, arguments);
    }
    
    public boolean isValidInitialisatorFrom(IType type) {
        return Initialisator.isValidInitialisatorFrom(this, type);
    }
    public boolean isValidDeclarationInitialisator() {
        return getArity() == (usesIdentifier()? 0 : 1);
    }
    public boolean isValidInitialisator() {        
        return (isWrapperInititialisatorMethod(getMethod()) || getMethod().isAnnotationPresent(JCHR_Init.class))
            && Initialisator.hasValidIdentifierParameter(this)
            && getArity() == (usesIdentifier()? 3 : 2);
    }
    
    public InitialisatorMethodInvocation getInstance() {
        return getInstance((String)null);
    }
    public InitialisatorMethodInvocation getInstance(String identifier) {
        if (usesIdentifier()) {            
            if (identifier == null) identifier = Variable.createIdentifier();
            return getInstance(new Arguments(Collections.singletonList((IArgument)new StringArgument(identifier))));
        }
        else return getInstance(EmptyArguments.getInstance());
    }
}
