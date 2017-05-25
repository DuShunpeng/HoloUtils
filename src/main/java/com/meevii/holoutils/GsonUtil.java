package com.meevii.holoutils;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;


public class GsonUtil {

    enum GsonWrapper {
        INSTANCE;
        Gson gson = new Gson();
    }

    private static String defLanguage = "en";

    public static Gson getInstance() {
        return GsonWrapper.INSTANCE.gson;
    }

    public static String toJson(Object object) {
        return GsonWrapper.INSTANCE.gson.toJson(object);
    }

    public static <T> T fromJson(String jsonString, Class<T> tClass) {
        return GsonWrapper.INSTANCE.gson.fromJson(jsonString, tClass);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return GsonWrapper.INSTANCE.gson.fromJson(json, typeOfT);
    }

    public static String loadJSONFromAsset(Context context, String jsonFileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(jsonFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static String loadJSONFromLocal(Context context, String jsonFileName) {
        String json = null;
        File file = context.getFileStreamPath(jsonFileName);
        if (file != null && file.exists()) {
            try {
                FileInputStream fis =context.openFileInput(jsonFileName);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader reader = new BufferedReader(isr);
                String readString = reader.readLine();
                StringBuilder result = new StringBuilder();
                while (readString != null) {
                    result.append(readString);
                    readString = reader.readLine();
                }
                json = result.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return json;
    }

    public static void saveJSONToLocal(Context context, String jsonFileName, String jsonData){
        if(TextUtil.isTextEmpty(jsonFileName)){
            return;
        }

        ExecutorUtil.submitLow(() -> {
            try {
                FileOutputStream fos = context.openFileOutput(jsonFileName, Context.MODE_PRIVATE);
                fos.write(jsonData.getBytes("UTF-8"));
                fos.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    // FIXME: 2/23/17 donot think it is needed below
//    public static Map<String, Object> getMapFromJson(String json) {
//
//        JsonParser parser = new JsonParser();
//        JsonObject jsonObj = parser.parse(json).getAsJsonObject();
//
//        Map<String, Object> map = new HashMap<>();
//        Set<Map.Entry<String, JsonElement>> entrySet = jsonObj.entrySet();
//        for (Iterator<Map.Entry<String, JsonElement>> iter = entrySet.iterator(); iter.hasNext(); ) {
//            Map.Entry<String, JsonElement> entry = iter.next();
//            String key = entry.getKey();
//            Object value = entry.getValue();
//            if (value instanceof JsonArray)
//                map.put(key, toList((JsonArray) value));
//            else if (value instanceof JsonObject)
//                map.put(key, toMap((JsonObject) value));
//            else
//                map.put(key, value);
//        }
//        return map;
//    }
//
//    private static Map<String, Object> toMap(JsonObject json) {
//        Map<String, Object> map = new HashMap<>();
//        Set<Map.Entry<String, JsonElement>> entrySet = json.entrySet();
//        for (Iterator<Map.Entry<String, JsonElement>> iter = entrySet.iterator(); iter.hasNext(); ) {
//            Map.Entry<String, JsonElement> entry = iter.next();
//            String key = entry.getKey();
//            Object value = entry.getValue();
//            if (value instanceof JsonArray)
//                map.put(key, toList((JsonArray) value));
//            else if (value instanceof JsonObject)
//                map.put(key, toMap((JsonObject) value));
//            else
//                map.put(key, value);
//        }
//        return map;
//    }
//
//    private static List<Object> toList(JsonArray json) {
//        List<Object> list = new ArrayList<>();
//        for (int i = 0; i < json.size(); i++) {
//            Object value = json.get(i);
//            if (value instanceof JsonArray) {
//                list.add(toList((JsonArray) value));
//            } else if (value instanceof JsonObject) {
//                list.add(toMap((JsonObject) value));
//            } else {
//                list.add(value);
//            }
//        }
//        return list;
//    }

    public static String getLocaleJsonStringFromJson(Context context, String jsonString) {
        Object localData = getLocalObject(context, jsonString);
        if(localData != null){
            return toJson(localData);
        }else {
            return "";
        }
    }

    public static String getLocaleValueStringFromJson(Context context, String jsonString) {
        Object localData = getLocalObject(context, jsonString);
        if(localData != null){
           return localData.toString();
        }else {
            return "";
        }
    }

    private static Object getLocalObject(Context context, String jsonString) {
        if (TextUtils.isEmpty(jsonString) || jsonString.equals("null")) {
            return null;
        }
        LinkedTreeMap allJsonData = (LinkedTreeMap)fromJson(jsonString, Object.class);
        String language = LocaleUtil.getCurrentLanguage(context);
        if(allJsonData.containsKey(language)){
            return allJsonData.get(language);
        }else {
            return allJsonData.get(defLanguage);
        }
    }
}