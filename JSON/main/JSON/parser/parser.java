package JSON.parser;

import JSON.TokenLists.*;
import JSON.structures.*;
import references.references.*;

import static JSON.StringElementMaps.StringElementMaps.*;
import static JSON.json.json.*;
import static JSON.tokenReader.tokenReader.*;
import static JSON.tokenTypeEnum.tokenTypeEnum.*;
import static lists.StringList.StringList.*;
import static nnumbers.StringToNumber.StringToNumber.*;
import static references.references.references.*;
import static strstrings.strings.strings.*;

public class parser {
    public static boolean ReadJSON(char[] string, ElementReference elementReference, StringArrayReference errorMessages) {
        TokenArrayReference tokenArrayReference;
        boolean success;

        // Tokenize.
        tokenArrayReference = new TokenArrayReference();
        success = JSONTokenize(string, tokenArrayReference, errorMessages);

        if (success) {
            // Parse.
            success = GetJSONValue(tokenArrayReference.array, elementReference, errorMessages);
        }

        return success;
    }

    public static boolean GetJSONValue(Token [] tokens, ElementReference elementReference, StringArrayReference errorMessages){
        NumberArrayReference counts;
        boolean success;
        NumberReference i;

        i = CreateNumberReference(0d);
        counts = CreateNumberArrayReferenceLengthValue(tokens.length, 0d);

        success = GetJSONValueWithCheckOption(tokens, i, 0d, elementReference, false, counts, errorMessages);

        if(success){
            i.numberValue = 0d;
            GetJSONValueWithCheckOption(tokens, i, 0d, elementReference, true, counts, errorMessages);
        }

        return success;
    }

    public static boolean GetJSONValueWithCheckOption(Token [] tokens, NumberReference i, double depth, ElementReference elementReference, boolean add, NumberArrayReference counts, StringArrayReference errorMessages){
        Token token;
        char [] str, substr;
        double stringToDecimalResult;
        boolean success;

        success = true;
        token = tokens[(int)i.numberValue];

        if(TokenTypeEnumEquals(token.type.name, "openCurly".toCharArray())){
            success = GetJSONObject(tokens, i, depth + 1d, elementReference, add, counts, errorMessages);
        }else if(TokenTypeEnumEquals(token.type.name, "openSquare".toCharArray())){
            success = GetJSONArray(tokens, i, depth + 1d, elementReference, add, counts, errorMessages);
        }else if(TokenTypeEnumEquals(token.type.name, "trueValue".toCharArray())){
            if(add){
                elementReference.element = CreateBooleanElement(true);
            }
            i.numberValue  = i.numberValue + 1d;
        }else if(TokenTypeEnumEquals(token.type.name, "falseValue".toCharArray())){
            if(add){
                elementReference.element = CreateBooleanElement(false);
            }
            i.numberValue  = i.numberValue + 1d;
        }else if(TokenTypeEnumEquals(token.type.name, "nullValue".toCharArray())){
            if(add) {
                elementReference.element = CreateNullElement();
            }
            i.numberValue  = i.numberValue + 1d;
        }else if(TokenTypeEnumEquals(token.type.name, "number".toCharArray())){
            if(add) {
                stringToDecimalResult = nCreateNumberFromDecimalString(token.value);
                elementReference.element = CreateNumberElement(stringToDecimalResult);
            }
            i.numberValue  = i.numberValue + 1d;
        }else if(TokenTypeEnumEquals(token.type.name, "string".toCharArray())){
            if(add) {
                substr = strSubstring(token.value, 1d, token.value.length - 1d);
                elementReference.element = CreateStringElement(substr);
            }
            i.numberValue  = i.numberValue + 1d;
        }else{
            str = "".toCharArray();
            str = strConcatenateString(str, "Invalid token first in value: ".toCharArray());
            str = strAppendString(str, token.type.name);
            AddStringRef(errorMessages, CreateStringReference(str));
            success = false;
        }

        if(success && depth == 0d){
            if(TokenTypeEnumEquals(tokens[(int)i.numberValue].type.name, "end".toCharArray())){
            }else{
                AddStringRef(errorMessages, CreateStringReference("The outer value cannot have any tokens following it.".toCharArray()));
                success = false;
            }
        }

        return success;
    }

