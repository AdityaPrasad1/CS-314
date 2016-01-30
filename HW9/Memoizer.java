import java.util.HashMap;

public class Memoizer {
    private HashMap<Object, Object> hashmap;
    private Functor f;
    public Memoizer (Functor f) {
        this.hashmap = new HashMap<Object, Object>();       // creates new HashMap
        this.f = f;
    }
    public Object call(Object x) {                          // finds x in hashmap
        if(this.hashmap.containsKey(x)) {                   // if it's aready in the table, retrieve it
            return this.hashmap.get(x);
        }
        else {                                              // otherwise, add it to the hashmap
            Object val = f.fn(x);
            this.hashmap.put(x, val);
            return val;                                     // and return the value
        }
    }
}
