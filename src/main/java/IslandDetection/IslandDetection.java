package IslandDetection;

import ProgressBar.*;

import java.util.BitSet;

public class IslandDetection {

    public static final byte OFF = 0;
    public static final byte SUPPORTED = 1;
    public static final byte ISLAND = 2;
    public static final byte CONNECTED = 3;


    public static byte[][][] checkIslands (BitSet[][] model, int layers, int rows, int columns, int supportedRadius){
        byte[][][] stateModel = new byte[layers][rows][columns];

        ProgressBar progressBar = new ProgressBar(layers);

        //i is layer, j is row, k is column
        for (int i = 0; i < layers; i++){
            //progress bar
            progressBar.makeProgress();
            for (int j = 0; j < rows; j++){
                for (int k = 0; k < columns; k++){
                    if (i == 0){
                        if (model[0][j].get(k) == true){
                            stateModel[0][j][k] = SUPPORTED;
                        }
                        else{
                            stateModel[0][j][k] = OFF;
                        }
                    }
                    else{
                        if (model[i][j].get(k) == true){
                            if(isSupported(stateModel, i, j, k) == true) stateModel[i][j][k] = SUPPORTED;
                            else if (isConnected(stateModel, model, i, j, k, supportedRadius) == true){
                                stateModel[i][j][k] = CONNECTED;
                            }
                            else stateModel[i][j][k] = ISLAND;
                        }
                        else stateModel[i][j][k] = OFF;
                    }
                }
            }
        }
        System.out.println("\r[==========] 100%");
        return stateModel;
    }

    protected static boolean isSupported (byte[][][] stateModel, int layer, int row, int column){
        boolean supported = false;
        if(layer == 0) return true;
        if (stateModel[layer - 1][row][column] == SUPPORTED) supported = true;
        return supported;
    }

    // returns true if above or diagonally above a supported or connected cell. Only supports gradient >= 1.
    protected static boolean isConnected (byte[][][] stateModel, BitSet[][] model, int layer, int row, int column, int supportedRadius){
        if(layer == 0) return true;
        if (stateModel[layer - 1][row][column] == SUPPORTED) return true;
        //the %2 == 1 bit checks whether it is supported or connected, as SUPPORTED(1) % 2 == 1 and CONNECTED(3) % 2 == 1
        for(int i = -supportedRadius; i<=supportedRadius; i++){
            int limit = (int)Math.floor(Math.sqrt(supportedRadius^2 - i^2));
            for(int j = -limit;j<=limit; j++){
                if (stateModel[layer - 1][row - i][column - j] % 2 == 1 && model[layer][row - i].get(column - j) == true) {
                    return true;
                }
            }
        }
        return false;
    }
}
