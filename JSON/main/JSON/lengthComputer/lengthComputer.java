package JSON.lengthComputer;

import JSON.structures.Element;
import references.references.StringArrayReference;

import static JSON.StringElementMaps.StringElementMaps.GetObjectValue;
import static JSON.StringElementMaps.StringElementMaps.GetStringElementMapKeySet;
import static JSON.writer.writer.JSONEscapedStringLength;
import static arrays.arrays.arrays.StringsEqual;
import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static nnumbers.NumberToString.NumberToString.nCreateStringDecimalFromNumber;
import static nnumbers.NumberToString.NumberToString.nCreateStringScientificNotationDecimalFromNumber;

public class lengthComputer {
    public static double ComputeJSONStringLength(Element element) {
        double result;

        result = 0d;

        if(StringsEqual(element.type, "object".toCharArray())){
            result = result + ComputeJSONObjectStringLength(element);
        }else if(StringsEqual(element.type, "string".toCharArray())){
            result = JSONEscapedStringLength(element.string) + 2d;
        }else if(StringsEqual(element.type, "array".toCharArray())){
            result = result + ComputeJSONArrayStringLength(element);
        }else if(StringsEqual(element.type, "number".toCharArray())){
            result = result + ComputeJSONNumberStringLength(element);
        }else if(StringsEqual(element.type, "null".toCharArray())){
            result = result + "null".toCharArray().length;
        }else if(StringsEqual(element.type, "boolean".toCharArray())){
            result = result + ComputeJSONBooleanStringLength(element);
        }

        return result;
    }

    public static double ComputeJSONBooleanStringLength(Element element) {
        double result;

        if(element.booleanValue){
            result = "true".toCharArray().length;
        }else{
            result = "false".toCharArray().length;
        }

        return result;
    }

    public static double ComputeJSONNumberStringLength(Element element) {
        double length;
        char[] a;

        if(element.number != 0d) {
            if(abs(element.number) >= pow(10d, 15d) || abs(element.number) <= pow(10d, -15d)){
                a = nCreateStringScientificNotationDecimalFromNumber(element.number);
                length = a.length;
            }else{
                a = nCreateStringDecimalFromNumber(element.number);
                length = a.length;
            }
        }else{
            a = nCreateStringDecimalFromNumber(element.number);
            length = a.length;
        }

        return length;
    }

    public static double ComputeJSONArrayStringLength(Element element) {
        Element arrayElement;
        double i;
        double length;

        length = 1d;

        for(i = 0d; i < element.array.length; i = i + 1d){
            arrayElement = element.array[(int)i];

            length = length + ComputeJSONStringLength(arrayElement);

            if(i + 1d != element.array.length){
                length = length + 1d;
            }
        }

        length = length + 1d;

        return length;
    }

    public static double ComputeJSONObjectStringLength(Element element) {
        char [] key;
        double i;
        StringArrayReference keys;
        Element objectElement;
        double length;

        length = 1d;

        keys = GetStringElementMapKeySet(element.object);
        for(i = 0; i < keys.stringArray.length; i = i + 1d){
            key = keys.stringArray[(int)i].string;
            objectElement = GetObjectValue(element.object, key);

            length = length + 1d;
            length = length + JSONEscapedStringLength(key);
            length = length + 1d;
            length = length + 1d;

            length = length + ComputeJSONStringLength(objectElement);

            if(i + 1d != keys.stringArray.length){
                length = length + 1d;
            }
        }

        length = length + 1d;

        return length;
    }
}
