package testEscaper;

import references.references.NumberReference;

import static JSON.writer.writer.JSONEscapeCharacter;
import static JSON.writer.writer.JSONMustBeEscaped;
import static references.references.references.CreateNumberReference;
import static testing.testing.testing.AssertEquals;
import static testing.testing.testing.AssertStringEquals;
import static testing.testing.testing.AssertTrue;

public class testEscaper {
    public static double testEscaper(){
        char c;
        NumberReference letters, failures;
        boolean mustBeEscaped;
        char [] escaped;

        failures = CreateNumberReference(0d);
        letters = CreateNumberReference(0d);

        c = (char)9d;
        mustBeEscaped = JSONMustBeEscaped(c, letters);
        AssertTrue(mustBeEscaped, failures);
        AssertEquals(letters.numberValue, 2d, failures);

        escaped = JSONEscapeCharacter(c);
        AssertStringEquals(escaped, "\\t".toCharArray(), failures);

        c = (char)0d;
        mustBeEscaped = JSONMustBeEscaped(c, letters);
        AssertTrue(mustBeEscaped, failures);
        AssertEquals(letters.numberValue, 6d, failures);

        escaped = JSONEscapeCharacter(c);
        AssertStringEquals(escaped, "\\u0000".toCharArray(), failures);

        return failures.numberValue;
    }
}
