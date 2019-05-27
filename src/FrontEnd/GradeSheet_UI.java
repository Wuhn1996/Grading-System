package FrontEnd;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import BackEnd.*;



public class GradeSheet_UI extends JFrame implements ActionListener, MouseListener{
    JPanel pNote = new JPanel();
    JButton addStudent = new JButton("+ Add Student");
    JButton studentInfo = new JButton("Student Info");
    JButton removeStudent = new JButton("- Remove Student");
    JButton addColumn = new JButton("Alter Assignment");
    JButton back = new JButton("Return Class List");
    JButton grade = new JButton();
    JButton complete = new JButton("End Course");
    JButton report = new JButton("Report");
    JButton exCredit = new JButton("Extra Credit");
    JButton addNote = new JButton("Save Note");
    JLabel note = new JLabel("Note");
    JLabel noteTime = new JLabel();
    JPanel pBnote = new JPanel();
    JPanel pB4 = new JPanel();
    JPanel pB3 = new JPanel();
    JPanel pB2 = new JPanel();
    JPanel pFunc4 = new JPanel();
    JPanel pFunc3 = new JPanel();
    JPanel pFunc2 = new JPanel();
    JTextArea noteText = new JTextArea();
    DefaultTableModel mSheet;
    JTable tSheet;
    DefaultTableModel wSheet;
    JTable titleSheet;
    Course course;
    String[][] rowData;
    int extra;
    int colSize;
    Grading_System grading_system;

    boolean isDataChanged = false;

