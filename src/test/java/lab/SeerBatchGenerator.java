package lab;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrFixedColumnsDataGenerator;
import com.imsweb.layout.LayoutFactory;
import com.imsweb.layout.record.fixed.naaccr.NaaccrLayout;

@SuppressWarnings("ConstantConditions")
public class SeerBatchGenerator {

    @SuppressWarnings("unused")
    enum SeerRegistry {

        CN("0000001551", "OK"),
        AK("0000001529", "AK"),
        AZ("0000001533", "AZ"),
        CA("0000001541", "CA"),
        CT("0000001502", "CT"),
        HI("0000001521", "HI"),
        IA("0000001522", "IA"),
        KY("0000001542", "KY"),
        LAX("0000001535", "CA"),
        LA("0000001543", "LA"),
        AT("0000001527", "GA"),
        DT("0000001520", "MI"),
        NJ("0000001544", "NJ"),
        NM("0000001523", "NM"),
        GA("0000001547", "GA"),
        RG("0000001537", "GA"),
        SF("0000001501", "CA"),
        SJ("0000001531", "CA"),
        SE("0000001525", "WA"),
        UT("0000001526", "UT"),
        NY("0000001100", "NY"),
        ZZ("0000000000", "MD");

        private final String _id;
        private final String _state;

        SeerRegistry(String id, String state) {
            _id = id;
            _state = state;
        }

        public String getId() {
            return _id;
        }

        public String getState() {
            return _state;
        }
    }

    private static final int _NUM_RECORDS = 10;
    private static final boolean _ALL_REGISTRIES = false;
    private static final SeerRegistry _SINGLE_REGISTRY = SeerRegistry.CT;    // ignored if _ALL_REGISTRIES set to true
    private static final int _NUM_FILES_PER_REG = 1;
    private static final boolean _ZIP_FILE = true;
    private static final boolean _APPEND_SIZE = true;
    private static final NaaccrLayout _LAYOUT = LayoutFactory.getNaaccrFixedColumnsLayout(LayoutFactory.LAYOUT_ID_NAACCR_15_ABSTRACT);
    private static final String _FILE_DESTINATION = "./build/";
    private static final String _FILE_NAME_TEMPLATE = "_generated_data";
    private static final String _FILE_NAME_EXTENSION = _ZIP_FILE ? ".txt.gz" : ".txt";

    public static void main(String[] args) throws IOException {
        if (_ALL_REGISTRIES)
            for (SeerRegistry registry : SeerRegistry.values())
                for (int count = 1; count <= _NUM_FILES_PER_REG; count++)
                    generateSingleRegistry(registry, count);
        else
            for (int count = 1; count <= _NUM_FILES_PER_REG; count++)
                generateSingleRegistry(_SINGLE_REGISTRY, count);
    }

    public static void generateSingleRegistry(SeerRegistry registry, int count) throws IOException {
        long t = System.currentTimeMillis();

        String filename = _FILE_DESTINATION + registry.toString().toLowerCase() + _FILE_NAME_TEMPLATE;
        filename += _APPEND_SIZE ? "-" + _NUM_RECORDS : "";
        filename += _NUM_FILES_PER_REG > 1 ? "-" + count : "";
        filename += _FILE_NAME_EXTENSION;
        File file = new File(filename);

        // set some constant values
        Map<String, String> constantValues = new HashMap<>();
        constantValues.put("registryId", registry.getId());

        // create the options
        NaaccrDataGeneratorOptions options = new NaaccrDataGeneratorOptions();
        options.setState(registry.getState());
        options.setConstantPatientValuesPostProcessing(constantValues);

        // create the generator and generate the file
        NaaccrFixedColumnsDataGenerator generator = new NaaccrFixedColumnsDataGenerator(_LAYOUT);
        generator.generateFile(file, _NUM_RECORDS, options);
        System.out.println("Generated " + _NUM_RECORDS + " " + _LAYOUT.getLayoutName() + " records for registry " + registry + " in " + (System.currentTimeMillis() - t) / 1000.0 + " seconds.");
    }
}
