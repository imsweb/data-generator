/*
 * Copyright (C) 2017 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;

public class RegistryIdRuleTest {

    private RegistryIdRule _rule = new RegistryIdRule();

    @Test
    public void testExecute(){
        Map<String, String> record = new HashMap<>();
        Map<String, Object> context = new HashMap<>();
        NaaccrDataGeneratorOptions options = new NaaccrDataGeneratorOptions();

        // options has no registryId
        _rule.execute(record, null, options, context);
        Assert.assertFalse(record.containsKey("registryId"));

        // options has empty string
        options.setRegistryId("");
        _rule.execute(record, null, options, context);
        Assert.assertFalse(record.containsKey("registryId"));

        // options has a valid value for registryId
        options.setRegistryId("REGISTRY");
        _rule.execute(record, null, options, context);
        Assert.assertEquals("REGISTRY", record.get("registryId"));
    }
}
