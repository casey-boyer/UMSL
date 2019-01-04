package edu.umsl.site;

import edu.umsl.site.entity.EducationHistory;
import edu.umsl.site.entity.ImageAttachment;
import edu.umsl.site.entity.User;
import edu.umsl.site.entity.WorkHistory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Repository
public class DefaultUserRepository implements UserRepository {
    @Inject Connection connection;

    //The ID of the user currently signed in
    private int user_id = -1;

    //The MediaType (Image.PNG, Image.JPEG, etc) of the user's profile picture
    private MediaType profilePictureType;

    //Return a User object
    @Override
    public User getUser() {
        User user = new User();
        PreparedStatement ps_user = null;
        ResultSet rs_user = null;

        //Get the username, password, first name, last name, and address id corresponding to this user.
        String get_user = "SELECT LOGON_ID, PASSWORD, FIRST_NAME, LAST_NAME, ADDRESS_ID " +
                "FROM USERS WHERE USERS_ID = ?";

        //If the user is not signed in, then return null.
        if (user_id == -1)
            return null;

        try {
            ps_user = connection.prepareStatement(get_user);
            ps_user.setString(1, String.valueOf(user_id));

            rs_user = ps_user.executeQuery();

            if (rs_user.next()) {
                user.setUsername(rs_user.getString("LOGON_ID"));
                user.setPassword(rs_user.getString("PASSWORD"));
                user.setFirstName(rs_user.getString("FIRST_NAME"));
                user.setLastName(rs_user.getString("LAST_NAME"));

                //Get the address of the user by invoking getAddress() with the current User object
                getAddress(rs_user.getInt("ADDRESS_ID"), user);

                //Get the fields in the USERDESC table by invoking the getUserDesc() method with the
                //user's id and the User object
                getUserDesc(user_id, user);

                //Get the user's profile picture
                if (getProfilePicture() == null)
                    user.setProfilePictureContents(null);
                else {
                    user.setProfilePictureContents("a dummy byte array".getBytes());
                }

                //Return the User object
                return user;
            }
            else //If there are no fields in the USER table, return null
                return null;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int updateUser(User user) {
        //Update the user by updating the row(s) in the USER, ADDRESS, and USERDESC tables
        //corresponding to this User.
        if (updateUsersTable(user) == -1)
            return -1;
        else if (updateAddressTable(user) == -1)
            return -1;
        else if (updateUserDescTable(user) == -1)
            return -1;
        else
            return 0;

    }

    //Update the columns in the USER table corresponding to this user.
    @Override
    public int updateUsersTable(User user) {
        PreparedStatement ps_updateUsersTable = null;

        //Update the PASSWORD, FIRST_NAME, and LAST_NAME columns in the USER table corresponding to this user's
        //id
        String query_updateUsersTable = "UPDATE USERS SET PASSWORD = ?, FIRST_NAME = ?, LAST_NAME = ? WHERE " +
                "USERS_ID = ?";

        try {
            ps_updateUsersTable = connection.prepareStatement(query_updateUsersTable);
            ps_updateUsersTable.setString(1, user.getPassword());
            ps_updateUsersTable.setString(2, user.getFirstName());
            ps_updateUsersTable.setString(3, user.getLastName());
            ps_updateUsersTable.setString(4, String.valueOf(user_id));

            ps_updateUsersTable.executeUpdate();

            return 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error: unable to update users table");
            return -1;
        }
    }

    @Override
    public int updateAddressTable(User user) {
        PreparedStatement ps_updateAddressTable = null;

        //Update the STREET_1, STREET_2, CITY, STATE, and ZIPCODE columns corresponding to the address ID
        //of this user
        String query_updateAddressTable = "UPDATE ADDRESS SET STREET_1 = ?, STREET_2 = ?, CITY = ?, STATE = ?, " +
                "ZIPCODE = ? WHERE ADDRESS_ID = ?";

        //Get the ADDRESS_ID column value corresponding to this user before executing the query
        int address_id = getAddressId();

        try {
            ps_updateAddressTable = connection.prepareStatement(query_updateAddressTable);
            ps_updateAddressTable.setString(1, user.getAddressStreetOne());
            ps_updateAddressTable.setString(2, user.getAddressStreetTwo());
            ps_updateAddressTable.setString(3, user.getCity());
            ps_updateAddressTable.setString(4, user.getState());
            ps_updateAddressTable.setString(5, user.getZipcode());
            ps_updateAddressTable.setString(6, String.valueOf(address_id));

            ps_updateAddressTable.executeUpdate();

            return 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Unable to update address table");
            return -1;
        }
    }

    @Override
    public int updateUserDescTable(User user) {
        PreparedStatement ps_updateUserDescTable = null;

        //Update the BIRTHDAY, PHONE, CELLPHONE, AREAS_OF_INTEREST, and TIME_ZONE columns corresponding to this
        //User's id in the USERDESC table.
        String query_updateUserDescTable = "UPDATE USERDESC SET BIRTHDAY = ?, PHONE = ?, CELLPHONE = ?, " +
                "AREAS_OF_INTEREST = ?, TIME_ZONE = ? WHERE USERS_ID = ?";

        try {
            ps_updateUserDescTable = connection.prepareStatement(query_updateUserDescTable);
            ps_updateUserDescTable.setString(1, user.getBirthday());
            ps_updateUserDescTable.setString(2, user.getPhoneNumber());
            ps_updateUserDescTable.setString(3, user.getCellPhoneNumber());
            ps_updateUserDescTable.setString(4, user.getAreasOfInterest());
            ps_updateUserDescTable.setString(5, user.getTimezone());
            ps_updateUserDescTable.setString(6, String.valueOf(user_id));

            ps_updateUserDescTable.executeUpdate();

            System.out.println("updateUserDescTable: Successfully updated users desc table");

            return 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Unable to update user desc table");
            return -1;
        }
    }

    //Get the row in the ADDRESS table corresponding to this user.
    @Override
    public int getAddress(int id, User user) {
        PreparedStatement ps_address = null;
        ResultSet rs_address = null;

        //Select the STREET_1, STREET_2, CITY, STATE, and ZIPCODE fields from the ADDRESS table where the
        //ADDRESS_ID value is equal to the id argument in this method's parameter list
        String get_address = "SELECT STREET_1, STREET_2, CITY, STATE, ZIPCODE FROM ADDRESS WHERE " +
                "ADDRESS_ID = ?";

        try {
            ps_address = connection.prepareStatement(get_address);
            ps_address.setString(1, String.valueOf(id));
            rs_address = ps_address.executeQuery();


            if(rs_address.next()) {
                user.setAddressStreetOne(rs_address.getString("STREET_1"));
                user.setAddressStreetTwo(rs_address.getString("STREET_2"));
                user.setCity(rs_address.getString("CITY"));
                user.setState(rs_address.getString("STATE"));
                user.setZipcode(rs_address.getString("ZIPCODE"));

                return 1;
            }
            else {
                System.out.println("getAddress(): rs_address.next() did not work");
                return -1;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("repository: getAddress failed");
            return -1;
        }
    }

    @Override
    public int getUserDesc(int id, User user) {
        PreparedStatement ps_getUserDesc = null;
        ResultSet rs_getUserDesc = null;

        //Get the values of the BIRTHDAY, PHONE, CELLPHONE, AREAS_OF_INTEREST, and TIME_ZONE columns
        //from the USERDESC table corrresponding to this user, based on this user's id.
        String query_getUserDesc = "SELECT BIRTHDAY, PHONE, CELLPHONE, AREAS_OF_INTEREST, TIME_ZONE " +
                "FROM USERDESC WHERE USERS_ID = ?";

        try {
            ps_getUserDesc = connection.prepareStatement(query_getUserDesc);
            ps_getUserDesc.setString(1, String.valueOf(user_id));

            rs_getUserDesc = ps_getUserDesc.executeQuery();

            if (rs_getUserDesc.next()) {
                user.setBirthday(rs_getUserDesc.getString("BIRTHDAY"));
                user.setPhoneNumber(rs_getUserDesc.getString("PHONE"));
                user.setCellPhoneNumber(rs_getUserDesc.getString("CELLPHONE"));
                user.setAreasOfInterest(rs_getUserDesc.getString("AREAS_OF_INTEREST"));
                user.setTimezone(rs_getUserDesc.getString("TIME_ZONE"));

                return 1;
            }
            else
                return -1;
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Could not get user details from USERDESC table");
            return -1;
        }
    }

    //Setter method for the user_id variable
    private void setUserId(int id) {
        this.user_id = id;
    }

    //Set the user_id variable when creating or signing-in a user. This variable will be used to keep track of
    //changes made to the various tables in the ENTERPRISE database corresponding to the current user.
    private int getUserId(String username) {
        PreparedStatement ps_id = null;
        ResultSet rs_id = null;

        //Get the USERS_ID column value from the USERS table where the LOGON_ID is equal to the username
        //provided in the method argument
        String getId = "SELECT USERS_ID FROM USERS WHERE LOGON_ID = ?";

        try {
            ps_id = connection.prepareStatement(getId);
            ps_id.setString(1, username);

            rs_id = ps_id.executeQuery();

            if (rs_id.next()) {
                int id = rs_id.getInt("USERS_ID");
                setUserId(id);
                return id;
            }
            else
                return -1;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    //Get the ADDRESS_ID column value of the ADDRESS table corresponding to the current user's id
    private int getAddressId() {
        PreparedStatement ps_addressId = null;
        ResultSet rs_addressId = null;

        //Select the ADDRESS_ID from the USERS table where the USERS_ID column is equal to the user's id
        String query_addressId = "SELECT ADDRESS_ID FROM USERS WHERE USERS_ID = ?";

        try {
            ps_addressId = connection.prepareStatement(query_addressId);
            ps_addressId.setString(1, String.valueOf(user_id));

            rs_addressId = ps_addressId.executeQuery();

            if (rs_addressId.next())
                return rs_addressId.getInt("ADDRESS_ID");
            else
                return -1;
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error: unable to get address id");
            return -1;
        }
    }

    //Verify that the username field exists in the USERS table, and that the password field matches the
    //PASSWORD column in this row of the USERS table
    @Override
    public int signInUser(User user) {
        PreparedStatement ps_verifyPassword = null;
        ResultSet rs_verifyPassword = null;

        //Select the value of the PASSWORD column from the USERS table where the LOGON_ID is equal to the
        //User object's username attribute
        String getUser = "SELECT PASSWORD FROM USERS WHERE LOGON_ID = ?";

        try {
            ps_verifyPassword = connection.prepareStatement(getUser);
            ps_verifyPassword.setString(1, user.getUsername());

            rs_verifyPassword = ps_verifyPassword.executeQuery();

            //If the username exists, check if the provided password is correct.
            if (rs_verifyPassword.next()) {
                String password = rs_verifyPassword.getString("PASSWORD");

                if (password.equals(user.getPassword())) {
                    getUserId(user.getUsername());
                    return 1;
                }
                else
                    return 0;
            }
            else //If the username does not exist, return 0 to indicate no rows were retrieved
                return 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    //Create a row for the new user in the USERS, ADDRESS, and USERDESC table(s) when a user registers.
    @Override
    public int createUser(User user) {
        PreparedStatement ps_address = null;
        PreparedStatement ps_getAddressId = null;
        ResultSet rs_addressId = null;
        PreparedStatement ps_user = null;
        PreparedStatement ps_userDesc = null;
        int address_id = -1;

        /*To insert a row into the USERS table, the ADDRESS_ID of the ADDRESS table must be retrieved
        * first. So, insert the user's address into the ADDRESS table, then retrieve the last inserted
        * ID of the address table (which will correspond to this user's address), and then insert
        * the row into the USERS table with this id.*/
        String insertUser = "INSERT INTO USERS (LOGON_ID, PASSWORD, FIRST_NAME, LAST_NAME, ADDRESS_ID) " +
                "VALUES (?, ?, ?, ?, ?)";
        String insertUserAddress = "INSERT INTO ADDRESS (STREET_1, STREET_2, CITY, STATE, ZIPCODE) " +
                "VALUES (?, ?, ?, ?, ?)";
        String getAddressID = "SELECT LAST_INSERT_ID() as id FROM ADDRESS";

        //Insert a new row into the USERDESC table, and only provide a value for the USERS_ID column, which
        //corresponds to this user's ID. This will be used to update the other column values later if
        //the user desires.
        String insertUserDesc = "INSERT INTO USERDESC (USERS_ID) VALUES(?)";

        //Return 0 to indicate that the row could not be inserted because the username is taken.
        if (checkIfUsernameTaken(user.getUsername()) == 1)
            return 0;

        try {
            ps_address = connection.prepareStatement(insertUserAddress);
            ps_address.setString(1, user.getAddressStreetOne());

            if (user.getAddressStreetTwo() != null)
                ps_address.setString(2, user.getAddressStreetTwo());
            else
                ps_address.setString(2, null);

            ps_address.setString(3, user.getCity());
            ps_address.setString(4, user.getState());
            ps_address.setString(5, user.getZipcode());

            ps_address.executeUpdate();

            ps_getAddressId = connection.prepareStatement(getAddressID);
            rs_addressId = ps_getAddressId.executeQuery();

            if (rs_addressId.next()) {
                address_id = rs_addressId.getInt("id");
            }

            ps_user = connection.prepareStatement(insertUser);
            ps_user.setString(1, user.getUsername());
            ps_user.setString(2, user.getPassword());
            ps_user.setString(3, user.getFirstName());
            ps_user.setString(4, user.getLastName());
            ps_user.setString(5, String.valueOf(address_id));

            ps_user.executeUpdate();

            getUserId(user.getUsername());

            ps_userDesc = connection.prepareStatement(insertUserDesc);
            ps_userDesc.setString(1, String.valueOf(user_id));

            ps_userDesc.executeUpdate();

            return 1;
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("could not insert user");
            return -1;
        }
    }

    //When the user registers, check if the username provided by the user is taken.
    @Override
    public int checkIfUsernameTaken(String username) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        //Select the USERS_ID column from the USERS table where the LOGON_ID column is equal to the username
        //provided.
        String getUser = "SELECT USERS_ID FROM USERS WHERE LOGON_ID = ?";

        try {
            ps = connection.prepareStatement(getUser);
            ps.setString(1, username);

            rs = ps.executeQuery();

            //If there were any rows returned, return 1 to indicate this username is taken; otherwise,
            //return 0 to indicate that this username is available.
            if (rs.next())
                return 1;
            else
                return 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    //Delete a row from the WORKHISTORY table based on the id in the argument
    @Override
    public int deleteWorkHistory(int id) {
        PreparedStatement ps_deleteWorkHistoryObj = null;

        String deleteWorkHistoryObj = "DELETE FROM WORKHISTORY WHERE WORKHISTORY_ID = ?";

        try {
            ps_deleteWorkHistoryObj = connection.prepareStatement(deleteWorkHistoryObj);
            ps_deleteWorkHistoryObj.setString(1, String.valueOf(id));

            ps_deleteWorkHistoryObj.executeUpdate();

            System.out.println("Successfully deleted row");

            return 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println ("could not delete work history row");
            return -1;
        }
    }

    //Delete a row from the EDUCATIONHISTORY table based on the id from the method argument
    @Override
    public int deleteEducationHistory(int id) {
        PreparedStatement ps_deleteEducationHistory = null;

        String deleteEducationHistoryObj = "DELETE FROM EDUCATIONHISTORY WHERE EDUCATIONHISTORY_ID = ?";

        try {
            ps_deleteEducationHistory = connection.prepareStatement(deleteEducationHistoryObj);
            ps_deleteEducationHistory.setString(1, String.valueOf(id));

            ps_deleteEducationHistory.executeUpdate();

            System.out.println("Successfully deleted education history");

            return 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Could not delete education history");
            return -1;
        }
    }

    //Update a row in the WORKHISTORY table and modify the column values to match the attributes of
    //the WORKHISTORY object in the method argument.
    @Override
    public int updateWorkHistory(WorkHistory workHistory) {
        PreparedStatement ps_updateWorkHistory = null;

        String query_updateWorkHistory = "UPDATE WORKHISTORY SET JOB_TITLE = ?, COMPANY_NAME = ?, " +
                "YEARS_OF_SERVICE = ? WHERE WORKHISTORY_ID = ? AND USERS_ID = ?";

        try {
            ps_updateWorkHistory = connection.prepareStatement(query_updateWorkHistory);
            ps_updateWorkHistory.setString(1, workHistory.getJobTitle());
            ps_updateWorkHistory.setString(2, workHistory.getCompanyName());
            ps_updateWorkHistory.setString(3, workHistory.getYearsOfService());
            ps_updateWorkHistory.setString(4, workHistory.getId());
            ps_updateWorkHistory.setString(5, String.valueOf(user_id));

            ps_updateWorkHistory.executeUpdate();

            return 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("could not update work history object");
            return -1;
        }
    }

    //Update a row in the EDUCATIONHISTORY table and modify the column values to match the attributes of
    //the EDUCATIONHISTORY object in the method argument.
    @Override
    public int updateEducationHistory(EducationHistory educationHistory) {
        PreparedStatement ps_updateEducationHistory = null;

        String query_updateEducationHistory = "UPDATE EDUCATIONHISTORY SET DEGREE_TYPE = ?, DEGREE_DISCIPLINE = ?, " +
                "YEAR_ACHIEVED = ?, UNIVERSITY_NAME = ? WHERE EDUCATIONHISTORY_ID = ? AND USERS_ID = ?";

        try {
            ps_updateEducationHistory = connection.prepareStatement(query_updateEducationHistory);
            ps_updateEducationHistory.setString(1, educationHistory.getDegreeType());
            ps_updateEducationHistory.setString(2, educationHistory.getDegreeDiscipline());
            ps_updateEducationHistory.setString(3, educationHistory.getYearAchieved());
            ps_updateEducationHistory.setString(4, educationHistory.getUniversityName());
            ps_updateEducationHistory.setString(5, educationHistory.getId());
            ps_updateEducationHistory.setString(6, String.valueOf(user_id));

            ps_updateEducationHistory.executeUpdate();

            System.out.println("Updated education history");

            return 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Could not update education history");
            return -1;
        }
    }

    //Retrieve all rows from the WORKHISTORY table corresponding to the current user's id.
    @Override
    public ArrayList<WorkHistory> getWorkHistory() {
        PreparedStatement ps_getWorkHistory = null;
        ResultSet rs_getWorkHistory = null;

        String query_workHistory = "SELECT * FROM WORKHISTORY WHERE USERS_ID = ?";
        ArrayList<WorkHistory> workHistoryArrayList = new ArrayList<>();

        try {
            ps_getWorkHistory = connection.prepareStatement(query_workHistory);
            ps_getWorkHistory.setString(1, String.valueOf(user_id));

            rs_getWorkHistory = ps_getWorkHistory.executeQuery();

            while (rs_getWorkHistory.next()) {
                WorkHistory workHistory = new WorkHistory();
                workHistory.setId(String.valueOf(rs_getWorkHistory.getInt("WORKHISTORY_ID")));
                workHistory.setJobTitle(rs_getWorkHistory.getString("JOB_TITLE"));
                workHistory.setCompanyName(rs_getWorkHistory.getString("COMPANY_NAME"));
                workHistory.setYearsOfService(rs_getWorkHistory.getString("YEARS_OF_SERVICE"));
                workHistoryArrayList.add(workHistory);
            }

            System.out.println("work history records are: " + workHistoryArrayList.size());

            return workHistoryArrayList;
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Could not get work history");
            return null;
        }
    }

    //Get a specific row from the WORKHISTORY table based on the ID provided in the method argument.
    @Override
    public WorkHistory getWorkHistoryEntity(int id) {
        PreparedStatement ps_getWorkHistory = null;
        ResultSet rs_getWorkHistory = null;

        String query_getWorkHistory = "SELECT * FROM WORKHISTORY WHERE USERS_ID = ? AND WORKHISTORY_ID = ?";

        try {
            ps_getWorkHistory = connection.prepareStatement(query_getWorkHistory);
            ps_getWorkHistory.setString(1, String.valueOf(user_id));
            ps_getWorkHistory.setString(2, String.valueOf(id));

            rs_getWorkHistory = ps_getWorkHistory.executeQuery();

            if (rs_getWorkHistory.next()) {
                WorkHistory workHistory = new WorkHistory();
                workHistory.setId(rs_getWorkHistory.getString("WORKHISTORY_ID"));
                workHistory.setJobTitle(rs_getWorkHistory.getString("JOB_TITLE"));
                workHistory.setCompanyName(rs_getWorkHistory.getString("COMPANY_NAME"));
                workHistory.setYearsOfService(rs_getWorkHistory.getString("YEARS_OF_SERVICE"));

                System.out.println("got work history object");
                return workHistory;
            }
            else {
                System.out.println("unable to get work history object");
                return null;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("could not get work history");
            return null;
        }
    }

    //Get a specific row from the EDUCATIONHISTORY table based on the ID provided in the method argument.
    @Override
    public EducationHistory getEducationHistoryEntity(int id) {
        PreparedStatement ps_getEducationHistory = null;
        ResultSet rs_getEducationHistory = null;

        String query_getEducationHistory = "SELECT * FROM EDUCATIONHISTORY WHERE USERS_ID = ? " +
                "AND EDUCATIONHISTORY_ID = ?";

        try {
            ps_getEducationHistory = connection.prepareStatement(query_getEducationHistory);
            ps_getEducationHistory.setString(1, String.valueOf(user_id));
            ps_getEducationHistory.setString(2, String.valueOf(id));

            rs_getEducationHistory = ps_getEducationHistory.executeQuery();

            if (rs_getEducationHistory.next()) {
                EducationHistory educationHistory = new EducationHistory();
                educationHistory.setId(rs_getEducationHistory.getString("EDUCATIONHISTORY_ID"));
                educationHistory.setDegreeType(rs_getEducationHistory.getString("DEGREE_TYPE"));
                educationHistory.setDegreeDiscipline(rs_getEducationHistory
                        .getString("DEGREE_DISCIPLINE"));
                educationHistory.setYearAchieved(rs_getEducationHistory
                        .getString("YEAR_ACHIEVED"));
                educationHistory.setUniversityName(rs_getEducationHistory.
                        getString("UNIVERSITY_NAME"));

                System.out.println("got education history object");
                return educationHistory;
            }
            else {
                System.out.println("unable to get education history object");
                return null;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("could not get education history");
            return null;
        }
    }

    //Add a new row to the WORKHISTORY table, and populate the columns with the values of the WorkHistory
    //object attributes.
    @Override
    public int addWorkHistory(WorkHistory workHistory) {
        PreparedStatement ps_addWorkHistory = null;

        String query_addWorkHistory = "INSERT INTO WORKHISTORY (USERS_ID, JOB_TITLE, COMPANY_NAME, " +
                "YEARS_OF_SERVICE) VALUES(?, ?, ?, ?)";

        try {
            ps_addWorkHistory = connection.prepareStatement(query_addWorkHistory);
            ps_addWorkHistory.setString(1, String.valueOf(user_id));
            ps_addWorkHistory.setString(2, workHistory.getJobTitle());
            ps_addWorkHistory.setString(3, workHistory.getCompanyName());
            ps_addWorkHistory.setString(4, workHistory.getYearsOfService());

            System.out.println("added work history");

            ps_addWorkHistory.executeUpdate();

            return 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Could not add work history");
            return -1;
        }
    }

    //Add a new row to the EDUCATIONHISTORY table, and populate the columns with the values of the EducationHistory
    //object attributes.
    @Override
    public int addEducationHistory(EducationHistory educationHistory) {
        PreparedStatement ps_addEducationHistory = null;

        String query_addEducationHistory = "INSERT INTO EDUCATIONHISTORY (USERS_ID, DEGREE_TYPE, " +
                "DEGREE_DISCIPLINE, YEAR_ACHIEVED, UNIVERSITY_NAME) VALUES (?, ?, ?, ?, ?)";

        try {
            ps_addEducationHistory = connection.prepareStatement(query_addEducationHistory);
            ps_addEducationHistory.setString(1, String.valueOf(user_id));
            ps_addEducationHistory.setString(2, educationHistory.getDegreeType());
            ps_addEducationHistory.setString(3, educationHistory.getDegreeDiscipline());
            ps_addEducationHistory.setString(4, educationHistory.getYearAchieved());
            ps_addEducationHistory.setString(5, educationHistory.getUniversityName());

            ps_addEducationHistory.executeUpdate();

            System.out.println("Successfully added education history to db");
            return 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Could not add education history to database");
            return -1;
        }
    }

    //Retrieve all rows from the EDUCATIONHISTORY table corresponding to the current user's id.
    @Override
    public ArrayList<EducationHistory> getEducationHistory() {
        PreparedStatement ps_getEducationHistory = null;
        ResultSet rs_getEducationHistory = null;

        String query_getEducationHistory = "SELECT * FROM EDUCATIONHISTORY WHERE USERS_ID = ?";
        ArrayList<EducationHistory> educationHistoryArrayList = new ArrayList<>();

        try {
            ps_getEducationHistory = connection.prepareStatement(query_getEducationHistory);
            ps_getEducationHistory.setString(1, String.valueOf(user_id));

            rs_getEducationHistory = ps_getEducationHistory.executeQuery();

            while (rs_getEducationHistory.next()) {
                EducationHistory educationHistory = new EducationHistory();

                educationHistory.setId(rs_getEducationHistory.getString("EDUCATIONHISTORY_ID"));
                educationHistory.setDegreeType(rs_getEducationHistory.getString("DEGREE_TYPE"));
                educationHistory.setDegreeDiscipline(rs_getEducationHistory.
                        getString("DEGREE_DISCIPLINE"));
                educationHistory.setYearAchieved(rs_getEducationHistory.getString("YEAR_ACHIEVED"));
                educationHistory.setUniversityName(rs_getEducationHistory.getString("UNIVERSITY_NAME"));

                educationHistoryArrayList.add(educationHistory);
            }

            System.out.println("Records retrieved from education history are " + educationHistoryArrayList.size());

            return educationHistoryArrayList;
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Could not get education history");
            return null;
        }
    }

    //Upload a profile picture to the USERDESC table corresponding to the USERS_ID column with the value
    //of the current user's id.
    @Override
    public int uploadProfilePicture(MultipartFile profilePicture) {
        PreparedStatement ps_profilePicture = null;

        //Get the row of the current user, and update their profile image.
        String profilePicture_query = "UPDATE USERDESC SET PROFILE_IMAGE = ?, PROFILE_IMAGE_TYPE = ? " +
                "WHERE USERS_ID = ?";

        try {
            ps_profilePicture = connection.prepareStatement(profilePicture_query);

            //Get the MultipartFile object as an InputStream object so it may be uploaded to the database
            //as a BLOB.
            InputStream inputStream = profilePicture.getInputStream();
            ps_profilePicture.setBlob(1, inputStream);
            ps_profilePicture.setString(2, profilePicture.getContentType());
            ps_profilePicture.setString(3, String.valueOf(user_id));

            ps_profilePicture.executeUpdate();

            //Set the profilePictureType, so this may be used when retrieving the profile picture
            //form the database.
            profilePictureType = MediaType.valueOf(profilePicture.getContentType());

            return 1;
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Could not upload profile picture");
            return -1;
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("could not upload picture; input stream error");
            return -1;
        }
    }

    //Get the PROFILE_IMAGE and PROFILE_IMAGE_TYPE values from the USERDESC table corresponding to the
    //current user's id. Return an ImageAttachment object, so that values such as MediaType and the
    //byte[] array do not get lost when the method ends.
    @Override
    public ImageAttachment getProfilePicture() {
        PreparedStatement ps_getProfilePicture = null;
        ResultSet rs_getProfilePicture = null;

        String query = "SELECT PROFILE_IMAGE, PROFILE_IMAGE_TYPE FROM USERDESC WHERE USERS_ID = ?";

        try {
            ps_getProfilePicture = connection.prepareStatement(query);
            ps_getProfilePicture.setString(1, String.valueOf(user_id));

            rs_getProfilePicture = ps_getProfilePicture.executeQuery();

            if (rs_getProfilePicture.next()) {
                //Get the byte[] array from the LONGBLOB PROFILE_IMAGE column.
                byte[] byte_array = rs_getProfilePicture.getBytes("PROFILE_IMAGE");

                //If the user has already uploaded a profile picture (but perhaps the application has
                //been redeployed and they are signing in again), set the profilePictureType variable.
                if (profilePictureType == null && byte_array != null) {
                    profilePictureType = MediaType.valueOf(rs_getProfilePicture.
                            getString("PROFILE_IMAGE_TYPE"));
                }
                else if (profilePictureType == null) {
                    return null;
                }
                else if (byte_array == null) {
                    return null;
                }

                //Create the ImageAttachment object and return it.
                ImageAttachment imageAttachment =
                        new ImageAttachment(profilePictureType, byte_array.length, byte_array);

                return imageAttachment;
            }
            else
                return null;
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Could not get image from database; SQL error");
            return null;
        }
    }
}
