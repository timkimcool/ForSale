package com.forsale;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public interface ForSaleConstants {
	public static int PORT = 5000;
	public static String HOST = "localhost";
	public static String RESOURCE_PATH = "/resources/";
	public static String PANE_COLOR = "-fx-background-color: rgba(234, 230, 220, 0.5)";
	public static Font FONT16 = Font.font("Helvetica", FontWeight.BOLD, 16); 
	public static Font FONT20 = Font.font("Helvetica", FontWeight.BOLD, 20);
	// top, right, bottom, left
	public static Insets PADDING = new Insets(10, 5, 5, 5);
	public static Insets PADDING5 = new Insets(5, 5, 0, 5);
	public static Insets PADDING2 = new Insets(20, 20, 20, 20);
	public static int SPACING = 5;
	public static double CARD_WIDTH = 295;
	public static double CARD_HEIGHT = 420;
	public static double COIN_WIDTH = 60;
	public static double CARD_SCALE = 1.35;			// bigger = smaller card
	public static double MAIN_PANE = 120/29;
	public static double MAIN_PANE2 = 480/104.75;
	public static double COIN_DIVIDE = 4.3;			// bigger = smaller coin
	public static double COIN_SPACING = 7.5;
	public static final String IP_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
											"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +	"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
}
