The staging data contains the "valid" codes for each schema and input field combination.

As an optimization, every schema ID and field ID is keyed and replaced by a short digit code;
those codes can be found in the "Staging_keys.csv" file.

The schemas to use for each site/histology is contained in teh "sites_sex_[female|male].csv" files.
Here are the columns contained in those files:
 - probability for the row
 - primary Site
 - histology ICD-O-3
 - behavior ICD-O-3
 - CS schema ID
 - TNM schema ID
 - EOD schema ID

The three "staging_[cs|tnm|eod].csv" files contain the possible values for each input of each schema.
Here are the columns contain in those files:
 - schema ID
 - field ID (NAACCR XML ID)
 - possible values

The values can contain ranges; not all values are digits.

The data doesn't contain frequencies for each value (that would be too much data); the framework assigns
a random value among the available ones.