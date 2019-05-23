package JSON.comparator;

import JSON.StringElementMaps.*;
import JSON.structures.*;
import references.references.*;

import static JSON.StringElementMaps.StringElementMaps.*;
import static JSON.elementTypeEnum.elementTypeEnum.*;
import static JSON.json.json.*;
import static JSON.parser.parser.*;
import static arrays.arrays.arrays.*;
import static math.math.math.*;

public class comparator {
    public static boolean JSONCompare(char [] a, char [] b, double epsilon, BooleanReference equal, StringArrayReference errorMessage){
        boolean success;
        ElementReference eaRef, ebRef;
        Element ea, eb;

        eaRef = new ElementReference();
        ebRef = new ElementReference();

        success = ReadJSON(a, eaRef, errorMessage);

        if(success) {
            ea = eaRef.element;

            success = ReadJSON(b, ebRef, errorMessage);

            if(success){
                eb = ebRef.element;

                equal.booleanValue = JSONCompareElements(ea, eb, epsilon);

                DeleteElement(eb);
            }

            DeleteElement(ea);
        }

        return success;
    }

    public static boolean JSONCompareElements(Element ea, Element eb, double epsilon){
        boolean equal;
        char[] typeName;

        equal = StringsEqual(ea.type.name, eb.type.name);
        
        if(equal){
            typeName = ea.type.name;
            if(ElementTypeEnumEquals(typeName, "object".toCharArray())){
                equal = JSONCompareObjects(ea.object, eb.object, epsilon);
            }else if(ElementTypeEnumEquals(typeName, "string".toCharArray())){
                equal = StringsEqual(ea.string, eb.string);
            }else if(ElementTypeEnumEquals(typeName, "array".toCharArray())){
                equal = JSONCompareArrays(ea.array, eb.array, epsilon);
            }else if(ElementTypeEnumEquals(typeName, "number".toCharArray())){
                equal = EpsilonCompare(ea.number, eb.number, epsilon);
            }else if(ElementTypeEnumEquals(typeName, "nullValue".toCharArray())){
                equal = true;
            }else if(ElementTypeEnumEquals(typeName, "booleanValue".toCharArray())){
                equal = ea.booleanValue == eb.booleanValue;
            }
        }
        
        return equal;
    }

    public static boolean JSONCompareArrays(Element[] ea, Element[] eb, double epsilon) {
        boolean equals;
        double i, length;

        equals = ea.length == eb.length;

        if(equals){
            length = ea.length;
            for(i = 0d; i < length && equals; i = i + 1d){
                equals = JSONCompareElements(ea[(int)i], eb[(int)i], epsilon);
            }
        }

        return equals;
    }

    public static boolean JSONCompareObjects(StringElementMap ea, StringElementMap eb, double epsilon) {
        boolean equals;
        double akeys, bkeys, i;
        StringArrayReference keys;
        char [] key;
        BooleanReference aFoundReference, bFoundReference;
        Element eaValue, ebValue;

        aFoundReference = new BooleanReference();
        bFoundReference = new BooleanReference();

        akeys = GetStringElementMapNumberOfKeys(ea);
        bkeys = GetStringElementMapNumberOfKeys(eb);

        equals = akeys == bkeys;

        if(equals){
            keys = GetStringElementMapKeySet(ea);

            for(i = 0d; i < keys.stringArray.length && equals; i = i + 1d){
                key = keys.stringArray[(int)i].string;

                eaValue = GetObjectValueWithCheck(ea, key, aFoundReference);
                ebValue = GetObjectValueWithCheck(eb, key, bFoundReference);

                if(aFoundReference.booleanValue && bFoundReference.booleanValue){
                    equals = JSONCompareElements(eaValue, ebValue, epsilon);
                }else{
                    equals = false;
                }
            }
        }

        return equals;
    }
}
