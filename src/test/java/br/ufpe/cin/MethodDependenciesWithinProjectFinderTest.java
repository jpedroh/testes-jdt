package br.ufpe.cin;

import java.nio.file.Paths;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.github.javaparser.ast.body.MethodDeclaration;

import br.ufpe.cin.MethodDependenciesFinder.MethodDependency;

public class MethodDependenciesWithinProjectFinderTest {
  @Test
  public void onlyShowDependenciesWithinTheProvidedProject() {
    MethodDeclaration methodDeclaration = TestUtils.getMethodDeclaration();
    Set<MethodDependency> methodDependencies = new MethodDependenciesFinder().getMethodDependencies(methodDeclaration);
    MethodDependency projectDependency = new MethodDependency("br.ufpe.cin.Calculadora", "somar");

    Set<MethodDependency> filteredDependencies = new MethodDependenciesWithinProjectFinder(TestUtils.TEST_PROJECT_PATH)
        .getMethodDependenciesWithinProject(methodDependencies);

    Assert.assertEquals(1, filteredDependencies.size());
    Assert.assertTrue(filteredDependencies.contains(projectDependency));
  }
}
