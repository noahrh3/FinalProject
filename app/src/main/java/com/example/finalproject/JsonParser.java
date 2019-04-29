package com.example.finalproject;

import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;
import org.json.JSONException;

public class JsonParser {
    public static String getLatex(final String json) {
        if (json == null) {
            throw new IllegalArgumentException("Json string is null");
        }
        try {
            JSONObject result = new JSONObject(json);
            double confidence = (Double) result.get("latex_confidence");
            if (confidence < .5) {
                return "Image is unclear. Cannot detect math symbols.";
            }
            return result.get("latex").toString();
        } catch (JSONException e) {
            return null;
        }
    }
}
