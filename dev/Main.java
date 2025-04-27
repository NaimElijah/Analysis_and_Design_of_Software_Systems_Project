import DomainLayer.EmpSubModule.EmployeeFacade;
import DomainLayer.SiteSubModule.SiteFacade;
import DomainLayer.TranSubModule.TransportFacade;
import DomainLayer.TruSubModule.TruckFacade;
import PresentationLayer.MainTranSysController;
import ServiceLayer.*;

public class Main {
   public static void main(String[] args) {

      TruckFacade tru_f = new TruckFacade();
      EmployeeFacade eff = new EmployeeFacade();
      SiteFacade sf = new SiteFacade();
      TransportFacade tran_f = new TransportFacade(eff, sf, tru_f);

      TransportService tran_s = new TransportService(tran_f);
      TruckService tru_s = new TruckService(tru_f);
      EmployeeService es = new EmployeeService(eff);
      SiteService site_s = new SiteService(sf);

      StartUpStateService start = new StartUpStateService(tran_s, tru_s, es, site_s);

      MainTranSysController mtsc = new MainTranSysController(tru_s, tran_s, site_s, es, start);

      mtsc.transportModuleStartup();      ///         <<<-----------------------   starts the whole Transport Module System

   }
}
