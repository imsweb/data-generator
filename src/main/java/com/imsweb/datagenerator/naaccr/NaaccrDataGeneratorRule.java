package com.imsweb.datagenerator.naaccr;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    public NaaccrDataGeneratorRule(String id, String name) {
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

    /**
     * Executes the logic of this patient rule.
     * @param record current record
     * @param otherRecords other records for patient
     * @param options generator options
     * @param context communication between the rules (must not be null)
     */
    public abstract void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options, Map<String, Object> context);

    /**
     * Checks that the provided records contains a non-blank value for each requested property.
     * @param record current record
     * @param properties requested properties
     * @return true if all the required properties have a value, false otherwise
     */
    protected boolean propertyHasValue(Map<String, String> record, String... properties) {
        for (String property : properties) {
            String val = record.get(property);
            if (val == null || val.trim().isEmpty())
                return false;
        }
        return true;
    }

    /**
     * Returns true if the date of diagnosis year falls into a given range. Upper and lower year bounds are set by
     * fromYear and toYear parameters if they are not null.
     * @param record record for tumor being evaluated
     * @param fromYear earliest year in range in four digits, inclusive; this bound is ignored if set to null
     * @param toYear latest year in range in four digits, inclusive; this bound is ignored if set to null
     * @return true if diagnosis year is in range; false if out of range or if dateOfDiagnosisYear not found in tumor
     */
    protected boolean inDxYearRange(Map<String, String> record, Integer fromYear, Integer toYear) {
        String dxYear = record.get("dateOfDiagnosisYear");

        // if no year, assume out of range
        if (dxYear == null)
            return false;

        int dxYearVal = Integer.parseInt(dxYear);
        return (fromYear == null || dxYearVal >= fromYear) && (toYear == null || dxYearVal <= toYear);
    }

    /**
     * Returns the current year.
     */
    protected int getCurrentYear() {
        return LocalDate.now().getYear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof NaaccrDataGeneratorRule))
            return false;
        NaaccrDataGeneratorRule that = (NaaccrDataGeneratorRule)o;
        return _id.equals(that._id);
    }

    @Override
    public int hashCode() {
        return _id.hashCode();
    }
}
