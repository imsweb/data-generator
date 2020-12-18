package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.imsweb.algorithms.napiia.NapiiaInputRecordDto;
import com.imsweb.algorithms.napiia.NapiiaResultsDto;
import com.imsweb.algorithms.napiia.NapiiaUtils;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorPatientRule;
import com.imsweb.naaccrxml.entity.Patient;

public class NapiiaRule extends NaaccrDataGeneratorPatientRule {

    // unique identifier for this rule
    public static final String ID = "napiia";

    /**
     * Constructor.
     */
    public NapiiaRule() {
        super(ID, "NAPIIA");
    }

    @Override
    public void execute(Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        NapiiaInputRecordDto input = new NapiiaInputRecordDto();
        input.setRace1(patient.getItemValue("race1"));
        input.setRace2(patient.getItemValue("race2"));
        input.setRace3(patient.getItemValue("race3"));
        input.setRace4(patient.getItemValue("race4"));
        input.setRace5(patient.getItemValue("race5"));
        input.setSpanishHispanicOrigin(patient.getItemValue("spanishHispanicOrigin"));
        input.setBirthplaceCountry(patient.getItemValue("birthplaceCountry"));
        input.setSex(patient.getItemValue("sex"));
        input.setNameLast(patient.getItemValue("nameLast"));
        input.setNameMaiden(StringUtils.isBlank(patient.getItemValue("nameBirthSurname")) ? patient.getItemValue("nameMaiden") : patient.getItemValue("nameBirthSurname"));
        input.setNameFirst(patient.getItemValue("nameFirst"));
        // this is using the algorithms library to compute the value...
        NapiiaResultsDto results = NapiiaUtils.computeNapiia(input);
        setValue(patient, "raceNapiia", results.getNapiiaValue());
    }
}
