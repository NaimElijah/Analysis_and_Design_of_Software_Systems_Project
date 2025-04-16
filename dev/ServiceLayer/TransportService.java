package ServiceLayer;

import DomainLayer.Facades.TransportFacade;
import com.fasterxml.jackson.*;

public class TransportService {
    private TransportFacade tran_f;

    public TransportService(TransportFacade tf) {
        this.tran_f = tf;
    }

    //TODO
}
