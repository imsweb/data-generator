package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorTumorRule;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

public class AgeRule extends NaaccrDataGeneratorTumorRule {

    // unique identifier for this rule
    public static final String ID = "age-at-dx";

    /**
     * Constructor.
     */
    public AgeRule() {
        super(ID, "Age at DX");
    }

    @Override
    public List<String> getRequiredProperties() {
        return Arrays.asList("dateOfBirthYear", "dateOfBirthMonth", "dateOfBirthDay", "dateOfDiagnosisYear", "dateOfDiagnosisMonth", "dateOfDiagnosisDay");
    }

    @Override
    public void execute(Tumor tumor, Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        int birthYear = Integer.parseInt(patient.getItemValue("dateOfBirthYear"));
        int birthMonth = Integer.parseInt(patient.getItemValue("dateOfBirthMonth"));
        int birthDay = Integer.parseInt(patient.getItemValue("dateOfBirthDay"));
        LocalDate dateOfBirth = LocalDate.of(birthYear, birthMonth, birthDay);

        int dxYear = Integer.parseInt(tumor.getItemValue("dateOfDiagnosisYear"));
        int dxMonth = Integer.parseInt(tumor.getItemValue("dateOfDiagnosisMonth"));
        int dxDay = Integer.parseInt(tumor.getItemValue("dateOfDiagnosisDay"));
        LocalDate dxDate = LocalDate.of(dxYear, dxMonth, dxDay);

        setValue(tumor, "ageAtDiagnosis", String.format("%03d", ChronoUnit.YEARS.between(dateOfBirth, dxDate)));
    }
}
