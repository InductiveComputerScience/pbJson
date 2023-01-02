package com.progsbase.libraries.JSON;

import DataStructures.Array.Structures.Data;

import java.util.List;
import java.util.Map;

import static DataStructures.Array.Structures.Structures.*;
import static JSON.Writer.Writer.WriteJSON;

/*
 This class writes a Java Object into a string with the following specs:
  - Map<String, Object> -> {}
  - List<Object> -> []
  - Object [] -> []
  - Double | Float | Integer | Long | Short | Byte -> number
  - String -> ""
  - null -> null
  - Boolean -> boolean
 */
public class JSONObjectWriter {
    public static String writeJSON(Object object){
        char[] value;

        try {
            Data e = unjavaifyJSONValue(object);
            value = WriteJSON(e);
        } catch (JSONException ex) {
            value = "".toCharArray();
        }

        return new String(value);
    }

    public static String writeJSONExceptionOnFailure(Object o) throws JSONException {
        Data e = unjavaifyJSONValue(o);
        char[] value = WriteJSON(e);

        return new String(value);
    }

    public static StringReturn writeJSONWithCheck(Object o) {
        StringReturn stringReturn = new StringReturn();

        try {
            Data e = unjavaifyJSONValue(o);
            stringReturn.success = true;
            stringReturn.errorMessage = "";
            stringReturn.str = new String(WriteJSON(e));
        } catch (JSONException ex) {
            stringReturn.success = false;
            stringReturn.errorMessage = ex.getMessage();
            stringReturn.str = "";
        }

        return stringReturn;
    }

    public static Data unjavaifyJSONValue(Object o) throws JSONException {
        Data e;

        if(o == null) {
            e = CreateNoTypeData();
        }else if(o instanceof Map) {
            e = unjavaifyJSONObject(o);
        }else if(o.getClass().isArray()) {
            e = unjavaifyJSONArrayArray(o);
        }else if(o instanceof List) {
            e = unjavaifyJSONArrayList(o);
        }else if(o instanceof String) {
            String s = (String)o;
            e = CreateStringData(s.toCharArray());
        }else if(o instanceof Double) {
            Double n = (Double)o;
            e = CreateNumberData(n);
        }else if(o instanceof Float) {
            Float n = (Float)o;
            e = CreateNumberData(n);
        }else if(o instanceof Integer) {
            Integer n = (Integer)o;
            e = CreateNumberData(n);
        }else if(o instanceof Long) {
            Long n = (Long)o;
            e = CreateNumberData(n);
        }else if(o instanceof Short) {
            Short n = (Short)o;
            e = CreateNumberData(n);
        }else if(o instanceof Byte) {
            Byte n = (Byte)o;
            e = CreateNumberData(n);
        }else if(o instanceof Boolean) {
            Boolean b = (Boolean)o;
            e = CreateBooleanData(b);
        }else{
            throw new JSONException("Cannot be converted to JSON structure: " + o.getClass());
        }

        return e;
    }

    public static Data unjavaifyJSONObject(Object o) throws JSONException {
        Map<String, Object> m = (Map)o;
        Data e = CreateStructData();
        int i = 0;

        for(Map.Entry<String, Object> p : m.entrySet()){
            Data s = unjavaifyJSONValue(p.getValue());
            AddDataToStruct(e.structure, p.getKey().toCharArray(), s);
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