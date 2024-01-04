import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Edit {

    private String name, action;
    private int x,y,w,h;
    public static void rotate(String name){
        File sourceFile = new File("images/"+name);
        BufferedImage originalImage = null;
        try {
            originalImage = ImageIO.read(sourceFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BufferedImage targetImage = Scalr.rotate(originalImage, Scalr.Rotation.CW_90);
        File targetFile = new File("images/"+name);
        try {
            ImageIO.write(targetImage, "jpg", targetFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        originalImage.flush();
        targetImage.flush();
    }

    public static void flipH(String name){
        File sourceFile = new File("images/"+name);
        BufferedImage originalImage = null;
        try {
            originalImage = ImageIO.read(sourceFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BufferedImage targetImage = Scalr.rotate(originalImage, Scalr.Rotation.FLIP_HORZ);
        File targetFile = new File("images/"+name);
        try {
            ImageIO.write(targetImage, "jpg", targetFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        originalImage.flush();
        targetImage.flush();
    }

    public static void flipV(String name){
        File sourceFile = new File("images/"+name);
        BufferedImage originalImage = null;
        try {
            originalImage = ImageIO.read(sourceFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BufferedImage targetImage = Scalr.rotate(originalImage, Scalr.Rotation.FLIP_VERT);
        File targetFile = new File("images/"+name);
        try {
            ImageIO.write(targetImage, "jpg", targetFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        originalImage.flush();
        targetImage.flush();
    }

    public static void crop(String name, int x, int y, int w, int h){
        File sourceFile = new File("images/"+name);
        BufferedImage originalImage = null;
        try {
            originalImage = ImageIO.read(sourceFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("x: "+x+"|y: "+y+"|w: "+w+"|h: "+ h);
        BufferedImage targetImage = Scalr.crop(originalImage, x, y, w, h); // x,y,w,h
        File targetFile = new File("images/"+name);

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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }
}
