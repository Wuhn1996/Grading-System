package BackEnd;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * Created by wangkaihong on 2019/4/9.
 */
public class Report {
    /*
    * Return statistic data of 1-each assignment, 2-each course to Report_UI
    * including Min, Max, Average(lowest and highest score removed), Median
    */
    private String[][] content;
    private String time;

    public Report() {
        content = new String[4][2];
        time = Calendar.getInstance().getTime().toString();
    }
    public Report(String[][] content){
        this.content = content;
        time = Calendar.getInstance().getTime().toString();
    }

    public String[][] getContent() {
        return content;
    }

    public void setContent(String[][] content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
