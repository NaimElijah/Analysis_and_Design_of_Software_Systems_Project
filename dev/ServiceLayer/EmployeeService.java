package ServiceLayer;

import DomainLayer.Facades.EmployeeFacade;
import com.fasterxml.jackson.*;

public class EmployeeService {
    private EmployeeFacade ef;

    public EmployeeService(EmployeeFacade ef) {
        this.ef = ef;
    }

    //TODO
}
