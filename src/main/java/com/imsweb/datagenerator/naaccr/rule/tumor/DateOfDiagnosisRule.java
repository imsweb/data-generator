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

    /**
     * Constructor.
     */
    public DateOfDiagnosisRule() {
        super(ID, "Date of Diagnosis");
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options, Map<String, String> context) {

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

        if (context != null)
            if (propertyHasValue(context, "currentTumor")) {
                Integer birthYear = Integer.valueOf(record.get("birthDateYear"));
                Integer birthMonth = Integer.valueOf(record.get("birthDateMonth"));
                Integer birthDay = Integer.valueOf(record.get("birthDateDay"));
                LocalDate birthDate = LocalDate.of(birthYear, birthMonth, birthDay);

                // PROBLEM: This brakes 3 previous rules:
                // 1. Minimum date must be within 10 years of today.
                // 2. Options specify a minimum DX date.
                // 3. This tumor must be diagnosed after the previous ones for this patient.
                minDxDates.clear();
                maxDxDates.clear();

                String currentTumor = context.get("currentTumor");
                int tumorAgeGroup = Integer.valueOf(context.get("tumor" + currentTumor + " ageGroup"));
                minDxDates.add(birthDate.plusYears((tumorAgeGroup * 10) - 10));
                maxDxDates.add(birthDate.plusYears((tumorAgeGroup * 10)));
            }



        LocalDate randomDate = RandomUtils.getRandomDateBetween(minDxDates, maxDxDates);

        record.put("dateOfDiagnosisYear", Integer.toString(randomDate.getYear()));
        record.put("dateOfDiagnosisMonth", Integer.toString(randomDate.getMonthValue()));
        record.put("dateOfDiagnosisDay", Integer.toString(randomDate.getDayOfMonth()));
    }
}