    public GradeSheet_UI(Grading_System grading_system, Course course) {
        this.grading_system = grading_system;
        this.course = course;
        if(course.isShow_Total()) {
            grade.setText("Hide TotalGrade");
        }
        else {
            grade.setText("Show TotalGrade");
        }
        String title = course.getCourseName() + " " + course.getSemester();

        setTitle(title);
        Container contentPane = this.getContentPane();
        contentPane.setLayout(null);
        /*assignment list*/
        ArrayList<Assignment> ass = course.getAssignments();
        rowData = course.getTable();
        int length = rowData[0].length;

        String[] columnNamesW = new String[length];

        columnNamesW[0] = "Item";
        columnNamesW[1] = "    ";
        int i = 2;
        for(Assignment a: ass){
            columnNamesW[i] = a.getName();
            i++;
        }

        String[] columnNames = new String[length];
        columnNames[0] = "ID";
        columnNames[1] = "Name";
        if(course.isShow_Total()) {
            extra = length - ass.size() - 1;
        }
        else {
            extra = length - ass.size();
        }
        if(extra == 3 && course.isShow_Total()){
            columnNames[length-1] = "Extra_credit";
            columnNamesW[length-1] = "    ";
            columnNames[length-2] = "Total";
            columnNamesW[length-2] = "    ";
        }
        else if(course.isShow_Total()){
            columnNames[length-1] = "Total";
            columnNamesW[length-1] = "    ";
        }else if(extra == 3){
            columnNames[length-1] = "Extra_credit";
            columnNamesW[length-1] = "    ";
        }
        int j = 2;
        for(Assignment a: ass){
            columnNames[j] = a.getName();
            j++;
        }

        colSize = ass.size() +2;
        String[][] rowDataW = new String[4][colSize];
        rowDataW[0][0] = "Weight_Undergraduate";
        rowDataW[1][0] = "Weight_Graduate";
        rowDataW[2][0] = "Total";
        rowDataW[3][0] = "Scoring_way";
        ArrayList<Double> weight_ug = course.getCriteria_UG().getWeight();
        ArrayList<Double> weight_g = course.getCriteria_G().getWeight();
        for(int c = 2; c < colSize; c++){
            rowDataW[0][c] = weight_ug.get(c-2).toString();
            rowDataW[1][c] = weight_g.get(c-2).toString();
            Double temp = ass.get(c-2).getTotal();
            String temp2 = ass.get(c-2).getScoring_method();
            rowDataW[2][c] = temp.toString();
            rowDataW[3][c] = temp2;
        }
        //course.getAssignmentInformation();
        wSheet = new DefaultTableModel(rowDataW, columnNamesW) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (!course.isEnd()){
                    if(course.isShow_Total() && extra == 3){
                        return (columnIndex != 0) & (columnIndex != 1)
                                & (columnIndex != length - 1)&(columnIndex != length - 2)
                                & (rowIndex != 3);
                    }

                    else if(extra == 3 || course.isShow_Total()){
                        return (columnIndex != 0) & (columnIndex != 1) & (columnIndex != length - 1);
                    }else{
                        return (columnIndex != 0) & (columnIndex != 1)
                                & (rowIndex != 3);
                    }
                }else {
                    return false;
                }
            }
        };
        wSheet.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if(e.getColumn() <  columnNames.length && e.getColumn() >=0){
                    int row = e.getFirstRow();
                    int col = e.getColumn();
                    //Object value = mSheet.getValueAt(row,col);
                    String value = (String)  wSheet.getValueAt(row,col);
                    int size = ass.size();
                    double[] weightChange= new double[size];
                    try {
                        if (row == 0) {
                            for (int i = 0; i < size; i++) {
                                Object wsu = wSheet.getValueAt(0, i + 2);
                                double v = Double.valueOf((String) wsu);
                                if(v < 0 || v > 1) {
                                    JOptionPane.showMessageDialog(null, "Invalid input!");
                                    wSheet.setValueAt("0.0", row, col);
                                    return;
                                }
                                weightChange[i] = v;
                            }
                            if (totalSum(weightChange) <= 1) {
                                int state = course.changeCriteria_UG(weightChange);
                                if (state == 2) {
                                    JOptionPane.showMessageDialog(null, "Sum of weights exceeds 1!");
                                }
                                if (state == 3) {
                                    JOptionPane.showMessageDialog(null, "Invalid operation, course is ended!");
                                }
                                if (state == 4) {
                                    JOptionPane.showMessageDialog(null, "Unknown error!");
                                }
                            } else {
                                //show notification
                                JOptionPane.showMessageDialog(null, "Total weight is invalid!");
                                wSheet.setValueAt("0.0", row, col);
                                weightChange[col - 2] = 0;
                                int state = course.changeCriteria_UG(weightChange);
                                if (state == 2) {
                                    JOptionPane.showMessageDialog(null, "Sum of weights exceeds 1!");
                                }
                                if (state == 3) {
                                    JOptionPane.showMessageDialog(null, "Invalid operation, course is ended!");
                                }
                                if (state == 4) {
                                    JOptionPane.showMessageDialog(null, "Unknown error!");
                                }
                            }
                        } else if (row == 1) {
                            for (int i = 0; i < size; i++) {
                                Object wsu = wSheet.getValueAt(1, i + 2);
                                double v = Double.valueOf((String) wsu);
                                if(v < 0 || v > 1) {
                                    JOptionPane.showMessageDialog(null, "Invalid input!");
                                    wSheet.setValueAt("0.0", row, col);
                                    return;
                                }
                                weightChange[i] = v;

                            }
                            if (totalSum(weightChange) <= 1) {
                                int state = course.changeCriteria_G(weightChange);
                                if (state == 2) {
                                    JOptionPane.showMessageDialog(null, "Sum of weights exceeds 1!");
                                }
                                if (state == 3) {
                                    JOptionPane.showMessageDialog(null, "Invalid operation, course is ended!");
                                }
                                if (state == 4) {
                                    JOptionPane.showMessageDialog(null, "Unknown error!");
                                }
                            } else {
                                //show notification
                                JOptionPane.showMessageDialog(null, "Total weight is invalid!");
                                wSheet.setValueAt("0.0", row, col);
                                weightChange[col - 2] = 0;
                                int state = course.changeCriteria_G(weightChange);
                                if (state == 2) {
                                    JOptionPane.showMessageDialog(null, "Sum of weights exceeds 1!");
                                }
                                if (state == 3) {
                                    JOptionPane.showMessageDialog(null, "Invalid operation, course is ended!");
                                }
                                if (state == 4) {
                                    JOptionPane.showMessageDialog(null, "Unknown error!");
                                }
                            }
                        } else {
                            int state = course.changeTotal(col - 2, Double.parseDouble(value));
                            if (state == 2) {
                                JOptionPane.showMessageDialog(null, "Assignment not found!");
                            }
                            if (state == 3) {
                                JOptionPane.showMessageDialog(null, "Invalid operation, course is ended!");
                            }
                            if (state == 4) {
                                JOptionPane.showMessageDialog(null, "Unknown error!");
                            }
                        }
                    }
                    catch (Exception e2) {
                        JOptionPane.showMessageDialog(null,"Invalid input!");
                        wSheet.setValueAt("0.0",row,col);
                    }
                    isDataChanged = true;
                    wSheet.fireTableDataChanged();//trytry
                }
            }
        });

        wSheet.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                rowData = course.getTable();
                DefaultTableModel tempModel = (DefaultTableModel) tSheet.getModel();
                if(isDataChanged){
                    isDataChanged = false;
                    tempModel.setDataVector(rowData,columnNames);
                    tSheet.repaint();
                }

            }
        });

        titleSheet = new JTable(wSheet);
        // Set the size of scroll panel window
        titleSheet.setPreferredScrollableViewportSize(new Dimension(400, 300));
        //spSheet = new JScrollPane(tSheet);

        mSheet = new DefaultTableModel(rowData, columnNames) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if(!course.isEnd()){
                    if(course.isShow_Total() && extra ==3){
                        return (columnIndex != 0) & (columnIndex != 1)
                                & (rowIndex != 0) & (columnIndex != length-2);

                    }else if(course.isShow_Total()){
                        return (columnIndex != 0) & (columnIndex != 1)
                                & (rowIndex != 0)& (columnIndex != length-1);

                    }else{
                        return (columnIndex != 0) & (columnIndex != 1) & (rowIndex != 0);
                    }
                }else{
                    return false;
                }

            }
        };

        tSheet = new JTable(mSheet);//trytry

        mSheet.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if(e.getColumn() <  columnNames.length && e.getColumn() >=0){
                    int row = e.getFirstRow();
                    int col = e.getColumn();
                    String value = (String) mSheet.getValueAt(row,col);
                    if(extra == 3 && e.getColumn() == columnNames.length -1){
                        double val = Double.parseDouble(value);
                        int state = course.modify(row - 1,val);
                        if(state == 2) {
                            JOptionPane.showMessageDialog(null,"Extra credits out of index!");
                        }
                        if(state == 3) {
                            JOptionPane.showMessageDialog(null,"Invalid operation, course is ended!");
                        }
                        if(state == 4) {
                            JOptionPane.showMessageDialog(null,"Unknown error!");
                        }

                    }else{
                        int state = course.setScore(row, col,value);
                        if(state == 2) {
                            mSheet.setValueAt("0.0",row,col);
                            JOptionPane.showMessageDialog(null,"Invalid scoring way!");
                        }
                        if(state == 3) {
                            mSheet.setValueAt("0.0",row,col);
                            JOptionPane.showMessageDialog(null,"Invalid operation, course is ended!");
                        }
                        if(state == 4) {
                            mSheet.setValueAt("0.0",row,col);
                            JOptionPane.showMessageDialog(null,"Invalid score!");
                        }
                        if(state == 5) {
                            mSheet.setValueAt("0.0",row,col);
                            JOptionPane.showMessageDialog(null,"Invalid input data type!");
                        }
                        if(state == 6) {
                            mSheet.setValueAt("0.0",row,col);
                            JOptionPane.showMessageDialog(null,"Unknown error!");
                        }
                        isDataChanged = true;
                        mSheet.fireTableDataChanged();//trytry
                    }

                }

            }

        });

        mSheet.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                rowData = course.getTable();
                DefaultTableModel tempModel = (DefaultTableModel) tSheet.getModel();
                if(isDataChanged){
                    isDataChanged = false;
                    tempModel.setDataVector(rowData,columnNames);
                    tSheet.repaint();
                }

            }
        });

        //weight check

        //tSheet = new JTable(mSheet);//trytry
        // Set the size of scroll panel window
        tSheet.setPreferredScrollableViewportSize(new Dimension(400, 300));
        //spSheet = new JScrollPane(tSheet);

        tSheet.getTableHeader().setReorderingAllowed(false);
        //tSheet.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //tSheet.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane jp = new JScrollPane(tSheet);
        jp.createHorizontalScrollBar();
        titleSheet.getTableHeader().setReorderingAllowed(false);
        //titleSheet.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane wp = new JScrollPane(titleSheet);


        pFunc4.setLayout(new FlowLayout());
        pFunc3.setLayout(new FlowLayout());
        pFunc2.setLayout(new FlowLayout());
        pB4.setLayout(new GridLayout(1,4));
        pB3.setLayout(new GridLayout(1,3));
        pB2.setLayout(new GridLayout(2,1));
        pBnote.setLayout(new GridLayout(1,1));
        //set color of button
        complete.setForeground(Color.RED);
        grade.setForeground(Color.BLUE);
        //noteTime.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        //noteTime.setBackground(Color.WHITE);
        noteTime.setOpaque(true);
        noteText.setLineWrap(true);
        JScrollPane notePane = new JScrollPane(noteText);

        pB4.add(addStudent);
        pB4.add(removeStudent);
        pB4.add(studentInfo);
        pB4.add(exCredit);
        pB3.add(addColumn);
        pB3.add(report);
        pB3.add(grade);
        pBnote.add(addNote);
        pB2.add(complete);
        pB2.add(back);

        pFunc4.add(pB4);
        pFunc3.add(pB3);
        pFunc2.add(pB2);
        note.setFont(new Font("Serif", Font.PLAIN, 18));
        wp.setBounds(50,50,800,85);
        jp.setBounds(50,135,800,400);
        note.setBounds(985,40,200,50);
        notePane.setBounds(875,90,275,300);
        noteTime.setBounds(875, 390,275,50);
        pBnote.setBounds(875,450,265,30);
        pFunc4.setBounds(100,550,700,50);
        pFunc3.setBounds(100,590,700,50);
        pFunc2.setBounds(900,550,200, 100);

        contentPane.add(jp);
        contentPane.add(wp);
        contentPane.add(notePane);
        //contentPane.add(noteText);
        contentPane.add(note);
        contentPane.add(noteTime);
        contentPane.add(pBnote);
        contentPane.add(pFunc4);
        contentPane.add(pFunc3);
        contentPane.add(pFunc2);
        addStudent.addActionListener(this);
        removeStudent.addActionListener(this);
        studentInfo.addActionListener(this);
        back.addActionListener(this);
        addColumn.addActionListener(this);
        grade.addActionListener(this);
        complete.addActionListener(this);
        report.addActionListener(this);
        exCredit.addActionListener(this);
        addNote.addActionListener(this);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        setResizable(false);
        setVisible(true);
        tSheet.addMouseListener(this);



    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource() == back){
            dispose();
            new Select_Course_UI(grading_system);
        }
        else if(e.getSource() == addColumn){
//            mSheet.addColumn("New Column");
            if(course.isEnd()) {
                JOptionPane.showMessageDialog(null,"Invalid operation, course is ended");
            }
            else {
                dispose();
                new ModifyCol_UI(grading_system, course);
            }
        }
        else if(e.getSource() == grade){
            if(grade.getText().equals("Show TotalGrade")) {
                course.setShow_Total(true);
                FileIO fileIO = new FileIO();
                fileIO.writeCourse(grading_system.getCourses());
            }else{
                course.setShow_Total(false);
                FileIO fileIO = new FileIO();
                fileIO.writeCourse(grading_system.getCourses());
            }
            new GradeSheet_UI(grading_system,course);
            dispose();
            //mSheet.fireTableDataChanged();
            //this.revalidate();
        }
        else if(e.getSource() == complete){
            int input = JOptionPane.showConfirmDialog(null, "Are you sure to end this course?");
            if(input == 0){
                tSheet.setEnabled(false);
                titleSheet.setEnabled(false);
                int state = course.endCourse();
                if(state == 1) {
                    FileIO fileIO = new FileIO();
                    fileIO.writeCourse(grading_system.getCourses());
                }
                if(state == 2) {
                    JOptionPane.showMessageDialog(null,"Course is already ended!");
                }
                if(state == 3) {
                    JOptionPane.showMessageDialog(null,"Unknown error!");
                }
            }
        }
        else if(e.getSource() == report){
            new GetReport_UI(grading_system,course);
        }
        else if(e.getSource() == exCredit){
            if(course.isEnd()) {
                JOptionPane.showMessageDialog(null,"Invalid operation, course is ended!");
            }
            else {
                int input = JOptionPane.showConfirmDialog(null, "Are you sure to add extra credit?");
                int state = course.extra();
                if (input == 0) {
                    if (state == 1) {
                        dispose();
                        new GradeSheet_UI(grading_system, course);
                    }
                    if (state == 2) {
                        JOptionPane.showMessageDialog(null, "Extra credits are already added!");
                    }
                    if (state == 3) {
                        JOptionPane.showMessageDialog(null, "Unknown error!");
                    }
                }
            }
        }
        else if(e.getSource() == addStudent){
            if(course.isEnd()) {
                JOptionPane.showMessageDialog(null,"Invalid operation, course is ended!");
            }
            else {
                new Add_Student_single_UI(grading_system, course);
                dispose();
            }
        }
        else if(e.getSource() == removeStudent){
            int select = tSheet.getSelectedRow();
            if(select == -1){
                JOptionPane.showMessageDialog(null,"Please select a student");
            }
            else {
                if (select != 0) {
                    int state = course.removeStudent(select - 1);
                    if(state == 1) {
                        mSheet.removeRow(select);
                    }
                    if(state == 3) {
                        JOptionPane.showMessageDialog(null,"Cannot find such student");
                    }
                    if(state == 4) {
                        JOptionPane.showMessageDialog(null,"Invalid operation, course is ended");
                    }
                    if(state == 5) {
                        JOptionPane.showMessageDialog(null,"Unknown error");
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null,"Cannot delete this row");
                }
            }
        }
        else if(e.getSource() == addNote){
            int col = tSheet.getSelectedColumn();
            int row = tSheet.getSelectedRow();
            String noteS = noteText.getText();
            if(col != -1 && row != -1) {
                int state = course.setNote(row, col, noteS);
                if(state == 2) {
                    JOptionPane.showMessageDialog(null,"Invalid operation, course is ended");
                }
                if(state == 3) {
                    JOptionPane.showMessageDialog(null,"Unknown error");
                }
            }
        }
        else if(e.getSource() == studentInfo){
            int select = tSheet.getSelectedRow();
            if(select == -1){
                JOptionPane.showMessageDialog(null,"Please select a student");
            }else{
                new showStudent_UI(grading_system,course,select);
            }

        }
    }


    private double totalSum(double[] arr){
        double res = 0;
        for(int i = 0; i < arr.length; i++){
            res += arr[i];
        }
        return res;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(!course.isEnd()) {
            noteText.setText(" ");
            noteTime.setText(" ");
            int row = -2;
            int col = -2;
            row = tSheet.getSelectedRow();
            col = tSheet.getSelectedColumn();

            if (extra < 3  ) {
                if (row != -2 && col != -2) {
                    String pullNote = course.getNote(row, col)[0];
                    String pullTime = course.getNote(row,col)[1];
                    noteText.setText(pullNote);
                    noteTime.setText("<html><p>Last modification:</p>" + pullTime + "</html>");
                }
            }else if(extra == 3 && col != tSheet.getColumnCount()-1){
                if (row != -2 && col != -2) {
                    String pullNote = course.getNote(row, col)[0];
                    String pullTime = course.getNote(row,col)[1];
                    noteText.setText(pullNote);
                    noteTime.setText("<html><p>Last modification:</p>" + pullTime + "</html>");
                }
            }
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
