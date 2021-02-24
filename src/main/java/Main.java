import FileConversion.FileOutput;
import IslandDetection.IslandDetection;
import org.opencv.core.Mat;
import org.zeroturnaround.zip.ZipUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.BitSet;
import java.util.Scanner;

import static FileConversion.FileInput.Sl1opener;
import static FileConversion.FileInput.processPNGs;

public class Main {
    public static void main(String[] args){
        nu.pattern.OpenCV.loadLocally();
        long startTime = System.nanoTime();


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


        File pngDir = null; // Extracts the file and returns the directory
        try {
            pngDir = Sl1opener(sl1file);
        } catch (Exception e) {
            System.out.println("File is malformed or does not exist");
            System.exit(1);
        }

        File[] pngFiles = pngDir.listFiles(pathname -> pathname.getName().endsWith(".png")); //Filters for png files
        assert pngFiles != null;

        //Concurrently converts PNGs to 3D matrix using BitSets
        BitSet[][] result = processPNGs(pngFiles);


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

        File supportedDir = FileOutput.modelToPngs(supportedModel, pngDir);
        File outFile = new File(sl1file.substring(0,sl1file.length()-4)+"SUPPORTED.sl1");
        ZipUtil.pack(supportedDir,outFile);

        deleteDirectory(new File("." + File.separator + "SliceSupporterTmp" ));

        long stopTime = System.nanoTime();
        System.out.println("Supported file at: "+outFile.getName()+" in "+ (float)(stopTime - startTime)/1000000000 +"s");

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
