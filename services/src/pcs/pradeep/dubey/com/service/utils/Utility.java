/**
 * 
 */
package pcs.pradeep.dubey.com.service.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * General Purpose utiliy class All method must be public and static
 * 
 * @author prdubey
 *
 */
public class Utility {

    public static List<String> listFilesForFolder(final File folder) {
	List<String> fileList = new ArrayList<String>();
	for (final File fileEntry : folder.listFiles()) {
	    if (fileEntry.isDirectory()) {
		listFilesForFolder(fileEntry);
	    } else {
		System.out.println(fileEntry.getName());
		fileList.add(fileEntry.getAbsolutePath());
	    }
	}
	return fileList;
    }

}
