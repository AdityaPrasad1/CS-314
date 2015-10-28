// libtest.java      GSN    03 Oct 08; 21 Feb 12; 26 Dec 13
// 

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

interface Functor { Object fn(Object x); }

interface Predicate { boolean pred(Object x); }

@SuppressWarnings("unchecked")
public class libtest {

    // ****** your code starts here ******


    public static Integer sumlist(LinkedList<Integer> lst) {
        int sum = 0;                // introduces extra variable sum
        for(Integer item: lst) {    // walks down the list and adds the value of each item to the sum
            sum += item;
        }
        return sum;
    }

    public static Integer sumarrlist(ArrayList<Integer> lst) {
        int sum = 0;                // introduces extra variable sum
        for(Integer item: lst) {    // walks down the list and adds the value of each item to the sum
            sum += item;
        }
        return sum;
    }

    public static LinkedList<Object> subset (Predicate p, LinkedList<Object> lst) {
        LinkedList<Object> lstb = new LinkedList<Object>();     // introduces new list which will contain items that satisfy the predicate
        for(Object item: lst) {                                 // walks down the list and checks if the items satisfy the predicate
            if(p.pred(item) == true)                            // if it does, it adds it to the new list
                lstb.add(item);
        }
        return lstb;
    }

    public static LinkedList<Object> dsubset (Predicate p, LinkedList<Object> lst) {
        ListIterator<Object> iterator = lst.listIterator();     // intoduces an explicit iterator to walk down the list because the function is destructive
        while(iterator.hasNext()) {                             // if the item doesn't satisfy the predicate, remove it from the lst
            if(p.pred(iterator.next()) == false)
                iterator.remove();
        }
        return lst;
    }


    public static Object some (Predicate p, LinkedList<Object> lst) {
        for(Object item: lst) {         // walks down the lst and checks for the first item that satisfies the predicate
            if(p.pred(item) == true) {
                return item;
            }
        }
        return null;                    // if none of the items satisfy the predicate, return null
    }

    public static LinkedList<Object> mapcar (Functor f, LinkedList<Object> lst) {
        LinkedList<Object> lstb = new LinkedList<Object>();     // introduces a new list that will contain the altered items
        for(Object item: lst) {                                 // walks down the list and adds the items that have been altered to the new list
            lstb.add(f.fn(item));
        }
        return lstb;
    }


    public static LinkedList<Object> merge (LinkedList<Object> lsta, LinkedList<Object> lstb) {
        LinkedList<Object> lstc = new LinkedList<Object>();     // introduces another list that will contain the merged lists
        ListIterator<Object> iteratora = lsta.listIterator();   // introduces an explicit iterator to walk down lsta
        ListIterator<Object> iteratorb = lstb.listIterator();   // introduces an explicit iterator to walk down lstb
        while(iteratora.hasNext() && iteratorb.hasNext()) {     // while both lists still have items to check, do the following:
            Comparable itema = (Comparable) lsta.get(iteratora.nextIndex());    // creates a comparable item to function as the next item in lsta, keeps position in list consistent by not using .next()
            Comparable itemb = (Comparable) lstb.get(iteratorb.nextIndex());    // creates a comparable item to function as the next item in lstb, keeps position in list consistent by not using .next()
            if(itema.compareTo(itemb) < 0) {        // if lsta item is smaller, add it to lstc
                lstc.add(iteratora.next());
            }
            else if(itema.compareTo(itemb) > 0) {   // if lstb item is smaller, add it to lstc
                lstc.add(iteratorb.next());
            }
            else{                                   // if both are the same size, add both
                lstc.add(iteratora.next());
                lstc.add(iteratorb.next());
            }
        }
        while(iteratora.hasNext()){     // if all of lstb has been used, continue with lsta
            lstc.add(iteratora.next());
        }
        while(iteratorb.hasNext()) {    // if all of lsta has been used, continue with lstb
            lstc.add(iteratorb.next());
        }
        return lstc;    // return the merged and sorted lists (with duplicates)
    }

    public static LinkedList<Object> sort (LinkedList<Object> lst) {
        LinkedList<Object> lstb = new LinkedList<Object>();     // new list to act as the first half of lst
        LinkedList<Object> lstc = new LinkedList<Object>();     // new list to act as the second half of lst
        int i = 0;      // introduces an int to function as the position
        if(lst.size() <= 1) {       // if the list consists of 1 or no elements
            return lst;             // return an unaltered lst as it's already sorted
        }
        else {
            for(Object item: lst) {             // otherwise, split list into two and put the lower and upper half into lstb and lstc respectively
                if(i < (int) lst.size()/2) {
                    lstb.add(item);
                    i++;
                }
                else {
                    lstc.add(item);
                }
            }
        }
        lstb = sort(lstb);          // recursively sort the consecutive halves
        lstc = sort(lstc);
        return merge(lstb, lstc);   // return the sorted combination of lstb and lstc (divide and conquer)
    }

