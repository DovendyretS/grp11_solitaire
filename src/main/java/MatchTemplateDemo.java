import org.opencv.core.Core;

public class MatchTemplateDemo {
    public static void main(String[] args) {
        // load the native OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // run code
        new Cards().run(args);
    }
}
