/*
 * Copyright (C) 2017 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.hl7.rule;

import com.imsweb.datagenerator.hl7.NaaccrHl7DataGeneratorOptions;
import com.imsweb.datagenerator.hl7.NaaccrHl7DataGeneratorRule;
import com.imsweb.layout.hl7.Hl7MessageBuilder;
import com.imsweb.layout.hl7.entity.Hl7Message;

public class ObservationSegmentRule extends NaaccrHl7DataGeneratorRule {

    public ObservationSegmentRule() {
        super("observation-segment", "Observation/Result Segment (OBX)");
    }

    @Override
    public void execute(Hl7Message message, NaaccrHl7DataGeneratorOptions options) {

        // OBX-1: set ID
        // OBX-2: value type (CE for coded value, ST for short string text, FT for formatted text, TX for text data)
        // OBX-3: observation identifier
        // OBX-5: observation value
        // OBX-11: observation result status (F=final)

        new Hl7MessageBuilder(message).withSegment("OBX")
                .withField(1, "1")
                .withField(2, "TX")
                .withField(3, "22633-2", "nature of specimen", "LN")
                .withField(5, "Bone marrow.")
                .withField(11, "F");

        new Hl7MessageBuilder(message).withSegment("OBX")
                .withField(1, "2")
                .withField(2, "TX")
                .withField(3, "22636-5", "clinical history", "LN")
                .withField(5, "Evaluate for non-Hodgkin's lymphoma: ALL: myelodysplastic syndromes: chronic Lymphoproliferative disorders, CLL. Prior therapy: chemotherapy, Fludarabine more than one month ago. CBC report received.")
                .withField(11, "F");

        new Hl7MessageBuilder(message).withSegment("OBX")
                .withField(1, "3")
                .withField(2, "TX")
                .withField(3, "22638-1", "comments", "LN")
                .withField(5, "Correlation with a comprehensive bone marrow morphology examination, CBC data/blood smear, and other relevant clincial and laboratory data is recommended.")
                .withField(11, "F");

        new Hl7MessageBuilder(message).withSegment("OBX")
                .withField(1, "4")
                .withField(2, "TX")
                .withField(3, "22637-3", "final diagnosis", "LN")
                .withField(5, "A small population of monoclonal B-cells (Kappa) is present in the bone marrow. The antigenic profile is consistent with chronic lymphocytic leukemia/small lymphocytic lymphoma (CLL/SLL).")
                .withField(11, "F");

        new Hl7MessageBuilder(message).withSegment("OBX")
                .withField(1, "5")
                .withField(2, "TX")
                .withField(3, "22634-0^gross description", "gross description", "LN")
                .withField(5, "Part #1 is labeled \"left breast biopsy\" and is received fresh after frozen section preparation. It consists of a single firm nodule measuring 3cm in circular diameter and 1.5cm in thickness surrounded by adherent firbrofatty tissue. On section a pale gray, slightly mottled appearance is revealed. Numerous sections are submitted for permanent processing.\\.br\\Part #2 is labeled \"apical left axillary tissue\" and is received fresh. It consists of two amorphous fibrofatty tissue masses without grossly discernible lymph nodes therein. Both pieces are rendered into numerous sections and submitted in their entirety for history.\\.br\\Part #3 is labeled \"contents of left radical mastectomy\" and is received flesh. It consists of a large ellipse of skin overlying breast tissue, the ellipse measuring 20cm in length and 14 cm in height. A freshly sutured incision extends 3cm directly lateral from the areola, corresponding to the closure for removal of part #1. Abundant amounts of fibrofatty connective tissue surround the entire beast and the deep aspect includes and 8cm length of pectoralis minor and a generous mass of overlying pectoralis major muscle. Incision from the deepest aspect of the specimen beneath the tumor mass reveals tumor extension gross to within 0.5cm of muscle. Sections are submitted according to the following code: DE- deep surgical resection margins; SU, LA, INF, ME -- full thickness radila samplings from the center of the tumor superiorly, laterally, inferiorly and medially, respectively: NI- nipple and subjacent tissue. Lymph nodes dissected free from axillary fibrofatty tissue from levels I, II, and III will be labeled accordingly.")
                .withField(11, "F");
    }
}
