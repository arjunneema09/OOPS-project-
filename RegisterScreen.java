import java.awt.*;
import java.io.*;
import javax.swing.*;

public class RegisterScreen extends JPanel {
    private JTextField userField;
    private JPasswordField passField;
    private JButton registerButton;
    private JFrame frame;

    public RegisterScreen(JFrame frame) {
        this.frame = frame;
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 245, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        JLabel titleLabel = new JLabel("Register");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 102, 255));
        add(titleLabel, gbc);

        gbc.gridy++; gbc.gridwidth = 1;
        add(new JLabel("New Username:"), gbc);
        gbc.gridx = 1;
        userField = new JTextField(15);
        add(userField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("New Password:"), gbc);
        gbc.gridx = 1;
        passField = new JPasswordField(15);
        add(passField, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        registerButton = new JButton("Register");
        registerButton.setBackground(new Color(51, 153, 255));
        registerButton.setForeground(Color.WHITE);
        add(registerButton, gbc);

        registerButton.addActionListener(e -> register());
    }

    private void register() {
        String username = userField.getText().trim();
        String password = new String(passField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Fields cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.csv", true))) {
            writer.write(username + "," + password);
            writer.newLine();
            JOptionPane.showMessageDialog(frame, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            frame.setContentPane(new LoginScreen(frame));
            frame.revalidate();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Could not save user.", "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
