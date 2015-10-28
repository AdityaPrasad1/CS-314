import java.lang.Math;

/**
 * Aditya Prasad
 * ap45485
 * CS 314
 *
 * this class Cons implements a Lisp-like Cons cell
 *
 * @author  Gordon S. Novak Jr.
 * @version 29 Nov 01; 25 Aug 08; 05 Sep 08; 08 Sep 08; 02 Sep 09; 27 Jan 10
 *          05 Feb 10; 16 Jul 10; 02 Sep 10; 13 Jul 11
 */

public class Cons
{
    // instance variables
    private Object car;
    private Cons cdr;
    private Cons(Object first, Cons rest)
    { car = first;
        cdr = rest; }

    // make a new Cons and put the arguments into it
    // add one new thing to the front of an existing list
    // cons("a", list("b", "c"))  =  (a b c)
    public static Cons cons(Object first, Cons rest)
    { return new Cons(first, rest); }

    // test whether argument is a Cons
    public static boolean consp (Object x)
    { return ( (x != null) && (x instanceof Cons) ); }

    // first thing in a list:    first(list("a", "b", "c")) = "a"
    // safe, returns null if lst is null
    public static Object first(Cons lst) {
        return ( (lst == null) ? null : lst.car  ); }

    // rest of a list after the first thing:
    //    rest(list("a", "b", "c")) = (b c)
    // safe, returns null if lst is null
    public static Cons rest(Cons lst) {
        return ( (lst == null) ? null : lst.cdr  ); }

    // second thing in a list:    second(list("a", "b", "c")) = "b"
    public static Object second (Cons x) { return first(rest(x)); }

    // third thing in a list:    third(list("a", "b", "c")) = "c"
    public static Object third (Cons x) { return first(rest(rest(x))); }

    // destructively replace the first
    public static void setfirst (Cons x, Object i) { x.car = i; }

    // destructively replace the rest
    public static void setrest  (Cons x, Cons y) { x.cdr = y; }

    // make a list of things:   list("a", "b", "c") = (a b c)
    public static Cons list(Object ... elements) {
        Cons list = null;
        for (int i = elements.length-1; i >= 0; i--) {
            list = cons(elements[i], list);
        }
        return list;
    }

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

    public static int square(int x) { return x*x; }

    // ****** your code starts here ******

    // Sum of squares of integers from 1..n
    public static int sumsq (int n) {
        if (n == 1)                         // base case is 1 because 1 is where the summation stops, n to 1
            return 1;
        return (n*n) + sumsq(n-1);          // squares the first number then repeats recursively n-1 more times and sums each time

    }

    // Addition using Peano arithmetic
    public static int peanoplus(int x, int y) {
        if(y == 0)                          // base case is 0 because 0 is the additive identity
            return x;
        return peanoplus(++x, --y);         // x increases by 1 and y decreases by 1 every time peanoplus is called until y hits 0, will always be called y times, essentially x+y
    }

    // Multiplication using Peano arithmetic
    public static int peanotimes(int x, int y) {
        if(y == 1)                                          // base case is 1 because 1 is the multiplicative identity
            return x;
        return peanoplus(x, peanotimes(x, --y));            // uses peanoplus within peanoplus to emulate multiplication, addition within addition, x is added to itself y-1 times
    }

    // n choose k: distinct subsets of k items chosen from n items
    public static int choose(int n, int k) {
        return (int) choose_aux(n, k, 1, 1);           // tail recursive, casts choose_aux as int because it returns long
    }

    // Auxiliary method for choose
    public static long choose_aux(int n, int k, long top, long bottom) {       // uses long type to accommodate possibility of very large numbers
        if(k == 0)                                                             // stops at 0 because 0 will bring the numerator or denominator to 0, more importantly dividing by 0 is illegal
            return top/bottom;
        return choose_aux(n-1, k-1, (long) n*top, (long) k*bottom);            // based on the choose formula, essentially uses factorial in the numerator by multiplying n by the first k-1 integers below n then divides by k factorial
    }

