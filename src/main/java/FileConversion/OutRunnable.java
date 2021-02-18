package FileConversion;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class OutRunnable implements Runnable{
    private  String stringFile;
    private Mat modelLayer;

    public OutRunnable(String stringFile, Mat modelLayer) {
        this.stringFile = stringFile;
        this.modelLayer = modelLayer;
    }

    @Override
    public void run() {
        Imgcodecs.imwrite(stringFile,modelLayer);
    }
}
