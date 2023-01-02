package JSON.Parser;

import DataStructures.Array.Structures.Data;
import DataStructures.Array.Structures.DataReference;
import references.references.*;

import static DataStructures.Array.Structures.Structures.*;
import static JSON.TokenReader.TokenReader.*;
import static arrays.arrays.arrays.*;
import static charCharacters.Characters.Characters.*;
import static lists.StringList.StringList.*;
import static nnumbers.StringToNumber.StringToNumber.*;
import static references.references.references.*;
import static strstrings.strings.strings.*;

public class Parser {
    public static boolean ReadJSON(char[] string, DataReference dataReference, StringArrayReference errorMessages) {
        StringArrayReference tokenArrayReference;
        boolean success;

        // Tokenize.
        tokenArrayReference = new StringArrayReference();
        success = JSONTokenize(string, tokenArrayReference, errorMessages);

        if (success) {
            // Parse.
            success = GetJSONValue(tokenArrayReference.stringArray, dataReference, errorMessages);
        }

        return success;
    }

    public static boolean GetJSONValue(StringReference [] tokens, DataReference dataReference, StringArrayReference errorMessages){
        NumberReference i;
        boolean success;

        i = CreateNumberReference(0d);
        success = GetJSONValueRecursive(tokens, i, 0d, dataReference, errorMessages);

        return success;
    }

    public static boolean GetJSONValueRecursive(StringReference [] tokens, NumberReference i, double depth, DataReference dataReference, StringArrayReference errorMessages){
        char [] str, substr, token;
        double stringToDecimalResult;
        boolean success;

        success = true;
        token = tokens[(int)i.numberValue].string;

        if(StringsEqual(token, "{".toCharArray())){
            success = GetJSONObject(tokens, i, depth + 1d, dataReference, errorMessages);
        }else if(StringsEqual(token, "[".toCharArray())){
            success = GetJSONArray(tokens, i, depth + 1d, dataReference, errorMessages);
        }else if(StringsEqual(token, "true".toCharArray())){
            dataReference.data = CreateBooleanData(true);
            i.numberValue  = i.numberValue + 1d;
        }else if(StringsEqual(token, "false".toCharArray())){
            dataReference.data = CreateBooleanData(false);
            i.numberValue  = i.numberValue + 1d;
        }else if(StringsEqual(token, "null".toCharArray())){
            dataReference.data = CreateNoTypeData();
            i.numberValue  = i.numberValue + 1d;
        }else if(charIsNumber(token[0]) || token[0] == '-'){
            stringToDecimalResult = nCreateNumberFromDecimalString(token);
            dataReference.data = CreateNumberData(stringToDecimalResult);
            i.numberValue  = i.numberValue + 1d;
        }else if(token[0] == '\"'){
            substr = strSubstring(token, 1d, token.length - 1d);
            dataReference.data = CreateStringData(substr);
            i.numberValue  = i.numberValue + 1d;
        }else{
            str = "".toCharArray();
            str = strConcatenateString(str, "Invalid token first in value: ".toCharArray());
            str = strAppendString(str, token);
            AddStringRef(errorMessages, CreateStringReference(str));
            success = false;
        }

        if(success && depth == 0d){
            if(StringsEqual(tokens[(int)i.numberValue].string, "<end>".toCharArray())){
            }else{
                AddStringRef(errorMessages, CreateStringReference("The outer value cannot have any tokens following it.".toCharArray()));
                success = false;
            }
        }

        return success;
    }

    public static boolean GetJSONObject(StringReference [] tokens, NumberReference i, double depth, DataReference dataReference, StringArrayReference errorMessages) {
        Data data, value;
        boolean done, success;
        char [] key, colon, comma, closeCurly;
        char [] keystring, str;
        DataReference valueReference;

        data = CreateStructData();
        valueReference = new DataReference();
        success = true;
        i.numberValue  = i.numberValue + 1d;

        if (!StringsEqual(tokens[(int)i.numberValue].string, "}".toCharArray())){
            done = false;

            for (; !done && success; ) {
                key = tokens[(int) i.numberValue].string;

                if (key[0] == '\"') {
                    i.numberValue = i.numberValue + 1d;
                    colon = tokens[(int) i.numberValue].string;
                    if (StringsEqual(colon, ":".toCharArray())) {
                        i.numberValue = i.numberValue + 1d;
                        success = GetJSONValueRecursive(tokens, i, depth, valueReference, errorMessages);

                        if (success) {
                            keystring = strSubstring(key, 1d, key.length - 1d);
                            value = valueReference.data;

                            AddDataToStruct(data.structure, keystring, value);

                            comma = tokens[(int) i.numberValue].string;
                            if (StringsEqual(comma, ",".toCharArray())) {
                                // OK
                                i.numberValue = i.numberValue + 1d;
                            } else {
                                done = true;
                            }
                        }
                    } else {
                        str = "".toCharArray();
                        str = strConcatenateString(str, "Expected colon after key in object: ".toCharArray());
                        str = strAppendString(str, colon);
                        AddStringRef(errorMessages, CreateStringReference(str));

                        success = false;
                        done = true;
                    }
                } else {
                    AddStringRef(errorMessages, CreateStringReference("Expected string as key in object.".toCharArray()));

                    done = true;
                    success = false;
                }
            }
        }

        if(success) {
            closeCurly = tokens[(int) i.numberValue].string;

            if (StringsEqual(closeCurly, "}".toCharArray())) {
                // OK
                dataReference.data = data;
                i.numberValue  = i.numberValue + 1d;
            } else {
                AddStringRef(errorMessages, CreateStringReference("Expected close curly brackets at end of object value.".toCharArray()));
                success = false;
            }
        }

        delete(valueReference);

        return success;
    }

    public static boolean GetJSONArray(StringReference [] tokens, NumberReference i, double depth, DataReference dataReference, StringArrayReference errorMessages) {
        Data data, value;
        char [] nextToken, comma;
        boolean done, success;
        DataReference valueReference;

        i.numberValue  = i.numberValue + 1d;

        valueReference = new DataReference();
        success = true;
        data = CreateArrayData();

        nextToken = tokens[(int)i.numberValue].string;

        if(!StringsEqual(nextToken, "]".toCharArray())) {
            done = false;
            for (; !done && success; ) {
                success = GetJSONValueRecursive(tokens, i, depth, valueReference, errorMessages);

                if(success){
                    value = valueReference.data;
                    AddDataToArray(data.array, value);

                    comma = tokens[(int) i.numberValue].string;

                    if (StringsEqual(comma, ",".toCharArray())) {
                        // OK
                        i.numberValue = i.numberValue + 1d;
                    } else {
                        done = true;
                    }
                }
            }
        }

        nextToken = tokens[(int)i.numberValue].string;
        if (StringsEqual(nextToken, "]".toCharArray())) {
            // OK
            i.numberValue = i.numberValue + 1d;
        } else {
            AddStringRef(errorMessages, CreateStringReference("Expected close square bracket at end of array.".toCharArray()));
            success = false;
        }

        dataReference.data = data;
        delete(valueReference);

        return success;
    }

    public static void delete(Object o){
        // Java has garbage collection.
    }
}
