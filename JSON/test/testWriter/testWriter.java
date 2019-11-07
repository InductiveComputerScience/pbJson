package testWriter;

import JSON.structures.Element;
import exampleMapper.Example;
import references.references.NumberReference;

import static JSON.StringElementMaps.StringElementMaps.SetStringElementMap;
import static JSON.json.json.*;
import static JSON.writer.writer.WriteJSON;
import static exampleMapper.exampleMapper.mapTo;
import static testing.testing.testing.*;

public class testWriter {
    public static void testWriter(NumberReference failures){
        char [] string;
        Element root;
        Example example;

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

        DeleteElement(root);
    }

    public static Element createExampleJSON() {
        Element root, innerObject, array;

        root = CreateObjectElement(3d);

        innerObject = CreateObjectElement(3d);

        SetStringElementMap(innerObject.object, 0d, "x1".toCharArray(), CreateNullElement());
        SetStringElementMap(innerObject.object, 1d, "x2".toCharArray(), CreateBooleanElement(true));
        SetStringElementMap(innerObject.object, 2d, "x3".toCharArray(), CreateBooleanElement(false));

        array = CreateArrayElement(3d);
        array.array[0] = CreateNumberElement(1.2);
        array.array[1] = CreateNumberElement(0.1);
        array.array[2] = CreateNumberElement(100d);

        SetStringElementMap(root.object, 0d, "a".toCharArray(), CreateStringElement("hei".toCharArray()));
        SetStringElementMap(root.object, 1d, "b".toCharArray(), array);
        SetStringElementMap(root.object, 2d, "x".toCharArray(), innerObject);

        return root;
    }

    public static void testWriterEscape(NumberReference failures){
        char [] string;
        Element root;

        root = CreateStringElement("\t\n".toCharArray());

        string = WriteJSON(root);

        AssertEquals(string.length, 6d, failures);

        AssertStringEquals("\"\\t\\n\"".toCharArray(), string, failures);

        DeleteElement(root);
    }
}
