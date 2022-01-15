import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import java.net.URL;
import java.util.Map;

import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;

import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * This class carries out the functions of buttons, and showing of the statistics pane.
 * The singular pane is divided into 4 sections, each with 2 buttons to move left and right from each statistic,
 * and two labels to show the information of each statistic.
 * Top left section is considered number 1,
 * Top right section is considered number 2,
 * Bottom left section is considered number 3,
 * Bottom right section is considered number 4.
 * Top label of each section is known as heading label.
 * Bottom label of each section is known as value label.
 *
 * @author Berke Muftuoglu, Jakub Mrozcek, Zhen-Chien Ong, Mairaj Khalid
 * @version 08/04/2021
 */
public class StatisticsPanel
{
    StatisticsPanelInfo info;

    // All the labels in the panel.
    @FXML
    private Label labelOneTop;
    @FXML
    private Label labelOneBottom;
    @FXML
    private Label labelTwoTop;
    @FXML
    private Label labelTwoBottom;
    @FXML
    private Label labelThreeTop;
    @FXML
    private Label labelThreeBottom;
    @FXML
    private Label labelFourTop;
    @FXML
    private Label labelFourBottom;
    @FXML
    private Button nextPage;
    @FXML
    private Button previousPage;

    private ArrayList<AirbnbListing> properties;

    // Gives a number to each type of heading of the heading labels of the panel
    private HashMap<Integer, String> label1DisplayNumbering = new HashMap<Integer, String>();

    // Gives the same number to the value of the value labels that is to be displayed with the heading
    private HashMap<Integer, String> label2DisplayNumbering = new HashMap<Integer, String>();

    // Keeps track of which heading and value are actively used so that the same heading and value are not used twice
    private HashMap<Integer, Boolean> textActiveCheck = new HashMap<Integer, Boolean>();
    private int currentFromPrice;
    private int currentToPrice;


    /**
     * Start the intialisation of info from statisticsPanelInfo class, and of the labels.
     * But do not intialise info if the properties list is empty or is null.
     */
    public void loadStatisticsPanel(ArrayList<AirbnbListing> properties)
    {
        if(!(properties == null) && !properties.isEmpty()){
            info = new StatisticsPanelInfo(properties);
        }else{
            info = null;
        }
        setToLabelInitial();

        this.properties = properties;

    }

    /**
     * Initialises the tracking information to all false, so that no heading and value are active intitally.
     */
    private void loadTrack(){
        textActiveCheck.put(1, false);
        textActiveCheck.put(2, false);
        textActiveCheck.put(3, false);
        textActiveCheck.put(4, false);
        textActiveCheck.put(5, false);
        textActiveCheck.put(6, false);
        textActiveCheck.put(7, false);
        textActiveCheck.put(8, false);
    }

    /**
     * Loads the headings for the heading labels.
     * Note, each heading has to be unique.
     */
    private void loadHeadings(){
        label1DisplayNumbering.put(1, "Total number of available properties");
        label1DisplayNumbering.put(2, "Average reviews for properties");
        label1DisplayNumbering.put(3, "Total homes and apartments:");
        label1DisplayNumbering.put(4, "Most expensive borough(s):");
        label1DisplayNumbering.put(5, "Cheapest borough(s):");
        label1DisplayNumbering.put(6, "Total hotels and similar:");
        label1DisplayNumbering.put(7, "Property(s) with most reviews:");
        label1DisplayNumbering.put(8, "Host(s) owning most properties (some not shown in search):");
    }

    /**
     * Loads the stats from the statisticsPanelInfo class for the value labels.
     * Numbering is set according to the headings.
     */
    private void loadStats(){
        label2DisplayNumbering.put(1, "" + info.getTotalPropertiesAvailable());
        label2DisplayNumbering.put(2, "" + info.getPropertiesAvgNoReviews());
        label2DisplayNumbering.put(3, "" + info.getTotalHomesAndApartments());
        label2DisplayNumbering.put(4, "" + info.getMostExpensiveBorough());
        label2DisplayNumbering.put(5, "" + info.getCheapestBorough());
        label2DisplayNumbering.put(6, "" + info.getTotalHotelsAndSimilar());
        label2DisplayNumbering.put(7, "" + info.getMostReviewedProperty());
        label2DisplayNumbering.put(8, "" + info.getHostMostProperties());
    }
    
