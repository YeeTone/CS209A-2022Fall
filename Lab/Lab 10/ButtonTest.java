package tutorial.lab10;

import javax.swing.*;

/**
 * @version 1.02 2018-05-01
 * @author Cay Horstmann
 */
public class ButtonTest
{
   public static void main(String[] args)
   {
         var frame = new ButtonFrame();
         frame.setTitle("ButtonTest");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setVisible(true);
   }
}
