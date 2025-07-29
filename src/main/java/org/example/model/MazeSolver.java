package org.example.model;

public interface MazeSolver {
    MazeResult getPath(boolean[][] grid,Cell star, Cell end);
}
