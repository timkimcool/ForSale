package com.forsale;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class LabelPane implements ForSaleConstants {
	private VBox VBoxPane;
	private Label label;
	private HBox HBoxPane;
	private FlowPane flowPane;
	private int bid;
	
	public LabelPane(String label, int width, int height) {
		this.label = new Label(label);
		this.label.setFont(FONT16);
		
		this.HBoxPane = new HBox();
		this.HBoxPane.setSpacing(SPACING);
		this.HBoxPane.setPrefSize(width, height);
		
		VBoxPane = new VBox();
		VBoxPane.getChildren().addAll(this.label, this.HBoxPane);
		VBoxPane.setStyle(PANE_COLOR);	
	}
	
	public LabelPane(String label) {
		this.label = new Label(label);
		this.label.setFont(FONT16);
		
		this.flowPane = new FlowPane();
		this.flowPane.setHgap(COIN_SPACING);
		this.flowPane.setAlignment(Pos.CENTER);
		
		VBoxPane = new VBox();
		VBoxPane.getChildren().addAll(this.label, this.flowPane);
		VBoxPane.setStyle(PANE_COLOR);	
	}
	
	public VBox getVBox() {
		return this.VBoxPane;
	}
	
	public HBox getHBox() {
		return this.HBoxPane;
	}
	
	public FlowPane getFlowPane() {
		return this.flowPane;
	}
	
	public void setFont(Font font) {
		this.label.setFont(font);
	}

	public Label getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label.setText(label);
	}
	
	public void repaint() {
		VBoxPane.getChildren().clear();
		VBoxPane.getChildren().addAll(this.label, this.HBoxPane);
	}

	public int getBid() {
		return bid;
	}

	public void setBid(int bid) {
		this.bid = bid;
	}
	
	
}
