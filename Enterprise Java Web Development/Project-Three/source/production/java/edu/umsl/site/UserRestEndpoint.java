package edu.umsl.site;

import edu.umsl.config.annotation.RestEndpoint;
import edu.umsl.site.entities.User;
import edu.umsl.site.exception.ResourceNotFoundException;
import edu.umsl.site.form.AdminForm;
import edu.umsl.site.form.UserForm;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.inject.Inject;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@RestEndpoint
public class UserRestEndpoint {
    @Inject UserManager userManager;

    /*This will return the discovery of all operations for project3/user.
    * The request body should be empty.*/
    @RequestMapping(value = "user", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> discover()
    {
        System.out.println("UserRestEndPoint: In discover()");
        HttpHeaders headers = new HttpHeaders();

        /*The return header should be Allow with a comma separated list of
        * the HTTP methods supported for the resource. For 'project3/user', the
        * supported HTTP methods are OPTIONS, POST, and GET.*/
        headers.add("Allow", "OPTIONS,POST,GET");

        /*Return 204 for successful OPTIONS request.*/
        return new ResponseEntity<>(null, headers, HttpStatus.NO_CONTENT);
    }

    /*This will return the discovery of all operations on the user
    * resource for project3/user/{id}.
    * The request body should be empty.*/
    @RequestMapping(value = "/{id}", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> discover(@PathVariable("id") long id)
    {
        System.out.println("UserRestEndPoint: In discover(@PathVariable(\"id\") long " + id);

        User user = user = this.userManager.getUser(id);

        /*If the user with the specified id does not exist, throw a ResourceNotFoundException()
        * so that a 404 response is returned.*/
        if (user == null)
            throw new ResourceNotFoundException();

        HttpHeaders headers = new HttpHeaders();

        /*The return header should be Allow with a comma separated list of
        * the HTTP methods supported for the resource. For 'project3/user/{id}', the
        * supported HTTP methods are OPTIONS, GET, PUT, and DELETE.*/
        headers.add("Allow", "OPTIONS,GET,PUT,DELETE");

        return new ResponseEntity<>(null, headers, HttpStatus.NO_CONTENT);
    }

    /*This will return a list of all the users in the USERS table. This
    * maps to a GET request with the value "project3/user".
    * Return 200 (HttpStatus.OK) for successful GET operations.*/
    @RequestMapping(value = "user", method = RequestMethod.GET)
    @ResponseBody @ResponseStatus(HttpStatus.OK)
    public UserList read()
    {
        System.out.println("UserRestEndPoint: In read()");
        //Create a UserList object to hold the users
        UserList userList = new UserList();

        //Get all the users from the users table
        userList.setValue(this.userManager.getAllUsers());

        //Return all the users
        return userList;
    }

    /*This will map a request with the value "project3/user/{id}" with the GET request method.
    * This returns the user's fields, as JSON, of the user with the specified ID, and a 200 Response
    * (HttpStatus.OK) is returned.
    * If this user is not found in the enterprise database, a ResourceNotFoundException is thrown, and
    * a 404 response is returned.*/
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody @ResponseStatus(HttpStatus.OK)
    public User read(@PathVariable("id") long id)
    {
        System.out.println("UserRestEndPoint: In read(@PathVariable(\"id\")long " + id);

        //Get the user with this ID from the database
        User user = this.userManager.getUser(id);

        //If the user with the specified ID does not exist, throw a ResourceNotFoundException()
        if (user == null)
            throw new ResourceNotFoundException();

        return user;
    }

    /*Register a new user when the request received is POST and the value is "project3/user".
    * The JSON containing the required user fields is expected in the request body.
    * Return 201 for successful POST request, and the response body will contain
    * the JSON of the created user. If all required data fields are not set,
    * return a 400 error and include the missing data fields in the response body.*/
    @RequestMapping(value = "user", method = RequestMethod.POST)
    public ResponseEntity<User> create(@RequestBody UserForm form)
    {
        System.out.println("UserRestEndPoint: In create(@RequestBody UserForm form)");

        User user = new User();

        //Initialize the fields of the user object
        user.setUsername(form.getUsername());
        user.setPassword(form.getPassword());
        user.setFirstname(form.getFirstName());
        user.setLastname(form.getLastName());

        //Save the user to the database.
        user = this.userManager.saveUser(user);

        //Create the URI path that will be put in the location of the response header. This
        //will indicate the URL for the newly created user.
        String uri = ServletUriComponentsBuilder.fromCurrentServletMapping()
                .path("/user/{id}").buildAndExpand(user.getId()).toString();

        HttpHeaders headers = new HttpHeaders();

        headers.add("Location", uri);

        return new ResponseEntity<>(user, headers, HttpStatus.CREATED);
    }

    /*This maps a DELETE request with the value 'project3/user/{id}', where the user
    * with the specified ID in the USERS table is to be deleted. Upon successful
    * deletion, a 204 response (HttpResponse.NO_CONTENT) is returned. A UserForm
    * object is required in the request body to verify that the 'admin' is deleting
    * the user.
    * If someone other than the admin attempts to delete the user, then a 403
    * error is returned.
    * If the user attempting to be deleted does not exist, a ResourceNotFoundException is thrown,
    * and a 404 error is returned.*/
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable("id") long id, @RequestBody AdminForm form)
    {
        User user = this.userManager.getUser(id);

        //If this user does not exist, throw a ResourceNotFoundException().
        if (user == null)
            throw new ResourceNotFoundException();

        //Test to ensure that the form in the RequestBody is not null
        if ((form.getAdminUsername() != null) && (form.getAdminPassword() != null))
        {
            //Test if the user attempting to delete the user with the specified ID has admin privileges.
            //If so, delete the user with the given ID and return a 204 response with an empty body.
            if (form.testIfAdmin())
            {
                this.userManager.deleteUser(id);
                return new ResponseEntity<>(null, null, HttpStatus.NO_CONTENT);
            }
        }

        //If the user does not have admin privileges, then return a 403 Forbidden response
        //with an empty response body.
        return new ResponseEntity<>(null, null, HttpStatus.FORBIDDEN);
    }

    /*This maps a PUT request with the value 'project3/user/{id}'. If the
    * PUT request is successful, an empty response body and 204 status (HttpStatus.NO_CONTENT)
    * is returned. A UserForm object is also expected, but all fields may be empty.
    * If the user with the specified ID does not exist, then a ResourceNotFoundException is thrown,
    * and the response returns a 404 error.*/
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") long id, @RequestBody(required = false) UserForm form)
    {
        System.out.println("UserRestEndPoint: In update(@PathVariable(\"id\")long " + id + " UserForm");

        /*Update the user based on the values received in the form. The form
        * may be empty, and all fields are optional.*/

        //Get the user with the specified id
        User user = this.userManager.getUser(id);

        //If this user does not exist, throw a 404 error.
        if (user == null)
            throw new ResourceNotFoundException();

        //Update the specified user.
        this.userManager.updateUser(user, form);
    }

    //Static class used to get or set a list of all the users in the USERS table. Only needs to be
    //used by the UserRestEndpoint class, hence why it is static.
    @XmlRootElement(name = "users")
    public static class UserList
    {
        private List<User> value;

        @XmlElement(name = "user")
        public List<User> getValue()
        {
            return value;
        }

        public void setValue(List<User> accounts)
        {
            this.value = accounts;
        }
    }
}
