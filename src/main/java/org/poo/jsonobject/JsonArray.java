package org.poo.jsonobject;

public final class JsonArray {
    private String data;

    public JsonArray() {
        data = "";
    }

    /**
     * Adds a new string to the JSON Array.
     *
     * @param value     the new string added
     */
    public void add(final String value) {
        addString(value);
    }

    /**
     * Adds a new JSON Object to the JSON Array.
     *
     * @param value     the new JsonObject added
     */
    public void add(final JsonObject value) {
        addJsonObject(value);
    }

    /**
     * Adds a new JSON Array to the JSON Array.
     *
     * @param value     the new JsonArray added
     */
    public void add(final JsonArray value) {
        addJsonArray(value);
    }

    /**
     * Adds a new double to the JSON Array.
     *
     * @param value     the new double added
     */
    public void add(final double value) {
        addDouble(value);
    }

    /**
     * Adds a new string to the JSON Array.
     *
     * @param value     the new string added
     */
    public void addString(final String value) {
        if (!data.isEmpty()) {
            data += ",\n";
        }
        data += "\"" + value + "\"";
    }

    /**
     * Adds a new double to the JSON Array.
     *
     * @param value     the new double added
     */
    public void addDouble(final double value) {
        if (!data.isEmpty()) {
            data += ",\n";
        }
        data += Double.toString(value);
    }

    /**
     * Adds a new JSON Object to the JSON Array.
     *
     * @param value     the new JsonObject added
     */
    public void addJsonObject(final JsonObject value) {
        if (!data.isEmpty()) {
            data += ",\n";
        }
        data += value.finalizeData();
    }

    /**
     * Adds a new JSON Array to the JSON Array.
     *
     * @param value     the new JsonArray added
     */
    public void addJsonArray(final JsonArray value) {
        if (!data.isEmpty()) {
            data += ",\n";
        }
        data += value.finalizeData();
    }

    /**
     * Adds a new String to the JSON Array.
     * !!! No quotes are added to mark the string as string.
     * !!! To be used for custom implementations.
     *
     * @param value     the new JsonArray added
     */
    public void addStringNoQuotes(final String value) {
        if (!data.isEmpty()) {
            data += ",\n";
        }
        data += value;
    }

    /**
     * Returns the data stored as a JSON Array.
     *
     * @return      a string representing the data stored in the JSON Array
     */
    public String finalizeData() {
        return "[\n" + data + "\n]";
    }
}
