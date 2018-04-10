package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class ComputedEthnicityRuleTest {

    private ComputedEthnicityRule _rule = new ComputedEthnicityRule();

    @Test
    public void testExecute() {
        Map<String, String> rec = new HashMap<>();
        Map<String, Object> context = new HashMap<>();

        // computedEthnicity = 1 (non-hispanic last name, non-hispanic maiden name)
        rec.put("sex", "2");
        rec.put("nameLast", "JOHNSON");
        rec.put("nameMaiden", "MILLER");
        _rule.execute(rec, null, null, context);
        Assert.assertTrue(rec.get("computedEthnicity").equals("1"));
        rec = new HashMap<>();

        // computedEthnicity = 2 (non-hispanic last name, did not check maiden name; or patient was male)
        rec.put("sex", "1");
        rec.put("nameLast", "JOHNSON");
        rec.put("nameMaiden", "");
        _rule.execute(rec, null, null, context);
        Assert.assertTrue(rec.get("computedEthnicity").equals("2"));
        rec = new HashMap<>();

        // computedEthnicity = 3 (non-hispanic last name, missing maiden name)
        rec.put("sex", "2");
        rec.put("nameLast", "JOHNSON");
        rec.put("nameMaiden", "");
        _rule.execute(rec, null, null, context);
        Assert.assertTrue(rec.get("computedEthnicity").equals("3"));
        rec = new HashMap<>();

        // computedEthnicity = 4 (hispanic last name, non-hispanic maiden name)
        rec.put("sex", "2");
        rec.put("nameLast", "ZUZUARREGUI");
        rec.put("nameMaiden", "MILLER");
        _rule.execute(rec, null, null, context);
        Assert.assertTrue(rec.get("computedEthnicity").equals("4"));
        rec = new HashMap<>();

        // computedEthnicity = 5 (hispanic last name, did not check maiden name; or patient was male)
        rec.put("sex", "1");
        rec.put("nameLast", "ZUZUARREGUI");
        rec.put("nameMaiden", "");
        _rule.execute(rec, null, null, context);
        Assert.assertTrue(rec.get("computedEthnicity").equals("5"));
        rec = new HashMap<>();

        // computedEthnicity = 6 (hispanic last name, missing maiden name)
        rec.put("sex", "2");
        rec.put("nameLast", "ZUZUARREGUI");
        rec.put("nameMaiden", "");
        _rule.execute(rec, null, null, context);
        Assert.assertTrue(rec.get("computedEthnicity").equals("6"));
        rec = new HashMap<>();

        // computedEthnicity = 7 (female, hispanic maiden name - last name doesn't matter)
        rec.put("sex", "2");
        rec.put("nameLast", "");
        rec.put("nameMaiden", "ABAD");
        _rule.execute(rec, null, null, context);
        Assert.assertTrue(rec.get("computedEthnicity").equals("7"));

        // verify computed ethnicity source (constant, 2)
        Assert.assertTrue(rec.get("computedEthnicitySource").equals("2"));
    }
}