    public static boolean GetJSONObject(Token[] tokens, NumberReference i, double depth, ElementReference elementReference, boolean add, NumberArrayReference counts, StringArrayReference errorMessages) {
        Element element, value;
        boolean done, success;
        Token key, colon, comma, closeCurly;
        char [] keystring, str;
        ElementReference valueReference;
        double countIndex, index;

        countIndex = i.numberValue;
        if(add){
            element = CreateObjectElement(counts.numberArray[(int)countIndex]);
        }else{
            element = new Element();
        }
        valueReference = new ElementReference();
        success = true;
        i.numberValue  = i.numberValue + 1d;
        index = 0d;

        if (!TokenTypeEnumEquals(tokens[(int)i.numberValue].type.name, "closeCurly".toCharArray())){
            done = false;

            for (; !done && success; ) {
                key = tokens[(int) i.numberValue];

                if (TokenTypeEnumEquals(key.type.name, "string".toCharArray())) {
                    i.numberValue = i.numberValue + 1d;
                    colon = tokens[(int) i.numberValue];
                    if (TokenTypeEnumEquals(colon.type.name, "colon".toCharArray())) {
                        i.numberValue = i.numberValue + 1d;
                        success = GetJSONValueWithCheckOption(tokens, i, depth, valueReference, add, counts, errorMessages);

                        if (success) {
                            value = valueReference.element;

                            keystring = strSubstring(key.value, 1d, key.value.length - 1d);
                            if(add){
                                SetStringElementMap(element.object, index, keystring, value);
                            }

                            index = index + 1d;

                            comma = tokens[(int) i.numberValue];
                            if (TokenTypeEnumEquals(comma.type.name, "comma".toCharArray())) {
                                // OK
                                i.numberValue = i.numberValue + 1d;
                            } else {
                                done = true;
                            }
                        }
                    } else {
                        str = "".toCharArray();
                        str = strConcatenateString(str, "Expected colon after key in object: ".toCharArray());
                        str = strAppendString(str, colon.type.name);
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
            closeCurly = tokens[(int) i.numberValue];

            if (TokenTypeEnumEquals(closeCurly.type.name, "closeCurly".toCharArray())) {
                // OK
                elementReference.element = element;
                i.numberValue  = i.numberValue + 1d;
            } else {
                AddStringRef(errorMessages, CreateStringReference("Expected close curly brackets at end of object value.".toCharArray()));
                success = false;
            }
        }

        counts.numberArray[(int)countIndex] = index;

        return success;
    }

    public static boolean GetJSONArray(Token[] tokens, NumberReference i, double depth, ElementReference elementReference, boolean add, NumberArrayReference counts, StringArrayReference errorMessages) {
        Element element, value;
        Token nextToken, comma;
        boolean done, success;
        ElementReference valueReference;
        double countIndex, index;

        index = 0d;
        countIndex = i.numberValue;
        i.numberValue  = i.numberValue + 1d;

        if(add){
            element = CreateArrayElement(counts.numberArray[(int)countIndex]);
        }else{
            element = new Element();
        }
        valueReference = new ElementReference();
        success = true;

        nextToken = tokens[(int)i.numberValue];

        if(!TokenTypeEnumEquals(nextToken.type.name, "closeSquare".toCharArray())) {
            done = false;
            for (; !done && success; ) {
                success = GetJSONValueWithCheckOption(tokens, i, depth, valueReference, add, counts, errorMessages);

                if(success){
                    value = valueReference.element;

                    if(add){
                        element.array[(int)index] = value;
                    }

                    index = index + 1d;

                    comma = tokens[(int) i.numberValue];

                    if (TokenTypeEnumEquals(comma.type.name, "comma".toCharArray())) {
                        // OK
                        i.numberValue = i.numberValue + 1d;
                    } else {
                        done = true;
                    }
                }
            }
        }

        nextToken = tokens[(int)i.numberValue];
        if (TokenTypeEnumEquals(nextToken.type.name, "closeSquare".toCharArray())) {
            // OK
            i.numberValue = i.numberValue + 1d;
            elementReference.element = element;
        } else {
            AddStringRef(errorMessages, CreateStringReference("Expected close square bracket at end of array.".toCharArray()));
            success = false;
        }

        elementReference.element = element;
        counts.numberArray[(int)countIndex] = index;

        return success;
    }
}