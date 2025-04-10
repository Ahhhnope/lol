package utilz;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class LoadSave {
    public static String background = "/background1.png";
    public static String gameTitle = "/Title.png";
    public static String[] menuButton = {"/PlayButton1.png", "/PlayButton2.png", "/PlayButton3.png"};

    public static BufferedImage getImage(String path) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream(path);

        try {
            img = ImageIO.read(is);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return img;
    }
}
