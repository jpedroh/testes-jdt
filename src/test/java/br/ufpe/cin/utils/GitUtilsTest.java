package br.ufpe.cin.utils;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.submodule.SubmoduleWalk;
import org.junit.Test;

public class GitUtilsTest {
  @Test
  public void testFetchFileContentsInCommit() throws IOException {
    Path testingFilePath = Path.of("src/main/java/br/ufpe/cin/CadastroConta.java");
    String commitHash = "3628a703eef1f3ee04a59caafc34b0b31b32665a";

    String fileContents = GitUtils.fetchFileContentsInCommit(getTestingRepository(), testingFilePath, commitHash);

    assertEquals(loadFromFile(), fileContents);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFetchFileContentsInCommitNonExistantFile() throws IOException {
    Path testingFilePath = Path.of("src/main/java/br/ufpe/cin/NonExistant.java");
    String commitHash = "3628a703eef1f3ee04a59caafc34b0b31b32665a";

    String fileContents = GitUtils.fetchFileContentsInCommit(getTestingRepository(), testingFilePath, commitHash);

    assertEquals(loadFromFile(), fileContents);
  }

  private static Repository getTestingRepository() throws IOException {
    Repository mainRepository = GitUtils.getRepositoryFromProjectPath(Path.of("./"));
    return SubmoduleWalk.getSubmoduleRepository(mainRepository, "src/test/resources/toy-project");
  }

  public String loadFromFile() throws IOException {
    return new String(Files.readAllBytes(
        Paths.get("src/test/resources/file-contents/CadastroConta.3628a703eef1f3ee04a59caafc34b0b31b32665a.java")));
  }
}
