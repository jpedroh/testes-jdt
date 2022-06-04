package br.ufpe.cin;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Set;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

import br.ufpe.cin.MethodDependenciesFinder.MethodDependency;

public class App {
  public static void main(String[] args) throws IOException, Exception {
    final String targetMethodName = "main";

    final CompilationUnit compilationUnit = new AstGenerator()
        .getAst(Paths.get("src/main/java"), "br.ufpe.cin", "App.java");

    final MethodDeclaration methodDeclaration = new MethodDeclarationFinder()
        .getMethodBlockFromTree(compilationUnit, targetMethodName);

    final Set<MethodDependency> methodDependencies = new MethodDependenciesFinder()
        .getMethodDependencies(methodDeclaration);

    final Set<MethodDependency> methodDependenciesWithinProject = new MethodDependenciesWithinProjectFinder("src/main/java")
        .getMethodDependenciesWithinProject(methodDependencies);

    methodDependenciesWithinProject.forEach(v -> {
      System.out.println(v.classQualifiedName);
      System.out.println(v.methodName);
    });
  }
}
