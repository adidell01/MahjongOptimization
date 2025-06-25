import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Random randomizer = new Random();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose between playing manually or using the analyzer:");
        System.out.println("1: Play manually");
        System.out.println("2: Use analyzer");
        int choice = scanner.nextInt();
        long time = System.nanoTime();
        long totalTime = System.nanoTime();
        if (choice == 1) {
            System.out.println("\nSelect starting player (0, 1, 2, 3(player))");
            Game game = new Game(scanner.nextInt());
            System.out.println(game.getPlayer().getHand().toString());
            System.out.println("Shanten: " + game.getPlayer().getShanten());
            String command;
            boolean showShanten = true;
            DrawAnalyzer analyzers = new DrawAnalyzer(game);
            analyzers.generateGraph(3);
            System.out.println();
            do {
                System.out.println("Time: " + (double) (System.nanoTime() - time) / 1000000000);
                command = scanner.next();
                time = System.nanoTime();
                if (command.equals("discard")) {
                    String in = scanner.nextLine();
                    boolean found = false;
                    try {
                        int index = Integer.valueOf(in.substring(1));
                        if (index < 0 || index > 13) {
                            System.out.println("Index must be between 0 and 13");
                        } else {
                            if (!game.discard(index)) {
                                System.out.println("The round has ended");
                                return;
                            }
                            found = true;
                        }
                    } catch (NumberFormatException e) {
                        for (Tile tile : game.getPlayer().getHand()) {
                            if (tile.toString().equalsIgnoreCase(in.substring(1))) {
                                if (!game.discard(game.getPlayer().getHand().indexOf(tile))) {
                                    System.out.println("The round has ended");
                                    return;
                                }
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            System.out.println("Tile" + in + " not found\n");
                        }
                    }
                    if (found) {
                        System.out.println(game.getPlayer().getHand().toString());
                        if (showShanten) {
                            System.out.println("Shanten: " + game.getPlayer().getShanten());
                        }
                    }
                } else if (command.equals("hand")) {
                    System.out.println(game.getPlayer().getHand().toString());
                    if (showShanten) {
                        System.out.println("Shanten: " + game.getPlayer().getShanten());
                    }
                } else if (command.equals("shanten")) {
                    if (showShanten) {
                        showShanten = false;
                        System.out.println("Shanten disabled");
                    } else {
                        showShanten = true;
                        System.out.println("Shanten enabled");
                    }
                } else if (command.equals("analyze")) {
                    try {
                        int depth = scanner.nextInt();
                        int width = scanner.nextInt();
                        Node rootNode = new Node(game);
                        HandAnalyzer analyzer = new HandAnalyzer(rootNode);
                        System.out.println("Generating graph...");
                        analyzer.generateGraph(depth, width);
                        System.out.println("Graph generated.");
                        analyzer.printRootChildren();
                        System.out.println("Best discard: " + analyzer.getBestDiscard());
                    } catch (NoSuchElementException e) {
                        System.out.println("Please enter a depth an width for \"analyze\".");
                    }
                } else if (!command.equals("exit")) {
                    System.out.println(
                            "discard <index>:\tDiscard tile at given index. Index must be number between 0 (left-most tile) and 13 (right-most tile)");
                    System.out.println("discard <tile>:\t\tDiscard specified tile if present.");
                    System.out.println("hand:\t\t\tPrints the player's current hand");
                    System.out.println("shanten:\t\tEnable/Disable showing shanten when printing hand");
                    System.out.println("exit:\t\t\tTerminates the programm");
                }
            } while (!command.equals("exit"));
        } else if (choice == 2) {
            System.out.println("How many games do you want to analyze? (Enter a positive integer)");
            int numGames = scanner.nextInt();
            System.out.println("select analyzer type:");
            System.out.println("1: DrawAnalyzer");
            System.out.println("2: HandAnalyzer");
            int analyzerChoice = scanner.nextInt();
            System.out.println("Graph depth? (Enter a positive integer)");
            int depth = scanner.nextInt();
            if (analyzerChoice == 1) {
                totalTime = System.nanoTime();
                for (int i = 0; i < numGames; i++) {
                    Game game = new Game(randomizer.nextInt(4));
                    long gameTime = System.nanoTime();
                    DrawAnalyzer drawAnalyzer = new DrawAnalyzer(game);
                    drawAnalyzer.generateGraph(depth);
                    long genTime = System.nanoTime();
                    time = System.nanoTime();
                    while (game.getPlayer().getShanten() != 0 && game.discard(drawAnalyzer.getBestDiscard())){
                        System.out.println("Time: " + (double) (System.nanoTime() - time) / 1000000000);
                        System.out.println(game.getPlayer().getHand().toString());
                        System.out.println("Shanten: " + game.getPlayer().getShanten());
                        time = System.nanoTime();
                    }
                    System.out.println("Graph generation time: " + (double) (genTime - gameTime) / 1000000000);
                    System.out.println("Game time: " + (double) (System.nanoTime() - gameTime) / 1000000000);
                }
                System.out.println("Total time: " + (double) (System.nanoTime() - totalTime) / 1000000000);

            } else if (analyzerChoice == 2) {
                System.out.println("Graph width? (Enter a positive integer)");
                int width = scanner.nextInt();
                for (int i = 0; i < numGames; i++) {

                    Game game = new Game(2);
                    Node rootNode = new Node(game);

                    while (rootNode.getShanten() > 0 && game.getTiles().size() > 0) {
                        HandAnalyzer handAnalyzer = new HandAnalyzer(rootNode);
                        handAnalyzer.generateGraph(depth, width);
                        Tile bestDiscard = handAnalyzer.getBestDiscard();
                        int indexToRemove = -1;
                        for (int j = 0; j < game.getPlayer().getHand().size(); j++) {
                            if (game.getPlayer().getHand().get(j).toString().equals(bestDiscard.toString())) {
                                indexToRemove = j;
                            }
                        }
                        game.discard(indexToRemove);
                        
                        rootNode = new Node(game);
                        System.out.println(game.getPlayer().getHand().toString());
                        System.out.println("Shanten: " + game.getPlayer().getShanten());
                    }
                }
            } else {
                System.out.println("Invalid analyzer choice. Please restart the program and choose either 1 or 2.");
            }
        } else {
            System.out.println("Invalid choice. Please restart the program and choose either 1 or 2.");
        }
        scanner.close();
    }
}