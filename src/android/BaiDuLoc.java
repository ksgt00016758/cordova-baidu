package org.apache.cordova.baidu;

import android.util.Log;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class BaiDuLoc extends CordovaPlugin { 

	private LocationClient mLocationClient = null;
	private JSONObject jsonObj = new JSONObject(); 
	private boolean result = false; 
	private CallbackContext callbackContext;
  	public boolean execute(String action, JSONArray args,final CallbackContext callbackContext) {
	  setCallbackContext(callbackContext);
      if (action.equals("get")) {        	
        	cordova.getActivity().runOnUiThread(new Runnable(){

        		public void run() {
        			Log.d("BaiduMaps", "run success11");
        			mLocationClient = new LocationClient(cordova.getActivity());
        			LocationClientOption option = new LocationClientOption();
        	        option.setOpenGps(false);							
        	        option.setCoorType("bd09ll");							
        	        option.setPriority(LocationClientOption.NetWorkFirst);	
        	        option.setProdName("BaiduLoc");							
        	        option.setScanSpan(5000);								
        	        mLocationClient.setLocOption(option);
       	        
                	mLocationClient.registerLocationListener(new BDLocationListener(){			
            			public void onReceiveLocation(BDLocation location) {
            				Log.d("BaiduMaps", "run success12");
            				if (location == null)
            					return;					
            				try {
            					jsonObj.put("Latitude",location.getLatitude());
            					jsonObj.put("Longitude", location.getLongitude());
            					jsonObj.put("LocType", location.getLocType());
            					jsonObj.put("Radius", location.getRadius());
           					
            					if (location.getLocType() == BDLocation.TypeGpsLocation){
            						jsonObj.put("Speed", location.getSpeed());
            						jsonObj.put("SatelliteNumber", location.getSatelliteNumber());
            					} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
            						jsonObj.put("AddrStr", location.getAddrStr());
            					}
            					Log.d("BaiduMaps", "run success"+jsonObj.toString());
                                mLocationClient.stop();
            					callbackContext.success(jsonObj);            					
            					result = true;
            				} catch (JSONException e) {
            					callbackContext.error(e.getMessage());
            					result = true;
            				}
            				
            			}
            			
            	
            			public void onReceivePoi(BDLocation poiLocation) {
            				// TODO Auto-generated method stub		
            			}	
            		}	);
                	mLocationClient.start();
                    mLocationClient.requestLocation();                    
        		}
        		
        	});            
        } else if(action.equals("stop")) {
        	mLocationClient.stop();
        	callbackContext.success(200);
        } else {
        	callbackContext.error(PluginResult.Status.INVALID_ACTION.toString());
        }
      
 		while (result == false) {
 			try {
 				Thread.sleep(100);
 			} catch (InterruptedException e) {
 			}
 		}	 	     
 		return result;
    }	
	
	public void onDestroy(){
    	if (mLocationClient != null && mLocationClient.isStarted()){
    		mLocationClient.stop();
    		mLocationClient = null;
    	}
    	super.onDestroy();
    }
	
	
	public CallbackContext getCallbackContext() {
		return callbackContext;
	}

	public void setCallbackContext(CallbackContext callbackContext) {
		this.callbackContext = callbackContext;
	}
}
