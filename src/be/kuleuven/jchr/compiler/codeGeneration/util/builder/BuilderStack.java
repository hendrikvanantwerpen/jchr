package be.kuleuven.jchr.compiler.codeGeneration.util.builder;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * @author Peter Van Weert
 */
public class BuilderStack extends Stack<BuilderStack.Node> {
    private static final long serialVersionUID = 1L;    

    public void push(List<Object> list) {        
        push(new Node(list));
    }
    public void push(Map<String, Object> map) {        
        push(new Node(map));
    }
    
    public final void beginList(final int size, final String key) 
    throws EmptyStackException, NullPointerException {
        final List<Object> list = new ArrayList<Object>(size);
        peek().put(key, list);
        push(list);
    }
    public final void beginMap(final int size, final String key) 
    throws EmptyStackException, NullPointerException {
        final Map<String, Object> map = new HashMap<String, Object>(size);
        peek().put(key, map);
        push(map);
    }
    
    public final void beginList(final int size) 
    throws EmptyStackException, NullPointerException {
        final List<Object> list = new ArrayList<Object>(size);
        peek().add(list);
        push(list);
    }
    public final void beginMap(final int size) 
    throws EmptyStackException, NullPointerException {
        final Map<String, Object> map = new HashMap<String, Object>(size);
        peek().add(map);
        push(map);
    }
    
    public static class Node {
        Map<String, Object> map;
        List<Object> list;
        
        public Node(Map<String, Object> map) {
            this.map = map;
        }
        
        public Node(List<Object> list) {
            this.list = list;
        }
        
        public final void add(Object object) throws NullPointerException {
            list.add(object);
        }
        
        public final void put(String key, Object value) throws NullPointerException {            
            map.put(key, value);
        }
        
        public boolean isList() {
            return (list != null);
        }
        
        public boolean isMap() {
            return (map != null);
        }
        
        public final int size() {
            return isList()? list.size() : map.size();
        }
        
        public Object getValue() {
            return isList()? getList() : getMap();
        }        
        
        public List<Object> getList() {
            return list;
        }
        public Map<String, Object> getMap() {
            return map;
        }
        
        @Override
        public String toString() {
            return isList()? list.toString() : map.toString();
        }
    }
}