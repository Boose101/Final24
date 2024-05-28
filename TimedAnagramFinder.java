import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class TimedAnagramFinder {
    private static HashMap<String, List<String>> dictionaryMap = new HashMap<>();
    private static Set<String> validWords = new HashSet<>();
    private static long TIME_LIMIT = 30000;
    private static boolean timeUp = false;

    public static void game(){

        ArrayList<String> dictionary = Help.readFile("english.txt");
        populateDictionaryMap(dictionary);

        String randomWord = getRandomWord(dictionary);

        Set<String> anagrams = findValidAnagrams(randomWord);
        validWords.addAll(anagrams);

        System.out.println("Find as many anagrams as you can for the word: " + randomWord);
        System.out.println("You have 30 seconds. Go!");

        Timer timer = new Timer();
        timer.schedule(new TimeUpTask(), TIME_LIMIT);

        List<String> userAnagrams = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        while (!timeUp) {
            if (scanner.hasNextLine()) {
                String userInput = scanner.nextLine().trim();
                if (validWords.contains(userInput) && !userAnagrams.contains(userInput)) {
                    userAnagrams.add(userInput);
                    System.out.println("Good! Keep going!");
                } else {
                    System.out.println("Invalid or duplicate anagram. Try again.");
                }
            }
        }

        scanner.close();

        System.out.println("Time's up!");
        System.out.println("You found " + userAnagrams.size() + " anagrams: " + userAnagrams);
        System.out.println("Possible anagrams were: " + anagrams);
    }
    private static void populateDictionaryMap(ArrayList<String> dictionary) {
        for (String word : dictionary) {
            String sortedWord = sortString(word);
            dictionaryMap.computeIfAbsent(sortedWord, k -> new ArrayList<>()).add(word);
        }
    }

    private static Set<String> findValidAnagrams(String word) {
        Set<String> results = new HashSet<>();
        findSubsets(word, "", results);
        Set<String> anagrams = new HashSet<>();
        for (String subset : results) {
            if (dictionaryMap.containsKey(sortString(subset))) {
                for (String dictWord : dictionaryMap.get(sortString(subset))) {
                    if (canFormFrom(dictWord, word)) {
                        anagrams.add(dictWord);
                    }
                }
            }
        }
        return anagrams;
    }

    private static void findSubsets(String word, String current, Set<String> results) {
        if (!current.isEmpty()) {
            results.add(current);
        }
        for (int i = 0; i < word.length(); i++) {
            findSubsets(word.substring(0, i) + word.substring(i + 1), current + word.charAt(i), results);
        }
    }

    private static String sortString(String word) {
        char[] chars = word.toCharArray();
        java.util.Arrays.sort(chars);
        return new String(chars);
    }

    private static String getRandomWord(ArrayList<String> dictionary) {
        Random random = new Random();
        return dictionary.get(random.nextInt(dictionary.size()));
    }

    private static boolean canFormFrom(String candidate, String original) {
        int[] originalCount = new int[26];
        for (char c : original.toCharArray()) {
            originalCount[c - 'a']++;
        }
        for (char c : candidate.toCharArray()) {
            if (originalCount[c - 'a'] == 0) {
                return false;
            }
            originalCount[c - 'a']--;
        }
        return true;
    }

    static class TimeUpTask extends TimerTask {
        @Override
        public void run() {
            timeUp = true;
            System.out.println("\nTime's up!");
        }
    }
}
