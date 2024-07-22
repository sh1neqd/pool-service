package ru.alexbogdan.pool_service.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.NumberFormat;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Length(max=100, message = "name must not exceed 100 characters")
    private String name;

    @NotEmpty(message = "phone must be not empty")
    @NumberFormat
    private String phone;

    @NotEmpty(message = "email must be not empty")
    @Email(message = "wrong email format")
    @Column(name = "email")
    private String email;
}
