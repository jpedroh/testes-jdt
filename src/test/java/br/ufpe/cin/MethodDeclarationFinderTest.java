package br.ufpe.cin;

import org.junit.Assert;
import org.junit.Test;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

import br.ufpe.cin.MethodDeclarationFinder.MethodNotFoundException;

public class MethodDeclarationFinderTest {
  @Test
  public void findsExistingMethod() {
    CompilationUnit cu = compileTestingCode();

    MethodDeclarationFinder methodDeclarationFinder = new MethodDeclarationFinder();
    MethodDeclaration methodDeclaration = methodDeclarationFinder.getMethodBlockFromTree(cu, "main");

    Assert.assertNotNull(methodDeclaration);
  }

  @Test(expected = MethodNotFoundException.class)
  public void doNotFindNonExistingMethod() {
    CompilationUnit cu = compileTestingCode();

    MethodDeclarationFinder methodDeclarationFinder = new MethodDeclarationFinder();
    methodDeclarationFinder.getMethodBlockFromTree(cu, "nonExisting");
  }

  private CompilationUnit compileTestingCode() {
    final String source = String.join("\n",
        "public class App {",
        "public static void main() {",
        "System.out.println(\"Hello, world!\");",
        "}",
        "}");

    return StaticJavaParser.parse(source);
  }
}
