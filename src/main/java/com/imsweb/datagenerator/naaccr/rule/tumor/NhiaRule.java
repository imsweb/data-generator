package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.List;
import java.util.Map;

import com.imsweb.algorithms.nhia.NhiaInputRecordDto;
import com.imsweb.algorithms.nhia.NhiaResultsDto;
import com.imsweb.algorithms.nhia.NhiaUtils;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;

public class NhiaRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "nhia";

    /**
     * Constructor.
     */
    public NhiaRule() {
        super(ID, "NHIA");
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        NhiaInputRecordDto input = new NhiaInputRecordDto();
        input.setSpanishHispanicOrigin(record.get("spanishHispanicOrigin"));
        input.setBirthplaceCountry(record.get("birthplaceCountry"));
        input.setSex(record.get("sex"));
        input.setRace1(record.get("race1"));
        input.setIhs(record.get("ihsLink"));
        input.setNameLast(record.get("nameLast"));
        input.setNameMaiden(record.get("nameMaiden"));
        input.setCountyAtDxAnalysis(record.get("countyAtDxAnalysis"));
        input.setStateAtDx(record.get("addrAtDxState"));
        // this is using the algorithms library to compute the result...
        NhiaResultsDto results = NhiaUtils.computeNhia(input, NhiaUtils.NHIA_OPTION_ALL_CASES);
        record.put("nhiaDerivedHispOrigin", results.getNhia());
    }
}
