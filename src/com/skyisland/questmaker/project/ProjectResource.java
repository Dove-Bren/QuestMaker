package com.skyisland.questmaker.project;

import java.io.File;
import java.io.IOException;

/**
 * A resourced kept and ultimately saved by the project
 * @author Skyler
 *
 */
public interface ProjectResource {

	void save(File saveFile) throws IOException;
	
}
