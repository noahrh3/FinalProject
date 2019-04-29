package com.example.finalproject;

import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;
import org.json.JSONException;

/**
 * Class to parse json string and return latex formatted string.
 */
class JsonParser {
    static String getLatex(final String json) {
        if (json == null) {
            throw new IllegalArgumentException("Json string is null");
        }
        try {
            JSONObject result = new JSONObject(json);
            try {
                Double confidence = (Double) result.get("latex_confidence");
                if (confidence < 0.5) {
                    return "Cannot extract math. Try cropping better.";
                }
            } catch (Exception e) {
                return "Cannot extract math. Try cropping better.";
            }
            return result.get("latex").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Image size may be too big, try cropping more.";
        }
    }
}
