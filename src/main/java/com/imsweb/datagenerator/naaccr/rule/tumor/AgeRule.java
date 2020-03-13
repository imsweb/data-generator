package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;

public class AgeRule extends NaaccrDataGeneratorRule {

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
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        int birthYear = Integer.parseInt(record.get("dateOfBirthYear"));
        int birthMonth = Integer.parseInt(record.get("dateOfBirthMonth"));
        int birthDay = Integer.parseInt(record.get("dateOfBirthDay"));
        LocalDate dateOfBirth = LocalDate.of(birthYear, birthMonth, birthDay);

        int dxYear = Integer.parseInt(record.get("dateOfDiagnosisYear"));
        int dxMonth = Integer.parseInt(record.get("dateOfDiagnosisMonth"));
        int dxDay = Integer.parseInt(record.get("dateOfDiagnosisDay"));
        LocalDate dxDate = LocalDate.of(dxYear, dxMonth, dxDay);

        record.put("ageAtDiagnosis", String.format("%03d", ChronoUnit.YEARS.between(dateOfBirth, dxDate)));
    }
}
