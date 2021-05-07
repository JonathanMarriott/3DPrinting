package ProgressBar;

public class ProgressBar {
    private int done, total;
    private char[] out;

    private int progress, progressTen;

    public ProgressBar(int total){
        this.done = 0;
        this.total = total;
        this.out = "\r[          ] ".toCharArray();

        this.progress = 0;
        this.progressTen = 0;
    }

    public void makeProgress(){
        done++;

        synchronized(this){
            if(((done * 100) / total) > progress){
                progress = progress + 1;
                progressTen = progress % 10;
                if(progressTen == 0) out[(progress / 10)+1] = '=';
                System.out.print(String.valueOf(out) + String.valueOf(progress) + "%");
                // if(progress == 100) System.out.print("\n");
            }
        }
    }
}
