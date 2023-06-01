import java.util.Optional;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.util.Pair;

public class AlertWithFields extends Application {
	private Stage stage;
	private String name;
	private int time;
	private Boolean cancel = false;
	@Override
	public void start(Stage stage){
		stage.show();
		
		name = "";
		time = 0;
		
		try {
			ConnectionAlert connectionAlert = new ConnectionAlert();
			Optional<ButtonType> result = connectionAlert.showAndWait();

			if(result.get() == ButtonType.OK) {
				name = connectionAlert.getName();
				time = connectionAlert.getAmount();
			}
			else {
				cancel = true;
				return;
			}
			
		}catch(NumberFormatException e) {
			Alert a = new Alert(Alert.AlertType.ERROR, "Fel inmatning!");
			a.showAndWait();
			return;
		}
	}


	public Pair<String, Integer> getAlert(Stage stage) {
		this.start(stage);
		if(cancel) {
			return null;
		}
		return new Pair<String, Integer>(name, time);
	}

}
