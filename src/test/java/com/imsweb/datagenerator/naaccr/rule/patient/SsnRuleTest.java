package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.imsweb.naaccrxml.entity.Patient;

public class SsnRuleTest {

    private final SsnRule _rule = new SsnRule();

    @Test
    public void testExecute() {
        // test the social security number rule 10 times
        for (int i = 0; i < 10; i++) {
            Patient patient = new Patient();
            Map<String, Object> context = new HashMap<>();
            _rule.execute(patient, null, context);
            String ssn = patient.getItemValue("socialSecurityNumber");

            // verify SSN is non-null and nine digits long
            Assert.assertNotNull(ssn);
            Assert.assertTrue(ssn.matches("\\d{9}"));

            // verify first three digits are valid (001 - 649)
            int ssnAreaCode = Integer.parseInt(ssn.substring(0, 3));
            Assert.assertTrue(ssnAreaCode >= 1 && ssnAreaCode <= 649);
        }
    }
}
