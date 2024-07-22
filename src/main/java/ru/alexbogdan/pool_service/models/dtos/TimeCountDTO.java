package ru.alexbogdan.pool_service.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TimeCountDTO {
    private String time;
    private int count;
}
