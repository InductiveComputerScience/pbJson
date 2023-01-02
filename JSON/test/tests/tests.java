package tests;

import references.references.BooleanReference;
import references.references.NumberReference;
import references.references.StringArrayReference;

import static JSON.Comparator.Comparator.JSONCompare;
import static JSON.Validator.Validator.IsValidJSON;
import static references.references.references.CreateBooleanReference;
import static references.references.references.CreateNumberReference;
import static references.references.references.CreateStringArrayReferenceLengthValue;
import static testEscaper.testEscaper.testEscaper;
import static testReader.testReader.*;
import static testTokenizer.testTokenizer.*;
import static testWriter.testWriter.*;
import static testing.testing.testing.AssertFalse;
import static testing.testing.testing.AssertTrue;

public class tests {
    public static double test(){
        NumberReference failures;

        failures = CreateNumberReference(0d);

        testReader(failures);
        test2(failures);
        testWriter(failures);
        testWriterEscape(failures);
        testTokenizer1(failures);
        testReaderExample(failures);
        testEscaper(failures);
        testValidator(failures);
        testComparator(failures);
        testWriter2(failures);
        testWriter3(failures);

        return failures.numberValue;
    }

    public static void testValidator(NumberReference failures) {
        char [] a, b, c, d;
        StringArrayReference errorMessages;

        errorMessages = CreateStringArrayReferenceLengthValue(0d, "".toCharArray());

        a = "{\"a\":\"hi\",\"b\":[1.2, 0.1, 100],\"x\":{\"x1\":null,\"x2\":true,\"x3\":false}}".toCharArray();
        b = "{{}}]".toCharArray();
        c = "".toCharArray();
        d = "{".toCharArray();

        AssertTrue(IsValidJSON(a, errorMessages), failures);
        AssertFalse(IsValidJSON(b, errorMessages), failures);
        AssertFalse(IsValidJSON(c, errorMessages), failures);
        AssertFalse(IsValidJSON(d, errorMessages), failures);
    }

    public static void testComparator(NumberReference failures) {
        char [] a, b;
        StringArrayReference errorMessages;
        BooleanReference equalsReference;
        boolean success;

        errorMessages = CreateStringArrayReferenceLengthValue(0d, "".toCharArray());
        equalsReference = CreateBooleanReference(false);

        a = "{\"a\":\"hi\",\"b\":[1.2, 0.1, 100],\"x\":{\"x1\":null,\"x2\":true,\"x3\":false}}".toCharArray();
        b = "{\"x\":{\"x1\":null,\"x2\":true,\"x3\":false},\"a\":\"hi\",\"b\":[1.2, 0.1, 100]}".toCharArray();

        success = JSONCompare(a, b, 0.0001, equalsReference, errorMessages);

        AssertTrue(success, failures);
        AssertTrue(equalsReference.booleanValue, failures);

        a = "{\"a\":\"hi\",\"b\":[1.201, 0.1, 100],\"x\":{\"x1\":null,\"x2\":true,\"x3\":false}}".toCharArray();
        b = "{\"x\":{\"x1\":null,\"x2\":true,\"x3\":false},\"a\":\"hi\",\"b\":[1.2, 0.1, 100]}".toCharArray();

        success = JSONCompare(a, b, 0.0001, equalsReference, errorMessages);

        AssertTrue(success, failures);
        AssertFalse(equalsReference.booleanValue, failures);

        success = JSONCompare(a, b, 0.1, equalsReference, errorMessages);

        AssertTrue(success, failures);
        AssertTrue(equalsReference.booleanValue, failures);
    }
}
