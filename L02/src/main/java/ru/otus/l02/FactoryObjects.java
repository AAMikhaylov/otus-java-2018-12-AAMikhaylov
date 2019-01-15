package ru.otus.l02;
import java.lang.reflect.InvocationTargetException;
class FactoryObjects<T> {
  private Class<T> cls;
  private int elmCount = 0;
  private String initString = null; 

  FactoryObjects(Class<T> objectsClass) {
    this.cls = objectsClass;
  }
  FactoryObjects(Class<T> objectsClass,String initString) {
    this.cls = objectsClass;
    this.initString = initString;
  }
  FactoryObjects(Class<T> objectsClass,int elmCount) {
    this.cls = objectsClass;
    this.elmCount = elmCount;
  }
  public T get()  {
    try {
    if (initString!=null)
      return cls.getDeclaredConstructor(char[].class).newInstance(initString.toCharArray());
    else
      if (elmCount != 0)
        return cls.getDeclaredConstructor(int.class).newInstance(elmCount);
      else
        return cls.getDeclaredConstructor().newInstance();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
    return null;
  }
}