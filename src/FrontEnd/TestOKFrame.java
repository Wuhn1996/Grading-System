package FrontEnd;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TestOKFrame extends JFrame implements ActionListener{
    JPanel pOK = new JPanel();
    JButton back = new JButton("Back");

    public TestOKFrame(){
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new GridLayout(2, 2));
        pOK.add(back);
        contentPane.add(pOK);
        back.addActionListener(this);
        setSize(400, 300);
        setTitle("Test OK");
        setResizable(false);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource() == back){
            dispose();
        }
    }
}
