import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainMenu extends JPanel {
    private JFrame frame; // Store the JFrame

    public MainMenu(JFrame frame) {
        this.frame = frame; // Store the frame
        setLayout(new GridBagLayout()); // Use GridBagLayout for better control

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1; // Make buttons expand horizontally
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("Main Menu");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 0, 0));
        gbc.gridwidth = 1;
        add(titleLabel, gbc);

        gbc.gridy++;
        JButton startGameButton = new JButton("Start Game");
        startGameButton.setFont(new Font("Arial", Font.BOLD, 16));
        startGameButton.setBackground(new Color(0, 0, 0));
        startGameButton.setForeground(Color.black);
        startGameButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SpaceInvadersPanel gamePanel = new SpaceInvadersPanel();
                frame.setContentPane(gamePanel);
                frame.setSize(gamePanel.getPreferredSize()); // Set the frame size
                frame.revalidate();
                frame.repaint(); // Ensure the game panel is drawn
                gamePanel.requestFocusInWindow(); // Give focus to the game panel
            }
        });
        add(startGameButton, gbc);

        gbc.gridy++;
        JButton creditsButton = new JButton("Credits");
        creditsButton.setFont(new Font("Arial", Font.BOLD, 16));
        creditsButton.setBackground(new Color(0, 0, 0));
        creditsButton.setForeground(Color.black);
        creditsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        creditsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Credits: Arjun, Adit", "Credits",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        add(creditsButton, gbc);

        gbc.gridy++;
        JButton quitButton = new JButton("Quit");
        quitButton.setFont(new Font("Arial", Font.BOLD, 16));
        quitButton.setBackground(new Color(0, 0, 0));
        quitButton.setForeground(Color.black);
        quitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(quitButton, gbc);

        // Add an empty label for spacing at the bottom
        gbc.gridy++;
        gbc.weighty = 1;
        add(new JLabel(), gbc);
    }
}

