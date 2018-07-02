import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageAugmentation {

    public static void main(String[] args) {
        File f = new File("C:\\Users\\alexey\\Desktop\\АГУ\\GitHub\\mnist\\mnist_png\\data\\numbers");
        File fnew = new File("C:\\Users\\alexey\\Desktop\\АГУ\\GitHub\\mnist\\mnist_png\\data\\numbers — new");
        String[] nameKey = f.list();

        for (String s : nameKey) {
            File folderWithImage = new File(f.getPath() + "\\" + s);
            String[] arrImage = folderWithImage.list();
            for (String pic : arrImage) {
                BufferedImage img = null;
                try {
                    img = ImageIO.read(new File(folderWithImage + "\\" + pic));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (int i = -90; i <= 90; i += 5) {
                    if (i == 0) continue;
                    BufferedImage img2 = rotate(img, i);
                    normalize(img2);
                    writeImage(img2, fnew + "\\" + s + "\\rot(" + i + ")" + pic);
                }
            }
        }
        System.out.println("DONE!");
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
