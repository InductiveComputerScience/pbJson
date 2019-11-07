package JSON.json;

import JSON.StringElementMaps.StringElementMap;
import JSON.ElementLists.ElementArrayReference;
import JSON.structures.Element;
import references.references.StringArrayReference;

import static JSON.StringElementMaps.StringElementMaps.GetStringElementMapKeySet;
import static JSON.StringElementMaps.StringElementMaps.GetObjectValue;
import static arrays.arrays.arrays.StringsEqual;
import static references.references.references.CreateStringArrayReferenceLengthValue;

public class json {
    public static Element CreateStringElement(char [] string){
        Element element;
        element = new Element();
        element.type = "string".toCharArray();
        element.string = string;
        return element;
    }

    public static Element CreateBooleanElement(boolean booleanValue){
        Element element;
        element = new Element();
        element.type = "boolean".toCharArray();
        element.booleanValue = booleanValue;
        return element;
    }

    public static Element CreateNullElement(){
        Element element;
        element = new Element();
        element.type = "null".toCharArray();
        return element;
    }

    public static Element CreateNumberElement(double number){
        Element element;
        element = new Element();
        element.type = "number".toCharArray();
        element.number = number;
        return element;
    }

    public static Element CreateArrayElement(double length){
        Element element;
        element = new Element();
        element.type = "array".toCharArray();
        element.array = new Element[(int)length];
        return element;
    }

    public static Element CreateObjectElement(double length){
        Element element;
        element = new Element();
        element.type = "object".toCharArray();
        element.object = new StringElementMap();
        element.object.stringListRef = CreateStringArrayReferenceLengthValue(length, "".toCharArray());
        element.object.elementListRef = new ElementArrayReference();
        element.object.elementListRef.array = new Element[(int)length];
        return element;
    }

    public static void DeleteElement(Element element) {
        if(StringsEqual(element.type, "object".toCharArray())){
            DeleteObject(element);
        }else if(StringsEqual(element.type, "string".toCharArray())){
            delete(element);
        }else if(StringsEqual(element.type, "array".toCharArray())){
            DeleteArray(element);
        }else if(StringsEqual(element.type, "number".toCharArray())){
            delete(element);
        }else if(StringsEqual(element.type, "null".toCharArray())){
            delete(element);
        }else if(StringsEqual(element.type, "boolean".toCharArray())){
            delete(element);
        }else{
        }
    }

    public static void delete(Object object) {
        // Java has garbage collection.
    }

    public static void DeleteObject(Element element) {
        StringArrayReference keys;
        double i;
        char [] key;
        Element objectElement;

        keys = GetStringElementMapKeySet(element.object);
        for(i = 0; i < keys.stringArray.length; i = i + 1d){
            key = keys.stringArray[(int)i].string;
            objectElement = GetObjectValue(element.object, key);
            DeleteElement(objectElement);
        }
    }

    public static void DeleteArray(Element element) {
        double i;
        Element arrayElement;

        for(i = 0; i < element.array.length; i = i + 1d){
            arrayElement = element.array[(int)i];
            DeleteElement(arrayElement);
        }
    }
}
