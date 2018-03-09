package app.src.main.entity;

import org.json.simple.JSONObject;

/**
 * all entities should implement this interface
 * this inteface serves to know how to return the object as json
 */
interface JsonInterface
{
    /**
     * @return JSONObject
     *
     * this function exists to allow me to choose what fields
     * of the Object will be passed
     */
    JSONObject getJson();
}
