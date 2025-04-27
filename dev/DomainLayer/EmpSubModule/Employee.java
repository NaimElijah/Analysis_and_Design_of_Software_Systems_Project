package DomainLayer.EmpSubModule;

import DomainLayer.enums.enumPermissionRank;

public class Employee {
    private int id;
    private String fname;
    private String lname;
    private boolean isDeleted;
    private enumPermissionRank permissions_rank;    //  0 - Admin, 1 - Manager, 2 - Driver.
    //  when a Driver get a Permissions Upgrade, it means that his permissions rank is 1 and He now gets the Transport Manager's Menu.

    public Employee(int id, String fname, String lname, enumPermissionRank permissionsRank) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.isDeleted = false;
        this.permissions_rank = permissionsRank;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getFname() {return fname;}
    public void setFname(String fname) {this.fname = fname;}
    public String getLname() {return lname;}
    public void setLname(String lname) {this.lname = lname;}
    public boolean getIsDeleted() {return isDeleted;}
    public void setIsDeleted(boolean deleted) {isDeleted = deleted;}
    public enumPermissionRank getPermissions_rank() {return permissions_rank;}
    public void setPermissions_rank(enumPermissionRank permissions_rank) { this.permissions_rank = permissions_rank; }

    @Override
    public String toString() {
        String res = "";
        res += "Employee ID: " + id + ", First Name: " + fname + ", Last Name: " + lname + ", Permissions Rank: " + permissions_rank.toString();
        return res;
    }
}
