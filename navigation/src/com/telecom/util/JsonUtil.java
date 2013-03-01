package com.telecom.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.telecom.model.AppInfo;
import com.telecom.model.Customer;
import com.telecom.model.Master;

public class JsonUtil {

    private static final String ResultKey = "Result";

    private static final String ResultSuccess = "Success";

    private static final String CUSTOMER_URI = "http://118.121.17.250/bass/AppRequest";

    private static final String APP_DOWN_URI = "http://118.121.17.250/bass/AppsDown";

    public static ArrayList<AppInfo> getAppList(int group) {
        ArrayList<AppInfo> appList = new ArrayList<AppInfo>();

        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("type", "group"));
        params.add(new BasicNameValuePair("group", "" + group));

        String appListString = HttpUtil.doGet(APP_DOWN_URI, params);

        if (TextUtils.isEmpty(appListString)) {
            return appList;
        }

        try {
            JSONObject object = new JSONObject(appListString);
            String result = object.getString(ResultKey);
            if (TextUtils.equals(result, ResultSuccess)) {
                JSONArray array = object.getJSONArray("AppList");

                for (int i = 0; i < array.length(); i++) {
                    AppInfo appInfo = new AppInfo();
                    JSONObject appObj = array.getJSONObject(i);

                    appInfo.setAppId(appObj.getString("AppId"));
                    appInfo.setAppName(appObj.getString("AppName"));
                    appInfo.setVersionCode(appObj.getString("AppVer"));
                    appInfo.setAppIconUri(appObj.getString("AppIcon"));
                    appInfo.setDownLink(appObj.getString("DownLink"));
                    appInfo.setAppDesc(appObj.getString("AppIntro"));
                    appInfo.setAppScreen(appObj.getString("AppScreen"));

                    appList.add(appInfo);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return appList;
    }

    public static Customer getCustomerInfoByIMSI(String imsi) {

        Customer cusInfo = new Customer();

        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("opt", "customer"));
        // TODO: comment this
        params.add(new BasicNameValuePair("imsi", imsi));
        // params.add(new BasicNameValuePair("imsi", "460030497828541"));
        String customerResult = HttpUtil.doGet(CUSTOMER_URI, params);
        if (TextUtils.isEmpty(customerResult)) {
            return null;
        }

        try {
            JSONObject object = new JSONObject(customerResult);
            String result = object.getString(ResultKey);
            if (TextUtils.equals(result, ResultSuccess)) {
                cusInfo.setApkUri(object.getString("ApkUrl"));
                cusInfo.setInvalidTime(object.getInt("InvalidTime"));
                JSONArray array = object.getJSONArray("CustomerList");
                JSONObject customer = array.getJSONObject(0);
                cusInfo.setCustomerName(customer.getString("CustomerName"));
                cusInfo.setPhoneNumber(customer.getString("PhoneNumber"));
                cusInfo.setProdId(customer.getString("ProdId"));
                cusInfo.setmAccountTime(customer.getString("AccountTime"));
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return cusInfo;
    }

    public static Master getMasterInfo(String phoneNum) {
        Master masterInfo = new Master();

        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("opt", "auth"));
        // TODO:comment this
        params.add(new BasicNameValuePair("number", phoneNum));
        // params.add(new BasicNameValuePair("number", "18980802828"));
        String customerResult = HttpUtil.doGet(CUSTOMER_URI, params);

        if (TextUtils.isEmpty(customerResult)) {
            return null;
        }

        try {
            JSONObject object = new JSONObject(customerResult);
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
