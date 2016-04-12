package be.kuleuven.jchr.compiler.CHRIntermediateForm.builder;

import static be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.CHRIntermediateFormBuilder.ConjunctBuildingBlock.Type.BI_INFIX;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.CHRIntermediateFormBuilder.ConjunctBuildingBlock.Type.BUILTIN;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.CHRIntermediateFormBuilder.ConjunctBuildingBlock.Type.COMPOSED_ID;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.CHRIntermediateFormBuilder.ConjunctBuildingBlock.Type.CONSTRUCTOR_INVOCATION_ARG;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.CHRIntermediateFormBuilder.ConjunctBuildingBlock.Type.INFIX;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.CHRIntermediateFormBuilder.ConjunctBuildingBlock.Type.MARKED_BUILTIN;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.CHRIntermediateFormBuilder.ConjunctBuildingBlock.Type.MARKED_INFIX;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.CHRIntermediateFormBuilder.ConjunctBuildingBlock.Type.METHOD_INVOCATION_ARG;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.CHRIntermediateFormBuilder.ConjunctBuildingBlock.Type.METHOD_INVOCATION_CON;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.CHRIntermediateFormBuilder.ConjunctBuildingBlock.Type.SIMPLE_ID;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.CHRIntermediateFormBuilder.ConjunctBuildingBlock.Type.UD_INFIX;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.CHRIntermediateFormBuilder.ConjunctBuildingBlock.Type.USER_DEFINED;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.CHRIntermediateForm;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.Handler;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.Identifier;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.ClassNameImplicitArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.Dummy;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.IArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.IImplicitArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.constant.BooleanArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.constant.ByteArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.constant.CharArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.constant.DoubleArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.constant.FloatArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.constant.IntArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.constant.LongArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.constant.NullArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.constant.ShortArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.constant.StringArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumented.IArgumented;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumented.IBasicArgumented;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.Arguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.ArgumentsDecorator;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.tables.BuiltInConstraintTable;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.tables.ClassTable;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.tables.OccurrenceTable;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.tables.RuleTable;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.tables.SolverTable;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.tables.UserDefinedConstraintTable;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.tables.VariableTable;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.IConjunct;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.BuiltInConstraint;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.BuiltInConstraintMethodInvocation;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.Failure;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IBuiltInConjunct;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IBuiltInConstraint;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IGuardConjunct;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.java.Assignment;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.java.AssignmentConjunct;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.java.IJavaConjunct;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.Occurrence;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.UserDefinedConstraint;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.AmbiguityException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.AmbiguousArgumentsException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.AmbiguousIdentifierException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.DuplicateIdentifierException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.IdentifierException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.IllegalArgumentsException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.IllegalIdentifierException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.IllegalVariableTypeException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.Matching;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.MatchingInfos;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.members.AbstractMethodInvocation;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.members.Constructor;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.members.ConstructorInvocation;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.members.Field;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.members.FieldAccess;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.rules.Rule;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.rules.RuleType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.solver.Solver;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.GenericType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.PrimitiveType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.TypeParameter;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.parameterizable.IParameterizable;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.variables.Variable;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.variables.VariableType;
import be.kuleuven.jchr.util.SingletonSet;
import be.kuleuven.jchr.util.builder.BasicBuilder;
import be.kuleuven.jchr.util.builder.BuilderException;
import be.kuleuven.jchr.util.builder.Current;



/**
 * @author Peter Van Weert
 */
