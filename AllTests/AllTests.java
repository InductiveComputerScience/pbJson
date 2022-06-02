import com.progsbase.libraries.JSON.*;
import org.junit.Test;

import java.util.List;

import static com.progsbase.libraries.JSON.JSONObjectReader.readJSONExceptionOnFailure;
import static com.progsbase.libraries.JSON.JSONObjectReader.readJSONWithCheck;
import static org.junit.Assert.*;
import static tests.tests.test;

public class AllTests {
    @Test
    public void runTest(){
        assertEquals(0, test(), 0);
    }

    @Test
    public void testJSONReaderJava() throws JSONException {
        String str = "{\"name\":\"base64\",\"version\":\"0.1.0\",\"business namespace\":\"no.inductive.idea10.programs\",\"scientific namespace\":\"computerscience.algorithms.base64\",\"imports\":[],\"imports2\":{},\"development imports\":[[\"\",\"no.inductive.idea10.programs\",\"arrays\",\"0.1.0\"]]}";

        Object o = JSONObjectReader.readJSON(str);
        Object o2 = readJSONExceptionOnFailure(str);
        JSONReturn jsonReturn = readJSONWithCheck(str);
    }

    @Test
    public void testJSONReaderJavaError() {
        String str = "{\"name\":\"base64\",\"version\":\"0.1.0\",\"business namespace\":\"no.inductive.idea10.programs\",\"scientific namespace\":\"computerscience.algorithms.base64\",\"imports\":[],\"imports2\":{},\"development imports\":[[\"\",\"no.inductive.idea10.programs\",\"arrays\",\"0.1.0\"]]";

        Object o = JSONObjectReader.readJSON(str);
        assertNull(o);
        boolean threwException = false;
        try {
            Object o2 = readJSONExceptionOnFailure(str);
        }catch (JSONException e){
            threwException = true;
        }
        assertTrue(threwException);
        JSONReturn jsonReturn = readJSONWithCheck(str);
        assertFalse(jsonReturn.success);
    }

    @Test
    public void testJSONReaderJavaReflective() throws JSONException {
        String str = "{\"a\":\"hei\",\"b\":[1.2,0.1,100],\"x\":{\"x1\":null,\"x2\":true,\"x3\":false}}";

        Example e = JSONReflectiveReader.readJSON(str, Example.class);

        System.out.println(e);
    }

    @Test
    public void testJSONReaderJavaReflective2() throws JSONException {
        String str = "{\"a\":\"hei\",\"b\":[1.2,0.1,100],\"x\":{\"x1\":null,\"x2\":true,\"x3\":false}}";

        Example2 e = JSONReflectiveReader.readJSON(str, Example2.class);

        System.out.println(e);
    }

    @Test
    public void testJSOWriterJavaObject1(){
        String str = "{\"name\":\"base64\",\"version\":\"0.1.0\",\"business namespace\":\"no.inductive.idea10.programs\",\"scientific namespace\":\"computerscience.algorithms.base64\",\"imports\":[],\"imports2\":{},\"development imports\":[[\"\",\"no.inductive.idea10.programs\",\"arrays\",\"0.1.0\"]]}";

        Object o = JSONObjectReader.readJSON(str);

        String str2 = JSONObjectWriter.writeJSON(o);

        System.out.println(str);
        System.out.println(str2);
    }

    @Test
    public void testJSOWriterJavaObject2(){
        String str = "{\"a\":\"hei\",\"b\":[1.2,0.1,100],\"x\":{\"x1\":null,\"x2\":true,\"x3\":false}}";

        Object o = JSONObjectReader.readJSON(str);

        String str2 = JSONObjectWriter.writeJSON(o);

        System.out.println(str);
        System.out.println(str2);
    }

    @Test
    public void testJSOWriterJavaReflectiveObject1() throws JSONException {
        String str = "{\"a\":\"hei\",\"b\":[1.2,0.1,100],\"x\":{\"x1\":null,\"x2\":true,\"x3\":false}}";

        Example e = JSONReflectiveReader.readJSON(str, Example.class);

        String str2 = JSONReflectiveWriter.writeJSON(e);

        System.out.println(str);
        System.out.println(str2);
    }

    @Test
    public void testJSOWriterJavaReflectiveObject2() throws JSONException {
        String str = "{\"a\":\"hei\",\"b\":[1.2,0.1,100],\"x\":{\"x1\":null,\"x2\":true,\"x3\":false}}";

        Example2 e = JSONReflectiveReader.readJSON(str, Example2.class);

        String str2 = JSONReflectiveWriter.writeJSON(e);

        System.out.println(str);
        System.out.println(str2);
    }

    @Test
    public void testJSONReaderJavaReflective3() throws JSONException {
        String str = "{\"x\": [[1, 2, 3], [1, 2, 3], [1, 2, 3]]}";

        DList e = JSONReflectiveReader.readJSON(str, DList.class);

        System.out.println(e.x);
    }

    @Test
    public void testJSONReaderJavaReflective4() throws JSONException {
        String str = "[[1.1, 2, 3], [1, 2, 3], [1, 2, 3]]";

        List<List<Double>> e = JSONReflectiveReader.readJSON(str, List.class, new GenericTypeGetter<List<List<Double>>>(){}.getType());

        System.out.println(e);
    }

    @Test
    public void testJSONReaderJavaReflectiveEnum() throws JSONException {
        String str = "{\"a\": \"b\"}";

        ClassWithEnum e = JSONReflectiveReader.readJSON(str, ClassWithEnum.class);

        assertEquals(e.a, TypeA.b);
    }

    @Test
    public void testJSONWriterJavaReflectiveEnum() throws JSONException {
        ClassWithEnum e = new ClassWithEnum();
        e.a = TypeA.c;

        String s = JSONReflectiveWriter.writeJSON(e);

        assertEquals(s, "{\"a\":\"c\"}");
    }

    @Test
    public void testAnnotation() throws JSONException {
        String json1 = "{\"package\":\"nice\"}";
        ExampleWithReservedKeyword exampleWithReservedKeyword = JSONReflectiveReader.readJSON(json1, ExampleWithReservedKeyword.class);

        assertEquals("nice", exampleWithReservedKeyword.packagex);

        String json2 = JSONReflectiveWriter.writeJSON(exampleWithReservedKeyword);

        assertEquals(json1, json2);
    }
}
