package DomainLayer.EmployeeSubModule;

import DTOs.ShiftDTO;
import DomainLayer.enums.ShiftType;
import DomainLayer.exception.InvalidInputException;
import DomainLayer.exception.UnauthorizedPermissionException;
import Util.Week;
import Util.config;


import java.time.DayOfWeek;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.stream.Collectors;

public class ShiftController {
    private final Set<Shift> shifts;
    private final Map<Week, Set<Shift>> weeklyShifts = new TreeMap<>();
    //private final AuthorisationController authorizationController;
    private final EmployeeController empCon;
    private long shiftIdCounter = 1;

    // Magic Number
    private final String ShiftManagerStr = "Shift Manager";

    public ShiftController(Set<Shift> shifts, AuthorisationController authorizationController, EmployeeController employeeController) {
        this.shifts = shifts;
        //this.authorizationController = authorizationController;
        this.empCon = employeeController;
        addShiftsToWeeklyShifts(shifts);
    }

    /**
     * Serialize a single shift to a string format.
     * Shift -> ShiftDTO -> String
     * @param shift - the shift to serialize
     * @return the serialized string representation of the shift
     */
    public String serializeShift(Shift shift) {
        if (shift == null) {
            throw new IllegalArgumentException("Shift cannot be null");
        }
        ShiftDTO dto = new ShiftDTO(shift.getId(), shift.getShiftType(), shift.getShiftDate(), shift.getRolesRequired(), shift.getAssignedEmployees(), shift.getAvailableEmployees(), shift.isAssignedShiftManager(), shift.isOpen(), shift.getStartHour(), shift.getEndHour(), shift.getCreateDate(), shift.getUpdateDate());
        return dto.serialize();
    }

    /**
     * Serialize a set of shifts to a string format.
     * Set<Shift> -> Set<ShiftDTO> -> String
     * Each shift is serialized to a new line.
     * @param shifts - the set of shifts to serialize
     * @return the serialized string representation of the shifts
     */
    public String serializeSetShifts(Set<Shift> shifts) {
        StringBuilder sb = new StringBuilder();
        for (Shift shift : shifts) {
            String serializedShift = serializeShift(shift);
            sb.append(serializedShift).append("\n");
        }
        return sb.toString();
    }
    /**
     * Serialize a set of shifts to a string format.
     * The shifts are sorted by date and then by type.
     * List<Shift> -> List<ShiftDTO> -> String
     * Each shift is serialized to a new line.
     * @param shifts - the set of shifts to serialize
     * @return the serialized string representation of the shifts
     */
    public String serializeArrayShifts(List<Shift> shifts) {
        // Sort the list by date and then by type
        shifts = shifts.stream()
                .sorted(Comparator.comparing(Shift::getShiftDate).thenComparing(Shift::getShiftType))
                .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        for (Shift shift : shifts) {
            String serializedShift = serializeShift(shift);
            sb.append(serializedShift).append("\n");
        }
        return sb.toString();
    }
    public String deserializeShift(String serializedShift) {
        if (serializedShift == null || serializedShift.isEmpty()) {
            throw new IllegalArgumentException("Serialized shift cannot be null or empty");
        }
        ShiftDTO dto = ShiftDTO.deserialize(serializedShift);
        return dto.toString();
    }

    /**
     * Deserialize a string representation of shifts to a list of Shift objects.
     * Each line in the string represents a serialized ShiftDTO.
     * String -> ShiftDTO -> Shift
     * @param serializedShifts - the serialized shifts as String
     * @return a list of Shift objects
     */
    public List<Shift> deserializeArrayShifts(String serializedShifts) {
        if (serializedShifts == null || serializedShifts.isEmpty()) {
            throw new IllegalArgumentException("Serialized shifts cannot be null or empty");
        }
        String[] serializedShiftArray = serializedShifts.split("\n");
        List<Shift> shiftsList = new ArrayList<>();
        for (String serializedShift : serializedShiftArray) {
            ShiftDTO dto = ShiftDTO.deserialize(serializedShift);
            Shift shift = new Shift(dto.getId(), dto.getShiftType(), dto.getShiftDate(), dto.getRolesRequired(), dto.getAssignedEmployees(), dto.getAvailableEmployees(), dto.isAssignedShiftManager(), dto.isOpen(),dto.getStartHour(),dto.getEndHour(),dto.getUpdateDate());
            shiftsList.add(shift);
        }
        return shiftsList;
    }

