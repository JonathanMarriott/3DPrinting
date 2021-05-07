package FileConversion;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import ProgressBar.ProgressBar;

public class OutRunnable implements Runnable{
    private  String stringFile;
    private Mat modelLayer;
    private ProgressBar progressBar;

    public OutRunnable(String stringFile, Mat modelLayer, ProgressBar progressBar) {
        this.stringFile = stringFile;
        this.modelLayer = modelLayer;
        this.progressBar = progressBar;
    }

    @Override
    public void run() {
        System.out.println(stringFile);
        System.out.println(Imgcodecs.imwrite(stringFile,modelLayer));
        progressBar.makeProgress();
    }
}
