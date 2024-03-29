package JSON.TokenReader;

import lists.LinkedListStrings.Structures.*;
import references.references.*;

import static arrays.arrays.arrays.*;
import static charCharacters.Characters.Characters.*;
import static lists.LinkedListStrings.LinkedListStringsFunctions.LinkedListStringsFunctions.*;
import static lists.StringList.StringList.*;
import static nnumbers.StringToNumber.StringToNumber.*;
import static references.references.references.*;
import static strstrings.strings.strings.*;

public class TokenReader {
    public static boolean JSONTokenize(char [] json, StringArrayReference tokensReference, StringArrayReference errorMessages) {
        double i;
        char c;
        char[] str;
        StringReference stringReference, tokenReference;
        NumberReference stringLength;
        boolean success;
        LinkedListStrings ll;

        ll = CreateLinkedListString();
        success = true;

        stringLength = new NumberReference();
        tokenReference = new StringReference();

        for(i = 0; i < json.length && success;) {
            c = json[(int)i];

            if (c == '{') {
                LinkedListAddString(ll, "{".toCharArray());
                i = i + 1d;
            }else if (c == '}') {
                LinkedListAddString(ll, "}".toCharArray());
                i = i + 1d;
            }else if (c == '[') {
                LinkedListAddString(ll, "[".toCharArray());
                i = i + 1d;
            }else if (c == ']') {
                LinkedListAddString(ll, "]".toCharArray());
                i = i + 1d;
            }else if (c == ':') {
                LinkedListAddString(ll, ":".toCharArray());
                i = i + 1d;
            }else if (c == ',') {
                LinkedListAddString(ll, ",".toCharArray());
                i = i + 1d;
            }else if (c == 'f') {
                success = GetJSONPrimitiveName(json, i, errorMessages, "false".toCharArray(), tokenReference);
                if(success) {
                    LinkedListAddString(ll, "false".toCharArray());
                    i = i + "false".toCharArray().length;
                }
            }else if (c == 't') {
                success = GetJSONPrimitiveName(json, i, errorMessages, "true".toCharArray(), tokenReference);
                if(success) {
                    LinkedListAddString(ll, "true".toCharArray());
                    i = i + "true".toCharArray().length;
                }
            }else if (c == 'n') {
                success = GetJSONPrimitiveName(json, i, errorMessages, "null".toCharArray(), tokenReference);
                if(success) {
                    LinkedListAddString(ll, "null".toCharArray());
                    i = i + "null".toCharArray().length;
                }
            }else if (c == ' ' || c == '\n' || c == '\t' || c == '\r') {
                // Skip.
                i = i + 1d;
            }else if (c == '\"'){
                success = GetJSONString(json, i, tokenReference, stringLength, errorMessages);
                if(success) {
                    LinkedListAddString(ll, tokenReference.string);
                    i = i + stringLength.numberValue;
                }
            }else if (IsJSONNumberCharacter(c)){
                success = GetJSONNumberToken(json, i, tokenReference, errorMessages);
                if(success) {
                    LinkedListAddString(ll, tokenReference.string);
                    i = i + tokenReference.string.length;
                }
            }else{
                str = strConcatenateCharacter("Invalid start of Token: ".toCharArray(), c);
                stringReference = CreateStringReference(str);
                AddStringRef(errorMessages, stringReference);
                i = i + 1d;
                success = false;
            }
        }

        if(success) {
            LinkedListAddString(ll, "<end>".toCharArray());
            tokensReference.stringArray = LinkedListStringsToArray(ll);
            FreeLinkedListString(ll);
        }

        return success;
    }

    public static boolean GetJSONNumberToken(char[] json, double start, StringReference tokenReference, StringArrayReference errorMessages) {
        char c;
        double end, i;
        boolean done, success;
        char [] numberString;

        end = json.length;
        done = false;

        for(i = start; i < json.length && !done; i = i + 1d) {
            c = json[(int)i];
            if(!IsJSONNumberCharacter(c)){
                done = true;
                end = i;
            }
        }

        numberString = strSubstring(json, start, end);

        success = IsValidJSONNumber(numberString, errorMessages);

        tokenReference.string = numberString;

        return success;
    }

