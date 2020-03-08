import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ForSaleClient extends Application implements ForSaleConstants{
	private boolean myTurn = false; 			// turn tracker
	// @@@ card array
	private Label lblTitle = new Label();		// create + initialize title label
	private Label lblStatus = new Label();		// create + initialize status label
	// @@@ GUI variables
	private DataInputStream fromServer;			// input and output stream from/to server
	private DataOutputStream toServer;
	private ObjectInputStream objFromServer;			// input and output stream from/to server
	private ObjectOutputStream objToServer;
	private Socket socket;
	private String labelText;
	private boolean continueToPlay = true;		// continue?
	private boolean waiting = true;				// wait for player to mark cell
	// private String host = HOST;					// host name or ip set in ForSaleConstants
	
	// Variables from server
	private Player me;
	private ArrayList<Card> pOffered = new ArrayList<Card>();
	private ArrayList<Coin> playerMoney = new ArrayList<Coin>();		// don't need?
	private int playerCount;
	private int remainingCards;
	private Image coinImage;
	
	// Fields to update
	private Label currentAction;
	// > Panes
	private LabelPane propertyOffered;
	private LabelPane deckPane;					// remaining cards
	private LabelPane yourBidPane;
	private int currentBid;
	private int previousBid;
	private int minBid;
	private LabelPane yourPropertyPane;
	private GridPane playerBidPane;
	private HBox actionPane;
	private ArrayList<LabelPane> playerPanes = new ArrayList<LabelPane>();
	private VBox mainPane;
	// Game State
	private boolean phase1;
	private boolean phase2;
	private HashMap<Integer, LabelPane> labelPanes = new HashMap<Integer, LabelPane>();
	@Override
	public void start(Stage primaryStage) throws Exception {
		// @@@ Create UI to hold interface
		mainPane = new VBox();
		
		//
		// Current action
		//
		actionPane = new HBox();
		actionPane.setAlignment(Pos.CENTER);
		actionPane.setSpacing(SPACING);
		currentAction = new Label("Waiting for players...");
		currentAction.setFont(FONT16);
		// Button bidButton = new Button("Bid");
		// Button passButton = new Button("Pass");
		// actionPane.getChildren().addAll(currentAction, bidButton, passButton);
		actionPane.getChildren().add(currentAction);
		actionPane.setPadding(PADDING);
		actionPane.setStyle("-fx-background-color: rgba(234, 230, 220, 1)");	
		//
		// Property Offered + Remaining cards
		//
		propertyOffered = new LabelPane("Property Offered", 1000, 225);
		deckPane = new LabelPane("x30", 200, 225);
		deckPane.getHBox().setAlignment(Pos.TOP_CENTER);
		deckPane.setFont(FONT20);
		deckPane.getVBox().setAlignment(Pos.CENTER);
		HBox pane2 = new HBox();
		pane2.setSpacing(15);
		pane2.setPadding(PADDING);
		pane2.getChildren().addAll(propertyOffered.getVBox(), deckPane.getVBox());
		//
		// Total players
		//
		playerBidPane = new GridPane();		
		playerBidPane.setHgap(SPACING);
		playerBidPane.setVgap(SPACING);
		//
		// Your Bid
		//
		yourBidPane = new LabelPane("Your current bid: $0 | Your previous bid: $0 | Coins in hand: 0 | Minimum bid: $0", 1000, 80);
		//
		// Your Property Cards
		//
		yourPropertyPane = new LabelPane("Your Property Cards", 1000, 225);
		//
		// Put it all together
		//
		mainPane.getChildren().addAll(actionPane, pane2, playerBidPane, yourBidPane.getVBox(), yourPropertyPane.getVBox());
		mainPane.setPadding(PADDING);
		mainPane.setSpacing(SPACING);
		Scene scene = new Scene(mainPane);
		primaryStage.setTitle("ForSale Client");
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
		// move variables down from main class into here??
		
		try {
			// create socket to connect to server
			socket = new Socket(HOST, PORT);
			fromServer = new DataInputStream(socket.getInputStream());
			toServer = new DataOutputStream(socket.getOutputStream());
			objFromServer = new ObjectInputStream(socket.getInputStream());
			objToServer = new ObjectOutputStream(socket.getOutputStream());
			// populate images
			deckPane.getHBox().getChildren().add(((Card) objFromServer.readObject()).getImageView());
			Image background = ((Card) objFromServer.readObject()).getCardImage();
			Background backgroundImage = new Background(new BackgroundImage(background, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
					BackgroundPosition.CENTER, BackgroundSize.DEFAULT));
			mainPane.setBackground(backgroundImage);
			coinImage = ((Card) objFromServer.readObject()).getCardImage();
			// populate player + money
			me = (Player) objFromServer.readObject();						// read Player object from server
			int playerNumber = me.getPlayerNumber();
			labelText = "Your current bid: $0 | Your previous bid: $0 | Coins in hand: $" + me.getMoney() + " | Minimum bid: $0";
			yourBidPane.getLabel().setText(labelText);
			repaintCoin(me.getMoney(), yourBidPane.getHBox(), true);
			// populate remaining Cards
			remainingCards = fromServer.readInt();							// read remaining cards from server
			deckPane.getLabel().setText("x" + remainingCards);
			
			playerCount = fromServer.readInt();								// read playerCount from server
			for (int i = 0; i < playerCount; i++) {
				if (i + 1 == playerNumber) {
					labelPanes.put(i + 1, new LabelPane("Player " + (i + 1) + " (YOU) bid $0", 600, 80));
				} else {
					labelPanes.put(i + 1, new LabelPane("Player " + (i + 1) + " bid $0", 600, 80));
				}

				playerBidPane.add(labelPanes.get(i + 1).getVBox(), i / 2, i % 2);
			// Button bidButton = new Button("Bid");
			// Button passButton = new Button("Pass");
			// actionPane.getChildren().addAll(currentAction, bidButton, passButton);
			// actionPane.getChildren().add(currentAction);
			}
			// populate Property Offered
			// pOffered = (ArrayList<Card>) objFromServer.readObject();		// read from server
			for (int i = 0; i < playerCount; i++) {
				Card c = (Card) objFromServer.readObject();
				propertyOffered.getHBox().getChildren().add(c.getImageView());
			}
			phase1 = fromServer.readBoolean();					// read current phase
		} catch (Exception ex) {
				ex.printStackTrace();
		}
		
		// @@@ Actual game thread
		new Thread(() -> {
			try {
				while(phase1) {
					int updatePlayer = fromServer.readInt();					// read turn player number
					System.out.println(updatePlayer + " " + me.getPlayerNumber());
					if (updatePlayer == me.getPlayerNumber()) {
						me = ((Player) (objFromServer.readUnshared()));					// read player object from server
					} else {
						Player dummy = ((Player) (objFromServer.readObject()));
					}
					currentBid = fromServer.readInt();							// read current bid from server
					
					String updateText = labelPanes.get(updatePlayer).getLabel().getText();
					String text = updateText.substring(0, updateText.indexOf('$') - 1);
					System.out.println(me.isBidding() + " " + me.isMyTurn() + " " + currentBid + " " + me.getPlayerNumber() + " " + updatePlayer);
					Platform.runLater(() -> {
						labelPanes.get(updatePlayer).getLabel().setText(text + currentBid);
						repaintCoin(currentBid, labelPanes.get(updatePlayer).getHBox(), false);
					});
					if (me.isMyTurn()) {
						// update buttons
						Platform.runLater(() ->
							actionPane.getChildren().clear());
						Button bidButton = new Button("Bid");
						Button passButton = new Button("Pass");
						Platform.runLater(() ->
							currentAction.setText("Your turn"));
						bidButton.setOnAction(e -> {
							int bid = 0;
							for (Coin c : playerMoney) {
								if (c.isSelected()) {
									bid++;
								}
							}
							if (bid > currentBid) {
								me.setMoney(me.getMoney() - bid);
								try {
									toServer.writeInt(bid);
									System.out.println(bid + " " + me.getMoney());
									Platform.runLater(() -> {
										repaintCoin(me.getMoney(), yourBidPane.getHBox(), true);
										waiting();
									});
									me.setMyTurn(false);
									System.out.println("client 1");
									objToServer.writeObject(me);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							} else { 
								Platform.runLater(() ->
									currentAction.setText("Your bid must be greater than $" + currentBid));
							}
						});
						passButton.setOnAction(e -> {
							try {
								toServer.writeInt(0);
								Platform.runLater(() ->
									waiting());
								me.setMyTurn(false);
								objToServer.writeObject(me);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						});
						System.out.println("my money: " + me.getMoney());
						Platform.runLater(() -> {
							actionPane.getChildren().addAll(currentAction, bidButton, passButton);
							labelText = "Your current bid: $0 | Your previous bid: $0 | Coins in hand: $" + me.getMoney() + " | Minimum bid: $" + (currentBid + 1);
							yourBidPane.getLabel().setText(labelText);
						});
					} else {
						Platform.runLater(() -> {
							waiting();
							// waitForPlayerAction(); ???
							labelText = "Your current bid: $0 | Your previous bid: $0 | Coins in hand: $" + me.getMoney() + " | Minimum bid: $" + (currentBid + 1);
							yourBidPane.getLabel().setText(labelText);
						});
					}
					
				}								// while loop
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}).start();
	}
	
	private void waitForPlayerAction() throws InterruptedException {
		while (waiting) {
			Thread.sleep(100);
		}
		waiting = true;
	}
	
	private void repaintCoin(int coins, HBox hBox, boolean me) {
		hBox.getChildren().clear();
		if (me) {
			for (int i = 0; i < coins; i++) {
				playerMoney.add(new Coin(yourBidPane, coinImage, true));
			}
			for (Coin c : playerMoney) {
				hBox.getChildren().add(c.getImageView());
			}
		} else {
			for (int i = 0; i < coins; i++) {
				hBox.getChildren().add(new Coin(yourBidPane, coinImage).getImageView());
			}
		}
	}
	private void waiting() {
		actionPane.getChildren().clear();
		currentAction.setText("Waiting for other players...");
		actionPane.getChildren().add(currentAction);
	}
	/**
	 * Main method for IDE usage only; not needed from command line
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
}
