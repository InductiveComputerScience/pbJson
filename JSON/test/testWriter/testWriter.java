package testWriter;

import DataStructures.Array.Structures.*;
import exampleMapper.*;
import references.references.*;

import static DataStructures.Array.Arrays.Arrays.*;
import static DataStructures.Array.Structures.Structures.*;
import static JSON.Parser.Parser.*;
import static JSON.Writer.Writer.*;
import static exampleMapper.exampleMapper.*;
import static testing.testing.testing.*;

public class testWriter {
    public static void testWriter(NumberReference failures){
        char [] string;
        Data root, e;
        Example example;
        char [] result;
        NumberReference numberIndex;

        root = createExampleJSON();

        string = WriteJSON(root);

        AssertEquals(string.length, 66d, failures);

        // Does not work with Java Maps..
        example = mapTo(root);

        AssertStringEquals("hei".toCharArray(), example.a, failures);
        AssertTrue(example.x.x1IsNull, failures);
        AssertTrue(example.x.x2, failures);
        AssertFalse(example.x.x3, failures);
        AssertEquals(1.2, example.b[0], failures);
        AssertEquals(0.1, example.b[1], failures);
        AssertEquals(100d, example.b[2], failures);

        FreeData(root);

        e = CreateNumberData(0d);
        result = new char [1];
        numberIndex = new NumberReference();
        WriteNumber(e, result, numberIndex);

        AssertStringEquals("0".toCharArray(), result, failures);
    }

    public static Data createExampleJSON() {
        Data root;
        Structure innerObject;
        Array array;

        root = CreateStructData();

        innerObject = CreateStructure();

        AddDataToStruct(innerObject, "x1".toCharArray(), CreateNoTypeData());
        AddBooleanToStruct(innerObject, "x2".toCharArray(),true);
        AddBooleanToStruct(innerObject, "x3".toCharArray(), false);

        array = CreateArray();
        AddNumberToArray(array, 1.2);
        AddNumberToArray(array, 0.1);
        AddNumberToArray(array, 100d);

        AddStringToStruct(root.structure, "a".toCharArray(), "hei".toCharArray());
        AddArrayToStruct(root.structure, "b".toCharArray(), array);
        AddStructToStruct(root.structure, "x".toCharArray(), innerObject);

        return root;
    }

    public static void testWriterEscape(NumberReference failures){
        char [] string;
        Data root;

        root = CreateStringData("\t\n".toCharArray());

        string = WriteJSON(root);

        AssertEquals(string.length, 6d, failures);

        AssertStringEquals("\"\\t\\n\"".toCharArray(), string, failures);

        FreeData(root);
    }

    public static void testWriter2(NumberReference failures){
        Data e;
        char [] result, json;
        DataReference obj;
        StringArrayReference errorMessages;
        boolean success;

        obj = new DataReference();
        errorMessages = new StringArrayReference();
        json = "{\"test1\":0,\"test2\":0,\"test3\":0}".toCharArray();
        success = ReadJSON(json, obj, errorMessages);

        AssertTrue(success, failures);

        if(success){
            e = obj.data;

            result = WriteJSON(e);

            AssertStringEquals(result, json, failures);
        }
    }

    public static void testWriter3(NumberReference failures){
        Data e;
        char [] result;

        e = CreateStructData();
        AddStringToStruct(e.structure, "example.com/style.js".toCharArray(), "console.log(\"hello world\")".toCharArray());

        result = WriteJSON(e);

        AssertStringEquals(result, "{\"example.com\\/style.js\":\"console.log(\\\"hello world\\\")\"}".toCharArray(), failures);
    }
}
