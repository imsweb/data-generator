package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class SequenceNumberCentralRuleTest {

    private SequenceNumberCentralRule _rule = new SequenceNumberCentralRule();

    @Test
    public void testExecute() {

        List<Map<String, String>> otherRecords = new ArrayList<>();
        Map<String, Object> context = new HashMap<>();

        // create a single tumor for the patient and verify sequence number is 00
        Map<String, String> rec = new HashMap<>();
        _rule.execute(rec, otherRecords, null, context);
        Assert.assertTrue(rec.get("sequenceNumberCentral").equals("00"));
        otherRecords.add(rec);

        // add a second tumor to the same patient
        rec = new HashMap<>();
        _rule.execute(rec, otherRecords, null, context);
        otherRecords.add(rec);
        // verify the previous tumor seq. no. changed from 00 to 01
        Assert.assertTrue(otherRecords.get(0).get("sequenceNumberCentral").equals("01"));
        // verify this new tumor's seq. no. is 02
        Assert.assertTrue(rec.get("sequenceNumberCentral").equals("02"));

        // add a third tumor to the same patient
        rec = new HashMap<>();
        _rule.execute(rec, otherRecords, null, context);
        otherRecords.add(rec);
        // verify sequence numbers of previous two tumors
        Assert.assertTrue(otherRecords.get(0).get("sequenceNumberCentral").equals("01"));
        Assert.assertTrue(otherRecords.get(1).get("sequenceNumberCentral").equals("02"));
        // verify this new tumor's seq. no. is 03
        Assert.assertTrue(rec.get("sequenceNumberCentral").equals("03"));
    }
}
