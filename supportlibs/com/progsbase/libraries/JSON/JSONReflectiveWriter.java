package com.progsbase.libraries.JSON;

import DataStructures.Array.Structures.Data;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static DataStructures.Array.Structures.Structures.*;
import static JSON.Writer.Writer.WriteJSON;

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

        Data e = unjavaifyJSONValue(t);
        value = WriteJSON(e);

        return new String(value);
    }

    public static <T> Data unjavaifyJSONValue(T t) throws JSONException {
        Data e;

        if(t == null) {
            e = CreateNoTypeData();
        }else if(t.getClass().isArray() && t.getClass().getComponentType() == char.class) {
            char [] s = (char[])t;
            e = CreateStringData(s);
        }else if(t.getClass().isArray()) {
            e = unjavaifyJSONArrayArray(t);
        }else if(t.getClass().isEnum()) {
            e = unjavaifyJSONEnum(t);
        }else if(t instanceof List) {
            e = unjavaifyJSONArrayList(t);
        }else if(t instanceof String) {
            String s = (String)t;
            e = CreateStringData(s.toCharArray());
        }else if(t instanceof Double) {
            Double n = (Double)t;
            e = CreateNumberData(n);
        }else if(t instanceof Float) {
            Float n = (Float)t;
            e = CreateNumberData(n);
        }else if(t instanceof Integer) {
            Integer n = (Integer)t;
            e = CreateNumberData(n);
        }else if(t instanceof Long) {
            Long n = (Long)t;
            e = CreateNumberData(n);
        }else if(t instanceof Short) {
            Short n = (Short)t;
            e = CreateNumberData(n);
        }else if(t instanceof Byte) {
            Byte n = (Byte)t;
            e = CreateNumberData(n);
        }else if(t instanceof Boolean) {
            Boolean b = (Boolean)t;
            e = CreateBooleanData(b);
        }else{
            e = unjavaifyJSONObject(t);
        }

        return e;
    }

    public static <T> Data unjavaifyJSONEnum(T t) {
        return CreateStringData(t.toString().toCharArray());
    }

    public static <T> Data unjavaifyJSONObject(T t) throws JSONException {
        Field[] fields = t.getClass().getFields();
        Data e = CreateStructData();
        int i = 0;

        for(Field f : fields){
            Data s;
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

            AddDataToStruct(e.structure, key.toCharArray(), s);
            i++;
        }

        return e;
    }

    public static Data unjavaifyJSONArrayList(Object o) throws JSONException {
        List<Object> l = (List<Object>)o;
        Data e = CreateArrayData();
        int i = 0;

        for(Object p : l){
            Data s = unjavaifyJSONValue(p);
            AddDataToArray(e.array, s);
            i++;
        }

        return e;
    }

    public static Data unjavaifyJSONArrayArray(Object o) throws JSONException {
        Object a[] = (Object[])o;
        Data e = CreateArrayData();

        for(Object p : a){
            Data s = unjavaifyJSONValue(p);
            AddDataToArray(e.array, s);
        }

        return e;
    }
}