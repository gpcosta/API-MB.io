package app.src.main.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Map;
import java.util.HashMap;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Calendar;
import org.json.simple.JSONObject;
import app.src.main.constants.HttpConstants;
import app.src.main.controller.AbstractController;
import app.src.main.entity.Booking;
import app.src.main.entity.Vehicle;
import app.src.main.entity.Booking;
import app.src.main.Application;

/**
 * BookingController makes all the operations on the bookings
 */
@RestController
@RequestMapping("/bookings")
public class BookingController extends AbstractController
{
    // all days of the week
    private String[] daysOfWeek = {"sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"};
    // id that will be used to create a new booking
    private final AtomicLong id = new AtomicLong();

    /**
     * @param String firstName (RequestParam)
     * @param String lastName (RequestParam)
     * @param String vehicleId (RequestParam)
     * @param String pickupDateString (RequestParam)
     * @param String createdAtString (RequestParam)
     *
     * @see app.controller.AbstractController - getResult()
     * @return JSONObject (when successful, "result" field in the response is a JSONObject
     * with all the important fields of the booking)
     *
     * example: http://localhost:8080/bookings/new?firstName=Goncalo&lastName=Costa&vehicleId=768a73af-4336-41c8-b1bd-76bd700378ce&pickupDate=2018-02-26T10:00:00&createdAt=2018-02-02T00:43:00
     */
    @RequestMapping("/new")
    public JSONObject newBooking(@RequestParam(value="firstName") String firstName, @RequestParam(value="lastName") String lastName,
    @RequestParam(value="vehicleId") String vehicleId, @RequestParam(value="pickupDate") String pickupDateString,
    @RequestParam(value="createdAt") String createdAtString)
    {
        if (firstName != null && lastName != null && vehicleId != null && pickupDateString != null) {
            // requested vehicle
            Vehicle vehicle = Application.vehicles.get(vehicleId);
            if (vehicle != null) {
                try {
                    // dates parsing
                    Date pickupDate = format.parse(pickupDateString.replace('T', ' '));
                    // in the case of the createdAtString == null, createdAt will have the current date
                    Date createdAt;
                    if (createdAtString != null) createdAt = format.parse(createdAtString.replace('T', ' '));
                    else createdAt = format.parse(format.format(Calendar.getInstance().getTime()));

                    // will only be possible to create a new booking if pickupDate is after of createdAt
                    if (pickupDate.getTime() > createdAt.getTime()) {
                        Booking booking = createBooking(vehicle, firstName, lastName, pickupDate, createdAt);
                        if (booking != null) {
                            Application.bookings.put(booking.getId(), booking);
                            vehicle.addBookingId(booking.getId());
                            return getResult(booking.getJson(), HttpConstants.CREATED, "Ok");
                        } else {
                            return getResult(new JSONObject(), HttpConstants.NO_CONTENT, "Vehicle not available: The vehicle that you choose are not available for this time slot.");
                        }
                    } else {
                        return getResult(new JSONObject(), HttpConstants.BAD_REQUEST, "Unsupported date: Your pickup date is before the date of creation of the booking.");
                    }
                } catch(java.text.ParseException e) {
                    return getResult(new JSONObject(), HttpConstants.BAD_REQUEST, "Error parsing: Please, check the parameters provided.");
                }
            }

            return getResult(new JSONObject(), HttpConstants.NOT_FOUND, "Vehicle not found: It is not possible to make a reservation for such vehicle.");
        }
        return getResult(new JSONObject(), HttpConstants.BAD_REQUEST, "Invalid parameters: You need to give us a firstName, a lastName, a vehicleId, a pickupDate and a createdAt");
    }

