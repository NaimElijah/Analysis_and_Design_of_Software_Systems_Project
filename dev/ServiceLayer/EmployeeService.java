package ServiceLayer;

import DomainLayer.EmpSubModule.EmployeeFacade;

import java.util.ArrayList;

public class EmployeeService {
    private EmployeeFacade ef;

    public EmployeeService(EmployeeFacade ef) {
        this.ef = ef;
    }


    public void  setPermissions(int empid, int permieeionsNum){}
    public void addEmployee(int empid, String fname, String lname, int permieeionsNum){}
    public void addDriver(int empid, String fname, String lname, int permieeionsNum, ArrayList<String> licenses){}
    public void addLicense(int empid, String License){}
    public void removeLicense(int empid, String License){}


    public String showEmployees(){
        String res = "";
        try {
            res = ef.showEmployees();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }


    public String showDrivers(){
        String res = "";
        try {
            res = ef.showDrivers();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }


    public String employeeToString(){   //TODO : seems like this function is not needed
        //todo
        return "";
    }

}