    public static boolean IsValidJSONNumber(char [] n, StringArrayReference errorMessages) {
        boolean success;
        double i;

        i = 0d;

        // JSON allows an optional negative sign.
        if(n[(int)i] == '-'){
            i = i + 1d;
        }

        if(i < n.length){
            success = IsValidJSONNumberAfterSign(n, i, errorMessages);
        }else{
            success = false;
            AddStringRef(errorMessages, CreateStringReference("Number must contain at least one digit.".toCharArray()));
        }

        return success;
    }

    public static boolean IsValidJSONNumberAfterSign(char[] n, double i, StringArrayReference errorMessages) {
        boolean success;

        if(charIsNumber(n[(int)i])){
            // 0 first means only 0.
            if(n[(int)i] == '0'){
                i = i + 1d;
            }else{
                // 1-9 first, read following digits.
                i = IsValidJSONNumberAdvancePastDigits(n, i);
            }

            if(i < n.length) {
                success = IsValidJSONNumberFromDotOrExponent(n, i, errorMessages);
            }else{
                // If integer, we are done now.
                success = true;
            }
        }else{
            success = false;
            AddStringRef(errorMessages, CreateStringReference("A number must start with 0-9 (after the optional sign).".toCharArray()));
        }

        return success;
    }

    public static double IsValidJSONNumberAdvancePastDigits(char[] n, double i) {
        boolean done;

        i = i + 1d;
        done = false;
        for (; i < n.length && !done; ) {
            if (charIsNumber(n[(int) i])) {
                i = i + 1d;
            } else {
                done = true;
            }
        }

        return i;
    }

    public static boolean IsValidJSONNumberFromDotOrExponent(char[] n, double i, StringArrayReference errorMessages){
        boolean wasDotAndOrE, success;

        wasDotAndOrE = false;
        success = true;

        if (n[(int) i] == '.') {
            i = i + 1d;
            wasDotAndOrE = true;

            if(i < n.length) {
                if (charIsNumber(n[(int) i])) {
                    // Read digits following decimal point.
                    i = IsValidJSONNumberAdvancePastDigits(n, i);

                    if(i == n.length){
                        // If non-scientific decimal number, we are done.
                        success = true;
                    }
                } else {
                    success = false;
                    AddStringRef(errorMessages, CreateStringReference("There must be numbers after the decimal point.".toCharArray()));
                }
            }else{
                success = false;
                AddStringRef(errorMessages, CreateStringReference("There must be numbers after the decimal point.".toCharArray()));
            }
        }

        if(i < n.length && success) {
            if (n[(int) i] == 'e' || n[(int) i] == 'E') {
                wasDotAndOrE = true;
                success = IsValidJSONNumberFromExponent(n, i, errorMessages);
            }else{
                success = false;
                AddStringRef(errorMessages, CreateStringReference("Expected e or E.".toCharArray()));
            }
        }else if(i == n.length && success){
            // If number with decimal point.
            success = true;
        }else{
            success = false;
            AddStringRef(errorMessages, CreateStringReference("There must be numbers after the decimal point.".toCharArray()));
        }

        if(wasDotAndOrE){

        }else{
            success = false;
            AddStringRef(errorMessages, CreateStringReference("Exprected decimal point or e or E.".toCharArray()));
        }

        return success;
    }

    public static boolean IsValidJSONNumberFromExponent(char[] n, double i, StringArrayReference errorMessages) {
        boolean success;

        i = i + 1d;

        if (i < n.length) {
            // The exponent sign can either + or -,
            if (n[(int) i] == '+' || n[(int) i] == '-') {
                i = i + 1d;
            }

            if (i < n.length) {
                if (charIsNumber(n[(int) i])) {
                    // Read digits following decimal point.
                    i = IsValidJSONNumberAdvancePastDigits(n, i);

                    if(i == n.length){
                        // We found scientific number.
                        success = true;
                    }else{
                        success = false;
                        AddStringRef(errorMessages, CreateStringReference("There was characters following the exponent.".toCharArray()));
                    }
                } else {
                    success = false;
                    AddStringRef(errorMessages, CreateStringReference("There must be a digit following the optional exponent sign.".toCharArray()));
                }
            } else {
                success = false;
                AddStringRef(errorMessages, CreateStringReference("There must be a digit following optional the exponent sign.".toCharArray()));
            }
        } else {
            success = false;
            AddStringRef(errorMessages, CreateStringReference("There must be a sign or a digit following e or E.".toCharArray()));
        }

        return success;
    }

