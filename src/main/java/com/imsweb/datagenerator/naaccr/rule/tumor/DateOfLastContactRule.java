package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorTumorRule;
import com.imsweb.naaccrxml.entity.AbstractEntity;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

public class DateOfLastContactRule extends NaaccrDataGeneratorTumorRule {

    // unique identifier for this rule
    public static final String ID = "date-of-last-contact";

    /**
     * Constructor.
     */
    public DateOfLastContactRule() {
        super(ID, "Date of Last Contact");
    }

    @Override
    public List<String> getRequiredProperties() {
        return Arrays.asList("dateOfDiagnosisYear", "dateOfDiagnosisMonth", "dateOfDiagnosisDay");
    }

    @Override
    public void execute(Tumor tumor, Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {

        LocalDate diagnosisDate = getDate(tumor, "dateOfDiagnosis");
        LocalDate lastContactDate = getDate(patient, "dateOfLastContact");

        // Diagnosis dates are not guaranteed to be generated in chronological order, so retain the latest one.
        if (diagnosisDate != null && (lastContactDate == null || diagnosisDate.isAfter(lastContactDate))) {
            setValue(patient, "dateOfLastContactYear", tumor.getItemValue("dateOfDiagnosisYear"));
            setValue(patient, "dateOfLastContactMonth", tumor.getItemValue("dateOfDiagnosisMonth"));
            setValue(patient, "dateOfLastContactDay", tumor.getItemValue("dateOfDiagnosisDay"));
        }
    }

    private LocalDate getDate(AbstractEntity entity, String property) {
        String year = entity.getItemValue(property + "Year");
        String month = entity.getItemValue(property + "Month");
        String day = entity.getItemValue(property + "Day");
        if (year == null || month == null || day == null)
            return null;
        return LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
    }
}
