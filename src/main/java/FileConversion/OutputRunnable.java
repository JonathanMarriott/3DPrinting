package FileConversion;

import ar.com.hjg.pngj.ImageInfo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.BitSet;

public class OutputRunnable implements Runnable{
    private final BitSet[] data;
    private final File outputFile;
    private final ImageInfo info;

    public OutputRunnable(BitSet[] data, File outputFile, ImageInfo info) {
        this.data = data;
        this.outputFile = outputFile;
        this.info = info;
    }

    @Override
    public void run() {
        try {
            outputFile.createNewFile();//ok to ignore
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
