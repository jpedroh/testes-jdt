package br.ufpe.cin;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class MethodDeclarationFinder {
  private class MethodVisitor extends ASTVisitor {
    Map<String, MethodDeclaration> methods = new HashMap<>();

    @Override
    public boolean visit(MethodDeclaration node) {
      methods.put(node.getName().toString(), node);
      return super.visit(node);
    }

    public Map<String, MethodDeclaration> getMethods() {
      return methods;
    }
  }

  public static class MethodNotFoundException extends IllegalArgumentException {
    private final String targetMethodName;

    public MethodNotFoundException(String targetMethodName) {
      super("Invalid method provided. Could not find method " + targetMethodName + " in source code.");
      this.targetMethodName = targetMethodName;
    }

    public String getTargetMethodName() {
      return targetMethodName;
    }
  }

  private final MethodVisitor visitor;

  public MethodDeclarationFinder() {
    this.visitor = new MethodVisitor();
  }

  public MethodDeclaration getMethodBlockFromTree(CompilationUnit cu, String targetMethodName) {
    cu.accept(visitor);

    if (!visitor.getMethods().containsKey(targetMethodName)) {
      throw new MethodNotFoundException(targetMethodName);
    }

    return visitor.getMethods().get(targetMethodName);
  }
}
