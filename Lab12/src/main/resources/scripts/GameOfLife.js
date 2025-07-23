/**
 * Implementacja automatu Game of Life.
 * @param {boolean[][]} grid
 * @returns {boolean[][]}
 */
function GameOfLife(grid) {
    const rows = grid.length;
    const cols = grid[0].length;
    const newGrid = [];
    for (let i = 0; i < rows; i++) {
        newGrid[i] = [];
        for (let j = 0; j < cols; j++) {
            let liveNeighbors = 0;
            for (let di = -1; di <= 1; di++) {
                for (let dj = -1; dj <= 1; dj++) {
                    if (di === 0 && dj === 0) continue;
                    const ni = (i + di + rows) % rows;
                    const nj = (j + dj + cols) % cols;
                    if (grid[ni][nj]) liveNeighbors++;
                }
            }
            newGrid[i][j] = (
                (grid[i][j] && (liveNeighbors === 2 || liveNeighbors === 3)) ||
                (!grid[i][j] && liveNeighbors === 3)
            );
        }
    }
    return newGrid;
}