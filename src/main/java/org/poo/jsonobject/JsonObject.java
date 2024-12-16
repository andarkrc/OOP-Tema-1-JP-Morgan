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
     * Adds a new "field": String pair to the JSON object.
     * !!! No quotes are added to mark the string as string.
     * !!! To be used for custom implementations.
     *
     * @param field     name of the field
     * @param value     value of the field
     */
    public void addStringNoQuotes(final String field, final String value) {
        if (!data.isEmpty()) {
            this.data += ",\n";
        }
        this.data += "\"" + field + "\": " + value;
    }

    /**
     * Finds the value (String) that has been added to the field "field".
     * @param field     name of the field
     * @return          value (String with no quotes)
     */
    public String getStringOfField(final String field) {
        int startIdx = data.indexOf(field) + field.length() + 4;
        int endIdx = startIdx;
        while (data.charAt(endIdx) != '\n' && endIdx < data.length() - 1
                && data.charAt(endIdx) != ',') {
            endIdx++;
        }
        if (endIdx < data.length() - 1) {
            endIdx--;
        }
        return data.substring(startIdx, endIdx);
    }

    /**
     * Finds the value (Double) that has been added to the field "field".
     * @param field     name of the field
     * @return          value (Double)
     */
    public Double getDoubleOfField(final String field) {
        int startIdx = data.indexOf(field) + field.length() + 3;
        int endIdx = startIdx;
        while (data.charAt(endIdx) != '\n' && endIdx < data.length() - 1
                && data.charAt(endIdx) != ',') {
            endIdx++;
        }

        return Double.parseDouble(data.substring(startIdx, endIdx));
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
