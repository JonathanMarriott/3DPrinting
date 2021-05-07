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
                            if(isSupported(stateModel, i, j, k) == true) stateModel[i][j][k] = SUPPORTED;
                            else if (isConnected(stateModel, model, i, j, k) == true){
                                stateModel[i][j][k] = CONNECTED;
                            }
                            else stateModel[i][j][k] = ISLAND;
                        }
                        else stateModel[i][j][k] = OFF;
                    }
                }
            }
        }
        System.out.println("\r[==========] 100%\n");
        return stateModel;
    }
    
    /*
    public static BitSet[][] checkIslands (BitSet[][] model){
        for (int i = 0; i < model.length; i++){
            for (int j = 0; j < model[i].length; j++){
                for (int k = 0; k < model[i][j].size(); k++){
                    if (i == 0){
                        if (model[0][j].get(k) == true){
                            stateModel[0][j][k] = State.SUPPORTED;
                            model[0][j].set(k, State.SUPPORTED);
                        }
                        else{
                            stateModel[0][j][k] = State.OFF;
                        }
                    }
                    else{
                        if (model[i - 1][j].get(k) == true){
                            if(isSupported(model, i, j, k) == true) stateModel[i][j][k] = State.SUPPORTED;
                            else stateModel[i][j][k] = State.ISLAND;
                        }
                        else stateModel[0][j][k] = State.OFF;
                    }
                }
            }
        }
        return model;
    }
    */
    /*
    private static boolean isSupported (BitSet[][] model, int layer, int row, int column){
        boolean supported = false;
        if (model[layer - 1][row].get(column) == true){
            supported = true;
        }
        return supported;
    }
    */
    private static boolean isSupported (byte[][][] stateModel, int layer, int row, int column){
        boolean supported = false;
        if(layer == 0) return true;
        if (stateModel[layer - 1][row][column] == SUPPORTED) supported = true;
        return supported;
    }

    // returns true if above or diagonally above a supported or connected cell. Only supports gradient >= 1.

    private static boolean isConnected (byte[][][] stateModel, BitSet[][] model, int layer, int row, int column){
        boolean connected = false;
        if(layer == 0) return true;
        if (stateModel[layer - 1][row][column] == SUPPORTED) return true;
        //the %2 == 1 bit checks whether it is supported or connected, as SUPPORTED(1) % 2 == 1 and CONNECTED(3) % 2 == 1
        for(int i = -3; i<=3; i++){
            for(int j = -3;j<=3; j++){
                if (stateModel[layer - 1][row - i][column - j] % 2 == 1 && model[layer][row - i].get(column - j) == true) {
                    connected = true;
//                }
//                else if (stateModel[layer - 1][row+1][column] % 2 == 1 && model[layer][row+1].get(column) == true){
//                    connected = true;
//                }
//                else if (stateModel[layer - 1][row][column-1] % 2 == 1 && model[layer][row].get(column-1) == true){
//                    connected = true;
//                }
//                else if (stateModel[layer - 1][row][column+1] % 2 == 1 && model[layer][row].get(column+1) == true){
//                    connected = true;
                    break;
                }
            }
        }

        return connected;
    }


}
