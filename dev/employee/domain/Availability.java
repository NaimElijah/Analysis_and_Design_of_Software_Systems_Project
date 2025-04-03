package employee.domain;

import java.time.LocalDate;

public class Availability {
    private long id;
    private long employeeId;
    private long shiftId;
    private boolean isAvailable;
    private LocalDate createDate;
    private LocalDate updateDate;


    public Availability(long id, long employeeId, long shiftId, boolean isAvailable) {
        this.id = id;
        this.employeeId = employeeId;
        this.shiftId = shiftId;
        this.isAvailable = isAvailable;
        this.createDate = LocalDate.now();
        this.updateDate = LocalDate.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public long getShiftId() {
        return shiftId;
    }

    public void setShiftId(long shiftId) {
        this.shiftId = shiftId;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }
}
