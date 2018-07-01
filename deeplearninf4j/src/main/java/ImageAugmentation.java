import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageAugmentation {

    public static void main(String[] args) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("C:\\Users\\alexey\\Desktop\\АГУ\\GitHub\\Neural-networks\\deeplearninf4j\\src\\main\\resources\\TestShape.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedImage img2 = rotate(img, 90);
        //System.out.println(img2.getRGB(0,0));
        for (int i = 0; i < img2.getWidth(); i++) {
            for (int j = 0; j < img2.getHeight(); j++) {
                if (img2.getRGB(i, j) == 0) img2.setRGB(i, j, -16777216);
            }
        }
        try {
            ImageIO.write(img2, "png", new File("C:\\Users\\alexey\\Desktop\\АГУ\\GitHub\\Neural-networks\\deeplearninf4j\\src\\main\\resources\\TestShape2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Good!");
    }

    public static BufferedImage rotate(BufferedImage image, double angle) {
        double sin = Math.abs(Math.sin(angle));
        double cos = Math.abs(Math.cos(angle));
        int w = image.getWidth();
        int h = image.getHeight();
        int neww = (int) Math.floor(w * cos + h * sin);
        int newh = (int) Math.floor(h * cos + w * sin);
        GraphicsConfiguration gc = getDefaultConfiguration();
        BufferedImage result = gc.createCompatibleImage(neww, newh, Transparency.TRANSLUCENT);
        Graphics2D g = result.createGraphics();
        g.translate((neww - w) / 2, (newh - h) / 2);
        g.rotate(angle, w/2, h/2);
        g.drawRenderedImage(image, null);
        g.dispose();
        return result;
    }

    private static GraphicsConfiguration getDefaultConfiguration() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        return gd.getDefaultConfiguration();
    }
}
