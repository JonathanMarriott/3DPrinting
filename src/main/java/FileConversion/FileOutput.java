package FileConversion;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.*;

import ar.com.hjg.pngj.*;
import org.zeroturnaround.zip.ZipUtil;
import org.zeroturnaround.zip.commons.FileUtils;



import org.opencv.core.Core;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;


/**
 * @author saadelmoutaouakil
 *
 *This Class creates a PNG file from an array of BitSets.
 *It writes line by line and therefore is space efficient 
 *Tested : Functionnal
 */
public class FileOutput {

    public static File modelToPngs(Mat[] model, File configFile,File pngExample){
        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
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
            //File outFile = new File("."+File.separator+"tmp"+File.separator+"out"+File.separator+jobDir+String.format("%05d",i)+".png");
            String stringFile = "."+File.separator+"tmp"+File.separator+"out"+File.separator+jobDir+String.format("%05d",i)+".png";
            //export(model[i],stringFile,reader.getImgInfo(),imageCodecs);
            Imgcodecs.imwrite(stringFile,model[i]);
            File outFile = new File("."+File.separator+"tmp"+File.separator+"out"+File.separator+jobDir+String.format("%05d",i)+".png");
            futures.add(CompletableFuture.runAsync(new OutputRunnable(model[i],outFile, reader.getImgInfo())));
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
        reader.close();
        return new File("."+File.separator+"tmp"+File.separator+"out");


    }

}


