package com.github.rickyortega.fieldsearch.dto;

import java.util.List;
import java.util.Map;

public class FieldSearchRequest {
    private List<String> fields;
    private Map<String, Object> filters;

    public FieldSearchRequest() {}

    public FieldSearchRequest(
            List<String> fields,
            Map<String, Object> filters
    ) {
        this.fields = fields;
        this.filters = filters;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(
            List<String> fields
    ) {
        this.fields = fields;
    }

    public Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(
            Map<String, Object> filters
    ) {
        this.filters = filters;
    }
}
