## Synthetic data generator

**Changes in version 1.33**

- Updated Layout library from version 5.5 to version 5.7.
- Updated NAACCR XML library from version 10.1 to version 11.1.

**Changes in version 1.32**

- Added new rule to populate Telephone (issue #51).
- Updated Layout library from version 5.3 to version 5.5.
- Updated NAACCR XML library from version 10.0 to version 10.1. 

**Changes in version 1.31**

- Stopped removing duplicates when creating facilities and physicians.
- Improved assignment of laterality in NAACCR rules.

**Changes in version 1.30**

- Re-added physician and facilities fields and classes that were actually used by other software.

**Changes in version 1.29**

- Added randomly generated text for text fields in NAACCR generator.
- Updated NAACCR XML library from version 8.10 to version 10.0.
- Updated Layout library from version 4.9 to version 5.3.

**Changes in version 1.28**

- Updated NAACCR XML library from version 8.9 to version 8.10.
- Updated Layout library from version 4.8 to version 4.9.

**Changes in version 1.27**

- Fixed StagingInput rule that assigned variable sex by mistake.

**Changes in version 1.26**

- Removed NHIA and NAPIIA rules to remove dependency to Algorithms library. 
- Replaced CS rule by new Staging Input rule covering CS, TNM and EOD inputs.
- Updated NAACCR XML library from version 8.3 to version 8.9.
- Updated Layout library from version 4.6 to version 4.8.

**Changes in version 1.25**

- Updated NAACCR XML library from version 8.2 to version 8.3.
- Updated Layout library from version 3.5 to version 4.6.

**Changes in version 1.24**

- Updated NAACCR XML library from version 7.13 to version 8.2.
- Updated Layout library from version 3.4 to version 4.5.
- Updated Algorithms library from version 3.4 to version 3.5.
- Updated Commons Lang library from version 3.11 to version 3.12.0.
- Updated Commons IO library from version 2.7 to version 2.11.0.

**Changes in version 1.23**

- Updated Layout library from version 3.3 to version 3.4.
- Updated Algorithms library from version 3.3 to version 3.4.
- Updated NAACCR XML library from version 7.12 to version 7.13.

**Changes in version 1.22**

- Updated Staging client library from version 6.0.0 to version 6.1.0.
- Updated CS library from version 02.05.50.7 to version 02.05.50.8.

**Changes in version 1.21**

- Updated Staging client library from version 5.0 to version 6.0.0.
- Updated CS library from version 02.05.50.4 to version 02.05.50.7.
- Updated Layout library from version 3.2 to version 3.3.
- Updated Algorithms library from version 3.0 to version 3.3.
- Updated NAACCR XML library from version 7.7 to version 7.12.

**Changes in version 1.20**

- Fixed NAACCR XML generator that was writing default base dictionaries in created XML data files.

**Changes in version 1.19**

- Fixed HL7 generator, SMP segment was not generated in the proper order.

**Changes in version 1.18**

- Fixed NAACCR XML generator not providing the user-defined dictionaries to the XML writer.
- Changed name rule so it assigns field 'nameBirthSurname' instead of 'nameMaiden' for NAACCR version 210 and later.
- Added SPM segment generation to the HL7 data generator.
- Added NAACCR XML library dependency version 7.7.
- Updated Layout library from version 2.0 to version 3.2.
- Updated Algorithms library from version 2.14 to version 3.0.
- Updated Staging client library from version 4.8 to version 5.0.
- Updated Commons Lang library from version 3.10 to version 3.11.

**Changes in version 1.17**

- Fixed NAACCR XML generator so it returns Patient objects and not list of records.

**Changes in version 1.16**

- Added support for creating NAACCR 21 XML data.

**Changes in version 1.15**

- Updated Layout library from version 2.0 to version 3.0.
- Updated Algorithms library from version 2.13 to version 2.14.
- Updated Staging client library from version 4.7 to version 4.8.
- Updated CS library from version 02.05.50.3 to version 02.05.50.4.
- Updated Commons Lang library from version 3.9 to version 3.10.
- Updated Commons IO library from version 2.6 to version 2.7.

**Changes in version 1.14**

- Aligned NAACCR fixed-column layout field names on NAACCR XML IDs.
- Updated Algorithms library from version 2.12 to version 2.13.
- Updated Layout library from version 1.25 to version 2.0.

**Changes in version 1.13**

- Updated Algorithms library from version 2.6 to version 2.12.
- Updated Layout library from version 1.21 to version 1.25.
- Updated Commons Lang library from version 3.7 to version 3.9.

**Changes in version 1.12**

- Updated Algorithms library from version 1.21 to version 2.6.
- Updated Layout library from version 1.19 to version 1.21.
- Updated Staging client library from version 4.1 to version 4.7.
- Updated CS library from version 02.05.50.1 to version 02.05.50.3.
- Removed GUI components (it was already not distributed anymore).

**Changes in version 1.11**

- Updated Layout library from version 1.18 to version 1.19.

**Changes in version 1.10**

- Now removing invalid fields when creating NAACCR patients.
- Updated Layout library from version 1.12 to version 1.18.
- Updated Algorithm library from version 1.16 to version 1.21.
- Updated CS library from version 02.05.50 to version 02.05.50.1.

**Changes in version 1.9**

- Added an option to the standalone GUI to generate NAACCR Number attribute for XML files.
- Updated Layout library from version 1.11 to version 1.12.
- Updated Algorithm library from version 1.14 to version 1.16.

**Changes in version 1.8**

- Fixed an exception when generating NAACCR XML Incidence files.
- Updated Staging client library from version 4.0 to version 4.1.
- Updated Layout library from version 1.10 to version 1.11.

**Changes in version 1.7**

- Added options for generating NAACCR XML files in the standalone GUI.
- Added new NAACCR XML generator.
- Fixed formatting issue in Collaborative Stage rule.
- Removed support for NAACCR 15 in the standalone GUI.

**Changes in version 1.6**

- Added support for NAACCR 18 format.
- Removed vital status option from the standalone GUI.
- Changed Vital Status rule to use 0 by default for vital status instead of 4.

**Changes in version 1.5**

- Updated Layout library from version 1.8 to version 1.10.
- Updated Staging library from version 2.14 to version 4.0.
- Updated Algorithm library from version 1.12 to version 1.14.

**Changes in version 1.4**

- Updated Layout library from version 1.6 to version 1.8.
- Updated Staging library from version 2.12 to version 2.14.
- Updated Algorithm library from version 1.10 to version 1.12.
- Added Patient context and Site frequency so that patient's Age and Age At Diagnosis is more appropriate for their tumors.
- Added a Facility and Physician generator. These providers will be used for specific NAACCR fields.

**Changes in version 1.3**

- Updated Staging library from version 2.10 to version 2.12. 

**Changes in version 1.2**

- Updated Algorithm library from version 1.9.1 to version 1.10.
- Updated Staging library from version 2.9 to version 2.10.

**Changes in version 1.1**

- Updated Layout library from version 1.5.1 to version 1.6.
- Updated Algorithm library from version 1.7 to version 1.9.1.
- Updated Staging library from version 2.5 to version 2.9 (includes TNM 1.5).

**Changes in version 1.0**

- Added support for generating NAACCR HL7 messages.
- Added new parameter for Registry ID.
- Fixed bug where DX date and/or DOLC could be set to a future date.
- Updated Layout library from version 1.4 to version 1.5.1.
- Updated Algorithm library from version 1.4.4 to version 1.7.
- Updated Staging library from version 2.3 to version 2.5.

**Changes in version 0.3**

- Removed Class-Path attribute from generated JAR.
- Updated Layout library from version 1.3.3 to version 1.4.
- Updated Algorithm library from version 1.4 to version 1.4.4.
- Updated Staging library from version 2.0 to version 2.3.
- Updated Commons IO library from version 2.4 to version 2.5.
- This library now requires Java 8 at minimum.

**Changes in version 0.2**

- Improved parameters validation.
- Fixed randomized birth state assignment.
- Fixed bug in all date assignments were a future date could be assigned.

**Changes in version 0.1**

- Initial release.
