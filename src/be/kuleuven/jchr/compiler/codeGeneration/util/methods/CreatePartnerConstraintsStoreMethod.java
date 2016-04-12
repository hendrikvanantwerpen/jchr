package be.kuleuven.jchr.compiler.codeGeneration.util.methods;

import be.kuleuven.jchr.compiler.codeGeneration.util.PartnerConstraintsStore;

/**
 * @author Peter Van Weert
 */
public class CreatePartnerConstraintsStoreMethod extends ObjectConstructor {
    private CreatePartnerConstraintsStoreMethod() {
        super(PartnerConstraintsStore.class);
    }
    private static CreatePartnerConstraintsStoreMethod instance;
    public static CreatePartnerConstraintsStoreMethod getInstance() {
        if (instance == null)
            instance = new CreatePartnerConstraintsStoreMethod();
        return instance;
    }
}