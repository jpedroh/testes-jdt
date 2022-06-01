package br.ufpe.cin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

/**
 * Hello world!
 *
 */
public class App 
{
  public static class MethodCall {
    String fqdn;
    String methodName;

    public MethodCall(String fqdn, String methodName) {
      this.fqdn = fqdn;
      this.methodName = methodName;
    }

    @Override
    public String toString() {
      return this.fqdn + "\t" + this.methodName;
    }

    @Override
    public boolean equals(Object obj) {
      if (!(obj instanceof MethodCall)) {
        return false;
      }
      return fqdn.equals(((MethodCall) obj).fqdn) && methodName.equals(((MethodCall) obj).methodName);
    }

    @Override
    public int hashCode() {
      return Objects.hash(fqdn, methodName);
    }
  }

  public static Set<MethodCall> methodCalls = new HashSet<>();

  public static void main(String[] args) throws IOException {
    final String javaCode = Files
        .readString(Paths.get(
            "//home//jpedroh//Projetos//cin//OSean.EX//src//main//java//org//serialization//ObjectSerializerGradle.java"));

    CompilationUnit compilationUnit = getCompilationUnit(javaCode);

    final String targetMethodName = "createBuildFileSupporters";

    compilationUnit.accept(new ASTVisitor() {
      public boolean visit(MethodDeclaration node) {
        if (!node.getName().toString().equals(targetMethodName)) {
          return true;
        }

        Block block = node.getBody();
        block.accept(new ASTVisitor() {
          public boolean visit(MethodInvocation node) {
            if (node.getExpression() != null) {
              ITypeBinding binding = node.getExpression().resolveTypeBinding();
              if (binding != null) {
                String classQualifiedName = binding.getQualifiedName();
                String methodName = node.getName().getIdentifier();
                methodCalls.add(new MethodCall(classQualifiedName, methodName));
              }
            }
            return true;
          }
        });
        return true;
      }
    });

    methodCalls.forEach((MethodCall v) -> System.out.println(v.toString()));
  }

  private static CompilationUnit getCompilationUnit(String fileContents) {
    org.eclipse.jface.text.Document document = new org.eclipse.jface.text.Document(fileContents);

    ASTParser parser = ASTParser.newParser(AST.JLS8);
    Map<String, String> options = JavaCore.getOptions(); // New!
    JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options); // New!
    parser.setCompilerOptions(options);
    parser.setEnvironment(
        new String[] { "//home//jpedroh//Projetos//cin//OSean.EX//target//classes" }, //
        new String[] { "//home//jpedroh//Projetos//cin//OSean.EX//src" },
        new String[] { "UTF-8" },
        true);
    parser.setUnitName("any_name");

    parser.setSource(document.get().toCharArray());
    parser.setKind(ASTParser.K_COMPILATION_UNIT);
    parser.setResolveBindings(true);
    parser.setBindingsRecovery(true);

    final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

    return cu;
    }
}
