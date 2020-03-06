import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ForSaleClientUI extends Application implements ForSaleConstants{
	private String panelColor = "-fx-background-color: rgba(234, 230, 220, 0.5)";
	private Font font = Font.font("Helvetica", FontWeight.BOLD,16); 
	private Insets padding = new Insets(15, 15, 15, 15);
	private int spacing = 15;
	private double cardWidth = 295/2;
	private double cardHeight = 420/2;
	private static Stage primaryStage;
	
	@Override
	public void start(Stage primaryStage) throws Exception {	
		VBox mainPane = new VBox();
		
		HBox actionPane = new HBox();
		actionPane.setAlignment(Pos.CENTER);
		actionPane.setSpacing(spacing);
		Label currentAction = new Label("You must select an action");
		currentAction.setFont(FONT16);
		Button bidButton = new Button("Bid");
		// bidButton.setStyle("-fx-border-color:grey; -fx-border-width: .5; -fx-border-radius: 10; -fx-background-color: skyblue; "
		//		+ "-fx-background-radius: 10; -fx-font: 16 helvitica; -fx-font-weight: bold; -fx-text-fill: white");
		// bidButton.setStyle("-fx-font: 12 helvitica;");
		Button passButton = new Button("Pass");
		actionPane.getChildren().addAll(currentAction, bidButton, passButton);
		actionPane.setPadding(padding);
		actionPane.setStyle("-fx-background-color: rgba(234, 230, 220, 1)");	
		
		LabelPane propertyOffered = new LabelPane("Property Offered", 1000, 225);
		propertyOffered.getHBox().getChildren().addAll(new Card("house1.png").getImageView(), new Card("house2.png").getImageView());
		LabelPane deckPane = new LabelPane("x30", 200, 225);
		deckPane.getHBox().setAlignment(Pos.TOP_CENTER);
		deckPane.getHBox().getChildren().add(new Card("houseBack.png").getImageView());
		deckPane.setFont(FONT20);
		deckPane.getVBox().setAlignment(Pos.CENTER);
		HBox pane2 = new HBox();
		pane2.setSpacing(15);
		pane2.setPadding(padding);
		pane2.getChildren().addAll(propertyOffered.getVBox(), deckPane.getVBox());
		
		GridPane playerBidPane = new GridPane();
		LabelPane p1Pane = new LabelPane("Player 1 bid $0", 600, 80);
		LabelPane p2Pane = new LabelPane("Player 2 bid $0", 600, 80);
		LabelPane p3Pane = new LabelPane("Player 3 bid $0", 600, 80);
		LabelPane p4Pane = new LabelPane("Player 4 bid $0", 600, 80);
		ImageView coinView = new Coin().getImageView();
		ImageView coinView2 = new Coin().getImageView();
		ImageView coinView3 = new Coin().getImageView();
		ImageView coinView4 = new Coin().getImageView();
		p1Pane.getHBox().getChildren().add(coinView);
		p2Pane.getHBox().getChildren().addAll(coinView2);
		p4Pane.getHBox().getChildren().addAll(coinView3, coinView4);
		playerBidPane.setHgap(spacing);
		playerBidPane.setVgap(spacing);
		playerBidPane.add(p1Pane.getVBox(), 0, 0);
		playerBidPane.add(p2Pane.getVBox(), 0, 1);
		playerBidPane.add(p3Pane.getVBox(), 1, 0);
		playerBidPane.add(p4Pane.getVBox(), 1, 1);
		
		LabelPane yourBidPane = new LabelPane("Your current bid: $3 | Your previous bid: $1 | Coins in hand: | Minimum bid: $5", 1000, 80);
		ImageView coinView5 = new Coin().getImageView();
		yourBidPane.getHBox().getChildren().add(coinView5);
		
		LabelPane yourPropertyPane = new LabelPane("Your Property Cards", 1000, 225);
		yourPropertyPane.getHBox().getChildren().addAll(new Card("house4.png").getImageView(), new Card("house5.png").getImageView());
		
		mainPane.getChildren().addAll(actionPane, pane2, playerBidPane, yourBidPane.getVBox(), yourPropertyPane.getVBox());
		Image background = new Image("file:/D:/CS/Eclipse/workspace/ForSale/resources/background.png");
		Background backgroundImage = new Background(new BackgroundImage(background, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.CENTER, BackgroundSize.DEFAULT));
		mainPane.setBackground(backgroundImage);
		mainPane.setPadding(padding);
		mainPane.setSpacing(spacing);
		
		Scene scene = new Scene(mainPane);
		primaryStage.setTitle("ForSaleTest");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
	}
	
	/**
	 * 	main method for IDE
	 */
	public static void main(String[] args) {
		Application.launch(args);;
	}

}
