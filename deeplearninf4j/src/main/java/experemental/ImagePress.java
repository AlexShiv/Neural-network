package experemental;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImagePress {
    public static final String PATH = "C:\\Users\\acer\\Downloads";

    public static void main(String[] args) throws IOException {
        BufferedImage old = ImageIO.read(new File(PATH + "\\8.png"));
        BufferedImage scaled = sizeDown(old, 28, 28);
        /*for (int i = 0; i < scaled.getWidth(); i++) {
            for (int j = 0; j < scaled.getHeight(); j++) {
                if (scaled.getRGB(i, j) == -1) scaled.setRGB(i, j, -16777216);
                else if (scaled.getRGB(i, j) == -16777216) scaled.setRGB(i, j, -1);
            }
        }*/
        ImageIO.write(scaled, "PNG", new File(PATH + "\\8new.png"));
        System.out.println("complete");
    }

    public static BufferedImage press(BufferedImage oldImage, int height, int width) {
        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics2D g = scaledImage.createGraphics();
        g.drawImage(oldImage, 0, 0, width, height, null);
        g.dispose();
        return scaledImage;
    }

    public static BufferedImage sizeDown(BufferedImage oldImage, int height, int width) {
        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int black = -16777216;
        double kh = oldImage.getHeight() / 28.0;
        double kw = oldImage.getWidth() / 28.0;
        for (int i = 0; i < oldImage.getWidth(); i++) {
            for (int j = 0; j < oldImage.getHeight(); j++) {
                if (oldImage.getRGB(i, j) >= -1) {
                    scaledImage.setRGB((int) (i / kw), ((int) (j / kh)), oldImage.getRGB(i, j));
                }
            }
        }
        return scaledImage;
    }
}
