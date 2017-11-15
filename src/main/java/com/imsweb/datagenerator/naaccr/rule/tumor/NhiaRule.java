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

    private static final String _DESCRIPTION = "NHIA is calculated based on Spanish/Hispanic Origin, Birthplace--Country, Sex, Race 1,<br/>"
            + "IHS, Last Name, Maiden Name, and State and County at DX.";

    /**
     * Constructor.
     */
    public NhiaRule() {
        super(ID, "NHIA");
    }

    @Override
    public String getDescription() {
        return _DESCRIPTION;
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {
        NhiaInputRecordDto input = new NhiaInputRecordDto();
        input.setSpanishHispanicOrigin(record.get("spanishHispanicOrigin"));
        input.setBirthplaceCountry(record.get("birthplaceCountry"));
        input.setSex(record.get("sex"));
        input.setRace1(record.get("race1"));
        input.setIhs(record.get("ihs"));
        input.setNameLast(record.get("nameLast"));
        input.setNameMaiden(record.get("nameMaiden"));
        input.setCountyAtDx(record.get("addressAtDxCounty"));
        input.setStateAtDx(record.get("addressAtDxState"));
        // this is using the algorithms library to compute the result...
        NhiaResultsDto results = NhiaUtils.computeNhia(input, NhiaUtils.NHIA_OPTION_ALL_CASES);
        record.put("nhia", results.getNhia());
    }
}
