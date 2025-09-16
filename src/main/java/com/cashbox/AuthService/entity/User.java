package com.cashbox.AuthService.entity;

import com.cashbox.AuthService.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Email format is invalid")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number and one special character"
    )
    private String password;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "First name can only contain letters")
    private String firstName;

    @Size(max = 50, message = "Middle name cannot exceed 50 characters")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "Middle name can only contain letters")
    private String middleName; // Optional field

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Last name can only contain letters")
    private String lastName;

    @Column(unique = true)
    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^\\+[1-9]\\d{1,14}$",
            message = "Phone number must include country code in E.164 format (e.g., +1234567890)"
    )
    private String phone;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @Builder.Default
    private boolean enabled = true;

    // Store roles as ENUMs
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(EnumType.STRING)
    @NotEmpty(message = "At least one role is required")
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_type_id", nullable = false)
    @NotNull(message = "Account type is required")
    private AccountType accountType;
}