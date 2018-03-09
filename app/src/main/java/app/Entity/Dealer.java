package app.src.main.entity;

import org.json.simple.JSONObject;
import java.util.List;
import java.util.ArrayList;
import app.src.main.entity.Vehicle;

/**
 * Dealer Class - representation of a Dealer
 */
public class Dealer
{
    /**
     * @var String  Unique identifier for a dealer
     */
    private String id;
    /**
     * @var String  Dealer name
     */
    private String name;
    /**
     * @var double  Dealer latitude
     */
    private double latitude;
    /**
     * @var double  Dealer longitude
     */
    private double longitude;
    /**
     * @var List<String>  Days of the week that the dealership is closed
     */
    private List<String> closed;
    /**
     * @var List<String>  All identifiers of the Dealer's vehicles
     */
    private List<String> vehiclesId;

    public Dealer(String id, String name, double latitude, double longitude, List<String> closed, List<String> vehiclesId)
    {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.closed = closed;
        this.vehiclesId = vehiclesId;
    }

    public String getId()
    {
        return this.id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public double getLatitude()
    {
        return this.latitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public double getLongitude()
    {
        return this.longitude;
    }

    public void setClosed(List<String> closed)
    {
        this.closed = closed;
    }

    public List<String> getClosed()
    {
        return this.closed;
    }

    public void addVehicleId(String vehicleId)
    {
        this.vehiclesId.add(vehicleId);
    }

    public void setVehiclesId(List<String> vehiclesId)
    {
        this.vehiclesId = vehiclesId;
    }

    public List<String> getVehiclesId()
    {
        return this.vehiclesId;
    }

    /**
     * @return JSONObject - will have all the important things about Dealer
     */
    public JSONObject getJson()
    {
        JSONObject result = new JSONObject();
        result.put("id", this.id);
        result.put("name", this.name);
        result.put("latitude", this.latitude);
        result.put("longitude", this.longitude);
        result.put("closed", this.closed);
        return result;
    }
}
