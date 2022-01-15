import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.collections.FXCollections;
import java.io.IOException;
import java.util.*;

/**
 * One of the panels in the main window
 *
 * Allows the user to filter through all available properties with a set of
 * parameters that they can manual select, for more specific results.
 *
 * @author Berke Muftuoglu, Jakub Mrozcek, Zhen-Chien Ong, Mairaj Khalid
 * @version 08/04/2021
 */
public class AdvanceSearch
{
    //The filter options
    @FXML
    private ComboBox<String> neighbourhoodComboBox;
    @FXML
    private ComboBox<String> roomTypeComboBox;
    @FXML
    private ComboBox<String> minReviewComboBox;
    @FXML
    private ComboBox<String> minNightComboBox;

    //The tableview and table columns
    @FXML
    public TableView<AirbnbListing> advanceSearchTable;
    @FXML
    public TableColumn<AirbnbListing, String> hostName;
    @FXML
    public TableColumn<AirbnbListing, Integer> propertyPrice;
    @FXML
    public TableColumn<AirbnbListing, String> neighbourhood;
    @FXML
    public TableColumn<AirbnbListing, String> roomType;
    @FXML
    public TableColumn<AirbnbListing, Integer> numOfReviews;
    @FXML
    public TableColumn<AirbnbListing, Integer> minNights;

    //The buttons in the window
    @FXML
    private Button leftButton;
    @FXML
    private Button rightButton;
    @FXML
    private Button searchButton;

    //The labels in the window
    @FXML
    private Label result;
    @FXML
    private Label priceRange;

    private ArrayList<AirbnbListing> boroughList;
    private ObservableList<AirbnbListing> properties; //List of properties in a price range
    private ObservableList<String> uniqueNeighbourhoods; //Unique borough names
    private ObservableList<String> uniqueRoomType; //Unique room types

    //private ObservableList<AirbnbListing> filteringProperties; //Used to filter properties
    private ObservableList<AirbnbListing> filteredProperties; //Used to remove duplicates from beforeSortProperties

    //Stores the choices made by the user
    private String neighbourhoodChoice;
    private String roomTypeChoice;
    private Integer minNumOfReviewChoice;
    private Integer minNightsChoice;

    /**
     * Constructor
     */
    public AdvanceSearch() {
        boroughList = new ArrayList<>();
        uniqueRoomType = FXCollections.observableArrayList();
        uniqueNeighbourhoods = FXCollections.observableArrayList();
        filteredProperties = FXCollections.observableArrayList();
    }

    /**
     * Initialize fxml elements, adds options and listeners to the combo boxes.
     */
    @FXML
    private void initialize() {
        minReviewComboBox.getItems().addAll("0", "20", "40", "60", "80", "100");

        minNightComboBox.getItems().addAll("0","1", "2", "5", "10", "15");

        addListenerToNeighbourhoodBox();
        addListenerToRoomTypeBox();
        addListenerToNumReviewBox();
        addListenerToMinNightsBox();

        hostName.setCellValueFactory(new PropertyValueFactory<>("Host_name"));
        propertyPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        neighbourhood.setCellValueFactory(new PropertyValueFactory<>("Neighbourhood"));
        roomType.setCellValueFactory(new PropertyValueFactory<>("Room_type"));
        numOfReviews.setCellValueFactory(new PropertyValueFactory<>("NumberOfReviews"));
        minNights.setCellValueFactory(new PropertyValueFactory<>("MinimumNights"));
    }

    /**
     * Stores the list of properties in a price range into a local variable in the class.
     * @param propertiesInPriceRange List of properties in price range.
     */
    public void setProperties(ArrayList<AirbnbListing> propertiesInPriceRange) {
        boroughList = propertiesInPriceRange;
        properties = FXCollections.observableArrayList(propertiesInPriceRange);
        loadProperties();
    }

    /**
     * Sets the label to display the currently selected price range.
     * @param fromPrice The user selected from price.
     * @param toPrice The user selected to price.
     */
    public void setPriceRange(int fromPrice, int toPrice) {
        priceRange.setText("Price Range: " + fromPrice + " - " + toPrice);
    }

    /**
     * Updates the table with the initial properties and options from the data.
     */
    private void loadProperties() {
        advanceSearchTable.setItems(properties);

        //Gets all unique strings from the data and adds it to the combobox options
        getUniqueNeighbourhoodName();
        neighbourhoodComboBox.getItems().addAll(uniqueNeighbourhoods);
        getUniqueRoomType();
        roomTypeComboBox.getItems().addAll(uniqueRoomType);

        updateResultCount();
    }

    /**
     * Adds listener to combo box. Stores the user's borough selection.
     */
    private void addListenerToNeighbourhoodBox() {
        neighbourhoodComboBox.getSelectionModel().selectedItemProperty().addListener((V, oldValue, newValue) -> {
            neighbourhoodChoice = newValue;
            System.out.println(neighbourhoodChoice);
        });
    }

    /**
     * Adds listener to combo box. Stores the user's room type selection.
     */
    private void addListenerToRoomTypeBox() {
        roomTypeComboBox.getSelectionModel().selectedItemProperty().addListener((V, oldValue, newValue) -> {
            roomTypeChoice = newValue;
            System.out.println(roomTypeChoice);
        });
    }

    /**
     * Adds listener to combo box. Stores the user's minimum number of reviews selection.
     */
    private void addListenerToNumReviewBox() {
        minReviewComboBox.getSelectionModel().selectedItemProperty().addListener((V, oldValue, newValue) -> {
            minNumOfReviewChoice = Integer.parseInt(newValue);
            System.out.println(minNumOfReviewChoice);
        });
    }

