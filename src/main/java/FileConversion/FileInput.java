package FileConversion;

import java.io.ByteArrayOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.Arrays;
import java.util.Scanner;
import org.zeroturnaround.zip.ZipUtil;

public class FileInput {
  public static void main(String[] args) throws Exception {
    Scanner inputScanner = new Scanner(System.in);
    System.out.println("Enter input filename: ");
    String fileName = inputScanner.nextLine();
    System.out.println("Enter input sl1 filename: ");
    String sl1file = inputScanner.nextLine();
    Sl1opener(sl1file);
    inputScanner.close();
    BufferedImage bImage = ImageIO.read(new File(fileName));
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ImageIO.write(bImage, "png", bos);
    byte[] data = bos.toByteArray();

    System.out.println(Arrays.toString(data));
  }

  public static void Sl1opener(String fileName){
    File output = new File("."+File.separator+"tmp"+File.separator+"pngs");
    ZipUtil.unpack(new File(fileName), output);

  }
}