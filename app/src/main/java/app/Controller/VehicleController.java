package app.src.main.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import org.json.simple.JSONObject;
import app.src.main.constants.HttpConstants;
import app.src.main.controller.AbstractController;
import app.src.main.entity.Vehicle;
import app.src.main.entity.Dealer;
import app.src.main.Application;

/**
 * VehicleController makes all the operations on the vehicles
 */
@RestController
@RequestMapping(value="/vehicles")
public class VehicleController extends AbstractController
{
    /**
     * @param String filter (RequestParam) - it is the filter that will be applied to the vehicles
     *
     * @see app.controller.AbstractController - getResult()
     * @return JSONObject (when successful, "result" field in the response is Map<String, List<JSONObject>>)
     *
     * example: http://localhost:8080/vehicles/filter/dealer
     */
    @RequestMapping(value="/filter/{filter}")
    public JSONObject getVehiclesBy(@PathVariable(value="filter") String filter)
    {
        if (filter.equals("model") || filter.equals("fuel") || filter.equals("transmission")) {
            Map<String, List<String>> temp = Application.vehiclesBy.get(filter);
            if (temp != null) return getResult(getVehicles(temp), HttpConstants.OK, "Ok");
        } else if (filter.equals("dealer")) {
            Map<String, List<String>> vehiclesByDealer = new HashMap<String, List<String>>();
            for (Dealer dealer : Application.dealers.values()) {
                vehiclesByDealer.put(dealer.getId(), dealer.getVehiclesId());
            }
            if (!vehiclesByDealer.isEmpty()) return getResult(getVehicles(vehiclesByDealer), HttpConstants.OK, "Ok");
        } else {
            return getResult(new JSONObject(), HttpConstants.NOT_FOUND, "Filter invalid: The filter doesn't exist.");
        }

        return getResult(new JSONObject(), HttpConstants.NO_CONTENT, "Filter invalid: The filter you tried to apply is not valid for existing vehicles.");
    }

    /**
     * @param Map<String, List<String>> vehiclesId - key of this Map is important to mantain because
     *                                               key is the filter applied
     *                                             - value is a List<String> with all vehicle id
     *
     * @return Map<String, List<JSONObject>> - keys are the same of vehiclesId and List<JSONObject> will
     *                                         have all vehicles that match to all vehicle id in vehiclesId
     *                                         (JSONObject contains the important info of a Vehicle in this case)
     */
    private Map<String, List<JSONObject>> getVehicles(Map<String, List<String>> vehiclesId)
    {
        // it's a TreeMap because will be returned by alphabetical order
        Map<String, List<JSONObject>> result = new TreeMap<String, List<JSONObject>>();

        for (String key : vehiclesId.keySet()) {
            List<JSONObject> temp = result.get(key);
            if (temp == null) {
                temp = new ArrayList<JSONObject>();
            }
            // get id of the intended vehicles
            List<String> ids = vehiclesId.get(key);

            if (ids != null) {
                for (String id : ids) temp.add(Application.vehicles.get(id).getJson());
            }

            result.put(key, temp);
        }

        return result;
    }
}