    protected Shift getShiftByIdAsShift(long doneBy, long shiftId) {
        String PERMISSION_REQUIRED = "GET_SHIFT";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to get shift");
        }
        if (shiftId <= 0) {
            throw new IllegalArgumentException("Shift ID must be a positive number");
        }
        return shifts.stream().filter(shift -> shift.getId() == shiftId).findFirst().orElse(null);
    }

    /**
     * add a shift to the system
     * @param doneBy                 employee who is creating the shift
     * @param shiftType              morning or evening
     * @param date                   date of the shift
     * @param rolesRequired          number of employees of each role required for the shift
     * @param assignedEmployees      employees assigned to the shift
     * @param isAssignedShiftManager true if the shift has a shift manager assigned, false otherwise
     * @param isOpen                 true if the place is open
     * @param startHour              the hours of the shift
     * @param endHour                the end hour of the shift
     * @param updateDate             the date of the last update
     * @return true if the shift was created successfully, false otherwise
     */
    public boolean createShift(long doneBy,ShiftType shiftType, LocalDate date, Map<String, Integer> rolesRequired, Map<String, Set<Long>> assignedEmployees, Set<Long> availableEmployees, boolean isAssignedShiftManager, boolean isOpen,LocalTime startHour , LocalTime endHour ,LocalDate updateDate) {
        String PERMISSION_REQUIRED = "CREATE_SHIFT";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to create shift");
        }
        if (shifts.stream().anyMatch(s -> s.getShiftDate().equals(date) && s.getShiftType().equals(shiftType))) {
            throw new RuntimeException("Shift already exists");
        }
        if (shiftType == null || date == null || rolesRequired == null || assignedEmployees == null) {
            throw new IllegalArgumentException("Shift type, date, roles required, and assigned employees cannot be null");
        }
        // Checks if a shift manager is assigned as a required role
        // TODO: test that in case of no shift manager there is no exception thrown
        if (!rolesRequired.containsKey(ShiftManagerStr) || rolesRequired.get(ShiftManagerStr) <= 0) {
            throw new InvalidInputException("At least one shift manager is required for every shift");
        }

        Shift newShift = new Shift(shiftIdCounter, shiftType, date, rolesRequired, assignedEmployees, availableEmployees, isAssignedShiftManager, isOpen, startHour,endHour, updateDate);
        shiftIdCounter++;
        boolean addedToWeekly = addShiftToWeekly(newShift);
        boolean added = shifts.add(newShift);
        return addedToWeekly && added;
    }



    /**
     * Create shifts from the next Sunday after the given date for one full week (Sunday to Saturday).
     * @param doneBy employee who is creating the shifts
     * @param startDate the date to start from (shifts will begin from the next Sunday)
     * @param rolesRequired number of employees of each role required for the shifts
     * @return true if all shifts were created successfully, false otherwise
     */
    public boolean createWeeklyShifts(long doneBy, LocalDate startDate, Map<String, Integer> rolesRequired) {
        final String PERMISSION_REQUIRED = "CREATE_SHIFT";

        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to create weekly shifts");
        }

        if (startDate == null || rolesRequired == null) {
            throw new IllegalArgumentException("Start date and roles required cannot be null");
        }

