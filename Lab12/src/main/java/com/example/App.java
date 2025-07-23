package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import com.example.core.ScriptManager;
import com.example.core.CellularAutomaton;

public class App extends Application {
    private static final int SIZE = 600;
    private static final int CELLS = 60;
    private boolean[][] grid = new boolean[CELLS][CELLS];
    private ScriptManager manager = new ScriptManager();

    @Override
    public void start(Stage stage) throws Exception {
        // Inicjalne ładowanie skryptu
        manager.load("GameOfLife");

        // Losowe wypełnienie siatki
        for (int i = 0; i < CELLS; i++)
            for (int j = 0; j < CELLS; j++)
                grid[i][j] = Math.random() < 0.3;

        Canvas canvas = new Canvas(SIZE, SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Menu do ładowania i wyładowania skryptu
        Menu fileMenu = new Menu("File");
        MenuItem loadItem = new MenuItem("Load GameOfLife");
        loadItem.setOnAction(e -> {
            try { manager.load("GameOfLife"); }
            catch (Exception ex) { ex.printStackTrace(); }
        });
        MenuItem unloadItem = new MenuItem("Unload");
        unloadItem.setOnAction(e -> manager.unload());
        fileMenu.getItems().addAll(loadItem, unloadItem);
        MenuBar menuBar = new MenuBar(fileMenu);

        BorderPane root = new BorderPane(canvas);
        root.setTop(menuBar);
        stage.setScene(new Scene(root));
        stage.setTitle("Cellular Automata");
        stage.show();

        // Pętla animacji
        new javafx.animation.AnimationTimer() {
            private long last = 0;
            @Override public void handle(long now) {
                if (now - last < 200_000_000) return;
                last = now;
                draw(gc);
                try {
                    CellularAutomaton automaton = manager.getAutomaton();
                    grid = automaton.nextGeneration(grid);
                } catch (Exception ignored) {}
            }
        }.start();
    }

    private void draw(GraphicsContext gc) {
        double w = SIZE / (double) CELLS;
        gc.clearRect(0, 0, SIZE, SIZE);
        for (int i = 0; i < CELLS; i++) {
            for (int j = 0; j < CELLS; j++) {
                if (grid[i][j]) gc.fillRect(j * w, i * w, w, w);
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}