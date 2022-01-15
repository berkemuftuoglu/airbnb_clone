import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Main class controller, it is the window for all 4 panes.
 *
 * @author Berke Muftuoglu, Jakub Mrozcek, Zhen-Chien Ong, Mairaj Khalid
 * @version 08/04/2021
 */
public class Main extends Application {
    // We keep track of the count, and label displaying the count:
    private ArrayList<AirbnbListing> properties;
    private static HashMap<String, Integer> boroughs;

    private AirbnbDataLoader dataLoader = new AirbnbDataLoader();

    //field that stores the currently selected upper bound price.
    private int currentToPrice = 7000;

    //field that stores the currently selected lower bound price.
    private int currentFromPrice = 0;

    //fields indicating whether a lower/upper bound price has been selected by the user.
    private boolean selectedFromPrice = false;
    private boolean selectedToPrice = false;

    //The scene of the application
    private Scene scene;

    @FXML
    private Pane mainPane;

    @FXML
    private BorderPane ContentBorder;

    @FXML
    private Button nextPage;

    @FXML
    private void initialize() {
        disableNextPageButton();
    }

    /**
     * The start method is the main entry point for every JavaFX application.
     * It is called after the init() method has returned and after
     * the system is ready for the application to begin running.
     *
     * @param stage the primary stage for this application.
     */
    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("WelcomePage.fxml"));

        Scene scene = new Scene(loader.load());
        stage.setTitle("AirBnB London Welcome Page");
        stage.setScene(scene);

        stage.show();
    }

    /**
     * Method sets the from value to the one selected from the drop down menu.
     **/
    public void from(ActionEvent e) throws IOException {
        MenuItem selected = (MenuItem) e.getSource();//Getting the menuItem that called the method

        String menuItemValue = selected.getText();//Setting the value of the menuItem to a variable called menuItem value

        currentFromPrice = tryParse(menuItemValue);

        if (checkIntegrity()) {
            selected.getParentMenu().setText(menuItemValue);
            System.out.println(currentFromPrice);//For testing purposes
            selectedFromPrice = true;
            if (selectedToPrice) {
                load();
            }
        } 
        else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Please select valid price range.");
            a.show();        
        }
    }

    /**
     * Method sets the to value to the one selected from the drop down menu.
     **/
    public void to(ActionEvent e) throws IOException {
        MenuItem selected = (MenuItem) e.getSource(); //Getting the menuItem that called the method

        String menuItemValue = selected.getText(); //Setting the value of the menuItem to a variable called menuItem value

        if (!menuItemValue.equals("1000+")) {
            currentToPrice = tryParse(menuItemValue);
            if (checkIntegrity()) {
                selected.getParentMenu().setText(menuItemValue);
                System.out.println(currentToPrice); //For testing purposes
                selectedToPrice = true;
                if (selectedFromPrice) {
                    load();
                }
            } else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Please select valid price range.");
            a.show();   
            }
        } else {
            //When 1000+ is selected then the to price is automatically set to 7000
            //which is the value of the highest price in the csv file.
            currentToPrice = 7000;
            selected.getParentMenu().setText(menuItemValue);
            System.out.println(currentToPrice); //For testing purposes
            selectedToPrice = true;
            if (selectedFromPrice) {
                load();
            }
        }
    }

    /**
     * Loads the properties from the AirbnbDataLoader.
     **/
    private void load() {
        Object[] array = dataLoader.load(currentFromPrice, currentToPrice);
        properties = (ArrayList<AirbnbListing>) array[0];
        boroughs = (HashMap<String, Integer>) array[1];

        //Once properties are loaded, the user can go to the map pane.
        enableNextPageButton();
    }

    /**
     * Changes the current window to the map window
     */
    @FXML
    private void enableMap() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Map.fxml"));

        Stage state = (Stage) nextPage.getScene().getWindow();
        state.setScene(new Scene(loader.load()));

        MapPanel mapController = loader.getController();
        mapController.setFilteredProperties(properties);
        mapController.setPriceRange(currentFromPrice,currentToPrice);

        state.setTitle("Map");
        state.show();
    }

    /**
     * Disables the next button
     */
    private void disableNextPageButton() {
        nextPage.setDisable(true);
    }

    /**
     * Enables the next button
     */
    private void enableNextPageButton() {
        nextPage.setDisable(false);
    }

    /**
     * Safe method to turn a string value (from the menu item) to an Integer.
     **/
    private Integer tryParse(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Defensive method to check whether the price range constraints from the specification are satisfied.
     * System will not allow price range changes that violate constraints.
     **/
    private boolean checkIntegrity() {
        if (currentToPrice < currentFromPrice) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @return HashMap of boroughs.
     */
    public static HashMap<String, Integer> getBoroughNumbers(){
        return boroughs;
    }
}