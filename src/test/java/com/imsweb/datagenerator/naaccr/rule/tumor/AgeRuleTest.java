package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class AgeRuleTest {

    private AgeRule _rule = new AgeRule();

    @Test
    public void testExecute() {
        List<Map<String, String>> otherRecords = new ArrayList<>();

        // set birthday to 7/5/1980 and DX date to 11/5/2015 (when patient is 35 years old)
        Map<String, String> rec = new HashMap<>();
        rec.put("birthDateYear", "1980");
        rec.put("birthDateMonth", "07");
        rec.put("birthDateDay", "05");
        rec.put("birthDateFlag", "");
        rec.put("dateOfDiagnosisYear", "2015");
        rec.put("dateOfDiagnosisMonth", "11");
        rec.put("dateOfDiagnosisDay", "05");
        rec.put("dateOfDiagnosisFlag", "");
        Map<String, Object> context = new HashMap<>();
        _rule.execute(rec, otherRecords, null, context);
        Assert.assertEquals("035", rec.get("ageAtDx"));

        // set DX day/month to birthday in 2015 and verify still 35
        rec.remove("ageAtDx");
        rec.put("dateOfDiagnosisMonth", "07");
        rec.put("dateOfDiagnosisDay", "05");
        _rule.execute(rec, otherRecords, null, context);
        Assert.assertEquals("035", rec.get("ageAtDx"));

        // set DX day/month to day before birthday in 2015 and verify age is now 34
        rec.remove("ageAtDx");
        rec.put("dateOfDiagnosisDay", "04");
        _rule.execute(rec, otherRecords, null, context);
        Assert.assertEquals("034", rec.get("ageAtDx"));
    }
}
