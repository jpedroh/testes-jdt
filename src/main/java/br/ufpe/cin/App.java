package br.ufpe.cin;

import java.io.IOException;
import java.util.Set;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import br.ufpe.cin.MethodDependenciesFinder.MethodDependency;

public class App {
  public static void main(String[] args) throws IOException {
    CompilationUnit compilationUnit = new ProjectAstGenerator().getProjectAst(
        new String[] { "//home//jpedroh//Projetos//cin//OSean.EX//target//classes" },
        new String[] { "//home//jpedroh//Projetos//cin//OSean.EX//src" },
        "//home//jpedroh//Projetos//cin//OSean.EX//src//main//java//org//serialization//ObjectSerializerGradle.java");

    final String targetMethodName = "createBuildFileSupporters";

    // final MethodDeclaration methodDeclaration = new MethodDeclarationFinder()
    // .getMethodBlockFromTree(compilationUnit, targetMethodName);

    // final Set<MethodDependency> methodDependencies = new
    // MethodDependenciesFinder()
    // .getMethodDependencies(methodDeclaration);

    // methodDependencies.forEach((MethodDependency v) ->
    // System.out.println(v.toString()));
  }
}
