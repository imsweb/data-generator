/*
 * Copyright (C) 2017 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.utils;

import org.junit.Assert;
import org.junit.Test;

public class SeerDistributionUtilsTest {

    @Test
    public void testCache() {
        Assert.assertNotNull(SeerDistributionUtils.getSex());
        SeerDistributionUtils.clearCache();
        Assert.assertNotNull(SeerDistributionUtils.getSex());
    }

    @Test
    public void testGetName() {
        Assert.assertNotNull(SeerDistributionUtils.getNameLast());
        Assert.assertNotNull(SeerDistributionUtils.getNameLast("0", null));
        Assert.assertNotNull(SeerDistributionUtils.getNameLast(null, "02"));
        Assert.assertNotNull(SeerDistributionUtils.getNameLast("1", "04"));
    }

    @Test
    public void testGetRace() {
        Assert.assertNotNull(SeerDistributionUtils.getRace());
    }

    @Test
    public void testGetHispanicOrigin() {
        Assert.assertNotNull(SeerDistributionUtils.getHispanicOrigin());
    }

    @Test
    public void testGetSex() {
        Assert.assertNotNull(SeerDistributionUtils.getSex());
    }

    @Test
    public void testGetVitalStatus() {
        Assert.assertNotNull(SeerDistributionUtils.getVitalStatus());
    }

    @Test
    public void testGetSite() {
        Assert.assertNotNull(SeerDistributionUtils.getSite());
        Assert.assertNotNull(SeerDistributionUtils.getSite(null));
        Assert.assertNotNull(SeerDistributionUtils.getSite("1"));
        Assert.assertNotNull(SeerDistributionUtils.getSite("2"));
    }

    @Test
    public void testGetCity() {
        Assert.assertNotNull(SeerDistributionUtils.getCity());
        Assert.assertNotNull(SeerDistributionUtils.getCity(null));
        Assert.assertNotNull(SeerDistributionUtils.getCity("?"));
        Assert.assertNotNull(SeerDistributionUtils.getCity("ak"));
        Assert.assertNotNull(SeerDistributionUtils.getCity("AK"));
    }
}
