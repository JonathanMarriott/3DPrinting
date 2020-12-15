package spe_pack;

import java.io.File;
import java.util.BitSet;

import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.ImageLineHelper;
import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngWriter;

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
            for (int i = 0; i < info.cols; i++) {
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

}


