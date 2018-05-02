package application;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;

import javafx.scene.control.TextArea;

public class MigrationCommandUtils {
	private boolean isWindows=System.getProperty("os.name").toLowerCase().startsWith("windows");
	private MigrationController migrationCtrl;
	private MigrationInfo migrationInfo;
	
	public MigrationCommandUtils(MigrationController migrationController) {
		migrationCtrl=migrationController;
		migrationInfo=migrationCtrl.getMigrationInfo();
		isWindows=System.getProperty("os.name").toLowerCase().startsWith("windows");
	}

	public int cloneCommand(TextArea processOutputTextArea) {
		ProcessBuilder builder = new ProcessBuilder();
		processOutputTextArea.appendText("cloning...");
		
		String repoUrl=migrationInfo.getSvnServerUrl()+migrationInfo.getSvnRepoName();
		String projectName=migrationInfo.getSvnRepoName();
		
		String cloneDirectory=migrationInfo.getMigrationDirectory()+"/"+migrationInfo.getSvnRepoName();
		String authors=migrationInfo.getMigrationDirectory()+"/authors.txt";
		
		String cloneCommand="git svn clone --trunk=/trunk --branches=/branches --branches=/bugfixes --tags=/tags "
				+ "--authors-file="+authors + " "+repoUrl +"  "+projectName;
		if (isWindows) {
			builder=getWindowsProcessBuilder(cloneDirectory,cloneCommand);

		} else {
			builder=getNonWindowsProcessBuilder(cloneDirectory,cloneCommand);
		}

		int exitCode=Integer.MAX_VALUE;

		try {
			Process process = builder.start();
			Worker worker = new Worker(process.getInputStream(),processOutputTextArea);
			
			Executors.newSingleThreadExecutor().submit(worker);

			exitCode = process.waitFor();
		}catch(Exception e) {
			processOutputTextArea.appendText("error in clone command: "+e.getMessage());
		}
		migrationCtrl.directoryView1.setRoot(Utils.getDirectoryTree(new File(migrationInfo.getMigrationDirectory()+"/"+migrationInfo.getSvnRepoName())));
		return exitCode;
	}

	public int initProjectDirectoryCommand() {
		int exitCode=Integer.MAX_VALUE;
		
		String fullPath=migrationInfo.getMigrationDirectory()+"/"+migrationInfo.getSvnRepoName();
		Path path=Paths.get(fullPath);
		String command="mkdir "+path;
		
		migrationCtrl.getProcessOutputTextArea().appendText("\n project/directory init command:"+command);
		try {

			ProcessBuilder builder=new ProcessBuilder();
			if (isWindows) {
				builder=getWindowsProcessBuilder(migrationInfo.getMigrationDirectory(),command);

			} else {
				builder=getNonWindowsProcessBuilder(migrationInfo.getMigrationDirectory(),command);
			}
			
			Process process1 = builder.start();
			Worker worker = new Worker(process1.getInputStream(),migrationCtrl.getProcessOutputTextArea());

			Executors.newSingleThreadExecutor().submit(worker);

			exitCode = process1.waitFor();
		}catch(Exception e) {
			migrationCtrl.getProcessOutputTextArea().appendText(e.getMessage()+"\n");
		}
		return exitCode;
	}
	public ProcessBuilder getWindowsProcessBuilder(String workingDirectory,String command) {

		ProcessBuilder builder = new ProcessBuilder();
		boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
		if (isWindows) {
			builder.command("cmd.exe", "/c", command);

		}
		builder.directory(new File(workingDirectory));

		return builder;

	}
	public ProcessBuilder getNonWindowsProcessBuilder(String workingDirectory,String command) {

		ProcessBuilder builder = new ProcessBuilder();
		boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
		if (!isWindows) {
			builder.command("sh", "-c", command);

		}
		
		builder.directory(new File(workingDirectory));

		return builder;

	}
}
