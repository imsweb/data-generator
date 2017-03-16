package com.imsweb.datagenerator.naaccr.rule.tumor;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;

import java.util.List;
import java.util.Map;

public class RegistryIdRule extends NaaccrDataGeneratorRule {

    public static final String ID = "registry_id";

    private static final String _CRITERIA = "If a valid Registry ID is specified, every record will have this Registry ID.<br/>"
            + "If no Registry ID is specified, the data file will have no Registry ID values";

    public RegistryIdRule() {
        super(ID, "Registry ID");
    }

    @Override
    public String getCriteria() {
        return _CRITERIA;
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {
        if(options.getRegistryId() != null && !"".equals(options.getRegistryId()))
            record.put("registryId", options.getRegistryId());
    }
}
