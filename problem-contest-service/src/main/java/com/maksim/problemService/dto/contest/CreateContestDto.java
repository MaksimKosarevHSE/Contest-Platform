package com.maksim.problemService.dto.contest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Schema(description = "Данные для создания нового контеста")
public class CreateContestDto {
    @NotBlank(message = "Название контеста обязательно")
    @Size(max = 255, message = "Название не может превышать 255 символов")
    @Schema(description = "Название контеста", example = "Кубок Трёх Флешек")
    private String title;

    @Schema(description = "Публичный ли контест", example = "true")
    private Boolean isPublic;

    @NotNull(message = "Время начала обязательно")
    @Future(message = "Время начала должно быть в будущем")
    @Schema(description = "Время начала", example = "2025-04-01T10:00:00")
    private LocalDateTime startTime;

    @NotNull(message = "Время окончания обязательно")
    @Future(message = "Время окончания должно быть в будущем")
    @Schema(description = "Время окончания", example = "2025-04-01T12:00:00")
    private LocalDateTime endTime; // В сервисе проверить надо, что > startTime

    @NotEmpty(message = "Список задач не может быть пустым")
    @Schema(description = "Список ID задач, включённых в контест")
    private List<@Positive Integer> problemsId;
}
