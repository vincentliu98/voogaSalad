import groovy.lang.Binding;
import groovy.lang.GroovyShell;

public class GroovyShellTest {
    public static void main(String[] args) {
        var sharedData = new Binding();
        var shell = new GroovyShell(sharedData);

        TestGameData.setup();
        sharedData.setVariable("GameData", TestGameData.class);

        shell.evaluate("GameData.addEntry('A', 'a', '0')");

        System.out.println(TestGameData.getValue("A", "a")); // 0

        shell.evaluate("GameData.addEntry('A', 'b', '1')");

        System.out.println(TestGameData.getValue("A", "b")); // 1

        shell.evaluate("GameData.addEntry('A', 'b', '2')");

        System.out.println(TestGameData.getValue("A", "b")); // 2

        System.out.println(TestGameData.getMap("A")); // a->0 b->2
    }
}