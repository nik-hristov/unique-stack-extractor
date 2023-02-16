package json;

import java.util.Objects;

/**
 * Represents the stacktrace
 */
public class StackFrame {
  String moduleName;
  String moduleVersion;
  String declaringClass;
  String methodName;
  String fileName;
  int lineNumber;
  int format;

  public String getModuleName() {
    return moduleName;
  }

  public void setModuleName(final String moduleName) {
    this.moduleName = moduleName;
  }

  public String getModuleVersion() {
    return moduleVersion;
  }

  public void setModuleVersion(final String moduleVersion) {
    this.moduleVersion = moduleVersion;
  }

  public String getDeclaringClass() {
    return declaringClass;
  }

  public void setDeclaringClass(final String declaringClass) {
    this.declaringClass = declaringClass;
  }

  public String getMethodName() {
    return methodName;
  }

  public void setMethodName(final String methodName) {
    this.methodName = methodName;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(final String fileName) {
    this.fileName = fileName;
  }

  public int getLineNumber() {
    return lineNumber;
  }

  public void setLineNumber(final int lineNumber) {
    this.lineNumber = lineNumber;
  }

  public int getFormat() {
    return format;
  }

  public void setFormat(final int format) {
    this.format = format;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final StackFrame that = (StackFrame) o;
    return Objects.equals(getModuleName(), that.getModuleName())
        && Objects.equals(getModuleVersion(), that.getModuleVersion())
        && getLineNumber() == that.getLineNumber()
        && getFormat() == that.getFormat()
        && Objects.equals(getDeclaringClass(), that.getDeclaringClass())
        && Objects.equals(getMethodName(), that.getMethodName())
        && Objects.equals(getFileName(), that.getFileName());
  }

  @Override
  public int hashCode() {
    int result = 1;
    final String moduleName = getModuleName();
    result = 31 * result + (moduleName != null ? moduleName.hashCode() : 0);

    final String moduleVersion = getModuleVersion();
    result = 31 * result + (moduleVersion != null ? moduleVersion.hashCode() : 0);

    final String declaringClass = getDeclaringClass();
    result = 31 * result + (declaringClass != null ? declaringClass.hashCode() : 0);

    final String methodName = getMethodName();
    result = 31 * result + (methodName != null ? methodName.hashCode() : 0);

    final String filename = getFileName();
    result = 31 * result + (filename != null ? filename.hashCode() : 0);
    result = 31 * result + getLineNumber();
    result = 31 * result + getFormat();
    return result;
  }
}
