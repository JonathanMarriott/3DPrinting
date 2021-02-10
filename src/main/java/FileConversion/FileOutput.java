package FileConversion;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import ar.com.hjg.pngj.*;
import org.zeroturnaround.zip.ZipUtil;
import org.zeroturnaround.zip.commons.FileUtils;

import javax.imageio.ImageIO;

/**
 * @author saadelmoutaouakil
 *
 *This Class creates a PNG file from an array of BitSets.
 *It writes line by line and therefore is space efficient 
 *Tested : Functionnal
 */
public class FileOutput {
    private static final int BLACK_RGB = 0 ;
    private static final int WHITE_RGB = 255 ;

    /**
     * @param input : PNG image in BitSet format
     * @param output_file The output file
     * @param info : Ihe ImageInfo file that should be identical to the PNG read before adding supports
     */
    public static void PNG_write(BitSet[] input, File output_file,
            ImageInfo info) {
        PngWriter writer = new PngWriter(output_file, info);
        ImageLineInt image_line = new ImageLineInt(info);

        for (int r = 0; r < info.rows; r++) {
            for (int i = 0; i < info.cols-1; i++) {
                if (input[r].get(i)) {
                    ImageLineHelper.setPixelRGB8(image_line, i, WHITE_RGB, WHITE_RGB, WHITE_RGB);
                } else {
                    ImageLineHelper.setPixelRGB8(image_line, i, BLACK_RGB,BLACK_RGB,BLACK_RGB);
                }

            }
            writer.writeRow(image_line);
        }
        
        writer.end();
    }
    public static void export (BitSet[] data,File outputFile,ImageInfo info) {
        int height = info.rows;
        int width = info.cols;
        BufferedImage outImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (data[i].get(j)) {
                    outImage.setRGB(j, i, 0xffffffff);
                } else {
                    outImage.setRGB(j, i, 0x00000000);
                }
            }
        }
        try {
            // Save as PNG
            ImageIO.write(outImage, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static File modelToPngs(BitSet[][] model, File configFile,File pngExample){
        Properties config = new Properties();
        try (FileInputStream in = new FileInputStream(configFile)) {
            config.load(in);
        }catch(Exception e){
            System.out.println("Error: No config file found");
        }
        String jobDir = (String) config.get("jobDir");
        PngReader reader = new PngReader(pngExample);
        File outDir = new File("."+File.separator+"tmp"+File.separator+"out");
        outDir.mkdir();//ok to ignore
        ArrayList<CompletableFuture<Void>> futures = new ArrayList<>();
        for(int i = 0; i< model.length; i++){
            File outFile = new File("."+File.separator+"tmp"+File.separator+"out"+File.separator+jobDir+String.format("%05d",i)+".png");
//            try {
//                outFile.createNewFile();//ok to ignore
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            futures.add(CompletableFuture.runAsync(new OutputRunnable(model[i],outFile, reader.getImgInfo())));
            //export(model[i],outFile,reader.getImgInfo());
        }
        try {
            FileUtils.copyFile(configFile,new File("."+File.separator+"tmp"+File.separator+"out"+File.separator+"config.ini"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        CompletableFuture[] cfs = futures.toArray(new CompletableFuture[0]); // Convert list of futures to an array
        //allOf turns the array to futures into a single future with the result as a list once all futures have completed
        CompletableFuture<Void> out = CompletableFuture.allOf(cfs);
        try {
            out.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return new File("."+File.separator+"tmp"+File.separator+"out");


    }

}


