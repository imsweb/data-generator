package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.List;
import java.util.Map;

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

        int currentYear = getCurrentYear();

        Integer year = RandomUtils.getRandomYear(currentYear - 100, currentYear - 5);
        Integer month = RandomUtils.getRandomMonth();
        Integer day = RandomUtils.getRandomDay(year, month);

        record.put("birthDateYear", year.toString());
        record.put("birthDateMonth", month.toString());
        record.put("birthDateDay", day.toString());

        record.put("birthplaceCountry", "USA");
        if (propertyHasValue(record, "addressCurrentState"))
            record.put("birthplaceState", record.get("addressCurrentState"));
        else
            record.put("birthplaceState", "XX");
    }
}
