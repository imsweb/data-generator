package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;

public class RxSummaryRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "rx-summary";

    /**
     * Constructor.
     */
    public RxSummaryRule() {
        super(ID, "RX Summary fields");
    }

    private static final String _DESCRIPTION = "RX Summary fields are set based on Date of Diagnosis";

    @Override
    public List<String> getRequiredProperties() {
        return Collections.singletonList("dateOfDiagnosisYear");
    }

    @Override
    public String getDescription() {
        return _DESCRIPTION;
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {

        record.put("rxSummTreatmentStatus", "9");
        // If Year of DX < 1998, RX Summ--Surg Prim Site must be blank.
        if (inDxYearRange(record, 1998, null))
            record.put("rxSummSurgPrimSite", "99");
        // If year of DX < 2003, RX summ--scope reg LN sur and RX summ--scope reg LN sur must be blank.
        if (inDxYearRange(record, 2003, null)) {
            record.put("rxSummScopeRegLnSur", "9");
            record.put("rxSummSurgOthRegDis", "9");
        }
        // If Year of DX < 1998 or > 2003, RX Summ-reg LN Examined must be blank
        if (inDxYearRange(record, 1997, 2003))
            record.put("rxSummRegLnExamined", "99");

        record.put("rxSummSurgicalMargins", "9");
        record.put("rxSummDxStgProc", "09");
        record.put("rxSummPalliativeProc", "9");
        record.put("rxSummRadiation", "9");
        record.put("rxSummRadToCns", "9");
        record.put("rxSummSurgRadSeq", "9");
        record.put("rxSummTransplntEndocr", "99");
        record.put("rxSummChemo", "99");
        record.put("rxSummHormone", "99");
        record.put("rxSummBrm", "99");
        record.put("rxSummOther", "9");

        // If year of DX < 2006, RX Summ--Systemic Sur Seq must be blank
        if (inDxYearRange(record, 2006, null))
            record.put("rxSummSystemicSurgSeq", "9");

        // If Year of DX < 1998 or > 2002, RX summ--surg site 98-02, RX summ--scope reg 98-02, and RX summ--surg oth 98-02 must be blank.
        if (inDxYearRange(record, 1998, 2002)) {
            record.put("rxSummSurgSite9802", "99");
            record.put("rxSummScopeReg9802", "9");
            record.put("rxSummSurgOth9802", "9");
        }

        record.put("reasonForNoSurgery", "8");
    }
}
