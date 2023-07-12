package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.utils.dto.SiteDto;
import com.imsweb.naaccrxml.entity.Item;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

import static com.imsweb.datagenerator.naaccr.NaaccrDataGenerator.CONTEXT_FLAG_AGE_GROUP_MAP;
import static com.imsweb.datagenerator.naaccr.NaaccrDataGenerator.CONTEXT_FLAG_CURRENT_TUMOR_INDEX;
import static com.imsweb.datagenerator.naaccr.NaaccrDataGenerator.CONTEXT_FLAG_MAX_AGE_GROUP;
import static com.imsweb.datagenerator.naaccr.NaaccrDataGenerator.CONTEXT_FLAG_SEX;
import static com.imsweb.datagenerator.naaccr.NaaccrDataGenerator.CONTEXT_FLAG_SITE_FREQ_MAP;

public class DateOfDiagnosisRuleTest {

    private final DateOfDiagnosisRule _rule = new DateOfDiagnosisRule();

    @Test
    public void testExecute() {

        Map<String, Object> context = new HashMap<>();

        // run the entire test 10 times (each run simulates multiple tumors for a single patient)
        for (int i = 0; i < 10; i++) {
            Patient patient = new Patient();
            // generate 10 tumors for this patient
            for (int j = 0; j < 10; j++) {
                Tumor tumor = new Tumor();

                context = new HashMap<>();

                _rule.execute(tumor, patient, null, context);

                Assert.assertTrue(tumor.getItemValue("dateOfDiagnosisYear").matches("\\d{4}"));
                Assert.assertTrue(tumor.getItemValue("dateOfDiagnosisMonth").matches("\\d{1,2}"));
                Assert.assertTrue(tumor.getItemValue("dateOfDiagnosisDay").matches("\\d{1,2}"));

                patient.addTumor(tumor);
            }

            // first current year should be between current year and 10 years ago (since no options are provided)
            int currentYear = LocalDate.now().getYear();
            int firstAssignedYear = Integer.parseInt(patient.getTumor(0).getItemValue("dateOfDiagnosisYear"));
            Assert.assertTrue(firstAssignedYear >= currentYear - 10 && firstAssignedYear <= currentYear);

            // other years should all be on or after the first assigned year
            for (Tumor otherTum : patient.getTumors()) {
                int assignedYear = Integer.parseInt(otherTum.getItemValue("dateOfDiagnosisYear"));
                Assert.assertTrue(assignedYear >= currentYear - 10 && assignedYear <= currentYear && assignedYear >= firstAssignedYear);

                // all dates generated should be on or before the current date because no options were passed
                LocalDate dxDate = LocalDate.of(Integer.parseInt(otherTum.getItemValue("dateOfDiagnosisYear")), Integer.parseInt(otherTum.getItemValue("dateOfDiagnosisMonth")),
                        Integer.parseInt(otherTum.getItemValue("dateOfDiagnosisDay")));
                Assert.assertTrue("Future date: " + dxDate, LocalDate.now().plusDays(1).isAfter(dxDate));
            }
        }

        // Use of a context
        context.put(CONTEXT_FLAG_SEX, "1");
        context.put(CONTEXT_FLAG_CURRENT_TUMOR_INDEX, 0);

        Map<Integer, SiteDto> siteFreqMap = new HashMap<>();
        Map<Integer, Integer> ageGroupMap = new HashMap<>();

        SiteDto dto = new SiteDto();
        dto.setSite("C001");
        dto.setHistology("8070");
        dto.setBehavior("3");
        siteFreqMap.put(0, dto);
        ageGroupMap.put(0, 5);

        context.put(CONTEXT_FLAG_SITE_FREQ_MAP, siteFreqMap);
        context.put(CONTEXT_FLAG_AGE_GROUP_MAP, ageGroupMap);
        context.put(CONTEXT_FLAG_MAX_AGE_GROUP, 5);

        Patient patient = new Patient();
        patient.addItem(new Item("dateOfBirthYear", "1940"));
        patient.addItem(new Item("dateOfBirthMonth", "7"));
        patient.addItem(new Item("dateOfBirthDay", "1"));

        NaaccrDataGeneratorOptions options = new NaaccrDataGeneratorOptions();
        options.setMaxDxYear(2005);

        Tumor tumor = new Tumor();

        _rule.execute(tumor, patient, options, context);

        LocalDate dateOfDx = LocalDate.of(
                Integer.parseInt(tumor.getItemValue("dateOfDiagnosisYear")),
                Integer.parseInt(tumor.getItemValue("dateOfDiagnosisMonth")),
                Integer.parseInt(tumor.getItemValue("dateOfDiagnosisDay")));
        LocalDate dateOfBirth = LocalDate.of(1940, 7, 1);

        LocalDate startDate = dateOfBirth.plusYears(5 * 10);
        LocalDate endDate = options.getMaxDxDate();
        Assert.assertTrue(dateOfDx.toString(), dateOfDx.isAfter(startDate) && dateOfDx.isBefore(endDate));

    }
}
