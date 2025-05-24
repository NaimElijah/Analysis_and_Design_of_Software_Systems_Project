package DomainLayer.EmployeeSubModule.Repository;

import DTOs.ShiftDTO;

import java.util.List;

public interface ShiftReposetory {
    /**
     * Creates a new shift.
     *
     * @param shiftDTO The shift DTO to create
     * @return true if the shift was created successfully, false otherwise
     */
    boolean create(ShiftDTO shiftDTO);

    /**
     * Updates an existing shift.
     *
     * @param shiftDTO The shift DTO to update
     * @return true if the shift was updated successfully, false otherwise
     */
    boolean update(ShiftDTO shiftDTO);

    /**
     * Deletes a shift.
     *
     * @param shiftId The ID of the shift to delete
     * @return true if the shift was deleted successfully, false otherwise
     */
    boolean delete(long shiftId);

    /**
     * Retrieves a shift by its ID.
     *
     * @param shiftId The ID of the shift to retrieve
     * @return The shift DTO with the given ID, or null if not found
     */
    ShiftDTO getById(long shiftId);

    /**
     * Retrieves all shifts.
     *
     * @return A list of all shift DTOs
     */
    List<ShiftDTO> getAll();



}
