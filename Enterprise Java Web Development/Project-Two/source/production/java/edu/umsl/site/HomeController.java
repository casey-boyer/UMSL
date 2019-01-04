package edu.umsl.site;

import edu.umsl.site.entity.*;
import edu.umsl.site.form.EducationHistoryForm;
import edu.umsl.site.form.RegisterForm;
import edu.umsl.site.form.SignInForm;
import edu.umsl.site.form.WorkHistoryForm;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import javax.inject.Inject;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
@MultipartConfig
public class HomeController
{
    @Inject UserService userService;

    /*Default mapping for deploying localhost:/8080/project2. Will return the JSP corresponding
    * to the sign in page.*/
    @RequestMapping("/")
    public View home(Map<String, Object> model)
    {
        model.put("signInUrl", "signIn");

        System.out.println("In home() method");

        return new RedirectView("/{signInUrl}", true);
    }

    /*If the user submits the form on the signIn.jsp page with the parameters username and password,
    * then redirect them to the custom 404 error page, because these parameters will only be present
    * in the URL if the user used a GET request.*/
    @RequestMapping(value = "/signIn", params = {"username", "password"}, method = RequestMethod.GET)
    public View signInError(Map<String, Object> model)
    {
        model.put("signInForm", new SignInForm());
        return new RedirectView("/error", true);
    }

    /*Redirect the user to the sign-in page*/
    @RequestMapping(value = "/signIn", method = RequestMethod.GET)
    public String signIn(Map<String, Object> model)
    {
        model.put("signInForm", new SignInForm());

        /*If the user has already signed-in, then redirect them to the profile page. Otherwise,
        * go to the sign in page.*/
        if (this.userService.getUser() != null)
            return "redirect:/profile";
        else
            return "user/signIn";
    }

    /*Receiving the completed form from the sign-in page*/
    @RequestMapping(value = "/signIn", method = RequestMethod.POST)
    public View userSignIn(SignInForm form, RedirectAttributes redirectAttributes) {
        User user = new User(form.getUsername(), form.getPassword());

        /*If the user provided an invalid password or username, this errorMessage will be
        * displayed on the sign-in page*/
        String errorMessage = this.userService.signInUser(user);
        redirectAttributes.addFlashAttribute("errorMessage", errorMessage);

        this.userService.getUser();

        /*If the error message is not null, then the user has provided an invalid username or
        * password; redirect them to the signIn JSP. Otherwise, proceed to the profile.*/
        if (errorMessage != null)
            return new RedirectView("/signIn", true);
        else
            return new RedirectView("/profile", true);
    }

    /*Redirect the user to the register page*/
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(Map<String, Object> model)
    {
        RegisterForm registerForm = new RegisterForm();
        model.put("registerForm", registerForm);

        //For providing a list of states
        model.put("stateMap", registerForm.getStateMap());
        model.put("user", this.userService.getUser());

        /*If the user is already signed-in or has already registered, redirect them
        * to the profile page. Otherwise, go to the register page.*/
        if (this.userService.getUser() != null) {
            return "redirect:/profile";
        }
        else
            return "user/register";
    }

    /*If the user selects the GET method when submitting the sign-in form, redirect them to the
    * custom 404 error page. This will occur when the form action is GET, and thus the following parameters
    * will be present in the URL upon submission.*/
    @RequestMapping(value = "/register", params = {"username", "password", "firstName"},
            method = RequestMethod.GET)
    public View registerError(Map<String, Object> model)
    {
        model.put("registerForm", new RegisterForm());
        return new RedirectView("/error", true);
    }

    /*Receiving the completed form from the register page*/
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public View userRegister(RegisterForm form, RedirectAttributes redirectAttributes) {
        User user = new User(form.getUsername(), form.getPassword(), form.getFirstName(), form.getLastName(),
                form.getAddressStreetOne(), form.getAddressStreetTwo(), form.getCity(), form.getState(),
                form.getZipcode());

        //If an error message was set, store this as a FLASH attribute in the redirect
        //attributes; this way, when the register method returns the register URL, the error
        //message will be displayed (instead of using a normal attribute and displaying this in the
        //URL).
        String errorMessage = this.userService.saveUserData(user);
        redirectAttributes.addFlashAttribute("errorMessage", errorMessage);


        //If there is an error message, store it in the model and redirect the user to the register
        //form again. Otherwise, go to the profile.
        if (errorMessage != null) {
            return new RedirectView("/register", true);
        }
        else
            return new RedirectView("/profile", true);
    }

