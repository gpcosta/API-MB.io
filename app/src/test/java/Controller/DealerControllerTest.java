package app.src.tests.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import app.src.main.constants.HttpConstants;
import app.src.main.controller.DealerController;
import app.src.main.entity.Dealer;
import app.src.main.Application;

/**
 * Test Class to the DealerController Class
 */
public class DealerControllerTest extends AbstractTest {
    private static Application app;
    private static DealerController dealerController;
    private static long numberCurrentTest = 0;

    /**
     * initialize the app and one DealerController
     */
    @BeforeClass
    public static void init()
    {
        app = new Application();
        app.file = "dataset.json";
        app.loadData();
        dealerController = new DealerController();
    }

    @Before
    public void beforeEachTest()
    {
        numberCurrentTest++;
        System.out.println("DealerController Test " + String.format("%03d", numberCurrentTest) + " begins");
    }

    @After
    public void afterEachTest()
    {
        System.out.println("DealerController Test " + String.format("%03d", numberCurrentTest) + " ends");
    }

    /**
     * test the closest Dealer function
     */
    @Test
    public void closestDealer()
    {
        double[] coords = {39, 10};
        JSONObject resultObtained = dealerController.getDealerByLocationAndVehicleAttributes(coords, null, null, null);
        JSONObject dealerExpected = app.dealers.get("bbcdbbad-5d0b-45ef-90ac-3581b997e063").getJson();
        JSONObject resultExpected = getResult(dealerExpected, HttpConstants.OK, "Ok");
        assertEquals(resultExpected, resultObtained);
    }

    /**
     * test the closest Dealer function with a vehicle with specified attributes
     */
    @Test
    public void closestDealerWithVehicleWithSpecifiedAttributes()
    {
        double[] coords = {39, 10};
        JSONObject resultObtained = dealerController.getDealerByLocationAndVehicleAttributes(coords, "E", "ELECTRIC", "AUTO");
        JSONObject dealerExpected = app.dealers.get("846679bd-5831-4286-969b-056e9c89d74c").getJson();
        JSONObject resultExpected = getResult(dealerExpected, HttpConstants.OK, "Ok");
        assertEquals(resultExpected, resultObtained);
    }

    /**
     * test the closest Dealer function with a vehicle with specified attributes (with bad parameters)
     */
    @Test
    public void closestDealerWithVehicleWithSpecifiedAttributesWithBadParameters()
    {
        double[] coords = {39, 10};
        JSONObject resultObtained = dealerController.getDealerByLocationAndVehicleAttributes(coords, "e", "ElECtRIc", "AUtO");
        JSONObject dealerExpected = app.dealers.get("846679bd-5831-4286-969b-056e9c89d74c").getJson();
        JSONObject resultExpected = getResult(dealerExpected, HttpConstants.OK, "Ok");
        assertEquals(resultExpected, resultObtained);
    }

    /**
     * test the sorted Dealers function
     */
    @Test
    public void sortedDealers()
    {
        double[] coords = {39, 10};
        JSONObject resultObtained = dealerController.getDealersByVehicleAttributesAndSortedByDistance(coords, null, null, null);
        JSONArray dealersExpected = new JSONArray();
        dealersExpected.add(app.dealers.get("d4f4d287-1ad6-4968-a8ff-e9e0009ad5d1").getJson());
        dealersExpected.add(app.dealers.get("846679bd-5831-4286-969b-056e9c89d74c").getJson());
        dealersExpected.add(app.dealers.get("bbcdbbad-5d0b-45ef-90ac-3581b997e063").getJson());
        JSONObject resultExpected = getResult(dealersExpected, HttpConstants.OK, "Ok");
        assertEquals(resultExpected, resultObtained);
    }