    // Add up a list of Integer
    // iterative version, using while
    public static int sumlist (Cons lst) {
        int sum = 0;
        while ( lst != null ) {
            sum += (Integer) first(lst);   // cast since first() can be Object
            lst = rest(lst); }
        return sum; }

    // second iterative version, using for
    public static int sumlistb (Cons arg) {
        int sum = 0;
        for (Cons lst = arg ; lst != null; lst = rest(lst) )
            sum += (Integer) first(lst);   // cast since first() can be Object
        return sum; }

    // recursive version
    public static int sumlistr (Cons lst) {
        if(lst == null)                                             // base case is when lst is null because there's nothing left in the list to add
            return 0;
        return (Integer) first(lst) + sumlistr(rest(lst));          // walks down the linked list and adds each element to the previous sum
    }

    // tail recursive version
    public static int sumlisttr (Cons lst) {
        return sumlisttr_aux(lst, 0);          // sets sum as 0 in auxiliary method
    }

    // auxiliary method for sumlisttr
    public static int sumlisttr_aux (Cons lst, int sum) {                      // uses additional parameter "sum" to keep track of total
        if(lst == null)                                                        // base case is when lst is null because there is nothing else left in the list to add
            return sum;
        return sumlisttr_aux(rest(lst), sum + (Integer) first(lst));           // walks down the linked list and adds each element to "sum"
    }

    // Sum of squared differences of elements of two lists
    // iterative version
    public static int sumsqdiff (Cons lst, Cons lstb) {
        int sum = 0;
        while(lst != null) {                                                        // while loop stops when lst is null because there's nothing left in the list
            sum += square((Integer) first(lst) - (Integer) first(lstb));            // subtracts the first element in lst by the first element in lstb then squares the difference
            lst = rest(lst);                                                        // removes the first element from lst
            lstb = rest(lstb);                                                      // removes the first element from lstb
        }
        return sum;
    }

    // recursive version
    public static int sumsqdiffr (Cons lst, Cons lstb) {
        if(lst == null)                                                                                             // base case is null because there's nothing left in the lists
            return 0;
        return square((Integer) first(lst) - (Integer) first(lstb)) + sumsqdiffr(rest(lst), rest(lstb));            // squares the difference between the first elements of lst and lstb and recursively continues for the following elements, sums the differences
    }

    // tail recursive version
    public static int sumsqdifftr (Cons lst, Cons lstb) {
        return sumsqdifftr_aux(lst, lstb, 0);          // sets sum as 0 in auxiliary method
    }

    // auxiliary method for sumsqdifftr
    public static int sumsqdifftr_aux (Cons lst, Cons lstb, int sum) {                                                     // uses additional parameter "sum" to keep track of total
        if(lst == null)                                                                                                 // base case is when lst is null because there's nothing left in the lists
            return sum;
        return sumsqdifftr_aux(rest(lst), rest(lstb), sum + square((Integer) first(lst) - (Integer) first(lstb)));         // squares the difference between the first elements of lst and lstb and recursively continues for the following elements, adds differences to sum
    }

    // Find the maximum value in a list of Integer
    // iterative version
    public static int maxlist (Cons lst) {
        int max = 0;                                    // sets up parameter "max" which will signify the biggest number in the list
        while(lst != null) {                            // stops when lst is null because there are no more elements in the list
            if((Integer) first(lst) > max) {            // checks to see if the next element in lst is greater than the current max
                max = (Integer) first(lst);             // if true, assigns that same element to "max"
                lst = rest(lst);                        // removes the element from the list
            }
            lst = rest(lst);                            // otherwise, removes element from the list
        }
        return max;
    }

    // recursive version
    public static int maxlistr (Cons lst) {
        if(lst == null) {                                               // stops at null because lst is empty at that point
            return 0;
        }
        else if(maxlistr(rest(lst)) > (Integer) first(lst)) {           // checks to see if the 2nd element in lst is greater than the first one
            return maxlistr(rest(lst));                                 // if so, compare the next element with the 2nd element and continue recursively
        }
        else {
            return (Integer) first(lst);                                // otherwise, return the first element which is bigger than the second
        }
    }

    // tail recursive version
    public static int maxlisttr (Cons lst) {
        return maxlisttr_aux(lst, 0);          // sets the temporary max as 0
    }

