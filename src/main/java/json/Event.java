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
