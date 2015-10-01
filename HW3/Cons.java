/**
 * Aditya Prasad
 * ap45485
 * CS 314
 *
 * this class Cons implements a Lisp-like Cons cell
 *
 * @author  Gordon S. Novak Jr.
 * @version 29 Nov 01; 25 Aug 08; 05 Sep 08; 08 Sep 08; 12 Sep 08; 16 Feb 09
 *          01 Feb 12; 08 Feb 12; 22 Sep 13; 26 Dec 13
 */



interface Functor { Object fn(Object x); }

interface Predicate { boolean pred(Object x); }

public class Cons
{
    // instance variables
    private Object car;   // traditional name for first
    private Cons cdr;     // "could-er", traditional name for rest
    private Cons(Object first, Cons rest)
    { car = first;
        cdr = rest; }

    // Cons is the data type.
    // cons() is the method that makes a new Cons and puts two pointers in it.
    // cons("a", null) = (a)
    // cons puts a new thing on the front of an existing list.
    // cons("a", list("b","c")) = (a b c)
    public static Cons cons(Object first, Cons rest)
    { return new Cons(first, rest); }

    // consp is true if x is a Cons, false if null or non-Cons Object
    public static boolean consp (Object x)
    { return ( (x != null) && (x instanceof Cons) ); }

    // first returns the first thing in a list.
    // first(list("a", "b", "c")) = "a"
    // safe, first(null) = null
    public static Object first(Cons lst) {
        return ( (lst == null) ? null : lst.car  ); }

    // rest of a list after the first thing.
    // rest(list("a", "b", "c")) = (b c)
    // safe, rest(null) = null
    public static Cons rest(Cons lst) {
        return ( (lst == null) ? null : lst.cdr  ); }

    // second thing in a list
    // second(list("+", "b", "c")) = "b"
    public static Object second (Cons x) { return first(rest(x)); }

    // third thing in a list
    // third(list("+", "b", "c")) = "c"
    public static Object third (Cons x) { return first(rest(rest(x))); }

    // destructively change the first() of a cons to be the specified object
    // setfirst(list("a", "b", "c"), 3) = (3 b c)
    public static void setfirst (Cons x, Object i) { x.car = i; }

    // destructively change the rest() of a cons to be the specified Cons
    // setrest(list("a", "b", "c"), null) = (a)
    // setrest(list("a", "b", "c"), list("d","e")) = (a d e)
    public static void setrest  (Cons x, Cons y) { x.cdr = y; }

    // make a list of the specified items
    // list("a", "b", "c") = (a b c)
    // list() = null
    public static Cons list(Object ... elements) {
        Cons list = null;
        for (int i = elements.length-1; i >= 0; i--) {
            list = cons(elements[i], list);
        }
        return list;
    }

    public static Cons nreverse (Cons lst) {
        Cons last = null; Cons next;
        while (lst != null)
        { next =  rest(lst);
            setrest(lst, last);
            last = lst;
            lst = next; };
        return last; }

    // convert a list to a string in parenthesized form for printing
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


    // add up elements of a list of numbers
    public static int sum (Cons lst) {
        int sum = 0;
        while(lst != null) {                        // stops at null because there's nothing left in the list
            sum += (Integer) first(lst);            // takes the first integer in the list and adds it to the sum
            lst = rest(lst);                        // removes the first integer from the list
        }
        return sum;
    }

    // mean = (sum x[i]) / n
    public static double mean (Cons lst) {
        int count = 0;
        int sum = sum(lst);             // uses the sum method to get the sum of lst
        while(lst != null) {            // stops at null because there's nothing left in the list
            count++;                    // increments count each time to find the number of elements in the list
            lst = rest(lst);            // removes the first integer from the list
        }
        return (double) sum/count;      // divides the sum by count to get the average
    }

    // square of the mean = mean(lst)^2

    // mean square = (sum x[i]^2) / n
    public static double meansq (Cons lst) {
        int sqsum = 0;
        int count = 0;
        while(lst != null) {
            sqsum += square((Integer) first(lst));          // takes the square of the first integer in the list and adds it to the sum
            count++;                                        // increments count each time to find the number of elements in the list
            lst = rest(lst);                                // removes the first integer from the list
        }
        return (double) sqsum/count;                        // divides the sum of the squar integers by count to get the mean square
    }

    public static double variance (Cons lst) {
        return meansq(lst) - (mean(lst) * mean(lst));       // subtracts the square of the mean from the mean square to get variance
    }

