package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

public class BirthRuleTest {

    private BirthRule _rule = new BirthRule();

    @Test
    public void testExecute() {
        Map<String, String> rec = new HashMap<>();
        _rule.execute(rec, null, null);

        // the birth date should have been assigned
        Assert.assertTrue(rec.get("birthDateYear").matches("\\d{4}"));
        Assert.assertTrue(rec.get("birthDateMonth").matches("\\d{1,2}"));
        Assert.assertTrue(rec.get("birthDateDay").matches("\\d{1,2}"));

        // it should be in a specific range (at least the year)
        int currentYear = LocalDate.now().getYear();
        int assignedYear = Integer.parseInt(rec.get("birthDateYear"));
        Assert.assertTrue(assignedYear >= currentYear - 100 && assignedYear <= currentYear - 5);

        // the country should have been assigned
        Assert.assertEquals("USA", rec.get("birthplaceCountry"));

        // if no state is provided, the birth state should be unknown
        Assert.assertEquals("XX", rec.get("birthplaceState"));

        // if a state is provided, the birth state should not be blank
        rec.put("addressCurrentState", "MD");
        _rule.execute(rec, null, null);
        Assert.assertEquals("MD", rec.get("birthplaceState"));
    }
}
