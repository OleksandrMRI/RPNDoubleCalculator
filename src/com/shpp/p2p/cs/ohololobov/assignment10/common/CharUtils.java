package com.shpp.p2p.cs.ohololobov.assignment10.common;

/**
 * The class contains common logic for several classes of program in static methods
 */
public class CharUtils {
    /**
     * private constructor prevents the creation of objects of this class
     */
    private CharUtils() {
    }

    /**
     * method checks if char a letter is
     *
     * @param ch checked char
     * @return true if the char lies in the range from a to z
     */
    public static boolean isLetter(char ch) {
        return ch >= 'a' && ch <= 'z';
    }

    /**
     * method checks if char a number is
     *
     * @param ch checked char
     * @return true if the char lies in the range from 0 to 9
     */
    public static boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    public static boolean isLetterIgnoreCase(char ch) {
        return isLetter(ch) ||(ch >= 'A' && ch <= 'Z');
    }
}
