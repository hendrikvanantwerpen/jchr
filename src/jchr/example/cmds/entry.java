package jchr.example.cmds;

import java.util.Map;

public class entry implements Map.Entry<String, Object> {
	public String _key;
	public Object _value;
	public entry( String key_, Object value_ ){
		_key = key_;
		_value = value_;
	}
	public String getKey() { return _key; }

	public Object getValue() { return _value; }

	public Object setValue(Object value_) { 
		Object value  = _value;
		_value = value_;
		return value; 
	}

	public String toString(){
		return "{" + _key + "," + _value + "}";
	}
}
