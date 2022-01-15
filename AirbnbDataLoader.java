import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import com.opencsv.CSVReader;

import java.net.URISyntaxException;
import java.util.HashMap;

public class AirbnbDataLoader {
    /**
     * First index holds the list of all properties of determined price range
     * Second index holds a map of boroughs and their occurrences
     * @param fromPrice User selected from price.
     * @param toPrice User selected to price.
     * @return an object array of list and map
     */
    public Object[] load(int fromPrice, int toPrice) {
        System.out.print("Begin loading Airbnb london dataset...");

        ArrayList<AirbnbListing> listings = new ArrayList<AirbnbListing>();
        HashMap numberOfOccurances = new HashMap<String, Integer>();
        
        try{
            URL url = getClass().getResource("airbnb-london.csv");
            CSVReader reader = new CSVReader(new FileReader(new File(url.toURI()).getAbsolutePath()));
            String [] line;
            //skip the first row (column headers)
            reader.readNext();
            while ((line = reader.readNext()) != null) {
                if(!(convertInt(line[8]) > fromPrice && convertInt(line[8]) < toPrice)) {continue;}
                //From and to prices are exclusive
                //only admitted prices are admitted and added to the listing
                String id = line[0];
                String name = line[1];
                String host_id = line[2];
                String host_name = line[3];
                String neighbourhood = line[4];
                double latitude = convertDouble(line[5]);
                double longitude = convertDouble(line[6]);
                String room_type = line[7];
                int price = convertInt(line[8]); // Price is is here
                int minimumNights = convertInt(line[9]);
                int numberOfReviews = convertInt(line[10]);
                String lastReview = line[11];
                double reviewsPerMonth = convertDouble(line[12]);
                int calculatedHostListingsCount = convertInt(line[13]);
                int availability365 = convertInt(line[14]);

                //first
                AirbnbListing listing = new AirbnbListing(id, name, host_id,
                        host_name, neighbourhood, latitude, longitude, room_type,
                        price, minimumNights, numberOfReviews, lastReview,
                        reviewsPerMonth, calculatedHostListingsCount, availability365
                );
                listings.add(listing);

                //second
                if(numberOfOccurances.containsKey(neighbourhood)){
                    numberOfOccurances.replace(neighbourhood, (Integer) numberOfOccurances.get(neighbourhood) + 1);
                } else {
                    numberOfOccurances.put(neighbourhood, 1);
                }
            }
        } catch(IOException | URISyntaxException e){
            System.out.println("Failure! Something went wrong");
            e.printStackTrace();
        }
        System.out.println("Success! Number of loaded records: " + listings.size());

        Object[] result = {
                            listings, numberOfOccurances
                        };
        return result;
    }

    /**
     * @param doubleString the string to be converted to Double type
     * @return the Double value of the string, or -1.0 if the string is 
     * either empty or just whitespace
     */
    private Double convertDouble(String doubleString){
        if(doubleString != null && !doubleString.trim().equals("")){
            return Double.parseDouble(doubleString);
        }
        return -1.0;
    }

    /**
     * @param intString the string to be converted to Integer type
     * @return the Integer value of the string, or -1 if the string is 
     * either empty or just whitespace
     */
    private Integer convertInt(String intString){
        if(intString != null && !intString.trim().equals("")){
            return Integer.parseInt(intString);
        }
        return -1;
    }
}