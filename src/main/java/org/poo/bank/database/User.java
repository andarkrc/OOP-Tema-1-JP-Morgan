package org.poo.bank.database;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.UserInput;

@Setter
@Getter
public final class User {
    private String firstName;
    private String lastName;
    private String email;

    public User(final UserInput input) {
        firstName = input.getFirstName();
        lastName = input.getLastName();
        email = input.getEmail();
    }
}
