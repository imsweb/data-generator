package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorPatientRule;
import com.imsweb.datagenerator.utils.DistributionUtils;
import com.imsweb.datagenerator.utils.RandomUtils;
import com.imsweb.naaccrxml.entity.Patient;

@SuppressWarnings("java:S2160")
public class NameRule extends NaaccrDataGeneratorPatientRule {

    // unique identifier for this rule
    public static final String ID = "name";

    // lists of valid name prefixes and suffixes
    private static final String[] _VALUES_PREFIXES_MALE = {"Dr", "Rev", "Mr"};
    private static final String[] _VALUES_PREFIXES_FEMALE = {"Dr", "Rev", "Ms", "Mrs"};
    private static final String[] _VALUES_SUFFIXES = {"CPA", "LLD", "MD", "PhD", "Ret", "RN"};
    private static final String[] _VALUES_SUFFIXES_MALE = {"Jr.", "Sr.", "III", "IV"};

    private final boolean _useMaidenNameField;

    /**
     * Constructor.
     */
    public NameRule(boolean useMaidenNameField) {
        super(ID, "Name (Last, First, Middle, Prefix, Suffix and Maiden if needed)");

        _useMaidenNameField = useMaidenNameField;
    }

    @Override
    public List<String> getRequiredProperties() {
        return Arrays.asList("spanishHispanicOrigin", "race1", "sex");
    }

    @Override
    public void execute(Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        setValue(patient, "nameLast", getLastNameByRace(patient));
        setValue(patient, "nameMiddle", getMiddleName(patient));
        setValue(patient, "nameFirst", getFirstName(patient));
        setValue(patient, "namePrefix", getPrefix(patient));
        setValue(patient, "nameSuffix", getSuffix(patient));

        String surnameKey = _useMaidenNameField ? "nameMaiden" : "nameBirthSurname";
        setValue(patient, surnameKey, getBirthSurnameName(patient));
        // if a birth surname was created and returned, generate a new last name
        if (!StringUtils.isBlank(patient.getItemValue(surnameKey)))
            setValue(patient, "nameLast", getSpouseLastName(patient));
    }

    /**
     * Generates a random last name based on patient's assigned race
     * @param patient current patient
     * @return randomly generated surname
     */
    protected String getLastNameByRace(Patient patient) {
        return DistributionUtils.getNameLast(patient.getItemValue("spanishHispanicOrigin"), patient.getItemValue("race1"));
    }

    /**
     * Based on patient sex, returns a random first name
     * @param patient current patient
     * @return randomly selected first name
     */
    protected String getFirstName(Patient patient) {
        return DistributionUtils.getNameFirst(patient.getItemValue("sex"));
    }

    /**
     * Based on patient sex returns a random middle name. There is a 5% chance only the middle
     * initial will be returned.
     * @param patient current patient
     * @return randomly selected middle name or middle initial
     */
    protected String getMiddleName(Patient patient) {
        String name = DistributionUtils.getNameFirst(patient.getItemValue("sex"));

        // 1/20 chance middle name will be abbreviated
        return RandomUtils.nextInt(20) == 0 ? name.charAt(0) + "." : name;
    }

    /**
     * Returns a random name prefix (Mr, Ms, Rev,...) based on the sex of the patient
     * @param patient current patient
     * @return name prefix
     */
    protected String getPrefix(Patient patient) {
        // 98% chance prefix will be blank
        if (RandomUtils.nextInt(100) > 1)
            return "";

        if ("1".equals(patient.getItemValue("sex")))
            return _VALUES_PREFIXES_MALE[RandomUtils.nextInt(_VALUES_PREFIXES_MALE.length)];
        else
            return _VALUES_PREFIXES_FEMALE[RandomUtils.nextInt(_VALUES_PREFIXES_FEMALE.length)];
    }

    /**
     * Returns a random name suffix (Jr, MD, III,...) based on sex of patient
     * @param patient current patient
     * @return name suffix
     */
    protected String getSuffix(Patient patient) {
        // 97% chance suffix will be blank
        if (RandomUtils.nextInt(100) > 2)
            return "";

        String suffix = "";
        if ("1".equals(patient.getItemValue("sex")))
            suffix = _VALUES_SUFFIXES_MALE[RandomUtils.nextInt(_VALUES_SUFFIXES_MALE.length)];
        // 50% chance to use non-male specific suffix
        if (RandomUtils.nextInt(1) == 0)
            suffix = _VALUES_SUFFIXES[RandomUtils.nextInt(_VALUES_SUFFIXES.length)];

        return suffix;
    }

    /**
     * If patient is female, this method will return a birth surname name 60% of the time. It will return a blank string otherwise. The surname is
     * assigned by simply reassigning the already generated last name to the surname name, since it already accounts for the patient's race.
     * @param patient current patient
     * @return patient's birth surname (same as current last name) or blank if no surname name
     */
    protected String getBirthSurnameName(Patient patient) {
        String birthSurname = "";
        if ("2".equals(patient.getItemValue("sex")))
            if (RandomUtils.nextInt(100) < 60)
                // patient is female and will have a birth surname - use the already generated last name
                birthSurname = patient.getItemValue("nameLast");
        return birthSurname;
    }

    /**
     * Produces a new last name independent of current patient. This is used as the new surname when a birth surname is present
     * @return random last name
     */
    protected String getSpouseLastName(Patient patient) {
        return DistributionUtils.getNameLast(patient.getItemValue("spanishHispanicOrigin"), patient.getItemValue("race1"));
    }
}
