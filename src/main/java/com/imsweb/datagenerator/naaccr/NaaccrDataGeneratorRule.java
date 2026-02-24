package com.imsweb.datagenerator.naaccr;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.imsweb.naaccrxml.NaaccrFormat;
import com.imsweb.naaccrxml.entity.AbstractEntity;
import com.imsweb.naaccrxml.entity.Item;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

public abstract class NaaccrDataGeneratorRule {

    // the unique ID of this rule
    private final String _id;

    // the name of this rule
    private final String _name;

    /**
     * Constructor.
     * @param id rule ID
     * @param name rule name
     */
    protected NaaccrDataGeneratorRule(String id, String name) {
        _id = id;
        _name = name;
    }

    /**
     * Returns the rule's unique identifier.
     */
    public String getId() {
        return _id;
    }

    /**
     * Returns the rule's name.
     */
    public String getName() {
        return _name;
    }

    /**
     * Returns the required properties for this rule. If one of those properties doesn't have a value, the rule will be skipped.
     */
    public List<String> getRequiredProperties() {
        return Collections.emptyList();
    }

    protected void setValue(AbstractEntity entity, String property, String value) {
        Item item = entity.getItem(property);
        if (item != null)
            item.setValue(value);
        else
            entity.addItem(new Item(property, value));
    }

    /**
     * Checks that the provided records contains a non-blank value for each requested property.
     * @param entity current entity
     * @param properties requested properties
     * @return true if all the required properties have a value, false otherwise
     */
    protected boolean hasValue(AbstractEntity entity, String... properties) {
        for (String property : properties) {
            String val = entity.getItemValue(property);
            if (val == null || val.trim().isEmpty())
                return false;
        }
        return true;
    }

    /**
     * Returns true if the date of diagnosis year falls into a given range. Upper and lower year bounds are set by
     * fromYear and toYear parameters if they are not null.
     * @param tumor tumor being evaluated
     * @param fromYear earliest year in range in four digits, inclusive; this bound is ignored if set to null
     * @param toYear latest year in range in four digits, inclusive; this bound is ignored if set to null
     * @return true if diagnosis year is in range; false if out of range or if dateOfDiagnosisYear not found in tumor
     */
    protected boolean inDxYearRange(Tumor tumor, Integer fromYear, Integer toYear) {
        String dxYear = tumor.getItemValue("dateOfDiagnosisYear");

        // if no year, assume out of range
        if (dxYear == null)
            return false;

        int dxYearVal = Integer.parseInt(dxYear);
        return (fromYear == null || dxYearVal >= fromYear) && (toYear == null || dxYearVal <= toYear);
    }

    protected boolean isOnOrBeforeNaaccrVersion(Map<String, Object> context, String naaccrVersion) {
        String contextVersion = (String)context.get(NaaccrDataGenerator.CONTEXT_NAACCR_VERSION);
        if (contextVersion == null || naaccrVersion == null)
            return false;
        return naaccrVersion.compareTo(contextVersion) >= 0;
    }

    protected String getSexValue(Patient patient, Map<String, Object> context) {
        return patient.getItemValue(isOnOrBeforeNaaccrVersion(context, NaaccrFormat.NAACCR_VERSION_250) ? "sex" : "sexAssignedAtBirth");
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof NaaccrDataGeneratorRule that))
            return false;
        return _id.equals(that._id);
    }

    @Override
    public final int hashCode() {
        return _id.hashCode();
    }
}
