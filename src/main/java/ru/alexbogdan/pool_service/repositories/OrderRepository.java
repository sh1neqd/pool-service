package ru.alexbogdan.pool_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alexbogdan.pool_service.models.Order;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByDate(LocalDate date);
    long countByDateAndTime(LocalDate date, LocalTime time);
    boolean existsByUserIdAndDate(Long userId, LocalDate date);
    long countByUserIdAndDate(Long userId, LocalDate date);
}
