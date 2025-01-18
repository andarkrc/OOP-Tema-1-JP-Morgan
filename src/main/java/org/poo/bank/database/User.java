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
    private String occupation;
    private Date birthDate;

    public User(final UserInput input) {
        firstName = input.getFirstName();
        lastName = input.getLastName();
        email = input.getEmail();
        occupation = input.getOccupation();
        String date = input.getBirthDate();
        String[] dateTokens = date.split("-");
        birthDate = new Date(Integer.parseInt(dateTokens[0]), Integer.parseInt(dateTokens[1]), Integer.parseInt(dateTokens[2]));
    }
}
