package com.imsweb.datagenerator.naaccr.rule;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;
import com.imsweb.datagenerator.random.DistributedRandomValueGenerator;
import com.imsweb.datagenerator.random.RandomUtils;
import com.imsweb.datagenerator.random.UniformRandomValueGenerator;

public abstract class AddressRule extends NaaccrDataGeneratorRule {

    // file to street name lists
    protected static final String _FILE_STREET_NAMES = "lists/street_names.csv";
    protected static final String _FILE_STREET_SUFFIXES = "lists/street_suffixes.csv";
    protected static final String _PATH_LOCATIONS = "frequencies/zip_codes/";

    // random value generators
    protected static final UniformRandomValueGenerator _VALUES_STREET_SUFFIX = new UniformRandomValueGenerator(_FILE_STREET_SUFFIXES);
    protected static final UniformRandomValueGenerator _VALUES_STREET_NAME = new UniformRandomValueGenerator(_FILE_STREET_NAMES);

    // address field names
    protected final String _fieldState;
    protected final String _fieldCity;
    protected final String _fieldPostalCode;
    protected final String _fieldCountry;
    protected final String _fieldStreetName;
    protected final String _fieldSupplementl;
    protected final String _fieldCounty;

    // the state that was used to initialize this rule
    protected String _state;

    // random city/state/zip generator
    protected DistributedRandomValueGenerator _valuesLocationReg = null;

    /**
     * Constructor.
     * @param id rule ID
     * @param name rule name
     * @param fieldState address state field name
     * @param fieldCity address city field name
     * @param fieldPostalCode address zip code field name
     * @param fieldCountry address country field name
     * @param fieldStreetName address street name field name
     * @param fieldSupplementl address supplemental field name
     */
    public AddressRule(String id, String name, String fieldState, String fieldCity, String fieldPostalCode, String fieldCountry, String fieldStreetName, String fieldSupplementl, String fieldCounty) {
        super(id, name);

        _fieldState = fieldState;
        _fieldCity = fieldCity;
        _fieldPostalCode = fieldPostalCode;
        _fieldCountry = fieldCountry;
        _fieldStreetName = fieldStreetName;
        _fieldSupplementl = fieldSupplementl;
        _fieldCounty = fieldCounty;
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {
        // create the state-dependent random value generator
        loadStateBasedLocations(options);
        if (_valuesLocationReg == null)
            return;

        // get random zip code and with it city and state
        List<String> cityStateZip = _valuesLocationReg.getRandomValueList();
        record.put(_fieldPostalCode, cityStateZip.get(0));
        record.put(_fieldCity, cityStateZip.get(1));
        record.put(_fieldState, cityStateZip.get(2));
        record.put(_fieldCountry, "USA");
        record.put(_fieldCounty, "999");

        // put random street name with house number (1 - 9999)
        record.put(_fieldStreetName, Integer.toString(RandomUtils.nextInt(9998) + 1) + " " + _VALUES_STREET_NAME.getRandomValue() + " " + _VALUES_STREET_SUFFIX.getRandomValue());
    }

    /**
     * Constructs the random value generator for location. This depends on the Registry state. Only locations in that state will be loaded into the generator.
     * @param options Data generator options containing registry
     */
    protected void loadStateBasedLocations(NaaccrDataGeneratorOptions options) {
        if (options != null && (_state == null || !_state.equals(options.getState()))) {
            _state = options.getState();
            if (_state != null)
                _valuesLocationReg = new DistributedRandomValueGenerator(_PATH_LOCATIONS + options.getState().toLowerCase() + ".csv");
        }
    }
}
