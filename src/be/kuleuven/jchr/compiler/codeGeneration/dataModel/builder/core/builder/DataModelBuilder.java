package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.builder;

import java.util.HashMap;
import java.util.Map;

import be.kuleuven.jchr.compiler.codeGeneration.dataModel.DataModel;
import be.kuleuven.jchr.util.builder.BasicBuilder;



public abstract class DataModelBuilder extends BasicBuilder<DataModel> implements IDataModelBuilder<DataModel> {
    
    protected void createRoot(int rootSize) {
        final Map<String, Object> root = 
            new HashMap<String, Object>(rootSize);
        setResult(new DataModel(root));
    }

}