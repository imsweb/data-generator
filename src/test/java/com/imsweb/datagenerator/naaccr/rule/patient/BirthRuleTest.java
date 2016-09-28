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
    }
}
