import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class TextureDump {
    public static void main(String[] args) throws Exception {
        File file = new File("src/main/resources/assets/minigames/textures/item/utility_golden_mango.png");
        BufferedImage img = ImageIO.read(file);
        System.out.println("Format / Type: " + img.getType());
        System.out.println("Width x Height: " + img.getWidth() + "x" + img.getHeight());
        for (int y = 0; y < img.getHeight(); y++) {
            System.out.printf("Y=%2d: ", y);
            for (int x = 0; x < img.getWidth(); x++) {
                int argb = img.getRGB(x, y);
                int a = (argb >> 24) & 0xff;
                int r = (argb >> 16) & 0xff;
                int g = (argb >> 8) & 0xff;
                int b = argb & 0xff;
                if (a == 0) {
                    System.out.print("...... ");
                } else {
                    System.out.printf("%02X%02X%02X ", r, g, b);
                }
            }
            System.out.println();
        }
    }
}
