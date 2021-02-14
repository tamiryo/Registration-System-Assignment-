package bgu.spl.net.impl.BGRSServer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * Passive object representing the Database where all courses and users are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add private fields and public methods to this class as you see fit.
 */
public class Database {

	public Object getKey() {
		return key;
	}

	private static class DatabaseHolder {
		private static Database instance= new Database();
	}

	private ConcurrentHashMap<Short, Course> courseNumMap;
	private ConcurrentHashMap<String,Course> courseNameMap;
	private ConcurrentHashMap<String,User> userMap;
	private Object key = new Object();


	//to prevent user from creating new Database
	private Database() {
		courseNumMap = new ConcurrentHashMap<Short,Course>();
		courseNameMap = new ConcurrentHashMap<String,Course>();
		userMap = new ConcurrentHashMap<String,User>();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Database getInstance() {
		return DatabaseHolder.instance;
	}
	

	/**
	 * loades the courses from the file path specified 
	 * into the Database, returns true if successful.
	 */
	boolean initialize(String coursesFilePath) {

		if (courseNumMap.isEmpty()) {
			try {
				List<String> strings = Files.readAllLines(Paths.get(coursesFilePath));
				int index = 1;
				for (String line : strings) {
					String[] split = line.split("[|]");
					Short courseNum = Short.parseShort(split[0]);
					String courseName = split[1];
					int[] kdamCourseList;
					if (split[2].equals("[]")) {
						kdamCourseList = new int[0];
					} else
						kdamCourseList = Stream.of(split[2].substring(1, split[2].length() - 1).split(",")).mapToInt(Integer::parseInt).toArray();
					int numOfMaxStudents = Integer.parseInt(split[3]);

					Course newCourse = new Course(courseNum, courseName, kdamCourseList, numOfMaxStudents,index);
					courseNumMap.putIfAbsent(courseNum, newCourse);
					courseNameMap.putIfAbsent(courseName, newCourse);
					index = index + 1;
				}
			} catch (IOException e) {
				return false;
			}
			return !courseNumMap.isEmpty();
		}
		else
			return false;
	}

	public ConcurrentHashMap<Short, Course> getCourseNumMap() {
		return courseNumMap;
	}

	public ConcurrentHashMap<String, Course> getCourseNameMap() {
		return courseNameMap;
	}

	public ConcurrentHashMap<String, User> getUserMap() {
		return userMap;
	}

}
