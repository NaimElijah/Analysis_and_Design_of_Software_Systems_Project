package ServiceLayer;

import DomainLayer.TransportFacade;

public class ManagerService {
    private TransportFacade tf;

    public ManagerService() {
        tf = new TransportFacade();
    }


}
