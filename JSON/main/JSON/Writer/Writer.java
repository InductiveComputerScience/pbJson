package JSON.Writer;

import DataStructures.Array.Structures.*;
import references.references.*;

import static DataStructures.Array.Arrays.Arrays.ArrayIndex;
import static DataStructures.Array.Structures.Structures.*;
import static JSON.LengthComputer.LengthComputer.*;
import static java.lang.Math.*;
import static nnumbers.NumberToString.NumberToString.*;
import static references.references.references.*;
import static strstrings.stream.stream.*;

public class Writer {
    public static char[] WriteJSON(Data data) {
        char[] result;
        double length;
        NumberReference index;

        length = ComputeJSONStringLength(data);
        result = new char[(int)length];
        index = CreateNumberReference(0d);

        if(IsStructure(data)){
            WriteObject(data, result, index);
        }else if(IsString(data)){
            WriteString(data, result, index);
        }else if(IsArray(data)){
            WriteArray(data, result, index);
        }else if(IsNumber(data)){
            WriteNumber(data, result, index);
        }else if(IsNoType(data)){
            strWriteStringToStingStream(result, index, "null".toCharArray());
        }else if(IsBoolean(data)){
            WriteBooleanValue(data, result, index);
        }

        return result;
    }

    public static void WriteBooleanValue(Data element, char[] result, NumberReference index) {

        if(element.booleanx){
            strWriteStringToStingStream(result, index, "true".toCharArray());
        }else{
            strWriteStringToStingStream(result, index, "false".toCharArray());
        }
    }

    public static void WriteNumber(Data element, char[] result, NumberReference index) {
        char[] numberString;

        if(element.number != 0d) {
            if (abs(element.number) >= pow(10d, 15d) || abs(element.number) <= pow(10d, -15d)) {
                numberString = nCreateStringScientificNotationDecimalFromNumber(element.number);
            } else {
                numberString = nCreateStringDecimalFromNumber(element.number);
            }
        }else{
            numberString = nCreateStringDecimalFromNumber(element.number);
        }

        strWriteStringToStingStream(result, index, numberString);

        // delete(numberString); // TODO: Start using = null instead,
    }

    public static void WriteArray(Data data, char[] result, NumberReference index) {
        char [] s;
        Data entry;
        double i;

        strWriteStringToStingStream(result, index, "[".toCharArray());

        for(i = 0; i < data.array.length; i = i + 1d){
            entry = ArrayIndex(data.array, i);

            s = WriteJSON(entry);
            strWriteStringToStingStream(result, index, s);

            if(i + 1d != data.array.length){
                strWriteStringToStingStream(result, index, ",".toCharArray());
            }
        }

        strWriteStringToStingStream(result, index, "]".toCharArray());
    }

    public static void WriteString(Data element, char[] result, NumberReference index) {
        strWriteStringToStingStream(result, index, "\"".toCharArray());
        element.string = JSONEscapeString(element.string);
        strWriteStringToStingStream(result, index, element.string);
        strWriteStringToStingStream(result, index, "\"".toCharArray());
    }

    public static char[] JSONEscapeString(char[] string) {
        double i, length;
        NumberReference index, lettersReference;
        char ns [], escaped [];

        length = JSONEscapedStringLength(string);

        ns = new char [(int)length];
        index = CreateNumberReference(0d);
        lettersReference = CreateNumberReference( 0d);

        for(i = 0d; i < string.length; i = i + 1d){
            if(JSONMustBeEscaped(string[(int)i], lettersReference)){
                escaped = JSONEscapeCharacter(string[(int)i]);
                strWriteStringToStingStream(ns, index, escaped);
            }else{
                strWriteCharacterToStingStream(ns, index, string[(int)i]);
            }
        }

        return ns;
    }

    public static double JSONEscapedStringLength(char[] string) {
        NumberReference lettersReference;
        double i, length;

        lettersReference = CreateNumberReference(0d);
        length = 0d;

        for(i = 0d; i < string.length; i = i + 1d){
            if(JSONMustBeEscaped(string[(int)i], lettersReference)){
                length = length + lettersReference.numberValue;
            }else{
                length = length + 1d;
            }
        }
        return length;
    }

