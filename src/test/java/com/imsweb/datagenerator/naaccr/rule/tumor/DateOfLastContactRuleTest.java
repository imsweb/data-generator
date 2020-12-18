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
        patient.addTumor(tumor);

        Map<String, Object> context = new HashMap<>();

        // for now, the date is just set to the DX date...
        tumor.addItem(new Item("dateOfDiagnosisYear", "2005"));
        tumor.addItem(new Item("dateOfDiagnosisMonth", "07"));
        tumor.addItem(new Item("dateOfDiagnosisDay", "04"));
        _rule.execute(tumor, patient, null, context);
        Assert.assertEquals("2005", tumor.getItemValue("dateOfLastContactYear"));
        Assert.assertEquals("07", tumor.getItemValue("dateOfLastContactMonth"));
        Assert.assertEquals("04", tumor.getItemValue("dateOfLastContactDay"));

    }
}
