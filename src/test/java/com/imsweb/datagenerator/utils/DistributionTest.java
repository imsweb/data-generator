/*
 * Copyright (C) 2017 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.utils;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.imsweb.datagenerator.utils.dto.SiteFrequencyDto;

public class DistributionTest {

    @Test
    public void testDistribution() {

        Distribution<String> d1 = Distribution.of(Collections.singletonList("VALUE"));
        Assert.assertEquals("VALUE", d1.getValue());
        Assert.assertEquals("VALUE", d1.getValue());
        Assert.assertEquals("VALUE", d1.getValue());

        Distribution<String> d2 = Distribution.of(Collections.singletonMap("VALUE", 100D));
        Assert.assertEquals("VALUE", d2.getValue());
        Assert.assertEquals("VALUE", d2.getValue());
        Assert.assertEquals("VALUE", d2.getValue());

        // TODO don't use real data, use fake URLs
        Map<Integer, String> mapping = new HashMap<>();
        mapping.put(1, "site");
        mapping.put(2, "histology");
        mapping.put(3, "behavior");
        URL url = Thread.currentThread().getContextClassLoader().getResource("frequencies/sites_sex_male.csv");
        Distribution<SiteFrequencyDto> d3 = Distribution.of(url, SiteFrequencyDto.class, mapping);
        System.out.println(d3.getValue().getSite());
        System.out.println(d3.getValue().getSite());
        System.out.println(d3.getValue().getSite());
        System.out.println(d3.getValue().getSite());
    }
}
