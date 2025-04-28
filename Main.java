package com.buckshotroulette;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Random;

public class Main extends Application {
    private BuckshotRouletteGame game; // The game instance
    private Label statusLabel; // Label to display the game status
    private Label playerHealthLabel; // Player health label
    private Label opponentHealthLabel; // Opponent health label
    private int playerHealth = 3; // Player starts with 3 lives
    private int opponentHealth = 3; // Opponent starts with 3 lives
    private Random random = new Random(); // Random generator for AI decisions

    @Override
    public void start(Stage primaryStage) {
        game = new BuckshotRouletteGame(); // Initialize the game

        // Create UI components
        statusLabel = new Label("Welcome to Buckshot Roulette! It's your turn. Choose an action.");
        statusLabel.setId("status-label"); // Style with CSS

        playerHealthLabel = new Label("Player Health: " + playerHealth);
        playerHealthLabel.setId("health-label"); // Style with CSS

        opponentHealthLabel = new Label("Opponent Health: " + opponentHealth);
        opponentHealthLabel.setId("health-label"); // Style with CSS

        Button spinButton = new Button("Spin the Bottle");
        spinButton.setId("action-button"); // Style with CSS

        Button shootButton = new Button("Shoot Yourself");
        shootButton.setId("action-button"); // Style with CSS

        Button restartButton = new Button("Restart Game");
        restartButton.setId("restart-button"); // Style with CSS

        // Button Handlers
        spinButton.setOnAction(e -> handleSpinBottle());
        shootButton.setOnAction(e -> handleShootYourself());
        restartButton.setOnAction(e -> resetGame());

        // Layout
        HBox healthBox = new HBox(20, playerHealthLabel, opponentHealthLabel); // Health labels side by side
        healthBox.setId("health-box"); // Style with CSS

        VBox layout = new VBox(20, healthBox, statusLabel, spinButton, shootButton, restartButton);
        layout.setId("main-layout"); // Style with CSS

        // Scene and Stage
        Scene scene = new Scene(layout, 600, 400);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm()); // Reference CSS file

