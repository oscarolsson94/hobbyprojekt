import java.util.*;

public class MineSweeperBoard {

	enum Command {
		f, // flagga
		r, // röj
		q, // quit
		unknown, // unknown input
	}

	static final int REGEX_CONVERT = 10;
	static final int GRID_DIMENSION = 10;
	static char[][] grid;
	static char[][] minefield;

	public void makeGrid() {

		
		grid = new char[GRID_DIMENSION][GRID_DIMENSION];

		
		for (int i = 0; i < GRID_DIMENSION; i++) {
			for (int j = 0; j < GRID_DIMENSION; j++)
				grid[i][j] = '-';
		}
		
		System.out.println("    A B C D E F G H I J");
		System.out.println("  +--------------------");

		for (int i = 0; i < GRID_DIMENSION; i++) {
			System.out.print(i + " " + "|");
			for (int j = 0; j < GRID_DIMENSION; j++)
				System.out.print(" " + grid[i][j]);
			System.out.println();
		}

		System.out.println();
		System.out.println(">");

	}

	public static void printGrid() {

		System.out.println("    A B C D E F G H I J");
		System.out.println("  +--------------------");

		for (int i = 0; i < GRID_DIMENSION; i++) {
			System.out.print(i + " " + "|");
			for (int j = 0; j < GRID_DIMENSION; j++)
				System.out.print(" " + grid[i][j]);
			System.out.println();
		}

		System.out.println();
		System.out.println(">");

	}

	public Command parseCommand(String input) {

		String commandString = input.split(" ")[0];
		commandString = commandString.toLowerCase();

		switch (commandString) {

		case "f":
			return Command.f;
		case "r":
			return Command.r;
		case "q":
			return Command.q;
		default:
			return Command.unknown;

		}

	}

	public String[] parseArguments(String input) {
		String[] commandAndArguments = input.split(" ");
		String[] arguments = new String[commandAndArguments.length - 1];

		for (int i = 1; i < commandAndArguments.length; i++) {
			arguments[i - 1] = commandAndArguments[i];
		}
		return arguments;
	}
	
	public void setF(int posX, int posY) {

		grid[posX][posY] = 'F';
		printGrid();
	}
	
	public void unSetF(int posX, int posY) {

		grid[posX][posY] = '-';
		printGrid();
	}
	
	public void setR(int posX, int posY) {

		revealMore(posX, posY);
		zeroToDot();
		printGrid();
	}
	
	public void setM(int posX, int posY) {

		grid[posX][posY] = 'M';
		printGrid();
	}

	public void makeMineField() {

		int minesPlaced = 0;
		minefield = new char[GRID_DIMENSION][GRID_DIMENSION];

		Random random = new Random();
		while (minesPlaced < 15) {
			int x = random.nextInt(10);
			int y = random.nextInt(10);
			// För att inte lägga en mina på en annan
			if (minefield[x][y] != '*') {
				minefield[x][y] = '*';
				minesPlaced++;

			}
		}
	}
	
	public boolean checkMines(int posX, int posY) {

		boolean isBomb = false;

		if (minefield[posX][posY] == '*') {
			isBomb = true;
		}
		return isBomb;
	}

	public void setAllBombs() {
		for (int i = 0; i < GRID_DIMENSION; i++) {
			for (int j = 0; j < GRID_DIMENSION; j++) {
				if (minefield[i][j] == '*') {
					grid[i][j] = 'm';
				}
			}
		}
	}
	
	public static void revealMore(int posX, int posY) {
		int minx, miny, maxx, maxy;

		// Gränser för att inte gå utanför grid
		minx = (posX <= 0 ? 0 : posX - 1);
		miny = (posY <= 0 ? 0 : posY - 1);
		maxx = (posX >= 10 - 1 ? 10 : posX + 2);
		maxy = (posY >= 10 - 1 ? 10 : posY + 2);

		int temp = closeMines(posX, posY);

		if (temp == 0) {
			// Loopa över alla närliggande grannar
			for (int i = minx; i < maxx; i++) {
				for (int j = miny; j < maxy; j++) {
					if (minefield[i][j] != '*' && (grid[i][j] == '-' || grid[i][j] == '1' || grid[i][j] == '2'
							|| grid[i][j] == '3' || grid[i][j] == '4' || grid[i][j] == '5' || grid[i][j] == '6'
							|| grid[i][j] == '7' || grid[i][j] == '8' || grid[i][j] == 'F')) {

						reveal(i, j);

						if (grid[i][j] == '0') {
							// Rekursivt
							revealMore(i, j);
						}
					}
				}
			}
		} else {

			reveal(posX, posY);

		}
	}

	public static int reveal(int posX, int posY) {

		grid[posX][posY] = Character.forDigit(closeMines(posX, posY), REGEX_CONVERT);

		return grid[posX][posY];
	}

	public static int closeMines(int posX, int posY) {

		int minx, miny, maxx, maxy;
		int result = 0;

// Gränser för att inte gå utanför grid
		minx = (posX <= 0 ? 0 : posX - 1);
		miny = (posY <= 0 ? 0 : posY - 1);
		maxx = (posX >= 10 - 1 ? 10 : posX + 2);
		maxy = (posY >= 10 - 1 ? 10 : posY + 2);

		// kolla alla närliggande grannar för minor
		for (int i = minx; i < maxx; i++) {
			for (int j = miny; j < maxy; j++) {
				if (minefield[i][j] == '*') {
					result++;
				}
			}
		}
		return result;
	}

	public void zeroToDot() {
		for (int i = 0; i < GRID_DIMENSION; i++) {
			for (int j = 0; j < GRID_DIMENSION; j++) {
				if (grid[i][j] == '0') {
					grid[i][j] = '.';
				}
			}

		}
	}

	public void checkWin() {
		int count = 0;

		for (int i = 0; i < GRID_DIMENSION; i++) {
			for (int j = 0; j < GRID_DIMENSION; j++) {
				if (grid[i][j] != '-' && grid[i][j] != 'F') {
					count++;
				}
			}

		}

		if (count == 85) {
			System.out.println("YOU WON");
		}
	}

}
