package app.src.main.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import org.json.simple.JSONObject;
import app.src.main.comparator.DealerComparatorByDistance;
import app.src.main.constants.HttpConstants;
import app.src.main.controller.AbstractController;
import app.src.main.entity.Vehicle;
import app.src.main.entity.Booking;
import app.src.main.entity.Dealer;
import app.src.main.geometry.Point;
import app.src.main.geometry.Polygon;
import app.src.main.Application;

/**
 * DealerController makes all the operations on the dealers
 */
@RestController
@RequestMapping(value="/dealers")
public class DealerController extends AbstractController
{
    /**
     * @param double[] coords (RequestParam)
     * @param String model (RequestParam)
     * @param String fuel (RequestParam)
     * @param String transmission (RequestParam)
     *
     * @see app.controller.AbstractController - getResult()
     * @return JSONObject (when successful, "result" field in the response is a JSONObject
     * with all the important fields of the right dealer)
     *
     * example: http://localhost:8080/dealers/closest?coords=39,10&model=AMG&fuel=GASOLINE&transmission=AUTO
     */
    @RequestMapping(value="/closest")
    public JSONObject getDealerByLocationAndVehicleAttributes(@RequestParam(value="coords") double[] coords,
    @RequestParam(value="model", required=false) String model, @RequestParam(value="fuel", required=false) String fuel,
    @RequestParam(value="transmission", required=false) String transmission)
    {
        if (coords != null && coords.length == 2) {
            double latitude = coords[0];
            double longitude = coords[1];
            // to avoid bad parameters
            if (model != null) model = model.toUpperCase();
            if (fuel != null) fuel = fuel.toUpperCase();
            if (transmission != null) transmission = transmission.toUpperCase();
            List<Dealer> possibleDealers = getPossibleDealers(model, fuel, transmission);
            Dealer dealer = getResultDealer(possibleDealers, latitude, longitude);
            // FIXME
            // tentar enviar em conjunto com o dealer tambem o Vehicle pelo qual ele foi encontrado
            // Map<String, JSONObject> result = new HashMap<String, JSONObject>();

            if (dealer == null) return getResult(new JSONObject(), HttpConstants.NO_CONTENT, "No dealer found: There is no dealer with that characteristics.");
            else return getResult(dealer.getJson(), HttpConstants.OK, "Ok");
        } else {
            return getResult(new JSONObject(), HttpConstants.BAD_REQUEST, "Please, verify the parameters given.");
        }
    }

    /**
     * @param double[] coords (RequestParam)
     * @param String model (RequestParam)
     * @param String fuel (RequestParam)
     * @param String transmission (RequestParam)
     *
     * @see app.controller.AbstractController - getResult()
     * @return JSONObject (when successful, "result" field in the response is a JSONObject
     * with all the important fields of all dealers that have a vehicle with that characteristics)
     *
     * example: http://localhost:8080/dealers/sorted?coords=39,10&model=AMG&fuel=ELECTRIC&transmission=AUTO
     */
    @RequestMapping(value="/sorted")
    public JSONObject getDealersByVehicleAttributesAndSortedByDistance(@RequestParam(value="coords") double[] coords,
    @RequestParam(value="model", required=false) String model, @RequestParam(value="fuel", required=false) String fuel,
    @RequestParam(value="transmission", required=false) String transmission)
    {
        // FIXME
        // tentar enviar em conjunto com o dealer tambem o Vehicle pelo qual ele foi encontrado

        if (coords != null && coords.length == 2) {
            double latitude = coords[0];
            double longitude = coords[1];
            // to avoid bad parameters
            if (model != null) model = model.toUpperCase();
            if (fuel != null) fuel = fuel.toUpperCase();
            if (transmission != null) transmission = transmission.toUpperCase();
            List<Dealer> possibleDealers = getPossibleDealers(model, fuel, transmission);
            if (!possibleDealers.isEmpty()) {
                List<JSONObject> result = new ArrayList<JSONObject>();
                Collections.sort(possibleDealers, new DealerComparatorByDistance(latitude, longitude));
                for (Dealer dealer : possibleDealers) result.add(dealer.getJson());

                return getResult(result, HttpConstants.OK, "Ok");
            }

            return getResult(new JSONObject(), HttpConstants.NO_CONTENT, "No dealer found: There is no dealer with that characteristics.");
        } else {
            return getResult(new JSONObject(), HttpConstants.BAD_REQUEST, "Please, verify the parameters given.");
        }
    }

