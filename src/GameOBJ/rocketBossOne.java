package GameOBJ;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import javax.swing.ImageIcon;

public class rocketBossOne {

    private double x, y;
    private double angle;
    private double speed = 2.5;
    private int width = 80;  // Tăng kích thước ngang
    private int height = 40; // Tăng kích thước dọc
    private Image bulletImage;
    private boolean active = true;

    public rocketBossOne(double x, double y, double targetX, double targetY) {
        this.x = x;
        this.y = y;

        // Calculate the angle toward the player
        double dx = targetX - x;
        double dy = targetY - y;
        this.angle = Math.toDegrees(Math.atan2(dy, dx));

        try {
            bulletImage = new ImageIcon(getClass().getResource("/GameImage/testTenLuaGame.png")).getImage();
        } catch (Exception e) {
            System.err.println("Cannot load boss bullet image: " + e.getMessage());
        }
    }

    public void update() {
        // Move based on the angle
        double radians = Math.toRadians(angle);
        x += Math.cos(radians) * speed;
        y += Math.sin(radians) * speed;
    }

    public void draw(Graphics2D g2) {
        if (!active) {
            return;
        }

        if (bulletImage != null) {
            // Lưu trạng thái ban đầu
            AffineTransform oldTransform = g2.getTransform();

            // Tính toán vị trí trung tâm để xoay
            double centerX = x + width / 2;
            double centerY = y + height / 2;

            // Đặt transform cho việc xoay
            AffineTransform transform = new AffineTransform();
            transform.translate(x, y);
            transform.rotate(Math.toRadians(angle), width / 2.0, height / 2.0);
            g2.setTransform(transform);

            // Vẽ đạn
            g2.drawImage(bulletImage, 0, 0, width, height, null);

            // Khôi phục lại transform ban đầu
            g2.setTransform(oldTransform);
        } else {
            // Fallback nếu không có hình ảnh
            g2.setColor(Color.RED);
            g2.fillOval((int) x, (int) y, width, height);
        }
    }

    public Area getShape() {
        return new Area(new Rectangle2D.Double(x, y, width, height));
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean check(int screenWidth, int screenHeight) {
        return x >= 0 && x <= screenWidth && y >= 0 && y <= screenHeight;
    }

    public double getDamage() {
        return 10;
    }
}