    // auxiliary method for maxlisttr
    public static int maxlisttr_aux (Cons lst, int max) {                    // sets up additional parameter "max" which will signify the biggest number in the list
        if(lst == null) {                                                    // stops when lst is null because there are nor more elements to compare
            return max;
        }
        else if((Integer) first(lst) > max) {                               // checks to see if the first element in lst is greater than "max"

            return maxlisttr_aux(rest(lst), (Integer) first(lst));           // walk down the rest of list and compare the elements with the current max
        }
        else {
            return maxlisttr_aux(rest(lst), max);                            // otherwise, remove that element from the list
        }
    }

    // Make a list of Binomial coefficients
    // binomial(2) = (1 2 1)
    public static Cons binomial(int n) {
        Cons lst = list(Integer.valueOf(1));            // sets lst as 1 to function as the left-hand 1 in the Pascal row
        return binomial_aux(lst, n);
    }

    // Auxiliary method for binomial
    public static Cons binomial_aux(Cons lst, int n) {
        Cons lstb = list(Integer.valueOf(1));                                                   // sets lstb as 1 to function as the righ-hand 1 in the Pascal row
        if(n == 0)                                                                              // stops when n is 0 because it has produced n rows to calculate the nth row
            return lst;
        return binomial_aux(cons(Integer.valueOf(1), binomial_auxb(lst, lstb)), n-1);           // sets up the structure of the linked list with the 1 at the beginning and uses binomial_auxb to produce the elements in the middle
    }

    // Auxiliary method for binomial_aux
    public static Cons binomial_auxb(Cons lst, Cons lstb) {                                                     // introdudes a second list lstb to function as the right-hand 1 in the Pascal row
        if(rest(lst) == null)                                                                                   // stops when lst is null because there's nothing left in the list
            return lstb;
        return binomial_auxb(rest(lst), cons((Integer) first(lst) + (Integer) first(rest(lst)), lstb));         // produces the elements in between the 1s at the beginning and the end of each row by taking the numbers from the previous rows and adding them sequentially
    }


    // ****** your code ends here ******


    public static void main( String[] args )
    {
        System.out.println("sumsq(5) = " + sumsq(5));

        System.out.println("peanoplus(3, 5) = " + peanoplus(3, 5));
        System.out.println("peanotimes(3, 5) = " + peanotimes(3, 5));
        System.out.println("peanotimes(30, 30) = " + peanotimes(30, 30));

        System.out.println("choose 5 3 = " + choose(5, 3));
        System.out.println("choose 100 3 = " + choose(100, 3));
        System.out.println("choose 20 10 = " + choose(20, 10));
        System.out.println("choose 100 5 = " + choose(100, 5));
        for (int i = 0; i <= 4; i++)
            System.out.println("choose 4 " + i + " = " + choose(4, i));

        Cons mylist = list(Integer.valueOf(3), Integer.valueOf(4),
                Integer.valueOf(8), Integer.valueOf(2));
        Cons mylistb = list(Integer.valueOf(2), Integer.valueOf(1),
                Integer.valueOf(6), Integer.valueOf(5));

        System.out.println("mylist = " + mylist);

        System.out.println("sumlist = " + sumlist(mylist));
        System.out.println("sumlistb = " + sumlistb(mylist));
        System.out.println("sumlistr = " + sumlistr(mylist));
        System.out.println("sumlisttr = " + sumlisttr(mylist));

        System.out.println("mylistb = " + mylistb);

        System.out.println("sumsqdiff = " + sumsqdiff(mylist, mylistb));
        System.out.println("sumsqdiffr = " + sumsqdiffr(mylist, mylistb));

        System.out.println("sumsqdifftr = " + sumsqdifftr(mylist, mylistb));

        System.out.println("maxlist " + mylist + " = " + maxlist(mylist));
        System.out.println("maxlistr = " + maxlistr(mylist));
        System.out.println("maxlisttr = " + maxlisttr(mylist));

        System.out.println("binomial(4) = " + binomial(4));
        System.out.println("binomial(20) = " + binomial(20));
    }

}
