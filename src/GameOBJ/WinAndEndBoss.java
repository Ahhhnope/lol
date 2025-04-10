package GameOBJ;

import MainGame.GameLeverOne;
import MainGame.manHinhMenu;
import javax.swing.JPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WinAndEndBoss extends JPanel {

    private Image backgroundImage;
    private Image victoryImage;
    private Image defeatImage;
    private JTextField nameField;
    private String playerName = "";
    private int playerScore = 0;
    private int damageDealt = 0;
    private boolean isVictory;
    private boolean showFinalScreen = false;
    private boolean bossDefeated = false;
    private Image homeButtonImg;
    private Image replayButtonImg;
    private Image nextButtonImg;
    private boolean showNameError = false;

    // Constants for layout
    private final int DIALOG_WIDTH = 1000;
    private final int DIALOG_HEIGHT = 550;

    private int bossMaxHP = 1000;

    public WinAndEndBoss() {
        setLayout(null);

        // Load images
        try {
            backgroundImage = new ImageIcon(getClass().getResource("/GameImage/ketthucgame.jpg")).getImage();
            nextButtonImg = new ImageIcon(getClass().getResource("/GameImage/buttonNext.png")).getImage();
            homeButtonImg = new ImageIcon(getClass().getResource("/GameImage/buttonHome.png")).getImage();
            replayButtonImg = new ImageIcon(getClass().getResource("/GameImage/buttonReplay.png")).getImage();
        } catch (Exception e) {
            System.err.println("Cannot load images: " + e.getMessage());
        }

        // Create name field
        nameField = new JTextField(15);
        nameField.setFont(new Font("Arial", Font.BOLD, 16));
        setFocusable(true);

        // Add key listener directly to the name field
        nameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String playerNameInput = nameField.getText().trim();
                    if (!playerNameInput.isEmpty()) {
                        processNameInput();
                    } else {
                        showNameError = true;
                        repaint();
                    }
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (showFinalScreen) {
                    // Lấy kích thước thực của panel
                    int width = getWidth();
                    int height = getHeight();
                    int centerX = width / 2;
                    int centerY = height / 2;

                    // Home button area
                    Rectangle homeRect = new Rectangle(centerX - 150, centerY + 70, 160, 60);
                    // Replay button area
                    Rectangle replayRect = new Rectangle(centerX + 20, centerY + 70, 160, 60);

                    if (homeRect.contains(e.getPoint())) {
                        goToHomeScreen();
                    } else if (replayRect.contains(e.getPoint())) {
                        replayGame();
                    }
                }
            }
        });
    }

    private void processNameInput() {
        playerName = nameField.getText().trim();
        if (playerName.isEmpty()) {
            playerName = "Player";
        }
        showFinalScreen = true;
        showNameError = false;
        nameField.setVisible(false);
        requestFocusInWindow();
        repaint();
    }

    private void goToHomeScreen() {
        // Đóng frame hiện tại
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        if (parentWindow != null) {
            parentWindow.dispose();
        }

        // Mở màn hình chính
        SwingUtilities.invokeLater(() -> {
            manHinhMenu mainMenu = new manHinhMenu();
            mainMenu.setVisible(true);
        });
    }

    private void replayGame() {
        // Đóng frame hiện tại
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        if (parentWindow != null) {
            parentWindow.dispose();
        }

        // Mở frame game mới
        SwingUtilities.invokeLater(() -> {
            JFrame gameFrame = new JFrame("Game Lever One");
            gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gameFrame.setSize(1366, 768);
            gameFrame.setLocationRelativeTo(null);

            GameLeverOne game = new GameLeverOne();
            gameFrame.add(game);
            gameFrame.setVisible(true);
            game.start();
        });
    }

    public void setBossDefeated(boolean defeated) {
        this.bossDefeated = defeated;
        this.isVictory = defeated;
    }

    public void setPlayerScore(int score) {
        this.playerScore = score;
    }

    public void setDamageDealt(int damage) {
        this.damageDealt = damage;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void showNameInputScreen() {
        removeAll();

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        nameField.setBounds(centerX - 100, centerY, 200, 30);
        nameField.setText("");
        add(nameField);

        nameField.setVisible(true);
        nameField.requestFocusInWindow();

        showNameError = false;
        showFinalScreen = false;

        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ background
        g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        if (!showFinalScreen) {
            Image endGameBackground = new ImageIcon(getClass().getResource(isVictory ? "/GameImage/WinGame.png" : "/GameImage/EndGame.png")).getImage();
            g2.drawImage(endGameBackground, centerX - DIALOG_WIDTH / 2, centerY - DIALOG_HEIGHT / 2, DIALOG_WIDTH, DIALOG_HEIGHT, this);

            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.setColor(Color.WHITE);
            g2.drawString("Name:", centerX - 150, centerY + 20);

            g2.setFont(new Font("Arial", Font.BOLD, 20));
            g2.drawString("Nhấp Enter để tiếp tục", centerX - 110, centerY + 75);

            if (showNameError) {
                g2.setColor(Color.RED);
                g2.drawString("Bạn phải nhập tên để tiếp tục", centerX - 150, centerY + 130);
            }
        } else {
            Image finalInfoImage = new ImageIcon(getClass().getResource(isVictory ? "/GameImage/ThongTinWinGame.png" : "/GameImage/ThongTinEndGame.png")).getImage();
            g2.drawImage(finalInfoImage, centerX - DIALOG_WIDTH / 2, centerY - DIALOG_HEIGHT / 2, DIALOG_WIDTH, DIALOG_HEIGHT, this);

            Font statsFont = new Font("Arial", Font.BOLD, 20);
            g2.setFont(statsFont);
            g2.setColor(Color.WHITE);

            String nameText = "Tên game: " + playerName;
            String scoreText = "Điểm của bạn: " + playerScore;
            String damageText = "Gây sát thương: " + damageDealt;
            String hpBossText = "HP boss còn lại: " + (bossMaxHP - damageDealt) + "/" + bossMaxHP;

            g2.drawString(nameText, centerX - 150, centerY - 100);
            g2.drawString(scoreText, centerX - 150, centerY - 60);
            g2.drawString(damageText, centerX - 150, centerY - 15);
            g2.drawString(hpBossText, centerX - 150, centerY + 30);

            // Vẽ thanh máu boss
            int healthBarWidth = 300;
            int healthBarHeight = 20;
            int healthX = centerX - healthBarWidth / 2;
            int healthY = getHeight() - 320; 

            float hpPercent = (float) (bossMaxHP - damageDealt) / bossMaxHP;
            int currentHealthWidth = (int) (healthBarWidth * hpPercent);

            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(healthX, healthY, healthBarWidth, healthBarHeight);

            g2.setColor(Color.RED);
            g2.fillRect(healthX, healthY, currentHealthWidth, healthBarHeight);

            g2.setColor(Color.WHITE);
            g2.drawRect(healthX, healthY, healthBarWidth, healthBarHeight);
            if (homeButtonImg != null) {
                g2.drawImage(homeButtonImg, centerX - 150, centerY + 80, 160, 60, null);
            }

            if (replayButtonImg != null) {
                g2.drawImage(replayButtonImg, centerX + 20, centerY + 80, 160, 60, null);
            }
        }
    }

    private void drawButton(Graphics2D g2, String text, int x, int y, int width, int height) {
        // Button background
        g2.setColor(new Color(0, 102, 204));
        g2.fillRoundRect(x, y, width, height, 10, 10);

        // Button border
        g2.setColor(new Color(0, 150, 255));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, width, height, 10, 10);

        // Button text
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics fm = g2.getFontMetrics();
        int textX = x + (width - fm.stringWidth(text)) / 2;
        int textY = y + (height + fm.getAscent() - fm.getDescent()) / 2;
        g2.drawString(text, textX, textY);
    }
}
