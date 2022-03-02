package com.forsale;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ForSaleClient extends Application implements ForSaleConstants{
	// @@@ GUI variables
	private DataInputStream fromServer;			// input and output stream from/to server
	private DataOutputStream toServer;
	private ObjectInputStream objFromServer;			// input and output stream from/to server
	private ObjectOutputStream objToServer;
	private Socket socket;
	private String labelText;

	// Variables from server
	private Player me;
	private ArrayList<Card> pOffered = new ArrayList<Card>();
	private ArrayList<Coin> playerMoney = new ArrayList<Coin>();		// don't need?
	private ArrayList<Card> playerCard = new ArrayList<Card>();
	private ArrayList<Integer> playerCurrency = new ArrayList<Integer>();
	private int playerCount;
	private int remainingCards;
	private int cardCount;
	private Image coinImage;
	private Card selectedCard = null;
	// Fields to update
	private Label currentAction = new Label("Waiting for server to start...");
	// > Panes
	private LabelPane propertyOffered = new LabelPane("Property Offered", 1000, 225);
	private LabelPane deckPane = new LabelPane("x30", 200, 225);				// remaining cards
	private LabelPane yourBidPane = new LabelPane("Your current bid: $0 | Coins in hand: 0 | Minimum bid: $0");
	private int currentBid;
	private int playerBid;
	private int currentPlayer;
	private LabelPane yourPropertyPane = new LabelPane("Your Property Cards", 100, 225);
	private LabelPane yourCurrencyPane= new LabelPane("Your Currency Cards", 100, 225);
	private HBox actionPane = new HBox();
	private VBox mainPane = new VBox();
	private HBox pane2 = new HBox();
	private HBox salePane = new HBox();
	// Game State
	private boolean phase1;
	private boolean phase2;
	private HashMap<Integer, LabelPane> labelPanes = new HashMap<Integer, LabelPane>();
	private HashMap<Integer, String> nameMap = new HashMap<Integer, String>();
	private HashMap<Integer, Card> allCards = new HashMap<Integer, Card>();
	int bid = 0;
	private String playerName;
	private String serverIP;
	private int port = 0;
	private Card add;
	private Button bidButton = new Button("Bid");
	private Button passButton = new Button("Pass");
	private Button sellButton = new Button("Sell");


	public void start(Stage primaryStage, String playerName, String serverIP, String port) throws Exception {
		this.playerName = playerName;
		this.serverIP = serverIP;
		this.port = Integer.parseInt(port);
		start(primaryStage);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		if (playerName == null) {
			playerName = "test";
			port = PORT;
			serverIP = HOST;
		}
		// UI to hold interface
		mainPane.setPrefSize(1200, 900);
		currentAction.setFont(FONT16);
		actionPane.getChildren().add(currentAction);
		actionPane.setStyle("-fx-background-color: rgba(234, 230, 220, 1)");	
		deckPane.setFont(FONT20);
		pane2.getChildren().addAll(propertyOffered.getVBox(), deckPane.getVBox());
		//
		// Pane properties
		//
		// HEIGHT
		actionPane.prefHeightProperty().bind(mainPane.heightProperty().divide(MAIN_PANE * 4));
		pane2.prefHeightProperty().bind(mainPane.heightProperty().divide(MAIN_PANE));
		propertyOffered.getVBox().prefHeightProperty().bind(mainPane.heightProperty().divide(MAIN_PANE));
		deckPane.getVBox().prefHeightProperty().bind(mainPane.heightProperty().divide(MAIN_PANE));
		salePane.prefHeightProperty().bind(mainPane.heightProperty().divide(MAIN_PANE));
		yourBidPane.getVBox().prefHeightProperty().bind(mainPane.heightProperty().divide(MAIN_PANE * 4/2));
		yourPropertyPane.getVBox().prefHeightProperty().bind(mainPane.heightProperty().divide(MAIN_PANE));
		// WIDTH
		actionPane.prefWidthProperty().bind(mainPane.widthProperty());
		pane2.prefWidthProperty().bind(mainPane.widthProperty());
		propertyOffered.getHBox().prefWidthProperty().bind(mainPane.widthProperty().divide(7/6));
		propertyOffered.getVBox().prefWidthProperty().bind(mainPane.widthProperty().divide(7/6));
		deckPane.getVBox().prefWidthProperty().bind(mainPane.widthProperty().divide(7));
		yourBidPane.getVBox().prefWidthProperty().bind(mainPane.widthProperty());		
		yourPropertyPane.getVBox().prefWidthProperty().bind(mainPane.widthProperty());
		yourCurrencyPane.getVBox().prefWidthProperty().bind(mainPane.widthProperty());
		// PADDING
		actionPane.setPadding(PADDING2);
		propertyOffered.getHBox().setPadding(PADDING);
		propertyOffered.getVBox().setPadding(PADDING5);
		deckPane.getVBox().setPadding(PADDING);
		yourBidPane.getVBox().setPadding(PADDING5);
		yourBidPane.getFlowPane().setPadding(PADDING5);
		yourPropertyPane.getHBox().setPadding(PADDING);
		yourPropertyPane.getVBox().setPadding(PADDING5);
		yourCurrencyPane.getHBox().setPadding(PADDING);
		yourCurrencyPane.getVBox().setPadding(PADDING5);
		// mainPane.setPadding(PADDING);
		// SPACING
		actionPane.setSpacing(15);
		pane2.spacingProperty().bind(mainPane.heightProperty().divide(50));
		salePane.spacingProperty().bind(mainPane.heightProperty().divide(50));
		propertyOffered.getHBox().spacingProperty().bind(mainPane.heightProperty().divide(100));
		yourCurrencyPane.getHBox().spacingProperty().bind(mainPane.heightProperty().divide(100));
		mainPane.spacingProperty().bind(mainPane.heightProperty().divide(60));
		// ALIGNMENT
		actionPane.setAlignment(Pos.CENTER);
		deckPane.getHBox().setAlignment(Pos.TOP_CENTER);
		deckPane.getVBox().setAlignment(Pos.BOTTOM_CENTER);
		yourBidPane.getFlowPane().setAlignment(Pos.CENTER_LEFT);
		//
		// Put it all together
		//
		mainPane.getChildren().addAll(actionPane);
		Scene scene = new Scene(mainPane);
		primaryStage.setTitle("ForSale Client: " + playerName);
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(e -> {
			Platform.exit();
			try {
				socket.close();
				fromServer.close();
				toServer.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			System.exit(0);								// this.dispose() closing window; use System.exit(0) to close application
		});
		connectToServer();
	}

	private void connectToServer() {

		try {
			// create socket to connect to server
			socket = new Socket(serverIP, port);
			fromServer = new DataInputStream(socket.getInputStream());
			toServer = new DataOutputStream(socket.getOutputStream());
			objFromServer = new ObjectInputStream(socket.getInputStream());
			objToServer = new ObjectOutputStream(socket.getOutputStream());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// @@@ Actual game thread
		new Thread(() -> {
			try {
				setupPhase1UI();
				phase1 = true;
				while(phase1) {
					boolean round = true;
					currentBid = 0;
					me.setBid(0);
					yourBidPane.setBid(0);
					// reset cards offered
					offerCards();
					Platform.runLater(() -> {
						labelText = "Your current bid: $0 | Coins in hand: $" + me.getMoney() + " | Minimum bid: $" + (currentBid + 1);
						yourBidPane.getLabel().setText(labelText);
					});
					while(round) {
						currentPlayer = fromServer.readInt();							// read turn player number
						currentBid = fromServer.readInt();								// read current bid
						if (currentPlayer == me.getPlayerNumber()) {
							// update buttons
							Platform.runLater(() -> {
								actionPane.getChildren().clear();
								currentAction.setText("Your turn");
								actionPane.getChildren().addAll(currentAction, bidButton, passButton);
							});
						} else { // if not your turn, just update your label text
							Platform.runLater(() -> waiting());
						}
						Platform.runLater(() -> {
							labelText = "Your current bid: $" + bid + " | Coins in hand: $" + me.getMoney() + " | Minimum bid: $" + (currentBid + 1);
							yourBidPane.getLabel().setText(labelText);
						});
						playerBid = fromServer.readInt();
						handleBid();

						// done bidding?
						round = fromServer.readBoolean();
						// if round is over, take care of last card
						if (!round) {
							String text;
							int currentPlayer = fromServer.readInt();
							Card addedCard = pOffered.remove(0);
							if (currentPlayer == me.getPlayerNumber()) {
								text = nameMap.get(currentPlayer) + " (YOU) paid $" + Math.abs(playerBid);
								playerCard.add(addedCard);
								add = addedCard;
								Platform.runLater(() -> {
									waiting();
								});
							} else {
								text = nameMap.get(currentPlayer) + " paid $" + Math.abs(playerBid);
							}
							Thread.sleep(1000);
							Platform.runLater(() -> {
								propertyOffered.getHBox().getChildren().clear();
								labelPanes.get(currentPlayer).getLabel().setText(text);
								labelPanes.get(currentPlayer).getFlowPane().getChildren().clear();
								labelPanes.get(currentPlayer).getFlowPane().getChildren().add(addedCard.getImageView());
								waiting();
								labelText = "Your current bid: $" + 0 + " | Coins in hand: $" + me.getMoney() + " | Minimum bid: $" + (currentBid + 1);
								yourBidPane.getLabel().setText(labelText);
							});
							Thread.sleep(2500);
							Platform.runLater(() -> yourPropertyPane.getHBox().getChildren().add(add.getImageView()));
						}
						phase1 = fromServer.readBoolean();
						// if round is over, but still phase 1
						if (!round && phase1) {
							// reset player bid amounts
							for (int i = 1; i <= playerCount; i++) {
								int j = i;
								if (j == me.getPlayerNumber()) {
									Platform.runLater(() -> {
										labelPanes.get(j).getLabel().setText(nameMap.get(j) + " (YOU) bid $0");
									});
								} else {
									Platform.runLater(() -> labelPanes.get(j).getLabel().setText(nameMap.get(j) + " bid $0"));
								}
								Platform.runLater(() -> labelPanes.get(j).getFlowPane().getChildren().clear());
							}
							me.setBidding(true);
						}
					}							// round loop
				}								// phase loop
				// set up GUI
				Card moneyBack = ((Card) objFromServer.readObject());
				Platform.runLater(() -> {
					mainPane.getChildren().clear();
					deckPane.getHBox().getChildren().clear();
					deckPane.getLabel().setText("x" + remainingCards);
					moneyBack.imageViewBind(deckPane.getVBox().prefHeightProperty().divide(CARD_SCALE));
					deckPane.getHBox().getChildren().add(moneyBack.getImageView());
					// update pane heights
					actionPane.prefHeightProperty().bind(mainPane.heightProperty().divide(MAIN_PANE2 * 4));
					pane2.prefHeightProperty().bind(mainPane.heightProperty().divide(MAIN_PANE2));
					propertyOffered.getVBox().prefHeightProperty().bind(mainPane.heightProperty().divide(MAIN_PANE2));
					deckPane.getVBox().prefHeightProperty().bind(mainPane.heightProperty().divide(MAIN_PANE2));
					salePane.prefHeightProperty().bind(mainPane.heightProperty().divide(MAIN_PANE2));
					yourBidPane.getVBox().prefHeightProperty().bind(mainPane.heightProperty().divide(MAIN_PANE2 * 4/2));
					yourPropertyPane.getVBox().prefHeightProperty().bind(mainPane.heightProperty().divide(MAIN_PANE2));
					yourCurrencyPane.getVBox().prefHeightProperty().bind(mainPane.heightProperty().divide(MAIN_PANE2));	
					propertyOffered.getLabel().setText("Currency Offered");
					// salePane.setAlignment(Pos.CENTER);
					// salePane.prefHeightProperty().bind(mainPane.heightProperty().divide(5));
					// property for sale using salePane
					for (int i = 1; i <= playerCount; i++) {
						if (i == me.getPlayerNumber()) {
							labelPanes.get(i).getLabel().setText("YOUR property for sale");
						} else {
							labelPanes.get(i).getLabel().setText(nameMap.get(i) + "'s property for sale");
						}
						labelPanes.get(i).getVBox().prefHeightProperty().bind(mainPane.heightProperty().divide(MAIN_PANE2));
						labelPanes.get(i).getVBox().prefWidthProperty().bind(mainPane.widthProperty().divide(playerCount));
						labelPanes.get(i).getVBox().setAlignment(Pos.TOP_CENTER);
						labelPanes.get(i).getFlowPane().getChildren().clear();
						labelPanes.get(i).getFlowPane().setAlignment(Pos.CENTER);
						// salePane.getChildren().add(labelPanes.get(i).getVBox());
					}
					yourCurrencyPane.getVBox().prefHeightProperty().bind(mainPane.heightProperty().divide(MAIN_PANE2));
					yourCurrencyPane.getVBox().prefWidthProperty().bind(mainPane.widthProperty());
					mainPane.getChildren().addAll(actionPane, pane2, salePane, yourPropertyPane.getVBox(), yourCurrencyPane.getVBox());
				});
				/*
				Card moneyCard = ((Card) objFromServer.readObject());
				Image moneyImage = moneyCard.getCardImage();
				Background moneyBackground = new Background(new BackgroundImage(moneyImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.REPEAT,
						BackgroundPosition.CENTER, BackgroundSize.DEFAULT));
				 */
				int currencyValue = 0;
				remainingCards = cardCount;
				phase2 = true;
				while (phase2) {
					// update action button + remainingCards
					Platform.runLater(() -> {
						actionPane.getChildren().clear();
						currentAction.setText("Choose a property");
						actionPane.getChildren().addAll(currentAction, sellButton);
					});
					// make cards clickable
					for (Card c : playerCard) {
						c.enableClick();
						c.addDeck(playerCard);
					}
					offerCards();
					// receive currency value earned
					HashMap<Integer, Integer> placeToPlayerNum = new HashMap<Integer, Integer>();
					int place;
					// add house cards to players panes
					for (int i = 1; i <= playerCount; i++) {
						// read place, player num, and player property
						place = fromServer.readInt();
						int playerNum = fromServer.readInt();
						placeToPlayerNum.put(place, playerNum);
						int houseValue = fromServer.readInt();
						if (playerNum == me.getPlayerNumber()) {
							continue;
						}
						Platform.runLater(() -> {
							labelPanes.get(playerNum).getFlowPane().getChildren().add(allCards.get(houseValue).getImageView());
						});
					}
					// add currency
					for (int i = 1; i <= playerCount; i++) {
						int j = i;
						Card c = minCard(pOffered);
						System.out.println("place: " + i + "player: " + placeToPlayerNum.get(i) + " currency: " + c.getValue());
						pOffered.remove(c);
						if (placeToPlayerNum.get(i) == me.getPlayerNumber()) {
							playerCurrency.add(c.getValue());
							add = c;
						}
						Platform.runLater(() -> {
							labelPanes.get(placeToPlayerNum.get(j)).getFlowPane().getChildren().add(c.getStackPane());
						});
					}
					Thread.sleep(3000);
					Platform.runLater(() -> {
						yourCurrencyPane.getHBox().getChildren().add(add.getStackPane());
						propertyOffered.getHBox().getChildren().clear();
						pOffered.clear();
						for (int i = 0; i < playerCount; i++) {
							int j = i;
							Platform.runLater(() ->	labelPanes.get(j + 1).getFlowPane().getChildren().clear());
						}
					});
					phase2 = fromServer.readBoolean();
				}
				// end game
				currencyValue = 0;
				for (int i : playerCurrency) {
					currencyValue += i;
				}
				toServer.writeInt(me.getMoney());
				toServer.writeInt(currencyValue);
				// property for sale using salePane
				for (int i = 1; i <= playerCount; i++) {
					int place = fromServer.readInt();
					String placement = placeToString(place);
					int playerNumber = fromServer.readInt();
					int playerScore = fromServer.readInt();
					int playerMoney = fromServer.readInt();
					int j = i;
					Platform.runLater(() -> {
						String text;
						labelPanes.get(j).getVBox().getChildren().clear();
						if (playerNumber == me.getPlayerNumber()) {
							if (place == 1) {
								text = "YOU WIN!" + "\n" + "Total Score: " + playerScore + "\n" + "Total Money: " + playerMoney;
							} else {
								text = "YOU got " + placement + "!\n" + "Total Score: " + playerScore + "\n" + "Total Money: " + playerMoney;
							}
							labelPanes.get(j).getLabel().setText(text);
							currentAction.setText(text);
							currentAction.setFont(FONT20);
							currentAction.setTextFill(Color.BLACK);
						} else {
							labelPanes.get(playerNumber).getLabel().setText(nameMap.get(playerNumber) + " got " + placement + "!\n" + "Total Score: " + playerScore + "\n" + "Total Money: " + playerMoney);
						}
						labelPanes.get(j).getVBox().getChildren().add(labelPanes.get(j).getLabel());
						labelPanes.get(j).getVBox().setAlignment(Pos.CENTER);
					});

				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}).start();
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

	private void repaintCoin(int coins, FlowPane flowPane, boolean me) {
		flowPane.getChildren().clear();
		if (me) {
			playerMoney.clear();
			for (int i = 0; i < coins; i++) {
				Coin c = new Coin(yourBidPane, coinImage, true);
				playerMoney.add(c);
				flowPane.getChildren().add(c.getImageView());
				c.getImageView().fitWidthProperty().bind(mainPane.heightProperty().divide(MAIN_PANE * COIN_DIVIDE));
			}
		} else {
			for (int i = 0; i < coins; i++) {
				Coin c = new Coin(yourBidPane, coinImage);
				c.getImageView().fitWidthProperty().bind(mainPane.heightProperty().divide(MAIN_PANE * COIN_DIVIDE));
				flowPane.getChildren().add(c.getImageView());
			}
		}
	}
	private void waiting() {
		actionPane.getChildren().clear();
		currentAction.setText("Waiting for other players...");
		actionPane.getChildren().add(currentAction);
	}
	private String placeToString(int i) {
		switch (i) {
		case 1:
			return "1st";
		case 2:
			return "2nd";
		case 3:
			return "3rd";
		case 4:
			return "4th";
		case 5:
			return "5th";
		case 6:
			return "6th";
		default:
			return "failed";
		}
	}
	
	private void setupPhase1UI() throws IOException, ClassNotFoundException {
		objToServer.writeObject(playerName);							// send player name
		// set up images for game
		// house back card
		Card houseBackCard = (Card) objFromServer.readObject();
		houseBackCard.imageViewBind(deckPane.getVBox().prefHeightProperty().divide(CARD_SCALE));
		// set up main UI
		Platform.runLater(() -> {
			mainPane.getChildren().addAll(pane2, salePane, yourBidPane.getVBox(), yourPropertyPane.getVBox());
			deckPane.getHBox().getChildren().add(houseBackCard.getImageView());
		});
		// set up background image
		Image background = ((Card) objFromServer.readObject()).getCardImage();
		Background backgroundImage = new Background(new BackgroundImage(background, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
				BackgroundPosition.CENTER, BackgroundSize.DEFAULT));
		mainPane.setBackground(backgroundImage);
		coinImage = ((Card) objFromServer.readObject()).getCardImage();	// read coin object
		// populate player + money
		me = (Player) objFromServer.readObject();						// read this Player object from server
		System.out.println("Player: " + me.getPlayerNumber());			// @WRITE@
		Platform.runLater(() -> repaintCoin(me.getMoney(), yourBidPane.getFlowPane(), true));
		// populate remaining Cards
		cardCount = fromServer.readInt();								// read remaining cards from server; reused for currency deck
		remainingCards = cardCount;
		// set up player count + bid tracker
		playerCount = fromServer.readInt();								// read playerCount from server
		// create player panes
		for (int i = 1; i <= playerCount; i++) {
			int j = i;
			playerName =  (String) objFromServer.readObject();
			nameMap.put((j), playerName);
			Platform.runLater(() -> {
				if (j == me.getPlayerNumber()) {
					labelPanes.put(j, new LabelPane(playerName + " (YOU) bid $0"));
				} else {
					labelPanes.put(j, new LabelPane(playerName + " bid $0"));
				}
				salePane.getChildren().add(labelPanes.get(j).getVBox());
				labelPanes.get(j).getLabel().setPadding(PADDING5);
				playerPaneSetup(labelPanes.get(j).getVBox());
			});
		}
		setupButtons();
	}
	
	private void setupButtons() {
		bidButton.setOnAction(e -> {
			bid = me.getBid();
			for (Coin c : playerMoney) {
				if (c.isSelected()) {
					bid++;
				}
			}
			if (bid > currentBid) {
				me.setMoney(me.getMoney() - bid);
				try {
					toServer.writeInt(bid);
					Platform.runLater(() -> {
						repaintCoin(me.getMoney(), yourBidPane.getFlowPane(), true);
						waiting();
					});
					me.setMyTurn(false);
					me.setBid(bid);
					yourBidPane.setBid(bid);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else { 
				bid = me.getBid();
				Platform.runLater(() ->
				currentAction.setText("Your bid must be greater than $" + currentBid));
			}
		});
		// Pass button setup
		passButton.setOnAction(e -> {
			try {
				if (bid > 0) {
					toServer.writeInt(bid * -1);
				} else {
					toServer.writeInt(bid);
				}
				me.setMyTurn(false);
				me.setBidding(false);
				// return half the coins
				me.setMoney(me.getMoney() + (bid / 2));
				Platform.runLater(() -> {
				if(bid > 0) {
					repaintCoin(me.getMoney(), yourBidPane.getFlowPane(), true);
				}
				waiting();
				labelText = "Your current bid: $" + 0 + " | Coins in hand: $" + me.getMoney() + " | Minimum bid: $" + (currentBid + 1);
				});
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		sellButton.setOnAction(e -> {
			try {
				int selectedCount = 0;
				for (Card c : playerCard) {
					if (c.isSelected()) {
						selectedCount++;
						selectedCard = c;
					}
				}
				if (selectedCount == 1) {
					toServer.writeInt(selectedCard.getValue());
					playerCard.remove(selectedCard);
					for (Card c : playerCard) {
						c.disableClick();
					}
					Platform.runLater(() -> {
						waiting();
						labelPanes.get(me.getPlayerNumber()).getFlowPane().getChildren().add(selectedCard.getImageView());
						yourPropertyPane.getHBox().getChildren().clear();
						for (Card c : playerCard) {
							yourPropertyPane.getHBox().getChildren().add(c.getImageView());
						}
					});
				} else {
					Platform.runLater(() ->
					currentAction.setText("Select one property "));
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
	}
	private void playerPaneSetup(VBox vbox) {
		vbox.prefHeightProperty().bind(mainPane.heightProperty().divide(MAIN_PANE));
		vbox.prefWidthProperty().bind(mainPane.widthProperty().divide(playerCount));
		vbox.setAlignment(Pos.TOP_CENTER);
		vbox.setSpacing(COIN_SPACING);
	}
	
	private void offerCards() throws ClassNotFoundException, IOException {
		remainingCards -= playerCount;
		for (int i = 1; i <= playerCount; i++) {
			Card c = (Card) objFromServer.readObject();
			pOffered.add(c);
			c.imageViewBind(propertyOffered.getVBox().prefHeightProperty().divide(CARD_SCALE));
			if (phase1) {
				Platform.runLater(() -> propertyOffered.getHBox().getChildren().add(c.getImageView()));
				allCards.put(c.getValue(), c);
			} else if (phase2) {
				Platform.runLater(() -> {
				c.currencyBox();
				c.getStackPane().getChildren().addAll(c.getImageView(), c.getLabel());
				propertyOffered.getHBox().getChildren().add(c.getStackPane());
				});
			}
		}
		Platform.runLater(() -> deckPane.getLabel().setText("x" + remainingCards));
	}
	
	private void handleBid() {
		String text;
		int currentPlayer = this.currentPlayer;
		if (playerBid > 0) {
			String updateText = labelPanes.get(currentPlayer).getLabel().getText();
			text = updateText.substring(0, updateText.indexOf('$'));
			Platform.runLater(() -> {
				labelPanes.get(currentPlayer).getLabel().setText(text + "$" + playerBid);
				repaintCoin(playerBid, labelPanes.get(currentPlayer).getFlowPane(), false);
			});
		} else if (playerBid <= 0) {
			Card addedCard = minCard(pOffered);
			pOffered.remove(addedCard);
			if (currentPlayer == me.getPlayerNumber()) {
				text = nameMap.get(currentPlayer) + " (YOU) paid $" + ((Math.abs(playerBid) + 1) / 2);
				playerCard.add(addedCard);
				add = addedCard;
			} else {
				text = nameMap.get(currentPlayer) + " paid $" + ((Math.abs(playerBid) + 1) / 2);
			}
			Platform.runLater(() -> {
				labelPanes.get(currentPlayer).getLabel().setText(text);
				labelPanes.get(currentPlayer).getFlowPane().getChildren().clear();
				labelPanes.get(currentPlayer).getFlowPane().getChildren().add(addedCard.getImageView());
			});
			// repaint cards
			Platform.runLater(() -> propertyOffered.getHBox().getChildren().clear());
			for (Card c : pOffered) {
				Platform.runLater(() -> propertyOffered.getHBox().getChildren().add(c.getImageView()));
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
