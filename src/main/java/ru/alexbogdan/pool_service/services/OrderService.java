package ru.alexbogdan.pool_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.alexbogdan.pool_service.config.PoolProperties;
import ru.alexbogdan.pool_service.exceptions.AppAnswer;
import ru.alexbogdan.pool_service.models.Order;
import ru.alexbogdan.pool_service.models.dtos.TimeCountDTO;
import ru.alexbogdan.pool_service.repositories.OrderRepository;
import ru.alexbogdan.pool_service.repositories.UserRepository;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PoolProperties poolProperties;

    private final int MAX_RESERVATIONS_PER_HOUR = 10;
    private final int MAX_RESERVATIONS_PER_DAY = 1;

    public List<TimeCountDTO> getOrdersByDate(LocalDate date) {
        List<Order> orders = orderRepository.findByDate(date);

        if (orders.isEmpty()) {
            return List.of();
        }

        Map<String, Long> timeCountMap = orders.stream()
                .collect(Collectors.groupingBy(order -> order.getTime().toString(), Collectors.counting()));

        return timeCountMap.entrySet().stream()
                .map(entry -> new TimeCountDTO(entry.getKey(), entry.getValue().intValue()))
                .collect(Collectors.toList());
    }

    public ResponseEntity<?> getAvailableTimes(LocalDate date) {
        LocalTime start = LocalTime.parse(poolProperties.getStart());
        LocalTime end = LocalTime.parse(poolProperties.getEnd());
        List<Order> orders = orderRepository.findByDate(date);

        List<LocalTime> allTimes = new ArrayList<>();
        LocalTime current = start;
        while (!current.isAfter(end)) {
            allTimes.add(current);
            current = current.plusHours(1);
        }

        List<LocalTime> reservedTimes = orders.stream()
                .filter(order -> orderRepository.countByDateAndTime(date, order.getTime()) >= MAX_RESERVATIONS_PER_HOUR)
                .map(Order::getTime)
                .collect(Collectors.toList());

        allTimes.removeAll(reservedTimes);
        if (allTimes.isEmpty()) {
            return new ResponseEntity<>(new AppAnswer(HttpStatus.NOT_FOUND.value(),
                    "invalid date or is empty"), HttpStatus.NOT_FOUND);
        } else return ResponseEntity.ok(allTimes);

    }

    public ResponseEntity<?> reserveOrder(Long userId, LocalDate date, LocalTime time) {
        if (!isWithinWorkingHours(time)) {
            return new ResponseEntity<>(new AppAnswer(HttpStatus.BAD_REQUEST.value(),
                    "time is outside of working hours"), HttpStatus.BAD_REQUEST);
        }

        if (orderRepository.countByDateAndTime(date, time) >= MAX_RESERVATIONS_PER_HOUR) {
            return new ResponseEntity<>(new AppAnswer(HttpStatus.BAD_REQUEST.value(),
                    "maximum reservations reached for this time"), HttpStatus.BAD_REQUEST);
        }

        if (orderRepository.existsByUserIdAndDate(userId, date)) {
            long userReservationsCount = orderRepository.countByUserIdAndDate(userId, date);
            if (userReservationsCount >= MAX_RESERVATIONS_PER_DAY) {
                return new ResponseEntity<>(new AppAnswer(HttpStatus.BAD_REQUEST.value(),
                        "user already has the maximum number of reservations for this day"), HttpStatus.BAD_REQUEST);
            }
        }

        Order order = new Order();
        order.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
        order.setDate(date);
        order.setTime(time);
        orderRepository.save(order);
        return new ResponseEntity<>(new AppAnswer(HttpStatus.OK.value(),
                "order is created"), HttpStatus.OK);
    }
    public ResponseEntity<?> cancelOrder(Long orderId) {
        if(orderRepository.findById(orderId).isEmpty()) {
            return new ResponseEntity<>(new AppAnswer(HttpStatus.NOT_FOUND.value(),
                    "order with this id not found"), HttpStatus.NOT_FOUND);
        } else {
            orderRepository.deleteById(orderId);
            return new ResponseEntity<>(new AppAnswer(HttpStatus.OK.value(),
                    "order is deleted"), HttpStatus.OK);
        }

    }

    private boolean isWithinWorkingHours(LocalTime time) {
        LocalTime start = LocalTime.parse(poolProperties.getStart());
        LocalTime end = LocalTime.parse(poolProperties.getEnd());
        return !time.isBefore(start) && !time.isAfter(end);
    }

}