    /**
     * test the sorted Dealers function with a vehicle with specified attributes
     */
    @Test
    public void sortedDealersWithVehicleWithSpecifiedAttributes()
    {
        double[] coords = {39, 10};
        JSONObject resultObtained = dealerController.getDealersByVehicleAttributesAndSortedByDistance(coords, "E", "ELECTRIC", "AUTO");
        JSONArray dealersExpected = new JSONArray();
        dealersExpected.add(app.dealers.get("d4f4d287-1ad6-4968-a8ff-e9e0009ad5d1").getJson());
        dealersExpected.add(app.dealers.get("846679bd-5831-4286-969b-056e9c89d74c").getJson());
        JSONObject resultExpected = getResult(dealersExpected, HttpConstants.OK, "Ok");
        assertEquals(resultExpected, resultObtained);
    }

    /**
     * test the closest Dealer function with a vehicle with specified attributes (with bad parameters)
     */
    @Test
    public void sortedDealersWithVehicleWithSpecifiedAttributesWithBadParameters()
    {
        double[] coords = {39, 10};
        JSONObject resultObtained = dealerController.getDealersByVehicleAttributesAndSortedByDistance(coords, "E", "ELEcTRiC", "AUtO");
        JSONArray dealersExpected = new JSONArray();
        dealersExpected.add(app.dealers.get("d4f4d287-1ad6-4968-a8ff-e9e0009ad5d1").getJson());
        dealersExpected.add(app.dealers.get("846679bd-5831-4286-969b-056e9c89d74c").getJson());
        JSONObject resultExpected = getResult(dealersExpected, HttpConstants.OK, "Ok");
        assertEquals(resultExpected, resultObtained);
    }

    /**
     * test the function that returns all the Dealers inside a polygon
     */
    @Test
    public void dealersInsidePolygon()
    {
        double[] coords = {45.039, 10.383, 37.87657, -20.54657, 25, 7};
        JSONObject resultObtained = dealerController.getDealersByVehicleAttributesAndInsidePolygon(coords, null, null, null);
        JSONArray dealersExpected = new JSONArray();
        dealersExpected.add(app.dealers.get("bbcdbbad-5d0b-45ef-90ac-3581b997e063").getJson());
        dealersExpected.add(app.dealers.get("846679bd-5831-4286-969b-056e9c89d74c").getJson());
        JSONObject resultExpected = getResult(dealersExpected, HttpConstants.OK, "Ok");
        assertEquals(resultExpected, resultObtained);
    }

    /**
     * test the function that returns all the Dealers inside a polygon with a vehicle with
     * specified attributes
     */
    @Test
    public void dealersInsidePolygonWithVehicleWithSpecifiedAttributes()
    {
        double[] coords = {45.039, 10.383, 37.87657, -20.54657, 25, 7};
        JSONObject resultObtained = dealerController.getDealersByVehicleAttributesAndInsidePolygon(coords, "E", "GASOLINE", "MANUAL");
        JSONArray dealersExpected = new JSONArray();
        dealersExpected.add(app.dealers.get("bbcdbbad-5d0b-45ef-90ac-3581b997e063").getJson());
        JSONObject resultExpected = getResult(dealersExpected, HttpConstants.OK, "Ok");
        assertEquals(resultExpected, resultObtained);
    }

    /**
     * test the function that returns all the Dealers inside a polygon with a vehicle
     * with specified attributes (with bad parameters)
     */
    @Test
    public void dealersInsidePolygonWithVehicleWithSpecifiedAttributesWithBadParameters()
    {
        double[] coords = {45.039, 10.383, 37.87657, -20.54657, 25, 7};
        JSONObject resultObtained = dealerController.getDealersByVehicleAttributesAndInsidePolygon(coords, "E", "GAsOLINe", "mANUAL");
        JSONArray dealersExpected = new JSONArray();
        dealersExpected.add(app.dealers.get("bbcdbbad-5d0b-45ef-90ac-3581b997e063").getJson());
        JSONObject resultExpected = getResult(dealersExpected, HttpConstants.OK, "Ok");
        assertEquals(resultExpected, resultObtained);
    }
}
