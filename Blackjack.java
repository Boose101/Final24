import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

public class Blackjack {
    private static LinkedList<String> deck;
    private static ArrayList<String> dealer;
    private static ArrayList<String> player;
    private static int dealerTotal;
    private static int playerTotal;


    public static void game() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Welcome to Blackjack!");
        System.out.println("You start with 200$ to bet with");
        Help.sleepC(1500);

        int cash = 200;
        boolean playing = true;

        ArrayList<String> oneDeck = Help.readFile("Deck.txt");
        

        deck = new LinkedList<>(Help.readFile("Deck.txt"));

        for(int i = 0; i < 4; i++){
            for (String card : oneDeck) {
                deck.add(card);
            }
        }
        //5 card deck

        while (playing) {
            System.out.println("You have " + cash + "$");
            System.out.println("How much do you want to buy in with?");
            int bet = scan.nextInt();
            if (bet > cash) {
                System.out.println("You tried to cheat the casino. That's not allowed");
                System.exit(1);
            }

            Help.clearC();
            System.out.println("The Dealer shuffles");
            Collections.shuffle(deck);

            Help.sleepC(2000);

            dealer = new ArrayList<>();
            dealerTotal = 0;
            player = new ArrayList<>();
            playerTotal = 0;

            deal();
            deal();

            boolean gameOn = true;
            while (gameOn) {
                System.out.println("You have " + player + ". Your value is " + playerTotal);
                System.out.println("The Dealer has " + dealer + ". The dealer value is " + dealerTotal);
                System.out.println("Do you want to hit, stand, or double?");
                String choice = scan.next();
                Help.clearC();

                switch (choice.toLowerCase()) {
                    case "hit":
                        dealPlayer();
                        if (playerTotal > 21) {
                            System.out.println("You busted");
                            cash -= bet;
                            gameOn = false;
                        }
                        break;
                    case "stand":
                        while (dealerTotal < 17) {
                            dealDealer();
                        }
                        cash = determineOutcome(bet, cash);
                        gameOn = false;
                        break;
                    case "double":
                        if (player.size() == 2) {
                            bet *= 2;
                            if (bet > cash) {
                                System.out.println("You tried to cheat the casino. That's not allowed");
                                System.exit(1);
                            }
                            System.out.println("Your bet has doubled to " + bet);
                            dealPlayer();
                            if (playerTotal > 21) {
                                System.out.println("You busted");
                                cash -= bet;
                            } else {
                                while (dealerTotal < 17) {
                                    dealDealer();
                                }
                                cash = determineOutcome(bet, cash);
                            }
                            gameOn = false;
                        } else {
                            System.out.println("You can only double down on the first move.");
                        }
                        break;
                    default:
                        System.out.println("Invalid choice. Please choose hit, stand, or double.");
                        break;
                }

                if (!gameOn) {
                    System.out.println("Your new cash total: " + cash);
                    if (cash <= 0) {
                        System.out.println("You are out of money! Game over.");
                        playing = false;
                    } else {
                        System.out.println("Do you want to play again? (yes/no)");
                        String playAgain = scan.next();
                        if (!playAgain.equalsIgnoreCase("yes")) {
                            playing = false;
                        }
                    }
                }
            }
        }

        scan.close();
    }

    private static void deal() {
        dealPlayer();
        dealDealer();
    }

    private static void dealPlayer() {
        player.add(deck.pop());
        playerTotal += getCardValue(player.get(player.size() - 1));
        System.out.println("Your cards: " + player);
    }

    private static void dealDealer() {
        dealer.add(deck.pop());
        dealerTotal += getCardValue(dealer.get(dealer.size() - 1));
        System.out.println("Dealer value: " + dealerTotal);
    }

    private static int getCardValue(String card) {
        String value = card.split("-")[0];
        switch (value) {
            case "11":  
            case "12":
            case "13":
                return 10;
            case "1": 
                return (playerTotal + 11 > 21) ? 1 : 11;
            default:
                return Integer.parseInt(value);
        }
    }

    private static int determineOutcome(int bet, int cash) {
        if (playerTotal > 21) {
            System.out.println("You busted. Dealer wins.");
            cash -= bet;
        } else if (dealerTotal > 21 || playerTotal > dealerTotal) {
            System.out.println("You win!");
            cash += bet;
        } else if (playerTotal < dealerTotal) {
            System.out.println("Dealer wins.");
            cash -= bet;
        } else {
            System.out.println("Push, bet is returned.");
        }
        return cash;
    }
}