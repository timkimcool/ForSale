import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ForSaleServer extends Application implements ForSaleConstants {
	private ServerSocket serverSocket;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		/**
		 * 		UI for server information
		 */
		// Text area for server information
		TextArea gameLog = new TextArea();
		gameLog.setEditable(false);							// disable user editing text area
		ScrollPane scrollPane = new ScrollPane(gameLog);
		
		// HBox for buttons
		HBox HBoxPane = new HBox();
		// HBoxPane.setPrefSize(200, 100);
		HBoxPane.setSpacing(10);
		HBoxPane.setAlignment(Pos.CENTER);
		Button btStart = new Button("Start");
		btStart.setDisable(true);
		btStart.setMinSize(60, 30);
		Button btQuit = new Button("Quit");
		btQuit.setMinSize(60, 30);
		btQuit.setOnAction(e -> {
			Platform.exit();
			try {
				serverSocket.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			System.exit(0);								// this.dispose() closing window; use System.exit(0) to close application
		});
		HBoxPane.getChildren().addAll(btStart, btQuit);
		
		// border pane to hold all the interfaces
		BorderPane borderPane = new BorderPane();			
		borderPane.setCenter(scrollPane);
		borderPane.setBottom(HBoxPane);
		Scene scene = new Scene(borderPane);
		primaryStage.setTitle("ForSale Sever");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(e -> {
			Platform.exit();
			try {
				serverSocket.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			System.exit(0);								// this.dispose() closing window; use System.exit(0) to close application
		});
		
		
		/**
		 * 		New Thread: create server socket, session once players connect
		 */
		
		/** 	@@@ TO DO @@@
		 * 			Allow server to start game
		 * 		
		 */
		
		new Thread(() -> {
			try {
				serverSocket = new ServerSocket(PORT);
				serverSocket.setReuseAddress(true);
				Platform.runLater(() -> gameLog.appendText(new Date() + ": Server started at socket " + PORT + "\n"));
				
				// Create session for players
				Platform.runLater(() -> gameLog.appendText(new Date() + ": Wait for players to join session\n"));
				
				ArrayList<Player> newPlayers = new ArrayList<Player>();
				int playerCount[] = {0};
				boolean flag[] = {true};
				while(flag[0]) {
					Socket socket = serverSocket.accept();
					newPlayers.add(new Player(playerCount[0] + 1));
					newPlayers.get(playerCount[0]).setPlayerSocket(socket);
					String test ="Player " + (playerCount[0] + 1) + "'s IP address " + newPlayers.get(playerCount[0]).getPlayerSocket().getInetAddress().getHostAddress() + '\n';
					Platform.runLater(() -> {
						gameLog.appendText(new Date() + ": Player " + (playerCount[0]) + " joined session\n");
						gameLog.appendText(test); });
					
					playerCount[0]++;
					
					if (playerCount[0] == 3) {
						btStart.setDisable(false);
						btStart.setOnAction(e -> {
							new Thread(new StartSession(newPlayers)).start();
							flag[0] = false;
						});
					}
					
				}
			} catch(IOException ex) {
				ex.printStackTrace();
			}
		}).start();
	}
	
	// Thread class for starting new game
	class StartSession implements Runnable, ForSaleConstants {
		private int	playerCount;
		private Deck houseDeck;
		private ArrayList<Card> pOffered;
		private ArrayList<Player> players;
		
		
		public StartSession(ArrayList<Player> players) {
			this.players = new ArrayList<Player>();
			this.players = players;
			pOffered = new ArrayList<Card>();
			playerCount = players.size();
			Initialize();
		}

		
		@Override
		public void run() {
			try {
				// Create data input and output streams and player setup
				for (Player p : players) {
					p.setToPlayer(new DataOutputStream(p.getPlayerSocket().getOutputStream()));
					p.setFromPlayer(new DataInputStream(p.getPlayerSocket().getInputStream()));
					p.setObjToPlayer(new ObjectOutputStream(p.getPlayerSocket().getOutputStream()));
					p.setObjFromPlayer(new ObjectInputStream(p.getPlayerSocket().getInputStream()));
					p.getObjToPlayer().writeObject(p);
					p.getToPlayer().writeInt(houseDeck.getDeck().size());
					p.getToPlayer().writeInt(playerCount);
					for(Card c : pOffered) {
						p.getObjToPlayer().writeObject(c);
					}
				}
				
				// Play game
				while(true) {
					
				}
				
			} catch(IOException ex) {
				ex.printStackTrace();
			}
			
		}
		
		private void Initialize() {		
			houseDeck = new Deck("house", playerCount);

			// Starting money
			int money;
			if (playerCount < 5) {
				money = 14;
			} else { money = 18; }
			
			for (Player p : players) {
				p.setMoney(money);
			}
			
			// starting property
			offerProperty();
		}
		// initialize board information to keep track of
		// cards, 
		private void offerProperty() {
			for (int i = 0; i < playerCount; i++) {
				pOffered.add(houseDeck.removeCard());
			}
		}
	}
	/**
	 * Main method for IDE usage only; not needed from command line
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
