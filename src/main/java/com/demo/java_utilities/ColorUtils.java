package com.demo.java_utilities;

public enum ColorUtils {
    RESET("\u001B[0m"),
    RED("\u001B[0;31m"),
    GREEN("\u001B[0;32m"),
    YELLOW("\u001B[0;33m"),
    BRIGHT_GREEN("\u001B[1;32m"),
    BLUE("\u001B[0;34m"),
    MAGENTA("\u001B[0;35m"),
    CYAN("\u001B[0;36m"),
    BRIGHT_RED("\u001B[1;31m"),
    BRIGHT_YELLOW("\u001B[1;33m"),
    BRIGHT_BLUE("\u001B[1;34m"),
    BRIGHT_MAGENTA("\u001B[1;35m"),
    BRIGHT_CYAN("\u001B[1;31m");

    private final String code;

    ColorUtils(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}