    public static char[] JSONEscapeCharacter(char c) {
        double code;
        char escaped [];
        StringReference hexNumber;
        double q, rs, s, b, f, n, r, t;

        code = c;

        q = 34d;
        rs = 92d;
        s = 47d;
        b = 8d;
        f = 12d;
        n = 10d;
        r = 13d;
        t = 9d;

        hexNumber = new StringReference();

        if(code == q){
            escaped = new char [2];
            escaped[0] = '\\';
            escaped[1] = '\"';
        }else if(code == rs){
            escaped = new char [2];
            escaped[0] = '\\';
            escaped[1] = '\\';
        }else if(code == s){
            escaped = new char [2];
            escaped[0] = '\\';
            escaped[1] = '/';
        }else if(code == b){
            escaped = new char [2];
            escaped[0] = '\\';
            escaped[1] = 'b';
        }else if(code == f){
            escaped = new char [2];
            escaped[0] = '\\';
            escaped[1] = 'f';
        }else if(code == n){
            escaped = new char [2];
            escaped[0] = '\\';
            escaped[1] = 'n';
        }else if(code == r){
            escaped = new char [2];
            escaped[0] = '\\';
            escaped[1] = 'r';
        }else if(code == t){
            escaped = new char [2];
            escaped[0] = '\\';
            escaped[1] = 't';
        }else if(code >= 0d && code <= 31d){
            escaped = new char [6];
            escaped[0] = '\\';
            escaped[1] = 'u';
            escaped[2] = '0';
            escaped[3] = '0';

            nCreateStringFromNumberWithCheck(code, 16d, hexNumber);

            if(hexNumber.string.length == 1d){
                escaped[4] = '0';
                escaped[5] = hexNumber.string[0];
            }else if(hexNumber.string.length == 2d){
                escaped[4] = hexNumber.string[0];
                escaped[5] = hexNumber.string[1];
            }
        }else{
            escaped = new char [1];
            escaped[0] = c;
        }

        return escaped;
    }

    public static boolean JSONMustBeEscaped(char c, NumberReference letters) {
        double code;
        boolean mustBeEscaped;
        double q, rs, s, b, f, n, r, t;

        code = c;
        mustBeEscaped = false;

        q = 34d;
        rs = 92d;
        s = 47d;
        b = 8d;
        f = 12d;
        n = 10d;
        r = 13d;
        t = 9d;

        if(code == q || code == rs || code == s || code == b || code == f || code == n || code == r || code == t){
            mustBeEscaped = true;
            letters.numberValue = 2d;
        }else if(code >= 0d && code <= 31d){
            mustBeEscaped = true;
            letters.numberValue = 6d;
        }else if(code >= pow(2d, 16d)){
            mustBeEscaped = true;
            letters.numberValue = 6d;
        }

        return mustBeEscaped;
    }

    public static void WriteObject(Data data, char[] result, NumberReference index) {
        char [] s, key, escapedKey;
        double i;
        StringReference [] keys;
        Data objectElement;

        strWriteStringToStingStream(result, index, "{".toCharArray());

        keys = GetStructKeys(data.structure);
        for(i = 0; i < keys.length; i = i + 1d){
            key = keys[(int)i].string;
            objectElement = GetDataFromStruct(data.structure, key);

            escapedKey = JSONEscapeString(key);
            strWriteStringToStingStream(result, index, "\"".toCharArray());
            strWriteStringToStingStream(result, index, escapedKey);
            strWriteStringToStingStream(result, index, "\"".toCharArray());
            strWriteStringToStingStream(result, index, ":".toCharArray());

            s = WriteJSON(objectElement);
            strWriteStringToStingStream(result, index, s);

            if(i + 1d != keys.length){
                strWriteStringToStingStream(result, index, ",".toCharArray());
            }
        }

        strWriteStringToStingStream(result, index, "}".toCharArray());
    }
}
