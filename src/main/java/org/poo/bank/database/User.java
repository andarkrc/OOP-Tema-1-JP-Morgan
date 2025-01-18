package org.poo.bank.database;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.UserInput;

import java.time.LocalDate;

@Setter
@Getter
public final class User {
    private String firstName;
    private String lastName;
    private String email;
    private String occupation;
    private LocalDate birthDate;

    public User(final UserInput input) {
        firstName = input.getFirstName();
        lastName = input.getLastName();
        email = input.getEmail();
        occupation = input.getOccupation();
        birthDate = LocalDate.parse(input.getBirthDate());
    }
}
