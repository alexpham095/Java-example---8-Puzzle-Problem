
import java.util.*;

public class eightPuzzle {
	
	static int numNode = 0;
	int step = 0;
	static int[] h = new int[100];
	int hcount = 0;
	int pSize = 9;
	HashMap<Integer, int[]> mDis;
	List<int[]> board;

	boolean h1 = false;
	boolean h2;
	boolean test = false;
	
	private class Node {
		private int fn = 0;
		private int gn = 0;
		private int[] state = new int[pSize];
		private int init = -1;
		private Node parent = null;
		
		public Node(int fn, int gn, int[] state, int zeroPos, Node parent) {
			this.fn = fn;
			this.init = zeroPos;
			this.gn = gn;
			this.parent = parent;
			this.state = state;
			++numNode;
		}
		
		//create getters
		public int getFn() {
			return fn;
		}

		public int getGn() {
			return gn;
		}

		public int[] getState() {
			return state;
		}

		public int getZeroPos() {
			return init;
		}

		public Node getParent() {
			return parent;
		}
	}

	public eightPuzzle() {	
		mDis = new HashMap<>();
		board = new ArrayList<>();

		board.add(new int[] { 1, 3 });
		board.add(new int[] { 0, 2, 4 });
		board.add(new int[] { 1, 5 });
		board.add(new int[] { 0, 4, 6 });
		board.add(new int[] { 1, 3, 5, 7 });
		board.add(new int[] { 2, 4, 8 });
		board.add(new int[] { 3, 7 });
		board.add(new int[] { 4, 6, 8 });
		board.add(new int[] { 5, 7 });

		mDis.put(0, new int[] { 0, 1, 2, 1, 2, 3, 2, 3, 4 });
		mDis.put(1, new int[] { 1, 0, 1, 2, 1, 2, 3, 2, 3 });
		mDis.put(2, new int[] { 2, 1, 0, 3, 2, 1, 4, 3, 2 });
		mDis.put(3, new int[] { 1, 2, 3, 0, 1, 2, 1, 2, 3 });
		mDis.put(4, new int[] { 2, 1, 2, 1, 0, 1, 2, 1, 2 });
		mDis.put(5, new int[] { 3, 2, 1, 2, 1, 0, 3, 2, 1 });
		mDis.put(6, new int[] { 2, 3, 4, 1, 2, 3, 0, 1, 2 });
		mDis.put(7, new int[] { 3, 2, 3, 2, 1, 2, 1, 0, 1 });
		mDis.put(8, new int[] { 4, 3, 2, 3, 2, 1, 2, 1, 0 });
	}
	
	//create setters
	public void setH1(boolean h1) {
		this.h1 = h1;
	}

	public void setH2(boolean h2) {
		this.h2 = h2;
	}

	private int[] swap(int[] state, int pos1, int pos2) {
		int[] newState = state.clone();
		newState[pos1] = state[pos2];
		newState[pos2] = state[pos1];

		return newState;
	}
	
	private void frontier(PriorityQueue<Node> queue, Node current, Node parent) {
		int[] state = current.getState();
		int pos = current.getZeroPos();
		
		int gn = current.getGn() + 1;
		int[] prevState = null;
		
		if (current.getParent() != null)
			prevState = current.getParent().getState();

		for (int move : board.get(pos)) {
			int[] nextState = swap(state, move, pos);
			
			if ((prevState == null) || (!Arrays.equals(nextState, prevState))) {
				int fn = gn + getH1(nextState) + getH2(nextState);
				queue.add(new Node(fn, gn, nextState, getZeroPos(nextState), current));
			}

		}
	}

	private int getZeroPos(int[] state) {
		for (int i = 0; i < state.length; ++i)
			if (state[i] == 0)
				return i;

		return -1;
	}


	private Comparator<Node> comp = new Comparator<Node>() {
		@Override
		public int compare(Node o1, Node o2) {
			return o1.fn - o2.fn;
		}
	};

	private int getH1(int[] state) {
		int estimatedCost = 0;
		for (int i = 0; i < state.length; ++i){
			if (state[i] != i){
				++estimatedCost;
			}
		}

		if (h1){
			return estimatedCost;
		}
		else{
			return 0;
		}
	}

