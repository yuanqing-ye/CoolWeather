package com.example.yuanqingye.coolweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.example.yuanqingye.coolweather.db.CoolWeatherDB;
import com.example.yuanqingye.coolweather.model.City;
import com.example.yuanqingye.coolweather.model.County;
import com.example.yuanqingye.coolweather.model.Province;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by yuanqing.ye on 2015/3/3.
 */
public class Utility {
    public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,
                                                               String response) {
        if (TextUtils.isEmpty(response)) return false;

        String[] allProvinces = response.split(",");

        if (allProvinces != null && allProvinces.length > 0) {
            for (String p : allProvinces) {
                String[] array = p.split("\\|");
                Province province = new Province();
                province.setProvinceCode(array[0].trim());
                province.setProvinceName(array[1].trim());

                coolWeatherDB.saveProvince(province);
            }
            return true;
        }

        return false;
    }

    public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB, String response,
                                               int provinceId) {
        if (TextUtils.isEmpty(response)) return false;

        String[] allCities = response.split(",");
        if (allCities != null && allCities.length > 0) {
            for (String c : allCities) {
                String[] array = c.split("\\|");
                City city = new City();
                city.setCityCode(array[0].trim());
                city.setCityName(array[1].trim());
                city.setProvinceId(provinceId);
                coolWeatherDB.saveCity(city);
            }
            return true;
        }
        return false;
    }

    public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB, String response,
                                                 int cityId) {
        if (TextUtils.isEmpty(response)) return false;

        String[] allCounties = response.split(",");

        if (allCounties != null && allCounties.length > 0) {
            for (String c : allCounties) {
                String[] array = c.split("\\|");
                County county = new County();
                county.setCountyCode(array[0].trim());
                county.setCountyName(array[1].trim());
                county.setCityId(cityId);

                coolWeatherDB.saveCounty(county);
            }

            return true;
        }

        return false;
    }

    public static void handleWeatherResponse(Context context, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");

            String cityName = weatherInfo.getString("city");
            String weatherCode = weatherInfo.getString("cityid");
            String temp1 = weatherInfo.getString("temp1");
            String temp2 = weatherInfo.getString("temp2");
            String weatherDesp = weatherInfo.getString("weather1");
            String publishTime = weatherInfo.getString("date_y");

            saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp,
                    publishTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void saveWeatherInfo(Context context, String cityName, String weatherCode,
                                       String temp1, String temp2, String weatherDesp,
                                       String publishTime) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context)
                .edit();
        editor.putBoolean("city_selected", true);
        editor.putString("city_name", cityName);
        editor.putString("weather_code", weatherCode);
        editor.putString("temp1", temp1);
        editor.putString("temp2", temp2);
        editor.putString("weather_desp", weatherDesp);
//        editor.putString("publish_time", publishTime);
        editor.putString("publish_time", sdf2.format(new Date()));
        editor.putString("current_date", sdf.format(new Date()));
        editor.commit();
    }
}
