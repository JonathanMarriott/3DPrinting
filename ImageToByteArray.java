import java.io.ByteArrayOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import java.util.Arrays;
import java.util.Scanner;

public class ImageToByteArray {
  public static void main(String args[]) throws Exception {
    Scanner inputScanner = new Scanner(System.in);
    System.out.println("Enter input filename: ");
    String fileName = inputScanner.nextLine();
    inputScanner.close();
    BufferedImage bImage = ImageIO.read(new File(fileName));
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ImageIO.write(bImage, "png", bos);
    byte[] data = bos.toByteArray();

    System.out.println(Arrays.toString(data));
  }
}