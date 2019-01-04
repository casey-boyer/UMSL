package edu.umsl.site;

import edu.umsl.site.entities.User;
import edu.umsl.site.form.UserForm;
import edu.umsl.site.repositories.UserRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultUserManager implements UserManager {
    @Inject UserRepository userRepository;

    @Override
    public User getUser(long id) {
        return this.userRepository.getById(id);
    }

    @Override
    public void deleteUser(long id) {
        this.userRepository.delete(id);
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        return this.toList(this.userRepository.findAll());
    }

    /*Update an existing user in the database. All form fields in the UserForm object are
    * optional when updating a user.*/
    @Override
    public User updateUser(User user, UserForm userForm) {

        //If the UserForm object is not null, then update the fields that
        //were provided.
        if (userForm != null)
        {
            //Update the username field if it is not null.
            if (userForm.getUsername() != null)
            {
                if (!userForm.getUsername().isEmpty())
                    user.setUsername(userForm.getUsername());
            }

            //Update the password field if it is not null.
            if (userForm.getPassword() != null)
            {
                if (!userForm.getPassword().isEmpty())
                    user.setPassword(userForm.getPassword());
            }

            //Update the first name field if it is not null.
            if (userForm.getFirstName() != null)
            {
                if (!userForm.getFirstName().isEmpty())
                    user.setFirstname(userForm.getFirstName());
            }

            //Update the last name field if it is not null.
            if (userForm.getLastName() != null)
            {
                if (!userForm.getLastName().isEmpty())
                    user.setLastname(userForm.getLastName());
            }
        }

        //Regardless if the UserForm (request body) was empty or not, update the date that this
        //row was last modified.
        user.setlastUpdated(getLastModified());

        //Save the user.
        user = this.saveUser(user);

        return user;
    }

    @Override
    public User saveUser(User user) {

        //Set the last updated time.
        user.setlastUpdated(getLastModified());

        this.userRepository.save(user);

        return user;
    }

    private String getLastModified()
    {
        //Obtain the current Instant from the system clock
        Instant timestamp = Instant.now();

        //Get the current time as a date to indicate when this user was last modified.
        //The date needs to be in the YYYY-MM-DD format for MySQL.
        LocalDateTime localDateTime = LocalDateTime.ofInstant(timestamp, ZoneId.systemDefault());
        String date = Integer.toString(localDateTime.getYear()) + "-" + localDateTime.getMonthValue()
                + "-" + localDateTime.getDayOfMonth();

        System.out.println("DefaultUserManager: in saveUser()\n\tThe date is " + date);

        return date;
    }

    private <E> List<E> toList(Iterable<E> i)
    {
        /*Iterable is a parent class of List<>
         * Since transactions are involved, if the resultSet is closed while the transaction is still
         * open, may have lost some data*/
        List<E> list = new ArrayList<>();
        i.forEach(list::add);
        return list;
    }
}
