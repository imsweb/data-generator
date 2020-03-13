package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class CollaborativeStageRuleTest {

    private CollaborativeStageRule _rule = new CollaborativeStageRule();

    @Test
    public void testExecute() {

        // valid codes for CSInput fields, as defined in their tables
        List<String> extList = new ArrayList<>(Arrays.asList("000", "010", "020", "050", "060", "070", "080", "100", "200", "300", "400", "450", "600", "700", "800", "810", "950", "999"));
        List<String> nodesList = new ArrayList<>(Arrays.asList("000", "100", "110", "200", "210", "300", "400", "500", "505", "800", "999"));
        List<String> metsList = new ArrayList<>(Arrays.asList("00", "10", "40", "50", "60", "99"));
        List<String> lviList = new ArrayList<>(Arrays.asList("0", "1", "8", "9"));

        // set required fields
        Map<String, Object> context = new HashMap<>();

        Map<String, String> rec = new HashMap<>();
        rec.put("primarySite", "C680");
        rec.put("histologicTypeIcdO3", "8000");
        rec.put("behaviorCodeIcdO3", "3");
        rec.put("grade", "9");
        rec.put("dateOfDiagnosisYear", "2013");
        rec.put("ageAtDiagnosis", "060");
        rec.put("vitalStatus", "1");
        rec.put("typeOfReportingSource", "1");
        _rule.execute(rec, null, null, context);

        // validate CSInput values are in acceptable ranges
        int size = Integer.parseInt(rec.get("csTumorSize"));
        Assert.assertTrue((size >= 0 && size <= 996) || size == 999);
        Assert.assertTrue(extList.contains(rec.get("csExtension")));
        int extEval = Integer.parseInt(rec.get("csTumorSizeExtEval"));
        Assert.assertTrue(extEval >= 0 && extEval <= 9 && extEval != 7 && extEval != 4);
        Assert.assertTrue(nodesList.contains(rec.get("csLymphNodes")));
        int nodesEval = Integer.parseInt(rec.get("csLymphNodesEval"));
        Assert.assertTrue(nodesEval >= 0 && nodesEval <= 9 && nodesEval != 7 && nodesEval != 4);
        int nodesPos = Integer.parseInt(rec.get("regionalNodesPositive"));
        Assert.assertTrue((nodesPos >= 0 && nodesPos <= 90) || nodesPos == 95 || (nodesPos >= 97 || nodesPos <= 99));
        int nodesExam = Integer.parseInt(rec.get("regionalNodesExamined"));
        Assert.assertTrue((nodesExam >= 0 && nodesExam <= 90) || (nodesExam >= 95 || nodesExam <= 99));
        Assert.assertTrue(metsList.contains(rec.get("csMetsAtDx")));
        int metsEval = Integer.parseInt(rec.get("csMetsEval"));
        Assert.assertTrue(metsEval >= 0 && metsEval <= 9 && metsEval != 7 && metsEval != 4);
        Assert.assertTrue(lviList.contains(rec.get("lymphVascularInvasion")));

        Assert.assertNotNull(rec.get("csSiteSpecificFactor1"));
        Assert.assertNotNull(rec.get("csSiteSpecificFactor2"));
        Assert.assertNotNull(rec.get("csSiteSpecificFactor3"));
        Assert.assertNotNull(rec.get("csSiteSpecificFactor4"));
        Assert.assertNotNull(rec.get("csSiteSpecificFactor5"));
        Assert.assertNotNull(rec.get("csSiteSpecificFactor6"));
        Assert.assertNotNull(rec.get("csSiteSpecificFactor7"));
        Assert.assertNotNull(rec.get("csSiteSpecificFactor8"));
        Assert.assertNotNull(rec.get("csSiteSpecificFactor9"));
        Assert.assertNotNull(rec.get("csSiteSpecificFactor10"));
        Assert.assertNotNull(rec.get("csSiteSpecificFactor11"));
        Assert.assertNotNull(rec.get("csSiteSpecificFactor12"));
        Assert.assertNotNull(rec.get("csSiteSpecificFactor13"));
        Assert.assertNotNull(rec.get("csSiteSpecificFactor14"));
        Assert.assertNotNull(rec.get("csSiteSpecificFactor15"));
        Assert.assertNotNull(rec.get("csSiteSpecificFactor16"));
        Assert.assertNotNull(rec.get("csSiteSpecificFactor17"));
        Assert.assertNotNull(rec.get("csSiteSpecificFactor18"));
        Assert.assertNotNull(rec.get("csSiteSpecificFactor19"));
        Assert.assertNotNull(rec.get("csSiteSpecificFactor20"));
        Assert.assertNotNull(rec.get("csSiteSpecificFactor21"));
        Assert.assertNotNull(rec.get("csSiteSpecificFactor22"));
        Assert.assertNotNull(rec.get("csSiteSpecificFactor23"));
        Assert.assertNotNull(rec.get("csSiteSpecificFactor24"));
        Assert.assertNotNull(rec.get("csSiteSpecificFactor25"));

        // derived version should be 020550
        Assert.assertEquals("020550", rec.get("csVersionDerived"));

        // C680, 8000 will only return one schema with no discriminator - so ssf25 should be 988
        Assert.assertEquals("988", rec.get("csSiteSpecificFactor25"));
    }
}
