package edu.umsl.site.form;

import edu.umsl.site.entity.EducationHistory;

import java.util.ArrayList;
import java.util.List;

/*This class will be used to get all of the rows from the EDUCATIONHISTORY table corresponding to
* the user.*/
public class EducationHistoryForm {
    private List<EducationHistory> educationHistoryList;

    public EducationHistoryForm() {
        this.educationHistoryList = new ArrayList<>();
    }

    public List<EducationHistory> getEducationHistoryList() {
        return educationHistoryList;
    }

    public void setEducationHistoryList(List<EducationHistory> educationHistoryList) {
        this.educationHistoryList = educationHistoryList;
    }
}
