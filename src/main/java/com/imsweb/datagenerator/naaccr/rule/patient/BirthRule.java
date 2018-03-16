package com.imsweb.datagenerator.naaccr.rule.patient;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;
import com.imsweb.datagenerator.utils.DistributionUtils;
import com.imsweb.datagenerator.utils.RandomUtils;

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
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options, Map<String, String> context) {

        // birth date should be no later than five years prior to min dx date (or current date if min dx date not defined)
        LocalDate maxBirthDate = options == null ? LocalDate.now().minusYears(15) : options.getMinDxDate().minusYears(5);
        // limit age to max 100 years
        LocalDate minBirthDate = maxBirthDate.minusYears(100);

        // Based on our tumors, pick an age that's possible for these sites.
        if (context != null)
            if (propertyHasValue(context, "totalTumorCount")) {
                int maxAgeGroup = -1;
                int tumorCount = Integer.valueOf(context.get("totalTumorCount"));
                for (int i = 0; i < tumorCount; i++) {
                    int tumorAgeGroup = Integer.valueOf(context.get("tumor" + i + " ageGroup"));
                    maxAgeGroup = Integer.max(maxAgeGroup, tumorAgeGroup);
                }
                if (maxAgeGroup >= 0) {
                    maxBirthDate = minBirthDate.plusYears(100 - (maxAgeGroup * 10));
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
