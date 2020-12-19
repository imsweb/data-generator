package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.imsweb.naaccrxml.entity.Item;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

public class AgeRuleTest {

    private final AgeRule _rule = new AgeRule();

    @Test
    public void testExecute() {
        Patient patient = new Patient();

        Tumor tumor = new Tumor();

        // set birthday to 7/5/1980 and DX date to 11/5/2015 (when patient is 35 years old)
        patient.addItem(new Item("dateOfBirthYear", "1980"));
        patient.addItem(new Item("dateOfBirthMonth", "07"));
        patient.addItem(new Item("dateOfBirthDay", "05"));
        tumor.addItem(new Item("dateOfDiagnosisYear", "2015"));
        tumor.addItem(new Item("dateOfDiagnosisMonth", "11"));
        tumor.addItem(new Item("dateOfDiagnosisDay", "05"));
        Map<String, Object> context = new HashMap<>();
        _rule.execute(tumor, patient, null, context);
        Assert.assertEquals("035", tumor.getItemValue("ageAtDiagnosis"));

        // set DX day/month to birthday in 2015 and verify still 35
        tumor.getItem("dateOfDiagnosisMonth").setValue("07");
        tumor.getItem("dateOfDiagnosisDay").setValue("05");
        _rule.execute(tumor, patient, null, context);
        Assert.assertEquals("035", tumor.getItemValue("ageAtDiagnosis"));

        // set DX day/month to day before birthday in 2015 and verify age is now 34
        tumor.getItem("dateOfDiagnosisDay").setValue("04");
        _rule.execute(tumor, patient, null, context);
        Assert.assertEquals("034", tumor.getItemValue("ageAtDiagnosis"));
    }
}
