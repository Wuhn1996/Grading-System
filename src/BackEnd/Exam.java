package BackEnd;

/**
 * Created by wangkaihong on 2019/4/27.
 */
public class Exam extends Assignment {
    public Exam() {
        super();
    }
    public Exam(Assignment previous) {
        super(previous);
    }
    public Exam(String name, double total, String scoring_method) {
        super(name,total,scoring_method);
    }
}
