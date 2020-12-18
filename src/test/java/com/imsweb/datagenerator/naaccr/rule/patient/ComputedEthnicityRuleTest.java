package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.imsweb.naaccrxml.entity.Item;
import com.imsweb.naaccrxml.entity.Patient;

public class ComputedEthnicityRuleTest {

    private final ComputedEthnicityRule _rule = new ComputedEthnicityRule();

    @Test
    public void testExecute() {
        Patient patient = new Patient();

        Map<String, Object> context = new HashMap<>();

        // computedEthnicity = 1 (non-hispanic last name, non-hispanic maiden name)
        patient.addItem(new Item("sex", "2"));
        patient.addItem(new Item("nameLast", "JOHNSON"));
        patient.addItem(new Item("nameMaiden", "MILLER"));
        _rule.execute(patient, null, context);
        Assert.assertEquals("1", patient.getItemValue("computedEthnicity"));
        patient = new Patient();

        // computedEthnicity = 2 (non-hispanic last name, did not check maiden name; or patient was male)
        patient.addItem(new Item("sex", "1"));
        patient.addItem(new Item("nameLast", "JOHNSON"));
        patient.addItem(new Item("nameMaiden", ""));
        _rule.execute(patient, null, context);
        Assert.assertEquals("2", patient.getItemValue("computedEthnicity"));
        patient = new Patient();

        // computedEthnicity = 3 (non-hispanic last name, missing maiden name)
        patient.addItem(new Item("sex", "2"));
        patient.addItem(new Item("nameLast", "JOHNSON"));
        patient.addItem(new Item("nameMaiden", ""));
        _rule.execute(patient, null, context);
        Assert.assertEquals("3", patient.getItemValue("computedEthnicity"));
        patient = new Patient();

        // computedEthnicity = 4 (hispanic last name, non-hispanic maiden name)
        patient.addItem(new Item("sex", "2"));
        patient.addItem(new Item("nameLast", "ZUZUARREGUI"));
        patient.addItem(new Item("nameMaiden", "MILLER"));
        _rule.execute(patient, null, context);
        Assert.assertEquals("4", patient.getItemValue("computedEthnicity"));
        patient = new Patient();

        // computedEthnicity = 5 (hispanic last name, did not check maiden name; or patient was male)
        patient.addItem(new Item("sex", "1"));
        patient.addItem(new Item("nameLast", "ZUZUARREGUI"));
        patient.addItem(new Item("nameMaiden", ""));
        _rule.execute(patient, null, context);
        Assert.assertEquals("5", patient.getItemValue("computedEthnicity"));
        patient = new Patient();

        // computedEthnicity = 6 (hispanic last name, missing maiden name)
        patient.addItem(new Item("sex", "2"));
        patient.addItem(new Item("nameLast", "ZUZUARREGUI"));
        patient.addItem(new Item("nameMaiden", ""));
        _rule.execute(patient, null, context);
        Assert.assertEquals("6", patient.getItemValue("computedEthnicity"));
        patient = new Patient();

        // computedEthnicity = 7 (female, hispanic maiden name - last name doesn't matter)
        patient.addItem(new Item("sex", "2"));
        patient.addItem(new Item("nameLast", ""));
        patient.addItem(new Item("nameMaiden", "ABAD"));
        _rule.execute(patient, null, context);
        Assert.assertEquals("7", patient.getItemValue("computedEthnicity"));

        // verify computed ethnicity source (constant, 2)
        Assert.assertEquals("2", patient.getItemValue("computedEthnicitySource"));
    }
}
