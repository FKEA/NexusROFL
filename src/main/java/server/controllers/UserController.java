package server.controllers;

import server.models.User;
import server.providers.EventProvider;
import server.providers.UserProvider;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Marc & Mikkel on 17-10-2017.
 * <p>
 * This is the UserController, which handles all the user-related logic.
 */
public class UserController {

    public UserController() {

    }

    /**
     * The method "validateUserCreation" is used to verify the input given from the client when trying to create a new user. This
     * is done to make sure the new user has a valid email, gender and semester in order to store in the database.
     * <p>
     * The below parameters are given from the userEndPoint and are called as parameters in the validateUseCreation
     * method.
     *
     * @param password
     * @param firstName
     * @param lastName
     * @param email
     * @param gender
     * @param description
     * @param major
     * @param semester
     * @return
     * @throws IllegalArgumentException
     */
    public User validateUserCreation(String password, String firstName, String lastName,
                                     String email, char gender, String description, String major, int semester) throws SQLException {

        UserProvider userProvider = new UserProvider();

        /**
         * A user object is created, which is needed to validate the different parameters.
         */
        User validatedUser = new User(password, firstName, lastName, email, description, String.valueOf(gender), major, semester);

        /**
         * The email is split up by the "@" in the email, which makes us able to validate only the domain-side of the
         * email address. This split also makes sure there's an @ in the mail, as it will cause an error which is caught
         * if there's no "@".
         */
        String[] emailParts;
        emailParts = email.split("@");
        String emailName = emailParts[0];
        String emailDomain = emailParts[1];


        /**
         * The email domain, gender and semester is checked by 3 if-methods. The first if-method checks if the email
         * domain is not equal to "cbs.dk" or ".cbs.dk", if it's not equal to one of the two, it throws an exception.
         * The second if (else-if), checks if the gender is not equal to either "m" of "f", if it is not equal to one
         * of the two, it once again throw an exception.
         * The last if (else-if) checks if the semester is below 1 or above 6, if so, it once again throws an exception.
         */
        if (!emailDomain.contains("cbs.dk") && !emailDomain.contains(".cbs.dk")
                ) {
            System.out.print("\nemail fejl\n");
            System.out.print(emailDomain);
            throw new IllegalArgumentException("Email did not contain correct a domain");
        } else if (!String.valueOf(gender).equalsIgnoreCase("f")
                && !String.valueOf(gender).equalsIgnoreCase("m")) {
            System.out.print("koen fejl");
            throw new IllegalArgumentException("Gender can only be \"m\" or \"f\"");
        } else if (semester < 1 || semester > 6) {
            System.out.print("semester fejl");
            throw new IllegalArgumentException("Semester must be a value of 1-6");
        } else if (userProvider.getUserByEmail(validatedUser.getEmail()) != null) {
            throw new IllegalArgumentException("Email already exists");
        }

        /**
         * If all of the above if-statements are false, the validated user-object is returned.
         */
        return validatedUser;
    }

    /**
     * This method is used to pre-validate the gender-object. This is done in order to make sure the length of the
     * input of the gender is only 1 character long. If it's above 1 character an exception is thrown.
     *
     * @param gender
     */
    public void validateGenderInput(String gender) {
        if (gender.length() > 1) {
            throw new IllegalArgumentException("Gender can only be \"m\" or \"f\"");
        }
    }

    /**
     * This method is used to get all specific participants for an specific event.
     *
     * @param event_id
     * @return
     */
    public ArrayList<User> getParticipants(int event_id) throws SQLException {


        EventProvider eventProvider = new EventProvider();
        UserProvider userProvider = new UserProvider();

        ArrayList<User> participants = new ArrayList<User>();

        for (Integer user_id : eventProvider.getParticipantIdsByEventId(event_id)) {

            participants.add(userProvider.getUser(user_id));

        }

        return participants;

    }
}