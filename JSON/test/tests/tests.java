package tests;

import static testReader.testReader.*;
import static testTokenizer.testTokenizer.*;
import static testWriter.testWriter.*;

public class tests {
    public static double test(){
        double failures;

        failures = 0d;

        failures = failures + testReader();
        failures = failures + test2();
        failures = failures + testWriter();
        failures = failures + testTokenizer1();
        failures = failures + testReaderExample();

        return failures;
    }
}
