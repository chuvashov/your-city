package com.yourcity.util;

/**
 * Created by Andrey on 28.03.2015.
 */
public class MuseumConversionFromJsonException extends Exception {

    private static final String DEFAULT_MESSAGE = "Conversion json to Museum object error. Wrong json.";

    public MuseumConversionFromJsonException() {
        super(DEFAULT_MESSAGE);
    }

    public MuseumConversionFromJsonException(String message) {
        super(message);
    }
}
