import java.util.Scanner;

public class MineSweeperMain {

	public static void main(String[] args) {

		MineSweeperBoard board = new MineSweeperBoard();

		board.makeGrid();
		board.makeMineField();

		Scanner scan = new Scanner(System.in);

		while (true) { // Oändlig loop för att hålla programmet igång
			String input = scan.nextLine();
			MineSweeperBoard.Command command = board.parseCommand(input);

			if (command == MineSweeperBoard.Command.unknown) { // Om kommando är unknown
				System.out.println(
						"Unknown command - The input must be a command (f,r or q) followed by a space, then a letter and a number. Ex: 'f a7'");
				continue;
			}

			String[] arguments = board.parseArguments(input);

			if (command == MineSweeperBoard.Command.f) { // Om kommando är f

				String s1 = arguments[0].toLowerCase();

				if (arguments[0].length() != 2) {
					System.out.println(
							"syntax error - The input must be a command (f,r or q) followed by a space, then a letter and a number. Ex: 'f a7'");
					continue;
				}

				int row1 = Character.getNumericValue(s1.charAt(1));
				int col1 = s1.charAt(0) - 97; // a

				if (row1 > 9 || row1 < 0 || col1 > 9 || col1 < 0 || s1.length() > 2 || s1.length() < 2) {
					System.out.println(
							"syntax error - Possible values are from a-j and from 0-9, the input must be a letter followed by a number. Try again!");
					continue;
				} else if (MineSweeperBoard.grid[row1][col1] == 'F') {
					board.unSetF(row1, col1);
				} else if (MineSweeperBoard.grid[row1][col1] != '-') {
					System.out.println("not allowed");
					continue;
				}

				else {
					board.setF(row1, col1);
				}
			} else if (command == MineSweeperBoard.Command.r) { // Om kommando är r

				String s1 = arguments[0].toLowerCase();

				if (arguments[0].length() != 2) {
					System.out.println(
							"syntax error - The input must be a command(f,r,q) followed by a space, then a letter and a number. Ex: 'f a7'");
					continue;
				}

				int row1 = Character.getNumericValue(s1.charAt(1));
				int col1 = s1.charAt(0) - 97;

				if (row1 > 9 || row1 < 0 || col1 > 9 || col1 < 0) {
					System.out.println(
							"syntax error - Possible values are from a-j and from 0-9, the input must be a letter followed by a number. Try again!");
					continue;
				} else if (MineSweeperBoard.grid[row1][col1] == '.' || MineSweeperBoard.grid[row1][col1] == '1'
						|| MineSweeperBoard.grid[row1][col1] == '2' || MineSweeperBoard.grid[row1][col1] == '3'
						|| MineSweeperBoard.grid[row1][col1] == '4' || MineSweeperBoard.grid[row1][col1] == '5'
						|| MineSweeperBoard.grid[row1][col1] == '6' || MineSweeperBoard.grid[row1][col1] == '7'
						|| MineSweeperBoard.grid[row1][col1] == '8') {
					System.out.println("not allowed - this position has already been swept");
					continue;
				} else if (board.checkMines(row1, col1) == true) { // om det finns en bomb på platsen, game over
					board.setAllBombs();
					board.setM(row1, col1);
					System.out.println("GAME OVER");
					System.exit(0);
				} else {
					board.setR(row1, col1);
					board.checkWin();
				}
			}

			else if (command == MineSweeperBoard.Command.q) {
				System.out.println("terminating program");
				System.exit(0);
			}

		}
	}
}
