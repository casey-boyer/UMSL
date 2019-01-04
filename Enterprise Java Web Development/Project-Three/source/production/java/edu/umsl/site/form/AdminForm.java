package edu.umsl.site.form;

/*This form verifies that the user attempting to delete another user has admin privileges.*/
public class AdminForm {
    private String adminUsername;
    private String adminPassword;

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    //Verify that the user has the correct admin privileges. The adminUsername and adminPassword must
    //equal "admin".
    public Boolean testIfAdmin() {
        if ((this.adminUsername.equals("admin")) && (this.adminPassword.equals("admin")))
            return true;
        else
            return false;
    }
}
