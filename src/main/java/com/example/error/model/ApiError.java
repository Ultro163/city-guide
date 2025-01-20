package com.example.error.model;

import com.example.util.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Модель, представляющая структуру ошибки, которая отправляется в ответе на запросы,
 * когда происходит исключение в приложении. Она содержит информацию о сообщении ошибки,
 * причине, статусе и времени возникновения ошибки.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    @Builder.Default
    List<String> errors = new ArrayList<>();
    String message;
    String reason;
    String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT)
    LocalDateTime timestamp;
}