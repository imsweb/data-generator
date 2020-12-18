package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class NameRuleTest {

    private final NameRule _rule = new NameRule(false);

    @Test
    public void testExecute() {
        for (String sex : Arrays.asList("1", "2")) {
            for (String race1 : Arrays.asList("01", "02", "03", "04", "05", "06", "07", "32", "96", "97", "98", "99")) {
                for (String hispanicOrigin : Arrays.asList("0", "1", "7", "8", "9")) {
                    Map<String, String> rec = new HashMap<>();
                    Map<String, Object> context = new HashMap<>();
                    rec.put("sex", sex);
                    rec.put("race1", race1);
                    rec.put("spanishHispanicOrigin", hispanicOrigin);
                    _rule.execute(rec, new ArrayList<>(), null, context);

                    // verify pattern of names
                    Assert.assertTrue(rec.get("nameFirst").matches("^[a-zA-Z]+$"));
                    Assert.assertTrue(rec.get("nameMiddle").matches("^[a-zA-Z]+$") || rec.get("nameMiddle").matches("^[A-Z]\\.$"));
                    Assert.assertTrue(rec.get("nameLast").matches("^[a-zA-Z]+$"));
                    Assert.assertTrue(rec.get("namePrefix").matches("^[\\w .]*$"));
                    Assert.assertTrue(rec.get("nameSuffix").matches("^[\\w .]*$"));

                    // verify male patient was not given a maiden name, else verify female maiden name if present
                    if (sex.equals("1"))
                        Assert.assertTrue(rec.get("nameBirthSurname").isEmpty());
                    else
                        Assert.assertTrue(rec.get("nameBirthSurname").isEmpty() || rec.get("nameBirthSurname").matches("^[a-zA-Z]+$"));
                }
            }
        }
    }
}
