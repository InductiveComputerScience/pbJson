package com.progsbase.libraries.JSON;

import JSON.structures.Element;

import java.lang.reflect.Field;
import java.util.List;

import static JSON.ElementLists.ElementLists.AddElement;
import static JSON.StringElementMaps.StringElementMaps.PutStringElementMap;
import static JSON.json.json.*;
import static JSON.writer.writer.WriteJSON;

/*
 This class writes a Java Object JSON into a Java Object with the following actual classes:
  - Map<String, T> -> {}
  - List<T> -> []
  - T [] -> []
  - Double | Float | Integer | Long | Short | Byte -> number
  - String -> ""
  - null -> null
  - Boolean -> boolean
 */
public class JSONReflectiveWriter {
    public static <T> String writeJSON(T t) throws JSONException{
        char[] value;

        Element e = unjavaifyJSONValue(t);
        value = WriteJSON(e);

        return new String(value);
    }

    private static <T> Element unjavaifyJSONValue(T t) throws JSONException {
        Element e;

        if(t == null) {
            e = CreateNullElement();
        }else if(t.getClass().isArray()) {
            e = unjavaifyJSONArrayArray(t);
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

    private static <T> Element unjavaifyJSONObject(T t) throws JSONException {
        Element e = CreateObjectElement();

        Field[] fields = t.getClass().getFields();

        for(Field f : fields){
            Element s;
            try {
                s = unjavaifyJSONValue(f.get(t));
            } catch (IllegalAccessException ex) {
                throw new JSONException(ex.getMessage());
            }
            PutStringElementMap(e.object, f.getName().toCharArray(), s);
        }

        return e;
    }

    private static Element unjavaifyJSONArrayList(Object o) throws JSONException {
        Element e = CreateArrayElement();

        List<Object> l = (List<Object>)o;

        for(Object p : l){
            Element s = unjavaifyJSONValue(p);
            e.array = AddElement(e.array, s);
        }

        return e;
    }

    private static Element unjavaifyJSONArrayArray(Object o) throws JSONException {
        Element e = CreateArrayElement();

        Object a[] = (Object[])o;

        for(Object p : a){
            Element s = unjavaifyJSONValue(p);
            e.array = AddElement(e.array, s);
        }

        return e;
    }
}