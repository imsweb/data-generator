package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.HashMap;
import java.util.Map;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.utils.dto.SiteFrequencyDto;

import static com.imsweb.datagenerator.naaccr.NaaccrDataGenerator.CONTEXT_FLAG_AGE_GROUP_MAP;
import static com.imsweb.datagenerator.naaccr.NaaccrDataGenerator.CONTEXT_FLAG_MAX_AGE_GROUP;
import static com.imsweb.datagenerator.naaccr.NaaccrDataGenerator.CONTEXT_FLAG_SEX;
import static com.imsweb.datagenerator.naaccr.NaaccrDataGenerator.CONTEXT_FLAG_SITE_FREQ_MAP;

public class BirthRuleTest {

    private BirthRule _rule = new BirthRule();

    @Test
    public void testExecute() {
        Map<String, String> rec = new HashMap<>();
        Map<String, Object> context = new HashMap<>();
        NaaccrDataGeneratorOptions options = new NaaccrDataGeneratorOptions();
        options.setMinDxYear(2000);
        _rule.execute(rec, null, options, context);

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
        context.put(CONTEXT_FLAG_SEX, "1");

        Map<Integer, SiteFrequencyDto> siteFreqMap = new HashMap<>();
        Map<Integer, Integer> ageGroupMap = new HashMap<>();

        SiteFrequencyDto dto = new SiteFrequencyDto();
        dto.setSite("C000");
        dto.setHistology("8070");
        dto.setBehavior("3");
        siteFreqMap.put(0, dto);
        ageGroupMap.put(0, 5);

        dto = new SiteFrequencyDto();
        dto.setSite("C001");
        dto.setHistology("8070");
        dto.setBehavior("3");
        siteFreqMap.put(1, dto);
        ageGroupMap.put(1, 8);

        context.put(CONTEXT_FLAG_SITE_FREQ_MAP, siteFreqMap);
        context.put(CONTEXT_FLAG_AGE_GROUP_MAP, ageGroupMap);
        context.put(CONTEXT_FLAG_MAX_AGE_GROUP, 8);


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