	private int getH2(int[] state) {
		int estimatedCost = 0;
		for (int i = 0; i < state.length; ++i){
			estimatedCost += mDis.get(state[i])[i];
		}

		if (h2){
			return estimatedCost;
		}
		else{
			return 0;
		}
	}
	


	private int[] getInitState() {
		Scanner keyboard = new Scanner(System.in);
		int[] state = new int[pSize];
		

		for (int i = 0; i < 3; ++i) {
			String[] line = keyboard.nextLine().split(" ");
			state[i * 3] = Integer.valueOf(line[0]);
			state[i * 3 + 1] = Integer.valueOf(line[1]);
			state[i * 3 + 2] = Integer.valueOf(line[2]);
		
		}

		return state;
	}
	
	private int[] getInitStateLine(){
		Scanner keyboard = new Scanner(System.in);
		int[] state = new int[pSize];
		
		String num = keyboard.next();
		int number = Integer.valueOf(num);
		for (int i = 8; i >= 0; i--) {
			state[i] = number % 10;
			
			number = number / 10;
		
		}
		return state;
	}
	
	private int[] getInitStateMult(int number){
		int[] state = new int[pSize];
		

		for (int i = 8; i >= 0; i--) {
			state[i] = number % 10;
			number = number / 10;
		
		}
		return state;
	}
	
	//check to see if initial state is solvable
	private boolean solvable(int[] state) {
		int invert = 0;
		
		for (int i = 0; i < state.length; ++i)
			for (int j = i + 1; j < state.length; ++j)
				if ((state[j] != 0) && (state[i] > state[j]))
					++invert;

		if (invert % 2 == 0)
			return true;
		else
			return false;
	}
	
	private boolean checkGoal(int[] record) {
		for (int i = 0; i < pSize; ++i)
			if (record[i] != i){
				return false;
			}

		return true;
	}

	private boolean isStateExplored(List<Node> explored, Node current) {
		for (Node node : explored)
			if (node.getState().equals(current.getState()))
				return true;

		return false;
	}

	private void printNode(Node node) {
		int[] state = node.getState();
		if (!test) {
			System.out.println("---------------");
			System.out.println(state[0] + " " + state[1] + " " + state[2] + " Current step: " + node.getGn());
			System.out.println(state[3] + " " + state[4] + " " + state[5]);
		} else {
			System.out.println(state[0] + " " + state[1] + " " + state[2]);
			System.out.println(state[3] + " " + state[4] + " " + state[5]);
		}
		System.out.println(state[6] + " " + state[7] + " " + state[8]);
	}

	private int search(int[] state, boolean all) {
		List<Node> explored = new ArrayList<>();
		PriorityQueue<Node> frontiers = new PriorityQueue<>(9, comp);
		int zeroPos = getZeroPos(state);
		int fn = 0;
		int gn = 0;

		// must have zero in the input
		if (zeroPos < 0) {
			System.out.println("Invalid input (must include 0s)");
			return 0;
		}

		fn = gn + getH1(state) + getH2(state);

		if (fn == 0) {
			System.out.println("Input is already in original state");
			return 0;
		}
		numNode = 0;

		Node root = new Node(fn, gn, state, zeroPos, null);
		frontier(frontiers, root, null);

		if (test)
			printNode(root);

		while (frontiers.size() > 0) {
			Node current = frontiers.remove();
			if (isStateExplored(explored, current))
				continue;

			if (!isStateExplored(explored, current.getParent()))
				explored.add(current.getParent());

			if (!checkGoal(current.getState()))
				frontier(frontiers, current, current.getParent());
			else {
				explored.add(current);
				break;
			}
		}

		List<Node> printNode = new ArrayList<>();
		Node node = explored.get(explored.size() - 1);
		while (node != null) {
			printNode.add(0, node);
			node = node.getParent();
		}

		if (!test) {
			if (!all){
				for (Node n : printNode) {
					printNode(n);
				}
			}
			System.out.println("-----------");
			System.out.println("Steps: " + (printNode.size() - 1));
			System.out.println("Nodes: " + numNode);
		}

		return (printNode.size() - 1);
	}

	public void run(int choice) {
		int[] state;
		if(choice == 1){
			state = getInitStateLine();
		}
		else{
			state = getInitState();
		}
		

		if (solvable(state)) {
			search(state,false);
		} else
			System.out.println("Unsolvable");
	}
	
