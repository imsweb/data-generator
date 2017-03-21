package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;
import com.imsweb.datagenerator.utils.RandomUtils;

public class SsnRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "social-security-number";

    /**
     * Constructor.
     */
    public SsnRule() {
        super(ID, "Social Security Number");
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {
        // three digit area code (001-649)
        String areaCode = String.format("%03d", RandomUtils.nextInt(648) + 1);
        // two digit group number (01-99)
        String groupNumber = String.format("%02d", RandomUtils.nextInt(98) + 1);
        // four single-digit serial numbers (0001-9999)
        String serialNumbers = String.format("%04d", RandomUtils.nextInt(9998) + 1);
        record.put("socialSecurityNumber", areaCode + groupNumber + serialNumbers);
    }
}
