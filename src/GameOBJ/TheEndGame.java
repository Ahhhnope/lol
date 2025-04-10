
package GameOBJ;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class TheEndGame extends JPanel{
    private JTextField nameField;
    private JButton nextButton;
    private String playerName = "";
    private int playerScore = 0;
    private boolean showNameInput = true;
    private boolean showGameSummary = false;
    private Image backgroundImage;

    public TheEndGame() {
        setLayout(new GridBagLayout()); // Căn giữa nội dung
        setOpaque(false);
        
        try {
            backgroundImage = new ImageIcon(getClass().getResource("/GameImage/BANNER_1.png")).getImage();
        } catch (Exception e) {
            System.err.println("Không thể tải hình nền: " + e.getMessage());
        }
        
        createNameInputUI();
    }

    private void createNameInputUI() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel nameLabel = new JLabel("Hãy nhập tên game của bạn:");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(nameLabel, gbc);

        gbc.gridy++;
        nameField = new JTextField(15);
        nameField.setFont(new Font("Arial", Font.PLAIN, 20));
        add(nameField, gbc);

        gbc.gridy++;
        nextButton = new JButton("Next");
        nextButton.setFont(new Font("Arial", Font.BOLD, 20));
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerName = nameField.getText().trim();
                if (!playerName.isEmpty()) {
                    showNameInput = false;
                    showGameSummary = true;
                    switchToGameSummary();
                }
            }
        });
        add(nextButton, gbc);
    }

    private void switchToGameSummary() {
        removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel gameOverLabel = new JLabel("Trò chơi kết thúc");
        gameOverLabel.setForeground(Color.WHITE);
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 28));
        add(gameOverLabel, gbc);

        gbc.gridy++;
        JLabel gameNameLabel = new JLabel("Tên game: " + playerName);
        gameNameLabel.setForeground(Color.WHITE);
        gameNameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(gameNameLabel, gbc);

        gbc.gridy++;
        JLabel scoreLabel = new JLabel("Điểm của bạn: " + playerScore);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(scoreLabel, gbc);

        gbc.gridy++;
        JLabel restartLabel = new JLabel("Nhấn phím Enter để bắt đầu lại trò chơi");
        restartLabel.setForeground(Color.WHITE);
        restartLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(restartLabel, gbc);

        revalidate();
        repaint();
    }

    public void setPlayerScore(int score) {
        this.playerScore = score;
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            int newWidth = getWidth() * 3 / 4;
            int newHeight = getHeight() * 3 / 4;
            int x = (getWidth() - newWidth) / 2;
            int y = (getHeight() - newHeight) / 2;
            g.drawImage(backgroundImage, x, y, newWidth, newHeight, this);
        }
    }
}
