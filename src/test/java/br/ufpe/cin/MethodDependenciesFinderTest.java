package br.ufpe.cin;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.github.javaparser.ast.body.MethodDeclaration;

import br.ufpe.cin.MethodDependenciesFinder.MethodDependency;

public class MethodDependenciesFinderTest {
  @Test
  public void returnsTheDependenciesOfMethod() {
    MethodDeclaration methodDeclaration = TestUtils.getMethodDeclaration();
    MethodDependency expectedDependency = new MethodDependency("br.ufpe.cin.Calculadora", "somar");

    Set<MethodDependency> methodDependencies = new MethodDependenciesFinder().getMethodDependencies(methodDeclaration);

    Assert.assertEquals(1, methodDependencies.size());
    Assert.assertTrue(methodDependencies.contains(expectedDependency));
  }
}
