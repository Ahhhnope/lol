/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MainGame;

import javax.swing.JFrame;
import javax.swing.*;
import java.awt.*;

public class LeaderBoardGame extends JFrame {

    private JButton btnBack, btnHome;

    public LeaderBoardGame() {
        setTitle("Menu - Game 2D");
        setSize(1366, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundIcon = new ImageIcon(getClass().getClassLoader().getResource("GameImage/manHinhNenScore.jpeg"));
                if (backgroundIcon.getImage() != null) {
                    g.drawImage(backgroundIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        mainPanel.setLayout(null);
        add(mainPanel);

        btnBack = createTextButton("Back", "GameImage/thanhMENU.png", 60, 350, 120, 60);
        btnHome = createTextButton("Home", "GameImage/thanhMENU.png", 1170, 350, 120, 60);

        mainPanel.add(btnBack);
        mainPanel.add(btnHome);
        
        btnBack.addActionListener(e -> backToMenuGame());
        btnHome.addActionListener(e -> Home());
        
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
    
     private void backToMenuGame() {
       this.setVisible(false);
        new MenuGame().setVisible(true);
    }

    private void Home() {
        this.setVisible(false);
        new manHinhMenu().setVisible(true);
    }
}
