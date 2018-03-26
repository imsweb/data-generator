/*
 * Copyright (C) 2016 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.naaccr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class NaaccrDataGeneratorRuleTest {

    private NaaccrDataGeneratorRule _rule = new NaaccrDataGeneratorRule("id", "name") {
        @Override
        public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        }
    };

    @Test
    public void testDxYearInRange() {
        Map<String, String> tumor = new HashMap<>();

        Assert.assertFalse(_rule.inDxYearRange(tumor, null, null));
        Assert.assertFalse(_rule.inDxYearRange(tumor, 2000, 2010));

        // put a value in the DX year
        tumor.put("dateOfDiagnosisYear", "2005");
        Assert.assertTrue(_rule.inDxYearRange(tumor, 2004, 2006));
        Assert.assertTrue(_rule.inDxYearRange(tumor, 2004, 2005));
        Assert.assertTrue(_rule.inDxYearRange(tumor, 2005, 2006));
        Assert.assertFalse(_rule.inDxYearRange(tumor, 2006, 2010));

        // check without lower bound on year
        tumor.put("dateOfDiagnosisYear", "1950");
        Assert.assertTrue(_rule.inDxYearRange(tumor, null, 2010));
        Assert.assertFalse(_rule.inDxYearRange(tumor, null, 1949));

        // check without upper bound on year
        tumor.put("dateOfDiagnosisYear", "3000");
        Assert.assertTrue(_rule.inDxYearRange(tumor, 2010, null));
        Assert.assertFalse(_rule.inDxYearRange(tumor, 3001, null));
    }

    @Test
    public void testGetCurrentYear() {
        Assert.assertNotNull(_rule.getCurrentYear());
    }

    @Test
    public void testPropertyHasValue() {
        Map<String, String> record = new HashMap<>();
        record.put("sec", "1");
        Assert.assertFalse(_rule.propertyHasValue(record, "sex"));
        record.put("sex", "");
        Assert.assertFalse(_rule.propertyHasValue(record, "sex"));
        record.put("sex", null);
        Assert.assertFalse(_rule.propertyHasValue(record, "sex"));
        record.put("sex", "1");
        Assert.assertTrue(_rule.propertyHasValue(record, "sex"));
    }
}
