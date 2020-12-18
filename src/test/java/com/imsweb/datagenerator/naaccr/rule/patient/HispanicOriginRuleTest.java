package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.imsweb.naaccrxml.entity.Patient;

public class HispanicOriginRuleTest {

    private final HispanicOriginRule _rule = new HispanicOriginRule();

    @Test
    public void testExecute() {
        // test the rule ten times
        for (int i = 0; i < 10; i++) {
            Patient patient = new Patient();
            Map<String, Object> context = new HashMap<>();
            _rule.execute(patient, null, context);
            Assert.assertTrue(patient.getItemValue("spanishHispanicOrigin").matches("^\\d$"));
        }
    }
}