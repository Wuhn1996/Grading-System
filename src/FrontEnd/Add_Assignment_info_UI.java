package FrontEnd;

import BackEnd.Course;
import BackEnd.Grading_System;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;


public class Add_Assignment_info_UI extends JFrame implements ActionListener {
    JPanel importLabel = new JPanel();
    JPanel importBox = new JPanel();

    JPanel buttons = new JPanel();

    JComboBox courses = new JComboBox(Select_Course_UI.course);
    JButton next = new JButton("Next");
    JButton returnBack = new JButton("Return");
    JButton cancel = new JButton("Cancel");
    JButton skip = new JButton("Skip");
    

    private Course course = new Course();
    public Grading_System grading_system;
    public String name;
    public String lecturerName;
    public String semesterName;

    public int selectNum = -1;


    public Add_Assignment_info_UI(Grading_System grading_system, String name, String lecturerName, String semesterName){
        this.grading_system = grading_system;
        this.name = name;
        this.lecturerName = lecturerName;
        this.semesterName = semesterName;
        Container contentPane = this.getContentPane();
        contentPane.setLayout(null);

        courses.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED){
                    selectNum = courses.getSelectedIndex();
                }
            }
        });
        courses.setSelectedItem(null);
        courses.setPreferredSize(new Dimension(350, 30));

        importLabel.add(new JLabel("Import From: "));
        importBox.add(courses);

        importLabel.setBounds(100,105,100,30);
        importBox.setBounds(150,100,500,30);
        contentPane.add(importLabel);
        contentPane.add(importBox);


        next.setForeground(Color.BLUE);
        cancel.setForeground(Color.RED);

        skip.addActionListener(this);
        next.addActionListener(this);
        returnBack.addActionListener(this);
        cancel.addActionListener(this);
        buttons.setLayout(new GridLayout(1,3));
        buttons.add(skip);
        buttons.add(next);
        buttons.add(returnBack);
        buttons.add(cancel);
        buttons.setBounds(250,250,350,30);
        contentPane.add(buttons);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(640, 360);
        setTitle("Add section information");
        setResizable(false);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == next){
            if (selectNum == -1){
                JOptionPane.showMessageDialog(null,"Please select a course to import or click skip");
            } else {
                dispose();
                new Add_Student_info_UI(grading_system, this.name, this.lecturerName, this.semesterName, selectNum);
            }
        } else if (e.getSource() == cancel){
            dispose();
            new Select_Course_UI(grading_system);
        } else if (e.getSource() == returnBack){
            dispose();
            new Add_Class_UI(grading_system);
        } else if (e.getSource() == skip){
            dispose();
            new Add_Student_info_UI(grading_system,this.name,this.lecturerName,this.semesterName, -1);
        }

    }

}
