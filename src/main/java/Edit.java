import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Edit {

    private String name, action;
    public static void rotate(String what){
        File sourceFile = new File("images/"+what);
        BufferedImage originalImage = null;
        try {
            originalImage = ImageIO.read(sourceFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BufferedImage targetImage = Scalr.rotate(originalImage, Scalr.Rotation.CW_90);
        File targetFile = new File("images/"+what);
        try {
            ImageIO.write(targetImage, "jpg", targetFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        originalImage.flush();
        targetImage.flush();
    }

    public static void flipH(String what){
        File sourceFile = new File("images/"+what);
        BufferedImage originalImage = null;
        try {
            originalImage = ImageIO.read(sourceFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BufferedImage targetImage = Scalr.rotate(originalImage, Scalr.Rotation.FLIP_HORZ);
        File targetFile = new File("images/"+what);
        try {
            ImageIO.write(targetImage, "jpg", targetFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        originalImage.flush();
        targetImage.flush();
    }

    public static void flipV(String what){
        File sourceFile = new File("images/"+what);
        BufferedImage originalImage = null;
        try {
            originalImage = ImageIO.read(sourceFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BufferedImage targetImage = Scalr.rotate(originalImage, Scalr.Rotation.FLIP_VERT);
        File targetFile = new File("images/"+what);
        try {
            ImageIO.write(targetImage, "jpg", targetFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        originalImage.flush();
        targetImage.flush();
    }

    public String getAction() {
        return action;
    }

    public String getName() {
        return name;
    }
}
