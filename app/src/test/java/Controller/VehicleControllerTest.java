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
import app.src.main.constants.HttpConstants;
import app.src.main.controller.VehicleController;
import app.src.main.entity.Vehicle;
import app.src.main.Application;

/**
 * Test Class to the VehicleController Class
 */
public class VehicleControllerTest extends AbstractTest {
    private static Application app;
    private static VehicleController vehicleController;
    private static long numberCurrentTest = 0;

    /**
     * initialize the app and one VehicleController
     */
    @BeforeClass
    public static void init()
    {
        app = new Application();
        app.file = "dataset.json";
        app.loadData();
        vehicleController = new VehicleController();
    }

    @Before
    public void beforeEachTest()
    {
        numberCurrentTest++;
        System.out.println("VehicleController Test " + String.format("%03d", numberCurrentTest) + " begins");
    }

    @After
    public void afterEachTest()
    {
        System.out.println("VehicleController Test " + String.format("%03d", numberCurrentTest) + " ends");
    }

    /**
     * test vehicle filter by model
     */
    @Test
    public void filterVehiclesByModel()
    {
        JSONObject resultObtained = vehicleController.getVehiclesBy("model");
        JSONArray vehiclesA = new JSONArray();
        JSONArray vehiclesAMG = new JSONArray();
        JSONArray vehiclesE = new JSONArray();
        JSONObject vehicles = new JSONObject();
        vehiclesA.add(app.vehicles.get("778a04fd-0a6a-4dc7-92bb-a7517608efc2").getJson());
        vehiclesA.add(app.vehicles.get("502ca69a-616b-43ce-9491-a6e20fc75a12").getJson());
        vehiclesAMG.add(app.vehicles.get("d5d0aabc-c0de-4f38-badc-759f96f5fca3").getJson());
        vehiclesAMG.add(app.vehicles.get("1cd6eae7-5f6f-42a7-a4ca-de7e498d9ce4").getJson());
        vehiclesAMG.add(app.vehicles.get("893d97bf-5a9d-4926-ace3-39ad0585c912").getJson());
        vehiclesAMG.add(app.vehicles.get("d723b0bd-8eb0-4826-bf5d-44754005d174").getJson());
        vehiclesAMG.add(app.vehicles.get("f0bd3390-58ae-4510-987e-a5bfe14973ff").getJson());
        vehiclesE.add(app.vehicles.get("768a73af-4336-41c8-b1bd-76bd700378ce").getJson());
        vehiclesE.add(app.vehicles.get("44a36bfa-ec8f-4448-b4c2-809203bdcb9e").getJson());
        vehiclesE.add(app.vehicles.get("136fbb51-8a06-42fd-b839-c01ab87e2c6c").getJson());
        vehiclesE.add(app.vehicles.get("875f00fa-9f67-44ea-bb26-75ff375fdd3f").getJson());
        vehicles.put("A", vehiclesA);
        vehicles.put("AMG", vehiclesAMG);
        vehicles.put("E", vehiclesE);
        JSONObject resultExpected = getResult(vehicles, HttpConstants.OK, "Ok");
        assertEquals(resultExpected, resultObtained);
    }

    /**
     * test vehicle filter by fuel type
     */
    @Test
    public void filterVehiclesByFuel()
    {
        JSONObject resultObtained = vehicleController.getVehiclesBy("fuel");
        JSONArray vehiclesElectric = new JSONArray();
        JSONArray vehiclesGasoline = new JSONArray();
        JSONObject vehicles = new JSONObject();
        vehiclesElectric.add(app.vehicles.get("768a73af-4336-41c8-b1bd-76bd700378ce").getJson());
        vehiclesElectric.add(app.vehicles.get("d5d0aabc-c0de-4f38-badc-759f96f5fca3").getJson());
        vehiclesElectric.add(app.vehicles.get("778a04fd-0a6a-4dc7-92bb-a7517608efc2").getJson());
        vehiclesElectric.add(app.vehicles.get("893d97bf-5a9d-4926-ace3-39ad0585c912").getJson());
        vehiclesElectric.add(app.vehicles.get("f0bd3390-58ae-4510-987e-a5bfe14973ff").getJson());
        vehiclesElectric.add(app.vehicles.get("875f00fa-9f67-44ea-bb26-75ff375fdd3f").getJson());
        vehiclesGasoline.add(app.vehicles.get("1cd6eae7-5f6f-42a7-a4ca-de7e498d9ce4").getJson());
        vehiclesGasoline.add(app.vehicles.get("44a36bfa-ec8f-4448-b4c2-809203bdcb9e").getJson());
        vehiclesGasoline.add(app.vehicles.get("d723b0bd-8eb0-4826-bf5d-44754005d174").getJson());
        vehiclesGasoline.add(app.vehicles.get("502ca69a-616b-43ce-9491-a6e20fc75a12").getJson());
        vehiclesGasoline.add(app.vehicles.get("136fbb51-8a06-42fd-b839-c01ab87e2c6c").getJson());
        vehicles.put("ELECTRIC", vehiclesElectric);
        vehicles.put("GASOLINE", vehiclesGasoline);
        JSONObject resultExpected = getResult(vehicles, HttpConstants.OK, "Ok");
        assertEquals(resultExpected, resultObtained);
    }

