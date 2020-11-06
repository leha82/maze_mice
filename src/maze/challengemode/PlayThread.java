package maze.challengemode;

import boot.*;

public class PlayThread extends Thread {
	private Maze maze;
	private MouseChallenge mouse;
	private ChallengeInfo ci;
	private int mode;
	private boolean playing;
	private int[][] map;
	private int curr_x, curr_y;

	// mode 0 : searching mode, mode 1 : challenge mode
	public PlayThread(BaseModule bm, ChallengeInfo ci, int mode) {
		this.maze = bm.maze;
		this.mouse = bm.mouse;
		this.ci = ci;
		this.mode = mode;
		this.playing = true;

		this.map = bm.getMap();
		curr_x = bm.getStart_x();
		curr_y = bm.getStart_y();

		this.mouse.initMouse();
		
		ci.addSearchCount();
	}

	private void moveMouse(int[][] map, int dir) {
		if (dir == 1 && curr_y > 0) {
			if (map[curr_y - 1][curr_x] == 0)
				curr_y--;
		} else if (dir == 2 && curr_x < maze.getWidth() - 1) {
			if (map[curr_y][curr_x + 1] == 0)
				curr_x++;
		} else if (dir == 3 && curr_y < maze.getHeight() - 1) {
			if (map[curr_y + 1][curr_x] == 0)
				curr_y++;
		} else if (dir == 4 && curr_x > 0) {
			if (map[curr_y][curr_x - 1] == 0)
				curr_x--;
		}
	}

	@Override
	public void run() {
		int dir = 0;
		try {
			while (playing) {
				// search mode���� play
				if (mode == 0) {
					dir = mouse.nextSearch(maze.getArea(curr_x, curr_y));
					if (dir == -1) {
						playing = false;
						break;
					}

					this.moveMouse(maze.getMap(), dir);

					ci.addOneSearchMove();

					if (ci.getLastSearchMove() >= ci.getLimitSearchMove()) {
						playing = false;
						break;
					}
					sleep(ci.getDelaySearch());
				} else if (mode == 1) {
					// challenge mode���� Play
					dir = mouse.nextMove(maze.getArea(curr_x, curr_y));

					this.moveMouse(maze.getMap(), dir);

					ci.addOneChallengeMove();

					if (isMouseGoal()) {
						playing = false;
						break;
					}
					
					sleep(ci.getDelayChallenge());
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}

	public void finish() {
		playing = false;
//		System.out.println("pt : finish");
	}

	public boolean isMouseGoal() {
		if (curr_x == maze.getEsc_x() && curr_y == maze.getEsc_y()) {
			return true;
		}

		return false;
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	public int getCurr_x() {
		return curr_x;
	}

	public void setCurr_x(int curr_x) {
		this.curr_x = curr_x;
	}

	public int getCurr_y() {
		return curr_y;
	}

	public void setCurr_y(int curr_y) {
		this.curr_y = curr_y;
	}
	
	
}
