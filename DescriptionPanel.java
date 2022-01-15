import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URI;

/**
 * A new window of the selected property.
 *
 * Displays the description of the selected property
 * as well as a google map link to it's location.
 *
 * @author Berke Muftuoglu, Jakub Mrozcek, Zhen-Chien Ong, Mairaj Khalid
 * @version 08/04/2021
 */
public class DescriptionPanel
{
    //FXML components
    @FXML
    private Label descLabel;
    @FXML
    private Button button;

    private double propertyLatitude;
    private double propertyLongitude;

    /**
     * Constructor
     */
    public DescriptionPanel() {
    }

    /**
     * Initialize components
     */
    @FXML
    private void initialize() {
    }

    /**
     * This is called from the PropertyList class to pass in the description of
     * the selected property to be displayed
     */
    public void setDescLabel(String desc) {
        descLabel.setText(desc);
    }

    /**
     * Sets the latitude and longitude of the selected property
     * @param latitude Latitude of the selected property
     * @param longitude Longitude of the selected property
     */
    public void setLocation(double latitude, double longitude) {
        propertyLatitude = latitude;
        propertyLongitude = longitude;
    }

    /**
     * Opens a Google maps page of the selected property location in the user's browser
     */
    @FXML
    public void openMap() throws Exception
    {
        URI uri = new URI("https://www.google.com/maps/place/" + propertyLatitude + "," + propertyLongitude);
        java.awt.Desktop.getDesktop().browse(uri);
    }
}
