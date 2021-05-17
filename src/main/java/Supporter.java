import java.util.BitSet;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import ProgressBar.*;

public class Supporter {
    public static void main(String[] args){
    }

    public static final byte OFF = 0;
    public static final byte SUPPORTED = 1;
    public static final byte ISLAND = 2;
    public static final byte CONNECTED = 3;

    public static Mat[] buildSupportsBasic(byte[][][] stateModel){
        int HEIGHT = stateModel.length;
        if(HEIGHT == 0) return null;
        int WIDTH = stateModel[0].length;
        if(WIDTH == 0) return null;
        int DEPTH = stateModel[0][0].length;
        if(DEPTH == 0) return null;

        Mat[] outSets = new Mat[HEIGHT];
        
        for (int i = 0 ; i < HEIGHT ; ++i) {
            outSets[i] = new Mat(WIDTH,DEPTH,CvType.CV_8U);
        }

        boolean[][] supportNeeded = new boolean[WIDTH][DEPTH];

        ProgressBar progressBar = new ProgressBar(HEIGHT);

        for(int i = HEIGHT - 1; i >= 0; i--){
            //progress bar
            progressBar.makeProgress();

            byte[][] slice = stateModel[i];
            for(int j = 0; j < WIDTH; j++){
                byte[] row = slice[j];

                for(int k = 0; k < DEPTH; k++){

                    byte cell = row[k];
                    //Cell cell = row[k];
                    if(cellOn(cell)) {
                        outSets[i].put(j,k,0xffff);
                        if (supportNeeded[j][k] && (cell == SUPPORTED || cell == CONNECTED)){
                            supportNeeded[j][k] = false;
                        }
                    }
                    if (cell == ISLAND) {
                        supportNeeded[j][k] = true;
                        supportNeeded[j-1][k-1] = true;
                    }
                    if(stateModel[Math.min(i+4,HEIGHT-1)][j][k] == ISLAND){
                        supportNeeded[j-1][k] = true;
                        supportNeeded[j][k-1] = true;
                        supportNeeded[j+1][k] = true;
                        supportNeeded[j][k+1] = true;
                    }
                    if (stateModel[Math.min(i+8,HEIGHT-1)][j][k] == ISLAND){
                        supportNeeded[j-1][k+1] = true;
                        supportNeeded[j+1][k-1] = true;
                        supportNeeded[j+1][k+1] = true;
                    }
                    else{
                        if(supportNeeded[j][k]) {
                            outSets[i].put(j,k,0xffff);
                            if(i <=23){
                                for(int b = -23+i;b<23-i;b++){
                                    for(int c = -23+i; c<23-i; c++){
                                        outSets[i].put(b+j,c+k,0xffff);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        System.out.println("\r[==========] 100%");
        return outSets;
    }


    public static boolean cellOn(Byte cell){
        return cell != OFF;
    }
}
