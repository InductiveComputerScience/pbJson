package com.progsbase.libraries.JSON;

import JSON.StringElementMaps.StringElementMap;
import JSON.structures.Element;
import JSON.structures.ElementReference;
import references.references.StringArrayReference;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static JSON.StringElementMaps.StringElementMaps.GetStringElementMapNumberOfKeys;
import static JSON.reader.reader.ReadJSON;
import static references.references.references.CreateStringArrayReferenceLengthValue;

/*
 This class reads JSON and sets the values of an object reflectively:
  - {} -> Map<String, T>
  - [] -> T[] or List<T>
  - number -> double | Double | Float | float | Integer | int | Long | long | Short | short | Byte | byte
  - number -> round, truncate -> Integer | int | Long | long | Short | short | Byte | byte
  - "" -> String
  - null -> null
  - boolean -> Boolean
  - Getters and setters not supported.
 */
public class JSONReflectiveReader {
    public static <T> T readJSON(String json, Class<T> clazz) throws JSONException {
        ElementReference elementReference;
        StringArrayReference errorMessages;
        T t;

        t = null;
        elementReference = new ElementReference();
        errorMessages = CreateStringArrayReferenceLengthValue(0d, "".toCharArray());

        boolean success = ReadJSON(json.toCharArray(), elementReference, errorMessages);

        if(success){
            t = javaifyJSONValue(elementReference.element, clazz, null);
        }

        return t;
    }

    private static <T> T javaifyJSONValue(Element element, Class<T> clazz, Type genericType) throws JSONException {
        T t = null;

        String type = new String(element.type.name);

        if(type.equals("nullValue")){
        }else{
            if (type.equals("object")) {
                t = javaifyJSONObject(element.object, clazz);
            } else if (type.equals("array")) {
                t = javaifyJSONArray(element.array, clazz, genericType);
            } else if (type.equals("string")) {
                if(clazz == String.class){
                    t = (T)new String(element.string);
                }
            } else if (type.equals("number")) {
                if(clazz == Double.class || clazz == double.class){
                    t = (T)new Double(element.number);
                }
                if(clazz == Float.class || clazz == float.class){
                    t = (T)new Float(element.number);
                }
                if(clazz == Integer.class || clazz == int.class){
                    t = (T)new Integer((int)Math.round(element.number));
                }
                if(clazz == Long.class || clazz == long.class){
                    t = (T)new Long(Math.round(element.number));
                }
                if(clazz == Short.class || clazz == short.class){
                    t = (T)new Short((short)Math.round(element.number));
                }
                if(clazz == Byte.class || clazz == byte.class){
                    t = (T)new Byte((byte)Math.round(element.number));
                }
            } else if (type.equals("booleanValue")) {
                if(clazz == Boolean.class || clazz == boolean.class){
                    t = (T)(Boolean)element.booleanValue;
                }
            }
        }

        return t;
    }

    private static <T> T javaifyJSONObject(StringElementMap object, Class<T> clazz) throws JSONException {
        T t;

        try {
            t = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new JSONException(e);
        }

        for(int i = 0; i < GetStringElementMapNumberOfKeys(object); i++){
            try {
                String key = new String(object.stringListRef.stringArray[i].string);
                Field field = clazz.getField(key);
                Object value = javaifyJSONValue(object.elementListRef.array[i], field.getType(), field.getGenericType());
                field.set(t, value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new JSONException(e);
            }
        }

        return t;
    }

    private static <T> T javaifyJSONArray(Element[] array, Class<T> clazz, Type genericType) throws JSONException {

        Class<?> componentType = clazz.getComponentType();
        if(componentType != null){
            Object a[] = (Object[])Array.newInstance(componentType, array.length);

            for(int i = 0; i < array.length; i++){
                a[i] = javaifyJSONValue(array[i], componentType, null);
            }

            return (T)a;
        }else{
            List<Object> list = new ArrayList<>(array.length);
            ParameterizedType p = (ParameterizedType)genericType;
            Class<?> typeClass = (Class<?>)p.getActualTypeArguments()[0];

            for(int i = 0; i < array.length; i++){
                list.add(javaifyJSONValue(array[i], typeClass, null));
            }

            return (T)list;
        }
    }
}
