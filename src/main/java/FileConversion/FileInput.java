package FileConversion;


import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class FileInput {

  /*
    Takes an Sl1 filename (assumed to be validated eg not a random zip of unknown files)
     extracts the contents(including config.ini) to the directory /tmp/pngs
     Returns directory of the extracted file
    */
  public static File Sl1opener(String fileName) throws Exception {
    File sl1file = new File(fileName);
    File output = new File("." + File.separator + "tmp" + File.separator + "pngs");
    ZipUtil.unpack(sl1file, output);
    if((Objects.requireNonNull(output.listFiles(path -> path.getName().equals("config.ini")))).length !=1){
      throw new Exception("No config.ini found in SL1 file provided");
    }

    return output;

  }

  /*
  Take array of png filenames as input, returns an 2D array of bitSets, each bitSet is a row of the image
  The values are set so that if a pixel is black the corresponding bit is set to false, otherwise it is set to true
  */
  public static BitSet[][] processPNGs(File[] pngFiles) {
    //Create a list for the concurrent futures to be stored
    ArrayList<CompletableFuture<Object[]>> futures = new ArrayList<>();
    int pos = 0;// Used to order the layers if they come back in a different order after processing
    for (File png : pngFiles) {
      futures.add(CompletableFuture.supplyAsync(new InputSupplier(png, pos))); // Each PNG is supplied as a new task to the common thread pool
      pos++;
    }
    CompletableFuture[] cfs = futures.toArray(new CompletableFuture[0]); // Convert list of futures to an array
    //allOf turns the array to futures into a single future with the result as a list once all futures have completed
    CompletableFuture<List<Object[]>> outList = CompletableFuture.allOf(cfs).thenApply(ignored -> futures.stream()
            .map(CompletableFuture::join)
            .collect(Collectors.toList()));

    List<Object[]> posPairs = new ArrayList<>();
    //Get the values out of the futures
    try {
      posPairs = outList.get();
    } catch (Exception e) {
      System.out.println("Error with File Input");
      e.printStackTrace();
    }
    //Sort the layers Back into correct order as concurrency may have messed up ordering
    posPairs.sort(Comparator.comparingInt(o -> (int) o[0]));
    //Remove the ordering integers from the object pairs
    BitSet[][] result = new BitSet[posPairs.size()][];
    for (int i = 0; i < posPairs.size(); i++) {
      result[i] = (BitSet[]) posPairs.get(i)[1];
    }

    return result;

  }
}

