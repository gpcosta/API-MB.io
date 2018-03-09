package app.src.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import app.src.main.constants.HttpConstants;
import app.src.main.controller.AbstractController;
import app.src.main.entity.Dealer;
import app.src.main.entity.Vehicle;
import app.src.main.entity.Booking;

/**
 * main class that made the API run
 * in this class, will also be loaded the data from the json file
 */
@SpringBootApplication
public class Application
{
    public static Map<String, Dealer> dealers;
    public static Map<String, Vehicle> vehicles;

    // each key will be a property of Vehicle that it's possible to list
    // each value will be a Map that will have a key with the options of the property
    // each option will has an ArrayList with several vehicleIds
    public static Map<String, Map<String, List<String>>> vehiclesBy;

    public static Map<String, Booking> bookings;

    // json file to load
    @Value("${file}")
    public static String file;

    // format used to save dates
    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Application()
    {
        dealers = new HashMap<String, Dealer>();
        vehicles = new HashMap<String, Vehicle>();

        // each key will be a property of Vehicle that it's possible to list
        // each value will be a Map that will have a key with the options of the property
        // each option will has an ArrayList with several vehicleIds
        vehiclesBy = new HashMap<String, Map<String, List<String>>>();
        vehiclesBy.put("model", new HashMap<String, List<String>>());
        vehiclesBy.put("fuel", new HashMap<String, List<String>>());
        vehiclesBy.put("transmission", new HashMap<String, List<String>>());

        bookings = new HashMap<String, Booking>();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        // if it is not specified, the default is "dataset.json"
        if (file == null) file = "dataset.json";

        loadData();
    }

    /**
     * @return JSONObject - tells the user if it is successful
     */
    public static void loadData()
    {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(file));
            // JSONObject that contains all json file
            JSONObject jsonObject = (JSONObject) obj;
            processDealers(jsonObject);
            processBookings(jsonObject);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException | java.text.ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param JSONObject jsonObject - file to be processed
     *
     * @return Map<String, Dealer> - return all Dealers already processed
     */
    private static void processDealers(JSONObject jsonObject)
    {
        // JSONArray that contains all dealers
        JSONArray jsonDealers  = (JSONArray) jsonObject.get("dealers");
        for (Object d : jsonDealers) {
            JSONObject jsonDealer = (JSONObject) d;
            JSONArray jsonClosed = (JSONArray) jsonDealer.get("closed");
            List<String> closed = new ArrayList<String>();
            for (Object c : jsonClosed) closed.add((String) c);

            // will process all the vehicles that this Dealer has
            List<String> vehiclesId = processThatDealerVehicles(jsonDealer);
            // create a new Dealer
            dealers.put((String) jsonDealer.get("id"), new Dealer((String) jsonDealer.get("id"), (String) jsonDealer.get("name"), (double) jsonDealer.get("latitude"),
                (double) jsonDealer.get("longitude"), closed, vehiclesId));
        }
    }

    /**
     * @param JSONObject jsonDealer
     *
     * @return List<String> - all vehicles ids that belong to the Dealer provided
     */
    private static List<String> processThatDealerVehicles(JSONObject jsonDealer)
    {
        JSONArray jsonVehicles = (JSONArray) jsonDealer.get("vehicles");
        List<String> dealerVehicles = new ArrayList<String>();
        for (Object v : jsonVehicles) {
            JSONObject jsonVehicle = (JSONObject) v;
            JSONObject jsonAvailability = (JSONObject) jsonVehicle.get("availability");

            // create a map that contains the availability of the vehicle
            Map<String, List<String>> availability = new HashMap<String, List<String>>();
            for(Object k : jsonAvailability.keySet()) {
                String key = (String) k;
                JSONArray jsonDay = (JSONArray) jsonAvailability.get(key);
                List<String> day = new ArrayList<String>();
                for (Object h : jsonDay) {
                    day.add((String) h);
                }
                availability.put(key, day);
            }

            // create new Vehicle
            Vehicle vehicle = new Vehicle((String) jsonVehicle.get("id"), (String) jsonVehicle.get("model"),
                (String) jsonVehicle.get("fuel"), (String) jsonVehicle.get("transmission"), availability);
            dealerVehicles.add(vehicle.getId());
            vehicle.setDealerId((String) jsonDealer.get("id"));

            // the next 3 lines will categorize the current vehicle
            categorizeVehicleBy(vehicle, "model", vehicle.getModel());
            categorizeVehicleBy(vehicle, "fuel", vehicle.getFuel());
            categorizeVehicleBy(vehicle, "transmission", vehicle.getTransmission());

            // put the processed vehicle in the public variable vehicles
            vehicles.put(vehicle.getId(), vehicle);
        }

        return dealerVehicles;
    }

