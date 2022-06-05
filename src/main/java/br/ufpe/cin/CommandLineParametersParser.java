package br.ufpe.cin;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CommandLineParametersParser {
  public static class CommandLineParameters {
    private final Path projectPath;
    private final String qualifiedClassName;
    private final String targetMethodName;

    public CommandLineParameters(String projectPath, String qualifiedClassName, String targetMethodName) {
      this.projectPath = Paths.get(projectPath);
      this.qualifiedClassName = qualifiedClassName;
      this.targetMethodName = targetMethodName;
    }

    public Path getProjectPath() {
      return projectPath;
    }

    public String getQualifiedClassName() {
      return qualifiedClassName;
    }

    public String getTargetMethodName() {
      return targetMethodName;
    }
  }

  private final Options options;
  private final CommandLineParser parser;
  private final HelpFormatter formatter;

  private static final String projectPathTag = "p";
  private static final String qualifiedClassNameTag = "c";
  private static final String targetMethodNameTag = "m";
  private static final String helpTag = "h";

  public CommandLineParametersParser() {
    options = new Options();
    parser = new DefaultParser();
    formatter = new HelpFormatter();

    Option projectPathOption = Option.builder()
        .option(projectPathTag)
        .longOpt("projectPath")
        .hasArg()
        .desc("Project path")
        .required()
        .build();
    Option qualifiedClassNameOption = Option.builder()
        .option(qualifiedClassNameTag)
        .longOpt("qualifiedClassName")
        .hasArg()
        .desc("Target Class Name")
        .required()
        .build();
    Option targetMethodNameOption = Option.builder()
        .option(targetMethodNameTag)
        .longOpt("targetMethodName")
        .hasArg()
        .desc("Target Method Name")
        .required()
        .build();
    Option helpOption = Option.builder()
        .option(helpTag)
        .longOpt("help")
        .desc("Shows this help message")
        .build();

    options
        .addOption(projectPathOption)
        .addOption(qualifiedClassNameOption)
        .addOption(targetMethodNameOption)
        .addOption(helpOption);
  }

  public CommandLineParameters parse(String[] args) {
    try {
      CommandLine cmd = parser.parse(options, args);

      if (cmd.hasOption("help")) {
        formatter.printHelp("", options);
        return null;
      }

      return new CommandLineParameters(cmd.getOptionValue(projectPathTag), cmd.getOptionValue(qualifiedClassNameTag),
          cmd.getOptionValue(targetMethodNameTag));
    } catch (ParseException e) {
      formatter.printHelp("OSean.EX", options);
      throw new IllegalArgumentException(e.getMessage());
    }
  }
}
