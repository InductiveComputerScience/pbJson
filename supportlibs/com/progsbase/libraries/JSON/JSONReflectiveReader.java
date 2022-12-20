package com.progsbase.libraries.JSON;

import JSON.StringElementMaps.StringElementMap;
import JSON.structures.Element;
import JSON.structures.ElementReference;
import references.references.StringArrayReference;

import java.lang.reflect.*;
import java.util.*;

import static JSON.StringElementMaps.StringElementMaps.GetStringElementMapNumberOfKeys;
import static JSON.parser.parser.ReadJSON;
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
    public static <T> boolean readJSON(String json, Reference<T> tReference, Class<T> clazz, StringReference errorMessage) {
        return readJSON(json, tReference, clazz, null, errorMessage);
    }

    public static <T> boolean readJSON(String json, Reference<T> tReference, Class<T> clazz, Type genericType, StringReference errorMessage) {
        boolean success;

        try {
            tReference.t = readJSON(json, clazz, genericType);
            success = true;
        } catch (JSONException e) {
            success = false;
            errorMessage.string = e.getMessage();
        }

        return success;
    }

    public static <T> T readJSON(String json, Class<T> clazz) throws JSONException {
        return readJSON(json, clazz, null, true);
    }

    public static <T> T readJSON(String json, Class<T> clazz, boolean complete) throws JSONException {
        return readJSON(json, clazz, null, complete);
    }

    public static <T> T readJSON(String json, Class<T> clazz, Type genericType) throws JSONException {
        return readJSON(json, clazz, genericType, true);
    }

    public static <T> T readJSON(String json, Class<T> clazz, Type genericType, boolean complete) throws JSONException {
        ElementReference elementReference;
        StringArrayReference errorMessages;
        T t;

        t = null;
        elementReference = new ElementReference();
        errorMessages = CreateStringArrayReferenceLengthValue(0d, "".toCharArray());

        boolean success = ReadJSON(json.toCharArray(), elementReference, errorMessages);

        if(success){
            t = javaifyJSONValue(elementReference.element, clazz, genericType, complete);
        }else{
            throw new JSONException(join(errorMessages.stringArray, "\n"));
        }

        return t;
    }

    public static String join(references.references.StringReference[] stringArray, String s) {
        StringBuilder joined = new StringBuilder();
        for(int i = 0; i < stringArray.length; i++){
            joined.append(new String(stringArray[i].string));
            if(i + 1 < stringArray.length) {
                joined.append(s);
            }
        }
        return joined.toString();
    }

    public static <T> T javaifyJSONValue(Element element, Class<T> clazz, Type genericType) throws JSONException {
        return javaifyJSONValue(element, clazz, genericType, true);
    }

    public static <T> T javaifyJSONValue(Element element, Class<T> clazz, Type genericType, boolean complete) throws JSONException {
        T t = null;

        String type = new String(element.type);

        if(type.equals("null")){
        }else{
            if (type.equals("object")) {
                t = javaifyJSONObject(element.object, clazz, complete);
            } else if (type.equals("array")) {
                t = javaifyJSONArray(element.array, clazz, genericType, complete);
            } else if (type.equals("string")) {
                if(clazz == String.class){
                    t = (T)new String(element.string);
                }
                if(clazz.isArray() && clazz.getComponentType() == char.class){
                    t = (T)element.string;
                }
                if(clazz.isEnum()){
                    Method valueOf;
                    try {
                        valueOf = clazz.getMethod("valueOf", String.class);
                    } catch (NoSuchMethodException e) {
                        throw new JSONException(e.getMessage());
                    }
                    try {
                        t = (T)valueOf.invoke(null, new String(element.string));
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                        throw new JSONException(e.getMessage());
                    }
                }
            } else if (type.equals("number")) {
                if(clazz == Double.class || clazz == double.class){
                    t = (T)Double.valueOf(element.number);
                }
                if(clazz == Float.class || clazz == float.class){
                    t = (T)Float.valueOf((float)element.number);
                }
                if(clazz == Integer.class || clazz == int.class){
                    t = (T)Integer.valueOf((int)Math.round(element.number));
                }
                if(clazz == Long.class || clazz == long.class){
                    t = (T)Long.valueOf(Math.round(element.number));
                }
                if(clazz == Short.class || clazz == short.class){
                    t = (T)Short.valueOf((short)Math.round(element.number));
                }
                if(clazz == Byte.class || clazz == byte.class){
                    t = (T)Byte.valueOf((byte)Math.round(element.number));
                }
            } else if (type.equals("boolean")) {
                if(clazz == Boolean.class || clazz == boolean.class){
                    t = (T)(Boolean)element.booleanValue;
                }
            }
        }

        return t;
    }

    public static String [] javaKeywords = new String [] {"abstract", "continue", "for", "new", "switch", "assert", "default", "goto", "package", "synchronized", "boolean", "do", "if", "private", "this", "break", "double", "implements", "protected", "throw", "byte", "else", "import", "public", "throws", "case", "enum", "instanceof", "return", "transient", "catch", "extends", "int", "short", "try", "char", "final", "interface", "static", "void", "class", "finally", "long", "strictfp", "volatile", "const", "float", "native", "super", "while"};

    public static <T> T javaifyJSONObject(StringElementMap object, Class<T> clazz) throws JSONException {
        return javaifyJSONObject(object, clazz, true);
    }

    public static <T> T javaifyJSONObject(StringElementMap object, Class<T> clazz, boolean complete) throws JSONException {
        T t;

        try {
            t = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new JSONException(e);
        }

        for(int i = 0; i < GetStringElementMapNumberOfKeys(object); i++){
            try {
                String key = new String(object.stringListRef.stringArray[i].string);

                Set<String> kws = new HashSet<>(ArrayToListConversion(javaKeywords));

                if(kws.contains(key)){
                    key = key + "x";
                }

                boolean setField;

                if(complete) {
                    setField = true;
                }else{
                    setField = ClassContainsField(clazz, key);
                }

                if(setField) {
                    Field field = clazz.getField(key);
                    Object value = javaifyJSONValue(object.elementListRef.array[i], field.getType(), field.getGenericType(), complete);
                    field.set(t, value);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new JSONException(e);
            }
        }

        return t;
    }

    public static boolean ClassContainsField(Class<?> clazz, String fieldName) {
        for (Field field : clazz.getFields()) {
            if (field.getName().equals(fieldName)) {
                return true;
            }
        }
        return false;
    }

    public static <T> List<T> ArrayToListConversion(T array[]) {
        List<T> list = new ArrayList<>();
        for (T t : array) {
            list.add(t);
        }
        return list;
    }

    public static <T> T javaifyJSONArray(Element[] array, Class<T> clazz, Type genericType, boolean complete) throws JSONException {

        Class<?> componentType = clazz.getComponentType();
        if(componentType != null){
            Object a[] = (Object[])Array.newInstance(componentType, array.length);

            for(int i = 0; i < array.length; i++){
                a[i] = javaifyJSONValue(array[i], componentType, null, complete);
            }

            return (T)a;
        }else{
            List<Object> list = new ArrayList<>(array.length);
            ParameterizedType p = (ParameterizedType)genericType;
            Type type = p.getActualTypeArguments()[0];
            Class<?> typeClass;
            Type typeGeneric;
            if(type instanceof Class){
                typeClass = (Class<?>) type;
                typeGeneric = null;
            }else{
                typeClass = List.class;
                typeGeneric = type;
            }


            for(int i = 0; i < array.length; i++){
                list.add(javaifyJSONValue(array[i], typeClass, typeGeneric, complete));
            }

            return (T)list;
        }
    }
}
