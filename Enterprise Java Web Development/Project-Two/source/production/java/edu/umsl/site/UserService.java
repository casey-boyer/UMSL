package edu.umsl.site;

import edu.umsl.site.entity.EducationHistory;
import edu.umsl.site.entity.ImageAttachment;
import edu.umsl.site.entity.User;
import edu.umsl.site.entity.WorkHistory;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;

public interface UserService {
    //For signing in a user; the return value will be an error message if an error occurred or NULL if
    //successful
    String signInUser(User user);

    //For registering a user; the return value will be an error message if an error occurred or NULL if
    //successful
    String saveUserData(User user);

    //Get the current user information
    User getUser();

    //Update the user's current information in the user database
    int updateUserDetails(User user);

    //Delete a row from the WORKHISTORY table
    int deleteWorkHistory(int id);

    //Delete a row from the EDUCATIONHISTORY table
    int deleteEducationHistory(int id);

    //Update a row in the WORKHISTORY table
    int updateWorkHistory(WorkHistory workHistory);

    //Update a row in the EDUCATIONHISTORY table
    int updateEducationHistory(EducationHistory educationHistory);

    //Get all the rows from the WORKHISTORY table for the current user
    ArrayList<WorkHistory> getWorkHistory();

    //Get a specific row from the WORKHISTORY table for the current user based on the id
    WorkHistory getWorkHistoryEntity(int id);

    //Get a specific row from the EDUCATIONHISTORY table for the current user based on the id
    EducationHistory getEducationHistoryEntity(int id);

    //Get all the rows from the EDUCATIONHISTORY table for the current user
    ArrayList<EducationHistory> getEducationHistory();

    //Upload the user's profile picture to the database
    int uploadProfilePicture(MultipartFile profilePicture);

    //Get the user's profile pictre from the database
    ImageAttachment getProfilePicture();

    //Add a row to the WORKHISTORY table for the current user
    public int addWorkHistory(WorkHistory workHistory);

    //Add a row to the EDUCATIONHISTORY table for the current user
    public int addEducationHistory(EducationHistory educationHistory);
}
