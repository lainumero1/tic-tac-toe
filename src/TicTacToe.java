import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.TimerTask;
import java.util.Timer;
import java.awt.*;

/**
 * The TicTacToe program implements an application of the Tic Tac Toe game using Java GUI programming. 
 * Upon starting, a JFrame is displayed on screen and player can interact with the components inside to carry out
 * a game of Tic Tac Toe. 
 * 
 * @author Lai Tsz Ng
 * @version 1.0
 */
public class TicTacToe {

	boolean hasResult = false;

	/**
	 * This is the main method which initiates an instance of TicTacToe and runs it. 
	 * @param args Unused.
	 */
	public static void main(String args[]) {
		TicTacToe gui = new TicTacToe();
		gui.go();
	}

	/**
	 * This method is used for all the main operations in a game, and creating all JComponent needed. 
	 */
	public void go() {
		JFrame frame = new JFrame();
		frame.setTitle("Tic Tac Toe");
		frame.setLayout(new GridBagLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JMenuBar menuBar = new JMenuBar();
		JMenu controlMenu = new JMenu("Control");
		JMenu helpMenu = new JMenu("Help");
		JMenuItem exit = new JMenuItem("Exit");
		JMenuItem instructions = new JMenuItem("Instructions");
		controlMenu.add(exit);
		helpMenu.add(instructions);
		menuBar.add(controlMenu);
		menuBar.add(helpMenu);
		
		/**
		 * This class listens to click action on the JMenuItem 'exit' in the JMenu 'controlMenu', 
		 * and exits the programme when clicked. 
		 */
		class ExitListener implements ActionListener {
			/**
			 * This method is called when the JMenuItem 'exit' is clicked. 
			 */
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		}
		/**
		 * This class listens to click action on the JMenuItem 'instructions' button in the JMenu 'help', 
		 * and shows a JOptionPane for game instruction. 
		 */
		class InstructionsListener implements ActionListener {
			/**
			 * This method is called when the JMenuItem 'instructions' is clicked. 
			 */
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Some information about the game:\n"
						+ "- The move is not occupied by any mark.\n"
						+ "- The move is made in the player's turn.\n"
						+ "- The move is made within the 3 x 3 board.\n"
						+ "The game would continue and switch among the player and the computer until it reaches either one of the following conditions:\n"
						+ "- Player wins.\n"
						+ "- Computer wins.\n"
						+ "- Draw.", "Instructions", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		exit.addActionListener(new ExitListener());
		instructions.addActionListener(new InstructionsListener());
		JPanel statusPanel = new JPanel();
		JLabel status = new JLabel("Enter your player name...");
		JPanel gamePanel = new JPanel();
		gamePanel.setLayout(new GridBagLayout());
		
		JLabel nameTitle = new JLabel("Enter your name: ");
		JTextField nameField = new JTextField(10);
		JButton submit = new JButton("Submit");

		JButton tile1 = new JButton();
		JButton tile2 = new JButton();
		JButton tile3 = new JButton();
		JButton tile4 = new JButton();
		JButton tile5 = new JButton();
		JButton tile6 = new JButton();
		JButton tile7 = new JButton();
		JButton tile8 = new JButton();
		JButton tile9 = new JButton();
		JButton[] tileList = {tile1, tile2, tile3, tile4, tile5, tile6, tile7, tile8, tile9};
		JLabel playerScore = new JLabel("0");
		JLabel compScore = new JLabel("0");
		JLabel drawScore = new JLabel("0");

		/**
		 * This class listens to click action on the nine JButton under the JPanel 'gridPanel', 
		 * then checks if click action is valid and acts accordingly. 
		 */
		class TileListener implements ActionListener {
			/**
			 * This method is called when any of the nine JButton under 'gridPanel' is clicked.
			 */
			public void actionPerformed(ActionEvent e) {
				JButton targetTile = (JButton) e.getSource();
				hasResult = false;
				if (targetTile.getText().length() == 2 && nameField.isEditable() == false
				 && status.getText().equals("Valid move. wait for your opponent. ") == false) {
					targetTile.setForeground(Color.GREEN);
					targetTile.setText("X");
					if (hasResult == false) {
						status.setText("Valid move. wait for your opponent. ");
					}
				}
			}
		}

		/**
		 * This class listens to property change on the text of JLabel 'status' under the JPanel 'statusPanel'. 
		 * If the text property changes, and the String "Valid move. wait for your opponent. " is detected, 
		 * the task statusAction would be run in 2 seconds. 
		 */
		class StatusListener implements PropertyChangeListener {
			/**
			 * This method is called when there is any property changes on the text of JLabel 'status'. 
			 */
			public void propertyChange(PropertyChangeEvent e) {
				if (status.getText().equals("Valid move. wait for your opponent. ")) {
					Timer statusTimer = new Timer();
					TimerTask statusAction = new TimerTask() {
						public void run() {
							Integer choice = (int) (Math.random() * 9);
							while (tileList[choice].getText().length() != 2) {
								choice = (int) (Math.random() * 9);
							}
							tileList[choice].setForeground(Color.RED);
							tileList[choice].setText("O");
							if (hasResult == false) {
								status.setText("Your opponent has moved, now is your turn. ");
							}
							statusTimer.cancel();
						}
					};
					statusTimer.schedule(statusAction, 2000);
				}
			}
		}

		/**
		 * This class listens to property change on the nine JButton under the JPanel 'gridPanel'. 
		 * If winning conditions for computer or player is detected, or all nine JButton are filled, 
		 * a JOptionPane message would be shown, announcing the result of the game, 
		 * and set score for computer/player/draw. 
		 * Finally, the game would be restarted after player closes the JOptionPane. 
		 */
		class CountingListener implements PropertyChangeListener {
			/**
			 * This method is called when any of the nine JButton under 'gridPanel' changes its property. 
			 */
			public void propertyChange(PropertyChangeEvent e) {
				if (tile1.getText().equals("O") && hasResult == false) {
					if ((tile1.getText().equals(tile2.getText()) && tile2.getText().equals(tile3.getText())) ||
					(tile1.getText().equals(tile5.getText()) && tile5.getText().equals(tile9.getText())) ||
					(tile1.getText().equals(tile4.getText()) && tile4.getText().equals(tile7.getText()))) {
						hasResult = true;
						JOptionPane.showMessageDialog(null, "Computer wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);	
						compScore.setText(String.valueOf(Integer.valueOf(compScore.getText()) + 1));
					}
				}

				if (tile2.getText().equals("O") && hasResult == false) {
					if (tile2.getText().equals(tile5.getText()) && tile5.getText().equals(tile8.getText())) {
						hasResult = true;
						JOptionPane.showMessageDialog(null, "Computer wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);	
						compScore.setText(String.valueOf(Integer.valueOf(compScore.getText()) + 1));
					}
				}

				if (tile3.getText().equals("O") && hasResult == false) {
					if ((tile3.getText().equals(tile5.getText()) && tile5.getText().equals(tile7.getText())) ||
					(tile3.getText().equals(tile6.getText()) && tile6.getText().equals(tile9.getText()))) {
						hasResult = true;
						JOptionPane.showMessageDialog(null, "Computer wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);	
						compScore.setText(String.valueOf(Integer.valueOf(compScore.getText()) + 1));
					}
				}

				if (tile4.getText().equals("O") && hasResult == false) {
					if (tile4.getText().equals(tile5.getText()) && tile5.getText().equals(tile6.getText())) {
						hasResult = true;
						JOptionPane.showMessageDialog(null, "Computer wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);	
						compScore.setText(String.valueOf(Integer.valueOf(compScore.getText()) + 1));
					}
				}
				
				if (tile7.getText().equals("O") && hasResult == false) {
					if (tile7.getText().equals(tile8.getText()) && tile8.getText().equals(tile9.getText())) {
						hasResult = true;
						JOptionPane.showMessageDialog(null, "Computer wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);	
						compScore.setText(String.valueOf(Integer.valueOf(compScore.getText()) + 1));
					}
				}

				if (tile1.getText().equals("X") && hasResult == false) {
					if ((tile1.getText().equals(tile2.getText()) && tile2.getText().equals(tile3.getText())) ||
					(tile1.getText().equals(tile5.getText()) && tile5.getText().equals(tile9.getText())) ||
					(tile1.getText().equals(tile4.getText()) && tile4.getText().equals(tile7.getText()))) {
						hasResult = true;
						JOptionPane.showMessageDialog(null, "Player wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);	
						playerScore.setText(String.valueOf(Integer.valueOf(playerScore.getText()) + 1));
					}		
				}

				if (tile2.getText().equals("X") && hasResult == false) {
					if (tile2.getText().equals(tile5.getText()) && tile5.getText().equals(tile8.getText())) {
						hasResult = true;
						JOptionPane.showMessageDialog(null, "Player wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);	
						playerScore.setText(String.valueOf(Integer.valueOf(playerScore.getText()) + 1));
					}
				}

				if (tile3.getText().equals("X") && hasResult == false) {
					if ((tile3.getText().equals(tile5.getText()) && tile5.getText().equals(tile7.getText())) ||
					(tile3.getText().equals(tile6.getText()) && tile6.getText().equals(tile9.getText()))) {
						hasResult = true;
						JOptionPane.showMessageDialog(null, "Player wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);	
						playerScore.setText(String.valueOf(Integer.valueOf(playerScore.getText()) + 1));
					}
				}

				if (tile4.getText().equals("X") && hasResult == false) {
					if (tile4.getText().equals(tile5.getText()) && tile5.getText().equals(tile6.getText())) {
						hasResult = true;
						JOptionPane.showMessageDialog(null, "Player wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);	
						playerScore.setText(String.valueOf(Integer.valueOf(playerScore.getText()) + 1));
					}
				}

				if (tile7.getText().equals("X") && hasResult == false) {
					if (tile7.getText().equals(tile8.getText()) && tile8.getText().equals(tile9.getText())) {
						hasResult = true;
						JOptionPane.showMessageDialog(null, "Player wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);	
						playerScore.setText(String.valueOf(Integer.valueOf(playerScore.getText()) + 1));
					}
				}

				if (tile1.getText().length() == 1 && hasResult == false) {
					boolean draw = true;
					for (int i = 1; i < tileList.length; i++) {
						if (tileList[i].getText().length() != tile1.getText().length()) {
							draw = false;
							break;
						}
					}
					if (draw == true) {
						hasResult = true;
						JOptionPane.showMessageDialog(null, "It's a draw!", "Game Over", JOptionPane.INFORMATION_MESSAGE);	
						drawScore.setText(String.valueOf(Integer.valueOf(drawScore.getText()) + 1));
					}
				}

				if (hasResult == true) {
					for (int i = 0; i < tileList.length; i++) {
						tileList[i].setText("  ");
					}
					status.setText("WELCOME " + nameField.getText().toUpperCase());
				}
			}
		}

		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridBagLayout());
		gridPanel.setBackground(Color.white);
		GridBagConstraints gridC = new GridBagConstraints();
		gridC.fill = GridBagConstraints.BOTH;
		gridC.weightx = 1;
		gridC.weighty = 1;		

		gridC.gridx = 0;
		gridC.gridy = 0;
		tile1.addActionListener(new TileListener());
		tile1.addPropertyChangeListener("text", new CountingListener());
		tile1.setBorder(new LineBorder(Color.BLACK));
		tile1.setMargin(new Insets(1, 1, 1, 1));
		tile1.setText("  ");
		tile1.setFont(new Font("Arial", Font.PLAIN, 40));
		gridPanel.add(tile1, gridC);
		
		gridC.gridx = 1;
		gridC.gridy = 0;
		tile2.addActionListener(new TileListener());
		tile2.addPropertyChangeListener("text", new CountingListener());
		tile2.setBorder(new LineBorder(Color.BLACK));
		tile2.setMargin(new Insets(1, 1, 1, 1));
		tile2.setText("  ");
		tile2.setFont(new Font("Arial", Font.PLAIN, 40));
		gridPanel.add(tile2, gridC);
		
		gridC.gridx = 2;
		gridC.gridy = 0;
		tile3.addActionListener(new TileListener());
		tile3.addPropertyChangeListener("text", new CountingListener());
		tile3.setBorder(new LineBorder(Color.BLACK));
		tile3.setMargin(new Insets(1, 1, 1, 1));
		tile3.setText("  ");
		tile3.setFont(new Font("Arial", Font.PLAIN, 40));
		gridPanel.add(tile3, gridC);
		
		gridC.gridx = 0;
		gridC.gridy = 1;
		tile4.addActionListener(new TileListener());
		tile4.addPropertyChangeListener("text", new CountingListener());
		tile4.setBorder(new LineBorder(Color.BLACK));
		tile4.setMargin(new Insets(1, 1, 1, 1));
		tile4.setText("  ");
		tile4.setFont(new Font("Arial", Font.PLAIN, 40));
		gridPanel.add(tile4, gridC);
		
		gridC.gridx = 1;
		gridC.gridy = 1;
		tile5.addActionListener(new TileListener());
		tile5.addPropertyChangeListener("text", new CountingListener());
		tile5.setBorder(new LineBorder(Color.BLACK));
		tile5.setMargin(new Insets(1, 1, 1, 1));
		tile5.setText("  ");
		tile5.setFont(new Font("Arial", Font.PLAIN, 40));
		gridPanel.add(tile5, gridC);
		
		gridC.gridx = 2;
		gridC.gridy = 1;
		tile6.addActionListener(new TileListener());
		tile6.addPropertyChangeListener("text", new CountingListener());
		tile6.setBorder(new LineBorder(Color.BLACK));
		tile6.setMargin(new Insets(1, 1, 1, 1));
		tile6.setText("  ");
		tile6.setFont(new Font("Arial", Font.PLAIN, 40));
		gridPanel.add(tile6, gridC);
		
		gridC.gridx = 0;
		gridC.gridy = 2;
		tile7.addActionListener(new TileListener());
		tile7.addPropertyChangeListener("text", new CountingListener());
		tile7.setBorder(new LineBorder(Color.BLACK));
		tile7.setMargin(new Insets(1, 1, 1, 1));
		tile7.setText("  ");
		tile7.setFont(new Font("Arial", Font.PLAIN, 40));
		gridPanel.add(tile7, gridC);
		
		gridC.gridx = 1;
		gridC.gridy = 2;
		tile8.addActionListener(new TileListener());
		tile8.addPropertyChangeListener("text", new CountingListener());
		tile8.setBorder(new LineBorder(Color.BLACK));
		tile8.setMargin(new Insets(1, 1, 1, 1));
		tile8.setText("  ");
		tile8.setFont(new Font("Arial", Font.PLAIN, 40));
		gridPanel.add(tile8, gridC);
		
		gridC.gridx = 2;
		gridC.gridy = 2;
		tile9.addActionListener(new TileListener());
		tile9.addPropertyChangeListener("text", new CountingListener());
		tile9.setBorder(new LineBorder(Color.BLACK));
		tile9.setMargin(new Insets(1, 1, 1, 1));
		tile9.setText("  ");
		tile9.setFont(new Font("Arial", Font.PLAIN, 40));
		gridPanel.add(tile9, gridC);
		
		JPanel scorePanel = new JPanel();
		scorePanel.setLayout(new GridBagLayout());
		scorePanel.setBorder(BorderFactory.createTitledBorder("Score"));

		JPanel player = new JPanel();
		player.setLayout(new BorderLayout());
		JLabel playerScoreTitle = new JLabel("Player Wins: ");
		player.add(playerScoreTitle, BorderLayout.WEST);
		player.add(playerScore, BorderLayout.EAST);
		
		JPanel comp = new JPanel();
		comp.setLayout(new BorderLayout());
		JLabel compScoreTitle = new JLabel("Computer Wins: ");
		comp.add(compScoreTitle, BorderLayout.WEST);
		comp.add(compScore, BorderLayout.EAST);

		JPanel draw = new JPanel();
		draw.setLayout(new BorderLayout());
		JLabel drawScoreTitle = new JLabel("Draws: ");
		draw.add(drawScoreTitle, BorderLayout.WEST);
		draw.add(drawScore, BorderLayout.EAST);

		GridBagConstraints scoreC = new GridBagConstraints();
		scoreC.gridx = 0;
		scoreC.gridy = 0;
		scoreC.weightx = 0;
		scoreC.weighty = 1;
		scoreC.fill = GridBagConstraints.HORIZONTAL;
		scoreC.insets = new Insets(10, -30, 50, 10);

		scoreC.gridx = 0;
		scoreC.gridy = 1;
		scorePanel.add(player, scoreC);
		
		scoreC.gridx = 0;
		scoreC.gridy = 2;
		scorePanel.add(comp, scoreC);

		scoreC.gridx = 0;
		scoreC.gridy = 3;
		scorePanel.add(draw, scoreC);
		
		JPanel nameTimePanel = new JPanel();
		nameTimePanel.setLayout(new BorderLayout());
		JPanel nameInputPanel = new JPanel();
		nameInputPanel.add(nameTitle);
		nameInputPanel.add(nameField);
		nameInputPanel.add(submit);

		/**
		 * This class listens to click action on the JButton 'submit' under the JPanel 'nameInputPanel', 
		 * then checks if JTextField 'nameField' contains any text.
		 * If text is detected, the game starts.  
		 */
		class SubmitListener implements ActionListener {
			/**
			 * This method is called when the JButton 'submit' is clicked. 
			 */
			public void actionPerformed(ActionEvent e) {
				if (nameField.getText().trim().length() > 0) {
					frame.setTitle("Tic Tac Toe - Player: " + nameField.getText());
					status.setText("WELCOME " + nameField.getText().toUpperCase());
					submit.setEnabled(false);
					nameField.setEditable(false);
				}
			}
		}
		submit.addActionListener(new SubmitListener());
		JPanel timePanel = new JPanel();
		JLabel timeLabel = new JLabel("Current Time: ");
		JLabel time = new JLabel();
		timePanel.add(timeLabel);
		Timer t = new Timer();
		TimerTask timeAction = new TimerTask() {
			/**
			 * This method sets the text content in the JLabel 'time'. 
			 */
			public void run() {
				String currentTime = java.time.LocalDateTime.now().toString().substring(11, 19);
				time.setText(currentTime);
			}
		};
		t.schedule(timeAction, 100, 1000);
		timePanel.add(time);
		nameTimePanel.add(nameInputPanel);
		nameTimePanel.add(timePanel);
		
		GridBagConstraints frameC = new GridBagConstraints();
		frameC.gridx = 0;
		frameC.gridy = 0;
		frameC.gridheight = 1;
		frameC.weightx = 1;
		frameC.weighty = 0;
		frameC.ipady = 0;
		frameC.fill = GridBagConstraints.HORIZONTAL; 
		
		statusPanel.add(status);
		status.addPropertyChangeListener("text", new StatusListener());
		frame.add(statusPanel, frameC);
		frameC.weighty = 1;
		frameC.ipady = 300;
		frameC.gridy = 1;
		frameC.fill = GridBagConstraints.BOTH; 
		
		GridBagConstraints gameC = new GridBagConstraints();
		gameC.weightx = 0.8;
		gameC.weighty = 1;
		gameC.gridx = 0;
		gameC.fill = GridBagConstraints.BOTH; 
		gamePanel.add(gridPanel, gameC);
		gameC.gridx = 1;
		gameC.weightx = 0.2;
		gameC.insets = new Insets(0, 5, 0, 5);
		gamePanel.add(scorePanel, gameC);
		frame.add(gamePanel, frameC);
		frameC.weighty = 0;
		frameC.ipady = 0;
		frameC.gridy = 2;
		frameC.fill = GridBagConstraints.HORIZONTAL; 

		frame.add(nameTimePanel, frameC);
		nameTimePanel.add(nameInputPanel, BorderLayout.NORTH);
		nameTimePanel.add(timePanel, BorderLayout.SOUTH);

		frame.setJMenuBar(menuBar);
		frame.setMinimumSize(new Dimension(500, 400));
		frame.setVisible(true);
	}
}
