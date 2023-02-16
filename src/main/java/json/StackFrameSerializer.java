package json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/**
 * Serializes and deserializes db-insights queries
 */
public class StackFrameSerializer implements JsonSerializer<StackFrame>, JsonDeserializer<StackFrame> {
  @Override
  public StackFrame deserialize(final JsonElement jsonElement, final Type type,
                                final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    final JsonObject object = jsonElement.getAsJsonObject();
    final StackFrame frame = new StackFrame();

    final JsonElement moduleName = object.get("moduleName");
    if (moduleName != null) {
      frame.setModuleName(moduleName.getAsString());
    }

    final JsonElement moduleVersion = object.get("moduleVersion");
    if (moduleVersion != null) {
      frame.setModuleVersion(moduleVersion.getAsString());
    }
    final JsonElement declaringClass = object.get("declaringClass");
    if (declaringClass != null) {
      frame.setDeclaringClass(declaringClass.getAsString());
    }

    final JsonElement methodName = object.get("methodName");
    if (methodName != null) {
      frame.setMethodName(methodName.getAsString());
    }
    final JsonElement fileName = object.get("fileName");
    if (fileName != null) {
      frame.setFileName(fileName.getAsString());
    }

    final JsonElement lineNumber = object.get("lineNumber");
    if (lineNumber != null) {
      frame.setLineNumber(lineNumber.getAsInt());
    }

    final JsonElement format = object.get("format");
    if (format != null) {
      frame.setFormat(format.getAsInt());
    }
    return frame;
  }

  @Override
  public JsonElement serialize(final StackFrame stackFrame, final Type type,
                               final JsonSerializationContext jsonSerializationContext) {
    final JsonObject object = new JsonObject();
    object.addProperty("moduleName", stackFrame.getModuleName());
    object.addProperty("moduleVersion", stackFrame.getModuleVersion());
    object.addProperty("declaringClass", stackFrame.getDeclaringClass());
    object.addProperty("methodName", stackFrame.getMethodName());
    object.addProperty("fileName", stackFrame.getFileName());
    object.addProperty("lineNumber", stackFrame.getLineNumber());
    object.addProperty("format", stackFrame.getFormat());
    return object;
  }
}
