package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;
import com.imsweb.datagenerator.utils.DistributionUtils;
import com.imsweb.datagenerator.utils.RandomUtils;

public class NameRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "name";

    private static final String _DESCRIPTION = "Last Name is randomly generated based on SEER frequency, Spanish/Hispanic Origin, and Race 1.<br/>"
            + "First Name is randomly generated from a list based on Sex.<br/>"
            + "Middle Name is randomly generated from a list based on Sex.<br/>"
            + "Prefix is randomly generated from a list based on Sex.<br/>"
            + "Suffix is randomly generated from a list based on Sex."
            + "Maiden Name is set randomly if Sex is female. If it is set, it will always be the value of Last Name.";

    // lists of valid name prefixes and suffixes
    private static final String[] _VALUES_PREFIXES_MALE = {"Dr", "Rev", "Mr"};
    private static final String[] _VALUES_PREFIXES_FEMALE = {"Dr", "Rev", "Ms", "Mrs"};
    private static final String[] _VALUES_SUFFIXES = {"CPA", "LLD", "MD", "PhD", "Ret", "RN"};
    private static final String[] _VALUES_SUFFIXES_MALE = {"Jr.", "Sr.", "III", "IV"};

    /**
     * Constructor.
     */
    public NameRule() {
        super(ID, "Name (Last, First, Middle, Prefix, Suffix and Maiden if needed)");
    }

    @Override
    public List<String> getRequiredProperties() {
        return Arrays.asList("spanishHispanicOrigin", "race1", "sex");
    }

    @Override
    public String getDescription() {
        return _DESCRIPTION;
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {
        record.put("nameLast", getLastNameByRace(record));
        record.put("nameMiddle", getMiddleName(record));
        record.put("nameFirst", getFirstName(record));
        record.put("namePrefix", getPrefix(record));
        record.put("nameSuffix", getSuffix(record));
        record.put("nameMaiden", getMaidenName(record));
        // if a maiden name was created and returned, generate a new last name
        if (!record.get("nameMaiden").isEmpty())
            record.put("nameLast", getSpouseLastName(record));
    }

    /**
     * Generates a random last name based on patient's assigned race
     * @param record patient record map
     * @return randomly generated surname
     */
    protected String getLastNameByRace(Map<String, String> record) {
        return DistributionUtils.getNameLast(record.get("spanishHispanicOrigin"), record.get("race1"));
    }

    /**
     * Based on patient sex, returns a random first name
     * @param record patient record map
     * @return randomly selected first name
     */
    protected String getFirstName(Map<String, String> record) {
        return DistributionUtils.getNameFirst(record.get("sex"));
    }

    /**
     * Based on patient sex returns a random middle name. There is a 5% chance only the middle
     * initial will be returned.
     * @param record patient record map
     * @return randomly selected middle name or middle initial
     */
    protected String getMiddleName(Map<String, String> record) {
        String name = DistributionUtils.getNameFirst(record.get("sex"));

        // 1/20 chance middle name will be abbreviated
        return RandomUtils.nextInt(20) == 0 ? name.charAt(0) + "." : name;
    }

    /**
     * Returns a random name prefix (Mr, Ms, Rev,...) based on the sex of the patient
     * @param record patient record map
     * @return name prefix
     */
    protected String getPrefix(Map<String, String> record) {
        // 98% chance prefix will be blank
        if (RandomUtils.nextInt(100) > 1)
            return "";

        if ("1".equals(record.get("sex")))
            return _VALUES_PREFIXES_MALE[RandomUtils.nextInt(_VALUES_PREFIXES_MALE.length)];
        else
            return _VALUES_PREFIXES_FEMALE[RandomUtils.nextInt(_VALUES_PREFIXES_FEMALE.length)];
    }

    /**
     * Returns a random name suffix (Jr, MD, III,...) based on sex of patient
     * @param record patient record map
     * @return name suffix
     */
    protected String getSuffix(Map<String, String> record) {
        // 97% chance suffix will be blank
        if (RandomUtils.nextInt(100) > 2)
            return "";

        String suffix = "";
        if ("1".equals(record.get("sex")))
            suffix = _VALUES_SUFFIXES_MALE[RandomUtils.nextInt(_VALUES_SUFFIXES_MALE.length)];
        // 50% chance to use non-male specific suffix
        if (RandomUtils.nextInt(1) == 0)
            suffix = _VALUES_SUFFIXES[RandomUtils.nextInt(_VALUES_SUFFIXES.length)];

        return suffix;
    }

    /**
     * If patient is female, this method will return a maiden name 60% of the time. It will return a blank string otherwise. The maiden name is
     * assigned by simply reassigning the already generated last name to the maiden name, since it already accounts for the patient's race.
     * @param record patient record map
     * @return patient's maiden name (same as current last name) or blank if no maiden name
     */
    protected String getMaidenName(Map<String, String> record) {
        String maidenName = "";
        if ("2".equals(record.get("sex")))
            if (RandomUtils.nextInt(100) < 60)
                // patient is female and will have a maiden name - use the already generated last name
                maidenName = record.get("nameLast");
        return maidenName;
    }

    /**
     * Produces a new last name independent of current patient. This is used as the new surname when a maiden name is present
     * @return random last name
     */
    protected String getSpouseLastName(Map<String, String> record) {
        return DistributionUtils.getNameLast(record.get("spanishHispanicOrigin"), record.get("race1"));
    }
}
