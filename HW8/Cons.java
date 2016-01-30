/**
 * this class Cons implements a Lisp-like Cons cell
 *
 * @author  Gordon S. Novak Jr.
 * @version 29 Nov 01; 25 Aug 08; 05 Sep 08; 08 Sep 08; 12 Sep 08; 24 Sep 08
 *          06 Oct 08; 07 Oct 08; 09 Oct 08; 23 Oct 08; 30 Oct 08; 07 Apr 09
 *          10 Apr 09; 02 Aug 10; 06 Aug 10; 07 Apr 14; 28 Oct 15
 */

/*  Copyright (C) 2015 Gordon Shaw Novak Jr.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
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

    // iterative version of length
    public static int length (Cons lst) {
        int n = 0;
        while ( lst != null ) {
            n++;
            lst = rest(lst); }
        return n; }

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

    public static Object copy_tree(Object tree) {
        if ( consp(tree) )
            return cons(copy_tree(first((Cons) tree)),
                    (Cons) copy_tree(rest((Cons) tree)));
        return tree; }

    public static Object subst(Object gnew, String old, Object tree) {
        if ( consp(tree) )
            return cons(subst(gnew, old, first((Cons) tree)),
                    (Cons) subst(gnew, old, rest((Cons) tree)));
        return (old.equals(tree)) ? gnew : tree; }

    // This sublis uses a ( (var value) ...) alist
    public static Object sublis(Cons alist, Object tree) {
        if ( consp(tree) )
            return cons(sublis(alist, first((Cons) tree)),
                    (Cons) sublis(alist, rest((Cons) tree)));
        if ( tree == null ) return null;
        Cons pair = assoc(tree, alist);
        return ( pair == null ) ? tree : second(pair); }

    public static Cons dummysub = list(list("t", "t"));

    public static Cons match(Object pattern, Object input) {
        return matchb(pattern, input, dummysub); }

    public static Cons matchb(Object pattern, Object input, Cons bindings) {
        if ( bindings == null ) return null;
        if ( consp(pattern) )
            if ( consp(input) )
                return matchb( rest( (Cons) pattern),
                        rest( (Cons) input),
                        matchb( first( (Cons) pattern),
                                first( (Cons) input),
                                bindings) );
            else return null;
        if ( varp(pattern) ) {
            Cons binding = assoc(pattern, bindings);
            if ( binding != null )
                if ( equal(input, second(binding)) )
                    return bindings;
                else return null;
            else return cons(list(pattern, input), bindings); }
        if ( eql(pattern, input) )
            return bindings;
        return null; }

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

    public static Object transform(Cons patpair, Cons input) {
        Cons bindings = match(first(patpair), input);
        if ( bindings == null ) return null;
        return sublis(bindings, second(patpair)); }

    // Transform a list of arguments.  If no change, returns original.
    public static Cons transformlst(Cons allpats, Cons input) {
        if ( input == null ) return null;
        Cons restt = transformlst(allpats, rest(input));
        Object thist = transformr(allpats, first(input));
        if ( thist == first(input) && restt == rest(input) )
            return input;
        return cons(thist, restt); }

    // Transform a single item.  If no change, returns original.
    public static Object transformr(Cons allpats, Object input) {
        //    System.out.println("transformr:  " + input.toString());
        if ( consp(input) ) {
            Cons listt = transformlst(allpats, (Cons) input);
            //       System.out.println("   lst =  " + listt.toString());
            return transformrb(allpats, allpats,
                    transformlst(allpats, listt)); }
        Object res = transformrb(allpats, allpats, input);
        //    System.out.println("   result =  " + res.toString());
        return res; }

    // Transform a single item.  If no change, returns original.
    public static Object transformrb(Cons pats, Cons allpats, Object input) {
        if ( pats == null ) return input;
        if ( input == null ) return null;
        Cons bindings = match(first((Cons)first(pats)), input);
        if ( bindings == null ) return transformrb(rest(pats), allpats, input);
        return sublis(bindings, second((Cons)first(pats))); }

    // Transform a single item repeatedly, until fixpoint (no change).
    public static Object transformfp(Cons allpats, Object input) {
        //    System.out.println("transformfp: " + input.toString());
        Object trans = transformr(allpats, input);
        if ( trans == input ) return input;
        //    System.out.println("    result = " + trans.toString());
        return transformfp(allpats, trans); }          // potential loop!

    public static boolean varp(Object x) {
        return ( stringp(x) &&
                ( ((String) x).charAt(0) == '?' ) ); }

    // Note: this list will handle most, but not all, cases.
    // The binary operators - and / have special cases.
    public static Cons opposites =
            list( list( "+", "-"), list( "-", "+"), list( "*", "/"),
                    list( "/", "*"), list( "sqrt", "expt"), list( "expt", "sqrt"),
                    list( "log", "exp"), list( "exp", "log") );

    public static String opposite(String op) {
        Cons pair = assoc(op, opposites);
        if ( pair != null ) return (String) second(pair);
        return "error"; }

    public static void javaprint(Object item, int tabs) {
        if ( item == null ) System.out.print("null");
        else if ( consp(item) ) javaprintlist((Cons) item, tabs);
        else if ( stringp(item) )
            if ( item.equals("zlparen") ) System.out.print("(");
            else if ( item.equals("zrparen") ) System.out.print(")");
            else if ( item.equals("zspace") ) System.out.print(" ");
            else if ( item.equals("znothing") ) ;
            else if ( item.equals("ztab") ) System.out.print("\t");
            else if ( item.equals("zreturn") ) System.out.println();
            else System.out.print((String)item);
        else System.out.print(item.toString()); }

    public static void javaprintlist(Cons lst, int tabs) {
        if ( lst != null ) {
            if ( stringp(first(lst)) )
                if ( ((String)first(lst)).equals("ztab" ) ) tabs++;
                else if ( ((String)first(lst)).equals("zuntab" ) ) tabs--;
                else if ( ((String)first(lst)).equals("zreturn" ) ) {
                    System.out.println();
                    for (int i = 0; i < tabs; i++) System.out.print("\t"); }
                else javaprint(first(lst), tabs);
            else javaprint(first(lst), tabs);
            javaprintlist(rest(lst), tabs); } }

    // ****** your code starts here ******

    // your program from the previous assignments
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
                if(rhs((Cons) tree) == null) {                  // check it's a unary operation
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

    // new for this assignment

    public static Double solveqns(Cons eqns, Cons vals, String v) {
        if(eqns == null || vals == null || v == null) { // if there are no equations, values, or variables
            return null;                                // then return null
        }
        Cons values = vals;
        while(values != null) {                         // otherwise, while there are still values
            Cons value = (Cons) first(values);          // holds the first value in the list of values
            if(v.equals(first(value))) {                // check if the desired variable has a value defined in vals
                return (Double) first(rest(value));     // if so, return the value
            }
            values = rest(values);                      // continue with the rest of the values
        }
        Cons equations = eqns;
        while(equations != null) {                      // otherwise look though list of equations
            String unknown = null;                      // String that will contain the unknown
            Cons eq = (Cons) first(equations);          // the first equation in the list
            Cons variables = vars(eq);                  // all the variables in that equation
            while(variables != null) {                  // while there are still variables to check
                Cons defined = assoc(first(variables), vals);
                if(defined == null && unknown == null) {        // check if the first variable in the list is defined in the values list and check if the unknown is found
                    unknown = first(variables).toString();      // if the variable isn't defined and unknown is empty, then set the undefined variable to unknown
                }

                variables = rest(variables);                    // otherwise continue with the rest of the variables
            }
            if(unknown != null) {                       // once the only unknown in the equation is found, evaluate it and add the value for the unknown to the list of values
                vals = cons(list(unknown, eval(rhs((Cons) solve(eq, unknown)), vals)), vals);
                return solveqns(eqns, vals, v);         // repeat the process
            }
            equations = rest(equations);                // continue with the rest of the equations
        }
        return null;                                    // if there are no more variables, return null
    }

    // Question 2 of Assignment 8
    // Code substitutions to be done in 'binaryfn' below
    // ((?function ...) (?combine ...) (?baseanswer ...))
    //    (defun ?function (tree)
    //       (if (consp tree)
    //           (?combine (?function (first tree))
    //                     (?function (rest tree)))
    //           ?baseanswer))
    public static Cons substitutions = readlist( list(
            "( (?function addnums) (?combine +) (?baseanswer (if (numberp tree) tree 0)))",

            // add to the following
            "( (?function countstrings) (?combine +) (?baseanswer (if (stringp tree) 1 0)))",
            "( (?function copytree) (?combine cons) (?baseanswer tree))",
            "( (?function mintree) (?combine min) (?baseanswer (if (numberp tree) tree null)))",
            "( (?function conses) (?combine add1) (?baseanswer (if (consp tree) 1 0)))"
    ));

    public static Cons optpats = readlist( list(
            "( (+ ?x 0)   ?x)",
            "( (+ 0 ?x)   ?x)",
            "( (expt ?x 1) ?x)",
            // add more
            "( (+ ?x ?x) (* 2 ?x))",
            "( (- ?x 0) ?x)",
            "( (- 0 ?x) ?x)",
            "( (- ?x ?x) 0)",
            "( (- (- ?x)) ?x)",
            "( (- (- ?x ?y)) (- ?y ?x))",
            "( (- 2 1) 1)",
            "( (- 3 1) 2)",
            "( (- 4 1) 3)",
            "( (* ?x 0) 0)",
            "( (* 0 ?x) 0)",
            "( (* ?x 1) ?x)",
            "( (* 1 ?x) ?x)",
            "( (* ?x ?x) (expt ?x 2))",
            "( (* (/ 1 2) (* 2 ?x)) ?x)",
            "( (/ ?x 1) ?x)",
            "( (/ 0 ?x) 0)",
            "( (/ ?x ?x) 1)",
            "( (expt ?x 0) 1)",
            "( (expt 0 ?x) 0)",
            "( (expt ?x 1) ?x)",
            "( (sqrt (expt ?x 2)) ?x)",
            "( (exp 0) 1)",
            "( (exp(log ?x)) ?x)",
            "( (log 1) 0)",
            "( (exp (log ?x)) ?x)"
    ));

    public static Cons derivpats = readlist( list(
            "( (deriv ?x ?x)   1)",
            "( (deriv (+ ?u ?v) ?x)  (+ (deriv ?u ?x) (deriv ?v ?x)))",
            // add more
            "( (deriv (- ?u ?v) ?x) (- (deriv ?u ?x) (deriv ?v ?v)))",
            "( (deriv (- ?v) ?x) (- (deriv ?v ?x)))",
            "( (deriv (* ?u ?v) ?x) (+ (* ?u (deriv ?v ?x)) (* ?v (deriv ?u ?x))))",
            "( (deriv (/ ?u ?v) ?x) (/ (- (* ?v (deriv ?u ?x)) (* ?u (deriv ?v ?x))) (expt ?v 2)))",
            "( (deriv (expt ?u ?c) ?x) (* (* ?c (expt ?u (- ?c 1))) (deriv ?u ?x)))",
            "( (deriv (sqrt ?u) ?x) (/ (* (/ 1 2) (deriv (?u ?x))) (sqrt ?u)))",
            "( (deriv (log ?u) ?x) (/ (deriv ?u ?x) ?u))",
            "( (deriv (exp ?u) ?x) (* (exp ?u) (deriv ?u ?x)))",
            "( (deriv (sin ?u) ?x) (* (cos ?u) (deriv ?u ?x)))",
            "( (deriv (cos ?u) ?x) (* (- (sin ?u)) (deriv ?u ?x)))",
            "( (deriv (tan ?u) ?x) (* (+ 1 (expt (tan ?u) 2)) (deriv ?u ?x)))",
            "( (deriv2 ?u ?x)  (deriv (deriv ?u ?x) ?x) )", // second derivative
            "( (deriv ?y ?x)   0)"   // this must be last!
    ));

    public static Cons javarestructpats = readlist( list(
            "( (return (if ?test ?then)) (if ?test (return ?then)) )",
            "( (return (if ?test ?then ?else)) (if ?test (return ?then) (return  ?else)) )",
            "( (defun ?fun ?args ?code) (zdefun ?fun (arglist ?args) (progn (return ?code))) )",
            "( (setq ?x (+ ?x 1)) (incf ?x) )"
    ));

    public static Cons javapats = readlist( list(
            "( (if ?test ?then) (if zspace zlparen zspace ?test zspace zrparen ztab zreturn ?then ))",
            "( (< ?x ?y)  (zlparen ?x zspace < zspace ?y zrparen))",
            "( (min ?x ?y) (Math.min zlparen ?x , zspace ?y zrparen))",
            "( (cons ?x ?y) (znothing cons zlparen ?x , zspace ?y zrparen))",
            "( (zdefun ?fun ?args ?code) (public zspace static zspace Object zspace ?fun zspace ?args zreturn ?code zreturn) )",
            "( (arglist (?x))   (zlparen Object zspace ?x zrparen))",
            "( (progn ?x) ({ ztab zreturn ?x zreturn }) )",
            "( (setq ?x ?y) (?x zspace = zspace ?y ; zreturn) )",
            "( (first ?x) (znothing first zlparen zlparen Cons zrparen ?x zrparen) )",
            // add more
            "( (add1 ?x ?y) (znothing ?x zspace , zspace ?y zrparen))",
            "( (+ ?x ?y) (znothing zlparen ?x zspace + zspace ?y zrparen))",
            "( (- ?x ?y) (znothing zlparen ?x zspace - zspace ?y zrparen))",
            "( (* ?x ?y) (znothing zlparen ?x zspace * zspace ?y zrparen))",
            "( (- ?x) (znothing - ?x))",
            "( (incf ?x) (znothing ?x + +))",
            "( (decf ?x) (znothing ?x - -))",
            "( (equal ?x ?y) (znothing zlparen ?x zspace == zspace ?y zrparen))",
            "( (> ?x ?y) (znothing zlparen ?x zspace > zspace ?y zrparen))",
            "( (<= ?x ?y) (znothing zlparen ?x zspace <= zspace ?y zrparen))",
            "( (>= ?x ?y) (znothing zlparen ?x zspace >= zspace ?y zrparen))",
            "( (not ?x) (znothing ! ?x))",
            "( (and ?x ?y) (znothing zlparen ?x zspace && zspace ?y zrparen))",
            "( (or ?x ?y) (znothing zlparen ?x zspace || zspace ?y zrparen))",
            "( (if ?test ?then ?else) (znothing if zspace zlparen ?test zrparen zspace { ztab zreturn ?then zuntab zreturn } zreturn else zspace { zreturn ?else zreturn }))",
            "( (return ?x) (znothing return zspace ?x ;))",
            "( (?fun ?x)   (znothing ?fun zlparen ?x zrparen))"  // must be last
    ));


    // ****** your code ends here ******

    public static void main( String[] args ) {

        Cons eqnsbat =
                readlist(
                        list("(= loss_voltage (* internal_resistance current))",
                                "(= loss_power (* internal_resistance (expt current 2)))",
                                "(= terminal_voltage (- voltage loss_voltage))",
                                "(= power (* terminal_voltage current))",
                                "(= work (* charge terminal_voltage))" ) );

        System.out.println("battery = " +
                solveqns(eqnsbat, (Cons)
                                reader("((current 0.3)(internal_resistance 4.0)(voltage 12.0))"),
                        "terminal_voltage"));
        Cons eqnscirc =
                readlist(
                        list("(= acceleration (/ (expt velocity 2) radius))",
                                "(= force        (* mass acceleration))",
                                "(= kinetic_energy   (* (/ mass 2) (expt velocity 2)))",
                                "(= moment_of_inertia (* mass (expt radius 2)))",
                                "(= omega (/ velocity radius))",
                                "(= angular_momentum (* omega moment_of_inertia))" ));

        System.out.println("circular = " +
                solveqns(eqnscirc, (Cons)
                                reader("((radius 4.0)(mass 2.0)(velocity 3.0))"),
                        "angular_momentum"));
        Cons eqnslens =
                readlist(
                        list("(= focal_length (/ radius 2))",
                                "(= (/ 1 focal_length) (+ (/ 1 image_distance) (/ 1 subject_distance)))",
                                "(= magnification (- (/ image_distance subject_distance)))",
                                "(= image_height (* magnification subject_height))" ));


        System.out.println("magnification = " +
                solveqns(eqnslens, (Cons)
                                reader("((subject_distance 6.0)(focal_length 9.0))"),
                        "magnification"));

        Cons eqnslift =
                readlist(
                        list("(= gravity     9.80665 )",
                                "(= weight      (* gravity mass))",
                                "(= force       weight)",
                                "(= work        (* force height))",
                                "(= speed       (/ height time))",
                                "(= power       (* force speed))",
                                "(= power       (/ work time)) ))" ));

        System.out.println("power = " +
                solveqns(eqnslift, (Cons)
                                reader("((weight 700.0)(height 8.0)(time 10.0)))"),
                        "power"));
        Cons binaryfn = (Cons) reader(
                "(defun ?function (tree) (if (consp tree) (?combine (?function (first tree)) (?function (rest tree))) ?baseanswer))");

        for ( Cons ptr = substitutions; ptr != null; ptr = rest(ptr) ) {
            Object trans = sublis((Cons) first(ptr), binaryfn);
            System.out.println("sublis:  " + trans.toString()); }

        Cons opttests = readlist( list(
                "(+ 0 foo)",
                "(* fum 1)",
                "(- (- y))",
                "(- (- x y))",
                "(+ foo (* fum 0))"
        ));

        for ( Cons ptr = opttests; ptr != null; ptr = rest(ptr) ) {
            System.out.println("opt:  " + ((Cons)first(ptr)).toString());
            Object trans = transformfp(optpats, first(ptr));
            System.out.println("      " + trans.toString()); }


        Cons derivtests = readlist( list(
                "(deriv x x)",
                "(deriv 3 x)",
                "(deriv z x)",
                "(deriv (+ x 3) x)",
                "(deriv (* x 3) x)",
                "(deriv (* 5 x) x)",
                "(deriv (sin x) x)",
                "(deriv (sin (* 2 x)) x)",
                "(deriv (+ (expt x 2) (+ (* 3 x) 6)) x)",
                "(deriv2 (+ (expt x 2) (+ (* 3 x) 6)) x)",
                "(deriv (sqrt (+ (expt x 2) 2)) x)",
                "(deriv (log (expt (+ 1 x) 3)) x)"

        ));

        for ( Cons ptr = derivtests; ptr != null; ptr = rest(ptr) ) {
            System.out.println("deriv:  " + ((Cons)first(ptr)).toString());
            Object trans = transformfp(derivpats, first(ptr));
            System.out.println("  der:  " + trans.toString());
            Object transopt = transformfp(optpats, trans);
            System.out.println("  opt:  " + transopt.toString()); }

        Cons javatests = readlist( list(
                "(* fum 7)",
                "(setq x y)",
                "(setq x (+ x 1))",
                "(setq area (* pi (expt r 2)))",
                "(if (> x 7) (setq y x))",
                "(if (or (> x 7) (< y 3)) (setq y x))",
                "(if (and (> x 7) (not (< y 3))) (setq y x))",
                "(defun factorial (n) (if (<= n 1) 1 (* n (factorial (- n 1)))))"
        ));

        for ( Cons ptr = javatests; ptr != null; ptr = rest(ptr) ) {
            System.out.println("java:  " + ((Cons)first(ptr)).toString());
            Cons restr = (Cons) transformfp(javarestructpats, first(ptr));
            System.out.println("       " + restr.toString());
            Cons trans = (Cons) transformfp(javapats, restr);
            System.out.println("       " + trans.toString());
            javaprintlist(trans, 0);
            System.out.println(); }


        for ( Cons ptr = substitutions; ptr != null; ptr = rest(ptr) ) {
            Object ltrans = sublis((Cons) first(ptr), binaryfn);
            System.out.println("sublis:  " + ltrans.toString());
            Cons restr = (Cons) transformfp(javarestructpats, ltrans);
            System.out.println("       " + restr.toString());
            Cons trans = (Cons) transformfp(javapats, restr);
            System.out.println("       " + trans.toString());
            javaprintlist(trans, 0);
            System.out.println(); }

    }

}
