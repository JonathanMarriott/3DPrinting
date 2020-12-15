import java.io.File;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Objects;
import java.util.Scanner;

import static FileConversion.FileInput.Sl1opener;
import static FileConversion.FileInput.pngToBitSets;

import FileConversion.FileOutput;
import IslandDetection.IslandDetection;
import org.zeroturnaround.zip.ZipUtil;

public class Main {
    public static void main(String[] args){
        //TODO Testing
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
        BitSet[][] result = output.toArray(new BitSet[output.size()][]);

        System.out.println("Checking for Islands");
        byte[][][] stateModel = IslandDetection.checkIslands(result);
        System.out.println("Adding Supports");
        BitSet[][] supportedModel = Supporter.buildSupportsBasic(stateModel);
        //BitSet[][] supportedModel = result;
        System.out.println("Creating new File");
        File supportedDir = FileOutput.modelToPngs(supportedModel,
                Objects.requireNonNull(pngDir.listFiles(path -> path.getName().equals("config.ini")))[0],
                pngFiles[0]);
        File outFile = new File(sl1file.substring(0,sl1file.length()-4)+"SUPPORTED.sl1");
        ZipUtil.pack(supportedDir,outFile);
        deleteDirectory(supportedDir);
        System.out.println("Supported file at: "+outFile.getName());



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
