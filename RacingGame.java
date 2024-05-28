import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class RacingGame {
    private static Random random = new Random();
    private static String SCOREBOARD_FILE = "scoreboard.txt";

    public static void game(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose difficulty (easy, medium, hard):");
        String difficulty = scanner.nextLine().trim().toLowerCase();

        Graph graph = new Graph();
        initializeGraph(graph, difficulty);

        List<Player> players = initializePlayers(scanner);

        graph.displayGraph();

        String finishNode = "Finish";
        boolean allPlayersStuck = true;

        boolean gameOn = true;
        while (gameOn) {
            for (Player player : players) {
                if (player.currentNode.equals(finishNode)) {
                    gameOn = false;
                    allPlayersStuck = false;
                    continue;
                }

                System.out.println(player.name + ", you are at " + player.currentNode);
                List<Edge> edges = graph.getEdges(player.currentNode);

                if (edges.isEmpty() || player.isStuck) {
                    System.out.println("No more paths to take from here. You are stuck!");
                    player.isStuck = true;
                    gameOn = false;
                    continue;
                }

                System.out.println("Choose your next path:");
                for (int i = 0; i < edges.size(); i++) {
                    Edge edge = edges.get(i);
                    System.out.println(i + ": Move to " + edge.destination + " (Time: " + edge.time + ", Health Cost: " + edge.healthCost + ")");
                }

                int choice = scanner.nextInt();
                if (choice < 0 || choice >= edges.size()) {
                    System.out.println("Invalid choice. You lose your turn.");
                } else {
                    Edge edge = edges.get(choice);
                    player.move(edge.destination, edge.time, edge.healthCost);
                }
            }
        }

        if (!allPlayersStuck) {
            calculateScores(players);
            updateScoreboard(players);
        } else {
            System.out.println("All players got stuck. No scores will be saved.");
        }

        scanner.close();
    }

    private static void initializeGraph(Graph graph, String difficulty) {
        int numNodes = 0;
        int minEdges = 0;
        int maxEdges = 0;
        int maxTime = 0;
        int maxHealthCost = 0;

        switch (difficulty) {
            case "easy":
                numNodes = 4;
                maxEdges = 2;
                maxTime = 5;
                maxHealthCost = 10;
                break;
            case "medium":
                numNodes = 6;
                maxEdges = 3;
                maxTime = 10;
                maxHealthCost = 20;
                break;
            case "hard":
                numNodes = 8;
                minEdges = 2;
                maxEdges = 4;
                maxTime = 15;
                maxHealthCost = 30;
                break;
            default:
                System.out.println("Invalid difficulty. Defaulting to medium.");
                numNodes = 6;
                maxEdges = 3;
                maxTime = 10;
                maxHealthCost = 20;
        }

        for (int i = 1; i <= numNodes; i++) {
            graph.addNode("Node" + i);
        }
        graph.addNode("Finish");

        for (String node : graph.getNodes()) {
            if (!node.equals("Finish")) {
                int numEdges = random.nextInt(maxEdges - minEdges + 1) + minEdges;
                for (int j = 0; j < numEdges; j++) {
                    String destination = graph.getNodes().get(random.nextInt(graph.getNodes().size()));
                    if (!node.equals(destination)) {
                        int time = random.nextInt(maxTime) + 1;
                        int healthCost = random.nextInt(maxHealthCost) + 1;
                        graph.addEdge(node, destination, time, healthCost);
                    }
                }
            }
        }

        graph.addEdge("Start", "Node1", random.nextInt(maxTime) + 1, random.nextInt(maxHealthCost) + 1);
        graph.addEdge("Start", "Node2", random.nextInt(maxTime) + 1, random.nextInt(maxHealthCost) + 1);
    }

    private static List<Player> initializePlayers(Scanner scanner) {
        List<Player> players = new ArrayList<>();
        System.out.println("Enter number of players:");
        int numPlayers = scanner.nextInt();
        scanner.nextLine(); 

        for (int i = 0; i < numPlayers; i++) {
            System.out.println("Enter name for player " + (i + 1) + ":");
            String name = scanner.nextLine();
            players.add(new Player(name, "Start", 100));
        }

        return players;
    }

    private static void calculateScores(List<Player> players) {
        System.out.println("Game Over! Scores:");
        for (Player player : players) {
            System.out.println(player.name + " - Time: " + player.time + ", Health: " + player.health);
        }
    }

    private static void updateScoreboard(List<Player> players) {
        try {
            List<String> lines = new ArrayList<>();
            File file = new File(SCOREBOARD_FILE);
            if (file.exists()) {
                lines = new ArrayList<>(Files.readAllLines(file.toPath()));
            }

            for (Player player : players) {
                if (!player.isStuck) {
                    lines.add(player.name + " - Time: " + player.time + ", Health: " + player.health);
                }
            }

            Files.write(file.toPath(), lines);
        } catch (IOException e) {
            System.err.println("Error updating scoreboard: " + e.getMessage());
        }
    }
}


class Graph {
    private Map<String, List<Edge>> adjList = new HashMap<>();
    private List<String> nodes = new ArrayList<>();

    public void addEdge(String source, String destination, int time, int healthCost) {
        adjList.computeIfAbsent(source, k -> new ArrayList<>()).add(new Edge(destination, time, healthCost));
    }

    public List<Edge> getEdges(String node) {
        return adjList.getOrDefault(node, new ArrayList<>());
    }

    public void addNode(String node) {
        nodes.add(node);
    }

    public List<String> getNodes() {
        return nodes;
    }

    public void displayGraph() {
        System.out.println("Course Overview:");
        for (String node : nodes) {
            System.out.print(node + " -> ");
            List<Edge> edges = adjList.get(node);
            if (edges != null) {
                for (Edge edge : edges) {
                    System.out.print(edge.destination + " (Time: " + edge.time + ", Health Cost: " + edge.healthCost + ") ");
                }
            }
            System.out.println();
        }
    }
}

class Edge {
    String destination;
    int time;
    int healthCost;

    Edge(String destination, int time, int healthCost) {
        this.destination = destination;
        this.time = time;
        this.healthCost = healthCost;
    }
}

class Player {
    String name;
    String currentNode;
    int health;
    int time;
    boolean isStuck = false;

    Player(String name, String startNode, int initialHealth) {
        this.name = name;
        this.currentNode = startNode;
        this.health = initialHealth;
        this.time = 0;
    }

    public boolean move(String destination, int time, int healthCost) {
        if (health > healthCost) {
            currentNode = destination;
            this.time += time;
            health -= healthCost;
            return true;
        } else {
            System.out.println(name + " does not have enough health to move to " + destination);
            isStuck = true;
            return false;
        }
    }
}