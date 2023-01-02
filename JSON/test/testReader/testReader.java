package testReader;

import DataStructures.Array.Structures.Array;
import DataStructures.Array.Structures.Data;
import DataStructures.Array.Structures.DataReference;
import references.references.*;

import static DataStructures.Array.Arrays.Arrays.ArrayIndex;
import static DataStructures.Array.Structures.Structures.GetDataFromStruct;
import static references.references.references.CreateStringArrayReferenceLengthValue;
import static references.references.references.CreateStringReference;
import static strstrings.strings.strings.*;
import static testWriter.testWriter.*;
import static JSON.Writer.Writer.*;
import static JSON.Parser.Parser.*;
import static testing.testing.testing.AssertEquals;
import static testing.testing.testing.AssertTrue;

public class testReader {
    public static void testReader(NumberReference failures){
        Data json;
        char [] string, string2;
        StringArrayReference errorMessages;
        DataReference elementReference;
        boolean success;

        json = createExampleJSON();
        string = WriteJSON(json);
        elementReference = new DataReference();

        errorMessages = CreateStringArrayReferenceLengthValue(0d, "".toCharArray());

        success = ReadJSON(string, elementReference, errorMessages);
        AssertTrue(success, failures);

        if(success) {
            json = elementReference.data;
            string2 = WriteJSON(json);

            AssertEquals(string.length, string2.length, failures);
        }
    }

    public static void test2(NumberReference failures){
        char [] string, string2;
        StringArrayReference errorMessages;
        Data json;
        DataReference elementReference;
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
        elementReference = new DataReference();
        success = ReadJSON(string, elementReference, errorMessages);
        AssertTrue(success, failures);

        if(success) {
            json = elementReference.data;

            string2 = WriteJSON(json);

            AssertEquals(string.length, string2.length, failures);
        }
    }

    public static void testReaderExample(NumberReference failures){
        char [] json;
        StringArrayReference errorMessages;
        DataReference elementReference;
        StringReference outputJSON;

        errorMessages = CreateStringArrayReferenceLengthValue(0d, "".toCharArray());
        elementReference = new DataReference();
        outputJSON = CreateStringReference("".toCharArray());

        json = "{\"a\":\"hi\",\"b\":[1.2, 0.1, 100],\"x\":{\"x1\":null,\"x2\":true,\"x3\":false}}".toCharArray();

        JSONExample(json, errorMessages, elementReference, outputJSON);
    }

    public static void JSONExample(char[] json, StringArrayReference errorMessages, DataReference elementReference, StringReference outputJSON) {
        boolean success;
        Data element, aElement;
        char [] string;
        Array array;
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
            element = elementReference.data;

            // The following is gets the value "hi" for key "a":
            aElement = GetDataFromStruct(element.structure, "a".toCharArray());
            string = aElement.string;

            // The following is gets the array [1.2, 0.1, 100] for key "b":
            aElement = GetDataFromStruct(element.structure, "b".toCharArray());
            array = aElement.array;
            x = ArrayIndex(array, 0d).number;
            y = ArrayIndex(array, 1d).number;
            z = ArrayIndex(array, 2d).number;

            // This is how you write JSON:
            outputJSON.string = WriteJSON(element);

        }else{
            // There was a problem, so we cannot read our data structure.
            // Instead, we can check out the error message.
            string = errorMessages.stringArray[0].string;
        }
    }
}
