package tests;

import references.references.BooleanReference;
import references.references.NumberReference;
import references.references.StringArrayReference;

import static JSON.comparator.comparator.JSONCompare;
import static JSON.validator.validator.IsValidJSON;
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
        double failures;

        failures = 0d;

        failures = failures + testReader();
        failures = failures + test2();
        failures = failures + testWriter();
        failures = failures + testWriterEscape();
        failures = failures + testTokenizer1();
        failures = failures + testReaderExample();
        failures = failures + testEscaper();
        failures = failures + testValidator();
        failures = failures + testComparator();

        return failures;
    }

    public static double testValidator() {
        char [] a, b;
        NumberReference failures;
        StringArrayReference errorMessages;

        failures = CreateNumberReference(0d);
        errorMessages = CreateStringArrayReferenceLengthValue(0d, "".toCharArray());

        a = "{\"a\":\"hi\",\"b\":[1.2, 0.1, 100],\"x\":{\"x1\":null,\"x2\":true,\"x3\":false}}".toCharArray();
        b = "{{}}]".toCharArray();

        AssertTrue(IsValidJSON(a, errorMessages), failures);
        AssertFalse(IsValidJSON(b, errorMessages), failures);

        return failures.numberValue;
    }

    public static double testComparator() {
        char [] a, b;
        NumberReference failures;
        StringArrayReference errorMessages;
        BooleanReference equalsReference;
        boolean success;

        failures = CreateNumberReference(0d);
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

        return failures.numberValue;
    }
}
