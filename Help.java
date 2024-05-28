import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
public class Help {


    public static ArrayList<String> readFile(String fileName){
        ArrayList<String> arr = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                arr.add(line);
            }
            return arr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Usability Functions
        public static void sleep(int num){
            try{Thread.sleep(num);} 
            catch (InterruptedException e) {Thread.currentThread().interrupt();}
        }
    
        public static void clearC(){
            System.out.print("\033[H\033[2J");  
            System.out.flush();  
        }

        public static void sleepC(int num){
            try{Thread.sleep(num);} 
            catch (InterruptedException e) {Thread.currentThread().interrupt();}
            System.out.print("\033[H\033[2J");  
            System.out.flush();  
        }
}
