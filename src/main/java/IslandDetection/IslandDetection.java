package IslandDetection;

import java.util.BitSet;
import ProgressBar.*;


public class IslandDetection {

    public static final byte OFF = 0;
    public static final byte SUPPORTED = 1;
    public static final byte ISLAND = 2;
    public static final byte CONNECTED = 3;


    public static byte[][][] checkIslands (BitSet[][] model, int layers, int rows, int columns){
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
                            if(isSupported(model, i, j, k) == true) stateModel[i][j][k] = SUPPORTED;
                            else stateModel[i][j][k] = ISLAND;
                        }
                        else stateModel[i][j][k] = OFF;
                    }
                }
            }
        }
        return stateModel;
    }
    
    private static boolean isSupported (BitSet[][] model, int layer, int row, int column){
        boolean supported = false;
        if (model[layer - 1][row].get(column) == true){
            supported = true;
        }
        return supported;
    }

}
