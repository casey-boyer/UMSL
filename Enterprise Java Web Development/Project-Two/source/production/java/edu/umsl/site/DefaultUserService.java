package edu.umsl.site;

import edu.umsl.site.entity.EducationHistory;
import edu.umsl.site.entity.ImageAttachment;
import edu.umsl.site.entity.User;
import edu.umsl.site.entity.WorkHistory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.inject.Inject;
import java.util.ArrayList;

@Service
public class DefaultUserService implements UserService {
    @Inject UserRepository userRepository;
    private User user = new User();

    //Sign in the user, and return an error message if the username or password is incorrect
    @Override
    public String signInUser(User user) {
        int result = userRepository.signInUser(user);
        String errorMessage;

        if (result == 0) {
            errorMessage = "The username or password is incorrect.";
            return errorMessage;
        }
        else if (result == 1) {

            return null;
        }
        else {
            errorMessage = "An unexpected error occurred trying to retrieve the user data.";
            return errorMessage;
        }
    }

    /*Retrieve the user data from the repository and check to see if a user with this
    * username exists. This method is invoked when registering a user. Return an error message if
    * the user could not be registered.*/
    @Override
    public String saveUserData(User user) {
        int result = userRepository.createUser(user);
        String errorMessage;

        if (result == 1)
            return null;
        else if (result == 0) {
            errorMessage = "This username is already taken.";
            return errorMessage;
        }
        else {
            errorMessage = "An unexpected error occurred trying to insert the user data into the" +
                    " database";
            return errorMessage;
        }
    }

    //Get a User object from the USER, ADDRESS, and USERDESC tables
    @Override
    public User getUser() {
        return this.userRepository.getUser();
    }

    //Update the USER, ADDRESS, and USERDESC tables
    @Override
    public int updateUserDetails(User user) {
        int result = this.userRepository.updateUser(user);
        return result;
    }

    //Delete the row with the specified id in the WORKHISTORY table.
    @Override
    public int deleteWorkHistory(int id) {
        return this.userRepository.deleteWorkHistory(id);
    }

    //Delete the row with the specified id in the EDUCATIONHISTORY table.
    @Override
    public int deleteEducationHistory(int id) {
        return this.userRepository.deleteEducationHistory(id);
    }

    //Update a row in the WORKHISTORY table based on the WorkHistory object's id
    @Override
    public int updateWorkHistory(WorkHistory workHistory) {
        return this.userRepository.updateWorkHistory(workHistory);
    }

    //Update a row in the EDUCATIONHISTORY table based on the EducationHistory object's id
    @Override
    public int updateEducationHistory(EducationHistory educationHistory) {
        return this.userRepository.updateEducationHistory(educationHistory);
    }

    //Return all the rows in the WORKHISTORY table as an ArrayList.
    @Override
    public ArrayList<WorkHistory> getWorkHistory() {
        return this.userRepository.getWorkHistory();
    }

    //Return a specific row from the WORKHISTORY table with the specified ID.
    @Override
    public WorkHistory getWorkHistoryEntity(int id) {
        return this.userRepository.getWorkHistoryEntity(id);
    }

    //Return a specific row from the EDUCATIONHISTORY table with the specified ID.
    @Override
    public EducationHistory getEducationHistoryEntity(int id) {
        return this.userRepository.getEducationHistoryEntity(id);
    }

    //Return all the rows in the EDUCATIONHISTORY table.
    @Override
    public ArrayList<EducationHistory> getEducationHistory() {
        return this.userRepository.getEducationHistory();
    }

    //Upload the user's profile picture.
    @Override
    public int uploadProfilePicture(MultipartFile profilePicture) {
        this.userRepository.uploadProfilePicture(profilePicture);
        return 0;
    }

    //Get the user's profile picture from the USERDESC table.
    @Override
    public ImageAttachment getProfilePicture() {
        return this.userRepository.getProfilePicture();
    }

    //Add a new work history field.
    @Override
    public int addWorkHistory(WorkHistory workHistory) {
        return this.userRepository.addWorkHistory(workHistory);
    }

    //Add a new education history field.
    @Override
    public int addEducationHistory(EducationHistory educationHistory) {
        return this.userRepository.addEducationHistory(educationHistory);
    }
}
