package MainGame;

import javax.swing.JFrame;
import javax.swing.*;
import java.awt.*;

public class MenuGame extends JFrame {
    
    private JButton btnBack, btnNext, btnLeft, btnRight;
    
    public MenuGame() {
        setTitle("Menu - Game 2D");
        setSize(1366, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundIcon = new ImageIcon(getClass().getClassLoader().getResource("Menu.png"));
                if (backgroundIcon.getImage() != null) {
                    g.drawImage(backgroundIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        mainPanel.setLayout(null);
        add(mainPanel);
        
        btnBack = createTextButton("<", "GameImage/thanhMENU.png", 60, 350, 120, 60);
        btnNext = createTextButton(">", "GameImage/thanhMENU.png", 1170, 350, 120, 60);
        btnLeft = createTextButton("Play", "GameImage/thanhMENU.png", 460, 450, 120, 60);
        btnRight = createTextButton("Play", "GameImage/thanhMENU.png", 790, 450, 120, 60);
        
        mainPanel.add(btnBack);
        mainPanel.add(btnNext);
        mainPanel.add(btnLeft);
        mainPanel.add(btnRight);
        
        btnBack.addActionListener(e -> backToMenu());
        btnNext.addActionListener(e -> goToLeaderBoard());
        btnLeft.addActionListener(e -> goToGameLevelOne());
        btnRight.addActionListener(e -> goToGameLevelTwo());
        
        setVisible(true);
    }
    
    private JButton createTextButton(String text, String imagePath, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.CENTER);
        
        try {
            ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(imagePath));
            Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.err.println("Không tìm thấy hình ảnh: " + imagePath);
        }
        
        return button;
    }
    
    private void backToMenu() {
        this.setVisible(false);
        new manHinhMenu().setVisible(true);
    }
    
    private void goToLeaderBoard() {
        this.setVisible(false);
        new LeaderBoardGame().setVisible(true);
    }
    
    private void goToGameLevelOne() {
        
        this.setVisible(false);
        
        JFrame gameLevelOneFrame = new JFrame("Game Level One");
        gameLevelOneFrame.setSize(1366, 768);
        gameLevelOneFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameLevelOneFrame.setLocationRelativeTo(null);
        gameLevelOneFrame.setResizable(false);

        // Tạo GameLeverOne component và thêm vào JFrame
        GameLeverOne gameLevelOne = new GameLeverOne();
        gameLevelOneFrame.add(gameLevelOne);

        // Hiển thị JFrame và khởi động game
        gameLevelOneFrame.setVisible(true);
        gameLevelOne.requestFocusInWindow();        
        gameLevelOne.start();
    }
    
    private void goToGameLevelTwo() {
        this.setVisible(false);
        
        JFrame gameleverTwoFrame = new JFrame("Game Lever Two");
        gameleverTwoFrame.setSize(1366,768);
        gameleverTwoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameleverTwoFrame.setLocationRelativeTo(null);
        gameleverTwoFrame.setResizable(false);
        
        GameLeverTwo gamleverTwo = new GameLeverTwo();
        gameleverTwoFrame.add(gamleverTwo);
        
        gameleverTwoFrame.setVisible(true);
        gameleverTwoFrame.requestFocusInWindow();
        gamleverTwo.start();
        

    }
    
}