    /**
     * @param double[] coordsPolygon (RequestParam)
     * @param String model (RequestParam)
     * @param String fuel (RequestParam)
     * @param String transmission (RequestParam)
     *
     * @see app.controller.AbstractController - getResult()
     * @return JSONObject (when successful, "result" field in the response is a JSONObject
     * with all the important fields of all dealers that have a vehicle with that characteristics
     * inside of the given polygon)
     *
     * example: http://localhost:8080/dealers/polygon?coordsPolygon=40,10,42,12,36.8473,-9.046,40,-18&model=AMG&fuel=ELECTRIC&transmission=AUTO
     */
    @RequestMapping(value="/polygon")
    public JSONObject getDealersByVehicleAttributesAndInsidePolygon(@RequestParam(value="coordsPolygon") double[] coordsPolygon,
    @RequestParam(value="model", required=false) String model, @RequestParam(value="fuel", required=false) String fuel,
    @RequestParam(value="transmission", required=false) String transmission)
    {
        if (coordsPolygon != null && coordsPolygon.length % 2 == 0 && coordsPolygon.length >= 6) {
            // to avoid bad parameters
            if (model != null) model = model.toUpperCase();
            if (fuel != null) fuel = fuel.toUpperCase();
            if (transmission != null) transmission = transmission.toUpperCase();
            List<Dealer> possibleDealers = getPossibleDealers(model, fuel, transmission);
            if (!possibleDealers.isEmpty()) {
                List<JSONObject> result = getDealersInsidePolygon(possibleDealers, coordsPolygon);
                if (!result.isEmpty()) return getResult(result, HttpConstants.OK, "Ok");
            }

            return getResult(new JSONObject(), HttpConstants.NO_CONTENT, "No dealer found: There is no dealer with that characteristics.");
        } else {
            return getResult(new JSONObject(), HttpConstants.BAD_REQUEST, "Please, verify the parameters given.");
        }
    }

    /**
     * @param List<Dealer> possibleDealers
     * @param double[] coordsPolygon
     *
     * @return List<JSONObject> - all possible dealers (json format) that are contained in the polygon
     */
    private List<JSONObject> getDealersInsidePolygon(List<Dealer> possibleDealers, double[] coordsPolygon)
    {
        List<JSONObject> result = new ArrayList<JSONObject>();
        List<Point> points = new ArrayList<Point>();
        for (int i = 0, j = 0, length = coordsPolygon.length; i < length; i++, j++) {
            points.add(new Point(coordsPolygon[i], coordsPolygon[++i]));
        }
        // create a new Polygon with the given points
        Polygon polygon = new Polygon(points);

        // check what dealers of the possibleDealers are contained in the polygon
        for (Dealer dealer : possibleDealers) {
            if (polygon.contains(new Point(dealer.getLatitude(), dealer.getLongitude()))) {
                result.add(dealer.getJson());
            }
        }

        return result;
    }

    /**
     * @param String model
     * @param String fuel
     * @param String transmission
     *
     * @return List<Dealer> - all possible dealers based on the characteristics given
     *                        if there is no characteristics, return all dealers
     */
    private List<Dealer> getPossibleDealers(String model, String fuel, String transmission)
    {
        List<Dealer> possibleDealers = new ArrayList<Dealer>();
        if (model != null || fuel != null || transmission != null) {
            List<Vehicle> possibleVehicles = getPossibleVehicles(model, fuel, transmission);
            possibleDealers = getPossibleDealersBasedOnVehicles(possibleVehicles);
        } else {
            possibleDealers.addAll(Application.dealers.values());
        }

        return possibleDealers;
    }

