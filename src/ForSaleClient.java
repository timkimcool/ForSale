import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

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
	// Game State
	private boolean phase1;
	private boolean phase2;

	@Override
	public void start(Stage primaryStage) throws Exception {
		// @@@ Create UI to hold interface
		VBox mainPane = new VBox();
		
		//
		// Current action
		//
		HBox actionPane = new HBox();
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
		deckPane.getHBox().getChildren().add(new Card("houseBack.png").getImageView());
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
		Image background = new Image("file:/D:/CS/Eclipse/workspace/ForSale/resources/background.png");
		Background backgroundImage = new Background(new BackgroundImage(background, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.CENTER, BackgroundSize.DEFAULT));
		mainPane.setBackground(backgroundImage);
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
	
	@SuppressWarnings("unchecked")
	private void connectToServer() {
		// move variables down from main class into here??
		
		try {
			// create socket to connect to server
			socket = new Socket(HOST, PORT);
			fromServer = new DataInputStream(socket.getInputStream());
			toServer = new DataOutputStream(socket.getOutputStream());
			objFromServer = new ObjectInputStream(socket.getInputStream());
			objToServer = new ObjectOutputStream(socket.getOutputStream());
			// populate player + money
			me = (Player) objFromServer.readObject();						// read from server
			int money = me.getMoney();
			int playerNumber = me.getPlayerNumber();
			labelText = "Your current bid: $0 | Your previous bid: $0 | Coins in hand: $" + money +" | Minimum bid: $0";
			yourBidPane.getLabel().setText(labelText);
			for (int i = 0; i < money; i++) {
				yourBidPane.getHBox().getChildren().add(new Coin().getImageView());
			}
			// populate remaining Cards
			remainingCards = fromServer.readInt();
			String labelText2 = "x" + remainingCards;
			deckPane.getLabel().setText(labelText2);
			
			playerCount = fromServer.readInt();								// read from server
			for (int i[] = {0}; i[0] < playerCount; i[0]++) {
				if (i[0] + 1 == playerNumber) {
					playerPanes.add(new LabelPane("Player " + (i[0] + 1) + " (YOU) bid $0", 600, 80));
				} else {
					playerPanes.add(new LabelPane("Player " + (i[0] + 1) + " bid $0", 600, 80));
					
				}

				playerBidPane.add(playerPanes.get(i[0]).getVBox(), i[0] / 2, i[0] % 2);
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
		} catch (Exception ex) {
				ex.printStackTrace();
		}
		
		// @@@ Actual game thread
		new Thread(() -> {
			try {
				boolean phase1 = fromServer.readBoolean();
				while(phase1) {
					me.setMyTurn(fromServer.readBoolean());
					// update buttons
					Button bidButton = new Button("Bid");
					Button passButton = new Button("Pass");
					currentAction.setText("Your turn");
					actionPane.getChildren().addAll(currentAction, bidButton, passButton);
					currentBid = fromServer.readInt();
				}
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
	
	/**
	 * Main method for IDE usage only; not needed from command line
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
}
