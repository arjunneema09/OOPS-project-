import javax.swing.*;

public class App {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Space Invaders");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(512, 512);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        // Set initial screen to Login
        LoginScreen loginScreen = new LoginScreen(frame);
        frame.setContentPane(loginScreen);

        frame.setVisible(true);
    }
}
