package com.albina.springproject.filter;

public class SearchCriteria {

    private String key;
    private SearchOperation operation;
    private Object value;
    private String join;

    public SearchCriteria(String key, SearchOperation operation, Object value, String join) {
        this.key = key;
        this.operation = operation;
        this.value = value;
        this.join = join;
    }

    public SearchCriteria(String key, SearchOperation operation, Object value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public SearchCriteria(String key, Object value) {
        this.key = key;
        this.value = value;
        this.operation = SearchOperation.EQUALS;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public SearchOperation getOperation() {
        return operation;
    }

    public void setOperation(SearchOperation operation) {
        this.operation = operation;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getJoin() {
        return join;
    }

    public void setJoin(String join) {
        this.join = join;
    }
}
