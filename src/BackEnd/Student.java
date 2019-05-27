package BackEnd;

/**
 * Created by wangkaihong on 2019/4/9.
 */
public abstract class Student {
    private String firstName;
    private String middleInitial;
    private String lastName;
    private String studentId;
    private String emailAddress;
    private boolean removedAfterExam;

    public Student() {
        this.firstName = null;
        this.middleInitial = null;
        this.lastName = null;
        this.studentId = null;
        this.emailAddress = null;
        this.removedAfterExam = false;
    }

    public Student(String firstName, String middleInitial, String lastName, String studentId, String emailAddress, boolean removedAfterExam) {
        this.firstName = firstName;
        this.middleInitial = middleInitial;
        this.lastName = lastName;
        this.studentId = studentId;
        this.emailAddress = emailAddress;
        this.removedAfterExam = removedAfterExam;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public boolean isRemovedAfterExam() {return this.removedAfterExam;}

    public void setRemovedAfterExam(boolean removedAfterExam) {this.removedAfterExam = removedAfterExam;}
}