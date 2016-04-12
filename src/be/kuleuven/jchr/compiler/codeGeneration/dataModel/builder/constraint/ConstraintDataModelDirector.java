package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.constraint;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.ICHRIntermediateForm;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.UserDefinedConstraint;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.category.ILookupCategory;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.type.BinaryGuardedLookupType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.type.ILookupType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.type.IndexType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.type.BinaryGuardedLookupType.BinaryGuardInfo;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.argumented.ArgumentedDataModelExtenderFacade;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.constraint.lookupType.HashMapArgumentInfosBuilder;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.constraint.lookupType.NoneArgumentInfosBuilder;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.director.CHRIntermediateFormDataModelDirector;
import be.kuleuven.jchr.util.Arrays;
import be.kuleuven.jchr.util.builder.BuilderException;


/**
 * @author Peter Van Weert
 */
public abstract class ConstraintDataModelDirector<B extends IConstraintDataModelBuilder<?>>
    extends CHRIntermediateFormDataModelDirector<B> {
    
    private UserDefinedConstraint constraint;
    
    private ArgumentedDataModelExtenderFacade conjunctDataModelExtenderFacade;

    public ConstraintDataModelDirector(
        ICHRIntermediateForm intermediateForm, 
        B builder, 
        UserDefinedConstraint current
    ) {
        this(intermediateForm, builder, current, ArgumentedDataModelExtenderFacade.getDefaultInstance());        
    }

    
    public ConstraintDataModelDirector(
        ICHRIntermediateForm intermediateForm, 
        B builder, 
        UserDefinedConstraint current,
        ArgumentedDataModelExtenderFacade extenderFacade
    ) {
        super(intermediateForm, builder);
        setConstraint(current);
        setConjunctDataModelExtenderFacade(extenderFacade);
    }

    /**
     * @see be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.basic.IBasicDataModelDirector#construct()
     */
    public void construct() throws BuilderException {
        getBuilder().init();
        getBuilder().beginConstraint(
            getConstraint().getIdentifier(),
            getConstraint().getInfix()
        );
        constructConstraintVariables();
        constructLookupCategories();
        getBuilder().endConstraint();
        getBuilder().finish();
    }
    
    protected void constructConstraintVariables() throws BuilderException {
        final UserDefinedConstraint constraint = getConstraint();
        final int nbArgs = constraint.getArity();
        
        getBuilder().beginConstraintVariables(nbArgs);        
        for (int i = 0; i < nbArgs; i++) {
            getBuilder().addConstraintVariable(
                constraint.getVariableIdAt(i),
                Arrays.firstIndexOf(getVariableTypes().toArray(), constraint.getVariableTypeAt(i))                
            );
        }
        getBuilder().endConstraintVariables();        
    }
    
    protected void constructLookupCategories() throws BuilderException {
        final UserDefinedConstraint constraint = getConstraint();
        getBuilder().beginLookupCategories(constraint.getNbLookupCategories());
        for (ILookupCategory lookupCategory : constraint.getLookupCategories())
            constructLookupCategory(lookupCategory);
        getBuilder().endLookupCategories();
    }
    
    protected void constructLookupCategory(ILookupCategory lookupCategory) throws BuilderException {
        getBuilder().beginLookupCategory(
            lookupCategory.getIndexType().toString(),
            lookupCategory.isMasterCategory()
        );
        constructVariables(lookupCategory);
        constructLookupTypes(lookupCategory);
        getBuilder().endLookupCategory();
    }
    
    protected void constructVariables(ILookupCategory lookupCategory) throws BuilderException {
        getBuilder().beginVariables(lookupCategory.getNbVariables());
        for (int index : lookupCategory.getVariableIndices() )
            getBuilder().addVariable(index);
        getBuilder().endVariables();
    }
    
    protected void constructLookupTypes(ILookupCategory lookupCategory) throws BuilderException {
        final IndexType indexType = lookupCategory.getIndexType();
        getBuilder().beginLookupTypes(lookupCategory.getNbLookupTypes());
        switch (indexType) {
            case NONE:
                constructNoneLookupType();
            break;
            
            case HASH_MAP:                
                constructHashMapLookupTypes(lookupCategory);
            break;
            
            default:
                throw new BuilderException("Unsupported index-type (" + indexType + ")");
        }
        getBuilder().endLookupTypes();
    }
    
    protected void constructNoneLookupType() throws BuilderException {
        final NoneArgumentInfosBuilder builder 
            = getBuilder().getNoneArgumentInfosBuilder();
        
        getBuilder().beginLookupType();
        builder.beginGuardInfos(0);
        builder.endGuardInfos();
        getBuilder().endLookupType();
    }
    
    protected void constructHashMapLookupTypes(ILookupCategory lookupCategory) throws BuilderException {
        final HashMapArgumentInfosBuilder builder 
            = getBuilder().getHashMapArgumentInfosBuilder();
        
        for (ILookupType lookupType : lookupCategory)
            constructHashMapLookupType((BinaryGuardedLookupType)lookupType, builder);
    }
    
    protected void constructHashMapLookupType(BinaryGuardedLookupType lookupType, HashMapArgumentInfosBuilder builder) throws BuilderException {
        getBuilder().beginLookupType();
        builder.beginGuardInfos(lookupType.getNbGuards());

        for (BinaryGuardInfo guardInfo : lookupType.getGuards()) {
            builder.beginGuardInfo();
            builder.buildOtherType(guardInfo.getOtherTypeString());
            insertEqualityConjunct(guardInfo, builder);
            builder.endGuardInfo();
        }
        
        builder.endGuardInfos();
        getBuilder().endLookupType();
    }
    
    protected abstract void insertEqualityConjunct(BinaryGuardInfo guardInfo, HashMapArgumentInfosBuilder builder) throws BuilderException;

    protected UserDefinedConstraint getConstraint() {
        return constraint;
    }

    public void setConstraint(UserDefinedConstraint current) {
        this.constraint = current;
    }

    protected ArgumentedDataModelExtenderFacade getConjunctDataModelExtenderFacade() {
        return conjunctDataModelExtenderFacade;
    }
    protected void setConjunctDataModelExtenderFacade(
            ArgumentedDataModelExtenderFacade conjunctDataModelExtenderFacade) {
        this.conjunctDataModelExtenderFacade = conjunctDataModelExtenderFacade;
    }
}