    /**
     * Loads "Empty" for the stats for the value labels if the info field dosent have refernce to the statisticsPanelInfo class.
     */
    private void loadStatsForEmpty()
    {
        label2DisplayNumbering.put(1, "Empty");
        label2DisplayNumbering.put(2, "Empty");
        label2DisplayNumbering.put(3, "Empty");
        label2DisplayNumbering.put(4, "Empty");
        label2DisplayNumbering.put(5, "Empty");
        label2DisplayNumbering.put(6, "Empty");
        label2DisplayNumbering.put(7, "Empty");
        label2DisplayNumbering.put(8, "Empty");
    }

    /**
     * Call methods to load required information into the HashMaps.
     * 
     * Input which information to be displayed intially.
     * Note, ensure information being input to textActiveCheck and setToLabel match accordingly.
     */
    private void setToLabelInitial()
    {
        loadHeadings();
        
        if(!(info == null)){
            loadStats();
        }else{
            loadStatsForEmpty();
        }
        
        loadTrack();

        textActiveCheck.replace(1, true);
        setToLabel(1, labelOneTop, labelOneBottom);

        textActiveCheck.replace(2, true);
        setToLabel(2, labelTwoTop, labelTwoBottom);

        textActiveCheck.replace(3, true);
        setToLabel(3, labelThreeTop, labelThreeBottom);

        textActiveCheck.replace(4, true);
        setToLabel(4, labelFourTop, labelFourBottom);
    }

    /**
     * Sets the values to the labels.
     */
    private void setToLabel(int key, Label label1, Label label2)
    {
        label1.setText("" + label1DisplayNumbering.get(key));
        label2.setText("" + label2DisplayNumbering.get(key));
    }

    /**
     * When one the buttons to go backwards are pushed.
     * Checks to see if information in the heading label corresponds with information in HashMap to find the key for all the HashMaps.
     * Once found, it finds the next unused information sequentially going backwards until it finds.
     */
    private void backButtonPushed(Label label1, Label label2)
    {
        int i = 1;
        boolean found = false;
        while (i <= label1DisplayNumbering.size() && found == false){
            if(label1.getText().equals(label1DisplayNumbering.get(i))){
                found = true;
                int i2 = i;
                while(textActiveCheck.get(i2)){
                    i2 = i2 - 1;
                    if (i2 < 1){
                        i2 = textActiveCheck.size();
                    }
                }
                textActiveCheck.replace(i, false);
                textActiveCheck.replace(i2, true);
                setToLabel(i2, label1, label2);
            }
            i++;
        }
    }

    /**
     * When one the buttons to go forwards are pushed.
     * Checks to see if information in the heading label corresponds with information in HashMap to find the key for all the HashMaps.
     * Once found, it finds the next unused information sequentially going forwards until it finds.
     */
    private void forwardButtonPushed(Label label1, Label label2)
    {
        int i = 1;
        boolean found = false;
        while (i <= label1DisplayNumbering.size() && found == false){
            if(label1.getText().equals(label1DisplayNumbering.get(i))){
                found = true;
                int i2 = i;
                while(textActiveCheck.get(i2)){
                    i2 = i2 + 1;
                    if (i2 > textActiveCheck.size()){
                        i2 = 1;
                    }
                }
                textActiveCheck.replace(i, false);
                textActiveCheck.replace(i2, true);
                setToLabel(i2, label1, label2);
            }
            i++;
        }
    }

    /**
     * Action taken on top left box, left button push.
     * Fade transition included.
     */
    @FXML
    private void buttonOneBack(ActionEvent e)
    {
        labelFadeOut(labelOneTop);
        labelFadeOut(labelOneBottom);

        backButtonPushed(labelOneTop,labelOneBottom);

        labelFadeIn(labelOneTop);
        labelFadeIn(labelOneBottom);
    }

