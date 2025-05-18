package DTOs;

import DomainLayer.enums.ShiftType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * Data Transfer Object for Shift entities.
 * This class is used to transfer shift data between layers.
 * It uses Jackson for JSON serialization/deserialization.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShiftDTO {
    // Static ObjectMapper configured for all serialization/deserialization
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private long id;
    private ShiftType shiftType;
    private LocalDate shiftDate;
    private Map<String, Integer> rolesRequired;   // e.g., {"Cashier": 2, "Security": 1}
    private Map<String, Set<Long>> assignedEmployees;  // e.g., {"Cashier": [123456789, 987654321], "Security": [112233445]}
    private Set<Long> availableEmployees;  // e.g., [123456789, 555444333]
    private boolean isAssignedShiftManager;
    private boolean isOpen;
    private LocalTime startHour;
    private LocalTime endHour;
    private LocalDate createDate;
    private LocalDate updateDate;

    /**
     * Default constructor for serialization
     */
    public ShiftDTO() {
        this.rolesRequired = new HashMap<>();
        this.assignedEmployees = new HashMap<>();
        this.availableEmployees = new HashSet<>();
    }

    /**
     * Full constructor for creating a ShiftDTO with all fields
     */
    public ShiftDTO(long id, ShiftType shiftType, LocalDate shiftDate, 
                   Map<String, Integer> rolesRequired, Map<String, Set<Long>> assignedEmployees, 
                   Set<Long> availableEmployees, boolean isAssignedShiftManager, 
                   boolean isOpen,LocalTime startHour , LocalTime endHour, LocalDate createDate, LocalDate updateDate) {
        this.id = id;
        this.shiftType = shiftType;
        this.shiftDate = shiftDate;
        this.rolesRequired = rolesRequired != null ? new HashMap<>(rolesRequired) : new HashMap<>();
        this.assignedEmployees = assignedEmployees != null ? new HashMap<>(assignedEmployees) : new HashMap<>();
        this.availableEmployees = availableEmployees != null ? new HashSet<>(availableEmployees) : new HashSet<>();
        this.isAssignedShiftManager = isAssignedShiftManager;
        this.isOpen = isOpen;
        this.startHour = startHour;
        this.endHour = endHour;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    // Getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ShiftType getShiftType() {
        return shiftType;
    }

    public void setShiftType(ShiftType shiftType) {
        this.shiftType = shiftType;
    }

    public LocalDate getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(LocalDate shiftDate) {
        this.shiftDate = shiftDate;
    }

    public Map<String, Integer> getRolesRequired() {
        return new HashMap<>(rolesRequired);
    }

    public void setRolesRequired(Map<String, Integer> rolesRequired) {
        this.rolesRequired = rolesRequired != null ? new HashMap<>(rolesRequired) : new HashMap<>();
    }

    public Map<String, Set<Long>> getAssignedEmployees() {
        return new HashMap<>(assignedEmployees);
    }

    public void setAssignedEmployees(Map<String, Set<Long>> assignedEmployees) {
        this.assignedEmployees = assignedEmployees != null ? new HashMap<>(assignedEmployees) : new HashMap<>();
    }

    public Set<Long> getAvailableEmployees() {
        return new HashSet<>(availableEmployees);
    }

    public void setAvailableEmployees(Set<Long> availableEmployees) {
        this.availableEmployees = availableEmployees != null ? new HashSet<>(availableEmployees) : new HashSet<>();
    }

    public boolean isAssignedShiftManager() {
        return isAssignedShiftManager;
    }

    public void setAssignedShiftManager(boolean assignedShiftManager) {
        this.isAssignedShiftManager = assignedShiftManager;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public LocalTime getStartHour() {
        return startHour;
    }

    public void setStartHour(LocalTime startHour) {this.startHour = startHour;}

    public LocalTime getEndHour() {return endHour;}

    public void setEndHour(LocalTime endHour) {this.endHour = endHour;}

    public String getHours() {return startHour + " - " + endHour;}

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * Serializes this ShiftDTO object to a JSON string
     * 
     * @return JSON string representation of this object
     * @throws RuntimeException if serialization fails
     */
    public String serialize() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (IOException e) {
            throw new SerializeException("Failed to serialize ShiftDTO to JSON", e);
        }
    }

    /**
     * Deserializes a ShiftDTO object from a JSON string
     * 
     * @param serialized JSON string representation of a ShiftDTO object
     * @return The deserialized ShiftDTO object
     * @throws RuntimeException if deserialization fails
     */
    public static ShiftDTO deserialize(String serialized) {
        try {
            return objectMapper.readValue(serialized, ShiftDTO.class);
        } catch (IOException e) {
            throw new SerializeException("Failed to deserialize ShiftDTO from JSON", e);
        }
    }

    public static Set<ShiftDTO> deserializeSet(String serialized) {
        try {
            return objectMapper.readValue(serialized, objectMapper.getTypeFactory().constructCollectionType(Set.class, ShiftDTO.class));
        } catch (IOException e) {
            throw new SerializeException("Failed to deserialize Set<ShiftDTO> from JSON", e);
        }
    }
    public static List<ShiftDTO> deserializeList(String serialized) {
        try {
            return objectMapper.readValue(serialized, objectMapper.getTypeFactory().constructCollectionType(List.class, ShiftDTO.class));
        } catch (IOException e) {
            throw new SerializeException("Failed to deserialize List<ShiftDTO> from JSON", e);
        }
    }
    public static ShiftDTO[] deserializeArray(String serialized) {
        try {
            return objectMapper.readValue(serialized, ShiftDTO[].class);
        } catch (IOException e) {
            throw new SerializeException("Failed to deserialize ShiftDTO[] from JSON", e);
        }
    }


    @Override
    public String toString() {
        return "ShiftDTO{" +
                "id=" + id +
                ", shiftType=" + shiftType +
                ", shiftDate=" + shiftDate +
                ", isOpen=" + isOpen +
                ", isAssignedShiftManager=" + isAssignedShiftManager +
                '}';
    }
}