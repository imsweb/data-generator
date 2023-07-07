package com.imsweb.datagenerator.naaccr.rule.patient;

import java.time.LocalDate;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorPatientRule;
import com.imsweb.datagenerator.utils.DistributionUtils;
import com.imsweb.datagenerator.utils.RandomUtils;
import com.imsweb.naaccrxml.entity.Patient;

import static com.imsweb.datagenerator.naaccr.NaaccrDataGenerator.CONTEXT_FLAG_MAX_AGE_GROUP;
import static java.time.temporal.ChronoUnit.DAYS;

public class BirthRule extends NaaccrDataGeneratorPatientRule {

    // unique identifier for this rule
    public static final String ID = "birth";

    /**
     * Constructor.
     */
    public BirthRule() {
        super(ID, "Date of Birth, Birthplace Country and State");
    }

    @Override
    public void execute(Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {

        // birth date should be no later than five years prior to min dx date (or current date if min dx date not defined)
        LocalDate maxBirthDate = options == null ? LocalDate.now() : options.getMinDxDate().minusYears(5);
        // limit age to max 100 years
        LocalDate minBirthDate = maxBirthDate.minusYears(100);

        // Based on our tumors, pick an age that's possible for these sites.
        Integer maxAgeGroup = (Integer)context.get(CONTEXT_FLAG_MAX_AGE_GROUP);
        if (maxAgeGroup != null && maxAgeGroup >= 0) {
            maxBirthDate = minBirthDate.plusYears(100L - (maxAgeGroup * 10));
            if (options != null) {
                LocalDate minDxDate = options.getMinDxDate();
                LocalDate maxDxDate = options.getMaxDxDate();
                long daysBetween = minDxDate.until(maxDxDate, DAYS) - 1;
                // Check that there are no more than 10 years between (365 * 10 days).
                if (daysBetween > 3650) {
                    daysBetween = 3650;
                }

                minBirthDate = minDxDate.minusYears(maxAgeGroup * 10L);
                maxBirthDate = minBirthDate.plusDays(daysBetween);
            }
        }

        LocalDate randomDate = RandomUtils.getRandomDateBetween(minBirthDate, maxBirthDate);

        setValue(patient, "dateOfBirthYear", Integer.toString(randomDate.getYear()));
        setValue(patient, "dateOfBirthMonth", Integer.toString(randomDate.getMonthValue()));
        setValue(patient, "dateOfBirthDay", Integer.toString(randomDate.getDayOfMonth()));
        setValue(patient, "birthplaceState", DistributionUtils.getState());
        setValue(patient, "birthplaceCountry", "USA");
    }
}