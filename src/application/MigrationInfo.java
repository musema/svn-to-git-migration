package application;

public class MigrationInfo {
	private String mapperFile;
	private String migrationDirectory;
	private String svnRepoName;
	private String gitRepoUrl;
	private MIGRATION_ACTION migrationAction;
	
	private String svnServerUrl;
	private String svnUsername;
	private String svnPassword;
	
	private String gitUsername;
	private String gitPassword;
	public String getMapperFile() {
		return mapperFile;
	}
	public void setMapperFile(String mapperFile) {
		this.mapperFile = mapperFile;
	}
	public String getMigrationDirectory() {
		return migrationDirectory;
	}
	public void setMigrationDirectory(String migrationDirectory) {
		this.migrationDirectory = migrationDirectory;
	}
	public String getSvnRepoName() {
		return svnRepoName;
	}
	public void setSvnRepoName(String svnRepoName) {
		this.svnRepoName = svnRepoName;
	}
	public String getGitRepoUrl() {
		return gitRepoUrl;
	}
	public void setGitRepoUrl(String gitRepoUrl) {
		this.gitRepoUrl = gitRepoUrl;
	}
	public MIGRATION_ACTION getMigrationAction() {
		return migrationAction;
	}
	public void setMigrationAction(MIGRATION_ACTION migrationAction) {
		this.migrationAction = migrationAction;
	}
	public String getSvnServerUrl() {
		return svnServerUrl;
	}
	public void setSvnServerUrl(String svnServerUrl) {
		this.svnServerUrl = svnServerUrl;
	}
	public String getSvnUsername() {
		return svnUsername;
	}
	public void setSvnUsername(String svnUsername) {
		this.svnUsername = svnUsername;
	}
	public String getSvnPassword() {
		return svnPassword;
	}
	public void setSvnPassword(String svnPassword) {
		this.svnPassword = svnPassword;
	}
	public String getGitUsername() {
		return gitUsername;
	}
	public void setGitUsername(String gitUsername) {
		this.gitUsername = gitUsername;
	}
	public String getGitPassword() {
		return gitPassword;
	}
	public void setGitPassword(String gitPassword) {
		this.gitPassword = gitPassword;
	}
	
	@Override
	public String toString() {

		StringBuilder sb=new StringBuilder();
		sb.append("MapperFile="+mapperFile+"\n");
		sb.append("MigrationDirectory="+migrationDirectory+"\n");
		sb.append("SvnRepoName="+svnRepoName+"\n");
		sb.append("GitRepoUrl="+gitRepoUrl+"\n");
		sb.append("MigrationAction="+migrationAction+"\n");
		sb.append("SvnServerUrl="+svnServerUrl+"\n");
		sb.append("SvnUsername="+svnUsername+"\n");
		sb.append("GitUsername="+gitUsername+"\n");
		sb.append("GitPassword="+gitPassword+"\n");
		
		return sb.toString();
	}
	

}
