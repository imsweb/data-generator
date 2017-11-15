package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;
import com.imsweb.datagenerator.utils.DistributionUtils;
import com.imsweb.datagenerator.utils.dto.SiteFrequencyDto;

public class SiteRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "site";

    private static final String _DESCRIPTION = "Primary Site, Histology, and Behavior are randomly generated based on SEER frequencies and Sex.<br/>"
            + "Grade is always set to 9. If Primary Site is C809 (unknown), Laterality is set to 0. Otherwise it is set to 9.";

    /**
     * Constructor.
     */
    public SiteRule() {
        super(ID, "Primary Site and related fields");
    }

    @Override
    public String getDescription() {
        return _DESCRIPTION;
    }

    @Override
    public void execute(Map<String, String> tumor, List<Map<String, String>> otherTumors, NaaccrDataGeneratorOptions options) {

        SiteFrequencyDto dto = DistributionUtils.getSite(tumor.get("sex"));
        tumor.put("primarySite", dto.getSite());
        tumor.put("histologyIcdO3", dto.getHistology());
        tumor.put("behaviorIcdO3", dto.getBehavior());

        // set grade and laterality to 9, unknown
        tumor.put("grade", "9");

        if (tumor.get("primarySite").equals("C809"))
            // laterality should be 0 if site is C809 (unknown)
            tumor.put("laterality", "0");
        else
            tumor.put("laterality", "9");
    }
}
