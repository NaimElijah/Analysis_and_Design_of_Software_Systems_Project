//package DomainLayer.EmpSubModule;
//
////import DomainLayer.Driver;
//import DomainLayer.enums.enumPermissionRank;
//import DomainLayer.enums.enumDriLicense;
//
//import javax.management.openmbean.KeyAlreadyExistsException;
//import javax.xml.transform.TransformerException;
//import java.io.FileNotFoundException;
//import java.util.ArrayList;
//import java.util.HashMap;
////TODO:      LATER DELETE  THIS FILE, THERE IS A NEW EMPLOYEE CONTROLLER       <<<---------------------
//TODO : THIS IS JUST HERE SO I CAN SEE THAT I DIDN'T MISS ANYTHING AND THEN DELETE THIS      <<<------------------------
//public class EmployeeFacade {
//    private HashMap<Integer, Employee> employees;
//
//    public EmployeeFacade() { employees = new HashMap<>(); }
//
//    public HashMap<Integer, Employee> getEmployees() {return employees;}
//    public void setEmployees(HashMap<Integer, Employee> employees) {this.employees = employees;}
//
////    public HashMap<Integer, Driver> getDrivers() {
////        HashMap<Integer, Driver> drivers = new HashMap<>();
////        for (Employee employee : employees.values()) {
////            if (employee instanceof Driver) {
////                Driver driver = (Driver) employee;
////                drivers.put(driver.getId(), driver);
////            }
////        }
////        return drivers;
////    }
//
//
////    public void addDriver(int empId, String fname, String lname, ArrayList<String> licenses) throws KeyAlreadyExistsException {
////        if (this.employees.containsKey(empId)){
////            throw new KeyAlreadyExistsException("The Employee Id you are trying to add already exists");
////        }
////        // converting the String to enumDriLicense:
////        ArrayList<enumDriLicense> drivingLicenses = new ArrayList<>();
////        for (String s : licenses) {
////            if (s.equals("A") && (!drivingLicenses.contains(enumDriLicense.A))) {
////                drivingLicenses.add(enumDriLicense.A);
////            } else if (s.equals("B") && (!drivingLicenses.contains(enumDriLicense.B))) {
////                drivingLicenses.add(enumDriLicense.B);
////            } else if (s.equals("C") && (!drivingLicenses.contains(enumDriLicense.C))) {
////                drivingLicenses.add(enumDriLicense.C);
////            } else if (s.equals("D") && (!drivingLicenses.contains(enumDriLicense.D))) {
////                drivingLicenses.add(enumDriLicense.D);
////            } else if (s.equals("E") && (!drivingLicenses.contains(enumDriLicense.E))) {
////                drivingLicenses.add(enumDriLicense.E);
////            }
////        }
////        this.employees.put(empId, new Driver(empId, fname, lname, enumPermissionRank.Driver, drivingLicenses));
////    }
//
//
//
////    public void addManager(int empId, String fname, String lname) throws KeyAlreadyExistsException {
////        if (this.employees.containsKey(empId)){
////            throw new KeyAlreadyExistsException("The Employee Id you are trying to add already exists");
////        }
////        this.employees.put(empId, new Employee(empId, fname, lname, enumPermissionRank.Manager));
////    }
//
//
//
////    public void initializeAdmin(int empId, String fname, String lname) throws KeyAlreadyExistsException, IllegalAccessException {
////        if (this.employees.containsKey(empId)){ throw new KeyAlreadyExistsException("The Employee Id you are trying to add as Admin already exists"); }
////        boolean adminExists = false;
////        for (Employee employee : employees.values()) {
////            if(employee.getPermissions_rank() == enumPermissionRank.Admin){
////                adminExists = true;
////                break;
////            }
////        }
////        if(!adminExists){
////            this.employees.put(empId, new Employee(empId, fname, lname, enumPermissionRank.Admin));
////        }else {
////            throw new IllegalAccessException("Cannot create another Admin, only one Admin Exists.");
////        }
////    }
//
//
//
//
//
//
//
//
//
//
//
//
////    public void removeEmployeeByAdmin(int empId) throws ClassNotFoundException, AssertionError, ArrayStoreException, ArithmeticException {   /// for Admin usage
////        if (!this.employees.containsKey(empId)) {
////            throw new ClassNotFoundException("The Employee Id you've entered doesn't exist");
////
////        } else if (employees.get(empId).getPermissions_rank().equals(enumPermissionRank.Admin)) {
////            throw new AssertionError("Can't delete Admin. (can't delete yourself)");
////
////        } else if (employees.get(empId) instanceof Driver) {  // if it's a Driver that is written in an Active Transport
////            if (((Driver)employees.get(empId)).getInTransportID() != -1){
////                throw new ArrayStoreException("Driver cannot be deleted because he is written in an Active Transport, if you want you can remove him from his transports or remove the Transports");
////            }
////
////        } else if (employees.get(empId).getPermissions_rank().equals(enumPermissionRank.Manager)) {
////            int amountOfManagers = 0;
////            for (Employee employee : employees.values()) {
////                if (employee.getPermissions_rank().equals(enumPermissionRank.Manager)) {
////                    amountOfManagers++;
////                }
////            }
////            if (amountOfManagers <= 1){
////                throw new ArithmeticException("Can't delete Manager because after deleting this Manager, there won't be any Managers left :(");
////            }
////        }
////
////        this.employees.get(empId).setIsDeleted(true);
////        this.employees.remove(empId);  // if all good
////    }
//
//
////    public void removeEmployeeByManager(int empId) throws ClassNotFoundException, AssertionError, ArrayStoreException {   /// for Manager usage
////        if (!this.employees.containsKey(empId)) {
////            throw new ClassNotFoundException("The Employee Id you've entered doesn't exist");
////        } else if (employees.get(empId).getPermissions_rank().equals(enumPermissionRank.Admin) || employees.get(empId).getPermissions_rank().equals(enumPermissionRank.Manager)) {
////            throw new AssertionError("Can't delete Admin or another Manager.");
////        } else if (employees.get(empId) instanceof Driver) {  // if it's a Driver that is written in an Active Transport
////            if (((Driver)employees.get(empId)).getInTransportID() != -1){
////                throw new ArrayStoreException("Driver cannot be deleted because he is written in an Active Transport, if you want you can remove him from his transports or remove the Transports");
////            }
////        }
////
////        this.employees.get(empId).setIsDeleted(true);
////        this.employees.remove(empId);
////    }
//
//
//
//
//
//
//
//
//
//
//
//
////    public void giveADriverAManagersPermissionRank(int empId) throws ClassNotFoundException, AssertionError, TransformerException {
////        if (!this.employees.containsKey(empId)) {
////            throw new ClassNotFoundException("The Employee Id you've entered doesn't exist");
////        } else if (employees.get(empId).getPermissions_rank().equals(enumPermissionRank.Manager)) {
////            throw new AssertionError("The Employee Already has a Manager's Permission Rank");
////        } else if (!(employees.get(empId) instanceof Driver)) {
////            throw new TransformerException("Cannot give a Driver, a Manager's Permission Rank if that person is not a Driver to begin with");
////        }
////        this.employees.get(empId).setPermissions_rank(enumPermissionRank.Manager);
////    }    //TODO:   see if this can be done using the new EmployeeController
//
//
//
//
//
//
//
//
//
//
////    public int getEmployeePermissionsRank(int loginIDGiven) {
////        int res = -1;
////        if (!this.employees.containsKey(loginIDGiven)) {
////            res = -1;  //  ID not in system
////        } else if (employees.get(loginIDGiven).getPermissions_rank().equals(enumPermissionRank.Admin)) {
////            res = 0;
////        } else if (employees.get(loginIDGiven).getPermissions_rank().equals(enumPermissionRank.Manager)) {
////            res = 1;
////        } else if (employees.get(loginIDGiven).getPermissions_rank().equals(enumPermissionRank.Driver)) {
////            res = 2;
////        }
////        return res;
////    }
//
//
//
//
//
//
//
//
//
//
//
//
////    public void addLicense(int empid, String License) throws ClassNotFoundException, FileNotFoundException, ArrayStoreException {
////        if (!this.employees.containsKey(empid)) {
////            throw new ClassNotFoundException("The Employee Id you've entered doesn't exist");
////        } else if (!(this.employees.get(empid) instanceof Driver)) {
////            throw new FileNotFoundException("You can't add a driving license in the system to an employee that isn't a Driver");
////        }
////
////        // converting the String to enumDriLicense:
////        enumDriLicense drivingLicense = null;
////        if (License.equals("A")) {
////            drivingLicense = enumDriLicense.A;
////        } else if (License.equals("B")) {
////            drivingLicense = enumDriLicense.B;
////        } else if (License.equals("C")) {
////            drivingLicense = enumDriLicense.C;
////        } else if (License.equals("D")) {
////            drivingLicense = enumDriLicense.D;
////        } else if (License.equals("E")) {
////            drivingLicense = enumDriLicense.E;
////        }
////
////        if (((Driver) this.employees.get(empid)).getLicenses().contains(drivingLicense)) {
////            throw new ArrayStoreException("The Driver already has that License");
////        }
////
////        ((Driver) this.employees.get(empid)).addLicense(drivingLicense);
////    }    //TODO:  now giving a driving license means adding a role(DriverA, DriverB, ...)
//
//
//
////    public void removeLicense(int empid, String License) throws ClassNotFoundException, FileNotFoundException, ArrayStoreException, ArithmeticException {
////        if (!this.employees.containsKey(empid)) {
////            throw new ClassNotFoundException("The Employee Id you've entered doesn't exist");
////        } else if (!(this.employees.get(empid) instanceof Driver)) {
////            throw new FileNotFoundException("You can't remove a driving license in the system to an employee that isn't a Driver");
////        } else if (((Driver) this.employees.get(empid)).getInTransportID() != -1) {
////            throw new ArithmeticException("Cannot remove the Driver's License because he is currently in an active Transport.");
////        }
////
////        // converting the String to enumDriLicense:
////        enumDriLicense drivingLicense = null;
////        if (License.equals("A")) {
////            drivingLicense = enumDriLicense.A;
////        } else if (License.equals("B")) {
////            drivingLicense = enumDriLicense.B;
////        } else if (License.equals("C")) {
////            drivingLicense = enumDriLicense.C;
////        } else if (License.equals("D")) {
////            drivingLicense = enumDriLicense.D;
////        } else if (License.equals("E")) {
////            drivingLicense = enumDriLicense.E;
////        }
////
////        if (!(((Driver) this.employees.get(empid)).getLicenses().contains(drivingLicense))) {
////            throw new ArrayStoreException("The Driver already doesn't have that License");
////        }
////
////        ((Driver) this.employees.get(empid)).removeLicense(drivingLicense);
////    }    //TODO:  now removing a driving license means removing a role(DriverA, DriverB, ...)
//
//
//
//
//
//
////    public String showEmployee(int id) throws ArrayStoreException {
////        if (!this.employees.containsKey(id)) { throw new ArrayStoreException("The Employee(ID) you want to show, doesn't exist"); }
////        String res = "";
////        for (Employee emp : this.employees.values()) {
////            if (emp.getId() == id) {
////                res = emp.toString();
////                break; // because this function is to show 1 employee
////            }
////        }
////        return res;
////    }
////
////
////    public String showEmployees() {
////        String res = "Showing all Employees:\n";
////        for (Employee employee : employees.values()) {
////            res += employee.toString() + "\n";
////        }
////        return res;
////    }
////
////    public String showDrivers(){
////        HashMap<Integer, Driver> drivers = this.getDrivers();
////        String res = "Showing All Drivers:\n";
////        for (Driver driver : drivers.values()) {
////            res += driver.toString() + "\n";
////        }
////        return res;
////    }
////
////    public String showManagers() {
////        String res = "Showing all Managers:\n";
////        for (Employee employee : employees.values()) {
////            if (employee.getPermissions_rank().equals(enumPermissionRank.Manager)) {
////                res += employee.toString() + "\n";
////            }
////        }
////        return res;
////    }
//
//
//}
