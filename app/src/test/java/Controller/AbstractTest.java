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
import java.util.Map;
import app.src.main.constants.HttpConstants;
import app.src.main.controller.VehicleController;
import app.src.main.entity.Vehicle;
import app.src.main.entity.Booking;
import app.src.main.entity.Dealer;
import app.src.main.Application;

/**
 * all Test Classes must extends AbstractTest
 */
public class AbstractTest
{
    /**
     * @param Object resultObject
     * @param int code
     * @param String status
     *
     * @return JSONObject - will return a json with resultObject variable as result,
     *                      code variable as code and status variable as status
     *
     * note: all results send by the api must use this function to send the result
     *       the return of this function will have the following form
     *              - result: is any kind of Object
     *              - code: is an int that specified what has happened
     *                      (HTTP code - see app/src/main/java/Constants/HttpConstants.java)
     *              - status: a message with more info about what has happened
     */
    JSONObject getResult(Object resultObject, int code, String status)
    {
        JSONObject result = new JSONObject();
        result.put("result", resultObject);
        result.put("code", code);
        result.put("status", status);
        return result;
    }
}
