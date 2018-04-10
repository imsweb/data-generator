/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.provider.physician.rule;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.imsweb.datagenerator.provider.ProviderDataGeneratorOptions;

public class NameRuleTest {

    private NameRule _rule = new NameRule();

    @Test
    public void testExecute() {

        for (String state : Arrays.asList("WA", "ND", "MD")) {

            Map<String, String> provider = new HashMap<>();
            ProviderDataGeneratorOptions options = new ProviderDataGeneratorOptions();
            options.setState(state);
            _rule.execute(provider, options);

            Assert.assertTrue(provider.get("npi").matches("\\d{10}"));
            Assert.assertTrue(provider.get("nameLast").length() > 0);
            Assert.assertTrue(provider.get("nameFirst").length() > 0);
            Assert.assertTrue(provider.get("addressState").equals(state));

        }
    }
}
