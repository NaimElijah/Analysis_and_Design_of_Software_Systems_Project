package DomainLayer.EmpSubModule;

import DomainLayer.enumDriLicense;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.HashMap;

public class EmployeeFacade {
    private HashMap<Integer, Employee> employees;

    public EmployeeFacade() {
        employees = new HashMap<>();
    }

    public HashMap<Integer, Driver> getDrivers() {
        HashMap<Integer, Driver> drivers = new HashMap<>();
        for (Employee employee : employees.values()) {
            if (employee instanceof Driver) {
                Driver driver = (Driver) employee;
                drivers.put(driver.getId(), driver);
            }
        }
        return drivers;
    }

    public HashMap<Integer, Employee> getEmployees() {return employees;}
    public void setEmployees(HashMap<Integer, Employee> employees) {this.employees = employees;}

    public void addEmployee(int empId, String fname, String lname) throws KeyAlreadyExistsException, AssertionError {
        if (this.employees.containsKey(empId)){
            throw new KeyAlreadyExistsException("The Employee Id you are trying to add already exists");
        }
        this.employees.put(empId, new Employee(empId, fname, lname, enumPermissionRank.UnrelatedEmployee));
        //TODO
    }


    public void addDriver(int empId, String fname, String lname, ArrayList<String> licenses) {
        //TODO
    }


    public void addManager(int empId, String fname, String lname) throws KeyAlreadyExistsException, AssertionError {
        if (this.employees.containsKey(empId)){
            throw new KeyAlreadyExistsException("The Employee Id you are trying to add already exists");
        }
        this.employees.put(empId, new Employee(empId, fname, lname, enumPermissionRank.Manager));
        //TODO
    }


    public void removeEmployee(int empId) throws ClassNotFoundException {   /// for Manager usage
        if (!this.employees.containsKey(empId)) {
            throw new ClassNotFoundException("The Employee Id you've entered doens't doesn't exist");
        } else if (employees.get(empId).getPermissions_rank().equals(enumPermissionRank.Admin) || employees.get(empId).getPermissions_rank().equals(enumPermissionRank.Manager)) {
            throw new AssertionError("Can't delete Admin or another Manager.");
        }
        //TODO
    }


    public void removeDriver(int empId) {
        //TODO
    }


    public void removeManager(int empId) throws ClassNotFoundException {   /// for Admin usage
        if (!this.employees.containsKey(empId)) {
            throw new ClassNotFoundException("The Employee Id you've entered doens't doesn't exist");
        } else if (employees.get(empId).getPermissions_rank().equals(enumPermissionRank.Admin)) {
            throw new AssertionError("Can't delete Admin.(can't delete Yourself)");
        }
        //TODO
    }


    public void setEmployeePermissionsRank(int empId, enumPermissionRank permissionRank) {
        //TODO
        //TODO: that if its permission rank is 3 (unrelated employee), and we set to 1, 2, cannot be 0 (only 1 Admin according to Requirement)
        //TODO: if he is 3(unrelated) and we set to 2(driver), so he becomes a new object Driver, and we ask for licenses etc.
        // there's also:  1 -> 2 (demotion, and ask for licenses etc, make him a new object Driver)  ,  2 -> 1 (promotion)  ,  3 -> 1 (promotion).
    }







    public String showEmployees() {
        String res = "Showing all Employees:";
        for (Employee employee : employees.values()) {
            res += employee.toString() + "\n";
        }
        return "";
    }

    public String showDrivers(){
        HashMap<Integer, Driver> drivers = this.getDrivers();
        String res = "Showing All Drivers:";
        for (Driver driver : drivers.values()) {
            res += driver.toString() + "\n";
        }
        return res;
    }

    public String showManagers() {
        String res = "Showing all Managers:";
        for (Employee employee : employees.values()) {
            if (employee.getPermissions_rank().equals(enumPermissionRank.Manager)) {
                res += employee.toString() + "\n";
            }
        }
        return "";
    }

}
