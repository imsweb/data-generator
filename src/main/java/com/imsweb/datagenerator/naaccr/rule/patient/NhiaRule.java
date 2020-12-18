package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.imsweb.algorithms.nhia.NhiaInputRecordDto;
import com.imsweb.algorithms.nhia.NhiaResultsDto;
import com.imsweb.algorithms.nhia.NhiaUtils;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorPatientRule;
import com.imsweb.naaccrxml.entity.Patient;

public class NhiaRule extends NaaccrDataGeneratorPatientRule {

    // unique identifier for this rule
    public static final String ID = "nhia";

    /**
     * Constructor.
     */
    public NhiaRule() {
        super(ID, "NHIA");
    }

    @Override
    public void execute(Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        NhiaInputRecordDto input = new NhiaInputRecordDto();
        input.setSpanishHispanicOrigin(patient.getItemValue("spanishHispanicOrigin"));
        input.setBirthplaceCountry(patient.getItemValue("birthplaceCountry"));
        input.setSex(patient.getItemValue("sex"));
        input.setRace1(patient.getItemValue("race1"));
        input.setIhs(patient.getItemValue("ihsLink"));
        input.setNameLast(patient.getItemValue("nameLast"));
        input.setNameMaiden(StringUtils.isBlank(patient.getItemValue("nameBirthSurname")) ? patient.getItemValue("nameMaiden") : patient.getItemValue("nameBirthSurname"));
        input.setCountyAtDxAnalysis(patient.getItemValue("countyAtDxAnalysis"));
        input.setStateAtDx(patient.getItemValue("addrAtDxState"));
        // this is using the algorithms library to compute the result...
        NhiaResultsDto results = NhiaUtils.computeNhia(input, NhiaUtils.NHIA_OPTION_ALL_CASES);
        setValue(patient, "nhiaDerivedHispOrigin", results.getNhia());
    }
}
