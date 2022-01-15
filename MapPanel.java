import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

/**
 * The controller class of the map panel
 *
 * @author Berke Muftuoglu, Jakub Mrozcek, Zhen-Chien Ong, Mairaj Khalid
 * @version 08/04/2021
 */
public class MapPanel
{
    //FXML components, buttons
    @FXML
    private RadioButton hillingdonButton;

    @FXML
    private RadioButton harrowButton;

    @FXML
    private RadioButton barnetButton;

    @FXML
    private RadioButton enfieldButton;

    @FXML
    private RadioButton walthamForestButton;

    @FXML
    private RadioButton redBridgeButton;

    @FXML
    private RadioButton haveringButton;

    @FXML
    private RadioButton bexleyButton;

    @FXML
    private RadioButton bromleyButton;

    @FXML
    private RadioButton croydonButton;

    @FXML
    private RadioButton suttonButton;

    @FXML
    private RadioButton kingstonUponThamesButton;

    @FXML
    private RadioButton richmondUponThamesButton;

    @FXML
    private RadioButton hounslowButton;

    @FXML
    private RadioButton ealingButton;

    @FXML
    private RadioButton brentButton;

    @FXML
    private RadioButton camdenButton;

    @FXML
    private RadioButton haringeyButton;

    @FXML
    private RadioButton islingtonButton;

    @FXML
    private RadioButton hackneyButton;

    @FXML
    private RadioButton newhamButton;

    @FXML
    private RadioButton barkingandDagenhamButton;

    @FXML
    private RadioButton greenwichButton;

    @FXML
    private RadioButton lewishamButton;

    @FXML
    private RadioButton southwarkButton;

    @FXML
    private RadioButton cityOfLondonButton;

    @FXML
    private RadioButton lambethButton;

    @FXML
    private RadioButton wandsworthButton;

    @FXML
    private RadioButton mertonButton;

    @FXML
    private RadioButton hammersmithAndFulhamButton;

    @FXML
    private RadioButton kensingtonAndChelseaButton;

    @FXML
    private RadioButton westminsterButton;

    @FXML
    private RadioButton towerHamletsButton;

    @FXML
    private Button nextPage;

    @FXML
    private Button previousPage;

    private ArrayList<AirbnbListing> boroughList;
    private ArrayList<RadioButton> mapButtons;
    private HashMap<String, Integer> boroughProperties;

    private int currentFromPrice;
    private int currentToPrice;


    /**
     * Constructor.
     */
    public MapPanel() {
        boroughList = new ArrayList<>();


        boroughProperties = new HashMap<>();
        boroughProperties = Main.getBoroughNumbers();

        mapButtons = new ArrayList<>();
    }

    /**
     * Initialize components.
     */
    @FXML
    private void initialize() {
        mapButtons.add(hackneyButton);
        mapButtons.add(cityOfLondonButton);
        mapButtons.add(haringeyButton);
        mapButtons.add(harrowButton);
        mapButtons.add(haveringButton);
        mapButtons.add(hounslowButton);
        mapButtons.add(hammersmithAndFulhamButton);
        mapButtons.add(barnetButton);
        mapButtons.add(barkingandDagenhamButton);
        mapButtons.add(bexleyButton);
        mapButtons.add(brentButton);
        mapButtons.add(bromleyButton);
        mapButtons.add(camdenButton);
        mapButtons.add(ealingButton);
        mapButtons.add(enfieldButton);
        mapButtons.add(greenwichButton);
        mapButtons.add(hillingdonButton);
        mapButtons.add(islingtonButton);
        mapButtons.add(kingstonUponThamesButton);
        mapButtons.add(kensingtonAndChelseaButton);
        mapButtons.add(lambethButton);
        mapButtons.add(lewishamButton);
        mapButtons.add(mertonButton);
        mapButtons.add(newhamButton);
        mapButtons.add(redBridgeButton);
        mapButtons.add(richmondUponThamesButton);
        mapButtons.add(southwarkButton);
        mapButtons.add(suttonButton);
        mapButtons.add(towerHamletsButton);
        mapButtons.add(walthamForestButton);
        mapButtons.add(wandsworthButton);
        mapButtons.add(westminsterButton);
        mapButtons.add(croydonButton);
        refactorButtons();
    }

