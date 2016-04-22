package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;
import com.imsweb.datagenerator.random.RandomUtils;

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
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {

        // birth date should be no later than five years prior to min dx date (or current date if min dx date not defined)
        LocalDate maxBirthDate = options == null ? LocalDate.now().minusYears(15) : options.getMinDxDate().minusYears(5);
        // limit age to max 100 years
        LocalDate minBirthDate = maxBirthDate.minusYears(100);

        LocalDate randomDate = RandomUtils.getRandomDateBetween(minBirthDate, maxBirthDate);

        record.put("birthDateYear", Integer.toString(randomDate.getYear()));
        record.put("birthDateMonth", Integer.toString(randomDate.getMonthOfYear()));
        record.put("birthDateDay", Integer.toString(randomDate.getDayOfMonth()));

        record.put("birthplaceCountry", "USA");
        if (propertyHasValue(record, "addressCurrentState"))
            record.put("birthplaceState", record.get("addressCurrentState"));
        else
            record.put("birthplaceState", "XX");
    }
}
