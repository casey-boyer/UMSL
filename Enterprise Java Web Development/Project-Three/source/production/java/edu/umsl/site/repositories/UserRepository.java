package edu.umsl.site.repositories;

import edu.umsl.site.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    //Return a User object given the user's ID in the USERS table.
    User getById(long id);
}
