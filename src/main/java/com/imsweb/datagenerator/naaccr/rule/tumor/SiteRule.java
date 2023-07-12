package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorTumorRule;
import com.imsweb.datagenerator.utils.DistributionUtils;
import com.imsweb.datagenerator.utils.dto.SiteDto;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

import static com.imsweb.datagenerator.naaccr.NaaccrDataGenerator.CONTEXT_FLAG_CURRENT_TUMOR_INDEX;
import static com.imsweb.datagenerator.naaccr.NaaccrDataGenerator.CONTEXT_FLAG_SITE_FREQ_MAP;

public class SiteRule extends NaaccrDataGeneratorTumorRule {

    // unique identifier for this rule
    public static final String ID = "site";

    /**
     * Constructor.
     */
    public SiteRule() {
        super(ID, "Primary Site and related fields");
    }

    @Override
    public void execute(Tumor tumor, Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {

        Integer currentTumorIndex = (Integer)context.get(CONTEXT_FLAG_CURRENT_TUMOR_INDEX);

        @SuppressWarnings("unchecked")
        Map<Integer, SiteDto> siteFreqMap = (Map<Integer, SiteDto>)context.get(CONTEXT_FLAG_SITE_FREQ_MAP);
        if (currentTumorIndex != null && siteFreqMap != null) {
            setValue(tumor, "primarySite", siteFreqMap.get(currentTumorIndex).getSite());
            setValue(tumor, "histologicTypeIcdO3", siteFreqMap.get(currentTumorIndex).getHistology());
            setValue(tumor, "behaviorCodeIcdO3", siteFreqMap.get(currentTumorIndex).getBehavior());
        }

        if (!hasValue(tumor, "primarySite")) {
            SiteDto dto = DistributionUtils.getSite(patient.getItemValue("sex"));
            setValue(tumor, "primarySite", dto.getSite());
            setValue(tumor, "histologicTypeIcdO3", dto.getHistology());
            setValue(tumor, "behaviorCodeIcdO3", dto.getBehavior());
        }

        // set grade and laterality to 9, unknown
        setValue(tumor, "grade", "9");

        if ("C809".equals(tumor.getItemValue("primarySite")))
            // laterality should be 0 if site is C809 (unknown)
            setValue(tumor, "laterality", "0");
        else
            setValue(tumor, "laterality", "9");
    }
}
