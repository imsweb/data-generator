package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorTumorRule;
import com.imsweb.datagenerator.utils.RandomUtils;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

public class SeerCodingSystemRule extends NaaccrDataGeneratorTumorRule {

    // unique identifier for this rule
    public static final String ID = "seer-coding-system";

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
    public void execute(Tumor tumor, Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {

        // don't assign anything prior to 2004
        if (!inDxYearRange(tumor, 2004, null))
            return;

        String code;
        int yearDx = Integer.parseInt(tumor.getItemValue("dateOfDiagnosisYear"));

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
        else if (yearDx == 2015)
            code = "F";    // If Year of DX is 2015 , SEER Coding Sys--Current must equal F (2015 SEER Coding Manual).
        else if (yearDx == 2016 || yearDx == 2017)
            code = "G";    // If Year of DX is 2016-2017, SEER Coding Sys--Current must equal G (2016 SEER Coding Manual).
        else
            code = "H";    // If Year of DX is 2015 or later, SEER Coding Sys--Current must equal F (2018 SEER Coding Manual).

        setValue(tumor, "seerCodingSysCurrent", code);
    }
}
