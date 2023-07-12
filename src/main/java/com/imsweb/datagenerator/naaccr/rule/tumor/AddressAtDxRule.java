package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorTumorRule;
import com.imsweb.datagenerator.utils.DistributionUtils;
import com.imsweb.datagenerator.utils.RandomUtils;
import com.imsweb.datagenerator.utils.dto.CityFrequencyDto;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

public class AddressAtDxRule extends NaaccrDataGeneratorTumorRule {

    // unique identifier for this rule
    public static final String ID = "address-dx";

    /**
     * Constructor.
     */
    public AddressAtDxRule() {
        super(ID, "Address at DX");
    }

    @Override
    public void execute(Tumor tumor, Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        if (options == null || options.getState() == null)
            return;

        // if this is not the first tumor, there is an 80% chance to keep previous address at DX
        if (patient != null && !patient.getTumors().isEmpty() && RandomUtils.nextInt(10) < 8) {
            Tumor otherTumor = patient.getTumor(patient.getTumors().size() - 1);
            setValue(tumor, "addrAtDxCity", otherTumor.getItemValue("addrAtDxCity"));
            setValue(tumor, "addrAtDxPostalCode", otherTumor.getItemValue("addrAtDxPostalCode"));
            setValue(tumor, "addrAtDxState", otherTumor.getItemValue("addrAtDxState"));
            setValue(tumor, "countyAtDx", otherTumor.getItemValue("countyAtDx"));
            setValue(tumor, "addrAtDxCountry", otherTumor.getItemValue("addrAtDxCountry"));
            setValue(tumor, "addrAtDxNoStreet", otherTumor.getItemValue("addrAtDxNoStreet"));
        }
        else {
            // get random zip code and with it city and state
            CityFrequencyDto dto = DistributionUtils.getCity(options.getState());
            setValue(tumor, "addrAtDxCity", dto.getCity());
            setValue(tumor, "addrAtDxPostalCode", dto.getZip());
            setValue(tumor, "addrAtDxState", dto.getState());
            setValue(tumor, "countyAtDx", "999");
            setValue(tumor, "addrAtDxCountry", "USA");

            // put random street name with house number (1 - 9999)
            String name = DistributionUtils.getStreetName();
            String suffix = DistributionUtils.getStreetSuffix();
            setValue(tumor, "addrAtDxNoStreet", (RandomUtils.nextInt(9998) + 1) + " " + name + " " + suffix);
        }
    }
}
