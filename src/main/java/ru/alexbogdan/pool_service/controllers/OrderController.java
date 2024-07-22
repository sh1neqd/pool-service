package ru.alexbogdan.pool_service.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.alexbogdan.pool_service.exceptions.AppAnswer;
import ru.alexbogdan.pool_service.models.dtos.ReservationDTO;
import ru.alexbogdan.pool_service.models.dtos.TimeCountDTO;
import ru.alexbogdan.pool_service.services.OrderService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/v0/pool/timetable")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllOrders(@RequestParam String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            List<TimeCountDTO> timeCountDtos = orderService.getOrdersByDate(localDate);

            if (timeCountDtos.isEmpty()) {
                return new ResponseEntity<>(new AppAnswer(HttpStatus.NOT_FOUND.value(), "no orders found"), HttpStatus.NOT_FOUND);
            } else {
                return ResponseEntity.ok(timeCountDtos);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new AppAnswer(HttpStatus.BAD_REQUEST.value(), "Invalid date format. Please use 'YYYY-MM-DD'"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/available", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAvailableTimes(@RequestParam String date) {
        try {
            return orderService.getAvailableTimes(LocalDate.parse(date));
        } catch (Exception e){
            return new ResponseEntity<>(new AppAnswer(HttpStatus.BAD_REQUEST.value(),
                    "invalid date format. Please use 'YYYY-MM-DD'"), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping(value = "/reserve", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> reserveOrder(@RequestBody ReservationDTO reservationDTO) {
        return orderService.reserveOrder(
                reservationDTO.getClientId(),
                LocalDate.parse(reservationDTO.getDate()),
                LocalTime.parse(reservationDTO.getTime())
        );
    }

    @GetMapping(value = "/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> cancelOrder(@RequestParam Long orderId) {
        return orderService.cancelOrder(orderId);
    }
}
