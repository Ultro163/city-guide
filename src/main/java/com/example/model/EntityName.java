package com.example.model;

import lombok.Getter;

/**
 * Перечисление, представляющее наименования сущностей, используемых в приложении.
 * Это перечисление позволяет произвести логирование в абстрактном сервисе.
 */
@Getter
public enum EntityName {
    USER("user"),
    CATEGORY("category"),
    CITY("city"),
    ATTRACTION("attraction"),
    REVIEW("review");

    private final String displayName;

    EntityName(String displayName) {
        this.displayName = displayName;
    }
}