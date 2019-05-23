package JSON.lengthComputer;

import JSON.structures.Element;
import references.references.StringArrayReference;

import static JSON.StringElementMaps.StringElementMaps.GetObjectValue;
import static JSON.StringElementMaps.StringElementMaps.GetStringElementMapKeySet;
import static JSON.elementTypeEnum.elementTypeEnum.ElementTypeEnumEquals;
import static JSON.writer.writer.JSONEscapedStringLength;
import static java.lang.Math.*;
import static nnumbers.NumberToString.NumberToString.nCreateStringDecimalFromNumber;
import static nnumbers.NumberToString.NumberToString.nCreateStringScientificNotationDecimalFromNumber;

public class lengthComputer {
    public static double ComputeJSONStringLength(Element element) {
        double result;

        result = 0d;

        if(ElementTypeEnumEquals(element.type.name, "object".toCharArray())){
            result = result + ComputeJSONObjectStringLength(element);
        }else if(ElementTypeEnumEquals(element.type.name, "string".toCharArray())){
            result = JSONEscapedStringLength(element.string) + 2d;
        }else if(ElementTypeEnumEquals(element.type.name, "array".toCharArray())){
            result = result + ComputeJSONArrayStringLength(element);
        }else if(ElementTypeEnumEquals(element.type.name, "number".toCharArray())){
            result = result + ComputerJSONNumberStringLength(element);
        }else if(ElementTypeEnumEquals(element.type.name, "nullValue".toCharArray())){
            result = result + "null".toCharArray().length;
        }else if(ElementTypeEnumEquals(element.type.name, "booleanValue".toCharArray())){
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

    public static double ComputerJSONNumberStringLength(Element element) {
        double length;

        if(abs(element.number) >= pow(10d, 15d) || abs(element.number) <= pow(10d, -15d)){
            length = nCreateStringScientificNotationDecimalFromNumber(element.number).length;
        }else{
            length = nCreateStringDecimalFromNumber(element.number).length;
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
