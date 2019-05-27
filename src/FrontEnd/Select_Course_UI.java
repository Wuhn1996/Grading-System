package FrontEnd;

import BackEnd.Course;
import BackEnd.Grading_System;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


public class Select_Course_UI extends JFrame implements ActionListener {
    JPanel label = new JPanel();
    JPanel courseList = new JPanel();
    JPanel buttons = new JPanel();

    static String[] course;
    JLabel courseLabel = new JLabel("Course: ");


    JButton enter = new JButton("Enter Course");
    JButton delete = new JButton("Delete Course");
    JButton add = new JButton("Create New Course");
    JButton logout = new JButton("Log out");

    int getSelect = -1;
    Grading_System grading_system;
    static Course coursetest = new Course();

    public Select_Course_UI(Grading_System grading_system) {
        this.grading_system = grading_system;
        course = grading_system.getCourseList();
        JComboBox courses = new JComboBox(course);
        courses.setPreferredSize(new Dimension(350, 30));
        Container contentPane = this.getContentPane();

        contentPane.setLayout(null);

        enter.setForeground(Color.BLUE);
        delete.setForeground(Color.RED);

        String defaultMessage ="please select";
        ComboBoxEditor editor= courses.getEditor();
        courses.configureEditor(editor,defaultMessage);
        courses.setSelectedItem(null);
        courses.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    getSelect = courses.getSelectedIndex();
                }
            }
        });

        label.add(courseLabel);
        label.setBounds(50,100,50,30);
        contentPane.add(label);

        this.courseList.add(courses);
        this.courseList.setBounds(0, 95, 600, 30);
        contentPane.add(this.courseList);

        add.addActionListener(this);
        enter.addActionListener(this);
        logout.addActionListener(this);
        delete.addActionListener(this);
        buttons.setLayout(new GridLayout(4, 1));
        buttons.add(enter);
        buttons.add(delete);
        buttons.add(add);
        buttons.add(logout);
        buttons.setBounds(400, 200, 200, 120);
        contentPane.add(buttons);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(640, 360);
        setTitle("Select Course");
        setResizable(false);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == add) {
            dispose();
            new Add_Class_UI(grading_system);
        } else if (e.getSource() == enter) {
            if (getSelect == -1){
//                dispose();
//                new Select_Course_UI(grading_system);
                JOptionPane.showMessageDialog(null,"Please select a course");
            }
            for (int i = 0; i < course.length; i++) {
                if (getSelect == i) {
                    dispose();
                    new GradeSheet_UI(grading_system,grading_system.getCourses().get(i));
                }
            }
        } else if (e.getSource() == logout) {
            dispose();
            new Grading_System_UI(grading_system);
        } else if (e.getSource() == delete){
            if (getSelect == -1){
                JOptionPane.showMessageDialog(null,"Please select a course");
            } else {
                int input = JOptionPane.showConfirmDialog(null, "Are you sure to delete this course?");
                if (input == JOptionPane.YES_OPTION){
                    grading_system.deleteCourse(getSelect);
                    dispose();
                    new Select_Course_UI(grading_system);
                } else {
                    dispose();
                    new Select_Course_UI(grading_system);
                }
            }
        }
    }
}