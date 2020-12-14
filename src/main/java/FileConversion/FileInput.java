package FileConversion;

import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngReader;
import org.zeroturnaround.zip.ZipUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Scanner;

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
//    System.out.println(Arrays.toString(data));
//    pngToBitSets("./tmp/pngs/Original_Prusa_SL1_Calibration_test_object_v200000.png");
  }


/*
  Takes an Sl1 filename (assumed to be validated eg not a random zip of unknown files)
   extracts the contents(including config.ini) to the directory /tmp/pngs
  */
public static void Sl1opener(String fileName){
    File output = new File("."+File.separator+"tmp"+File.separator+"pngs");
    ZipUtil.unpack(new File(fileName), output);

  }

  /*
  Take png filename as input, returns an array of bitSets, each bitSet is a row of the image
  The values are set so that if a pixel is black the corresponding bit is set to false, otherwise it is set to true
  */
  public static BitSet[] pngToBitSets(String pngFile){
    PngReader reader = new PngReader(new File(pngFile));
    int rows = reader.getImgInfo().rows;
    int cols = reader.getImgInfo().cols;
    BitSet[] outSet = new BitSet[rows];
    System.out.println(reader.toString());
    int i = 0;
    while(reader.hasMoreRows()) {
      outSet[i] = new BitSet(cols);
      int[] currentLine = ((ImageLineInt) reader.readRow()).getScanline();
      for(int j=0;j<cols;j++){
        if(currentLine[j]==0){
          outSet[i].set(j,false);
        }
        else{
          outSet[i].set(j,true);
        }
      }
      i++;
    }
    return outSet;
  }
}