    public static double stddev (Cons lst) {
        return Math.sqrt(variance(lst));            // squareroots the variance to get standard deviation
    }

    public static double sine (double x) {
        return sineb(x, x, 1, 1);           // sets the multiplicative factor as x and the factorial factor as 1
    }

    // public static double sineb ( ... )
    public static double sineb (double x, double y, double n, double m) {           // introduces extra parameters y and m to act as multiplicative and factorial factors
        double sum = 0;
        if(m <= 19)                                                                 // stops at m = 19 because m + 2 = 21, and the Taylor series stops when the power of x is 21
            sum += (x / n) + sineb(x*y*y*-1 , y, n*(m+1)*(m+2), m+2);               // sums the first term then calls sineb again to get the nex term which is 2 powers and 2 elements of factorial higher
        return sum;
    }

    public static int length (Cons lst) {
        Cons lstb = lst;
        int count = 0;
        while(lstb != null) {           // stops when the list is empty
            count++;                    // count increments by 1 each time
            lstb = rest(lstb);          // the first element is removed from the list
        }
        return count;
    }

    public static Cons nthcdr (int n, Cons lst) {
        if(length(lst) < n)                 // stops at n because rest needs to be applied to lst only n times
            return null;
        return nthcdr_aux(n, lst);
    }

    public static Cons nthcdr_aux (int n, Cons lst) {
        if(n == 0)                                  // stops when n drops to 0
            return lst;
        return nthcdr_aux(n-1, rest(lst));          // applies rest to list n times
    }

    public static Object elt (Cons lst, int n) {
        return first(nthcdr(n, lst));           // applies rest to list n times then takes the first item in the remaining list
    }

