package br.ufpe.cin;

import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.github.javaparser.ast.body.MethodDeclaration;

import br.ufpe.cin.MethodDependenciesFinder.MethodDependency;

public class MethodDependenciesFinderTest {
  @Test
  public void returnsTheDependenciesOfMethod() {
    MethodDeclaration methodDeclaration = TestUtils.getMethodDeclaration();
    MethodDependency projectDependency = new MethodDependency("br.ufpe.cin.Calculadora", "somar");
    MethodDependency javaDependency = new MethodDependency("java.io.PrintStream", "println");

    Set<MethodDependency> methodDependencies = new MethodDependenciesFinder().getMethodDependencies(methodDeclaration);

    methodDependencies.forEach(v -> System.out.println(v.toString()));

    Assert.assertEquals(2, methodDependencies.size());
    Assert.assertTrue(methodDependencies.containsAll(List.of(projectDependency, javaDependency)));
  }
}
