package FileConversion;

import ar.com.hjg.pngj.PngReader;
import org.opencv.core.Mat;
import org.zeroturnaround.zip.commons.FileUtils;

import ProgressBar.ProgressBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class FileOutput {

    public static File modelToPngs(Mat[] model, File sl1Dir){
        File configFile = Objects.requireNonNull(sl1Dir.listFiles(path -> path.getName().equals("config.ini")))[0];
        File pngExample = Objects.requireNonNull(sl1Dir.listFiles(pathname -> pathname.getName().endsWith(".png")))[0];
        File[] prusaFiles = sl1Dir.listFiles(path -> path.getName().equals("prusaslicer.ini"));
        File prusaFile = null;
        boolean prusaFilePresent = false;
        if (prusaFiles != null && prusaFiles.length>=1){
            prusaFilePresent = true;
            prusaFile = prusaFiles[0];
        }
        Properties config = new Properties();
        try (FileInputStream in = new FileInputStream(configFile)) {
            config.load(in);
        }catch(Exception e){
            System.out.println("Error: No config file found");
        }
        String jobDir = (String) config.get("jobDir");
        PngReader reader = new PngReader(pngExample);
        File outDir = new File("."+File.separator+"SliceSupporterTmp"+File.separator+"out");
        outDir.mkdir();//ok to ignore
        ArrayList<CompletableFuture<Void>> futures = new ArrayList<>();

        ProgressBar progressBar = new ProgressBar(model.length);

        for(int i = 0; i< model.length; i++){
            String stringFile = "."+File.separator+"SliceSupporterTmp"+File.separator+"out"+File.separator+jobDir+String.format("%05d",i)+".png";
            futures.add(CompletableFuture.runAsync(new OutRunnable(stringFile,model[i], progressBar)));

        }
        try {
            FileUtils.copyFile(configFile,new File("."+File.separator+"SliceSupporterTmp"+File.separator+"out"+File.separator+"config.ini"));
            if (prusaFilePresent) {
                FileUtils.copyFile(prusaFile, new File("." + File.separator + "SliceSupporterTmp" + File.separator + "out" + File.separator + "prusaslicer.ini"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        CompletableFuture[] cfs = futures.toArray(new CompletableFuture[0]); // Convert list of futures to an array
        // allOf turns the array to futures into a single future with the result as a list once all futures have completed
        CompletableFuture<Void> out = CompletableFuture.allOf(cfs);
        try {
            out.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        reader.close();

        System.out.println("\r[==========] 100%");
        return new File("."+File.separator+"SliceSupporterTmp"+File.separator+"out");


    }

}


