package com.weather.storm.util;

import java.math.BigDecimal;

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

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    public static float getRunningMean(float oldMean, float newValue, long count) {
        return count == 0 ? 0.0f : (float) round(((1.0f * oldMean) * (count - 1)) / count + (newValue / count), 2);
    }

}
