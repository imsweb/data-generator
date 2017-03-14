package com.imsweb.datagenerator.naaccr.rule.tumor;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;

import java.util.List;
import java.util.Map;

public class RegistryIdRule extends NaaccrDataGeneratorRule {

    public static final String ID = "registry_id";

    public RegistryIdRule() {
        super(ID, "Registry ID");
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {
        record.put("registryId", options.getRegistryId() == null ? "" : options.getRegistryId());
    }
}
