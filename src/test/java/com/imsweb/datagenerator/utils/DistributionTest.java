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

import com.imsweb.datagenerator.utils.dto.SiteDto;

public class DistributionTest {

    @Test
    public void testDistribution() {

        // test a uniform distribution using a list (using a single possible value to keep it simple)
        Distribution<String> dList = Distribution.of(Collections.singletonList("VALUE"));
        Assert.assertEquals("VALUE", dList.getValue());
        Assert.assertEquals("VALUE", dList.getValue());
        Assert.assertEquals("VALUE", dList.getValue());

        // test a distribution using frequencies (using a single possible value to keep it simple)
        Distribution<String> dMap = Distribution.of(Collections.singletonMap("VALUE", 100D));
        Assert.assertEquals("VALUE", dMap.getValue());
        Assert.assertEquals("VALUE", dMap.getValue());
        Assert.assertEquals("VALUE", dMap.getValue());

        // test loading a uniform distribution from a URLs (using a single possible value to keep it simple)
        Distribution<String> dListUrl = Distribution.of(getUrl("test_distribution_race.csv"));
        Assert.assertEquals("01", dListUrl.getValue());

        // test loading a distribution using frequencies s (using a single possible value to keep it simple)
        Distribution<String> dMapUrl = Distribution.of(getUrl("test_distribution_name.csv"));
        Assert.assertEquals("name", dMapUrl.getValue());

        // test loading a complex distribution from a URL (using a single possible value to keep it simple)
        Map<Integer, String> mapping = new HashMap<>();
        mapping.put(1, "site");
        mapping.put(2, "histology");
        mapping.put(3, "behavior");
        Distribution<SiteDto> dComplexMapUrl = Distribution.of(getUrl("test_distribution_site.csv"), SiteDto.class, mapping);
        Assert.assertEquals("C123", dComplexMapUrl.getValue().getSite());
        Assert.assertEquals("8000", dComplexMapUrl.getValue().getHistology());
        Assert.assertEquals("3", dComplexMapUrl.getValue().getBehavior());

        // test loading a complex distribution from a GZipped URL (using a single possible value to keep it simple)
        Distribution<SiteDto> dComplexMapGzippedUrl = Distribution.of(getUrl("test_distribution_site.csv.gz"), SiteDto.class, mapping);
        Assert.assertEquals("C123", dComplexMapGzippedUrl.getValue().getSite());
        Assert.assertEquals("8000", dComplexMapGzippedUrl.getValue().getHistology());
        Assert.assertEquals("3", dComplexMapGzippedUrl.getValue().getBehavior());
    }

    private URL getUrl(String name) {
        return Thread.currentThread().getContextClassLoader().getResource(name);
    }
}
