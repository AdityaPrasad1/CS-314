// Account.java

public class Account implements Comparable<Account> {
    private String name;
    private Integer amount;
    public Account(String nm, Integer amt) {
        name = nm;
        amount = amt; }
    public static Account account(String nm, Integer amt) {
        return new Account(nm, amt); }
    public String name() { return name; }
    public Integer amount() { return amount; }
    public boolean equals(Object x) {
        if ( x == null ) return false;
        else if ( getClass() != x.getClass() ) return false;
        else return name.equals( ((Account)x).name); }

    // return -1 to sort this account before x, else 1
    public int compareTo(Account x) {
        if(((Comparable) this.name).compareTo((Comparable) x.name) < 0) {       // if this item comes before x
            return -1;                                                          // return a -1
        }
        else if(((Comparable) this.name).compareTo((Comparable) x.name) > 0) {  // if this item comes after x
            return 1;                                                           // return a 1
        }
        else {                                                  // if they are the same, compare the amounts per account
            if(this.amount < 0 && x.amount < 0) {               // if both amounts are withdrawals, check which withdrawal is greater
                if(this.amount < x.amount)
                    return -1;                                  // if this withdrawal is greater, return a -1
                return 1;                                       // otherwise return a 1
            }
            else if(this. amount < 0 && x.amount > 0) {         // if this amount is a withdrawal and the x amount is a deposit
                return 1;                                       // return a 1
            }
            else {                                              // if both amounts are deposits, check which deposit is greater
                if(this.amount > x.amount)                      // if this deposit is greater
                    return -1;                                  // return a -1
                return 1;                                       // if not, return a 1
            }
        }

        // ***** your code here *****

    }

    public String toString() {
        return ( "(" + this.name + " " + this.amount + ")"); }
}
