package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.imsweb.naaccrxml.entity.Item;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

public class MaritalStatusRuleTest {

    private final MaritalStatusRule _rule = new MaritalStatusRule();

    @Test
    public void testExecute() {
        Patient patient = new Patient();

        Map<String, Object> context = new HashMap<>();

        // test with no maiden name
        for (int i = 0; i < 5; i++) {
            Tumor tumor = new Tumor();
            patient.addTumor(tumor);
            patient.addItem(new Item("nameMaiden", ""));
            _rule.execute(tumor, patient, null, context);
            int maritalStatus = Integer.parseInt(tumor.getItemValue("maritalStatusAtDx"));
            Assert.assertTrue(maritalStatus >= 1 && maritalStatus <= 6);
        }

        // test with maiden name
        for (int i = 0; i < 5; i++) {
            Tumor tumor = new Tumor();
            patient.addTumor(tumor);
            patient.addItem(new Item("nameMaiden", "MaidenNameTest"));
            _rule.execute(tumor, patient, null, context);
            int maritalStatus = Integer.parseInt(tumor.getItemValue("maritalStatusAtDx"));
            Assert.assertTrue(maritalStatus >= 2 && maritalStatus <= 5);
        }
    }
}
