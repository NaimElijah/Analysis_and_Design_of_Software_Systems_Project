package DomainLayer;

public class TermsOfEmployment {

    public enum ContractType {
        HOURLY,
        MONTHLY
    }

    private ContractType contractType;
    private double hourlyWage;
    private double pensionPercentage;
    private double severancePercentage;
    private boolean travelPaymentIncluded;
    private double travelPaymentPerDay;
    private boolean overtimeEnabled;
    private double firstOvertimeMultiplier;
    private double additionalOvertimeMultiplier;
    private int weeklyHoursLimit;
    private int annualVacationDays;
    private int annualSickDays;
    private double taxCreditPoints;

    public TermsOfEmployment(
            ContractType contractType,
            double hourlyWage,
            double pensionPercentage,
            double severancePercentage,
            boolean travelPaymentIncluded,
            double travelPaymentPerDay,
            boolean overtimeEnabled,
            double firstOvertimeMultiplier,
            double additionalOvertimeMultiplier,
            int weeklyHoursLimit,
            int annualVacationDays,
            int annualSickDays,
            double taxCreditPoints
    ) {
        this.contractType = contractType;
        this.hourlyWage = hourlyWage;
        this.pensionPercentage = pensionPercentage;
        this.severancePercentage = severancePercentage;
        this.travelPaymentIncluded = travelPaymentIncluded;
        this.travelPaymentPerDay = travelPaymentPerDay;
        this.overtimeEnabled = overtimeEnabled;
        this.firstOvertimeMultiplier = firstOvertimeMultiplier;
        this.additionalOvertimeMultiplier = additionalOvertimeMultiplier;
        this.weeklyHoursLimit = weeklyHoursLimit;
        this.annualVacationDays = annualVacationDays;
        this.annualSickDays = annualSickDays;
        this.taxCreditPoints = taxCreditPoints;
    }


    // Getters only (or add setters if needed)

    public ContractType getContractType() {
        return contractType;
    }

    public double getHourlyWage() {
        return hourlyWage;
    }

    public double getPensionPercentage() {
        return pensionPercentage;
    }

    public double getSeverancePercentage() {
        return severancePercentage;
    }

    public boolean isTravelPaymentIncluded() {
        return travelPaymentIncluded;
    }

    public double getTravelPaymentPerDay() {
        return travelPaymentPerDay;
    }

    public boolean isOvertimeEnabled() {
        return overtimeEnabled;
    }

    public double getFirstOvertimeMultiplier() {
        return firstOvertimeMultiplier;
    }

    public double getAdditionalOvertimeMultiplier() {
        return additionalOvertimeMultiplier;
    }

    public int getWeeklyHoursLimit() {
        return weeklyHoursLimit;
    }

    public int getAnnualVacationDays() {
        return annualVacationDays;
    }

    public int getAnnualSickDays() {
        return annualSickDays;
    }

    public double getTaxCreditPoints() {
        return taxCreditPoints;
    }

    @Override
    public String toString() {
        return "TermsOfEmployment{" +
                "contractType=" + contractType +
                ", hourlyWage=" + hourlyWage +
                ", pensionPercentage=" + pensionPercentage +
                ", severancePercentage=" + severancePercentage +
                ", travelPaymentIncluded=" + travelPaymentIncluded +
                ", travelPaymentPerDay=" + travelPaymentPerDay +
                ", overtimeEnabled=" + overtimeEnabled +
                ", firstOvertimeMultiplier=" + firstOvertimeMultiplier +
                ", additionalOvertimeMultiplier=" + additionalOvertimeMultiplier +
                ", weeklyHoursLimit=" + weeklyHoursLimit +
                ", annualVacationDays=" + annualVacationDays +
                ", annualSickDays=" + annualSickDays +
                ", taxCreditPoints=" + taxCreditPoints +
                '}';
    }
}
