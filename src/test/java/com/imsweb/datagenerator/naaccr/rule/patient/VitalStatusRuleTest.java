package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class VitalStatusRuleTest {

    private VitalStatusRule _rule = new VitalStatusRule();

    @Test
    public void testExecute() {

        List<String> validVs = new ArrayList<>(Arrays.asList("0", "1", "4"));

        // test the rule ten times, asserting that the execute() method always assigns a valid vital status code to the patient
        for (int i = 0; i < 10; i++) {
            Map<String, String> rec = new HashMap<>();
            Map<String, Object> context = new HashMap<>();
            _rule.execute(rec, null, null, context);
            Assert.assertTrue(validVs.contains(rec.get("vitalStatus")));
        }
    }
}
