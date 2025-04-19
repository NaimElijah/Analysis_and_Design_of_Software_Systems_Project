package DomainLayer.EmpSubModule;

public class Employee {
    private int id;
    private String fname;
    private String lname;
    private int permissions_rank;    // maybe: 0 for System Admin, 1 for Transport Manager, 2 for Driver.
    //  when a Driver get a Permissions Upgrade, it means that his premissions rank is 1 and He now gets the Transport Manager's Menu.

    public Employee(int id, String fname, String lname, int permissions) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.permissions_rank = permissions;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getFname() {return fname;}
    public void setFname(String fname) {this.fname = fname;}
    public String getLname() {return lname;}
    public void setLname(String lname) {this.lname = lname;}
    public int getPermissions_rank() {return permissions_rank;}
    public void setPermissions_rank(int permissions_rank) {this.permissions_rank = permissions_rank;}

    @Override
    public String toString() {
        String res = "";
        res += "Employee ID: " + id + ", First Name: " + fname + ", Last Name: " + lname + ", Permissions Rank: " + permissions_rank;
        return res;
    }
}
