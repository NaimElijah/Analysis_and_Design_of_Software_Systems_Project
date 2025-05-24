package DomainLayer;

import DomainLayer.EmployeeSubModule.*;
import DomainLayer.EmployeeSubModule.Repository.*;
import DomainLayer.TransportDomain.SiteSubModule.SiteFacade;
import DomainLayer.TransportDomain.TransportSubModule.TransportController;
import DomainLayer.TransportDomain.TruckSubModule.TruckFacade;
import ServiceLayer.*;
import ServiceLayer.EmployeeSubModule.EmployeeService;
import ServiceLayer.EmployeeSubModule.ShiftService;
import ServiceLayer.TransportServices.*;
import PresentationLayer.EmployeeSubModule.HR_MainCLI;
import PresentationLayer.TransportPresentation.MainTranSysCLI;
import PresentationLayer.TransportPresentation.TranManCLI;

import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Factory class for creating and initializing system components.
 * Implements the Factory Pattern to encapsulate the creation logic.
 */
public class SystemFactory {
    public SystemFactory() {
        // No initialization needed
    }

    /**
     * Creates and initializes the Employee Module components.
     * @param minimalMode If true, initializes with minimal data (admin only).
     * @return EmployeeModuleComponents containing all initialized components.
     * @throws IOException If there's an error loading data.
     */
    public EmployeeModuleComponents createEmployeeModule(boolean minimalMode) throws IOException {
        // Initialize repositories
        AuthorisationRepository authorisationRepository = new AuthorisationRepositoryImpl();
        BranchRepository branchRepository = new BranchRepositoryImpl();
        EmployeeRepository employeeRepository = new EmployeeRepositoryImpl();

        // Initialize controllers with repositories
        AuthorisationController authController = new AuthorisationController(
            authorisationRepository
        );

        EmployeeController employeeController = new EmployeeController(
            employeeRepository,
            branchRepository,
            authorisationRepository,
            authController
        );

        // Initialize ShiftController with an empty set of shifts for now
        // In the future, this should be replaced with a ShiftRepository
        ShiftController shiftController = new ShiftController(
            new HashSet<>(),
            authController,
            employeeController
        );

        AssignmentController assignmentController = new AssignmentController(employeeController, shiftController);
        AvailabilityController availabilityController = new AvailabilityController(employeeController, shiftController);

        EmployeeService employeeService = new EmployeeService(employeeController, authController);
        ShiftService shiftService = new ShiftService(shiftController, assignmentController, availabilityController);

        return new EmployeeModuleComponents(
            authController, 
            employeeController, 
            shiftController, 
            assignmentController, 
            availabilityController, 
            employeeService, 
            shiftService
        );
    }

    /**
     * Creates and initializes the Transport Module components.
     * @param employeeComponents The EmployeeModuleComponents to use for integration.
     * @return TransportModuleComponents containing all initialized components.
     */
    public TransportModuleComponents createTransportModule(EmployeeModuleComponents employeeComponents) {
        // Initialize repositories
        //TODO           <<<-------------------------     <<---------------------------------

        // Initialize facades
        TruckFacade truckFacade = new TruckFacade();
        SiteFacade siteFacade = new SiteFacade();

        // Initialize controllers
        TransportController transportController = new TransportController(siteFacade, truckFacade);

        // Create EmployeeIntegrationService
        EmployeeIntegrationService employeeIntegrationService = new EmployeeIntegrationService(
            employeeComponents.getEmployeeService(),
            employeeComponents.getShiftService()
        );

        // Initialize services
        TruckService truckService = new TruckService(truckFacade, employeeIntegrationService);
        TransportService transportService = new TransportService(transportController, employeeIntegrationService);
        SiteService siteService = new SiteService(siteFacade, employeeIntegrationService);

        // Initialize startup service
        StartUpStateService startUpService = new StartUpStateService(transportService, truckService, siteService);  // if this specific one needed

        return new TransportModuleComponents(
            truckFacade,
            siteFacade,
            transportController,
            truckService,
            transportService,
            siteService,
            startUpService,
            employeeIntegrationService
        );
    }

