package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class HispanicOriginRuleTest {

    private HispanicOriginRule _rule = new HispanicOriginRule();

    @Test
    public void testExecute() {
        // test the rule ten times
        for (int i = 0; i < 10; i++) {
            Map<String, String> rec = new HashMap<>();
            Map<String, Object> context = new HashMap<>();
            _rule.execute(rec, null, null, context);
            Assert.assertTrue(rec.get("spanishHispanicOrigin").matches("^\\d$"));
        }
    }
}