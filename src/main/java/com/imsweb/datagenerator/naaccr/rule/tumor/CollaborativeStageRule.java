package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;
import com.imsweb.datagenerator.utils.RandomUtils;
import com.imsweb.decisionengine.Endpoint.EndpointType;
import com.imsweb.staging.Staging;
import com.imsweb.staging.cs.CsDataProvider;
import com.imsweb.staging.cs.CsDataProvider.CsVersion;
import com.imsweb.staging.cs.CsSchemaLookup;
import com.imsweb.staging.cs.CsStagingData;
import com.imsweb.staging.cs.CsStagingData.CsInput;
import com.imsweb.staging.cs.CsStagingData.CsOutput;
import com.imsweb.staging.entities.StagingColumnDefinition;
import com.imsweb.staging.entities.StagingEndpoint;
import com.imsweb.staging.entities.StagingRange;
import com.imsweb.staging.entities.StagingSchema;
import com.imsweb.staging.entities.StagingSchemaInput;
import com.imsweb.staging.entities.StagingTable;
import com.imsweb.staging.entities.StagingTableRow;

public class CollaborativeStageRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "collaborative-stage";

    // Map of NAACCR record field names to CS table lookup keys
    private static final Map<String, String> _CS_FIELDS;

    static {
        _CS_FIELDS = new LinkedHashMap<>();
        _CS_FIELDS.put("csExtension", "extension");
        _CS_FIELDS.put("csTumorSizeExtEval", "extension_eval");
        _CS_FIELDS.put("csLymphNodes", "nodes");
        _CS_FIELDS.put("regionalNodesPositive", "nodes_pos");
        _CS_FIELDS.put("regionalNodesExamined", "nodes_exam");
        _CS_FIELDS.put("csMetsAtDx", "mets");
        _CS_FIELDS.put("csTumorSize", "size");
        _CS_FIELDS.put("csLymphNodesEval", "nodes_eval");
        _CS_FIELDS.put("csMetsEval", "mets_eval");
        _CS_FIELDS.put("lymphVascularInvasion", "lvi");
        _CS_FIELDS.put("csSiteSpecificFactor1", "ssf1");
        _CS_FIELDS.put("csSiteSpecificFactor2", "ssf2");
        _CS_FIELDS.put("csSiteSpecificFactor3", "ssf3");
        _CS_FIELDS.put("csSiteSpecificFactor4", "ssf4");
        _CS_FIELDS.put("csSiteSpecificFactor5", "ssf5");
        _CS_FIELDS.put("csSiteSpecificFactor6", "ssf6");
        _CS_FIELDS.put("csSiteSpecificFactor7", "ssf7");
        _CS_FIELDS.put("csSiteSpecificFactor8", "ssf8");
        _CS_FIELDS.put("csSiteSpecificFactor9", "ssf9");
        _CS_FIELDS.put("csSiteSpecificFactor10", "ssf10");
        _CS_FIELDS.put("csSiteSpecificFactor11", "ssf11");
        _CS_FIELDS.put("csSiteSpecificFactor12", "ssf12");
        _CS_FIELDS.put("csSiteSpecificFactor13", "ssf13");
        _CS_FIELDS.put("csSiteSpecificFactor14", "ssf14");
        _CS_FIELDS.put("csSiteSpecificFactor15", "ssf15");
        _CS_FIELDS.put("csSiteSpecificFactor16", "ssf16");
        _CS_FIELDS.put("csSiteSpecificFactor17", "ssf17");
        _CS_FIELDS.put("csSiteSpecificFactor18", "ssf18");
        _CS_FIELDS.put("csSiteSpecificFactor19", "ssf19");
        _CS_FIELDS.put("csSiteSpecificFactor20", "ssf20");
        _CS_FIELDS.put("csSiteSpecificFactor21", "ssf21");
        _CS_FIELDS.put("csSiteSpecificFactor22", "ssf22");
        _CS_FIELDS.put("csSiteSpecificFactor23", "ssf23");
        _CS_FIELDS.put("csSiteSpecificFactor24", "ssf24");
    }

    // staging object
    private final Staging _staging = Staging.getInstance(CsDataProvider.getInstance(CsVersion.v020550));

    /**
     * Constructor.
     */
    public CollaborativeStageRule() {
        super(ID, "Collaborative Stage fields");
    }

    @Override
    public List<String> getRequiredProperties() {
        return Arrays.asList("primarySite", "histologicTypeIcdO3", "behaviorCodeIcdO3", "grade", "dateOfDiagnosisYear", "ageAtDiagnosis", "vitalStatus", "typeOfReportingSource");
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options, Map<String, Object> context) {

        // Collaborative Stage was used only in 2004-2015
        if (!inDxYearRange(record, 2004, 2015))
            return;

        List<StagingSchema> lookup = _staging.lookupSchema(new CsSchemaLookup(record.get("primarySite"), record.get("histologicTypeIcdO3")));

        // get first schema - if multiple schemas returned, this will only be used to get a discriminator, and lookup will be repeated
        StagingSchema schema = _staging.getSchema(lookup.get(0).getId());
        Map<String, StagingSchemaInput> inputMap = schema.getInputMap();
        String schemaId = schema.getId();

        // assign discriminator to ssf25
        record.put("csSiteSpecificFactor25", getRandomValueFromTable(inputMap.get("ssf25").getTable(), "ssf25", record, schemaId));

        if (lookup.size() == 0)
            return;

        // if multiple schemas were returned, use discriminator to make another lookup
        if (lookup.size() > 1) {
            lookup = _staging.lookupSchema(new CsSchemaLookup(record.get("primarySite"), record.get("histologicTypeIcdO3"), record.get("csSiteSpecificFactor25")));
            if (lookup.size() == 0)
                return;
            schema = _staging.getSchema(lookup.get(0).getId());
            inputMap = schema.getInputMap();
            schemaId = schema.getId();
        }

        // loop over input fields, putting values into the record
        for (Entry<String, String> entry : _CS_FIELDS.entrySet()) {
            List<String> validValues = getAllValidValues(entry.getValue(), record, schemaId);
            if (validValues == null)
                // there are no restrictions on the valid values for this key - look up random value from tables
                record.put(entry.getKey(), getRandomValueFromTable(inputMap.get(entry.getValue()).getTable(), entry.getValue(), record, schemaId));
            else
                // there are restrictions on this key; select a random value from the valid values list
                record.put(entry.getKey(), validValues.get(RandomUtils.nextInt(validValues.size())));
        }

        record.put("csVersionInputCurrent", "020550");
        record.put("csVersionOriginal", "020550");

        CsStagingData data = new CsStagingData();
        data.setInput(CsInput.PRIMARY_SITE, record.get("primarySite"));
        data.setInput(CsInput.HISTOLOGY, record.get("histologicTypeIcdO3"));
        data.setInput(CsInput.BEHAVIOR, record.get("behaviorCodeIcdO3"));
        data.setInput(CsInput.GRADE, record.get("grade"));
        data.setInput(CsInput.DX_YEAR, record.get("dateOfDiagnosisYear"));
        data.setInput(CsInput.CS_VERSION_ORIGINAL, record.get("csVersionOriginal"));
        data.setInput(CsInput.TUMOR_SIZE, record.get("csTumorSize"));
        data.setInput(CsInput.EXTENSION, record.get("csExtension"));
        data.setInput(CsInput.EXTENSION_EVAL, record.get("csTumorSizeExtEval"));
        data.setInput(CsInput.LYMPH_NODES, record.get("csLymphNodes"));
        data.setInput(CsInput.LYMPH_NODES_EVAL, record.get("csLymphNodesEval"));
        data.setInput(CsInput.REGIONAL_NODES_POSITIVE, record.get("regionalNodesPositive"));
        data.setInput(CsInput.REGIONAL_NODES_EXAMINED, record.get("regionalNodesExamined"));
        data.setInput(CsInput.METS_AT_DX, record.get("csMetsAtDx"));
        data.setInput(CsInput.METS_EVAL, record.get("csMetsEval"));
        data.setInput(CsInput.LVI, record.get("lymphVascularInvasion"));
        data.setInput(CsInput.AGE_AT_DX, record.get("ageAtDiagnosis"));

        data.setSsf(1, record.get("csSiteSpecificFactor1"));
        data.setSsf(2, record.get("csSiteSpecificFactor2"));
        data.setSsf(3, record.get("csSiteSpecificFactor3"));
        data.setSsf(4, record.get("csSiteSpecificFactor4"));
        data.setSsf(5, record.get("csSiteSpecificFactor5"));
        data.setSsf(6, record.get("csSiteSpecificFactor6"));
        data.setSsf(7, record.get("csSiteSpecificFactor7"));
        data.setSsf(8, record.get("csSiteSpecificFactor8"));
        data.setSsf(9, record.get("csSiteSpecificFactor9"));
        data.setSsf(10, record.get("csSiteSpecificFactor10"));
        data.setSsf(11, record.get("csSiteSpecificFactor11"));
        data.setSsf(12, record.get("csSiteSpecificFactor12"));
        data.setSsf(13, record.get("csSiteSpecificFactor13"));
        data.setSsf(14, record.get("csSiteSpecificFactor14"));
        data.setSsf(15, record.get("csSiteSpecificFactor15"));
        data.setSsf(16, record.get("csSiteSpecificFactor16"));
        data.setSsf(17, record.get("csSiteSpecificFactor17"));
        data.setSsf(18, record.get("csSiteSpecificFactor18"));
        data.setSsf(19, record.get("csSiteSpecificFactor19"));
        data.setSsf(20, record.get("csSiteSpecificFactor20"));
        data.setSsf(21, record.get("csSiteSpecificFactor21"));
        data.setSsf(22, record.get("csSiteSpecificFactor22"));
        data.setSsf(23, record.get("csSiteSpecificFactor23"));
        data.setSsf(24, record.get("csSiteSpecificFactor24"));
        data.setSsf(25, record.get("csSiteSpecificFactor25"));

        _staging.stage(data);

        // set flag to 1 (AJCC fields derived from CS) or blank if not derived (before 2004)
        record.put("derivedAjccFlag", "1");
        record.put("derivedSs1977Flag", "1");
        record.put("derivedSs2000Flag", "1");

        // If Year of DX > 2003, the following CS Data Items cannot be blank
        record.put("csVersionDerived", data.getOutput(CsOutput.CSVER_DERIVED));
        record.put("derivedAjcc6T", data.getOutput(CsOutput.STOR_AJCC6_T));
        record.put("derivedAjcc6N", data.getOutput(CsOutput.STOR_AJCC6_N));
        record.put("derivedAjcc6M", data.getOutput(CsOutput.STOR_AJCC6_M));
        record.put("derivedAjcc6TDescriptor", data.getOutput(CsOutput.STOR_AJCC6_TDESCRIPTOR));
        record.put("derivedAjcc6NDescriptor", data.getOutput(CsOutput.STOR_AJCC6_NDESCRIPTOR));
        record.put("derivedAjcc6MDescriptor", data.getOutput(CsOutput.STOR_AJCC6_MDESCRIPTOR));
        record.put("derivedAjcc6StageGroup", data.getOutput(CsOutput.STOR_AJCC6_STAGE));
        record.put("derivedAjcc7T", data.getOutput(CsOutput.STOR_AJCC7_T));
        record.put("derivedAjcc7N", data.getOutput(CsOutput.STOR_AJCC7_N));
        record.put("derivedAjcc7M", data.getOutput(CsOutput.STOR_AJCC7_M));
        record.put("derivedAjcc7TDescriptor", data.getOutput(CsOutput.STOR_AJCC7_TDESCRIPTOR));
        record.put("derivedAjcc7NDescriptor", data.getOutput(CsOutput.STOR_AJCC7_NDESCRIPTOR));
        record.put("derivedAjcc7MDescriptor", data.getOutput(CsOutput.STOR_AJCC7_MDESCRIPTOR));
        record.put("derivedAjcc7StageGroup", data.getOutput(CsOutput.STOR_AJCC7_STAGE));
        record.put("derivedSs1977", data.getOutput(CsOutput.STOR_SS1977_STAGE));
        record.put("derivedSs2000", data.getOutput(CsOutput.STOR_SS2000_STAGE));

        if (record.get("csMetsAtDx").equals("00")) {
            record.put("csMetsAtDxBone", "0");
            record.put("csMetsAtDxBrain", "0");
            record.put("csMetsAtDxLung", "0");
            record.put("csMetsAtDxLiver", "0");
        }
        else if (record.get("csMetsAtDx").equals("98") && !schemaId.equals("ill_defined_other")) {
            record.put("csMetsAtDxBone", "8");
            record.put("csMetsAtDxBrain", "8");
            record.put("csMetsAtDxLung", "8");
            record.put("csMetsAtDxLiver", "8");
        }
        else {
            // if any of these are 1, csMetsAtDx must not be 00 or 99
            record.put("csMetsAtDxBone", "9");
            record.put("csMetsAtDxBrain", "9");
            record.put("csMetsAtDxLung", "9");
            record.put("csMetsAtDxLiver", "9");
        }
    }

    /**
     * Takes a table name and key and returns a random value for the given key
     * @param tableName CS Table name
     * @param key table key name
     * @return random value
     */
    private String getRandomValueFromTable(String tableName, String key, Map<String, String> record, String schemaId) {
        StagingTable table = _staging.getTable(tableName);

        // get a random row from the table
        List<StagingTableRow> tableRows = getValidTableRows(table, key, record, schemaId);
        StagingTableRow randomRow = tableRows.get(RandomUtils.nextInt(tableRows.size()));

        // get a random value or value range from the row using key
        List<StagingRange> stringRange = randomRow.getColumnInput(key);
        StagingRange randomStringRange = stringRange.get(RandomUtils.nextInt(stringRange.size()));

        // if no range, return high value, otherwise pick a random value in range and return it
        String value;
        if (randomStringRange.getHigh().equals(randomStringRange.getLow()))
            value = randomStringRange.getHigh();
        else {
            int high = Integer.parseInt(randomStringRange.getHigh());
            int low = Integer.parseInt(randomStringRange.getLow());
            value = StringUtils.leftPad(Integer.toString(RandomUtils.nextInt(high - low + 1) + low), randomStringRange.getLow().length(), "0");
        }

        return value;
    }

    /**
     * Takes a staging table and filters out any rows from the table that contain invalid or obsolete codes. It returns a list
     * of all valid rows. If there are no invalid codes in the table, the results of this method will be the same as table.getTableRows() in StagingTable.
     * @param table table containing rows for filtering
     * @param key table key being looked up
     * @param record tumor record
     * @param schemaId schema for this tumor
     * @return list of rows containing valid key values
     */
    private List<StagingTableRow> getValidTableRows(StagingTable table, String key, Map<String, String> record, String schemaId) {
        List<StagingTableRow> validRows = new ArrayList<>();

        // get the index number of the description column
        Integer descriptionColumnNumber = null;

        // find the description column number
        List<StagingColumnDefinition> stagingColumnDefinitions = table.getColumnDefinitions();
        for (int i = 0; i < stagingColumnDefinitions.size(); i++)
            if (stagingColumnDefinitions.get(i).getType().toString().equals("DESCRIPTION"))
                descriptionColumnNumber = i;

        List<StagingTableRow> tableRows = table.getTableRows();
        List<List<String>> tableRawRows = table.getRawRows();
        int rowsInTable = tableRows.size();

        for (int i = 0; i < rowsInTable; i++) {

            StagingTableRow row = tableRows.get(i);

            // check invalid values list - omit any input values that are marked invalid
            List<StagingRange> inputs = row.getColumnInput(key);
            boolean isInvalidValue = !inputs.isEmpty() && getInvalidValues(key, record, schemaId).contains(inputs.get(0).getHigh());

            // check for error in endpoints - omit these from table
            List<StagingEndpoint> endpoints = row.getEndpoints();
            boolean isErrorEndPoint = false;
            if (endpoints != null && !endpoints.isEmpty())
                for (StagingEndpoint endpoint : endpoints)
                    isErrorEndPoint |= endpoint.getType().equals(EndpointType.ERROR);

            // check for obsolete note in description - omit these
            boolean isObsoleteInDesc = descriptionColumnNumber != null && tableRawRows.get(i).get(descriptionColumnNumber).contains("OBSOLETE");

            if (!isObsoleteInDesc && !isInvalidValue && !isErrorEndPoint)
                validRows.add(tableRows.get(i));
        }

        // TODO: need better fix to remove 988 without leaving table empty
        if (validRows.size() > 1) {
            int i;
            for (i = 0; i < validRows.size(); i++)
                if (validRows.get(i).getColumnInput(key).get(0).getHigh().equals("988"))
                    validRows.remove(i);
        }

        return validRows;
    }

    /**
     * Returns a list of all valid values for a specific key or null if it cannot define all the valid values. If a list is returned, only values in the list
     * may be used when randomly selecting. If null is returned, it can be assumed all possible values are valid unless later identified as invalid.
     * @param key table key being looked up
     * @param record tumor record
     * @param schemaId schema for this tumor
     * @return list of all acceptable values for the key, or null if all acceptable values cannot be defined.
     */
    private List<String> getAllValidValues(String key, Map<String, String> record, String schemaId) {
        List<String> validValues = null;
        switch (key) {
            case "ssf4":
            case "ssf5":
                if (schemaId.equals("breast") && !record.get("csLymphNodes").equals("000"))
                    validValues = new ArrayList<>(Collections.singletonList("987"));
                break;
            case "ssf13":
                if (schemaId.equals("breast") && record.get("csSiteSpecificFactor7").equals("998"))
                    validValues = Collections.singletonList("988");
                break;
            case "nodes_pos":
            case "nodes_exam":
                if (record.get("typeOfReportingSource").equals("7") || schemaId.equals("heme_retic") || schemaId.equals("lymphoma") || schemaId.equals("brain") || schemaId.equals("cns_other")
                        || schemaId.equals("ill_defined_other") || schemaId.equals("placenta") || schemaId.equals("intracranial_gland") || schemaId.equals("myeloma_plasma_cell_disorder"))
                    validValues = Collections.singletonList("99");
                break;
            default:
        }
        return validValues;
    }

    /**
     * Returns a list of all invalid values for the specific key. When scanning the key's table, these are omitting from random selection
     * @param key table key being looked up
     * @param record tumor record
     * @param schemaId schema for this tumor
     * @return list of invalid values for the key
     */
    private List<String> getInvalidValues(String key, Map<String, String> record, String schemaId) {
        List<String> invalidValues = new ArrayList<>();

        switch (key) {
            case "size":
            case "nodes_eval":
            case "mets_eval":
                // for living patients (VS=1), remove eval values specifying evidence from autopsy
                if (record.get("vitalStatus").equals("1"))
                    invalidValues.add("8");
                break;
            case "extension_eval":
                // for living patients (VS=1), remove eval values specifying evidence from autopsy
                if (record.get("vitalStatus").equals("1"))
                    if (schemaId.equals("prostate"))
                        invalidValues.addAll(Arrays.asList("3", "8"));
                    else
                        invalidValues.addAll(Arrays.asList("2", "8"));
                break;
            case "ssf4":
            case "ssf5":
                if (schemaId.equals("breast") && record.get("csLymphNodes").equals("000"))
                    invalidValues.add("987");
                break;
            case "ssf13":
                if (schemaId.equals("breast") && !record.get("csSiteSpecificFactor12").equals("998"))
                    invalidValues.add("998");
                break;
            default:
        }
        return invalidValues;
    }
}
