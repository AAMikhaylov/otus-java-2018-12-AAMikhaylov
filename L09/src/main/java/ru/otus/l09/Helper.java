package ru.otus.l09;

import javax.json.Json;
import javax.json.JsonValue;
import java.math.BigDecimal;
import java.util.Collection;

public class Helper {
    final static boolean isCollection(Class<?> cls) {
        if (cls == Object.class || cls == null) return false;
        Class<?>[] interfaces = cls.getInterfaces();
        for (Class intrface : interfaces)
            if (intrface == Collection.class)
                return true;
        return isCollection(cls.getSuperclass());
    }

    final static JsonValue getJsonValue(Object object) {
        if (object.getClass() == String.class)
            return Json.createValue((String) object);
        if (object.getClass() == Byte.class)
            return Json.createValue((Byte) object);
        if (object.getClass() == Boolean.class)
            if ((Boolean) object) return JsonValue.TRUE;
            else return JsonValue.FALSE;
        if (object.getClass() == Short.class)
            return Json.createValue((Short) object);
        if (object.getClass() == Character.class)
            return Json.createValue(object.toString());
        if (object.getClass() == Integer.class)
            return Json.createValue((Integer) object);
        if (object.getClass() == Float.class) {
            BigDecimal bd = new BigDecimal(((Float) object).toString());
            return Json.createValue(bd);
        }
        if (object.getClass() == Double.class)
            return Json.createValue((Double) object);
        if (object.getClass() == Long.class)
            return Json.createValue((long) object);
        return null;
    }

    final static boolean isWrapper(Class<?> cls) {
        if (cls == Byte.class || cls == Boolean.class || cls == Short.class || cls == Character.class ||
                cls == Integer.class || cls == Float.class || cls == Long.class || cls == Double.class)
            return true;
        return false;
    }

    final static boolean isString(Class<?> cls) {
        if (cls == String.class)
            return true;
        return false;
    }
}
