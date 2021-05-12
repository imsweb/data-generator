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
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorTumorRule;
import com.imsweb.datagenerator.utils.RandomUtils;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;
import com.imsweb.staging.Staging;
import com.imsweb.staging.cs.CsDataProvider;
import com.imsweb.staging.cs.CsDataProvider.CsVersion;
import com.imsweb.staging.cs.CsSchemaLookup;
import com.imsweb.staging.cs.CsStagingData;
import com.imsweb.staging.cs.CsStagingData.CsInput;
import com.imsweb.staging.cs.CsStagingData.CsOutput;
import com.imsweb.staging.entities.ColumnDefinition;
import com.imsweb.staging.entities.Endpoint;
import com.imsweb.staging.entities.Endpoint.EndpointType;
import com.imsweb.staging.entities.Input;
import com.imsweb.staging.entities.Range;
import com.imsweb.staging.entities.Schema;
import com.imsweb.staging.entities.Table;
import com.imsweb.staging.entities.TableRow;

public class CollaborativeStageRule extends NaaccrDataGeneratorTumorRule {

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
        return Arrays.asList("primarySite", "histologicTypeIcdO3", "behaviorCodeIcdO3", "grade", "dateOfDiagnosisYear", "ageAtDiagnosis", "typeOfReportingSource");
    }

    @Override
    public void execute(Tumor tumor, Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {

        // Collaborative Stage was used only in 2004-2015
        if (!inDxYearRange(tumor, 2004, 2015))
            return;

        List<Schema> lookup = _staging.lookupSchema(new CsSchemaLookup(tumor.getItemValue("primarySite"), tumor.getItemValue("histologicTypeIcdO3")));

        // get first schema - if multiple schemas returned, this will only be used to get a discriminator, and lookup will be repeated
        Schema schema = _staging.getSchema(lookup.get(0).getId());
        Map<String, ? extends Input> inputMap = schema.getInputMap();
        String schemaId = schema.getId();

        // assign discriminator to ssf25
        setValue(tumor, "csSiteSpecificFactor25", getRandomValueFromTable(inputMap.get("ssf25").getTable(), "ssf25", tumor, patient, schemaId));

        if (lookup.size() == 0)
            return;

        // if multiple schemas were returned, use discriminator to make another lookup
        if (lookup.size() > 1) {
            lookup = _staging.lookupSchema(new CsSchemaLookup(tumor.getItemValue("primarySite"), tumor.getItemValue("histologicTypeIcdO3"), tumor.getItemValue("csSiteSpecificFactor25")));
            if (lookup.size() == 0)
                return;
            schema = _staging.getSchema(lookup.get(0).getId());
            inputMap = schema.getInputMap();
            schemaId = schema.getId();
        }

        // loop over input fields, putting values into the record
        for (Entry<String, String> entry : _CS_FIELDS.entrySet()) {
            List<String> validValues = getAllValidValues(entry.getValue(), tumor, schemaId);
            if (validValues == null)
                // there are no restrictions on the valid values for this key - look up random value from tables
                setValue(tumor, entry.getKey(), getRandomValueFromTable(inputMap.get(entry.getValue()).getTable(), entry.getValue(), tumor, patient, schemaId));
            else
                // there are restrictions on this key; select a random value from the valid values list
                setValue(tumor, entry.getKey(), validValues.get(RandomUtils.nextInt(validValues.size())));
        }

        setValue(tumor, "csVersionInputCurrent", "020550");
        setValue(tumor, "csVersionOriginal", "020550");

        CsStagingData data = new CsStagingData();
        data.setInput(CsInput.PRIMARY_SITE, tumor.getItemValue("primarySite"));
        data.setInput(CsInput.HISTOLOGY, tumor.getItemValue("histologicTypeIcdO3"));
        data.setInput(CsInput.BEHAVIOR, tumor.getItemValue("behaviorCodeIcdO3"));
        data.setInput(CsInput.GRADE, tumor.getItemValue("grade"));
        data.setInput(CsInput.DX_YEAR, tumor.getItemValue("dateOfDiagnosisYear"));
        data.setInput(CsInput.CS_VERSION_ORIGINAL, tumor.getItemValue("csVersionOriginal"));
        data.setInput(CsInput.TUMOR_SIZE, tumor.getItemValue("csTumorSize"));
        data.setInput(CsInput.EXTENSION, tumor.getItemValue("csExtension"));
        data.setInput(CsInput.EXTENSION_EVAL, tumor.getItemValue("csTumorSizeExtEval"));
        data.setInput(CsInput.LYMPH_NODES, tumor.getItemValue("csLymphNodes"));
        data.setInput(CsInput.LYMPH_NODES_EVAL, tumor.getItemValue("csLymphNodesEval"));
        data.setInput(CsInput.REGIONAL_NODES_POSITIVE, tumor.getItemValue("regionalNodesPositive"));
        data.setInput(CsInput.REGIONAL_NODES_EXAMINED, tumor.getItemValue("regionalNodesExamined"));
        data.setInput(CsInput.METS_AT_DX, tumor.getItemValue("csMetsAtDx"));
        data.setInput(CsInput.METS_EVAL, tumor.getItemValue("csMetsEval"));
        data.setInput(CsInput.LVI, tumor.getItemValue("lymphVascularInvasion"));
        data.setInput(CsInput.AGE_AT_DX, tumor.getItemValue("ageAtDiagnosis"));

        data.setSsf(1, tumor.getItemValue("csSiteSpecificFactor1"));
        data.setSsf(2, tumor.getItemValue("csSiteSpecificFactor2"));
        data.setSsf(3, tumor.getItemValue("csSiteSpecificFactor3"));
        data.setSsf(4, tumor.getItemValue("csSiteSpecificFactor4"));
        data.setSsf(5, tumor.getItemValue("csSiteSpecificFactor5"));
        data.setSsf(6, tumor.getItemValue("csSiteSpecificFactor6"));
        data.setSsf(7, tumor.getItemValue("csSiteSpecificFactor7"));
        data.setSsf(8, tumor.getItemValue("csSiteSpecificFactor8"));
        data.setSsf(9, tumor.getItemValue("csSiteSpecificFactor9"));
        data.setSsf(10, tumor.getItemValue("csSiteSpecificFactor10"));
        data.setSsf(11, tumor.getItemValue("csSiteSpecificFactor11"));
        data.setSsf(12, tumor.getItemValue("csSiteSpecificFactor12"));
        data.setSsf(13, tumor.getItemValue("csSiteSpecificFactor13"));
        data.setSsf(14, tumor.getItemValue("csSiteSpecificFactor14"));
        data.setSsf(15, tumor.getItemValue("csSiteSpecificFactor15"));
        data.setSsf(16, tumor.getItemValue("csSiteSpecificFactor16"));
        data.setSsf(17, tumor.getItemValue("csSiteSpecificFactor17"));
        data.setSsf(18, tumor.getItemValue("csSiteSpecificFactor18"));
        data.setSsf(19, tumor.getItemValue("csSiteSpecificFactor19"));
        data.setSsf(20, tumor.getItemValue("csSiteSpecificFactor20"));
        data.setSsf(21, tumor.getItemValue("csSiteSpecificFactor21"));
        data.setSsf(22, tumor.getItemValue("csSiteSpecificFactor22"));
        data.setSsf(23, tumor.getItemValue("csSiteSpecificFactor23"));
        data.setSsf(24, tumor.getItemValue("csSiteSpecificFactor24"));
        data.setSsf(25, tumor.getItemValue("csSiteSpecificFactor25"));

        _staging.stage(data);

        // set flag to 1 (AJCC fields derived from CS) or blank if not derived (before 2004)
        setValue(tumor, "derivedAjccFlag", "1");
        setValue(tumor, "derivedSs1977Flag", "1");
        setValue(tumor, "derivedSs2000Flag", "1");

        // If Year of DX > 2003, the following CS Data Items cannot be blank
        setValue(tumor, "csVersionDerived", data.getOutput(CsOutput.CSVER_DERIVED));
        setValue(tumor, "derivedAjcc6T", data.getOutput(CsOutput.STOR_AJCC6_T));
        setValue(tumor, "derivedAjcc6N", data.getOutput(CsOutput.STOR_AJCC6_N));
        setValue(tumor, "derivedAjcc6M", data.getOutput(CsOutput.STOR_AJCC6_M));
        setValue(tumor, "derivedAjcc6TDescriptor", data.getOutput(CsOutput.STOR_AJCC6_TDESCRIPTOR));
        setValue(tumor, "derivedAjcc6NDescriptor", data.getOutput(CsOutput.STOR_AJCC6_NDESCRIPTOR));
        setValue(tumor, "derivedAjcc6MDescriptor", data.getOutput(CsOutput.STOR_AJCC6_MDESCRIPTOR));
        setValue(tumor, "derivedAjcc6StageGroup", data.getOutput(CsOutput.STOR_AJCC6_STAGE));
        setValue(tumor, "derivedAjcc7T", data.getOutput(CsOutput.STOR_AJCC7_T));
        setValue(tumor, "derivedAjcc7N", data.getOutput(CsOutput.STOR_AJCC7_N));
        setValue(tumor, "derivedAjcc7M", data.getOutput(CsOutput.STOR_AJCC7_M));
        setValue(tumor, "derivedAjcc7TDescriptor", data.getOutput(CsOutput.STOR_AJCC7_TDESCRIPTOR));
        setValue(tumor, "derivedAjcc7NDescriptor", data.getOutput(CsOutput.STOR_AJCC7_NDESCRIPTOR));
        setValue(tumor, "derivedAjcc7MDescriptor", data.getOutput(CsOutput.STOR_AJCC7_MDESCRIPTOR));
        setValue(tumor, "derivedAjcc7StageGroup", data.getOutput(CsOutput.STOR_AJCC7_STAGE));
        setValue(tumor, "derivedSs1977", data.getOutput(CsOutput.STOR_SS1977_STAGE));
        setValue(tumor, "derivedSs2000", data.getOutput(CsOutput.STOR_SS2000_STAGE));

        if (tumor.getItemValue("csMetsAtDx").equals("00")) {
            setValue(tumor, "csMetsAtDxBone", "0");
            setValue(tumor, "csMetsAtDxBrain", "0");
            setValue(tumor, "csMetsAtDxLung", "0");
            setValue(tumor, "csMetsAtDxLiver", "0");
        }
        else if (tumor.getItemValue("csMetsAtDx").equals("98") && !schemaId.equals("ill_defined_other")) {
            setValue(tumor, "csMetsAtDxBone", "8");
            setValue(tumor, "csMetsAtDxBrain", "8");
            setValue(tumor, "csMetsAtDxLung", "8");
            setValue(tumor, "csMetsAtDxLiver", "8");
        }
        else {
            // if any of these are 1, csMetsAtDx must not be 00 or 99
            setValue(tumor, "csMetsAtDxBone", "9");
            setValue(tumor, "csMetsAtDxBrain", "9");
            setValue(tumor, "csMetsAtDxLung", "9");
            setValue(tumor, "csMetsAtDxLiver", "9");
        }
    }

    /**
     * Takes a table name and key and returns a random value for the given key
     * @param tableName CS Table name
     * @param key table key name
     * @param tumor tumor
     * schemaId the schema ID
     * @return random value
     */
    private String getRandomValueFromTable(String tableName, String key, Tumor tumor, Patient patient, String schemaId) {
        Table table = _staging.getTable(tableName);

        // get a random row from the table
        List<TableRow> tableRows = getValidTableRows(table, key, tumor, patient, schemaId);
        TableRow randomRow = tableRows.get(RandomUtils.nextInt(tableRows.size()));

        // get a random value or value range from the row using key
        List<? extends Range> stringRange = randomRow.getColumnInput(key);
        Range randomStringRange = stringRange.get(RandomUtils.nextInt(stringRange.size()));

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
     * @param tumor tumor
     * @param schemaId schema for this tumor
     * @return list of rows containing valid key values
     */
    private List<TableRow> getValidTableRows(Table table, String key, Tumor tumor, Patient patient, String schemaId) {
        List<TableRow> validRows = new ArrayList<>();

        // get the index number of the description column
        Integer descriptionColumnNumber = null;

        // find the description column number
        List<? extends ColumnDefinition> stagingColumnDefinitions = table.getColumnDefinitions();
        for (int i = 0; i < stagingColumnDefinitions.size(); i++)
            if (stagingColumnDefinitions.get(i).getType().toString().equals("DESCRIPTION"))
                descriptionColumnNumber = i;

        List<? extends TableRow> tableRows = table.getTableRows();
        List<List<String>> tableRawRows = table.getRawRows();
        int rowsInTable = tableRows.size();

        for (int i = 0; i < rowsInTable; i++) {

            TableRow row = tableRows.get(i);

            // check invalid values list - omit any input values that are marked invalid
            List<? extends Range> inputs = row.getColumnInput(key);
            boolean isInvalidValue = !inputs.isEmpty() && getInvalidValues(key, tumor, patient, schemaId).contains(inputs.get(0).getHigh());

            // check for error in endpoints - omit these from table
            List<? extends Endpoint> endpoints = row.getEndpoints();
            boolean isErrorEndPoint = false;
            if (endpoints != null && !endpoints.isEmpty())
                for (Endpoint endpoint : endpoints)
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
     * @param tumor tumor
     * @param schemaId schema for this tumor
     * @return list of all acceptable values for the key, or null if all acceptable values cannot be defined.
     */
    private List<String> getAllValidValues(String key, Tumor tumor, String schemaId) {
        List<String> validValues = null;
        switch (key) {
            case "ssf4":
            case "ssf5":
                if (schemaId.equals("breast") && !tumor.getItemValue("csLymphNodes").equals("000"))
                    validValues = new ArrayList<>(Collections.singletonList("987"));
                break;
            case "ssf13":
                if (schemaId.equals("breast") && tumor.getItemValue("csSiteSpecificFactor7").equals("998"))
                    validValues = Collections.singletonList("988");
                break;
            case "nodes_pos":
            case "nodes_exam":
                if (tumor.getItemValue("typeOfReportingSource").equals("7") || schemaId.equals("heme_retic") || schemaId.equals("lymphoma") || schemaId.equals("brain") || schemaId.equals("cns_other")
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
     * @param tumor tumor
     * @param schemaId schema for this tumor
     * @return list of invalid values for the key
     */
    private List<String> getInvalidValues(String key, Tumor tumor, Patient patient, String schemaId) {
        List<String> invalidValues = new ArrayList<>();

        switch (key) {
            case "size":
            case "nodes_eval":
            case "mets_eval":
                // for living patients (VS=1), remove eval values specifying evidence from autopsy
                if ("1".equals(patient.getItemValue("vitalStatus")))
                    invalidValues.add("8");
                break;
            case "extension_eval":
                // for living patients (VS=1), remove eval values specifying evidence from autopsy
                if ("1".equals(patient.getItemValue("vitalStatus")))
                    if (schemaId.equals("prostate"))
                        invalidValues.addAll(Arrays.asList("3", "8"));
                    else
                        invalidValues.addAll(Arrays.asList("2", "8"));
                break;
            case "ssf4":
            case "ssf5":
                if (schemaId.equals("breast") && tumor.getItemValue("csLymphNodes").equals("000"))
                    invalidValues.add("987");
                break;
            case "ssf13":
                if (schemaId.equals("breast") && !tumor.getItemValue("csSiteSpecificFactor12").equals("998"))
                    invalidValues.add("998");
                break;
            default:
        }
        return invalidValues;
    }
}