    /**
     * Adds listener to combo box. Stores the user's minimum nights selection.
     */
    private void addListenerToMinNightsBox() {
        minNightComboBox.getSelectionModel().selectedItemProperty().addListener((V, oldValue, newValue) -> {
                minNightsChoice = Integer.parseInt(newValue);
                System.out.println(minNightsChoice);
        });
    }

    /**
     * Filters through the properties based on the user's selection
     */
    @FXML
    private void searchProperties() {

        ObservableList<AirbnbListing> filteringProperties = FXCollections.observableArrayList();
        filteringProperties.addAll(properties);

        if(!isErrorInChoices()) {
            filteringProperties.removeIf(listing -> !listing.getNeighbourhood().equals(neighbourhoodChoice));
            filteringProperties.removeIf(listing -> !listing.getRoom_type().equals(roomTypeChoice));
            filteringProperties.removeIf(listing -> listing.getNumberOfReviews() < minNumOfReviewChoice);
            filteringProperties.removeIf(listing -> listing.getMinimumNights() < minNightsChoice);

            if(filteredProperties.isEmpty()) {
                //Removes duplicates
                for (AirbnbListing listings : filteringProperties) {
                    if (!filteredProperties.contains(listings)) {
                        filteredProperties.add(listings);
                    }
                }

                noResultsInfoDialog();

                advanceSearchTable.setItems(filteredProperties);
                updateSortedResultCount();
            }
            else {
                resetInfoDialog();
            }
        }
    }

    /**
     * Displays an information dialog when there are no matching results from the search.
     */
    private void noResultsInfoDialog() {
        if(filteredProperties.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("There is no matching results.");
            a.show();
        }
    }

    /**
     * Displays an information dialog if the user presses search twice.
     */
    private void resetInfoDialog() {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText("Please press reset to search again.");
        a.show();
    }

    /**
     * Resets the table to the initial properties, empties the filter properties list
     * and updates the result count.
     */
    @FXML
    private void resetProperties() {
        advanceSearchTable.setItems(properties);
        filteredProperties.clear();

        updateResultCount();
        System.out.println("reset" + properties.size());

    }

    /**
     * Checks if all combo boxes have been selected, if not display an error message.
     * @return true, if a combo box option is not selected, false otherwise.
     */
    private boolean isErrorInChoices() {
        if(neighbourhoodChoice == null) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Please select borough.");
            a.show();
            return true;
        }
        if(roomTypeChoice == null) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Please select room type.");
            a.show();
            return true;
        }
        if(minNumOfReviewChoice == null) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Please select minimum number of reviews.");
            a.show();
            return true;
        }
        if(minNightsChoice == null) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Please select minimum nights to stay.");
            a.show();
            return true;
        }
        return false;
    }

    /**
     * Stores all unique borough names from the list of properties in a local variable.
     */
    private void getUniqueNeighbourhoodName() {
        for (AirbnbListing property : properties) {
            if (!uniqueNeighbourhoods.contains(property.getNeighbourhood())) {
                uniqueNeighbourhoods.add(property.getNeighbourhood());
            }
        }
    }

    /**
     * Stores all unique room types from the list of properties in a local variable.
     */
    private void getUniqueRoomType() {
        for (AirbnbListing property : properties) {
            if (!uniqueRoomType.contains(property.getRoom_type())) {
                uniqueRoomType.add(property.getRoom_type());
            }
        }
    }

    /**
     * Displays the number of filtered properties in the list.
     */
    private void updateSortedResultCount() {
        result.setText("Results: " + filteredProperties.size());
    }

    /**
     * Displays the number of initial properties in the list.
     */
    private void updateResultCount() {
        result.setText("Results: " + properties.size());
    }

    /**
     * Changes the stage to the next right panel. (Welcome Page)
     * @throws IOException When IO operation fails.
     */
    @FXML
    private void rightPage() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("WelcomePage.fxml"));

        Stage stage = (Stage) rightButton.getScene().getWindow();
        stage.setScene(new Scene(loader.load()));

        stage.setTitle("AirBnB London Welcome Page");
        stage.show();
    }

    /**
     * Changes the stage to the next left panel. (Statistics Panel)
     * @throws IOException When IO operation fails.
     */
    @FXML
    private void leftPage() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("StatisticsPanel.fxml"));

        Stage state = (Stage) leftButton.getScene().getWindow();
        state.setScene(new Scene(loader.load()));

        StatisticsPanel statisticsController = loader.getController();
        statisticsController.loadStatisticsPanel(boroughList);

        state.setTitle("Statistics Panel");
        state.show();

    }

    /**
     * Opens a new window to display information of the selected property.
     * @throws IOException When IO operation fails. 
     */
    @FXML
    private void descWindow() throws IOException {
        if(!(advanceSearchTable.getSelectionModel().getSelectedItem() == null)) {
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
     * Returns the description from the selected property.
     */
    private String getSelectedPropertyDescription() {
        return advanceSearchTable.getSelectionModel().getSelectedItem().getName();
    }

    /**
     * Returns the latitude from the selected property.
     */
    private double getSelectedPropertyLatitude() {
        return advanceSearchTable.getSelectionModel().getSelectedItem().getLatitude();
    }

    /**
     * Returns the longitude from the selected property.
     */
    private double getSelectedPropertyLongitude() {
        return advanceSearchTable.getSelectionModel().getSelectedItem().getLongitude();
    }
}
