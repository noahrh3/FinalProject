package com.example.finalproject;

import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;
import org.json.JSONException;

public class JsonParser {
    public static String getLatex(final String json) {
        if (json == null) {
            return null;
        }
        try {
            JSONObject result = new JSONObject(json);
            return result.get("latex").toString();
        } catch (JSONException e) {
            return null;
        }
    }
}
