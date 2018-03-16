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

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;

public class ComputedEthnicityRule extends NaaccrDataGeneratorRule {

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
        return Arrays.asList("sex", "nameLast", "nameMaiden");
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options, Map<String, String> context) {

        boolean lastNameMatch = _surnames.contains(record.get("nameLast").toUpperCase());
        boolean maidenNameEmpty = record.get("nameMaiden") == null || record.get("nameMaiden").isEmpty();
        boolean maidenNameMatch = !maidenNameEmpty && _surnames.contains(record.get("nameMaiden").toUpperCase());
        boolean isFemale = record.get("sex").equals("2");

        String computedEthnicity;
        if (maidenNameMatch && isFemale)
            computedEthnicity = "7";    // hispanic maiden name (female)
        else if (!lastNameMatch)
            if (isFemale)
                if (maidenNameEmpty)
                    computedEthnicity = "3";    // non-hispanic last name, missing maiden name
                else
                    computedEthnicity = "1";    // non-hispanic last name, non-hispanic maiden name
            else
                computedEthnicity = "2";    // non-hispanic last name, did not check maiden name; or patient was male
        else if (isFemale)
            if (maidenNameEmpty)
                computedEthnicity = "6";    // hispanic last name, missing maiden name
            else
                computedEthnicity = "4";    // hispanic last name, non-hispanic maiden name
        else
            computedEthnicity = "5";    // hispanic last name, did not check maiden name; or patient was male

        record.put("computedEthnicity", computedEthnicity);
        record.put("computedEthnicitySource", "2");
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
