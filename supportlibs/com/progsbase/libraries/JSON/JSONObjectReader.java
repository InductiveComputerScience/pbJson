package com.progsbase.libraries.JSON;

import DataStructures.Array.Structures.*;
import references.references.*;
import references.references.StringReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static DataStructures.Array.Arrays.Arrays.ArrayIndex;
import static DataStructures.Array.Structures.Structures.*;
import static JSON.Parser.Parser.*;
import static references.references.references.*;

/*
 This class reads JSON into a Java Object with the following actual classes:
  - {} -> Map<String, Object>
  - [] -> List<Object>
  - number -> Double
  - "" -> String
  - null -> null
  - boolean -> Boolean
 */
public class JSONObjectReader {
    public static Object readJSON(String json){
        DataReference dataReference;
        StringArrayReference errorMessages;
        Object object;

        object = null;
        dataReference = new DataReference();
        errorMessages = CreateStringArrayReferenceLengthValue(0d, "".toCharArray());

        boolean success = ReadJSON(json.toCharArray(), dataReference, errorMessages);

        if(success){
            object = javaifyJSONValue(dataReference.data);
        }

        return object;
    }

    public static Object readJSONExceptionOnFailure(String json) throws JSONException {
        DataReference dataReference;
        StringArrayReference errorMessages;
        Object object;

        dataReference = new DataReference();
        errorMessages = CreateStringArrayReferenceLengthValue(0d, "".toCharArray());

        boolean success = ReadJSON(json.toCharArray(), dataReference, errorMessages);

        if(success){
            object = javaifyJSONValue(dataReference.data);
        }else{
            String errorMessage = joinErrorMessages(errorMessages);

            throw new JSONException(errorMessage);
        }

        return object;
    }

    public static String joinErrorMessages(StringArrayReference errorMessages) {
        StringBuilder errorMessage = new StringBuilder();

        for (int i = 0; i < errorMessages.stringArray.length; i++) {
            errorMessage.append(i + 1);
            errorMessage.append(". ");
            errorMessage.append(new String(errorMessages.stringArray[i].string));
            errorMessage.append(" ");
        }

        return errorMessage.toString();
    }

    public static JSONReturn readJSONWithCheck(String json) {
        DataReference dataReference;
        StringArrayReference errorMessages;
        Object object;

        object = null;
        JSONReturn jsonReturn = new JSONReturn();
        dataReference = new DataReference();
        errorMessages = CreateStringArrayReferenceLengthValue(0d, "".toCharArray());

        boolean success = ReadJSON(json.toCharArray(), dataReference, errorMessages);

        if(success){
            object = javaifyJSONValue(dataReference.data);
        }else{
            jsonReturn.errorMessage = joinErrorMessages(errorMessages);
        }

        jsonReturn.object = object;
        jsonReturn.success = success;

        return jsonReturn;
    }

    public static Object javaifyJSONValue(Data data) {
        Object o;

        o = null;

        if(IsStructure(data)){
            o = javaifyJSONObject(data.structure);
        }else if(IsArray(data)){
            o = javaifyJSONArray(data.array);
        }else if(IsString(data)){
            o = new String(data.string);
        }else if(IsNumber(data)){
            o = data.number;
        }else if(IsBoolean(data)){
            o = data.booleanx;
        }else if(IsNoType(data)){
            o = null;
        }

        return o;
    }

    public static Object javaifyJSONObject(Structure object) {
        Map<String, Object> resultObject = new HashMap<>();

        StringReference[] keys = GetStructKeys(object);

        for(int i = 0; i < keys.length; i++){
            resultObject.put(new String(keys[i].string), javaifyJSONValue(GetDataFromStruct(object, keys[i].string)));
        }

        return resultObject;
    }

    public static Object javaifyJSONArray(Array array) {
        List<Object> resultArray = new ArrayList<>();

        for(int i = 0; i < array.length; i++){
            resultArray.add(javaifyJSONValue(ArrayIndex(array, i)));
        }

        return resultArray;
    }
}