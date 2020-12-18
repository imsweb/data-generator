package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorTumorRule;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

public class MultiTumorsRule extends NaaccrDataGeneratorTumorRule {

    // unique identifier for this rule
    public static final String ID = "multi-tumors";

    /**
     * Constructor.
     */
    public MultiTumorsRule() {
        super(ID, "Multiple Tumors fields");
    }

    @Override
    public List<String> getRequiredProperties() {
        return Arrays.asList("dateOfDiagnosisYear", "dateOfDiagnosisMonth", "dateOfDiagnosisDay");
    }

    @Override
    public void execute(Tumor tumor, Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {

        // don't assign anything prior to 2007
        if (!inDxYearRange(tumor, 2007, null))
            return;

        // for now this is set to the DX date...
        setValue(tumor, "dateOfMultTumorsYear", tumor.getItemValue("dateOfDiagnosisYear"));
        setValue(tumor, "dateOfMultTumorsMonth", tumor.getItemValue("dateOfDiagnosisMonth"));
        setValue(tumor, "dateOfMultTumorsDay", tumor.getItemValue("dateOfDiagnosisDay"));

        setValue(tumor, "multiTumorRptAsOnePrim", "99");
        setValue(tumor, "multiplicityCounter", "99");
    }
}
