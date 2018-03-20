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

        // Use of a context
        Map<String, String> context = new HashMap<>();
        context.put("sex", "1");
        context.put("currentTumor", "0");
        context.put("tumor0 primarySite", "C000");
        context.put("tumor0 histologyIcdO3", "8070");
        context.put("tumor0 behaviorIcdO3", "3");

        Map<String, String> rec = new HashMap<>();
        rec.put("sex", "1");
        _rule.execute(rec, null, null, context);

        Assert.assertTrue(rec.get("primarySite").equals("C000"));
        Assert.assertTrue(rec.get("histologyIcdO3").equals("8070"));
        Assert.assertTrue(rec.get("behaviorIcdO3").equals("3"));
        Assert.assertTrue(rec.get("grade").equals("9"));
        Assert.assertTrue(rec.get("laterality").equals("9"));

        context = new HashMap<>();
        context.put("sex", "1");
        context.put("currentTumor", "0");
        context.put("tumor0 primarySite", "C809");
        context.put("tumor0 histologyIcdO3", "8111");
        context.put("tumor0 behaviorIcdO3", "9");

        rec = new HashMap<>();
        rec.put("sex", "1");
        _rule.execute(rec, null, null, context);

        Assert.assertTrue(rec.get("primarySite").equals("C809"));
        Assert.assertTrue(rec.get("histologyIcdO3").equals("8111"));
        Assert.assertTrue(rec.get("behaviorIcdO3").equals("9"));
        Assert.assertTrue(rec.get("grade").equals("9"));
        Assert.assertTrue(rec.get("laterality").equals("0"));
    }
}
