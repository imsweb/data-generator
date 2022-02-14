/*
 * Copyright (C) 2022 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Assert;
import org.junit.Test;

import com.imsweb.datagenerator.utils.DistributionUtils;
import com.imsweb.datagenerator.utils.dto.SiteFrequencyDto;
import com.imsweb.naaccrxml.entity.Item;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

import static com.imsweb.datagenerator.naaccr.NaaccrDataGenerator.CONTEXT_FLAG_CURRENT_TUMOR_INDEX;
import static com.imsweb.datagenerator.naaccr.NaaccrDataGenerator.CONTEXT_FLAG_SITE_FREQ_MAP;

public class StagingInputRuleTest {

    private final StagingInputRule _rule = new StagingInputRule();

    @Test
    public void testDescription() {
        Assert.assertNotNull(_rule.getDescription());
    }

    @Test
    public void testExecuteMale() {
        execute("1");
    }

    @Test
    public void testExecuteFemale() {
        execute("2");
    }

    private void execute(String sex) {
        Patient patient = new Patient();

        Map<String, Object> context = new HashMap<>();
        context.put(CONTEXT_FLAG_CURRENT_TUMOR_INDEX, 1);
        Map<Integer, SiteFrequencyDto> siteFreqMap = new HashMap<>();
        siteFreqMap.put(1, DistributionUtils.getSite(sex));
        context.put(CONTEXT_FLAG_SITE_FREQ_MAP, siteFreqMap);

        // don't check these fields since we are setting them "manually"
        Set<String> exceptions = new HashSet<>(Arrays.asList("dateOfDiagnosisYear", "primarySite", "histologicTypeIcdO3", "behaviorCodeIcdO3"));

        // CS values are all digits, but TNM and EOD can contain characters...
        Pattern validValue = Pattern.compile("^[0-9A-Za-z\\s.]+[+\\-]?$");

        // CS
        Tumor tumor = new Tumor();
        tumor.addItem(new Item("dateOfDiagnosisYear", "2013"));
        tumor.addItem(new Item("primarySite", "C680"));
        tumor.addItem(new Item("histologicTypeIcdO3", "8000"));
        tumor.addItem(new Item("behaviorCodeIcdO3", "3"));
        _rule.execute(tumor, patient, null, context);
        for (Item item : tumor.getItems())
            if (!exceptions.contains(item.getNaaccrId()))
                Assert.assertTrue(item.getNaaccrId() + ": " + item.getValue(), NumberUtils.isDigits(item.getValue()) && item.getValue().length() <= 3);

        // TNM
        tumor = new Tumor();
        tumor.addItem(new Item("dateOfDiagnosisYear", "2017"));
        tumor.addItem(new Item("primarySite", "C680"));
        tumor.addItem(new Item("histologicTypeIcdO3", "8000"));
        tumor.addItem(new Item("behaviorCodeIcdO3", "3"));
        _rule.execute(tumor, patient, null, context);
        for (Item item : tumor.getItems())
            if (!exceptions.contains(item.getNaaccrId()))
                Assert.assertTrue(item.getNaaccrId() + ": " + item.getValue(), validValue.matcher(item.getValue()).matches());

        // EOD
        tumor = new Tumor();
        tumor.addItem(new Item("dateOfDiagnosisYear", "2019"));
        tumor.addItem(new Item("primarySite", "C680"));
        tumor.addItem(new Item("histologicTypeIcdO3", "8000"));
        tumor.addItem(new Item("behaviorCodeIcdO3", "3"));
        _rule.execute(tumor, patient, null, context);
        for (Item item : tumor.getItems())
            if (!exceptions.contains(item.getNaaccrId()))
                Assert.assertTrue(item.getNaaccrId() + ": " + item.getValue(), validValue.matcher(item.getValue()).matches());
    }

    //    @SuppressWarnings("ConstantConditions")
    //    public static void main(String[] args) {
    //        //Staging staging = Staging.getInstance(CsDataProvider.getInstance(CsVersion.LATEST));
    //        Staging staging = Staging.getInstance(TnmDataProvider.getInstance(TnmVersion.LATEST));
    //        //Staging staging = Staging.getInstance(EodDataProvider.getInstance(EodVersion.LATEST));
    //
    //        SchemaLookup lookup = new SchemaLookup();
    //        lookup.setSite("C384");
    //        lookup.setHistology("9050");
    //        lookup.setInput("sex", "2");
    //
    //        List<Schema> schemas = staging.lookupSchema(lookup);
    //        System.out.println(schemas.stream().map(Schema::getId).collect(Collectors.toList()));
    //
    //        Schema schema = schemas.stream().filter(s -> "pleura".equals(s.getId())).findFirst().orElse(null);
    //
    //        Input input = schema.getInputs().stream().filter(i -> "csSiteSpecificFactor5".equals(i.getNaaccrXmlId())).findFirst().orElse(null);
    //
    //        Table table = staging.getTable(input.getTable());
    //
    //        for (List<String> row : table.getRawRows())
    //            System.out.println(row.get(0));
    //    }
}
