package JSON.TokenLists;

import JSON.structures.Token;

public class TokenLists {
    public static Token[] AddToken(Token[] list, Token a) {
        Token newlist[];
        double i;

        newlist = new Token[(int)(list.length + 1d)];

        for(i = 0; i < list.length; i = i + 1d){
            newlist[(int)i] = list[(int)i];
        }
        newlist[list.length] = a;

        delete(list);

        return newlist;
    }

    public static void AddTokenRef(TokenArrayReference list, Token i) {
        list.array = AddToken(list.array, i);
    }

    public static Token[] RemoveToken(Token[] list, double  n) {
        Token newlist[];
        double i;

        newlist = new Token[(int)(list.length - 1d)];

        for(i = 0; i < list.length; i = i + 1d){
            if(i < n){
                newlist[(int)i] = list[(int)i];
            }
            if(i > n){
                newlist[(int)(i - 1d)] = list[(int)i];
            }
        }

        delete(list);

        return newlist;
    }

    public static Token GetTokenRef(TokenArrayReference list, double i) {
        return list.array[(int)i];
    }

    public static void RemoveTokenRef(TokenArrayReference list, double i) {
        list.array = RemoveToken(list.array, i);
    }

    public static void delete(Object object){
        // Java has garbage collection.
    }
}
