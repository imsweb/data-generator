package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.joda.time.Years;

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
        return Arrays.asList("birthDateYear", "birthDateMonth", "birthDateDay", "dateOfDiagnosisYear", "dateOfDiagnosisMonth", "dateOfDiagnosisDay");
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {
        Integer birthYear = Integer.valueOf(record.get("birthDateYear"));
        Integer birthMonth = Integer.valueOf(record.get("birthDateMonth"));
        Integer birthDay = Integer.valueOf(record.get("birthDateDay"));
        LocalDate birthDate = new LocalDate(birthYear, birthMonth, birthDay);

        Integer dxYear = Integer.valueOf(record.get("dateOfDiagnosisYear"));
        Integer dxMonth = Integer.valueOf(record.get("dateOfDiagnosisMonth"));
        Integer dxDay = Integer.valueOf(record.get("dateOfDiagnosisDay"));
        LocalDate dxDate = new LocalDate(dxYear, dxMonth, dxDay);

        record.put("ageAtDx", String.format("%03d", Years.yearsBetween(birthDate, dxDate).getYears()));
    }
}
