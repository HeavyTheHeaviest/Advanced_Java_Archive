package com.example.core;

public interface CellularAutomaton {
    /**
     * Oblicza kolejną generację.
     *
     * @param current obecna siatka (true = żywa komórka)
     * @return nowa siatka
     */
    boolean[][] nextGeneration(boolean[][] current);
}