package DomainLayer.EmpSubModule;

import java.util.ArrayList;
import java.util.HashMap;

public class EmployeeFacade {
    private HashMap<Integer, Employee> employees;
    public EmployeeFacade() {
        employees = new HashMap<>();
    }

    public String showEmployees() {
        ///todo
        return "";
    }
    public String showDrivers(){
        //todo
        return "";
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
    public void setEmpPermissions(int empId, int permissionsNum) {}   /// added the Id so we know who to set permissions to
    /// this is so a System admin can upgrade permission for, let's say, drivers(permissions = 2), to permissions = 1 of Transport Managers and see the Transport Manager's menu now.
    public void addEmployee(int empId, String fname, String lname, int permissions, ArrayList<String> licenses) {}
    public void addDriver(int empId, String fname, String lname, int permissions, ArrayList<String> licenses) {}
    public void addLicense(int empId, String license) {}
    public void removeLicense(int empId, String license) {}

}
