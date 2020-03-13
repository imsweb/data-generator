package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.imsweb.algorithms.napiia.NapiiaInputRecordDto;
import com.imsweb.algorithms.napiia.NapiiaResultsDto;
import com.imsweb.algorithms.napiia.NapiiaUtils;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;

public class NapiiaRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "napiia";

    /**
     * Constructor.
     */
    public NapiiaRule() {
        super(ID, "NAPIIA");
    }

    @Override
    public List<String> getRequiredProperties() {
        return Arrays.asList("dateOfDiagnosisYear", "dateOfDiagnosisMonth", "dateOfDiagnosisDay");
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        NapiiaInputRecordDto input = new NapiiaInputRecordDto();
        input.setRace1(record.get("race1"));
        input.setRace2(record.get("race2"));
        input.setRace3(record.get("race3"));
        input.setRace4(record.get("race4"));
        input.setRace5(record.get("race5"));
        input.setSpanishHispanicOrigin(record.get("spanishHispanicOrigin"));
        input.setBirthplaceCountry(record.get("birthplaceCountry"));
        input.setSex(record.get("sex"));
        input.setNameLast(record.get("nameLast"));
        input.setNameMaiden(record.get("nameMaiden"));
        input.setNameFirst(record.get("nameFirst"));
        // this is using the algorithms library to compute the value...
        NapiiaResultsDto results = NapiiaUtils.computeNapiia(input);
        record.put("raceNapiia", results.getNapiiaValue());
    }
}
