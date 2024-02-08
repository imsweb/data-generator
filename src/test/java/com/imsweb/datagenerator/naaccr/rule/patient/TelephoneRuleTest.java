package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.imsweb.naaccrxml.entity.Patient;

public class TelephoneRuleTest {

    private final TelephoneRule _rule = new TelephoneRule();

    @Test
    public void testExecute() {
        Patient patient = new Patient();
        Map<String, Object> context = new HashMap<>();
        _rule.execute(patient, null, context);
        Assert.assertNotNull(patient.getItemValue("telephone"));
    }
}