    /**
     * Creates a MainCLI for the Employee Module.
     * @param employeeService The EmployeeService to use.
     * @param shiftService The ShiftService to use.
     * @param loginId The ID of the logged-in employee.
     * @return A MainCLI instance.
     */
    public HR_MainCLI createEmployeeCLI(EmployeeService employeeService, ShiftService shiftService, long loginId) {
        return new HR_MainCLI(employeeService, shiftService, loginId);
    }

    /**
     * Creates a TranManCLI for the Transport Module.
     * @param components The TransportModuleComponents to use.
     * @param employeeComponents The EmployeeModuleComponents to use.
     * @return A TranManCLI instance.
     */
    public TranManCLI createTransportCLI(TransportModuleComponents components, EmployeeModuleComponents employeeComponents) {
        // Create MainTranSysCLI
        MainTranSysCLI mainTranSysCLI = new MainTranSysCLI(
            components.getTruckService(),
            components.getTransportService(),
            components.getSiteService(),
            components.getEmployeeIntegrationService(),
            components.getStartUpService()
        );

        // Create Scanner
        Scanner scanner = new Scanner(System.in);

        return new TranManCLI(
            mainTranSysCLI,
            components.getTruckService(),
            components.getTransportService(),
            components.getSiteService(),
            employeeComponents.getEmployeeService(),
            components.getEmployeeIntegrationService(),
            scanner
        );
    }

    /**
     * Container class for Employee Module components.
     */
    public static class EmployeeModuleComponents {
        private final AuthorisationController authController;
        private final EmployeeController employeeController;
        private final ShiftController shiftController;
        private final AssignmentController assignmentController;
        private final AvailabilityController availabilityController;
        private final EmployeeService employeeService;
        private final ShiftService shiftService;

        public EmployeeModuleComponents(
                AuthorisationController authController,
                EmployeeController employeeController,
                ShiftController shiftController,
                AssignmentController assignmentController,
                AvailabilityController availabilityController,
                EmployeeService employeeService,
                ShiftService shiftService) {
            this.authController = authController;
            this.employeeController = employeeController;
            this.shiftController = shiftController;
            this.assignmentController = assignmentController;
            this.availabilityController = availabilityController;
            this.employeeService = employeeService;
            this.shiftService = shiftService;
        }

        public AuthorisationController getAuthController() {
            return authController;
        }

        public EmployeeController getEmployeeController() {
            return employeeController;
        }

        public ShiftController getShiftController() {
            return shiftController;
        }

        public AssignmentController getAssignmentController() {
            return assignmentController;
        }

        public AvailabilityController getAvailabilityController() {
            return availabilityController;
        }

        public EmployeeService getEmployeeService() {
            return employeeService;
        }

        public ShiftService getShiftService() {
            return shiftService;
        }
    }

    /**
     * Container class for Transport Module components.
     */
    public static class TransportModuleComponents {
        private final TruckFacade truckFacade;
        private final SiteFacade siteFacade;
        private final TransportController transportController;
        private final TruckService truckService;
        private final TransportService transportService;
        private final SiteService siteService;
        private final StartUpStateService startUpService;
        private final EmployeeIntegrationService employeeIntegrationService;

        public TransportModuleComponents(
                TruckFacade truckFacade,
                SiteFacade siteFacade,
                TransportController transportController,
                TruckService truckService,
                TransportService transportService,
                SiteService siteService,
                StartUpStateService startUpService,
                EmployeeIntegrationService employeeIntegrationService) {
            this.truckFacade = truckFacade;
            this.siteFacade = siteFacade;
            this.transportController = transportController;
            this.truckService = truckService;
            this.transportService = transportService;
            this.siteService = siteService;
            this.startUpService = startUpService;
            this.employeeIntegrationService = employeeIntegrationService;
        }

        public TruckFacade getTruckFacade() {
            return truckFacade;
        }

        public SiteFacade getSiteFacade() {
            return siteFacade;
        }

        public TransportController getTransportController() {
            return transportController;
        }

        public TruckService getTruckService() {
            return truckService;
        }

        public TransportService getTransportService() {
            return transportService;
        }

        public SiteService getSiteService() {
            return siteService;
        }

        public StartUpStateService getStartUpService() {
            return startUpService;
        }

        public EmployeeIntegrationService getEmployeeIntegrationService() {
            return employeeIntegrationService;
        }
    }
}
