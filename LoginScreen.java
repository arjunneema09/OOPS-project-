import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class LoginScreen extends JPanel {
    private JTextField userField;
    private JPasswordField passField;
    private JButton loginButton, registerButton;
    private JFrame frame;

    public LoginScreen(JFrame frame) {
        this.frame = frame;
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 248, 255)); // Soft background

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 20, 12, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        JLabel titleLabel = new JLabel("Welcome Back 👋");
        titleLabel.setFont(new Font("San Francisco", Font.BOLD, 28));
        titleLabel.setForeground(new Color(0, 0, 0));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, gbc);

        gbc.gridy++;
        JLabel subtitle = new JLabel("Please login to continue");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(Color.BLACK);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(subtitle, gbc);

        gbc.gridy++; gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        addLabel("Username:", gbc, 0, 2);
        userField = createInputField();
        gbc.gridx = 1;
        add(userField, gbc);

        gbc.gridy++; gbc.gridx = 0;
        addLabel("Password:", gbc, 0, 3);
        passField = new JPasswordField(15);
        styleField(passField);
        gbc.gridx = 1;
        add(passField, gbc);

        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginButton = createButton("Login", new Color(0, 0, 0));
        add(loginButton, gbc);

        gbc.gridy++;
        registerButton = createButton("Register", new Color(0, 0, 0));
        add(registerButton, gbc);

        loginButton.addActionListener(e -> login());
        registerButton.addActionListener(e -> {
            frame.setContentPane(new RegisterScreen(frame));
            frame.revalidate();
        });
    }

    private void addLabel(String text, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        JLabel label = new JLabel(text);
        label.setFont(new Font("San Francisco", Font.PLAIN, 14));
        label.setForeground(new Color(0, 0, 0));
        add(label, gbc);
    }

    private JTextField createInputField() {
        JTextField field = new JTextField(15);
        styleField(field);
        return field;
    }

    private void styleField(JComponent field) {
        field.setFont(new Font("San Francisco", Font.PLAIN, 14));
        field.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        field.setBackground(Color.white);
        field.setOpaque(true);
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("San Francisco", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setContentAreaFilled(true);
        return button;
    }

    private void login() {
        String username = userField.getText().trim();
        String password = new String(passField.getPassword()).trim();
        boolean isValid = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("users.csv"))) {
            String line;
            reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    isValid = true;
                    break;
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error reading users.csv", "File Error", JOptionPane.ERROR_MESSAGE);
        }

        if (isValid) {
            frame.setContentPane(new MainMenu(frame));
            frame.revalidate();
        } else {
            JOptionPane.showMessageDialog(frame, "Incorrect Username or Password", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 300);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(new LoginScreen(frame));
        frame.setVisible(true);
    }
}
