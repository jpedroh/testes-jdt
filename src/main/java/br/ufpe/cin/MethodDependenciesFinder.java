package br.ufpe.cin;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

public class MethodDependenciesFinder {
  @Builder
  @EqualsAndHashCode
  @ToString
  @AllArgsConstructor
  public static class MethodDependency {
    public final String qualifiedName;
    public final String methodName;
  }

  private static class MethodDependenciesVisitor extends ASTVisitor {
    private final Set<MethodDependency> methodCalls = new HashSet<>();

    public boolean visit(MethodInvocation node) {
      if (node.getExpression() != null) {
        ITypeBinding binding = node.getExpression().resolveTypeBinding();
        if (binding != null) {
          String classQualifiedName = binding.getQualifiedName();
          String methodName = node.getName().getIdentifier();
          methodCalls.add(MethodDependency.builder().qualifiedName(classQualifiedName).methodName(methodName).build());
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
