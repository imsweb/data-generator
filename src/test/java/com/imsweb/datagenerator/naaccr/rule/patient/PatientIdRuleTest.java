package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class PatientIdRuleTest {

    private PatientIdRule _rule = new PatientIdRule();

    @Test
    public void testExecute() {
        // create ten test patient records, and ensure that the patient Id is incrementing from one for each patient
        for (int i = 1; i <= 10; i++) {
            Map<String, String> rec = new HashMap<>();
            _rule.execute(rec, null, null);
            Assert.assertTrue(String.format("%08d", i).equals(rec.get("patientIdNumber")));
        }
    }
}
