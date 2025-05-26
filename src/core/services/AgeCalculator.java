package core.services;

import java.time.LocalDate;
import java.time.Period;

/**
 * Provides a utility method to calculate the age based on a birthdate.
 */
public class AgeCalculator {

    /**
     * Calculates the age in years from a given birthdate to the current date.
     *
     * @param birthdate The birthdate as a LocalDate.
     * @return The age in full years.
     */
    public static int calculateAge(LocalDate birthdate) {
        // LocalDate.now() uses the system default time-zone.
        return Period.between(birthdate, LocalDate.now()).getYears();
    }
}