package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.HashMap;
import java.util.Map;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;

public class BirthRuleTest {

    private BirthRule _rule = new BirthRule();

    @Test
    public void testExecute() {
        Map<String, String> rec = new HashMap<>();
        NaaccrDataGeneratorOptions options = new NaaccrDataGeneratorOptions();
        options.setMinDxYear(2000);
        _rule.execute(rec, null, options);

        // the birth date should have been assigned
        Assert.assertTrue(rec.get("birthDateYear").matches("\\d{4}"));
        Assert.assertTrue(rec.get("birthDateMonth").matches("\\d{1,2}"));
        Assert.assertTrue(rec.get("birthDateDay").matches("\\d{1,2}"));

        // it should be in a specific range
        LocalDate assignedDate = LocalDate.of(Integer.parseInt(rec.get("birthDateYear")), Integer.parseInt(rec.get("birthDateMonth")), Integer.parseInt(rec.get("birthDateDay")));
        Assert.assertTrue(assignedDate.toString(), assignedDate.isBefore(options.getMinDxDate().minusYears(5).plusDays(1)) &&
                assignedDate.isAfter(options.getMinDxDate().minusYears(105).minusDays(1)));

        // the country should have been assigned
        Assert.assertEquals("USA", rec.get("birthplaceCountry"));
        // state is random but should always be assigned
        Assert.assertTrue(rec.get("birthplaceState").matches("[A-Z]{2}"));

        // Use of a context
        Map<String, String> context = new HashMap<>();
        context.put("sex", "1");
        context.put("totalTumorCount", "2");
        context.put("tumor0 primarySite", "C000");
        context.put("tumor0 histologyIcdO3", "8070");
        context.put("tumor0 behaviorIcdO3", "3");
        context.put("tumor0 ageGroup", "5");
        context.put("tumor1 primarySite", "C001");
        context.put("tumor1 histologyIcdO3", "8070");
        context.put("tumor1 behaviorIcdO3", "3");
        context.put("tumor1 ageGroup", "8");

        rec = new HashMap<>();
        options = new NaaccrDataGeneratorOptions();

        // 5 year range maximum 1920 - 1925.
        options.setMinDxYear(2000);
        options.setMaxDxYear(2005);

        _rule.execute(rec, null, options, context);

        assignedDate = LocalDate.of(Integer.parseInt(rec.get("birthDateYear")), Integer.parseInt(rec.get("birthDateMonth")), Integer.parseInt(rec.get("birthDateDay")));
        LocalDate startDate = options.getMinDxDate().minusYears(80).minusDays(1);
        LocalDate endDate = options.getMaxDxDate().minusYears(80).plusDays(1);
        String assertMsg = assignedDate.toString() + ": Not between " + startDate.toString() + " and " + endDate.toString();
        Assert.assertTrue(assertMsg, assignedDate.isAfter(startDate) && assignedDate.isBefore(endDate));

        // 10 year range maximum 1920 - 1930.
        options.setMinDxYear(2000);
        options.setMaxDxYear(2015);

        _rule.execute(rec, null, options, context);

        assignedDate = LocalDate.of(Integer.parseInt(rec.get("birthDateYear")), Integer.parseInt(rec.get("birthDateMonth")), Integer.parseInt(rec.get("birthDateDay")));
        startDate = options.getMinDxDate().minusYears(80).minusDays(1);
        endDate = options.getMaxDxDate().minusYears(80).plusDays(1);
        assertMsg = assignedDate.toString() + ": Not between " + startDate.toString() + " and " + endDate.toString();
        Assert.assertTrue(assertMsg, assignedDate.isAfter(startDate) && assignedDate.isBefore(endDate));

    }
}
