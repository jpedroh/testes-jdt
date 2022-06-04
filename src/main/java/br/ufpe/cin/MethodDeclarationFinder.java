package br.ufpe.cin;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

public class MethodDeclarationFinder {
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

  public MethodDeclaration getMethodBlockFromTree(CompilationUnit compilationUnit, String targetMethodName) {
    return compilationUnit
        .findAll(MethodDeclaration.class)
        .stream()
        .filter(md -> md.getName().toString().equals(targetMethodName))
        .findAny()
        .orElseThrow(() -> new MethodNotFoundException(targetMethodName));
  }
}
