package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.imsweb.naaccrxml.entity.Item;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

public class DateOfLastContactRuleTest {

    private final DateOfLastContactRule _rule = new DateOfLastContactRule();

    @Test
    public void testExecute() {
        Patient patient = new Patient();

        Tumor tumor = new Tumor();

        Map<String, Object> context = new HashMap<>();

        // for now, the date is just set to the DX date...
        tumor.addItem(new Item("dateOfDiagnosisYear", "2005"));
        tumor.addItem(new Item("dateOfDiagnosisMonth", "07"));
        tumor.addItem(new Item("dateOfDiagnosisDay", "04"));
        _rule.execute(tumor, patient, null, context);
        Assert.assertEquals("2005", patient.getItemValue("dateOfLastContactYear"));
        Assert.assertEquals("07", patient.getItemValue("dateOfLastContactMonth"));
        Assert.assertEquals("04", patient.getItemValue("dateOfLastContactDay"));

    }

    @Test
    public void testDeadPatientWithOutOfOrderDiagnosisDates() {
        Patient patient = new Patient();
        patient.addItem(new Item("vitalStatus", "0"));

        Tumor laterTumor = new Tumor();
        laterTumor.addItem(new Item("dateOfDiagnosisYear", "2006"));
        laterTumor.addItem(new Item("dateOfDiagnosisMonth", "08"));
        laterTumor.addItem(new Item("dateOfDiagnosisDay", "05"));
        _rule.execute(laterTumor, patient, null, new HashMap<>());

        Tumor earlierTumor = new Tumor();
        earlierTumor.addItem(new Item("dateOfDiagnosisYear", "2005"));
        earlierTumor.addItem(new Item("dateOfDiagnosisMonth", "07"));
        earlierTumor.addItem(new Item("dateOfDiagnosisDay", "04"));
        _rule.execute(earlierTumor, patient, null, new HashMap<>());

        Assert.assertEquals("2006", patient.getItemValue("dateOfLastContactYear"));
        Assert.assertEquals("08", patient.getItemValue("dateOfLastContactMonth"));
        Assert.assertEquals("05", patient.getItemValue("dateOfLastContactDay"));
    }
}
