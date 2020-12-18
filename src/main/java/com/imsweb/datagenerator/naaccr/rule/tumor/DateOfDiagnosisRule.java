package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorTumorRule;
import com.imsweb.datagenerator.utils.RandomUtils;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

import static com.imsweb.datagenerator.naaccr.NaaccrDataGenerator.CONTEXT_FLAG_AGE_GROUP_MAP;
import static com.imsweb.datagenerator.naaccr.NaaccrDataGenerator.CONTEXT_FLAG_CURRENT_TUMOR_INDEX;

public class DateOfDiagnosisRule extends NaaccrDataGeneratorTumorRule {

    // unique identifier for this rule
    public static final String ID = "date-of-diagnosis";

    /**
     * Constructor.
     */
    public DateOfDiagnosisRule() {
        super(ID, "Date of Diagnosis");
    }

    @Override
    public void execute(Tumor tumor, Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {

        // latest possible date set only by options if defined
        Set<LocalDate> maxDxDates = new HashSet<>();
        LocalDate maxDate = options == null ? LocalDate.now() : options.getMaxDxDate();
        maxDxDates.add(maxDate);

        Set<LocalDate> minDxDates = new HashSet<>();
        // never go before min date defined in options, or current date minus ten years if options not defined
        minDxDates.add(options == null ? LocalDate.now().minusYears(10) : options.getMinDxDate());
        // never go before the year of birth
        minDxDates.add(LocalDate.of(Integer.parseInt(patient.getItemValue("dateOfBirthYear")) + 1, 1, 1));
        // never go before dx date of patient's most recent tumor (if this isn't the first one)
        if (!patient.getTumors().isEmpty()) {
            Tumor lastTumor = patient.getTumor(patient.getTumors().size() - 1);
            minDxDates.add(LocalDate.of(
                    Integer.parseInt(lastTumor.getItemValue("dateOfDiagnosisYear")),
                    Integer.parseInt(lastTumor.getItemValue("dateOfDiagnosisMonth")),
                    Integer.parseInt(lastTumor.getItemValue("dateOfDiagnosisDay"))));
        }

        if (context.get(CONTEXT_FLAG_CURRENT_TUMOR_INDEX) != null) {
            int birthYear = Integer.parseInt(patient.getItemValue("dateOfBirthYear"));
            int birthMonth = Integer.parseInt(patient.getItemValue("dateOfBirthMonth"));
            int birthDay = Integer.parseInt(patient.getItemValue("dateOfBirthDay"));
            LocalDate dateOfBirth = LocalDate.of(birthYear, birthMonth, birthDay);

            // PROBLEM: This brakes 3 previous rules:
            // 1. Minimum date must be within 10 years of today.
            // 2. Options specify a minimum DX date.
            // 3. This tumor must be diagnosed after the previous ones for this patient.
            // From Fabian: Only #2 is required. Try to get all tumors to use this minimum. If that can't be done, at least one tumor must meet it.
            minDxDates.clear();
            maxDxDates.clear();

            int currentTumorIndex = (int)context.get(CONTEXT_FLAG_CURRENT_TUMOR_INDEX);
            @SuppressWarnings("unchecked")
            Map<Integer, Integer> ageGroupMap = (Map<Integer, Integer>)context.get(CONTEXT_FLAG_AGE_GROUP_MAP);
            minDxDates.add(dateOfBirth.plusYears((ageGroupMap.get(currentTumorIndex) * 10)));
            maxDxDates.add(maxDate);
        }

        LocalDate randomDate = RandomUtils.getRandomDateBetween(minDxDates, maxDxDates);

        setValue(tumor, "dateOfDiagnosisYear", Integer.toString(randomDate.getYear()));
        setValue(tumor, "dateOfDiagnosisMonth", Integer.toString(randomDate.getMonthValue()));
        setValue(tumor, "dateOfDiagnosisDay", Integer.toString(randomDate.getDayOfMonth()));
    }
}
