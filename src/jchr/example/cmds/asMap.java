package jchr.example.cmds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;


import bsh.CallStack;
import bsh.Interpreter;
import bsh.NameSpace;
import bsh.This;
import bsh.UtilEvalError;

public class asMap implements Map {

	public NameSpace namespace;
	
	public static Map invoke( Interpreter env, CallStack callstack ) 
	{
		return new asMap( callstack.top() );
	}
	
	public static Map invoke( Interpreter env, CallStack callstack, NameSpace ns_ ) 
	{
		return new asMap( ns_ );
	}
	
	public static Map invoke( Interpreter env, CallStack callstack, This ths_ ) 
	{
		return new asMap( ths_ );
	}

	public static Map invoke( Interpreter env, CallStack callstack, Map map_ ) 
	{
		return map_;
	}

	private asMap() {
		// TODO Auto-generated constructor stub
	}

	private asMap( NameSpace ns_ ){
		namespace = ns_;
	}
	
	private asMap( This ths_ ){
		namespace = ths_.getNameSpace();
	}

	public void clear() {
		// TODO Auto-generated method stub
	}

	public boolean containsKey(Object arg0) {
		try {
			return namespace.getVariable( arg0.toString() ) != null;
		} catch (UtilEvalError e) {
		}
		return false;
	}

	public boolean containsValue(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public Set entrySet() {
		String names[] = namespace.getVariableNames();
		Set< entry > entrySet =  new LinkedHashSet<entry>();
		for( String name : names ){
			try {
				entrySet.add( new entry( name, namespace.getVariable(name)) );
			} catch (UtilEvalError e) {
			}
		}
		return entrySet;
	}

	public Object get(Object arg0) {
		try {
			Object o = namespace.getVariable((String) arg0);
			if( o instanceof This ){
				return new asMap( (This)o );
			}
			return o;
		} catch (UtilEvalError e) {
		}
		return false;
	}

	public boolean isEmpty() {
		return namespace.getVariableNames().length == 0;
	}

	public Set keySet() {
		String names[] = namespace.getVariableNames();
		HashSet<String> hashSet = new HashSet<String>( Arrays.asList(names) );
		return hashSet;
	}

	public Object put(Object arg0, Object arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public void putAll(Map arg0) {
		// TODO Auto-generated method stub
	}

	public Object remove(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public int size() {
		// TODO Auto-generated method stub
		return namespace.getVariableNames().length;
	}

	public Collection values() {
		String names[] = namespace.getVariableNames();
		ArrayList<Object> values =  new ArrayList<Object>();
		for( String name : names ){
			try {
				Object o = namespace.getVariable(name);
				if( o instanceof This ){
					o = new asMap( (This)o );
				}
				values.add( o );
			} catch (UtilEvalError e) {
			}
		}
		return values;
	}

}
