package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;

public class DateOfDiagnosisRuleTest {

    private DateOfDiagnosisRule _rule = new DateOfDiagnosisRule();

    @Test
    public void testExecute() {

        // run the entire test 10 times (each run simulates multiple tumors for a single patient)
        for (int i = 0; i < 10; i++) {
            List<Map<String, String>> records = new ArrayList<>();

            // generate 10 tumors for this patient
            for (int j = 0; j < 10; j++) {
                Map<String, String> rec = new HashMap<>();
                _rule.execute(rec, records, null);

                Assert.assertTrue(rec.get("dateOfDiagnosisYear").matches("\\d{4}"));
                Assert.assertTrue(rec.get("dateOfDiagnosisMonth").matches("\\d{1,2}"));
                Assert.assertTrue(rec.get("dateOfDiagnosisDay").matches("\\d{1,2}"));

                records.add(rec);
            }

            // first current year should be between current year and 10 years ago (since no options are provided)
            int currentYear = LocalDate.now().getYear();
            int firstAssignedYear = Integer.parseInt(records.get(0).get("dateOfDiagnosisYear"));
            Assert.assertTrue(firstAssignedYear >= currentYear - 10 && firstAssignedYear <= currentYear);

            // other years should all be on or after the first assigned year
            for (Map<String, String> otherRec : records) {
                int assignedYear = Integer.parseInt(otherRec.get("dateOfDiagnosisYear"));
                Assert.assertTrue(assignedYear >= currentYear - 10 && assignedYear <= currentYear && assignedYear >= firstAssignedYear);

                // all dates generated should be on or before the current date because no options were passed
                LocalDate dxDate = LocalDate.of(Integer.parseInt(otherRec.get("dateOfDiagnosisYear")), Integer.parseInt(otherRec.get("dateOfDiagnosisMonth")), Integer.parseInt(otherRec.get("dateOfDiagnosisDay")));
                Assert.assertTrue("Future date: " + dxDate.toString(), LocalDate.now().plusDays(1).isAfter(dxDate));
            }
        }

        // Use of a context
        Map<String, String> context = new HashMap<>();
        context.put("sex", "1");
        context.put("currentTumor", "0");
        context.put("totalTumorCount", "1");
        context.put("tumor0 ageGroup", "5");

        Map<String, String> rec = new HashMap<>();
        rec.put("birthDateYear", "1940");
        rec.put("birthDateMonth", "7");
        rec.put("birthDateDay", "1");

        NaaccrDataGeneratorOptions options = new NaaccrDataGeneratorOptions();
        options.setMaxDxYear(2005);

        _rule.execute(rec, null, options, context);

        LocalDate dateOfDx = LocalDate.of(Integer.valueOf(rec.get("dateOfDiagnosisYear")), Integer.valueOf(rec.get("dateOfDiagnosisMonth")), Integer.valueOf(rec.get("dateOfDiagnosisDay")));
        LocalDate birthDate = LocalDate.of(1940, 7, 1);

        LocalDate startDate = birthDate.plusYears(5 * 10);
        LocalDate endDate = options.getMaxDxDate();
        Assert.assertTrue(dateOfDx.toString(), dateOfDx.isAfter(startDate) && dateOfDx.isBefore(endDate));


    }
}
