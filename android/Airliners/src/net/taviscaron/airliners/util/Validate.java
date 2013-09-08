package net.taviscaron.airliners.util;

/**
 * Some asserts
 * @author Andrei Senchuk
 */
public class Validate {
    public static void notNull(Object obj, String message) {
        if(obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void makeSure(boolean cond, String message) {
        if(!cond) {
            throw new IllegalArgumentException(message);
        }
    }
}
