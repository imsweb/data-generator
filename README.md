# Data Generator

[![Build Status](https://travis-ci.org/imsweb/data-generator.svg?branch=master)](https://travis-ci.org/imsweb/data-generator)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.imsweb/data-generator/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.imsweb/data-generator)

This Java library can be used to create cancer-related synthetic data files.

## Download

The library will soon be available on [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.imsweb%22%20AND%20a%3A%22data-generator%22).

To include it to your Maven or Gradle project, use the group ID `com.imsweb` and the artifact ID `data-generator`.

You can check out the [release page](https://github.com/imsweb/data-generator/releases) for a list of the releases and their changes.

This library requires Java 7 or a more recent version.

## Usage

There are two ways to use this library:

1. Embed it into your own Java software.
2. Download the executable JAR and double click it to start the standalone GUI.

## Creating Random Data

The library is composed of rules, each rule being responsible for assigning one or several fields. It comes with a set of rules (very basic at this point) to assign some of the NAACCR fields;
but if embedded, it can be setup to create any fixed-column or comma-separated file format.

The library uses three ways to assign values:
1. ***Constant values***: the rule always assigns the same value to the field.
2. ***Random values from a list***: the rule assigns a random value from a specific list of values.
3. ***Random values based on a frequency***: the rule uses a frequency (usually from a CSV file) to get the value to assign; this results in more common values being assigned more often.

In addition to those assignment mechanisms, each rule might have dependencies to the values assigned by previous rules.

The default rules use frequencies extracted from the SEER data.

## About SEER

This library was developed through the [SEER](http://seer.cancer.gov/) program.

The Surveillance, Epidemiology and End Results program is a premier source for cancer statistics in the United States.
The SEER program collects information on incidence, prevalence and survival from specific geographic areas representing
a large portion of the US population and reports on all these data plus cancer mortality data for the entire country.