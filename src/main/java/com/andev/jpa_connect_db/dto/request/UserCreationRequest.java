package com.andev.jpa_connect_db.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    String id;
    @Size(min = 3, message = "Username must be more than 3 character!!!")
    String username;

    @Size(min = 8, message = "Password need more than 8 character!!!")
    String password;
    String firstName;
    String lastName;
    LocalDate dob;
}
