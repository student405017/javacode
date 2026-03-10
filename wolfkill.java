import java.text.BreakIterator;
import java.util.Random;
import java.util.Scanner;

public class test {
    static class player {
        private int id;
        private String role;
        private boolean alive;

        public player(){
            this.id=id ;
            this.role=role;
            this.alive=true;
        }
        public int getId(){
            
            
            return 0;
        }

        public String getRole(){
            return role;
        }
        public boolean getAlive(){
            return alive;
        }

        public void kill(){
            alive=false;
        }

        public String getPublicInfo(){
            if(alive){
                return "player " +id+"[alive]";
            }
            else{
                return "player "+id+"[dead]";
            }
        }
        public static void main(String[] args){
            Scanner sc =new Scanner(System.in);
            Random rand =new Random();
            System.out.println("wolfgame");
            System.out.println("entet number of player");
            int n=sc.nextInt();
            sc.nextLine();
            while (n<4|| n>10) {
                System.out.println("4 and 10 ");
                n=sc.nextInt();
                sc.nextLine();
            }
            player[] players=new player[n];
            int wolfIndex=rand.nextInt();

            for(int i=0;i<n;i++){
                if( i==wolfIndex){
                    players[i]=new player(i+1,role="狼人");
                }else{
                    players[i]=new player(i+1,role="村民");
                }
               
            }
            System.out.println();
            System.out.print("role assignement start");
            System.out.print("each player take turn to role");
            for(int i =0;i<n;i++){
                System.out.println();
                System.out.println("player"+(i+1)+"please Enter");
                sc.nextLine();
                System.out.println("your role:"+players[i].getRole());
                System.out.println("memorzie your role and next people" );
                sc.nextLine();
                for(int line=0;line<30;line++){
                    System.out.println();
                }
            }
            boolean gameOver=false;
            int round=1;
            while (!gameOver){
                System.out.println("Round"+round);
                System.out.println();
                System.out.println("night falls werwolf wakes up");
                int aliveWolf= findAliveWhereWolf(players);
                if(aliveWolf!= -1){
                    System.out.println("werwolf is your turn");
                    System.out.println("allive players");
                    printAlivePlayers(players);
                    int target=-1;

                    while (true){
                        System.out.println("chose a player to kill");
                        if(sc.hasNext()){
                            targetID=sc.next();
                            System.out.println();
                            if(isValidTarget(targetID,players[aliveWereWolf].getId())){
                                
                                break;

                            }else{
                                System.out.println("再輸入一次");
                                sc.nextLine();
                            }
                        }
                    }
                    players[target-1].kill();
                    System.out.println("night resaults :players"+players+"die");
                }else{
                     System.out.println("no werewolf live");
                }
                if(checkKillagerwin(players)){
                    System.out.println("villagers win");
                    gameOver=true;
                }else if(checkKillwerewolfwin(players)){
                    System.out.println("wolf win");
                    gameOver=true;
                }

                if(gameOver){
                    break;
                }
                int voteId =-1;
      

            }
            public static int printAlivePlayers(Player[] players){
                    for(int i=0;i<players.length;i++){
                        if(players[i].isAlive()&& players[i].getRole().equals("werewolf")){
                            return i;
                        }
                    }
                    return -1;
                }
            public static void findAliveWhereWolf(Player[] players){
                    for(int i=0;i<players.length;i++){
                        System.out.print(players[i].getPublicInfo());
                    }
            }
        }
    }
}