    /*Go to the user's profile after the user registers or signs in.*/
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profile(Map<String, Object> model)
    {
        /*If the user has not registered or signed-in, then redirect them to the sign in page.
        * Otherwise, redirect the user to the profile page.*/
        if (this.userService.getUser() == null) {
            return "redirect:/signIn";
        }
        else {
            //Get all of the rows corresponding to this user from the WORKHISTORY table.
            WorkHistoryForm workHistoryForm = new WorkHistoryForm();
            workHistoryForm.setWorkHistoryList(this.userService.getWorkHistory());

            //Get all of the rows corresponding to this user from the EDUCATIONHISTORY table.
            EducationHistoryForm  educationHistoryForm = new EducationHistoryForm();
            educationHistoryForm.setEducationHistoryList(this.userService.getEducationHistory());

            model.put("workHistoryForm", workHistoryForm);
            model.put("educationHistoryForm", educationHistoryForm);
            model.put("user", this.userService.getUser());

            //Go to the profile JSP.
            return "home/profile";
        }
    }

    /*If the user clicks the 'Edit Details' link on the profile JSP page, then allow them to edit
    * all of the User object fields.*/
    @RequestMapping(value = "/editDetails", method = RequestMethod.GET)
    public String editProfileDetails(Map<String, Object> model) {

        /*If the user has not yet signed in or registered, redirect them to the sign-in JSP.*/
        if (this.userService.getUser() == null) {
            return "redirect:/signIn";
        }
        else {
            //Get the current user from the database.
            User user = this.userService.getUser();

            //For selecting a state
            user.setStateMap();
            model.put("stateMap", user.getStateMap().getStateMap());

            //Put the user object in the model to make it available to the form
            model.put("user", user);

            //Go to the editDetails JSP
            return "home/editDetails";
        }
    }

    /*Recieve the submitted form from the editDetails JSP.*/
    @RequestMapping(value = "/editDetails", params="editUserDetails", method = RequestMethod.POST)
    public RedirectView submitProfileDetails(User user, @RequestParam("profilePicture") MultipartFile file) {

        /*If the user has not yet signed in or registered, redirect them to the sign in JSP.*/
        if (this.userService.getUser() == null) {
            return new RedirectView("/signIn", true);
        }
        else {
            //If the image file in the form has a size equal to 0, then the user did not select a profile
            //picture, so do not upload the profile picture; otherwise, upload the new profile picture.
            if (file.getSize() != 0) {
                System.out.println("File is not null, uploading profile picture");
                this.userService.uploadProfilePicture(file);
            }

            //Update the user details obtained from the form
            this.userService.updateUserDetails(user);

            //Return to the profile JSP where the changes will be reflected
            return new RedirectView("/profile", true);
        }
    }

    /*If the user submits the form on the editDetails page using GET, redirect them to a custom 404 error page.
    * This REQUEST will contain the password, first name, and last name parameters.*/
    @RequestMapping(value = "/editDetails", params = {"password", "firstName", "lastName"},
            method = RequestMethod.GET)
    public RedirectView editDetailsError(Map<String, Object> model) {
        return new RedirectView("/error", true);
    }

    /*If the user submits a form with the GET option, redirect them to a custom 404 ERROR page.*/
    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String error(Map<String, Object> model)
    {
        return "error/404error";
    }


    /*Recieve the profile picture uploaded by the user*/
    @RequestMapping(value = "/editProfile", params="editProfilePicture", method = RequestMethod.POST)
    public View editProfilePicture(@RequestParam("profilePicture") MultipartFile file) {
        if (file.isEmpty())
            System.out.println("file is empty");
        else {
            System.out.println("file is not empty");
            this.userService.uploadProfilePicture(file);
        }

        return new RedirectView("/editProfile", true);
    }

