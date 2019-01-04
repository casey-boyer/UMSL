package edu.umsl.site;

import edu.umsl.site.entity.EducationHistory;
import edu.umsl.site.entity.ImageAttachment;
import edu.umsl.site.entity.User;
import edu.umsl.site.entity.WorkHistory;
import edu.umsl.site.form.WorkHistoryForm;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

public interface UserRepository {
    //Get the rows from EDUCATIONHISTORY and WORKHISTORY for the current user, as well as the
    //row from the USER, USERDESC, and ADDRESS table for the current user. Use the results to return a
    //User object.
    User getUser();

    //Update the row in the ADDRESS, USER, and USERDESC table corresponding to the specific user.
    int updateUser(User user);

    //Update the row in the USER table corresponding to the specific user.
    int updateUsersTable(User user);

    //Update the row in the ADDRESS table corresponding to the specific user.
    int updateAddressTable(User user);

    //Update the row in the USERDESC table corresponding to the specific user.
    int updateUserDescTable(User user);

    //Get the row from the ADDRESS table corresponding to the specific user.
    int getAddress(int id, User user);

    //Get the row from the USERDESC table corresponding to the specific user.
    int getUserDesc(int id, User user);

    //Verify the username and password for the user trying to sign in by matching the user.password and
    //user.username fields to the corresponding row in the USER table.
    int signInUser(User user);

    //Create a new user when a user registers in the USER, ADDRESS, and USERDESC table
    int createUser(User user);

    //Check if the username desired by the user is already taken when the user registers
    int checkIfUsernameTaken(String username);

    //Upload the user's profile picture to the USERDESC table
    int uploadProfilePicture(MultipartFile profilePicture);

    //Delete a row from the WORKHISTORY table corresponding to the specific row ID and user
    int deleteWorkHistory(int id);

    //Delete a row form the EDUCATIONHISTORY table corresponding to the speific row ID and user
    int deleteEducationHistory(int id);

    //Update a row in the WORKHISTORY table corresponding to the id attribute of the WorkHistory object
    int updateWorkHistory(WorkHistory workHistory);

    //Update a row in the EDUCATIONHISTORY table corresponding to the id attribute of the EducationHistory
    //object
    int updateEducationHistory(EducationHistory educationHistory);

    //Add a row to the WORKHISTORY table, using the fields of the WorkHistory object, based on the current user
    int addWorkHistory(WorkHistory workHistory);

    //Add a row to the EDUCATIONHISTORY table, using the fields of the EducationHistoyr object, based on the
    //current user
    int addEducationHistory(EducationHistory educationHistory);

    //Get all the rows from the WORKHISTORY table for a specific user
    ArrayList<WorkHistory> getWorkHistory();

    //Get a specific row from the WORKHISTORY table and return a WorkHistory object
    WorkHistory getWorkHistoryEntity(int id);

    //Get a specific row from the EDUCATIONHISTORY table and return a EducationHistory object
    EducationHistory getEducationHistoryEntity(int id);

    //Get all the rows from the EDUCATIONHISTORY table for a specific user
    ArrayList<EducationHistory> getEducationHistory();

    //Get the profile picture corresponding to the specific user from the USERDESC table
    ImageAttachment getProfilePicture();
}
