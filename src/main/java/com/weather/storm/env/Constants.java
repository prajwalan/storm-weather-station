package com.weather.storm.env;

public class Constants {

    public enum MEASUREMENT_ENTITY {
        TEMPERATURE(1), PRECIPITATION(2);

        public final int value;

        private MEASUREMENT_ENTITY(int value) {
            this.value = value;
        }

        public boolean compare(int value) {
            return this.value == value;
        }

        public static MEASUREMENT_ENTITY fromInt(Integer id) {
            if (id != null) {
                MEASUREMENT_ENTITY[] values = MEASUREMENT_ENTITY.values();
                for (int i = 0; i < values.length; i++) {
                    if (values[i].compare(id.intValue())) {
                        return values[i];
                    }
                }
            }
            return null;
        }
    }
}
