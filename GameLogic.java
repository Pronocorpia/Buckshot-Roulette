import java.util.Stack;
import java.util.Random;

public class GameLogic {
    private final Stack<Integer> chamber = new Stack<>();
    private final Random rand = new Random();

    private int playerHealth = 2;
    private int computerHealth = 2;

    public void loadBullets(int bulletCount) {
        chamber.clear();
        int liveBullets = rand.nextInt(bulletCount) + 1;
        for (int i = 0; i < bulletCount; i++) {
            if (i < liveBullets) {
                chamber.push(1); // Live bullet
            } else {
                chamber.push(0); // Blank bullet
            }
        }
    }

    public boolean shoot(int target) {
        if (chamber.isEmpty()) return false;

        int bullet = chamber.pop();
        if (bullet == 1) {
            if (target == 0) playerHealth--; // Player
            else if (target == 1) computerHealth--; // Computer
            return true; // A live bullet was fired
        }
        return false; // A blank bullet was fired
    }

    // Enemy's turn to shoot randomly
    public boolean enemyTurn() {
        if (playerHealth <= 0 || computerHealth <= 0) return false; // Game over

        int target = rand.nextInt(2); // 0 = Player, 1 = Self
        return shoot(target);
    }

    public int getPlayerHealth() {
        return playerHealth;
    }

    public int getComputerHealth() {
        return computerHealth;
    }

    public boolean isGameOver() {
        return playerHealth <= 0 || computerHealth <= 0;
    }
}
