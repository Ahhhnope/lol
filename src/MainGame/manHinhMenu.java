package MainGame;

import utilz.LoadSave;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class manHinhMenu extends JFrame {

    private JButton btnPlay, btnMenu, btnExit;
    public int screenWidth = 1366;
    public int screenheight = 768;

    public manHinhMenu() {
        setTitle("Game 2D - Huy Binh");
        setSize(1366, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        BufferedImage background = LoadSave.getImage(LoadSave.background);
        BufferedImage gameTitle = LoadSave.getImage(LoadSave.gameTitle);

        // Tạo JPanel chứa background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                g.drawImage(background, 0, 0, screenWidth, screenheight, null);
                g.drawImage(gameTitle, 25, 60, gameTitle.getWidth(), gameTitle.getHeight(), null);


            }
        };
        mainPanel.setLayout(new GridBagLayout());

        JLabel title = new JLabel();
        title.setFont(new Font("Arial", Font.BOLD, 48));
        title.setForeground(Color.WHITE);

        btnPlay = createTextButton("", "PlayButton1.png", 250, 70);
        btnMenu = createTextButton("", "Menu1.png", 250, 70);
        btnExit = createTextButton("", "Exut.png", 250, 70);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(40, 0, 30, 0);
        mainPanel.add(title, gbc);

        gbc.insets = new Insets(60, 0, 10, 0);
        mainPanel.add(btnPlay, gbc);
        mainPanel.add(btnMenu, gbc);
        mainPanel.add(btnExit, gbc);

        add(mainPanel);

        btnPlay.addActionListener(e -> startGame());
        btnMenu.addActionListener(e -> openMenu());
        btnExit.addActionListener(e -> System.exit(0));

    }

    public void importMenuButton() {

    }

    private JButton createImageButton(String imagePath, int width, int height) {
        java.net.URL imgURL = getClass().getClassLoader().getResource(imagePath);
        if (imgURL == null) {
            System.err.println("Không tìm thấy hình ảnh: " + imagePath);
            return new JButton();
        }
        ImageIcon icon = new ImageIcon(imgURL);
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        JButton button = new JButton(new ImageIcon(img));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        return button;
    }

    private JButton createTextButton(String text, String imagePath, int width, int height) {
        JButton button = createImageButton(imagePath, width, height);
        button.setText(text);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setForeground(Color.WHITE);
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.CENTER);
        return button;
    }

    private void startGame() {
        JFrame gameFrame = new JFrame("Game 2D - Huy Binh");
        gameFrame.setSize(1366, 768);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setResizable(false);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setLayout(new BorderLayout());

        try {
            ComponentGame.PlaneGame planeGame = new ComponentGame.PlaneGame();
            gameFrame.add(planeGame);

            gameFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowOpened(WindowEvent e) {
                    planeGame.start();
                }
            });

            gameFrame.setVisible(true);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi khởi động game!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void openMenu() {
        new MenuGame();
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(manHinhMenu::new);
    }
}
