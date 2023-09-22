package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorTumorRule;
import com.imsweb.datagenerator.utils.DistributionUtils;
import com.imsweb.datagenerator.utils.RandomUtils;
import com.imsweb.datagenerator.utils.dto.SiteFrequencyDto;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

import static com.imsweb.datagenerator.naaccr.NaaccrDataGenerator.CONTEXT_FLAG_CURRENT_TUMOR_INDEX;
import static com.imsweb.datagenerator.naaccr.NaaccrDataGenerator.CONTEXT_FLAG_SITE_FREQ_MAP;

public class SiteRule extends NaaccrDataGeneratorTumorRule {

    // unique identifier for this rule
    public static final String ID = "site";

    private static final Set<String> _PAIRED_SITES = new HashSet<>(
            Arrays.asList("C079", "C080", "C081", "C090", "C091", "C098", "C099", "C300", "C301", "C310", "C312", "C340", "C341", "C342", "C343", "C348", "C349", "C384", "C400", "C401", "C402",
                    "C403", "C413", "C414", "C440", "C441", "C442", "C443", "C444", "C445", "C446", "C447", "C471", "C472", "C491", "C492", "C500", "C501", "C502", "C503", "C504", "C505", "C506",
                    "C508", "C509", "C569", "C570", "C620", "C621", "C629", "C630", "C631", "C649", "C659", "C669", "C690", "C691", "C692", "C693", "C694", "C695", "C696", "C698", "C699", "C700",
                    "C710", "C711", "C712", "C713", "C714", "C722", "C723", "C724", "C725", "C740", "C741", "C749", "C754"));

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
        Map<Integer, SiteFrequencyDto> siteFreqMap = (Map<Integer, SiteFrequencyDto>)context.get(CONTEXT_FLAG_SITE_FREQ_MAP);
        if (currentTumorIndex != null && siteFreqMap != null) {
            setValue(tumor, "primarySite", siteFreqMap.get(currentTumorIndex).getSite());
            setValue(tumor, "histologicTypeIcdO3", siteFreqMap.get(currentTumorIndex).getHistology());
            setValue(tumor, "behaviorCodeIcdO3", siteFreqMap.get(currentTumorIndex).getBehavior());
        }

        if (!hasValue(tumor, "primarySite")) {
            SiteFrequencyDto dto = DistributionUtils.getSite(patient.getItemValue("sex"));
            setValue(tumor, "primarySite", dto.getSite());
            setValue(tumor, "histologicTypeIcdO3", dto.getHistology());
            setValue(tumor, "behaviorCodeIcdO3", dto.getBehavior());
        }

        // set grade to 9 (unknown)
        setValue(tumor, "grade", "9");

        // set laterality based on site (for paired sites, 45% right, 45% left, 10% unknown
        if (_PAIRED_SITES.contains(tumor.getItemValue("primarySite"))) {
            int token = RandomUtils.nextIntInRange(1, 100);
            if (token <= 45)
                setValue(tumor, "laterality", "1");
            else if (token <= 90)
                setValue(tumor, "laterality", "2");
            else
                setValue(tumor, "laterality", "9");
        }
        else
            setValue(tumor, "laterality", "0");
    }
}
