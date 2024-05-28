import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Memorize {

    public static void game() {
        Scanner scan = new Scanner(System.in);

        Text[] text_arr = {
            new Text("This is an AVL Tree memorization game.", 1000),
            new Text("You will be given a number of nodes to memorize, in a set amount of time", 1500),
            new Text("You will need to figure out how to insert them.", 1000),
            new Text("Note: It is pretty annoying... even on easy", 2000),
        };

        for (Text text : text_arr) {
            System.out.println(text.get_text());
            Help.sleep(text.get_time_length());
            Help.clearC();
        }

        System.out.println("Pick a mode: Easy, Medium, or Difficult");

        int numNodes = 0;
        int maxVal = 0;

        switch(scan.nextLine().toLowerCase()){
            case "easy":
                numNodes = 5;
                maxVal = 10;
            case "medium":
                numNodes = 10;
                maxVal = 25;
            case "difficult":
                numNodes = 20;
                maxVal = 50;
        }

        List<Integer> insertionOrder = new ArrayList<>();
        AVLTree avl = generateRandomAVLTree(numNodes, maxVal, insertionOrder);
        System.out.println("In-order traversal of the AVL Tree:");
        avl.inOrder();
        System.out.println();

        System.out.println("Guess the insertion order:");
        List<Integer> guessedOrder = new ArrayList<>();
        String guessed = scan.nextLine();
        String[] guessedSplit = guessed.split(" ");
        for(String str: guessedSplit){
            guessedOrder.add(Integer.parseInt(str));
        }

        if (insertionOrder.equals(guessedOrder)) {
            System.out.println("Correct! You guessed the insertion order.");
        } else {
            System.out.println("Incorrect. The actual insertion order was:");
            System.out.println(insertionOrder);
        }

        scan.close();
    }

    public static AVLTree generateRandomAVLTree(int numberOfNodes, int maxValue, List<Integer> insertionOrder) {
        Random random = new Random();
        AVLTree avl = new AVLTree(null);
        Set<Integer> values = new HashSet<>();

        while (values.size() < numberOfNodes) {
            int value = random.nextInt(maxValue + 1); 
            if (!values.contains(value)) {
                avl.insert(value);
                insertionOrder.add(value);
                values.add(value);
            }
        }
        return avl;
    }

    public static boolean areBSTsEqual(Node n1, Node n2) {
        if (n1 == null && n2 == null) {
            return true;
        }
        if (n1 == null || n2 == null) {
            return false;
        }
        if (n1.getVal() != n2.getVal()) {
            return false;
        }
        return areBSTsEqual(n1.getLeft(), n2.getLeft()) && areBSTsEqual(n1.getRight(), n2.getRight());
    }
}

class Node {
    private Node left;
    private Node right;
    private int val;
    private int height;

    public Node(Node left, Node right, int val) {
        this.left = left;
        this.right = right;
        this.val = val;
        this.height = 1;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public int getVal() {
        return val;
    }

    public int getHeight() {
        return height;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

class AVLTree {
    private Node root;

    public AVLTree(Node root) {
        this.root = root;
    }

    public void inOrder() {
        inOrderRec(root);
    }

    private void inOrderRec(Node root) {
        if (root != null) {
            inOrderRec(root.getLeft());
            System.out.print(root.getVal() + " ");
            inOrderRec(root.getRight());
        }
    }

    public void insert(int val) {
        root = insertRec(root, val);
    }

    private Node insertRec(Node node, int val) {
        if (node == null) {
            return new Node(null, null, val);
        }

        if (val < node.getVal()) {
            node.setLeft(insertRec(node.getLeft(), val));
        } else if (val > node.getVal()) {
            node.setRight(insertRec(node.getRight(), val));
        } else {
            return node;
        }

        node.setHeight(1 + Math.max(height(node.getLeft()), height(node.getRight())));

        int balance = getBalance(node);


        // LL
        if (balance > 1 && val < node.getLeft().getVal()) {
            return rightRotate(node);
        }

        // RR
        if (balance < -1 && val > node.getRight().getVal()) {
            return leftRotate(node);
        }

        // LR
        if (balance > 1 && val > node.getLeft().getVal()) {
            node.setLeft(leftRotate(node.getLeft()));
            return rightRotate(node);
        }

        // RL
        if (balance < -1 && val < node.getRight().getVal()) {
            node.setRight(rightRotate(node.getRight()));
            return leftRotate(node);
        }

        return node;
    }

    private Node rightRotate(Node y) {
        Node x = y.getLeft();
        Node T2 = x.getRight();

        x.setRight(y);
        y.setLeft(T2);

        y.setHeight(Math.max(height(y.getLeft()), height(y.getRight())) + 1);
        x.setHeight(Math.max(height(x.getLeft()), height(x.getRight())) + 1);

        return x;
    }

    private Node leftRotate(Node x) {
        Node y = x.getRight();
        Node T2 = y.getLeft();

        y.setLeft(x);
        x.setRight(T2);

        x.setHeight(Math.max(height(x.getLeft()), height(x.getRight())) + 1);
        y.setHeight(Math.max(height(y.getLeft()), height(y.getRight())) + 1);

        return y;
    }

    private int height(Node node) {
        if (node == null) {
            return 0;
        }
        return node.getHeight();
    }

    private int getBalance(Node node) {
        if (node == null) {
            return 0;
        }
        return height(node.getLeft()) - height(node.getRight());
    }
}
