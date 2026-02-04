/*
 * Copyright (C) 2022 Information Management Services, Inc.
 */
package lab;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import testing.TestingUtils;

import com.imsweb.staging.Staging;
import com.imsweb.staging.entities.ColumnDefinition.ColumnType;
import com.imsweb.staging.entities.Input;
import com.imsweb.staging.entities.Schema;
import com.imsweb.staging.entities.SchemaLookup;
import com.imsweb.staging.entities.Table;

public class StagingDataGenerator {

    public static void main(String[] args) throws Exception {
        Staging cs = loadStagingInstance("cs-02.05.50.zip");
        Staging tnm = loadStagingInstance("tnm-2.1.zip");
        Staging eod = loadStagingInstance("eod_public-3.3.zip");

        SchemaInfo info = new SchemaInfo();

        Map<String, String> keyCache = readOldKeys("frequencies/staging_keys.csv");

        updateSchemasInSiteFile(cs, tnm, eod, "frequencies/sites_sex_female.csv", info, keyCache);
        updateSchemasInSiteFile(cs, tnm, eod, "frequencies/sites_sex_male.csv", info, keyCache);

        createStagingFiles(cs, info.getCsSchemas(), "frequencies/staging_cs.csv", keyCache);
        createStagingFiles(tnm, info.getTnmSchemas(), "frequencies/staging_tnm.csv", keyCache);
        createStagingFiles(eod, info.getEodSchemas(), "frequencies/staging_eod.csv", keyCache);

        File file = new File(TestingUtils.getWorkingDirectory() + "/src/main/resources/frequencies/staging_keys.csv");
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8))) {
            for (String key : keyCache.keySet().stream().sorted().toList()) {
                writer.write(key + "," + keyCache.get(key) + "\r\n");
            }
        }
    }

    private static Staging loadStagingInstance(String data) {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("staging/" + data)) {
            return Staging.getInstance(is);
        }
        catch (IOException e) {
            throw new IllegalStateException("Unable to initialize staging from " + data, e);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static Map<String, String> readOldKeys(String filePath) {
        Map<String, String> keyCache = new HashMap<>();

        File keysFile = new File(TestingUtils.getWorkingDirectory() + "/src/main/resources/" + filePath);
        if (keysFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(keysFile.toPath()), StandardCharsets.US_ASCII))) {
                String line = reader.readLine();
                while (line != null) {
                    String[] parts = StringUtils.split(line, ',');
                    keyCache.put(parts[0], parts[1]);
                    line = reader.readLine();
                }

            }
            catch (IOException e) {
                throw new IllegalStateException("Unable to read staging keys", e);
            }
        }

        return keyCache;
    }

    private static void updateSchemasInSiteFile(Staging cs, Staging tnm, Staging eod, String filename, SchemaInfo info, Map<String, String> keyCache) throws Exception {
        File file = new File(TestingUtils.getWorkingDirectory() + "/src/main/resources/" + filename);

        List<List<String>> lines = new ArrayList<>();
        for (String line : TestingUtils.readFile(file)) {
            String[] parts = StringUtils.split(line, ',');

            String freq = parts[0];
            String site = parts[1];
            String hist = parts[2];
            String beh = parts[3];

            SchemaLookup lookup = new SchemaLookup();
            lookup.setSite(site);
            lookup.setHistology(hist);
            lookup.setInput("sex_at_birth", filename.contains("female") ? "2" : "1");

            Schema csSchema = filterSchemas(cs.lookupSchema(lookup));
            Schema tnmSchema = filterSchemas(tnm.lookupSchema(lookup));
            Schema eodSchema = filterSchemas(eod.lookupSchema(lookup));

            List<String> newLines = new ArrayList<>();
            newLines.add(freq);
            newLines.add(site);
            newLines.add(hist);
            newLines.add(beh);

            info.getCsSchemas().add(csSchema.getId());
            String key = keyCache.computeIfAbsent(csSchema.getId(), k -> String.valueOf(keyCache.size() + 1));
            newLines.add(key);

            info.getTnmSchemas().add(tnmSchema.getId());
            key = keyCache.computeIfAbsent(tnmSchema.getId(), k -> String.valueOf(keyCache.size() + 1));
            newLines.add(key);

            info.getEodSchemas().add(eodSchema.getId());
            key = keyCache.computeIfAbsent(eodSchema.getId(), k -> String.valueOf(keyCache.size() + 1));
            newLines.add(key);

            lines.add(newLines);
        }

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8))) {
            for (List<String> line : lines) {
                writer.write(String.join(",", line));
                writer.write("\r\n");
            }
        }
    }

    private static Schema filterSchemas(List<Schema> schemas) {
        Set<String> exclusions = getInputExclusions();

        // only include a schema if it has at least one non-obsolete input (some TNM schema are completely obsolete)
        return schemas.stream()
                .filter(schema -> {
                    for (Input input : schema.getInputs().stream().sorted(Comparator.comparing(Input::getNaaccrXmlId)).toList()) {
                        if (!exclusions.contains(input.getKey()) && !input.getName().contains("OBSOLETE")) {
                            return true;
                        }
                    }
                    return false;
                })
                .findFirst()
                .orElse(null);
    }

    private static Set<String> getInputExclusions() {
        // these are the input we don't want to set
        Set<String> exclusions = new HashSet<>();
        exclusions.add("site");
        exclusions.add("hist");
        exclusions.add("year_dx");
        exclusions.add("cs_input_version_original");
        exclusions.add("behavior");
        exclusions.add("grade");
        exclusions.add("age_dx");
        exclusions.add("sex_at_birth");
        exclusions.add("ssf25"); // we are always taking the first schema, we don't use discriminators...
        return exclusions;
    }

    private static void createStagingFiles(Staging stating, Set<String> schemas, String filename, Map<String, String> keyCache) throws IOException {

        // these are the input we don't want to set
        Set<String> exclusions = getInputExclusions();

        File file = new File(TestingUtils.getWorkingDirectory() + "/src/main/resources/" + filename);
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8))) {

            for (String schemaId : schemas.stream().sorted().toList()) {
                Schema schema = stating.getSchema(schemaId);
                if (schema == null) {
                    throw new IllegalStateException("Unable to get schema '" + schemaId + "'");
                }

                Pattern digitRegx = Pattern.compile("^\\d+$|^\\d+-\\d+$");

                for (Input input : schema.getInputs().stream().sorted(Comparator.comparing(Input::getNaaccrXmlId)).toList()) {
                    if (!exclusions.contains(input.getKey()) && !input.getName().contains("OBSOLETE")) {
                        //System.out.println("\n***** " + input.getName());
                        Table table = stating.getTable(input.getTable());

                        int codeIdx = -1, descIdx = -1;
                        for (int i = 0; i < table.getColumnDefinitions().size(); i++) {
                            if (table.getColumnDefinitions().get(i).getType() == ColumnType.INPUT) {
                                codeIdx = i;
                            }
                            else if (table.getColumnDefinitions().get(i).getType() == ColumnType.DESCRIPTION) {
                                descIdx = i;
                            }
                        }

                        if (codeIdx != -1) {
                            boolean allDigitValues = true;
                            List<String> codes = new ArrayList<>();
                            for (List<String> rows : table.getRawRows()) {
                                if (descIdx == -1 || !rows.get(descIdx).contains("OBSOLETE")) {
                                    String code = rows.get(codeIdx);
                                    if (!StringUtils.isBlank(code)) {
                                        //System.out.println(" > " + code);
                                        codes.add(code);
                                        allDigitValues &= digitRegx.matcher(code).matches();
                                    }
                                }
                            }
                            if (allDigitValues) {
                                codes = recombineRanges(codes);
                            }
                            //System.out.println(input.getNaaccrXmlId() + ": " + String.join(";", codes));
                            String fieldKey = keyCache.computeIfAbsent(input.getNaaccrXmlId(), k -> String.valueOf(keyCache.size() + 1));
                            writer.write(keyCache.get(schemaId) + "," + fieldKey + "," + String.join(";", codes));
                            writer.write("\r\n");
                        }
                    }
                }
            }
        }
    }

    private static List<String> recombineRanges(List<String> list) {
        int length;
        if (list.getFirst().contains("-"))
            length = StringUtils.split(list.getFirst(), '-')[0].length();
        else
            length = list.getFirst().length();

        List<Integer> integers = new ArrayList<>();
        for (String s : list) {
            if (s.contains("-")) {
                String[] parts = StringUtils.split(s, '-');
                int low = Integer.parseInt(parts[0]);
                int high = Integer.parseInt(parts[1]);
                for (int i = low; i <= high; i++) {
                    integers.add(i);
                }
            }
            else {
                integers.add(Integer.parseInt(s));
            }
        }

        List<String> newRanges = new ArrayList<>();

        int curIdx = 0;
        while (curIdx < integers.size()) {
            int nextIdx = curIdx + 1;
            while (nextIdx < integers.size() && integers.get(nextIdx).equals(integers.get(nextIdx - 1) + 1)) {
                nextIdx++;
            }

            Integer low = integers.get(curIdx);
            Integer high = integers.get(nextIdx - 1);
            if (low.equals(high)) {
                newRanges.add(StringUtils.leftPad(low.toString(), length, '0'));
            }
            else if (low.equals(high - 1)) {
                newRanges.add(StringUtils.leftPad(low.toString(), length, '0'));
                newRanges.add(StringUtils.leftPad(high.toString(), length, '0'));
            }
            else {
                newRanges.add(StringUtils.leftPad(low.toString(), length, '0') + "-" + StringUtils.leftPad(high.toString(), length, '0'));
            }
            curIdx = nextIdx;
        }

        return newRanges;
    }

    private static class SchemaInfo {

        private final Set<String> _csSchemas;
        private final Set<String> _tnmSchemas;
        private final Set<String> _eodSchemas;

        public SchemaInfo() {
            _csSchemas = new HashSet<>();
            _tnmSchemas = new HashSet<>();
            _eodSchemas = new HashSet<>();
        }

        public Set<String> getCsSchemas() {
            return _csSchemas;
        }

        public Set<String> getTnmSchemas() {
            return _tnmSchemas;
        }

        public Set<String> getEodSchemas() {
            return _eodSchemas;
        }
    }
}
