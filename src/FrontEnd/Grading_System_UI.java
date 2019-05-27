package FrontEnd;


import BackEnd.Grading_System;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Grading_System_UI extends JFrame implements ActionListener{
    JButton login = new JButton("Login");
    JLabel log = new JLabel("Login");


    JLabel userName = new JLabel("username：");
    JLabel passWord = new JLabel("password: ");
    JTextField inputUserName = new JTextField();
    JPasswordField inputPassWord = new JPasswordField();

    JLabel img = new JLabel();

    String[] courseList;
    Grading_System grading_system;

    JSeparator jSeparator = new JSeparator();


    public Grading_System_UI(Grading_System gradingSystem){
        grading_system = gradingSystem;
        Container contentPane = this.getContentPane();
        contentPane.setLayout(null);

        login.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        login.setLabel("Log in");
        login.setForeground(Color.BLUE);
        login.addActionListener(this);


        log.setFont(new java.awt.Font("Lucida Grande", 1, 36)); // NOI18N
        log.setText("Login");

        jSeparator.setBackground(new java.awt.Color(0, 0, 0));

        userName.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        userName.setText("Username: ");

        passWord.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        passWord.setText("Password: ");

        img.setIcon(new ImageIcon("seal.gif"));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 127, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(userName)
                                        .addComponent(passWord))
                                .addGap(26, 26, 26)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(inputUserName)
                                        .addComponent(inputPassWord, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(105, 105, 105))
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(26, 26, 26)
                                                .addComponent(jSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(15, 15, 15)
                                                .addComponent(img)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(log, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(238, 238, 238)
                                                .addComponent(login, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(img, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(log, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(userName)
                                        .addComponent(inputUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(passWord)
                                        .addComponent(inputPassWord, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(login, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(47, Short.MAX_VALUE))
        );
        courseList = gradingSystem.getCourseList();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(640, 360);
        setTitle("Welcome");
        setResizable(false);
        setVisible(true);
    }

    public Grading_System getGradingSystem(){
        return this.grading_system;
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource() == login){
            if(inputUserName.getText().equals("cpk") && String.valueOf(inputPassWord.getPassword()).equals("1234")) {
                dispose();
                new Select_Course_UI(grading_system);
            }
            else {
                JOptionPane.showMessageDialog(null, "Incorrect username or password!");
            }
        }
    }
    public static void main(String[] args) {
        Grading_System_UI gradingSystemUI = new Grading_System_UI(new Grading_System());
    }
}