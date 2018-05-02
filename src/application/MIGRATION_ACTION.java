package application;

import java.util.ArrayList;
import java.util.List;

public enum MIGRATION_ACTION {
	LOAD_SVN_AUTHORS,
	CLONE_SVN_REPO,
	RESUME_CLONE,
	CLEANUP,
	SYNCHRONIZE_SVN_AND_GIT,
	PUSH_TO_REMOTE_GIT;
	public static List<String> getListOfActions(){
		List<String> actions=new ArrayList<>();
		for(MIGRATION_ACTION action:MIGRATION_ACTION.values()) {
			actions.add(action.name());
		}
		return actions;
	}
}
