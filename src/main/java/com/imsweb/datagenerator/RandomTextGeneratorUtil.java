/*
 * Copyright (C) 2023 Information Management Services, Inc.
 */
package com.imsweb.datagenerator;

import java.util.Random;

public final class RandomTextGeneratorUtil {

    private static final String[] _FILLER_TEXT = new String[] {
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
            "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.",
            "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
    };
    private static final Random _RANDOM = new Random();

    private RandomTextGeneratorUtil() {}

    public static String getRandomText() {
        return _FILLER_TEXT[_RANDOM.nextInt(_FILLER_TEXT.length)];
    }
}