    public static LinkedList<Object> intersection (LinkedList<Object> lsta, LinkedList<Object> lstb) {
        lsta = sort(lsta);      // sort lsta
        lstb = sort(lstb);      // sort lstb
        LinkedList<Object> lstc = new LinkedList<Object>();     // introduces another list that will contain the intersection of the lists
        ListIterator<Object> iteratora = lsta.listIterator();   // introduces an explicit iterator to walk down lsta
        ListIterator<Object> iteratorb = lstb.listIterator();   // introduces an explicit iterator to walk down lstb
        while(iteratora.hasNext() && iteratorb.hasNext()) {     // while both lists still have items to check, do the following:
            Comparable itema = (Comparable) lsta.get(iteratora.nextIndex());    // creates a comparable item to function as the next item in lsta, keeps position in list consistent by not using .next()
            Comparable itemb = (Comparable) lstb.get(iteratorb.nextIndex());    // creates a comparable item to function as the next item in lstb, keeps position in list consistent by not using .next()
            if(itema.compareTo(itemb) < 0) {        // if lsta element doesn't equal lstb element, proceed to the enxt item in lsta
                iteratora.next();
            }
            else if(itema.compareTo(itemb) > 0) {   // if lsta element doesn't equal lstb element, proceed to the enxt item in lstb
                iteratorb.next();
            }
            else{                                   // if lsta element equals lstb element, add that value to lstc, and go to the next element in lstb
                lstc.add(iteratora.next());
                iteratorb.next();
            }
        }
        return lstc;        // returns the intersection of lsta and lstb
    }

    public static LinkedList<Object> reverse (LinkedList<Object> lst) {
        LinkedList<Object> lstb = new LinkedList<Object>();     // introduces new list that will contain the reverse of lst
        for(Object item: lst) {     // walks down lst and adds each successive element at the beginning of lstb, thus reversing lst
            lstb.addFirst(item);
        }
        return lstb;        // returns the reverse of lst
    }

    // ****** your code ends here ******

    public static void main(String args[]) {
        LinkedList<Integer> lst = new LinkedList<Integer>();
        lst.add(new Integer(3));
        lst.add(new Integer(17));
        lst.add(new Integer(2));
        lst.add(new Integer(5));
        System.out.println("lst = " + lst);
        System.out.println("sum = " + sumlist(lst));

        ArrayList<Integer> lstb = new ArrayList<Integer>();
        lstb.add(new Integer(3));
        lstb.add(new Integer(17));
        lstb.add(new Integer(2));
        lstb.add(new Integer(5));
        System.out.println("lstb = " + lstb);
        System.out.println("sum = " + sumarrlist(lstb));

        final Predicate myp = new Predicate()
        { public boolean pred (Object x)
            { return ( (Integer) x > 3); }};

        LinkedList<Object> lstc = new LinkedList<Object>();
        lstc.add(new Integer(3));
        lstc.add(new Integer(17));
        lstc.add(new Integer(2));
        lstc.add(new Integer(5));
        System.out.println("lstc = " + lstc);
        System.out.println("subset = " + subset(myp, lstc));

        System.out.println("lstc     = " + lstc);
        System.out.println("dsubset  = " + dsubset(myp, lstc));
        System.out.println("now lstc = " + lstc);

        LinkedList<Object> lstd = new LinkedList<Object>();
        lstd.add(new Integer(3));
        lstd.add(new Integer(17));
        lstd.add(new Integer(2));
        lstd.add(new Integer(5));
        System.out.println("lstd = " + lstd);
        System.out.println("some = " + some(myp, lstd));

        final Functor myf = new Functor()
        { public Integer fn (Object x)
            { return new Integer( (Integer) x + 2); }};

        System.out.println("mapcar = " + mapcar(myf, lstd));

        LinkedList<Object> lste = new LinkedList<Object>();
        lste.add(new Integer(1));
        lste.add(new Integer(3));
        lste.add(new Integer(5));
        lste.add(new Integer(6));
        lste.add(new Integer(9));
        lste.add(new Integer(11));
        lste.add(new Integer(23));
        System.out.println("lste = " + lste);
        LinkedList<Object> lstf = new LinkedList<Object>();
        lstf.add(new Integer(2));
        lstf.add(new Integer(3));
        lstf.add(new Integer(6));
        lstf.add(new Integer(7));
        System.out.println("lstf = " + lstf);
        System.out.println("merge = " + merge(lste, lstf));

        lste = new LinkedList<Object>();
        lste.add(new Integer(1));
        lste.add(new Integer(3));
        lste.add(new Integer(5));
        lste.add(new Integer(7));
        System.out.println("lste = " + lste);
        lstf = new LinkedList<Object>();
        lstf.add(new Integer(2));
        lstf.add(new Integer(3));
        lstf.add(new Integer(6));
        lstf.add(new Integer(6));
        lstf.add(new Integer(7));
        lstf.add(new Integer(10));
        lstf.add(new Integer(12));
        lstf.add(new Integer(17));
        System.out.println("lstf = " + lstf);
        System.out.println("merge = " + merge(lste, lstf));

        LinkedList<Object> lstg = new LinkedList<Object>();
        lstg.add(new Integer(39));
        lstg.add(new Integer(84));
        lstg.add(new Integer(5));
        lstg.add(new Integer(59));
        lstg.add(new Integer(86));
        lstg.add(new Integer(17));
        System.out.println("lstg = " + lstg);

        System.out.println("intersection(lstd, lstg) = "
                + intersection(lstd, lstg));
        System.out.println("reverse lste = " + reverse(lste));

        System.out.println("sort(lstg) = " + sort(lstg));

    }
}
