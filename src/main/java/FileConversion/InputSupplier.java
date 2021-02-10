package FileConversion;

import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngReader;

import java.io.File;
import java.util.BitSet;
import java.util.function.Supplier;

//PNG processing task which is executed concurrently
//returns a object array {pos,Bitset[]} where index 0 is the position of the layer and index 1 is the bitset array for that layer
public class InputSupplier implements Supplier<Object[]> {
    private File pngFile;
    private int pos;

    public InputSupplier(File pngFile, int pos) {
        this.pngFile = pngFile;
        this.pos = pos;
    }

    /*
  Returns the position the supplier was instantiated with and an array of bitSets, each bitSet is a row of the image,
  The values are set so that if a pixel is black the corresponding bit is set to false, otherwise it is set to true
  */
    @Override
    public Object[] get() {
        PngReader reader = new PngReader(pngFile);
        int rows = reader.getImgInfo().rows;
        int cols = reader.getImgInfo().cols;
        BitSet[] outSet = new BitSet[rows];
        int i = 0;
        while(reader.hasMoreRows()) {
            outSet[i] = new BitSet(cols);
            int[] currentLine = ((ImageLineInt) reader.readRow()).getScanline();
            for(int j=0;j<cols;j++){
                if(currentLine[j]==0){
                    outSet[i].set(j,false);
                }
                else{
                    outSet[i].set(j,true);
                }
            }
            i++;
        }
        return new Object[]{pos,outSet};
    }
}
