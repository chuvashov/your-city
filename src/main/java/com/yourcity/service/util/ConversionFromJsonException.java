package com.yourcity.service.util;

/**
 * Created by Andrey on 28.03.2015.
 */
public class ConversionFromJsonException extends Exception {

    private static final String DEFAULT_MESSAGE = "Conversion from json to object error. Wrong json.";

    public ConversionFromJsonException() {
        super(DEFAULT_MESSAGE);
    }

    public ConversionFromJsonException(String message) {
        super(message);
    }
}
