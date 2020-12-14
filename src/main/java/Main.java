import java.io.File;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Scanner;

import static FileConversion.FileInput.Sl1opener;
import static FileConversion.FileInput.pngToBitSets;

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
            System.out.println("Enter a filename ending in sl1: ");
            sl1file = inputScanner.nextLine();
        }
        inputScanner.close();
        File pngDir = Sl1opener(sl1file); // Extracts the file and returns the directory
        File[] pngFiles = pngDir.listFiles(pathname -> pathname.getName().endsWith(".png")); //Filters for png files
        assert pngFiles != null;
        ArrayList<BitSet[]> output = new ArrayList<>();
        for(File png:pngFiles){// converts each png to an array of bitsets
            output.add(pngToBitSets(png));
        }
        BitSet[][] result =  output.toArray(new BitSet[output.size()][]);





    }
}
