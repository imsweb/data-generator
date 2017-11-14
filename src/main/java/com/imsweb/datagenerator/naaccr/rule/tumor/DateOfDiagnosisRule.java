package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;
import com.imsweb.datagenerator.utils.RandomUtils;

public class DateOfDiagnosisRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "date-of-diagnosis";

    private static final String _CRITERIA = "Date of Diagnosis is randomly generated between January 1 of the minimum DX year and December 31 of the maximum DX year.<br/>"
            + "If no minimum year was specified, then the current date minus 10 years is used as the minimum date.<br/>"
            + "If no maximum year was specified, then the current date is used as the maximum date.<br/>"
            + "If a patient has multiple tumors, each tumor after the first one will use the previous tumor's Date of Diagnosis as its minimum date.<br/>"
            + "This ensures that all tumors on a patient are in chronological order.";

    /**
     * Constructor.
     */
    public DateOfDiagnosisRule() {
        super(ID, "Date of Diagnosis");
    }

    @Override
    public String getCriteria() {
        return _CRITERIA;
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
            minDxDates.add(LocalDate.of(Integer.parseInt(record.get("birthDateYear")) + 1, 1, 1));
        // never go before dx date of patient's most recent tumor (if this isn't the first one)
        if (!otherRecords.isEmpty()) {
            Map<String, String> lastTumor = otherRecords.get(otherRecords.size() - 1);
            minDxDates.add(LocalDate.of(Integer.parseInt(lastTumor.get("dateOfDiagnosisYear")), Integer.parseInt(lastTumor.get("dateOfDiagnosisMonth")),
                    Integer.parseInt(lastTumor.get("dateOfDiagnosisDay"))));
        }

        LocalDate randomDate = RandomUtils.getRandomDateBetween(minDxDates, maxDxDates);

        record.put("dateOfDiagnosisYear", Integer.toString(randomDate.getYear()));
        record.put("dateOfDiagnosisMonth", Integer.toString(randomDate.getMonthValue()));
        record.put("dateOfDiagnosisDay", Integer.toString(randomDate.getDayOfMonth()));
    }
}
