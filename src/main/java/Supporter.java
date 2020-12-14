import java.util.ArrayList;
import java.util.BitSet;

public class Supporter {
    public static void main(String[] args){
    }

    public static boolean[][][] buildSupportsBasic(Cell[][][] cells){
        int HEIGHT = cells.length;
        if(HEIGHT == 0) return null;
        int WIDTH = cells[0].length;
        if(WIDTH == 0) return null;
        int DEPTH = cells[0][0].length;
        if(DEPTH == 0) return null;

//        BitSet outSet = new BitSet(HEIGHT*WIDTH*DEPTH);

        boolean[][][] output = new boolean[HEIGHT][WIDTH][DEPTH];

        boolean[][] supportNeeded = new boolean[WIDTH][DEPTH];

        for(int i = HEIGHT; i >= 0; i--){
            Cell[][] slice = cells[i];
            for(int j = 0; j < WIDTH; j++){
                Cell[] row = slice[j];
                for(int k = 0; k < DEPTH; k++){
                    Cell cell = row[k];
                    if(cellOn(cell)) {
                        output[i][j][k] = true;
                    }
                    if (cell == Cell.UNSUPPORTED) {
                        supportNeeded[j][k] = true;
                    } else{
                        if(supportNeeded[j][k]) output[i][j][k] = true;
                    }
                }
            }
        }

        return output;
    }

    public static int getPosition(int HEIGHT, int WIDTH, int DEPTH, int h, int w, int d){
        int sliceStart = (WIDTH*DEPTH) * (HEIGHT - h - 1);
        int rowStart = (DEPTH) * (w);
        return sliceStart + rowStart + d;
    }

    public static boolean cellOn(Cell cell){
        if(cell == Cell.EMPTY) return false;
        return true;
    }
}
