/**
 * this class Cons implements a Lisp-like Cons cell
 *
 * @author  Gordon S. Novak Jr.
 * @version 29 Nov 01; 25 Aug 08; 05 Sep 08; 08 Sep 08; 12 Sep 08; 24 Sep 08
 *          06 Oct 08; 07 Oct 08; 09 Oct 08; 23 Oct 08; 27 Mar 09; 06 Aug 10
 *          30 Dec 13
 */

public class Cons
{
    // instance variables
    private Object car;
    private Cons cdr;
    private Cons(Object first, Cons rest)
    { car = first;
        cdr = rest; }
    public static Cons cons(Object first, Cons rest)
    { return new Cons(first, rest); }
    public static boolean consp (Object x)
    { return ( (x != null) && (x instanceof Cons) ); }
    // safe car, returns null if lst is null
    public static Object first(Cons lst) {
        return ( (lst == null) ? null : lst.car  ); }
    // safe cdr, returns null if lst is null
    public static Cons rest(Cons lst) {
        return ( (lst == null) ? null : lst.cdr  ); }
    public static Object second (Cons x) { return first(rest(x)); }
    public static Object third (Cons x) { return first(rest(rest(x))); }
    public static void setfirst (Cons x, Object i) { x.car = i; }
    public static void setrest  (Cons x, Cons y) { x.cdr = y; }
    public static Cons list(Object ... elements) {
        Cons list = null;
        for (int i = elements.length-1; i >= 0; i--) {
            list = cons(elements[i], list);
        }
        return list;
    }
    // access functions for expression representation
    public static Object op  (Cons x) { return first(x); }
    public static Object lhs (Cons x) { return first(rest(x)); }
    public static Object rhs (Cons x) { return first(rest(rest(x))); }
    public static boolean numberp (Object x)
    { return ( (x != null) &&
            (x instanceof Integer || x instanceof Double) ); }
    public static boolean integerp (Object x)
    { return ( (x != null) && (x instanceof Integer ) ); }
    public static boolean floatp (Object x)
    { return ( (x != null) && (x instanceof Double ) ); }
    public static boolean stringp (Object x)
    { return ( (x != null) && (x instanceof String ) ); }

    // convert a list to a string for printing
    public String toString() {
        return ( "(" + toStringb(this) ); }
    public static String toString(Cons lst) {
        return ( "(" + toStringb(lst) ); }
    private static String toStringb(Cons lst) {
        return ( (lst == null) ?  ")"
                : ( first(lst) == null ? "()" : first(lst).toString() )
                + ((rest(lst) == null) ? ")"
                : " " + toStringb(rest(lst)) ) ); }

    public boolean equals(Object other) { return equal(this,other); }

    // tree equality
    public static boolean equal(Object tree, Object other) {
        if ( tree == other ) return true;
        if ( consp(tree) )
            return ( consp(other) &&
                    equal(first((Cons) tree), first((Cons) other)) &&
                    equal(rest((Cons) tree), rest((Cons) other)) );
        return eql(tree, other); }

    // simple equality test
    public static boolean eql(Object tree, Object other) {
        return ( (tree == other) ||
                ( (tree != null) && (other != null) &&
                        tree.equals(other) ) ); }

    // member returns null if requested item not found
    public static Cons member (Object item, Cons lst) {
        if ( lst == null )
            return null;
        else if ( item.equals(first(lst)) )
            return lst;
        else return member(item, rest(lst)); }

    public static Cons union (Cons x, Cons y) {
        if ( x == null ) return y;
        if ( member(first(x), y) != null )
            return union(rest(x), y);
        else return cons(first(x), union(rest(x), y)); }

    public static boolean subsetp (Cons x, Cons y) {
        return ( (x == null) ? true
                : ( ( member(first(x), y) != null ) &&
                subsetp(rest(x), y) ) ); }

    public static boolean setEqual (Cons x, Cons y) {
        return ( subsetp(x, y) && subsetp(y, x) ); }

    // combine two lists: (append '(a b) '(c d e))  =  (a b c d e)
    public static Cons append (Cons x, Cons y) {
        if (x == null)
            return y;
        else return cons(first(x),
                append(rest(x), y)); }

