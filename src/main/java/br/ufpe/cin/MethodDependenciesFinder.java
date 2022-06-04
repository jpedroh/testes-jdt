package br.ufpe.cin;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;

public class MethodDependenciesFinder {
  public static class MethodDependency {
    final String classQualifiedName;
    final String methodName;

    public MethodDependency(String classQualifiedName, String methodName) {
      this.classQualifiedName = classQualifiedName;
      this.methodName = methodName;
    }

    @Override
    public String toString() {
      return this.classQualifiedName + "\t" + this.methodName;
    }

    @Override
    public boolean equals(Object obj) {
      if (!(obj instanceof MethodDependency)) {
        return false;
      }
      return classQualifiedName.equals(((MethodDependency) obj).classQualifiedName)
          && methodName.equals(((MethodDependency) obj).methodName);
    }

    @Override
    public int hashCode() {
      return Objects.hash(classQualifiedName, methodName);
    }
  }

  public Set<MethodDependency> getMethodDependencies(MethodDeclaration methodDeclaration) {
    return methodDeclaration
        .findAll(MethodCallExpr.class)
        .stream()
        .map((call) -> {
          final String classQualifiedName = call.resolve().getPackageName() + "." + call.resolve().getClassName();
          return new MethodDependency(classQualifiedName, call.getNameAsString());
        })
        .collect(Collectors.toSet());
  }
}
