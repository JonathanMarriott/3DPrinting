import static FileConversion.FileInput.Sl1opener;
import static FileConversion.FileInput.pngToBitSets;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Objects;
import java.util.Scanner;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.zeroturnaround.zip.ZipUtil;

import FileConversion.FileOutput;
import IslandDetection.IslandDetection;

public class Main {
    public static void main(String[] args){
        nu.pattern.OpenCV.loadLocally();
        //TODO Testing
        long start = System.currentTimeMillis();
       
        
        Scanner inputScanner = new Scanner(System.in);
        String sl1file;
        if(args.length>0){
            sl1file = args[0]; // Take File from command line argument
        }
        else {

            System.out.println("Enter input sl1 filename: ");
            sl1file = inputScanner.nextLine();
        }
        while (!sl1file.endsWith(".sl1")) { // Check File ends with .sl1
            System.out.println("Enter a input filename ending in sl1: ");
            sl1file = inputScanner.nextLine();
        }
        inputScanner.close();
        System.out.println("File opening");
        File pngDir = Sl1opener(sl1file); // Extracts the file and returns the directory
        File[] pngFiles = pngDir.listFiles(pathname -> pathname.getName().endsWith(".png")); //Filters for png files
        assert pngFiles != null;
        ArrayList<BitSet[]> output = new ArrayList<>();
        for(File png:pngFiles){// converts each png to an array of bitsets
            output.add(pngToBitSets(png));
        }
        //Mat[] results = output.toArray(new Mat[output.size()]);
        BitSet[][] result = output.toArray(new BitSet[output.size()][]);
        

        System.out.println("Checking for Islands");
        int layers = pngFiles.length;
        int rows = -1, columns = -1;
        try{
            BufferedImage bimg = ImageIO.read(pngFiles[0]);
            rows = bimg.getHeight();
            columns = bimg.getWidth();
        } catch (IOException e){
            e.printStackTrace();
            System.exit(0);
        }
        
        byte[][][] stateModel = IslandDetection.checkIslands(result, layers, rows, columns);
        
        
        System.out.println("Adding Supports");
        Mat[] supportedModel = Supporter.buildSupportsBasic(stateModel);

        System.out.println("Creating new File");
        File supportedDir = FileOutput.modelToPngs(supportedModel,
                Objects.requireNonNull(pngDir.listFiles(path -> path.getName().equals("config.ini")))[0],
                pngFiles[0]);
        File outFile = new File(sl1file.substring(0,sl1file.length()-4)+"SUPPORTED.sl1");
        ZipUtil.pack(supportedDir,outFile);
        deleteDirectory(supportedDir);
        
        
        System.out.println("Supported file at: "+outFile.getName());
        System.out.println("Execution time " + (System.currentTimeMillis()-start)/1000.0 + " s");

    }

    private static boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }
}
