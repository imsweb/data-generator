package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class SexRuleTest {

    private SexRule _rule = new SexRule();

    @Test
    public void testExecute() {

        List<String> validSex = new ArrayList<>(Arrays.asList("1", "2"));

        // test the rule ten times, asserting that the execute() method always assigns a valid sex code to the patient
        for (int i = 0; i < 10; i++) {
            Map<String, String> rec = new HashMap<>();
            _rule.execute(rec, null, null);
            Assert.assertTrue(validSex.contains(rec.get("sex")));
        }
    }
}
