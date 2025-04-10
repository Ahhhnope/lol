/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GameOBJ;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author Admin
 */
public class Bullect {

    private double x;
    private double y;
    private final Shape shape;
    private final Color color = new Color(255, 255, 255);
    private final float angle;
    private double size;
    private float speed = 1f;

    public Bullect(double x, double y, float angle, double size, double speed) {
        x += player.PLAYER_SILE / 2 - (size / 2);
        y += player.PLAYER_SILE / 2 - (size / 2);
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.size = size;
        this.speed = this.speed;
        shape = new Ellipse2D.Double(0, 0, size, size);
    }

    public void update() {
        x += Math.cos(Math.toRadians(angle)) * speed;
        y += Math.sin(Math.toRadians(angle)) * speed;
    }
    
    public boolean check(int width, int height){
        if(x <= -size || y < -size || x > width || y > height){
            return false;
        } else {
            return true;
        }
    }
    
    public void draw(Graphics2D g2){
        AffineTransform oldTranfrom = g2.getTransform();
        g2.setColor(color);
        g2.translate(x, y);
        g2.fill(shape);
        g2.setTransform(oldTranfrom);
    }
    
    public Shape getShape(){
        return new Area(new Ellipse2D.Double(x, y, size, size));
    }
    
    public double getX(){
        return x;
    }
    
    public double getY(){
        return y;
    }
    
     public double getSize(){
        return size;
    }
    
    public double getCenterX(){
        return x+size/2;
    }
    
      public double getCenterY(){
        return y+size/2;
    }

}
