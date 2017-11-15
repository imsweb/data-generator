# Data Generator

[![Build Status](https://travis-ci.org/imsweb/data-generator.svg?branch=master)](https://travis-ci.org/imsweb/data-generator)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.imsweb/data-generator/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.imsweb/data-generator)

This Java library can be used to create cancer-related synthetic data files.

## Download

The library is available on [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.imsweb%22%20AND%20a%3A%22data-generator%22).

To include it to your Maven or Gradle project, use the group ID `com.imsweb` and the artifact ID `data-generator`.

You can check out the [release page](https://github.com/imsweb/data-generator/releases) for a list of the releases and their changes.

This library requires Java 8 or a more recent version.

## Usage

There are two ways to use this library:

1. Embed it into your own Java software and call one of the data generators.
2. Download the executable JAR from the [release page](https://github.com/imsweb/data-generator/releases) and double click it to start the standalone GUI.

There are currently three types of generator that are provided:
 - RecordDataGenerator can bu used with generic fixed-columns layouts.
 - NaaccrDataGenerator can be used for NAACCR fixed-columns layouts.
 - NaaccrHl7DataGenerator can be used for NAACCR HL7 layouts.

Here is an example using the NAACCR generator:
```java
// create the generator
NaaccrDataGenerator generator = new NaaccrDataGenerator(LayoutFactory.LAYOUT_ID_NAACCR_16_ABSTRACT);

// generate a single patient with 2 tumors
// tumors are represented by individual maps, patients are represented as list of maps
List<Map<String, String>> patient = generator.generatePatient(2);

// generate a file with 500 patients, each one will have between 1 and 3 tumors
// with 1 being a much higher probability
generator.generateFile(targetFile, 500)
```

The NAACCR generator current provides rules for the following fields:
 - Patient ID Number
 - Sex
 - Race 1-5
 - Spanish/Hispanic Origin
 - Social Security Number
 - Name (Last, First, Middle, Prefix, Suffix and Maiden if needed)
 - Vital Status
 - Cause of death and ICD Revision Number
 - Date of Birth, Birthplace Country and State
 - Current address
 - Computed Ethnicity
 - IHS
 - Registry ID
 - Tumor Record Number
 - SEER Record Number
 - Sequence Number Central
 - Date of Diagnosis
 - Primary Site and related fields
 - Age at DX
 - Date of Initial RX
 - Date of Last Contact
 - Address at DX
 - Marital Status at DX
 - Diagnostic Confirmation
 - Type of Reporting Source
 - Census fields
 - RX Summary fields
 - SEER Type of Follow Up
 - Primary Payer at DX
 - Tumor Marker 1, 2 and 3
 - SEER Coding System
 - Multiple Tumors fields
 - Date Conclusive DX
 - Collaborative Stage fields
 - NHIA
 - NAPIIA

Here is an example using the NAACCR HL7 generator:
```java
// create the generator
NaaccrHl7DataGenerator generator = new NaaccrHl7DataGenerator(LayoutFactory.LAYOUT_ID_NAACCR_HL7_2_5_1);

// generate a single message
Hl7Message message = generator.generateMessage();

// generate a file with 10 messages
generator.generateFile(targetFile, 10)
```

The current NAACCR HL7 generator provides rules for the following segments:
 - Control Segment (MSH)
 - Patient Identifier Segment (PID)
 - Next of Kin Segment (NK1)
 - Patient Visit Segment (PV1)
 - Common Order Segment (ORC)
 - Observation Request Segment (OBR)
 - Observation/Result Segment (OBX)

Both NAACCR generators accept an additional options object as an input to the generate methods, that object can be used to customize the random data generation of some fields.

## Defining Variables

This library supports variables and file formats through the [layout framework](https://github.com/imsweb/layout). A layout object must be used
to initialize one of the data generator objects (although some of them supports providing just the layout ID).

## Creating Random Data

The library uses rules to create data; each rule being responsible for assigning one or several fields. The NAACCR generators come with a set of basic rules to assign some of the NAACCR fields;
the generic generator does't come with any rules.

The library uses three ways to assign values:

1. ***Constant values***: the rule always assigns the same value to the field.
2. ***Random values from a list***: the rule assigns a random value from a specific list of values.
3. ***Random values based on a frequency***: the rule uses a frequency (usually from a CSV file) to get the value to assign; this results in more common values being assigned more often.

In addition to those assignment mechanisms, each rule might have dependencies to the values assigned by previous rules.

The default NAACCR rules use frequencies extracted from the SEER data.

To know more about the default NAACCR rules, check out the [rule package](https://github.com/imsweb/data-generator/tree/master/src/main/java/com/imsweb/datagenerator/naaccr/rule).

## About SEER

This library was developed through the [SEER](http://seer.cancer.gov/) program.

The Surveillance, Epidemiology and End Results program is a premier source for cancer statistics in the United States.
The SEER program collects information on incidence, prevalence and survival from specific geographic areas representing
a large portion of the US population and reports on all these data plus cancer mortality data for the entire country.