package com.urgentrn.urncexchange.utils;

public class PinValidator {

    public static boolean isSequential(String pin) {
        int p1 = Character.getNumericValue(pin.charAt(0));
        int p2 = Character.getNumericValue(pin.charAt(1));
        int p3 = Character.getNumericValue(pin.charAt(2));
        int p4 = Character.getNumericValue(pin.charAt(3));
        return p2 == (p1 + 1) % 10 && p3 == (p1 + 2) % 10 && p4 == (p1 + 3) % 10;
    }

    public static boolean isRepeated(String pin) {
        return pin.charAt(1) == pin.charAt(2) && (pin.charAt(0) == pin.charAt(1) || pin.charAt(2) == pin.charAt(3));
    }

    public static boolean isInteger(String pin) {
        try {
            Integer.parseInt(pin);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isEmpty(String pin) {
        return pin == null || pin.length() == 0;
    }

    public static boolean isConsecutive(final String pin) {
        int[] digits = new int[pin.length()];
        int[] differences = new int[pin.length() - 1];
        int temp;

        for (int i = 0; i < pin.length(); i++)
            try {
                digits[i] = Integer.parseInt(String.valueOf(pin.charAt(i)));
            } catch (NumberFormatException e) {
                return false;
            }

        for (int i = 0; i < digits.length - 1; i++)
            differences[i] = Math.abs(digits[i] - digits[i + 1]);

        if (differences.length != 0) {
            temp = differences[0];
            for (int i = 1; i < differences.length; i++)
                if (temp != differences[i]) return false;
        }

        return true;
    }
}
