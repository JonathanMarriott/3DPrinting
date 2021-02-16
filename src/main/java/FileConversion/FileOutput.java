package FileConversion;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.Properties;

import ar.com.hjg.pngj.*;

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
        
        
        for(int i = 0; i< model.length; i++){
            //File outFile = new File("."+File.separator+"tmp"+File.separator+"out"+File.separator+jobDir+String.format("%05d",i)+".png");
            String stringFile = "."+File.separator+"tmp"+File.separator+"out"+File.separator+jobDir+String.format("%05d",i)+".png";
            //export(model[i],stringFile,reader.getImgInfo(),imageCodecs);
            Imgcodecs.imwrite(stringFile,model[i]);
        }
        try {
            FileUtils.copyFile(configFile,new File("."+File.separator+"tmp"+File.separator+"out"+File.separator+"config.ini"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return new File("."+File.separator+"tmp"+File.separator+"out");


    }

}


