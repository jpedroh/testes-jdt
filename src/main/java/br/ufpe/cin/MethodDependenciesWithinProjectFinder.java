package br.ufpe.cin;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

import br.ufpe.cin.MethodDependenciesFinder.MethodDependency;

public class MethodDependenciesWithinProjectFinder {
  private Path projectPath;

  public MethodDependenciesWithinProjectFinder(Path projectPath) {
    this.projectPath = projectPath;
  }

  public Set<MethodDependency> getMethodDependenciesWithinProject(Set<MethodDependency> methodDependencies) {
    return methodDependencies
        .stream()
        .filter(methodDependency -> {
          final Path filePath = JavaProjectUtils.qualifiedNameToPath(methodDependency.qualifiedName);
          return projectPath.resolve(filePath).toFile().exists();
        })
        .collect(Collectors.toSet());
  }
}