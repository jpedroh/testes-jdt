package br.ufpe.cin;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jgit.lib.Repository;

import br.ufpe.cin.CommandLineParametersParser.CommandLineParameters;
import br.ufpe.cin.MethodDependenciesFinder.MethodDependency;
import br.ufpe.cin.utils.GitUtils;

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

    final Repository repository = GitUtils
            .getRepositoryFromProjectPath(Path.of("/home/jpedroh/Projetos/cin/testes-jdt"));

    System.out.println(GitUtils.wasFileModifiedBetweenCommits(repository,
            Path.of("/home/jpedroh/Projetos/cin/testes-jdt/src/main/java/br/ufpe/cin/App.java"),
            "92e7317db520bb326216522b016acf99605dd173", "b81c97399c99d76190f2a977d7ae0b70b0e0d6e3"));
    System.out
            .println(GitUtils.wasFileModifiedBetweenCommits(repository,
                    Path.of("/home/jpedroh/Projetos/cin/testes-jdt/.gitignore"),
                    "92e7317db520bb326216522b016acf99605dd173", "b81c97399c99d76190f2a977d7ae0b70b0e0d6e3"));
  }
}
