package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;
import com.imsweb.datagenerator.random.RandomUtils;

public class DateOfDiagnosisRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "date-of-diagnosis";

    /**
     * Constructor.
     */
    public DateOfDiagnosisRule() {
        super(ID, "Date of Diagnosis");
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {

        // get max year from the options
        Integer maxDxYear = options == null ? null : options.getMaxDxYear();
        if (maxDxYear == null)
            maxDxYear = getCurrentYear();

        // get min year from the options
        Integer minDxYear = options == null ? null : options.getMinDxYear();
        if (minDxYear == null)
            minDxYear = maxDxYear - 10;

        // if we have already assigned a DX year, take the next year available
        if (!otherRecords.isEmpty())
            minDxYear = Math.min(maxDxYear, Integer.parseInt(otherRecords.get(otherRecords.size() - 1).get("dateOfDiagnosisYear")) + 1);

        // never go before the year of birth
        if (propertyHasValue(record, "birthDateYear"))
            minDxYear = Math.max(minDxYear, Integer.parseInt(record.get("birthDateYear")) + 1);

        Integer year = RandomUtils.getRandomYear(minDxYear, maxDxYear);
        Integer month = RandomUtils.getRandomMonth();
        Integer day = RandomUtils.getRandomDay(year, month);

        record.put("dateOfDiagnosisYear", year.toString());
        record.put("dateOfDiagnosisMonth", month.toString());
        record.put("dateOfDiagnosisDay", day.toString());
    }
}
