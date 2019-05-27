package BackEnd;

import java.util.ArrayList;

/**
 * Created by wangkaihong on 2019/4/9.
 */
public class Criteria {
    private ArrayList<Double> weights;


    public Criteria() {
        weights = new ArrayList<>();
    }

    public Criteria(Criteria previous) {
        weights = new ArrayList<>();
        weights.addAll(previous.getWeight());
    }

    public Criteria(double[] add_weights) {
        weights = new ArrayList<>();
        for(int i = 0; i < add_weights.length;i++) {
            weights.add(add_weights[i]);
        }

    }

    public Criteria(ArrayList<Double> weight) {
        this.weights = weight;
    }


    public void changeCriteria(double[] add_weights) {
        weights = new ArrayList<>();
        for(int i = 0; i < add_weights.length;i++) {
            weights.add(add_weights[i]);
        }
    }

    public ArrayList<Double> getWeight() {
        return weights;
    }

    public void setWeight(ArrayList<Double> weight) {
        this.weights = weight;
    }

    public boolean validateWeight() {
        // return true if valid, return false if invalid
        return true;
    }
}
