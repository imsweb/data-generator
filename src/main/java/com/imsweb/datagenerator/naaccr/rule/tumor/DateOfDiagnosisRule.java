package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.time.LocalDate;
import java.util.Map;

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

        // the date range requested in the options is a hard requirement; the other constraints (age group of the site, year of birth, dx date of the
        // previous tumor) are only applied when they don't push the generated date outside of that range
        LocalDate maxDate = options == null ? LocalDate.now() : options.getMaxDxDate();
        LocalDate minDate = options == null ? LocalDate.now().minusYears(10) : options.getMinDxDate();
        // the min dx date defaults to ten years ago when it's not provided in the options; that default can end up after a requested max dx date
        if (minDate.isAfter(maxDate))
            minDate = maxDate.minusYears(10);

        // never go before the year of birth
        if (hasValue(patient, "dateOfBirthYear"))
            minDate = tightenMinDate(minDate, maxDate, LocalDate.of(Integer.parseInt(patient.getItemValue("dateOfBirthYear")) + 1, 1, 1));

        // never go before dx date of patient's most recent tumor (if this isn't the first one)
        if (!patient.getTumors().isEmpty()) {
            Tumor lastTumor = patient.getTumor(patient.getTumors().size() - 1);
            minDate = tightenMinDate(minDate, maxDate, LocalDate.of(
                    Integer.parseInt(lastTumor.getItemValue("dateOfDiagnosisYear")),
                    Integer.parseInt(lastTumor.getItemValue("dateOfDiagnosisMonth")),
                    Integer.parseInt(lastTumor.getItemValue("dateOfDiagnosisDay"))));
        }

        // never diagnose the tumor before the patient reaches the age group that was picked for its site
        @SuppressWarnings("unchecked")
        Map<Integer, Integer> ageGroupMap = (Map<Integer, Integer>)context.get(CONTEXT_FLAG_AGE_GROUP_MAP);
        Integer currentTumorIndex = (Integer)context.get(CONTEXT_FLAG_CURRENT_TUMOR_INDEX);
        if (ageGroupMap != null && currentTumorIndex != null && hasValue(patient, "dateOfBirthYear", "dateOfBirthMonth", "dateOfBirthDay")) {
            Integer ageGroup = ageGroupMap.get(currentTumorIndex);
            // the age group is -1 for a site that has no age distribution, in which case it tells us nothing about the dx date
            if (ageGroup != null && ageGroup > 0) {
                LocalDate dateOfBirth = LocalDate.of(
                        Integer.parseInt(patient.getItemValue("dateOfBirthYear")),
                        Integer.parseInt(patient.getItemValue("dateOfBirthMonth")),
                        Integer.parseInt(patient.getItemValue("dateOfBirthDay")));
                minDate = tightenMinDate(minDate, maxDate, dateOfBirth.plusYears(ageGroup * 10L));
            }
        }

        LocalDate randomDate = RandomUtils.getRandomDateBetween(minDate, maxDate);

        setValue(tumor, "dateOfDiagnosisYear", Integer.toString(randomDate.getYear()));
        setValue(tumor, "dateOfDiagnosisMonth", Integer.toString(randomDate.getMonthValue()));
        setValue(tumor, "dateOfDiagnosisDay", Integer.toString(randomDate.getDayOfMonth()));
    }

    /**
     * Returns the candidate date if it is a tighter minimum than the current one and still leaves a valid range, the current minimum otherwise.
     */
    private static LocalDate tightenMinDate(LocalDate currentMinDate, LocalDate maxDate, LocalDate candidate) {
        if (candidate.isAfter(currentMinDate) && !candidate.isAfter(maxDate))
            return candidate;
        return currentMinDate;
    }
}
