package application;

import java.io.File;

import javafx.scene.control.TreeItem;

public class Utils {
	public static TreeItem<String> getDirectoryTree(File directory) {
		TreeItem<String> root = new TreeItem<String>(directory.getName());
		for(File f : directory.listFiles()) {
			if(f.isDirectory()) { 
				root.getChildren().add(getDirectoryTree(f));
			} else {
				root.getChildren().add(new TreeItem<String>(f.getName()));
			}
		}
		return root;
	}
}
