import java.io.File;  
import java.io.FileNotFoundException;  
import java.util.Scanner;

public class ReadFile {
  public static void main(String[] args) {
    try {
      Scanner inputScanner = new Scanner(System.in);
      System.out.println("Enter input filename: ");
      String fileName = inputScanner.nextLine();

      File myObj = new File(fileName);
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
        String data = myReader.nextLine();
        System.out.println(data);
      }
      myReader.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }
}