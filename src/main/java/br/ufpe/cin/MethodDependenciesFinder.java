package br.ufpe.cin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class MethodDependenciesFinder {
  public static class MethodDependency {
    public final String qualifiedName;
    public final String methodName;

    public MethodDependency(String fqdn, String methodName) {
      this.qualifiedName = fqdn;
      this.methodName = methodName;
    }

    @Override
    public String toString() {
      return this.qualifiedName + "\t" + this.methodName;
    }

    @Override
    public boolean equals(Object obj) {
      if (!(obj instanceof MethodDependency)) {
        return false;
      }
      return qualifiedName.equals(((MethodDependency) obj).qualifiedName)
          && methodName.equals(((MethodDependency) obj).methodName);
    }

    @Override
    public int hashCode() {
      return Objects.hash(qualifiedName, methodName);
    }
  }

  private static class MethodDependenciesVisitor extends ASTVisitor {
    private final Set<MethodDependency> methodCalls = new HashSet<>();

    public boolean visit(MethodInvocation node) {
      if (node.getExpression() != null) {
        ITypeBinding binding = node.getExpression().resolveTypeBinding();
        if (binding != null) {
          String classQualifiedName = binding.getQualifiedName();
          String methodName = node.getName().getIdentifier();
          methodCalls.add(new MethodDependency(classQualifiedName, methodName));
        }
      }
      return true;
    }

    public Set<MethodDependency> getMethodDependencies() {
      return this.methodCalls;
    }
  }

  private final MethodDependenciesVisitor visitor;

  public MethodDependenciesFinder() {
    this.visitor = new MethodDependenciesVisitor();
  }

  public Set<MethodDependency> getMethodDependencies(MethodDeclaration methodDeclaration) {
    methodDeclaration.getBody().accept(visitor);
    return visitor.getMethodDependencies();
  }
}
