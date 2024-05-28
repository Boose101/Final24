import java.util.Scanner;

/**
 * main
 * Use as launchboard for programs
 */
public class Main {

    public static void main(String[] args) {
        Text[] text_arr = 
            {new Text("Hi, this is my final project", 700), 
            new Text ("A series of options will be provided", 700),
            new Text("Please select one", 500),
            new Text("I hope you have a nice viewing experiance", 700),
            new Text("Option one is a game of blackjack.", 750),
            new Text("Option two is a memorization game", 750),
            new Text("Option three is an timed Anagram Finder", 750),
            new Text("Option four is an Racing game", 750),
            new Text("If you want more information on how each topic is made please enter the number then help ie. 1help", 1000)
        };

        for (Text text : text_arr) {
            System.out.println(text.get_text());
            Help.sleep(text.get_time_length());
            Help.clearC();
        }

        Scanner scan = new Scanner(System.in);

        boolean not_entered = true;

        while(not_entered) {
            System.out.println("If you want more information on how each topic is made please enter the number then help ie. 1help");
            System.out.println("Please enter the number of the game you would like to play");
            String input = scan.nextLine();
            int option = -1;

            if(input.contains("help")){
                if(input.substring(0, 1).matches("[1-4]")){
                    option = Integer.parseInt(input.substring(0, 1));
                    not_entered = false;
                    
                    if(option == 1){
                        System.out.println("The blackjack game uses a linked list to hold all of the cards");
                    }else if(option == 2){
                        System.out.println("The memorization game uses an AVL as the thing you need to memorize, and tests AVL inserition order");
                    }else if(option == 3){
                        System.out.println("The anagram game uses hashmaps to do a quick lookup of the anagrams of the selected word");
                    }else{
                        System.out.println("The racing game uses a graph as the course that the player / players traverse. Credit due to Mrs. Castillo");
                    }
                }
            }
            else if(input.matches("[1-4]")){
                option = Integer.parseInt(input);
                System.out.println("hi");
                not_entered = false;
                if(option == 1){
                    Blackjack.game();
                }else if(option == 2){
                    Memorize.game();
                }else if(option == 3){
                    TimedAnagramFinder.game();
                }else{
                    RacingGame.game();
                }
            } else{
                System.out.println("Please enter a number between 1 and 4 or enter a help option ie. 1help - 4help");
                Help.sleepC(2000);
            }
        }
        scan.close();
    }

}