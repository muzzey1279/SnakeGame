import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.*;


public class GamePanel extends JPanel implements ActionListener {
	
	static final int BOARD_WIDTH = 600;
    static final int BOARD_HEIGHT = 600;
    static final int UNIT_SIZE = 40;
    static final int GAME_UNITS = (BOARD_WIDTH*BOARD_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 75;
    final int snakeX[] = new int[GAME_UNITS];
    final int snakeY[] = new int[GAME_UNITS];
    int size = 6;
    int applesEaten;
    int highScore;
    private int appleX;
    private int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    JButton playAgainButton = new JButton();
    
    
	GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
		this.setBackground(Color.BLACK);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		this.playAgainButton = new JButton("Play Again");
		timer = new Timer(DELAY, this);
		startGame();
		
	}
		
		 
	public void startGame() {
		timer.stop();
		repaint();
		direction='R';
		size = 6;
		applesEaten = 0;
		newSnake();
		newApple();
		running = true;
        timer.start();
    }
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g) {
		if(running) {
			drawApple(g);
			drawSnake(g);
			getScore(g);
		}
		
		else{
			gameOver(g);
		}
			
	}
	
	public void drawApple(Graphics g) {
		g.setColor(Color.RED);
		g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
	}
	
	public void drawSnake(Graphics g) {
		
		for(int i = 0; i<size; i++) {
			if(i==0) {
				g.setColor(new Color(45, 180, 0));
				g.fillRect(snakeX[i], snakeY[i], UNIT_SIZE, UNIT_SIZE);
			}
			else {
				g.setColor(Color.green);
				g.fillRect(snakeX[i], snakeY[i], UNIT_SIZE, UNIT_SIZE);
			}
		}
	}
	
	public void newApple() {
		appleX = random.nextInt((int)(BOARD_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(BOARD_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	
	public void newSnake() {
		snakeX[0] = 0;
		snakeY[0] = 0;
	}
	public void move() {
		for(int i = size; i>0; i--) {
			snakeX[i] = snakeX[i-1];
			snakeY[i] = snakeY[i-1];
		}
		
		switch(direction) {
		case 'U':
			snakeY[0] = snakeY[0] - UNIT_SIZE;
			break;
		case 'D':
			snakeY[0] = snakeY[0] + UNIT_SIZE;
			break;
		case 'L':
			snakeX[0] = snakeX[0] - UNIT_SIZE;
			break;
		case 'R':
			snakeX[0] = snakeX[0] + UNIT_SIZE;
			break;
		}
		
	}
	public void checkApple() { 
		if((snakeX[0] == appleX) && (snakeY[0] == appleY)) {
			size++;
			applesEaten++;
			newApple();
		}
	}
	public void checkCollisions() {
		for(int i = size; i>0; i--) {
			if((snakeX[0] == snakeX[i]) && (snakeY[0] == snakeY[i])) {
				running = false;
			}
		}
		if((snakeX[0]<0)) {
			running = false;
		}
		if(snakeX[0]>BOARD_WIDTH) {
			running = false;
		}
		if(snakeY[0]<0) {
			running = false;
		}
		if(snakeY[0]>BOARD_HEIGHT){
			running = false;
		}
		if(!running) {
			timer.stop();
		}
	}
	public void gameOver(Graphics g) {
		g.setColor(Color.red);
		g.setFont(new Font("Apple Casual", Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Game Over", (BOARD_WIDTH - metrics.stringWidth("Game Over"))/2, BOARD_HEIGHT/2);
		getScore(g);
		getHighScore(g);
		playAgain();
		}
	    
	public void playAgain() {
		 for (int i = 0; i < size; i++) {
	            snakeX[i] = 0;
	            snakeY[i] = 0;
	        }
	        size = 0;
        playAgainButton.setBounds(250, 400, 100, 30);
        add(playAgainButton);
        playAgainButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	remove(playAgainButton);
                startGame();
            }
        });
		
	}
	
	public void getScore(Graphics g) {
		g.setColor(Color.blue);
		g.setFont(new Font("Helvetica", Font.PLAIN, 40));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Score: " + applesEaten, (BOARD_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, BOARD_HEIGHT/10);
	}
	public void getHighScore(Graphics g) {
		if(applesEaten>highScore) {
			highScore = applesEaten;
		}
		g.setColor(Color.green);
		g.setFont(new Font("Helvetica", Font.PLAIN, 40));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("High Score: " + highScore, (BOARD_WIDTH - metrics.stringWidth("High Score: " + highScore))/2, BOARD_HEIGHT/5);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		//if(running) {
			move();
			checkApple();
			checkCollisions();
		//}
		repaint();
		}

	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R' || direction!='L') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L' || direction!='R') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D' || direction!='U') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U' || direction!='D') {
					direction = 'D';
				}
				break;
			}
		}
	}
}