public class CHRIntermediateFormBuilder extends
        BasicBuilder<CHRIntermediateForm> implements
        ICHRIntermediateFormBuilder<CHRIntermediateForm> {

	protected String chrPkg = "ud";
	
    protected final Current<Rule> currentRule = new Current<Rule>();

    protected final Current<UserDefinedConstraint> currentUdConstraint = new Current<UserDefinedConstraint>();

    protected final Current<ConjunctBuildingStrategy> currentCBS = new Current<ConjunctBuildingStrategy>();

    protected final Current<TypeParameter> currentTypeParameter = new Current<TypeParameter>();

    protected final Current<VariableType> currentVariableType = new Current<VariableType>();

    private boolean currentBoolean;

    private final Stack<IParameterizable> currentParameterizable = new Stack<IParameterizable>();

    private Handler handler;

    private Set<VariableType> variableTypes;

    private VariableTable variableTable;

    private UserDefinedConstraintTable udConstraintTable;

    private BuiltInConstraintTable biConstraintTable;

    private RuleTable ruleTable;

    private ClassTable classTable;

    private SolverTable solverTable;
    
    private OccurrenceTable identifiedOccurrences;

    protected void beginParameterizing(IParameterizable parameterizable) {
        currentParameterizable.push(parameterizable);
    }

    protected void beginNonParameterizable() {
        currentParameterizable.push(null);
    }

    protected boolean isParameterizing() {
        return !currentParameterizable.isEmpty()
                && currentParameterizable.peek() != null;
    }

    protected IParameterizable getCurrentParameterizable() {
        if (!isParameterizing())
            throw new IllegalStateException();
        return currentParameterizable.peek();
    }

    protected IParameterizable endParameterizing() {
        if (!isParameterizing())
            throw new IllegalStateException();
        return currentParameterizable.pop();
    }

    protected void endNonParameterizable() {
        if (currentParameterizable.pop() != null)
            throw new IllegalStateException();
    }

    public CHRIntermediateFormBuilder() {
        setBiConstraintTable(new BuiltInConstraintTable());
        setClassTable(new ClassTable());
        setRuleTable(new RuleTable());
        setSolverTable(new SolverTable());
        setUdConstraintTable(new UserDefinedConstraintTable());
        setVariableTable(new VariableTable());
        setVariableTypes(new HashSet<VariableType>());
        setIdentifiedOccurrences(new OccurrenceTable());
    }

    /**
     * @see be.kuleuven.jchr.util.builder.Builder#init()
     */
    @Override
    public void init() throws BuilderException {
        reset();
    }

    /**
     * TODO
     * 
     * @see be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.ICHRIntermediateFormBuilder#abort()
     */

    @Override
    public void abort() {
        reset();
    }

    protected void reset() {
        getBiConstraintTable().reset();
        getClassTable().reset();
        getRuleTable().reset();
        getSolverTable().reset();
        getUdConstraintTable().reset();
        getVariableTable().reset();
        getVariableTypes().clear();
        setResult(null);
    }

    /**
     * @see be.kuleuven.jchr.util.builder.Builder#finish()
     */

    @Override
    public void finish() throws BuilderException {
        if (super.getResult() != null)
            throw new BuilderException("Building already finished");
        setResult(new CHRIntermediateForm( getChrPackage(), getHandler(), getVariableTypes(),
                getVariableTable().getValues(), getUdConstraintTable()
                        .getValues(), getRuleTable().getValues(),
                getSolverTable().getValues()));
    }

    @Override
    public CHRIntermediateForm getResult() throws BuilderException {
        final CHRIntermediateForm result = super.getResult();
        if (result == null)
            throw new BuilderException("Did you forget to finish the building?");
        return result;
    }

    public void beginHandler(String identifier) throws BuilderException {
        try {
            setHandler(new Handler(identifier));
        } catch (IllegalIdentifierException ie) {
            throw new BuilderException(ie);
        }
    }

    public void beginTypeParameters() throws BuilderException {
        // NOP
    }

    public void beginTypeParameter(String identifier) throws BuilderException {
        try {
            currentTypeParameter.set(new TypeParameter(identifier));

        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        } catch (IllegalIdentifierException ie) {
            throw new BuilderException(ie);
        }
    }

    public void beginUpperBound(String name) throws BuilderException {
        try {
            Identifier.testIdentifier(name);
            if (PrimitiveType.isPrimitiveType(name))
                throw new IdentifierException(
                        "A primitive type cannot be a upperbound");
            else if (isTypeParameter(name)) {
                if (isCurrentTypeParameter(name))
                    throw new BuilderException("Illegal forward reference to type parameter " + name);
                else {
                    currentTypeParameter.get()
                            .addUpperBound(getTypeParameter(name));
                    beginNonParameterizable();
                }
            } else {
                final GenericType genericType = GenericType
                        .getInstance(getClass(name));

                if (genericType.isParameterizable())
                    beginParameterizing(genericType);
                else {
                    currentTypeParameter.get().addUpperBound(genericType);
                    beginNonParameterizable();
                }
            }

        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        } catch (IndexOutOfBoundsException iobe) {
            throw new BuilderException(iobe);
        } catch (IdentifierException ie) {
            throw new BuilderException(ie);
        } catch (IllegalArgumentException iae) {
            throw new BuilderException(iae);
        } catch (ClassNotFoundException cnfe) {
            throw new BuilderException(cnfe);
        }
    }

    public void endUpperBound() throws BuilderException {
        try {
            if (isParameterizing())
                currentTypeParameter.get().addUpperBound(
                        (GenericType) endParameterizing());
            else
                endNonParameterizable();

        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        } catch (ClassCastException cce) {
            throw new BuilderException(cce);
        }
    }

    public void endTypeParameter() throws BuilderException {
        try {
            getHandler().addTypeParameter(currentTypeParameter.get());
            currentTypeParameter.reset();
        } catch (DuplicateIdentifierException die) {
            throw new BuilderException(die);
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }

    public void endTypeParameters() throws BuilderException {
        // NOP
    }

    /**
     * @see be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.ICHRIntermediateFormBuilder#beginClassDeclarations()
     */

    public void beginClassDeclarations() throws BuilderException {
        // NOP
    }

    public void importClass(String id) throws BuilderException {
        try {
            Identifier.testIdentifier(id);
            getClassTable().importClass(id);

        } catch (NoClassDefFoundError ncdfe) {
            throw new BuilderException(ncdfe);
        } catch (ClassNotFoundException cnfe) {
            throw new BuilderException(cnfe);
        } catch (IllegalIdentifierException iie) {
            throw new BuilderException(iie);
        } catch (DuplicateIdentifierException die) {
            throw new BuilderException(die);
        }
    }
    
    public void importPackage(String id) throws BuilderException {
        try {
            Identifier.testIdentifier(id);
            getClassTable().importPackage(id);

        } catch (IllegalIdentifierException iie) {
            throw new BuilderException(iie);
        } 
    }
    

    /**
     * @see be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.ICHRIntermediateFormBuilder#endClassDeclarations()
     */

    public void endClassDeclarations() throws BuilderException {
//        getClassTable().ensureDefaultImports();
    }

    /**
     * @see be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.ICHRIntermediateFormBuilder#beginSolverDeclarations()
     */

    public void beginSolverDeclarations() throws BuilderException {
        // NOP
    }

    /**
     * @see be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.ICHRIntermediateFormBuilder#declareSolver(java.lang.String)
     */

    public void beginSolverDeclaration(String solverInterface)
            throws BuilderException {
        try {
            Identifier.testIdentifier(solverInterface);
            beginParameterizing(new Solver(getClass(solverInterface)));

        } catch (IllegalIdentifierException iie) {
            throw new BuilderException(iie);
        } catch (ClassNotFoundException cnfe) {
            throw new BuilderException(cnfe);
        } catch (AmbiguousIdentifierException aie) {
            throw new BuilderException(aie);
        } catch (IllegalArgumentException iae) {
            throw new BuilderException(iae);
        }
    }

    public void beginTypeArguments() throws BuilderException {
        if (!isParameterizing())
            throw new BuilderException("Not parameterizing");
    }

    public void beginTypeArgument(String id) throws BuilderException {
        try {
            Identifier.testIdentifier(id);

            if (PrimitiveType.isPrimitiveType(id))
                throw new IdentifierException("A primitive type cannot be a type parameter");
            if (isTypeParameter(id)) {
                getCurrentParameterizable().addTypeParameter(getTypeParameter(id));
                beginNonParameterizable();
            } else {
                GenericType genericType = GenericType.getInstance(getClass(id));

                if (genericType.isParameterizable())
                    beginParameterizing(genericType);
                else {
                    getCurrentParameterizable().addTypeParameter(genericType);
                    beginNonParameterizable();
                }
            }

        } catch (IdentifierException iie) {
            throw new BuilderException(iie);
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        } catch (ClassNotFoundException cnfe) {
            throw new BuilderException(cnfe);
        } catch (AmbiguityException ae) {
            throw new BuilderException(ae);
        }
    }

    public void endTypeArgument() throws BuilderException {
        try {
            if (isParameterizing()) {
                getCurrentParameterizable().addTypeParameter(
                        (GenericType) endParameterizing());
            } else
                endNonParameterizable();

        } catch (IllegalArgumentException iae) {
            throw new BuilderException(iae);
        } catch (IndexOutOfBoundsException iobe) {
            throw new BuilderException(iobe);
        } catch (AmbiguityException ae) {
            throw new BuilderException(ae);
        }
    }

    public void endTypeArguments() throws BuilderException {
        try {
            if (isParameterizing() && !getCurrentParameterizable().isValid())
                throw new BuilderException("Invalid type arguments");

        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }

    public void buildSolverName(String identifier) throws BuilderException {
        try {
            if (identifier == null)
                identifier = getSolverTable().createID();
            else {
                Identifier.testUdSimpleIdentifier(identifier);
                if (getSolverTable().isDeclaredId(identifier))
                    throw new DuplicateIdentifierException(identifier);
            }
            ((Solver) getCurrentParameterizable()).setIdentifier(identifier);

        } catch (IllegalIdentifierException iie) {
            throw new BuilderException(iie);
        } catch (DuplicateIdentifierException die) {
            throw new BuilderException(die);
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        } catch (ClassCastException cce) {
            throw new BuilderException(cce);
        }
    }

    /**
     * @see be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.ICHRIntermediateFormBuilder#endSolverDeclarations()
     */

    public void endSolverDeclaration() throws BuilderException {
        try {
            final Solver solver = (Solver) endParameterizing();
            getSolverTable().declareSolver(solver);
            getBiConstraintTable().registerSolver(solver);

        } catch (DuplicateIdentifierException die) {
            throw new BuilderException(die);
        } catch (IdentifierException ie) {
            throw new BuilderException(ie);
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        } catch (IllegalArgumentException iae) {
            throw new BuilderException(iae);
        } catch (ClassCastException cce) {
            throw new BuilderException(cce);
        }
    }

    /**
     * @see be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.ICHRIntermediateFormBuilder#endSolverDeclarations()
     */

    public void endSolverDeclarations() throws BuilderException {
        // NOP
    }

    /**
     * @see be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.ICHRIntermediateFormBuilder#beginConstraintDeclarations()
     */

    public void beginConstraintDeclarations() throws BuilderException {
        if (currentUdConstraint.isSet()) {
            throw new BuilderException("Illegal state");
        }
    }

    /**
     * @see be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.ICHRIntermediateFormBuilder#beginConstraintDeclaration(java.lang.String)
     */

    public void beginConstraintDeclaration(String id) throws BuilderException {
        try {
            Identifier.testUdSimpleIdentifier(id);
            currentUdConstraint.set(getUdConstraintTable()
                    .declareConstraint(id));

        } catch (IllegalIdentifierException iie) {
            throw new BuilderException(iie);
        } catch (DuplicateIdentifierException die) {
            throw new BuilderException(die);
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }

    public void buildInfixIdentifier(String infix) throws BuilderException {
        try {
            final UserDefinedConstraint ud = currentUdConstraint.get();
            
            if (ud.getArity() != 2)
                throw new BuilderException(currentUdConstraint.get()
                        .getIdentifier()
                        + " is not a binary constraint "
                        + "=> cannot have infix notation");

            ud.setInfix(infix);            
            getUdConstraintTable().declareInfixFor(ud);
        } catch (DuplicateIdentifierException die) {
            throw new BuilderException(die);
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }

    /**
     * @see be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.ICHRIntermediateFormBuilder#beginConstraintArgument()
     */
    public void beginConstraintArgument() throws BuilderException {
        // NOP
    }

    public void beginConstraintArgumentType(String type, boolean fixed)
            throws BuilderException {
        beginVariableType(type, fixed);
    }

    public void endConstraintArgumentType() throws BuilderException {
        endVariableType();
    }

    public void buildConstraintArgumentName(String name)
            throws BuilderException {

        try {
            final UserDefinedConstraint current = currentUdConstraint.get();
            if (name == null)
                name = "$X" + current.getArity();
            else
                Identifier.testUdSimpleIdentifier(name, true);

            current.addVariable(new Variable(currentVariableType.get(), name));

        } catch (IllegalIdentifierException iie) {
            throw new BuilderException(iie);
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        } catch (DuplicateIdentifierException die) {
            throw new BuilderException(die);
        }
    }

    public void endConstraintArgument() throws BuilderException {
        currentVariableType.reset();
    }

    /**
     * @see be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.ICHRIntermediateFormBuilder#endConstraintDeclaration()
     */

    public void endConstraintDeclaration() {
        currentUdConstraint.reset();
    }

    /**
     * @see be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.ICHRIntermediateFormBuilder#endConstraintDeclarations()
     */

    public void endConstraintDeclarations() throws BuilderException {
        if (currentUdConstraint.isSet()) {
            throw new BuilderException("Illegal state");
        }
    }

    /**
     * @see be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.ICHRIntermediateFormBuilder#beginRules()
     */

    public void beginRules() throws BuilderException {
        if (currentRule.isSet())
            throw new BuilderException("Illegal state");
    }

    public void beginVariableDeclarations() throws BuilderException {
        // NOP
    }

    public void beginVariableType(String type, boolean fixed)
            throws BuilderException {
        try {
            if (PrimitiveType.isPrimitiveType(type)) {
                currentVariableType.set(VariableType.getInstance(PrimitiveType
                        .getInstance(type), fixed));
                beginNonParameterizable();
            } else {
                Identifier.testIdentifier(type);

                if (isTypeParameter(type)) {
                    currentVariableType.set(VariableType.getInstance(
                            getTypeParameter(type), fixed));
                    beginNonParameterizable();
                } else {
                    final GenericType genericType = GenericType
                            .getInstance(getClass(type));

                    if (genericType.isParameterizable()) {
                        beginParameterizing(genericType);
                        currentBoolean = fixed;
                    } else {
                        currentVariableType.set(VariableType.getInstance(
                                genericType, fixed));
                        beginNonParameterizable();
                    }
                }
            }
        } catch (IllegalIdentifierException iie) {
            throw new BuilderException(iie);
        } catch (ClassNotFoundException cnfe) {
            throw new BuilderException(cnfe);
        } catch (IllegalArgumentException iae) {
            throw new BuilderException(iae);
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        } catch (IllegalVariableTypeException ivte) {
            throw new BuilderException(ivte);
        } catch (AmbiguousIdentifierException aie) {
            throw new BuilderException(aie);
        }
    }

    public void endVariableType() throws BuilderException {
        try {
            if (isParameterizing()) {
                currentVariableType.set(VariableType.getInstance(
                        (GenericType) endParameterizing(), currentBoolean));
            }

            final VariableType type = currentVariableType.get();
            if (!type.hasInitializedBuiltInConjuncts()) {
                guardCBS.beginBuiltinConstraint(IBuiltInConstraint.EQ);

                guardCBS.beginArguments();
                guardCBS.addArgument(Dummy.getOneInstance(type));
                guardCBS.addArgument(Dummy.getOtherInstance(type.getType()));
                guardCBS.endArguments();

                type.initEq((IBasicArgumented)guardCBS.getBuiltinConstraint(type.isFixed()));

                addVariableType(type);
            }

        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        } catch (IllegalVariableTypeException ivte) {
            throw new BuilderException(ivte);
        }
    }

    /**
     * @see be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.ICHRIntermediateFormBuilder#declareVariable(java.lang.String,
     *      boolean)
     */

    public void declareVariable(String id) throws BuilderException {

        try {
            Identifier.testUdSimpleIdentifier(id, true);
            getVariableTable().declareVariable(currentVariableType.get(), id);

        } catch (IllegalIdentifierException iie) {
            throw new BuilderException(iie);
        } catch (DuplicateIdentifierException die) {
            throw new BuilderException(die);
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        } catch (ClassCastException cce) {
            throw new BuilderException(cce);
        }
    }

    /**
     * @see be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.ICHRIntermediateFormBuilder#endVariableDeclarations()
     */

    public void endVariableDeclarations() throws BuilderException {
        currentVariableType.reset();
    }
    
    public void beginRule() throws BuilderException {
        if (currentRule.isSet())
            throw new BuilderException("Previous rule not finished yet");
    }

    public void beginRuleDefinition(RuleType type) throws BuilderException {
        beginRuleDefinition(null, type);
    }

    /**
     * @see be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.ICHRIntermediateFormBuilder#beginRuleDefinition(java.lang.String,
     *      int)
     */
    public void beginRuleDefinition(String id, RuleType type)
            throws BuilderException {
        try {
            currentRule.set(getRuleTable().declareRule(id, type));

        } catch (DuplicateIdentifierException die) {
            throw new BuilderException(die);
        } catch (IllegalIdentifierException iie) {
            throw new BuilderException(iie);
        } catch (IllegalArgumentException iae) {
            throw new BuilderException(iae);
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }

    public void beginBody() throws BuilderException {
        try {
            currentCBS.set(bodyCBS);

        } catch (IllegalStateException ise) {
            throw new BuilderException();
        }
    }

    /**
     * @see be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.ICHRIntermediateFormBuilder#beginGuardConstraint(java.lang.String)
     */

    public void beginGuard() throws BuilderException {
        try {
            currentCBS.set(guardCBS);

        } catch (IllegalStateException ise) {
            throw new BuilderException();
        }
    }

    public void beginHead() throws BuilderException {
        // NOP
    }

    public void beginKeptOccurrences() throws BuilderException {
        try {
            occurrenceBS.setRemoved(Occurrence.KEPT);
            currentCBS.set(occurrenceBS);

        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }

    public void beginRemovedOccurrences() throws BuilderException {
        try {
            occurrenceBS.setRemoved(Occurrence.REMOVED);
            currentCBS.set(occurrenceBS);

        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }
    
    public void addFailureConjunct() throws BuilderException {
        currentCBS.get().addFailureConjunct();
    }

    public void addFieldAccessConjunct(String id) throws BuilderException {
        try {
            Identifier.testComposedIdentifier(id);
            currentCBS.get().addFieldAccessConstraint(id);
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        } catch (IllegalIdentifierException iie) {
            throw new BuilderException(iie);
        } catch (UnsupportedOperationException uoe) {
            throw new BuilderException(uoe);
        }
    }

    public void beginIdentifiedConjunct(String id) throws BuilderException {
        if (Identifier.isSimple(id))
            beginSimpleIdConjunct(id);
        else
            beginComposedIdConjunct(id);
    }

    public void beginSimpleIdConjunct(String id) throws BuilderException {
        try {
            currentCBS.get().beginSimpleIdConstraint(id);
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }

    public void beginBuiltInConstraint(String id) throws BuilderException {
        try {
            currentCBS.get().beginBuiltinConstraint(id);
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }

    public void beginUserDefinedConstraint(String id) throws BuilderException {
        try {
            currentCBS.get().beginUserDefinedConstraint(id);
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }

    public void beginComposedIdConjunct(String id) throws BuilderException {
        try {
            currentCBS.get().beginComposedIdConstraint(id);
        } catch (IllegalStateException iie) {
            throw new BuilderException(iie);
        }
    }

    public void beginMarkedBuiltInConstraint(String id) throws BuilderException {
        try {
            currentCBS.get().beginMarkedBuiltinConstraint(id);
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }

    public void beginMethodInvocationConjunct(String id)
            throws BuilderException {
        try {
            currentCBS.get().beginMethodInvocationConstraint(id);
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }

    public void beginArguments() throws BuilderException {
        try {
            currentCBS.get().beginArguments();
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }

    public void addConstantArgument() throws BuilderException {
        try {
            currentCBS.get().addConstantArgument();
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }

    public void addConstantArgument(boolean value) throws BuilderException {
        try {
            currentCBS.get().addConstantArgument(value);
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }

    public void addConstantArgument(byte value) throws BuilderException {
        try {
            currentCBS.get().addConstantArgument(value);
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }

    public void addConstantArgument(char value) throws BuilderException {
        try {
            currentCBS.get().addConstantArgument(value);
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }

    public void addConstantArgument(double value) throws BuilderException {
        try {
            currentCBS.get().addConstantArgument(value);
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }

    public void addConstantArgument(float value) throws BuilderException {
        try {
            currentCBS.get().addConstantArgument(value);
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }

    public void addConstantArgument(int value) throws BuilderException {
        try {
            currentCBS.get().addConstantArgument(value);
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }

    public void addConstantArgument(long value) throws BuilderException {
        try {
            currentCBS.get().addConstantArgument(value);
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }

    public void addConstantArgument(short value) throws BuilderException {
        try {
            currentCBS.get().addConstantArgument(value);
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }

    public void addConstantArgument(String value) throws BuilderException {
        try {
            if (value == null)
                throw new BuilderException("null <> String literal");
            currentCBS.get().addConstantArgument(value);
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }
    
    public void addFieldAccessArgument(String id) throws BuilderException {
        try {
            Identifier.testComposedIdentifier(id);
            currentCBS.get().addFieldAccessArgument(id);
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        } catch (IllegalIdentifierException iie) {
            throw new BuilderException(iie);
        }
    }

    public void addIdentifiedArgument(String id) throws BuilderException {
        try {
            Identifier.testIdentifier(id);
            currentCBS.get().addIdentifiedArgument(id);
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        } catch (IllegalIdentifierException iie) {
            throw new BuilderException(iie);
        }
    }

    public void beginConstructorInvocationArgument(String id)
            throws BuilderException {
        try {
            currentCBS.get().beginConstructorInvocationArgument(id);
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }

    public void beginMethodInvocationArgument(String id)
            throws BuilderException {
        try {
            currentCBS.get().beginMethodInvocationArgument(id);
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        } catch (UnsupportedOperationException uoe) {
            throw new BuilderException(uoe);
        }
    }

    public void endMethodInvocationArgument() throws BuilderException {
        try {
            currentCBS.get().endMethodInvocationArgument();
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }

    public void endConstructorInvocationArgument() throws BuilderException {
        try {
            currentCBS.get().endConstructorInvocationArgument();
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }

    public void endArguments() throws BuilderException {
        try {
            currentCBS.get().endArguments();
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }

    public void endSimpleIdConjunct() throws BuilderException {
        try {
            currentCBS.get().endSimpleIdConstraint();
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }

    public void endBuiltInConstraint() throws BuilderException {
        try {
            currentCBS.get().endBuiltinConstraint();
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }

    public void endUserDefinedConstraint() throws BuilderException {
        try {
            currentCBS.get().endUserDefinedConstraint();
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }

    public void endComposedIdConjunct() throws BuilderException {
        try {
            currentCBS.get().endComposedIdConstraint();
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        } catch (UnsupportedOperationException uoe) {
            throw new BuilderException(uoe);
        }
    }

    public void endMarkedBuiltInConstraint() throws BuilderException {
        try {
            currentCBS.get().endMarkedBuiltinConstraint();
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        } catch (UnsupportedOperationException uoe) {
            throw new BuilderException(uoe);
        }
    }

    public void endMethodInvocationConjunct() throws BuilderException {
        try {
            currentCBS.get().endMethodInvocationConstraint();
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        } catch (UnsupportedOperationException uoe) {
            throw new BuilderException(uoe);
        }
    }

    public void endIdentifiedConjunct() throws BuilderException {
        try {
            currentCBS.get().endIdentifiedConstraint();
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        } catch (UnsupportedOperationException uoe) {
            throw new BuilderException(uoe);
        }
    }

    public void beginInfixConstraint() throws BuilderException {
        try {
            currentCBS.get().beginInfixConstraint();
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }

    public void buildBuiltInInfix(String infix) throws BuilderException {
        try {
            currentCBS.get().buildBuiltinInfix(infix);
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }

    public void buildInfix(String infix) throws BuilderException {
        try {
            currentCBS.get().buildInfix(infix);
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }

    public void buildMarkedInfix(String infix) throws BuilderException {
        try {
            currentCBS.get().buildMarkedInfix(infix);
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }

    public void buildUserDefinedInfix(String infix) throws BuilderException {
        try {
            currentCBS.get().buildUserDefinedInfix(infix);
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }

    public void endInfixConstraint() throws BuilderException {
        try {
            currentCBS.get().endInfixConstraint();
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }
    
    public void buildOccurrenceId(String id) throws BuilderException {
        try {
            final Occurrence occurrence = currentRule.get().getLastOccurence();
            getIdentifiedOccurrences().identify(id, occurrence);
            
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        } catch (DuplicateIdentifierException die) {
            throw new BuilderException(die);
        } catch (IllegalIdentifierException iie) {
            throw new BuilderException(iie);
        }
    }

    public void endKeptOccurrences() throws BuilderException {
        currentCBS.reset();
    }

    public void endRemovedOccurrences() throws BuilderException {
        currentCBS.reset();
    }

    public void endHead() throws BuilderException {
        if (occurrenceBS.hasUnknownVariables())
            throw new BuilderException("Unknown variable(s) used: "
                    + occurrenceBS.getUnknownVariables());
        else
            occurrenceBS.resetUnknownVariables();
    }

    public void endGuard() throws BuilderException {
        currentCBS.reset();
    }

    public void endBody() throws BuilderException {
        currentCBS.reset();
    }

    public void endRuleDefinition() throws BuilderException {
        // NOP
    }
    
    public void beginPragmas() throws BuilderException {
        // NOP
    }
    
    public void addPassivePragma(String id) throws BuilderException {
        try {
            getIdentifiedOccurrence(id).setPassive();
        } catch (IllegalIdentifierException iie) {
            throw new BuilderException(iie);
        }
    }
    
    public void endPragmas() throws BuilderException {
        // NOP
    }
    
    public void endRule() throws BuilderException {
        try {
            final String id = currentRule.get().getIdentifier();
            System.out.println("Compiled rule: " + id);

            if (!currentRule.get().isValid())
                throw new BuilderException("Invallid rule definition: " + id);

            currentRule.reset();
            occurrenceBS.resetUsedVariables();
            bodyCBS.resetDeclaredVariables();
            getIdentifiedOccurrences().reset();

        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }

    public void endRules() throws BuilderException {
        if (currentRule.isSet()) {
            throw new BuilderException("Illegal state");
        }
    }

    /**
     * @see be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.ICHRIntermediateFormBuilder#endHandler()
     */

    public void endHandler() throws BuilderException {
        // NOP
    }

    protected Handler getHandler() {
        return handler;
    }

    protected void setHandler(Handler handler) {
        this.handler = handler;
    }

    protected Set<VariableType> getVariableTypes() {
        return variableTypes;
    }
    
    protected void addVariableType(VariableType type) {
        if (getVariableTypes().add(type))
            getBiConstraintTable().registerVariableType(type);
    }

    protected void setVariableTypes(Set<VariableType> variableTypes) {
        this.variableTypes = variableTypes;
    }

    protected BuiltInConstraintTable getBiConstraintTable() {
        return biConstraintTable;
    }

    protected void setBiConstraintTable(BuiltInConstraintTable biConstraintTable) {
        this.biConstraintTable = biConstraintTable;
    }

    protected ClassTable getClassTable() {
        return classTable;
    }

    protected void setClassTable(ClassTable classTable) {
        this.classTable = classTable;
    }

    protected RuleTable getRuleTable() {
        return ruleTable;
    }

    protected void setRuleTable(RuleTable ruleTable) {
        this.ruleTable = ruleTable;
    }

    protected SolverTable getSolverTable() {
        return solverTable;
    }

    protected void setSolverTable(SolverTable solverTable) {
        this.solverTable = solverTable;
    }

    protected UserDefinedConstraintTable getUdConstraintTable() {
        return udConstraintTable;
    }

    protected void setUdConstraintTable(UserDefinedConstraintTable udConstraintTable) {
        this.udConstraintTable = udConstraintTable;
    }

    protected VariableTable getVariableTable() {
        return variableTable;
    }

    protected void setVariableTable(VariableTable variableTable) {
        this.variableTable = variableTable;
    }
    
    protected OccurrenceTable getIdentifiedOccurrences() {
        return identifiedOccurrences;
    }
    
    protected void setIdentifiedOccurrences(
            OccurrenceTable identifiedOccurrences) {
        this.identifiedOccurrences = identifiedOccurrences;
    }

    protected boolean isVariable(String id) {
        return getVariableTable().isDeclaredId(id);
    }

    protected Variable getVariable(String id) {
        return getVariableTable().get(id);
    }

    protected Solver getSolver(String id) {
        return getSolverTable().get(id);
    }

    protected boolean isSolver(String id) {
        return getSolver(id) != null;
    }

    protected Class<?> getClass(String identifier) 
    throws ClassNotFoundException, AmbiguousIdentifierException {        
        return getClassTable().getClass(identifier);
    }

    protected boolean isTypeParameter(String id) {
        return getHandler().hasAsTypeParameter(id)
            || isCurrentTypeParameter(id);
    }
    
    protected boolean isCurrentTypeParameter(String id) {
        return currentTypeParameter.isSet()
            && currentTypeParameter.get().getIdentifier().equals(id);
    }

    protected TypeParameter getTypeParameter(String id) {
        if (isCurrentTypeParameter(id))
            return currentTypeParameter.get();
        else
            return getHandler().getTypeParameter(id);
    }

    protected UserDefinedConstraint getUdConstraint(String identifier, boolean infix)
            throws IllegalIdentifierException {
        UserDefinedConstraint result = getUdConstraintTable().get(identifier, infix);
        if (result == null)
            throw new IllegalIdentifierException(
                    "No constraint with identifier " + identifier
                            + " has been defined by the user.");
        return result;
    }

    protected Set<IBuiltInConstraint<?>> getBiConstraints(Solver solver,
            String identifier, boolean asks, int arity, boolean infix)
            throws IllegalIdentifierException {
        Set<IBuiltInConstraint<?>> result = getBiConstraintTable().get(solver,
                identifier, asks, arity, infix);
        if (result == null)
            throw new IllegalIdentifierException(
                    "No builtin constraint with identifier "
                            + BuiltInConstraintTable.createBiConstraintId(
                                    identifier, asks, arity) + " known.");
        return result;
    }

    protected Set<IBuiltInConstraint<?>> getBiConstraints(String identifier,
            boolean asks, int arity, boolean infix)
            throws IllegalIdentifierException {
        Set<IBuiltInConstraint<?>> result = getBiConstraintTable().get(identifier,
                asks, arity, infix);
        if (result == null)
            throw new IllegalIdentifierException(
                    "No builtin constraint with identifier "
                            + BuiltInConstraintTable.createBiConstraintId(
                                    identifier, asks, arity) + " known.");
        return result;
    }
    
    protected Occurrence getIdentifiedOccurrence(String id) throws IllegalIdentifierException {
        final Occurrence result = getIdentifiedOccurrences().get(id);
        if (result == null) 
            throw new IllegalIdentifierException("No occurrence was tagged with " + id);
        return result;
    }
    
    /*
     * version 1.0.3    (Peter Van Weert)
     *  - Assignments are treated differently: if it is a 
     *      tell-constraint with the right identifier, an assignment
     *      is always added to the result.
     *      See also changes in the built-in-constraint-table!
     */
    protected Set<IBuiltInConstraint<?>> getJavaConstraints(
        ConjunctBuildingBlock current, boolean asks, boolean infix) throws IllegalIdentifierException {
        
        final String identifier = current.getIdentifier();
        final boolean canBeAssignment 
            = identifier.equals(Assignment.ID) && !asks; 
        final Set<IBuiltInConstraint<?>> result 
            = getBiConstraintTable().getJavaConstraints(identifier, asks, infix);
        
        if (canBeAssignment) {
            if (result == null) {            
                return new SingletonSet<IBuiltInConstraint<?>>(
                    new Assignment(current.getTypeAt(0))
                );
            }
            else    // result != null
                result.add(new Assignment(current.getTypeAt(0)));
        }
        
        // ! canBeAssignment
        if (result == null)
            throw new IllegalIdentifierException(
                "No java constraint with identifier "
                    + BuiltInConstraintTable.createBiConstraintId(identifier, asks, 2) 
                    + " known."
                );
        
        return result;
    }

    protected final BodyConjunctBuildingStrategy bodyCBS = new BodyConjunctBuildingStrategy();

    protected final GuardConjunctBuildingStrategy guardCBS = new GuardConjunctBuildingStrategy();

    protected final OccurrenceBuildingStrategy occurrenceBS = new OccurrenceBuildingStrategy();

    protected abstract class ConjunctBuildingStrategy {
        private Stack<ConjunctBuildingBlock> currentBuildingBlock;

        public ConjunctBuildingStrategy() {
            setCurrentBuildingBlock(new Stack<ConjunctBuildingBlock>());
        }

        protected Stack<ConjunctBuildingBlock> getCurrentBuildingBlock() {
            return currentBuildingBlock;
        }

        protected void setCurrentBuildingBlock(
                Stack<ConjunctBuildingBlock> stack) {
            this.currentBuildingBlock = stack;
        }

        protected void push(ConjunctBuildingBlock.Type type) {
            push(new ConjunctBuildingBlock(type));
        }

        protected void push(String id, ConjunctBuildingBlock.Type type) {
            push(new ConjunctBuildingBlock(id, type));
        }

        protected void push(ConjunctBuildingBlock buildingBlock) {
            if (buildingBlock.isConstraint() != getCurrentBuildingBlock()
                    .isEmpty())
                throw new IllegalStateException();
            getCurrentBuildingBlock().push(buildingBlock);
        }

        protected ConjunctBuildingBlock pop() {
            try {
                return getCurrentBuildingBlock().pop();
            } catch (EmptyStackException ese) {
                throw new IllegalStateException("No current building block set");
            }
        }

        protected ConjunctBuildingBlock pop(ConjunctBuildingBlock.Type type) {
            final ConjunctBuildingBlock result = pop();
            if (result.getType() != type)
                throw new IllegalStateException("Expected: " + type
                        + " <> Found: " + result.getType());
            return result;
        }

        protected ConjunctBuildingBlock peek() throws IllegalStateException {
            try {
                return getCurrentBuildingBlock().peek();
            } catch (EmptyStackException ese) {
                throw new IllegalStateException("No current building block set");
            }
        }

        protected ConjunctBuildingBlock peek(ConjunctBuildingBlock.Type type)
                throws IllegalStateException {
            final ConjunctBuildingBlock result = peek();
            if (result.getType() != type)
                throw new IllegalStateException("Expected: " + type
                        + " <> Found: " + result.getType());
            return result;
        }

        protected boolean buildingTopLevel() {
            return getCurrentBuildingBlock().size() == 1;
        }
        
        public abstract void addFailureConjunct()
                throws BuilderException;

        public abstract void addFieldAccessConstraint(String id)
                throws BuilderException;

        public abstract void beginSimpleIdConstraint(String id)
                throws BuilderException;

        public abstract void beginBuiltinConstraint(String id)
                throws BuilderException;

        public abstract void beginComposedIdConstraint(String id)
                throws BuilderException;

        public abstract void beginMarkedBuiltinConstraint(String id)
                throws BuilderException;

        public abstract void beginMethodInvocationConstraint(String id)
                throws BuilderException;

        public void beginUserDefinedConstraint(String id)
                throws BuilderException {
            try {
                if (!getUdConstraintTable().isDeclaredId(id, false))
                    throw new IllegalIdentifierException(id);
                push(id, USER_DEFINED);

            } catch (IllegalIdentifierException iie) {
                throw new BuilderException(iie);
            } catch (IllegalStateException ise) {
                throw new BuilderException(ise);
            }
        }

        public void beginArguments() throws BuilderException {
            // NOP
        }

        protected void addArgument(IArgument argument) throws BuilderException {
            try {
                peek().addArgument(argument);
            } catch (IllegalStateException ise) {
                throw new BuilderException(ise);
            }
        }

        public void addConstantArgument() throws BuilderException {
            addArgument(NullArgument.getInstance());
        }

        public void addConstantArgument(boolean value) throws BuilderException {
            addArgument(BooleanArgument.getInstance(value));
        }

        public void addConstantArgument(byte value) throws BuilderException {
            addArgument(new ByteArgument(value));
        }

        public void addConstantArgument(char value) throws BuilderException {
            addArgument(new CharArgument(value));
        }

        public void addConstantArgument(double value) throws BuilderException {
            addArgument(new DoubleArgument(value));
        }

        public void addConstantArgument(float value) throws BuilderException {
            addArgument(new FloatArgument(value));
        }

        public void addConstantArgument(int value) throws BuilderException {
            addArgument(new IntArgument(value));
        }

        public void addConstantArgument(long value) throws BuilderException {
            addArgument(new LongArgument(value));
        }

        public void addConstantArgument(short value) throws BuilderException {
            addArgument(new ShortArgument(value));
        }

        public void addConstantArgument(String value) throws BuilderException {
            addArgument(new StringArgument(value));
        }
        
        public void addFieldAccessArgument(String value) throws BuilderException {
            addArgument(getFieldAccess(value));
        }

        public void addIdentifiedArgument(String id) throws BuilderException {
            if (isVariable(id))
                addVariableArgument(getVariable(id));
            else
                addArgument(getImplicietArgument(id));
        }
        
        protected boolean isVariable(String id) {
            return CHRIntermediateFormBuilder.this.isVariable(id);
        }
        
        protected Variable getVariable(String id) {
            return CHRIntermediateFormBuilder.this.getVariable(id);
        }

        protected abstract void addVariableArgument(Variable var)
                throws BuilderException;

        public void beginMethodInvocationArgument(String id)
                throws BuilderException {
            try {
                Identifier.testComposedIdentifier(id);
                push(id, METHOD_INVOCATION_ARG);

            } catch (IllegalIdentifierException iie) {
                throw new BuilderException(iie);
            } catch (IllegalStateException ise) {
                throw new BuilderException(ise);
            }
        }

        public void endMethodInvocationArgument() throws BuilderException {
            final AbstractMethodInvocation<?> invocation = endMethodInvocation(pop(METHOD_INVOCATION_ARG));
            if (invocation.canBeArgument())
                addArgument(invocation);
            else
                throw new BuilderException("Illegal argument: " + invocation);
        }

        public void beginConstructorInvocationArgument(String id)
                throws BuilderException {
            try {
                Identifier.testComposedIdentifier(id);
                final Class theClass = CHRIntermediateFormBuilder.this.getClass(id);
                final GenericType theType = GenericType.getInstance(theClass);

                if (theType.isParameterizable())
                    beginParameterizing(theType);
                else
                    beginNonParameterizable();

                push(id, CONSTRUCTOR_INVOCATION_ARG);

            } catch (IllegalIdentifierException iie) {
                throw new BuilderException(iie);
            } catch (IllegalStateException ise) {
                throw new BuilderException(ise);
            } catch (ClassNotFoundException cnfe) {
                throw new BuilderException(cnfe);
            } catch (AmbiguousIdentifierException aie) {
                throw new BuilderException(aie);
            }
        }

        public void endConstructorInvocationArgument() throws BuilderException {
            final ConjunctBuildingBlock current = pop(CONSTRUCTOR_INVOCATION_ARG);
            GenericType theType = null;
            
            try {
                if (isParameterizing())
                    theType = (GenericType) endParameterizing();
                else {
                    theType = GenericType.getInstance(
                        CHRIntermediateFormBuilder.this.getClass(current.getIdentifier())
                    );
                    endNonParameterizable();
                }

                final Constructor[] constructors = theType.getConstructors();
                final Set<Constructor> set = new HashSet<Constructor>(constructors.length);
                Collections.addAll(set, constructors);
                addArgument(
                    (ConstructorInvocation) Matching.getBestMatch(set, current.getArguments())
                );

            } catch (ClassNotFoundException cnfe) {
                throw new BuilderException(cnfe);
            } catch (AmbiguousIdentifierException aie) {
                throw new BuilderException(aie);
            } catch (AmbiguousArgumentsException ae) {
                throw new BuilderException("Unable to determine best suited constructor for type " + theType, ae);
            } catch (IllegalArgumentsException iae) {
                throw new BuilderException("No suitable constructor found for type " + theType, iae);
            } 
        }

        protected AbstractMethodInvocation endMethodInvocation(
                final ConjunctBuildingBlock current) throws BuilderException {

            final String methodName = Identifier.getTail(current.getIdentifier());
            
            try {
                final IImplicitArgument implicitArgument = 
                    getImplicietArgument(Identifier.getBody(current.getIdentifier()));
                current.getArguments().addImplicitArgument(implicitArgument);
                
                return (AbstractMethodInvocation)
                    Matching.getBestMatch(
                        implicitArgument.getMethods(methodName), 
                        current.getArguments()
                    );
                
            } catch (AmbiguousArgumentsException aae) {
                throw new BuilderException("Unable to determine which method with name " + methodName + " was ment", aae);
            } catch (IllegalArgumentsException iae) {
                throw new BuilderException("No suitable method found with name \"" + methodName + "\"", iae);
            } catch (IllegalArgumentException iae) {
                throw new BuilderException(iae);
            }
        }
        
        protected FieldAccess getFieldAccess(String id) throws BuilderException {
            try {
                final IImplicitArgument implicitArgument            
                    = getImplicietArgument(Identifier.getBody(id));
                final Field field 
                    = implicitArgument.getField(Identifier.getTail(id));
                
                return field.getInstance(new Arguments(implicitArgument));
                
            } catch (NoSuchFieldException nsfe) {
                throw new BuilderException(nsfe);
            } catch (AmbiguityException ae) {
                throw new BuilderException(ae);
            }
        }

        protected IImplicitArgument getImplicietArgument(String id)
                throws BuilderException {
            try {
                return getArgumentRec(id);
            } catch (IdentifierException ie) {
                throw new BuilderException(ie);
            }
        }

        private IImplicitArgument getArgumentRec(String id)
                throws IdentifierException, BuilderException {
            if (Identifier.isSimple(id)) {
                if (getVariableTable().isDeclaredId(id))
                    return getVariableTable().get(id);
                else if (getSolverTable().isDeclaredId(id))
                    return getSolverTable().get(id);
                else if (isTypeParameter(id))
                    throw new BuilderException(
                            "Cannot select from a type variable: " + id);

            } else { // Identifier.isComposed(id)
                try {
                    final IImplicitArgument implicitArgument = getArgumentRec(Identifier
                            .getBody(id));
                    final Field field = implicitArgument.getField(Identifier
                            .getTail(id));
                    final IArguments arguments = new Arguments(implicitArgument);
                    return (IImplicitArgument) field.getInstance(arguments,
                            field.canHaveAsArguments(arguments));

                } catch (NoSuchFieldException nsfe) {
                    // NOP
                } catch (AmbiguityException ae) {
                    // NOP
                } catch (IdentifierException ie) {
                    // NOP
                }
            }

            try {
                return new ClassNameImplicitArgument(
                        CHRIntermediateFormBuilder.this.getClass(id));
            } catch (ClassNotFoundException cnfe) {
                throw new IdentifierException("Unknown identifier: " + id);
            }
        }

        public void endArguments() throws BuilderException {
            // NOP
        }

        public void endIdentifiedConstraint() throws BuilderException {
            if (Identifier.isSimple(peek().getIdentifier()))
                endSimpleIdConstraint();
            else
                endComposedIdConstraint();
        }

        public abstract void endSimpleIdConstraint() throws BuilderException;

        public abstract void endBuiltinConstraint() throws BuilderException;

        public abstract void endUserDefinedConstraint() throws BuilderException;

        public abstract void endComposedIdConstraint() throws BuilderException;

        public abstract void endMarkedBuiltinConstraint()
                throws BuilderException;

        public abstract void endMethodInvocationConstraint()
                throws BuilderException;

        public void beginInfixConstraint() throws BuilderException {
            try {
                push(INFIX);
            } catch (IllegalStateException ise) {
                throw new BuilderException(ise);
            }
        }

        public void buildInfix(String infix) throws BuilderException {
            if (Identifier.isComposed(infix)) {
                final String head = Identifier.getHead(infix);
                if (head.equals(IBuiltInConstraint.BUILTIN_MARK) || isSolver(infix)) {
                    buildMarkedInfix(infix);
                    return;
                }
            }
            peek(INFIX).setIdentifier(infix);
        }

        protected void buildInfix(String infix, ConjunctBuildingBlock.Type type)
                throws BuilderException {
            peek(INFIX).setIdentifier(infix);
            peek(INFIX).setType(type);
        }

        public void buildBuiltinInfix(String infix) throws BuilderException {
            buildInfix(infix, BI_INFIX);
        }

        public void buildMarkedInfix(String infix) throws BuilderException {
            buildInfix(infix, MARKED_INFIX);
        }

        public void buildUserDefinedInfix(String infix) throws BuilderException {
            buildInfix(infix, UD_INFIX);
        }

        public abstract void endInfixConstraint() throws BuilderException;

        protected ConjunctBuildingBlock popInfixConstraint()
                throws IllegalStateException {
            final ConjunctBuildingBlock current = pop();

            if (!current.hasIdentifier())
                throw new IllegalStateException(
                        "The identifier for the current constraint has not been set");
            if (current.getArity() != 2)
                throw new IllegalStateException(
                        "Illegal number of arguments for an infix constraint");
            if (!current.getType().isInfix())
                throw new IllegalStateException(
                        "Currently not building an infix constraint");
            return current;
        }
    }

    protected abstract class BasicConjunctBuildingStrategy extends
            ConjunctBuildingStrategy {

        @Override
        public void beginSimpleIdConstraint(String id) throws BuilderException {
            try {
                Identifier.testSimpleIdentifier(id);
                push(id, SIMPLE_ID);

            } catch (IllegalIdentifierException iie) {
                throw new BuilderException(iie);
            } catch (IllegalStateException ise) {
                throw new BuilderException(ise);
            }
        }

        @Override
        public void beginBuiltinConstraint(String id) throws BuilderException {
            try {
                Identifier.testSimpleIdentifier(id);
                push(id, BUILTIN);

            } catch (IllegalIdentifierException iie) {
                throw new BuilderException(iie);
            } catch (IllegalStateException ise) {
                throw new BuilderException(ise);
            }
        }

        @Override
        public void beginComposedIdConstraint(String id)
                throws BuilderException {
            try {
                Identifier.testComposedIdentifier(id);
                push(id, COMPOSED_ID);

            } catch (IllegalIdentifierException iie) {
                throw new BuilderException(iie);
            } catch (IllegalStateException ise) {
                throw new BuilderException(ise);
            }
        }

        @Override
        public void beginMarkedBuiltinConstraint(String id)
                throws BuilderException {
            try {
                Identifier.testComposedIdentifier(id);
                push(id, MARKED_BUILTIN);

            } catch (IllegalIdentifierException iie) {
                throw new BuilderException(iie);
            } catch (IllegalStateException ise) {
                throw new BuilderException(ise);
            }
        }

        @Override
        public void beginMethodInvocationConstraint(String id)
                throws BuilderException {
            try {
                Identifier.testComposedIdentifier(id);
                push(id, METHOD_INVOCATION_CON);

            } catch (IllegalIdentifierException iie) {
                throw new BuilderException(iie);
            } catch (IllegalStateException ise) {
                throw new BuilderException(ise);
            }
        }

        protected abstract boolean buildingAskConjunct();

        protected abstract void addConjunct(IConjunct conjunct,
                boolean infix) throws BuilderException;

        @Override
        public void endBuiltinConstraint() throws BuilderException {
            addConjunct(getBuiltinConstraint(), false);
        }

        public IBuiltInConjunct<?> getBuiltinConstraint(boolean allFixed)
                throws BuilderException {
            try {
                return getBuiltinConstraint(pop(BUILTIN), false, allFixed);
            } catch (IllegalStateException ise) {
                throw new BuilderException(ise);
            }
        }

        public IBuiltInConjunct<?> getBuiltinConstraint() throws BuilderException {
            try {
                return getBuiltinConstraint(pop(BUILTIN), false);
            } catch (IllegalStateException ise) {
                throw new BuilderException(ise);
            }
        }

        protected void endBuiltinConstraint(ConjunctBuildingBlock current,
                boolean infix) throws BuilderException {
            addConjunct(getBuiltinConstraint(current, infix), infix);
        }

        protected IBuiltInConjunct<?> getBuiltinConstraint(
                ConjunctBuildingBlock current, boolean infix)
                throws BuilderException {
            return getBuiltinConstraint(current, infix, current.allFixed());
        }
        
        /*
         * version 1.0.3    (Peter Van Weert)
         *  - Added buildingAskConjunct() to the test whether
         *      or not a Java-constraint should be tried first.
         */
        protected IBuiltInConjunct<?> getBuiltinConstraint(
                ConjunctBuildingBlock current, boolean infix, boolean allFixed)
                throws BuilderException {
            try {
                if (buildingAskConjunct() && allFixed)
                    return getJavaConstraintTry(current, infix);
                else
                    return getBuiltinConstraintTry(current, infix);

            } catch (AmbiguousArgumentsException aae) {
                throw new BuilderException(aae); // failure
            } catch (IllegalArgumentsException iae) {
                // NOP (try next)
            } catch (IllegalIdentifierException iie) {
                // NOP (try next)
            }

            if (!buildingAskConjunct() || !allFixed)
                return getJavaConstraint(current, infix);
            else
                return getBuiltinConstraint2(current, infix);
        }

        protected IBuiltInConjunct<?> getBuiltinConstraint2(
                ConjunctBuildingBlock current, boolean infix)
                throws BuilderException {
            try {
                return getBuiltinConstraintTry(current, infix);
            } catch (AmbiguousArgumentsException aae) {
                throw new BuilderException(aae);
            } catch (IllegalArgumentsException iae) {
                throw new BuilderException(iae);
            } catch (IllegalIdentifierException iie) {
                throw new BuilderException(iie);
            }
        }

        protected void endBuiltinConstraintTry(ConjunctBuildingBlock current,
                boolean infix) throws AmbiguousArgumentsException,
                IllegalArgumentsException, BuilderException,
                IllegalIdentifierException {
            addConjunct(getBuiltinConstraintTry(current, infix), infix);
        }

        protected IBuiltInConjunct<?> getBuiltinConstraintTry(
                ConjunctBuildingBlock current, boolean infix)
                throws AmbiguousArgumentsException, IllegalArgumentsException,
                BuilderException, IllegalIdentifierException {
            try {
                IBuiltInConjunct<?> result = (IBuiltInConjunct<?>) Matching
                        .getBestMatch(getBiConstraints(current.getIdentifier(),
                                buildingAskConjunct(), current.getArity(),
                                infix), current.getArguments());
                return result;
            } catch (IllegalStateException ise) {
                throw new BuilderException(ise);
            }
        }

        protected IJavaConjunct<?> getJavaConstraint(
                ConjunctBuildingBlock current, boolean infix)
                throws BuilderException {
            try {
                return getJavaConstraintTry(current, infix);
            } catch (IllegalArgumentsException iae) {
                throw new BuilderException(iae);
            } catch (AmbiguousArgumentsException aae) {
                throw new BuilderException(aae);
            } catch (IllegalIdentifierException iie) {
                throw new BuilderException(iie);
            }
        }
        
        protected IJavaConjunct<?> getJavaConstraintTry(
                ConjunctBuildingBlock current, boolean infix)
                throws BuilderException, IllegalArgumentsException,
                AmbiguousArgumentsException, IllegalIdentifierException {
            try {
                return (IJavaConjunct<?>) Matching.getBestMatch(
                    getJavaConstraints(current, buildingAskConjunct(), infix), 
                    current.getArguments()
                );
            } catch (IllegalStateException ise) {
                throw new BuilderException(ise);
            }
        }

        @Override
        public void endComposedIdConstraint() throws BuilderException {
            try {
                final ConjunctBuildingBlock current = pop(COMPOSED_ID);
                if (Identifier.isSimple(Identifier.getBody(current
                        .getIdentifier())))
                    try {
                        endMarkedBuiltinConstraintTry(current, false);
                        return; // success
                    } catch (AmbiguousArgumentsException aae) {
                        throw new BuilderException(aae); // failure
                    } catch (IllegalArgumentsException iae) {
                        // NOP (try next)
                    } catch (IllegalIdentifierException iie) {
                        // NOP (try next)
                    }

                endMethodInvocationConstraint(current);

            } catch (IllegalStateException ise) {
                throw new BuilderException(ise);
            }
        }

        @Override
        public void endMarkedBuiltinConstraint() throws BuilderException {
            endMarkedBuiltinConstraint(pop(MARKED_BUILTIN), false);
        }

        protected void endMarkedBuiltinConstraint(
                ConjunctBuildingBlock current, boolean infix)
                throws BuilderException {
            try {
                endMarkedBuiltinConstraintTry(current, infix);

            } catch (IllegalIdentifierException iae) {
                throw new BuilderException(iae);
            } catch (IllegalArgumentsException iae) {
                throw new BuilderException(iae);
            } catch (AmbiguousArgumentsException aae) {
                throw new BuilderException(aae);
            }
        }

        protected void endMarkedBuiltinConstraintTry(
                ConjunctBuildingBlock current, boolean infix)
                throws IllegalArgumentsException, AmbiguousArgumentsException,
                IllegalIdentifierException, BuilderException {
            final String head = Identifier.getHead(current.getIdentifier()), tail = Identifier
                    .getEntireTail(current.getIdentifier());

            if (head.equals(IBuiltInConstraint.BUILTIN_MARK)) {
                current.setIdentifier(tail);
                endBuiltinConstraintTry(current, infix);
            } else {
                if (!isSolver(head))
                    throw new IllegalIdentifierException(current
                            .getIdentifier());
                else
                    try {
                        addConjunct((IConjunct) Matching.getBestMatch(
                                getBiConstraints(getSolver(head), current
                                        .getIdentifier(),
                                        buildingAskConjunct(), current
                                                .getArity(), infix), current
                                        .getArguments()), infix);
                    } catch (IllegalStateException ise) {
                        throw new BuilderException(ise);
                    }
            }
        }

        @Override
        public void endMethodInvocationConstraint() throws BuilderException {
            try {
                endMethodInvocation(pop(METHOD_INVOCATION_CON));
            } catch (IllegalStateException ise) {
                throw new BuilderException(ise);
            }
        }

        protected void endMethodInvocationConstraint(
                ConjunctBuildingBlock current) throws BuilderException {
            final AbstractMethodInvocation<?> invocation = endMethodInvocation(current);
            if (buildingAskConjunct() && !invocation.canBeAskConjunct())
                throw new BuilderException("Illegal ask constraint: "
                        + invocation);
            addConjunct(invocation, false);
        }

        @Override
        public void endInfixConstraint() throws BuilderException {
            final ConjunctBuildingBlock current = popInfixConstraint();
            switch (current.getType()) {
            case INFIX:
                endSimpleIdConstraint(current, true);
                break;
            case UD_INFIX:
                endUserDefinedConstraint(current, true);
                break;
            case BI_INFIX:
                endBuiltinConstraint(current, true);
                break;
            case MARKED_INFIX:
                endMarkedBuiltinConstraint(current, true);
                break;
            default:
                throw new RuntimeException("fout in popInfixConstraint?");
            }
        }

        @Override
        public final void endSimpleIdConstraint() throws BuilderException {
            try {
                endSimpleIdConstraint(pop(SIMPLE_ID), false);
            } catch (IllegalStateException ise) {
                throw new BuilderException(ise);
            }
        }

        @Override
        public final void endUserDefinedConstraint() throws BuilderException {
            try {
                endUserDefinedConstraint(pop(USER_DEFINED), false);
            } catch (IllegalStateException ise) {
                throw new BuilderException(ise);
            }
        }

        public abstract void endSimpleIdConstraint(
                ConjunctBuildingBlock current, boolean infix)
                throws BuilderException;

        public abstract void endUserDefinedConstraint(
                ConjunctBuildingBlock current, boolean infix)
                throws BuilderException;
    }

    protected class BodyConjunctBuildingStrategy extends
            BasicConjunctBuildingStrategy {

        @Override
        protected boolean buildingAskConjunct() {
            return false;
        }

        @Override
        protected void addConjunct(IConjunct conjunct, boolean infix)
                throws BuilderException {
            try {
                boolean initialisation = false;
                
                if (hasVariablesToDeclare()) {
                    if (conjunct instanceof AssignmentConjunct) {
                        AssignmentConjunct assignment = (AssignmentConjunct)conjunct;
                        if (hasToDeclare(assignment.getArgumentAt(1)))
                            throw new BuilderException(
                                "Using unknown variable: " + assignment.getArgumentAt(1)
                            );
                        
                        // ==> hasToDeclare(conjunct.getArgumentAt(0)))
                        ((AssignmentConjunct) conjunct).setDeclarator();
                        addDeclaredVariable((Variable) assignment.getArgumentAt(0));
                        
                    } else if (conjunct instanceof BuiltInConstraintMethodInvocation) {
                        BuiltInConstraintMethodInvocation builtIn =
                            (BuiltInConstraintMethodInvocation) conjunct;
                        BuiltInConstraint constraint =
                            builtIn.getArgumentableType();
                        if (constraint.isEquality()) {
                            final IArgument
                                arg0 = builtIn.getArgumentIgnoringImplicitAt(0), 
                                arg1 = builtIn.getArgumentIgnoringImplicitAt(1);
                            final boolean 
                                dec0 = hasToDeclare(arg0),
                                dec1 = hasToDeclare(arg1);

                            if (dec0 && dec1) {
                                doDeclaration((Variable) arg1);
                                if (doInitialisation((Variable) arg0, arg1))
                                    initialisation = true;
                            } else if (dec0 && !dec1) {
                                if (doInitialisation((Variable) arg0, arg1))
                                    initialisation = true;
                                else
                                    doDeclaration((Variable) arg0);
                            } else if (!dec0 && dec1) {
                                if (doInitialisation((Variable) arg1, arg0))
                                    initialisation = true;
                                else
                                    doDeclaration((Variable) arg1);
                            }
                        }
                    } 
                    
                    for (Variable variable : getVariablesToDeclare())
                        doDeclaration(variable, false);
                    resetVariablesToDeclare();
                }

                
                if (!initialisation)
                    currentRule.get().addBodyConjunct(conjunct);

            } catch (IllegalStateException ise) {
                throw new BuilderException(ise);
            }
        }
        
        @Override
        public void addFailureConjunct() throws BuilderException {
            currentRule.get().addBodyConjunct(Failure.getInstance());
        }

        @Override
        public void addFieldAccessConstraint(String id) throws BuilderException {
            throw new BuilderException(
                "You cannot use a field access as body-constraint!"
            );
        }

        @Override
        protected void addVariableArgument(Variable var)
                throws BuilderException {
            ensureDeclared(var);
            addArgument(var);
        }

        protected boolean isDeclared(Variable var) {
            return occurrenceBS.hasUsedVariable(var)
                || hasDeclared(var)
                || hasToDeclare(var);
        }

        private List<Variable> 
            variablesToDeclare = new ArrayList<Variable>(),
            declaredVariables = new ArrayList<Variable>();

        protected void doDeclaration(Variable var) throws BuilderException {
            doDeclaration(var, true);
        }
        
        protected void doDeclaration(Variable var, boolean register) throws BuilderException {
            if (! var.isFixed()) {
                try {
                    final IConjunct conjunct = var.getDeclaratorInstance();
                    if (conjunct == null)
                        throw new BuilderException("Cannot declare " + var);
                    currentRule.get().addBodyConjunct(conjunct);
                    addDeclaredVariable(var, register);
                } catch (IllegalStateException ise) {
                    throw new BuilderException(ise);
                } catch (AmbiguityException ae) {
                    throw new BuilderException(ae);
                }
            } else {
                throw new BuilderException(
                    "Using unknown \"fixed\" variable: " + var
                );
            }
        }

        protected boolean doInitialisation(Variable var, IArgument argument)
                throws BuilderException {
            try {
                final IConjunct conjunct = 
                    var.getInitialisingDeclaratorInstanceFrom(argument.getType());

                if (conjunct == null) return false;

                currentRule.get().addBodyConjunct(conjunct);
                addDeclaredVariable(var);

                return true;

            } catch (IllegalStateException ise) {
                throw new BuilderException(ise);
            } catch (AmbiguityException ae) {
                throw new BuilderException(ae);
            }
        }

        /**
         * Ensures the given variable will be declared. If we are building a
         * top-level argumentable (i.e. a conjunct), this means we will add
         * it to the "variables to be declared", which will be dealt with 
         * when this conjunct is finished. If not, we do declaration right
         * away.   
         */
        protected void ensureDeclared(Variable var) throws BuilderException {
            if (isDeclared(var)) return;
            
            if (!buildingTopLevel())
                doDeclaration(var);
            else
                addVariableToDeclare(var);
        }

        public void resetVariablesToDeclare() {
            getVariablesToDeclare().clear();
        }

        protected boolean hasVariablesToDeclare() {
            return !getVariablesToDeclare().isEmpty();
        }
        
        private boolean hasToDeclare(IArgument argument) {
            return (argument instanceof Variable)
                    && hasToDeclare((Variable) argument);
        }

        protected boolean hasToDeclare(Variable var) {
            return getVariablesToDeclare().contains(var);
        }

        protected int getNbVariablesToDeclare() {
            return getVariablesToDeclare().size();
        }

        protected List<Variable> getVariablesToDeclare() {
            return variablesToDeclare;
        }

        protected void addVariableToDeclare(Variable var) {
            getVariablesToDeclare().add(var);
        }

        protected List<Variable> getDeclaredVariables() {
            return declaredVariables;
        }

        protected boolean hasDeclared(Variable variable) {
            return declaredVariables.contains(variable);
        }

        protected void addDeclaredVariable(Variable variable) {
            addDeclaredVariable(variable, true);
        }
        protected void addDeclaredVariable(Variable variable, boolean register) {
            getDeclaredVariables().add(variable);
            if (register && hasToDeclare(variable))
                getVariablesToDeclare().remove(variable);
        }

        protected void resetDeclaredVariables() {
            getDeclaredVariables().clear();
        }

        @Override
        public void endSimpleIdConstraint(ConjunctBuildingBlock current,
                boolean infix) throws BuilderException {
            try {
                endUserDefinedConstraintTry(current, infix);
                return; // success
            } catch (AmbiguousArgumentsException aae) {
                throw new BuilderException(aae); // failure
            } catch (IllegalArgumentsException iae) {
                // NOP (try next)
            } catch (IllegalIdentifierException iie) {
                // NOP (try next)
            }

            endBuiltinConstraint(current, infix);
        }

        @Override
        public void endUserDefinedConstraint(ConjunctBuildingBlock current,
                boolean infix) throws BuilderException {
            try {
                endUserDefinedConstraintTry(current, infix);
            } catch (AmbiguousArgumentsException aae) {
                throw new BuilderException(aae);
            } catch (IllegalArgumentsException iae) {
                throw new BuilderException(iae);
            } catch (IllegalIdentifierException iie) {
                throw new BuilderException(iie);
            }
        }

        protected void endUserDefinedConstraintTry(
                ConjunctBuildingBlock current, boolean infix)
                throws AmbiguousArgumentsException, IllegalArgumentsException,
                IllegalIdentifierException, BuilderException {

            final UserDefinedConstraint constraint = getUdConstraint(current
                    .getIdentifier(), infix);
            final IArguments arguments = current.getArguments();
            final MatchingInfos matchingInfos = constraint
                    .canHaveAsArguments(arguments);

            if (matchingInfos.isAmbiguous())
                throw new AmbiguousArgumentsException(constraint, arguments);
            if (!matchingInfos.isMatch())
                throw new IllegalArgumentsException(constraint, arguments);

            addConjunct((IConjunct) constraint.getInstance(arguments,
                    matchingInfos), infix);
        }
    }

    protected class GuardConjunctBuildingStrategy extends
            BasicConjunctBuildingStrategy {
        @Override
        protected boolean buildingAskConjunct() {
            return true;
        }

        @Override
        protected void addConjunct(IConjunct conjunct, boolean infix)
                throws BuilderException {
            try {
                currentRule.get().addGuardConstraint((IGuardConjunct) conjunct);
            } catch (IllegalStateException ise) {
                throw new BuilderException(ise);
            } catch (ClassCastException cce) {
                throw new BuilderException(cce);
            }
        }
        
        @Override
        public void addFailureConjunct() throws BuilderException {
            throw new UnsupportedOperationException("You cannot add a failure to a guard!");
        }

        @Override
        public void addFieldAccessConstraint(String id) throws BuilderException {
            try {
                final IImplicitArgument implicitArgument = getImplicietArgument(Identifier
                        .getBody(id));
                final Field field = implicitArgument.getField(Identifier
                        .getTail(id));
                addConjunct(field.getInstance(new Arguments(implicitArgument)), false);
                
            } catch (IllegalArgumentException iae) {
                throw new BuilderException(iae);
            } catch (AmbiguityException ae) {
                throw new BuilderException(ae);
            } catch (NoSuchFieldException nsfe) {
                throw new BuilderException(nsfe);
            }
        }

        @Override
        protected void addVariableArgument(Variable var)
                throws BuilderException {
            if (occurrenceBS.hasUsedVariable(var))
                addArgument(var);
            else
                throw new BuilderException("Local variable found in guard: "
                        + var);
        }

        @Override
        public void endSimpleIdConstraint(ConjunctBuildingBlock current,
                boolean infix) throws BuilderException {
            endBuiltinConstraint(current, infix);
        }

        @Override
        public void beginUserDefinedConstraint(String id)
                throws BuilderException {
            throw new BuilderException("No deep guards are allowed");
        }

        @Override
        public void endUserDefinedConstraint(ConjunctBuildingBlock current,
                boolean infix) throws BuilderException {
            throw new BuilderException("No deep guards are allowed");
        }

        @Override
        public void buildUserDefinedInfix(String infix) throws BuilderException {
            throw new BuilderException("No deep guards are allowed");
        }
    }

    protected class OccurrenceBuildingStrategy extends
            ConjunctBuildingStrategy {
        private final HashMap<String, Integer> usedVariables;

        private final Set<Variable> unknownVariables;

        private final UnsupportedOperationException UNSUPPORTED 
            = new UnsupportedOperationException("Only user defined constraints allowed in head");

        protected final static String NON_VARIABLE = "";

        protected boolean removed;

        public OccurrenceBuildingStrategy(boolean removed) {
            this();
            setRemoved(removed);
        }

        public OccurrenceBuildingStrategy() {
            usedVariables = new HashMap<String, Integer>();
            unknownVariables = new HashSet<Variable>();
            resetUsedVariables();
        }

        protected HashMap<String, Integer> getUsedVariables() {
            return usedVariables;
        }

        public void testVariable(Variable variable) {
            if (!hasUsedVariable(variable))
                addUnknownVariable(variable);
        }

        public void addUnknownVariable(Variable variable) {
            getUnknownVariables().add(variable);
        }

        public void removeUnknownVariable(Variable variable) {
            getUnknownVariables().remove(variable);
        }

        public Set<Variable> getUnknownVariables() {
            return unknownVariables;
        }

        public void resetUnknownVariables() {
            getUnknownVariables().clear();
        }

        public boolean hasUsedVariable(String id) {
            return getUsedVariables().containsKey(id);
        }

        public boolean hasUsedVariable(Variable variable) {
            return hasUsedVariable(variable.getIdentifier());
        }

        public void addUsedVariable(Variable variable) {
            addUsedVariable(variable.getIdentifier());
            removeUnknownVariable(variable);
        }

        protected void addUsedVariable(String id) {
            getUsedVariables().put(id, Integer.valueOf(0));
        }

        public boolean hasUnknownVariables() {
            return !getUnknownVariables().isEmpty();
        }

        public void resetUsedVariables() {
            getUsedVariables().clear();
            addUsedVariable(NON_VARIABLE);
        }

        public String createIdentifier(Variable variable) {
            return createIdentifier(variable.getIdentifier());
        }

        public String createIdentifier(String id) {
            int nb = usedVariables.get(id).intValue();
            usedVariables.put(id, Integer.valueOf(++nb));
            return "$" + id + nb;
        }

        @Override
        public void beginUserDefinedConstraint(String id)
                throws BuilderException {
            push(id, USER_DEFINED);
        }
        
        @Override
        public void endUserDefinedConstraint() throws BuilderException {
            try {
                endUserDefinedConstraint(pop(USER_DEFINED), false);
            } catch (IllegalStateException ise) {
                throw new BuilderException(ise);
            }
        }

        protected void endUserDefinedConstraint(
            ConjunctBuildingBlock current,
            boolean infix
        ) throws BuilderException {
            try {
                final UserDefinedConstraint udConstraint 
                    = getUdConstraint(current.getIdentifier(), infix);
                final IArguments arguments = current.getArguments();

                IArgument argument;
                Variable variable;

                final int arity = arguments.getArity();
                for (int i = 0; i < arity; i++) {
                    argument = arguments.getArgumentAt(i);

                    if (argument instanceof Variable) {
                        variable = (Variable) argument;
                        if (! variable.isAnonymous()) {
                            // TODO stop processing occurrences from left to right!
                            if (hasUsedVariable(variable)) {
                                final Variable newVar = makeGuardExcplicitFor(
                                    variable,
                                    udConstraint.getVariableTypeAt(i)
                                );
                                arguments.replaceArgumentAt(i, newVar);
                            } else { // !hasUsedVariable(variable)
                                if (!argument.isDirectlyAssignableTo(
                                    udConstraint.getFormalParameterAt(i)
                                )) {
                                    throw new BuilderException("Illegal argument: " + argument);
                                }
                                else {
                                    addUsedVariable(variable);
                                }
                            }
                        }
                    } else { // ! argument instanceof Variable
                        
                        // If the argument is not a variable, it could contain
                        // variables that are unknown...
                        //  TODO stop processing heads from left to right!
                        if (argument instanceof IArgumented<?>) {
                            for (Variable var : ((IArgumented<?>) argument).getVariablesInArguments())
                                testVariable(var);
                        }

                        arguments.replaceArgumentAt(i, makeGuardExcplicitFor(
                            argument, udConstraint.getVariableTypeAt(i))
                        );
                    }
                }

                udConstraint.newInstance(currentRule.get(), arguments,
                        isRemoved());

            } catch (IdentifierException ie) {
                throw new BuilderException(ie);
            }
        }
        
        @Override
        public void addFailureConjunct() throws BuilderException {
            throw UNSUPPORTED;
        }

        @Override
        public void addFieldAccessConstraint(String id) throws BuilderException {
            throw UNSUPPORTED;
        }

        @Override
        public void beginComposedIdConstraint(String id)
                throws BuilderException {
            throw UNSUPPORTED;
        }

        @Override
        public void endComposedIdConstraint() throws BuilderException {
            throw UNSUPPORTED;
        }

        @Override
        public void beginMarkedBuiltinConstraint(String id)
                throws BuilderException {
            throw UNSUPPORTED;
        }

        @Override
        public void endMarkedBuiltinConstraint() throws BuilderException {
            throw UNSUPPORTED;
        }

        @Override
        public void beginSimpleIdConstraint(String id) throws BuilderException {
            beginUserDefinedConstraint(id);
        }

        @Override
        public void endSimpleIdConstraint() throws BuilderException {
            endUserDefinedConstraint();
        }

        @Override
        public void beginBuiltinConstraint(String id) throws BuilderException,
                UnsupportedOperationException {
            throw UNSUPPORTED;
        }

        @Override
        public void endBuiltinConstraint() throws BuilderException,
                UnsupportedOperationException {
            throw UNSUPPORTED;
        }

        @Override
        public void beginMethodInvocationConstraint(String id)
                throws BuilderException, UnsupportedOperationException {
            throw UNSUPPORTED;
        }

        @Override
        public void endMethodInvocationConstraint() throws BuilderException,
                UnsupportedOperationException {
            throw UNSUPPORTED;
        }

        @Override
        public void addVariableArgument(Variable variable)
                throws BuilderException {
            addArgument(variable);
        }
        
        @Override
        protected boolean isVariable(String id) {
            return (id.charAt(0) == '_') || super.isVariable(id);
        }
        
        @Override
        protected Variable getVariable(String id) {
            if (id.charAt(0) == '_')
                return Variable.getAnonymousInstance();
            else 
                return super.getVariable(id);
        }

        protected Variable makeGuardExcplicitFor(IArgument argument,
                VariableType expectedType) throws BuilderException {
            return makeGuardExcplicitFor(createIdentifier(NON_VARIABLE),
                    argument, expectedType);
        }

        protected Variable makeGuardExcplicitFor(
            Variable variable,
            VariableType expectedType
        ) throws BuilderException {
            return makeGuardExcplicitFor(
                createIdentifier(variable), variable, expectedType
            );
        }

        protected Variable makeGuardExcplicitFor(
            String newIdentifier,
            IArgument argument, 
            VariableType expectedType
        ) throws BuilderException {
            Variable variable = expectedType.getInstance(newIdentifier);

            guardCBS.beginBuiltinConstraint(IBuiltInConstraint.EQ);
            guardCBS.beginArguments();
            guardCBS.addArgument(variable);
            guardCBS.addArgument(argument);
            guardCBS.endArguments();
            guardCBS.endBuiltinConstraint();

            return variable;
        }

        protected boolean isRemoved() {
            return removed;
        }

        public void setRemoved(boolean removed) {
            this.removed = removed;
        }

        @Override
        public void buildBuiltinInfix(String infix) throws BuilderException {
            throw UNSUPPORTED;
        }

        @Override
        public void buildInfix(String infix) throws BuilderException {
            buildUserDefinedInfix(infix);
        }

        @Override
        public void buildMarkedInfix(String infix) throws BuilderException {
            throw UNSUPPORTED;
        }

        @Override
        public void endInfixConstraint() throws BuilderException {
            try {
                final ConjunctBuildingBlock current = popInfixConstraint();
                switch (current.getType()) {
                case UD_INFIX:
                    endUserDefinedConstraint(current, true);
                    break;

                default:
                    throw new IllegalStateException(
                            "Current constraint isn't a user defined constraint");
                }
            } catch (IllegalStateException ise) {
                throw new BuilderException(ise);
            }
        }
    }

    protected static class ConjunctBuildingBlock extends ArgumentsDecorator {
        public static enum Type {
            BUILTIN, MARKED_BUILTIN, USER_DEFINED, METHOD_INVOCATION_CON, METHOD_INVOCATION_ARG(
                    false, false), CONSTRUCTOR_INVOCATION_ARG(false, false), COMPOSED_ID, SIMPLE_ID, INFIX(
                    true), UD_INFIX(true), BI_INFIX(true), MARKED_INFIX(true);

            private boolean constraint;

            private boolean infix;

            private Type() {
                this(false);
            }

            private Type(boolean infix) {
                this(infix, true);
            }

            private Type(boolean infix, boolean constraint) {
                setInfix(infix);
                setConstraint(constraint);
            }

            protected void setConstraint(boolean constraint) {
                this.constraint = constraint;
            }

            public boolean isConstraint() {
                return constraint;
            }

            public boolean isInfix() {
                return infix;
            }

            protected void setInfix(boolean infix) {
                this.infix = infix;
            }
        }

        private Type type;

        private String identifier;

        public ConjunctBuildingBlock(Type type) {
            setArguments(new Arguments());
            setType(type);
        }

        public ConjunctBuildingBlock(String id, Type type) {
            this(type);
            setIdentifier(id);
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public boolean hasIdentifier() {
            return getIdentifier() != null;
        }

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        public boolean isConstraint() {
            return getType().isConstraint();
        }
    }

    public String getChrPackage(){
    	return chrPkg;
    }
    
	public void beginEndPackage(String pkg) throws BuilderException {
		chrPkg = pkg;
	}
}