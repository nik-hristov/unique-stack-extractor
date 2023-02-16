package json;

import java.util.List;
import java.util.Objects;

/**
 * It represents a parsed line from a db-insights query
 * Currently only holds the stack trace elements.
 */
public class Event {
  private String timestamp;
  private List<StackFrame> stackTraceElements;
  private String appId;
  private String orgId;

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(final String timestamp) {
    this.timestamp = timestamp;
  }

  public List<StackFrame> getStackTraceElements() {
    return stackTraceElements;
  }

  public void setStackTraceElements(final List<StackFrame> stackTraceElements) {
    this.stackTraceElements = stackTraceElements;
  }

  public String getAppId() {
    return appId;
  }

  public void setAppId(final String appId) {
    this.appId = appId;
  }

  public String getOrgId() {
    return orgId;
  }

  public void setOrgId(final String orgId) {
    this.orgId = orgId;
  }

  public void omit(final boolean simpleCompare, final boolean omitFormatAndLineNumbers, final boolean omitFilenames) {
    for (final StackFrame frame : stackTraceElements) {
      if (simpleCompare) {
        final String declaringClass = frame.getDeclaringClass();
        if (declaringClass.contains(".$Proxy")) {
          frame.setDeclaringClass(declaringClass.substring(0, declaringClass.lastIndexOf(".") + 1) + "$Proxy");
        } else if (declaringClass.contains("$Lambda")) {
          frame.setDeclaringClass(declaringClass.substring(0, declaringClass.lastIndexOf("$Lambda")) +
              "LambdaReference");
        }

        final String methodName = frame.getMethodName();
        if (methodName.contains("$")) {
          frame.setMethodName(methodName.substring(0, methodName.lastIndexOf("$")));
        }
      }
      if (omitFormatAndLineNumbers) {
        frame.setFormat(-1);
        frame.setLineNumber(-1);
      }
      if (omitFilenames) {
        frame.setFileName("<omitted>");
      }
    }
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final Event that = (Event) o;
    return Objects.equals(getStackTraceElements(), that.getStackTraceElements());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getStackTraceElements());
  }
}
