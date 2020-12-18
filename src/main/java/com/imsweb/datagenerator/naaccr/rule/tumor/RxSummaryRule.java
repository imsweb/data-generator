package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorTumorRule;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

public class RxSummaryRule extends NaaccrDataGeneratorTumorRule {

    // unique identifier for this rule
    public static final String ID = "rx-summary";

    /**
     * Constructor.
     */
    public RxSummaryRule() {
        super(ID, "RX Summary fields");
    }

    @Override
    public List<String> getRequiredProperties() {
        return Collections.singletonList("dateOfDiagnosisYear");
    }

    @Override
    public void execute(Tumor tumor, Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {

        setValue(tumor, "rxSummTreatmentStatus", "9");
        // If Year of DX < 1998, RX Summ--Surg Prim Site must be blank.
        if (inDxYearRange(tumor, 1998, null))
            setValue(tumor, "rxSummSurgPrimSite", "99");
        // If year of DX < 2003, RX summ--scope reg LN sur and RX summ--scope reg LN sur must be blank.
        if (inDxYearRange(tumor, 2003, null)) {
            setValue(tumor, "rxSummScopeRegLnSur", "9");
            setValue(tumor, "rxSummSurgOthRegDis", "9");
        }
        // If Year of DX < 1998 or > 2003, RX Summ-reg LN Examined must be blank
        if (inDxYearRange(tumor, 1997, 2003))
            setValue(tumor, "rxSummRegLnExamined", "99");

        setValue(tumor, "rxSummSurgicalMargins", "9");
        setValue(tumor, "rxSummDxStgProc", "09");
        setValue(tumor, "rxSummPalliativeProc", "9");
        setValue(tumor, "rxSummRadiation", "9");
        setValue(tumor, "rxSummRadToCns", "9");
        setValue(tumor, "rxSummSurgRadSeq", "9");
        setValue(tumor, "rxSummTransplntEndocr", "99");
        setValue(tumor, "rxSummChemo", "99");
        setValue(tumor, "rxSummHormone", "99");
        setValue(tumor, "rxSummBrm", "99");
        setValue(tumor, "rxSummOther", "9");

        // If year of DX < 2006, RX Summ--Systemic Sur Seq must be blank
        if (inDxYearRange(tumor, 2006, null))
            setValue(tumor, "rxSummSystemicSurSeq", "9");

        // If Year of DX < 1998 or > 2002, RX summ--surg site 98-02, RX summ--scope reg 98-02, and RX summ--surg oth 98-02 must be blank.
        if (inDxYearRange(tumor, 1998, 2002)) {
            setValue(tumor, "rxSummSurgSite9802", "99");
            setValue(tumor, "rxSummScopeReg9802", "9");
            setValue(tumor, "rxSummSurgOth9802", "9");
        }

        setValue(tumor, "reasonForNoSurgery", "8");
    }
}