    public static boolean IsJSONNumberCharacter(char c) {
        char [] numericCharacters;
        boolean found;
        double i;

        numericCharacters = "0123456789.-+eE".toCharArray();

        found = false;

        for(i = 0; i < numericCharacters.length; i = i + 1d){
            if(numericCharacters[(int)i] == c){
                found = true;
            }
        }

        return found;
    }

    public static boolean GetJSONPrimitiveName(char[] string, double start, StringArrayReference errorMessages, char[] primitive, StringReference tokenReference) {
        char c, p;
        boolean done, success;
        double i;
        char [] str, token;

        done = false;
        success = true;

        token = "".toCharArray();

        for(i = start; i < string.length && ((i - start) < primitive.length) && !done; i = i + 1d) {
            c = string[(int)i];
            p = primitive[(int)(i - start)];
            if(c == p){
                // OK
                if((i + 1d - start) == primitive.length){
                    done = true;
                }
            }else{
                str = "".toCharArray();
                str = strConcatenateString(str, "Primitive invalid: ".toCharArray());
                str = strAppendCharacter(str, c);
                str = strAppendString(str, " vs ".toCharArray());
                str = strAppendCharacter(str, p);

                AddStringRef(errorMessages, CreateStringReference(str));
                done = true;
                success = false;
            }
        }

        if(done){
            if(StringsEqual(primitive, "false".toCharArray())) {
                token = "false".toCharArray();
            }
            if(StringsEqual(primitive, "true".toCharArray())) {
                token = "true".toCharArray();
            }
            if(StringsEqual(primitive, "null".toCharArray())) {
                token = "null".toCharArray();
            }
        }else{
            AddStringRef(errorMessages, CreateStringReference("Primitive invalid".toCharArray()));
            success = false;
        }

        tokenReference.string = token;

        return success;
    }

    public static boolean GetJSONString(char [] json, double start, StringReference tokenReference, NumberReference stringLengthReference, StringArrayReference errorMessages) {
        boolean success, done;
        char [] string, hex;
        NumberReference characterCount, hexReference;
        double i, l, c;
        StringReference errorMessage;

        characterCount = CreateNumberReference(0d);
        hex = CreateString(4d, '0');
        hexReference = new NumberReference();
        errorMessage = new StringReference();

        success = IsValidJSONStringInJSON(json, start, characterCount, stringLengthReference, errorMessages);

        if(success){
            l = characterCount.numberValue;
            string = new char[(int) l];

            c = 0d;
            string[(int)c] = '\"';
            c = c + 1d;

            done = false;
            for(i = start + 1d; !done; i = i + 1d){
                if(json[(int)i] == '\\'){
                    i = i + 1d;
                    if(json[(int)i] == '\"' || json[(int)i] == '\\' || json[(int)i] == '/') {
                        string[(int)c] = json[(int)i];
                        c = c + 1d;
                    }else if(json[(int) i] == 'b'){
                        string[(int)c] = (char)8d;
                        c = c + 1d;
                    }else if(json[(int) i] == 'f'){
                        string[(int)c] = (char)12d;
                        c = c + 1d;
                    }else if(json[(int) i] == 'n'){
                        string[(int)c] = (char)10d;
                        c = c + 1d;
                    }else if(json[(int) i] == 'r'){
                        string[(int)c] = (char)13d;
                        c = c + 1d;
                    }else if(json[(int) i] == 't'){
                        string[(int)c] = (char)9d;
                        c = c + 1d;
                    }else if(json[(int) i] == 'u'){
                        i = i + 1d;
                        hex[0] = charToUpperCase(json[(int)(i + 0d)]);
                        hex[1] = charToUpperCase(json[(int)(i + 1d)]);
                        hex[2] = charToUpperCase(json[(int)(i + 2d)]);
                        hex[3] = charToUpperCase(json[(int)(i + 3d)]);
                        nCreateNumberFromStringWithCheck(hex, 16d, hexReference, errorMessage);
                        string[(int)c] = (char)hexReference.numberValue;
                        i = i + 3d;
                        c = c + 1d;
                    }
                }else if(json[(int)i] == '\"'){
                    string[(int)c] = json[(int)i];
                    c = c + 1d;
                    done = true;
                }else{
                    string[(int)c] = json[(int)i];
                    c = c + 1d;
                }
            }

            tokenReference.string = string;
            success = true;
        }else{
            AddStringRef(errorMessages, CreateStringReference("End of string was not found.".toCharArray()));
            success = false;
        }

        return success;
    }

