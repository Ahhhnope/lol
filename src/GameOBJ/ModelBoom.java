/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GameOBJ;

/**
 *
 * @author Admin
 */
public class ModelBoom {


    private double size;
    private float angle;

    public ModelBoom() {
    }

    public ModelBoom(double size, float angle) {
        this.size = size;
        this.angle = angle;
    }
    
    

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
    
}
