package org.poo.jsonobject;

public final class JsonObject {
    private String data;


    public JsonObject() {
        this.data = "";
    }

    /**
     * Adds a new "field": "string" pair to the JSON object.
     *
     * @param field     name of the field
     * @param value     value of the field
     */
    public void add(final String field, final String value) {
        addString(field, value);
    }

    /**
     * Adds a new "field": integer pair to the JSON object.
     *
     * @param field     name of the field
     * @param value     value of the field
     */
    public void add(final String field, final int value) {
        addInt(field, value);
    }

    /**
     * Adds a new "field": double pair to the JSON object.
     *
     * @param field     name of the field
     * @param value     value of the field
     */
    public void add(final String field, final double value) {
        addDouble(field, value);
    }
    /**
     * Adds a new "field": {JsonObject} pair to the JSON object.
     *
     * @param field     name of the field
     * @param value     value of the field
     */
    public void add(final String field, final JsonObject value) {
        addJsonObject(field, value);
    }

    /**
     * Adds a new "field": [JsonArray] pair to the JSON object.
     *
     * @param field     name of the field
     * @param value     value of the field
     */
    public void add(final String field, final JsonArray value) {
        addJsonArray(field, value);
    }

    /**
     * Adds a new "field": "string" pair to the JSON object.
     *
     * @param field     name of the field
     * @param value     value of the field
     */
    public void addString(final String field, final String value) {
        if (!data.isEmpty()) {
            this.data += ",\n";
        }
        this.data += "\"" + field + "\": \"" + value + "\"";
    }

    /**
     * Adds a new "field": integer pair to the JSON object.
     *
     * @param field     name of the field
     * @param value     value of the field
     */
    public void addInt(final String field, final int value) {
        if (!data.isEmpty()) {
            this.data += ",\n";
        }
        this.data += "\"" + field + "\": " + value;
    }

    /**
     * Adds a new "field": double pair to the JSON object.
     *
     * @param field     name of the field
     * @param value     value of the field
     */
    public void addDouble(final String field, final double value) {
        if (!data.isEmpty()) {
            this.data += ",\n";
        }
        this.data += "\"" + field + "\": " + value;
    }

    /**
     * Adds a new "field": [JsonArray] pair to the JSON object.
     *
     * @param field     name of the field
     * @param value     value of the field
     */
    public void addJsonArray(final String field, final JsonArray value) {
        if (!data.isEmpty()) {
            this.data += ",\n";
        }
        this.data += "\"" + field + "\": " + value.finalizeData();
    }

    /**
     * Adds a new "field": {JsonObject} pair to the JSON object.
     *
     * @param field     name of the field
     * @param value     value of the field
     */
    public void addJsonObject(final String field, final JsonObject value) {
        if (!data.isEmpty()) {
            this.data += ",\n";
        }
        this.data += "\"" + field + "\": " + value.finalizeData();
    }

    /**
     * Returns the data stored as a JSON Object.
     *
     * @return      a string representing the data stored in the JSON Object
     */
    public String finalizeData() {
        return "{\n" + data + "\n}";
    }
}
