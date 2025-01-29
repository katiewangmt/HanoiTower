/**
 *	Tower of Hanoi Puzzle. You start with a three peg board and a number of
 *	disks of increasingly larger diameters, each with a hole in the center
 *	to fit around a peg.
 *
 *	At the start of the game, the disks are stacked on the left peg with
 *	decreasing diameters. You can start with any number of disks. More
 *	disks means a more challenging puzzle.
 *
 *	Below is the start of a Tower of Hanoi puzzle with 4 disks (or levels).
 *
 * Level
 *	4     -|-         |          |     
 *	3    --|--        |          |     
 *	2   ---|---       |          |     
 *	1  ----|----      |          |     
 *	  -----+----------+----------+-----
 * Peg     0          1          2     
 *
 *
 *	The objective is to move the stack of disks from the left peg to the
 *	center peg in the same order as they began. The solution looks like this:
 *
 * Level
 *	4      |         -|-         |     
 *	3      |        --|--        |     
 *	2      |       ---|---       |     
 *	1      |      ----|----      |     
 *	  -----+----------+----------+-----
 * Peg     0          1          2     
 *
 *
 *	The rules are simple. The disks are moved peg to peg, only one at a time,
 *	and a larger disk cannot be placed on top of a smaller disk.
 *
 *	There are three methods to play the game:
 *		1. playGame() - Lets the user play the game.
 *		2. solve4Disks() - A step-by-step algorithm that solves a 4-disk puzzle.
 *		3. solvePuzzle() - Automated algorithm to solve the puzzle with any
 *							number of disks. (implemented by assistant)
 *
 *	Requires Prompt class
 *
 *	@author	Mr Greenstein
 *	@since  January 22nd, 2025
 */
public class HanoiTower {
	
	private int [][] tower;	// rows = pegs; cols = disk number 1 to levels
	private int levels;		// number of disks or levels to move
	private int numMoves;	// total number of moves
	
	private final boolean PRINT_TOWER = true;	// print tower after each move
		
	// Constructor
	public HanoiTower() {
		levels = 0;
		numMoves = 0;
	}
	
	public static void main(String[] args) {
		HanoiTower h = new HanoiTower();
		h.run(args);
	}
	
	public void run(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: java HanoiTower <numLevels> [play|solve]");
			System.exit(0);
		}

		// Check the second argument to determine the mode
		if (args.length >= 2 && args[1].equalsIgnoreCase("solve")) {
			solvePuzzle(args);
		} else {
			playGame(args);
		}

