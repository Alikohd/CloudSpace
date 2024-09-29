package ru.oleevych.cloudspace.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users",
        uniqueConstraints = @UniqueConstraint(name = "unique_username", columnNames = "username")
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(unique = true)
    private String username;
    @NotNull
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
