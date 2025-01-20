package com.example.util;

/**
 * Утилитный класс для хранения常ых констант, используемых в проекте.
 * Этот класс предоставляет доступ к различным константам, таким как формат даты и времени.
 * <p>
 * Этот класс является утилитным, и его экземпляры не могут быть созданы.
 */
public class Constants {
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private Constants() {
        throw new IllegalStateException("Utility class");
    }
}