    // look up key in an association list
    // (assoc 'two '((one 1) (two 2) (three 3)))  =  (two 2)
    public static Cons assoc(Object key, Cons lst) {
        if ( lst == null )
            return null;
        else if ( key.equals(first((Cons) first(lst))) )
            return ((Cons) first(lst));
        else return assoc(key, rest(lst)); }

    public static int square(int x) { return x*x; }
    public static int pow (int x, int n) {
        if ( n <= 0 ) return 1;
        if ( (n & 1) == 0 )
            return square( pow(x, n / 2) );
        else return x * pow(x, n - 1); }

    public static Cons formulas =
            list( list( "=", "s", list("*", new Double(0.5),
                            list("*", "a",
                                    list("expt", "t", new Integer(2))))),
                    list( "=", "s", list("+", "s0", list( "*", "v", "t"))),
                    list( "=", "a", list("/", "f", "m")),
                    list( "=", "v", list("*", "a", "t")),
                    list( "=", "f", list("/", list("*", "m", "v"), "t")),
                    list( "=", "f", list("/", list("*", "m",
                                    list("expt", "v", new Integer(2))),
                            "r")),
                    list( "=", "h", list("-", "h0", list("*", new Double(4.94),
                            list("expt", "t",
                                    new Integer(2))))),
                    list( "=", "c", list("sqrt", list("+",
                            list("expt", "a",
                                    new Integer(2)),
                            list("expt", "b",
                                    new Integer(2))))),
                    list( "=", "v", list("*", "v0",
                            list("-", new Double(1.0),
                                    list("exp", list("/", list("-", "t"),
                                            list("*", "r", "c"))))))
            );

    // Note: this list will handle most, but not all, cases.
    // The binary operators - and / have special cases.
    public static Cons opposites =
            list( list( "+", "-"), list( "-", "+"), list( "*", "/"),
                    list( "/", "*"), list( "sqrt", "expt"), list( "expt", "sqrt"),
                    list( "log", "exp"), list( "exp", "log") );

    public static void printanswer(String str, Object answer) {
        System.out.println(str +
                ((answer == null) ? "null" : answer.toString())); }

    public static Cons nreverse (Cons lst) {
        Cons last = null; Cons next;
        while (lst != null)
        { next =  rest(lst);
            setrest(lst, last);
            last = lst;
            lst = next; };
        return last; }

    // ****** your code starts here ******


    public static Cons findpath(Object item, Object cave) {
        if(item == null || cave == null) {              // if either item or cave is empty, return null
            return null;
        }
        return nreverse(findpathb(item, cave, null));   // otherwise, return the reverse of the path provided by aux method
    }

    // Auxiliary method for findpath
    public static Cons findpathb(Object item, Object cave, Cons path){      // adds additional "path" parameter to keep track of it
        if(consp(cave) == true){        // if the first element is an internal node, check its leaves and add "first" to path
            Cons endpath = findpathb(item, first((Cons) cave), cons("first", path));
            if(endpath == null){        // if the current path is null, that means there's no match, proceed with the rest of the tree and add "rest" to path
                endpath = findpathb(item, rest((Cons) cave), cons("rest", path));
            }
            return endpath;
        }
        else if(item.equals(cave)){     // if the leaf matches the item, add "done" to path
            return cons("done", path);
        }
        return null;        // otherwise, return null
    }

    public static Object follow(Cons path, Object cave) {
        if(path == null || cave == null){       // if either path or cave is empty, return null
            return null;
        }
        while(path != null){                    // while there are still steps in path
            Object nextstep = first(path);
            if("first".equals(nextstep)){       // check if the next step is first, if so use first
                cave = first((Cons) cave);
            }
            else if("rest".equals(nextstep)){   // check if the next step is rest, if so use rest
                cave = rest((Cons) cave);
            }
            path = rest(path);                  // then continue onto the next step in path
        }
        return cave;                            // return item found by following path
    }

    public static Object corresp(Object item, Object tree1, Object tree2) {
        return follow(findpath(item, tree1), tree2);        // goes through the tree2 following path from findpath of tree1
    }

    public static Cons solve(Cons e, String v) {
        if(e == null || v == null) {                        // if either the equation of the variables are null
            return null;                                    // return null
        }
        Cons end = solveb(e, v);                            // create an end equation with the isolated variable using solveb
        if(end == null) {                                   // if end equation is null
            end = solveb(list("=", rhs(e), lhs(e)), v);     // set it to result of solveb solving (= lhs rhs) for v
        }
        return end;
    }

