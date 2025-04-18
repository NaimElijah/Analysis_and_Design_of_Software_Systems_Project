package DomainLayer.EmpSubModule;

import java.util.ArrayList;
import java.util.HashMap;

public class EmployeeFacade {
    //

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
    public void setPermissions(int permissionsNum) {}
    public void addEmployee(int empId, String fname, String lname, int permissions, ArrayList<String> licenses) {}
    public void addDriver(int empId, String fname, String lname, int permissions, ArrayList<String> licenses) {}
    public void addLicense(int empId,String license) {}
    public void removeLicense(int empId,String license) {}
    public String employeeToString(int empId) {
        //todo
        return "";
    }
}
