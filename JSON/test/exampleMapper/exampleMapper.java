package exampleMapper;

import JSON.StringElementMaps.StringElementMap;
import JSON.structures.Element;

import static JSON.StringElementMaps.StringElementMaps.GetObjectValue;
import static JSON.elementTypeEnum.elementTypeEnum.ElementTypeEnumEquals;

public class exampleMapper {
    public static Example mapTo(Element root) {
        Example example;

        example = new Example();
        example.a = GetObjectValue(root.object, "a".toCharArray()).string;
        example.b = mapbTo(GetObjectValue(root.object, "b".toCharArray()).array);
        example.x = mapXTo(GetObjectValue(root.object, "x".toCharArray()).object);

        return example;
    }

    public static X mapXTo(StringElementMap object) {
        X x;

        x = new X();

        if(ElementTypeEnumEquals(GetObjectValue(object, "x1".toCharArray()).type.name, "nullValue".toCharArray())){
            x.x1IsNull = true;
            x.x1 = "".toCharArray();
        }

        x.x2 = GetObjectValue(object, "x2".toCharArray()).booleanValue;
        x.x3 = GetObjectValue(object, "x3".toCharArray()).booleanValue;

        return x;
    }

    public static double[] mapbTo(Element[] array) {
        double [] b;
        double i;

        b = new double[array.length];

        for(i = 0; i < array.length; i = i + 1d){
            b[(int)i] = array[(int)i].number;
        }

        return b;
    }
}
