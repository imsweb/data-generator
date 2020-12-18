package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorTumorRule;
import com.imsweb.datagenerator.utils.RandomUtils;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

public class MaritalStatusRule extends NaaccrDataGeneratorTumorRule {

    // unique identifier for this rule
    public static final String ID = "marital-status";

    /**
     * Constructor.
     */
    public MaritalStatusRule() {
        super(ID, "Marital Status at DX");
    }

    @Override
    public void execute(Tumor tumor, Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        // no maiden name (possibly male) - all possible marital status values are valid (1-6)
        if (!hasValue(tumor, "nameMaiden", "nameBirthSurname"))
            setValue(tumor, "maritalStatusAtDx", Integer.toString(RandomUtils.nextInt(6) + 1));
        else // patient has maiden name - status may be 2-married, 3-separated, 4-divorced, 5-widowed
            setValue(tumor, "maritalStatusAtDx", Integer.toString(RandomUtils.nextInt(4) + 2));
    }
}
