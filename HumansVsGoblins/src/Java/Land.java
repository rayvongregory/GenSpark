import java.util.ArrayList;
import java.util.Arrays;

public class Land {
    private final int numRows;

    public Land(int numPlayers) {
        switch (numPlayers) {
            case 3 -> this.numRows = 6;
            case 5 -> this.numRows = 7;
            default -> this.numRows = 8;
        }
    }

    public int getNumRows() {
        return this.numRows;
    }

    public ArrayList<GamePiece> getSquare(int x, int y, ArrayList<Human> humans, ArrayList<Goblin> goblins, ArrayList<Item> items) {
        ArrayList<GamePiece> pieces = new ArrayList<>();
        for(Human h:humans) {
            if(Arrays.toString(h.getPosition()).equals("[%s, %s]".formatted(x,y))) {
                pieces.add(h);
            }
        }
        for(Goblin g:goblins) {
            if(Arrays.toString(g.getPosition()).equals("[%s, %s]".formatted(x,y))) {
                pieces.add(g);
            }
        }
        for(Item i: items) {
            if(Arrays.toString(i.getPosition()).equals("[%s, %s]".formatted(x,y))) {
                pieces.add(i);
            }
        }
        return pieces;
    }

    public void print(ArrayList<Human> humans, ArrayList<Goblin> goblins, ArrayList<Item> items) {
        for (int i = 0; i < this.numRows; i++) {
            if(i == 0) {
                System.out.print("　　　　　　　　"+getFullwidthNumber(i)+"　　　　　　　");
            } else if(i == numRows-1) {
                System.out.println(getFullwidthNumber(i));
            } else {
                System.out.print(getFullwidthNumber(i)+"　　　　　　　");
            }
        }
        for(int i = 0; i < this.numRows;i++) {
            System.out.print("　　　　");
            System.out.print("－－－－－－－－".repeat(this.numRows));
            System.out.println("－");
            System.out.print("　　　　｜");
            System.out.println("　　　　　　　｜".repeat(this.numRows));
            System.out.print("　　"+getFullwidthNumber(i)+"　｜");
            for(int j = 0; j < this.numRows; j++) {
                ArrayList<GamePiece> gamePieces = getSquare(j,i,humans, goblins, items); //works better visually when j and i are switched
                  switch(gamePieces.size()) {
                      case 0-> System.out.print("　　　　　　　｜");
                      case 1-> {
                          GamePiece gp = gamePieces.get(0);
                          System.out.printf("　　　%s　　　｜", gp.getIcon());
                      }
                      case 2-> {
                          GamePiece gp1 = gamePieces.get(0);
                          GamePiece gp2 = gamePieces.get(1);
                          System.out.printf("　　%s　%s　　｜", gp1.getIcon(), gp2.getIcon());
                      }
                      case 3-> {
                          GamePiece gp1 = gamePieces.get(0);
                          GamePiece gp2 = gamePieces.get(1);
                          GamePiece gp3 = gamePieces.get(2);
                          System.out.printf("　　%s%s%s　　｜", gp1.getIcon(), gp2.getIcon(), gp3.getIcon());
                      }
                      case 4 -> {
                          GamePiece gp1 = gamePieces.get(0);
                          GamePiece gp2 = gamePieces.get(1);
                          GamePiece gp3 = gamePieces.get(2);
                          GamePiece gp4 = gamePieces.get(3);
                          System.out.printf("　%s%s　%s%s　｜", gp1.getIcon(), gp2.getIcon(), gp3.getIcon(),
                                  gp4.getIcon());
                      }
                      case 5 -> {
                          GamePiece gp1 = gamePieces.get(0);
                          GamePiece gp2 = gamePieces.get(1);
                          GamePiece gp3 = gamePieces.get(2);
                          GamePiece gp4 = gamePieces.get(3);
                          GamePiece gp5 = gamePieces.get(4);
                          System.out.printf("　%s%s%s%s%s　｜", gp1.getIcon(), gp2.getIcon(), gp3.getIcon(),
                                  gp4.getIcon(), gp5.getIcon());
                      }
                      case 6 -> {
                          GamePiece gp1 = gamePieces.get(0);
                          GamePiece gp2 = gamePieces.get(1);
                          GamePiece gp3 = gamePieces.get(2);
                          GamePiece gp4 = gamePieces.get(3);
                          GamePiece gp5 = gamePieces.get(4);
                          GamePiece gp6 = gamePieces.get(5);
                          System.out.printf("%s%s%s　%s%s%s｜", gp1.getIcon(), gp2.getIcon(), gp3.getIcon(),
                                  gp4.getIcon(), gp5.getIcon(), gp6.getIcon());
                      }
                      default -> {
                          GamePiece gp1 = gamePieces.get(0);
                          GamePiece gp2 = gamePieces.get(1);
                          GamePiece gp3 = gamePieces.get(2);
                          GamePiece gp4 = gamePieces.get(3);
                          GamePiece gp5 = gamePieces.get(4);
                          GamePiece gp6 = gamePieces.get(5);
                          GamePiece gp7 = gamePieces.get(6);
                          System.out.printf("%s%s%s%s%s%s%s｜", gp1.getIcon(), gp2.getIcon(), gp3.getIcon(),
                                  gp4.getIcon(), gp5.getIcon(), gp6.getIcon(), gp7.getIcon());
                      }
                  }
            }
            System.out.print("\n　　　　｜");
            System.out.println("　　　　　　　｜".repeat(this.numRows));
            if(i == this.numRows-1) {
                System.out.print("　　　　");
                System.out.print("－－－－－－－－".repeat(this.numRows));
                System.out.println("－\n");
            }
        }
    }

    private static char getFullwidthNumber(int num) {
       switch(num) {
           case 0 -> {
               return '０';
           }
           case 1 -> {
               return '１';
           }
           case 2 -> {
               return '２';
           }
           case 3 -> {
               return '３';
           }
           case 4 -> {
               return '４';
           }
           case 5 -> {
               return '５';
           }
           case 6 -> {
               return '６';
           }
           case 7 -> {
               return '７';
           }
           case 8 -> {
               return '８';
           }
           default -> {
               return '９';
           }
       }
    }
}
