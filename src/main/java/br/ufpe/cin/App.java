package br.ufpe.cin;

import java.io.IOException;
import java.util.Set;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

import br.ufpe.cin.CommandLineParametersParser.CommandLineParameters;
import br.ufpe.cin.MethodDependenciesFinder.MethodDependency;

public class App {
  public static void main(String[] args) throws IOException, Exception {
    final CommandLineParametersParser parser = new CommandLineParametersParser();
    final CommandLineParameters params = parser.parse(args);

    final CompilationUnit compilationUnit = new AstGenerator()
        .getAstForClass(params.getProjectPath(), params.getQualifiedClassName());

    final MethodDeclaration methodDeclaration = new MethodDeclarationFinder()
        .getMethodBlockFromTree(compilationUnit, params.getTargetMethodName());

    final Set<MethodDependency> methodDependencies = new MethodDependenciesFinder()
        .getMethodDependencies(methodDeclaration);

    final Set<MethodDependency> methodDependenciesWithinProject = new MethodDependenciesWithinProjectFinder(params.getProjectPath())
        .getMethodDependenciesWithinProject(methodDependencies);

    methodDependenciesWithinProject.forEach(v -> {
      System.out.println(v.classQualifiedName);
      System.out.println(v.methodName);
    });
  }
}
