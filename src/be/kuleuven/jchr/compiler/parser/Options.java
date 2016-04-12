package be.kuleuven.jchr.compiler.parser;

import org.kohsuke.args4j.IllegalOptionParameterException;
import org.kohsuke.args4j.UndefinedOptionException;
import org.kohsuke.args4j.opts.BooleanOption;
import org.kohsuke.args4j.opts.IntOption;
import org.kohsuke.args4j.opts.StringOption;

public class Options {
    public BooleanOption hashMap = new BooleanOption("-hashMap", true);
    
    public void setOption(String optionName, boolean value) 
    throws UndefinedOptionException, IllegalOptionParameterException {
        try {
            ((BooleanOption)getOption(optionName)).value = value;               
        } catch (ClassCastException cce) {
            throw new IllegalOptionParameterException(optionName, value + "(a boolean)");
        }
    }
    
    public void setOption(String optionName, String value) 
    throws UndefinedOptionException, IllegalOptionParameterException {
        try {
            ((StringOption)getOption(optionName)).value = value;
        } catch (ClassCastException cce) {
            throw new IllegalOptionParameterException(optionName, value + "(a String)");
        }
    }
    
    public void setOption(String optionName, int value) 
    throws UndefinedOptionException, IllegalOptionParameterException {
        try {
            ((IntOption)getOption(optionName)).value = value;
        } catch (ClassCastException cce) {
            throw new IllegalOptionParameterException(optionName, value + "(an integer)");
        }
    }
    
    protected Object getOption(String optionName) throws UndefinedOptionException {
        try {                
            return getClass().getField(optionName).get(this);
        } catch (IllegalAccessException e) {
            throw new InternalError();
        } catch (NoSuchFieldException e) {
            throw new UndefinedOptionException(optionName);
        }            
    }
}