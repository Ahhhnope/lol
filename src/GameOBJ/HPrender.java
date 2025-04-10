
package GameOBJ;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Admin
 */
public class HPrender {

       private final HP hp;

    public HPrender(HP hp) {
        this.hp = hp;
    }

    protected void HPRender(Graphics2D g2, Shape shape, double y) {
        if (hp.getCurrectHP() != hp.getMAX_HP()) {
            double HPY = shape.getBounds().getY() - y - 10;
            g2.setColor(new Color(70, 70, 70));
            g2.fill(new Rectangle2D.Double(0, HPY, player.PLAYER_SILE, 2));
            g2.setColor(new Color(253, 91, 91));
            double Hpsize = hp.getCurrectHP() / hp.getMAX_HP() * player.PLAYER_SILE;
            g2.fill((new Rectangle2D.Double(0, HPY, Hpsize, 2)));
        }
    }

    public boolean updateHP(Double cutHP) {
        hp.setCurrectHP(hp.getCurrectHP() - cutHP);
        return hp.getCurrectHP() > 0;
    }

    public double getHP() {
        return hp.getCurrectHP();
    }

    public double getMaxHP() { 
        return hp.getMAX_HP();
    }

    public void resetHP() {
        hp.setCurrectHP(hp.getMAX_HP());
    }
}
