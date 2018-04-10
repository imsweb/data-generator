/*
 * Copyright (C) 2017 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.utils;

import org.junit.Assert;
import org.junit.Test;

public class DistributionUtilsTest {

    @Test
    public void testCache() {
        Assert.assertNotNull(DistributionUtils.getSex());
        DistributionUtils.clearCache();
        Assert.assertNotNull(DistributionUtils.getSex());
    }

    @Test
    public void testGetNameLast() {
        Assert.assertNotNull(DistributionUtils.getNameLast());
        Assert.assertNotNull(DistributionUtils.getNameLast("0", null));
        Assert.assertNotNull(DistributionUtils.getNameLast(null, "02"));
        Assert.assertNotNull(DistributionUtils.getNameLast("1", "04"));
    }

    @Test
    public void testGetNameFirst() {
        Assert.assertNotNull(DistributionUtils.getNameFirst());
        Assert.assertNotNull(DistributionUtils.getNameFirst(null));
        Assert.assertNotNull(DistributionUtils.getNameFirst("1"));
        Assert.assertNotNull(DistributionUtils.getNameFirst("2"));
    }

    @Test
    public void testGetRace() {
        Assert.assertNotNull(DistributionUtils.getRace());
    }

    @Test
    public void testGetHispanicOrigin() {
        Assert.assertNotNull(DistributionUtils.getHispanicOrigin());
    }

    @Test
    public void testGetSex() {
        Assert.assertNotNull(DistributionUtils.getSex());
    }

    @Test
    public void testGetVitalStatus() {
        Assert.assertNotNull(DistributionUtils.getVitalStatus());
    }

    @Test
    public void testGetSite() {
        Assert.assertNotNull(DistributionUtils.getSite());
        Assert.assertNotNull(DistributionUtils.getSite(null));
        Assert.assertNotNull(DistributionUtils.getSite("1"));
        Assert.assertNotNull(DistributionUtils.getSite("2"));
    }

    @Test
    public void testGetStreetName() {
        Assert.assertNotNull(DistributionUtils.getStreetName());
    }

    @Test
    public void testGetStreetSuffix() {
        Assert.assertNotNull(DistributionUtils.getStreetSuffix());
    }

    @Test
    public void testGetCity() {
        Assert.assertNotNull(DistributionUtils.getCity());
        Assert.assertNotNull(DistributionUtils.getCity(null));
        Assert.assertNotNull(DistributionUtils.getCity("?"));
        Assert.assertNotNull(DistributionUtils.getCity("ak"));
        Assert.assertNotNull(DistributionUtils.getCity("AK"));
    }

    @Test
    public void testGetStreetState() {
        Assert.assertNotNull(DistributionUtils.getState());
    }

    public void testGetAgeGroup() {
        Assert.assertNotNull(DistributionUtils.getAgeGroup(null));
        Assert.assertNotNull(DistributionUtils.getAgeGroup("C000"));
        Assert.assertNotNull(DistributionUtils.getAgeGroup("C779"));
        Assert.assertNotNull(DistributionUtils.getAgeGroup("C900"));

        int ageGroupVal = DistributionUtils.getAgeGroup("C900");
        Assert.assertTrue(ageGroupVal == -1);
        ageGroupVal = DistributionUtils.getAgeGroup("C002");
        Assert.assertTrue(ageGroupVal >= 3 && ageGroupVal <= 8);
    }
  
    @Test
    public void testGetFacility() {
        Assert.assertNotNull(DistributionUtils.getFacility());
        Assert.assertNotNull(DistributionUtils.getFacility(null));
        Assert.assertNotNull(DistributionUtils.getFacility("?"));
        Assert.assertNotNull(DistributionUtils.getFacility("ak"));
        Assert.assertNotNull(DistributionUtils.getFacility("AK"));
    }

    @Test
    public void testGetPhysician() {
        Assert.assertNotNull(DistributionUtils.getPhysician());
        Assert.assertNotNull(DistributionUtils.getPhysician(null));
        Assert.assertNotNull(DistributionUtils.getPhysician("?"));
        Assert.assertNotNull(DistributionUtils.getPhysician("ak"));
        Assert.assertNotNull(DistributionUtils.getPhysician("AK"));
    }
}
