package com.imsweb.datagenerator.naaccr.rule.patient;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;
import com.imsweb.datagenerator.utils.DistributionUtils;
import com.imsweb.datagenerator.utils.RandomUtils;

import static com.imsweb.datagenerator.naaccr.NaaccrDataGenerator.CONTEXT_FLAG_MAX_AGE_GROUP;
import static java.time.temporal.ChronoUnit.DAYS;


public class BirthRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "birth";

    /**
     * Constructor.
     */
    public BirthRule() {
        super(ID, "Date of Birth, Birthplace Country and State");
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options, Map<String, Object> context) {

        // birth date should be no later than five years prior to min dx date (or current date if min dx date not defined)
        //LocalDate maxBirthDate = options == null ? LocalDate.now().minusYears(15) : options.getMinDxDate().minusYears(5);
        LocalDate maxBirthDate = options == null ? LocalDate.now() : options.getMinDxDate().minusYears(5);
        // limit age to max 100 years
        LocalDate minBirthDate = maxBirthDate.minusYears(100);

        // Based on our tumors, pick an age that's possible for these sites.
        Integer maxAgeGroup = (Integer)context.get(CONTEXT_FLAG_MAX_AGE_GROUP);
        if (maxAgeGroup != null)
            if (maxAgeGroup >= 0) {
                maxBirthDate = minBirthDate.plusYears(100 - (maxAgeGroup * 10));
                if (options != null) {
                    LocalDate minDxDate = options.getMinDxDate();
                    LocalDate maxDxDate = options.getMaxDxDate();
                    long daysBetween = minDxDate.until(maxDxDate, DAYS) - 1;
                    if (daysBetween > 3650) daysBetween = 3650;

                    minBirthDate = minDxDate.minusYears(maxAgeGroup * 10);
                    maxBirthDate = minBirthDate.plusDays(daysBetween);
                }
            }

        LocalDate randomDate = RandomUtils.getRandomDateBetween(minBirthDate, maxBirthDate);

        record.put("birthDateYear", Integer.toString(randomDate.getYear()));
        record.put("birthDateMonth", Integer.toString(randomDate.getMonthValue()));
        record.put("birthDateDay", Integer.toString(randomDate.getDayOfMonth()));
        record.put("birthplaceState", DistributionUtils.getState());
        record.put("birthplaceCountry", "USA");
    }
}
