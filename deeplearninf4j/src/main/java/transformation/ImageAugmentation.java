package transformation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageAugmentation {

    public static void main(String[] args) {
        File f;
        File fnew;
        try {
            f = new File("C:\\Users\\acer\\Desktop\\ASU\\Нейронные сети\\mnist\\mnist_png\\training");
            fnew = new File("C:\\Users\\acer\\Desktop\\ASU\\Нейронные сети\\mnist\\mnist_png\\training — rotate");
            /*f = new File("C:\\Users\\acer\\Desktop\\ASU\\Нейронные сети\\mnist\\mnist_png\\data\\numbers");
            fnew = new File("C:\\Users\\acer\\Desktop\\ASU\\Нейронные сети\\mnist\\mnist_png\\data\\numbers — new")*/;
        } catch (Exception e) {
            System.out.println("НЕВЕРНЫЙ ПУТЬ К ФАЙЛАМ!");
            return;
        }
        String[] nameKey = f.list();

        for (String s : nameKey) {
            File folderWithImage = new File(f.getPath() + "\\" + s);
            String[] arrImage = folderWithImage.list();
            for (String pic : arrImage) {
                pic = pic.substring(0, pic.length() - 4);
                BufferedImage img;
                try {
                    img = ImageIO.read(new File(folderWithImage + "\\" + pic + ".png"));
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
                for (int i = -45; i <= 45; i += 5) {
                    if (i == 0) continue;
                    BufferedImage img2 = rotate(img, i);
                    normalize(img2);
                    writeImage(img2, fnew + "\\" + s + "\\" + pic + "angle(" + i + ").png");
                }
            }
            System.out.println("Complete " + s);
        }
        System.out.println("DONE!");
    }

    public static BufferedImage rotate(BufferedImage image, double angle) {
        angle = (Math.PI / 180) * (angle);
        int w = image.getWidth();
        int h = image.getHeight();
        GraphicsConfiguration gc = getDefaultConfiguration();
        BufferedImage result = gc.createCompatibleImage(w, h, Transparency.TRANSLUCENT);
        Graphics2D g = result.createGraphics();
        g.rotate(angle, w / 2, h / 2);
        g.drawRenderedImage(image, null);
        g.dispose();
        return result;
    }

    private static GraphicsConfiguration getDefaultConfiguration() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        return gd.getDefaultConfiguration();
    }

    private static void normalize(BufferedImage image) {
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                if (image.getRGB(i, j) == 0) image.setRGB(i, j, -16777216);
            }
        }
    }

    private static void writeImage(BufferedImage image, String path) {
        try {
            ImageIO.write(image, "png", new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
