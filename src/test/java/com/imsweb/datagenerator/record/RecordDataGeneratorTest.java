package com.imsweb.datagenerator.record;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import testing.TestingUtils;

import com.imsweb.layout.record.fixed.FixedColumnsField;
import com.imsweb.layout.record.fixed.FixedColumnsLayout;

public class RecordDataGeneratorTest {

    @Test
    public void testGenerator() {
        RecordDataGenerator generator = createGenerator();

        // 1 rule
        Assert.assertEquals(1, generator.getRules().size());
        Assert.assertNotNull(generator.getRules().get(0).getId());
        Assert.assertNotNull(generator.getRules().get(0).getName());
        Assert.assertEquals("", generator.getRules().get(0), generator.getRules().get(0));
        Assert.assertEquals(generator.getRules().get(0).hashCode(), generator.getRules().get(0).hashCode());

        // no rules
        generator.getRules().clear();
        Assert.assertEquals(0, generator.getRules().size());

        // a layout is required
        try {
            //noinspection ConstantConditions
            new RecordDataGenerator(null);
            Assert.fail();
        }
        catch (RuntimeException e) {
            // expected
        }
    }

    @Test
    public void testGenerateRecord() {
        RecordDataGenerator generator = createGenerator();

        // null options
        Map<String, String> rec = generator.generateRecord(null);
        Assert.assertEquals("x", rec.get("field1"));

        // empty options
        Map<String, Object> options = new HashMap<>();
        rec = generator.generateRecord(options);
        Assert.assertEquals("x", rec.get("field1"));

        // option with a key that doesn't apply
        options.put("whatever", "whatever");
        rec = generator.generateRecord(options);
        Assert.assertEquals("x", rec.get("field1"));

        // option that does apply
        options.put("uppercase", Boolean.TRUE);
        rec = generator.generateRecord(options);
        Assert.assertEquals("X", rec.get("field1"));

        // remove the rule from the generator
        generator.getRules().clear();
        rec = generator.generateRecord(options);
        Assert.assertNull(rec.get("field1"));
    }

    @Test
    public void testGenerateFile() throws IOException {
        RecordDataGenerator generator = createGenerator();

        // regular file, 1 record
        File file = TestingUtils.createFile("data-generator-1-rec.txt");
        generator.generateFile(file, 1, null);
        List<String> lines = TestingUtils.readFile(file);
        Assert.assertEquals(1, lines.size());
        Assert.assertEquals("x---------", lines.get(0));

        // regular file, 10 records
        file = TestingUtils.createFile("data-generator-10-rec.txt");
        generator.generateFile(file, 10, null);
        lines = TestingUtils.readFile(file);
        Assert.assertEquals(10, lines.size());
        Assert.assertEquals("x---------", lines.get(0));
        Assert.assertEquals("x---------", lines.get(9));

        // compressed file, 1 record
        file = TestingUtils.createFile("data-generator-10-rec.txt.gz");
        generator.generateFile(file, 1, null);
        lines = TestingUtils.readFile(file);
        Assert.assertEquals(1, lines.size());
        Assert.assertEquals("x---------", lines.get(0));

        // compressed file, 10 records
        file = TestingUtils.createFile("data-generator-10-rec.txt.gz");
        generator.generateFile(file, 10, null);
        lines = TestingUtils.readFile(file);
        Assert.assertEquals(10, lines.size());
        Assert.assertEquals("x---------", lines.get(0));
        Assert.assertEquals("x---------", lines.get(9));

        // use an option
        Map<String, Object> options = new HashMap<>();
        options.put("uppercase", Boolean.TRUE);
        generator.generateFile(file, 1, options);
        lines = TestingUtils.readFile(file);
        Assert.assertEquals(1, lines.size());
        Assert.assertEquals("X---------", lines.get(0));

        // bad number of records
        try {
            generator.generateFile(file, 0, null);
            Assert.fail();
        }
        catch (RuntimeException e) {
            // expected
        }
    }

    private RecordDataGenerator createGenerator() {

        // first, let's create a fake layout
        FixedColumnsLayout layout = new FixedColumnsLayout();
        layout.setLayoutId("test");
        layout.setLayoutName("Test");
        layout.setLayoutLineLength(10);
        FixedColumnsField f1 = new FixedColumnsField();
        f1.setName("field1");
        f1.setStart(1);
        f1.setEnd(10);
        f1.setPadChar("-");
        layout.setFields(Collections.singleton(f1));

        // then lets create the generator
        RecordDataGenerator generator = new RecordDataGenerator(layout);

        // and finally, let's add a rule for the unique field
        generator.addRule(new RecordDataGeneratorRule("field1-rule", "Field 1") {
            @Override
            public void execute(Map<String, String> rec, Map<String, Object> options) {
                if (options != null && Boolean.TRUE.equals(options.get("uppercase")))
                    rec.put("field1", "X");
                else
                    rec.put("field1", "x");

            }
        });

        return generator;
    }
}
