package MainGame;

import ComponentGame.KEY;
import GameOBJ.Bullect;
import GameOBJ.Effect;
import GameOBJ.ModelGameBossTwo;
import GameOBJ.TheEndGame;
import GameOBJ.WinAndEndBossTwo;
import GameOBJ.player;
import GameOBJ.rocket;
import GameOBJ.rocketBossTwo;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GameLeverTwo extends JComponent {

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
    private final int MAX_BIG_BULLETS = 15;

    private int countdownTimer = 15;
    private boolean showCountdown = true;
    private boolean showIntroScreen = false;
    private ModelGameBossTwo boss;

    private List<rocketBossTwo> bossBullets;
    private int totalDamageDealt = 0;
    private boolean gameEnded = false;

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

    public GameLeverTwo() {
        backgroundImage = new ImageIcon(getClass().getResource("/GameImage/GameLVtwo.jpg")).getImage();
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
        JButton homeButton = new JButton(new ImageIcon(getClass().getResource("/GameImage/HOMEGAME.png")));
        homeButton.setPreferredSize(new Dimension(220, 120));
        homeButton.setContentAreaFilled(false);
        homeButton.setBorderPainted(false);
        homeButton.setFocusPainted(false);
        homeButton.addActionListener(e -> {
            pauseDialog.dispose();
            returnToMainMenu();
        });

        // Button Continue bằng hình ảnh
        JButton continueButton = new JButton(new ImageIcon(getClass().getResource("/GameImage/BACKGAME.png")));
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
        if (showCountdown) {
            // Vẽ số đếm ngược ở giữa màn hình
            g2.setColor(Color.YELLOW);
            g2.setFont(getFont().deriveFont(Font.BOLD, 48f));
            FontMetrics fm = g2.getFontMetrics();
            String countText = String.valueOf(countdownTimer);
            Rectangle2D textBounds = fm.getStringBounds(countText, g2);
            int textX = (width - (int) textBounds.getWidth()) / 2;
            int textY = (height + (int) textBounds.getHeight()) / 2;
            g2.drawString(countText, textX, textY);
        } else if (showIntroScreen) {
            // Giảm kích thước ảnh
            Image introImage = new ImageIcon(getClass().getResource("/GameImage/thongtinbosstwo.png")).getImage();
            int newWidth = (int) (width * 0.65);
            int newHeight = (int) (height * 0.65);
            int centerX = (width - newWidth) / 2;
            int centerY = (height - newHeight) / 2;

            // Chỉ vẽ màn hình giới thiệu, không vẽ nhân vật game
            g2.drawImage(introImage, centerX, centerY, newWidth, newHeight, null);

            // Thêm hướng dẫn nhấn Enter
            g2.setColor(Color.WHITE);
            g2.setFont(getFont().deriveFont(Font.BOLD, 24f));
            String enterText = "";
            FontMetrics fm = g2.getFontMetrics();
            Rectangle2D textBounds = fm.getStringBounds(enterText, g2);
            int textX = (width - (int) textBounds.getWidth()) / 2;
            int textY = height - 100;
            g2.drawString(enterText, textX, textY);

            // Dừng mọi nhân vật game
            return;
        }
        if (boss.isActive()) {
            boss.draw(g2, width, height);
        }

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

            synchronized (bossBullets) {
                for (int i = 0; i < bossBullets.size(); i++) {
                    rocketBossTwo bullet = bossBullets.get(i);
                    if (bullet != null) {
                        bullet.draw(g2);
                    }
                }
            }

            // Vẽ giao diện người dùng
            drawUserInterface();
            drawBackButton();
        } else {
            if (boss.isActive()) {
                boss.draw(g2, width, height);
            }

            // For other game elements you want to keep visible
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

            // Show end game screen
            if (endGamePanel == null && !gameEnded) {
                showEndGameScreen();
            }
        }
    }

    private void showEndGameScreen() {
        if (gameEnded) {
            return;
        }
        gameEnded = true;

        // Tạo WinAndEndBoss panel thay vì TheEndGame
        WinAndEndBossTwo endScreen = new WinAndEndBossTwo();
        endScreen.setBossDefeated(false); // Player thua
        endScreen.setPlayerScore(score);
        endScreen.setDamageDealt(totalDamageDealt);
        endScreen.setBounds(0, 0, width, height);

        // Thêm panel vào component
        this.add(endScreen);
        endScreen.showNameInputScreen();
        this.revalidate();

        // Panel nhận focus
        endScreen.setFocusable(true);
        endScreen.requestFocusInWindow();
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

        if (boss.isActive()) {
            bossShooting();

            // Kiểm tra va chạm giữa player và boss
            if (player.isALive()) {
                Area playerArea = player.getShape();
                Area bossArea = boss.getShape();
                playerArea.intersect(bossArea);

                if (!playerArea.isEmpty()) {
                    player.setAlive(false);

                    // Hiệu ứng nổ
                    double x = player.getX() + player.PLAYER_SILE / 2;
                    double y = player.getY() + player.PLAYER_SILE / 2;
                    boomEffects.add(new Effect(x, y, 10, 10, 100, 0.3f, new Color(230, 207, 105)));
                    boomEffects.add(new Effect(x, y, 10, 5, 100, 0.5f, new Color(255, 70, 70)));
                }
            }

            // Update boss bullets
            for (int i = 0; i < bossBullets.size(); i++) {
                rocketBossTwo bullet = bossBullets.get(i);
                if (bullet != null) {
                    bullet.update();

                    // Check collision with player
                    if (player.isALive()) {
                        Area playerArea = player.getShape();
                        Area bulletArea = bullet.getShape();
                        playerArea.intersect(bulletArea);

                        if (!playerArea.isEmpty()) {
                            // Player hit by boss bullet
                            if (!player.updateHP(bullet.getDamage())) {
                                player.setAlive(false);
                                // Add explosion effect
                                double x = player.getX() + player.PLAYER_SILE / 2;
                                double y = player.getY() + player.PLAYER_SILE / 2;
                                boomEffects.add(new Effect(x, y, 10, 10, 100, 0.3f, new Color(230, 207, 105)));
                                boomEffects.add(new Effect(x, y, 10, 5, 100, 0.5f, new Color(255, 70, 70)));
                            }

                            // Remove the bullet
                            bossBullets.remove(bullet);
                            i--;
                            continue;
                        }
                    }

                    // Remove bullet if out of bounds
                    if (!bullet.check(width, height)) {
                        bossBullets.remove(bullet);
                        i--;
                    }
                }
            }
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
        boss = new ModelGameBossTwo();
        boss.setLocation(width, height);
        player = new player();
        player.changeLocation(150, 150);
        bossBullets = new ArrayList<>();

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

        new Thread(() -> {
            while (countdownTimer > 0 && start) {
                sleep(1000); // Đợi 1 giây
                countdownTimer--;
            }
            if (start) {
                showCountdown = false;
                showIntroScreen = true; // Hiển thị màn hình giới thiệu sau khi đếm ngược
            }
        }).start();

    }

    private void bossShooting() {
        if (boss.isActive() && player.isALive() && !showCountdown && !showIntroScreen) {

            if (Math.random() < 0.01) {

                double bossX = boss.getShape().getBounds().getCenterX();
                double bossY = boss.getShape().getBounds().getCenterY();

                double playerX = player.getX() + player.PLAYER_SILE / 2;
                double playerY = player.getY() + player.PLAYER_SILE / 2;

                rocketBossTwo bullet = new rocketBossTwo(bossX, bossY, playerX, playerY);
                bossBullets.add(bullet);
            }
        }
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
                    if (showIntroScreen) {
                        showIntroScreen = false;
                        boss.setActive(true); // Kích hoạt boss
                    }
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
        boolean bulletHit = false;  // Thêm biến để theo dõi đạn có va chạm chưa

        // Kiểm tra va chạm với tên lửa
        synchronized (Rockets) {
            for (int i = 0; i < Rockets.size(); i++) {
                rocket rocketA = Rockets.get(i);
                if (rocketA != null) {
                    Area area = new Area(bullect.getShape());
                    area.intersect(rocketA.getShape());
                    if (!area.isEmpty()) {
                        bulletHit = true;  // Đánh dấu đạn đã va chạm

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
                        break;  // Thoát khỏi vòng lặp vì đạn đã bị tiêu diệt
                    }
                }
            }
        }

        // Kiểm tra va chạm với boss nếu đạn chưa va chạm với tên lửa
        if (!bulletHit && boss.isActive()) {
            Area area = new Area(bullect.getShape());
            area.intersect(boss.getShape());
            if (!area.isEmpty()) {
                bulletHit = true;  // Đánh dấu đạn đã va chạm

                // Add damage to total damage counter
                totalDamageDealt += bullect.getSize();

                synchronized (boomEffects) {
                    boomEffects.add(new Effect(bullect.getCenterX(), bullect.getCenterY(), 3, 5, 60, 0.5f, new Color(230, 207, 105)));
                }
                if (!boss.updateHP(bullect.getSize())) {
                    score += 100; // Thêm điểm khi đánh bại boss
                    boss.setActive(false);
                    // Show win screen when boss is defeated
                    showWinScreen();

                    double x = boss.getShape().getBounds().getCenterX();
                    double y = boss.getShape().getBounds().getCenterY();
                    synchronized (boomEffects) {
                        boomEffects.add(new Effect(x, y, 10, 10, 100, 0.3f, new Color(230, 207, 105)));
                        boomEffects.add(new Effect(x, y, 10, 5, 100, 0.5f, new Color(255, 70, 70)));
                        boomEffects.add(new Effect(x, y, 15, 15, 150, 0.2f, new Color(255, 255, 255)));
                    }
                }
            }
        }

        // Xóa đạn nếu đã va chạm
        if (bulletHit) {
            synchronized (Bullects) {
                Bullects.remove(bullect);
            }
        }
    }

    private void showWinScreen() {
        if (gameEnded) {
            return;
        }
        gameEnded = true;

        WinAndEndBossTwo winScreen = new WinAndEndBossTwo();
        winScreen.setBossDefeated(true);
        winScreen.setPlayerScore(score);
        winScreen.setDamageDealt(totalDamageDealt);
        winScreen.setBounds(0, 0, width, height);

        // Stop game updates
        boss.setActive(false);

        this.add(winScreen);
        winScreen.showNameInputScreen();
        this.revalidate();

        // Make sure the win screen gets focus
        winScreen.setFocusable(true);
        winScreen.requestFocusInWindow();
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
