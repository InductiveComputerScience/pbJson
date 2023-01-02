package exampleMapper;

import DataStructures.Array.Structures.*;

import static DataStructures.Array.Arrays.Arrays.*;
import static DataStructures.Array.Structures.Structures.*;

public class exampleMapper {
    public static Example mapTo(Data root) {
        Example example;

        example = new Example();
        example.a = GetDataFromStruct(root.structure, "a".toCharArray()).string;
        example.b = mapbTo(GetDataFromStruct(root.structure, "b".toCharArray()).array);
        example.x = mapXTo(GetDataFromStruct(root.structure, "x".toCharArray()).structure);

        return example;
    }

    public static X mapXTo(Structure object) {
        X x;

        x = new X();

        if(IsNoType(GetDataFromStruct(object, "x1".toCharArray()))){
            x.x1IsNull = true;
            x.x1 = "".toCharArray();
        }

        x.x2 = GetDataFromStruct(object, "x2".toCharArray()).booleanx;
        x.x3 = GetDataFromStruct(object, "x3".toCharArray()).booleanx;

        return x;
    }

    public static double[] mapbTo(Array array) {
        double [] b;
        double i;

        b = new double[(int)ArrayLength(array)];

        for(i = 0; i < array.length; i = i + 1d){
            b[(int)i] = ArrayIndex(array, i).number;
        }

        return b;
    }
}
