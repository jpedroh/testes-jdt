package br.ufpe.cin.utils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

public class GitUtils {
  public static String fetchFileContentsInCommit(Repository repository, Path relativeFilePath, String commitHash) {
    try {
      RevWalk revWalk = new RevWalk(repository);
      RevCommit commit = revWalk.parseCommit(ObjectId.fromString(commitHash));
      RevTree tree = commit.getTree();
      TreeWalk treeWalk = new TreeWalk(repository);
      treeWalk.addTree(tree);
      treeWalk.setRecursive(true);
      treeWalk.setFilter(PathFilter.create(relativeFilePath.toString()));
      if (!treeWalk.next()) {
        treeWalk.close();
        revWalk.close();
        throw new IllegalArgumentException("File with path " + relativeFilePath + " was not found on the tree.");
      }
      ObjectId objectId = treeWalk.getObjectId(0);
      ObjectLoader loader = repository.open(objectId);
      treeWalk.close();
      revWalk.close();

      return new String(loader.getBytes());
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static boolean wasFileModifiedBetweenCommits(Repository repo, Path filePath, String newHash, String oldHash) {
    try {
      Git git = new Git(repo);
      List<DiffEntry> result = git.diff()
          .setNewTree(prepareTreeParser(repo, newHash))
          .setOldTree(prepareTreeParser(repo, oldHash))
          .call();
      git.close();

      return result.stream()
          .anyMatch(entry -> {
            return entry.getNewPath().equals(filePath.toString()) || entry.getOldPath().equals(filePath.toString());
          });
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  private static AbstractTreeIterator prepareTreeParser(Repository repository, String objectId) throws IOException {
    try (RevWalk walk = new RevWalk(repository)) {
      RevCommit commit = walk.parseCommit(repository.resolve(objectId));
      RevTree tree = walk.parseTree(commit.getTree().getId());

      CanonicalTreeParser treeParser = new CanonicalTreeParser();
      try (ObjectReader reader = repository.newObjectReader()) {
        treeParser.reset(reader, tree.getId());
      }

      walk.dispose();

      return treeParser;
    }
  }

  public static Repository getRepositoryFromProjectPath(Path projectPath) {
    try {
      FileRepositoryBuilder builder = new FileRepositoryBuilder();
      return builder.setGitDir(projectPath.resolve(".git").toFile())
          .readEnvironment()
          .findGitDir()
          .build();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}
