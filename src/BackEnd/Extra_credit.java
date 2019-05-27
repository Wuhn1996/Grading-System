package BackEnd;

import java.util.ArrayList;

/**
 * Created by wangkaihong on 2019/4/25.
 */
public class Extra_credit {
    private ArrayList<Double> extra_credits;

    public Extra_credit() {
        this.extra_credits = null;
    }

    public Extra_credit(ArrayList<Double> extra_credits) {
        this.extra_credits = extra_credits;
    }

//    public Extra_credit(int size) {
//        this.extra_credits = new ArrayList<>();
//        for(int i = 0; i < size; i++) {
//            extra_credits.add(0.0);
//        }
//    }

    public void create() {
        extra_credits = new ArrayList<>();
    }

    public void add_one() {
        extra_credits.add(0.0);
    }

    public boolean isEmpty() {
        return extra_credits.size() == 0;
    }

    public int modify(int index, double score) {
        // parameters:
        // int: index: index in extra credit to be modified
        // double : score: score to bu added
        //
        // return: int: return 1 if succeeded, return 2 if out of index, return 3 if unknown error.
        if(index >= extra_credits.size()) {
            return 2;
        }
        else {
            try {
                extra_credits.set(index,score);
                return 1;
            }
            catch (Exception e) {
                return 3;
            }
        }
    }

    public int remove(int index) {
        // parameters:
        // int: index: index in extra credit to be removed
        //
        // return: int: return 1 if succeeded, return 2 if out of index, return 3 if unknown error.
        if(index >= extra_credits.size()) {
            return 2;
        }
        else {
            try {
                extra_credits.remove(index);
                return 1;
            }
            catch (Exception e) {
                return 3;
            }
        }
    }

    public ArrayList<Double> getExtra_credits() {
        return extra_credits;
    }

    public void setExtra_credits(ArrayList<Double> extra_credits) {
        this.extra_credits = extra_credits;
    }


}
