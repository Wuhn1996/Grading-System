package FrontEnd;

import BackEnd.Course;
import BackEnd.Grading_System;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;

public class NoteFrame extends JFrame implements ActionListener{
    JPanel pNote = new JPanel();
    JButton back = new JButton("Back");
    JTextArea taNote = new JTextArea(2,50);
    JScrollPane spNote = new JScrollPane(taNote);
    //JTextField tfNote = new JTextField(5);
    DefaultTableModel mNote;
    Grading_System grading_system;
    Course course;
    public NoteFrame(Grading_System grading_system, Course course){
        this.grading_system = grading_system;
        this.course = course;
        Container contentPane = this.getContentPane();
        contentPane.setLayout(null);
        //spNote.add(taNote);
        pNote.add(back);
        pNote.add(spNote);
        pNote.setBounds(0,500,1000,100);
        //pNote.add(tfNote);
        contentPane.add(pNote);
        back.addActionListener(this);
        taNote.setEditable(false);
        //tfNote.setEditable(false);

        Object[] columnNames = {"Name", "HW1", "HW2", "Exam1", "Total"};
        Object[][] rowData = {
                {"Jack", "", "aaaaaaabgbsrbthhthet", "", ""},
                {"John", "", "", "bb", ""},
                {"Sue", "cc", "", "", ""},
                {"Jane", "", "", "", "dd"},
                {"Joe", "", "", "", ""}
        };
        mNote = new DefaultTableModel(rowData, columnNames) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnIndex != 0;
            }
        };
        JTable tNote = new JTable(mNote);
        tNote.setPreferredScrollableViewportSize(new Dimension(400, 300));
        JScrollPane js = new JScrollPane(tNote);
        js.setBounds(50,50,900,400);
        contentPane.add(js);
        tNote.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tNote.rowAtPoint(evt.getPoint());
                int col = tNote.columnAtPoint(evt.getPoint());
                if (row >= 0 && col >= 0) {
                    taNote.setText(tNote.getValueAt(row, col).toString());
                }
            }
        });

        setSize(1000, 600);
        setTitle("Notes");
        setResizable(false);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource() == back){
            dispose();
            new GradeSheet_UI(grading_system,course);
        }
    }
}
