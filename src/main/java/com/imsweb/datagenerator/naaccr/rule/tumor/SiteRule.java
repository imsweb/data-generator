package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;
import com.imsweb.datagenerator.random.DistributedRandomValueGenerator;

public class SiteRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "site";

    // file to the female frequencies
    private static final String _FILE_FREQUENCY_SITE_FEMALE = "frequencies/sites_sex_female.csv";

    // file to the male frequencies
    private static final String _FILE_FREQUENCY_SITE_MALE = "frequencies/sites_sex_male.csv";

    // random site generator for females
    private static final DistributedRandomValueGenerator VALUES_FEMALE = new DistributedRandomValueGenerator(_FILE_FREQUENCY_SITE_FEMALE);

    // random site generator for males
    private static final DistributedRandomValueGenerator VALUES_MALE = new DistributedRandomValueGenerator(_FILE_FREQUENCY_SITE_MALE);

    /**
     * Constructor.
     */
    public SiteRule() {
        super(ID, "Primary Site and related fields");
    }

    @Override
    public void execute(Map<String, String> tumor, List<Map<String, String>> otherTumors, NaaccrDataGeneratorOptions options) {

        List<String> values = tumor.get("sex").equals("2") ? VALUES_FEMALE.getRandomValueList() : VALUES_MALE.getRandomValueList();
        tumor.put("primarySite", values.get(0));
        tumor.put("histologyIcdO3", values.get(1));
        tumor.put("behaviorIcdO3", values.get(2));

        // set grade and laterality to 9, unknown
        tumor.put("grade", "9");

        if (tumor.get("primarySite").equals("C809"))
            // laterality should be 0 if site is C809 (unknown)
            tumor.put("laterality", "0");
        else
            tumor.put("laterality", "9");
    }
}
