package ru.alexbogdan.pool_service.models.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReservationDTO {
    private Long clientId;
    private String date;
    private String time;
}
