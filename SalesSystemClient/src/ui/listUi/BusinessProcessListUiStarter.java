package ui.listUi;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.uiAssistants.StageFactory;
import ui.uiAssistants.StageSize;

/**
 * 经营历程表界面
 * @author guxinyu
 * @version 2017.12.11
 *
 */
public class BusinessProcessListUiStarter {
	private Stage stage=StageFactory.getFinanceStage();
	
	public void businessProcessListUi(){
		URL location = getClass().getResource("BusinessProcessListUi.fxml");
		FXMLLoader loader=new FXMLLoader();
		loader.setLocation(location);
	    loader.setBuilderFactory(new JavaFXBuilderFactory());
		Parent root=null;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		Scene scene=new Scene(root, StageSize.STAGE_WIDTH, StageSize.STAGE_HEIGHT);
		stage.setScene(scene);
		
		BusinessProcessListUiController controller=loader.getController();
		controller.init();
	
		stage.show();
	}
}
