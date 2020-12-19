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
        Map<String, Object> context = new HashMap<>();

        // test with no birth surname
        for (int i = 0; i < 5; i++) {
            Patient patient = new Patient();
            Tumor tumor = new Tumor();
            _rule.execute(tumor, patient, null, context);
            int maritalStatus = Integer.parseInt(tumor.getItemValue("maritalStatusAtDx"));
            Assert.assertTrue(maritalStatus >= 1 && maritalStatus <= 6);
            patient.addTumor(tumor);
        }

        // test with birth surname
        for (int i = 0; i < 5; i++) {
            Patient patient = new Patient();
            patient.addItem(new Item("nameBirthSurname", "Something"));
            Tumor tumor = new Tumor();
            _rule.execute(tumor, patient, null, context);
            int maritalStatus = Integer.parseInt(tumor.getItemValue("maritalStatusAtDx"));
            Assert.assertTrue(maritalStatus >= 2 && maritalStatus <= 5);
            patient.addTumor(tumor);
        }
    }
}
