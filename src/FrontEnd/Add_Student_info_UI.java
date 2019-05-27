package FrontEnd;

import BackEnd.Grading_System;
import BackEnd.Student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;


public class Add_Student_info_UI extends JFrame implements ActionListener {
    JPanel p = new JPanel();
    JLabel lCheck = new JLabel("Directory to file: ");
    JTextField file_path = new JTextField(20);

    JPanel buttons = new JPanel();
    JButton ok = new JButton("OK");
    JButton returnBack = new JButton("Return");
    JButton cancel = new JButton("Cancel");
    JButton browse = new JButton("Browse");

    public static ArrayList<Student> student_list;
    public Grading_System grading_system;
    public String name;
    public String lecturerName;
    public String semesterName;
    public int course_ind;

    public String filePath = null;

    public Add_Student_info_UI(Grading_System grading_system, String name, String lecturerName, String semesterName, int course_ind){
        this.grading_system = grading_system;
        this.name = name;
        this.lecturerName = lecturerName;
        this.semesterName = semesterName;
        this.course_ind = course_ind;

        Container contentPane = this.getContentPane();
        contentPane.setLayout(null);
        p.add(lCheck);
        p.add(file_path);
        p.add(browse);

        ok.setForeground(Color.BLUE);
        cancel.setForeground(Color.RED);
        ok.addActionListener(this);
        cancel.addActionListener(this);
        returnBack.addActionListener(this);
        browse.addActionListener(this);
        buttons.setLayout(new GridLayout(1,3));
        buttons.add(ok);
        buttons.add(returnBack);
        buttons.add(cancel);
        buttons.setBounds(300,250,300,30);
        contentPane.add(buttons);

        p.setBounds(20,100,680,50);
        contentPane.add(p);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(640, 360);
        setTitle("Add Student information");
        setResizable(false);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == cancel){
            dispose();
            new Select_Course_UI(grading_system);
        } else if(e.getSource() == ok){
            filePath = file_path.getText();
            if (filePath.equals("")){
                JOptionPane.showMessageDialog(null,"Please import students file");
                new Add_Student_info_UI(grading_system,name,lecturerName,semesterName,course_ind);
            } else {
                int state = grading_system.addCourse(name, lecturerName, semesterName, filePath, course_ind);
                if(state == 1) {
                    dispose();
                    new Select_Course_UI(grading_system);
                }
                if(state == 2) {
                    JOptionPane.showMessageDialog(null,"Course name have conflict!");
                }
                if(state == 3) {
                    dispose();
                    JOptionPane.showMessageDialog(null,"Chosen Course is invalid!");
                }
                if(state == 4) {
                    dispose();
                    JOptionPane.showMessageDialog(null,"Course name cannot be empty!");
                }
                if(state == 5) {
                    dispose();
                    JOptionPane.showMessageDialog(null,"Lecturer name cannot be empty!");
                }
                if(state == 6) {
                    dispose();
                    JOptionPane.showMessageDialog(null,"Semester cannot be empty!");
                }
                if(state == 7) {
                    dispose();
                    JOptionPane.showMessageDialog(null,"Student information file name cannot be empty!");
                }
                if(state == 8) {
                    dispose();
                    JOptionPane.showMessageDialog(null,"Invalid format in student information file!");
                }
                if(state == 9) {
                    dispose();
                    JOptionPane.showMessageDialog(null,"Student information file not found!");
                }
                if(state == 10) {
                    dispose();
                    JOptionPane.showMessageDialog(null,"Unknown error!");
                }

            }
        } else if(e.getSource() == returnBack){
            dispose();
            new Add_Assignment_info_UI(grading_system,name, lecturerName, semesterName);
        } else if(e.getSource() == browse) {
            javax.swing.JFileChooser jFileChooser1 = new JFileChooser();
            try {
                jFileChooser1.showOpenDialog(null);
                File f = jFileChooser1.getSelectedFile();
                String fname = f.getAbsolutePath();
                file_path.setText(fname);
            }catch (Exception except) {
            }
        }
    }

}
