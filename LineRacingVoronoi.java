import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {
    static int[][] state = new int[30][20];

    private static int[][] initializeDistances() {
        int[][] distanceMatrix = new int[30][20];
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 20; j++) {
                distanceMatrix[i][j] = -1;
            }
        }
        return distanceMatrix;
    }

    private static void findDistances(int x, int y, int[][] distanceMatrix, int depth) {
        if (x < 0 || y < 0 || x >= 30 || y >= 20 || state[x][y] != 0
            || (distanceMatrix[x][y] != -1 && distanceMatrix[x][y] <= depth)) {
            return;
        }
        distanceMatrix[x][y] = depth;
        findDistances(x - 1, y, distanceMatrix, depth + 1);
        findDistances(x + 1, y, distanceMatrix, depth + 1);
        findDistances(x, y - 1, distanceMatrix, depth + 1);
        findDistances(x, y + 1, distanceMatrix, depth + 1);
    }

    private static int getVoronoiArea(int[][] distancesPlayer, int[][] distancesOpponent) {
        int result = 0;
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 20; j++) {
                if (distancesPlayer[i][j] != -1 && (distancesOpponent[i][j] == -1 || distancesPlayer[i][j] <= distancesOpponent[i][j])) {
                    result++;
                }
            }
        }
        return result;
    }

    private static void removePlayerTrace(int playerId) {
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 20; j++) {
                if (state[i][j] == playerId) {
                    state[i][j] = 0;
                }
            }
        }
    } 

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        
        // game loop
        while (true) {
            int N = in.nextInt(); // total number of players (2 to 4).
            int P = in.nextInt(); // your player number (0 to 3).
            int playerX = 0;
            int playerY = 0;
            int[][] opponentDistanceMatrix = initializeDistances();
            for (int i = 0; i < N; i++) {
                int X0 = in.nextInt(); // starting X coordinate of lightcycle (or -1)
                int Y0 = in.nextInt(); // starting Y coordinate of lightcycle (or -1)
                int X1 = in.nextInt(); // starting X coordinate of lightcycle (can be the same as X0 if you play before this player)
                int Y1 = in.nextInt(); // starting Y coordinate of lightcycle (can be the same as Y0 if you play before this player)

                if (X0 == -1) {
                    removePlayerTrace(i + 1);
                }
                else {
                    if (i != P) {
                        findDistances(X1, Y1, opponentDistanceMatrix, 0); // mark distances for opponents
                    }

                    // update player location
                    if (i == P) {
                        playerX = X1;
                        playerY = Y1;
                    }

                    state[X1][Y1] = i + 1;
                }
            }

            int[][] distancesLeft = initializeDistances();
            findDistances(playerX - 1, playerY, distancesLeft, 1);
            int voronoiLeftArea = getVoronoiArea(distancesLeft, opponentDistanceMatrix);

            int[][] distancesRight = initializeDistances();
            findDistances(playerX + 1, playerY, distancesRight, 1);
            int voronoiRightArea = getVoronoiArea(distancesRight, opponentDistanceMatrix);
            
            int[][] distancesUp = initializeDistances();
            findDistances(playerX, playerY - 1, distancesUp, 1);
            int voronoiUpArea = getVoronoiArea(distancesUp, opponentDistanceMatrix);
            
            int[][] distancesDown = initializeDistances();
            findDistances(playerX, playerY + 1, distancesDown, 1);
            int voronoiDownArea = getVoronoiArea(distancesDown, opponentDistanceMatrix);

            int maxVoronoiArea = Math.max(voronoiLeftArea, Math.max(voronoiRightArea, Math.max(voronoiUpArea, voronoiDownArea)));
            System.err.format("Left: %d, Right: %d, Up: %d, Down: %d, Max: %d\n",
                voronoiLeftArea, voronoiRightArea, voronoiUpArea, voronoiDownArea, maxVoronoiArea);
            if (voronoiLeftArea == maxVoronoiArea) {
                System.out.println("LEFT");
            }
            else if (voronoiRightArea == maxVoronoiArea) {
                System.out.println("RIGHT");
            }
            else if (voronoiUpArea == maxVoronoiArea) {
                System.out.println("UP");
            }
            else /* if (voronoiDownArea == maxVoronoiArea) */ {
                System.out.println("DOWN");
            }
        }
    }
}