/*
 * Copyright (C) 2023 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorTumorTextRule;
import com.imsweb.naaccrxml.NaaccrFormat;
import com.imsweb.naaccrxml.NaaccrXmlDictionaryUtils;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

public class NaaccrDataGeneratorTumorTextRuleTest {

    @Test
    public void testRules() {
        List<Class<? extends NaaccrDataGeneratorTumorTextRule>> rules = new ArrayList<>();
        rules.add(DxProcTextRule.class);
        rules.add(RxTextRule.class);
        rules.add(TumorTextRule.class);

        for (Class<? extends NaaccrDataGeneratorTumorTextRule> ruleClass : rules) {
            NaaccrDataGeneratorTumorTextRule rule = null;

            try {
                rule = ruleClass.getDeclaredConstructor().newInstance();
            }
            catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                Assert.fail();
            }

            Tumor tumor = new Tumor();
            Patient patient = new Patient();
            Map<String, Object> context = new HashMap<>();
            rule.execute(tumor, patient, null, context);

            for (String property : rule.getNAACCRTextFields()) {
                Assert.assertNotNull(NaaccrXmlDictionaryUtils.getBaseDictionaryByVersion(NaaccrFormat.NAACCR_VERSION_LATEST).getItemByNaaccrId(property));
                Assert.assertNotNull(tumor.getItemValue(property));
            }
        }
    }
}
