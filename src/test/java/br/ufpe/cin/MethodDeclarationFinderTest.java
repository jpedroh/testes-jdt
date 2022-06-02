package br.ufpe.cin;

import java.util.Optional;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.junit.Assert;
import org.junit.Test;

public class MethodDeclarationFinderTest {
  @Test
  public void findsExistingMethod() {
    CompilationUnit cu = compileTestingCode();

    MethodDeclarationFinder methodDeclarationFinder = new MethodDeclarationFinder();
    Optional<MethodDeclaration> methodDeclaration = methodDeclarationFinder.getMethodBlockFromTree(cu, "main");

    Assert.assertNotNull(methodDeclaration.get());
  }

  @Test
  public void doNotFindNonExistingMethod() {
    CompilationUnit cu = compileTestingCode();

    MethodDeclarationFinder methodDeclarationFinder = new MethodDeclarationFinder();
    Optional<MethodDeclaration> methodDeclaration = methodDeclarationFinder.getMethodBlockFromTree(cu, "nonExisting");

    Assert.assertTrue(methodDeclaration.isEmpty());
  }

  private CompilationUnit compileTestingCode() {
    final String source = String.join("\n",
        "public class App {",
        "public static void main() {",
        "System.out.println(\"Hello, world!\")",
        "}",
        "}");

    ASTParser parser = ASTParser.newParser(AST.JLS8);
    JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, JavaCore.getOptions());
    parser.setCompilerOptions(JavaCore.getOptions());
    parser.setUnitName("testing_jdt");
    parser.setSource(source.toCharArray());
    parser.setKind(ASTParser.K_COMPILATION_UNIT);

    return (CompilationUnit) parser.createAST(null);
  }
}