    // Auxiliary method or solve
    public static Cons solveb(Cons e, String v) {
        if(v.equals(lhs(e))) {              // if it's solving for the variable that the equation equals, return equation
            return e;
        }
        else if(v.equals(rhs(e))) {         // if it's solving for the variable that's on the rhs, return the equation with lhs and rhs flipped
            return list("=", v, lhs(e));
        }
        else if(consp(rhs(e)) == true) {    // othwerwise, check if the rhs of the equation is a tree
            Cons rhs = (Cons) rhs(e);       // create a cons that will hold the rhs of the equation
            Object inverse = first(rest(assoc(op(rhs), opposites)));    // additionally, create an object that will act as the inverse operation of the ones found
            if(rhs(rhs) == null) {          // if the original operator is unary
                if("+".equals(inverse)) {   // check if it's a negative number, if so divide both sides by -1
                    return solveb(list("=", list("-", lhs(e)), lhs(rhs)), v);
                }
                else if("expt".equals(inverse)) {   // check if it's squareroot, if so square both sides
                    return solveb(list("=", list(inverse, lhs(e), 2.0), lhs(rhs)), v);
                }
                else {                      // if it's any other unary operation, do the inverse to both sides
                    return solveb(list("=", list(inverse, lhs(e)), lhs(rhs)), v);
                }
            }
            else {                              // if binary
                if("sqrt".equals(inverse)) {    // if the original operator is squaring (binary)
                    return solveb(list("=", list(inverse, lhs(e)), lhs(rhs)), v);     // squareroot both sides
                }
                else {                          // if it's any other binary operator, do the inverse operation with rhs of the rhs onto the lhs
                    Cons end = solveb(list("=", list(inverse, lhs(e), rhs(rhs)), lhs(rhs)), v);
                    if(end == null) {           // if there's nothing in end yet, do the first inverse operation
                        if("*".equals(inverse)) {       // if it's division, multiply
                            end = solveb(list("=", list("/", lhs(rhs), lhs(e)), rhs(rhs)), v);
                        }
                        else if("+".equals(inverse)) {  // if it's subtraction, add
                            end = solveb(list("=", list("-", lhs(rhs), lhs(e)), rhs(rhs)), v);
                        }
                        else {                          // if it's any other, do the inverse
                            end = solveb(list("=", list(inverse, lhs(e), lhs(rhs)), rhs(rhs)), v);
                        }
                    }
                    return end;                         // return the isolated variable
                }
            }
        }
        return null;                                    // otherwise return null
    }

    public static Double solveit (Cons equations, String var, Cons values) {
        Cons eq = null;
        while(equations != null && eq == null) {            // while there are still equations and eq is empty
            Cons vars = vars((Cons) first(equations));      // create new list with all the variables in the first equation
            if(member(var, vars) != null) {                 // if the variable is a member of the equation
                Cons vals = values;                         // create a holder for the values
                while(vals != null){                        // while there are still values
                    if(member(first((Cons) first(vals)), vars) == null) {
                        break;                              // if the value isn't part of the vars, break the loop
                    }
                    vals = rest(vals);                      // otherwise continue with the rest of the values
                }
                if(vals == null) {                          // once there are no more values
                    eq = (Cons) first(equations);           // set eq to the first equation
                }
            }
            equations = rest(equations);                    // continue with the rest of the equations
        }
        return eval(rhs((Cons) solve(eq, var)), values);    // evaluate the specific equation with the values
    }


    // Include your functions vars and eval from the previous assignment.
// Modify eval as described in the assignment.
    public static Cons vars (Object expr) {
        Cons vars = null;                                                   // creates an empty list that will hold the variables
        if(consp(expr) == true) {                                           // checks if the expression is a list
            vars = union(vars(lhs((Cons) expr)), vars(rhs((Cons) expr)));   // if so, check the lhs/rhs for variables recursively
        }                                                                   // union is used to makes sure there are no duplicates
        else if(stringp(expr)) {                                            // once a leaf is reached, check if it's a variable by checking if it's a String
            vars = cons(expr, vars);                                        // if it is, add it to the list of variables
        }
        return vars;
    }

