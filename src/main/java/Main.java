import FileConversion.FileOutput;
import IslandDetection.DetectionTesting;
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
        welcomeMessage();

        int supportRadius = 25;
        String sl1file = null;
        Scanner inputScanner = new Scanner(System.in);

        for(int i = 0; i < args.length; i++){
            switch(args[i]){
            case "--help":
                System.out.println("### Help Menu ###\n" +
                "-- Input --\n" +
                "This program only accepts .sl1 files as input. If you have a .stl, .obj, .amf, .xml, .3mf or .prusa file, you can convert this to .sl1 using PrusaSlicer.\n" +
                "\n" +
                "-- Output --\n" +
                "Your outputted .sl1 file will be in the same directory as the input file you supplied, with 'SUPPORTED' appended to the file name. E.g. a.sl1 as input will produce aSUPPORTED.sl1 as output.\n" +
                "\n" +
                "-- java.lang.OutOfMemoryError --\n" +
                "If you see the above error message, you will need to allocate more memory to the program. Adding the argument '-Xmx6g' will increase the heap size to 6gb, which will be enough for most prints. You can increase this as necessary.\n" +
                "\n" +
                "-- Support Radius --\n" +
                "The support radius defines how much area above a single pixel will be considered supported. A radius of 3 means that any pixel which is less than 3 pixels away from a supported pixel will also be demeed supported.\n" +
                "You can adjust this by using the -radius flag. For example, adding '-radius 10' to the end of your command line statement will set the radius to be 10 pixels." +
                "If you are printing a model which has many close, but not connected, pixels, you may need to reduce the support radius, or remove it entirely by setting it to 0. (Setting to a very low value will significantly increase runtime).\n"
                );
                return;
            case "-radius":
                i+=1;
                if(i == args.length){
                    System.out.println("--radius requires an integer argument. Please see --help for details.");
                    return;
                }
                supportRadius = Integer.parseInt(args[i]);
                break;
            case "test":
                sl1file = args[0]; // Have to do this so that compiler won't complain :)
                DetectionTesting.test();
                System.exit(0);
                break;
            default:
                sl1file = args[i];
                break;
            }
        }

        if(sl1file == null){
            System.out.println("Enter input sl1 filename: ");
            sl1file = inputScanner.nextLine();
        }


        nu.pattern.OpenCV.loadLocally();

        while (!sl1file.endsWith(".sl1")) { // Check File ends with .sl1
            System.out.println("Enter a input filename ending in sl1: ");
            sl1file = inputScanner.nextLine();
        }
        inputScanner.close();
        deleteDirectory(new File("." + File.separator + "SliceSupporterTmp" ));
        long start = System.currentTimeMillis();
        long startTime = System.nanoTime();
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

        System.out.println("File opening time was: "+ (float)(System.nanoTime() - startTime)/1000000000 +"s");

        System.out.println("Checking for Islands");
        startTime = System.nanoTime();
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

        pngFiles = null;
        System.gc();
        
        byte[][][] stateModel = IslandDetection.checkIslands(result, layers, rows, columns, supportRadius);

        result = null;
        System.gc();

        System.out.println("Island Detection time was: "+ (float)(System.nanoTime() - startTime)/1000000000 +"s");
        System.out.println("Adding Supports");
        startTime = System.nanoTime();
        Mat[] supportedModel = Supporter.buildSupportsBasic(stateModel);
        stateModel = null;
        System.gc();

        System.out.println("Support Building time was: "+ (float)(System.nanoTime() - startTime)/1000000000 +"s");
        System.out.println("Creating new File");
        startTime = System.nanoTime();

        File supportedDir = FileOutput.modelToPngs(supportedModel, pngDir);
        File outFile = new File(sl1file.substring(0,sl1file.length()-4)+"SUPPORTED.sl1");
        ZipUtil.pack(supportedDir,outFile);

        deleteDirectory(new File("." + File.separator + "SliceSupporterTmp" ));

        System.out.println("File writing time was: "+ (float)(System.nanoTime() - startTime)/1000000000 +"s");
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

    private static void welcomeMessage(){
        System.out.println("### Welcome to 3D SliceSupporter ###");
        System.out.println("To get help, please use the --help flag. This includes a fix for the java.lang.OutOfMemoryError error message.");
        System.out.println("By default, the support radius is set to 25. To change this, please use the -radius flag. See the help menu for more information.\n");
    }
}
