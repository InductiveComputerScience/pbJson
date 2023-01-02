package JSON.Validator;

import DataStructures.Array.Structures.DataReference;
import references.references.StringArrayReference;

import static DataStructures.Array.Structures.Structures.FreeData;
import static JSON.Parser.Parser.ReadJSON;

public class Validator {
    public static boolean IsValidJSON(char [] json, StringArrayReference errorMessage){
        boolean success;
        DataReference elementReference;

        elementReference = new DataReference();

        success = ReadJSON(json, elementReference, errorMessage);

        if(success) {
            FreeData(elementReference.data);
        }

        return success;
    }
}
