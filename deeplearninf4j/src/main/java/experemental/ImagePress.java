package experemental;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImagePress {
    public static final String PATH = "C:\\Users\\acer\\Downloads";

    public static void main(String[] args) throws IOException {
        BufferedImage old = ImageIO.read(new File(PATH + "\\8new.png"));
//        BufferedImage scaled = sizeDown(old, 28, 28);
        BufferedImage scaled = changeSize(old, 0.5, 0.5);

        ImageIO.write(scaled, "PNG", new File(PATH + "\\8new3.png"));
        System.out.println("complete");
    }

    public static BufferedImage press(BufferedImage oldImage, int height, int width) {
        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = scaledImage.createGraphics();
        g.drawImage(oldImage, 0, 0, width, height, null);
        g.dispose();
        return scaledImage;
    }

    public static BufferedImage sizeDown(BufferedImage oldImage, int height, int width) {
        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
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

    public static BufferedImage changeSize(BufferedImage oldImage, double kw, double kh) {
        BufferedImage scaledImage = new BufferedImage(oldImage.getWidth(), oldImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();

        at.scale(kw, kh);
        AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

        scaledImage = scaleOp.filter(oldImage, scaledImage);
        return scaledImage;
        /*GraphicsConfiguration gc = getDefaultConfiguration();
        BufferedImage result = gc.createCompatibleImage(oldImage.getWidth(), oldImage.getHeight(), Transparency.TRANSLUCENT);
        Graphics2D g = result.createGraphics();
        g.scale(kw, kh);
        g.drawRenderedImage(oldImage, null);
        g.dispose();
        return result;*/
    }

    private static GraphicsConfiguration getDefaultConfiguration() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        return gd.getDefaultConfiguration();
    }
}
