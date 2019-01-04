package edu.umsl.site;

import edu.umsl.site.entities.User;
import edu.umsl.site.form.UserForm;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface UserManager {
    User getUser(long id); //Get the user with the specified ID

    void deleteUser(long id); //Delete the user with the specified ID

    List<User> getAllUsers(); //Return all existing users

    User updateUser(User user, UserForm userForm); //Update an existing user

    //Save a user to the database. The user object may not be null, and if it is, the
    //error message defined in validation_en_US.properties is returned.
    User saveUser(@NotNull(message = "{validate.userManager.saveUser.user}")
                  @Valid User user);
}
