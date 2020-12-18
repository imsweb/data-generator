package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.imsweb.naaccrxml.entity.Patient;

public class PatientIdRuleTest {

    private final PatientIdRule _rule = new PatientIdRule();

    @Test
    public void testExecute() {
        // create ten test patient records, and ensure that the patient Id is incrementing from one for each patient
        for (int i = 1; i <= 10; i++) {
            Patient patient = new Patient();
            Map<String, Object> context = new HashMap<>();
            _rule.execute(patient, null, context);
            Assert.assertEquals(String.format("%08d", i), patient.getItemValue(("patientIdNumber")));
        }
    }
}
