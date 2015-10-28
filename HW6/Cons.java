/**
 * this class Cons implements a Lisp-like Cons cell
 *
 * @author  Gordon S. Novak Jr.
 * @version 29 Nov 01; 25 Aug 08; 05 Sep 08; 08 Sep 08; 12 Sep 08; 24 Sep 08
 *          06 Oct 08; 07 Oct 08; 09 Oct 08; 27 Mar 09; 18 Mar 11; 09 Oct 13
 *          30 Dec 13
 */

import java.util.StringTokenizer;

interface Functor { Object fn(Object x); }

interface Predicate { boolean pred(Object x); }

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
    // test whether argument is a Cons
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
    public static int pow (int x, int n) {        // x to the power n
        if ( n <= 0 ) return 1;
        if ( (n & 1) == 0 )
            return square( pow(x, n / 2) );
        else return x * pow(x, n - 1); }

    public static Object reader(String str) {
        return readerb(new StringTokenizer(str, " \t\n\r\f()'", true)); }

    public static Object readerb( StringTokenizer st ) {
        if ( st.hasMoreTokens() ) {
            String nexttok = st.nextToken();
            if ( nexttok.charAt(0) == ' ' ||
                    nexttok.charAt(0) == '\t' ||
                    nexttok.charAt(0) == '\n' ||
                    nexttok.charAt(0) == '\r' ||
                    nexttok.charAt(0) == '\f' )
                return readerb(st);
            if ( nexttok.charAt(0) == '(' )
                return readerlist(st);
            if ( nexttok.charAt(0) == '\'' )
                return list("QUOTE", readerb(st));
            return readtoken(nexttok); }
        return null; }

    public static Object readtoken( String tok ) {
        if ( (tok.charAt(0) >= '0' && tok.charAt(0) <= '9') ||
                ((tok.length() > 1) &&
                        (tok.charAt(0) == '+' || tok.charAt(0) == '-' ||
                                tok.charAt(0) == '.') &&
                        (tok.charAt(1) >= '0' && tok.charAt(1) <= '9') ) ||
                ((tok.length() > 2) &&
                        (tok.charAt(0) == '+' || tok.charAt(0) == '-') &&
                        (tok.charAt(1) == '.') &&
                        (tok.charAt(2) >= '0' && tok.charAt(2) <= '9') )  ) {
            boolean dot = false;
            for ( int i = 0; i < tok.length(); i++ )
                if ( tok.charAt(i) == '.' ) dot = true;
            if ( dot )
                return Double.parseDouble(tok);
            else return Integer.parseInt(tok); }
        return tok; }

    public static Cons readerlist( StringTokenizer st ) {
        if ( st.hasMoreTokens() ) {
            String nexttok = st.nextToken();
            if ( nexttok.charAt(0) == ' ' ||
                    nexttok.charAt(0) == '\t' ||
                    nexttok.charAt(0) == '\n' ||
                    nexttok.charAt(0) == '\r' ||
                    nexttok.charAt(0) == '\f' )
                return readerlist(st);
            if ( nexttok.charAt(0) == ')' )
                return null;
            if ( nexttok.charAt(0) == '(' ) {
                Cons temp = readerlist(st);
                return cons(temp, readerlist(st)); }
            if ( nexttok.charAt(0) == '\'' ) {
                Cons temp = list("QUOTE", readerb(st));
                return cons(temp, readerlist(st)); }
            return cons( readtoken(nexttok),
                    readerlist(st) ); }
        return null; }

    // read a list of strings, producing a list of results.
    public static Cons readlist( Cons lst ) {
        if ( lst == null )
            return null;
        return cons( reader( (String) first(lst) ),
                readlist( rest(lst) ) ); }

    // You can use these association lists if you wish.
    public static Cons engwords = list(list("+", "sum"),
            list("-", "difference"),
            list("*", "product"),
            list("/", "quotient"),
            list("expt", "power"));

    public static Cons opprec = list(list("=", 1),
            list("+", 5),
            list("-", 5),
            list("*", 6),
            list("/", 6) );

    public static Cons nreverse (Cons lst) {
        Cons last = null; Cons next;
        while (lst != null)
        { next =  rest(lst);
            setrest(lst, last);
            last = lst;
            lst = next; };
        return last; }

