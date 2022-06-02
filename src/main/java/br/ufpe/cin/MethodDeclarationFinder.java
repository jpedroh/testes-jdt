package br.ufpe.cin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

  private final MethodVisitor visitor;

  public MethodDeclarationFinder() {
    this.visitor = new MethodVisitor();
  }

  public Optional<MethodDeclaration> getMethodBlockFromTree(CompilationUnit cu, String targetMethodName) {
    cu.accept(visitor);

    return Optional.ofNullable(visitor.getMethods().get(targetMethodName));
  }
}
