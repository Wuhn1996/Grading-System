package BackEnd;

import java.util.ArrayList;

/**
 * Created by wangkaihong on 2019/4/9.
 */
public class Sheet {
    private ArrayList<ArrayList<Cell>> cells;


    public Sheet() {
        ArrayList<Cell> temp = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            temp.add(new Cell());
        }
        cells = new ArrayList<ArrayList<Cell>>();
        cells.add(temp);
    }

    public Sheet(ArrayList<ArrayList<Cell>> c){
        cells = c;
    }

    public ArrayList<ArrayList<Cell>> getAllCell() {
        return cells;
    }

    public double getCellScore(int cor1, int cor2) {
        return cells.get(cor1).get(cor2).getScore();
    }

    public Note getCellNote(int cor1, int cor2) {
        return cells.get(cor1).get(cor2).getNote();
    }

    public int getHeight() {
        return cells.size();
    }

    public int getWidth() {
        if(cells.size() > 0) {
            return cells.get(0).size();
        }
        return 0;
    }
    public ArrayList<Double> getScoreRow(int index) {
        ArrayList<Double> ret = new ArrayList<>();
        for(int i = 0; i < cells.get(index).size(); i++) {
            ret.add(cells.get(index).get(i).getScore());
        }
        return ret;
    }

    public ArrayList<Double> getScoreColumn(int index) {
        ArrayList<Double> ret = new ArrayList<>();
        for(int i = 0; i < cells.size(); i++) {
            ret.add(cells.get(i).get(index).getScore());
        }
        return ret;
    }

    public void removeRow(int ind) {
        cells.remove(ind);
    }

    public void setCell(ArrayList<ArrayList<Cell>> cell) {
        this.cells = cell;
    }

    public void setScore(int cor1,int cor2, double score) {
        cells.get(cor1).get(cor2).setScore(score);
    }

    public void setNote(int cor1,int cor2, String note) {
        cells.get(cor1).get(cor2).setNote(note);
    }

    public void addRows(int row_number) {
        for(int i = 0; i < row_number;i++) {
            ArrayList<Cell> temp = new ArrayList<>();
            for(int j = 0; j < cells.get(0).size();j++) {
                temp.add(new Cell());
            }
            cells.add(temp);
        }
    }

    public void addColumns(int col_number, double score) {
        for(int i = 0; i < cells.size(); i++) {
            for(int j = 0; j < col_number; j++) {
//                cells.get(i).add(new Cell());
                cells.get(i).add(cells.get(i).size()-1,new Cell(score));
            }
        }
    }
}
