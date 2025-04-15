package DomainLayer.Objects;

public class Employee {
    private int id;
    private String fname;
    private String lname;
    private int permissions;

    public Employee(int id, String fname, String lname, int permissions) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.permissions = permissions;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getFname() {return fname;}
    public void setFname(String fname) {this.fname = fname;}

    public String getLname() {return lname;}
    public void setLname(String lname) {this.lname = lname;}

    public int getPermissions() {return permissions;}
    public void setPermissions(int permissions) {this.permissions = permissions;}


    @Override
    public String toString() {
        return "";                          //TODO       <<-------------------
    }
}
