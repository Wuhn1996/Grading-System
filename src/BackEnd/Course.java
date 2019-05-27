package BackEnd;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Created by wangkaihong on 2019/4/9.
 */
public class Course implements Reportable {
    //Course name cannot be changed
    private String courseName;
    private String lecturerName;
    private String semester;
    private Sheet sheet;
    private ArrayList<Student> students;
    private ArrayList<Assignment> assignments;
    private Criteria criteria_UG;
    private Criteria criteria_G;
    private boolean end;
    private boolean show_Total;
    private Extra_credit extra_credits;

    public Course() {
        this.courseName = "";
        this.lecturerName = "";
        this.semester = "";
        this.sheet = new Sheet();
        this.students = new ArrayList<>();
        this.assignments = new ArrayList<>();
        this.criteria_UG = null;
        this.criteria_G = null;
        this.end = false;
        show_Total = false;
        this.extra_credits = new Extra_credit();
    }
    public Course(String cN, String lN, String s, Sheet sh, ArrayList<Student> stu, ArrayList<Assignment> assign, Criteria c_ug, Criteria c_g, boolean end, Extra_credit extra, boolean show_total){
        courseName = cN;
        lecturerName = lN;
        semester = s;
        sheet = sh;
        students = stu;
        assignments = assign;
        criteria_UG = c_ug;
        criteria_G = c_g;
        this.end = end;
        this.extra_credits = extra;
        show_Total = show_total;
    }


