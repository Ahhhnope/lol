package ComponentGame;

import GameOBJ.Bullect;
import GameOBJ.Effect;
import GameOBJ.TheEndGame;
import GameOBJ.player;
import GameOBJ.rocket;
import MainGame.manHinhMenu;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PlaneGame extends JComponent {

    private Graphics2D g2;
    private BufferedImage offScreenImage;
    private TheEndGame endGamePanel;

    private int width;
    private int height;
    private Thread thread;
    private boolean start = true;
    private KEY key;
    private int shotTime;

    // Thêm biến đếm số đạn to hiện tại
    private int bigBulletCount = 0;
    private final int MAX_BIG_BULLETS = 5;

    // Tạo lớp đạn bổ sung trên bản đồ
    private class BulletPowerUp {

        private double x, y;
        private final int size = 20;
        private boolean active = true;
        private Image powerUpImage;

        public BulletPowerUp(double x, double y) {
            this.x = x;
            this.y = y;
            // Sử dụng hình ảnh hoặc đổi sang màu khác nếu không có hình
            try {
                powerUpImage = new ImageIcon(getClass().getResource("/GameImage/bullet_powerup.png")).getImage();
            } catch (Exception e) {
                // Nếu không có hình ảnh, sẽ vẽ hình tròn trong phương thức draw
            }
        }

        public void draw(Graphics2D g) {
            if (!active) {
                return;
            }

            if (powerUpImage != null) {
                g.drawImage(powerUpImage, (int) x, (int) y, size, size, null);
            } else {
                g.setColor(Color.ORANGE);
                g.fillOval((int) x, (int) y, size, size);
                g.setColor(Color.RED);
                g.drawOval((int) x, (int) y, size, size);
            }
        }

        public Area getShape() {
            return new Area(new Rectangle2D.Double(x, y, size, size));
        }

        public boolean isActive() {
            return active;
        }

        public void deactivate() {
            active = false;
        }
    }

    // Thêm danh sách các đạn bổ sung
    private List<BulletPowerUp> bulletPowerUps;

    //FPS GAME
    private final int FPS = 60;
    private final int TARGET_TIME = 1000000000 / FPS;
    //Game OBJ
    private player player;
    private List<Bullect> Bullects;
    private List<rocket> Rockets;
    private List<Effect> boomEffects;
    private int score = 0;

    private Image backgroundImage;
    private Image backButtonImage;
    private Image playerIconImage;

    // Định nghĩa vùng của nút back
    private Rectangle2D backButtonArea;

    public PlaneGame() {
        backgroundImage = new ImageIcon(getClass().getResource("/GameImage/background.png")).getImage();
        // Tải hình ảnh nút back - đảm bảo bạn có file hình này trong thư mục resources
        backButtonImage = new ImageIcon(getClass().getResource("/GameImage/Backimg.jpeg")).getImage();
        // Tải hình ảnh icon người chơi cho góc trên bên trái
        playerIconImage = new ImageIcon(getClass().getResource("/GameImage/AvatarPlayer.jpeg")).getImage();

        // Thêm mouse listener để xử lý sự kiện click vào nút back
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (backButtonArea != null && backButtonArea.contains(e.getPoint())) {
                    showPauseDialog();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (backButtonArea != null && backButtonArea.contains(e.getPoint())) {
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
                if (SwingUtilities.isLeftMouseButton(e)) {
                    key.setMOUSE_LEFT(true);
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    key.setMOUSE_RIGHT(true);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                if (SwingUtilities.isLeftMouseButton(e)) {
                    key.setMOUSE_LEFT(false);
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    key.setMOUSE_RIGHT(false);
                }
            }
        });

        // Thêm mouse motion listener để thay đổi con trỏ khi hover trên nút back
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (backButtonArea != null && backButtonArea.contains(e.getPoint())) {
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                } else {
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });
    }

    private void showPauseDialog() {
        // Tạm dừng game
        boolean originalStartState = start;
        start = false;
        
        
//        File imageCheck = new File("/GameImage/Backimg.jpeg");
//
//        if (imageCheck.exists()) {
//            System.out.println("Image file found!");
//
//        } else {
//            System.out.println("Image file not found!");
//        }
        // Tạo JDialog
        JDialog pauseDialog = new JDialog();
        pauseDialog.setUndecorated(true);
        pauseDialog.setSize(800, 400); // Tăng kích thước
        pauseDialog.setLocationRelativeTo(this);
        pauseDialog.setBackground(new Color(0, 0, 0, 0)); // Làm nền trong suốt

        // Panel chính với hình nền
        JPanel mainPanel = new JPanel() {
            private Image backgroundImage = new ImageIcon(getClass().getResource("/GameImage/MODEL_1.png")).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setLayout(new GridBagLayout()); // Căn giữa nội dung
        mainPanel.setOpaque(false);

        // Panel chứa button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 90, 10));
        buttonPanel.setOpaque(false);

        // Button Home bằng hình ảnh
        ImageIcon hombut = new ImageIcon(getClass().getResource("/GameImage/buttonHome.png"));
        Image temp = hombut.getImage().getScaledInstance(240, 80, Image.SCALE_DEFAULT);
        hombut = new ImageIcon(temp);
        JButton homeButton = new JButton(hombut);
        
        JButton button = new JButton(hombut);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        
//        homeButton.setPreferredSize(new Dimension(220, 120));
        homeButton.addActionListener(e -> {
            pauseDialog.dispose();
            returnToMainMenu();
        });

//        // Button Continue bằng hình ảnh
        JButton continueButton = new JButton(new ImageIcon(getClass().getResource("/GameImage/buttonNext.png")));
        continueButton.setPreferredSize(new Dimension(200, 120));
        continueButton.setContentAreaFilled(false);
        continueButton.setBorderPainted(false);
        continueButton.setFocusPainted(false);
        continueButton.addActionListener(e -> {
            pauseDialog.dispose();
            if (!start) {
                start = true;
                if (thread == null || !thread.isAlive()) {
                    start();
                }
            }
        });

        buttonPanel.add(homeButton);
        buttonPanel.add(continueButton);

        // Thêm buttonPanel vào mainPanel ở vị trí trung tâm
        mainPanel.add(buttonPanel);
        pauseDialog.add(mainPanel);
        pauseDialog.setVisible(true);
    }

// Thêm phương thức returnToMainMenu() trong class PlaneGame
    private void returnToMainMenu() {
        // Dừng game
        start = false;

        // Đảm bảo thread game được dừng đúng cách
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }

        // Ẩn cửa sổ game thay vì đóng hoàn toàn
        SwingUtilities.invokeLater(() -> {
            JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (currentFrame != null) {
                currentFrame.setVisible(false); // Ẩn cửa sổ hiện tại
            }

            // Hiển thị lại màn hình menu chính
            manHinhMenu menu = new manHinhMenu();
            menu.setVisible(true);
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (offScreenImage != null) {
            // Hiển thị buffer ra màn hình
            g.drawImage(offScreenImage, 0, 0, this);
        }
    }

    private void drawGame() {

        g2.drawImage(backgroundImage, 0, 0, width, height, null);

        // Vẽ nhân vật và các đối tượng khác (giữ nguyên code cũ)
        if (player.isALive()) {
            player.draw(g2);

            // Vẽ các đối tượng khác...
            synchronized (Bullects) {
                for (int i = 0; i < Bullects.size(); i++) {
                    Bullect bullect = Bullects.get(i);
                    if (bullect != null) {
                        bullect.draw(g2);
                    }
                }
            }

            synchronized (Rockets) {
                for (int i = 0; i < Rockets.size(); i++) {
                    rocket rocket = Rockets.get(i);
                    if (rocket != null) {
                        rocket.draw(g2);
                    }
                }
            }

            synchronized (boomEffects) {
                for (int i = 0; i < boomEffects.size(); i++) {
                    Effect effect = boomEffects.get(i);
                    if (effect != null) {
                        effect.draw(g2);
                    }
                }
            }

            // Vẽ các đạn powerup
            synchronized (bulletPowerUps) {
                for (BulletPowerUp powerup : bulletPowerUps) {
                    if (powerup.isActive()) {
                        powerup.draw(g2);
                    }
                }
            }

            // Vẽ giao diện người dùng
            drawUserInterface();
            drawBackButton();
        } else {
            // Khi người chơi đã chết, hiển thị TheEndGame thay vì màn hình Game Over cũ
            if (endGamePanel == null) {
                showEndGameScreen();
            }
        }
    }

    private void showEndGameScreen() {
        // Tạo panel TheEndGame
        endGamePanel = new TheEndGame();
        endGamePanel.setPlayerScore(score);
        endGamePanel.setBounds(0, 0, width, height);

        // Thêm panel vào component
        this.add(endGamePanel);
        this.revalidate();

        // Thêm key listener cho endGamePanel để bắt sự kiện Enter
        endGamePanel.setFocusable(true);
        endGamePanel.requestFocusInWindow();
        endGamePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Khởi động lại trò chơi
                    resetGame();
                    // Xóa endGamePanel
                    remove(endGamePanel);
                    endGamePanel = null;
                    revalidate();
                    repaint();
                    // Đặt lại focus cho game
                    requestFocusInWindow();
                }
            }
        });
    }

