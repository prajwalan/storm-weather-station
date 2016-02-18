package com.weather.storm.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CommonUtil {

    public static Gson createJsonConvertor(boolean serializeNulls) {
        GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().disableHtmlEscaping();

        if (serializeNulls) {
            builder.serializeNulls();
        }

        return builder.create();
    }

    public static Gson createJsonConvertor() {
        return createJsonConvertor(false);
    }
}
