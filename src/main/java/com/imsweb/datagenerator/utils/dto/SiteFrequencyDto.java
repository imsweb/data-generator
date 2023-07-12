/*
 * Copyright (C) 2017 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.utils.dto;

public class SiteFrequencyDto {

    private String _site;

    private String _histology;

    private String _behavior;

    private String _csSchemaId;

    private String _tnmSchemaId;

    private String _eodSchemaId;

    public String getSite() {
        return _site;
    }

    public void setSite(String site) {
        _site = site;
    }

    public String getHistology() {
        return _histology;
    }

    public void setHistology(String histology) {
        _histology = histology;
    }

    public String getBehavior() {
        return _behavior;
    }

    public void setBehavior(String behavior) {
        _behavior = behavior;
    }

    public String getCsSchemaId() {
        return _csSchemaId;
    }

    public void setCsSchemaId(String csSchemaId) {
        _csSchemaId = csSchemaId;
    }

    public String getTnmSchemaId() {
        return _tnmSchemaId;
    }

    public void setTnmSchemaId(String tnmSchemaId) {
        _tnmSchemaId = tnmSchemaId;
    }

    public String getEodSchemaId() {
        return _eodSchemaId;
    }

    public void setEodSchemaId(String eodSchemaId) {
        _eodSchemaId = eodSchemaId;
    }
}