// Sửa phương thức resetGame
    private void resetGame() {
        score = 0;
        Rockets.clear();
        Bullects.clear();
        boomEffects.clear();
        bulletPowerUps.clear();
        bigBulletCount = 0;
        player.changeLocation(150, 150);
        player.reset();
        player.setAlive(true);  // Đảm bảo player được đánh dấu là còn sống

        // Đảm bảo nhân vật không di chuyển ngay sau khi nhấn Enter
        key.setKEY_LEFT(false);
        key.setKEY_RIGHT(false);
        key.setKEY_W(false);
        key.setKEY_S(false);
        key.setKEY_SPACE(false);
        key.setKEY_J(false);
        key.setKEY_K(false);
        key.setMOUSE_LEFT(false);
        key.setMOUSE_RIGHT(false);

        // Tạo đạn powerup mới
        addBulletPowerUp();
    }

    private void drawUserInterface() {
        // Vẽ icon người chơi
        int iconSize = 45;
        int padding = 5;
        if (playerIconImage != null) {
            g2.drawImage(playerIconImage, padding, padding, iconSize, iconSize, null);
        } else {
            // Vẽ một hình tượng trưng nếu không có image
            g2.setColor(Color.WHITE);
            g2.fillOval(padding, padding, iconSize, iconSize);
        }

        // Vẽ thanh máu
        int healthBarWidth = 150;
        int healthBarHeight = 15;
        int healthBarX = padding + iconSize + 10;
        int healthBarY = padding + 5;

        // Vẽ nền thanh máu
        g2.setColor(new Color(70, 70, 70));
        g2.fill(new Rectangle2D.Double(healthBarX, healthBarY, healthBarWidth, healthBarHeight));

        // Vẽ thanh máu hiện tại
        g2.setColor(new Color(253, 91, 91));
        double currentHealthWidth = (player.getHP() / player.getMaxHP()) * healthBarWidth;
        g2.fill(new Rectangle2D.Double(healthBarX, healthBarY, currentHealthWidth, healthBarHeight));

        // Vẽ viền cho thanh máu
        g2.setColor(Color.WHITE);
        g2.draw(new Rectangle2D.Double(healthBarX, healthBarY, healthBarWidth, healthBarHeight));

        // Hiển thị điểm số
        g2.setColor(Color.WHITE);
        g2.setFont(getFont().deriveFont(Font.BOLD, 15f));
        g2.drawString("Score: " + score, healthBarX, healthBarY + healthBarHeight + 15);

        // Hiển thị số đạn to còn lại
        g2.setColor(Color.YELLOW);
        g2.drawString("Đạn to: " + bigBulletCount + "/" + MAX_BIG_BULLETS, healthBarX, healthBarY + healthBarHeight + 35);
    }

    private void drawBackButton() {
        // Vẽ nút back ở góc trên bên phải
        int buttonWidth = 80;
        int buttonHeight = 30;
        int buttonX = width - buttonWidth - 10;
        int buttonY = 10;

        // Lưu vùng của nút back để xử lý sự kiện click
        backButtonArea = new Rectangle2D.Double(buttonX, buttonY, buttonWidth, buttonHeight);

        if (backButtonImage != null) {
            // Sử dụng hình ảnh cho nút back
            g2.drawImage(backButtonImage, buttonX, buttonY, buttonWidth, buttonHeight, null);
        } else {
            // Vẽ nút back thủ công nếu không có hình ảnh
            g2.setColor(new Color(50, 50, 50, 200));
            g2.fill(backButtonArea);
            g2.setColor(Color.WHITE);
            g2.draw(backButtonArea);

            g2.setFont(getFont().deriveFont(Font.BOLD, 16f));
            FontMetrics fm = g2.getFontMetrics();
            String backText = "BACK";
            Rectangle2D textBounds = fm.getStringBounds(backText, g2);
            int textX = buttonX + (buttonWidth - (int) textBounds.getWidth()) / 2;
            int textY = buttonY + (buttonHeight + (int) textBounds.getHeight()) / 2;
            g2.drawString(backText, textX, textY);
        }
    }

    public void start() {
        width = getWidth();
        height = getHeight();

        // Tạo BufferedImage để vẽ mượt
        offScreenImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2 = offScreenImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        initObjectGame();
        initKeyBoard();
        initMouse();
        initBullect();

        // Chạy Thread để xử lý logic game và FPS
        thread = new Thread(() -> {
            while (start) {
                long startTime = System.nanoTime();

                updateGame();
                // Vẽ toàn bộ game lên offScreenImage
                drawGame();
                // Hiển thị hình ảnh lên màn hình thông qua repaint()
                repaint();

                long time = System.nanoTime() - startTime;
                long sleepTime = (TARGET_TIME - time) / 1000000;
                if (sleepTime > 0) {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    private void updateGame() {
        if (player.isALive()) {
            player.update();
            limitPlayer(); // Giới hạn nhân vật trong màn hình
            checkBulletPowerUpCollision(); // Kiểm tra va chạm với đạn powerup
        }

        for (int i = 0; i < Rockets.size(); i++) {
            Rockets.get(i).update();
        }

        for (int i = 0; i < Bullects.size(); i++) {
            Bullects.get(i).update();
        }

        for (int i = 0; i < boomEffects.size(); i++) {
            boomEffects.get(i).update();
        }
    }

    // Kiểm tra va chạm người chơi với đạn powerup
    private void checkBulletPowerUpCollision() {
        synchronized (bulletPowerUps) {
            for (int i = 0; i < bulletPowerUps.size(); i++) {
                BulletPowerUp powerup = bulletPowerUps.get(i);
                if (powerup.isActive()) {
                    Area playerArea = player.getShape();
                    Area powerupArea = powerup.getShape();

                    playerArea.intersect(powerupArea);
                    if (!playerArea.isEmpty()) {
                        // Người chơi nhặt được đạn
                        powerup.deactivate();
                        bigBulletCount = Math.min(bigBulletCount + 1, MAX_BIG_BULLETS);
                        bulletPowerUps.remove(i);
                        i--;

                        // Tạo thêm đạn powerup mới sau một khoảng thời gian
                        new Thread(() -> {
                            sleep(10000); // Đợi 10 giây
                            addBulletPowerUp();
                        }).start();
                    }
                }
            }
        }
    }

    // Thêm đạn powerup vào bản đồ
    private void addBulletPowerUp() {
        if (start) {
            Random random = new Random();
            // Tạo vị trí ngẫu nhiên cho đạn powerup, tránh rìa màn hình
            int x = random.nextInt(width - 100) + 50;
            int y = random.nextInt(height - 100) + 50;

            synchronized (bulletPowerUps) {
                bulletPowerUps.add(new BulletPowerUp(x, y));
            }
        }
    }

    private void limitPlayer() {
        int playerX = (int) player.getX();
        int playerY = (int) player.getY();

        // Giới hạn trong màn hình
        if (playerX < 0) {
            player.changeLocation(0, playerY);
        }
        if (playerX > width - player.PLAYER_SILE) {
            player.changeLocation(width - player.PLAYER_SILE, playerY);
        }
        if (playerY < 0) {
            player.changeLocation(playerX, 0);
        }
        if (playerY > height - player.PLAYER_SILE) {
            player.changeLocation(playerX, height - player.PLAYER_SILE);
        }
    }

    private void addRocket() {
        Random ran = new Random();
        int locationY = ran.nextInt(height - 50) + 25;
        rocket rockets = new rocket();
        rockets.changeLocation(0, locationY);
        rockets.changeAngle(0);
        Rockets.add(rockets);
        int locationY2 = ran.nextInt(height - 50) + 25;
        rocket rockets2 = new rocket();
        rockets2.changeLocation(width, locationY2);
        rockets2.changeAngle(180);
        Rockets.add(rockets2);
    }

    private void initObjectGame() {
        player = new player();
        player.changeLocation(150, 150);
        Rockets = new ArrayList<>();
        boomEffects = new ArrayList<>();
        bulletPowerUps = new ArrayList<>();

        // Thêm đạn powerup đầu tiên
        addBulletPowerUp();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (start) {
                    addRocket();
                    sleep(3000);
                }
            }
        }).start();
    }

    private void initMouse() {
        // Mouse listener đã được thêm trong constructor
    }

    private void initKeyBoard() {
        key = new KEY();
        requestFocus();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    key.setKEY_LEFT(true);
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    key.setKEY_RIGHT(true);
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    key.setKEY_SPACE(true);
                } else if (e.getKeyCode() == KeyEvent.VK_J) {
                    key.setKEY_J(true);
                } else if (e.getKeyCode() == KeyEvent.VK_K) {
                    key.setKEY_K(true);
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    key.setKEY_ENTER(true);
                } else if (e.getKeyCode() == KeyEvent.VK_W) {
                    key.setKEY_W(true);
                } else if (e.getKeyCode() == KeyEvent.VK_S) {
                    key.setKEY_S(true);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    key.setKEY_LEFT(false);
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    key.setKEY_RIGHT(false);
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    key.setKEY_SPACE(false);
                } else if (e.getKeyCode() == KeyEvent.VK_J) {
                    key.setKEY_J(false);
                } else if (e.getKeyCode() == KeyEvent.VK_K) {
                    key.setKEY_K(false);
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    key.setKEY_ENTER(false);
                } else if (e.getKeyCode() == KeyEvent.VK_W) {
                    key.setKEY_W(false);
                } else if (e.getKeyCode() == KeyEvent.VK_S) {
                    key.setKEY_S(false);
                }
            }

        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                float s = 0.5f;

                while (start) {
                    if (player.isALive()) {
                        float angle = (float) player.getAngle();
                        if (key.isKEY_LEFT()) {
                            angle -= s;
                        }
                        if (key.isKEY_RIGHT()) {
                            angle += s;
                        }
                        if (key.isKEY_J() || key.isKEY_K() || key.isMOUSE_RIGHT() || key.isMOUSE_LEFT()) {
                            if (shotTime == 0) {
                                if (key.isKEY_J() || key.isMOUSE_RIGHT()) {
                                    // Regular bullet (small)
                                    Bullects.add(0, new Bullect(player.getX(), player.getY(), (float) player.getAngle(), 5, 3f));
                                } else if ((key.isKEY_K() || key.isMOUSE_LEFT()) && bigBulletCount > 0) {
                                    // Big bullet - only if we have power-ups available
                                    Bullects.add(0, new Bullect(player.getX(), player.getY(), (float) player.getAngle(), 20, 3f));
                                    // Reduce bullet count
                                    bigBulletCount--;
                                }
                            }
                            shotTime++;
                            if (shotTime == 15) {
                                shotTime = 0;
                            }
                        } else {
                            shotTime = 0;
                        }
                        if (key.isKEY_SPACE()) {
                            player.speedUp();
                        } else {
                            player.speedDown();
                        }

                        if (key.isKEY_W()) {
                            player.speedUp();
                        }
                        if (key.isKEY_S()) {

                            double angleRad = Math.toRadians(player.getAngle());
                            double moveX = -Math.cos(angleRad) * 0.5;
                            double moveY = -Math.sin(angleRad) * 0.5;
                            player.changeLocation(player.getX() + moveX, player.getY() + moveY);
                        }

                        player.update();
                        player.changeAngle(angle);
                    } else {
                        if (key.isKEY_ENTER()) {
                            resetGame();
                        }
                    }
                    for (int i = 0; i < Rockets.size(); i++) {
                        rocket rocketss = Rockets.get(i);
                        if (rocketss != null) {
                            rocketss.update();
                            if (!rocketss.check(width, height)) {
                                Rockets.remove(rocketss);

                            } else {
                                if (player.isALive()) {
                                    checkPlayer(rocketss);
                                }
                            }

                        }
                    }
                    sleep(5);
                }
            }
        }).start();
    }

    private void initBullect() {
        Bullects = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (start) {
                    for (int i = 0; i < Bullects.size(); i++) {
                        Bullect bullect = Bullects.get(i);
                        if (bullect != null) {
                            bullect.update();
                            checkBullects(bullect);
                            if (!bullect.check(width, height)) {
                                Bullects.remove(bullect);
                            }
                        } else {
                            Bullects.remove(bullect);
                        }
                    }
                    for (int i = 0; i < boomEffects.size(); i++) {
                        Effect BoomEffect = boomEffects.get(i);
                        if (BoomEffect != null) {
                            BoomEffect.update();
                            if (!BoomEffect.check()) {
                                boomEffects.remove(BoomEffect);
                            }
                        } else {
                            boomEffects.remove(BoomEffect);
                        }
                    }
                    sleep(1);
                }
            }
        }).start();
    }

    public void checkBullects(Bullect bullect) {
        synchronized (Rockets) {
            for (int i = 0; i < Rockets.size(); i++) {
                rocket rocketA = Rockets.get(i);
                if (rocketA != null) {
                    Area area = new Area(bullect.getShape());
                    area.intersect(rocketA.getShape());
                    if (!area.isEmpty()) {
                        synchronized (boomEffects) {
                            boomEffects.add(new Effect(bullect.getCenterX(), bullect.getCenterY(), 3, 5, 60, 0.5f, new Color(230, 207, 105)));
                        }
                        if (!rocketA.updateHP(bullect.getSize())) {
                            score++;
                            Rockets.remove(rocketA);
                            double x = rocketA.getX() + rocketA.ROCKET_SIZE / 2;
                            double y = rocketA.getY() + rocketA.ROCKET_SIZE / 2;
                            synchronized (boomEffects) {
                                boomEffects.add(new Effect(x, y, 5, 5, 75, 0.05f, new Color(32, 178, 169)));
                                boomEffects.add(new Effect(x, y, 5, 5, 75, 0.1f, new Color(32, 178, 169)));
                                boomEffects.add(new Effect(x, y, 10, 10, 100, 0.3f, new Color(230, 207, 105)));
                                boomEffects.add(new Effect(x, y, 10, 5, 100, 0.5f, new Color(255, 70, 70)));
                                boomEffects.add(new Effect(x, y, 10, 5, 150, 0.2f, new Color(255, 255, 255)));
                            }
                        }
                        synchronized (Bullects) {
                            Bullects.remove(bullect);
                        }
                    }
                }
            }
        }
    }

    public void checkPlayer(rocket rocketA) {
        if (rocketA != null) {
            Area area = new Area(player.getShape());
            area.intersect(rocketA.getShape());
            if (!area.isEmpty()) {
                double rocketHp = rocketA.getHP();
                if (!rocketA.updateHP(player.getHP())) {
                    Rockets.remove(rocketA);
                    double x = rocketA.getX() + rocketA.ROCKET_SIZE / 2;
                    double y = rocketA.getY() + rocketA.ROCKET_SIZE / 2;
                    boomEffects.add(new Effect(x, y, 5, 5, 75, 0.05f, new Color(32, 178, 169)));
                    boomEffects.add(new Effect(x, y, 5, 5, 75, 0.1f, new Color(32, 178, 169)));
                    boomEffects.add(new Effect(x, y, 10, 10, 100, 0.3f, new Color(230, 207, 105)));
                    boomEffects.add(new Effect(x, y, 10, 5, 100, 0.5f, new Color(255, 70, 70)));
                    boomEffects.add(new Effect(x, y, 10, 5, 150, 0.2f, new Color(255, 255, 255)));
                }

                if (!player.updateHP(rocketHp)) {
                    player.setAlive(false);
                    double x = player.getX() + player.PLAYER_SILE / 2;
                    double y = player.getY() + player.PLAYER_SILE / 2;
                    boomEffects.add(new Effect(x, y, 5, 5, 75, 0.05f, new Color(32, 178, 169)));
                    boomEffects.add(new Effect(x, y, 5, 5, 75, 0.1f, new Color(32, 178, 169)));
                    boomEffects.add(new Effect(x, y, 10, 10, 100, 0.3f, new Color(230, 207, 105)));
                    boomEffects.add(new Effect(x, y, 10, 5, 100, 0.5f, new Color(255, 70, 70)));
                    boomEffects.add(new Effect(x, y, 10, 5, 150, 0.2f, new Color(255, 255, 255)));
                }

            }
        }

    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    private void sleep(long speed) {
        try {
            Thread.sleep(speed);
        } catch (InterruptedException ex) {
            System.err.println(ex);
        }
    }
}
