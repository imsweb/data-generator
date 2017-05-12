/*
 * Copyright (C) 2017 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.hl7.rule;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.imsweb.datagenerator.hl7.NaaccrHl7DataGeneratorOptions;
import com.imsweb.datagenerator.hl7.NaaccrHl7DataGeneratorRule;
import com.imsweb.datagenerator.utils.DistributedRandomValueGenerator;
import com.imsweb.datagenerator.utils.RandomUtils;
import com.imsweb.datagenerator.utils.UniformRandomValueGenerator;
import com.imsweb.layout.hl7.Hl7MessageBuilder;
import com.imsweb.layout.hl7.entity.Hl7Message;

public class PatientIdentifierSegmentRule extends NaaccrHl7DataGeneratorRule {

    protected static final DistributedRandomValueGenerator _NAME_LAST = new DistributedRandomValueGenerator("frequencies/last_names_white.csv");

    protected static final UniformRandomValueGenerator _NAME_FIRST = new UniformRandomValueGenerator("lists/first_names_male.csv");

//    protected static final DistributedRandomValueGenerator _SEX_DIST = new DistributedRandomValueGenerator();
    //
    //    static {
    //        _SEX_DIST.add("M", 49.0);
    //        _SEX_DIST.add("F", 49.0);
    //        _SEX_DIST.add("U", 2.0);
    //
    //    }

    public PatientIdentifierSegmentRule() {
        super("patient-identifier-segment", "Patient Identifier Segment (PID)");
    }

    @Override
    public void execute(Hl7Message message, NaaccrHl7DataGeneratorOptions options) {

        //String sex = _SEX_DIST.getRandomValue();
        LocalDate dob = RandomUtils.getRandomDateBetween(LocalDate.of(1925, 1, 1), LocalDate.now().minusYears(1));

        new Hl7MessageBuilder(message).withSegment("PID")

                // PID-1: set ID
                .withField(1, "1")

                // PID-3: patient identifier list (providing a medical record number)
                .withField(3)
                .withRepeatedField()
                .withComponent(1, "MR" + RandomUtils.getRandomStringOfDigits(6))
                .withComponent(5, "MR")
                .withRepeatedField()
                .withComponent(1, RandomUtils.getRandomStringOfDigits(9))
                .withComponent(5, "SS")

                // PID-5: patient name (last, first, middle)
                .withField(5, _NAME_LAST.getRandomValue(), _NAME_FIRST.getRandomValue(), RandomUtils.getRandomStringOfLetters(1))

                // PID-6: maiden name
                .withField(6)

                // PID-7: date of birth
                .withField(7, DateTimeFormatter.ofPattern("yyyyMMdd").format(dob))

                // PID-8: sex (M, F, U)
                .withField(8, "M")

                // PID-9: alias -> not set

                // PID-10: race
                .withField(10, "2106-3", "While", "HL70005")

                // PID-11: address (name, supp, city, state, zip)

                // PID-12: county

                // PID-13: phone
                .withField(13)
                .withComponent(6, RandomUtils.getRandomStringOfDigits(3))
                .withComponent(7, RandomUtils.getRandomStringOfDigits(7))

                // PID-16: marital status at DX (S=Single, M=Married, A=Separated, D=Divorced, W=Widowed)
                .withField(16, "M")

                // PID-22: spanish-hispanic origin
                .withField(22, "0");

                // PID-23: birth place

    }

    // CODES FOR RACE
    //            '1002-5' : '03',
    //            '2029-7' : '16',
    //            '2033-9' : '13',
    //            '2036-2' : '06',
    //            '2037-0' : '12',
    //            '2039-6' : '05',
    //            '2040-4' : '08',
    //            '2041-2' : '11',
    //            '2044-6' : '17',
    //            '2046-1' : '14',
    //            '2047-9' : '10',
    //            '2054-5' : '02',
    //            '2078-4' : '25',
    //            '2079-2' : '07',
    //            '2080-0' : '27',
    //            '2081-8' : '26',
    //            '2082-6' : '28',
    //            '2085-9' : '20',
    //            '2087-5' : '22',
    //            '2088-3' : '21',
    //            '2100-6' : '30',
    //            '2101-4' : '31',
    //            '2102-2' : '32',
    //            '2106-3' : '01',
    //            '2131-1' : '98',
    //            '2500-7' : '97',
}
