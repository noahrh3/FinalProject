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
            if (json.equals("SERVERS_DOWN")) {
                return "Servers may be down my dude.";
            }
            if (json.equals("FILE_NOT_FOUND")) {
                return "We couldn't find that file my dude.";
            }
            if (json.equals("NO_INTERNET")) {
                return "Make sure you have internet connection my dude.";
            }
            JSONObject result = new JSONObject(json);
            try {
                Double confidence = (Double) result.get("latex_confidence");
                if (confidence < 0.5) {
                    return "Cannot extract math. Try cropping better my dude.";
                }
            } catch (Exception e) {
                return "Cannot extract math. Try cropping better my dude.";
            }
            return result.get("latex").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Please select a picture my dude";
        }
    }
}
