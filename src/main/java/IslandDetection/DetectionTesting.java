package IslandDetection;
import java.util.BitSet;

public class DetectionTesting {

    public static final byte OFF = 0;
    public static final byte SUPPORTED = 1;
    public static final byte ISLAND = 2;
    public static final byte CONNECTED = 3;

    public static void test(){
        byte[][][] stateModel = new byte[10][10][10];
        BitSet[][] model = new BitSet[10][10];
        for (int i = 0; i < 10; i++){
            for (int j = 0; j < 10; j++){
                model[i][j] = new BitSet(10);
            }
        }

        stateModel[0][5][5] = SUPPORTED;
        assert IslandDetection.isSupported(stateModel, 1, 5, 5);
        System.out.println("connected to bottom - test passed");

        stateModel[0][5][5] = OFF;
        assert !(IslandDetection.isSupported(stateModel, 1, 5, 5));
        System.out.println("not connected to bottom - test passed");

        
        stateModel[0][5][5] = SUPPORTED;
        stateModel[1][5][5] = SUPPORTED;
        model[0][5].set(5);
        model[1][5].set(5);
        model[1][6].set(5);
        assert (IslandDetection.isConnected(stateModel, model, 1, 6, 5));
        System.out.println("side branch connected - test passed");
        

    }
}
