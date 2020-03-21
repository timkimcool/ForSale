package com.forsale;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
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
	private int port = 0;
	
	public void start(Stage primaryStage, String port) throws Exception {
		this.port = Integer.parseInt(port);
		start(primaryStage);
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		if (port == 0) {
			port = PORT;
		}
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
		
		new Thread(() -> {
			try {
				serverSocket = new ServerSocket(port);
				Platform.runLater(() -> {
					try {
						serverSocket.getInetAddress();
						gameLog.appendText(new Date() + ": Server started at IP address " + InetAddress.getLocalHost() + "\n");
					} catch (UnknownHostException e1) {
						e1.printStackTrace();
					}
				});
				Platform.runLater(() -> gameLog.appendText(new Date() + ": Server started at port " + port + "\n"));
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
							btStart.setDisable(true);
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
		private Deck currencyDeck;
		private ArrayList<Card> pOffered;
		private ArrayList<Player> players;
		
		// Game state
		private boolean phase1 = true;
		private boolean phase2 = true;
		private int currentBid;
		private int playerBid;
		// Game pieces
		Card removedCard;
		private boolean resetStartingPlayer = false;
		private Player startPlayer;
		
		
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
					// set player name
					p.setName((String) p.getObjFromPlayer().readObject());
					System.out.println(p.getName());
					// send images
					p.getObjToPlayer().writeObject(new Card("houseBack.png", 0, true));
					p.getObjToPlayer().writeObject(new Card("background.png", 0, false));
					p.getObjToPlayer().writeObject(new Card("dollar.png", 0, false));
					// send game values
					p.getObjToPlayer().writeObject(p);
					p.getToPlayer().writeInt(houseDeck.getDeck().size());
					p.getToPlayer().writeInt(playerCount);
				}
				for (Player p : players) {
					sendToPlayers(p.getName());
				}
				
				// Play game
				// phase 1
				Collections.shuffle(players);						// randomize player order
				while(phase1) {
					// reset each round
					boolean round = true;
					currentBid = 0;
					playerBid = 0;
					// reset cards offered
					offerCard(houseDeck);
					for(Card c : pOffered) {
						sendToPlayers(c);
					}
					for (Player p1 : players) {
						p1.setBidding(true);
					}
					while (round) {
						for (Player p : players) {
							if (resetStartingPlayer) {
								if (p.getPlayerNumber() != startPlayer.getPlayerNumber()) {
									continue;
								} else { resetStartingPlayer = false; }
							}
							// if everyone else passed, set last bidding player as previous player + bidding to false
							if (!round && p.isBidding()) {
								p.setBidding(false);
								startPlayer = p;
								resetStartingPlayer = true;
							}
							if (p.isBidding()) {							// player is bidding/didn't pass yet
								sendToPlayers(p.getPlayerNumber());			// player number's turn
								sendToPlayers(currentBid);					// current bid amount
								// if player passed
								playerBid = p.getFromPlayer().readInt();	// get bid from player
								sendToPlayers(playerBid);
								if (playerBid <= 0) {						// -1 bid = pass
									p.setBidding(false);
									removedCard = minCard(pOffered);
									pOffered.remove(removedCard);
								} else {									// else player bids
									currentBid = playerBid;					// current bid;
								}
								// done bidding
								round = stillBidding();
								sendToPlayers(round);
								if (!round) {
									for (Player p1 : players) {
										if (p1.isBidding()) {
											sendToPlayers(p1.getPlayerNumber());
										}
									}
								}
								if (houseDeck.getDeck().size() == 0) {
									phase1 = false;
								}
								sendToPlayers (phase1);
							}	
						}
					}
				}
				System.out.println("Send card info");
				sendToPlayers(new Card("moneyBack.png", 0, true));
				HashMap<Integer, Player> currencyMap = new HashMap<Integer, Player>();
				ArrayList<Integer> playerCurrencies = new ArrayList<Integer>();
				int playerValue = 0;
				currencyMap.clear();
				playerCurrencies.clear();
				while (phase2) {
					// offer cards to players
					System.out.println("Offer cards");
					offerCard(currencyDeck);
					for (Card c : pOffered) {
						sendToPlayers(c);
					}
					// read property cards player sent
					for (Player p : players) {
						playerValue = p.getFromPlayer().readInt();
						System.out.println("received: " + playerValue);
						currencyMap.put(playerValue, p);
						playerCurrencies.add(playerValue);

					}
					// send currency to players
					Collections.sort(playerCurrencies);
					int place = 1;
					while (playerCurrencies.size() != 0) {
						playerValue = playerCurrencies.remove(0);
						// 
						sendToPlayers(place);
						sendToPlayers(currencyMap.get(playerValue).getPlayerNumber());
						sendToPlayers(playerValue);
						place++;
						System.out.println("player: " + currencyMap.get(playerValue).getPlayerNumber() + " place: " + place + " card: " + playerValue);
					}
					if (currencyDeck.getDeck().size() == 0) {
						phase2 = false;
					}
					sendToPlayers(phase2);
				}					// end of phase 2
				// program game end; display game winner
				playerCurrencies.clear();
				for (Player p : players) {
					p.setMoney(p.getFromPlayer().readInt());							// read player money
					p.setTotalScore(p.getFromPlayer().readInt() + p.getMoney());		// set score (total currency + money)
				}
				for (Player p : players) {
					int place = 1;
					for (Player p1 : players) {
						if (!p.equals(p1)) {
							if (p.compareTo(p1) == -1) {
								place++;
							}
						}
					}
					sendToPlayers(place);
					sendToPlayers(p.getPlayerNumber());
					sendToPlayers(p.getTotalScore());
					sendToPlayers(p.getMoney());
				}
				
			} catch(IOException | ClassNotFoundException ex) {
				ex.printStackTrace();
			}
			
		}
		
		private void Initialize() {		
			houseDeck = new Deck("house", playerCount);
			houseDeck.makeHouseDeck();
			currencyDeck = new Deck("money", playerCount);
			currencyDeck.makeCurrencyDeck();
			
			// Starting money
			int money;
			if (playerCount > 4) {
				money = 14;
			} else { money = 18; }
			
			for (Player p : players) {
				p.setMoney(money);
			}
		}
		// initialize board information to keep track of
		// cards, 
		private void offerCard(Deck deck) {
			pOffered.clear();
			for (int i = 0; i < playerCount; i++) {
				pOffered.add(deck.removeCard());
			}
		}
		
		private void sendToPlayers(Object o) {
			for(Player p : players) {
				try {
					p.getObjToPlayer().writeObject(o);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		private void sendToPlayers(boolean o) {
			for(Player p : players) {
				try {
					p.getToPlayer().writeBoolean(o);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		private void sendToPlayers(int i) {
			for(Player p : players) {
				try {
					p.getToPlayer().writeInt(i);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private boolean stillBidding() {
			int stillBidding = 0;
			for (Player p : players) {
				if (p.isBidding()) {
					stillBidding++;
					if (stillBidding > 1) { return true; }
				}
			}
			return false;
		}
		private Card minCard(ArrayList<Card> cards) {
			int minValue = 30;
			Card retCard = null;
			for (Card c : cards) {
				if (c.getValue() < minValue) {
					retCard = c;
					minValue = c.getValue();
				}
			}
			return retCard;
		}
	}
	/**
	 * Main method for IDE usage only; not needed from command line
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
