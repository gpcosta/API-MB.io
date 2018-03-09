package app.src.main.entity;

import org.json.simple.JSONObject;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import app.src.main.entity.Booking;
import app.src.main.entity.Dealer;

/**
 * Vehicle Class - representation of a vehicle
 */
public class Vehicle
{
    /**
     * @var String  Unique identifier for a vehicle
     */
    private String id;
    /**
     * @var String  Vehicle model
     */
    private String model;
    /**
     * @var String  Vehicle fuel type
     */
    private String fuel;
    /**
     * @var String  Vehicle transmission
     */
    private String transmission;
    /**
     * @var Map<String, List<String>>  Days of the week (and their respective hours)
     * that the vehicle is available
     */
    private Map<String, List<String>> availability;
    /**
     * @var String  Identifier of the Dealer that owns the vehicle
     */
    private String dealerId;
    /**
     * @var List<String>  All identifiers of the bookings for the vehicle
     */
    private List<String> bookingsId;

    public Vehicle(String id, String model, String fuel, String transmission, Map<String, List<String>> availability)
    {
        this.id = id;
        // avoid bad parameters
        this.model = model.toUpperCase();
        this.fuel = fuel.toUpperCase();
        this.transmission = transmission.toUpperCase();
        this.availability = availability;
        this.bookingsId = new ArrayList<String>();
    }

    public String getId()
    {
        return this.id;
    }

    public void setModel(String model)
    {
        this.model = model;
    }

    public String getModel()
    {
        return this.model;
    }

    public void setFuel(String fuel)
    {
        this.fuel = fuel;
    }

    public String getFuel()
    {
        return this.fuel;
    }

    public void setTransmission(String transmission)
    {
        this.transmission = transmission;
    }

    public String getTransmission()
    {
        return this.transmission;
    }

    public boolean checkAvailability(String day, String time)
    {
        if (this.availability != null) {
            // will go through all days in this.availability variable
            for (String key : this.availability.keySet()) {
                if (key.equals(day)) {
                    // will go through all hours in this.availability.get(key)
                    for (String t : this.availability.get(key)) {
                        if (t.equals(time)) return true;
                    }
                }
            }
        }

        return false;
    }

    public void setAvailability(Map<String, List<String>> availability)
    {
        this.availability = availability;
    }

    public Map<String, List<String>> getAvailability()
    {
        return this.availability;
    }

    public void setDealerId(String dealerId)
    {
        this.dealerId = dealerId;
    }

    public String getDealerId()
    {
        return this.dealerId;
    }

    public void addBookingId(String bookingId)
    {
        this.bookingsId.add(bookingId);
    }

    public void setBookingsId(List<String> bookingsId)
    {
        this.bookingsId = bookingsId;
    }

    public List<String> getBookingsId()
    {
        return this.bookingsId;
    }

    /**
     * @return JSONObject - will have all the important things about Vehicle
     */
    public JSONObject getJson()
    {
        JSONObject result = new JSONObject();
        List<JSONObject> bookings = new ArrayList<JSONObject>();
        result.put("id", this.id);
        result.put("model", this.model);
        result.put("fuel", this.fuel);
        result.put("transmission", this.transmission);
        result.put("availability", this.availability);
        result.put("dealer", this.dealerId);
        /*for (String bookingId : this.bookingsId) bookings.add(allBookings.get(bookingId).getJson());
        result.put("bookings", bookings);*/
        return result;
    }
}
