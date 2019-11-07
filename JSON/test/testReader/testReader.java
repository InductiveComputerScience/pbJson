package testReader;

import JSON.structures.Element;
import JSON.structures.ElementReference;
import references.references.NumberReference;
import references.references.StringArrayReference;
import references.references.StringReference;

import static JSON.StringElementMaps.StringElementMaps.GetObjectValue;
import static JSON.parser.parser.ReadJSON;
import static JSON.writer.writer.WriteJSON;
import static references.references.references.CreateStringArrayReferenceLengthValue;
import static references.references.references.CreateStringReference;
import static strstrings.strings.strings.strAppendString;
import static strstrings.strings.strings.strConcatenateString;
import static testWriter.testWriter.createExampleJSON;
import static testing.testing.testing.AssertEquals;
import static testing.testing.testing.AssertTrue;

public class testReader {
    public static void testReader(NumberReference failures){
        Element json;
        char [] string, string2;
        StringArrayReference errorMessages;
        ElementReference elementReference;
        boolean success;

        json = createExampleJSON();
        string = WriteJSON(json);
        elementReference = new ElementReference();

        errorMessages = CreateStringArrayReferenceLengthValue(0d, "".toCharArray());

        success = ReadJSON(string, elementReference, errorMessages);
        AssertTrue(success, failures);

        if(success) {
            json = elementReference.element;
            string2 = WriteJSON(json);

            AssertEquals(string.length, string2.length, failures);
        }
    }

    public static void test2(NumberReference failures){
        char [] string, string2;
        StringArrayReference errorMessages;
        Element json;
        ElementReference elementReference;
        boolean success;

        string = strConcatenateString("{".toCharArray(), "\"name\":\"base64\",".toCharArray());
        string = strAppendString(string, "\"version\":\"0.1.0\",".toCharArray());
        string = strAppendString(string, "\"business namespace\":\"no.inductive.idea10.programs\",".toCharArray());
        string = strAppendString(string, "\"scientific namespace\":\"computerscience.algorithms.base64\",".toCharArray());
        string = strAppendString(string, "\"imports\":[".toCharArray());
        string = strAppendString(string, "]," .toCharArray());
        string = strAppendString(string, "\"imports2\":{" .toCharArray());
        string = strAppendString(string, "}," .toCharArray());
        string = strAppendString(string, "\"development imports\":[" .toCharArray());
        string = strAppendString(string, "[\"\",\"no.inductive.idea10.programs\",\"arrays\",\"0.1.0\"]" .toCharArray());
        string = strAppendString(string, "]" .toCharArray());
        string = strAppendString(string, "}" .toCharArray());

        errorMessages = CreateStringArrayReferenceLengthValue(0d, "".toCharArray());
        elementReference = new ElementReference();
        success = ReadJSON(string, elementReference, errorMessages);
        AssertTrue(success, failures);

        if(success) {
            json = elementReference.element;

            string2 = WriteJSON(json);

            AssertEquals(string.length, string2.length, failures);
        }
    }

    public static void testReaderExample(NumberReference failures){
        char [] json;
        StringArrayReference errorMessages;
        ElementReference elementReference;
        StringReference outputJSON;

        errorMessages = CreateStringArrayReferenceLengthValue(0d, "".toCharArray());
        elementReference = new ElementReference();
        outputJSON = CreateStringReference("".toCharArray());

        json = "{\"a\":\"hi\",\"b\":[1.2, 0.1, 100],\"x\":{\"x1\":null,\"x2\":true,\"x3\":false}}".toCharArray();

        JSONExample(json, errorMessages, elementReference, outputJSON);
    }

    public static void JSONExample(char[] json, StringArrayReference errorMessages, ElementReference elementReference, StringReference outputJSON) {
        boolean success;
        Element element, aElement;
        char [] string;
        Element array [];
        double x, y, z;

        /* The following JSON is in the string json:
           {
             "a": "hi",
             "b": [1.2, 0.1, 100],
             "x": {
               "x1": null,
               "x2": true,
               "x3": false
             }
           }
        */

        // This functions reads the JSON
        success = ReadJSON(json, elementReference, errorMessages);

        // The return value 'success' is set to true of the parsing succeeds,
        // if not, errorMessages contains the reason.
        if(success){
            // We can now extract the data structure:
            element = elementReference.element;

            // The following is gets the value "hi" for key "a":
            aElement = GetObjectValue(element.object, "a".toCharArray());
            string = aElement.string;

            // The following is gets the array [1.2, 0.1, 100] for key "b":
            aElement = GetObjectValue(element.object, "b".toCharArray());
            array = aElement.array;
            x = array[0].number;
            y = array[1].number;
            z = array[2].number;

            // This is how you write JSON:
            outputJSON.string = WriteJSON(element);

        }else{
            // There was a problem, so we cannot read our data structure.
            // Instead, we can check out the error message.
            string = errorMessages.stringArray[0].string;
        }
    }
}