    /*When the profile image is displayed on a JSP, the src attribute will have the contents "img-response",
    * and this method will be invoked. This will retrieve the profile picture from the database, and
    * write the byte[] array corresponding to this image to the Response object output stream, so the
    * image may be displayed.*/
    @RequestMapping(value="/img-response", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getImageAsResponseEntity(HttpServletResponse response) throws IOException {
        HttpHeaders httpHeaders = new HttpHeaders();

        //Get the profile picture
        ImageAttachment imageAttachment = this.userService.getProfilePicture();

        if (imageAttachment == null) {
            return null;
        }

        //Set the content type as the image MediaType, the content length as the image size, and
        //then write the byte[] array of the image to the response output stream. Return the
        //ResponseEntity with the byte[] array, HttpHeaders object, and 200 OK status.
        httpHeaders.setContentType(imageAttachment.getMimeContentType());
        httpHeaders.setContentLength(imageAttachment.getSize());
        response.getOutputStream().write(imageAttachment.getContents());

        return new ResponseEntity<byte[]>(imageAttachment.getContents(), httpHeaders, HttpStatus.OK);
    }

    /*Get the ID of the work history row the user wishes to modify, and redirect them to the
    * workHistory page.*/
    @RequestMapping(value = "/editWorkHistory/{workHistoryId}", method = RequestMethod.GET)
    public View editWorkHistory(@PathVariable("workHistoryId") int id, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("workHistoryId", id);

        return new RedirectView("/workHistory", true);
    }

    //Bring the user to the workHistory.jsp page to modify a work history entry; get the ID of the row
    //to be modified from the previous flash attribute placed in the RedirectAttributes object.
    @RequestMapping(value = "/workHistory", method = RequestMethod.GET)
    public String updateWorkHistory(@ModelAttribute("workHistoryId") int id, Map<String, Object> model) {
        if (this.userService.getUser() == null)
            return "redirect:/signIn";
        else {
            //Get the row corresponding to this WorkHistory object from the WORKHISTORY table, using
            //the provided id
            model.put("workHistory", this.userService.getWorkHistoryEntity(id));

            //Go to the workHistory JSP
            return "home/workHistory";
        }
    }

    /*If the user submits the form on the workHistory JSP using the GET request method, redirect them
    * to the custom 404 error page.*/
    @RequestMapping(value = "/workHistory", params = {"jobTitle", "companyName", "yearsOfService"},
            method = RequestMethod.GET)
    public View editWorkHistoryError(Map<String, Object> model)
    {
        return new RedirectView("/error", true);
    }

    /*If the user submits the form on the workHistory JSP using the GET request method, redirect them
     * to the custom 404 error page.*/
    @RequestMapping(value = "/workHistoryAdd", params = {"jobTitle", "companyName", "yearsOfService"},
            method = RequestMethod.GET)
    public View editWorkHistoryAddError(Map<String, Object> model)
    {
        return new RedirectView("/error", true);
    }

    //Submit the user's modified work history entry to the database.
    @RequestMapping(value = "/workHistory", params = "editWorkHistory", method = RequestMethod.POST)
    public RedirectView modifyWorkHistory(WorkHistory workHistory) {
        if (this.userService.getUser() == null)
            return new RedirectView("/signIn", true);
        else {
            //Update this row in the WORKHISTORY table.
            this.userService.updateWorkHistory(workHistory);

            //Go to the profile jsp to reflect the changes
            return new RedirectView("/profile", true);
        }
    }

    //If the user selects 'Add' on the profile page and adds a work history, then add it to the
    //WORKHISTORY table.
    @RequestMapping(value = "/workHistoryAdd", params = "editWorkHistory", method = RequestMethod.POST)
    public RedirectView enterWorkHistory(WorkHistory workHistory) {
        if (this.userService.getUser() == null)
            return new RedirectView("/signIn", true);
        else {
            //Add this entry to the WORKHISTORY table
            this.userService.addWorkHistory(workHistory);
            return new RedirectView("/profile", true);
        }
    }

    //If the user selects 'ADD' on the profile page, then redirect them to workHistory so they may enter
    //a form which will be added to the WORKHISTORY table.
    @RequestMapping(value = "/workHistoryAdd", method = RequestMethod.GET)
    public String addWorkHistory(Map<String, Object> model) {
        if (this.userService.getUser() == null)
            return "redirect:/signIn";
        else {
            //Put an empty WorkHistory object in the model for the form attribute
            model.put("workHistory", new WorkHistory());

            return "home/workHistory";
        }
    }

    /*When the user deletes a work history item, the ID of that item (in the WORKHISTORY table) will
    * be retrieved so it may be deleted*/
    @RequestMapping(value = "/workHistoryDelete/{workHistoryId}", method = RequestMethod.GET)
    public View deleteWorkHistory(@PathVariable("workHistoryId") int id) {
        //Delete the row in the WORKHISTORY table corresponding to this id.
        this.userService.deleteWorkHistory(id);

        return new RedirectView("/profile", true);
    }

    /*Get the ID of the education history row the user wishes to modify, and redirect them to the
     * educationHistory page.*/
    @RequestMapping(value = "/editEducationHistory/{educationHistoryId}", method = RequestMethod.GET)
    public View editEducationHistory(@PathVariable("educationHistoryId") int id,
                                     RedirectAttributes redirectAttributes) {
        //Put the id as a flash attribute, so that the updateEducationHistory() method may retreive it
        //and display it on the educationHistory page
        redirectAttributes.addFlashAttribute("educationHistoryId", id);

        return new RedirectView("/educationHistory", true);
    }

    //Bring the user to the educationHistory.jsp page to modify a work history entry; get the ID of the row
    //to be modified from the previous flash attribute placed in the RedirectAttributes object.
    @RequestMapping(value = "/educationHistory", method = RequestMethod.GET)
    public String updateEducationHistory(@ModelAttribute("educationHistoryId") int id, Map<String, Object> model) {
        if (this.userService.getUser() == null)
            return "redirect:/signIn";
        else {
            //Get the EducationHistory object corresponding to this id from the
            //EDUCATIONHISTORY table and place it in the model for a form attribute
            model.put("educationHistory", this.userService.getEducationHistoryEntity(id));
            return "home/educationHistory";
        }
    }

    /*If the user submits the educationHistory form using GET, redirect them to the custom 404 error page*/
    @RequestMapping(value = "/educationHistory", params = {"degreeType", "degreeDiscipline", "yearAchieved",
            "universityName"}, method = RequestMethod.GET)
    public View editEducationHistoryError(Map<String, Object> model)
    {
        return new RedirectView("/error", true);
    }

    /*If the user submits the educationHistory form using GET, redirect them to the custom 404 error page*/
    @RequestMapping(value = "/educationHistoryAdd", params = {"degreeType", "degreeDiscipline", "yearAchieved",
            "universityName"}, method = RequestMethod.GET)
    public View editEducationHistoryAddError(Map<String, Object> model)
    {
        return new RedirectView("/error", true);
    }

    //Submit the user's modified education history entry to the database.
    @RequestMapping(value = "/educationHistory", params = "editEducationHistory", method = RequestMethod.POST)
    public RedirectView modifyEducationHistory(EducationHistory educationHistory) {
        if (this.userService.getUser() == null)
            return new RedirectView("/signIn", true);
        else {

            //Update the row in the EDUCATIONHISTORY table corresponding to this id.
            this.userService.updateEducationHistory(educationHistory);
            return new RedirectView("/profile", true);
        }
    }

    //If the user selects 'Add' on the profile page and adds a education history, then add it to the
    //EDUCATIONHISTORY table.
    @RequestMapping(value = "/educationHistoryAdd", params = "editEducationHistory", method = RequestMethod.POST)
    public RedirectView enterEducationHistory(EducationHistory educationHistory) {
        if (this.userService.getUser() == null)
            return new RedirectView("/signIn", true);
        else {
            //Add the new education history entity to the EDUCATIONHISTORY table
            this.userService.addEducationHistory(educationHistory);
            return new RedirectView("/profile", true);
        }
    }

    //If the user selects 'ADD' on the profile page, then redirect them to educationHistory so they may enter
    //a form which will be added to the EDUCATIONHISTORY table.
    @RequestMapping(value = "/educationHistoryAdd", method = RequestMethod.GET)
    public String addEducationHistory(Map<String, Object> model) {
        if (this.userService.getUser() == null)
            return "redirect:/signIn";
        else {
            //Put an empty EducationHistory object in the model so it may be used as a form
            model.put("educationHistory", new EducationHistory());
            return "home/educationHistory";
        }
    }

    /*When the user deletes an education history item, the id of that item (in the EDUCATIONHISTORY table)
    * will be retrieved so it may be deleted.*/
    @RequestMapping(value = "/educationHistoryDelete/{educationHistoryId}", method = RequestMethod.GET)
    public View deleteEducationHistory(@PathVariable("educationHistoryId") int id) {
        //Delete the row in the EDUCATIONHISTORY table corresponding to this id
        this.userService.deleteEducationHistory(id);
        return new RedirectView("/profile", true);
    }
}