    /**
     * Action taken on top right box, left button push.
     */
    @FXML
    private void buttonTwoBack(ActionEvent e)
    {
        labelFadeOut(labelTwoTop);
        labelFadeOut(labelTwoBottom);

        backButtonPushed(labelTwoTop, labelTwoBottom);

        labelFadeIn(labelTwoTop);
        labelFadeIn(labelTwoBottom);
    }

    /**
     * Action taken on bottom left box, left button push.
     */
    @FXML
    private void buttonThreeBack(ActionEvent e)
    {
        labelFadeOut(labelThreeTop);
        labelFadeOut(labelThreeBottom);

        backButtonPushed(labelThreeTop, labelThreeBottom);

        labelFadeIn(labelThreeTop);
        labelFadeIn(labelThreeBottom);
    }

    /**
     * Action taken on bottom right box, left button push.
     */
    @FXML
    private void buttonFourBack(ActionEvent e)
    {
        labelFadeOut(labelFourTop);
        labelFadeOut(labelFourBottom);

        backButtonPushed(labelFourTop, labelFourBottom);

        labelFadeIn(labelFourTop);
        labelFadeIn(labelFourBottom);
    }

    /**
     * Action taken on top left box, right button push.
     */
    @FXML
    private void buttonOneForward(ActionEvent e)
    {
        labelFadeOut(labelOneTop);
        labelFadeOut(labelOneBottom);

        forwardButtonPushed(labelOneTop, labelOneBottom);

        labelFadeIn(labelOneTop);
        labelFadeIn(labelOneBottom);
    }

    /**
     * Action taken on top right box, right button push.
     */
    @FXML
    private void buttonTwoForward(ActionEvent e)
    {
        labelFadeOut(labelTwoTop);
        labelFadeOut(labelTwoBottom);

        forwardButtonPushed(labelTwoTop, labelTwoBottom);

        labelFadeIn(labelTwoTop);
        labelFadeIn(labelTwoBottom);
    }

    /**
     * Action taken on bottom left box, right button push.
     * Fade transition included.
     */
    @FXML
    private void buttonThreeForward(ActionEvent e)
    {
        labelFadeOut(labelThreeTop);
        labelFadeOut(labelThreeBottom);

        forwardButtonPushed(labelThreeTop, labelThreeBottom);

        labelFadeIn(labelThreeTop);
        labelFadeIn(labelThreeBottom);
    }

    /**
     * Action taken on bottom right box, right button push.
     */
    @FXML
    private void buttonFourForward(ActionEvent e)
    {
        labelFadeOut(labelFourTop);
        labelFadeOut(labelFourBottom);

        forwardButtonPushed(labelFourTop, labelFourBottom);

        labelFadeIn(labelFourTop);
        labelFadeIn(labelFourBottom);
    }

    /**
     * Fade out transition for labels.
     */
    private void labelFadeOut(Label label)
    {
        FadeTransition fade = new FadeTransition(Duration.millis(300), label);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.play();
    }

    /**
     * Fade in transition for labels
     */
    private void labelFadeIn(Label label)
    {
        FadeTransition fade = new FadeTransition(Duration.millis(300), label);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();
    }

    /**
     * Loads next panel "Advance Search", and its class.
     */
    @FXML
    private void goToNextPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AdvanceSearch.fxml"));

        Stage stage = (Stage) nextPage.getScene().getWindow();
        stage.setScene(new Scene(loader.load()));

        AdvanceSearch advanceSearchController = loader.getController();
        advanceSearchController.setProperties(properties);
        advanceSearchController.setPriceRange(currentFromPrice, currentToPrice);

        stage.centerOnScreen();

        stage.setTitle("Advance Search");
        stage.show();
    }

    /**
     * Loads previous panel "Map", and its class.
     */
    @FXML
    private void goToPreviousPage() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Map.fxml"));

        Stage state = (Stage) previousPage.getScene().getWindow();
        state.setScene(new Scene(loader.load()));

        state.setTitle("AirBnB London Welcome Page");
        MapPanel mapController = loader.getController();
        mapController.setFilteredProperties(properties);

        state.setTitle("Map");
        state.show();

    }
    
    /**
     * Stores the current from and to prices, as selected by the user.
     */
    public void setPriceRange(int fromPrice, int toPrice) {
        currentFromPrice = fromPrice; 
        currentToPrice = toPrice;
    }
}