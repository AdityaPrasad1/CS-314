/**
 * this class Cons implements a Lisp-like Cons cell
 *
 * @author  Gordon S. Novak Jr.
 * @version 29 Nov 01; 25 Aug 08; 05 Sep 08; 08 Sep 08; 12 Sep 08; 24 Sep 08
 *          02 Oct 09; 12 Feb 10; 04 Oct 12; 03 Oct 14; 25 Feb 15
 */

interface Functor { Object fn(Object x); }

interface Predicate { boolean pred(Object x); }

@SuppressWarnings("unchecked")
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

    public static Cons nreverse (Cons lst) {
        Cons last = null; Cons next;
        while (lst != null)
        { next =  rest(lst);
            setrest(lst, last);
            last = lst;
            lst = next; };
        return last; }

    // iterative destructive merge using compareTo
    public static Cons dmerj (Cons x, Cons y) {
        if ( x == null ) return y;
        else if ( y == null ) return x;
        else { Cons front = x;
            if ( ((Comparable) first(x)).compareTo(first(y)) < 0)
                x = rest(x);
            else { front = y;
                y = rest(y); };
            Cons end = front;
            while ( x != null )
            { if ( y == null ||
                    ((Comparable) first(x)).compareTo(first(y)) < 0)
            { setrest(end, x);
                x = rest(x); }
            else { setrest(end, y);
                y = rest(y); };
                end = rest(end); }
            setrest(end, y);
            return front; } }

    public static Cons midpoint (Cons lst) {
        Cons current = lst;
        Cons prev = current;
        while ( lst != null && rest(lst) != null) {
            lst = rest(rest(lst));
            prev = current;
            current = rest(current); };
        return prev; }

    // Destructive merge sort of a linked list, Ascending order.
    // Assumes that each list element implements the Comparable interface.
    // This function will rearrange the order (but not location)
    // of list elements.  Therefore, you must save the result of
    // this function as the pointer to the new head of the list, e.g.
    //    mylist = llmergesort(mylist);
    public static Cons llmergesort (Cons lst) {
        if ( lst == null || rest(lst) == null)
            return lst;
        else { Cons mid = midpoint(lst);
            Cons half = rest(mid);
            setrest(mid, null);
            return dmerj( llmergesort(lst),
                    llmergesort(half)); } }


    // ****** your code starts here ******
    // add other functions as you wish.

    public static Cons union (Cons x, Cons y) {
        x = llmergesort(x);     // sorts x
        y = llmergesort(y);     // sorts y
        x = mergeunion(x, y);   // sets x as the difference between the two sets
        return dmerj(x, y);     // merges the new x and y to get all the elements of both lists, sorted, no duplicates
    }

    // following is a helper function for union
    public static Cons mergeunion (Cons x, Cons y) {
        if(x == null || y == null) {        // checks to see if one of the lists is empty
            return null;                    // if so, stop
        }
        else if(first(x).equals(first(y))) {        // if the first elements in both lists are equal
            return mergeunion(rest(x), rest(y));    // proceed to the next elements on the lists
        }
        else if(((Comparable) first(x)).compareTo((Comparable) first(y)) < 0) {     // if element x comes before element y
            return cons(first(x), mergeunion(rest(x), y));                          // add element x to the new set, and continue with list y and the rest of list x
        }
        else {
            return cons(first(y), mergeunion(x, rest(y)));      // if element y is smaller, add y to the new set and continue with list x and the rest of y
        }
    }

    public static Cons setDifference (Cons x, Cons y) {
        x = llmergesort(x);     // sorts x
        y = llmergesort(y);     // sorts y
        return mergediff(x, y); // returns the elements that are in set x and but aren't in set y
    }

    // following is a helper function for setDifference
    public static Cons mergediff (Cons x, Cons y) {
        if(x == null || y == null) {        // checks to see if one of the lists is empty
            return null;                    // if so, stop
        }
        else if(first(x).equals(first(y))) {        // if the first elements in both lists are equal
            return mergediff(rest(x), rest(y));     // proceed to the next elements on the lists
        }
        else if(((Comparable) first(x)).compareTo((Comparable) first(y)) < 0) {     // if element x comes before element y
            return cons(first(x), mergediff(rest(x), y));                           // add element x to the new set, and continue with list y and the rest of list x
        }
        else {
            return cons(first(y), mergediff(x, rest(y)));      // if element y is smaller, add y to the new set and continue with list x and the rest of y
        }
    }

    public static Cons bank(Cons accounts, Cons updates) {
        Cons updatedAccounts = null;                        // introduces a new list that will contain the updated balances
        updates = llmergesort(updates);                     // sorts the updates by name then by amount
        while(updates != null) {                            // while the list isn't empty
            Account account = (Account) first(accounts);    // introduce new Acccount that will function as the old account
            Account update = (Account) first(updates);      // introduce new Account that will function as the updated account
            int x = 0;
            if(account == null) {
                x = -1;
            }
            else {
                x = update.name().compareTo(account.name());
            }

            if (x > 0) {                                                // if the account names don't match,
                updatedAccounts = cons(account, updatedAccounts);       // add the account to the list of updated account
                accounts = rest(accounts);                              // check the rest of the accounts
            }
            else if(x < 0) {                                            // if the account names don't match, then it's a new account
                if(update.amount() < 0) {                               // if it's a withdrawal, then don't add it and notify
                    System.out.println("No Account: " + update.name() + ", Balance: " + update.amount());
                }
                else {                                                  // if it's a deposit
                    System.out.println("New Account: " + update.name() + ", Balance: " + update.amount());
                    updatedAccounts = cons(update, updatedAccounts);    // then add it to the accounts
                }
                updates = rest(updates);                                // check the rest of the updates
            }
            else {                                                      // if the update and the account names match
                int balance = account.amount() + update.amount();       // combine the current balance and the updated balance
                if(balance < 0) {                                       // if it's an overdraft, create a notification
                    System.out.println("OVERDRAFT - Account: " + account.name() + ", Balance: " + balance);
                    balance -= 30;      // charge a $30 overdraft fee
                }
                setfirst(accounts, new Account(account.name(), balance));       // update account with the new balance
                updates = rest(updates);                                        // continue with the rest of the updates
            }
        }
        while(accounts != null) {                                           // while there are still accounts to check
            updatedAccounts = cons(first(accounts), updatedAccounts);       // add the updated accounts to the new list
            accounts = rest(accounts);                                      // continue with the rest of the accounts
        }

        return nreverse(updatedAccounts);       // reverse the list because it's in the opposite order
    }

    public static String [] mergearr(String [] x, String [] y) {
        String[] z = new String[x.length + y.length];       // create an array with the length of x and y combined
        int i = 0;                                          // introduce three integers to act as the indices for the arrays
        int j = 0;
        int k = 0;
        while(j < x.length && k < y.length) {       // while the indices for both lists are less than the array lengths
            if(x[j].compareTo(y[k]) < 0) {          // if x is smaller than y
                z[i] = x[j];                        // set the index in the new array to x
                i++;                                // increment new array index
                j++;                                // increment x index
            }
            else if(x[j].compareTo(y[k]) > 0) {     // if y is smaller than x
                z[i] = y[k];                        // set the index in the new array to y
                i++;                                // increment new array index
                k++;                                // increment y index
            }
            else {                  // if they are the same value
                z[i] = x[j];        // add x to the new array
                i++;                // increment new array index
                j++;                // increment x index
                z[i] = y[k];        // add y to the next index in new array
                i++;                // increment new array index
                k++;                // increment y index
            }
        }
        while(j < x.length) {       // if all of y has been used, continue the same process with x
            z[i] = x[j];
            i++;
            j++;
        }
        while(k < y.length) {       // if all of x has been used, continue the same process with y
            z[i] = y[k];
            i++;
            k++;
        }
        return z;       // return the new array
    }

    public static boolean markup (Cons text) {
        int position = 0;                           // introduces a position parameter
        Cons stack = null;                          // introduces a Cons stack
        while (text != null) {                      // while there are still Strings in the list
            String pos = (String) first(text);      // check that String isn't empty and that it's a tag < > or </ >
            if (pos.length() > 0 && pos.charAt(0) == '<') {
                if (pos.charAt(1) == '/') {         // if it's a closing tag </ >
                    if (!pos.substring(2).equals(first(stack))) {       // if the first item in the stack doesn't match the tag (Offending tag)
                        if (first(stack) != null) {                     // then check if the stack has a tag stored, if so send out a message
                            System.out.println("Offending Tag: " + pos + ", Position: " + position + ", Expected Tag: </" + first(stack));
                        }
                        else {                                          // if not, send out a message saying it's unbalanced
                            System.out.println("Offending Tag: " + pos + ", Position: " + position + ", Unbalanced");
                        }
                        return false;       // return false because it's not balanced
                    }
                    stack = rest(stack);    // if the first item in the stack does match, continue with the next item in the stack
                }
                else {      // if it's another tag, then add it to the stack
                    stack = cons(pos.substring(1), stack);
                }
            }
            text = rest(text);      // if it's not a tag at all, proceed down the list
            position++;             // increment position
        }
        if (first(stack) != null) {     // if the stack still has contents and hasn't found a matching tag, send out a message saying the tags are not balanced
            System.out.println("Offending Tag: <" + first(stack) + ", Position: " + position + ", Unbalanced");
            return false;               // return false because it's not balanced
        }
        return true;        // if everything matches, return true
    }


    // ****** your code ends here ******

    public static void main( String[] args )
    {
        Cons set1 = list("d", "b", "c", "a");
        Cons set2 = list("f", "d", "b", "g", "h");
        System.out.println("set1 = " + Cons.toString(set1));
        System.out.println("set2 = " + Cons.toString(set2));
        System.out.println("union = " + Cons.toString(union(set1, set2)));

        Cons set3 = list("d", "b", "c", "a");
        Cons set4 = list("f", "d", "b", "g", "h");
        System.out.println("set3 = " + Cons.toString(set3));
        System.out.println("set4 = " + Cons.toString(set4));
        System.out.println("difference = " +
                Cons.toString(setDifference(set3, set4)));

        Cons accounts = list(
                new Account("Arbiter", new Integer(498)),
                new Account("Flintstone", new Integer(102)),
                new Account("Foonly", new Integer(123)),
                new Account("Kenobi", new Integer(373)),
                new Account("Rubble", new Integer(514)),
                new Account("Tirebiter", new Integer(752)),
                new Account("Vader", new Integer(1024)) );

        Cons updates = list(
                new Account("Foonly", new Integer(100)),
                new Account("Flintstone", new Integer(-10)),
                new Account("Arbiter", new Integer(-600)),
                new Account("Garble", new Integer(-100)),
                new Account("Rabble", new Integer(100)),
                new Account("Flintstone", new Integer(-20)),
                new Account("Foonly", new Integer(10)),
                new Account("Tirebiter", new Integer(-200)),
                new Account("Flintstone", new Integer(10)),
                new Account("Flintstone", new Integer(-120))  );
        System.out.println("accounts = " + accounts.toString());
        System.out.println("updates = " + updates.toString());
        Cons newaccounts = bank(accounts, updates);
        System.out.println("result = " + newaccounts.toString());

        String[] arra = {"a", "big", "dog", "hippo"};
        String[] arrb = {"canary", "cat", "fox", "turtle"};
        String[] resarr = mergearr(arra, arrb);
        for ( int i = 0; i < resarr.length; i++ )
            System.out.println(resarr[i]);

        Cons xmla = list( "<TT>", "foo", "</TT>");
        Cons xmlb = list( "<TABLE>", "<TR>", "<TD>", "foo", "</TD>",
                "<TD>", "bar", "</TD>", "</TR>",
                "<TR>", "<TD>", "fum", "</TD>", "<TD>",
                "baz", "</TD>", "</TR>", "</TABLE>" );
        Cons xmlc = list( "<TABLE>", "<TR>", "<TD>", "foo", "</TD>",
                "<TD>", "bar", "</TD>", "</TR>",
                "<TR>", "<TD>", "fum", "</TD>", "<TD>",
                "baz", "</TD>", "</WHAT>", "</TABLE>" );
        Cons xmld = list( "<TABLE>", "<TR>", "<TD>", "foo", "</TD>",
                "<TD>", "bar", "</TD>", "", "</TR>",
                "</TABLE>", "</NOW>" );
        Cons xmle = list( "<THIS>", "<CANT>", "<BE>", "foo", "<RIGHT>" );
        Cons xmlf = list( "<CATALOG>",
                "<CD>",
                "<TITLE>", "Empire", "Burlesque", "</TITLE>",
                "<ARTIST>", "Bob", "Dylan", "</ARTIST>",
                "<COUNTRY>", "USA", "</COUNTRY>",
                "<COMPANY>", "Columbia", "</COMPANY>",
                "<PRICE>", "10.90", "</PRICE>",
                "<YEAR>", "1985", "</YEAR>",
                "</CD>",
                "<CD>",
                "<TITLE>", "Hide", "your", "heart", "</TITLE>",
                "<ARTIST>", "Bonnie", "Tyler", "</ARTIST>",
                "<COUNTRY>", "UK", "</COUNTRY>",
                "<COMPANY>", "CBS", "Records", "</COMPANY>",
                "<PRICE>", "9.90", "</PRICE>",
                "<YEAR>", "1988", "</YEAR>",
                "</CD>", "</CATALOG>");
        System.out.println("xmla = " + xmla.toString());
        System.out.println("result = " + markup(xmla));
        System.out.println("xmlb = " + xmlb.toString());
        System.out.println("result = " + markup(xmlb));
        System.out.println("xmlc = " + xmlc.toString());
        System.out.println("result = " + markup(xmlc));
        System.out.println("xmld = " + xmld.toString());
        System.out.println("result = " + markup(xmld));
        System.out.println("xmle = " + xmle.toString());
        System.out.println("result = " + markup(xmle));
        System.out.println("xmlf = " + xmlf.toString());
        System.out.println("result = " + markup(xmlf));
    }

}