    public static boolean IsValidJSONString(char [] jsonString, StringArrayReference errorMessages) {
        boolean valid;
        NumberReference numberReference, stringLength;

        numberReference = new NumberReference();
        stringLength = new NumberReference();

        valid = IsValidJSONStringInJSON(jsonString, 0d, numberReference, stringLength, errorMessages);

        return valid;
    }

    public static boolean IsValidJSONStringInJSON(char[] json, double start, NumberReference characterCount, NumberReference stringLengthReference, StringArrayReference errorMessages) {
        boolean success, done;
        double i, j;
        char c;

        success = true;
        done = false;

        characterCount.numberValue = 1d;

        for(i = start + 1d; i < json.length && !done && success; i = i + 1d) {
            if(!IsJSONIllegalControllCharacter(json[(int)i])) {
                if (json[(int) i] == '\\') {
                    i = i + 1d;
                    if(i < json.length) {
                        if(json[(int) i] == '\"' || json[(int) i] == '\\' || json[(int) i] == '/' || json[(int) i] == 'b' || json[(int) i] == 'f' || json[(int) i] == 'n' || json[(int) i] == 'r' || json[(int) i] == 't') {
                            characterCount.numberValue = characterCount.numberValue + 1d;
                        }else if(json[(int) i] == 'u') {
                            if(i + 4d < json.length){
                                for(j = 0d; j < 4d && success; j = j + 1d) {
                                    c = json[(int)(i + j + 1d)];
                                    if(nCharacterIsNumberCharacterInBase(c, 16d) || c == 'a' || c == 'b' || c == 'c' || c == 'd' || c == 'e' || c == 'f'){
                                    }else{
                                        success = false;
                                        AddStringRef(errorMessages, CreateStringReference("\\u must be followed by four hexadecimal digits.".toCharArray()));
                                    }
                                }
                                characterCount.numberValue = characterCount.numberValue + 1d;
                                i = i + 4d;
                            }else{
                                success = false;
                                AddStringRef(errorMessages, CreateStringReference("\\u must be followed by four characters.".toCharArray()));
                            }
                        }else{
                            success = false;
                            AddStringRef(errorMessages, CreateStringReference("Escaped character invalid.".toCharArray()));
                        }
                    }else{
                        success = false;
                        AddStringRef(errorMessages, CreateStringReference("There must be at least two character after string escape.".toCharArray()));
                    }
                }else if (json[(int) i] == '\"') {
                    characterCount.numberValue = characterCount.numberValue + 1d;
                    done = true;
                }else{
                    characterCount.numberValue = characterCount.numberValue + 1d;
                }
            }else{
                success = false;
                AddStringRef(errorMessages, CreateStringReference("Unicode code points 0-31 not allowed in JSON string.".toCharArray()));
            }
        }

        if(done){
            stringLengthReference.numberValue = i - start;
        }else{
            success = false;
            AddStringRef(errorMessages, CreateStringReference("String must end with \".".toCharArray()));
        }

        return success;
    }

    public static boolean IsJSONIllegalControllCharacter(char c) {
        double cnr;
        boolean isControll;

        cnr = c;

        if(cnr >= 0d && cnr < 32d){
            isControll = true;
        }else{
            isControll = false;
        }

        return isControll;
    }
}