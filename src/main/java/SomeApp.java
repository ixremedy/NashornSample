import java.io.IOException;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import static java.lang.Thread.currentThread;

public class SomeApp {
    private static ScriptEngine engine =
            new ScriptEngineManager().getEngineByName("nashorn");
    private static Invocable invocable = (Invocable) engine;

    public static String jsFunction1(int a, int b) {
        try {
            var txtStream = currentThread().getContextClassLoader().getResourceAsStream("js_function1.js");
            var asString = new String(txtStream.readAllBytes());

            engine.eval(asString);
            return invocable.invokeFunction("func", a, b).toString();
        } catch (IOException exc) {
            System.err.println("Caught exception: " + exc.getMessage());
        } catch (ScriptException exc) {
            System.err.println("Caught Script exception: " + exc.getMessage());
        } catch (NoSuchMethodException exc) {
            System.err.println("Requested Method Not Found exception: " + exc.getMessage());
        }
        return null;
    }

    public static Integer[] getAB() {
        try {
            var aTxtStream = currentThread().getContextClassLoader().getResourceAsStream("params/a.txt");
            var aAsString = new String(aTxtStream.readAllBytes());

            var bTxtStream = currentThread().getContextClassLoader().getResourceAsStream("params/b.txt");
            var bAsString = new String(bTxtStream.readAllBytes());

            var arr = new Integer[]{Integer.parseInt(aAsString),Integer.parseInt(bAsString)};

            return arr;
        } catch (IOException exc) {
            System.err.println("Caught exception: " + exc.getMessage());
        }

        return null;
    }

    public static void main(String[] args) {
        var ab = getAB();
        var result = jsFunction1(ab[0], ab[1]);
        System.out.println(result);
    }
}
