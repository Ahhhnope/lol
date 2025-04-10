/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GameOBJ;

/**
 *
 * @author Admin
 */
public class HP {

    private double MAX_HP;
    private double currectHP;

    public double getMAX_HP() {
        return MAX_HP;
    }

    public void setMAX_HP(double MAX_HP) {
        this.MAX_HP = MAX_HP;
    }

    public double getCurrectHP() {
        return currectHP;
    }

    public void setCurrectHP(double currectHP) {
        this.currectHP = currectHP;
    }

    public HP(double MAX_HP, double currectHP) {
        this.MAX_HP = MAX_HP;
        this.currectHP = currectHP;
    }

    public HP() {
    }
}
