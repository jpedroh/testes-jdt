package br.ufpe.cin;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.javaparser.utils.CodeGenerationUtils;

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
          String derivedFileName = CodeGenerationUtils.packageToPath(methodDependency.classQualifiedName) + ".java";
          return projectPath.resolve(derivedFileName).toFile().exists();
        })
        .collect(Collectors.toSet());
  }
}
