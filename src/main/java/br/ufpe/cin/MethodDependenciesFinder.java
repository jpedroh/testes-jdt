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
    String fqdn;
    String methodName;

    public MethodDependency(String fqdn, String methodName) {
      this.fqdn = fqdn;
      this.methodName = methodName;
    }

    @Override
    public String toString() {
      return this.fqdn + "\t" + this.methodName;
    }

    @Override
    public boolean equals(Object obj) {
      if (!(obj instanceof MethodDependency)) {
        return false;
      }
      return fqdn.equals(((MethodDependency) obj).fqdn) && methodName.equals(((MethodDependency) obj).methodName);
    }

    @Override
    public int hashCode() {
      return Objects.hash(fqdn, methodName);
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
