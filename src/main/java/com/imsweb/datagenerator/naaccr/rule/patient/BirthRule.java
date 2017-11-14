package com.imsweb.datagenerator.naaccr.rule.patient;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;
import com.imsweb.datagenerator.utils.DistributionUtils;
import com.imsweb.datagenerator.utils.RandomUtils;

public class BirthRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "birth";

    private static final String _CRITERIA = "Birthplace Country is always set to USA. If no state was selected, then all states have an equal chance to be assigned as Birthplace State.<br/>"
            + "If a state was selected, that state has a 90% chance of being assigned as Birthplace State. The other states have an equal chance of being assigned."
            + "Date of Birth is randomly generated between a minimum and maximum date. The minimum date is always 100 years before the current date to ensure that no patient is older than 100."
            + "The maximum date is 5 years prior to the January 1 of the minimum DX year. If no minimum DX year was provided, the minimum date is 15 years before the current date.";
    // random birth state value generator
    private DistributedRandomValueGenerator _stateValues;

    /**
     * Constructor.
     */
    public BirthRule() {
        super(ID, "Date of Birth, Birthplace Country and State");
    }

    @Override
    public String getCriteria() {
        return _CRITERIA;
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {

        // birth date should be no later than five years prior to min dx date (or current date if min dx date not defined)
        LocalDate maxBirthDate = options == null ? LocalDate.now().minusYears(15) : options.getMinDxDate().minusYears(5);
        // limit age to max 100 years
        LocalDate minBirthDate = maxBirthDate.minusYears(100);

        LocalDate randomDate = RandomUtils.getRandomDateBetween(minBirthDate, maxBirthDate);

        record.put("birthDateYear", Integer.toString(randomDate.getYear()));
        record.put("birthDateMonth", Integer.toString(randomDate.getMonthValue()));
        record.put("birthDateDay", Integer.toString(randomDate.getDayOfMonth()));
        record.put("birthplaceState", DistributionUtils.getState());
        record.put("birthplaceCountry", "USA");
    }
}
