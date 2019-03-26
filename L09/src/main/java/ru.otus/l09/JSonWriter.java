package ru.otus.l09;

import javax.json.*;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Iterator;


public class JSonWriter {
    private OutputStream os;

    public JSonWriter(OutputStream os) {
        this.os = os;
    }

    private void addJsonCollection(JsonObjectBuilder buildingJsonObject, Object collectObject, String fieldName) {
        Collection<?> collection = (Collection) collectObject;
        if (collection.size() > 0) {
            JsonArrayBuilder jsonArray = Json.createArrayBuilder();
            Iterator<?> itr = collection.iterator();
            while (itr.hasNext()) {
                Object collectElm = itr.next();
                if (collectElm != null) {
                    if (Helper.isWrapper(collectElm.getClass()) || Helper.isString(collectElm.getClass())) {
                        JsonValue jv = Helper.getJsonValue(collectElm);
                        if (jv != null)
                            jsonArray.add(jv);
                    } else jsonArray.add(jsonBuild(collectElm));
                }
            }
            buildingJsonObject.add(fieldName, jsonArray);
        }
    }

    private void addJsonArray(JsonObjectBuilder buildingJsonObject, Object array, String fieldName) {
        if (Array.getLength(array) > 0) {
            JsonArrayBuilder jsonArray = Json.createArrayBuilder();
            for (int i = 0; i < Array.getLength(array); i++) {
                Object arrayElm = Array.get(array, i);
                if (arrayElm != null) {
                    if (Helper.isWrapper(arrayElm.getClass()) || Helper.isString(arrayElm.getClass())) {
                        JsonValue jv = Helper.getJsonValue(arrayElm);
                        if (jv != null)
                            jsonArray.add(jv);
                    } else jsonArray.add(jsonBuild(arrayElm));
                }
            }
            buildingJsonObject.add(fieldName, jsonArray);
        }
    }

    private void addJsonElement(JsonObjectBuilder buildingJsonObject, Object object, String fieldName) {
        if (Helper.isString(object.getClass()) || Helper.isWrapper(object.getClass())) {
            JsonValue jv;
            jv = Helper.getJsonValue(object);
            if (jv != null)
                buildingJsonObject.add(fieldName, jv);
        } else if (object.getClass().isArray())
            addJsonArray(buildingJsonObject, object, fieldName);
        else if (Helper.isCollection(object.getClass()))
            addJsonCollection(buildingJsonObject, object, fieldName);
        else {
            buildingJsonObject.add(fieldName, jsonBuild(object));
        }
    }

    JsonObjectBuilder jsonBuild(Object object) {
        if (object == null) return null;
        JsonObjectBuilder buildingObject = Json.createObjectBuilder();
        Field[] fields = object.getClass().getDeclaredFields();
        try {
            for (Field f : fields) {
                f.setAccessible(true);
                if (!Modifier.isStatic(f.getModifiers()) && !Modifier.isTransient(f.getModifiers()) && f.get(object) != null) {
                    addJsonElement(buildingObject, f.get(object), f.getName());
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return buildingObject;
    }

    public void writeObject(Object obj) {
        JsonObjectBuilder buildingObject = jsonBuild(obj);
        JsonObject jo = buildingObject.build();
        JsonWriter jw = Json.createWriter(os);
        System.out.println("json=" + jo);
        jw.writeObject(jo);
    }

}
