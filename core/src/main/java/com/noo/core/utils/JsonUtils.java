//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.noo.core.utils;

import android.text.TextUtils;

import com.noo.core.log.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class JsonUtils {

    public interface JsonSerializable {
        String toJsonString();
    }

    private static final String EMPTY_JSON_STRING = "{}";

    public JsonUtils() {
    }

    public static String toString(JSONObject obj) {
        JSONStringer stringer = new JSONStringer();

        try {
            JSONObjectToStringInternal(stringer, obj);
        } catch (JSONException e) {
            Logger.e(e);
        }

        return stringer.toString();
    }

    private static void JSONObjectToStringInternal(JSONStringer stringer, JSONObject object) throws JSONException {
        TreeMap map = new TreeMap();
        Iterator iter;
        Object value;
        if (object != null && object.length() != 0) {
            iter = object.keys();

            while (iter.hasNext()) {
                String entry = (String) iter.next();
                value = object.get(entry);
                map.put(entry, value);
            }
        }

        stringer.object();
        iter = map.entrySet().iterator();

        while (iter.hasNext()) {
            Entry entry1 = (Entry) iter.next();
            stringer.key((String) entry1.getKey());
            value = entry1.getValue();
            if (value instanceof JSONObject) {
                JSONObjectToStringInternal(stringer, (JSONObject) value);
            } else {
                stringer.value(value);
            }
        }

        stringer.endObject();
    }

    public static Object parseObject(String json) {
        if (!TextUtils.isEmpty(json) && !TextUtils.isEmpty(json.trim())) {
            String jsonString = json.trim();

            try {
                if (jsonString.startsWith("[")) {
                    JSONArray e1 = new JSONArray(jsonString);
                    return e1;
                } else {
                    JSONObject e = new JSONObject(jsonString);
                    return e;
                }
            } catch (Exception e) {
                Logger.e(e);
                return null;
            }
        } else {
            return null;
        }
    }

    public static String toJsonString(Object obj) {
        try {
            Object e = toJsonObject(obj);
            return e.toString();
        } catch (JSONException e) {
            Logger.e(e);
            return "{}";
        }
    }

    public static Object toJsonObject(Object obj) throws JSONException {
        return toJsonObject(obj, "{}");
    }

    private static Object toJsonObject(Object obj, String defaultValue) throws JSONException {
        return obj instanceof Map ? toJSONObject((Map) obj) : (obj instanceof Collection ? toJSONObject((Collection) obj) : (!(obj instanceof Boolean) && !(obj instanceof Byte) && !(obj instanceof Character) && !(obj instanceof Float) && !(obj instanceof Short) && !(obj instanceof Integer) && !(obj instanceof Long) && !(obj instanceof Double) && !(obj instanceof String) ? (obj instanceof JsonSerializable ? ((JsonSerializable) obj).toJsonString() : defaultValue) : obj));
    }

    private static JSONObject toJSONObject(Map map) throws JSONException {
        JSONObject ret = new JSONObject();
        if (map != null && map.size() != 0) {
            Iterator iter = map.entrySet().iterator();

            while (iter.hasNext()) {
                Entry ety = (Entry) iter.next();
                if (ety.getKey() instanceof String) {
                    ret.put((String) ety.getKey(), toJsonObject(ety.getValue(), (String) null));
                }
            }
        }

        return ret;
    }

    private static JSONArray toJSONObject(Collection list) throws JSONException {
        JSONArray ret = new JSONArray();
        if (list != null && list.size() != 0) {
            Iterator iter = list.iterator();

            while (iter.hasNext()) {
                ret.put(toJsonObject(iter.next()));
            }
        }

        return ret;
    }

    @SuppressWarnings("unchecked")
    public static Map praseJSONObject(JSONObject object) {
        TreeMap map = new TreeMap(new Comparator<String>() {
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);
            }
        });
        if (object != null && object.length() != 0) {
            Iterator iter = object.keys();

            while (iter.hasNext()) {
                String key = (String) iter.next();

                try {
                    Object e = object.get(key);
                    if (JSONObject.NULL.equals(e)) {
                        map.put(key, (Object) null);
                    } else if (e instanceof JSONObject) {
                        map.put(key, praseJSONObject((JSONObject) e));
                    } else if (e instanceof JSONArray) {
                        map.put(key, praseJSONArray((JSONArray) e));
                    } else {
                        map.put(key, e);
                    }
                } catch (JSONException e) {
                    Logger.e(e);
                }
            }
        }

        return map;
    }

    public static Collection praseJSONArray(JSONArray array) {
        ArrayList collection = new ArrayList();
        if (array != null && array.length() != 0) {
            for (int i = 0; i < array.length(); ++i) {
                try {
                    Object e = array.get(i);
                    if (e instanceof JSONObject) {
                        collection.add(praseJSONObject((JSONObject) e));
                    } else if (e instanceof JSONArray) {
                        collection.add(praseJSONArray((JSONArray) e));
                    } else {
                        collection.add(e);
                    }
                } catch (JSONException e) {
                    Logger.e(e);
                }
            }
        }

        return collection;
    }

    public static JSONObject getJSONObject(Map map) {
        JSONObject object = null;

        try {
            object = (JSONObject) toJsonObject(map);
        } catch (JSONException e) {
            Logger.e(e);
        }

        return object;
    }

    public static JSONArray getJSONArray(Collection collection) {
        JSONArray array = null;

        try {
            array = (JSONArray) toJsonObject(collection);
        } catch (JSONException e) {
            Logger.e(e);
        }

        return array;
    }

    public static JSONObject getJSONObject(String jsonStr) {
        JSONObject object = new JSONObject();

        try {
            object = new JSONObject(jsonStr);
        } catch (JSONException e) {
            Logger.e(e);
        }

        return object;
    }

    public static JSONArray getJSONArray(String jsonStr) {
        JSONArray array = new JSONArray();

        try {
            array = new JSONArray(jsonStr);
        } catch (JSONException e) {
            Logger.e(e);
        }

        return array;
    }

    public static JSONObject getJSONObject(JSONObject jsonObject, String name) {
        JSONObject object;
        try {
            object = new JSONObject(jsonObject.get(name).toString());
        } catch (JSONException var4) {
            object = new JSONObject();
        }

        return object;
    }
}
