package app.src.main.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import app.src.main.entity.Dealer;
import app.src.main.entity.Vehicle;
import app.src.main.entity.Booking;

/**
 * all controllers should extend this class
 */
public abstract class AbstractController
{
    // format used to save dates
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
