package ServiceLayer;

import DomainLayer.EmpSubModule.EmployeeFacade;

import java.util.ArrayList;

public class EmployeeService {
    private EmployeeFacade ef;

    public EmployeeService(EmployeeFacade ef) {
        this.ef = ef;
    }

    //TODO

    public String showEmployees(){
        // todo
        return "";
    }
    public String showDrivers(){
        //todo
        return "";
    }
    public void  setPermissions(int empid, int permieeionsNum){}
    public void addEmployee(int empid, String fname, String lname, int permieeionsNum){}
    public void addDriver(int empid, String fname, String lname, int permieeionsNum, ArrayList<String>){}
    public void addLicense(int empid, String Licrnse){}
    public void removeLicense(int empid, String Licrnse){}
    public String employeeToString(){
        //todo
        return "";
    }

}
