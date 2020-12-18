package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.imsweb.naaccrxml.entity.Patient;

public class RaceRuleTest {

    private final RaceRule _rule = new RaceRule();

    @Test
    public void testExecute() {

        List<String> validRace1 = new ArrayList<>(Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "10", "11", "12", "13",
                "14", "15", "16", "17", "20", "21", "22", "25", "26", "27", "28", "30", "31", "32", "96", "97", "98", "99"));

        List<String> validRace2345 = new ArrayList<>(validRace1);
        validRace2345.add("88");

        // test the rule 5 times, asserting that the execute() method always assigns a valid race code to the patient
        for (int i = 0; i < 5; i++) {
            Patient patient = new Patient();
            Map<String, Object> context = new HashMap<>();
            _rule.execute(patient, null, context);
            Assert.assertTrue("race1 valid code", validRace1.contains(patient.getItemValue("race1")));
            Assert.assertTrue("race2 valid code", validRace2345.contains(patient.getItemValue("race2")));
            Assert.assertTrue("race3 valid code", validRace2345.contains(patient.getItemValue("race3")));
            Assert.assertTrue("race4 valid code", validRace2345.contains(patient.getItemValue("race4")));
            Assert.assertTrue("race5 valid code", validRace2345.contains(patient.getItemValue("race5")));
        }
    }
}