		// Print out number of moves
		System.out.println("\nIt took you " + numMoves + " moves with " + levels + " levels.\n");
	}
	
	/**
	 *	User plays the game
	 *	@param args		the arguments passed from the command line
	 */
	public void playGame(String[] args) {
		// Input the number of levels in the game
		getLevels(args);
		
		initializeTower();
		printIntroduction();
		
		// Play the game
		while (! isDone()) {
			printTowers();
			int from = Prompt.getInt("\nMove disk FROM peg", 0, 2);
			int to = Prompt.getInt("          TO   peg", 0, 2);
			if (from == to)
				System.out.println("\nERROR: from and to pegs cannot be the same. Try again.\n");
			else
				if (! moveDisk(from, to))
					System.out.println("\nERROR: Invalid move. Try again.\n");
		}
		
		// Game over
		printTowers();
		System.out.println("\nSUCCESS!!! You won!\n");
	}
	
	/**
	 *	Algorithm to solve 4 disk problem step-by-step
	 */
	public void solve4Disks() {
		levels = 4;
		initializeTower();
		System.out.println("\nSolving the game step-by-step using 4 levels.\n");
		printTowers();
		// solving the problem in steps
		moveDisk(0, 2);		printTowers();
		moveDisk(0, 1);		printTowers();
		moveDisk(2, 1);		printTowers();
		moveDisk(0, 2);		printTowers();
		moveDisk(1, 0);		printTowers();
		moveDisk(1, 2);		printTowers();
		moveDisk(0, 2);		printTowers();
		moveDisk(0, 1);		printTowers();
		moveDisk(2, 1);		printTowers();
		moveDisk(2, 0);		printTowers();
		moveDisk(1, 0);		printTowers();
		moveDisk(2, 1);		printTowers();
		moveDisk(0, 2);		printTowers();
		moveDisk(0, 1);		printTowers();
		moveDisk(2, 1);		printTowers();
	}
	
	/**
	 *	Get the number of levels to use
	 *	@param args		the arguments passed from the command line
	 */
	public void getLevels(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: java HanoiTower <numLevels>");
			System.exit(0);
		}
		try {
			levels = Integer.parseInt(args[0]);
			if (levels < 1) {
				System.err.println("ERROR: Number of levels must be at least 1.");
				System.exit(-1);
			}
		}
		catch (NumberFormatException e) {
			System.err.println("ERROR: Number of levels must be an integer");
			System.out.println("Usage: java HanoiTower <numLevels>");
			System.exit(-1);
		}
	}
	
	/**
	 *	Introduction to game
	 */
	public void printIntroduction() {
		System.out.println("\n");
		System.out.println("  _____                               __   _   _                   _ ");
		System.out.println(" |_   _|____      _____ _ __    ___  / _| | | | | __ _ _ __   ___ (_)");
		System.out.println("   | |/ _ \\ \\ /\\ / / _ \\ '__|  / _ \\| |_  | |_| |/ _` | '_ \\ / _ \\| |");
		System.out.println("   | | (_) \\ V  V /  __/ |    | (_) |  _| |  _  | (_| | | | | (_) | |");
		System.out.println("   |_|\\___/ \\_/\\_/ \\___|_|     \\___/|_|   |_| |_|\\__,_|_| |_|\\___/|_|");
		System.out.println("\n");
		System.out.println("Welcome to the Tower Of Hanoi Game. You are given a wooden board with three tall");
		System.out.println("pegs in a row. Wooden disks are stacked in decreasing diameter on the left peg.");
		System.out.println("The objective is to move the stack of disks from the left peg to the center peg");
		System.out.println("in the same order as they began. Disks are moved from peg to peg, one at a time,");
		System.out.println("and a larger disk cannot be placed on a smaller disk.");
		System.out.println("\nLet's begin!\n");
	}
	
	/**
	 *	Determine if the game is done and all of the disks are on the center peg.
	 *	@return		true if the game is done; false otherwise
	 */
	public boolean isDone() {
		for (int disk = 0; disk < levels; disk++) {
			if (tower[1][disk] != levels - disk) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 *	Automated solver method
	 *	@param args		the arguments passed from the command line
	 */
	public void solvePuzzle(String[] args) {
		// Input the number of levels in the game
		getLevels(args);
		
		initializeTower();
		
		System.out.println("\nSolving the Tower of Hanoi puzzle automatically...\n");
		printTowers();
		
		// Start the recursive solving process
		hanoi(levels, 0, 1, 2);
	}
	
	/*
	 *	Recursive method to solve Tower of Hanoi.
	 *
	 *	@param n        Number of disks to move
	 *	@param fromPeg  The source peg index
	 *	@param toPeg    The destination peg index
	 *	@param auxPeg   The auxiliary peg index
	 */
	private void hanoi(int n, int fromPeg, int toPeg, int auxPeg) {
		if (n == 1) {
			if (moveDisk(fromPeg, toPeg)) {
				printTowers();
			}
			return;
		}
		hanoi(n - 1, fromPeg, auxPeg, toPeg);
		if (moveDisk(fromPeg, toPeg)) {
			printTowers();
		}
		hanoi(n - 1, auxPeg, toPeg, fromPeg);
	}
	
	/**
	 *	Move the top disk from peg to peg. Move is successful if a smaller
	 *	disk is moved on top of a larger disk. It is unsuccessful if a
	 *	larger disk tries to move on top a smaller disk.
	 *
	 *	@param from		the index of the from peg (0, 1, or 2)
	 *	@param to		the index of the to peg (0, 1, or 2)
	 *	@return			true if move successful, false otherwise
	 */
	public boolean moveDisk(int from, int to) {
		// Find the top disk on the "from" peg
		int fromDisk = -1;
		for (int i = 0; i < levels; i++) {
			if (tower[from][i] != 0) {
				fromDisk = tower[from][i];
				tower[from][i] = 0;
				break;
			}
		}

		if (fromDisk == -1) {
			// No disk to move
			return false;
		}

		// Find the top disk on the "to" peg
		int toDiskPosition = -1;
		for (int i = 0; i < levels; i++) {
			if (tower[to][i] == 0) {
				toDiskPosition = i;
				break;
			}
		}

		// Check if move is valid
		if (toDiskPosition == -1) {
			// No space to move
			// Restore the moved disk
			for (int i = levels - 1; i >= 0; i--) {
				if (tower[from][i] == 0) {
					tower[from][i] = fromDisk;
					break;
				}
			}
			return false;
		}

		// Check if there's a disk to compare sizes
		if (tower[to][toDiskPosition] != 0 && fromDisk > tower[to][toDiskPosition]) {
			// Cannot place larger disk on smaller disk
			// Restore the moved disk
			for (int i = levels - 1; i >= 0; i--) {
				if (tower[from][i] == 0) {
					tower[from][i] = fromDisk;
					break;
				}
			}
			return false;
		}

		// Move the disk
		tower[to][toDiskPosition] = fromDisk;
		numMoves++;
		return true;
	}
	
	/**
	 *	Initialize the tower array.
	 *	tower 0 row = lowest level on tower or peg
	 *	tower column = size of disk (1 = smallest, levels = largest)
	 */
	public void initializeTower() {
		// 3 pegs and number of disks or levels of disks
		tower = new int [3][levels];
		// on first peg (index 0) stack biggest disk number (levels) at 0 column index
		for (int disk = 0; disk < levels; disk++)
			tower[0][disk] = levels - disk;
		// set other peg columns empty (= 0)
		for (int peg = 1; peg < 3; peg++)
			for (int disk = 0; disk < levels; disk++)
				tower[peg][disk] = 0;
	}
	
	/**
	 *	Print the towers of Hanoi
	 */
	public void printTowers() {
		System.out.println("\nLevel");
		// for each level of peg starting with the bottom (index 0)
		for (int level = levels - 1; level >= 0; level--) {
			System.out.printf("%2d ", (level + 1));
			printLine(0, level);	// peg 0
			printLine(1, level);	// peg 1
			printLine(2, level);	// peg 2
			System.out.println();
		}
		// print the base of the towers
		printBase();
		System.out.println("\n");
	}
	
	/**
	 *	Print the base of the towers
	 */
	public void printBase() {
		printChar(3, " ");
		for (int a = 0; a < 3; a++) {
			// print left side of peg
			printChar(levels + 1, "-");
			// print peg base
			System.out.print("+");
			// print right side of peg
			printChar(levels + 1, "-");
		}
		System.out.println();
		// print labels on base
		System.out.print("Peg");
		for (int a = 0; a < 3; a++) {
			// print left side of peg
			printChar(levels + 1, " ");
			// print peg base
			System.out.print("" + a);
			// print right side of peg
			printChar(levels + 1, " ");
		}
	}
	
	/**
	 *	Print one peg and the disk width (level)
	 *	@param peg		the peg 0, 1, or 2
	 *	@param level	the disk width
	 */
	public void printLine(int peg, int level) {
		/*
			Print peg one (index 0) at level
		*/
		// print empty spaces for left side of disk
		printChar(levels - tower[peg][level] + 1, " ");
		// print left side of disk
		printDisk(tower[peg][level]);
		// print peg
		System.out.print("|");
		// print right side of disk
		printDisk(tower[peg][level]);
		// print empty spaces for right side of disk
		printChar(levels - tower[peg][level] + 1, " ");
	}
	
	/**
	 *	Print a character n times
	 *	@param n	the number of times to print str
	 *	@param str	the String to print
	 */
	public void printChar(int n, String str) {
		for (int a = 0; a < n; a++) System.out.print(str);
	}
	
	/**
	 *	Print one side of a disk
	 *	@param n	width of disk
	 */
	public void printDisk(int n) {
		for (int a = 0; a < n; a++) System.out.print("-");
	}
}