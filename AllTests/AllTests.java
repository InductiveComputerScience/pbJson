import com.progsbase.libraries.JSON.*;
import org.junit.Test;

import static com.progsbase.libraries.JSON.JSONObjectReader.readJSON;
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

        Object o = readJSON(str);
        Object o2 = readJSONExceptionOnFailure(str);
        JSONReturn jsonReturn = readJSONWithCheck(str);
    }

    @Test
    public void testJSONReaderJavaError() {
        String str = "{\"name\":\"base64\",\"version\":\"0.1.0\",\"business namespace\":\"no.inductive.idea10.programs\",\"scientific namespace\":\"computerscience.algorithms.base64\",\"imports\":[],\"imports2\":{},\"development imports\":[[\"\",\"no.inductive.idea10.programs\",\"arrays\",\"0.1.0\"]]";

        Object o = readJSON(str);
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

        Object o = readJSON(str);

        String str2 = JSONObjectWriter.writeJSON(o);

        System.out.println(str);
        System.out.println(str2);
    }

    @Test
    public void testJSOWriterJavaObject2(){
        String str = "{\"a\":\"hei\",\"b\":[1.2,0.1,100],\"x\":{\"x1\":null,\"x2\":true,\"x3\":false}}";

        Object o = readJSON(str);

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
}
