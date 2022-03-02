package com.forsale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ForSaleGame extends Application implements ForSaleConstants{
	public static void main(String[] args) {
			//Application.launch(ForSaleServer.class, args);
			launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		StackPane mainPane = new StackPane();
		Image forSaleImage = new Image(this.getClass().getResourceAsStream("/resources/BoxLogo.png"));
		ImageView forSaleImageView = new ImageView(forSaleImage);
		forSaleImageView.fitWidthProperty().bind(mainPane.widthProperty());
		forSaleImageView.fitHeightProperty().bind(mainPane.heightProperty());
		VBox mainControlPane = new VBox();
		mainControlPane.setAlignment(Pos.CENTER);
		mainControlPane.setSpacing(SPACING);
		/*
		Background moneyBackground = new Background(new BackgroundImage(forSaleImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.CENTER, BackgroundSize.DEFAULT));
		mainPane.setSpacing(SPACING);
		mainPane.setBackground(moneyBackground);
		mainPane.setAlignment(Pos.BOTTOM_CENTER);
		*/
		Label text = new Label();
		text.setAlignment(Pos.CENTER);
		text.setText("Create or Join?");
		text.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
		text.setBackground(new Background(new BackgroundFill(Color.rgb(234, 230, 220, 0.8), CornerRadii.EMPTY, Insets.EMPTY)));
		HBox hbox = new HBox();
		hbox.setSpacing(SPACING);
		hbox.setAlignment(Pos.CENTER);
		Button serverButton = new Button("Create");	
		Button submitButton = new Button("Submit");
		submitButton.setDefaultButton(true);
		TextField userInput = new TextField();
		userInput.requestFocus();
		userInput.maxWidthProperty().bind(mainPane.prefWidthProperty().divide(2));
		serverButton.setOnAction(e -> {
			text.setText("Enter port number: ");
			submitButton.setOnAction(e4 -> {
				String port = userInput.getText();
				try { 
					int portNumber = Integer.parseInt(port);
					if (portNumber <= 65535 && portNumber >= 1025) {
						try {
							new ForSaleServer().start(primaryStage, port);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					} else {
						text.setText("Enter a number between 1025-65535");
						userInput.requestFocus();
					}
				} catch (NumberFormatException a) {
					text.setText("Enter a number between 1025-65535");
					userInput.requestFocus();
				}
			});
			mainControlPane.getChildren().clear();
			mainControlPane.getChildren().addAll(text, userInput, submitButton);
		});
		Button clientButton = new Button("Join");
		clientButton.setOnAction(e -> {
			text.setText("Enter your name: ");
			submitButton.setOnAction(e1 -> {
				String playerName = userInput.getText();
				Pattern pattern = Pattern.compile(IP_PATTERN);
				userInput.clear();
				text.setText("Enter server IP address: ");
				userInput.requestFocus();
				submitButton.setOnAction(e3 -> {
					String serverIP = userInput.getText();
					Matcher matcher = pattern.matcher(serverIP);
					if (matcher.matches()) {
						userInput.clear();
						text.setText("Enter port number: ");
						userInput.requestFocus();
						submitButton.setOnAction(e4 -> {
							String port = userInput.getText();
							try { 
								int portNumber = Integer.parseInt(port);
								if (portNumber <= 65535 && portNumber >= 1025) {
									try {
										new ForSaleClient().start(primaryStage, playerName, serverIP, port);
									} catch (Exception e2) {
										e2.printStackTrace();
									}
								} else {
									text.setText("Enter a number between 1025-65535");
									userInput.requestFocus();
								}
							} catch (NumberFormatException a) {
								text.setText("Enter a number between 1025-65535");
								userInput.requestFocus();
							}
						});
					} else {
						text.setText("Invalid IP address" + "\n" + "Please try again");
						userInput.requestFocus();
					}

				});
			});
			mainControlPane.getChildren().clear();
			mainControlPane.getChildren().addAll(text, userInput, submitButton);
		});
		hbox.getChildren().addAll(serverButton, clientButton);
		mainPane.setPrefSize(700, 400);
		mainControlPane.getChildren().addAll(text, hbox);
		mainPane.getChildren().addAll(forSaleImageView, mainControlPane);
		mainPane.setAlignment(Pos.CENTER);
		primaryStage.setTitle("ForSaleGame");
		Scene scene = new Scene(mainPane);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
	}
}
