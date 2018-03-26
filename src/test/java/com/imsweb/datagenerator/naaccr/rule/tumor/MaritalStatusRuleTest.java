package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class MaritalStatusRuleTest {

    private MaritalStatusRule _rule = new MaritalStatusRule();

    @Test
    public void testExecute() {

        Map<String, Object> context = new HashMap<>();

        // test with no maiden name
        for (int i = 0; i < 5; i++) {
            Map<String, String> rec = new HashMap<>();
            rec.put("nameMaiden", "");
            _rule.execute(rec, null, null, context);
            int maritalStatus = Integer.parseInt(rec.get("maritalStatusAtDx"));
            Assert.assertTrue(maritalStatus >= 1 && maritalStatus <= 6);
        }

        // test with maiden name
        for (int i = 0; i < 5; i++) {
            Map<String, String> rec = new HashMap<>();
            rec.put("nameMaiden", "MaidenNameTest");
            _rule.execute(rec, null, null, context);
            int maritalStatus = Integer.parseInt(rec.get("maritalStatusAtDx"));
            Assert.assertTrue(maritalStatus >= 2 && maritalStatus <= 5);
        }
    }
}
