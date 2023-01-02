package testTokenizer;

import references.references.*;

import static JSON.TokenReader.TokenReader.*;
import static references.references.references.*;
import static strstrings.strings.strings.*;
import static testing.testing.testing.*;

public class testTokenizer {
    public static void testTokenizer1(NumberReference failures){
        NumberReference countReference, stringLength;
        StringArrayReference errorMessages, tokenArrayReference;
        boolean success;
        StringReference[] numbers;
        double i;

        countReference = CreateNumberReference(0d);
        stringLength = CreateNumberReference(0d);
        errorMessages = CreateStringArrayReferenceLengthValue(0d, "".toCharArray());

        tokenArrayReference = new StringArrayReference();

        success = JSONTokenize("false".toCharArray(), tokenArrayReference, errorMessages);
        AssertTrue(success, failures);
        if(success) {
            AssertEquals(tokenArrayReference.stringArray.length, 2d, failures);
            AssertStringEquals(tokenArrayReference.stringArray[0].string, "false".toCharArray(), failures);
        }

        numbers = strSplitByString("11, -1e-1, -0.123456789e-99, 1E1, -0.1E23".toCharArray(), ", ".toCharArray());

        for(i = 0d; i < numbers.length; i = i + 1d){
            success = JSONTokenize(numbers[(int)i].string, tokenArrayReference, errorMessages);
            AssertTrue(success, failures);
            if(success) {
                AssertEquals(tokenArrayReference.stringArray.length, 2d, failures);
                AssertStringEquals(tokenArrayReference.stringArray[0].string, numbers[(int)i].string, failures);
            }
        }

        success = IsValidJSONStringInJSON("\"\"".toCharArray(), 0d, countReference, stringLength, errorMessages);
        AssertTrue(success, failures);
        if(success) {
            AssertEquals(countReference.numberValue, 2d, failures);
        }

        success = IsValidJSONString("\"\\u1234\\n\\r\\f\\b\\t\"".toCharArray(), errorMessages);
        AssertTrue(success, failures);

        success = JSONTokenize("\"".toCharArray(), tokenArrayReference, errorMessages);
        AssertFalse(success, failures);

        success = IsValidJSONNumber("1.1-e1".toCharArray(), errorMessages);
        AssertFalse(success, failures);

        success = IsValidJSONNumber("1E+2".toCharArray(), errorMessages);
        AssertTrue(success, failures);

        success = IsValidJSONString("\"\\uAAAG\"".toCharArray(), errorMessages);
        AssertFalse(success, failures);
    }
}
