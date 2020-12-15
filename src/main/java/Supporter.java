import java.util.ArrayList;
import java.util.BitSet;

public class Supporter {
    public static void main(String[] args){
    }

    public static final byte OFF = 0;
    public static final byte SUPPORTED = 1;
    public static final byte ISLAND = 2;
    public static final byte CONNECTED = 3;

    public static BitSet[][] buildSupportsBasic(byte[][][] stateModel){
        int HEIGHT = stateModel.length;
        if(HEIGHT == 0) return null;
        int WIDTH = stateModel[0].length;
        if(WIDTH == 0) return null;
        int DEPTH = stateModel[0][0].length;
        if(DEPTH == 0) return null;

//        BitSet outSet = new BitSet(HEIGHT*WIDTH*DEPTH);
        BitSet[][] outSet = new BitSet[HEIGHT][WIDTH];

        //boolean[][][] output = new boolean[HEIGHT][WIDTH][DEPTH];

        boolean[][] supportNeeded = new boolean[WIDTH][DEPTH];

        for(int i = HEIGHT - 1; i >= 0; i--){
            byte[][] slice = stateModel[i];
            //Cell[][] slice = cells[i];
            for(int j = 0; j < WIDTH; j++){
                byte[] row = slice[j];
                //Cell[] row = slice[j];
                outSet[i][j] = new BitSet(DEPTH);
                for(int k = 0; k < DEPTH; k++){

                    //test cell - I know that in the testSlice, 0,907,542 is a white pixel
                    /* 
                    if (i == 0) {
                        if (j == 907) {
                            if (k == 542) {
                                System.out.println();
                            }
                        }
                    }
                    */
                    byte cell = row[k];
                    //Cell cell = row[k];
                    if(cellOn(cell)) {
                        outSet[i][j].set(k, true);
                        //output[i][j][k] = true;
                    }
                    if (cell == ISLAND) {
                        supportNeeded[j][k] = true;
                    } else{
                        if(supportNeeded[j][k]) {
                            outSet[i][j].set(k, true);
                            //output[i][j][k] = true;
                        }
                    }
                }
            }
        }

        return outSet;
    }

    public static int getPosition(int HEIGHT, int WIDTH, int DEPTH, int h, int w, int d){
        int sliceStart = (WIDTH*DEPTH) * (HEIGHT - h - 1);
        int rowStart = (DEPTH) * (w);
        return sliceStart + rowStart + d;
    }

    public static boolean cellOn(Byte cell){
        if(cell == OFF) return false;
        return true;
    }
}
