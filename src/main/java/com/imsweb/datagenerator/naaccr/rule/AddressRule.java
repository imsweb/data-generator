package com.imsweb.datagenerator.naaccr.rule;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;
import com.imsweb.datagenerator.utils.DistributionUtils;
import com.imsweb.datagenerator.utils.RandomUtils;
import com.imsweb.datagenerator.utils.dto.CityFrequencyDto;

public abstract class AddressRule extends NaaccrDataGeneratorRule {

    // address field names
    protected final String _fieldState;
    protected final String _fieldCity;
    protected final String _fieldPostalCode;
    protected final String _fieldCountry;
    protected final String _fieldStreetName;
    protected final String _fieldSupp;
    protected final String _fieldCounty;

    // the state that was used to initialize this rule
    protected String _state;

    /**
     * Constructor.
     * @param id rule ID
     * @param name rule name
     * @param fieldState address state field name
     * @param fieldCity address city field name
     * @param fieldPostalCode address zip code field name
     * @param fieldCountry address country field name
     * @param fieldStreetName address street name field name
     * @param fieldSupp address supplemental field name
     */
    public AddressRule(String id, String name, String fieldState, String fieldCity, String fieldPostalCode, String fieldCountry, String fieldStreetName, String fieldSupp, String fieldCounty) {
        super(id, name);

        _fieldState = fieldState;
        _fieldCity = fieldCity;
        _fieldPostalCode = fieldPostalCode;
        _fieldCountry = fieldCountry;
        _fieldStreetName = fieldStreetName;
        _fieldSupp = fieldSupp;
        _fieldCounty = fieldCounty;
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        if (options == null || options.getState() == null)
            return;

        // get random zip code and with it city and state
        CityFrequencyDto dto = DistributionUtils.getCity(options.getState());
        record.put(_fieldCity, dto.getCity());
        record.put(_fieldPostalCode, dto.getZip());
        record.put(_fieldState, dto.getState());
        record.put(_fieldCounty, "999");
        record.put(_fieldCountry, "USA");

        // put random street name with house number (1 - 9999)
        String name = DistributionUtils.getStreetName();
        String suffix = DistributionUtils.getStreetSuffix();
        record.put(_fieldStreetName, Integer.toString(RandomUtils.nextInt(9998) + 1) + " " + name + " " + suffix);
    }
}
