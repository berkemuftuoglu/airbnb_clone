import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is made to get retrieve and calculate information about the properties,
 * and make them available for the StatisticsPanel class to use.
 *
 * @author Berke Muftuoglu, Jakub Mrozcek, Zhen-Chien Ong, Mairaj Khalid
 * @version 08/04/2021
 */
public class StatisticsPanelInfo {
    // The total properties available to get information about
    private ArrayList<AirbnbListing> properties;

    // The variables in which the information is to be loaded
    private int totalPropertiesAvailable;
    private int propertiesAvgNoReviews;
    private int totalHomesAndApartments;
    private int totalHotelsAndSimilar;
    private String mostExpensiveBorough;
    private String cheapestBorough;
    private String mostReviewedProperty;
    private String hostMostProperties;

    /**
     * Use the ArrayList input to get all the properties available to retrieve information about,
     * and then call the loadInfo method to start loading that information.
     * 
     * Note, do not input properties as null, that will give an error,
     * but entering properties as empty list will not give an error, but may not give the required output.
     */
    public StatisticsPanelInfo(ArrayList<AirbnbListing> properties) {
        this.properties = properties;
        loadInfo();
    }

    /**
     * Set the variables that store inforamtion intially to 0 for int, and "" for String.
     * Then call all the methods required to retrieve the information to be put inside them.
     */
    private void loadInfo() {
        totalPropertiesAvailable = 0;
        propertiesAvgNoReviews = 0;
        totalHomesAndApartments = 0;
        totalHotelsAndSimilar = 0;
        mostExpensiveBorough = "";
        cheapestBorough = "";
        mostReviewedProperty = "";
        hostMostProperties = "";
        if(!properties.isEmpty()){
            setTotalAvailableProperties();
            setPropertiesAvgNoReviews();
            setRoomTypeInfo();
            setBoroughsInfo();
            setMostReviewedProperty();
            setHostMostProperties();
        }
    }

    /**
     * Calculate and load the total number of available properties.
     */
    private void setTotalAvailableProperties() {
        totalPropertiesAvailable = properties.size();
    }

    /**
     * Calculate and load the average number of reviews for all avaialble properties.
     */
    private void setPropertiesAvgNoReviews() {
        int totalReviews = 0;
        int counter = 0;
        for (AirbnbListing property : properties) {
            totalReviews = totalReviews + property.getNumberOfReviews();
            counter++;
        }
        propertiesAvgNoReviews = totalReviews / counter;
    }

    /**
     * Calculate and load the total number of private and non-private rooms.
     */
    private void setRoomTypeInfo() {
        int counterNonPrivate = 0;
        int counterPrivate = 0;
        for (AirbnbListing property : properties) {
            if (!property.getRoom_type().equals("Private room")) {
                counterNonPrivate++;
            } else {
                counterPrivate++;
            }
        }

        totalHomesAndApartments = counterNonPrivate;
        totalHotelsAndSimilar = counterPrivate;
    }

    /**
     * Calculate and load the most expensive and cheapest boroughs.
     */
    private void setBoroughsInfo() {
        // Create HashMap to link the boroughs to their total prices
        HashMap<String, Integer> boroughPrices = new HashMap<String, Integer>();
        for (AirbnbListing property : properties) {
            String neighbourhood = property.getNeighbourhood();
            if (boroughPrices.containsKey(neighbourhood)) {
                boroughPrices.replace(neighbourhood, boroughPrices.get(neighbourhood) + (property.getPrice() * property.getMinimumNights()));
            } else {
                boroughPrices.put(neighbourhood, (property.getPrice() * property.getMinimumNights()));
            }
        }

        // Use that HashMap to find which boroughs are most expensive and cheapest

        String mostExpensive = "";
        String cheapest = "";
        int increasingPrice = 0;
        int decreasingPrice = -1;
        for (String neighbourhood : boroughPrices.keySet()) {
            int currentPrice = boroughPrices.get(neighbourhood);
            if (currentPrice > increasingPrice) {
                mostExpensive = neighbourhood;
                increasingPrice = currentPrice;
            } else if (currentPrice == increasingPrice) {
                mostExpensive = mostExpensive + ", " + neighbourhood;
            }

            if (currentPrice < decreasingPrice || decreasingPrice < 0) {
                cheapest = neighbourhood;
                decreasingPrice = currentPrice;
            } else if (currentPrice == decreasingPrice) {
                cheapest = cheapest + ", " + neighbourhood;
            }
        }

        if(decreasingPrice == -1){
            decreasingPrice = 0;
        }

        mostExpensiveBorough = mostExpensive + " at: £" + increasingPrice;
        cheapestBorough = cheapest + " at: £" + decreasingPrice;
    }

    /**
     * Calculate and load the properties with the most reviews.
     */
    private void setMostReviewedProperty() {
        String mostReviewed = "";
        String currentReviewed = "";
        int mostReviews = 0;
        int currentReviews = 0;
        for (AirbnbListing property : properties) {
            currentReviewed = property.getName();
            currentReviews = property.getNumberOfReviews();
            if (currentReviews > mostReviews) {
                mostReviewed = currentReviewed;
                mostReviews = currentReviews;
            } else if (currentReviews == mostReviews) {
                mostReviewed = mostReviewed + ", " + currentReviewed;
            }
        }

        mostReviewedProperty = mostReviewed + " at: " + mostReviews;
    }

    /**
     * Claculate and load the host with the most properties.
     */
    private void setHostMostProperties() {
        // Create an Arraylist to link a host to their calculated listings count from the AirbnbListing class
        ArrayList<String> host_ids = new ArrayList<String>();
        String mostPropertiesHost = "";
        String currentHost_id = "";
        String currentHost_name = "";
        int mostProperties = 0;
        int currentProperties = 0;
        for (AirbnbListing property : properties) {
            currentHost_id = property.getHost_id();
            currentHost_name = property.getHost_name();
            if (!host_ids.contains(currentHost_id)) {
                host_ids.add(currentHost_id);
                currentProperties = property.getCalculatedHostListingsCount();
                if (currentProperties > mostProperties) {
                    mostPropertiesHost = currentHost_name;
                    mostProperties = currentProperties;
                } else if (currentProperties == mostProperties) {
                    mostPropertiesHost = mostPropertiesHost + ", " + currentHost_name;
                }
            }
        }

        hostMostProperties = mostPropertiesHost + " at: " + mostProperties;
    }

    /**
     * @return The total number of rpoperties available.
     */
    public int getTotalPropertiesAvailable() {
        return totalPropertiesAvailable;
    }

    /**
     * @return the average number of reviews for all available properties.
     */
    public int getPropertiesAvgNoReviews() {
        return propertiesAvgNoReviews;
    }

    /**
     * @return the total number of homes and apartments, as in non-private rooms.
     */
    public int getTotalHomesAndApartments() {
        return totalHomesAndApartments;
    }

    /**
     * @return the total number of hotels and similar, as in private rooms.
     */
    public int getTotalHotelsAndSimilar() {
        return totalHotelsAndSimilar;
    }

    /**
     * @return the most expensive borough.
     */
    public String getMostExpensiveBorough() {
        return mostExpensiveBorough;
    }

    /**
     * @return the cheapest borough.
     */
    public String getCheapestBorough() {
        return cheapestBorough;
    }

    /**
     * @return the property with most reviews.
     */
    public String getMostReviewedProperty() {
        return mostReviewedProperty;
    }

    /**
     * @return the host owning most properties.
     */
    public String getHostMostProperties() {
        return hostMostProperties;
    }
}