//        // Move to the next Sunday if not already Sunday
//        if (startDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
//            int daysUntilSunday = (DayOfWeek.SUNDAY.getValue() - startDate.getDayOfWeek().getValue() + 7) % 7;
//            startDate = startDate.plusDays(daysUntilSunday);
//        }

        for (int day = 0; day < 7; day++) {
            LocalDate date = startDate.plusDays(day);
            DayOfWeek dayOfWeek = date.getDayOfWeek();

            // Morning shift for all days except Saturday
            if (dayOfWeek != DayOfWeek.SATURDAY) {
                if (!AddNewShift(date, ShiftType.MORNING, rolesRequired)) return false;
            }

            // Evening shift for Sunday to Thursday and Saturday (not Friday)
            if (dayOfWeek != DayOfWeek.FRIDAY) {
                if (!AddNewShift(date, ShiftType.EVENING, rolesRequired)) return false;
            }
        }

        return true;
    }

    /**
     * create and add
     * @param date the date of the shift
     * @param type the type of the shift (morning or evening)
     * @param rolesRequired number of employees of each role required for the shift
     * @return true if the shift was added successfully, false otherwise
     */
    private boolean AddNewShift(LocalDate date, ShiftType type, Map<String, Integer> rolesRequired) {
        // Check if a shift of this type already exists for the date
        if (shifts.stream().anyMatch(s -> s.getShiftDate().equals(date) && s.getShiftType() == type)) {
            throw new RuntimeException(type + " shift already exists for " + date);
        }
        // Checks if a shift manager is assigned as a required role
        if (!rolesRequired.containsKey(ShiftManagerStr)) {
            throw new InvalidInputException("Shift manager is required for every shift");
        }

        Set<Long> availableEmployees = new HashSet<>();
        Map<String, Set<Long>> assignedEmployees = new HashMap<>();
        boolean isOpen = (type == ShiftType.MORNING && date.getDayOfWeek() != DayOfWeek.SATURDAY)
                || (type == ShiftType.EVENING && date.getDayOfWeek() != DayOfWeek.FRIDAY && date.getDayOfWeek() != DayOfWeek.SATURDAY);

        LocalTime startHour = type == ShiftType.MORNING ? config.START_HOUR_MORNING : config.START_HOUR_EVENING;
        LocalTime endHour = type == ShiftType.MORNING ? config.END_HOUR_MORNING : config.END_HOUR_EVENING;
        Shift shift = new Shift(shiftIdCounter++, type, date, rolesRequired, assignedEmployees, availableEmployees, false, isOpen, startHour,endHour, LocalDate.now());

        boolean added = shifts.add(shift);
        // Even if the shift wasn't added to the weekly shifts map, it's still in the shifts set
        addShiftToWeekly(shift);
        return added;
    }

    /**
     * remove a shift from the system
     * @param doneBy   employee who is removing the shift
     * @param shiftId id of the shift to remove
     * @return true if the shift was removed successfully, false otherwise
     */
    public boolean removeShiftByID(long doneBy, long shiftId) {
        String PERMISSION_REQUIRED = "REMOVE_SHIFT";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to remove shift");
        }
        if (shiftId <= 0) {
            throw new IllegalArgumentException("Shift ID must be a positive number");
        }
        Shift shiftToRemove = shifts.stream().filter(shift -> shift.getId() == shiftId).findFirst().orElse(null);
        if (shiftToRemove == null) {
            throw new RuntimeException("Shift does not exist");
        }
        shifts.remove(shiftToRemove);
        return true;
    }

    public boolean removeShift(long doneBy, LocalDate date,ShiftType shiftType) {
        String PERMISSION_REQUIRED = "REMOVE_SHIFT";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to delete shift");
        }
        Shift shiftToRemove = shifts.stream()
                .filter(shift -> shift.getShiftDate().equals(date) && shift.getShiftType().equals(shiftType))
                .findFirst()
                .orElse(null);
        if (shiftToRemove == null) {
            throw new RuntimeException("Shift does not exist");
        }
        shifts.remove(shiftToRemove);
        return true;
    }

    /**
     * update a shift in the system
     * @param doneBy                 employee who is updating the shift
     * @param shiftId                id of the shift to update
     * @param shiftType              morning or evening
     * @param date                   date of the shift
     * @param isAssignedShiftManager true if the shift has a shift manager assigned, false otherwise
     * @param isOpen                 true if the place is open
     * @param updateDate             the date of the last update
     * @return true if the shift was updated successfully, false otherwise
     */
    public boolean updateShift(long doneBy, long shiftId,ShiftType shiftType, LocalDate date, boolean isAssignedShiftManager, boolean isOpen,LocalTime startHour , LocalTime endHour, LocalDate updateDate) {
        String PERMISSION_REQUIRED = "UPDATE_SHIFT";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to update shift");
        }
        if (shiftId <= 0) {
            throw new IllegalArgumentException("Shift ID must be a positive number");
        }
        if (shiftType == null || date == null ) {
            throw new IllegalArgumentException("Shift type, date cannot be null");
        }
