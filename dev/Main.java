import DomainLayer.EmpSubModule.EmployeeFacade;
import DomainLayer.SiteSubModule.SiteFacade;
import DomainLayer.TranSubModule.TransportFacade;
import DomainLayer.TruSubModule.TruckFacade;
import PresentationLayer.TransportSystemController;
import ServiceLayer.EmployeeService;
import ServiceLayer.TransportService;
import ServiceLayer.SiteService;
import ServiceLayer.TruckService;

public class Main {
   public static void main(String[] args) {

      TransportFacade tran_f = new TransportFacade();
      TruckFacade tru_f = new TruckFacade();
      EmployeeFacade eff = new EmployeeFacade();
      SiteFacade sf = new SiteFacade();

      TransportService tran_s = new TransportService(tran_f);
      TruckService tru_s = new TruckService(tru_f);
      EmployeeService es = new EmployeeService(eff);
      SiteService site_s = new SiteService(sf);

      TransportSystemController tsc = new TransportSystemController(tru_s, tran_s, site_s, es);

      tsc.transportModuleStartup();      //         <<<-----------------------   starts the whole system/program

   }
}
