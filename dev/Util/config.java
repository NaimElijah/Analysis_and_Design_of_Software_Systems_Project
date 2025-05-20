package Util;

import java.time.LocalTime;

public class config {
    // Default hour values
    public static final LocalTime START_HOUR_MORNING = LocalTime.of(8, 0);
    public static final LocalTime END_HOUR_MORNING = LocalTime.of(16, 0);
    public static final LocalTime START_HOUR_EVENING = LocalTime.of(16, 0);
    public static final LocalTime END_HOUR_EVENING = LocalTime.of(20, 0);

    public static final LocalTime BLOCK_AVAILABILITY_START = LocalTime.of(16, 0);

    // Defult roles
    public static final String ROLE_ADMIN = "Admin";
    public static final String ROLE_HR_MANAGER = "HR Manager";
    public static final String ROLE_SHIFT_MANAGER = "Shift Manager";
    public static final String ROLE_TRANSPORT_MANAGER = "Transport Manager";
    public static final String ROLE_CASHIER = "Cashier";
    public static final String ROLE_CLEANER = "Cleaner";
    public static final String ROLE_STOCKER = "Stocker";
    public static final String ROLE_DRIVER_A = "DriverA";
    public static final String ROLE_DRIVER_B = "DriverB";
    public static final String ROLE_DRIVER_C = "DriverC";
    public static final String ROLE_DRIVER_D = "DriverD";
    public static final String ROLE_DRIVER_E = "DriverE";




}
