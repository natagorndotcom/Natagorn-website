import java.util.Scanner;
public class projectwithdiff {

    // global var for difficulty
    public static int diff = -1;
    public static int[] diffsize = {5, 9, 16};

    public static void main(String[] Args) {
        try (Scanner sc = new Scanner(System.in)) {

            System.out.print("Enter difficulty | 1 for easy, 2 for medium, 3 for hard : ");
            diff = sc.nextInt() - 1;
            while (diff < 0 || diff >= 3) {
                System.out.println("Invalid difficulty! Only 1 or 2 or 3");
                System.out.print("Enter difficulty | 1 for easy, 2 for medium, 3 for hard : ");
                diff = sc.nextInt() - 1;
            }

            char[][] playgrid;
            playgrid = new char[diffsize[diff]][diffsize[diff]];
            for (int i = 0; i < diffsize[diff]; ++i) {
                for (int j = 0; j < diffsize[diff]; ++j) {
                    playgrid[i][j] = '□';
                }
            }

            int bombcount = 0;
            // set the bomb on system grid
            int[][] sysgrid;
            sysgrid = new int[diffsize[diff]][diffsize[diff]];
            for (int i = 0; i < diffsize[diff]; ++i) {
                for (int j = 0; j < diffsize[diff]; ++j) {
                    int randomnum = (int)(Math.random() * 101);
                    if (randomnum > 80) {
                        sysgrid[i][j] = -1;
                        bombcount++;
                    }
                }
            }

            // fill bombs count
            int[] traversalr = {1, 1, 1, 0, 0, -1, -1, -1};
            int[] traversalc = {-1, 0, 1, -1, 1, -1, 0, 1};
            for (int i = 0; i < diffsize[diff]; ++i) {
                for (int j = 0; j < diffsize[diff]; ++j) {
                    if (sysgrid[i][j] == -1) continue;
                    int count = 0;
                    for (int k = 0; k < 8; ++k) {
                        int newR = traversalr[k] + i;
                        int newC = traversalc[k] + j;

                        if (newR < 0 || newR >= diffsize[diff] || newC < 0 || newC >= diffsize[diff]) {
                            continue;
                        } else if (sysgrid[newR][newC] == -1) count++;
                        else continue;
                    }
                    sysgrid[i][j] = count;
                }
            }
            
            // Start game
            boolean lost = false;
            boolean invaltile = false;
            boolean openedtile = false;
            do {
                
                // debug print, does not matter
                // for (int i = 0; i < 16; ++i) {
                //     for (int j = 0; j < 16; ++j) {
                //         System.out.printf(" %d", sysgrid[i][j]);
                //     }
                //     System.out.println();
                // }


                printtable(playgrid);

                // if the previous try was invalid
                if (invaltile) {
                    System.out.println("Invalid tile, try again!");
                    invaltile = false;
                } else if (openedtile) {
                    System.out.println("You opened this tile, choose another one!");
                    openedtile = false;
                }
                
                System.out.println("Enter the tile coordinates integer to open (row column): ");
                
                // check valid integer input
                int selectR;
                int selectC;

                selectR = sc.nextInt() - 1;
                selectC = sc.nextInt() - 1;

                // check for valid tiles
                if (!checkvalidtile(selectR, selectC)) {
                    invaltile = true;
                    continue;
                }

                if (!checkvalidopenedtile(selectR, selectC, playgrid)) {
                    openedtile = true;
                    continue;
                }

                // Lost the game (Found bomb)
                if (sysgrid[selectR][selectC] == -1) {
                    lost = true;
                    continue;
                }

                // decide whether to clear the tile if the adjacent tiles are zero
                if (sysgrid[selectR][selectC] == 0) {
                    dfs(playgrid, sysgrid, selectR, selectC);
                } else {
                    removetile(playgrid, sysgrid, selectR, selectC);
                }

                // Check if won the game
                if (checkWin(playgrid, bombcount)) {
                    break;
                } else {
                    continue;
                }

            } while (!lost);

            // Won
            if (!lost) {
                printwhenlost(playgrid, sysgrid);
                printtable(playgrid);
                System.out.println("--------------------------");
                System.out.println("Congratulations, you won!");
                System.out.println("--------------------------");
            } else {
                printwhenlost(playgrid, sysgrid);
                printtable(playgrid);
                System.out.println("--------------------------");
                System.out.println("You lost :(, nice try though!");
            }



        }
    }

    // need fix
    static void printtable(char[][] grid) {
        if (diff == 2) {
            System.out.println("   1  2  3  4  5  6  7  8  9  10 11 12 13 14 15 16");
            for (int i = 0; i < 16; ++i) {
                for (int j = 0; j < 16; ++j) {
                    if (i < 9 && j == 0) System.out.printf("%d  ", i + 1); 
                    else if (j == 0) System.out.printf("%d ", i + 1); 
    
                    System.out.print(grid[i][j] + "  ");
                }
                System.out.println();
            }
        } else if (diff == 1) {
            System.out.println("  1 2 3 4 5 6 7 8 9");
            for (int i = 0; i < 9; ++i) {
                for (int j = 0; j < 9; ++j) {
                    if (j == 0) System.out.printf("%d ", i + 1);
                    System.out.print(grid[i][j] + " ");
                }
                System.out.println();
            }
        } else if (diff == 0) {
            System.out.println("  1 2 3 4 5");
            for (int i = 0; i < 5; ++i) {
                for (int j = 0; j < 5; ++j) {
                    if (j == 0) System.out.printf("%d ", i+1);
                    System.out.printf(grid[i][j] + " ");
                }
                System.out.println();
            }
        }

    }

    static void removetile(char[][] playgrid, int[][] sysgrid, int r, int c) {
        playgrid[r][c] = (char)(sysgrid[r][c] + 48);
    }

    static boolean checkWin(char[][] playgrid, int bombcount) {
        int checkunopened = 0;
        for (int i = 0; i < diffsize[diff]; ++i) {
            for (int j = 0; j < diffsize[diff]; ++j) {
                if (playgrid[i][j] == '□') checkunopened++;
            }
        }
        return checkunopened == bombcount ? true : false;
    }

    static void printwhenlost(char[][] playgrid, int[][] sysgrid) {
        for (int i = 0; i < diffsize[diff]; ++i) {
            for (int j = 0; j < diffsize[diff]; ++j) {
                if (playgrid[i][j] == '□' && sysgrid[i][j] == -1) {
                    playgrid[i][j] = 'B';
                }
            }
        }
    }

    static boolean checkvalidtile(int row, int column) {
        return (row < 0 || row >= diffsize[diff] || column < 0 || column >= diffsize[diff]) ? false : true;
    }

    static boolean checkvalidopenedtile(int row, int column, char[][] playgrid) {
        return (playgrid[row][column] != '□') ? false : true;
    }

    static void dfs(char[][] playgrid, int[][] sysgrid, int r, int c) {
        if (playgrid[r][c] == '□' && sysgrid[r][c] == 0) {
        playgrid[r][c] = '0';
            int[] traversalr = {1, 1, 1, 0, 0, -1, -1, -1};
            int[] traversalc = {-1, 0, 1, -1, 1, -1, 0, 1};
            for (int i = 0; i < 8; ++i) {
                int newR = r + traversalr[i];
                int newC = c + traversalc[i];

                if (checkvalidtile(newR, newC)) {
                    dfs(playgrid, sysgrid, newR, newC);
                }
            }
        } else if (playgrid[r][c] == '□') {
            removetile(playgrid, sysgrid, r, c);
        }
        else return;

    }
}
