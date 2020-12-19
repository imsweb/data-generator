package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

public class SequenceNumberCentralRuleTest {

    private final SequenceNumberCentralRule _rule = new SequenceNumberCentralRule();

    @Test
    public void testExecute() {
        Patient patient = new Patient();

        Map<String, Object> context = new HashMap<>();

        // create a single tumor for the patient and verify sequence number is 00
        Tumor tumor = new Tumor();
        _rule.execute(tumor, patient, null, context);
        Assert.assertEquals("00", tumor.getItemValue("sequenceNumberCentral"));
        patient.addTumor(tumor);

        // add a second tumor to the same patient
        Tumor tumor2 = new Tumor();
        _rule.execute(tumor2, patient, null, context);
        // verify the previous tumor seq. no. changed from 00 to 01
        Assert.assertEquals("01", patient.getTumor(0).getItemValue("sequenceNumberCentral"));
        // verify this new tumor's seq. no. is 02
        Assert.assertEquals("02", tumor2.getItemValue("sequenceNumberCentral"));
        patient.addTumor(tumor2);

        // add a third tumor to the same patient
        Tumor tumor3 = new Tumor();
        _rule.execute(tumor3, patient, null, context);
        // verify sequence numbers of previous two tumors
        Assert.assertEquals("01", patient.getTumor(0).getItemValue("sequenceNumberCentral"));
        Assert.assertEquals("02", patient.getTumor(1).getItemValue("sequenceNumberCentral"));
        // verify this new tumor's seq. no. is 03
        Assert.assertEquals("03", tumor3.getItemValue("sequenceNumberCentral"));
        patient.addTumor(tumor3);
    }
}
