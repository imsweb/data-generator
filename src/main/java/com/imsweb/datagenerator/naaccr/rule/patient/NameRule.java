package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;
import com.imsweb.datagenerator.utils.DistributedRandomValueGenerator;
import com.imsweb.datagenerator.utils.RandomUtils;
import com.imsweb.datagenerator.utils.UniformRandomValueGenerator;

public class NameRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "name";

    // file to last name frequencies
    protected static final String _FREQUENCY_FILE_LAST_WHITE = "frequencies/last_names_white.csv";
    protected static final String _FREQUENCY_FILE_LAST_BLACK = "frequencies/last_names_black.csv";
    protected static final String _FREQUENCY_FILE_LAST_API = "frequencies/last_names_api.csv";
    protected static final String _FREQUENCY_FILE_LAST_HISPANIC = "frequencies/last_names_hispanic.csv";

    // file to first names
    protected static final String _FILE_FIRST_MALE = "lists/first_names_male.csv";
    protected static final String _FILE_FIRST_FEMALE = "lists/first_names_female.csv";

    // random name generators
    protected static final DistributedRandomValueGenerator _VALUES_WHITE = new DistributedRandomValueGenerator(_FREQUENCY_FILE_LAST_WHITE);
    protected static final DistributedRandomValueGenerator _VALUES_BLACK = new DistributedRandomValueGenerator(_FREQUENCY_FILE_LAST_BLACK);
    protected static final DistributedRandomValueGenerator _VALUES_API = new DistributedRandomValueGenerator(_FREQUENCY_FILE_LAST_API);
    protected static final DistributedRandomValueGenerator _VALUES_HISPANIC = new DistributedRandomValueGenerator(_FREQUENCY_FILE_LAST_HISPANIC);
    protected static final UniformRandomValueGenerator _VALUES_NAME_FIRST_MALE = new UniformRandomValueGenerator(_FILE_FIRST_MALE);
    protected static final UniformRandomValueGenerator _VALES_NAME_FIRST_FEMALE = new UniformRandomValueGenerator(_FILE_FIRST_FEMALE);

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
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {
        record.put("nameLast", getLastNameByRace(record));
        record.put("nameMiddle", getMiddleName(record));
        record.put("nameFirst", getFirstName(record));
        record.put("namePrefix", getPrefix(record));
        record.put("nameSuffix", getSuffix(record));
        record.put("nameMaiden", getMaidenName(record));
        // if a maiden name was created and returned, generate a new last name
        if (!record.get("nameMaiden").isEmpty())
            record.put("nameLast", getSpouseLastName());
    }

    /**
     * Generates a random last name based on patient's assigned race
     * @param record patient record map
     * @return randomly generated surname
     */
    protected String getLastNameByRace(Map<String, String> record) {
        if (!record.get("spanishHispanicOrigin").equals("0"))
            return _VALUES_HISPANIC.getRandomValue();
        else if (record.get("race1").equals("02"))
            return _VALUES_BLACK.getRandomValue();
        else if (Arrays.asList("04", "05", "06", "07", "08", "10", "11", "12", "13", "14", "15", "16", "17", "20", "21", "22", "25", "26",
                "27", "28", "30", "31", "32", "96", "97").contains(record.get("race1")))
            return _VALUES_API.getRandomValue();
        else
            return _VALUES_WHITE.getRandomValue();
    }

    /**
     * Based on patient sex, returns a random first name
     * @param record patient record map
     * @return randomly selected first name
     */
    protected String getFirstName(Map<String, String> record) {
        return record.get("sex").equals("1") ? _VALUES_NAME_FIRST_MALE.getRandomValue() : _VALES_NAME_FIRST_FEMALE.getRandomValue();
    }

    /**
     * Based on patient sex returns a random middle name. There is a 5% chance only the middle
     * initial will be returned.
     * @param record patient record map
     * @return randomly selected middle name or middle initial
     */
    protected String getMiddleName(Map<String, String> record) {
        String name = getFirstName(record);

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

        if (record.get("sex").equals("1"))
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
        if (record.get("sex").equals("1"))
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
        if (record.get("sex").equals("2"))
            if (RandomUtils.nextInt(100) < 60)
                // patient is female and will have a maiden name - use the already generated last name
                maidenName = record.get("nameLast");
        return maidenName;
    }

    /**
     * Produces a new last name independent of current patient. This is used as the new surname when a maiden name is present
     * @return random last name
     */
    protected String getSpouseLastName() {
        switch (RandomUtils.nextInt(10)) {
            case 0:
                return _VALUES_HISPANIC.getRandomValue();
            case 1:
                return _VALUES_BLACK.getRandomValue();
            case 2:
                return _VALUES_API.getRandomValue();
            default:
                return _VALUES_WHITE.getRandomValue();
        }
    }
}