        primaryStage.setScene(scene);
        primaryStage.setTitle("Buckshot Roulette - Enhanced GUI");
        primaryStage.show();
    }

    // Handle "Spin the Bottle" Button
    private void handleSpinBottle() {
        if (game.isGameOver()) {
            statusLabel.setText("Game over! Please reset to play again.");
            return;
        }

        boolean isPlayerTurn = game.spinBottle();
        if (isPlayerTurn) {
            statusLabel.setText("The bottle points to YOU!");
            delay(() -> {
                boolean isBulletFired = game.pullTrigger();
                if (isBulletFired) {
                    playerHealth--;
                    playerHealthLabel.setText("Player Health: " + playerHealth);
                    if (playerHealth == 0) {
                        statusLabel.setText("You pulled the trigger and got shot! Game over. You lose!");
                    } else {
                        statusLabel.setText("You pulled the trigger and got shot! The gun resets!");
                        game.resetGame();
                        delay(this::handleAITurn, 1000);
                    }
                } else {
                    statusLabel.setText("You pulled the trigger and survived! It's now your opponent's turn.");
                    delay(this::handleAITurn, 1000);
                }
            }, 1000);
        } else {
            statusLabel.setText("The bottle points to your OPPONENT!");
            delay(() -> {
                boolean isBulletFired = game.pullTrigger();
                if (isBulletFired) {
                    opponentHealth--;
                    opponentHealthLabel.setText("Opponent Health: " + opponentHealth);
                    if (opponentHealth == 0) {
                        statusLabel.setText("The opponent pulled the trigger and got shot! Game over. You win!");
                    } else {
                        statusLabel.setText("The opponent pulled the trigger and got shot! The gun resets!");
                        game.resetGame();
                        delay(this::handleAITurn, 1000);
                    }
                } else {
                    statusLabel.setText("The opponent pulled the trigger and survived! Switching turns...");
                    delay(this::handleAITurn, 1000);
                }
            }, 1000);
        }
    }

    // Handle "Shoot Yourself" Button
    private void handleShootYourself() {
        if (game.isGameOver()) {
            statusLabel.setText("Game over! Please reset to play again.");
            return;
        }

        statusLabel.setText("You pull the trigger...");
        delay(() -> {
            boolean isBulletFired = game.pullTrigger();
            if (isBulletFired) {
                playerHealth--;
                playerHealthLabel.setText("Player Health: " + playerHealth);
                if (playerHealth == 0) {
                    statusLabel.setText("The chamber was live and you shot yourself! Game over. You lose!");
                } else {
                    statusLabel.setText("The chamber was live and you shot yourself! Your health decreases by 1. The gun resets.");
                    game.resetGame();
                    delay(this::handleAITurn, 1000);
                }
            } else {
                statusLabel.setText("It was a blank. Now it's your opponent's turn.");
                delay(this::handleAITurn, 1000);
            }
        }, 1000);
    }

    // AI Opponent's Turn Logic
    private void handleAITurn() {
        statusLabel.setText("It's your opponent's turn...");
        delay(() -> {
            boolean aiChoice = random.nextBoolean();
            if (aiChoice) {
                statusLabel.setText("The opponent spins the bottle...");
                delay(() -> {
                    boolean aiSpinResult = game.spinBottle();
                    if (aiSpinResult) {
                        statusLabel.setText("The bottle points to YOU!");
                        delay(() -> {
                            boolean isBulletFired = game.pullTrigger();
                            if (isBulletFired) {
                                playerHealth--;
                                playerHealthLabel.setText("Player Health: " + playerHealth);
                                if (playerHealth == 0) {
                                    statusLabel.setText("The opponent forced you to pull the trigger and you got shot! Game over. You lose!");
                                } else {
                                    statusLabel.setText("The opponent forced you to pull the trigger and you got shot! The gun resets.");
                                    game.resetGame();
                                }
                            } else {
                                statusLabel.setText("You survived! Switching turns...");
                            }
                        }, 1000);
                    } else {
                        statusLabel.setText("The bottle points to the OPPONENT!");
                        delay(() -> {
                            boolean isBulletFired = game.pullTrigger();
                            if (isBulletFired) {
                                opponentHealth--;
                                opponentHealthLabel.setText("Opponent Health: " + opponentHealth);
                                if (opponentHealth == 0) {
                                    statusLabel.setText("The opponent pulled the trigger and got shot! Game over. You win!");
                                } else {
                                    statusLabel.setText("The opponent pulled the trigger and got shot! The gun resets!");
                                    game.resetGame();
                                }
                            } else {
                                statusLabel.setText("The opponent survived! It's now your turn.");
                            }
                        }, 1000);
                    }
                }, 1000);
            } else {
                statusLabel.setText("The opponent chooses to shoot itself...");
                delay(() -> {
                    boolean isBulletFired = game.pullTrigger();
                    if (isBulletFired) {
                        opponentHealth--;
                        opponentHealthLabel.setText("Opponent Health: " + opponentHealth);
                        if (opponentHealth == 0) {
                            statusLabel.setText("The opponent pulled the trigger and got shot! Game over. You win!");
                        } else {
                            statusLabel.setText("The opponent pulled the trigger and got shot! The gun resets!");
                            game.resetGame();
                        }
                    } else {
                        statusLabel.setText("The opponent survived! It's now your turn.");
                    }
                }, 1000);
            }
        }, 1000);
    }

    // Reset the game state
    private void resetGame() {
        playerHealth = 3;
        opponentHealth = 3;
        playerHealthLabel.setText("Player Health: " + playerHealth);
        opponentHealthLabel.setText("Opponent Health: " + opponentHealth);
        game.resetGame();
        statusLabel.setText("Game reset! It's your turn. Choose an action.");
    }

    // Utility method to add a delay between actions
    private void delay(Runnable action, int milliseconds) {
        new Thread(() -> {
            try {
                Thread.sleep(milliseconds);
                Platform.runLater(action);
            } catch (InterruptedException ignored) {
            }
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
