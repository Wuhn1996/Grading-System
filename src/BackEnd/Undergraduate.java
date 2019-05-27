package BackEnd;

/**
 * Created by wangkaihong on 2019/4/9.
 */
public class Undergraduate extends Student {
    public Undergraduate() {
        super();
    }
    public Undergraduate(String firstName, String middleInitial, String lastName, String studentId, String emailAddress, boolean removedAfterExam) {
        super(firstName, middleInitial, lastName, studentId, emailAddress, removedAfterExam);
    }
}
