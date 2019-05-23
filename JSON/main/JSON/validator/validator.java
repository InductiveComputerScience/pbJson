package JSON.validator;

import JSON.structures.ElementReference;
import references.references.StringArrayReference;

import static JSON.json.json.DeleteElement;
import static JSON.parser.parser.ReadJSON;

public class validator {
    public static boolean IsValidJSON(char [] json, StringArrayReference errorMessage){
        boolean success;
        ElementReference elementReference;

        elementReference = new ElementReference();

        success = ReadJSON(json, elementReference, errorMessage);

        if(success) {
            DeleteElement(elementReference.element);
        }

        return success;
    }
}
