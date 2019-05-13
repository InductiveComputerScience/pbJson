package JSON.writer;

import JSON.structures.Element;
import references.references.StringArrayReference;

import static JSON.StringElementMaps.StringElementMaps.GetStringElementMapKeySet;
import static JSON.StringElementMaps.StringElementMaps.GetObjectValue;
import static JSON.elementTypeEnum.elementTypeEnum.ElementTypeEnumEquals;
import static java.lang.Math.*;
import static nnumbers.NumberToString.NumberToString.*;
import static strstrings.strings.strings.strAppendString;
import static strstrings.strings.strings.strConcatenateString;

public class writer {
    public static char[] WriteJSON(Element element) {
        char[] result;

        if(ElementTypeEnumEquals(element.type.name, "object".toCharArray())){
            result = WriteObject(element);
        }else if(ElementTypeEnumEquals(element.type.name, "string".toCharArray())){
            result = WriteString(element);
        }else if(ElementTypeEnumEquals(element.type.name, "array".toCharArray())){
            result = WriteArray(element);
        }else if(ElementTypeEnumEquals(element.type.name, "number".toCharArray())){
            result = WriteNumber(element);
        }else if(ElementTypeEnumEquals(element.type.name, "nullValue".toCharArray())){
            result = "null".toCharArray();
        }else if(ElementTypeEnumEquals(element.type.name, "booleanValue".toCharArray())){
            result = WriteBooleanValue(element);
        }else{
            result = "?".toCharArray();
        }

        return result;
    }

    public static char[] WriteBooleanValue(Element element) {
        char [] result;

        if(element.booleanValue){
            result = "true".toCharArray();
        }else{
            result = "false".toCharArray();
        }

        return result;
    }

    public static char[] WriteNumber(Element element) {
        char[] numberString;

        if(abs(element.number) >= pow(10d, 15d) || abs(element.number) <= pow(10d, -15d)){
            numberString = nCreateStringScientificNotationDecimalFromNumber(element.number);
        }else{
            numberString = nCreateStringDecimalFromNumber(element.number);
        }

        return numberString;
    }

    public static char[] WriteArray(Element element) {
        char [] string, s;
        Element arrayElement;
        double i;

        string = "".toCharArray();
        string = strConcatenateString(string, "[".toCharArray());

        for(i = 0; i < element.array.length; i = i + 1d){
            arrayElement = element.array[(int)i];

            s = WriteJSON(arrayElement);
            string = strAppendString(string, s);

            if(i + 1d != element.array.length){
                string = strAppendString(string, ",".toCharArray());
            }
        }

        string = strAppendString(string, "]".toCharArray());

        return string;
    }

    public static char[] WriteString(Element element) {
        char [] string;

        string = "".toCharArray();
        string = strConcatenateString(string, "\"".toCharArray());
        string = strAppendString(string, element.string);
        string = strAppendString(string, "\"".toCharArray());

        return string;
    }

    public static char[] WriteObject(Element element) {
        char [] string, s, key;
        double i;
        StringArrayReference keys;
        Element objectElement;

        string = "".toCharArray();
        string = strConcatenateString(string, "{".toCharArray());

        keys = GetStringElementMapKeySet(element.object);
        for(i = 0; i < keys.stringArray.length; i = i + 1d){
            key = keys.stringArray[(int)i].string;
            objectElement = GetObjectValue(element.object, key);

            string = strAppendString(string, "\"".toCharArray());
            string = strAppendString(string, key);
            string = strAppendString(string, "\"".toCharArray());
            string = strAppendString(string, ":".toCharArray());

            s = WriteJSON(objectElement);
            string = strAppendString(string, s);

            if(i + 1d != keys.stringArray.length){
                string = strAppendString(string, ",".toCharArray());
            }
        }

        string = strAppendString(string, "}".toCharArray());

        return string;
    }
}
