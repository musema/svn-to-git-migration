package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MigrationController implements Initializable{

	@FXML
	private GridPane mainGrid;

	@FXML
	private TextArea mapperFileContent;

	@FXML
	private TextArea processOutputTextArea;

	@FXML
	private Button chooseMapperFileBtn;

	@FXML
	private Button chooseMigrationFolderBtn;

	@FXML
	private ComboBox<String> chooseSvnRepoSeletBox;

	@FXML
	private ComboBox<String> chooseMigrationActionSelectBox;

	@FXML
	private Label selectedMapperFile;

	@FXML
	private Label selectedMigrationDirectory;

	@FXML
	private Label selectedSvnRepo;

	@FXML
	private TextField selectedGitRepo;

	@FXML
	private Label selectedAction;

	@FXML
	private TextField progressTextArea;

	Map<String,String> repositoryMap=new HashMap<String,String>();

	@FXML
	private TextField svnServerUrl;

	@FXML
	private TextField svnUsername;

	@FXML
	private PasswordField svnPassword;

	@FXML
	TreeView<String> directoryView1;

	private MigrationInfo migrationInfo=new MigrationInfo();
	
	public TextArea getProcessOutputTextArea() {
		return processOutputTextArea;
	}
	public void setProcessOutputTextArea(TextArea processOutputTextArea) {
		this.processOutputTextArea = processOutputTextArea;
	}
	public TreeView<String> getDirectoryView1() {
		return directoryView1;
	}
	public void setDirectoryView1(TreeView<String> directoryView1) {
		this.directoryView1 = directoryView1;
	}
	public MigrationInfo getMigrationInfo() {
		return migrationInfo;
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		chooseMigrationActionSelectBox.getItems()
		.addAll(MIGRATION_ACTION.getListOfActions());
		selectedMigrationDirectory.setText(System.getProperty("user.home"));
	}
	@FXML
	public void browseMapperFileAction(ActionEvent event) {
		FileChooser fileChooser=new FileChooser();
		fileChooser.setTitle("Choose SVN and Git Repo Mapper");
		Stage stage=(Stage) mainGrid.getScene().getWindow();

		File mapperFile=fileChooser.showOpenDialog(stage);

		if(mapperFile!=null) {
			selectedMapperFile.setText(mapperFile.getAbsolutePath());
			
			
			System.out.println("file is found");
			System.out.println(mapperFile);
			try {
				readFile(mapperFile);
			}catch(Exception e) {
				System.out.println("Exception!!"+e.getMessage()+e.getCause());
			}

		}

		chooseSvnRepoSeletBox.getItems().clear();
		for(Entry<String, String> repo:repositoryMap.entrySet()) {
			chooseSvnRepoSeletBox.getItems().add(repo.getKey());
		}
		/*
		 * load the svn credentials
		 */
		Properties prop=new Properties();
		InputStream input=null;

		String confDir;
		if(mapperFile!=null) {
			confDir=mapperFile.getParent();
			try {
				input=new FileInputStream((confDir+"/conf.properties"));
				prop.load(input);
				svnServerUrl.setText(prop.getProperty("svn_server_url","eneter svn server url"));
				svnUsername.setText(prop.getProperty("svn_username","enter svn username here"));
				svnPassword.setText(prop.getProperty("svn_password","eneter svn password here"));
			}catch(Exception e) {
				System.out.println("error while loading propertes: "+e);
			}
		}


	}

	@FXML 
	private void openMigrationDirectory() {
		try {
			DirectoryChooser dc = new DirectoryChooser();
			dc.setTitle("Select Migration Directory");
			File desktop = new File(System.getProperty("user.home") + File.separator + "Desktop");

			Stage stage=(Stage)chooseMigrationFolderBtn.getScene().getWindow();

			if (desktop != null) dc.setInitialDirectory(desktop);

			File migrationFolder = dc.showDialog(stage);

			if (migrationFolder != null) {
				selectedMigrationDirectory.setText(migrationFolder.getAbsolutePath());
			}
		} catch (Exception e) {
			progressTextArea.appendText(e.getMessage());
		}
	}

	@FXML
	public void onChooseSvnRepoSeletBoxChanges() {
		selectedSvnRepo.setText(chooseSvnRepoSeletBox.getValue());
		selectedGitRepo.setText(repositoryMap.get(chooseSvnRepoSeletBox.getValue()));
	}
	@FXML
	public void onMigrationActionChanges() {
		selectedAction.setText(chooseMigrationActionSelectBox.getValue());
		System.out.println("action"+chooseMigrationActionSelectBox.getValue());
	}
	public void readFile(File file) {

		StringBuilder sb = new StringBuilder();
		BufferedReader br=null;
		try {
			br=new BufferedReader(new FileReader(file));

			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append("\n");

				String[] str=line.split("=");
				if(str.length==1) {
					repositoryMap.put(str[0].trim(), "NO_GIT_REPO_MAPPED");
				}
				else if(str.length==2)
					repositoryMap.put(str[0].trim(), str[1].trim());
				line = br.readLine();
			}

		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		mapperFileContent.setText(sb.toString());
		System.out.println(sb.toString());
	}

	public void listHomeDirectory() {

	}
	@FXML
	public void startMigrationAction() {
		migrationInfo.setMapperFile(selectedMapperFile.getText());
		migrationInfo.setMigrationDirectory(selectedMigrationDirectory.getText());
		migrationInfo.setSvnRepoName(selectedSvnRepo.getText());
		migrationInfo.setGitRepoUrl(selectedGitRepo.getText());
		migrationInfo.setMigrationAction(MIGRATION_ACTION.valueOf(selectedAction.getText()));
		
		migrationInfo.setSvnServerUrl(svnServerUrl.getText());
		migrationInfo.setSvnUsername(svnUsername.getText());
		migrationInfo.setSvnPassword(svnPassword.getText());
				
		processOutputTextArea.clear();
		processOutputTextArea.appendText(migrationInfo.toString());
		processOutputTextArea.appendText("\n migration action selected:"+selectedAction.getText());

		MigrationCommandUtils commandUtils=new MigrationCommandUtils(this);
		
		String workingDirectory=selectedMigrationDirectory.getText().trim();
		processOutputTextArea.appendText("\n working directory will be:"+workingDirectory);

		String selectedProjectName=selectedSvnRepo.getText().equalsIgnoreCase("NOT_SELECTED")?"Project"+Math.random()*100:selectedSvnRepo.getText();
		/*
		 * check if the directory exists
		 */
		int exitCode=Integer.MAX_VALUE;
		
		Path projectPath=Paths.get(workingDirectory+"/"+selectedProjectName);
		if(!isProjectExists(projectPath)) {
			processOutputTextArea.appendText(projectPath+"isn't found in the path, initializing for the first time\n");
			exitCode=commandUtils.initProjectDirectoryCommand();
			if(exitCode!=0) {
				//return;
			}
		}
		else {
			processOutputTextArea.appendText("this project is found in the path\n");
		}

		exitCode=commandUtils.cloneCommand(processOutputTextArea);
		
		String fullPath=workingDirectory+"/"+selectedProjectName;
		TreeItem<String> view1=Utils.getDirectoryTree(new File(fullPath));
		directoryView1.setRoot(view1);

	}

	private boolean isProjectExists(Path projectPath) {
		return Files.exists(projectPath);
	}
	

}
