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
        do {
            System.out.println();
            command = scanner.next();
            if(command.equals("discard")){
                String in = scanner.nextLine();
                boolean found = false;
                try{
                int index = Integer.valueOf(in.substring(1));
                if(index < 0 || index > 13){
                    System.out.println("Index must be between 0 and 13");
                } else {
                    game.discard(index);
                    found = true;
                }
                }
                catch(NumberFormatException e){
                    for(Tile tile : game.getPlayer().getHand()){
                        if(tile.toString().equalsIgnoreCase(in.substring(1))){
                            game.discard(game.getPlayer().getHand().indexOf(tile));
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