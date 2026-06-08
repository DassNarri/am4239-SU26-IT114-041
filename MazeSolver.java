public class MazeSolver {
    // Maze layout: 'S' = Start, 'E' = Exit, '#' = Wall, ' ' = Path
    static char[][] maze = {
        {'S', ' ', ' ', '#', ' '},
        {'#', '#', ' ', '#', ' '},
        {' ', ' ', ' ', ' ', ' '},
        {' ', '#', '#', ' ', '#'},
        {' ', ' ', ' ', ' ', 'E'}
    };
    static boolean[][] visited = new boolean[maze.length][maze[0].length];  // Tracks visited cells

    public static void main(String[] args) {
        if (solveMaze(0, 0)) {
            System.out.println("Path found!");
        } else {
            System.out.println("No path found.");
        }
        printMaze();  // Print the maze with the path traveled
    }

    // Recursive method to solve the maze
    public static boolean solveMaze(int x, int y) {
        // Check boundaries and obstacles
        if (x < 0 || y < 0 || x >= maze.length || y >= maze[0].length || maze[x][y] == '#' || visited[x][y]) {
            return false;
        }
        // Check if the exit is found
        if (maze[x][y] == 'E') return true;

        visited[x][y] = true;  // Mark the current cell as visited
        maze[x][y] = '*';      // Mark the path traveled

        // Recursively explore neighbors: down, up, right, left
        if (solveMaze(x + 1, y) || solveMaze(x - 1, y) || solveMaze(x, y + 1) || solveMaze(x, y - 1)) {
            return true;
        }

        // Backtrack if no path is found in this direction
        maze[x][y] = ' ';  // Reset the path marking if this route is not valid
        return false;
    }

    // Method to print the maze showing the path traveled
    public static void printMaze() {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                System.out.print(maze[i][j] + " ");
            }
            System.out.println();
        }
    }
}