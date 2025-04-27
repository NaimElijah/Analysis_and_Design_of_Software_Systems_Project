package ServiceLayer;

import DomainLayer.EmpSubModule.EmployeeFacade;

import javax.management.openmbean.KeyAlreadyExistsException;
import javax.xml.transform.TransformerException;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class EmployeeService {
    private EmployeeFacade ef;
    public EmployeeService(EmployeeFacade ef) {
        this.ef = ef;
    }


    public String addManager(int empid, String fname, String lname){
        if (empid < 0 || fname.isEmpty() || lname.isEmpty() || fname.isBlank() || lname.isBlank()) {
            return "Manager Details cannot be negative or empty or blank";
        }
        try {
            ef.addManager(empid, fname, lname);
        } catch (KeyAlreadyExistsException e) {
            return "The Employee Id you are trying to add already exists";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";
    }

    public String addDriver(int empid, String fname, String lname, ArrayList<String> licenses){
        if (empid < 0 || fname.isEmpty() || lname.isEmpty() || fname.isBlank() || lname.isBlank()) {
            return "Driver Details cannot be negative or empty or blank";
        }
        for (String license : licenses) {
            if (!(license.equals("A") || license.equals("B") || license.equals("C") || license.equals("D") || license.equals("E"))) {
                return "Each License must be one of: A,B,C,D,E.";
            }
        }
        try {
            ef.addDriver(empid, fname, lname, licenses);
        } catch (KeyAlreadyExistsException e) {
            return "The Employee Id you are trying to add already exists";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";
    }



    public String initializeAdmin(int empId, String fname, String lname) throws KeyAlreadyExistsException, IllegalAccessException {
        if (empId < 0 || fname.isEmpty() || lname.isEmpty() || fname.isBlank() || lname.isBlank()) { return "Manager Details cannot be negative or empty or blank"; }
        try {
            ef.initializeAdmin(empId, fname, lname);
        } catch (KeyAlreadyExistsException e) {
            return "The Employee Id you are trying to add as Admin already exists";
        } catch (IllegalAccessException e) {
            return "Cannot create another Admin, only one Admin Exists.";
        }catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";
    }














    public String removeEmployeeByAdmin(int empId) {   /// for Admin usage
        if (empId < 0) { return "Employee ID cannot be negative"; }
        try {
            ef.removeEmployeeByAdmin(empId);
        } catch (ClassNotFoundException e) {
            return "The Employee Id you've entered doesn't exist";
        } catch (AssertionError e) {
            return "Can't delete Admin. (can't delete yourself)";
        } catch (ArrayStoreException e) {
            return "Driver cannot be deleted because he is written in an Active Transport, if you want you can remove the Transport or remove him from his transports";
        } catch (ArithmeticException e) {
            return "Can't delete Manager because after deleting this Manager, there won't be any Managers left :(";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";
    }


    public String removeEmployeeByManager(int empId){   /// for Manager usage
        if (empId < 0) { return "Employee ID cannot be negative"; }
        try {
            ef.removeEmployeeByManager(empId);
        } catch (ClassNotFoundException e) {
            return "The Employee Id you've entered doesn't exist";
        } catch (AssertionError e) {
            return "Can't delete Admin or another Manager.";
        } catch (ArrayStoreException e) {
            return "Driver cannot be deleted because he is written in an Active Transport, if you want you can remove the Transport or remove him from his transports";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";
    }
















    public String giveADriverAManagersPermissionRank(int empid){
        if (empid < 0) { return "Employee ID cannot be negative"; }
        try {
            ef.giveADriverAManagersPermissionRank(empid);
        } catch (ClassNotFoundException e) {
            return "The Employee Id you've entered doesn't exist";
        } catch (AssertionError e) {
            return "The Employee Already has a Manager's Permission Rank";
        } catch (TransformerException e) {
            return "Cannot give a Driver, a Manager's Permission Rank if that person is not a Driver to begin with";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";
    }











    public int getEmployeePermissionsRank(int loginIDGiven) {
        int res = 0;
        if (loginIDGiven < 0) { return -1; }
        try {
            res = ef.getEmployeePermissionsRank(loginIDGiven);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return res;
    }















    public String addLicense(int empid, String license){
        if (empid < 0) { return "Employee ID cannot be negative"; }
        if (!(license.equals("A") || license.equals("B") || license.equals("C") || license.equals("D") || license.equals("E"))) {
            return "License can only be one of: A, B, C, D, E.";
        }
        try {
            ef.addLicense(empid, license);
        } catch (ClassNotFoundException e) {
            return "The Employee Id you've entered doesn't exist";
        } catch (FileNotFoundException e) {
            return "You can't add a driving license in the system to an employee that isn't a Driver";
        } catch (ArrayStoreException e) {
            return "The Driver already has that License";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";
    }


    public String removeLicense(int empid, String license){
        if (empid < 0) { return "Employee ID cannot be negative"; }
        if (!(license.equals("A") || license.equals("B") || license.equals("C") || license.equals("D") || license.equals("E"))) {
            return "License can only be one of: A, B, C, D, E.";
        }
        try {
            ef.removeLicense(empid, license);
        } catch (ClassNotFoundException e) {
            return "The Employee Id you've entered doesn't exist";
        } catch (FileNotFoundException e) {
            return "You can't remove a driving license in the system to an employee that isn't a Driver";
        } catch (ArrayStoreException e) {
            return "The Driver already doesn't have that License";
        } catch (ArithmeticException e) {
            return "Cannot remove the Driver's License because he is currently in an active Transport.";
        }catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";
    }







    public String showEmployee(int id) {
        if (id < 0){ return "ID cannot be negative"; }
        String res = "";
        try {
            res = ef.showEmployee(id);
        } catch (ArrayStoreException e) {
            return "The Employee(ID) you want to show, doesn't exist";
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


    public String showEmployees(){
        String res = "";
        try {
            res = ef.showEmployees();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }



    public String showManagers(){
        String res = "";
        try {
            res = ef.showManagers();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }


}
