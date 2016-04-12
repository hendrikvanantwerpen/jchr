package be.kuleuven.jchr.compiler.CHRIntermediateForm.builder;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.rules.RuleType;
import be.kuleuven.jchr.util.builder.BuilderException;
import be.kuleuven.jchr.util.builder.IBuilder;



/**
 * Mogelijke producten:
 * 		- CHRIntermediateForm
 * 		- normaal bestand
 * 		- XML bestand (uiteraard bestaat er een XSLT voor ;-) )
 * 		- (semantische) verificatie
 * 		- echo
 * Mogelijke directors:
 * 		- manueel (vanuit java-code)
 * 			OPM: hierbij mogelijk maken dat bijvoorbeeld
 * 				een regel van de parser wordt gebruikt om
 * 				enkel een regel parsen? Is mogelijk!!
 * 		- normaal bestand
 * 		- XML bestand
 * 
 * @author Peter Van Weert
 */
public interface ICHRIntermediateFormBuilder<Result> extends IBuilder<Result> {
	public void beginEndPackage( String pkg )  throws BuilderException;
	
    public void beginClassDeclarations() throws BuilderException;
    
    	public void importClass(String id) throws BuilderException;
        
        public void importPackage(String id) throws BuilderException;
    	
    public void endClassDeclarations() throws BuilderException;
    
    public void beginHandler(String name) throws BuilderException;
    
    	public void beginTypeParameters() throws BuilderException;
    	
    		public void beginTypeParameter(String name) throws BuilderException;
    			
    			public void beginUpperBound(String name) throws BuilderException;
    			
    			public void endUpperBound() throws BuilderException;
    		
    		public void endTypeParameter() throws BuilderException;
    	
    	public void endTypeParameters() throws BuilderException;
	    
	    public void beginSolverDeclarations() throws BuilderException;
	    
    	    public void beginSolverDeclaration(String solverInterface) throws BuilderException;
    	    	
    	    	public void beginTypeArguments() throws BuilderException; 
    	    
    	    		public void beginTypeArgument(String type) throws BuilderException;
    	    		
    	    		public void endTypeArgument() throws BuilderException;
    	    		
    	    	public void endTypeArguments() throws BuilderException;
    	    	
    	    	public void buildSolverName(String name) throws BuilderException;
    	    	
    	    public void endSolverDeclaration() throws BuilderException;
	    
	    public void endSolverDeclarations() throws BuilderException;
	    
	    public void beginConstraintDeclarations() throws BuilderException;
	    
	    	public void beginConstraintDeclaration(String id) throws BuilderException;
	    	
                public void buildInfixIdentifier(String infix) throws BuilderException;
            
	    		public void beginConstraintArgument() throws BuilderException;
                
                    public void beginConstraintArgumentType(String type, boolean fixed) throws BuilderException;

                        /* typeParameters */
                    
                    public void endConstraintArgumentType() throws BuilderException;
                    
	    			public void buildConstraintArgumentName(String name) throws BuilderException;
	    		
	    		public void endConstraintArgument() throws BuilderException;
	    	
	    	public void endConstraintDeclaration();
	    
	    public void endConstraintDeclarations() throws BuilderException;
	    
	    public void beginRules() throws BuilderException;
	    
	    	public void beginVariableDeclarations() throws BuilderException;	    	
	    		
                public void beginVariableType(String type, boolean fixed) throws BuilderException;
	    		    
                    /* typeParameters */
                
                public void endVariableType() throws BuilderException;
	    
	    		public void declareVariable(String id) throws BuilderException;
	    	
	    	public void endVariableDeclarations() throws BuilderException;
            
            public void beginRule() throws BuilderException;
            
    	    	public void beginRuleDefinition(RuleType type) throws BuilderException;
    	    	
    	    	public void beginRuleDefinition(String id, RuleType type) throws BuilderException ;
    	    		// begin van een strategie...
    	    		public void beginHead() throws BuilderException;
    	    			public void beginKeptOccurrences() throws BuilderException;
    	    			public void beginRemovedOccurrences() throws BuilderException;
    	    				
