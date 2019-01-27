import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final Pattern latlongPattern = Pattern.compile("maps.google.com/maps\\?f=q&amp;hl=en&amp;q=(.*),\\+(.*)&amp");
    private static final Pattern descrPattern = Pattern.compile("<p style=\"font-size:14px; margin: 16px 0px 0px 0px\"><b>.*Technical Details.*?</div>", Pattern.DOTALL);
    private static final Pattern powerPattern = Pattern.compile("([\\d|,]*).?Watts");

    public static void main(String[] args) throws IOException {
        if (args == null || args.length == 0) {
            System.err.println("Input argument must point to the folder with files to parse");
            return;
        }
        File dir = new File(args[0]);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            List<Data> data = new ArrayList<>();
            for (File child : directoryListing) {
                if (child.isFile() && child.canRead()) {
                    String content = readFile(child.getPath(), Charset.defaultCharset());
                    data.add(parseContent(content));
                    System.out.println(String.format("Finished parsing file %s", child));
                }
            }
            for (Data datum : data) {
                System.out.println(datum);
            }
        } else {
            System.err.println("Input argument is not a folder");
        }
    }

    private static Data parseContent(String content) {
        Data data = new Data();
        Matcher mLatLong = latlongPattern.matcher(content);
        Matcher mDescr = descrPattern.matcher(content);
        Matcher mPower = powerPattern.matcher(content);
        if (mLatLong.find() && mDescr.find() && mPower.find()) {
            data.latitude = mLatLong.group(1);
            data.longitude = mLatLong.group(2);
            data.title = "`" + mDescr.group(0)
                    .replace("/info", "https://radio-locator.com/info")
                    .replace("/cgi-bin/", "https://radio-locator.com/cgi-bin/")
                    .replace("/images/", "https://radio-locator.com/images/")
                    .replace("class=\"tech_label tech_nowrap\"", "")
                    .replace("class=\"tech_value\"", "")
                    .replace("class=\"tech_size\"", "")
                    + "`";
            data.power = mPower.group(1);
        } else {
            System.err.println("No match");
        }
        return data;
    }

    private static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    static class Data {
        String latitude;
        String longitude;
        String title;
        String power;

        @Override
        public String toString() {
            return "[" +
                    latitude + ',' +
                    longitude + "," +
                    title + ",\"" +
                    power + "\"],";
        }
    }
}
