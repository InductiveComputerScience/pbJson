package testTokenizer;

import JSON.TokenLists.*;
import references.references.*;

import static JSON.tokenReader.tokenReader.*;
import static references.references.references.*;
import static strstrings.strings.strings.*;
import static testing.testing.testing.*;

public class testTokenizer {
    public static double testTokenizer1(){
        NumberReference failures, countReference, stringLength;
        StringArrayReference errorMessages;
        TokenArrayReference tokenArrayReference;
        boolean success;
        StringReference[] numbers;
        double i;

        failures = CreateNumberReference(0d);
        countReference = CreateNumberReference(0d);
        stringLength = CreateNumberReference(0d);
        errorMessages = CreateStringArrayReferenceLengthValue(0d, "".toCharArray());

        tokenArrayReference = new TokenArrayReference();

        success = JSONTokenize("false".toCharArray(), tokenArrayReference, errorMessages);
        AssertTrue(success, failures);
        if(success) {
            AssertEquals(tokenArrayReference.array.length, 2d, failures);
            AssertStringEquals(tokenArrayReference.array[0].type.name, "falseValue".toCharArray(), failures);
        }

        numbers = strSplitByString("11, -1e-1, -0.123456789e-99, 1E1, -0.1E23".toCharArray(), ", ".toCharArray());

        for(i = 0d; i < numbers.length; i = i + 1d){
            success = JSONTokenize(numbers[(int)i].string, tokenArrayReference, errorMessages);
            AssertTrue(success, failures);
            if(success) {
                AssertEquals(tokenArrayReference.array.length, 2d, failures);
                AssertStringEquals(tokenArrayReference.array[0].value, numbers[(int)i].string, failures);
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

        return failures.numberValue;
    }
}
