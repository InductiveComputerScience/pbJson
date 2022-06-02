package com.progsbase.libraries.JSON;

import JSON.structures.Element;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static JSON.StringElementMaps.StringElementMaps.SetStringElementMap;
import static JSON.json.json.*;
import static JSON.writer.writer.WriteJSON;

/*
 This class writes a Java Object JSON into a Java Object with the following actual classes:
  - Map<String, T> -> {}
  - List<T> -> []
  - T [] -> []
  - Double | Float | Integer | Long | Short | Byte -> number
  - String -> ""
  - char [] -> ""
  - null -> null
  - Boolean -> boolean
 */
public class JSONReflectiveWriter {
    public static <T> boolean writeJSON(T t, StringReference jsonReference, StringReference errorMessage) {
        boolean success;

        try {
            jsonReference.string = writeJSON(t);
            success = true;
        } catch (JSONException e) {
            success = false;
            errorMessage.string = e.getMessage();
        }

        return success;
    }

    public static <T> String writeJSON(T t) throws JSONException {
        char[] value;

        Element e = unjavaifyJSONValue(t);
        value = WriteJSON(e);

        return new String(value);
    }

    public static <T> Element unjavaifyJSONValue(T t) throws JSONException {
        Element e;

        if(t == null) {
            e = CreateNullElement();
        }else if(t.getClass().isArray() && t.getClass().getComponentType() == char.class) {
            char [] s = (char[])t;
            e = CreateStringElement(s);
        }else if(t.getClass().isArray()) {
            e = unjavaifyJSONArrayArray(t);
        }else if(t.getClass().isEnum()) {
            e = unjavaifyJSONEnum(t);
        }else if(t instanceof List) {
            e = unjavaifyJSONArrayList(t);
        }else if(t instanceof String) {
            String s = (String)t;
            e = CreateStringElement(s.toCharArray());
        }else if(t instanceof Double) {
            Double n = (Double)t;
            e = CreateNumberElement(n);
        }else if(t instanceof Float) {
            Float n = (Float)t;
            e = CreateNumberElement(n);
        }else if(t instanceof Integer) {
            Integer n = (Integer)t;
            e = CreateNumberElement(n);
        }else if(t instanceof Long) {
            Long n = (Long)t;
            e = CreateNumberElement(n);
        }else if(t instanceof Short) {
            Short n = (Short)t;
            e = CreateNumberElement(n);
        }else if(t instanceof Byte) {
            Byte n = (Byte)t;
            e = CreateNumberElement(n);
        }else if(t instanceof Boolean) {
            Boolean b = (Boolean)t;
            e = CreateBooleanElement(b);
        }else{
            e = unjavaifyJSONObject(t);
        }

        return e;
    }

    public static <T> Element unjavaifyJSONEnum(T t) {
        return CreateStringElement(t.toString().toCharArray());
    }

    public static <T> Element unjavaifyJSONObject(T t) throws JSONException {
        Field[] fields = t.getClass().getFields();
        Element e = CreateObjectElement(fields.length);
        int i = 0;

        for(Field f : fields){
            Element s;
            try {
                s = unjavaifyJSONValue(f.get(t));
            } catch (IllegalAccessException ex) {
                throw new JSONException(ex.getMessage());
            }
            String key = f.getName();

            if(key.endsWith("x")){
                Set<String> kws = new HashSet<>(JSONReflectiveReader.ArrayToListConversion(JSONReflectiveReader.javaKeywords));
                String newkey = key.substring(0, key.length() - 1);
                if(kws.contains(newkey)){
                    key = newkey;
                }
            }

            SetStringElementMap(e.object, i, key.toCharArray(), s);
            i++;
        }

        return e;
    }

    public static Element unjavaifyJSONArrayList(Object o) throws JSONException {
        List<Object> l = (List<Object>)o;
        Element e = CreateArrayElement(l.size());
        int i = 0;

        for(Object p : l){
            Element s = unjavaifyJSONValue(p);
            e.array[i] = s;
            i++;
        }

        return e;
    }

    public static Element unjavaifyJSONArrayArray(Object o) throws JSONException {
        Object a[] = (Object[])o;
        Element e = CreateArrayElement(a.length);
        int i = 0;

        for(Object p : a){
            Element s = unjavaifyJSONValue(p);
            e.array[i] = s;
            i++;
        }

        return e;
    }
}