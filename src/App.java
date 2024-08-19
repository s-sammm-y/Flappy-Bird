//window creation
import javax.swing.*;
public class App {
    public static void main(String[] args) {
        int boardWidth = 360;
        int boardHeight = 600;

        //setting up the window
        JFrame frame = new JFrame("Flappy Bird");
        //frame.setVisible(true);
        frame.setSize(boardWidth,boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //we need to add a jpanel for our canvas
        //creating an instance of FlappyBird class to set up canvas
        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird);
        frame.pack(); //we need dimensions 360 640 without including the title bar
        flappyBird.requestFocus();//for key listners
        frame.setVisible(true);
    }
}