    /**
     * @param Vehicle vehicle
     * @param String categorizeBy - string by which the vehicle will be categorized
     *                              example: "model"
     * @param String category - example: "AMG"
     *
     * will store the id of the vehicle in the right category
     */
    private static void categorizeVehicleBy(Vehicle vehicle, String categorizeBy, String category)
    {
        // get all vehicles organized by categories
        Map<String, List<String>> vehiclesByCategory = vehiclesBy.get(categorizeBy);
        if (vehiclesByCategory != null) {
            // get all vehicles that belong to that category
            List<String> vehiclesByThatCategory = vehiclesByCategory.get(category);
            if (vehiclesByThatCategory != null) vehiclesByThatCategory.add(vehicle.getId());
            else {
                List<String> temp = new ArrayList<String>();
                temp.add(vehicle.getId());
                vehiclesByCategory.put(category, temp);
            }
        }
    }

    /**
     * @param JSONObject jsonObject - file to be processed
     *
     * @throws java.text.ParseException
     *
     * @return Map<String, Booking> - processed bookings
     */
    private static void processBookings(JSONObject jsonObject) throws java.text.ParseException
    {
        // get all bookings to be processed
        JSONArray jsonBookings  = (JSONArray) jsonObject.get("bookings");
        for (Object b : jsonBookings) {
            JSONObject jsonBooking = (JSONObject) b;
            String bookingId = (String) jsonBooking.get("id");

            // format dates that can throw java.text.ParseException
            Date pickupDate = format.parse(((String) jsonBooking.get("pickupDate")).replace('T', ' '));
            Date createdAt = format.parse(((String) jsonBooking.get("createdAt")).replace('T', ' '));

            // create new Booking
            Booking booking = new Booking(bookingId, (String) jsonBooking.get("firstName"),
                (String) jsonBooking.get("lastName"), (String) jsonBooking.get("vehicleId"),
                pickupDate, createdAt);

            // know if the current Booking is over booked
            boolean isOverBooked = false;
            String actualVehicleId = booking.getVehicleId();
            Date actualDate = booking.getPickupDate();
            // determine if the current booking is over booked and sets the correct variables
            for (String id : vehicles.get(booking.getVehicleId()).getBookingsId()) {
                Booking forBooking = bookings.get(id);
                Date dateToCompare = forBooking.getPickupDate();
                if (actualDate.equals(dateToCompare)) {
                    // if it reaches this place, it means that booking is over booked
                    isOverBooked = true;
                    // this is true if the forBooking is the last of the list of bookings for that vehicle for that date
                    if (forBooking.getNextBookingId() == null) {
                        // so, the current booking will be the new last
                        forBooking.setNextBookingId(booking.getId());
                        // booking is not certainly the main booking of this vehicle
                        booking.setIsMainBooking(false);
                        break;
                    }
                }
            }

            if (!isOverBooked) booking.setIsMainBooking(true);

            // FIXME
            //if (jsonBooking.has("cancelledAt")) booking.setCancelledAt((String) jsonBooking.get("cancelledAt"));
            //if (jsonBooking.has("cancelledReason")) booking.setCancelledReason((String) jsonBooking.get("cancelledReason"));
            bookings.put(bookingId, booking);
            // save in the right vehicle that this booking is associated to it
            vehicles.get(booking.getVehicleId()).addBookingId(bookingId);
        }
    }
}
