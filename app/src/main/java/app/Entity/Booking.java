package app.src.main.entity;

import org.json.simple.JSONObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import app.src.main.entity.Vehicle;
import app.src.main.entity.Dealer;

/**
 * Booking Class - representation of a booking
 */
public class Booking
{
    /**
     * @var String  Unique identifier for a booking
     */
    private String id;

    /**
     * @var string  Vehicle identifier
     */
    private String vehicleId;

    /**
     * @var string  Customer's first name
     */
    private String firstName;

    /**
     * @var string  Customer's last name
     */
    private String lastName;

    /**
     * @var Date  Day and time of the booking
     */
    private Date pickupDate;

    /**
     * @var Date  Day and Time that the booking entry was created
     */
    private Date createdAt;

    /**
     * @var Date 	Day and Time that this booking was cancelled
     */
    private Date cancelledAt;

    /**
     * @var String 	Reason for the booking cancelation
     */
    private String cancelledReason;

    /**
    * @var boolean  Know if this Booking is the main Booking of the vehicle (true)
    * or a secondary Booking (false)
    */
    private boolean isMainBooking;

    /**
     * @var String  Id of the next booking that has the same vehicle and date
     */
    private String nextBookingId;

    public Booking(String id, String firstName, String lastName, String vehicleId, Date pickupDate, Date createdAt)
    {
        this.id = id;
        this.vehicleId = vehicleId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pickupDate = pickupDate;
        this.createdAt = createdAt;
    }

    public Booking(String id, String firstName, String lastName, String vehicleId, Date pickupDate, Date createdAt,
    String nextBookingId)
    {
        this(id, firstName, lastName, vehicleId, pickupDate, createdAt);
        this.nextBookingId = nextBookingId;
    }

    public String getId()
    {
        return this.id;
    }

    public void setVehicleId(String vehicleId)
    {
        this.vehicleId = vehicleId;
    }

    public String getVehicleId()
    {
        return this.vehicleId;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getFirstName()
    {
        return this.firstName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getLastName()
    {
        return this.lastName;
    }

    public void setPickupDate(Date pickupDate)
    {
        this.pickupDate = pickupDate;
    }

    public Date getPickupDate()
    {
        return this.pickupDate;
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt()
    {
        return this.createdAt;
    }

    public void setCancelledAt(Date cancelledAt)
    {
        this.cancelledAt = cancelledAt;
    }

    public Date getCancelledAt()
    {
        return this.cancelledAt;
    }

    public void setCancelledReason(String cancelledReason)
    {
        this.cancelledReason = cancelledReason;
    }

    public String getCancelledReason()
    {
        return this.cancelledReason;
    }

    public void setIsMainBooking(boolean isMainBooking)
    {
        this.isMainBooking = isMainBooking;
    }

    public boolean isMainBooking()
    {
        return this.isMainBooking;
    }

    public void setNextBookingId(String nextBookingId)
    {
        this.nextBookingId = nextBookingId;
    }

    public String getNextBookingId()
    {
        return this.nextBookingId;
    }

    /**
     * @return JSONObject - will have all the important things about Booking
     */
    public JSONObject getJson()
    {
        // format used to save dates
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        JSONObject result = new JSONObject();
        result.put("id", this.id);
        result.put("firstName", this.firstName);
        result.put("lastName", this.lastName);
        result.put("pickupDate", format.format(this.pickupDate));
        result.put("createdAt", format.format(this.createdAt));
        // if it was already cancelled, will be added to the result cancelledAt and cancelledReason properties
        if (this.cancelledAt != null) {
            result.put("cancelledAt", format.format(this.cancelledAt));
            result.put("cancelledReason", this.cancelledReason);
        }
        result.put("isMainBooking", this.isMainBooking);
        result.put("vehicleId", this.vehicleId);
        return result;
    }
}
