/*
 * Copyright (C) 2017 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.hl7.rule;

import java.util.Map;

import com.imsweb.datagenerator.hl7.NaaccrHl7DataGeneratorOptions;
import com.imsweb.datagenerator.hl7.NaaccrHl7DataGeneratorRule;
import com.imsweb.datagenerator.utils.DistributionUtils;
import com.imsweb.datagenerator.utils.RandomUtils;
import com.imsweb.datagenerator.utils.dto.CityDto;
import com.imsweb.layout.hl7.Hl7MessageBuilder;
import com.imsweb.layout.hl7.entity.Hl7Message;

public class CommonOrderSegmentRule extends NaaccrHl7DataGeneratorRule {

    public CommonOrderSegmentRule() {
        super("common-order-segment", "Common Order Segment (ORC)");
    }

    @Override
    public void execute(Hl7Message message, NaaccrHl7DataGeneratorOptions options, Map<String, Object> context) {

        // facility street name (will be used for the name as well)
        String facilityStreet = DistributionUtils.getStreetName();

        // address
        String[] facilityAddress = null;
        String[] orderingAddress = null;
        if (options != null && options.getState() != null) {
            CityDto dto1 = DistributionUtils.getCity(options.getState());
            facilityAddress = new String[] {facilityStreet, null, dto1.getCity(), dto1.getState(), dto1.getZip()};

            CityDto dto2 = DistributionUtils.getCity(options.getState());
            orderingAddress = new String[] {DistributionUtils.getStreetName(), null, dto2.getCity(), dto2.getState(), dto2.getZip()};
        }

        new Hl7MessageBuilder(message).withSegment("ORC")

                // ORC-1: order control
                .withField(1, "1")

                // ORC-21: ordering facility name
                .withField(21, facilityStreet + " Health Center")

                // ORC-22: ordering facility address
                .withField(22, facilityAddress)

                // ORC-23: ordering facility phone
                .withField(23)
                .withComponent(6, RandomUtils.getRandomStringOfDigits(3))
                .withComponent(7, RandomUtils.getRandomStringOfDigits(7))

                // ORC-24: ordering provider address
                .withField(24, orderingAddress);
    }
}
