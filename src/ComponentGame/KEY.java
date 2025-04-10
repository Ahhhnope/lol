/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ComponentGame;

/**
 *
 * @author Admin
 */
public class KEY {
  private boolean KEY_RIGHT;
    private boolean KEY_LEFT;
    private boolean KEY_SPACE;
    private boolean KEY_J;
    private boolean KEY_K;
    private boolean KEY_ENTER;
    private boolean KEY_W;
    private boolean KEY_S;
    private boolean MOUSE_LEFT;
    private boolean MOUSE_RIGHT;

    public KEY() {
    }

    public KEY(boolean KEY_RIGHT, boolean KEY_LEFT, boolean KEY_SPACE, boolean KEY_J, boolean KEY_K, boolean KEY_ENTER, boolean KEY_W, boolean KEY_S, boolean MOUSE_LEFT, boolean MOUSE_RIGHT) {
        this.KEY_RIGHT = KEY_RIGHT;
        this.KEY_LEFT = KEY_LEFT;
        this.KEY_SPACE = KEY_SPACE;
        this.KEY_J = KEY_J;
        this.KEY_K = KEY_K;
        this.KEY_ENTER = KEY_ENTER;
        this.KEY_W = KEY_W;
        this.KEY_S = KEY_S;
        this.MOUSE_LEFT = MOUSE_LEFT;
        this.MOUSE_RIGHT = MOUSE_RIGHT;
    }

    public boolean isKEY_RIGHT() {
        return KEY_RIGHT;
    }

    public void setKEY_RIGHT(boolean KEY_RIGHT) {
        this.KEY_RIGHT = KEY_RIGHT;
    }

    public boolean isKEY_LEFT() {
        return KEY_LEFT;
    }

    public void setKEY_LEFT(boolean KEY_LEFT) {
        this.KEY_LEFT = KEY_LEFT;
    }

    public boolean isKEY_SPACE() {
        return KEY_SPACE;
    }

    public void setKEY_SPACE(boolean KEY_SPACE) {
        this.KEY_SPACE = KEY_SPACE;
    }

    public boolean isKEY_J() {
        return KEY_J;
    }

    public void setKEY_J(boolean KEY_J) {
        this.KEY_J = KEY_J;
    }

    public boolean isKEY_K() {
        return KEY_K;
    }

    public void setKEY_K(boolean KEY_K) {
        this.KEY_K = KEY_K;
    }

    public boolean isKEY_ENTER() {
        return KEY_ENTER;
    }

    public void setKEY_ENTER(boolean KEY_ENTER) {
        this.KEY_ENTER = KEY_ENTER;
    }

    public boolean isKEY_W() {
        return KEY_W;
    }

    public void setKEY_W(boolean KEY_W) {
        this.KEY_W = KEY_W;
    }

    public boolean isKEY_S() {
        return KEY_S;
    }

    public void setKEY_S(boolean KEY_S) {
        this.KEY_S = KEY_S;
    }

    public boolean isMOUSE_LEFT() {
        return MOUSE_LEFT;
    }

    public void setMOUSE_LEFT(boolean MOUSE_LEFT) {
        this.MOUSE_LEFT = MOUSE_LEFT;
    }

    public boolean isMOUSE_RIGHT() {
        return MOUSE_RIGHT;
    }

    public void setMOUSE_RIGHT(boolean MOUSE_RIGHT) {
        this.MOUSE_RIGHT = MOUSE_RIGHT;
    }
    
    
    
}
