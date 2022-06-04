package br.ufpe.cin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Set;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.SourceRoot;

import br.ufpe.cin.MethodDependenciesFinder.MethodDependency;

public class App {

  private static ParserConfiguration createParserConfiguration() throws Exception {
    TypeSolver typeSolver = new CombinedTypeSolver(
        new ReflectionTypeSolver(false),
        new JavaParserTypeSolver(new File("src/main/java")));

    return new ParserConfiguration().setSymbolResolver(new JavaSymbolSolver(typeSolver));
  }

  public static void main(String[] args) throws IOException, Exception {
    CompilationUnit compilationUnit = new SourceRoot(Paths.get("src/main/java"),
        createParserConfiguration())
        .parse("br.ufpe.cin", "App.java");

    final String targetMethodName = "main";

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
