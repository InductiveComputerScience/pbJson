package JSON.tokenTypeEnum;

import JSON.structures.TokenType;
import references.references.BooleanReference;

import static arrays.arrays.arrays.StringsEqual;

public class tokenTypeEnum {
    public static TokenType GetTokenType(char [] elementTypeName){
        TokenType et;

        et = new TokenType();
        et.name = elementTypeName;

        return et;
    }

    public static TokenType GetAndCheckTokenType(char [] elementTypeName, BooleanReference found){
        TokenType[] elementTypes;
        TokenType tokenType;
        double i, count;

        count = 12;

        elementTypes = new TokenType[(int)count];

        for(i = 0; i < count; i = i + 1d){
            elementTypes[(int)i] = new TokenType();
        }

        elementTypes[0].name = "openCurly".toCharArray();
        elementTypes[1].name = "closeCurly".toCharArray();
        elementTypes[2].name = "openSquare".toCharArray();
        elementTypes[3].name = "closeSquare".toCharArray();
        elementTypes[4].name = "comma".toCharArray();
        elementTypes[5].name = "colon".toCharArray();
        elementTypes[6].name = "nullValue".toCharArray();
        elementTypes[7].name = "trueValue".toCharArray();
        elementTypes[8].name = "falseValue".toCharArray();
        elementTypes[9].name = "string".toCharArray();
        elementTypes[10].name = "number".toCharArray();
        elementTypes[11].name = "end".toCharArray();

        found.booleanValue = false;
        tokenType = new TokenType();
        for(i = 0; i < count && !found.booleanValue; i = i + 1d){
            tokenType = elementTypes[(int)i];
            if(StringsEqual(tokenType.name, elementTypeName)){
                found.booleanValue = true;
            }
        }

        return tokenType;
    }

    public static boolean TokenTypeEnumStructureEquals(TokenType a, TokenType b){
        return StringsEqual(a.name, b.name);
    }

    public static boolean TokenTypeEnumEquals(char [] a, char [] b){
        boolean equals;
        TokenType eta, etb;
        BooleanReference founda, foundb;

        founda = new BooleanReference();
        foundb = new BooleanReference();

        eta = GetAndCheckTokenType(a, founda);
        etb = GetAndCheckTokenType(b, foundb);

        if(founda.booleanValue && foundb.booleanValue){
            equals = TokenTypeEnumStructureEquals(eta, etb);
        }else{
            equals = false;
        }

        return equals;
    }
}
