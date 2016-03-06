package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class DateOfLastContactRuleTest {

    private DateOfLastContactRule _rule = new DateOfLastContactRule();

    @Test
    public void testExecute() {
        List<Map<String, String>> otherRecords = new ArrayList<>();

        Map<String, String> rec = new HashMap<>();

        // for now, the date is just set to the DX date...
        rec.put("dateOfDiagnosisYear", "2005");
        rec.put("dateOfDiagnosisMonth", "07");
        rec.put("dateOfDiagnosisDay", "04");
        _rule.execute(rec, otherRecords, null);
        Assert.assertEquals("2005", rec.get("dateOfLastContactYear"));
        Assert.assertEquals("07", rec.get("dateOfLastContactMonth"));
        Assert.assertEquals("04", rec.get("dateOfLastContactDay"));

    }
}