    /**
     * @param Vehicle vehicle
     * @param Date pickupDate
     * @param Date createdAt
     *
     * @return Booking - the created booking (or null if it's not possible to create one)
     */
    private Booking createBooking(Vehicle vehicle, String firstName, String lastName,
    Date pickupDate, Date createdAt)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(pickupDate);
        // get day of the week of the given pickupDate
        String dayOfWeek = daysOfWeek[c.get(Calendar.DAY_OF_WEEK) - 1];
        // get hour of the day of the given pickupDate
        int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        // get minutes of the given pickupDate
        int minute = c.get(Calendar.MINUTE);
        if (vehicle.checkAvailability(dayOfWeek, String.format("%02d%02d", hourOfDay, minute))) {
            Booking booking = new Booking(String.valueOf(id.incrementAndGet()), firstName, lastName, vehicle.getId(), pickupDate, createdAt);
            boolean isOverBooked = false;
            long pickupTime = pickupDate.getTime();
            for (String bookingId : vehicle.getBookingsId()) {
                Booking b = getBooking(bookingId);
                Date dateToCompare = b.getPickupDate();
                if (pickupDate.equals(dateToCompare)) {
                    // if it reaches this place, it means that booking is over booked
                    isOverBooked = true;
                    // this is true if the b is the last of the list of bookings for that vehicle for that date
                    if (b.getNextBookingId() == null) {
                        // so, the current booking will be the new last
                        b.setNextBookingId(booking.getId());
                        // booking is not certainly the main booking of this vehicle
                        booking.setIsMainBooking(false);
                        break;
                    }
                }
            }

            if (!isOverBooked) booking.setIsMainBooking(true);
            return booking;
        }
        return null;
    }

    /**
     * @param String id (RequestParam)
     * @param String cancelledAtString (RequestParam)
     * @param String cancelledReason (RequestParam)
     *
     * @see app.controller.AbstractController - getResult()
     * @return JSONObject (when successful, "result" field in the response is a JSONObject
     * that will have two keys:
     *    cancelled - the booking that has been cancelled
     *    nextBooking - if there is no next booking for this vehicle in that date
     *                  will be {}, if not will be the next booking)
     *
     * example: http://localhost:8080/bookings/cancel?id=1&cancelledAt=2018-03-04T21:32:00&cancelledReason=Because I can
     */
    @RequestMapping("/cancel")
    public JSONObject cancelBooking(@RequestParam(value="id") String id, @RequestParam(value="cancelledAt") String cancelledAtString,
    @RequestParam(value="cancelledReason") String cancelledReason)
    {
        if (id != null && cancelledAtString != null && cancelledReason != null) {
            Booking booking = getBooking(id);
            if (booking != null) {
                try {
                    Date cancelledAt;
                    if (cancelledAtString != null) cancelledAt = format.parse(cancelledAtString.replace('T', ' '));
                    else cancelledAt = format.parse(format.format(Calendar.getInstance().getTime()));

                    // only allow to cancel once
                    if (booking.getCancelledAt() == null) {
                        Map<String, JSONObject> result = cancelBooking(booking, cancelledAt, cancelledReason);
                        return getResult(new JSONObject(result), HttpConstants.OK, "Ok");
                    } else {
                        return getResult(new JSONObject(), HttpConstants.NO_CONTENT, "Booking already cancelled: The chosen booking was already cancelled.");
                    }
                } catch(ParseException e) {
                    return getResult(new JSONObject(), HttpConstants.BAD_REQUEST, "Error parsing: Please, check the parameters provided.");
                }
            }
        }

        return getResult(new JSONObject(), HttpConstants.NOT_FOUND, "Booking not found: The id provided doesn't correspond to a real booking.");
    }

    /**
     * @param Booking booking - booking to cancel
     * @param Date cancelledAt
     * @param String cancelledReason
     *
     * @return Map<String, JSONObject> - will have two keys
     * cancelled - the booking that has been cancelled
     * nextBooking - if there is no next booking for this vehicle in that date
     *               will be {}, if not will be the next booking
     */
    public Map<String, JSONObject> cancelBooking(Booking booking, Date cancelledAt, String cancelledReason)
    {
        booking.setCancelledAt(cancelledAt);
        booking.setCancelledReason(cancelledReason);
        Booking originalBooking = booking;

        // result will have two keys
        // cancelled - the booking that has been cancelled
        // nextBooking - if there is no next booking for this vehicle in that date
        //               will be {}, if not will be the next booking
        Map<String, JSONObject> result = new HashMap<String, JSONObject>();
        // it is here because it is the default nextBooking
        result.put("nextBooking", new JSONObject());

        if (booking.isMainBooking()) {
            booking.setIsMainBooking(false);
            while (true) {
                String nextBookingId = booking.getNextBookingId();
                if (nextBookingId != null) {
                    Booking nextBooking = getBooking(nextBookingId);
                    if (nextBooking.getCancelledAt() == null) {
                        nextBooking.setIsMainBooking(true);
                        result.put("nextBooking", nextBooking.getJson());
                        break;
                    } else {
                        booking = nextBooking;
                    }
                } else {
                    break;
                }
            }
        }

        // is here because it has to be the updated originalBooking
        result.put("cancelled", originalBooking.getJson());
        return result;
    }

    /**
     * @param String id
     *
     * @return Booking - will return a Booking or null if it's not found any
     */
    private Booking getBooking(String id)
    {
        return Application.bookings.get(id);
    }

    /**
     * @param long id
     *
     * only useful for the tests
     */
    public void setId(long id)
    {
        this.id.set(id);
    }
}
