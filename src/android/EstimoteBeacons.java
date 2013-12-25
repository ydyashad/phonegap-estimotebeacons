package com.example.estimotebeacons;

import android.os.RemoteException;
import android.util.Log;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: A.Salachyonok
 * Date: 23.12.13
 * Class description
 */
public class EstimoteBeacons extends CordovaPlugin {

    public static final String START_ESTIMOTE_BEACONS_DISCOVERY_FOR_REGION = "startEstimoteBeaconsDiscoveryForRegion";
    public static final String STOP_ESTIMOTE_BEACON_DISCOVERY_FOR_REGION = "stopEstimoteBeaconsDiscoveryForRegion";

    public static final String START_RANGING_BEACONS_IN_REGION = "startRangingBeaconsInRegion";
    public static final String STOP_RANGING_BEACON_IN_REGION = "stopRangingBeaconsInRegion";

    public static final String GET_BEACONS = "getBeacons";


    private BeaconManager iBeaconManager;



    private Region currentRegion;

    private List<Beacon> beacons = new ArrayList<Beacon>();


    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        iBeaconManager = new BeaconManager(this.cordova.getActivity().getApplicationContext());
        Beacon b = new Beacon("UUID", "macAddress", 1, 1, 1, 1);
        List<Beacon> bs = new ArrayList<Beacon>();
        bs.add(b);
        EstimoteBeacons.this.beacons = bs;
        currentRegion = new Region("uniqueId", null, null);
        iBeaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {
                if (beacons == null || beacons.size() < 1) {

                } else {
                    EstimoteBeacons.this.beacons = beacons;
                }

            }
        });
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Log.d(EstimoteBeacons.class.toString(), "action - >" + action);

        try {
            if (action.equalsIgnoreCase(START_ESTIMOTE_BEACONS_DISCOVERY_FOR_REGION)) {
                startEstimoteBeaconsDiscoveryForRegion();
                callbackContext.success(callbackContext.getCallbackId());
                return true;
            }

            if (action.equalsIgnoreCase(STOP_ESTIMOTE_BEACON_DISCOVERY_FOR_REGION)) {
                stopEstimoteBeaconsDiscoveryForRegion();
                callbackContext.success(callbackContext.getCallbackId());
                return true;
            }

            if (action.equalsIgnoreCase(START_RANGING_BEACONS_IN_REGION)) {
                startRangingBeaconsInRegion();
                callbackContext.success(callbackContext.getCallbackId());
                return true;
            }

            if (action.equalsIgnoreCase(STOP_RANGING_BEACON_IN_REGION)) {
                stopRangingBeaconsInRegion();
                callbackContext.success(callbackContext.getCallbackId());
                return true;
            }

            if (action.equalsIgnoreCase(GET_BEACONS)) {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, listToJSONArray(beacons)));
                return true;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            callbackContext.error(e.getMessage());
            return false;
        }
        return false;
    }

    private void startEstimoteBeaconsDiscoveryForRegion() throws RemoteException {
        // TODO: stub
    }

    private void stopEstimoteBeaconsDiscoveryForRegion() throws RemoteException {
        // TODO: stub
    }

    private void startRangingBeaconsInRegion() throws RemoteException{
        iBeaconManager.startRanging(currentRegion);
    }

    private void stopRangingBeaconsInRegion() throws RemoteException{
        iBeaconManager.stopRanging(currentRegion);
    }



    /**
     * Convert list of beacons(@com.estimote.sdk.Beacon) to @JSONArray
     * @param beacons - list of beacons (@com.estimote.sdk.Beacon)
     * @return JSONArray
     * @throws JSONException
     */
    private JSONArray listToJSONArray(List<Beacon> beacons) throws JSONException{
        JSONArray jArray = new JSONArray();
        for (Beacon beacon : beacons) {
            jArray.put(beaconToJSONObject(beacon));
        }
        return jArray;
    }

    /**
     * Convert beacon (@com.estimote.sdk.Beacon) to @JSONObject
     * @param beacon - beacon(@com.estimote.sdk.Beacon)
     * @return JSONObject
     * @throws JSONException
     */
    private JSONObject beaconToJSONObject(Beacon beacon) throws JSONException{
        JSONObject object = new JSONObject();
        object.put("proximityUUID", beacon.proximityUUID);
        object.put("major", beacon.major);
        object.put("minor", beacon.minor);
        object.put("rssi", beacon.rssi);
        object.put("macAddress", beacon.macAddress);
        object.put("measuredPower", beacon.measuredPower);
        return object;
    }
}
