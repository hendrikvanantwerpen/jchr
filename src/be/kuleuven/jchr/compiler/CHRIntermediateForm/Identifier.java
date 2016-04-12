package be.kuleuven.jchr.compiler.CHRIntermediateForm;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.IllegalIdentifierException;


/**
 * @author Peter Van Weert
 */
public abstract class Identifier {
    private final static Set<String> RESERVED_WORDS;
    static {
        RESERVED_WORDS = new HashSet<String>();
        for (String s : new String[] {
            "abstract" ,  "default" ,  "if"         ,  "private"   ,  "this",
            "boolean"  ,  "do"      ,  "implements" ,  "protected" ,  "throw",
            "break"    ,  "double"  ,  "import"     ,  "public"    ,  "throws",
            "byte"     ,  "else"    ,  "instanceof" ,  "return"    ,  "transient",
            "case"     ,  "extends" ,  "int"        ,  "short"     ,  "try",
            "catch"    ,  "final"   ,  "interface"  ,  "static"    ,  "void",
            "char"     ,  "finally" ,  "long"       ,  "strictfp"  ,  "volatile",
            "class"    ,  "float"   ,  "native"     ,  "super"     ,  "while",
            "const"    ,  "for"     ,  "new"        ,  "switch"    ,
            "continue" ,  "goto"    ,  "package"    ,  "synchronized",
            
            "true"     ,  "false"   , "null",
            
            "handler", "constraint", "constraints", "solver", 
            "rules", "variable"
        }) RESERVED_WORDS.add(s);
    }

    public static boolean isValidIdentifier(String id) {
        if (id == null) return false;
        
        StringTokenizer st = new StringTokenizer(id, ".");
        
        while (st.hasMoreTokens()) {
            if (! isValidSimpleIdentifier(st.nextToken()))
                return false;
        }
        
        return true;
    }
    
    public static boolean isValidSimpleIdentifier(String id) {
        if (id == null) return false;
        
        if (RESERVED_WORDS.contains(id)) return false;
        
        if (id.length() == 0 || !Character.isJavaIdentifierStart(id.charAt(0)))
            return false;
        for (int i = 1; i < id.length(); i++) {
            if (!Character.isJavaIdentifierPart(id.charAt(i)))
                return false;
        }
        
        return true;
    }
    
    public static boolean isValidComposedIdentifier(String id) {
        return isValidIdentifier(id) && !isSimple(id);
    }
    
    public static boolean isValidUdSimpleIdentifier(String id) {
        return isValidSimpleIdentifier(id) 
            && (id.charAt(0) != '$') // used for generated identifiers
            && (id.charAt(0) != '_') // used for anonymous variables
        ;
    }
    
    public static void testIdentifier(String id) throws IllegalIdentifierException {
        if (! isValidIdentifier(id)) 
            throw new IllegalIdentifierException(id);
    }
    
    public static void testSimpleIdentifier(String id) throws IllegalIdentifierException {
        if (! isValidSimpleIdentifier(id)) 
            throw new IllegalIdentifierException(id);
    }
    
    public static void testComposedIdentifier(String id) throws IllegalIdentifierException {
        if (! isValidComposedIdentifier(id)) 
            throw new IllegalIdentifierException(id);
    }
    
    public static void testUdSimpleIdentifier(String id, boolean upperCaseFirst) throws IllegalIdentifierException {
        if (!isValidUdSimpleIdentifier(id) 
            || (upperCaseFirst(id) != upperCaseFirst))            
                throw new IllegalIdentifierException(id);
    }
    
    public static void testUdSimpleIdentifier(String id) throws IllegalIdentifierException {
        testUdSimpleIdentifier(id, false);
    }
    
    public static int getNbTokens(final String value) {        
        final int length = value.length();
        final char[] chars = new char[length];
        value.getChars(0, length, chars, 0);
        int result = 1;
        for (int i = 0; i < length; i++)
            if (chars[i] == '.')
                result++;
        return result;
    }
    
    public static boolean upperCaseFirst(String s) {
        final char first = s.charAt(0);
        return Character.isUpperCase(first) || first == '_';
    }
    
    public static String getTail(String s) throws IllegalArgumentException {
        try {
            final int index = s.lastIndexOf('.') + 1;
            
            if (index == 0)
                return s;
            else
                return s.substring(index);
        }
        catch (NullPointerException npe) {
            throw new IllegalArgumentException(s);
        }
        catch (IndexOutOfBoundsException iobe) {
            throw new IllegalArgumentException(s);
        }
    }
    
    public static String getEntireTail(String s) throws IllegalArgumentException {
        try {
            final int index = s.indexOf('.') + 1;
            
            if (index == 0)
                return s;
            else
                return s.substring(index, s.length());
        }
        catch (NullPointerException npe) {
            throw new IllegalArgumentException(s);
        }
        catch (IndexOutOfBoundsException iobe) {
            throw new IllegalArgumentException(s);
        }
    }
    
    public static String getHead(String s) throws IllegalArgumentException {
        try {
            final int index = s.indexOf('.');
            
            if (index == -1)
                return s;
            else
                return s.substring(0, index);
        }
        catch (NullPointerException npe) {
            throw new IllegalArgumentException(s);
        }
        catch (IndexOutOfBoundsException iobe) {
            throw new IllegalArgumentException(s);
        }
    }
    
    public static String getBody(String s) throws IllegalArgumentException {
        try {
            final int index = s.lastIndexOf('.');
            
            if (index == -1)
                return s;
            else
                return s.substring(0, index);
        }
        catch (NullPointerException npe) {
            throw new IllegalArgumentException(s);
        }
        catch (IndexOutOfBoundsException iobe) {
            throw new IllegalArgumentException(s);
        }
    }
    
    public static boolean isComposed(final String value) {
        return !isSimple(value);
    }
    
    public static boolean isSimple(final String value) {
        return value.indexOf('.') < 0;
    }
    
    public static String flatten(String value) {
        return value.replace('.', '_');
    }    
}