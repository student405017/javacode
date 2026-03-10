import java.util.Random;
import java.util.Scanner;

public class wolfkill {
    private static final String WEREWOLF = "狼人";
    private static final String VILLAGER = "村民";

    static class Player {
        private final int id;
        private final String role;
        private boolean alive;

        Player(int id, String role) {
            this.id = id;
            this.role = role;
            this.alive = true;
        }

        public int getId() {
            return id;
        }

        public String getRole() {
            return role;
        }

        public boolean isAlive() {
            return alive;
        }

        public void kill() {
            alive = false;
        }

        public String getPublicInfo() {
            return "玩家 " + id + (alive ? " [存活]" : " [死亡]");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        System.out.println("狼人殺");
        int playerCount = readPlayerCount(scanner);
        Player[] players = createPlayers(playerCount, random);

        showRolesToPlayers(players, scanner);
        runGame(players, scanner);
    }

    private static int readPlayerCount(Scanner scanner) {
        System.out.println("請輸入玩家人數（4 到 10 人）：");

        while (true) {
            if (scanner.hasNextInt()) {
                int count = scanner.nextInt();
                scanner.nextLine();
                if (count >= 4 && count <= 10) {
                    return count;
                }
            } else {
                scanner.nextLine();
            }
            System.out.println("請輸入 4 到 10 之間的有效數字：");
        }
    }   

    private static Player[] createPlayers(int playerCount, Random random) { 
        Player[] players = new Player[playerCount];
        int wolfIndex = random.nextInt(playerCount);

        for (int i = 0; i < playerCount; i++) {
            String role = (i == wolfIndex) ? WEREWOLF : VILLAGER;
            players[i] = new Player(i + 1, role);
        }
        return  players;
    }

    private static void showRolesToPlayers(Player[] players, Scanner scanner) {
        System.out.println();
        System.out.println("開始分配身分。");
        System.out.println("請每位玩家依序按 Enter 查看自己的角色。");

        for (Player player : players) {
            System.out.println();
            System.out.println("請玩家 " + player.getId() + " 按 Enter。");
            scanner.nextLine();
            System.out.println("你的角色是：" + player.getRole());
            System.out.println("請記住你的角色，然後按 Enter 換下一位玩家。");
            scanner.nextLine();
            clearScreenWithBlankLines();
        }
    }

    private static void runGame(Player[] players, Scanner scanner) {
        int round = 1;

        while (true) {
            System.out.println("第 " + round + " 回合");
            System.out.println("天黑請閉眼，狼人請睜眼。");

            int wolfIndex = findAliveWerewolf(players);
            if (wolfIndex != -1) {
                printAlivePlayers(players);
                int target = readTarget(scanner, players, players[wolfIndex].getId(), "請選擇一位要殺掉的玩家編號：");
                players[target - 1].kill();
                System.out.println("夜晚結果：玩家 " + target + " 死亡。");
            } else {
                System.out.println("場上已經沒有存活的狼人。");
            }

            if (checkVillagerWin(players)) {
                System.out.println("村民獲勝。");
                break;
            }
            if (checkWerewolfWin(players)) {
                System.out.println("狼人獲勝。");
                break;
            }

            System.out.println();
            System.out.println("天亮了，開始白天投票。");
            printAlivePlayers(players);
            int voteId = readTarget(scanner, players, -1, "請選擇一位要投票出局的玩家編號：");
            players[voteId - 1].kill();
            System.out.println("投票結果：玩家 " + voteId + " 被淘汰。");

            if (checkVillagerWin(players)) {
                System.out.println("村民獲勝。");
                break;
            }
            if (checkWerewolfWin(players)) {
                System.out.println("狼人獲勝。");
                break;
            }

            round++;
            System.out.println();
        }

        System.out.println("遊戲結束，最終狀態：");
        printAllPlayers(players);
    }

    private static int findAliveWerewolf(Player[] players) {
        for (int i = 0; i < players.length; i++) {
            if (players[i].isAlive() && WEREWOLF.equals(players[i].getRole())) {
                return i;
            }
        }
        return -1;
    }

    private static void printAlivePlayers(Player[] players) {
        System.out.println("目前存活玩家：");
        for (Player player : players) {
            if (player.isAlive()) {
                System.out.println("玩家 " + player.getId());
            }
        }
    }

    private static void printAllPlayers(Player[] players) {
        for (Player player : players) {
            System.out.println(player.getPublicInfo() + " 角色=" + player.getRole());
        }
    }

    private static int readTarget(Scanner scanner, Player[] players, int excludedId, String prompt) {
        while (true) {
            System.out.println(prompt);
            if (scanner.hasNextInt()) {
                int targetId = scanner.nextInt();
                scanner.nextLine();
                if (isValidTarget(targetId, players, excludedId)) {
                    return targetId;
                }
            } else {
                scanner.nextLine();
            }
            System.out.println("輸入無效，請重新輸入。");
        }
    }

    private static boolean isValidTarget(int targetId, Player[] players, int excludedId) {
        if (targetId < 1 || targetId > players.length) {
            return false;
        }
        if (targetId == excludedId) {
            return false;
        }
        return players[targetId - 1].isAlive();
    }

    private static boolean checkVillagerWin(Player[] players) {
        return findAliveWerewolf(players) == -1;
    }

    private static boolean checkWerewolfWin(Player[] players) {
        int villagersAlive = 0;
        int werewolvesAlive = 0;

        for (Player player : players) {
            if (!player.isAlive()) {
                continue;
            }
            if (WEREWOLF.equals(player.getRole())) {
                werewolvesAlive++;
            } else {
                villagersAlive++;
            }
        }

        return werewolvesAlive > 0 && werewolvesAlive >= villagersAlive;
    }

    private static void clearScreenWithBlankLines() {
        for (int i = 0; i < 25; i++) {
            System.out.println();
        }
    }
}