    public Course(String courseName, String lecturerName, String semester, ArrayList<Student> student_list, Course previous) {
        this.courseName = courseName;
        this.lecturerName = lecturerName;
        this.semester = semester;
        this.sheet = new Sheet(); // todo
        this.students = student_list;
        this.sheet.addRows(student_list.size());
        if(previous == null) {
            this.assignments = new ArrayList<Assignment>();
            this.criteria_UG = new Criteria();
            this.criteria_G = new Criteria();
        }
        else { // deep copy
            this.assignments = new ArrayList<>();
            for(int i = 0; i < previous.assignments.size(); i++) {
                assignments.add(new Assignment(previous.assignments.get(i)));
            }
            for(int i = 0; i < assignments.size(); i++) {
                if(assignments.get(i).getScoring_method().equals("deduction")) {
                    this.sheet.addColumns(1, 1);
                }
                else {
                    this.sheet.addColumns(1, 0);
                }
            }
            this.criteria_UG = new Criteria(previous.criteria_UG);
            this.criteria_G = new Criteria(previous.criteria_G);
        }
        this.end = false;
        this.show_Total = false;
        this.extra_credits = new Extra_credit();
    }


    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }

    public ArrayList<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(ArrayList<Assignment> assignments) {
        this.assignments = assignments;
    }

    public Criteria getCriteria_UG() {
        return criteria_UG;
    }

    public Criteria getCriteria_G() {
        return criteria_G;
    }

    public void setCriteria_UG(Criteria criteria_UG) {
        this.criteria_UG = criteria_UG;
    }

    public void setCriteria_G(Criteria criteria_G) {
        this.criteria_G = criteria_G;
    }
    //Add get extra_credit
    public Extra_credit getExtra_credits() {
        return extra_credits;
    }

    public boolean isShow_Total() {
        return show_Total;
    }

    public void setShow_Total(boolean show_Total) {
        this.show_Total = show_Total;
    }

    public void setExtra_credits(Extra_credit extra_credits) {
        this.extra_credits = extra_credits;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public int addStudent(String firstName, String middleInitial, String lastName, String studentId, String emailAddress, String studentType) {
        // parameter:
        // firstName: String: First name of student
        // middleInitial: String: middleInitial of student
        // lastName: String: Last name of student
        // studentId: String: ID of student
        // emailAddress: String: Email address of student
        // studentType: String: Undergraduate or graduate
        //
        // return 1 if succeeded, return 2 if invalid firstName, return 3 if invalid middleInitial, return 4 if invalid lastName,
        // return 5 if invalid studentId, return 6 if invalid emailAddress, return 7 if invalid studentType,
        // return 8 if course is ended, return 9 if unknown error
        if(end) {
            return 8;
        }
        if(firstName == null || firstName.equals("")) {
            return 2;
        }
//        if(middleInitial == null || middleInitial.equals("")) {
//            return 3;
//        }
        if(lastName == null || lastName.equals("")) {
            return 4;
        }
        if(studentId == null || studentId.equals("")) {
            return 5;
        }
        if(emailAddress == null || emailAddress.equals("")) {
            return 6;
        }
        if(studentType == null || studentType.equals("")) {
            return 7;
        }
        try {
            if(studentType.equals("undergraduate")) {
                Student student = new Undergraduate(firstName, middleInitial, lastName, studentId, emailAddress, false);
                students.add(student);
                if(extra_credits.getExtra_credits() != null) {
                    extra_credits.add_one();
                }
                sheet.addRows(1);
                for(int i = 0; i < assignments.size(); i++) {
                    if(assignments.get(i).getScoring_method().equals("deduction")) {
                        sheet.getAllCell().get(sheet.getAllCell().size() - 1).get(i + 2).setScore(1);
                    }
                }
                FileIO fileIO = new FileIO();
                fileIO.writeCell(sheet.getAllCell(),courseName+semester);
                fileIO.writeStudentInfo(students,courseName+semester);

                return 1;
            }
            if(studentType.equals("graduate")) {
                Student student = new Graduate(firstName, middleInitial, lastName, studentId, emailAddress,false);
                students.add(student);
                if(extra_credits.getExtra_credits() != null) {
                    extra_credits.add_one();
                }
                sheet.addRows(1);
                for(int i = 0; i < assignments.size(); i++) {
                    if(assignments.get(i).getScoring_method().equals("deduction")) {
                        sheet.getAllCell().get(sheet.getAllCell().size() - 1).get(i + 2).setScore(1);
                    }
                }
                FileIO fileIO = new FileIO();
                fileIO.writeStudentInfo(students,courseName+semester);
                return 1;
            }
            else {
                return 7;
            }

        }
        catch (Exception e) {
            return 9;
        }
    }
    public int removeStudent(int index) {
        // parameters:
        // index: int: index of student in the arraylist you would like to remove
        //
        // return 1 if succeeded, return 2 if succeeded, return 3 if student not found, return 4 if course is ended, return 5 if unknown error
        if(end) {
            return 4;
        }
        try {
            if (index >= students.size() || index < 0) {
                return 3;
            } else {
                boolean examed = false;
                ArrayList<Double> row = sheet.getScoreRow(index + 1);
                for(int i = 0; i < assignments.size(); i++) {
                    if(assignments.get(i) instanceof Exam && row.get(i + 2) > 0) {
                        examed = true;
                    }
                }
                if(examed) {
                    students.get(index).setRemovedAfterExam(true);
                    FileIO fileIO = new FileIO();
                    fileIO.writeCell(sheet.getAllCell(),courseName+semester);
                    fileIO.writeStudentInfo(students,courseName+semester);
                    return 2;
                }
                else {
                    students.remove(index);
                    if (extra_credits.getExtra_credits() != null) {
                        extra_credits.remove(index);
                    }
                    sheet.removeRow(index + 1);
                    FileIO fileIO = new FileIO();
                    fileIO.writeCell(sheet.getAllCell(),courseName+semester);
                    fileIO.writeStudentInfo(students,courseName+semester);
                    return 1;
                }
            }
        }
        catch (Exception e) {
            return 5;
        }
    }
    public int addAssignment(String name, double total, String scoring_method, boolean is_exam) {
        // parameters:
        // name: String: name of the assignment
        // total: double: total score of the assignment
        // scoring_method: String: scoring method of the assignment
        //
        // return 1 if succeeded, return 2 if invalid name, return 3 if invalid total,
        // return 4 if invalid scoring_method, return 5 if course is ended, return 6 if unknown error
        if(end) {
            return 5;
        }
        try {
            if (name == null || name.equals("")) {
                return 2;
            }

            if (total <= 0) {
                return 3;
            }
            if (!scoring_method.equals("deduction") && !scoring_method.equals("raw") && !scoring_method.equals("percentage")) {
                return 4;
            }
            if(is_exam) {
                Assignment assignment = new Exam(name, total, scoring_method);
                assignments.add(assignment);
            }
            else {
                Assignment assignment = new Assignment(name, total, scoring_method);
                assignments.add(assignment);
            }
            if(scoring_method.equals("deduction")) {
                sheet.addColumns(1,1);
            }
            else {
                sheet.addColumns(1,0);
            }
            FileIO fileIO = new FileIO();
            fileIO.writeAssignment(assignments,courseName+semester);
            fileIO.writeCell(sheet.getAllCell(),courseName+semester);
            return 1;
        }
        catch (Exception e) {
            return 6;
        }
    }
    public int changeAssignment(int index, String name, double total, String scoring_method) {
        //parameters:
        // index: int: index of assignment you would like to change in the arraylist of assignments
        // name: String: name of the assignment
        // total: double: total score of the assignment
        // scoring_method: String: scoring method of the assignment
        //
        //return 1 if succeeded , return 2 if assignment not found, return 3 if invalid name,
        // return 4 if invalid total, return 5 if invalid scoring_method,
        // return 6 if course is ended, return 7 is unknown error
        if(end) {
            return 6;
        }
        try {
            if (index >= assignments.size() || index < 0) {
                return 2;
            }
            if (name == null || name.equals("")) {
                return 3;
            }
            if (total <= 0) {
                return 4;
            }
            if (!scoring_method.equals("deduction") && !scoring_method.equals("raw") && !scoring_method.equals("percentage")) {
                return 5;
            }
            Assignment assignment = assignments.get(index);
            assignment.changeAssignment(name, total, scoring_method);
            return 1;
        }
        catch (Exception e) {
            return 7;
        }
    }

    public int changeCriteria_UG(double[] weights) {
        //parameters:
        // weights: double[]: Array of double representing weights
        //
        // return 1 if succeeded , return 2 if sum of weights is greater than 1,
        // return 3 if course is ended, return 4 if unknown error
        if(end) {
            return 3;
        }
        try {
            double sum = 0;

            for (int i = 0; i < weights.length; i++) {
                sum += weights[i];
            }
            if (sum > 1) {
                return 2;
            }
            criteria_UG.changeCriteria(weights);
            calTotal();
            FileIO fileIO = new FileIO();
            fileIO.writeCriteria(criteria_UG,courseName+semester+"UG");
            return 1;
        }
        catch (Exception e) {
            return 4;
        }
    }
    public int changeCriteria_G(double[] weights) {
        //parameters:
        // weights: double[]: Array of double representing weights
        //
        // return 1 if succeeded , return 2 if sum of weights is greater than 1,
        // return 3 if course is ended, return 4 if unknown error
        if(end) {
            return 3;
        }
        try {
            double sum = 0;
            for (int i = 0; i < weights.length; i++) {
                sum += weights[i];
            }
            if (sum > 1) {
                return 2;
            }
            criteria_G.changeCriteria(weights);
            calTotal();
            FileIO fileIO = new FileIO();
            fileIO.writeCriteria(criteria_G,courseName+semester+"G");
            return 1;
        }
        catch (Exception e) {
            return 4;
        }
    }
    public String[][] getTable() { //
        // parameters:
        // None
        // return String[][] of content of sheet, first two columns: ID, Name, null of unknown error
        DecimalFormat df  = new DecimalFormat("#.00");
        try {
            if(show_Total) {
                if (extra_credits.getExtra_credits() != null) {
                    int offset_column = 2;
                    int offset_row = 1;
                    int height = sheet.getHeight();
                    int width = sheet.getWidth();

                    String[][] table = new String[height][width + 1];
                    table[0][0] = "Student ID";
                    table[0][1] = "Student Name";
                    for (int j = 0; j < width - offset_column - 1; j++) {
                        table[0][j + offset_column] = assignments.get(j).getName();
                    }
                    table[0][width - 1] = "Total";
                    table[0][width] = "Extra Credit";

                    for (int i = 1; i < height; i++) { // todo
                        String student_type;
                        if(students.get(i - offset_row) instanceof Undergraduate) {
                            student_type = "U";
                        }
                        else {
                            student_type = "G";
                        }
                        table[i][0] = student_type + ":"+ students.get(i - offset_row).getStudentId();
                        table[i][1] = students.get(i - offset_row).getFirstName() + " " + students.get(i - offset_row).getLastName();
                        for (int j = offset_column; j < width - 1; j++) {
                            if (assignments.get(j - offset_column).getScoring_method().equals("raw")) {
                                double s = ((sheet.getCellScore(i, j) * assignments.get(j - offset_column).getTotal()));
                                if(String.valueOf(s).length() >= 6) {
                                    table[i][j] = String.valueOf(df.format(s));
                                }
                                else {
                                    table[i][j] = String.valueOf(s);
                                }
                            } else {
                                if (assignments.get(j - offset_column).getScoring_method().equals("deduction")) {
                                    double s = ((assignments.get(j - offset_column).getTotal() - (sheet.getCellScore(i, j) * assignments.get(j - offset_column).getTotal())));
                                    if(String.valueOf(s).length() >= 6) {
                                        table[i][j] = String.valueOf(df.format(s));
                                    }
                                    else {
                                        table[i][j] = String.valueOf(s);
                                    }
                                } else {
                                    if (assignments.get(j - offset_column).getScoring_method().equals("percentage")) {
                                        double s = ((sheet.getCellScore(i, j)));
                                        if(String.valueOf(s).length() >= 6) {
                                            table[i][j] = String.valueOf(df.format(s));
                                        }
                                        else {
                                            table[i][j] = String.valueOf(s);
                                        }

                                    } else {
                                        return null;
                                    }

                                }
                            }
                        }
                        table[i][width - 1] = String.valueOf(sheet.getCellScore(i, width - 1));
                        table[i][width] = String.valueOf(extra_credits.getExtra_credits().get(i - offset_row));
                    }
                    return table;
                } else {
                    int offset_column = 2;
                    int offset_row = 1;
                    int height = sheet.getHeight();
                    int width = sheet.getWidth();
                    String[][] table = new String[height][width];
                    table[0][0] = "Student ID";
                    table[0][1] = "Student Name";
                    for (int j = 0; j < width - offset_column - 1; j++) {
                        table[0][j + offset_column] = assignments.get(j).getName();
                    }
                    table[0][width - 1] = "Total";

                    for (int i = offset_row; i < height; i++) { //
                        String student_type;
                        if(students.get(i - offset_row) instanceof Undergraduate) {
                            student_type = "U";
                        }
                        else {
                            student_type = "G";
                        }
                        table[i][0] = student_type + ":"+ students.get(i - offset_row).getStudentId();
                        table[i][1] = students.get(i - offset_row).getFirstName() + " " + students.get(i - offset_row).getLastName();
                        for (int j = offset_column; j < width - 1; j++) {
                            if (assignments.get(j - offset_column).getScoring_method().equals("raw")) {
                                double s = ((sheet.getCellScore(i, j) * assignments.get(j - offset_column).getTotal()));
                                if(String.valueOf(s).length() >= 6) {
                                    table[i][j] = String.valueOf(df.format(s));
                                }
                                else {
                                    table[i][j] = String.valueOf(s);
                                }
                            } else {
                                if (assignments.get(j - offset_column).getScoring_method().equals("deduction")) {
                                    double s = ((assignments.get(j - offset_column).getTotal() - (sheet.getCellScore(i, j) * assignments.get(j - offset_column).getTotal())));
                                    if(String.valueOf(s).length() >= 6) {
                                        table[i][j] = String.valueOf(df.format(s));
                                    }
                                    else {
                                        table[i][j] = String.valueOf(s);
                                    }
                                } else {
                                    if (assignments.get(j - offset_column).getScoring_method().equals("percentage")) {
                                        double s = ((sheet.getCellScore(i, j)));
                                        if(String.valueOf(s).length() >= 6) {
                                            table[i][j] = String.valueOf(df.format(s));
                                        }
                                        else {
                                            table[i][j] = String.valueOf(s);
                                        }

                                    } else {
                                        return null;
                                    }

                                }
                            }
                        }

                        table[i][width - 1] = String.valueOf(sheet.getCellScore(i, width - 1));
                    }
                    return table;
                }
            }
            else {
                if (extra_credits.getExtra_credits() != null) {
                    int offset_column = 2;
                    int offset_row = 1;
                    int height = sheet.getHeight();
                    int width = sheet.getWidth();

                    String[][] table = new String[height][width];
                    table[0][0] = "Student ID";
                    table[0][1] = "Student Name";
                    for (int j = 0; j < width - offset_column - 1; j++) {
                        table[0][j + offset_column] = assignments.get(j).getName();
                    }
                    table[0][width - 1] = "Extra Credit";

                    for (int i = 1; i < height; i++) { // todo
                        String student_type;
                        if(students.get(i - offset_row) instanceof Undergraduate) {
                            student_type = "U";
                        }
                        else {
                            student_type = "G";
                        }
                        table[i][0] = student_type + ":"+ students.get(i - offset_row).getStudentId();
                        table[i][1] = students.get(i - offset_row).getFirstName() + " " + students.get(i - offset_row).getLastName();
                        for (int j = offset_column; j < width - 1; j++) {
                            if (assignments.get(j - offset_column).getScoring_method().equals("raw")) {
                                double s = ((sheet.getCellScore(i, j) * assignments.get(j - offset_column).getTotal()));
                                if(String.valueOf(s).length() >= 6) {
                                    table[i][j] = String.valueOf(df.format(s));
                                }
                                else {
                                    table[i][j] = String.valueOf(s);
                                }
                            } else {
                                if (assignments.get(j - offset_column).getScoring_method().equals("deduction")) {
                                    double s = ((assignments.get(j - offset_column).getTotal() - (sheet.getCellScore(i, j) * assignments.get(j - offset_column).getTotal())));
                                    if(String.valueOf(s).length() >= 6) {
                                        table[i][j] = String.valueOf(df.format(s));
                                    }
                                    else {
                                        table[i][j] = String.valueOf(s);
                                    }
                                } else {
                                    if (assignments.get(j - offset_column).getScoring_method().equals("percentage")) {
                                        double s = ((sheet.getCellScore(i, j)));
                                        if(String.valueOf(s).length() >= 6) {
                                            table[i][j] = String.valueOf(df.format(s));
                                        }
                                        else {
                                            table[i][j] = String.valueOf(s);
                                        }

                                    } else {
                                        return null;
                                    }

                                }
                            }
                        }
                        table[i][width - 1] = String.valueOf(extra_credits.getExtra_credits().get(i - offset_row));
                    }
                    return table;
                }
                else {
                    int offset_column = 2;
                    int offset_row = 1;
                    int height = sheet.getHeight();
                    int width = sheet.getWidth();
                    String[][] table = new String[height][width - 1];
                    table[0][0] = "Student ID";
                    table[0][1] = "Student Name";
                    for (int j = 0; j < width - offset_column - 1; j++) {
                        table[0][j + offset_column] = assignments.get(j).getName();
                    }
                    for (int i = offset_row; i < height; i++) { //
                        String student_type;
                        if(students.get(i - offset_row) instanceof Undergraduate) {
                            student_type = "U";
                        }
                        else {
                            student_type = "G";
                        }
                        table[i][0] = student_type + ":"+ students.get(i - offset_row).getStudentId();
                        table[i][1] = students.get(i - offset_row).getFirstName() + " " + students.get(i - offset_row).getLastName();
                        for (int j = offset_column; j < width - 1; j++) {
                            if (assignments.get(j - offset_column).getScoring_method().equals("raw")) {
                                double s = ((sheet.getCellScore(i, j) * assignments.get(j - offset_column).getTotal()));
                                if(String.valueOf(s).length() >= 6) {
                                    table[i][j] = String.valueOf(df.format(s));
                                }
                                else {
                                    table[i][j] = String.valueOf(s);
                                }
                            } else {
                                if (assignments.get(j - offset_column).getScoring_method().equals("deduction")) {
                                    double s = ((assignments.get(j - offset_column).getTotal() - (sheet.getCellScore(i, j) * assignments.get(j - offset_column).getTotal())));
                                    if(String.valueOf(s).length() >= 6) {
                                        table[i][j] = String.valueOf(df.format(s));
                                    }
                                    else {
                                        table[i][j] = String.valueOf(s);
                                    }
                                } else {
                                    if (assignments.get(j - offset_column).getScoring_method().equals("percentage")) {
                                        double s = ((sheet.getCellScore(i, j)));
                                        if(String.valueOf(s).length() >= 6) {
                                            table[i][j] = String.valueOf(df.format(s));
                                        }
                                        else {
                                            table[i][j] = String.valueOf(s);
                                        }

                                    } else {
                                        return null;
                                    }

                                }
                            }
                        }
                    }
                    return table;
                }
            }
        }
        catch (Exception e) {
            return null;
        }
    }
    public int setScore(int cor1,int cor2, String score) { //
        // parameters:
        // cor1: int: coordinate of row of cell to modify
        // cor2: int: coordinate of column of cell to modify
        // score: String: score to be modified
        //
        // return 1 if succeeded, return 2 if invalid score type, return 3 if course is ended, return 4 if invalid score input, return 5 if invalid input data type, return 6 if unknown error
        if(end) {
            return 3;
        }
        try {
            int real_cor2 = cor2 - 2; // offset first two columns
            if(assignments.get(real_cor2).getScoring_method().equals("raw")) {
                if(Double.valueOf(score) < 0 || Double.valueOf(score) > assignments.get(real_cor2).getTotal()) {
                    return 4;
                }
                double input = Double.valueOf(score) / assignments.get(real_cor2).getTotal();
                DecimalFormat df  = new DecimalFormat("#.00");
                input = Double.valueOf(df.format(input));
                sheet.setScore(cor1, cor2, input);
            }
            else {
                if (assignments.get(real_cor2).getScoring_method().equals("deduction")) {
                    if(Double.valueOf(score) < 0 || Double.valueOf(score) > assignments.get(real_cor2).getTotal()) {
                        return 4;
                    }
                    double input = (-Double.valueOf(score) + assignments.get(real_cor2).getTotal()) / assignments.get(real_cor2).getTotal();
                    DecimalFormat df  = new DecimalFormat("#.00");
                    input = Double.valueOf(df.format(input));
                    sheet.setScore(cor1, cor2, input);
                }
                else {
                    if (assignments.get(real_cor2).getScoring_method().equals("percentage")) {
                        if(Double.valueOf(score) < 0 || Double.valueOf(score) > 1) {
                            return 4;
                        }
                        double input = Double.valueOf(score);
                        DecimalFormat df  = new DecimalFormat("#.00");
                        input = Double.valueOf(df.format(input));
                        sheet.setScore(cor1, cor2, input);
                    }
                    else {
                        return 2;
                    }
                }
            }

            calTotal();
            FileIO fileIO = new FileIO();
            fileIO.writeCell(sheet.getAllCell(),courseName+semester);
            return 1;
        }
        catch (NumberFormatException e) {
            return 5;
        }
        catch (Exception e) {
            return 6;
        }
    }
    public String[] getNote(int cor1,int cor2) { //
        // parameters:
        // cor1: int: coordinate of row of cell to check
        // cor2: int: coordinate of column of cell to check
        //
        // return String[] of content of Note including content of note and time of last change, null of unknown error
        try {
            return sheet.getCellNote(cor1,cor2).getNoteContent();
        }
        catch (Exception e) {
            return null;
        }
    }

    public int setNote(int cor1,int cor2, String note) { //
        // parameters:
        // cor1: int: coordinate of row of cell to modify
        // cor2: int: coordinate of column of cell to modify
        // note: String: note to be modified
        //
        // return 1 if succeeded, return 2 if course is ended, return 3 if unknown error
        if(end) {
            return 2;
        }
        try {
            sheet.setNote(cor1, cor2, note);
            FileIO fileIO = new FileIO();
            fileIO.writeCell(sheet.getAllCell(),courseName+semester);
            return 1;
        }
        catch (Exception e) {
            return 3;
        }
    }

    public String[][] getAssignmentInformation() {
        // parameters:
        // none
        //
        // return: 2d String array of information about name, total score, weight for UG, weight for G, scoring method
        String[][] table = new String[assignments.size()][6];
        for(int i = 0; i < assignments.size();i++) {
            table[i][0] = assignments.get(i).getName();
            table[i][1] = String.valueOf(assignments.get(i).getTotal());
            table[i][2] = String.valueOf(criteria_UG.getWeight().get(i));
            table[i][3] = String.valueOf(criteria_G.getWeight().get(i)); //todo
            table[i][4] = assignments.get(i).getScoring_method();
            table[i][5] = Boolean.toString(assignments.get(i) instanceof Exam);
        }
        return table;
    }
    public int changeTotal(int index, double new_total) {
        // parameters:
        // index: int: index of assignment about to change
        // new_total: double: new total score to change
        //
        // return: 1 if succeeded, return 2 if assignment not found, return 3 if course is ended, return 4 if unknown error
        if(end) {
            return 3;
        }
        try {
            if (index >= assignments.size() || index < 0) {
                return 2;
            } else {
                assignments.get(index).setTotal(new_total);
                FileIO fileIO = new FileIO();
                fileIO.writeAssignment(assignments,courseName+semester);
                return 1;
            }
        }
        catch (Exception e) {
            return 4;
        }
    }
    public Report getReport(int assignIndex) {
        // tbd
        {
            String[][] res = new String[1][4];
            ArrayList<Double> listScore = new ArrayList<Double>();
            double min = 0;
            double max = 0;
            double ave = 0;
            double med = 0;
            double sumD = 0;

            DecimalFormat df  = new DecimalFormat("#.0000");

            if (assignIndex == -1){
                return null;
            } else if(assignIndex == this.getAssignments().size() + 1){
                return reportTotalToUI();
            }

            int i = -1;
            int listSize = 0;
            for(Double tempD : this.sheet.getScoreColumn(assignIndex+2)){
                if(i >= 0 && !this.getStudents().get(i).isRemovedAfterExam()){
                    listScore.add(tempD);
                    sumD = sumD + tempD;
                    listSize = listSize + 1;
                }
                i = i + 1;
            }
            //listScore.remove(0);
            Collections.sort(listScore);
            min = Collections.min(listScore);
            max = Collections.max(listScore);
            if(listSize <= 2){
                ave = 0;
            } else {
                ave = (sumD - min - max) / (listSize - 2);
            }
            if(listSize % 2 == 0){
                med = (listScore.get(listSize/2) + listScore.get(listSize/2-1)) / 2;
            } else{
                med = listScore.get(listSize/2);
            }
            res[0][0] = String.valueOf(df.format(min));
            res[0][1] = String.valueOf(df.format(max));
            res[0][2] = String.valueOf(df.format(ave));
            res[0][3] = String.valueOf(df.format(med));

            Report rep = new Report(res);
            return rep;
        }    }

    public Report reportTotalToUI(){
        DecimalFormat df  = new DecimalFormat("#.00");
        String[][] res = new String[1][4];
        double min = 0;
        double max = 0;
        double ave = 0;
        double med = 0;
        ArrayList<Double> listScore = new ArrayList<Double>();
        double sumD = 0;
        int i = -1;
        int listSize = 0;
        for(Double tempD : this.sheet.getScoreColumn(this.sheet.getAllCell().get(0).size()-1)){
            if(i >= 0 && !this.getStudents().get(i).isRemovedAfterExam()){
                listScore.add(tempD);
                sumD = sumD + tempD;
                listSize = listSize + 1;
            }
            i = i + 1;
        }
        Collections.sort(listScore);
        min = Collections.min(listScore);
        max = Collections.max(listScore);
        if(listSize <= 2){
            ave = 0;
        } else {
            ave = (sumD - min - max) / (listSize - 2);
        }
        if(listSize % 2 == 0){
            med = (listScore.get(listSize/2) + listScore.get(listSize/2-1)) / 2;
        } else{
            med = listScore.get(listSize/2);
        }
        res[0][0] = String.valueOf(df.format(min));
        res[0][1] = String.valueOf(df.format(max));
        res[0][2] = String.valueOf(df.format(ave));
        res[0][3] = String.valueOf(df.format(med));

        Report rep = new Report(res);
        return rep;
    }

    public int endCourse() {
        //return 1 if succeeded , return 2 if course is already ended, return 3 if unknown error
        try {
            if (end) {
                return 2;
            }
            end = true;
            return 1;
        }
        catch (Exception e) {
            return 3;
        }
    }
    public int extra() {
        //return 1 if succeeded , return 2 if extra credit is already added, return 3 if unknown error
        try {
            if (extra_credits.getExtra_credits() != null) {
                return 2;
            }
            extra_credits.create();
            for(int i = 0; i < students.size();i++) {
                extra_credits.add_one();
            }
            FileIO fileIO = new FileIO();
            fileIO.writeCell(sheet.getAllCell(),courseName+semester);
            fileIO.writeExtraCredit(extra_credits,courseName+semester);
            return 1;
        }
        catch (Exception e) {
            return 3;
        }
    }

    public int modify(int index, double score) {
        // parameters:
        // int: index: index in extra credit to be modified
        // double : score: score to bu added
        //
        // return: int: return 1 if succeeded, return 2 if out of index, return 3 if course is ended, return 4 if unknown error.
        if(isEnd()) {
            return 3;
        }
        if(index >= extra_credits.getExtra_credits().size()) {
            return 2;
        }
        else {
            try {
                extra_credits.getExtra_credits().set(index,score);
                FileIO fileIO = new FileIO();
                fileIO.writeCell(sheet.getAllCell(),courseName+semester);
                fileIO.writeExtraCredit(extra_credits,courseName+semester);
                return 1;
            }
            catch (Exception e) {
                return 4;
            }
        }
    }

    public int calTotal() {
        //
        // return 1 if succeeded, return 2 if unknown error
        double[] res = new double[students.size()];
        DecimalFormat df  = new DecimalFormat("#.00");
        for(int i = 0; i < res.length ;i++) {
            if(students.get(i) instanceof Undergraduate) {
                ArrayList<Double> row = sheet.getScoreRow(i + 1);
                if(row.size() - 3 != criteria_UG.getWeight().size()) {
                    return 2;
                }
                double score = 0;
                for(int j = 2; j < row.size() - 1; j++) {
                    score += row.get(j) * criteria_UG.getWeight().get(j - 2);
                }
                res[i] = Double.valueOf(df.format(100 * score));
            }
            else {
                ArrayList<Double> row = sheet.getScoreRow(i + 1);
                if(row.size() - 3 != criteria_G.getWeight().size()) {
                    return 2;
                }
                double score = 0;
                for(int j = 2; j < row.size() - 1; j++) {
                    score += row.get(j) * criteria_G.getWeight().get(j - 2);
                }
                res[i] = Double.valueOf(df.format(100 * score));
            }
        }
        for(int i = 0; i < students.size(); i++) {
            sheet.setScore(i + 1,sheet.getWidth() - 1,res[i]);
        }
        return 1;
    }
    public static void main(String[] args) {
        String a = "1a";
        double b = Double.valueOf(a);
    }
}