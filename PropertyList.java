import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.collections.FXCollections;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;


/**
 * A new window of the selected borough.
 *
 * Displays a table of properties in the selected borough
 * in the given price range. With some filtering options for
 * the table.
 *
 * @author Berke Muftuoglu, Jakub Mrozcek, Zhen-Chien Ong, Mairaj Khalid
 * @version 08/04/2021
 */
public class PropertyList
{
    //The tableview and table columns
    @FXML
    public TableView<AirbnbListing> tableview;
    @FXML
    public TableColumn<AirbnbListing, String> boroughName;
    @FXML
    public TableColumn<AirbnbListing, Integer> propertyPrice;
    @FXML
    public TableColumn<AirbnbListing, Integer> numOfReview;
    @FXML
    public TableColumn<AirbnbListing, Integer> minStay;

    @FXML
    private ChoiceBox<String> sortByBox; //Lets the user pick how to sort the table
    @FXML
    private Label result; //Shows the number of results at the top right

    private ObservableList<AirbnbListing> properties; //Stores the list of properties in the borough and price range

    /**
     * Constructor.
     */
    public PropertyList() {
    }

    /**
     * Initialize tables and choice box elements.
     */
    @FXML
    private void initialize() {
        sortByBox.getItems().addAll("Number Of Reviews", "Price", "Alphabetically");
        sortByBox.getSelectionModel().select("Number Of Reviews");

        boroughName.setCellValueFactory(new PropertyValueFactory<>("Host_name"));
        propertyPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        numOfReview.setCellValueFactory(new PropertyValueFactory<>("NumberOfReviews"));
        minStay.setCellValueFactory(new PropertyValueFactory<>("MinimumNights"));
    }

    /**
     * Adds listener to choice box. Sorts the table view of properties based on the selected
     * value in the choice box.
     */
    private void addListenerToSortByBox() {
        sortByBox.getSelectionModel().selectedItemProperty().addListener((V, oldValue, newValue) -> {
            ObservableList<AirbnbListing> sortedProperties = properties;

            if("Number Of Reviews".equals(newValue)) {
                //System.out.println("review"); 
                sortedProperties.sort(Comparator.comparing(AirbnbListing::getNumberOfReviews).reversed());
                tableview.setItems(sortedProperties);
            }
            if("Price".equals(newValue)) {
                //System.out.println("price");
                sortedProperties.sort(Comparator.comparing(AirbnbListing::getPrice));
                tableview.setItems(sortedProperties);
            }
            if("Alphabetically".equals(newValue)) {
                //System.out.println("alpha");
                sortedProperties.sort(Comparator.comparing(AirbnbListing::getHost_name));
                tableview.setItems(sortedProperties);
            }
        });
    }

    /**
     * Opens a new window to display the selected property description.
     */
    @FXML
    private void descWindow() throws IOException {
        if(!(tableview.getSelectionModel().getSelectedItem() == null)) {
    
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DescriptionPanel.fxml"));
    
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
    
            DescriptionPanel descController = loader.getController();
            descController.setDescLabel(getSelectedPropertyDescription());
            descController.setLocation(getSelectedPropertyLatitude(), getSelectedPropertyLongitude());
    
            stage.setTitle("Extra details");
            stage.show();
        }
    }

    /**
     * Converts the arraylist of properties, with the prices and borough name sorted
     * into an ObservableList to be displayed in the table.
     * @param boroughList ArrayList of sorted properties
     */
    public void setBoroughList(ArrayList<AirbnbListing> boroughList) {
        properties = FXCollections.observableArrayList(boroughList);
        loadProperties();
    }

    /**
     * Loads the properties into the table, and the listener action after the
     * list of properties have been received from main class.
     */
    private void loadProperties() {
        tableview.setItems(properties);
        addListenerToSortByBox();
        updateResultCount();
    }

    /**
     * Displays the number of properties in the list.
     */
    private void updateResultCount() {
        result.setText("Results: " + properties.size());
    }

    /**
     * Returns the description from the selected property.
     */
    private String getSelectedPropertyDescription() {
        return tableview.getSelectionModel().getSelectedItem().getName();
    }

    /**
     * Returns the latitude from the selected property.
     */
    private double getSelectedPropertyLatitude() {
        return tableview.getSelectionModel().getSelectedItem().getLatitude();
    }

    /**
     * Returns the longitude from the selected property.
     */
    private double getSelectedPropertyLongitude() {
        return tableview.getSelectionModel().getSelectedItem().getLongitude();
    }
}
