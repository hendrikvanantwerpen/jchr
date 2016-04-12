package be.kuleuven.jchr.compiler.CHRIntermediateForm.members;

import java.util.Set;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.ArgumentType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.IImplicitArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumented.Argumented;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IGuardConjunct;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.schedule.ScheduleComponentType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.AmbiguityException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.MatchingInfo;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.GenericType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.PrimitiveType;


/**
 * @author Peter Van Weert
 */
public class FieldAccess extends Argumented<Field> 
implements IImplicitArgument, IGuardConjunct {
    
    public FieldAccess(Field field, IArguments arguments) {
        super(field, arguments);
    }
    
    public String getName() {
        return getArgumentableType().getName();
    }
    
    public ArgumentType getArgumentType() {
        return ArgumentType.FIELD_ACCESS;
    }

    public Set<Method> getMethods(String id) {
        return getType().getMethods(id);
    }
    public Field getField(String name) throws AmbiguityException, NoSuchFieldException {
        return getType().getField(name);
    }
    public IType getType() {
        return getArgumentableType().getType();
    }

    public MatchingInfo isAssignableTo(IType type) {
        return getType().isAssignableTo(type);
    }
    public boolean isDirectlyAssignableTo(IType type) {
        return getType().isDirectlyAssignableTo(type);
    }
    
    public boolean isFixed() {
        return getType().isFixed();
    }
    
    public boolean canBeAskConjunct() {
        final IType type = getType();
        return type == PrimitiveType.BOOLEAN_TYPE
            || type == GenericType.getNonParameterizableInstance(Boolean.class);
        /* we vertrouwen hier op auto-unboxing */
    }
    
    public ScheduleComponentType getScheduleComponentType() {
        return ScheduleComponentType.GUARD;
    }
}
