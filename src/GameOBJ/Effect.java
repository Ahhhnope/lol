/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GameOBJ;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Random;

/**
 *
 * @author Admin
 */
public class Effect {

    private final double x;
    private final double y;
    private final double max_distance;
    private final int max_size;
    private final Color color;
    private final int totaLeffect;
    private final float speed;
    private double current_distance;
    private ModelBoom booms[];
    private float apha = 1f;

    public Effect(double x, double y, int totaLeffect, int max_size, int max_distance, float speed, Color color) {
        this.x = x;
        this.y = y;
        this.totaLeffect = totaLeffect;
        this.max_size = max_size;
        this.max_distance = max_distance;
        this.speed = speed;
        this.color = color;
        createRandom();
    }

    private void createRandom() {
        booms = new ModelBoom[totaLeffect];
        float per = 360f / totaLeffect;
        Random ran = new Random();

        for (int i = 0; i < totaLeffect; i++) {
            int r = ran.nextInt((int) per) + 1;
            int boomSize = ran.nextInt(max_size) + 1;
            float angle = i * per + r;
            booms[i ] = new ModelBoom(boomSize, angle);
        }
    }
    
    public void draw(Graphics2D g2){
        AffineTransform oldTransFrom = g2.getTransform();
        Composite oldCompostie = g2.getComposite();
        g2.setColor(color);
        g2.translate(x, y);
        for (ModelBoom b : booms) {
            double bx = Math.cos(Math.toRadians(b.getAngle())) * current_distance;
            double by = Math.sin(Math.toRadians(b.getAngle())) * current_distance;
            double boomSize = b.getSize();
            double space = boomSize / 2;
            if(current_distance >= max_distance - (max_distance * 0.7f)){
                apha = (float) ((max_distance - current_distance) / (max_distance * 0.7f));
            }
            if(apha > 1){
                apha = 1;
            } else if(apha < 0){
                apha = 0;
            }
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, apha));
            g2.fill(new Rectangle2D.Double(bx - space, by - space, boomSize, boomSize));
            
        }
        g2.setComposite(oldCompostie);
        g2.setTransform(oldTransFrom);
    }
    
    public void update(){
        current_distance += speed;
    }
    
    public boolean check(){
        return current_distance < max_distance;
    }
}
