package com.zaytsevp.kafkaexample.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Pavel Zaytsev
 */
public class StringGenerator {

    public static String generateRandomString(int length) {
        boolean useLetters = true;
        boolean useNumbers = false;

        return RandomStringUtils.random(length, useLetters, useNumbers);
    }

    public static String generateRandomStringWithRandomWorld(int length) {
        boolean useLetters = true;
        boolean useNumbers = false;

        boolean hasWorldWord = ThreadLocalRandom.current().nextBoolean();

        return RandomStringUtils.random(length, useLetters, useNumbers) + (hasWorldWord ? "world" : "");
    }
}
