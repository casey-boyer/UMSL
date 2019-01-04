package edu.umsl.site.entity;

public class WorkHistory {
    private String id; //The ID of the object in the WORKHISTORY table.
    private String jobTitle;
    private String companyName;
    private String yearsOfService;

    //Empty constructor, for adding a new WorkHistory object
    public WorkHistory(){}

    //Constructor which requires all fields
    public WorkHistory(String id, String jobTitle, String companyName, String yearsOfService) {
        this.id = id;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.yearsOfService = yearsOfService;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getYearsOfService() {
        return yearsOfService;
    }

    public void setYearsOfService(String yearsOfService) {
        this.yearsOfService = yearsOfService;
    }
}
