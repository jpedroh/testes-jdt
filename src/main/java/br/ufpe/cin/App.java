package br.ufpe.cin;

import java.io.IOException;
import java.util.Set;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import br.ufpe.cin.CommandLineParametersParser.CommandLineParameters;
import br.ufpe.cin.MethodDependenciesFinder.MethodDependency;

public class App {
  public static void main(String[] args) throws IOException {
    final CommandLineParameters parameters = new CommandLineParametersParser().parse(args);

    final CompilationUnit compilationUnit = new ProjectAstGenerator()
        .getProjectAst(parameters.classTargetPath, parameters.sourcePath, parameters.targetClassPath);

    final MethodDeclaration methodDeclaration = new MethodDeclarationFinder()
        .getMethodBlockFromTree(compilationUnit, parameters.targetMethodName);

    final Set<MethodDependency> methodDependencies = new MethodDependenciesFinder()
        .getMethodDependencies(methodDeclaration);

    final Set<MethodDependency> methodDependenciesWithinProject = new MethodDependenciesWithinProjectFinder(parameters.sourcePath)
        .getMethodDependenciesWithinProject(methodDependencies);

    methodDependenciesWithinProject.forEach((MethodDependency v) -> System.out.println(v.toString()));
  }
}