	public void runAll(int num) {
		int[] state = getInitStateMult(num);
		if(h1){
			h[hcount++] = getH1(state);
		}else if(h2){
			h[hcount++] = getH2(state);
		}
		
		if (solvable(state)) {
			search(state,true);
		} else
			System.out.println("Unsolvable");
	}

	public void runRandom(int times) {
		int[] runTimes = new int[50];
		int[] nodeNum = new int[50];
		Random rand = new Random();

		for (int time = 0; time < times; ++time) {
			int[] state = new int[pSize];
			HashSet<Integer> record = new HashSet<>();
			
			// generate randomized state
			for (int i = 0; i < pSize; ++i) {
				int num = rand.nextInt(9);
				if (record.contains(num)) {
					--i;
					continue;
				} else {
					state[i] = num;
					record.add(num);
				}
			}
			
			
			if (solvable(state)) {
				if (test)
					System.out.println("Test " + (time + 1) + ":");
				int length = search(state,false);

				++runTimes[length];
				nodeNum[length] += numNode;

			} else
				--time;
		}
	}

	public static void main(String[] args) {
		eightPuzzle ps = new eightPuzzle();
		ps.setH1(false);
		ps.setH2(false);
		
		System.out.println("Which algorithm would you like to run?");
		System.out.println("(1) h1 = the number of misplaced tiles; (2) h2 = Manhattan");
		Scanner keyboard = new Scanner(System.in);
		String choice = keyboard.nextLine();
		if(choice.equals("1")){
			ps.setH1(true);
		}
		if(choice.equals("2")){
			ps.setH2(true);
		}
		System.out.println("How would you like to run the program?");
		System.out.print("(1)Manual Straight Line | (2) Manual Grid | (3) Manual Multiple Straight Line| (4)Random");
		choice = keyboard.nextLine();
		if (choice.equals("1")) {
			while (!choice.equals("x") && !choice.equals("X")) {
				System.out.println("Enter your puzzle: ");
				long startTime = System.currentTimeMillis();
				ps.run(Integer.valueOf(choice));
				long endTime = System.currentTimeMillis();
				System.out.println("Time:" + (endTime - startTime) + " ms");
				System.out.println("X to exit");
				keyboard = new Scanner(System.in);
				choice = keyboard.nextLine();
			}
		}else if(choice.equals("2")){
			while (!choice.equals("x") && !choice.equals("X")) {
				System.out.println("Enter your puzzle: ");
				long startTime = System.currentTimeMillis();
				ps.run(Integer.valueOf(choice));
				long endTime = System.currentTimeMillis();
				System.out.println("Time:" + (endTime - startTime) + " ms");
				System.out.println("X to exit");
				keyboard = new Scanner(System.in);
				choice = keyboard.nextLine();
			}
		}
		else if (choice.equals("3")) {
			long time = 0;
			int totalNode = 0;
			while (!choice.equals("x") && !choice.equals("X")) {
				System.out.println("Enter your puzzles: ");
				int count = 1;
				while(count <= 100){
					String num = keyboard.next();
					System.out.println("Puzzle " + count);
					long startTime = System.nanoTime();
					ps.runAll(Integer.valueOf(num));
					totalNode += numNode;
					long endTime = System.nanoTime();
					time += endTime - startTime;
					if(count == 99){
						System.out.println("Click Enter to see Final");
					}
					count++;
				}

				long average = time/100;
				int aveNodes = totalNode/100;
				System.out.println("Average Run Time: "+ average + "ns");
				System.out.println("Average Number of Nodes: " + aveNodes);
				System.out.println("X to exit");
				keyboard = new Scanner(System.in);
				choice = keyboard.nextLine();
			}
		}else if (choice.equals("4")) {
			while (!choice.equals("x") && !choice.equals("X")) {
				System.out.println("Randomly generated puzzle:");
				long startTime = System.currentTimeMillis();
				ps.runRandom(1);
				long endTime = System.currentTimeMillis();
				System.out.println("Time:" + (endTime - startTime) + " ms");
				System.out.println("X to exit");
				keyboard = new Scanner(System.in);
				choice = keyboard.nextLine();
			}
		} else {
			System.out.println("Choose an option (1), (2), or (3), Try Again");
		}
		System.out.println("Exiting");
	}
}