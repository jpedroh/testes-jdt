package br.ufpe.cin;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

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

    final Repository repository = GitUtils.getRepositoryFromProjectPath(parameters.projectPath);

    final Set<MethodDependency> dependenciesModifiedInCommit = methodDependenciesWithinProject
            .stream()
            .filter(dependency -> {
                final Path filePath = JavaProjectUtils.qualifiedNameToPath(dependency.qualifiedName);

                return GitUtils.wasFileModifiedBetweenCommits(repository, filePath, parameters.leftCommitHash, parameters.rightCommitHash);
            }).collect(Collectors.toSet());

    dependenciesModifiedInCommit.forEach((MethodDependency v) -> System.out.println(v.toString()));
  }
}
