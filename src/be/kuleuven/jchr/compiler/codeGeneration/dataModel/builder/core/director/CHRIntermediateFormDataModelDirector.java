package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.director;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.CHRIntermediateFormDecorator;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.ICHRIntermediateForm;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.builder.IDataModelBuilder;

public abstract class CHRIntermediateFormDataModelDirector<B extends IDataModelBuilder<?>> 
extends CHRIntermediateFormDecorator 
implements IDataModelDirector<B> {
    
    private B builder;

    /**
     * @param intermediateForm
     */
    public CHRIntermediateFormDataModelDirector(ICHRIntermediateForm intermediateForm, B builder) {
        super(intermediateForm);
        setBuilder(builder);
    }
    
    public B getBuilder() {
        return builder;
    }
    protected void setBuilder(B builder) {
        this.builder = builder;
    }
}