    	    					// etc etc
                        
                            public void buildOccurrenceId(String id) throws BuilderException;
        			
        				public void endKeptOccurrences() throws BuilderException;
        		    	public void endRemovedOccurrences() throws BuilderException;
    		    	public void endHead() throws BuilderException;
    
    	    		public void beginGuard() throws BuilderException;
    	    		public void beginBody() throws BuilderException;
                    
                        public void addFieldAccessConjunct(String id) throws BuilderException;
                        
                        public void addFailureConjunct() throws BuilderException;
    	    			
    	    			public void beginIdentifiedConjunct(String id) throws BuilderException;
    
                        public void beginComposedIdConjunct(String id) throws BuilderException;	    		
    		    		public void beginMarkedBuiltInConstraint(String id) throws BuilderException;
    		    		public void beginMethodInvocationConjunct(String id) throws BuilderException;
    
                        public void beginSimpleIdConjunct(String id) throws BuilderException;
                        public void beginBuiltInConstraint(String id) throws BuilderException;
                        public void beginUserDefinedConstraint(String id) throws BuilderException;
                        
    		    			public void beginArguments() throws BuilderException;
    		    			
    		    				public void addIdentifiedArgument(String id) throws BuilderException;
    		    				
    		    				public void addConstantArgument(boolean value) throws BuilderException;
    		    				public void addConstantArgument(byte value) throws BuilderException;
    		    				public void addConstantArgument(short value) throws BuilderException;
    		    				public void addConstantArgument(int value) throws BuilderException;
    		    				public void addConstantArgument(char value) throws BuilderException;
    		    				public void addConstantArgument(long value) throws BuilderException;
    		    				public void addConstantArgument(float value) throws BuilderException;
    		    				public void addConstantArgument(double value) throws BuilderException;
    		    				public void addConstantArgument(String value) throws BuilderException;
    		    				public void addConstantArgument(/* null */) throws BuilderException;
    		    				
                                public void addFieldAccessArgument(String id) throws BuilderException; 
                                
    		    				public void beginMethodInvocationArgument(String id) throws BuilderException;
                                public void beginConstructorInvocationArgument(String id) throws BuilderException;
    		    				
    		    					// beginArguments etc etc
    		    				
    		    				public void endMethodInvocationArgument() throws BuilderException;
                                public void endConstructorInvocationArgument() throws BuilderException;
    		    			
    		    			public void endArguments() throws BuilderException;
    		    		
    		    			public void endIdentifiedConjunct() throws BuilderException;
    		    			
    		    			public void endComposedIdConjunct() throws BuilderException;	    		
    			    		public void endMarkedBuiltInConstraint() throws BuilderException;
    			    		public void endMethodInvocationConjunct() throws BuilderException;
    			    		
    			    		public void endSimpleIdConjunct() throws BuilderException;
    			    		public void endBuiltInConstraint() throws BuilderException;
    			    		public void endUserDefinedConstraint() throws BuilderException;
                            
                            public void beginInfixConstraint() throws BuilderException;
                                
                                // add argument 1
                            
                                public void buildInfix(String infix) throws BuilderException;
                                public void buildMarkedInfix(String infix) throws BuilderException;
                                public void buildBuiltInInfix(String infix) throws BuilderException;
                                public void buildUserDefinedInfix(String infix) throws BuilderException;
                            
                                // add argument 2
                                
                            public void endInfixConstraint() throws BuilderException;
                            
    		    	public void endGuard() throws BuilderException;
    		    	public void endBody() throws BuilderException;
	    		
    		    public void endRuleDefinition() throws BuilderException;
                
                public void beginPragmas() throws BuilderException;
                
                    public void addPassivePragma(String id) throws BuilderException;
                
                public void endPragmas() throws BuilderException;
                    
		    public void endRule() throws BuilderException;
	    
	    public void endRules() throws BuilderException;
    
    public void endHandler() throws BuilderException;
}