package testWriter;

import exampleMapper.Example;
import JSON.structures.Element;
import references.references.NumberReference;

import static JSON.StringElementMaps.StringElementMaps.PutStringElementMap;
import static JSON.ElementLists.ElementLists.AddElement;
import static JSON.json.json.*;
import static JSON.writer.writer.*;
import static exampleMapper.exampleMapper.mapTo;
import static references.references.references.CreateNumberReference;
import static testing.testing.testing.*;

public class testWriter {
    public static double testWriter(){
        char [] string;
        Element root;
        Example example;
        NumberReference failures;

        failures = CreateNumberReference(0d);

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

        return failures.numberValue;
    }

    public static Element createExampleJSON() {
        Element root, innerObject, array;

        root = CreateObjectElement();

        innerObject = CreateObjectElement();

        PutStringElementMap(innerObject.object, "x1".toCharArray(), CreateNullElement());
        PutStringElementMap(innerObject.object, "x2".toCharArray(), CreateBooleanElement(true));
        PutStringElementMap(innerObject.object, "x3".toCharArray(), CreateBooleanElement(false));

        array = CreateArrayElement();
        array.array = AddElement(array.array, CreateNumberElement(1.2));
        array.array = AddElement(array.array, CreateNumberElement(0.1));
        array.array = AddElement(array.array, CreateNumberElement(100d));

        PutStringElementMap(root.object, "a".toCharArray(), CreateStringElement("hei".toCharArray()));
        PutStringElementMap(root.object, "b".toCharArray(), array);
        PutStringElementMap(root.object, "x".toCharArray(), innerObject);

        return root;
    }


}