    /**
     * test vehicle filter by transmission
     */
    @Test
    public void filterVehiclesByTransmission()
    {
        JSONObject resultObtained = vehicleController.getVehiclesBy("transmission");
        JSONArray vehiclesAuto = new JSONArray();
        JSONArray vehiclesManual = new JSONArray();
        JSONObject vehicles = new JSONObject();
        vehiclesAuto.add(app.vehicles.get("768a73af-4336-41c8-b1bd-76bd700378ce").getJson());
        vehiclesAuto.add(app.vehicles.get("d5d0aabc-c0de-4f38-badc-759f96f5fca3").getJson());
        vehiclesAuto.add(app.vehicles.get("778a04fd-0a6a-4dc7-92bb-a7517608efc2").getJson());
        vehiclesAuto.add(app.vehicles.get("893d97bf-5a9d-4926-ace3-39ad0585c912").getJson());
        vehiclesAuto.add(app.vehicles.get("d723b0bd-8eb0-4826-bf5d-44754005d174").getJson());
        vehiclesAuto.add(app.vehicles.get("f0bd3390-58ae-4510-987e-a5bfe14973ff").getJson());
        vehiclesAuto.add(app.vehicles.get("136fbb51-8a06-42fd-b839-c01ab87e2c6c").getJson());
        vehiclesAuto.add(app.vehicles.get("875f00fa-9f67-44ea-bb26-75ff375fdd3f").getJson());
        vehiclesManual.add(app.vehicles.get("1cd6eae7-5f6f-42a7-a4ca-de7e498d9ce4").getJson());
        vehiclesManual.add(app.vehicles.get("44a36bfa-ec8f-4448-b4c2-809203bdcb9e").getJson());
        vehiclesManual.add(app.vehicles.get("502ca69a-616b-43ce-9491-a6e20fc75a12").getJson());
        vehicles.put("AUTO", vehiclesAuto);
        vehicles.put("MANUAL", vehiclesManual);
        JSONObject resultExpected = getResult(vehicles, HttpConstants.OK, "Ok");
        assertEquals(resultExpected, resultObtained);
    }

    /**
     * test vehicle filter by dealer
     */
    @Test
    public void filterVehiclesByDealer()
    {
        JSONObject resultObtained = vehicleController.getVehiclesBy("dealer");
        JSONArray vehiclesDealer1 = new JSONArray();
        JSONArray vehiclesDealer2 = new JSONArray();
        JSONArray vehiclesDealer3 = new JSONArray();
        JSONObject vehicles = new JSONObject();
        vehiclesDealer1.add(app.vehicles.get("768a73af-4336-41c8-b1bd-76bd700378ce").getJson());
        vehiclesDealer1.add(app.vehicles.get("d5d0aabc-c0de-4f38-badc-759f96f5fca3").getJson());
        vehiclesDealer1.add(app.vehicles.get("1cd6eae7-5f6f-42a7-a4ca-de7e498d9ce4").getJson());
        vehiclesDealer2.add(app.vehicles.get("778a04fd-0a6a-4dc7-92bb-a7517608efc2").getJson());
        vehiclesDealer2.add(app.vehicles.get("893d97bf-5a9d-4926-ace3-39ad0585c912").getJson());
        vehiclesDealer2.add(app.vehicles.get("44a36bfa-ec8f-4448-b4c2-809203bdcb9e").getJson());
        vehiclesDealer2.add(app.vehicles.get("d723b0bd-8eb0-4826-bf5d-44754005d174").getJson());
        vehiclesDealer3.add(app.vehicles.get("f0bd3390-58ae-4510-987e-a5bfe14973ff").getJson());
        vehiclesDealer3.add(app.vehicles.get("502ca69a-616b-43ce-9491-a6e20fc75a12").getJson());
        vehiclesDealer3.add(app.vehicles.get("136fbb51-8a06-42fd-b839-c01ab87e2c6c").getJson());
        vehiclesDealer3.add(app.vehicles.get("875f00fa-9f67-44ea-bb26-75ff375fdd3f").getJson());
        vehicles.put("846679bd-5831-4286-969b-056e9c89d74c", vehiclesDealer1);
        vehicles.put("bbcdbbad-5d0b-45ef-90ac-3581b997e063", vehiclesDealer2);
        vehicles.put("d4f4d287-1ad6-4968-a8ff-e9e0009ad5d1", vehiclesDealer3);
        JSONObject resultExpected = getResult(vehicles, HttpConstants.OK, "Ok");
        assertEquals(resultExpected, resultObtained);
    }
}
