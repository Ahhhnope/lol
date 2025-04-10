package MainGame;

import ComponentGame.PlaneGame;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main extends JFrame {

    
    public Main() {
        init();
    }

    public void init() {
        setTitle("Game 2D - Huy Binh");
        setSize(1366, 768);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        PlaneGame planeGame = new PlaneGame();
        add(planeGame);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                planeGame.start();
            }
        });
    }

    // Phương thức main đã được sửa đổi để bắt đầu với màn hình menu
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                manHinhMenu menu = new manHinhMenu();
                menu.setVisible(true);

            }
        });
    }
}
