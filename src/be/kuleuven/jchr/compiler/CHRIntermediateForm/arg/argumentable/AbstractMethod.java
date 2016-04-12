package be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumentable;


import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.MatchingInfos;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.reflect.Reflection;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.GenericType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.PrimitiveType;



/**
 * @author Peter Van Weert
 */
public abstract class AbstractMethod<T extends AbstractMethod<?>>
extends Argumentable<T> {

    private GenericType base;
    
    private Method method;
    
    public AbstractMethod() {
        // NOP
    }
    
    public AbstractMethod(Method method) {
        init(method);
    }
    
    public void init(Method method) {
        init(getBase(), method);
    }
    
    public void init(GenericType base, Method method) {
        setBase(base);
        setMethod(method);
    }
  
    public AbstractMethod(GenericType base, Method method) {        
        init(base, method);
    }
    
    public Method getMethod() {
        return method;
    }
    public String getName() {
        return getMethod().getName();        
    }
    protected void setMethod(Method method) {
        this.method = method;
    }
    
    public IType getFormalParameterAt(int index) {
    	if (index == 0)
    		return getBase();
		else
	        return Reflection.reflect(getBase(), getMethod().getGenericParameterTypes()[index-1]);
    }
    public int getArity() {
        return getMethod().getGenericParameterTypes().length + 1;
    }

    public IType getReturnType() {
        return Reflection.reflect(getBase(), getMethod().getGenericReturnType());
    }
    
    protected GenericType getBase() {
        return base;
    }
    protected void setBase(GenericType base) {
        this.base = base;
    }
    
    @Override
    public boolean equals(Object other) {
        return (other instanceof AbstractMethod) 
            && this.equals((AbstractMethod)other); 
    }
    
    public boolean equals(AbstractMethod other) {
        return  (this == other) || ( other != null
            &&  this.getMethod().equals(other.getMethod())
	    	&&  this.getBase().equals(other.getBase())
        );
    }
    
    @Override
    public int hashCode() {
        return 37 * (37 * 23 
            + getMethod().hashCode()) 
            + getBase().hashCode();
    }
    
    public boolean isValidArgument() {
        return ! getReturnType().equals(GenericType.getNonParameterizableInstance(Void.TYPE)); 
    }
    
    public boolean returnsBoolean() {
        return getReturnType() == PrimitiveType.BOOLEAN_TYPE
            || getReturnType() == GenericType.getNonParameterizableInstance(Boolean.class);
        /* we vertrouwen hier op auto-unboxing! */
    }
    
    @Override
    public String toString() {
        return getMethod().getName() + super.toString() + " returns " + getReturnType();
    }
    
    public boolean isStatic() {
        return Modifier.isStatic(getMethod().getModifiers());
    }
    public Class<?> getDeclaringClass() {
        return getMethod().getDeclaringClass();
    }
    public String getDeclaringClassName() {
        return getDeclaringClass().getCanonicalName();
    }
    
    @Override
    public MatchingInfos canHaveAsArguments(IArguments arguments) {
        if (! arguments.hasImplicitArgument()) 
            return MatchingInfos.NO_MATCH;
        else
            return super.canHaveAsArguments(arguments);
    }
}