    public static double interpolate (Cons lst, double x) {
        int i = (int) x;                                                                                // sets i to the floored value of x
        double delta = x - i;                                                                           // sets the change to x - i
        return (Integer) elt(lst, i) + delta*((Integer) elt(lst, i+1) - (Integer) elt(lst, i));         // takes the ith element and adds it to the change multiplied by the difference between i+1 and i
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

    public static int sumtr (Cons lst) {
        return sumtr_aux(lst, 0);           // sets sum as 0
    }

    public static int sumtr_aux (Cons lst, int sum) {           // introduces additional parameter "sum"
        if(lst == null) {                                       // stops when list is empty then returns the sum
            return sum;
        }
        else if(consp(first(lst)) == true) {                                    // checks to see if the first element in list is a sublist
            sum += sumtr((Cons) first(lst));                                    // if so, call sumtr until the next element found is an int, not a sublist
            return sumtr_aux(rest(lst), sum);                                   // continues to the next element
        }
        else {
            return sumtr_aux(rest(lst), sum + (Integer) first(lst));            // otherwise, continues to the next element
        }
    }

    // use auxiliary functions as you wish.
    public static Cons subseq (Cons lst, int start, int end) {
        Cons lstb = list(Integer.valueOf((Integer) elt(lst, start)));           // sets lstb as the element at index "start"
        for(int i = start+1; i < end; i++) {                                    // starts at index start+1 because start has already been added, and stops at index end
            lstb = cons((Integer) elt(lst, i), lstb);                           // adds the next integer to the front of the list
        }
        return nreverse(lstb);                                                  // returns the reverse of lstb because it's in the opposite order
    }

    public static Cons posfilter (Cons lst) {
        return posfilter_aux(lst, null);            // sets lstb as empty
    }

    public static Cons posfilter_aux (Cons lst, Cons lstb) {                            // introduce extra parameter lstb to act as the non-negative list
        if(lst == null) {                                                               // stops when lst is empty
            return nreverse(lstb);                                                      // returns the reverse of lstb because it's in the opposite order
        }
        else if((Integer) first(lst) >= 0) {                                            // checks to see if the element is non-negative
            return posfilter_aux(rest(lst), cons((Integer) first(lst), lstb));          // if so, adds it to lstb and checks the next element
        }
        else {
            return posfilter_aux(rest(lst), lstb);                                      // otherwise, checks next element
        }
    }

    public static Cons subset (Predicate p, Cons lst) {
        return subset_aux(p, lst, null);            // sets lstb as empty
    }

    public static Cons subset_aux (Predicate p, Cons lst, Cons lstb) {                  // introduces extra parameter lstb to hold elements that satisfy the prediate
        if(lst == null) {                                                               // stops when lst is empty
            return nreverse(lstb);                                                      // returns the reverse of lstb because it's in the opposite order
        }
        else if(p.pred(first(lst)) == true) {                                           // checks to see if the first element satisfies the predicate
            return subset_aux(p, rest(lst), cons((Integer) first(lst), lstb));          // if so, adds it to lstb and checks the next element
        }
        else {
            return subset_aux(p, rest(lst), lstb);                                      // otherwise, checks the next element
        }
    }

    public static Cons mapcar (Functor f, Cons lst) {
        return mapcar_aux(f, lst, null);            // sets lstb as empty
    }

    public static Cons mapcar_aux (Functor f, Cons lst, Cons lstb) {                        // introduces extra parameter lstb to hold the elements that the f has been applied to
        if(lst == null)                                                                     // stops when lst is empty
            return nreverse(lstb);                                                          // returns the reverse of lstb because it's in the opposite order
        return mapcar_aux(f, rest(lst), cons((Integer) f.fn(first(lst)), lstb));            // applies f to the first element then adds it to lstb, continues to the next element
    }

    public static Object some (Predicate p, Cons lst) {
        if(p.pred(first(lst)) == true)                  // if the element satisfies the predicate, return that element
            return first(lst);
        else if(p.pred(first(lst)) == false)            // if the first element is false, keep checking the following elements to see if they satisfy the predicate
            return some(p, rest(lst));
        return null;                                    // otherwise, return null
    }

    public static boolean every (Predicate p, Cons lst) {
        if(p.pred(first(lst)) == false)             // if the element doesn't satisfy the element, return false
            return false;
        else if(p.pred(first(lst)) == true)         // if the first element satisfies the predicate, check the following elements to see if they satisfy the element
            return every(p, rest(lst));
        return true;                                // if every element satisies the predicate, return true
    }

    // ****** your code ends here ******

    public static void main( String[] args )
    {
        Cons mylist =
                list(95, 72, 86, 70, 97, 72, 52, 88, 77, 94, 91, 79,
                        61, 77, 99, 70, 91 );
        System.out.println("mylist = " + mylist.toString());
        System.out.println("sum = " + sum(mylist));
        System.out.println("mean = " + mean(mylist));
        System.out.println("meansq = " + meansq(mylist));
        System.out.println("variance = " + variance(mylist));
        System.out.println("stddev = " + stddev(mylist));
        System.out.println("sine(0.5) = " + sine(0.5));  // 0.47942553860420301
        System.out.print("nthcdr 5 = ");
        System.out.println(nthcdr(5, mylist));
        System.out.print("nthcdr 18 = ");
        System.out.println(nthcdr(18, mylist));
        System.out.println("elt 5 = " + elt(mylist,5));

        Cons mylistb = list(0, 30, 56, 78, 96);
        System.out.println("mylistb = " + mylistb.toString());
        System.out.println("interpolate(3.4) = " + interpolate(mylistb, 3.4));
        Cons binom = binomial(12);
        System.out.println("binom = " + binom.toString());
        System.out.println("interpolate(3.4) = " + interpolate(binom, 3.4));

        Cons mylistc = list(1, list(2, 3), list(list(list(list(4)),
                        list(5)),
                6));
        System.out.println("mylistc = " + mylistc.toString());
        System.out.println("sumtr = " + sumtr(mylistc));
        Cons mylistcc = list(1, list(7, list(list(2), 3)),
                list(list(list(list(list(list(list(4)))), 9))),
                list(list(list(list(5), 4), 3)),
                list(6));
        System.out.println("mylistcc = " + mylistcc.toString());
        System.out.println("sumtr = " + sumtr(mylistcc));

        Cons mylistd = list(0, 1, 2, 3, 4, 5, 6 );
        System.out.println("mylistd = " + mylistd.toString());
        System.out.println("subseq(2 5) = " + subseq(mylistd, 2, 5));

        Cons myliste = list(3, 17, -2, 0, -3, 4, -5, 12 );
        System.out.println("myliste = " + myliste.toString());
        System.out.println("posfilter = " + posfilter(myliste));

        final Predicate myp = new Predicate()
        { public boolean pred (Object x)
            { return ( (Integer) x > 3); }};

        System.out.println("subset = " + subset(myp, myliste).toString());

        final Functor myf = new Functor()
        { public Integer fn (Object x)
            { return  (Integer) x + 2; }};

        System.out.println("mapcar = " + mapcar(myf, mylistd).toString());

        System.out.println("some = " + some(myp, myliste).toString());

        System.out.println("every = " + every(myp, myliste));
    }

}