    /**
     * @param String model
     * @param String fuel
     * @param String transmission
     *
     * @return List<Vehicle> - all possible vehicles that match the
     * given characteristics
     */
    private List<Vehicle> getPossibleVehicles(String model, String fuel, String transmission)
    {
        List<Vehicle> possibleVehicles = new ArrayList<Vehicle>();
        // will have the first filter that is not null
        String filter = "";
        // will have the string that will be used to make the first filter
        String filterBy = "";
        if (model != null) {
            filter = "model";
            filterBy = model;
        } else if (fuel != null) {
            filter = "fuel";
            filterBy = fuel;
        } else if (transmission != null) {
            filter = "transmission";
            filterBy = transmission;
        }

        if (!filter.equals("")) {
            Map<String, List<String>> vehiclesByFilter = Application.vehiclesBy.get(filter);
            // get all the vehicles ids that match to the filterBy
            List<String> vehiclesByThatFilter = vehiclesByFilter.get(filterBy);
            if (vehiclesByThatFilter != null) {
                // vehicles ids to vehicles
                possibleVehicles = getVehicles(vehiclesByThatFilter);
            }

            // temp will store all the final possible vehicles
            List<Vehicle> temp = new ArrayList<Vehicle>();
            for (Vehicle v : possibleVehicles) {
                if ((model == null || v.getModel().equals(model)) && (fuel == null || v.getFuel().equals(fuel)) &&
                (transmission == null || v.getTransmission().equals(transmission))) {
                    temp.add(v);
                }
            }
            possibleVehicles = temp;
        }

        return possibleVehicles;
    }

    /**
     * @param List<String> vehiclesId - ids of the vehicles that will
     *                                  be transformed to the real vehicles
     *
     * @return List<Vehicle>
     *
     * get the real vehicles that match to the given vehiclesId
     */
    private List<Vehicle> getVehicles(List<String> vehiclesId)
    {
        List<Vehicle> result = new ArrayList<Vehicle>();
        if (vehiclesId != null) {
            for (String id : vehiclesId) result.add(Application.vehicles.get(id));
        }

        return result;
    }

    /**
     * @param List<Vehicle> possibleVehicles - all possible vehicles to choose from
     *
     * @return List<Dealer> - possible dealers
     */
    private List<Dealer> getPossibleDealersBasedOnVehicles(List<Vehicle> possibleVehicles)
    {
        List<Dealer> possibleDealers = new ArrayList<Dealer>();
        // it's used a HashSet to force a dealer to appear only once in the possibleDealers
        Set<Dealer> temp = new HashSet<Dealer>();
        for (Vehicle v : possibleVehicles) {
            temp.add(Application.dealers.get(v.getDealerId()));
        }
        // I prefer to work with an ArrayList so this line
        possibleDealers.addAll(temp);

        return possibleDealers;
    }

    /**
     * @param List<Dealer> possibleDealers
     * @param double latitude
     * @param double longitude
     *
     * @return Dealer - will return the closest Dealer (null if there is no Dealer)
     */
    private Dealer getResultDealer(List<Dealer> possibleDealers, double latitude, double longitude)
    {
        Dealer dealer = null;
        // the first value is not important. could be 0 or 9999
        double lessDifference = 0;
        for (int i = 0, length = possibleDealers.size(); i < length; i++) {
            Dealer d = possibleDealers.get(i);

            // don't use the Math.abs() because will return a float
            double lat = latitude - d.getLatitude();
            lat = (lat < 0 ? -lat : lat);
            double lon = longitude - d.getLongitude();
            lon = (lon < 0 ? -lon : lon);

            double diffTemp = lat + lon;
            if (i == 0 || diffTemp < lessDifference) {
                lessDifference = diffTemp;
                dealer = d;
            }
        }

        return dealer;
    }
}
