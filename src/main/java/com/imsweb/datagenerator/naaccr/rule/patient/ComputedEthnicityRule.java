package com.imsweb.datagenerator.naaccr.rule.patient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorPatientRule;
import com.imsweb.naaccrxml.entity.Patient;

@SuppressWarnings("java:S2160") // no need to override equals
public class ComputedEthnicityRule extends NaaccrDataGeneratorPatientRule {

    // unique identifier for this rule
    public static final String ID = "computed-ethnicity";

    // resource file containing list of spanish surnames
    protected static final String _FILE_SPANISH_SURNAMES = "frequencies/spanish_surnames.csv";

    // cached list of spanish surnames
    protected List<String> _surnames;

    /**
     * Constructor.
     */
    public ComputedEthnicityRule() {
        super(ID, "Computed Ethnicity");

        _surnames = loadSurnamesFromResources();
    }

    @Override
    public List<String> getRequiredProperties() {
        return Arrays.asList("sex", "nameLast");
    }

    @Override
    public void execute(Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {

        boolean lastNameMatch = _surnames.contains(patient.getItemValue("nameLast").toUpperCase());
        String birthSurname = patient.getItemValue("nameBirthSurname");
        if (StringUtils.isBlank(birthSurname))
            birthSurname = patient.getItemValue("nameMaiden");
        boolean birthSurnameEmpty = StringUtils.isBlank(birthSurname);
        boolean birthSurnameMatch = !birthSurnameEmpty && _surnames.contains(birthSurname.toUpperCase());
        boolean isFemale = patient.getItemValue("sex").equals("2");

        String computedEthnicity;
        if (birthSurnameMatch && isFemale)
            computedEthnicity = "7";    // hispanic maiden name (female)
        else if (!lastNameMatch) {
            if (isFemale) {
                if (birthSurnameEmpty)
                    computedEthnicity = "3";    // non-hispanic last name, missing maiden name
                else
                    computedEthnicity = "1";    // non-hispanic last name, non-hispanic maiden name
            }
            else
                computedEthnicity = "2";    // non-hispanic last name, did not check maiden name; or patient was male
        }
        else if (isFemale) {
            if (birthSurnameEmpty)
                computedEthnicity = "6";    // hispanic last name, missing maiden name
            else
                computedEthnicity = "4";    // hispanic last name, non-hispanic maiden name
        }
        else
            computedEthnicity = "5";    // hispanic last name, did not check maiden name; or patient was male

        setValue(patient, "computedEthnicity", computedEthnicity);
        setValue(patient, "computedEthnicitySource", "2");
    }

    /**
     * Reads line-separated list of spanish surnames from a resource file into a list
     */
    protected List<String> loadSurnamesFromResources() {
        List<String> spanishSurnames = new ArrayList<>();

        URL url = Thread.currentThread().getContextClassLoader().getResource(_FILE_SPANISH_SURNAMES);
        if (url == null)
            throw new IllegalStateException("Resource at '" + _FILE_SPANISH_SURNAMES + "' could not be found");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null)
                spanishSurnames.add(line);
        }
        catch (IOException e) {
            throw new IllegalStateException(e.getMessage());
        }

        return spanishSurnames;
    }
}
