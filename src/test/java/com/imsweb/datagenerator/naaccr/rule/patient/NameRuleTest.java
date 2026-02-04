package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.imsweb.naaccrxml.entity.Item;
import com.imsweb.naaccrxml.entity.Patient;

public class NameRuleTest {

    private final NameRule _rule = new NameRule();

    @Test
    public void testExecute() {
        for (String sex : Arrays.asList("1", "2")) {
            for (String race1 : Arrays.asList("01", "02", "03", "04", "05", "06", "07", "32", "96", "97", "98", "99")) {
                for (String hispanicOrigin : Arrays.asList("0", "1", "7", "8", "9")) {
                    Patient patient = new Patient();
                    Map<String, Object> context = new HashMap<>();
                    patient.addItem(new Item("sex", sex));
                    patient.addItem(new Item("race1", race1));
                    patient.addItem(new Item("spanishHispanicOrigin", hispanicOrigin));
                    _rule.execute(patient, null, context);

                    // verify pattern of names
                    Assert.assertTrue(patient.getItemValue("nameFirst").matches("^[a-zA-Z]+$"));
                    Assert.assertTrue(patient.getItemValue("nameMiddle").matches("^[a-zA-Z]+$") || patient.getItemValue("nameMiddle").matches("^[A-Z]\\.$"));
                    Assert.assertTrue(patient.getItemValue("nameLast").matches("^[a-zA-Z]+$"));
                    Assert.assertTrue(patient.getItemValue("namePrefix").matches("^[\\w .]*$"));
                    Assert.assertTrue(patient.getItemValue("nameSuffix").matches("^[\\w .]*$"));

                    // verify male patient was not given a maiden name, else verify female maiden name if present
                    if (sex.equals("1"))
                        Assert.assertTrue(patient.getItemValue("nameBirthSurname").isEmpty());
                    else
                        Assert.assertTrue(patient.getItemValue("nameBirthSurname").isEmpty() || patient.getItemValue("nameBirthSurname").matches("^[a-zA-Z]+$"));
                }
            }
        }
    }
}
