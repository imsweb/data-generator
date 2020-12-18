package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorTumorRule;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

public class DateOfConclusiveDxRule extends NaaccrDataGeneratorTumorRule {

    // unique identifier for this rule
    public static final String ID = "date-conclusive-dx";

    /**
     * Constructor.
     */
    public DateOfConclusiveDxRule() {
        super(ID, "Date Conclusive DX");
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
        setValue(tumor, "dateConclusiveDxYear", tumor.getItemValue("dateOfDiagnosisYear"));
        setValue(tumor, "dateConclusiveDxMonth", tumor.getItemValue("dateOfDiagnosisMonth"));
        setValue(tumor, "dateConclusiveDxDay", tumor.getItemValue("dateOfDiagnosisDay"));
    }
}
