package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;
import com.imsweb.datagenerator.utils.RandomUtils;

public class SeerCodingSystemRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "seer-coding-system";

    private static final String _CRITERIA = "SEER Coding System is based on Date of Diagnosis.<br/>"
            + "If the Date of Diagnosis is before 2004, this field is not set."
            + "If the Date of Diagnosis is in 2004, 2005, or 2006, the field is set to 7."
            + "If the Date of Diagnosis is in 2007, the field is randomly set to either 8 or 9."
            + "If the Date of Diagnosis is in 2008 or 2009, the field is set to 9."
            + "If the Date of Diagnosis is in 2010, the field is set to A."
            + "If the Date of Diagnosis is in 2011, the field is set to B."
            + "If the Date of Diagnosis is in 2012, the field is set to C."
            + "If the Date of Diagnosis is in 2013, the field is set to D."
            + "If the Date of Diagnosis is in 2014, the field is set to E."
            + "If the Date of Diagnosis is in 2015 or later, the field is set to F.";

    /**
     * Constructor.
     */
    public SeerCodingSystemRule() {
        super(ID, "SEER Coding System");
    }

    @Override
    public List<String> getRequiredProperties() {
        return Collections.singletonList("dateOfDiagnosisYear");
    }

    @Override
    public String getCriteria() {
        return _CRITERIA;
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {

        // don't assign anything prior to 2004
        if (!inDxYearRange(record, 2004, null))
            return;

        String code;
        int yearDx = Integer.parseInt(record.get("dateOfDiagnosisYear"));

        if (yearDx > 2003 && yearDx < 2007)
            code = "7";    // If Year of DX > 2003 and < 2007, SEER Coding Sys--Current must equal 7 (2004 SEER Coding Manual).
        else if (yearDx == 2007)
            code = Integer.toString(
                    RandomUtils.nextInt(2) + 8);    // If Year of DX = 2007, SEER Coding Sys--Current must equal 8 (Jan 2007 SEER Coding Manual) or 9 (2007 SEER Coding Manual with 2008 changes).
        else if (yearDx > 2007 && yearDx < 2010)
            code = "9";    // If Year of DX > 2007 and < 2010, SEER Coding Sys--Current must equal 9 (January 2008 SEER Coding Manual).
        else if (yearDx == 2010)
            code = "A";    // If Year of DX = 2010, SEER Coding Sys--Current must equal A (2010 SEER Coding Manual).
        else if (yearDx == 2011)
            code = "B";    // If Year of DX is 2011, SEER Coding Sys--Current must equal B (2011 SEER Coding Manual).
        else if (yearDx == 2012)
            code = "C";    // If Year of DX is 2012, SEER Coding Sys--Current must equal C (2012 SEER Coding Manual).
        else if (yearDx == 2013)
            code = "D";    // If Year of DX is 2013, SEER Coding Sys--Current must equal D (2013 SEER Coding Manual).
        else if (yearDx == 2014)
            code = "E";    // If Year of DX is 2014, SEER Coding Sys--Current must equal E (2014 SEER Coding Manual).
        else
            code = "F";    // If Year of DX is 2015 or later, SEER Coding Sys--Current must equal F (2015 SEER Coding Manual).

        record.put("seerCodingSysCurrent", code);
    }
}
