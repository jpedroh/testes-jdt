package br.ufpe.cin;

import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

import br.ufpe.cin.MethodDependenciesFinder.MethodDependency;
import br.ufpe.cin.utils.JavaProjectUtils;

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