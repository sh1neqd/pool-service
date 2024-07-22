package ru.alexbogdan.pool_service.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.alexbogdan.pool_service.models.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET" +
            " name = :#{#user.getName()}," +
            " email = :#{#user.getEmail()}," +
            " phone = :#{#user.getPhone()}" +
            " WHERE id = :#{#user.getId()}", nativeQuery = true)
    int updateUser(@Param("user") User user);
}