//        if (date.isBefore(LocalDate.now())) {
//            throw new IllegalArgumentException("Date cannot be in the past");
//        }
        if (updateDate == null) {
            throw new IllegalArgumentException("Update date cannot be null");
        }
//        if (updateDate.isBefore(date)) {
//            throw new IllegalArgumentException("Update date cannot be before the shift date");
//        }
//        if (updateDate.isAfter(LocalDate.now())) {
//            throw new IllegalArgumentException("Update date cannot be in the future");
//        }
        if (isAssignedShiftManager) {
            throw new IllegalArgumentException("Shift manager must be assigned if isAssignedShiftManager is true");
        }
        Shift shiftToUpdate = shifts.stream().filter(shift -> shift.getId() == shiftId).findFirst().orElse(null);
        if (shiftToUpdate == null) {
            throw new RuntimeException("Shift does not exist");
        }
        shiftToUpdate.setShiftType(shiftType);
        shiftToUpdate.setShiftDate(date);
        shiftToUpdate.setAssignedShiftManager(isAssignedShiftManager);
        shiftToUpdate.setOpen(isOpen);
        shiftToUpdate.setStartHour(startHour);
        shiftToUpdate.setEndHour(endHour);
        shiftToUpdate.setUpdateDate(updateDate);
        return true;
    }

    /**
     * get a shift from the system
     * @param doneBy employee who is requesting the shift
     * @param shiftId id of the shift to get
     * @return the shift if it exists, null otherwise
     */
    public String getShiftByID(long doneBy, long shiftId) {
        String PERMISSION_REQUIRED = "GET_SHIFT";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to get shift");
        }
        if (shiftId <= 0) {
            throw new IllegalArgumentException("Shift ID must be a positive number");
        }
        Shift shiftToGet = shifts.stream().filter(shift -> shift.getId() == shiftId).findFirst().orElse(null);
        if (shiftToGet == null) {
            throw new RuntimeException("Shift does not exist");
        }
        ShiftDTO dto = new ShiftDTO(shiftToGet.getId(), shiftToGet.getShiftType(), shiftToGet.getShiftDate(), shiftToGet.getRolesRequired(), shiftToGet.getAssignedEmployees(), shiftToGet.getAvailableEmployees(), shiftToGet.isAssignedShiftManager(), shiftToGet.isOpen(), shiftToGet.getStartHour(), shiftToGet.getEndHour(), shiftToGet.getCreateDate(), shiftToGet.getUpdateDate());
        return dto.serialize();
    }

    /**
     * get all shifts from the system
     * @param doneBy employee who is requesting the shifts
     * @return all shifts
     */
    public String getAllShifts(long doneBy) {
        String PERMISSION_REQUIRED = "GET_SHIFT";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to get all dtos");
        }
        if (shifts.isEmpty()) {
            throw new RuntimeException("No Shifts were found");
        }
        // return all dtos as strings DTO serialized
        List<Shift> shiftsList = new ArrayList<>(shifts); // Convert Set to List

        return serializeArrayShifts(shiftsList);
    }

    /**
     * get all shifts for a specific date
     * @param doneBy employee who is requesting the shifts
     * @param date date of the shifts
     * @return all shifts for the date
     */
    public String getAllShiftsByDate(long doneBy, LocalDate date) {
        String PERMISSION_REQUIRED = "GET_SHIFT";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to get shifts by date");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        // return all shifts for the date as strings DTO serialized
        Set<Shift> shiftsByDate = shifts.stream().filter(shift -> shift.getShiftDate().equals(date)).collect(Collectors.toSet());
        if (shiftsByDate.isEmpty()) {
            throw new RuntimeException("No shifts found for the date");
        }
        return serializeSetShifts(shiftsByDate);
    }

    /**
     * get all shifts for a specific employee
     * @param doneBy employee who is requesting the shifts
     * @param employeeID employee to get shifts for
     * @return all shifts for the employee
     */
    public String getShiftsByEmployee(long doneBy, long employeeID) {
        String PERMISSION_REQUIRED = "GET_SHIFT";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to get shifts by employee");
        }
        if (employeeID <= 0) {
            throw new IllegalArgumentException("Employee ID must be a positive number");
        }
        // return all shifts for the employee as strings DTO serialized
        Set<Shift> shiftsByEmployee = shifts.stream().filter(shift -> shift.getAssignedEmployees().values().stream().anyMatch(set -> set.contains(employeeID))).collect(Collectors.toSet());
        if (shiftsByEmployee.isEmpty()) {
            throw new RuntimeException("No shifts found for the employee");
        }

        return serializeSetShifts(shiftsByEmployee);
    }

    /**
     * get shift by date and shift type
     * @param doneBy    employee who is requesting the shift
     * @param date      date of the shift
     * @param shiftType morning or evening
     * @return the shift if it exists, null otherwise
     */
    public String getshift(long doneBy, LocalDate date,ShiftType shiftType) {
        String PERMISSION_REQUIRED = "GET_SHIFT";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to get shifts by date and type");
        }
        if (date == null || shiftType == null) {
            throw new IllegalArgumentException("Date and shift type cannot be null");
        }
        return shifts.stream()
                .filter(shift -> shift.getShiftDate().equals(date) && shift.getShiftType().equals(shiftType))
                .findFirst()
                .map(this::serializeShift)
                .orElseThrow(() -> new RuntimeException("Shift does not exist"));
    }

    /**
     * update the roles required for a shift
     * @param doneBy        employee who is updating the shift
     * @param shiftId       id of the shift to update
     * @param rolesRequired number of employees of each role required for the shift
     * @return true if the roles were updated successfully, false otherwise
     */
    public boolean updateRolesRequired(long doneBy, long shiftId, String role, Integer rolesRequired) {
        String PERMISSION_REQUIRED = "ROLE_REQUIRED";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to update shifts (roles required)");
        }
        // Check if its not removing the shift manager role
        if (role.equals(ShiftManagerStr) && rolesRequired <= 0) {
            throw new IllegalArgumentException("Shift manager role cannot be removed, at least 1 shift manager in the shift is required");
        }
        if (rolesRequired == null) {
            throw new IllegalArgumentException("Roles required cannot be null");
        }
        if (shiftId <= 0) {
            throw new IllegalArgumentException("Shift ID must be a positive number");
        }
        Shift shiftToUpdate = shifts.stream().filter(shift -> shift.getId() == shiftId).findFirst().orElse(null);
        if (shiftToUpdate == null) {
            throw new RuntimeException("Shift does not exist");
        }
        Map <String, Integer> rolesRequiredMap = shiftToUpdate.getRolesRequired();
        rolesRequiredMap.put(role, rolesRequired);
        shiftToUpdate.setRolesRequired(rolesRequiredMap);
        shiftToUpdate.setUpdateDate(LocalDate.now());
        return true;
    }

    /**
     * update the shift manager for a shift
     * @param doneBy                employee who is updating the shift
     * @param shiftId                id of the shift to update
     * @param isAssignedShiftManager true if the shift has a shift manager assigned, false otherwise
     * @return true if the shift manager was updated successfully, false otherwise
     */
    public boolean updateShiftManager(long doneBy, long shiftId, boolean isAssignedShiftManager) {
        String PERMISSION_REQUIRED = "UPDATE_SHIFT";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to update shifts");
        }
        if (shiftId <= 0) {
            throw new IllegalArgumentException("Shift ID must be a positive number");
        }
        Shift shiftToUpdate = shifts.stream().filter(shift -> shift.getId() == shiftId).findFirst().orElse(null);
        if (shiftToUpdate == null) {
            throw new RuntimeException("Shift does not exist");
        }
        shiftToUpdate.setAssignedShiftManager(isAssignedShiftManager);
        shiftToUpdate.setUpdateDate(LocalDate.now());
        return true;
    }

    /**
     * update the open status for a shift
     * @param doneBy employee who is updating the shift
     * @param shiftId id of the shift to update
     * @param isOpen  true if the place is open, false otherwise
     * @return true if the open status was updated successfully, false otherwise
     */
    public boolean updateOpenStatus(long doneBy, long shiftId, boolean isOpen) {
        String PERMISSION_REQUIRED = "UPDATE_SHIFT";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to update shifts");
        }
        if (shiftId <= 0) {
            throw new IllegalArgumentException("Shift ID must be a positive number");
        }
        Shift shiftToUpdate = shifts.stream().filter(shift -> shift.getId() == shiftId).findFirst().orElse(null);
        if (shiftToUpdate == null) {
            throw new RuntimeException("Shift does not exist");
        }
        shiftToUpdate.setOpen(isOpen);
        shiftToUpdate.setUpdateDate(LocalDate.now());
        return true;
    }


    /**
     * Removes a role requirement from a shift.
     * @param doneBy   employee who is removing the shift
     * @param shiftId the ID of the shift
     * @param role    the role to remove
     * @return true if the role was removed, false if it was not present
     * @throws RuntimeException if the shift does not exist
     */
    public boolean removeRoleRequired(long doneBy, long shiftId, String role) {
        String PERMISSION_REQUIRED = "ROLE_REQUIRED";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to update Role required");
        }

        Shift shiftToUpdate = shifts.stream()
                .filter(shift -> shift.getId() == shiftId)
                .findFirst()
                .orElse(null);

        if (shiftToUpdate == null) {
            throw new RuntimeException("Shift does not exist");
        }
        // Check if its not removing the shift manager role
        if (role.equals(ShiftManagerStr)) {
            throw new IllegalArgumentException("Shift manager role cannot be removed.");
        }
        if (role == null || role.isEmpty()) {
            throw new IllegalArgumentException("Role cannot be null or empty");
        }
        if (shiftId <= 0) {
            throw new IllegalArgumentException("Shift ID must be a positive number");
        }
        if (!shiftToUpdate.getRolesRequired().containsKey(role)) {
            throw new IllegalArgumentException("Role not found in the shift");
        }
        if (shiftToUpdate.getAssignedEmployees().containsKey(role) && !shiftToUpdate.getAssignedEmployees().get(role).isEmpty()) {
            throw new IllegalArgumentException("Cannot remove role that has assigned employees");
        }
        Map<String, Integer> requiredRoles = shiftToUpdate.getRolesRequired();
        shiftToUpdate.setUpdateDate(LocalDate.now());
        return requiredRoles.remove(role) != null;
    }

    public String getRoles(long doneBy) {
        String PERMISSION_REQUIRED = "GET_ROLES";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to get all roles");
        }
        if (doneBy <= 0) {
            throw new IllegalArgumentException("Employee ID must be a positive number");
        }
        return shifts.stream()
                .flatMap(shift -> shift.getRolesRequired().keySet().stream())
                .distinct()
                .collect(Collectors.joining(", "));
    }


    public String getShiftsByWeek(long doneBy, Week week) {
        String PERMISSION_REQUIRED = "GET_SHIFT";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to get shifts by week");
        }

        Set<Shift> shiftsSet = weeklyShifts.get(week);
        if (shiftsSet == null) {
            return null; // No shifts for this week
        }

        List<Shift> shiftsList = new ArrayList<>(shiftsSet);
        List<ShiftDTO> dtos = shiftsList.stream()
                .map(shift -> new ShiftDTO(shift.getId(), shift.getShiftType(), shift.getShiftDate(), shift.getRolesRequired(), shift.getAssignedEmployees(), shift.getAvailableEmployees(), shift.isAssignedShiftManager(), shift.isOpen(), shift.getStartHour(), shift.getEndHour(), shift.getCreateDate(), shift.getUpdateDate()))
                .collect(Collectors.toList());

        dtos.sort(Comparator.comparing(ShiftDTO::getShiftDate).thenComparing(ShiftDTO::getShiftType)); // optional: morning before evening

        return serializeArrayShifts(shiftsList);
    }


    public void addShiftsToWeeklyShifts(Set<Shift> shifts) {
        if (shifts == null || shifts.isEmpty()) {
            return; // Nothing to add, just return
        }

        for (Shift shift : shifts) {
            if (shift == null || shift.getShiftDate() == null) {
                throw new IllegalArgumentException("Shift or shift date cannot be null");
            }
            addShiftToWeekly(shift);
        }
    }

    public boolean addShiftToWeekly(Shift shift) {
        Week week = Week.from(shift.getShiftDate());
        // Comparator to sort by date (and optionally by hour if needed)
        Comparator<Shift> byDate = Comparator.comparing(Shift::getShiftDate);
        return weeklyShifts.computeIfAbsent(week, k -> new TreeSet<>(byDate)).add(shift);
    }

    public Shift getShiftbyDateAndTime(long doneBy, LocalDate date, LocalTime hour) {
        String PERMISSION_REQUIRED = "GET_SHIFT";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to get shifts by date and time");
        }
        if (date == null || hour == null) {
            throw new IllegalArgumentException("Date and hour cannot be null");
        }

        return shifts.stream()
                .filter(shift -> shift.getShiftDate().equals(date)
                        && !hour.isBefore(shift.getStartHour())
                        && hour.isBefore(shift.getEndHour()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves all shifts assigned to a specific employee.
     * The requesting employee must have the "GET_SHIFT" permission.
     * An exception is thrown if the permission is not granted or the provided employee ID is invalid.
     *
     * @param doneBy     the ID of the employee requesting the shifts
     * @param employeeId the ID of the employee whose shifts are being retrieved
     * @return a serialized string representation of all shifts assigned to the specified employee
     * @throws UnauthorizedPermissionException if the requesting employee does not have the required permission
     * @throws IllegalArgumentException        if the provided employee ID is not a positive number
     */
    public String getShiftByEmployee(long doneBy, long employeeId) {
        String PERMISSION_REQUIRED = "GET_SHIFT";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to get shifts by employee");
        }
        if (employeeId <= 0) {
            throw new IllegalArgumentException("Employee ID must be a positive number");
        }
        List<Shift> EmployeeShifts = new ArrayList<>();
        EmployeeShifts = shifts.stream()
                .filter(shift -> shift.getAssignedEmployees().values().stream().anyMatch(set -> set.contains(employeeId)))
                .toList();

        return serializeArrayShifts(EmployeeShifts);
    }
}
