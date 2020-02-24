package main;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
/**
 * @author  Tino Peltola
 * This Class controls the GUI and the program with one another.
 */
public class DashboardController extends Application {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtTicket;

    @FXML
    private TextField txtValue;

    @FXML
    private TextField txtVAR;

    @FXML
    private Button btnAdd;

    @FXML
    private TableView<Asset> tbvAssets;

    @FXML
    private RadioButton rdb95;

    @FXML
    private ToggleGroup confidenceToggles;

    @FXML
    private RadioButton rdb98;

    @FXML
    private RadioButton rdb99;

    @FXML
    private RadioButton rdbHistorical;

    @FXML
    private ToggleGroup methodToggles;

    @FXML
    private RadioButton rdbGaussian;

    @FXML
    private RadioButton rdbMonteCarlo;
    
    private static volatile DashboardController instance = null;

    @FXML
    void initialize() {
    	tbvAssets.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
    	tbvAssets.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("value"));
    	instance = this;
    }
    
    public static synchronized DashboardController getInstance() {
    	if(instance == null) {
    		new Thread(() -> Application.launch(DashboardController.class)).start();
    	}
    	while(instance == null) {	
    	}
    	return instance;
    }
    
	@Override
	public void start(Stage primaryStage) throws Exception {
		VBox root = (VBox) FXMLLoader.load(getClass().getResource("DashboardView.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("VAR Calculator");
		primaryStage.show();	
	}
	/**
	 * Checks that values are valid and adds to list 
	 */
	@FXML
	public void addAsset(ActionEvent event) {
		File tempFile = new File("data\\"+ txtTicket.getText()+".L.csv");
		Component frame = null;
		//check there is file for asset
		if(tempFile.exists()==true){
			//check there is enought data in the file 
			if(lineCounter("data\\"+ txtTicket.getText()+".L.csv")>101){
				try{
					//create asset and places in the table
					Asset newTicket = new Asset(txtTicket.getText(), Double.valueOf(txtValue.getText()));
					tbvAssets.getItems().add(newTicket);	
				}catch(Exception e ){
					//error if value is not double 
					JOptionPane.showMessageDialog(frame,
							"Looks like there was error with value!"
					    		+ "\nPlease make only put value not currency symbol ",
					    	"Value Error",
					    	JOptionPane.ERROR_MESSAGE);
				}
			}else{
				//error if there isn't enought data 
				JOptionPane.showMessageDialog(frame,
					"Looks like there isnt enough data!"
				    	+ "\nMake sure there is atleast 100 entries of historical data in file",
				    "File Error",
				    JOptionPane.ERROR_MESSAGE);
			}
		}else{
			//error message if file not found 
			JOptionPane.showMessageDialog(frame,
				    "No file for stock found!"
				    + "\nPlease make sure stock file is in data folder ",
				    "File Error",
				    JOptionPane.ERROR_MESSAGE);
		}
	}
	/**
	 * Counts the number of lines 
	 * @return number of lines counted 
	 */
	private static int lineCounter(String fp){
		 BufferedReader br = null; 
		 int rowCount = 0;
		//try reading file and count line count, catch file not found error or IO error
		try {
           br = new BufferedReader(new FileReader(fp));
           //counts the lines of the file 
           while ((br.readLine()) != null) {
               rowCount++;
           }
       } catch (IOException e) {
           e.printStackTrace();
       } finally {
           if (br != null) {
           	//try close the reader if it isn't null yet
               try {
                   br.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       }
		 return rowCount;
	}
	/**
	 * fethces all data from gui and calcules var displaying it to the user. 
	 */
	@FXML
	public void calculateVaR(ActionEvent event) {
		//get confidence level
		RadioButton selectedRadioButton = (RadioButton) confidenceToggles.getSelectedToggle();
		int confidence = Integer.parseInt(selectedRadioButton.getText().substring(0,2));
		
		//create portfolio and inseting all the assets 
		Portfolio port =  new Portfolio(); 
		for(Asset entry : tbvAssets.getItems()) {
			port.addAsset(entry.getName(), entry.getValue());
		}
		//getting the method user wants to use 
		RadioButton methodRadioButton = (RadioButton) methodToggles.getSelectedToggle();
		
		if(Objects.equals(methodRadioButton.getText(),"Historical")){
			ValueAtRiskHistorical vHis = new ValueAtRiskHistorical(confidence,port);
			txtVAR.setText(vHis.calculate() + "£");
		}
		if(Objects.equals(methodRadioButton.getText(),"Gaussian")){
			ValueAtRiskGaussian vGau = new ValueAtRiskGaussian(confidence,port);
			txtVAR.setText(vGau.calculate() + "£");
		}
		if(Objects.equals(methodRadioButton.getText(),"Monte Carlo")){
			ValueAtRiskMonteCarlo vMC = new ValueAtRiskMonteCarlo(confidence,port,10000);
			txtVAR.setText(vMC.evaluate() + "£");
		}
		
	}
	/**
	 * Getter method for ticket name 
	 * @return ticket name 
	 */
	public String getTicketName() {return txtTicket.getText();}	
}
