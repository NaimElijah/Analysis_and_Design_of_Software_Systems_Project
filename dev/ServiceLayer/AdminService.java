package ServiceLayer;

import DomainLayer.TransportFacade;

public class AdminService {
    private TransportFacade tf;

    public AdminService() {
        tf = new TransportFacade();
    }
}
