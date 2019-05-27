package BackEnd;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import BackEnd.FileIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by wangkaihong on 2019/4/9.
 */
public class Grading_System {
    private ArrayList<Course> courses;

    public Grading_System() {
        // Constructor: read from IO and construct system instance
        FileIO fileIO = new FileIO();
        this.courses = fileIO.readCourse();
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }

    public String[] getCourseList() {
        String[] list = new String[courses.size()];
        for(int i = 0; i < list.length;i++) {
            list[i] = courses.get(i).getCourseName()+" "+courses.get(i).getSemester();
        }
        return list;
    }

    public int addCourse(String courseName, String lecturerName, String semester, String student_file_dir, int previous_Course_ind) {
        // parameters:
        // courseName: String :name of the course
        // lecturerName: String :name of the lecturer
        // semester: String :name of the semester
        // student_file_dir: String :directory to student file
        // previous_Course_ind: int :index of the course, -1 means not coping from previous courses.
        //
        // return 1 if succeeded, return 2 if have course name conflict, return 3 if invalid index,
        // return 4 if invalid course name, return 5 if invalid lecturer name, return 6 if invalid semester name,
        // return 7 if invalid path to student info file, return 8 if invalid format of student info file,
        // return 9 if file not found, return 10 if unknown error

        //testtest
        Course previous = null;
        if(previous_Course_ind != -1) {
            if (previous_Course_ind >= courses.size() || previous_Course_ind < 0) {
                return 3;
            }
             previous = courses.get(previous_Course_ind);
        }
        for(int i = 0;i < courses.size();i++) {
            if((courseName+" "+semester).equals(courses.get(i).getCourseName()+" "+courses.get(i).getSemester())) {
                return 2;
            }
        }
        if(courseName == null || courseName.length() == 0) {//|| lecturerName == null || semester == null || student_file_dir == null) {
            return 4;
        }
        else {
            if(lecturerName == null || lecturerName.length() == 0) {
                return 5;
            }
            else {
                if(semester == null || semester.length() == 0) {
                    return 6;
                }
                else {
                    if(student_file_dir == null || student_file_dir.length() == 0) {
                        return 7;
                    }
                    else {
                        try {
                            ArrayList<Student> student_list = getStudentsFromFile(student_file_dir);
                            courses.add(new Course(courseName,lecturerName,semester,student_list,previous));
                            FileIO fileIO = new FileIO();
                            fileIO.writeCourse(courses);
                            return 1;
                        }
                        catch (FileNotFoundException e) {
                            return 9;
                        }
                        catch (InvalidStudentFileException e) {
                            return 8;
                        }
                        catch (Exception e) {
                            return 10;
                        }
                    }
                }
            }
        }
    }

        public ArrayList<Student> getStudentsFromFile(String student_file_dir) throws FileNotFoundException, InvalidStudentFileException, Exception{
        ArrayList<Student> stduent_list = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(student_file_dir));
            scanner.useDelimiter("\n");
            while (scanner.hasNext()) {
                String temp = scanner.next();
                String[] arr = temp.split(",");
                if(arr.length != 6) {
                    throw new InvalidStudentFileException();
                }
                if(arr[5].contains("U")) {
                    stduent_list.add(new Undergraduate(arr[0], arr[1], arr[2], arr[3], arr[4], false));
                }
                else {
                    if(arr[5].contains("G")) {
                        stduent_list.add(new Graduate(arr[0], arr[1], arr[2], arr[3], arr[4],false));
                    }
                    else {
                        throw new InvalidStudentFileException();
                    }
                }
            }
            scanner.close();
        }
        catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        }
        catch (InvalidStudentFileException e) {
            throw new InvalidStudentFileException();
        }
        catch (Exception e) {
            throw new Exception();
        }
        return stduent_list;
    }

    public int deleteCourse(int previous_Course_ind) {
        // parameters:
        // previous_Course_ind: int :index of the course
        // return 1 if succeeded, return 2 if course not found, return 3 if unknown error
        if(previous_Course_ind >= courses.size() || previous_Course_ind < 0) {
            return 2;
        }
        if(courses.get(previous_Course_ind)==null) {
            return 3;
        }
        try {
            FileIO fileIO = new FileIO();
            Course deleteCourse = this.getCourses().get(previous_Course_ind);
            fileIO.deleteClassFile(deleteCourse.getCourseName() + deleteCourse.getSemester());
            courses.remove(previous_Course_ind);
            fileIO.writeCourse(getCourses());
            return 1;
        }
        catch (Exception e) {
            return 3;
        }
    }

}
