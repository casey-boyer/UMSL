package edu.umsl.site.entity;

public class EducationHistory {
    private String id; //ID in the EDUCATIONHISTORY table
    private String degreeType;
    private String degreeDiscipline;
    private String yearAchieved;
    private String universityName;

    //Empty constructor
    public EducationHistory(){}

    //Constructor which initializes all fields
    public EducationHistory(String id, String degreeType, String degreeDiscipline, String yearAchieved,
                            String universityName) {
        this.id = id;
        this.degreeType = degreeType;
        this.degreeDiscipline = degreeDiscipline;
        this.yearAchieved = yearAchieved;
        this.universityName = universityName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDegreeType() {
        return degreeType;
    }

    public void setDegreeType(String degreeType) {
        this.degreeType = degreeType;
    }

    public String getDegreeDiscipline() {
        return degreeDiscipline;
    }

    public void setDegreeDiscipline(String degreeDiscipline) {
        this.degreeDiscipline = degreeDiscipline;
    }

    public String getYearAchieved() {
        return yearAchieved;
    }

    public void setYearAchieved(String yearAchieved) {
        this.yearAchieved = yearAchieved;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }
}
