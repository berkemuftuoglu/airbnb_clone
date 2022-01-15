import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * The test class StatisticsPanelInfoTest.
 *
 * @author  Berke Muftuoglu, Jakub Mrozcek, Zhen-Chien Ong, Mairaj Khalid
 * @version 08/04/2021
 */
public class StatisticsPanelInfoTest
{
    /**
     * Default constructor for test class StatisticsPanelInfoTest
     */
    public StatisticsPanelInfoTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp()
    {
    }

    /**
     * Tests basic initialization, with just 5 values, with error conditions input.
     * Certain values are repeating, to check for issues.
     */
    @Test
    public void testBasicInitialization()
    {
        ArrayList<AirbnbListing> properties = new ArrayList<AirbnbListing>();
        properties.add(new AirbnbListing("1","a","1","a","a", 0.0, 0.0,"Private room", 5, 2, 3, "a", 2, 5, 10));
        properties.add(new AirbnbListing("2","b","1","a","b", 0.0, 0.0,"non Private room", 6, 4, 4, "a", 5, 5, 10));
        properties.add(new AirbnbListing("3","c","2","b","c", 0.0, 0.0,"non Private room", 10, 6, 6, "a", 3, 7, 10));
        properties.add(new AirbnbListing("4","d","2","b","c", 0.0, 0.0,"Private room", 5, 4, 3, "a", 1, 7, 10));
        properties.add(new AirbnbListing("5","e","2","b","d", 0.0, 0.0,"Private room", 5, 2, 3, "a", 4, 7, 10));
        StatisticsPanelInfo info = new StatisticsPanelInfo(properties);
        assertEquals(5, info.getTotalPropertiesAvailable());
        assertEquals(3, info.getPropertiesAvgNoReviews());
        assertEquals(2, info.getTotalHomesAndApartments());
        assertEquals(3, info.getTotalHotelsAndSimilar());
        assertEquals("c at: £80", info.getMostExpensiveBorough());
        assertEquals("a, d at: £10", info.getCheapestBorough());
        assertEquals("c at: 6", info.getMostReviewedProperty());
        assertEquals("b at: 7", info.getHostMostProperties());
    }
    
    /**
     * Test with an empty properties list.
     */
    @Test
    public void testEmptyListInput()
    {
        ArrayList<AirbnbListing> properties = new ArrayList<AirbnbListing>();
        StatisticsPanelInfo info = new StatisticsPanelInfo(properties);
        assertEquals(0, info.getTotalPropertiesAvailable());
        assertEquals(0, info.getPropertiesAvgNoReviews());
        assertEquals(0, info.getTotalHomesAndApartments());
        assertEquals(0, info.getTotalHotelsAndSimilar());
        assertEquals("", info.getMostExpensiveBorough());
        assertEquals("", info.getCheapestBorough());
        assertEquals("", info.getMostReviewedProperty());
        assertEquals("", info.getHostMostProperties());
    }
    
    /**
     * Test if null is input into initialization.
     * This should throw error.
     */
    @Test /*(expected=NullPointerException.class)*/
    public void testNullInput()
    {
        StatisticsPanelInfo info = new StatisticsPanelInfo(null);
    }
    
    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @AfterEach
    public void tearDown()
    {
    }
}