    public static Double eval (Object tree, Cons bindings) {
        Double val = 0.0;                                       // introduces a starting value of 0
        if(consp(tree) == true) {                               // checks if the next element is a list
            String operator = (String) op((Cons) tree);         // String that stores operator
            Double lhs = eval(lhs((Cons) tree), bindings);      // Integer that stores evaluation of the left hand side
            Double rhs = eval(rhs((Cons) tree), bindings);      // Integer that stores evaluation of the right hand side
            if("+".equals(operator)) {                          // if operator is addition, add lhs and rhs
                val = lhs + rhs;
            }
            else if("-".equals(operator)) {                     // if operator is subtraction
                if(rest((Cons) rhs((Cons) tree)) == null) {     // check it's a unary operation
                    val = lhs * -1;                             // if so, make the left hand side a negative value
                }
                else {                                          // otherwise subtract the right hand side from the left
                    val = lhs - rhs;
                }
            }
            else if("*".equals(operator)) {                     // if operator is multiplication, multiply lhs and rhs
                val = lhs * rhs;
            }
            else if("/".equals(operator)) {                     // if operator is division, divide lhs by rhs
                val = lhs / rhs;
            }
            else if("expt".equals(operator)) {                  // if operator is exponent, raise lhs to rhs
                val = Math.pow(lhs, rhs);
            }
            else if("sqrt".equals(operator)) {                  // if operator is squareroot, squareroot lhs
                val = Math.sqrt(lhs);
            }
            else if("exp".equals(operator)) {                   // if operator is exponentiation, raise e to power of lhs
                val = Math.exp(lhs);
            }
            else if("log".equals(operator)) {                   // if operator is natural log, take natural log of lhs
                val = Math.log(lhs);
            }
        }
        else if(tree != null) {                                 // if it's a leaf check
            if(assoc(tree, bindings) == null) {                 // if it's a Double, then set val to its value
                val = Double.parseDouble(tree.toString());      // has to use parseDouble otherwise exception occurs, thinks Double is being applied to Integer
            }
            else {                                              // if it's a variable, check the association list
                val = (Double) first(rest(assoc(tree, bindings)));
            }
        }
        return val;
    }


    // ****** your code ends here ******

    public static void main( String[] args ) {

        Cons cave = list("rocks", "gold", list("monster"));
        Cons path = findpath("gold", cave);
        printanswer("cave = " , cave);
        printanswer("path = " , path);
        printanswer("follow = " , follow(path, cave));

        Cons caveb = list(list(list("green", "eggs", "and"),
                        list(list("ham"))),
                "rocks",
                list("monster",
                        list(list(list("gold", list("monster"))))));
        Cons pathb = findpath("gold", caveb);
        printanswer("caveb = " , caveb);
        printanswer("pathb = " , pathb);
        printanswer("follow = " , follow(pathb, caveb));

        Cons treea = list(list("my", "eyes"),
                list("have", "seen", list("the", "light")));
        Cons treeb = list(list("my", "ears"),
                list("have", "heard", list("the", "music")));
        printanswer("treea = " , treea);
        printanswer("treeb = " , treeb);
        printanswer("corresp = " , corresp("light", treea, treeb));

        System.out.println("formulas = ");
        Cons frm = formulas;
        Cons vset = null;
        while ( frm != null ) {
            printanswer("   "  , ((Cons)first(frm)));
            vset = vars((Cons)first(frm));
            while ( vset != null ) {
                printanswer("       "  ,
                        solve((Cons)first(frm), (String)first(vset)) );
                vset = rest(vset); }
            frm = rest(frm); }

        Cons bindings = list( list("a", (Double) 32.0),
                list("t", (Double) 4.0));
        printanswer("Eval:      " , rhs((Cons)first(formulas)));
        printanswer("  bindings " , bindings);
        printanswer("  result = " , eval(rhs((Cons)first(formulas)), bindings));

        printanswer("Tower: " , solveit(formulas, "h0",
                list(list("h", new Double(0.0)),
                        list("t", new Double(4.0)))));

        printanswer("Car: " , solveit(formulas, "a",
                list(list("v", new Double(88.0)),
                        list("t", new Double(8.0)))));

        printanswer("Capacitor: " , solveit(formulas, "c",
                list(list("v", new Double(3.0)),
                        list("v0", new Double(6.0)),
                        list("r", new Double(10000.0)),
                        list("t", new Double(5.0)))));

        printanswer("Ladder: " , solveit(formulas, "b",
                list(list("a", new Double(6.0)),
                        list("c", new Double(10.0)))));

    }

}
