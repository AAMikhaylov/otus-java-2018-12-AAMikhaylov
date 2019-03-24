package ru.otus.l09;

import javax.json.*;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;


public class JSonWriter {
    private OutputStream os;

    public JSonWriter(OutputStream os) {
        this.os = os;
    }

    private boolean isCollection(Class<?> cls) {
        if (cls == null) return false;
        Class<?>[] interfaces = cls.getInterfaces();
        for (Class intrface : interfaces)
            if (intrface == Collection.class) {
                return true;
            } else if (isCollection(intrface))
                return true;
        return false;
    }

    private void addJsonCollection(JsonObjectBuilder buildingJsonObject, Object srcObject, Field f) {
        try {
            Collection<?> collect = (Collection) f.get(srcObject);
            if (collect.size() > 0) {
                JsonArrayBuilder jsonArray = Json.createArrayBuilder();
                Iterator<?> itr = collect.iterator();
                while (itr.hasNext()) {
                    Object arrElm = itr.next();
                    addJsonArrayElm(jsonArray, arrElm);
                }
                buildingJsonObject.add(f.getName(), jsonArray);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    private void addJsonArrayElm(JsonArrayBuilder jsonArray, Object arrayElm) {
        if (arrayElm != null) {
            if (arrayElm.getClass() == byte.class || arrayElm.getClass() == Byte.class)
                jsonArray.add((Byte) arrayElm);
            else if (arrayElm.getClass() == boolean.class || arrayElm.getClass() == Boolean.class)
                jsonArray.add((Boolean) arrayElm);
            else if (arrayElm.getClass() == char.class || arrayElm.getClass() == Character.class)
                jsonArray.add((Character) arrayElm);
            else if (arrayElm.getClass() == short.class || arrayElm.getClass() == Short.class)
                jsonArray.add((Short) arrayElm);
            else if (arrayElm.getClass() == int.class || arrayElm.getClass() == Integer.class)
                jsonArray.add((Integer) arrayElm);
            else if (arrayElm.getClass() == float.class || arrayElm.getClass() == Float.class)
                jsonArray.add((Float) arrayElm);
            else if (arrayElm.getClass() == long.class || arrayElm.getClass() == Long.class)
                jsonArray.add((Long) arrayElm);
            else if (arrayElm.getClass() == double.class || arrayElm.getClass() == Double.class)
                jsonArray.add((Double) arrayElm);
            else if (arrayElm.getClass() == String.class)
                jsonArray.add((String) arrayElm);
            else
                jsonArray.add(jsonBuild(arrayElm));
        }
    }

    private void addJsonArray(JsonObjectBuilder buildingJsonObject, Object srcObject, Field f) {
        try {
            Object objArray = f.get(srcObject);
            if (Array.getLength(objArray) > 0) {
                JsonArrayBuilder jsonArray = Json.createArrayBuilder();
                for (int i = 0; i < Array.getLength(objArray); i++) {
                    Object arrayElm = Array.get(objArray, i);
                    addJsonArrayElm(jsonArray, arrayElm);
                }
                buildingJsonObject.add(f.getName(), jsonArray);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void addJsonElement(JsonObjectBuilder buildingJsonObject, Object obj, Field f) {
        try {
            if (f.getType() == byte.class || f.getType() == Byte.class)
                buildingJsonObject.add(f.getName(), f.getByte(obj));
            else if (f.getType() == boolean.class || f.getType() == Boolean.class)
                buildingJsonObject.add(f.getName(), f.getBoolean(obj));
            else if (f.getType() == char.class || f.getType() == Character.class)
                buildingJsonObject.add(f.getName(), f.getChar(obj));
            else if (f.getType() == short.class || f.getType() == Short.class)
                buildingJsonObject.add(f.getName(), f.getShort(obj));
            else if (f.getType() == int.class || f.getType() == Integer.class)
                buildingJsonObject.add(f.getName(), f.getInt(obj));
            else if (f.getType() == float.class || f.getType() == Float.class)
                buildingJsonObject.add(f.getName(), f.getFloat(obj));
            else if (f.getType() == long.class || f.getType() == Long.class)
                buildingJsonObject.add(f.getName(), f.getLong(obj));
            else if (f.getType() == double.class || f.getType() == Double.class)
                buildingJsonObject.add(f.getName(), f.getDouble(obj));
            else if (f.getType() == String.class)
                buildingJsonObject.add(f.getName(), (String) f.get(obj));
            else if (f.getType().isArray())
                addJsonArray(buildingJsonObject, obj, f);
            else if (isCollection(f.getType()))
                addJsonCollection(buildingJsonObject, obj, f);
            else {
                buildingJsonObject.add(f.getName(), jsonBuild((Object) f.get(obj)));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    JsonObjectBuilder jsonBuild(Object object) {
        if (object == null) return null;
        JsonObjectBuilder buildingObject = Json.createObjectBuilder();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            addJsonElement(buildingObject, object, f);
        }
        return buildingObject;
    }
    public void writeObject(Object obj) {
        JsonObjectBuilder buildingObject = jsonBuild(obj);
        JsonObject jo = buildingObject.build();
        JsonWriter jw = Json.createWriter(os);
        jw.writeObject(jo);
    }

}
