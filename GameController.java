import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class GameController {
    private final GameLogic game = new GameLogic();

    @FXML
    private Label playerHealthLabel;

    @FXML
    private Label computerHealthLabel;

    @FXML
    private Button shootSelfButton;

    @FXML
    private Button shootOpponentButton;

    @FXML
    public void initialize() {
        // Load bullets at the start
        game.loadBullets(6);

        // Update health labels
        updateHealthLabels();

        // Set button actions
        shootSelfButton.setOnAction(e -> {
            if (game.shoot(0)) {
                System.out.println("Player shot themselves!");
            }
            updateHealthLabels();
            checkGameOver();
        });

        shootOpponentButton.setOnAction(e -> {
            if (game.shoot(1)) {
                System.out.println("Player shot the opponent!");
            }
            updateHealthLabels();
            checkGameOver();
        });
    }

    private void updateHealthLabels() {
        playerHealthLabel.setText("Player Health: " + game.getPlayerHealth());
        computerHealthLabel.setText("Opponent Health: " + game.getComputerHealth());
    }

    private void checkGameOver() {
        if (game.getPlayerHealth() <= 0) {
            System.out.println("Game Over! Opponent Wins!");
            disableButtons();
        } else if (game.getComputerHealth() <= 0) {
            System.out.println("Game Over! Player Wins!");
            disableButtons();
        }
    }

    private void disableButtons() {
        shootSelfButton.setDisable(true);
        shootOpponentButton.setDisable(true);
    }
}