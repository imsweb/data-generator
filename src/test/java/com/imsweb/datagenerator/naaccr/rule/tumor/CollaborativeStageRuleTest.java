package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.imsweb.naaccrxml.entity.Item;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

public class CollaborativeStageRuleTest {

    private final CollaborativeStageRule _rule = new CollaborativeStageRule();

    @Test
    public void testExecute() {
        Patient patient = new Patient();

        Tumor tumor = new Tumor();
        patient.addTumor(tumor);

        // valid codes for CSInput fields, as defined in their tables
        List<String> extList = new ArrayList<>(Arrays.asList("000", "010", "020", "050", "060", "070", "080", "100", "200", "300", "400", "450", "600", "700", "800", "810", "950", "999"));
        List<String> nodesList = new ArrayList<>(Arrays.asList("000", "100", "110", "200", "210", "300", "400", "500", "505", "800", "999"));
        List<String> metsList = new ArrayList<>(Arrays.asList("00", "10", "40", "50", "60", "99"));
        List<String> lviList = new ArrayList<>(Arrays.asList("0", "1", "8", "9"));

        // set required fields
        Map<String, Object> context = new HashMap<>();

        tumor.addItem(new Item("primarySite", "C680"));
        tumor.addItem(new Item("histologicTypeIcdO3", "8000"));
        tumor.addItem(new Item("behaviorCodeIcdO3", "3"));
        tumor.addItem(new Item("grade", "9"));
        tumor.addItem(new Item("dateOfDiagnosisYear", "2013"));
        tumor.addItem(new Item("ageAtDiagnosis", "060"));
        tumor.addItem(new Item("vitalStatus", "1"));
        tumor.addItem(new Item("typeOfReportingSource", "1"));
        _rule.execute(tumor, patient, null, context);

        // validate CSInput values are in acceptable ranges
        int size = Integer.parseInt(tumor.getItemValue("csTumorSize"));
        Assert.assertTrue((size >= 0 && size <= 996) || size == 999);
        Assert.assertTrue(extList.contains(tumor.getItemValue("csExtension")));
        int extEval = Integer.parseInt(tumor.getItemValue("csTumorSizeExtEval"));
        Assert.assertTrue(extEval >= 0 && extEval <= 9 && extEval != 7 && extEval != 4);
        Assert.assertTrue(nodesList.contains(tumor.getItemValue("csLymphNodes")));
        int nodesEval = Integer.parseInt(tumor.getItemValue("csLymphNodesEval"));
        Assert.assertTrue(nodesEval >= 0 && nodesEval <= 9 && nodesEval != 7 && nodesEval != 4);
        Assert.assertTrue(metsList.contains(tumor.getItemValue("csMetsAtDx")));
        int metsEval = Integer.parseInt(tumor.getItemValue("csMetsEval"));
        Assert.assertTrue(metsEval >= 0 && metsEval <= 9 && metsEval != 7 && metsEval != 4);
        Assert.assertTrue(lviList.contains(tumor.getItemValue("lymphVascularInvasion")));

        Assert.assertNotNull(tumor.getItemValue("csSiteSpecificFactor1"));
        Assert.assertNotNull(tumor.getItemValue("csSiteSpecificFactor2"));
        Assert.assertNotNull(tumor.getItemValue("csSiteSpecificFactor3"));
        Assert.assertNotNull(tumor.getItemValue("csSiteSpecificFactor4"));
        Assert.assertNotNull(tumor.getItemValue("csSiteSpecificFactor5"));
        Assert.assertNotNull(tumor.getItemValue("csSiteSpecificFactor6"));
        Assert.assertNotNull(tumor.getItemValue("csSiteSpecificFactor7"));
        Assert.assertNotNull(tumor.getItemValue("csSiteSpecificFactor8"));
        Assert.assertNotNull(tumor.getItemValue("csSiteSpecificFactor9"));
        Assert.assertNotNull(tumor.getItemValue("csSiteSpecificFactor10"));
        Assert.assertNotNull(tumor.getItemValue("csSiteSpecificFactor11"));
        Assert.assertNotNull(tumor.getItemValue("csSiteSpecificFactor12"));
        Assert.assertNotNull(tumor.getItemValue("csSiteSpecificFactor13"));
        Assert.assertNotNull(tumor.getItemValue("csSiteSpecificFactor14"));
        Assert.assertNotNull(tumor.getItemValue("csSiteSpecificFactor15"));
        Assert.assertNotNull(tumor.getItemValue("csSiteSpecificFactor16"));
        Assert.assertNotNull(tumor.getItemValue("csSiteSpecificFactor17"));
        Assert.assertNotNull(tumor.getItemValue("csSiteSpecificFactor18"));
        Assert.assertNotNull(tumor.getItemValue("csSiteSpecificFactor19"));
        Assert.assertNotNull(tumor.getItemValue("csSiteSpecificFactor20"));
        Assert.assertNotNull(tumor.getItemValue("csSiteSpecificFactor21"));
        Assert.assertNotNull(tumor.getItemValue("csSiteSpecificFactor22"));
        Assert.assertNotNull(tumor.getItemValue("csSiteSpecificFactor23"));
        Assert.assertNotNull(tumor.getItemValue("csSiteSpecificFactor24"));
        Assert.assertNotNull(tumor.getItemValue("csSiteSpecificFactor25"));

        // derived version should be 020550
        Assert.assertEquals("020550", tumor.getItemValue("csVersionDerived"));

        // C680, 8000 will only return one schema with no discriminator - so ssf25 should be 988
        Assert.assertEquals("988", tumor.getItemValue("csSiteSpecificFactor25"));
    }
}
