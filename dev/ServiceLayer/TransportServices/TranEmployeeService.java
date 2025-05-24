





//TODO:       putting this here so that I remember to make sure these functionalities happen/ to use the EmployeeService/EmployeeIntegrationService
//TODO:       putting this here so that I remember to make sure these functionalities happen/ to use the EmployeeService/EmployeeIntegrationService
//TODO:       putting this here so that I remember to make sure these functionalities happen/ to use the EmployeeService/EmployeeIntegrationService
//TODO:       putting this here so that I remember to make sure these functionalities happen/ to use the EmployeeService/EmployeeIntegrationService
//TODO:       putting this here so that I remember to make sure these functionalities happen/ to use the EmployeeService/EmployeeIntegrationService
//TODO:       putting this here so that I remember to make sure these functionalities happen/ to use the EmployeeService/EmployeeIntegrationService
//TODO:       putting this here so that I remember to make sure these functionalities happen/ to use the EmployeeService/EmployeeIntegrationService
//TODO:       putting this here so that I remember to make sure these functionalities happen/ to use the EmployeeService/EmployeeIntegrationService
//TODO:       putting this here so that I remember to make sure these functionalities happen/ to use the EmployeeService/EmployeeIntegrationService
//TODO:       putting this here so that I remember to make sure these functionalities happen/ to use the EmployeeService/EmployeeIntegrationService
//TODO:       putting this here so that I remember to make sure these functionalities happen/ to use the EmployeeService/EmployeeIntegrationService
///   left only functions as MEMOs for me to see that we implement/redirect these functionalities in the correct places.

//    public String giveADriverAManagersPermissionRank(int empid){  //TODO:     <<<-----------   make sure that Transport Manager can give a Driver a Transport Manager's Role
//        if (empid < 0) { return "Employee ID cannot be negative"; }
//        try {
//            ef.giveADriverAManagersPermissionRank(empid);
//        } catch (ClassNotFoundException e) {
//            return "The Employee Id you've entered doesn't exist";
//        } catch (AssertionError e) {
//            return "The Employee Already has a Manager's Permission Rank";
//        } catch (TransformerException e) {
//            return "Cannot give a Driver, a Manager's Permission Rank if that person is not a Driver to begin with";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Exception";
//        }
//        return "Success";
//    }
//
//
//
//    public String addLicense(int empid, String license){  //TODO:     <<<-----------   make sure that Transport Manager can give a Driver a Transport Manager's Role
//        if (empid < 0) { return "Employee ID cannot be negative"; }
//        if (!(license.equals("A") || license.equals("B") || license.equals("C") || license.equals("D") || license.equals("E"))) {
//            return "License can only be one of: A, B, C, D, E.";
//        }
//        try {
//            ef.addLicense(empid, license);
//        } catch (ClassNotFoundException e) {
//            return "The Employee Id you've entered doesn't exist";
//        } catch (FileNotFoundException e) {
//            return "You can't add a driving license in the system to an employee that isn't a Driver";
//        } catch (ArrayStoreException e) {
//            return "The Driver already has that License";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Exception";
//        }
//        return "Success";
//    }
//
//
//    public String removeLicense(int empid, String license){  //TODO:     <<<-----------   make sure that Transport Manager can give a Driver a Transport Manager's Role
//        if (empid < 0) { return "Employee ID cannot be negative"; }
//        if (!(license.equals("A") || license.equals("B") || license.equals("C") || license.equals("D") || license.equals("E"))) {
//            return "License can only be one of: A, B, C, D, E.";
//        }
//        try {
//            ef.removeLicense(empid, license);
//        } catch (ClassNotFoundException e) {
//            return "The Employee Id you've entered doesn't exist";
//        } catch (FileNotFoundException e) {
//            return "You can't remove a driving license in the system to an employee that isn't a Driver";
//        } catch (ArrayStoreException e) {
//            return "The Driver already doesn't have that License";
//        } catch (ArithmeticException e) {
//            return "Cannot remove the Driver's License because he is currently in an active Transport.";
//        }catch (Exception e) {
//            e.printStackTrace();
//            return "Exception";
//        }
//        return "Success";
//    }
//
//
//
//
//}
