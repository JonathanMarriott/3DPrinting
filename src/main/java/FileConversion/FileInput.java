package FileConversion;

import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngReader;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.util.BitSet;

public class FileInput {

/*
  Takes an Sl1 filename (assumed to be validated eg not a random zip of unknown files)
   extracts the contents(including config.ini) to the directory /tmp/pngs
   Returns directory of the extracted file
   //TODO: Check the contents of the zip for the config ini and correct num of pngs
  */
//TODO Testing
public static File Sl1opener(String fileName){
  File sl1file = new File(fileName);
    File output = new File("."+File.separator+"tmp"+File.separator+"pngs");
    ZipUtil.unpack(sl1file, output);
    return output;

  }

  /*
  Take png filename as input, returns an array of bitSets, each bitSet is a row of the image
  The values are set so that if a pixel is black the corresponding bit is set to false, otherwise it is set to true
  */
  //TODO Testing
  public static BitSet[] pngToBitSets(File pngFile){
    PngReader reader = new PngReader(pngFile);
    int rows = reader.getImgInfo().rows;
    int cols = reader.getImgInfo().cols;
    BitSet[] outSet = new BitSet[rows];
    //System.out.println(reader.toString());
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
    reader.close();
    return outSet;
  }
}