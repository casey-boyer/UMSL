package edu.umsl.site.form;

import edu.umsl.site.entity.WorkHistory;

import java.util.ArrayList;
import java.util.List;

/*This class will be used to get all of the rows from the WORKHISTORY table corresponding to
 * the user.*/
public class WorkHistoryForm {
    private List<WorkHistory> workHistoryList;

    public WorkHistoryForm() {
        workHistoryList = new ArrayList<>();
    }

    public List<WorkHistory> getWorkHistoryList() {
        return workHistoryList;
    }

    public void setWorkHistoryList(List<WorkHistory> workHistoryList) {
        this.workHistoryList = workHistoryList;
    }
}
