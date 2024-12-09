package org.poo;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.UserInput;

@Setter
@Getter
public class User {
    private String firstName;
    private String lastName;
    private String email;

    public User(UserInput input) {
        firstName = input.getFirstName();
        lastName = input.getLastName();
        email = input.getEmail();
    }
}
