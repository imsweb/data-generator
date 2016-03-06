package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class SiteRuleTest {

    private SiteRule _rule = new SiteRule();

    @Test
    public void testExecute() {
        // test the rule ten times, asserting that the execute() method assigns appropriate patterns for all three fields
        for (String sex : Arrays.asList("1", "2")) {
            for (int i = 0; i < 5; i++) {
                Map<String, String> rec = new HashMap<>();
                rec.put("sex", sex);
                _rule.execute(rec, null, null);
                // primary site must be 'C' followed by three digits; histology must be four digits, and behavior one digit
                Assert.assertTrue("Primary site value pattern match", rec.get("primarySite").matches("C\\d{3}"));
                Assert.assertTrue("Histology value pattern match", rec.get("histologyIcdO3").matches("\\d{4}"));
                Assert.assertTrue("Behavior value pattern match", rec.get("behaviorIcdO3").matches("\\d"));
            }
        }
    }
}
