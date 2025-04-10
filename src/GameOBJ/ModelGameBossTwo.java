package GameOBJ;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import javax.swing.ImageIcon;

/**
 *
 * @author Admin
 */
public class ModelGameBossTwo {

    private double x, y;
    private double hp = 1000;
    private final double MAX_HP = 1000;
    private Image bossImage;
    private int bossWidth = 350;
    private int bossHeight = 350;
    private boolean active = false;

    public ModelGameBossTwo() {
        try {
            bossImage = new ImageIcon(getClass().getResource("/GameImage/may_bay3.png")).getImage();
        } catch (Exception e) {
            System.err.println("Không thể tải hình ảnh boss: " + e.getMessage());
        }
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setLocation(int screenWidth, int screenHeight) {

        this.x = screenWidth - bossWidth - 50;
        this.y = (screenHeight - bossHeight) / 2;
    }

    public void draw(Graphics2D g2, int width, int height) {
        if (!active) {
            return;
        }

        // Xoay hình ảnh boss 90 độ
        AffineTransform oldTransform = g2.getTransform();
        AffineTransform transform = new AffineTransform();

        // Xác định tâm xoay
        double centerX = x + bossWidth / 2;
        double centerY = y + bossHeight / 2;

        // Xoay ảnh 90 độ (Math.toRadians(90))
        transform.rotate(Math.toRadians(45), centerX, centerY);

        // Áp dụng transform
        g2.setTransform(transform);

        // Vẽ boss
        g2.drawImage(bossImage, (int) x, (int) y, bossWidth, bossHeight, null);

        // Khôi phục transform gốc
        g2.setTransform(oldTransform);

        // Vẽ thanh máu
        int healthBarWidth = 800;
        int healthBarHeight = 20;
        int healthBarX = (width - healthBarWidth) / 2;
        int healthBarY = healthBarHeight + height - 100;

        g2.setColor(new Color(70, 70, 70));
        g2.fill(new Rectangle2D.Double(healthBarX, healthBarY, healthBarWidth, healthBarHeight));

        g2.setColor(Color.RED);
        double currentHealthWidth = (hp / MAX_HP) * healthBarWidth;
        g2.fill(new Rectangle2D.Double(healthBarX, healthBarY, currentHealthWidth, healthBarHeight));

        g2.setColor(Color.WHITE);
        g2.draw(new Rectangle2D.Double(healthBarX, healthBarY, healthBarWidth, healthBarHeight));

        g2.setColor(Color.WHITE);
        String hpText = (int) hp + "/" + (int) MAX_HP;
        g2.drawString(hpText, healthBarX + healthBarWidth + 10, healthBarY + 15);
    }

    public Area getShape() {
        return new Area(new Rectangle2D.Double(x, y, bossWidth, bossHeight));
    }

    public boolean updateHP(double damage) {
        hp -= damage;
        return hp > 0;
    }

    public double getHP() {
        return hp;
    }
}
