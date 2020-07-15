/*
 * Copyright (C) 2017 Information Management Services, Inc.
 */
package lab;

import com.imsweb.datagenerator.hl7.NaaccrHl7DataGenerator;
import com.imsweb.datagenerator.hl7.NaaccrHl7DataGeneratorRule;
import com.imsweb.layout.LayoutFactory;

public class RuleDescriptionGenerator {

    public static void main(String[] args) {
        NaaccrHl7DataGenerator generator = new NaaccrHl7DataGenerator(LayoutFactory.LAYOUT_ID_NAACCR_HL7_V4);

        for (NaaccrHl7DataGeneratorRule rule : generator.getRules())
            System.out.println(" - " + rule.getName());
    }

}
