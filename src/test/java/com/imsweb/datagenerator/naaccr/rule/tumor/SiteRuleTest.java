package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.imsweb.datagenerator.utils.dto.SiteFrequencyDto;

import static com.imsweb.datagenerator.naaccr.NaaccrDataGenerator.CONTEXT_FLAG_AGE_GROUP_MAP;
import static com.imsweb.datagenerator.naaccr.NaaccrDataGenerator.CONTEXT_FLAG_CURRENT_TUMOR_INDEX;
import static com.imsweb.datagenerator.naaccr.NaaccrDataGenerator.CONTEXT_FLAG_MAX_AGE_GROUP;
import static com.imsweb.datagenerator.naaccr.NaaccrDataGenerator.CONTEXT_FLAG_SEX;
import static com.imsweb.datagenerator.naaccr.NaaccrDataGenerator.CONTEXT_FLAG_SITE_FREQ_MAP;

public class SiteRuleTest {

    private SiteRule _rule = new SiteRule();

    @Test
    public void testExecute() {
        // test the rule ten times, asserting that the execute() method assigns appropriate patterns for all three fields
        Map<String, Object> context = new HashMap<>();
        for (String sex : Arrays.asList("1", "2")) {
            for (int i = 0; i < 5; i++) {
                Map<String, String> rec = new HashMap<>();
                rec.put("sex", sex);
                _rule.execute(rec, null, null, context);
                // primary site must be 'C' followed by three digits; histology must be four digits, and behavior one digit
                Assert.assertTrue("Primary site value pattern match", rec.get("primarySite").matches("C\\d{3}"));
                Assert.assertTrue("Histology value pattern match", rec.get("histologicTypeIcdO3").matches("\\d{4}"));
                Assert.assertTrue("Behavior value pattern match", rec.get("behaviorCodeIcdO3").matches("\\d"));
            }
        }

        // Use of a context
        context.put(CONTEXT_FLAG_SEX, "1");
        context.put(CONTEXT_FLAG_CURRENT_TUMOR_INDEX, 0);

        Map<Integer, SiteFrequencyDto> siteFreqMap = new HashMap<>();
        Map<Integer, Integer> ageGroupMap = new HashMap<>();

        SiteFrequencyDto dto = new SiteFrequencyDto();
        dto.setSite("C000");
        dto.setHistology("8070");
        dto.setBehavior("3");
        siteFreqMap.put(0, dto);
        ageGroupMap.put(0, 5);

        context.put(CONTEXT_FLAG_SITE_FREQ_MAP, siteFreqMap);
        context.put(CONTEXT_FLAG_AGE_GROUP_MAP, ageGroupMap);
        context.put(CONTEXT_FLAG_MAX_AGE_GROUP, 5);

        Map<String, String> rec = new HashMap<>();
        rec.put("sex", "1");
        _rule.execute(rec, null, null, context);

        Assert.assertEquals("C000", rec.get("primarySite"));
        Assert.assertEquals("8070", rec.get("histologicTypeIcdO3"));
        Assert.assertEquals("3", rec.get("behaviorCodeIcdO3"));
        Assert.assertEquals("9", rec.get("grade"));
        Assert.assertEquals("9", rec.get("laterality"));

        context = new HashMap<>();
        context.put(CONTEXT_FLAG_SEX, "1");
        context.put(CONTEXT_FLAG_CURRENT_TUMOR_INDEX, 0);

        siteFreqMap = new HashMap<>();
        ageGroupMap = new HashMap<>();

        dto = new SiteFrequencyDto();
        dto.setSite("C809");
        dto.setHistology("8111");
        dto.setBehavior("9");
        siteFreqMap.put(0, dto);
        ageGroupMap.put(0, 5);

        context.put(CONTEXT_FLAG_SITE_FREQ_MAP, siteFreqMap);
        context.put(CONTEXT_FLAG_AGE_GROUP_MAP, ageGroupMap);
        context.put(CONTEXT_FLAG_MAX_AGE_GROUP, 5);

        rec = new HashMap<>();
        rec.put("sex", "1");
        _rule.execute(rec, null, null, context);

        Assert.assertEquals("C809", rec.get("primarySite"));
        Assert.assertEquals("8111", rec.get("histologicTypeIcdO3"));
        Assert.assertEquals("9", rec.get("behaviorCodeIcdO3"));
        Assert.assertEquals("9", rec.get("grade"));
        Assert.assertEquals("0", rec.get("laterality"));
    }
}
