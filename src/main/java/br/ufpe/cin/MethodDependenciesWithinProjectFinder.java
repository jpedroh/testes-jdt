package br.ufpe.cin;

import java.io.File;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.javaparser.utils.CodeGenerationUtils;

import br.ufpe.cin.MethodDependenciesFinder.MethodDependency;

public class MethodDependenciesWithinProjectFinder {
  private String projectPath;

  public MethodDependenciesWithinProjectFinder(String projectPath) {
    this.projectPath = projectPath;
  }

  public Set<MethodDependency> getMethodDependenciesWithinProject(Set<MethodDependency> methodDependencies) {
    return methodDependencies
        .stream()
        .filter(methodDependency -> {
          String derivedFileName = CodeGenerationUtils.packageToPath(methodDependency.classQualifiedName) + ".java";
          return new File(Paths.get(projectPath, derivedFileName).toString()).exists();
        })
        .collect(Collectors.toSet());
  }

}
