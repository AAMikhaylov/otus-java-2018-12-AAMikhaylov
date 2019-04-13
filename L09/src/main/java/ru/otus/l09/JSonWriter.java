package ru.otus.l09;

import javax.json.*;
import java.io.IOException;
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

    private JsonValue getJsonForCollection(Object collectObject) {
        Collection<?> collection = (Collection) collectObject;
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        if (collectObject != null) {
            Iterator<?> itr = collection.iterator();
            while (itr.hasNext()) {
                Object collectElm = itr.next();
                if (collectElm == null)
                    jsonArray.add(JsonValue.NULL);
                else
                    jsonArray.add(getJson(collectElm));
            }
        }
        return jsonArray.build();
    }

    private JsonValue getJsonForArray(Object array) {
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        if (array != null) {
            for (int i = 0; i < Array.getLength(array); i++) {
                Object arrayElm = Array.get(array, i);
                if (arrayElm == null)
                    jsonArray.add(JsonValue.NULL);
                else
                    jsonArray.add(getJson(arrayElm));
            }
        }
        return jsonArray.build();
    }

    private JsonValue getJsonForObject(Object object) {
        JsonObjectBuilder buildingObject = Json.createObjectBuilder();
        Field[] fields = object.getClass().getDeclaredFields();
        try {
            for (Field f : fields) {
                f.setAccessible(true);
                if (!Modifier.isStatic(f.getModifiers()) && !Modifier.isTransient(f.getModifiers())) {
                    if (f.get(object) != null)
                        buildingObject.add(f.getName(), getJson(f.get(object)));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return buildingObject.build();
    }

    JsonValue getJson(Object object) {
        if (object == null)
            return JsonValue.NULL;
        if (Helper.isString(object.getClass()))
            return Json.createValue(object.toString());
        if (Helper.isWrapper(object.getClass()))
            return Helper.getJsonValue(object);
        if (object.getClass().isArray())
            return getJsonForArray(object);
        if (Helper.isCollection(object.getClass()))
            return getJsonForCollection(object);
        return getJsonForObject(object);
    }

    public void writeObject(Object object) {
        String jsonString = getJson(object).toString();
        try {
            os.write(jsonString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


