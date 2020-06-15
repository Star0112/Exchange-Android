package com.urgentrn.urncexchange.utils;

public class GraphUtils {

    public static int getPeriodFactor(int period) {
        switch (period) {
            case 0:
                return 60;
            case 1:
                return 60 * 24;
            case 2:
                return 60 * 24 * 7;
            case 3:
                return 60 * 24 * 30;
            case 4:
                return 60 * 24 * 365;
            default:
                return 60 * 24 * 365;
        }
    }

    public static int getInterval(int period) {
        switch (period) {
            case 0: // 1 hour
                return 1;
            case 1: // 1 day
                return 4;
            case 2: // 1 week
                return 30;
            case 3: // 1 month
                return 120;
            default: // 1 year
                return 1440;
        }
    }
}
