import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class ConnectionAlert extends Alert{
	
	private TextField nameField = new TextField();
	private TextField timeField = new TextField();
	private GridPane grid;
	public ConnectionAlert() {
		super(AlertType.CONFIRMATION);
		grid = new GridPane();
		grid.addRow(0, new Label("Name:"), nameField);
		grid.addRow(1, new Label("Time:"), timeField);
		getDialogPane().setContent(grid);
	}
	
	public String getName() {
		return nameField.getText();
	}
	public int getAmount() {
		return Integer.parseInt(timeField.getText());
	}
	
	public GridPane getGrid() {
		return grid;
	}
}
