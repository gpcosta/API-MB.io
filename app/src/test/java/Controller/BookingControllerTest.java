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
import app.src.main.constants.HttpConstants;
import app.src.main.controller.BookingController;
import app.src.main.entity.Booking;
import app.src.main.Application;

/**
 * Test Class to the BookingController Class
 */
public class BookingControllerTest extends AbstractTest {
    private static Application app;
    private static BookingController bookingController;
    private static long numberCurrentTest = 0;

    /**
     * initialize the app and one BookingController
     */
    @BeforeClass
    public static void init()
    {
        app = new Application();
        app.file = "dataset.json";
        app.loadData();
        bookingController = new BookingController();
    }

    @Before
    public void beforeEachTest()
    {
        numberCurrentTest++;
        System.out.println("BookingController Test " + String.format("%03d", numberCurrentTest) + " begins");
    }

    @After
    public void afterEachTest()
    {
        System.out.println("BookingController Test " + String.format("%03d", numberCurrentTest) + " ends");
    }

    /**
     * test the creation of a new booking
     */
    @Test
    public void createNewBooking()
    {
        // simulates that the current id already is 0
        bookingController.setId(0);
        JSONObject resultObtained = bookingController.newBooking("Goncalo", "Costa", "f0bd3390-58ae-4510-987e-a5bfe14973ff", "2018-03-12T10:00:00", "2018-03-11T08:00:00");
        JSONObject bookingExpected = new JSONObject();
        bookingExpected.put("id", "1");
        bookingExpected.put("firstName", "Goncalo");
        bookingExpected.put("lastName", "Costa");
        bookingExpected.put("pickupDate", "2018-03-12T10:00:00");
        bookingExpected.put("createdAt", "2018-03-11T08:00:00");
        bookingExpected.put("isMainBooking", true);
        bookingExpected.put("vehicleId", "f0bd3390-58ae-4510-987e-a5bfe14973ff");
        JSONObject resultExpected = getResult(bookingExpected, HttpConstants.CREATED, "Ok");
        assertEquals(resultExpected, resultObtained);
    }

    /**
     * test the creation of two new bookings for the same vehicle and
     * same time slot
     */
    @Test
    public void createNewBookingAfterAnotherForTheSameSlotAndVehicle()
    {
        // simulates that the current id already is 1
        bookingController.setId(1);
        bookingController.newBooking("Goncalo", "Costa", "f0bd3390-58ae-4510-987e-a5bfe14973ff", "2018-03-12T10:00:00", "2018-03-11T08:00:00");
        JSONObject resultObtained = bookingController.newBooking("Goncalo", "Costa", "f0bd3390-58ae-4510-987e-a5bfe14973ff", "2018-03-12T10:00:00", "2018-03-11T08:00:00");
        JSONObject bookingExpected = new JSONObject();
        bookingExpected.put("id", "3");
        bookingExpected.put("firstName", "Goncalo");
        bookingExpected.put("lastName", "Costa");
        bookingExpected.put("pickupDate", "2018-03-12T10:00:00");
        bookingExpected.put("createdAt", "2018-03-11T08:00:00");
        bookingExpected.put("isMainBooking", false);
        bookingExpected.put("vehicleId", "f0bd3390-58ae-4510-987e-a5bfe14973ff");
        JSONObject resultExpected = getResult(bookingExpected, HttpConstants.CREATED, "Ok");
        assertEquals(resultExpected, resultObtained);
    }

    /**
     * test the cancellation of one booking
     */
    @Test
    public void cancelBooking()
    {
        JSONObject resultObtained = bookingController.cancelBooking("9036592a-64fd-43d0-8970-b64a56e43d53", "2018-03-12T10:00:00", "Just a test");
        JSONObject cancelled = new JSONObject();
        cancelled.put("id", "9036592a-64fd-43d0-8970-b64a56e43d53");
        cancelled.put("firstName", "Abdullah");
        cancelled.put("lastName", "Little");
        cancelled.put("vehicleId", "893d97bf-5a9d-4926-ace3-39ad0585c912");
        cancelled.put("pickupDate", "2018-03-04T10:30:00");
        cancelled.put("createdAt", "2018-02-26T08:42:46");
        cancelled.put("cancelledAt", "2018-03-12T10:00:00");
        cancelled.put("cancelledReason", "Just a test");
        cancelled.put("isMainBooking", false);
        JSONObject nextBooking = new JSONObject();
        JSONObject result = new JSONObject();
        result.put("cancelled", cancelled);
        result.put("nextBooking", nextBooking);
        JSONObject resultExpected = getResult(result, HttpConstants.OK, "Ok");
        assertEquals(resultExpected, resultObtained);
    }

    /**
     * test the cancellation of one booking with overbooking
     */
    @Test
    public void cancelBookingWithOverBooking()
    {
        JSONObject resultObtained = bookingController.cancelBooking("1c6bd910-12b1-45d6-b4d8-cdff2f37db90", "2018-03-12T10:00:00", "Just a test");
        JSONObject cancelled = new JSONObject();
        cancelled.put("id", "1c6bd910-12b1-45d6-b4d8-cdff2f37db90");
        cancelled.put("firstName", "Joanna");
        cancelled.put("lastName", "Randolph");
        cancelled.put("vehicleId", "44a36bfa-ec8f-4448-b4c2-809203bdcb9e");
        cancelled.put("pickupDate", "2018-03-03T10:30:00");
        cancelled.put("createdAt", "2018-02-26T08:42:46");
        cancelled.put("cancelledAt", "2018-03-12T10:00:00");
        cancelled.put("cancelledReason", "Just a test");
        cancelled.put("isMainBooking", false);
        JSONObject nextBooking = new JSONObject();
        nextBooking.put("id", "a78cd580-817a-4fda-91cc-7868a79b5948");
        nextBooking.put("firstName", "Justine");
        nextBooking.put("lastName", "Hartman");
        nextBooking.put("vehicleId", "44a36bfa-ec8f-4448-b4c2-809203bdcb9e");
        nextBooking.put("pickupDate", "2018-03-03T10:30:00");
        nextBooking.put("createdAt", "2018-02-26T08:42:46");
        nextBooking.put("isMainBooking", true);
        JSONObject result = new JSONObject();
        result.put("cancelled", cancelled);
        result.put("nextBooking", nextBooking);
        JSONObject resultExpected = getResult(result, HttpConstants.OK, "Ok");
        assertEquals(resultExpected, resultObtained);
    }
}