// ****** your code starts here ******

    public static Integer maxbt (Object tree) {
        Integer max = Integer.MIN_VALUE;                // sets the initial max as the lowest possible integer value
        if(consp(tree) == true) {                       // checks to see if the next element is another tree
            if(maxbt(first((Cons) tree)) > max) {       // if so, check if the first leaf in that tree is an int that's greater than max
                max = maxbt(first((Cons) tree));        // if it is, set max to that integer
            }
            if(maxbt(rest((Cons) tree)) > max) {        // continues the same process with the rest of the tree
                max = maxbt(rest((Cons) tree));
            }
        }
        else if(tree != null && (Integer) tree > max) { // if the next element isn't null and is an integer that's greater than max
            max = (Integer) tree;                       // set the max as that value
        }
        return max;
    }

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

    public static boolean occurs(Object value, Object tree) {
        boolean occurs = false;             // creates an initial boolean value
        if(consp(tree) == true) {           // if the element is a list, check the lhs and rhs for an occurence of "value"
            occurs = occurs(value, lhs((Cons) tree)) || occurs(value, rhs((Cons) tree));    // if either side has an occurence
        }                                                                                   // then the boolean value is true
        else if(value != null){             // if there is a value
            occurs = value.equals(tree);    // check to see if that value is an occurence of the specified value
        }                                   // if not, return false. Otherwise, return true
        return occurs;
    }

    public static Integer eval (Object tree) {
        Integer val = 0;                                        // introduces a starting value of 0
        if(consp(tree) == true) {                               // checks if the next element is a list
            String operator = (String) op((Cons) tree);         // String that stores operator
            Integer lhs = eval(lhs((Cons) tree));               // Integer that stores evaluation of the left hand side
            Integer rhs = eval(rhs((Cons) tree));               // Integer that stores evaluation of the right hand side
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
                val = pow(lhs, rhs);
            }
        }
        else if(tree != null) {                                 // if it's a leaf, then set val to the leaf's value
            val = (Integer) tree;
        }
        return val;
    }

    public static Integer eval (Object tree, Cons bindings) {
        Integer val = 0;                                        // introduces a starting value of 0
        if(consp(tree) == true) {                               // checks if the next element is a list
            String operator = (String) op((Cons) tree);         // String that stores operator
            Integer lhs = eval(lhs((Cons) tree), bindings);     // Integer that stores evaluation of the left hand side
            Integer rhs = eval(rhs((Cons) tree), bindings);     // Integer that stores evaluation of the right hand side
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
                val = pow(lhs, rhs);
            }
        }
        else if(tree != null) {                                 // if it's a leaf check
            if(assoc(tree, bindings) == null) {                 // if it's an int, then set val to its value
                val = (Integer) tree;
            }
            else {                                              // if it's a variable, check the association list
                val = (Integer) first(rest(assoc(tree, bindings)));
            }
        }
        return val;
    }

    public static Cons english (Object tree) {
        Cons english = null;                                    // introduces empty list
        if(consp(tree) == true) {
            String operator = (String) op((Cons) tree);         // String that stores operator
            Cons lhs = english(lhs((Cons) tree));               // list that stores the english of the lhs
            Cons rhs = english(rhs((Cons) tree));               // list that stores the english of the rhs
            english = cons("the", english);                     // start the sentence with "the
            if("+".equals(operator)) {                          // if addition, use the word "sum"
                english = cons("sum", english);
            }
            else if("-".equals(operator)) {                     // if subtraction, check whether it's unary or binary
                if(first(rhs) == null) {                        // if unary, say "negative" value
                    english = cons("negative", null);
                }
                else {                                          // if binary, say "difference"
                    english = cons("difference", english);
                }
            }
            else if("*".equals(operator)) {                     // if multiplication, say "product"
                english = cons("product", english);
            }
            else if("/".equals(operator)) {                     // if division, say "quotient"
                english = cons("quotient", english);
            }
            else if("expt".equals(operator)) {                  // if exponent, say "the value of __ to the power of __"
                english = cons("the value", english);
            }
            if(first(rhs) != null && !"-".equals(operator)) {   // if it's a binary operation, add "of" between operator and numbers
                english = cons("of", english);
            }
            while(lhs != null) {                                // while there is still more in the left hand side
                english = cons(first(lhs), english);            // continue down the tree
                lhs = rest(lhs);                                // proceed with the rest of the leaves
            }
            if(first(rhs) != null) {                            // if there is still more in the right hand side (binary)
                if("expt".equals(operator)) {
                    english = cons("to the power of", english); // if it's an exponent, use "to the power of" phrasing
                }
                else {
                    english = cons("and", english);             // otherwise, add "and" between the two values
                }
                while(rhs != null) {                            // while there is still more in the right hand side
                    english = cons(first(rhs), english);        // continue down the tree
                    rhs = rest(rhs);                            // proceed with the rest of the leaves
                }
            }
        }
        else {
            return cons(tree, english);                         // reversed sentence
        }
        return nreverse(english);                               // return the reverse of the original sentence
    }


    public static String tojava (Object tree) {
        return (tojavab(tree, 0) + ";");
    }

    public static String tojavab (Object tree, int prec) {
        String java = "";                                       // intoduces an empty String that will hold the java version
        int precedence = 0;                                     // introduces an integer that functions as precedence
        if(consp(tree)) {
            String operator = (String) op((Cons) tree);         // String that stores operator
            Object lhs = lhs((Cons) tree);                      // list that stores the left hand side of the tree
            Object rhs = rhs((Cons) tree);                      // list that stores the left hand side of the tree
            if("=".equals(operator)) {                          // if equivalence, precedence is 1
                precedence = 1;
            }
            else if("+".equals(operator) || ("-".equals(operator) && rhs != null)) {
                precedence = 5;                                 // if addition/subtraction, precedence is 5
            }
            else if("*".equals(operator) || "/".equals(operator) || ("-".equals(operator) && rhs == null)) {
                precedence = 6;                                 // if multiplication/division/negation, precedence is 6
            }
            if(rhs != null) {                                   // if binary, set java to the java version of lhs and operation and java version of rhs
                java = tojavab(lhs, precedence) + operator + tojavab(rhs, precedence);
                if(precedence <= prec) {                        // if the precedence is lower than the neighborhood precedence
                    java = "(" + java + ")";                    // put parentheses around it
                }
            }
            else {                                              // if unary
                if("-".equals(operator)) {                      // check if it's negation. If so, add parentheses
                    java = "(" + operator + tojavab(lhs, precedence) + ")";
                }
                else {                                          // if it's a function that's no on operator list, give it "Math.(__)" syntax
                    java = "Math." + operator + "(" + tojavab(lhs, precedence) + ")";
                }
            }
        }
        else {
            java = tree.toString();                             // if it's an individual leaf, add it to the java version
        }
        return java;
    }

    // ****** your code ends here ******

    public static void main( String[] args ) {
        Cons bt1 = (Cons) reader("(((23 77) -3 88) ((((99)) (7))) 15 -1)");
        System.out.println("bt1 = " + bt1.toString());
        System.out.println("maxbt(bt1) = " + maxbt(bt1));

        Cons expr1 = list("=", "f", list("*", "m", "a"));
        System.out.println("expr1 = " + expr1.toString());
        System.out.println("vars(expr1) = " + vars(expr1).toString());

        Cons expr2 = list("=", "f", list("/", list("*", "m",
                        list("expt", "v",
                                new Integer(2))),
                "r"));
        System.out.println("expr2 = " + expr2.toString());
        System.out.println("vars(expr2) = " + vars(expr2).toString());
        System.out.println("occurs(m, expr2) = " + occurs("m", expr2));
        System.out.println("occurs(7, expr2) = " + occurs(new Integer(7), expr2));
        Cons expr9 = list( "=", "v",
                list("*", "v0",
                        list("exp", list("/", list("-", "t"),
                                list("*", "r", "c")))));
        System.out.println("expr9 = " + expr9.toString());
        System.out.println("vars(expr9) = " + vars(expr9).toString());
        System.out.println("occurs(c, expr9) = " + occurs("c", expr9));
        System.out.println("occurs(m, expr9) = " + occurs("m", expr9));

        Cons expr3 = list("+", new Integer(3), list("*", new Integer(5),
                new Integer(7)));
        System.out.println("expr3 = " + expr3.toString());
        System.out.println("eval(expr3) = " + eval(expr3));

        Cons expr4 = list("+", list("-", new Integer(3)),
                list("expt", list("-", new Integer(7),
                                list("/", new Integer(4),
                                        new Integer(2))),
                        new Integer(3)));
        System.out.println("expr4 = " + expr4.toString());
        System.out.println("eval(expr4) = " + eval(expr4));

        System.out.println("eval(b) = " + eval("b", list(list("b", 7))));

        Cons expr5 = list("+", new Integer(3), list("*", new Integer(5), "b"));
        System.out.println("expr5 = " + expr5.toString());
        System.out.println("eval(expr5) = " + eval(expr5, list(list("b", 7))));

        Cons expr6 = list("+", list("-", "c"),
                list("expt", list("-", "b", list("/", "z", "w")),
                        new Integer(3)));
        Cons alist = list(list("c", 3), list("b", 7), list("z", 4),
                list("w", 2), list("fred", 5));
        System.out.println("expr6 = " + expr6.toString());
        System.out.println("alist = " + alist.toString());
        System.out.println("eval(expr6) = " + eval(expr6, alist));
        System.out.println("english(expr5) = " + english(expr5).toString());
        System.out.println("english(expr6) = " + english(expr6).toString());
        System.out.println("tojava(expr1) = " + tojava(expr1).toString());
        Cons expr7 = list("=", "x", list("*", list("+", "a", "b"), "c"));
        System.out.println("expr7 = " + expr7.toString());
        System.out.println("tojava(expr7) = " + tojava(expr7).toString());
        Cons expr8 = list("=", "x", list("*", "r", list("sin", "theta")));
        System.out.println("expr8 = " + expr8.toString());
        System.out.println("tojava(expr8) = " + tojava(expr8).toString());
        System.out.println("expr9 = " + expr9.toString());
        System.out.println("tojava(expr9) = " + tojava(expr9).toString());


        Cons set3 = list("d", "b", "c", "a");

    }

}
