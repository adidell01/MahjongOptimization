import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nSelect starting player (0, 1, 2, 3(player))");
        Game game = new Game(scanner.nextInt());
        System.out.println(game.getPlayer().getHand().toString());
        System.out.println("Shanten: " + game.getPlayer().getShanten());
        String command;
        boolean showShanten = true;
        long time = System.nanoTime();
        DrawAnalyzer analyzers = new DrawAnalyzer(game);
        analyzers.generateGraph(3);
        System.out.println();
        do {
            System.out.println("Time: " + (double) (System.nanoTime() - time)/1000000000);
            command = scanner.next();
            time = System.nanoTime();
            if(command.equals("discard")){
                String in = scanner.nextLine();
                boolean found = false;
                try{
                int index = Integer.valueOf(in.substring(1));
                if(index < 0 || index > 13){
                    System.out.println("Index must be between 0 and 13");
                } else {
                    if(!game.discard(index)){
                        System.out.println("The round has ended");
                        return;
                    }
                    found = true;
                }
                }
                catch(NumberFormatException e){
                    for(Tile tile : game.getPlayer().getHand()){
                        if(tile.toString().equalsIgnoreCase(in.substring(1))){
                            if(!game.discard(game.getPlayer().getHand().indexOf(tile))){
                                System.out.println("The round has ended");
                                return;
                            }
                            found = true;
                            break;
                        }
                    }
                    if(!found){
                        System.out.println("Tile" + in + " not found\n");
                    }
                }
                if(found){
                    System.out.println(game.getPlayer().getHand().toString());
                    if(showShanten){
                        System.out.println("Shanten: " + game.getPlayer().getShanten());
                    }
                }
            } else if(command.equals("hand")){
                System.out.println(game.getPlayer().getHand().toString());
                if(showShanten){
                    System.out.println("Shanten: " + game.getPlayer().getShanten());
                }
            } else if(command.equals("shanten")){
                if(showShanten){
                    showShanten = false;
                    System.out.println("Shanten disabled");
                } else {
                    showShanten = true;
                    System.out.println("Shanten enabled");
                }
            } else if(command.equals("analyze")){
                try{
                int depth = scanner.nextInt();
                int width = scanner.nextInt();
                Node rootNode = new Node(game);
                HandAnalyzer analyzer = new HandAnalyzer(rootNode);
                System.out.println("Generating graph...");
                analyzer.generateGraph(depth, width);
                System.out.println("Graph generated.");
                analyzer.printRootChildren();
                System.out.println("Best discard: " + analyzer.getBestDiscard());
                }
                catch(NoSuchElementException e){
                    System.out.println("Please enter a depth an width for \"analyze\".");
                }
            } else if(!command.equals("exit")){
                System.out.println("discard <index>:\tDiscard tile at given index. Index must be number between 0 (left-most tile) and 13 (right-most tile)");
                System.out.println("discard <tile>:\t\tDiscard specified tile if present.");
                System.out.println("hand:\t\t\tPrints the player's current hand");
                System.out.println("shanten:\t\tEnable/Disable showing shanten when printing hand");
                System.out.println("exit:\t\t\tTerminates the programm");
            }
        } while(!command.equals("exit"));
    }
}