    /**
     * Opens a new window containing the description of the selected property.
     * @param e ActionEvent
     * @throws IOException When IO operation fails.
     */
    @FXML
    private void secondWindow(ActionEvent e) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("PropertyList.fxml"));

        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));

        PropertyList propController = loader.getController();

        RadioButton selected = (RadioButton) e.getSource();
        String boroughName = selected.getId().replace("Button", "");

        ArrayList<AirbnbListing> filteredListOfProperties;
        filteredListOfProperties = filteredArrayList(boroughName);
        propController.setBoroughList(filteredListOfProperties);

        if (!filteredListOfProperties.isEmpty()) {
            stage.setTitle(filteredListOfProperties.get(0).getNeighbourhood());
            stage.show();
        }
    }

    /**
     * Assigns the properties within the price range selected by the user to a local variable.
     * @param input ArrayList of properties in the specified price range.
     */
    public void setFilteredProperties(ArrayList<AirbnbListing> input) {
        boroughList = input;
    }

    /**
     * Filters through the properties in the price range with the selected borough.
     * @param boroughName The name of the borough.
     * @return ArrayList of properties.
     */
    private ArrayList<AirbnbListing> filteredArrayList(String boroughName){
        ArrayList<AirbnbListing> result = new ArrayList<>();
        for(AirbnbListing value : boroughList){
            if(value.getNeighbourhood().toLowerCase()
                    .replaceAll("\\s+","").
                            equals(boroughName.toLowerCase().
                                    replaceAll("\\s+",""))){
                result.add(value);
            }
        }
        return result;
    }

    /**
     * Changes the stage to the next right panel. (Statistics Page)
     * @throws IOException When IO operation fails.
     */
    @FXML
    private void goToNextPage() throws IOException{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("StatisticsPanel.fxml"));

        Stage state = (Stage) nextPage.getScene().getWindow();
        state.setScene(new Scene(loader.load()));

        state.setTitle("Statistics Panel");
        state.show();
        
        StatisticsPanel statisticsController = loader.getController();
        statisticsController.loadStatisticsPanel(boroughList);
        statisticsController.setPriceRange(currentFromPrice, currentToPrice);
    }

    /**
     * Changes the stage to the next left panel. (Welcome Panel)
     * @throws IOException When IO operation fails.
     */
    @FXML
    private void goToPreviousPage() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("WelcomePage.fxml"));

        Stage state = (Stage) previousPage.getScene().getWindow();
        state.setScene(new Scene(loader.load()));

        state.setTitle("AirBnB London Welcome Page");
        state.show();

    }

    /**
     * Stores the current from and to prices, as selected by the user.
     */
    public void setPriceRange(int fromPrice, int toPrice) {
        currentFromPrice = fromPrice; 
        currentToPrice = toPrice;
    }

    /**
     * Method called at the intiialization of the map panel to resize the buttons
     * based on how many properties are valid in each borough within the given price range.
     */
    private void refactorButtons(){
        Iterator it = boroughProperties.entrySet().iterator();
        while (it.hasNext()) {
                HashMap.Entry pair = (HashMap.Entry)it.next();
                String boroughName = (String) pair.getKey();
                System.out.println(boroughName);
                int boroughCount = (Integer) pair.getValue();
                for (RadioButton borough : mapButtons){
                    String nameOfBorough = borough.getId().replace("Button", "");
                    if (boroughName.toLowerCase().replaceAll("\\s+","").equals(nameOfBorough.toLowerCase().replaceAll("\\s+","")) ){
                        if (boroughCount >= 1750){
                            borough.getStyleClass().add("huge");

                        }
                        else if(boroughCount >= 1250 && boroughCount < 1750){
                            borough.getStyleClass().add("upmid");
                        }
                        else if(boroughCount >= 750 && boroughCount < 1250){
                            borough.getStyleClass().add("mid");
                        }
                        else if (boroughCount > 250 && boroughCount < 750){
                            borough.getStyleClass().add("low");
                        }
                        else if (boroughCount > 0 && boroughCount <= 250){
                            borough.getStyleClass().add("minimal");
                        }
                    }
                }
        }
    }
}