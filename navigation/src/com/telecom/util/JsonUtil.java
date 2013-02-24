package com.telecom.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.telecom.model.Customer;
import com.telecom.model.Master;

public class JsonUtil {

    private static final String customerString = "{" + 
                                            "\"CustomerList\": ["+
                                            "{"+
                                                    "\"CustomerName\": \"罗晶\","+
                                                    "\"PhoneNumber\": \"18080005203\","+
                                                    "\"ProdId\": \"102029829644\""+
                                            "}"+
                                            "],"+
                                            "\"Result\": \"Success\""+
                                        "}";
    private static final String masterString = "{"+
                                                    "\"Result\": \"Success\","+
                                                    "\"UserList\": ["+
                                                            "{"+
                                                                    "\"UserId\": \"2082216\","+
                                                                    "\"UserName\": \"程小川（高新）\""+
                                                            "}"+
                                                    "]"+
                                                "}";

    private static final String ResultKey = "Result";

    private static final String ResultSuccess ="Success";
    
    public static Customer getCustomerInfoByIMSI() {

        Customer cusInfo = new Customer();

        try {
            JSONObject object = new JSONObject(customerString);
            String result = object.getString(ResultKey);
            if (TextUtils.equals(result, ResultSuccess)) {
                JSONArray array = object.getJSONArray("CustomerList");
                JSONObject customer = array.getJSONObject(0);
                cusInfo.setCustomerName(customer.getString("CustomerName"));
                cusInfo.setPhoneNumber(customer.getString("PhoneNumber"));
                cusInfo.setProdId(customer.getString("ProdId"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        return cusInfo;
    }
    
    public static Master getMasterInfo() {
        Master masterInfo = new Master();

        try {
            JSONObject object = new JSONObject(masterString);
            String result = object.getString(ResultKey);
            if (TextUtils.equals(result, ResultSuccess)) {
                JSONArray array = object.getJSONArray("UserList");
                JSONObject master = array.getJSONObject(0);
                masterInfo.setUserId(master.getString("UserId"));
                masterInfo.setUserName(master.getString("UserName"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        return masterInfo;
    }
}
