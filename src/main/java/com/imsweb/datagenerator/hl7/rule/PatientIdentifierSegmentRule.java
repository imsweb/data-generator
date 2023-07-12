/*
 * Copyright (C) 2017 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.hl7.rule;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.imsweb.datagenerator.hl7.NaaccrHl7DataGeneratorOptions;
import com.imsweb.datagenerator.hl7.NaaccrHl7DataGeneratorRule;
import com.imsweb.datagenerator.utils.DistributionUtils;
import com.imsweb.datagenerator.utils.RandomUtils;
import com.imsweb.datagenerator.utils.dto.CityDto;
import com.imsweb.layout.hl7.Hl7MessageBuilder;
import com.imsweb.layout.hl7.entity.Hl7Message;

public class PatientIdentifierSegmentRule extends NaaccrHl7DataGeneratorRule {

    private static final Map<String, String> _RACE_MAPPING = new HashMap<>();

    static {
        _RACE_MAPPING.put("03", "1002-5");
        _RACE_MAPPING.put("16", "2029-7");
        _RACE_MAPPING.put("13", "2033-9");
        _RACE_MAPPING.put("06", "2036-2");
        _RACE_MAPPING.put("12", "2037-0");
        _RACE_MAPPING.put("05", "2039-6");
        _RACE_MAPPING.put("08", "2040-4");
        _RACE_MAPPING.put("11", "2041-2");
        _RACE_MAPPING.put("17", "2044-6");
        _RACE_MAPPING.put("14", "2046-1");
        _RACE_MAPPING.put("10", "2047-9");
        _RACE_MAPPING.put("02", "2054-5");
        _RACE_MAPPING.put("25", "2078-4");
        _RACE_MAPPING.put("07", "2079-2");
        _RACE_MAPPING.put("27", "2080-0");
        _RACE_MAPPING.put("26", "2081-8");
        _RACE_MAPPING.put("28", "2082-6");
        _RACE_MAPPING.put("20", "2085-9");
        _RACE_MAPPING.put("22", "2087-5");
        _RACE_MAPPING.put("21", "2088-3");
        _RACE_MAPPING.put("30", "2100-6");
        _RACE_MAPPING.put("31", "2101-4");
        _RACE_MAPPING.put("32", "2102-2");
        _RACE_MAPPING.put("01", "2106-3");
        _RACE_MAPPING.put("98", "2131-1");
        _RACE_MAPPING.put("97", "2500-7");
    }

    public PatientIdentifierSegmentRule() {
        super("patient-identifier-segment", "Patient Identifier Segment (PID)");
    }

    @Override
    public void execute(Hl7Message message, NaaccrHl7DataGeneratorOptions options, Map<String, Object> context) {

        // sex
        String sex = DistributionUtils.getSex();
        context.put("sex", sex);

        // race
        String race = DistributionUtils.getRace();
        context.put("race", race);

        // spanish/hispanic origin
        String hispanicOrigin = DistributionUtils.getHispanicOrigin();
        context.put("hispanicOrigin", hispanicOrigin);

        // date of birth
        LocalDate dob = RandomUtils.getRandomDateBetween(LocalDate.of(1925, 1, 1), LocalDate.now().minusYears(1));

        // address
        String[] address = null;
        if (options != null && options.getState() != null) {
            CityDto dto = DistributionUtils.getCity(options.getState());
            address = new String[] {DistributionUtils.getStreetName(), null, dto.getCity(), dto.getState(), dto.getZip()};
        }

        // put a site in the context, based on the sex
        context.put("site", DistributionUtils.getSite(sex));

        new Hl7MessageBuilder(message).withSegment("PID")

                // PID-1: set ID
                .withField(1, "1")

                // PID-3: patient identifier list (providing a medical record number and a social security number)
                .withField(3)
                .withRepeatedField()
                .withComponent(1, "MR" + RandomUtils.getRandomStringOfDigits(6))
                .withComponent(5, "MR")
                .withRepeatedField()
                .withComponent(1, RandomUtils.getRandomStringOfDigits(9))
                .withComponent(5, "SS")

                // PID-5: patient name (last, first, middle)
                .withField(5, DistributionUtils.getNameLast(hispanicOrigin, race), DistributionUtils.getNameFirst(sex), RandomUtils.getRandomStringOfLetters(1))

                // PID-6: maiden name
                .withField(6)

                // PID-7: date of birth
                .withField(7, DateTimeFormatter.ofPattern("yyyyMMdd").format(dob))

                // PID-8: sex (M, F, U)
                .withField(8, getSex(sex))

                // PID-9: alias -> not set

                // PID-10: race
                .withField(10, _RACE_MAPPING.get(race), null, "HL70005")

                // PID-11: address (name, supp, city, state, zip)
                .withField(11, address)

                // PID-12: county
                .withField(12, "999")

                // PID-13: phone
                .withField(13)
                .withComponent(6, RandomUtils.getRandomStringOfDigits(3))
                .withComponent(7, RandomUtils.getRandomStringOfDigits(7))

                // PID-16: marital status at DX (S=Single, M=Married, A=Separated, D=Divorced, W=Widowed)
                .withField(16, "M")

                // PID-22: spanish-hispanic origin
                .withField(22, DistributionUtils.getHispanicOrigin());

        // PID-23: birth place -> not set

    }

    private String getSex(String num) {
        String sex;

        if ("1".equals(num))
            sex = "M";
        else if ("2".equals(num))
            sex = "F";
        else
            sex = "U";

        return sex;
    }
}
