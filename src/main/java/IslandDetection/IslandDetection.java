package IslandDetection;

import java.util.BitSet;


public class IslandDetection {

    public static final byte OFF = 0;
    public static final byte SUPPORTED = 1;
    public static final byte ISLAND = 2;
    public static final byte CONNECTED = 3;


    public static byte[][][] checkIslands (BitSet[][] model, int layers, int rows, int columns){
        byte[][][] stateModel = new byte[layers][rows][columns];

        //variables for progress bar
        int progress = 0;
        int progressTen;
        char[] out = "\r[          ] ".toCharArray();

        //i is layer, j is row, k is column
        for (int i = 0; i < layers; i++){
            //progress bar
            if((((i + 1) * 100) / layers) > progress){
                progress = progress + 1;
                progressTen = progress % 10;
                if(progressTen == 0) out[(progress / 10)+1] = '=';
                System.out.print(String.valueOf(out) + String.valueOf(progress) + "%");
            }
            for (int j = 0; j < rows; j++){
                for (int k = 0; k < columns; k++){
                    //System.out.println(model[i][j].size());
                    //System.out.println(model[i][j].length());
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
        System.out.print("\n");
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

    private static boolean isSupported (BitSet[][] model, int layer, int row, int column){
        boolean supported = false;
        if (model[layer - 1][row].get(column) == true){
            supported = true;
        }
        return supported;
    }

    /* potential isConnected function

    private boolean isConnected (BitSet[][] model, int column, int layer, int row){
        boolean connected = false;
        if (model[layer - 1][row].get(column) == true){
            connected = true;
        }
        if (model[layer - 1][row-1].get(column) == true && model[layer][row-1].get(column) == true){
            connected = true;
        }
        if (model[layer - 1][row+1].get(column) == true && model[layer][row+1].get(column) == true){
            connected = true;
        }
        if (model[layer - 1][row].get(column-1) == true && model[layer][row].get(column-1) == true){
            connected = true;
        }
        if (model[layer - 1][row].get(column-1) == true && model[layer][row].get(column-1) == true){
            connected = true;
        }
        return connected;
    }
    */

}
