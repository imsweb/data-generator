/*
 * Copyright (C) 2016 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.naaccr;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.imsweb.naaccrxml.entity.Item;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

public class NaaccrDataGeneratorRuleTest {

    private final NaaccrDataGeneratorTumorRule _rule = new NaaccrDataGeneratorTumorRule("id", "name") {
        @Override
        public void execute(Tumor tumor, Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
            // test is meant to unit test other methods, not main execute one...
        }
    };

    @Test
    public void testDxYearInRange() {
        Tumor tumor = new Tumor();

        Assert.assertFalse(_rule.inDxYearRange(tumor, null, null));
        Assert.assertFalse(_rule.inDxYearRange(tumor, 2000, 2010));

        // put a value in the DX year
        tumor.addItem(new Item("dateOfDiagnosisYear", "2005"));
        Assert.assertTrue(_rule.inDxYearRange(tumor, 2004, 2006));
        Assert.assertTrue(_rule.inDxYearRange(tumor, 2004, 2005));
        Assert.assertTrue(_rule.inDxYearRange(tumor, 2005, 2006));
        Assert.assertFalse(_rule.inDxYearRange(tumor, 2006, 2010));

        // check without lower bound on year
        tumor.getItem("dateOfDiagnosisYear").setValue("1950");
        Assert.assertTrue(_rule.inDxYearRange(tumor, null, 2010));
        Assert.assertFalse(_rule.inDxYearRange(tumor, null, 1949));

        // check without upper bound on year
        tumor.getItem("dateOfDiagnosisYear").setValue("3000");
        Assert.assertTrue(_rule.inDxYearRange(tumor, 2010, null));
        Assert.assertFalse(_rule.inDxYearRange(tumor, 3001, null));
    }

    @Test
    public void testPropertyHasValue() {
        Patient patient = new Patient();
        Assert.assertFalse(_rule.hasValue(patient, "sex"));
        patient.addItem(new Item("sex", ""));
        Assert.assertFalse(_rule.hasValue(patient, "sex"));
        patient.getItem("sex").setValue(null);
        Assert.assertFalse(_rule.hasValue(patient, "sex"));
        patient.getItem("sex").setValue("1");
        Assert.assertTrue(_rule.hasValue(patient, "sex"));
    }
}
