package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.LocalDate;

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

        // latest possible date set only by options if defined
        Set<LocalDate> maxDxDates = new HashSet<>();
        LocalDate maxDate = options == null ? LocalDate.now() : options.getMaxDxDate();
        maxDxDates.add(maxDate);

        Set<LocalDate> minDxDates = new HashSet<>();
        // never go before min date defined in options, or current date minus ten years if options not defined
        minDxDates.add(options == null ? LocalDate.now().minusYears(10) : options.getMinDxDate());
        // never go before the year of birth
        if (propertyHasValue(record, "birthDateYear"))
            minDxDates.add(new LocalDate(Integer.parseInt(record.get("birthDateYear")) + 1, 1, 1));
        // never go before dx date of patient's most recent tumor (if this isn't the first one)
        if (!otherRecords.isEmpty()) {
            Map<String, String> lastTumor = otherRecords.get(otherRecords.size() - 1);
            minDxDates.add(new LocalDate(Integer.parseInt(lastTumor.get("dateOfDiagnosisYear")), Integer.parseInt(lastTumor.get("dateOfDiagnosisMonth")),
                    Integer.parseInt(lastTumor.get("dateOfDiagnosisDay"))).plusDays(1));
        }

        LocalDate randomDate = RandomUtils.getRandomDateBetween(minDxDates, maxDxDates);

        record.put("dateOfDiagnosisYear", Integer.toString(randomDate.getYear()));
        record.put("dateOfDiagnosisMonth", Integer.toString(randomDate.getMonthOfYear()));
        record.put("dateOfDiagnosisDay", Integer.toString(randomDate.getDayOfMonth()));
    }
}
