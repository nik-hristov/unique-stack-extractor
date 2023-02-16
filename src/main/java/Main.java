import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import json.Event;
import json.StackFrame;
import json.StackFrameSerializer;

/**
 * Run it like java -jar unique-stacktrace-extract.jar file.sql
 * <p>
 * Additional arguments can be added for easier comparison:
 * -simple = to change lambda and proxy references, because identical stacktraces might appear as non-unique because
 * of lambdas or proxies, for instance ProviderRegistryImpl$$Lambda$530/0x00000000ac610860 will be simplified to
 * ProviderRegistryImpl$LambdaReference and jdk.proxy3.$Proxy74 will become jdk.proxy3.$Proxy
 * <p>
 * -ln = remove line numbers and format (both become -1), because it sometimes appears that two identical stacks have
 * slightly different line number and format fields (for instance one has line number 87 and format 0 and the other
 * stack frame has line number 88 and format 1)
 * <p>
 * - fn = remove filenames (filenames become <omitted> ) - there are cases where the filename is not present and
 * there are two absolutely identical stacktraces, but one of them completely lacks filename fields.
 * <p>
 * And no, it's not a problem in the deserializing, the filename fields do not exist in the db-audit result.
 * <p>
 * sooo normally you would want to run it with just -simple like so:
 * <b>java -jar unique-stacktrace-extract.jar file.sql -simple</b>
 */
public class Main {
  // we expect stack to be second column, but who knows
  private static int columnIndex = 2;
  public static final Gson GSON =
      new GsonBuilder()
          .registerTypeAdapter(StackFrame.class, new StackFrameSerializer())
          .create();
  public static boolean simpleCompare = false;
  public static boolean omitLinesAndFormat = false;
  public static boolean omitFilenames = false;

  public static void main(String[] args) {
    String inputFileName = args[0];
    if (inputFileName == null || inputFileName.isBlank()) {
      System.out.println("Input filename can't be empty");
      return;
    }
    if (args.length > 1) {
      for (int i = 1; i < args.length; i++) {
        switch (args[i]) {
          case "-simple":
            System.out.println("Simple compare: ON\nLambdas and proxies will be overridden to avoid confusion");
            simpleCompare = true;
            break;
          case "-ln":
            omitLinesAndFormat = true;
            System.out.println("Format and line numbers will be omitted");
            break;
          case "-fn":
            System.out.println("File names will be omitted");
            omitFilenames = true;
            break;
        }
      }
    }

    try {
      List<Event> events = loadFile(inputFileName);
      System.out.println("Found " + events.size() + " db events");
      Set<Event> uniques = compareUniques(events);
      System.out.println("Extracted " + uniques.size() + " unique stack traces");
      final String outputFilename = inputFileName.substring(0, inputFileName.lastIndexOf(".")) + "-uniques.json";
      try (FileWriter fl = new FileWriter(outputFilename)) {
        GSON.toJson(uniques, fl);
        System.out.println("Unique stacktraces saved to: " + outputFilename);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Load the file to parse
   *
   * @param filename filename (eg. /Downloads/that-data.sql
   * @return the list of parsed stack events
   * @throws IOException
   */
  private static List<Event> loadFile(final String filename) throws IOException {
    final File file = new File(filename);
    final BufferedReader br = new BufferedReader(new FileReader(file));

    final List<Event> clmAuditData = new ArrayList<>();
    String st;
    // we skip first 3 lines
    int count = 0;
    while ((st = br.readLine()) != null) {
      if (count < 2 || st.startsWith("+-")) {
        if (count == 1) {
          setColumnIndex(st);
        }
        count++;
        continue;
      }
      final Event data = onLineRead(st);
      clmAuditData.add(data);
    }
    return clmAuditData;
  }

  /**
   * Read and parse a line from the text file
   *
   * @param line the line
   * @return the data event extracted from that line
   */
  private static Event onLineRead(final String line) {
    final String[] elements = line.split("\\|");
    final String stackTrace = elements[columnIndex].trim();
    final Type stackType = new TypeToken<ArrayList<StackFrame>>() {
    }.getType();
    final List<StackFrame> stackFrames = GSON.fromJson(stackTrace, stackType);
    final Event data = new Event();
    data.setStackTraceElements(stackFrames);
    return data;
  }

  private static Set<Event> compareUniques(final List<Event> events) {
    Set<Event> uniques = new HashSet<>();
    if (omitRequired()) {
      for (Event event : events) {
        event.omit(simpleCompare, omitLinesAndFormat, omitFilenames);
        uniques.add(event);
      }
    } else {
      uniques.addAll(events);
    }
    return uniques;
  }

  private static boolean omitRequired() {
    return (simpleCompare || omitFilenames || omitLinesAndFormat);
  }

  /**
   * Just in case at some point I change the query and the STACK column is not where I expect it to be.
   *
   * @param line
   */
  private static void setColumnIndex(final String line) {
    final String[] columns = line.split("\\|");
    for (int i = 0; i < columns.length; i++) {
      if ("stack".equals(columns[i].trim())) {
        System.out.println("Stack found at column index: " + i);
        columnIndex = i;
        return;
      }
    }
    System.out.println("Column <stack> was not found.");
  }
}
