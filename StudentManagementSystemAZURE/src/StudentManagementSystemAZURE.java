import javax.swing.*;
import javax.swing.table.JTableHeader;

import com.mysql.cj.jdbc.CallableStatement;

import java.util.*;
import java.util.function.Function;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class MyGenericList<T extends Comparable<T>> {
	private class Node<T> {
		T value;
		Node<T> next;

		Node() {

		}

		Node(T value, Node<T> n) {
			this.value = value;
			this.next = n;
		}

	}

	private Node<T> first = null;
	int count = 0;

	public void add(T element) {
		Node<T> newnode = new Node<T>();
		newnode.value = element;
		newnode.next = null;

		if (first == null) {
			first = newnode;
		} else {
			Node<T> lastnode = gotolastnode(first);
			lastnode.next = newnode;
		}
		count++;
	}

	public void overwrite(int index, T element) {
		int counter = 0;
		Node<T> nodepointer = first;
		while (counter != index) {
			nodepointer = nodepointer.next;
			counter++;
		}
		nodepointer.value = element;
	}

	public T get(int pos) {
		Node<T> Nodeptr = first;
		int hopcount = 0;
		while (hopcount < count && hopcount < pos) {
			if (Nodeptr != null) {
				Nodeptr = Nodeptr.next;
			}
			hopcount++;
		}
		return Nodeptr.value;
	}

	private Node<T> gotolastnode(Node<T> nodepointer) {
		if (nodepointer == null) {
			return nodepointer;
		} else {
			if (nodepointer.next == null)
				return nodepointer;
			else
				return gotolastnode(nodepointer.next);

		}

	}

	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		MyGenericList<T>.Node<T> p = first;
		while (p != null) {
			strBuilder.append(p.value + "\n");
			p = p.next;
		}
		return strBuilder.toString();
	}

	public int size() {
		int count = 0;
		MyGenericList<T>.Node<T> p = first;
		while (p != null) {
			count++;
			p = p.next;
		}
		return count;
	}

	public void remove(int index) {
		if (index < 0 || index >= size()) {
			String message = String.valueOf(index);
			throw new IndexOutOfBoundsException(message);
		}

		if (index == 0) {
			first = first.next;
		} else {
			Node<T> pred = first;
			for (int k = 1; k <= index - 1; k++)
				pred = pred.next;
			pred.next = pred.next.next;
		}
	}

}

class Student implements Comparable<Student> {
	private int ID;
	private String firstName;
	private String lastName;
	private String address;
	private String city;
	private String state;

	public Student() {
		ID = 1000; // never used but offset to eliminate problems
		firstName = "";
		lastName = "";
		address = "";
		city = "";
		state = "";
	}

	public Student(String firstName, String lastName, String address, String city, String state) {// to create object
		this.ID = 1000; // never used but offset to eliminate problems
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.state = state;
	}

	public Student(int ID, String firstName, String lastName, String address, String city, String state) {// to set
																											// correct
																											// ID number
																											// //
																											// reconstruct
																											// // object
		this.ID = ID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.state = state;
	}

	public int getID() {
		return this.ID;
	}

	public String getFirstName() { // ADDED A TRIM METHOD SEE HOW THAT EFFECTS OUTPUT
		return this.firstName.trim();
	}

	public String getLastName() {
		return this.lastName.trim();
	}

	public String getAddress() {
		return this.address.trim();
	}

	public String getCity() {
		return this.city.trim();
	}

	public String getState() {
		return this.state.trim();
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void printStudent() {
		System.out.println("Student ID: " + this.ID);
		System.out.println("First: " + this.firstName);
		System.out.println("Last: " + this.lastName);
		System.out.println("Address: " + this.address);
		System.out.println("City: " + this.city);
		System.out.println("State: " + this.state);
	}

	public String toString() {
		return "SID: " + ID + "\n" + "First Name: " + firstName + "\n" + "Last Name: " + lastName + "\n" + "Address: "
				+ address + "\n" + "City: " + city + "\n" + "State: " + state + "\n";
	}

	@Override
	public int compareTo(Student o) {
		Integer thisID = new Integer(this.getID());
		Integer compareID = new Integer(o.getID());
		return (thisID.compareTo(compareID));
	}
}

class StudentFile {
	private final int RECORD_SIZE = 168; // ID = 4, firstName = 30, lastName = 40, address = 60, city = 30, state = 4
	private RandomAccessFile studentFile;

	public StudentFile(String filename) throws FileNotFoundException {
		try {
			studentFile = new RandomAccessFile(filename, "rw");
		} catch (FileNotFoundException e) {
			System.out.println("Error processing student file " + filename);
		}
	}

	public void writeStudentFile(Student temp) throws IOException {
		studentFile.writeInt(temp.getID()); // write the ID (4 bytes)
		String firstName = temp.getFirstName();
		if (firstName.length() > 15) { // write the first name (15 chars, 30 bytes)
			for (int i = 0; i < 15; i++)
				studentFile.writeChar(firstName.charAt(i));
		} else {
			studentFile.writeChars(firstName);
			for (int i = 0; i < 15 - firstName.length(); i++)
				studentFile.writeChar(' ');
		}

		String lastName = temp.getLastName();
		if (lastName.length() > 20) { // write the last name , (20 chars, 40 bytes)
			for (int i = 0; i < 20; i++)
				studentFile.writeChar(lastName.charAt(i));
		} else {
			studentFile.writeChars(lastName);
			for (int i = 0; i < 20 - lastName.length(); i++)
				studentFile.writeChar(' ');
		}

		String address = temp.getAddress();
		if (address.length() > 30) { // write the address, (30 chars, 60 bytes)
			for (int i = 0; i < 30; i++)
				studentFile.writeChar(address.charAt(i));
		} else {
			studentFile.writeChars(address);
			for (int i = 0; i < 30 - address.length(); i++)
				studentFile.writeChar(' ');
		}

		String city = temp.getCity();
		if (city.length() > 15) { // write the city, (15 chars, 30 bytes)
			for (int i = 0; i < 15; i++)
				studentFile.writeChar(city.charAt(i));
		} else {
			studentFile.writeChars(city);
			for (int i = 0; i < 15 - city.length(); i++)
				studentFile.writeChar(' ');
		}

		String state = temp.getState();
		if (state.length() > 2) { // write the state, (2 chars, 4 bytes)
			for (int i = 0; i < 2; i++)
				studentFile.writeChar(state.charAt(i));
		} else {
			studentFile.writeChars(state);
			for (int i = 0; i < 2 - state.length(); i++)
				studentFile.writeChar(' ');
		}
	}

	public Student readStudentFile() throws IOException {
		int ID = studentFile.readInt(); // extract ID

		char[] firstNameCharArray = new char[15]; // extract firstName
		for (int i = 0; i < 15; i++)
			firstNameCharArray[i] = studentFile.readChar();
		String firstName = new String(firstNameCharArray);
		firstName.trim();

		char[] lastNameCharArray = new char[20]; // extract lastName
		for (int i = 0; i < 20; i++)
			lastNameCharArray[i] = studentFile.readChar();
		String lastName = new String(lastNameCharArray);
		lastName.trim();

		char[] addressCharArray = new char[30]; // extract address
		for (int i = 0; i < 30; i++)
			addressCharArray[i] = studentFile.readChar();
		String address = new String(addressCharArray);
		address.trim();

		char[] cityCharArray = new char[15]; // extract city
		for (int i = 0; i < 15; i++)
			cityCharArray[i] = studentFile.readChar();
		String city = new String(cityCharArray);
		city.trim();

		char[] stateCharArray = new char[2]; // extract state
		for (int i = 0; i < 2; i++)
			stateCharArray[i] = studentFile.readChar();
		String state = new String(stateCharArray);
		state.trim();

		Student temp = new Student(ID, firstName, lastName, address, city, state);
		return temp;
	}

	private long getByteNum(long recordNum) {
		return RECORD_SIZE * recordNum;
	}

	public void moveFilePointer(long recordNum) throws IOException {
		studentFile.seek(getByteNum(recordNum));
	}

	public long getNumberOfRecords() throws IOException {
		return studentFile.length() / RECORD_SIZE;
	}

	public void setFileLength() { // Truncates the student file to overwrite all data when overwriting with new
									// linked list data
		try {
			studentFile.setLength(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close() throws IOException {
		studentFile.close();
	}

}

class Department implements Comparable<Department> {
	private int DID;
	private String departmentName;

	public Department() {
		DID = 1000; // never used but offset to eliminate problems
		departmentName = "";
	}

	public Department(String departmentName) {// to create object
		this.DID = 1000; // never used but offset to eliminate problems
		this.departmentName = departmentName;
	}

	public Department(int DID, String departmentName) {// to set
														// correct
														// ID number
														// //
														// reconstruct
														// // object
		this.DID = DID;
		this.departmentName = departmentName;
	}

	public int getDID() {
		return this.DID;
	}

	public String getDepartmentName() {
		return this.departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public void printDepartment() {
		System.out.println("DID: " + this.DID);
		System.out.println("Department Name: " + this.departmentName);
	}

	@Override
	public int compareTo(Department arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}

class DepartmentFile {
	private final int RECORD_SIZE = 44; // DID = 4, departmentName = 40
	private RandomAccessFile departmentFile;

	public DepartmentFile(String filename) throws FileNotFoundException {
		try {
			departmentFile = new RandomAccessFile(filename, "rw");
		} catch (FileNotFoundException e) {
			System.out.println("Error processing course file " + filename);
		}
	}

	public void writeDepartmentFile(Department temp) throws IOException {
		departmentFile.writeInt(temp.getDID()); // write the DID (4 bytes)
		String departmentName = temp.getDepartmentName();
		if (departmentName.length() > 20) { // write the Course ID (6 chars, 12 bytes)
			for (int i = 0; i < 20; i++)
				departmentFile.writeChar(departmentName.charAt(i));
		} else {
			departmentFile.writeChars(departmentName);
			for (int i = 0; i < 20 - departmentName.length(); i++)
				departmentFile.writeChar(' ');
		}
	}

	public Department readDepartmentFile() throws IOException {
		int DID = departmentFile.readInt(); // extract DID

		char[] departmentNameCharArray = new char[20]; // extract department Name
		for (int i = 0; i < 20; i++)
			departmentNameCharArray[i] = departmentFile.readChar();
		String departmentName = new String(departmentNameCharArray);
		departmentName.trim();

		Department temp = new Department(DID, departmentName);
		return temp;
	}

	private long getByteNum(long recordNum) {
		return RECORD_SIZE * recordNum;
	}

	public void moveFilePointer(long recordNum) throws IOException {
		departmentFile.seek(getByteNum(recordNum));
	}

	public long getNumberOfRecords() throws IOException {
		return departmentFile.length() / RECORD_SIZE;
	}

	public void close() throws IOException {
		departmentFile.close();
	}

}

class Instructor implements Comparable<Instructor> {
	private int IID;
	private String instructorName;
	private int instructorDID;

	public Instructor() {
		IID = 1000; // never used but offset to eliminate problems
		instructorName = "";
		instructorDID = -1;
	}

	public Instructor(String instructorName) {// to create object
		this.IID = 1000; // never used but offset to eliminate problems
		this.instructorName = instructorName;
		this.instructorDID = -1;
	}

	public Instructor(int DID, String instructorName, int instructorDID) {// to set
																			// correct
																			// ID number
																			// //
																			// reconstruct
																			// // object
		this.IID = DID;
		this.instructorName = instructorName;
		this.instructorDID = instructorDID;
	}

	public int getIID() {
		return this.IID;
	}

	public String getInstructorName() {
		return this.instructorName;
	}

	public int getInstructorDID() {
		return this.instructorDID;
	}

	public void setInstructorName(String instructorName) {
		this.instructorName = instructorName;
	}

	public void setInstructorDID(int instructorDID) {
		this.instructorDID = instructorDID;
	}

	public void printInstructor() {
		System.out.println("IID: " + this.IID);
		System.out.println("Instructor Name: " + this.instructorName);
		System.out.println("Instructor DID: " + this.instructorDID);
	}

	@Override
	public int compareTo(Instructor arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}

class InstructorFile {
	private final int RECORD_SIZE = 48; // IID = 4, instructorName = 40, instructorDID = 4
	private RandomAccessFile instructorFile;

	public InstructorFile(String filename) throws FileNotFoundException {
		try {
			instructorFile = new RandomAccessFile(filename, "rw");
		} catch (FileNotFoundException e) {
			System.out.println("Error processing course file " + filename);
		}
	}

	public void writeInstructorFile(Instructor temp) throws IOException {
		instructorFile.writeInt(temp.getIID()); // write the IID (4 bytes)
		String instructorName = temp.getInstructorName();
		if (instructorName.length() > 20) { // write the instuctorName (20 chars, 40 bytes)
			for (int i = 0; i < 20; i++)
				instructorFile.writeChar(instructorName.charAt(i));
		} else {
			instructorFile.writeChars(instructorName);
			for (int i = 0; i < 20 - instructorName.length(); i++)
				instructorFile.writeChar(' ');
		}
		instructorFile.writeInt(temp.getInstructorDID());
	}

	public Instructor readInstructorFile() throws IOException {
		int IID = instructorFile.readInt(); // extract IID

		char[] instructorNameCharArray = new char[20]; // extract instructor Name
		for (int i = 0; i < 20; i++)
			instructorNameCharArray[i] = instructorFile.readChar();
		String instructorName = new String(instructorNameCharArray);
		instructorName.trim();

		int instructorDID = instructorFile.readInt(); // extract departmentDID

		Instructor temp = new Instructor(IID, instructorName, instructorDID);
		return temp;
	}

	private long getByteNum(long recordNum) {
		return RECORD_SIZE * recordNum;
	}

	public void moveFilePointer(long recordNum) throws IOException {
		instructorFile.seek(getByteNum(recordNum));
	}

	public long getNumberOfRecords() throws IOException {
		return instructorFile.length() / RECORD_SIZE;
	}

	public void close() throws IOException {
		instructorFile.close();
	}

}

class Course implements Comparable<Course> {
	private int CNUM;
	private String CID;
	private String cName;
	private String instructor;
	private String department;

	public Course() {
		this.CNUM = 1000; // never used but offset to eliminate possible issues
		this.CID = "";
		this.cName = "";
		this.instructor = "";
		this.department = "";
	}

	public Course(String CID, String cName, String instructor, String department) { // to create object
		this.CNUM = 1000; // never used but offset to eliminate potential problems
		this.CID = CID;
		this.cName = cName;
		this.instructor = instructor;
		this.department = department;
	}

	public Course(int CNUM, String CID, String cName, String instructor, String department) { // to reconstruct objet
		this.CNUM = CNUM;
		this.CID = CID;
		this.cName = cName;
		this.instructor = instructor;
		this.department = department;
	}

	public int getCNUM() {
		return this.CNUM;
	}

	public String getCID() {
		return this.CID;
	}

	public String getcName() {
		return this.cName;
	}

	public String getInstructor() {
		return this.instructor;
	}

	public String getDepartment() {
		return this.department;
	}

	public void setCID(String CID) {
		this.CID = CID;
	}

	public void setcName(String cName) {
		this.cName = cName;
	}

	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public void printCourse() {
		System.out.println("CNUM: " + this.CNUM);
		System.out.println("CID: " + this.CID);
		System.out.println("Course Name: " + this.cName);
		System.out.println("Instructor: " + this.instructor);
		System.out.println("Department: " + this.department);
	}

	@Override
	public int compareTo(Course arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}

class CourseFile {
	private final int RECORD_SIZE = 110; // CNUM = 4, CID = 12, cName = 24, instructor = 30, department = 40
	private RandomAccessFile courseFile;

	public CourseFile(String filename) throws FileNotFoundException {
		try {
			courseFile = new RandomAccessFile(filename, "rw");
		} catch (FileNotFoundException e) {
			System.out.println("Error processing course file " + filename);
		}
	}

	public void writeCourseFile(Course temp) throws IOException {
		courseFile.writeInt(temp.getCNUM()); // write the CNUM (4 bytes)
		String CID = temp.getCID();
		if (CID.length() > 6) { // write the Course ID (6 chars, 12 bytes)
			for (int i = 0; i < 6; i++)
				courseFile.writeChar(CID.charAt(i));
		} else {
			courseFile.writeChars(CID);
			for (int i = 0; i < 6 - CID.length(); i++)
				courseFile.writeChar(' ');
		}

		String cName = temp.getcName();
		if (cName.length() > 12) { // write the course name , (12 chars, 24 bytes)
			for (int i = 0; i < 12; i++)
				courseFile.writeChar(cName.charAt(i));
		} else {
			courseFile.writeChars(cName);
			for (int i = 0; i < 12 - cName.length(); i++)
				courseFile.writeChar(' ');
		}

		String instructor = temp.getInstructor();
		if (instructor.length() > 15) { // write the instructor, (15 chars, 30 bytes)
			for (int i = 0; i < 15; i++)
				courseFile.writeChar(instructor.charAt(i));
		} else {
			courseFile.writeChars(instructor);
			for (int i = 0; i < 15 - instructor.length(); i++)
				courseFile.writeChar(' ');
		}

		String department = temp.getDepartment();
		if (department.length() > 20) { // write the department, (20 chars, 40 bytes)
			for (int i = 0; i < 20; i++)
				courseFile.writeChar(department.charAt(i));
		} else {
			courseFile.writeChars(department);
			for (int i = 0; i < 20 - department.length(); i++)
				courseFile.writeChar(' ');
		}

	}

	public Course readCourseFile() throws IOException {
		int CNUM = courseFile.readInt(); // extract CNUM

		char[] CIDCharArray = new char[6]; // extract Course ID
		for (int i = 0; i < 6; i++)
			CIDCharArray[i] = courseFile.readChar();
		String CID = new String(CIDCharArray);
		CID.trim();

		char[] cNameCharArray = new char[12]; // extract Course Name
		for (int i = 0; i < 12; i++)
			cNameCharArray[i] = courseFile.readChar();
		String cName = new String(cNameCharArray);
		cName.trim();

		char[] InstructorCharArray = new char[15]; // extract instructor
		for (int i = 0; i < 15; i++)
			InstructorCharArray[i] = courseFile.readChar();
		String instructor = new String(InstructorCharArray);
		instructor.trim();

		char[] DepartmentCharArray = new char[20]; // extract department
		for (int i = 0; i < 20; i++)
			DepartmentCharArray[i] = courseFile.readChar();
		String department = new String(DepartmentCharArray);
		department.trim();

		Course temp = new Course(CNUM, CID, cName, instructor, department);
		return temp;
	}

	private long getByteNum(long recordNum) {
		return RECORD_SIZE * recordNum;
	}

	public void moveFilePointer(long recordNum) throws IOException {
		courseFile.seek(getByteNum(recordNum));
	}

	public long getNumberOfRecords() throws IOException {
		return courseFile.length() / RECORD_SIZE;
	}

	public void close() throws IOException {
		courseFile.close();
	}

}

class Enrollment implements Comparable<Enrollment> {
	private int enrollmentID;
	private int studentID;
	private int cNum;
	private String year;
	private String semester;
	private String grade;

	public Enrollment() {
		this.studentID = 0;
		this.cNum = 0;
		this.year = "";
		this.semester = "";
		this.grade = "";
	}

	public Enrollment(int studentID, int cNum, String year, String semester, String grade) {
		this.enrollmentID = 1000; // never used offset to avoid a potential problem
		this.studentID = studentID;
		this.cNum = cNum;
		this.year = year;
		this.semester = semester;
		this.grade = grade;
	}

	public Enrollment(int enrollmentID, int studentID, int cNum, String year, String semester, String grade) {
		this.enrollmentID = enrollmentID;
		this.studentID = studentID;
		this.cNum = cNum;
		this.year = year;
		this.semester = semester;
		this.grade = grade;
	}

	public int getStudentID() {
		return this.studentID;
	}

	public int getcNum() {
		return this.cNum;
	}

	public int getEID() {
		return this.enrollmentID;
	}

	public String getYear() {
		return this.year;
	}

	public String getSemester() {
		return this.semester;
	}

	public String getGrade() {
		return this.grade;
	}

	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}

	public void setcNum(int cNum) {
		this.cNum = cNum;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public void printEnrollment() {
		System.out.println("Enrollment ID: " + this.enrollmentID);
		System.out.println("StudentID: " + this.studentID);
		System.out.println("Course Number: " + this.cNum);
		System.out.println("Year: " + this.year);
		System.out.println("Semester: " + this.semester);
		System.out.println("Grade: " + this.grade);
	}

	@Override
	public int compareTo(Enrollment arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}

class EnrollmentFile {
	private final int RECORD_SIZE = 38; // BYTES Enrollment ID = 4, Student ID = 4, cNum = 4, year = 8, semester = 16,
										// grade = 2
	private RandomAccessFile enrollmentFile;

	public EnrollmentFile(String filename) throws FileNotFoundException {
		try {
			enrollmentFile = new RandomAccessFile(filename, "rw");
		} catch (FileNotFoundException e) {
			System.out.println("Error processing enrollment file " + filename);
		}
	}

	public void writeEnrollmentFile(Enrollment temp) throws IOException {
		enrollmentFile.writeInt(temp.getEID()); // write the enrollment ID (4 bytes)
		enrollmentFile.writeInt(temp.getStudentID()); // write the Student ID (4 bytes)
		enrollmentFile.writeInt(temp.getcNum()); // write the course number (4 bytes)
		String year = temp.getYear();
		if (year.length() > 4) { // write the year (4 chars, 8 bytes)
			for (int i = 0; i < 4; i++)
				enrollmentFile.writeChar(year.charAt(i));
		} else {
			enrollmentFile.writeChars(year);
			for (int i = 0; i < 4 - year.length(); i++)
				enrollmentFile.writeChar(' ');
		}

		String semester = temp.getSemester();
		if (semester.length() > 8) { // write the semester , (8 chars, 16 bytes)
			for (int i = 0; i < 8; i++)
				enrollmentFile.writeChar(semester.charAt(i));
		} else {
			enrollmentFile.writeChars(semester);
			for (int i = 0; i < 8 - semester.length(); i++)
				enrollmentFile.writeChar(' ');
		}

		String grade = temp.getGrade();
		if (grade.length() > 1) { // write the instructor, (15 chars, 30 bytes)
			for (int i = 0; i < 1; i++)
				enrollmentFile.writeChar(grade.charAt(i));
		} else {
			enrollmentFile.writeChars(grade);
			for (int i = 0; i < 1 - grade.length(); i++)
				enrollmentFile.writeChar(' ');
		}

	}

	public Enrollment readEnrollmentFile() throws IOException {
		int enrollmentID = enrollmentFile.readInt(); // extract enrollment ID
		int studentID = enrollmentFile.readInt(); // extract student ID
		int cNum = enrollmentFile.readInt(); // extract course number
		char[] yearCharArray = new char[4]; // extract year field
		for (int i = 0; i < 4; i++)
			yearCharArray[i] = enrollmentFile.readChar();
		String year = new String(yearCharArray);
		year.trim();

		char[] semesterCharArray = new char[8]; // extract semester
		for (int i = 0; i < 8; i++)
			semesterCharArray[i] = enrollmentFile.readChar();
		String semester = new String(semesterCharArray);
		semester.trim();

		char[] gradeCharArray = new char[1]; // extract grade
		for (int i = 0; i < 1; i++)
			gradeCharArray[i] = enrollmentFile.readChar();
		String grade = new String(gradeCharArray);
		grade.trim();

		Enrollment temp = new Enrollment(enrollmentID, studentID, cNum, year, semester, grade);
		return temp;
	}

	private long getByteNum(long recordNum) {
		return RECORD_SIZE * recordNum;
	}

	public void moveFilePointer(long recordNum) throws IOException {
		enrollmentFile.seek(getByteNum(recordNum));
	}

	public long getNumberOfRecords() throws IOException {
		return enrollmentFile.length() / RECORD_SIZE;
	}

	public void close() throws IOException {
		enrollmentFile.close();
	}

	public int readInt() throws IOException {
		return enrollmentFile.readInt();
	}
}

class StudentAddPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5149000693352260886L;
	private JLabel studentAdd_label;
	private JLabel studentAdd_firstNameLabel;
	private JLabel studentAdd_lastNameLabel;
	private JLabel studentAdd_addressLabel;
	private JLabel studentAdd_cityLabel;
	private JLabel studentAdd_stateLabel;
	private JTextField studentAdd_firstNameText;
	private JTextField studentAdd_lastNameText;
	private JTextField studentAdd_addressText;
	private JTextField studentAdd_cityText;
	private JTextField studentAdd_stateText;

	private JComboBox studentAdd_stateBox;

	public String getFirstName() {
		return studentAdd_firstNameText.getText();
	}

	public String getLastName() {
		return studentAdd_lastNameText.getText();
	}

	public String getAddress() {
		return studentAdd_addressText.getText();
	}

	public String getCity() {
		return studentAdd_cityText.getText();
	}

	public String getState() { // ERASE OLD RETURN LATER
		// return studentAdd_stateText.getText();
		return (String) studentAdd_stateBox.getSelectedItem();
	}

	public StudentAddPanel() throws FileNotFoundException {
		buildStudentAddPanel();
	}

	public void buildStudentAddPanel() {
		setLayout(new GridLayout(7, 1));

		JPanel label_JPanel = new JPanel();
		studentAdd_label = new JLabel("Add Student");
		label_JPanel.add(studentAdd_label);

		JPanel firstName_JPanel = new JPanel();
		studentAdd_firstNameLabel = new JLabel("First Name");
		studentAdd_firstNameText = new JTextField(15);
		firstName_JPanel.add(studentAdd_firstNameLabel);
		firstName_JPanel.add(studentAdd_firstNameText);

		JPanel lastName_JPanel = new JPanel();
		studentAdd_lastNameLabel = new JLabel("Last Name");
		studentAdd_lastNameText = new JTextField(20);
		lastName_JPanel.add(studentAdd_lastNameLabel);
		lastName_JPanel.add(studentAdd_lastNameText);

		JPanel address_JPanel = new JPanel();
		studentAdd_addressLabel = new JLabel("Address");
		studentAdd_addressText = new JTextField(30);
		address_JPanel.add(studentAdd_addressLabel);
		address_JPanel.add(studentAdd_addressText);

		JPanel city_JPanel = new JPanel();
		studentAdd_cityLabel = new JLabel("City");
		studentAdd_cityText = new JTextField(15);
		city_JPanel.add(studentAdd_cityLabel);
		city_JPanel.add(studentAdd_cityText);

		JPanel state_JPanel = new JPanel();
		/*
		 * studentAdd_stateLabel = new JLabel("State"); //NOT NEEDED AFTER COMBO BOX
		 * studentAdd_stateText = new JTextField(2);
		 * state_JPanel.add(studentAdd_stateLabel);
		 * state_JPanel.add(studentAdd_stateText);
		 */
		String[] states = { "AK", "AL", "AR", "AS", "AZ", "CA", "CO", "CT", "DC", "DE", "FL", "GA", "GU", "HI", "IA",
				"ID", "IL", "IN", "KS", "KY", "LA", "MA", "MD", "ME", "MI", "MN", "MO", "MP", "MS", "MT", "NC", "ND",
				"NE", "NH", "NJ", "NM", "NV", "NY", "OH", "OK", "OR", "PA", "PR", "RI", "SC", "SD", "TN", "TX", "UM",
				"UT", "VA", "VI", "VT", "WA", "WI", "WV", "WY" };
		studentAdd_stateBox = new JComboBox(states);
		state_JPanel.add(studentAdd_stateBox);

		add(label_JPanel);
		add(firstName_JPanel);
		add(lastName_JPanel);
		add(address_JPanel);
		add(city_JPanel);
		add(state_JPanel);
	}

	public Student createStudent(int numberOfStudentsInFile) {
		String firstName;
		String lastName;
		String address;
		String city;
		String state;

		firstName = studentAdd_firstNameText.getText();
		lastName = studentAdd_lastNameText.getText();
		address = studentAdd_addressText.getText();
		city = studentAdd_cityText.getText();
		state = studentAdd_stateText.getText();

		Student newStudent = new Student(numberOfStudentsInFile, firstName, lastName, address, city, state);
		return newStudent;

	}

	public void studentAddDisplayPanel() {

		setLayout(new GridLayout(2, 1));

		JPanel label_JPanel1 = new JPanel();
		studentAdd_label = new JLabel("Student Details");
		label_JPanel1.add(studentAdd_label);

		JPanel firstName_JPanel1 = new JPanel();
		studentAdd_firstNameText.setEditable(false);
		firstName_JPanel1.add(studentAdd_firstNameLabel);
		firstName_JPanel1.add(studentAdd_firstNameText);

		add(label_JPanel1);
		add(firstName_JPanel1);
		validate();
		setVisible(true);

	}
}

class PreCourseAddPanel extends JPanel {

	// database additions
	String[] departmentNamesData;

	private static final long serialVersionUID = 1L;
	private JLabel preCourseAdd_label;

	private JLabel preCourseAdd_departmentLabel;
	private JComboBox preCourseAdd_departmentCombo;

	public String getDepartment() {
		return (String) preCourseAdd_departmentCombo.getSelectedItem();
	}

	public int getDepartmentIndex() {
		return preCourseAdd_departmentCombo.getSelectedIndex();
	}

	public PreCourseAddPanel(String[] array) throws FileNotFoundException {
		this.departmentNamesData = array;
		buildCourseAddPanel();
	}

	public void buildCourseAddPanel() { /// ERASE OLD CODE HERE
		setLayout(new GridLayout(2, 1));

		JPanel label_JPanel = new JPanel();
		preCourseAdd_label = new JLabel("Add Course - Department Selection");
		label_JPanel.add(preCourseAdd_label);

		JPanel Department_JPanel = new JPanel();
		preCourseAdd_departmentLabel = new JLabel("Department");
		// courseAdd_departmentCombo = new JComboBox(listToArrayDepartment);
		preCourseAdd_departmentCombo = new JComboBox(departmentNamesData);
		Department_JPanel.add(preCourseAdd_departmentLabel);
		Department_JPanel.add(preCourseAdd_departmentCombo);

		add(label_JPanel);
		add(Department_JPanel);
	}

}

class PreEditCoursePanel extends JPanel {

	// database additions
	String[] departmentNamesData;

	private static final long serialVersionUID = 1L;
	private JLabel preEditCourse_label;

	private JLabel preEditCourse_departmentLabel;
	private JComboBox preEditCourse_departmentCombo;

	public String getDepartment() {
		return (String) preEditCourse_departmentCombo.getSelectedItem();
	}

	public int getDepartmentIndex() {
		return preEditCourse_departmentCombo.getSelectedIndex();
	}

	public PreEditCoursePanel(String[] array) throws FileNotFoundException {
		this.departmentNamesData = array;
		buildPreEditCoursePanel();
	}

	public void buildPreEditCoursePanel() { /// ERASE OLD CODE HERE
		setLayout(new GridLayout(2, 1));

		System.out.println("---------------THIS IS CALLED - preeditcoursePanel--------");

		JPanel label_JPanel = new JPanel();
		preEditCourse_label = new JLabel("Edit Course - Department Selection");
		label_JPanel.add(preEditCourse_label);

		JPanel Department_JPanel = new JPanel();
		preEditCourse_departmentLabel = new JLabel("Department");
		// courseAdd_departmentCombo = new JComboBox(listToArrayDepartment);
		preEditCourse_departmentCombo = new JComboBox(departmentNamesData);
		Department_JPanel.add(preEditCourse_departmentLabel);
		Department_JPanel.add(preEditCourse_departmentCombo);

		add(label_JPanel);
		add(Department_JPanel);
	}

}

class CourseAddPanel extends JPanel {
	// dont need the generic linked lists anymore.
	private MyGenericList<Department> departmentLinkedListCopy;
	private MyGenericList<Instructor> instructorLinkedListCopy;
	String[] listToArrayDepartment; // to convert list into string array for comboBox
	String[] listToArrayInstructor;

	// database additions
	String departmentName;
	String[] instructorNamesData;
	int numberOfInstructorsForCourse = 0;

	private static final long serialVersionUID = 1L;
	private JLabel courseAdd_label;
	private JLabel courseAdd_CIDLabel;
	private JLabel courseAdd_cNameLabel;
	private JLabel courseAdd_instructorLabel;
	private JLabel courseAdd_departmentLabel;

	private JTextField courseAdd_CIDText;
	private JTextField courseAdd_cNameText;
	private JTextField courseAdd_instructorText;
	private JTextField courseAdd_departmentText; // REPLACING WITH COMBOBOX

	private JComboBox courseAdd_departmentCombo;
	private JComboBox courseAdd_instructorCombo;

	public String getCID() {
		return courseAdd_CIDText.getText();
	}

	public String getcName() {
		return courseAdd_cNameText.getText();
	}

	/*
	 * public String getInstructor() { return courseAdd_instructorText.getText(); }
	 */
	public String getDepartment() { // REPLACE WITH COMBO BOX
		return courseAdd_departmentText.getText();
		// return (String) courseAdd_departmentCombo.getSelectedItem(); // not needed
		// since there is no combo box anymore
	}

	public String getInstructor() { // REPLACE WITH COMBO BOX
		// return courseAdd_departmentText.getText();
		return (String) courseAdd_instructorCombo.getSelectedItem();
	}

	// removing these
	public void linkedListToArrayDepartment() {
		listToArrayDepartment = new String[departmentLinkedListCopy.size()];
		for (int i = 0; i < departmentLinkedListCopy.size(); i++) {
			Department temp = departmentLinkedListCopy.get(i);
			listToArrayDepartment[i] = temp.getDepartmentName();
		}

	}

	// removing these
	public void linkedListToArrayInstructor() {
		listToArrayInstructor = new String[instructorLinkedListCopy.size()];
		for (int i = 0; i < instructorLinkedListCopy.size(); i++) {
			Instructor temp = instructorLinkedListCopy.get(i);
			listToArrayInstructor[i] = temp.getInstructorName();
		}

	}

	// not being called due to DATABASE
	public CourseAddPanel(MyGenericList<Department> departmentLinkedListCopy,
			MyGenericList<Instructor> instructorLinkedListCopy) throws FileNotFoundException {
		this.departmentLinkedListCopy = departmentLinkedListCopy;
		this.instructorLinkedListCopy = instructorLinkedListCopy;
		buildCourseAddPanel();
	}

	public CourseAddPanel(String departmentName, String[] array) throws FileNotFoundException {
		this.departmentName = departmentName;
		this.instructorNamesData = array;
		buildCourseAddPanel();
	}

	public void buildCourseAddPanel() { /// ERASE OLD CODE HERE
		setLayout(new GridLayout(7, 1));

		JPanel label_JPanel = new JPanel();
		courseAdd_label = new JLabel("Add Course");
		label_JPanel.add(courseAdd_label);

		JPanel CID_JPanel = new JPanel(); // PANEL NAMES ARE ALL SCREWED UP = FIX LATER
		courseAdd_CIDLabel = new JLabel("Course ID");
		courseAdd_CIDText = new JTextField(6);
		CID_JPanel.add(courseAdd_CIDLabel);
		CID_JPanel.add(courseAdd_CIDText);

		JPanel Name_JPanel = new JPanel();
		courseAdd_cNameLabel = new JLabel("Course Name");
		courseAdd_cNameText = new JTextField(12);
		Name_JPanel.add(courseAdd_cNameLabel);
		Name_JPanel.add(courseAdd_cNameText);
		/*
		 * JPanel address_JPanel5 = new JPanel(); courseAdd_instructorLabel = new
		 * JLabel("Instructor"); courseAdd_instructorText = new JTextField(15);
		 * address_JPanel5.add(courseAdd_instructorLabel);
		 * address_JPanel5.add(courseAdd_instructorText);
		 */
		// linkedListToArrayDepartment(); //removing these due to database
		// linkedListToArrayInstructor(); //removing these due to database

		JPanel Department_JPanel = new JPanel();
		courseAdd_departmentLabel = new JLabel("Department");
		// courseAdd_departmentCombo = new JComboBox(listToArrayDepartment);
		// courseAdd_departmentCombo = new JComboBox(departmentNamesData);
		courseAdd_departmentText = new JTextField(departmentName);
		courseAdd_departmentText.setEditable(false);
		Department_JPanel.add(courseAdd_departmentLabel);
		Department_JPanel.add(courseAdd_departmentText);

		JPanel Instructor_JPanel = new JPanel();
		courseAdd_instructorLabel = new JLabel("Instructor");
		courseAdd_instructorCombo = new JComboBox(instructorNamesData);
		Instructor_JPanel.add(courseAdd_instructorLabel);
		Instructor_JPanel.add(courseAdd_instructorCombo);

		add(label_JPanel);
		add(CID_JPanel);
		add(Name_JPanel);
		add(Department_JPanel);
		add(Instructor_JPanel);

	}

	public Course createCourse(int numberOfCoursesInFile) { // THIS CLASS WAS MOVED TO SUPER CLASS
		String CID;
		String cName;
		String instructor;
		String department;

		CID = courseAdd_CIDText.getText();
		cName = courseAdd_cNameText.getText();
		instructor = courseAdd_instructorText.getText();
		department = courseAdd_departmentText.getText();

		Course newCourse = new Course(numberOfCoursesInFile, CID, cName, instructor, department);
		return newCourse;

	}
}

class CourseDisplayPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	Course course;
	private JLabel courseAfterAdd_label;
	private JLabel courseAfterAdd_CIDLabel;
	private JLabel courseAfterAdd_cNameLabel;
	private JLabel courseAfterAdd_instructorLabel;
	private JLabel courseAfterAdd_departmentLabel;

	private JTextField courseAfterAdd_CIDText;
	private JTextField courseAfterAdd_cNameText;
	private JTextField courseAfterAdd_instructorText;
	private JTextField courseAfterAdd_departmentText;

	private JLabel courseAfterAdd_cNUMLabel;
	private JTextField courseAfterAdd_cNUMText;

	public CourseDisplayPanel(Course temp) {
		this.course = temp;
		buildCourseDisplayPanel();
	}

	public void buildCourseDisplayPanel() {
		setLayout(new GridLayout(7, 1));

		JPanel label_JPanel = new JPanel();
		courseAfterAdd_label = new JLabel("Display Course");
		label_JPanel.add(courseAfterAdd_label);

		JPanel city_JPanel = new JPanel();
		courseAfterAdd_cNUMLabel = new JLabel("cNUM");
		String intToStringCNUM = String.valueOf(course.getCNUM());
		courseAfterAdd_cNUMText = new JTextField(intToStringCNUM);
		courseAfterAdd_cNUMText.setEditable(false);
		city_JPanel.add(courseAfterAdd_cNUMLabel);
		city_JPanel.add(courseAfterAdd_cNUMText);

		JPanel ID_JPanel = new JPanel();
		courseAfterAdd_CIDLabel = new JLabel("Course ID");
		String intToString = String.valueOf(course.getCID());
		courseAfterAdd_CIDText = new JTextField(intToString);
		courseAfterAdd_CIDText.setEditable(false);
		ID_JPanel.add(courseAfterAdd_CIDLabel);
		ID_JPanel.add(courseAfterAdd_CIDText);

		JPanel firstName_JPanel = new JPanel();
		courseAfterAdd_cNameLabel = new JLabel("Course Name");
		courseAfterAdd_cNameText = new JTextField(course.getcName());
		courseAfterAdd_cNameText.setEditable(false);
		firstName_JPanel.add(courseAfterAdd_cNameLabel);
		firstName_JPanel.add(courseAfterAdd_cNameText);

		JPanel lastName_JPanel = new JPanel();
		courseAfterAdd_instructorLabel = new JLabel("Instructor");
		courseAfterAdd_instructorText = new JTextField(course.getInstructor());
		courseAfterAdd_instructorText.setEditable(false);
		lastName_JPanel.add(courseAfterAdd_instructorLabel);
		lastName_JPanel.add(courseAfterAdd_instructorText);

		JPanel address_JPanel = new JPanel();
		courseAfterAdd_departmentLabel = new JLabel("Department");
		courseAfterAdd_departmentText = new JTextField(course.getDepartment());
		courseAfterAdd_departmentText.setEditable(false);
		address_JPanel.add(courseAfterAdd_departmentLabel);
		address_JPanel.add(courseAfterAdd_departmentText);

		add(label_JPanel);
		add(city_JPanel);
		add(ID_JPanel);
		add(firstName_JPanel);
		add(lastName_JPanel);
		add(address_JPanel);

	}

}

class CourseEditDisplayPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	Course course;
	private JLabel courseAfterEdit_label;
	private JLabel courseAfterEdit_CIDLabel;
	private JLabel courseAfterEdit_cNameLabel;
	private JLabel courseAfterEdit_instructorLabel;
	private JLabel courseAfterEdit_departmentLabel;

	private JTextField courseAfterEdit_CIDText;
	private JTextField courseAfterEdit_cNameText;
	private JTextField courseAfterEdit_instructorText;
	private JTextField courseAfterEdit_departmentText;

	private JLabel courseAfterEdit_cNUMLabel;
	private JTextField courseAfterEdit_cNUMText;

	public CourseEditDisplayPanel(Course temp) {
		this.course = temp;
		buildCourseEditDisplayPanel();
	}

	public void buildCourseEditDisplayPanel() {
		setLayout(new GridLayout(7, 1));

		JPanel label_JPanel = new JPanel();
		courseAfterEdit_label = new JLabel("Display Course");
		label_JPanel.add(courseAfterEdit_label);

		JPanel city_JPanel = new JPanel();
		courseAfterEdit_cNUMLabel = new JLabel("cNUM");
		String intToStringCNUM = String.valueOf(course.getCNUM());
		courseAfterEdit_cNUMText = new JTextField(intToStringCNUM);
		courseAfterEdit_cNUMText.setEditable(false);
		city_JPanel.add(courseAfterEdit_cNUMLabel);
		city_JPanel.add(courseAfterEdit_cNUMText);

		JPanel ID_JPanel = new JPanel();
		courseAfterEdit_CIDLabel = new JLabel("Course ID");
		String intToString = String.valueOf(course.getCID());
		courseAfterEdit_CIDText = new JTextField(intToString);
		courseAfterEdit_CIDText.setEditable(false);
		ID_JPanel.add(courseAfterEdit_CIDLabel);
		ID_JPanel.add(courseAfterEdit_CIDText);

		JPanel firstName_JPanel = new JPanel();
		courseAfterEdit_cNameLabel = new JLabel("Course Name");
		courseAfterEdit_cNameText = new JTextField(course.getcName());
		courseAfterEdit_cNameText.setEditable(false);
		firstName_JPanel.add(courseAfterEdit_cNameLabel);
		firstName_JPanel.add(courseAfterEdit_cNameText);

		JPanel lastName_JPanel = new JPanel();
		courseAfterEdit_instructorLabel = new JLabel("Instructor");
		courseAfterEdit_instructorText = new JTextField(course.getInstructor());
		courseAfterEdit_instructorText.setEditable(false);
		lastName_JPanel.add(courseAfterEdit_instructorLabel);
		lastName_JPanel.add(courseAfterEdit_instructorText);

		JPanel address_JPanel = new JPanel();
		courseAfterEdit_departmentLabel = new JLabel("Department");
		courseAfterEdit_departmentText = new JTextField(course.getDepartment());
		courseAfterEdit_departmentText.setEditable(false);
		address_JPanel.add(courseAfterEdit_departmentLabel);
		address_JPanel.add(courseAfterEdit_departmentText);

		add(label_JPanel);
		add(city_JPanel);
		add(ID_JPanel);
		add(firstName_JPanel);
		add(lastName_JPanel);
		add(address_JPanel);

	}

}

class StudentDisplayPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Student student;
	private JLabel studentAfterAdd_label;
	private JLabel studentAfterAdd_firstNameLabel;
	private JLabel studentAfterAdd_lastNameLabel;
	private JLabel studentAfterAdd_addressLabel;
	private JLabel studentAfterAdd_cityLabel;
	private JLabel studentAfterAdd_stateLabel;
	private JTextField studentAfterAdd_firstNameText;
	private JTextField studentAfterAdd_lastNameText;
	private JTextField studentAfterAdd_addressText;
	private JTextField studentAfterAdd_cityText;
	private JTextField studentAfterAdd_stateText;
	private JLabel studentAfterAdd_IDLabel;
	private JTextField studentAfterAdd_IDText;

	public StudentDisplayPanel(Student temp) {
		this.student = temp;
		buildStudentDisplayPanel();
	}

	public void buildStudentDisplayPanel() {
		setLayout(new GridLayout(7, 1));

		JPanel label_JPanel = new JPanel();
		studentAfterAdd_label = new JLabel("Display Student");
		label_JPanel.add(studentAfterAdd_label);

		JPanel ID_JPanel = new JPanel();
		studentAfterAdd_IDLabel = new JLabel("Student ID");
		String intToString = String.valueOf(student.getID());
		studentAfterAdd_IDText = new JTextField(intToString);
		studentAfterAdd_IDText.setEditable(false);
		ID_JPanel.add(studentAfterAdd_IDLabel);
		ID_JPanel.add(studentAfterAdd_IDText);

		JPanel firstName_JPanel = new JPanel();
		studentAfterAdd_firstNameLabel = new JLabel("First Name");
		studentAfterAdd_firstNameText = new JTextField(student.getFirstName());
		studentAfterAdd_firstNameText.setEditable(false);
		firstName_JPanel.add(studentAfterAdd_firstNameLabel);
		firstName_JPanel.add(studentAfterAdd_firstNameText);

		JPanel lastName_JPanel = new JPanel();
		studentAfterAdd_lastNameLabel = new JLabel("Last Name");
		studentAfterAdd_lastNameText = new JTextField(student.getLastName());
		studentAfterAdd_lastNameText.setEditable(false);
		lastName_JPanel.add(studentAfterAdd_lastNameLabel);
		lastName_JPanel.add(studentAfterAdd_lastNameText);

		JPanel address_JPanel = new JPanel();
		studentAfterAdd_addressLabel = new JLabel("Address");
		studentAfterAdd_addressText = new JTextField(student.getAddress());
		studentAfterAdd_addressText.setEditable(false);
		address_JPanel.add(studentAfterAdd_addressLabel);
		address_JPanel.add(studentAfterAdd_addressText);

		JPanel city_JPanel = new JPanel();
		studentAfterAdd_cityLabel = new JLabel("City");
		studentAfterAdd_cityText = new JTextField(student.getCity());
		studentAfterAdd_cityText.setEditable(false);
		city_JPanel.add(studentAfterAdd_cityLabel);
		city_JPanel.add(studentAfterAdd_cityText);

		JPanel state_JPanel = new JPanel();
		studentAfterAdd_stateLabel = new JLabel("State");
		studentAfterAdd_stateText = new JTextField(student.getState());
		studentAfterAdd_stateText.setEditable(false);
		state_JPanel.add(studentAfterAdd_stateLabel);
		state_JPanel.add(studentAfterAdd_stateText);

		add(label_JPanel);
		add(ID_JPanel);
		add(firstName_JPanel);
		add(lastName_JPanel);
		add(address_JPanel);
		add(city_JPanel);
		add(state_JPanel);

	}

}

class StudentSearchPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel studentSearch_label;
	private JLabel studentSearch_inputIDLabel;
	private JTextField studentSearch_inputIDText;

	public String getInputStudentIDText() {
		return studentSearch_inputIDText.getText();
	}

	public StudentSearchPanel() throws FileNotFoundException {

		buildStudentSearchPanel();
	}

	public void buildStudentSearchPanel() {
		setLayout(new GridLayout(2, 1));

		JPanel label_JPanel = new JPanel();
		studentSearch_label = new JLabel("Search Student");
		label_JPanel.add(studentSearch_label);

		JPanel enterID_JPanel = new JPanel();
		studentSearch_inputIDLabel = new JLabel("Enter Student ID to search");
		studentSearch_inputIDText = new JTextField(15);
		enterID_JPanel.add(studentSearch_inputIDLabel);
		enterID_JPanel.add(studentSearch_inputIDText);

		add(label_JPanel);
		add(enterID_JPanel);
	}

}

class StudentEditPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Student student;
	private JLabel studentEdit_label;
	private JLabel studentEdit_firstNameLabel;
	private JLabel studentEdit_lastNameLabel;
	private JLabel studentEdit_addressLabel;
	private JLabel studentEdit_cityLabel;
	private JLabel studentEdit_stateLabel;
	private JTextField studentEdit_firstNameText;
	private JTextField studentEdit_lastNameText;
	private JTextField studentEdit_addressText;
	private JTextField studentEdit_cityText;
	private JTextField studentEdit_stateText;

	private JLabel studentEdit_IDLabel;
	private JTextField studentEdit_IDText;

	private JLabel studentEdit_label2; // 2 stands for user input version
	private JLabel studentEdit_firstNameLabel2;
	private JLabel studentEdit_lastNameLabel2;
	private JLabel studentEdit_addressLabel2;
	private JLabel studentEdit_cityLabel2;
	private JLabel studentEdit_stateLabel2;
	private JTextField studentEdit_studentIDText2;
	private JTextField studentEdit_firstNameText2;
	private JTextField studentEdit_lastNameText2;
	private JTextField studentEdit_addressText2;
	private JTextField studentEdit_cityText2;
	private JTextField studentEdit_stateText2;

	private JLabel studentEdit_IDLabel2;
	private JTextField studentEdit_IDText2;

	private JComboBox studentAdd_stateBox;

	public String getStudentEdit_studentIDText2() {
		return studentEdit_studentIDText2.getText();
	}

	public String getStudentEdit_firstNameText2() {
		return studentEdit_firstNameText2.getText();
	}

	public String getStudentEdit_lastNameText2() {
		return studentEdit_lastNameText2.getText();
	}

	public String getStudentEdit_addressText2() {
		return studentEdit_addressText2.getText();
	}

	public String getStudentEdit_cityText2() {
		return studentEdit_cityText2.getText();
	}

	public String getStudentEdit_stateText2() {
		// return studentEdit_stateText2.getText();
		return (String) studentAdd_stateBox.getSelectedItem();
	}

	public String getInputStudentIDText() {
		return studentEdit_IDText.getText();
	}

	public StudentEditPanel(Student temp) throws FileNotFoundException {
		this.student = temp;
		buildStudentEditPanel(student);
	}

	public void buildStudentEditPanel(Student student) {

		JPanel label_JPanel = new JPanel();
		studentEdit_label = new JLabel("[Old Student Data]");
		label_JPanel.add(studentEdit_label);

		JPanel ID_JPanel = new JPanel();
		studentEdit_IDLabel = new JLabel("Student ID");
		String intToString = String.valueOf(student.getID());
		studentEdit_IDText = new JTextField(intToString);
		studentEdit_IDText.setEditable(false);
		ID_JPanel.add(studentEdit_IDLabel);
		ID_JPanel.add(studentEdit_IDText);

		JPanel firstName_JPanel = new JPanel();
		studentEdit_firstNameLabel = new JLabel("First Name");
		studentEdit_firstNameText = new JTextField(student.getFirstName());
		studentEdit_firstNameText.setEditable(false);
		firstName_JPanel.add(studentEdit_firstNameLabel);
		firstName_JPanel.add(studentEdit_firstNameText);

		JPanel lastName_JPanel = new JPanel();
		studentEdit_lastNameLabel = new JLabel("Last Name");
		studentEdit_lastNameText = new JTextField(student.getLastName());
		studentEdit_lastNameText.setEditable(false);
		lastName_JPanel.add(studentEdit_lastNameLabel);
		lastName_JPanel.add(studentEdit_lastNameText);

		JPanel address_JPanel = new JPanel();
		studentEdit_addressLabel = new JLabel("Address");
		studentEdit_addressText = new JTextField(student.getAddress());
		studentEdit_addressText.setEditable(false);
		address_JPanel.add(studentEdit_addressLabel);
		address_JPanel.add(studentEdit_addressText);

		JPanel city_JPanel = new JPanel();
		studentEdit_cityLabel = new JLabel("City");
		studentEdit_cityText = new JTextField(student.getCity());
		studentEdit_cityText.setEditable(false);
		city_JPanel.add(studentEdit_cityLabel);
		city_JPanel.add(studentEdit_cityText);

		JPanel state_JPanel = new JPanel();
		studentEdit_stateLabel = new JLabel("State");
		studentEdit_stateText = new JTextField(student.getState());
		studentEdit_stateText.setEditable(false);
		state_JPanel.add(studentEdit_stateLabel);
		state_JPanel.add(studentEdit_stateText);

		///// DATA 2 input
		JPanel label_JPanel2 = new JPanel();
		studentEdit_label2 = new JLabel("[New Student Data]");
		label_JPanel2.add(studentEdit_label2);

		JPanel ID_JPanel2 = new JPanel();
		studentEdit_IDLabel2 = new JLabel("Student ID");
		String intToString2 = String.valueOf(student.getID());
		studentEdit_IDText2 = new JTextField(intToString2);
		studentEdit_IDText2.setEditable(false);
		ID_JPanel2.add(studentEdit_IDLabel2);
		ID_JPanel2.add(studentEdit_IDText2);

		JPanel firstName_JPanel2 = new JPanel();
		studentEdit_firstNameLabel2 = new JLabel("First Name");
		studentEdit_firstNameText2 = new JTextField(15);
		firstName_JPanel2.add(studentEdit_firstNameLabel2);
		firstName_JPanel2.add(studentEdit_firstNameText2);

		JPanel lastName_JPanel2 = new JPanel();
		studentEdit_lastNameLabel2 = new JLabel("Last Name");
		studentEdit_lastNameText2 = new JTextField(20);
		lastName_JPanel2.add(studentEdit_lastNameLabel2);
		lastName_JPanel2.add(studentEdit_lastNameText2);

		JPanel address_JPanel2 = new JPanel();
		studentEdit_addressLabel2 = new JLabel("Address");
		studentEdit_addressText2 = new JTextField(30);
		address_JPanel2.add(studentEdit_addressLabel2);
		address_JPanel2.add(studentEdit_addressText2);

		JPanel city_JPanel2 = new JPanel();
		studentEdit_cityLabel2 = new JLabel("City");
		studentEdit_cityText2 = new JTextField(15);
		city_JPanel2.add(studentEdit_cityLabel2);
		city_JPanel2.add(studentEdit_cityText2);

		JPanel state_JPanel2 = new JPanel();
		// studentEdit_stateLabel2 = new JLabel("State");
		// studentEdit_stateText2 = new JTextField(2);
		// state_JPanel2.add(studentEdit_stateLabel2);
		// state_JPanel2.add(studentEdit_stateText2);
		///////

		String[] states = { "AK", "AL", "AR", "AS", "AZ", "CA", "CO", "CT", "DC", "DE", "FL", "GA", "GU", "HI", "IA",
				"ID", "IL", "IN", "KS", "KY", "LA", "MA", "MD", "ME", "MI", "MN", "MO", "MP", "MS", "MT", "NC", "ND",
				"NE", "NH", "NJ", "NM", "NV", "NY", "OH", "OK", "OR", "PA", "PR", "RI", "SC", "SD", "TN", "TX", "UM",
				"UT", "VA", "VI", "VT", "WA", "WI", "WV", "WY" };
		studentAdd_stateBox = new JComboBox(states);
		state_JPanel2.add(studentAdd_stateBox);
		////////

		JPanel oldEditData = new JPanel();
		oldEditData.setLayout(new GridLayout(7, 1));

		oldEditData.add(label_JPanel);
		oldEditData.add(ID_JPanel);
		oldEditData.add(firstName_JPanel);
		oldEditData.add(lastName_JPanel);
		oldEditData.add(address_JPanel);
		oldEditData.add(city_JPanel);
		oldEditData.add(state_JPanel);

		JPanel newEditData = new JPanel();
		newEditData.setLayout(new GridLayout(7, 1));

		newEditData.add(label_JPanel2);
		newEditData.add(ID_JPanel2);
		newEditData.add(firstName_JPanel2);
		newEditData.add(lastName_JPanel2);
		newEditData.add(address_JPanel2);
		newEditData.add(city_JPanel2);
		newEditData.add(state_JPanel2);

		JLabel editMenuLabel = new JLabel("Edit Student");
		JPanel editMenuLabelPanel = new JPanel();
		editMenuLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		editMenuLabelPanel.add(editMenuLabel);

		setLayout(new BorderLayout());

		add(oldEditData, BorderLayout.WEST);
		add(newEditData, BorderLayout.EAST);
		add(editMenuLabelPanel, BorderLayout.NORTH);

		JLabel imageLabel = new JLabel();
		ImageIcon arrows = new ImageIcon("rightarrow.png");
		imageLabel.setIcon(arrows);

		add(imageLabel, BorderLayout.CENTER);

	}

}

class CourseEditPanel extends JPanel {
	private MyGenericList<Department> departmentLinkedListCopy;
	private MyGenericList<Instructor> instructorLinkedListCopy;
	String[] listToArrayDepartment; // to convert list into string array for comboBox
	String[] listToArrayInstructor;
	private static final long serialVersionUID = 1L;
	Course course;

	// database additions
	String departmentName;
	String[] instructorNamesData;
	int numberOfInstructorsForCourse = 0;

	private JLabel courseEdit_label;
	private JLabel courseEdit_CIDLabel;
	private JLabel courseEdit_cNameLabel;
	private JLabel courseEdit_instructorLabel;
	private JLabel courseEdit_departmentLabel;

	private JTextField courseEdit_CIDText;
	private JTextField courseEdit_cNameText;
	private JTextField courseEdit_instructorText;
	private JTextField courseEdit_departmentText;

	private JLabel courseEdit_CNUMLabel;
	private JTextField courseEdit_CNUMText;

	// 2 stands for user input version
	private JLabel courseEdit_label2;
	private JLabel courseEdit_CIDLabel2;
	private JLabel courseEdit_cNameLabel2;
	private JLabel courseEdit_instructorLabel2;
	private JLabel courseEdit_departmentLabel2;

	private JTextField courseEdit_CIDText2;
	private JTextField courseEdit_cNameText2;
	private JTextField courseEdit_instructorText2;
	private JTextField courseEdit_departmentText2;

	private JLabel courseEdit_CNUMLabel2;
	private JTextField courseEdit_CNUMText2;

	private JTextField courseEdit_IDText;

	private JComboBox courseEdit_departmentCombo;
	private JComboBox courseEdit_instructorCombo;

	public void linkedListToArrayDepartment() {
		listToArrayDepartment = new String[departmentLinkedListCopy.size()];
		for (int i = 0; i < departmentLinkedListCopy.size(); i++) {
			Department temp = departmentLinkedListCopy.get(i);
			listToArrayDepartment[i] = temp.getDepartmentName();
		}

	}

	public void linkedListToArrayInstructor() {
		listToArrayInstructor = new String[instructorLinkedListCopy.size()];
		for (int i = 0; i < instructorLinkedListCopy.size(); i++) {
			Instructor temp = instructorLinkedListCopy.get(i);
			listToArrayInstructor[i] = temp.getInstructorName();
		}

	}

	public String getDepartment() { // REPLACE WITH COMBO BOX
		return courseEdit_departmentText2.getText();
		// return courseAdd_departmentText.getText();
		// return (String) courseEdit_departmentCombo.getSelectedItem();
	}

	public String getInstructor() { // REPLACE WITH COMBO BOX
		// return courseAdd_departmentText.getText();
		return (String) courseEdit_instructorCombo.getSelectedItem();
	}

	public String getCourseEdit_CNUMText2() {
		return courseEdit_CNUMText2.getText();
	}

	public String getCourseEdit_CIDText2() {
		return courseEdit_CIDText2.getText();
	}

	public String getCourseEdit_cNameText2() {
		return courseEdit_cNameText2.getText();
	}

	public String getCourseEdit_instructorText2() {
		return courseEdit_instructorText2.getText();
	}

	public String getCourseEdit_departmentText2() {
		return courseEdit_departmentText2.getText();
	}

	public String getInputCourseIDText() {
		return courseEdit_IDText.getText();
	}

	public CourseEditPanel(Course temp, MyGenericList<Department> departmentLinkedListCopy,
			MyGenericList<Instructor> instructorLinkedListCopy) throws FileNotFoundException {
		this.course = temp;
		this.departmentLinkedListCopy = departmentLinkedListCopy;
		this.instructorLinkedListCopy = instructorLinkedListCopy;
		buildCourseEditPanel(course);
	}

	public CourseEditPanel(Course temp, String departmentName, String[] array) throws FileNotFoundException {
		this.course = temp;
		this.departmentName = departmentName;
		this.instructorNamesData = array;
		buildCourseEditPanel(course);
	}

	public void buildCourseEditPanel(Course course) {

		System.out.println("Department Name: " + departmentName);
		for (String i : instructorNamesData)
			System.out.println("In filtered ins: " + i);

		JPanel label_JPanel = new JPanel();
		courseEdit_label = new JLabel("[Old Course Data]");
		label_JPanel.add(courseEdit_label);

		JPanel ID_JPanel = new JPanel();
		courseEdit_CNUMLabel = new JLabel("cNum");
		String intToString = String.valueOf(course.getCNUM());
		courseEdit_CNUMText = new JTextField(intToString);
		courseEdit_CNUMText.setEditable(false);
		ID_JPanel.add(courseEdit_CNUMLabel);
		ID_JPanel.add(courseEdit_CNUMText);

		JPanel firstName_JPanel = new JPanel();
		courseEdit_CIDLabel = new JLabel("Course ID");
		courseEdit_CIDText = new JTextField(course.getCID());
		courseEdit_CIDText.setEditable(false);
		firstName_JPanel.add(courseEdit_CIDLabel);
		firstName_JPanel.add(courseEdit_CIDText);

		JPanel lastName_JPanel = new JPanel();
		courseEdit_cNameLabel = new JLabel("Course Name");
		courseEdit_cNameText = new JTextField(course.getcName());
		courseEdit_cNameText.setEditable(false);
		lastName_JPanel.add(courseEdit_cNameLabel);
		lastName_JPanel.add(courseEdit_cNameText);

		JPanel address_JPanel = new JPanel();
		courseEdit_instructorLabel = new JLabel("Instructor");
		courseEdit_instructorText = new JTextField(course.getInstructor());
		courseEdit_instructorText.setEditable(false);
		address_JPanel.add(courseEdit_instructorLabel);
		address_JPanel.add(courseEdit_instructorText);

		JPanel city_JPanel = new JPanel();
		courseEdit_departmentLabel = new JLabel("Department");
		courseEdit_departmentText = new JTextField(course.getDepartment());
		courseEdit_departmentText.setEditable(false);
		city_JPanel.add(courseEdit_departmentLabel);
		city_JPanel.add(courseEdit_departmentText);

		///// DATA 2 input
		JPanel label_JPanel2 = new JPanel();
		courseEdit_label2 = new JLabel("[New Course Data]");
		label_JPanel2.add(courseEdit_label2);

		JPanel ID_JPanel2 = new JPanel();
		courseEdit_CNUMLabel2 = new JLabel("cNum");
		String intToString2 = String.valueOf(course.getCNUM());
		courseEdit_CNUMText2 = new JTextField(intToString2);
		courseEdit_CNUMText2.setEditable(false);
		ID_JPanel2.add(courseEdit_CNUMLabel2);
		ID_JPanel2.add(courseEdit_CNUMText2);

		JPanel firstName_JPanel2 = new JPanel();
		courseEdit_CIDLabel2 = new JLabel("Course ID");
		courseEdit_CIDText2 = new JTextField(15);
		firstName_JPanel2.add(courseEdit_CIDLabel2);
		firstName_JPanel2.add(courseEdit_CIDText2);

		JPanel lastName_JPanel2 = new JPanel();
		courseEdit_cNameLabel2 = new JLabel("Course Name");
		courseEdit_cNameText2 = new JTextField(20);
		lastName_JPanel2.add(courseEdit_cNameLabel2);
		lastName_JPanel2.add(courseEdit_cNameText2);
		/*
		 * JPanel address_JPanel2 = new JPanel(); courseEdit_instructorLabel2 = new
		 * JLabel("Instructor"); courseEdit_instructorText2 = new JTextField(30);
		 * address_JPanel2.add(courseEdit_instructorLabel2);
		 * address_JPanel2.add(courseEdit_instructorText2);
		 * 
		 * JPanel city_JPanel2 = new JPanel(); courseEdit_departmentLabel2 = new
		 * JLabel("Department"); courseEdit_departmentText2 = new JTextField(15);
		 * city_JPanel2.add(courseEdit_departmentLabel2);
		 * city_JPanel2.add(courseEdit_departmentText2);
		 */
		// linkedListToArrayDepartment(); removed due to database
		// linkedListToArrayInstructor();

		JPanel city_JPanel2 = new JPanel();
		courseEdit_departmentLabel2 = new JLabel("Department");
		// courseEdit_departmentCombo = new JComboBox(listToArrayDepartment);
		// courseEdit_departmentCombo = new JComboBox(listToArrayDepartment);
		courseEdit_departmentText2 = new JTextField(departmentName);
		courseEdit_departmentText2.setEditable(false);
		city_JPanel2.add(courseEdit_departmentLabel2);
		city_JPanel2.add(courseEdit_departmentText2);

		JPanel address_JPanel2 = new JPanel();
		courseEdit_instructorLabel2 = new JLabel("Instructor");
		courseEdit_instructorCombo = new JComboBox(instructorNamesData);
		address_JPanel2.add(courseEdit_instructorLabel2);
		address_JPanel2.add(courseEdit_instructorCombo);

		///////

		JPanel oldEditData = new JPanel();
		oldEditData.setLayout(new GridLayout(7, 1));

		oldEditData.add(label_JPanel);
		oldEditData.add(ID_JPanel);
		oldEditData.add(firstName_JPanel);
		oldEditData.add(lastName_JPanel);
		oldEditData.add(city_JPanel);
		oldEditData.add(address_JPanel);

		JPanel newEditData = new JPanel();
		newEditData.setLayout(new GridLayout(7, 1));

		newEditData.add(label_JPanel2);
		newEditData.add(ID_JPanel2);
		newEditData.add(firstName_JPanel2);
		newEditData.add(lastName_JPanel2);
		newEditData.add(city_JPanel2);
		newEditData.add(address_JPanel2);

		JLabel editMenuLabel = new JLabel("Edit Course");
		JPanel editMenuLabelPanel = new JPanel();
		editMenuLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		editMenuLabelPanel.add(editMenuLabel);

		setLayout(new BorderLayout());

		add(oldEditData, BorderLayout.WEST);
		add(newEditData, BorderLayout.EAST);
		add(editMenuLabelPanel, BorderLayout.NORTH);

		JLabel imageLabel = new JLabel();
		ImageIcon arrows = new ImageIcon("rightarrow.png");
		imageLabel.setIcon(arrows);

		add(imageLabel, BorderLayout.CENTER);

	}

}

class CourseSearchPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel courseSearch_label;
	private JLabel courseSearch_inputIDLabel;
	private JTextField courseSearch_inputIDText;

	public String getInputCourseIDText() {
		return courseSearch_inputIDText.getText();
	}

	public CourseSearchPanel() throws FileNotFoundException {

		buildCourseSearchPanel();
	}

	public void buildCourseSearchPanel() {
		setLayout(new GridLayout(2, 1));

		JPanel label_JPanel = new JPanel();
		courseSearch_label = new JLabel("Search Course");
		label_JPanel.add(courseSearch_label);

		JPanel enterID_JPanel = new JPanel();
		courseSearch_inputIDLabel = new JLabel("Enter course ID to search");
		courseSearch_inputIDText = new JTextField(15);
		enterID_JPanel.add(courseSearch_inputIDLabel);
		enterID_JPanel.add(courseSearch_inputIDText);

		add(label_JPanel);
		add(enterID_JPanel);
	}

}

class EnrollmentFirstPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel enrollmentSearch_label;
	private JLabel enrollmentSearch_inputIDLabel;
	private JTextField enrollmentSearch_inputIDText;

	public String getInputStudentIDText() {
		return enrollmentSearch_inputIDText.getText();
	}

	public EnrollmentFirstPanel() throws FileNotFoundException {

		buildEnrollmentFirstPanel();
	}

	public void buildEnrollmentFirstPanel() {
		setLayout(new GridLayout(2, 1));

		JPanel label_JPanel = new JPanel();
		enrollmentSearch_label = new JLabel("Select Student");
		label_JPanel.add(enrollmentSearch_label);

		JPanel enterID_JPanel = new JPanel();
		enrollmentSearch_inputIDLabel = new JLabel("Enter student ID to search");
		enrollmentSearch_inputIDText = new JTextField(15);
		enterID_JPanel.add(enrollmentSearch_inputIDLabel);
		enterID_JPanel.add(enrollmentSearch_inputIDText);

		add(label_JPanel);
		add(enterID_JPanel);
	}

}

class EnrollmentSecondPanel extends JPanel {
	private MyGenericList<Course> courseLinkedListCopy;
	String[] listToArrayCourse; // to convert list into string array for comboBox
	private static final long serialVersionUID = 1L;
	Student student;
	Course course;
	private JLabel studentAfterAdd_label;
	private JLabel studentAfterAdd_firstNameLabel;
	private JLabel studentAfterAdd_lastNameLabel;
	private JLabel studentAfterAdd_addressLabel;
	private JLabel studentAfterAdd_cityLabel;
	private JLabel studentAfterAdd_stateLabel;
	private JTextField studentAfterAdd_firstNameText;
	private JTextField studentAfterAdd_lastNameText;
	private JTextField studentAfterAdd_addressText;
	private JTextField studentAfterAdd_cityText;
	private JTextField studentAfterAdd_stateText;
	private JLabel studentAfterAdd_IDLabel;
	private JTextField studentAfterAdd_IDText;

	private JLabel createCourseEnrollment_Label;
	private JLabel createCourseEnrollment_courseIDLabel;
	private JLabel createCourseEnrollment_yearLabel;
	private JLabel createCourseEnrollment_semesterLabel;
	private JTextField createCourseEnrollment_courseIDText;

	private JComboBox createCourseEnrollment_CourseComboBox;
	private JComboBox createCourseEnrollment_yearComboBox;
	private JComboBox createCourseEnrollment_semesterComboBox;

	String[] years = { "2020", "2021" };
	String[] semesters = { "Fall", "Winter", "Spring", "Summer" };

	// Database adds
	String[] allCourses;

	public String getStudentID() {
		return this.studentAfterAdd_IDText.getText();
	}

	public String getCourseID() { // Not used anymore because of combo
		return this.createCourseEnrollment_courseIDText.getText();
	}

	public String getYear() { // ERASE OLD CODE
		// return this.createCourseEnrollment_yearText.getText();
		return (String) this.createCourseEnrollment_yearComboBox.getSelectedItem();
	}

	public String getSemester() {
		// return this.createCourseEnrollment_semesterText.getText();
		return (String) this.createCourseEnrollment_semesterComboBox.getSelectedItem();
	}

	public String getCourse() { // REPLACE WITH COMBO BOX
		// return courseAdd_departmentText.getText();
		return (String) createCourseEnrollment_CourseComboBox.getSelectedItem();
	}

	public int getCourseIndex() {
		return createCourseEnrollment_CourseComboBox.getSelectedIndex() + 1; // offsets
	}

	public void linkedListToArrayCourse() {
		listToArrayCourse = new String[courseLinkedListCopy.size()];
		for (int i = 0; i < courseLinkedListCopy.size(); i++) {
			Course temp = courseLinkedListCopy.get(i);
			listToArrayCourse[i] = temp.getCID();
		}

	}

	// old linked list contructor
	public EnrollmentSecondPanel(Student temp, MyGenericList<Course> courseLinkedListCopy) {
		this.student = temp;
		this.courseLinkedListCopy = courseLinkedListCopy;
		buildEnrollmentSecondPanel();
	}

	public EnrollmentSecondPanel(Student temp, String[] allCourses) {
		this.student = temp;
		this.allCourses = allCourses;
		buildEnrollmentSecondPanel();
	}

	public void buildEnrollmentSecondPanel() {

		JPanel label_JPanel = new JPanel();
		studentAfterAdd_label = new JLabel("Selected Student");
		label_JPanel.add(studentAfterAdd_label);

		JPanel ID_JPanel = new JPanel();
		studentAfterAdd_IDLabel = new JLabel("Student ID");
		String intToString = String.valueOf(student.getID());
		studentAfterAdd_IDText = new JTextField(intToString);
		studentAfterAdd_IDText.setEditable(false);
		ID_JPanel.add(studentAfterAdd_IDLabel);
		ID_JPanel.add(studentAfterAdd_IDText);

		JPanel firstName_JPanel = new JPanel();
		studentAfterAdd_firstNameLabel = new JLabel("First Name");
		studentAfterAdd_firstNameText = new JTextField(student.getFirstName());
		studentAfterAdd_firstNameText.setEditable(false);
		firstName_JPanel.add(studentAfterAdd_firstNameLabel);
		firstName_JPanel.add(studentAfterAdd_firstNameText);

		JPanel lastName_JPanel = new JPanel();
		studentAfterAdd_lastNameLabel = new JLabel("Last Name");
		studentAfterAdd_lastNameText = new JTextField(student.getLastName());
		studentAfterAdd_lastNameText.setEditable(false);
		lastName_JPanel.add(studentAfterAdd_lastNameLabel);
		lastName_JPanel.add(studentAfterAdd_lastNameText);

		JPanel address_JPanel = new JPanel();
		studentAfterAdd_addressLabel = new JLabel("Address");
		studentAfterAdd_addressText = new JTextField(student.getAddress());
		studentAfterAdd_addressText.setEditable(false);
		address_JPanel.add(studentAfterAdd_addressLabel);
		address_JPanel.add(studentAfterAdd_addressText);

		JPanel city_JPanel = new JPanel();
		studentAfterAdd_cityLabel = new JLabel("City");
		studentAfterAdd_cityText = new JTextField(student.getCity());
		studentAfterAdd_cityText.setEditable(false);
		city_JPanel.add(studentAfterAdd_cityLabel);
		city_JPanel.add(studentAfterAdd_cityText);

		JPanel state_JPanel = new JPanel();
		studentAfterAdd_stateLabel = new JLabel("State");
		studentAfterAdd_stateText = new JTextField(student.getState());
		studentAfterAdd_stateText.setEditable(false);
		state_JPanel.add(studentAfterAdd_stateLabel);
		state_JPanel.add(studentAfterAdd_stateText);

		JPanel studentDisplaySecondPanel = new JPanel();
		studentDisplaySecondPanel.setLayout(new GridLayout(7, 1));

		studentDisplaySecondPanel.add(label_JPanel);
		studentDisplaySecondPanel.add(ID_JPanel);
		studentDisplaySecondPanel.add(firstName_JPanel);
		studentDisplaySecondPanel.add(lastName_JPanel);
		studentDisplaySecondPanel.add(address_JPanel);
		studentDisplaySecondPanel.add(city_JPanel);
		studentDisplaySecondPanel.add(state_JPanel);

		JPanel courseDisplaySecondPanel = new JPanel();
		courseDisplaySecondPanel.setLayout(new GridLayout(4, 1));

		JPanel courseLabel = new JPanel();
		createCourseEnrollment_Label = new JLabel("Create Enrollment");
		courseLabel.add(createCourseEnrollment_Label);

		// linkedListToArrayCourse(); not calling this anymore

		JPanel courseID_JPanel = new JPanel();
		createCourseEnrollment_courseIDLabel = new JLabel("Course ID");
		// createCourseEnrollment_courseIDText = new JTextField(4);
		// System.out.println(listToArrayCourse);
		// createCourseEnrollment_CourseComboBox = new JComboBox(listToArrayCourse);
		createCourseEnrollment_CourseComboBox = new JComboBox(allCourses);
		courseID_JPanel.add(createCourseEnrollment_courseIDLabel);
		courseID_JPanel.add(createCourseEnrollment_CourseComboBox);

		JPanel courseYear_JPanel = new JPanel();
		createCourseEnrollment_yearLabel = new JLabel("Course Year");
		// createCourseEnrollment_yearText = new JTextField(4);
		createCourseEnrollment_yearComboBox = new JComboBox(years);
		courseYear_JPanel.add(createCourseEnrollment_yearLabel);
		courseYear_JPanel.add(createCourseEnrollment_yearComboBox);

		JPanel courseSemester_JPanel = new JPanel();
		createCourseEnrollment_semesterLabel = new JLabel("Course Semester");
		// createCourseEnrollment_semesterText = new JTextField(8);
		createCourseEnrollment_semesterComboBox = new JComboBox(semesters);
		courseSemester_JPanel.add(createCourseEnrollment_semesterLabel);
		courseSemester_JPanel.add(createCourseEnrollment_semesterComboBox);

		courseDisplaySecondPanel.add(courseLabel);
		courseDisplaySecondPanel.add(courseID_JPanel);
		courseDisplaySecondPanel.add(courseYear_JPanel);
		courseDisplaySecondPanel.add(courseSemester_JPanel);

		JLabel createEnrollmentLabel = new JLabel("Enrollment");
		JPanel createEnrollmentPanel = new JPanel();
		createEnrollmentLabel.setLayout(new FlowLayout(FlowLayout.CENTER));
		createEnrollmentPanel.add(createEnrollmentLabel);

		setLayout(new BorderLayout());

		add(studentDisplaySecondPanel, BorderLayout.WEST);
		add(courseDisplaySecondPanel, BorderLayout.EAST);
		add(createEnrollmentPanel, BorderLayout.NORTH);

	}
}

class EnrollmentDisplayPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Enrollment enrollment;
	private JLabel enrollmentDisplay_label;
	private JLabel enrollmentDisplay_EnrollmentIDLabel;
	private JLabel enrollmentDisplay_CourseIDLabel;
	private JLabel enrollmentDisplay_StudentIDLabel;
	private JLabel enrollmentDisplay_YearLabel;
	private JLabel enrollmentDisplay_SemesterLabel;
	private JLabel enrollmentDisplay_GradeLabel;

	private JTextField enrollmentDisplay_EnrollmentIDText;
	private JTextField enrollmentDisplay_CourseIDText;
	private JTextField enrollmentDisplay_StudentIDText;
	private JTextField enrollmentDisplay_YearText;
	private JTextField enrollmentDisplay_SemesterText;
	private JTextField enrollmentDisplay_GradeText;

	public EnrollmentDisplayPanel(Enrollment enrollment) {

		this.enrollment = enrollment;
		buildEnrollmentDisplayPanel();
	}

	public void buildEnrollmentDisplayPanel() {
		setLayout(new GridLayout(7, 1));

		JPanel label_JPanel = new JPanel();
		enrollmentDisplay_label = new JLabel("Display Enrollment");
		label_JPanel.add(enrollmentDisplay_label);

		JPanel enrollmentID_JPanel = new JPanel();
		enrollmentDisplay_EnrollmentIDLabel = new JLabel("Enrollment ID");
		String intToString = String.valueOf(enrollment.getEID());
		enrollmentDisplay_EnrollmentIDText = new JTextField(intToString);
		enrollmentDisplay_EnrollmentIDText.setEditable(false);
		enrollmentID_JPanel.add(enrollmentDisplay_EnrollmentIDLabel);
		enrollmentID_JPanel.add(enrollmentDisplay_EnrollmentIDText);

		JPanel courseID_JPanel = new JPanel();
		enrollmentDisplay_CourseIDLabel = new JLabel("Course ID");
		String intToString2 = String.valueOf(enrollment.getcNum());
		enrollmentDisplay_CourseIDText = new JTextField(intToString2);
		enrollmentDisplay_CourseIDText.setEditable(false);
		courseID_JPanel.add(enrollmentDisplay_CourseIDLabel);
		courseID_JPanel.add(enrollmentDisplay_CourseIDText);

		JPanel studentID_JPanel = new JPanel();
		enrollmentDisplay_StudentIDLabel = new JLabel("Student ID");
		String intToString3 = String.valueOf(enrollment.getStudentID());
		enrollmentDisplay_StudentIDText = new JTextField(intToString3);
		enrollmentDisplay_StudentIDText.setEditable(false);
		studentID_JPanel.add(enrollmentDisplay_StudentIDLabel);
		studentID_JPanel.add(enrollmentDisplay_StudentIDText);

		JPanel year_JPanel = new JPanel();
		enrollmentDisplay_YearLabel = new JLabel("Year");
		enrollmentDisplay_YearText = new JTextField(enrollment.getYear());
		enrollmentDisplay_YearText.setEditable(false);
		year_JPanel.add(enrollmentDisplay_YearLabel);
		year_JPanel.add(enrollmentDisplay_YearText);

		JPanel semester_JPanel = new JPanel();
		enrollmentDisplay_SemesterLabel = new JLabel("Semester");
		enrollmentDisplay_SemesterText = new JTextField(enrollment.getSemester());
		enrollmentDisplay_SemesterText.setEditable(false);
		semester_JPanel.add(enrollmentDisplay_SemesterLabel);
		semester_JPanel.add(enrollmentDisplay_SemesterText);

		JPanel grade_JPanel = new JPanel();
		enrollmentDisplay_GradeLabel = new JLabel("Grade");
		enrollmentDisplay_GradeText = new JTextField(enrollment.getGrade());
		enrollmentDisplay_GradeText.setEditable(false);
		grade_JPanel.add(enrollmentDisplay_GradeLabel);
		grade_JPanel.add(enrollmentDisplay_GradeText);

		add(label_JPanel);
		add(enrollmentID_JPanel);
		add(courseID_JPanel);
		add(studentID_JPanel);
		add(year_JPanel);
		add(semester_JPanel);
		add(grade_JPanel);

	}

}

class EnrollmentEditPanel extends JPanel {
	private MyGenericList<Student> studentLinkedListCopy;
	private MyGenericList<Course> courseLinkedListCopy;
	String[] listToArrayCourse;
	String[] listToArrayStudent;
	String[] years = { "2020", "2021" };
	String[] semesters = { "Fall", "Winter", "Spring", "Summer" };

	// Database additions
	String[] allCourses;
	String[] allStudents;

	private static final long serialVersionUID = 1L;
	Enrollment enrollment;
	private JLabel enrollmentDisplay_label;
	private JLabel enrollmentDisplay_EnrollmentIDLabel;
	private JLabel enrollmentDisplay_CourseIDLabel;
	private JLabel enrollmentDisplay_StudentIDLabel;
	private JLabel enrollmentDisplay_YearLabel;
	private JLabel enrollmentDisplay_SemesterLabel;
	private JLabel enrollmentDisplay_GradeLabel;

	private JTextField enrollmentDisplay_EnrollmentIDText;
	private JTextField enrollmentDisplay_CourseIDText;
	private JTextField enrollmentDisplay_StudentIDText;
	private JTextField enrollmentDisplay_YearText;
	private JTextField enrollmentDisplay_SemesterText;
	private JTextField enrollmentDisplay_GradeText;

	// 2 stands for user input version
	private JLabel enrollmentDisplay_label2;
	private JLabel enrollmentDisplay_EnrollmentIDLabel2;
	private JLabel enrollmentDisplay_CourseIDLabel2;
	private JLabel enrollmentDisplay_StudentIDLabel2;
	private JLabel enrollmentDisplay_YearLabel2;
	private JLabel enrollmentDisplay_SemesterLabel2;
	private JLabel enrollmentDisplay_GradeLabel2;

	private JTextField enrollmentDisplay_EnrollmentIDText2;
	private JTextField enrollmentDisplay_CourseIDText2;
	private JTextField enrollmentDisplay_StudentIDText2;
	private JTextField enrollmentDisplay_YearText2;
	private JTextField enrollmentDisplay_SemesterText2;
	private JTextField enrollmentDisplay_GradeText2;

	private JComboBox enrollmentDisplay_courseComboBox;
	private JComboBox enrollmentDisplay_studentComboBox;
	private JComboBox enrollmentDisplay_yearComboBox;
	private JComboBox enrollmentDisplay_semesterComboBox;

	public String getEnrollmentDisplay_EnrollmentIDText2() {
		return enrollmentDisplay_EnrollmentIDText2.getText();
	}

	public String getEnrollmentDisplay_CourseIDText2() {
		return enrollmentDisplay_CourseIDText2.getText();
	}

	public String getEnrollmentDisplay_StudentIDText2() {
		return enrollmentDisplay_StudentIDText2.getText();
	}

	public String getEnrollmentDisplay_YearText2() {
		return enrollmentDisplay_YearText2.getText();
	}

	public String getEnrollmentDisplay_SemesterText2() {
		return enrollmentDisplay_SemesterText2.getText();
	}

	public String getEnrollmentDisplay_GradeText2() {
		return enrollmentDisplay_GradeText2.getText();
	}

	public String getCourse() { // REPLACE WITH COMBO BOX
		// return courseAdd_departmentText.getText();
		return (String) enrollmentDisplay_courseComboBox.getSelectedItem();
	}

	public String getStudent() { // REPLACE WITH COMBO BOX
		// return courseAdd_departmentText.getText();
		return (String) enrollmentDisplay_studentComboBox.getSelectedItem();
	}

	public int getCourseIndex() {
		return enrollmentDisplay_courseComboBox.getSelectedIndex() + 1; // offsets
	}

	public int getStudentIndex() {
		return enrollmentDisplay_studentComboBox.getSelectedIndex() + 1; // offsets
	}

	public String getEnrollmentDisplay_YearText() { // ERASE OLD CODE
		// return this.gradeIntro_YearText.getText();
		return (String) enrollmentDisplay_yearComboBox.getSelectedItem();
	}

	public String getEnrollmentDisplay_SemesterText() { // ERASE OLD CODE
		// return this.gradeIntro_SemesterText.getText();
		return (String) enrollmentDisplay_semesterComboBox.getSelectedItem();
	}

	public void linkedListToArrayCourse() {
		listToArrayCourse = new String[courseLinkedListCopy.size()];
		for (int i = 0; i < courseLinkedListCopy.size(); i++) {
			Course temp = courseLinkedListCopy.get(i);
			listToArrayCourse[i] = temp.getCID();
		}
	}

	public void linkedListToArrayStudent() {
		listToArrayStudent = new String[studentLinkedListCopy.size()];
		for (int i = 0; i < studentLinkedListCopy.size(); i++) {
			Student temp = studentLinkedListCopy.get(i);
			listToArrayStudent[i] = temp.getFirstName().charAt(0) + " " + temp.getLastName();
		}
	}

	public EnrollmentEditPanel(Enrollment temp, MyGenericList<Student> studentLinkedListCopy,
			MyGenericList<Course> courseLinkedListCopy) throws FileNotFoundException {
		this.enrollment = temp;
		this.studentLinkedListCopy = studentLinkedListCopy;
		this.courseLinkedListCopy = courseLinkedListCopy;
		buildCourseEditPanel(enrollment);
	}

	public EnrollmentEditPanel(Enrollment temp, String[] allCourses, String[] allStudents)
			throws FileNotFoundException {
		this.enrollment = temp;
		this.allCourses = allCourses;
		this.allStudents = allStudents;
		buildCourseEditPanel(enrollment);
	}

	public void buildCourseEditPanel(Enrollment enrollment) {

		JPanel label_JPanel = new JPanel();
		enrollmentDisplay_label = new JLabel("[Old Enrollment Data]");
		label_JPanel.add(enrollmentDisplay_label);

		JPanel EID_JPanel = new JPanel();
		enrollmentDisplay_EnrollmentIDLabel = new JLabel("EID");
		String intToString = String.valueOf(enrollment.getEID());
		enrollmentDisplay_EnrollmentIDText = new JTextField(intToString);
		enrollmentDisplay_EnrollmentIDText.setEditable(false);
		EID_JPanel.add(enrollmentDisplay_EnrollmentIDLabel);
		EID_JPanel.add(enrollmentDisplay_EnrollmentIDText);

		JPanel CID_JPanel = new JPanel();
		enrollmentDisplay_CourseIDLabel = new JLabel("Course ID");
		String intToString2 = String.valueOf(enrollment.getcNum());
		enrollmentDisplay_CourseIDText = new JTextField(intToString2);
		enrollmentDisplay_CourseIDText.setEditable(false);
		CID_JPanel.add(enrollmentDisplay_CourseIDLabel);
		CID_JPanel.add(enrollmentDisplay_CourseIDText);

		JPanel SID_JPanel = new JPanel();
		enrollmentDisplay_StudentIDLabel = new JLabel("Student ID");
		String intToString3 = String.valueOf(enrollment.getStudentID());
		enrollmentDisplay_StudentIDText = new JTextField(intToString3);
		enrollmentDisplay_StudentIDText.setEditable(false);
		SID_JPanel.add(enrollmentDisplay_StudentIDLabel);
		SID_JPanel.add(enrollmentDisplay_StudentIDText);

		JPanel Year_JPanel = new JPanel();
		enrollmentDisplay_YearLabel = new JLabel("Year");
		enrollmentDisplay_YearText = new JTextField(enrollment.getYear());
		enrollmentDisplay_YearText.setEditable(false);
		Year_JPanel.add(enrollmentDisplay_YearLabel);
		Year_JPanel.add(enrollmentDisplay_YearText);

		JPanel Semester_JPanel = new JPanel();
		enrollmentDisplay_SemesterLabel = new JLabel("Semester");
		enrollmentDisplay_SemesterText = new JTextField(enrollment.getSemester());
		enrollmentDisplay_SemesterText.setEditable(false);
		Semester_JPanel.add(enrollmentDisplay_SemesterLabel);
		Semester_JPanel.add(enrollmentDisplay_SemesterText);

		JPanel Grade_JPanel = new JPanel();
		enrollmentDisplay_GradeLabel = new JLabel("Grade");
		enrollmentDisplay_GradeText = new JTextField(enrollment.getGrade());
		enrollmentDisplay_GradeText.setEditable(false);
		Grade_JPanel.add(enrollmentDisplay_GradeLabel);
		Grade_JPanel.add(enrollmentDisplay_GradeText);

		// linkedListToArrayCourse(); Removed due to databases
		// linkedListToArrayStudent();
		///// DATA 2 input
		JPanel label_JPanel2 = new JPanel();
		enrollmentDisplay_label2 = new JLabel("[New Enrollment Data]");
		label_JPanel2.add(enrollmentDisplay_label2);

		JPanel EID_JPanel2 = new JPanel();
		enrollmentDisplay_EnrollmentIDLabel2 = new JLabel("EID");
		String intToString4 = String.valueOf(enrollment.getEID());
		enrollmentDisplay_EnrollmentIDText2 = new JTextField(intToString4);
		enrollmentDisplay_EnrollmentIDText2.setEditable(false);
		EID_JPanel2.add(enrollmentDisplay_EnrollmentIDLabel2);
		EID_JPanel2.add(enrollmentDisplay_EnrollmentIDText2);

		JPanel CID_JPanel2 = new JPanel();
		enrollmentDisplay_CourseIDLabel2 = new JLabel("Course ID");
		// enrollmentDisplay_CourseIDText2 = new JTextField(4);
		// enrollmentDisplay_courseComboBox = new JComboBox(listToArrayCourse);
		enrollmentDisplay_courseComboBox = new JComboBox(allCourses);
		CID_JPanel2.add(enrollmentDisplay_CourseIDLabel2);
		CID_JPanel2.add(enrollmentDisplay_courseComboBox);

		JPanel SID_JPanel2 = new JPanel();
		enrollmentDisplay_StudentIDLabel2 = new JLabel("Student ID");
		// enrollmentDisplay_StudentIDText2 = new JTextField(4);
		enrollmentDisplay_studentComboBox = new JComboBox(allStudents);
		SID_JPanel2.add(enrollmentDisplay_StudentIDLabel2);
		SID_JPanel2.add(enrollmentDisplay_studentComboBox);

		JPanel Year_JPanel2 = new JPanel();
		enrollmentDisplay_YearLabel2 = new JLabel("Year");
		// enrollmentDisplay_YearText2 = new JTextField(4);
		enrollmentDisplay_yearComboBox = new JComboBox(years);
		Year_JPanel2.add(enrollmentDisplay_YearLabel2);
		Year_JPanel2.add(enrollmentDisplay_yearComboBox);

		JPanel Semester_JPanel2 = new JPanel();
		enrollmentDisplay_SemesterLabel2 = new JLabel("Semester");
		// enrollmentDisplay_SemesterText2 = new JTextField(9);
		enrollmentDisplay_semesterComboBox = new JComboBox(semesters);
		Semester_JPanel2.add(enrollmentDisplay_SemesterLabel2);
		Semester_JPanel2.add(enrollmentDisplay_semesterComboBox);

		JPanel Grade_JPanel2 = new JPanel();
		enrollmentDisplay_GradeLabel2 = new JLabel("Grade");
		enrollmentDisplay_GradeText2 = new JTextField(enrollment.getGrade());
		enrollmentDisplay_GradeText2.setEditable(false);
		Grade_JPanel2.add(enrollmentDisplay_GradeLabel2);
		Grade_JPanel2.add(enrollmentDisplay_GradeText2);

		///////

		JPanel oldEditData = new JPanel();
		oldEditData.setLayout(new GridLayout(7, 1));

		oldEditData.add(label_JPanel);
		oldEditData.add(EID_JPanel);
		oldEditData.add(CID_JPanel);
		oldEditData.add(SID_JPanel);
		oldEditData.add(Year_JPanel);
		oldEditData.add(Semester_JPanel);
		oldEditData.add(Grade_JPanel);

		JPanel newEditData = new JPanel();
		newEditData.setLayout(new GridLayout(7, 1));

		newEditData.add(label_JPanel2);
		newEditData.add(EID_JPanel2);
		newEditData.add(CID_JPanel2);
		newEditData.add(SID_JPanel2);
		newEditData.add(Year_JPanel2);
		newEditData.add(Semester_JPanel2);
		newEditData.add(Grade_JPanel2);

		JLabel editMenuLabel = new JLabel("Edit Enrollment");
		JPanel editMenuLabelPanel = new JPanel();
		editMenuLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		editMenuLabelPanel.add(editMenuLabel);

		setLayout(new BorderLayout());

		add(oldEditData, BorderLayout.WEST);
		add(newEditData, BorderLayout.EAST);
		add(editMenuLabelPanel, BorderLayout.NORTH);

		JPanel arrowPanel = new JPanel();
		JLabel imageLabel = new JLabel();
		ImageIcon arrows = new ImageIcon("rightarrow.png");
		imageLabel.setIcon(arrows);

		arrowPanel.add(imageLabel);

		// add(imageLabel, BorderLayout.CENTER);
		add(arrowPanel, BorderLayout.CENTER);

	}

}

class EnrollmentSearchPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel enrollmentSearch_label;
	private JLabel enrollmentSearch_inputIDLabel;
	private JTextField enrollmentSearch_inputIDText;

	public String getInputEnrollmentIDText() {
		return enrollmentSearch_inputIDText.getText();
	}

	public EnrollmentSearchPanel() throws FileNotFoundException {

		buildEnrollmentSearchPanel();
	}

	public void buildEnrollmentSearchPanel() {
		setLayout(new GridLayout(2, 1));

		JPanel label_JPanel = new JPanel();
		enrollmentSearch_label = new JLabel("Search Enrollment");
		label_JPanel.add(enrollmentSearch_label);

		JPanel enterID_JPanel = new JPanel();
		enrollmentSearch_inputIDLabel = new JLabel("Enter enrollment ID to search");
		enrollmentSearch_inputIDText = new JTextField(15);
		enterID_JPanel.add(enrollmentSearch_inputIDLabel);
		enterID_JPanel.add(enrollmentSearch_inputIDText);

		add(label_JPanel);
		add(enterID_JPanel);
	}

}

class GradeAddPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel gradeIntro_Label;
	private JLabel gradeIntro_SIDLabel;
	private JLabel gradeIntro_YearLabel;
	private JLabel gradeIntro_SemesterLabel;

	private JTextField gradeIntro_SIDText;
	private JTextField gradeIntro_YearText;
	private JTextField gradeIntro_SemesterText;

	private JComboBox gradeIntro_yearComboBox;
	private JComboBox gradeIntro_semesterComboBox;

	String[] years = { "2020", "2021" };
	String[] semesters = { "Fall", "Winter", "Spring", "Summer" };

	public String getGradeIntro_SIDText() {
		return this.gradeIntro_SIDText.getText();
	}

	public String getIntro_YearText() { // ERASE OLD CODE
		// return this.gradeIntro_YearText.getText();
		return (String) gradeIntro_yearComboBox.getSelectedItem();
	}

	public String getIntro_SemesterText() { // ERASE OLD CODE
		// return this.gradeIntro_SemesterText.getText();
		return (String) gradeIntro_semesterComboBox.getSelectedItem();
	}

	public GradeAddPanel() throws FileNotFoundException {
		buildGradePanel();
	}

	public void buildGradePanel() { // ERASE OLD CODE
		setLayout(new GridLayout(4, 1));

		JPanel label_JPanel = new JPanel();
		gradeIntro_Label = new JLabel("Select Student and Term");
		label_JPanel.add(gradeIntro_Label);

		JPanel SID_JPanel = new JPanel();
		gradeIntro_SIDLabel = new JLabel("Student ID");
		gradeIntro_SIDText = new JTextField(4);
		SID_JPanel.add(gradeIntro_SIDLabel);
		SID_JPanel.add(gradeIntro_SIDText);

		JPanel Year_JPanel = new JPanel();
		gradeIntro_YearLabel = new JLabel("Year");
		// gradeIntro_YearText = new JTextField(4);
		gradeIntro_yearComboBox = new JComboBox(years);
		Year_JPanel.add(gradeIntro_YearLabel);
		Year_JPanel.add(gradeIntro_yearComboBox);

		JPanel Semester_JPanel = new JPanel();
		gradeIntro_SemesterLabel = new JLabel("Semester");
		// gradeIntro_SemesterText = new JTextField(9);
		gradeIntro_semesterComboBox = new JComboBox(semesters);
		Semester_JPanel.add(gradeIntro_SemesterLabel);
		Semester_JPanel.add(gradeIntro_semesterComboBox);

		add(label_JPanel);
		add(SID_JPanel);
		add(Year_JPanel);
		add(Semester_JPanel);
	}

}

class PostGradePanelOLD extends JPanel {
	/**
	 * 
	 */
	Enrollment enrollment;
	EnrollmentFile enrollmentRecord;
	int readValue; // reading enrollment file for each SID
	int studentID; // SID Value entered by user
	private static final long serialVersionUID = 1L;
	private JTextField enrollmentSearch_inputIDText;
	private JLabel postPanel_GradeLabel;
	private JTextArea postPanel_GradeTextArea;
	private JLabel postPanel_EIDLabel;
	private JTextField postPanel_EIDText;
	private JLabel postPanel_NewGradeLabel;
	// private JTextField postPanel_NewGradeText; old

	private JComboBox postPanel_gradesComboBox;

	String[] grades = { "A", "B", "C", "D", "F", "W", "I" };

	public String getPostPanel_EIDText() {
		return this.postPanel_EIDText.getText();
	}

	public String getPostPanel_NewGradeText() {
		// return this.postPanel_NewGradeText.getText();
		return (String) postPanel_gradesComboBox.getSelectedItem();
	}

	public String getInputStudentIDText() {
		return enrollmentSearch_inputIDText.getText();
	}

	public PostGradePanelOLD() throws FileNotFoundException {

		buildPostGradePanel();
	}

	public void apphendTextArea(String string) {
		postPanel_GradeTextArea.append(string);
	}

	public void setRows(int num) {
		this.postPanel_GradeTextArea.setRows(num);
	}

	public void buildPostGradePanel() {
		setLayout(new BorderLayout());

		JPanel label_JPanel = new JPanel();
		postPanel_GradeLabel = new JLabel("Enrolled Classes");
		label_JPanel.add(postPanel_GradeLabel);

		JPanel textArea_JPanel = new JPanel();
		postPanel_GradeTextArea = new JTextArea(
				"EID    Course    SID         Year         Semester" + "        Grade \n", 10, 10);
		postPanel_GradeTextArea.setEditable(false);
		textArea_JPanel.add(postPanel_GradeTextArea);

		JPanel course_JPanel = new JPanel();
		postPanel_EIDLabel = new JLabel("Enter EID #");
		postPanel_EIDText = new JTextField(9);
		course_JPanel.add(postPanel_EIDLabel);
		course_JPanel.add(postPanel_EIDText);

		JPanel newGrade_JPanel = new JPanel();
		postPanel_NewGradeLabel = new JLabel("Enter New Grade");
		// postPanel_NewGradeText = new JTextField(9);
		postPanel_gradesComboBox = new JComboBox(grades);
		newGrade_JPanel.add(postPanel_NewGradeLabel);
		newGrade_JPanel.add(postPanel_gradesComboBox);

		JPanel inputPanel = new JPanel();
		inputPanel.add(course_JPanel);
		inputPanel.add(newGrade_JPanel);

		add(label_JPanel, BorderLayout.NORTH);
		add(textArea_JPanel, BorderLayout.CENTER);
		add(inputPanel, BorderLayout.SOUTH);

	}

}

//replacing text area with Jtable
class PostGradePanel extends JPanel {
	/**
	 * 
	 */
	Enrollment enrollment;
	EnrollmentFile enrollmentRecord;

	// database additions
	String[][] filteredEnrollmentData;

	int readValue; // reading enrollment file for each SID
	int studentID; // SID Value entered by user
	private static final long serialVersionUID = 1L;
	private JTextField enrollmentSearch_inputIDText;
	private JLabel postPanel_GradeLabel;
	private JTextArea postPanel_GradeTextArea;
	private JLabel postPanel_EIDLabel;
	private JTextField postPanel_EIDText;
	private JLabel postPanel_NewGradeLabel;
	// private JTextField postPanel_NewGradeText; old

	private JComboBox postPanel_gradesComboBox;

	String[] grades = { "A", "B", "C", "D", "F", "W", "I" };

	public String getPostPanel_EIDText() {
		return this.postPanel_EIDText.getText();
	}

	public String getPostPanel_NewGradeText() {
		// return this.postPanel_NewGradeText.getText();
		return (String) postPanel_gradesComboBox.getSelectedItem();
	}

	public String getInputStudentIDText() {
		return enrollmentSearch_inputIDText.getText();
	}

	public PostGradePanel(String[][] tableArray) throws FileNotFoundException {
		this.filteredEnrollmentData = tableArray;
		buildPostGradePanel();
	}

	public void apphendTextArea(String string) {
		postPanel_GradeTextArea.append(string);
	}

	public void setRows(int num) {
		this.postPanel_GradeTextArea.setRows(num);
	}

	public void buildPostGradePanel() {
		setLayout(new BorderLayout());

		JPanel label_JPanel = new JPanel();
		postPanel_GradeLabel = new JLabel("Enrolled Classes");
		label_JPanel.add(postPanel_GradeLabel);

		JPanel panel = new JPanel();
		String data[][] = filteredEnrollmentData;
		String column[] = { "EID", "SID", "CNUM", "Year", "Semester", "Grade" };
		JTable table = new JTable(data, column);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setPreferredSize(new Dimension(100, 6000));
		JTableHeader header = table.getTableHeader();
		header.setBackground(Color.yellow);
		JScrollPane pane = new JScrollPane(table);
		panel.add(pane);
		// enrollmentTableFrame.add(panel);
		// enrollmentTableFrame.setSize(500, 400);
		// departmentTableFrame.setUndecorated(true); removed causing an error when you
		// cycle it
		// enrollmentTableFrame.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// enrollmentTableFrame.setVisible(true);

		JPanel course_JPanel = new JPanel();
		postPanel_EIDLabel = new JLabel("Enter EID #");
		postPanel_EIDText = new JTextField(9);
		course_JPanel.add(postPanel_EIDLabel);
		course_JPanel.add(postPanel_EIDText);

		JPanel newGrade_JPanel = new JPanel();
		postPanel_NewGradeLabel = new JLabel("Enter New Grade");
		// postPanel_NewGradeText = new JTextField(9);
		postPanel_gradesComboBox = new JComboBox(grades);
		newGrade_JPanel.add(postPanel_NewGradeLabel);
		newGrade_JPanel.add(postPanel_gradesComboBox);

		JPanel inputPanel = new JPanel();
		inputPanel.add(course_JPanel);
		inputPanel.add(newGrade_JPanel);

		add(label_JPanel, BorderLayout.NORTH);
		add(panel, BorderLayout.CENTER);
		add(inputPanel, BorderLayout.SOUTH);

	}

}

class ReportPrintPanel extends JPanel {

	private MyGenericList<Course> courseLinkedListCopy;
	String[] listToArrayCourse;

	// Database additions
	String[] allCourses;
	String[][] filteredEnrollmentData;

	private static final long serialVersionUID = 1L;
	private JLabel mainPanel_ReportPrint_Label;
	private JLabel mainPanel_ReportPrint_YearLabel;
	private JComboBox mainPanel_ReportPrint_YearComboBox;
	private JLabel mainPanel_ReportPrint_CourseLabel;
	// private JTextField mainPanel_ReportPrint_CourseText;
	private JComboBox mainPanel_ReportPrint_CourseComboBox;
	private JTextArea mainPanel_Report_TextArea;

	String[] years = { "2020", "2021" };

	// old linked list constructor
	public ReportPrintPanel(MyGenericList<Course> courseLinkedListCopy) {
		this.courseLinkedListCopy = courseLinkedListCopy;
		buildReportPrintPanel();
	}

	// new database constructor
	public ReportPrintPanel(String[] allCourses, String[][] filteredEnrollmentData) {
		this.allCourses = allCourses;
		this.filteredEnrollmentData = filteredEnrollmentData;
		buildReportPrintPanel();
	}

	public void apphendTextArea(String string) {
		mainPanel_Report_TextArea.append(string);
	}

	// old linked list version
	public void buildReportPrintPanelOLD() {
		setLayout(new BorderLayout());

		JPanel label_JPanel = new JPanel();
		mainPanel_ReportPrint_Label = new JLabel("Report Print");
		label_JPanel.add(mainPanel_ReportPrint_Label);

		String[] headers = { "EID", "Course #", "SID", "Year", "Semester", "Grade" };
		JPanel textArea_JPanel = new JPanel();

		mainPanel_Report_TextArea = new JTextArea(
				String.format("%-10s", headers[0]) + String.format("%-15s", headers[1])
						+ String.format("%-20s", headers[2]) + String.format("%-20s", headers[3])
						+ String.format("%-22s", headers[4]) + String.format("%s", headers[5]) + "\n",
				10, 10);
		mainPanel_Report_TextArea.append("----------------------------------------------------------------------------"
				+ "------------------------\n");
		mainPanel_Report_TextArea.setLineWrap(false);
		mainPanel_Report_TextArea.setEditable(false);
		textArea_JPanel.add(mainPanel_Report_TextArea);

		JPanel year_JPanel = new JPanel();
		mainPanel_ReportPrint_YearLabel = new JLabel("Enter Year");
		mainPanel_ReportPrint_YearComboBox = new JComboBox(years);
		year_JPanel.add(mainPanel_ReportPrint_YearLabel);
		year_JPanel.add(mainPanel_ReportPrint_YearComboBox);

		linkedListToArrayCourse(); // Needed to populate the arrayCourse string

		JPanel course_JPanel = new JPanel();
		mainPanel_ReportPrint_CourseLabel = new JLabel("Enter Course Number");
		// mainPanel_ReportPrint_CourseText = new JTextField(9);
		mainPanel_ReportPrint_CourseComboBox = new JComboBox(listToArrayCourse);
		course_JPanel.add(mainPanel_ReportPrint_CourseLabel);
		course_JPanel.add(mainPanel_ReportPrint_CourseComboBox);

		JPanel inputPanel = new JPanel();
		inputPanel.add(year_JPanel);
		inputPanel.add(course_JPanel);

		add(label_JPanel, BorderLayout.NORTH);
		add(textArea_JPanel, BorderLayout.CENTER);
		add(inputPanel, BorderLayout.SOUTH);
	}

	// new database version
	public void buildReportPrintPanel() {
		setLayout(new BorderLayout());

		JPanel label_JPanel = new JPanel();
		mainPanel_ReportPrint_Label = new JLabel("Report Print");
		label_JPanel.add(mainPanel_ReportPrint_Label);

		JPanel panel = new JPanel();
		String data[][] = filteredEnrollmentData;
		String column[] = { "EID", "SID", "CNUM", "Year", "Semester", "Grade" };
		JTable table = new JTable(data, column);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setPreferredSize(new Dimension(100, 6000));
		JTableHeader header = table.getTableHeader();
		header.setBackground(Color.yellow);
		JScrollPane pane = new JScrollPane(table);
		panel.add(pane);

		JPanel year_JPanel = new JPanel();
		mainPanel_ReportPrint_YearLabel = new JLabel("Enter Year");
		mainPanel_ReportPrint_YearComboBox = new JComboBox(years);
		year_JPanel.add(mainPanel_ReportPrint_YearLabel);
		year_JPanel.add(mainPanel_ReportPrint_YearComboBox);

		// linkedListToArrayCourse(); // Needed to populate the arrayCourse string

		JPanel course_JPanel = new JPanel();
		mainPanel_ReportPrint_CourseLabel = new JLabel("Enter Course Number");
		// mainPanel_ReportPrint_CourseText = new JTextField(9);
		mainPanel_ReportPrint_CourseComboBox = new JComboBox(allCourses);
		course_JPanel.add(mainPanel_ReportPrint_CourseLabel);
		course_JPanel.add(mainPanel_ReportPrint_CourseComboBox);

		JPanel inputPanel = new JPanel();
		inputPanel.add(year_JPanel);
		inputPanel.add(course_JPanel);

		add(label_JPanel, BorderLayout.NORTH);
		add(panel, BorderLayout.CENTER);
		add(inputPanel, BorderLayout.SOUTH);
	}

	public String getMainPanel_ReportPrint_YearText() {
		return (String) mainPanel_ReportPrint_YearComboBox.getSelectedItem();
	}

	public String getMainPanel_ReportPrint_CourseText() {
		return (String) mainPanel_ReportPrint_CourseComboBox.getSelectedItem();
	}

	public int getCourseIndex() {
		return mainPanel_ReportPrint_CourseComboBox.getSelectedIndex() + 1; // offsets
	}

	public void linkedListToArrayCourse() {
		listToArrayCourse = new String[courseLinkedListCopy.size()];
		for (int i = 0; i < courseLinkedListCopy.size(); i++) {
			Course temp = courseLinkedListCopy.get(i);
			listToArrayCourse[i] = temp.getCID();
		}
	}
}

class DepartmentAddPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5149000693352260886L;
	private JLabel departmentAdd_label;
	private JLabel departmentAdd_nameLabel;
	private JTextField departmentAdd_nameText;
	private JTable table;

	public String getDepartmentName() {
		return departmentAdd_nameText.getText();
	}

	public DepartmentAddPanel() throws FileNotFoundException {
		buildDepartmentAddPanel();
	}

	public void buildDepartmentAddPanel() { // ERASE OLD NOTES
		setLayout(new GridLayout(3, 1));

		JPanel label_JPanel = new JPanel();
		departmentAdd_label = new JLabel("Add Department");
		label_JPanel.add(departmentAdd_label);

		JPanel departmentName_JPanel = new JPanel();
		departmentAdd_nameLabel = new JLabel("Department Name");
		departmentAdd_nameText = new JTextField(20); // changed to 20 from 15 check
		departmentName_JPanel.add(departmentAdd_nameLabel);
		departmentName_JPanel.add(departmentAdd_nameText);

		/*
		 * JFrame frame = new JFrame("Department Table"); JPanel panel = new JPanel();
		 * String data[][]={ {"101","Amit"}, {"102","Jai"},}; String
		 * column[]={"DID","departmentNAME"}; JTable table = new JTable(data,column);
		 * table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		 * table.setPreferredSize(new Dimension(100,6000)); JTableHeader header =
		 * table.getTableHeader(); header.setBackground(Color.yellow); JScrollPane pane
		 * = new JScrollPane(table); panel.add(pane); frame.add(panel);
		 * frame.setSize(500,200); frame.setUndecorated(true);
		 * frame.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
		 * //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 * frame.setVisible(true);
		 */

		/*
		 * String data[][]={ {"101","Amit","670000"}, {"102","Jai","780000"},
		 * {"101","Sachin","700000"}, {"101","Sachin","700000"},
		 * {"101","Sachin","700000"}, {"101","Sachin","700000"},
		 * {"101","Sachin","700000"}, {"101","Sachin","700000"}}; String
		 * column[]={"ID","NAME","SALARY"};
		 * 
		 * JPanel table_JPanel = new JPanel(); table = new JTable(data,column);
		 * table.setEnabled(false); JScrollPane sp=new JScrollPane(table);
		 * table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); table_JPanel.add(table);
		 * table_JPanel.add(sp);
		 */

		add(label_JPanel);
		add(departmentName_JPanel);
		// add(panel);

		// add(table);
	}

	public Department createDepartment(int numberOfStudentsInFile) {
		String departmentName;

		departmentName = departmentAdd_nameText.getText();

		Department newDepartment = new Department(numberOfStudentsInFile, departmentName);
		return newDepartment;

	}

	public void DepartmentAddDisplayPanel() {

		setLayout(new GridLayout(2, 1));

		JPanel label_JPanel1 = new JPanel();
		departmentAdd_label = new JLabel("Department Details");
		label_JPanel1.add(departmentAdd_label);

		JPanel departmentName_JPanel1 = new JPanel();
		departmentAdd_nameText.setEditable(false);
		departmentName_JPanel1.add(departmentAdd_nameLabel);
		departmentName_JPanel1.add(departmentAdd_nameText);

		add(label_JPanel1);
		add(departmentName_JPanel1);
		validate();
		setVisible(true);

	}
}

class DepartmentDisplayPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	Department department;
	private JLabel departmentAfterAdd_label;
	private JLabel departmentAfterAdd_nameLabel;

	private JTextField departmentAfterAdd_nameText;

	private JLabel departmentAfterAdd_DIDLabel;
	private JTextField departmentAfterAdd_DIDText;

	public DepartmentDisplayPanel(Department temp) {
		this.department = temp;
		buildDepartmentDisplayPanel();
	}

	public void buildDepartmentDisplayPanel() {
		setLayout(new GridLayout(3, 1));

		JPanel label_JPanel = new JPanel();
		departmentAfterAdd_label = new JLabel("Display Department");
		label_JPanel.add(departmentAfterAdd_label);

		JPanel DID_JPanel = new JPanel();
		departmentAfterAdd_DIDLabel = new JLabel("Department ID");
		String intToString = String.valueOf(department.getDID());
		departmentAfterAdd_DIDText = new JTextField(intToString);
		departmentAfterAdd_DIDText.setEditable(false);
		DID_JPanel.add(departmentAfterAdd_DIDLabel);
		DID_JPanel.add(departmentAfterAdd_DIDText);

		JPanel departmentName_JPanel = new JPanel();
		departmentAfterAdd_nameLabel = new JLabel("Department Name");
		departmentAfterAdd_nameText = new JTextField(department.getDepartmentName());
		departmentAfterAdd_nameText.setEditable(false);
		departmentName_JPanel.add(departmentAfterAdd_nameLabel);
		departmentName_JPanel.add(departmentAfterAdd_nameText);

		add(label_JPanel);
		add(DID_JPanel);
		add(departmentName_JPanel);

	}

}

class DepartmentEditPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Department department;
	private JLabel departmentEdit_label;
	private JLabel departmentEdit_nameLabel;

	private JTextField departmentEdit_nameText;

	private JLabel departmentEdit_DIDLabel;
	private JTextField departmentEdit_DIDText;

	private JLabel departmentEdit_label2; // 2 stands for user input version
	private JLabel departmentEdit_nameLabel2;

	private JTextField studentEdit_studentIDText2; // CHECK THIS
	private JTextField departmentEdit_nameText2;

	private JLabel departmentEdit_DIDLabel2;
	private JTextField departmentEdit_DIDText2;

	public String getStudentEdit_studentIDText2() { // CHECK THIS
		return studentEdit_studentIDText2.getText();
	}

	public String getDepartmentEdit_nameText2() {
		return departmentEdit_nameText2.getText();
	}

	public String getInputDepartmentIDText() {
		return departmentEdit_DIDText.getText();
	}

	public DepartmentEditPanel(Department temp) throws FileNotFoundException {
		this.department = temp;
		buildDepartmentEditPanel(department);
	}

	public void buildDepartmentEditPanel(Department department) {

		JPanel label_JPanel = new JPanel();
		departmentEdit_label = new JLabel("[Old Department Data]");
		label_JPanel.add(departmentEdit_label);

		JPanel DID_JPanel = new JPanel();
		departmentEdit_DIDLabel = new JLabel("Department ID");
		String intToString = String.valueOf(department.getDID());
		departmentEdit_DIDText = new JTextField(intToString);
		departmentEdit_DIDText.setEditable(false);
		DID_JPanel.add(departmentEdit_DIDLabel);
		DID_JPanel.add(departmentEdit_DIDText);

		JPanel departmentName_JPanel = new JPanel();
		departmentEdit_nameLabel = new JLabel("Department Name");
		departmentEdit_nameText = new JTextField(department.getDepartmentName());
		departmentEdit_nameText.setEditable(false);
		departmentName_JPanel.add(departmentEdit_nameLabel);
		departmentName_JPanel.add(departmentEdit_nameText);

		///// DATA 2 input
		JPanel label_JPanel2 = new JPanel();
		departmentEdit_label2 = new JLabel("[New Department Data]");
		label_JPanel2.add(departmentEdit_label2);

		JPanel DID_JPanel2 = new JPanel();
		departmentEdit_DIDLabel2 = new JLabel("Department ID");
		String intToString2 = String.valueOf(department.getDID());
		departmentEdit_DIDText2 = new JTextField(intToString2);
		departmentEdit_DIDText2.setEditable(false);
		DID_JPanel2.add(departmentEdit_DIDLabel2);
		DID_JPanel2.add(departmentEdit_DIDText2);

		JPanel departmentName_JPanel2 = new JPanel();
		departmentEdit_nameLabel2 = new JLabel("Department Name");
		departmentEdit_nameText2 = new JTextField(20);
		departmentName_JPanel2.add(departmentEdit_nameLabel2);
		departmentName_JPanel2.add(departmentEdit_nameText2);

		///////

		JPanel oldEditData = new JPanel();
		oldEditData.setLayout(new GridLayout(3, 1));

		oldEditData.add(label_JPanel);
		oldEditData.add(DID_JPanel);
		oldEditData.add(departmentName_JPanel);

		JPanel newEditData = new JPanel();
		newEditData.setLayout(new GridLayout(3, 1));

		newEditData.add(label_JPanel2);
		newEditData.add(DID_JPanel2);
		newEditData.add(departmentName_JPanel2);

		JLabel editMenuLabel = new JLabel("Edit Department");
		JPanel editMenuLabelPanel = new JPanel();
		editMenuLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		editMenuLabelPanel.add(editMenuLabel);

		setLayout(new BorderLayout());

		add(oldEditData, BorderLayout.WEST);
		add(newEditData, BorderLayout.EAST);
		add(editMenuLabelPanel, BorderLayout.NORTH);

		JLabel imageLabel = new JLabel();
		ImageIcon arrows = new ImageIcon("rightarrow.png");
		imageLabel.setIcon(arrows);

		add(imageLabel, BorderLayout.CENTER);

	}

}

class DepartmentSearchPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel departmentSearch_label;
	private JLabel departmentSearch_inputDIDLabel;
	private JTextField departmentSearch_inputDIDText;

	public String getInputDepartmentDIDText() {
		return departmentSearch_inputDIDText.getText();
	}

	public DepartmentSearchPanel() throws FileNotFoundException {

		buildDepartmentSearchPanel();
	}

	public void buildDepartmentSearchPanel() {
		setLayout(new GridLayout(2, 1));

		JPanel label_JPanel = new JPanel();
		departmentSearch_label = new JLabel("Search Department");
		label_JPanel.add(departmentSearch_label);

		JPanel enterID_JPanel = new JPanel();
		departmentSearch_inputDIDLabel = new JLabel("Enter Department DID to search");
		departmentSearch_inputDIDText = new JTextField(20);
		enterID_JPanel.add(departmentSearch_inputDIDLabel);
		enterID_JPanel.add(departmentSearch_inputDIDText);

		add(label_JPanel);
		add(enterID_JPanel);
	}

}

class InstructorAddPanel extends JPanel {
	private MyGenericList<Department> departmentLinkedListCopy;
	String[] listToArray;
	private static final long serialVersionUID = -5149000693352260886L;
	private JLabel instructorAdd_label;

	private JLabel instructorAdd_nameLabel;
	private JTextField instructorAdd_nameText;

	private JLabel instructorAdd_departmentLabel;
	private JComboBox instructorAdd_departmentCombo;
	private JTextField instructorAdd_departmentComboText;

	public String getInstructorName() {
		return instructorAdd_nameText.getText();
	}

	public void linkedListToArray() {
		listToArray = new String[departmentLinkedListCopy.size()];
		for (int i = 0; i < departmentLinkedListCopy.size(); i++) {
			Department temp = departmentLinkedListCopy.get(i);
			listToArray[i] = temp.getDepartmentName();
		}

	}

	public String getDepartment() { // REPLACE WITH COMBO BOX
		// return courseAdd_departmentText.getText();
		return (String) instructorAdd_departmentCombo.getSelectedItem();
	}

	public int getDepartmentIndex() {
		// return instructorAdd_departmentCombo.getSelectedIndex(); //linked list
		return instructorAdd_departmentCombo.getSelectedIndex() + 1; // database offset by 1
	}

	// linked list constructor not using with MYSQL
	public InstructorAddPanel(MyGenericList<Department> departmentLinkedListCopy) throws FileNotFoundException {
		this.departmentLinkedListCopy = departmentLinkedListCopy;
		buildInstructorAddPanel();
	}

	// database constructor - input is a string array created from database
	public InstructorAddPanel(String[] array) throws FileNotFoundException {
		this.listToArray = array;
		buildInstructorAddPanel();
	}

	public void buildInstructorAddPanel() { // ERASE OLD CODE
		setLayout(new GridLayout(3, 1));

		JPanel label_JPanel = new JPanel();
		instructorAdd_label = new JLabel("Add Instructor");
		label_JPanel.add(instructorAdd_label);

		JPanel instructorName_JPanel = new JPanel();
		instructorAdd_nameLabel = new JLabel("Instructor Name");
		instructorAdd_nameText = new JTextField(20);
		instructorName_JPanel.add(instructorAdd_nameLabel);
		instructorName_JPanel.add(instructorAdd_nameText);

		// linkedListToArray(); turned off for MYSQL

		JPanel instructor_ComboBox_Panel = new JPanel();
		instructorAdd_departmentLabel = new JLabel("Department");
		// courseAdd_departmentText = new JTextField(20); replacing with combo box
		// city_JPanel5.add(courseAdd_departmentLabel);
		// city_JPanel5.add(courseAdd_departmentText);

		instructorAdd_departmentCombo = new JComboBox(listToArray);
		instructor_ComboBox_Panel.add(instructorAdd_departmentLabel);
		instructor_ComboBox_Panel.add(instructorAdd_departmentCombo);

		add(label_JPanel);
		add(instructorName_JPanel);
		add(instructor_ComboBox_Panel);
	}

	public Instructor createInstructor(int numberOfInstructorsInFile) { // has a print function that needs erasing
		String instructorName;
		instructorName = instructorAdd_nameText.getText();

		int instructorDID;
		instructorDID = instructorAdd_departmentCombo.getSelectedIndex();
		System.out.println(" In instructor add panel index of combobox: " + instructorDID);

		Instructor newInstructor = new Instructor(numberOfInstructorsInFile, instructorName, instructorDID);
		return newInstructor;

	}

	public void DepartmentAddDisplayPanel() {

		setLayout(new GridLayout(3, 1));

		JPanel label_JPanel1 = new JPanel();
		instructorAdd_label = new JLabel("Instructor Details");
		label_JPanel1.add(instructorAdd_label);

		JPanel instructorName_JPanel1 = new JPanel();
		instructorAdd_nameText.setEditable(false);
		instructorName_JPanel1.add(instructorAdd_nameLabel);
		instructorName_JPanel1.add(instructorAdd_nameText);

		JPanel instructorDID_JPanel1 = new JPanel();
		// instructorAdd_departmentComboText = getDepartment();
		// instructorDID_JPanel1.add(instructorAdd_departmentLabel);
		// instructorDID_JPanel1.add(getDepartment());

		add(label_JPanel1);
		add(instructorName_JPanel1);
		validate();
		setVisible(true);

	}
}

class InstructorDisplayPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	Instructor instructor;
	private JLabel instructorAfterAdd_label;

	private JLabel instructorAfterAdd_IIDLabel;
	private JTextField instructorAfterAdd_IIDText;

	private JLabel instructorAfterAdd_nameLabel;
	private JTextField instructorAfterAdd_nameText;

	private JLabel instructorAfterAdd_DIDLabel;
	private JTextField instructorAfterAdd_DIDText;

	public InstructorDisplayPanel(Instructor temp) {
		this.instructor = temp;
		buildInstructorDisplayPanel();
	}

	public void buildInstructorDisplayPanel() { // DID is a number instead of a value = change later maybe
		setLayout(new GridLayout(4, 1));
		JPanel label_JPanel = new JPanel();
		instructorAfterAdd_label = new JLabel("Display Instructor");
		label_JPanel.add(instructorAfterAdd_label);

		JPanel IID_JPanel = new JPanel();
		instructorAfterAdd_IIDLabel = new JLabel("Instructor IID");
		String intToString = String.valueOf(instructor.getIID());
		instructorAfterAdd_IIDText = new JTextField(intToString);
		instructorAfterAdd_IIDText.setEditable(false);
		IID_JPanel.add(instructorAfterAdd_IIDLabel);
		IID_JPanel.add(instructorAfterAdd_IIDText);

		JPanel instructorName_JPanel = new JPanel();
		instructorAfterAdd_nameLabel = new JLabel("Instructor Name");
		instructorAfterAdd_nameText = new JTextField(instructor.getInstructorName());
		instructorAfterAdd_nameText.setEditable(false);
		instructorName_JPanel.add(instructorAfterAdd_nameLabel);
		instructorName_JPanel.add(instructorAfterAdd_nameText);

		JPanel instructorDID_JPanel = new JPanel();
		instructorAfterAdd_DIDLabel = new JLabel("Department DID");
		// int offsetDepartment = 1; // So that department[0] displays as department[1]
		// // not needed anymore with databases
		// String intToString2 =
		// String.valueOf(instructor.getInstructorDID()+offsetDepartment);
		String intToString2 = String.valueOf(instructor.getInstructorDID());
		instructorAfterAdd_DIDText = new JTextField(intToString2);
		instructorAfterAdd_DIDText.setEditable(false);
		instructorDID_JPanel.add(instructorAfterAdd_DIDLabel);
		instructorDID_JPanel.add(instructorAfterAdd_DIDText);

		add(label_JPanel);
		add(IID_JPanel);
		add(instructorName_JPanel);
		add(instructorDID_JPanel);

	}

}

class InstructorEditPanel extends JPanel {
	private MyGenericList<Department> departmentLinkedListCopy;
	String[] listToArray;
	String[] departmentNames;
	String departmentName; // displayed in edit section could not pull from DID and departmentNames so had
							// to pass.

	private static final long serialVersionUID = 1L;
	Instructor instructor;
	private JLabel instructorEdit_label;

	private JLabel instructorEdit_IIDLabel;
	private JTextField instructorEdit_IIDText;

	private JLabel instructorEdit_nameLabel;
	private JTextField instructorEdit_nameText;

	private JLabel instructorEdit_DIDLabel;
	private JTextField instructorEdit_DIDText;

	private JLabel instructorEdit_DIDNameLabel;
	private JTextField instructorEdit_DIDNameText;

	private JLabel instructorEdit_label2;

	private JLabel instructorEdit_IIDLabel2;
	private JTextField instructorEdit_IIDText2;

	private JLabel instructorEdit_nameLabel2;
	private JTextField instructorEdit_nameText2;

	private JLabel instructorEdit_departmentNameLabel2; // combo box for edit menu [new data]
	private JComboBox instructorEdit_departmentCombo;

	public String getInstructorEdit_nameText2() {
		return instructorEdit_nameText2.getText();
	}

	public String getDepartment() { // REPLACE WITH COMBO BOX
		// return courseAdd_departmentText.getText();
		return (String) instructorEdit_departmentCombo.getSelectedItem();
	}

	public int getDepartmentIndex() {
		// return instructorEdit_departmentCombo.getSelectedIndex(); old linked list
		return instructorEdit_departmentCombo.getSelectedIndex() + 1; // offset for databases
	}

	public String getDepartmentNameFromLinkedList() {
		int index = instructor.getInstructorDID();
		Department temp = departmentLinkedListCopy.get(index);
		return temp.getDepartmentName();
	}

	// old linked list constructor //NOT USED
	public InstructorEditPanel(Instructor temp, MyGenericList<Department> departmentLinkedListCopy)
			throws FileNotFoundException {
		this.instructor = temp;
		this.departmentLinkedListCopy = departmentLinkedListCopy;
		// buildInstructorEditPanel(instructor);
	}

	// database constructor
	public InstructorEditPanel(Instructor temp, String departmentName, String[] departmentNames)
			throws FileNotFoundException {
		this.instructor = temp;
		this.departmentNames = departmentNames;
		this.departmentName = departmentName;
		buildInstructorEditPanel(instructor, departmentName);
	}

	public void linkedListToArray() {
		listToArray = new String[departmentLinkedListCopy.size()];
		for (int i = 0; i < departmentLinkedListCopy.size(); i++) {
			Department temp = departmentLinkedListCopy.get(i);
			listToArray[i] = temp.getDepartmentName();
		}

	}

	public void buildInstructorEditPanel(Instructor instructor, String departmentName) {

		JPanel label_JPanel = new JPanel();
		instructorEdit_label = new JLabel("[Old Instructor Data]");
		label_JPanel.add(instructorEdit_label);

		JPanel IID_JPanel = new JPanel();
		instructorEdit_IIDLabel = new JLabel("Instructor IID");
		String intToString = String.valueOf(instructor.getIID());
		instructorEdit_IIDText = new JTextField(intToString);
		instructorEdit_IIDText.setEditable(false);
		IID_JPanel.add(instructorEdit_IIDLabel);
		IID_JPanel.add(instructorEdit_IIDText);

		JPanel instructorName_JPanel = new JPanel();
		instructorEdit_nameLabel = new JLabel("Instructor Name");
		instructorEdit_nameText = new JTextField(instructor.getInstructorName());
		instructorEdit_nameText.setEditable(false);
		instructorName_JPanel.add(instructorEdit_nameLabel);
		instructorName_JPanel.add(instructorEdit_nameText);

		JPanel instructorDID_JPanel = new JPanel();
		instructorEdit_DIDLabel = new JLabel("Department DID");
		// int offsetByOne = 1; NOT NEEDED IN DATABASE ERASE LATER
		// String intToString2 = String.valueOf(instructor.getInstructorDID() +
		// offsetByOne); //so department[0] displays as department[1]
		String intToString2 = String.valueOf(instructor.getInstructorDID()); // no offset needed for database
		instructorEdit_DIDText = new JTextField(intToString2);
		instructorEdit_DIDText.setEditable(false);
		instructorDID_JPanel.add(instructorEdit_DIDLabel);
		instructorDID_JPanel.add(instructorEdit_DIDText);

		JPanel instructorDIDName_JPanel = new JPanel();
		instructorEdit_DIDNameLabel = new JLabel("Department Name");
		// instructorEdit_DIDNameText = new
		// JTextField(getDepartmentNameFromLinkedList());
		// int indexToDepartmentName = instructor.getInstructorDID() - 1; // offset from
		// 1 to 0. //not used
		// instructorEdit_DIDNameText = new
		// JTextField(departmentNames[indexToDepartmentName]); //not used
		instructorEdit_DIDNameText = new JTextField(departmentName);

		instructorEdit_DIDNameText.setEditable(false);
		instructorDIDName_JPanel.add(instructorEdit_DIDNameLabel);
		instructorDIDName_JPanel.add(instructorEdit_DIDNameText);

		///// DATA 2 input
		JPanel label_JPanel2 = new JPanel();
		instructorEdit_label2 = new JLabel("[New Instructor Data]");
		label_JPanel2.add(instructorEdit_label2);

		JPanel IID_JPanel2 = new JPanel();
		instructorEdit_IIDLabel2 = new JLabel("Instructor IID");
		String intToString3 = String.valueOf(instructor.getIID());
		instructorEdit_IIDText2 = new JTextField(intToString3);
		instructorEdit_IIDText2.setEditable(false);
		IID_JPanel2.add(instructorEdit_IIDLabel2);
		IID_JPanel2.add(instructorEdit_IIDText2);

		JPanel instructorName_JPanel2 = new JPanel();
		instructorEdit_nameLabel2 = new JLabel("Instructor Name");
		instructorEdit_nameText2 = new JTextField(20);
		instructorName_JPanel2.add(instructorEdit_nameLabel2);
		instructorName_JPanel2.add(instructorEdit_nameText2);

		// linkedListToArray(); not used anymore because databases

		JPanel instructorComboBox_JPanel2 = new JPanel();
		instructorEdit_departmentNameLabel2 = new JLabel("Department Name");
		// instructorEdit_departmentCombo = new JComboBox(listToArray);
		instructorEdit_departmentCombo = new JComboBox(departmentNames);
		instructorComboBox_JPanel2.add(instructorEdit_departmentNameLabel2);
		instructorComboBox_JPanel2.add(instructorEdit_departmentCombo);

		///////

		JPanel oldEditData = new JPanel();
		oldEditData.setLayout(new GridLayout(5, 1));

		oldEditData.add(label_JPanel);
		oldEditData.add(IID_JPanel);
		oldEditData.add(instructorName_JPanel);
		oldEditData.add(instructorDID_JPanel);
		oldEditData.add(instructorDIDName_JPanel);

		JPanel newEditData = new JPanel();
		newEditData.setLayout(new GridLayout(4, 1));

		newEditData.add(label_JPanel2);
		newEditData.add(IID_JPanel2);
		newEditData.add(instructorName_JPanel2);
		newEditData.add(instructorComboBox_JPanel2);

		JLabel editMenuLabel = new JLabel("Edit Instructor");
		JPanel editMenuLabelPanel = new JPanel();
		editMenuLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		editMenuLabelPanel.add(editMenuLabel);

		setLayout(new BorderLayout());

		add(oldEditData, BorderLayout.WEST);
		add(newEditData, BorderLayout.EAST);
		add(editMenuLabelPanel, BorderLayout.NORTH);

		JLabel imageLabel = new JLabel();
		ImageIcon arrows = new ImageIcon("rightarrow.png");
		imageLabel.setIcon(arrows);

		add(imageLabel, BorderLayout.CENTER);

	}

}

class InstructorSearchPanel extends JPanel { // NEEDS VALUE CHECKING

	private static final long serialVersionUID = 1L;
	private JLabel instructorSearch_label;
	private JLabel instructorSearch_inputIIDLabel;
	private JTextField instructorSearch_inputIIDText;

	public String getInputInstructorIIDText() {
		return instructorSearch_inputIIDText.getText();
	}

	public InstructorSearchPanel() throws FileNotFoundException {

		buildInstructorSearchPanel();
	}

	public void buildInstructorSearchPanel() {
		setLayout(new GridLayout(2, 1));

		JPanel label_JPanel = new JPanel();
		instructorSearch_label = new JLabel("Search Instructor");
		label_JPanel.add(instructorSearch_label);

		JPanel enterID_JPanel = new JPanel();
		instructorSearch_inputIIDLabel = new JLabel("Enter Instructor IID to search");
		instructorSearch_inputIIDText = new JTextField(20); // CHECK THIS VALUE
		enterID_JPanel.add(instructorSearch_inputIIDLabel);
		enterID_JPanel.add(instructorSearch_inputIIDText);

		add(label_JPanel);
		add(enterID_JPanel);
	}

}

public class StudentManagementSystemAZURE extends JFrame {

	public static String URL;
	public static String USERNAME;
	public static String PASSWORD;
	public static String timeFix = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

	private static final long serialVersionUID = 1L;
	private final int WINDOW_WIDTH = 800;
	private final int WINDOW_HEIGHT = 600;

	private JMenuBar menuBar;
	private JMenu addMenu;
	private JMenuItem studentAdd;
	private JMenuItem departmentAdd;
	private JMenuItem instructorAdd;
	private JMenuItem courseAdd;
	private JMenuItem enrollmentAdd;

	private JMenu searchMenu;
	private JMenuItem studentSearch;
	private JMenuItem departmentSearch;
	private JMenuItem instructorSearch;
	private JMenuItem courseSearch;
	private JMenuItem enrollmentSearch;

	private JMenu gradeMenu;
	private JMenuItem gradeAdd;

	private JMenu reportMenu;
	private JMenuItem reportPrint;
	// Student Panel Classes
	private StudentAddPanel studentAddPanel;
	private StudentSearchPanel studentSearchPanel;
	private StudentDisplayPanel studentDisplayPanel;
	private StudentEditPanel studentEditPanel;
	// Course Panel Classes
	private PreCourseAddPanel preCourseAddPanel;
	private PreEditCoursePanel preEditCoursePanel;
	private CourseAddPanel courseAddPanel;
	private CourseDisplayPanel courseDisplayPanel;
	private CourseEditDisplayPanel courseEditDisplayPanel; //
	private CourseEditPanel courseEditPanel;
	private CourseSearchPanel courseSearchPanel;
	// Enrollment Panel Classes
	private EnrollmentFirstPanel enrollmentFirstPanel;
	private EnrollmentSecondPanel enrollmentSecondPanel;
	private EnrollmentDisplayPanel enrollmentDisplayPanel;
	private EnrollmentEditPanel enrollmentEditPanel;
	private EnrollmentSearchPanel enrollmentSearchPanel;
	// Grade Panel Classes
	private GradeAddPanel gradeAddPanel;
	private PostGradePanel postGradePanel;
	// Report Panel Classes
	private ReportPrintPanel reportPrintPanel;
	// Department Panel Classes
	private DepartmentAddPanel departmentAddPanel;
	private DepartmentSearchPanel departmentSearchPanel;
	private DepartmentDisplayPanel departmentDisplayPanel;
	private DepartmentEditPanel departmentEditPanel;
	// Instructor Panel Classes
	private InstructorAddPanel instructorAddPanel;
	private InstructorDisplayPanel instructorDisplayPanel;
	private InstructorEditPanel instructorEditPanel;
	private InstructorSearchPanel instructorSearchPanel;

	// Student Buttons
	private JButton createButton;
	private JButton cancelButton_AddPanel;
	private JButton okButton_DisplayPanel;
	private JButton editButton_DisplayPanel;
	private JButton deleteButton_DisplayPanel;
	private JButton cancelButton_SearchPanel;
	private JButton searchButton;
	private JButton saveButton_EditPanel;
	private JButton cancelButton_EditPanel;
	// Student Panels
	private JPanel mainPanel;
	private JPanel displayPanel;
	private JPanel searchPanel;
	private JPanel editPanel;
	private JPanel buttonPanel; // to edit display
	// Course Buttons
	private JButton nextButton_Course;
	private JButton nextCancel_Course;
	private JButton nextButton_PreEdit_Course;
	private JButton nextCancel_PreEdit_Course;
	private JButton createButton_Course;
	private JButton cancelButton_AddPanel_Course;
	private JButton okButton_DisplayPanel_Course;
	private JButton editButton_DisplayPanel_Course;
	private JButton okButton_EditDisplayPanel_Course;//
	private JButton editButton_EditDisplayPanel_Course;//
	private JButton saveButton_EditPanel_Course;
	private JButton cancelButton_EditPanel_Course;
	private JButton searchButton_Course;
	private JButton cancelButton_SearchPanel_Course;
	// Course Panels
	private JPanel preMainPanel_Course;
	private JPanel preEditPanel_Course;
	private JPanel mainPanel_Course;
	private JPanel displayPanel_Course;
	private JPanel editDisplayPanel_Course;//
	private JPanel searchPanel_Course;
	private JPanel editPanel_Course;
	private JPanel buttonPanel_Course;
	// Enrollment Panels
	private JPanel firstPanel_Enrollment; // gets student ID
	private JPanel mainPanel_Enrollment;
	// private JPanel buttonPanel_FirstPanel_Enrollment;
	private JPanel buttonPanel_SecondPanel_Enrollment;
	private JPanel displayPanel_Enrollment;
	private JPanel buttonPanel_DisplayPanel_Enrollment;
	private JPanel editPanel_Enrollment;
	private JPanel searchPanel_Enrollment;
	// Enrollment Buttons
	private JButton searchButton_FirstPanel_Enrollment;
	private JButton cancelButton_FirstPanel_Enrollment;
	private JButton createButton_SecondPanel_Enrollment;
	private JButton cancelButton_SecondPanel_Enrollment;
	private JButton okButton_DisplayPanel_Enrollment;
	private JButton editButton_DisplayPanel_Enrollment;
	private JButton saveButton_EditPanel_Enrollment;
	private JButton cancelButton_EditPanel_Enrollment;
	private JButton searchButton_Enrollment;
	private JButton cancelButton_SearchPanel_Enrollment;

	// Grade Panels
	private JPanel introPanel_Grade;
	private JPanel postPanel_Grade;
	private JPanel buttonPanel_PostPanel_Grade;
	// Grade Buttons
	private JButton searchButton_IntroPanel_Grade;
	private JButton cancelButton_IntroPanel_Grade;
	private JButton postButton_PostPanel_Grade;
	private JButton cancelButton_PostPanel_Grade;
	// Report Panels
	private JPanel mainPanel_Report;
	// Report Buttons
	private JButton printButton_MainPanel_Report;
	private JButton cancelButton_MainPanel_Report;

	// Department Buttons
	private JButton createButton_AddPanel_Department;
	private JButton cancelButton_AddPanel_Department;
	private JButton okButton_DisplayPanel_Department;
	private JButton editButton_DisplayPanel_Department;
	private JButton cancelButton_SearchPanel_Department;
	private JButton searchButton_Department;
	private JButton saveButton_EditPanel_Department;
	private JButton cancelButton_EditPanel_Department;
	// Department Panels
	private JPanel mainPanel_Department;
	private JPanel displayPanel_Department;
	private JPanel searchPanel_Department;
	private JPanel editPanel_Department;
	private JPanel buttonPanel_DisplayPanel_Department; // to edit display
	private JPanel buttonPanel_SearchPanel_Department;
	// Instructor Panels
	private JPanel mainPanel_Instructor;
	private JPanel displayPanel_Instructor;
	private JPanel editPanel_Instructor;
	private JPanel buttonPanel_DisplayPanel_Instructor; // to edit display
	private JPanel searchPanel_Instructor;
	private JPanel buttonPanel_SearchPanel_Instructor;
	// Instructor Buttons
	private JButton createButton_AddPanel_Instructor;
	private JButton cancelButton_AddPanel_Instructor;
	private JButton okButton_DisplayPanel_Instructor;
	private JButton editButton_DisplayPanel_Instructor;
	private JButton cancelButton_SearchPanel_Instructor;
	private JButton searchButton_Instructor;
	private JButton saveButton_EditPanel_Instructor;
	private JButton cancelButton_EditPanel_Instructor;

	// JTables
	JFrame departmentTableFrame;
	JFrame instructorTableFrame;
	JFrame studentTableFrame;
	JFrame courseTableFrame;
	JFrame enrollmentTableFrame;

	// Group JTables
	JFrame courseStudentTableFrame;
	JFrame StudentCourseGradeTableFrame;

	StudentFile studentRecord;
	CourseFile courseRecord;
	EnrollmentFile enrollmentRecord;
	DepartmentFile departmentRecord;
	InstructorFile instructorRecord;

	MyGenericList<Student> studentLinkedList;
	MyGenericList<Department> departmentLinkedList;
	MyGenericList<Instructor> instructorLinkedList;
	MyGenericList<Course> courseLinkedList;
	MyGenericList<Enrollment> enrollmentLinkedList;

	ArrayList<Integer> studentPosition = new ArrayList<Integer>();// not used right now

	public static void getURLString() {

		try {
			File myObj = new File("app.config.txt");
			Scanner myReader = new Scanner(myObj);

			URL = myReader.nextLine();
			// System.out.println(URL);

			USERNAME = myReader.nextLine();
			// System.out.println(Connection);

			PASSWORD = myReader.nextLine();

			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

	}

	public Connection getConnection() throws SQLException {
		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", USERNAME);
		connectionProps.put("password", PASSWORD);

		conn = DriverManager.getConnection(URL + timeFix, connectionProps);
		return conn;
	}

	/**
	 * Run a SQL command which does not return a recordset:
	 * CREATE/INSERT/UPDATE/DELETE/DROP/etc.
	 * 
	 * @throws SQLException If something goes wrong
	 */
	public boolean executeUpdate(Connection conn, String command) throws SQLException {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(command); // This will throw a SQLException if it fails
			return true;
		} finally {

			// This will run whether we throw an exception or not
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	/**
	 * Connect to MySQL and do some stuff.
	 */
	public void run() {

		// Connect to MySQL
		Connection conn = null;
		try {
			conn = this.getConnection();
			System.out.println("Connected to database");
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			return;
		}

		/*
		 * // Create a table try { String createString = "CREATE TABLE " +
		 * this.tableName + " ( " + "ID INTEGER NOT NULL, " +
		 * "NAME varchar(40) NOT NULL, " + "STREET varchar(40) NOT NULL, " +
		 * "CITY varchar(20) NOT NULL, " + "STATE char(2) NOT NULL, " + "ZIP char(5), "
		 * + "PRIMARY KEY (ID))"; this.executeUpdate(conn, createString);
		 * System.out.println("Created a table"); } catch (SQLException e) {
		 * System.out.println("ERROR: Could not create the table"); e.printStackTrace();
		 * return; }
		 * 
		 * // Drop the table/* try { String dropString = "DROP TABLE " + this.tableName;
		 * this.executeUpdate(conn, dropString);
		 * System.out.println("Dropped the table"); } catch (SQLException e) {
		 * System.out.println("ERROR: Could not drop the table"); e.printStackTrace();
		 * return; }
		 */
	}

	public StudentManagementSystemAZURE() throws FileNotFoundException, IOException {
		getURLString();
		run();
		setTitle("Student Management System - (use cancel button before new menu selection)");
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		buildMenuBar();
		setVisible(true);

		studentRecord = new StudentFile("student.dat");
		departmentRecord = new DepartmentFile("department.dat");
		instructorRecord = new InstructorFile("instructor.dat");
		courseRecord = new CourseFile("course.dat");
		enrollmentRecord = new EnrollmentFile("enrollment.dat");

		studentLinkedList = new MyGenericList<Student>();
		departmentLinkedList = new MyGenericList<Department>();
		instructorLinkedList = new MyGenericList<Instructor>();
		courseLinkedList = new MyGenericList<Course>();
		enrollmentLinkedList = new MyGenericList<Enrollment>();

		loadStudentLinkedList();
		loadDepartmentLinkedList();
		loadInstructorLinkedList();
		loadCourseLinkedList();
		loadEnrollmentLinkedList();

	}

	private void buildMenuBar() {
		menuBar = new JMenuBar();

		buildAddMenu();
		buildSearchMenu();
		buildGradeMenu();
		buildReportsMenu();

		menuBar.add(addMenu);
		menuBar.add(searchMenu);
		menuBar.add(gradeMenu);
		menuBar.add(reportMenu);
		setJMenuBar(menuBar);
	}

	private void buildAddMenu() {
		addMenu = new JMenu("Add");
		studentAdd = new JMenuItem("Student");
		departmentAdd = new JMenuItem("Department");
		instructorAdd = new JMenuItem("Instructor");
		courseAdd = new JMenuItem("Course");
		enrollmentAdd = new JMenuItem("Enrollment");

		addMenu.add(studentAdd);
		addMenu.add(departmentAdd);
		addMenu.add(instructorAdd);
		addMenu.add(courseAdd);
		addMenu.add(enrollmentAdd);

		studentAdd.addActionListener(new studentAddListener());
		departmentAdd.addActionListener(new departmentAddListener());
		instructorAdd.addActionListener(new instructorAddListener());
		courseAdd.addActionListener(new preCourseAddListener());
		// courseAdd.addActionListener(new CourseAddListener());
		enrollmentAdd.addActionListener(new EnrollmentAddListener());
	}

	private void buildSearchMenu() {
		searchMenu = new JMenu("Search");
		studentSearch = new JMenuItem("Student");
		departmentSearch = new JMenuItem("Department");
		instructorSearch = new JMenuItem("Instructor");
		courseSearch = new JMenuItem("Course");
		enrollmentSearch = new JMenuItem("Enrollment");
		// add action listener
		searchMenu.add(studentSearch);
		searchMenu.add(departmentSearch);
		searchMenu.add(instructorSearch);
		searchMenu.add(courseSearch);
		searchMenu.add(enrollmentSearch);

		studentSearch.addActionListener(new studentSearchListener());
		departmentSearch.addActionListener(new departmentSearchListener());
		instructorSearch.addActionListener(new InstructorSearchListener());
		courseSearch.addActionListener(new CourseSearchListener());
		enrollmentSearch.addActionListener(new EnrollmentSearchListener());
	}

	private void buildGradeMenu() {
		gradeMenu = new JMenu("Grades");
		gradeAdd = new JMenuItem("Add");

		gradeMenu.add(gradeAdd);

		gradeAdd.addActionListener(new gradeAddListener());
	}

	private void buildReportsMenu() {
		reportMenu = new JMenu("Reports");
		reportPrint = new JMenuItem("Print Report");

		reportMenu.add(reportPrint);

		reportPrint.addActionListener(new reportPrintListener());
	}

	public Student createStudent(int numberOfStudentsInFile) {
		String firstName;
		String lastName;
		String address;
		String city;
		String state;

		firstName = studentAddPanel.getFirstName();
		lastName = studentAddPanel.getLastName();
		address = studentAddPanel.getAddress();
		city = studentAddPanel.getCity();
		state = studentAddPanel.getState();

		Student newStudent = new Student(numberOfStudentsInFile, firstName, lastName, address, city, state);
		return newStudent;

	}

	public Course createCourse(int numberOfCoursesInFile) {
		String CID;
		String cName;
		String instructor;
		String department;

		CID = courseAddPanel.getCID();
		cName = courseAddPanel.getcName();
		instructor = courseAddPanel.getInstructor();
		// department = courseAddPanel.getDepartment();
		department = courseAddPanel.getDepartment();

		Course newCourse = new Course(numberOfCoursesInFile, CID, cName, instructor, department);
		return newCourse;
	}

	public Enrollment createEnrollment(int numberOfEnrollmentsInFile) {
		int studentID;
		int cNum;
		String year;
		String semester;
		String grade;

		studentID = Integer.parseInt(enrollmentSecondPanel.getStudentID());
		cNum = enrollmentSecondPanel.getCourseIndex();// offsets
		year = enrollmentSecondPanel.getYear().trim();
		semester = enrollmentSecondPanel.getSemester().trim();
		;
		grade = "N"; // added this recently not tested yet
		Enrollment newEnrollment = new Enrollment(numberOfEnrollmentsInFile, studentID, cNum, year, semester, grade);
		return newEnrollment;
	}

	public Department createDepartment(int numberOfDepartmentsInFile) {
		String departmentName;
		departmentName = departmentAddPanel.getDepartmentName();
		Department newDepartment = new Department(numberOfDepartmentsInFile, departmentName);
		return newDepartment;

	}

	public Instructor createInstructorOLD(int numberOfInstructorsInFile) { // made new version because needed actual
																			// string for did instead of index can erase
																			// later if everything is ok
		String instructorName;
		instructorName = instructorAddPanel.getInstructorName();
		int instructorDID = instructorAddPanel.getDepartmentIndex();
		System.out.println("DID in create Instructor: " + instructorDID);
		Instructor newInstructor = new Instructor(numberOfInstructorsInFile, instructorName, instructorDID);
		return newInstructor;

	}

	public Instructor createInstructor(int numberOfInstructorsInFile) { // solved problem with incorrect DIDindex in
																		// instructor add/edit
		String instructorName;
		instructorName = instructorAddPanel.getInstructorName();
		String departmentStringValue = instructorAddPanel.getDepartment();
		int departmentIndex = getDepartmentIndexMYSQL(departmentStringValue);
		int instructorDID = departmentIndex;
		System.out.println("DID in create Instructor: " + departmentIndex);
		Instructor newInstructor = new Instructor(numberOfInstructorsInFile, instructorName, instructorDID);
		return newInstructor;

	}

	public void buildDisplayPanel(Student temp) {
		studentDisplayPanel = new StudentDisplayPanel(temp);

	}

	// Loads data into grade table
	public void fillTextArea() throws FileNotFoundException {
		// enrollmentRecord = new EnrollmentFile("enrollment.dat");
		// studentRecord = new StudentFile("student.dat");
		int studentIDSubMenu = Integer.parseInt(gradeAddPanel.getGradeIntro_SIDText());
		String yearEntered = gradeAddPanel.getIntro_YearText();
		String semesterEntered = gradeAddPanel.getIntro_SemesterText();
		try {
			enrollmentRecord.moveFilePointer(0);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int readValueSID;
		String readValueYear, readValueSemester;

		try {
			postGradePanel
					.apphendTextArea("----------------------------------------------------------------------------"
							+ "------------------------\n");
			for (int i = 0; i < enrollmentLinkedList.size(); i++) {
				// Enrollment temp = enrollmentRecord.readEnrollmentFile(); OLD
				Enrollment temp = enrollmentLinkedList.get(i);
				readValueSID = temp.getStudentID();
				readValueYear = temp.getYear();
				readValueSemester = temp.getSemester();
				readValueSemester.trim();

				if (readValueSID == studentIDSubMenu && yearEntered.contentEquals(readValueYear) && semesterEntered
						.replaceAll("\\s+", "").equalsIgnoreCase(readValueSemester.replaceAll("\\s+", ""))) {
					studentRecord.moveFilePointer(studentIDSubMenu - 1); // Not sure if this is needed
					studentRecord.moveFilePointer(studentIDSubMenu - 1);
					String build = (temp.getEID() + "          " + temp.getcNum() + "          " + temp.getStudentID()
							+ "          " + temp.getYear() + "          " + temp.getSemester() + "          "
							+ temp.getGrade());
					postGradePanel.apphendTextArea(build + "\n");
				}
				if (readValueSID != studentIDSubMenu) {
					// enrollmentRecord.moveFilePointer(i + 1); OLD
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public void fillTextArea2() throws FileNotFoundException {

		int studentIDSubMenu = Integer.parseInt(gradeAddPanel.getGradeIntro_SIDText());
		String yearEntered = gradeAddPanel.getIntro_YearText();
		String semesterEntered = gradeAddPanel.getIntro_SemesterText();

	}

	public void fillReportTextArea(String year, int cNum) throws FileNotFoundException {
		enrollmentRecord = new EnrollmentFile("enrollment.dat");

		String yearEntered = year;

		int courseNum = cNum;
		try {
			enrollmentRecord.moveFilePointer(0);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int readValueSID;
		String readValueYear;

		try {
			for (int i = 0; i < enrollmentRecord.getNumberOfRecords(); i++) {
				Enrollment temp = enrollmentRecord.readEnrollmentFile();
				readValueSID = temp.getcNum();
				readValueYear = temp.getYear();

				if (readValueSID == courseNum && yearEntered.contentEquals(readValueYear)) {

					String newSemester = temp.getSemester().replaceAll("[\\s|\\u00A0]+", "");
					String newSID = String.valueOf(temp.getStudentID()).replaceAll("[\\s|\\u00A0]+", "");
					String build = (String.format("%-15s", String.valueOf(temp.getEID()))
							+ String.format("%-20s", String.valueOf(temp.getcNum()))
							+ String.format("%-20s", String.valueOf(newSID)));
					// System.out.print(build); when i print out the format is perfect but for some
					// reason its not in the textArea. Tried everything!
					String build2 = (String.format("%-22s", temp.getYear().replaceAll("[\\s|\\u00A0]+", ""))
							+ String.format("%-30s", newSemester.replaceAll("[\\s|\\u00A0]+", ""))
							+ String.format("%-4s", temp.getGrade().replaceAll("[\\s|\\u00A0]+", "")));
					// System.out.print(build2+"\n"); when i print out the format is perfect but for
					// some reason its not in the textArea. Tried everything!

					reportPrintPanel.apphendTextArea(build + build2 + "\n");
				}
				if (readValueSID != courseNum) {
					enrollmentRecord.moveFilePointer(i + 1);
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public void loadStudentLinkedList() throws IOException { // ERASE OLD LINES
		long file_length = studentRecord.getNumberOfRecords();
		studentRecord.moveFilePointer(0);
		for (int i = 0; i < file_length; i++) {
			Student temp = studentRecord.readStudentFile();
			studentLinkedList.add(temp);
			temp = studentLinkedList.get(i);
			// System.out.println(temp.getFirstName());
		}
		printStudentLinkedList();
	}

	public void loadDepartmentLinkedList() throws IOException { // ERASE OLD LINES
		long file_length = departmentRecord.getNumberOfRecords();
		departmentRecord.moveFilePointer(0);
		for (int i = 0; i < file_length; i++) {
			Department temp = departmentRecord.readDepartmentFile();
			departmentLinkedList.add(temp);
			temp = departmentLinkedList.get(i);
			// System.out.println(temp.getDepartmentName());
		}
		printDepartmentLinkedList();
	}

	public void loadInstructorLinkedList() throws IOException { // ERASE OLD LINES
		long file_length = instructorRecord.getNumberOfRecords();
		instructorRecord.moveFilePointer(0);
		for (int i = 0; i < file_length; i++) {
			Instructor temp = instructorRecord.readInstructorFile();
			instructorLinkedList.add(temp);
			temp = instructorLinkedList.get(i);
			// System.out.println(temp.getFirstName());
		}
		printInstructorLinkedList();
	}

	public void loadCourseLinkedList() throws IOException { // ERASE OLD LINES
		long file_length = courseRecord.getNumberOfRecords();
		courseRecord.moveFilePointer(0);
		for (int i = 0; i < file_length; i++) {
			Course temp = courseRecord.readCourseFile();
			courseLinkedList.add(temp);
			temp = courseLinkedList.get(i);
			// System.out.println(temp.getFirstName());
		}
		printCourseLinkedList();
	}

	public void loadEnrollmentLinkedList() throws IOException { // ERASE OLD LINES
		long file_length = enrollmentRecord.getNumberOfRecords();
		enrollmentRecord.moveFilePointer(0);
		for (int i = 0; i < file_length; i++) {
			Enrollment temp = enrollmentRecord.readEnrollmentFile();
			enrollmentLinkedList.add(temp);
			temp = enrollmentLinkedList.get(i);
			// System.out.println(temp.getFirstName());
		}
		printEnrollmentLinkedList();
	}

	public void printStudentLinkedList() {
		System.out.println("---------student----------");
		for (int i = 0; i < studentLinkedList.size(); i++) {
			Student temp = studentLinkedList.get(i);
			System.out.println(temp.getFirstName());
		}
	}

	public void printDepartmentLinkedList() {
		System.out.println("----------department----------");
		for (int i = 0; i < departmentLinkedList.size(); i++) {
			Department temp = departmentLinkedList.get(i);
			System.out.println(temp.getDepartmentName());
		}
	}

	public void printInstructorLinkedList() {
		System.out.println("----------instructor----------");
		for (int i = 0; i < instructorLinkedList.size(); i++) {
			Instructor temp = instructorLinkedList.get(i);
			System.out.println(temp.getInstructorName());
		}
	}

	public void printCourseLinkedList() {
		System.out.println("----------course----------");
		for (int i = 0; i < courseLinkedList.size(); i++) {
			Course temp = courseLinkedList.get(i);
			System.out.println(temp.getCID());
		}
	}

	public void printEnrollmentLinkedList() {
		System.out.println("----------enrollment----------");
		for (int i = 0; i < enrollmentLinkedList.size(); i++) {
			Enrollment temp = enrollmentLinkedList.get(i);
			System.out.println(temp.getEID());
		}
	}

	public void loadStudentPositionArrayList() {
		studentPosition.clear();
		for (int i = 0; i < studentLinkedList.size(); i++) {
			addSIDToArrayList(studentLinkedList.get(i).getID());
		}
	}

	public void addSIDToArrayList(int SID) {
		studentPosition.add(SID);
	}

	public int mappedSIDValue(int SIDValue) {
		loadStudentPositionArrayList();
		return studentPosition.indexOf(SIDValue);
	}

	public String[] sortStringArray(String[] array) { // sorts the combo box order
		Arrays.sort(array, Comparator.comparing(String::length).thenComparing(Function.identity()));
		return array;
	}

	// Student CALL Procedures
	public int countStudentMYSQL() {
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return 0;
		}
		CallableStatement statement = null;
		int numOfStudentRows = 0;
		try {
			statement = (CallableStatement) conn.prepareCall("{call getStudentCount(?)}");
			statement.registerOutParameter("sCount", java.sql.Types.INTEGER);
			statement.execute();
			numOfStudentRows = statement.getInt("sCount");
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// System.out.println("OUTPUT COUNT: " + numOfStudentRows);
		return numOfStudentRows + 1;

	}

	public void insertStudentMYSQL(Student temp) {
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return;
		}
		CallableStatement statement = null;

		try {
			int sID = temp.getID();
			String fName = temp.getFirstName();
			String lName = temp.getLastName();
			String address = temp.getAddress();
			String city = temp.getCity();
			String state = temp.getState();

			statement = (CallableStatement) conn.prepareCall("{call insertStudent(" + String.valueOf(sID) + ",'" + fName
					+ "','" + lName + "','" + address + "','" + city + "','" + state + "')}");
			System.out.println(statement);

			statement.execute();

			statement.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public void updateStudentMYSQL(Student temp) {
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return;
		}
		CallableStatement statement = null;

		try {
			int sID = temp.getID();
			String fName = temp.getFirstName();
			String lName = temp.getLastName();
			String address = temp.getAddress();
			String city = temp.getCity();
			String state = temp.getState();

			statement = (CallableStatement) conn.prepareCall("{call updateStudent(" + String.valueOf(sID) + ",'" + fName
					+ "','" + lName + "','" + address + "','" + city + "','" + state + "')}");
			System.out.println(statement);

			statement.execute();

			statement.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public Student getStudentMYSQL(int index) {
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return null;
		}

		CallableStatement statement = null;
		ResultSet result = null;

		try {
			statement = (CallableStatement) conn.prepareCall("{call getStudent(" + String.valueOf(index) + ")}");
			result = statement.executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(statement);

		Student temp = null;

		try {
			result.next();
			int studentID = result.getInt(1);
			String firstName = result.getString(2);
			String lastName = result.getString(3);
			String address = result.getString(4);
			String city = result.getString(5);
			String state = result.getString(6);
			temp = new Student(studentID, firstName, lastName, address, city, state);
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return temp;
	}

	public String[] getAllStudentsMYSQL() {
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return null;
		}

		CallableStatement statement = null;
		ResultSet result = null;

		try {
			statement = (CallableStatement) conn.prepareCall("{call getAllStudents()}");
			result = statement.executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(statement);
		
		String[] allStudents = new String[countStudentMYSQL() - 1];

		try {
			int counter = 0;
			while (result.next()) {
				allStudents[counter] = String.valueOf(result.getInt(1));
				counter++;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		for (String i : allStudents) { // erase this later print option
			System.out.println(i);
		}
		return allStudents;

	}
	
	// Department CALL Procedures
	public int countDepartmentMYSQL() {
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return 0;
		}
		CallableStatement statement = null;
		int numOfDepartmentRows = 0;
		try {
			statement = (CallableStatement) conn.prepareCall("{call getDepartmentCount(?)}");
			statement.registerOutParameter("dCount", java.sql.Types.INTEGER);
			statement.execute();
			numOfDepartmentRows = statement.getInt("dCount");
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// System.out.println("OUTPUT COUNT: " + numOfStudentRows);
		return numOfDepartmentRows + 1;

	}

	public void insertDepartmentMYSQL(Department temp) {
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return;
		}
		CallableStatement statement = null;

		try {
			int dID = temp.getDID();
			String dName = temp.getDepartmentName();

			statement = (CallableStatement) conn
					.prepareCall("{call insertDepartment(" + String.valueOf(dID) + ",'" + dName + "')}");
			System.out.println(statement);

			statement.execute();

			statement.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public void updateDepartmentMYSQL(Department temp) {
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return;
		}
		CallableStatement statement = null;

		try {
			int dID = temp.getDID();
			String dName = temp.getDepartmentName();

			statement = (CallableStatement) conn
					.prepareCall("{call updateDepartment(" + String.valueOf(dID) + ",'" + dName + "')}");
			System.out.println(statement);

			statement.execute();

			statement.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public Department getDepartmentMYSQL(int index) {
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return null;
		}

		CallableStatement statement = null;
		ResultSet result = null;

		try {
			statement = (CallableStatement) conn.prepareCall("{call getDepartment(" + String.valueOf(index) + ")}");
			result = statement.executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(statement);

		Department temp = null;

		try {
			result.next();
			int DID = result.getInt(1);
			String dName = result.getString(2);

			temp = new Department(DID, dName);
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return temp;

	}
	
	public String[] getAllDepartmentsMYSQL() {
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return null;
		}

		CallableStatement statement = null;
		ResultSet result = null;

		try {
			statement = (CallableStatement) conn.prepareCall("{call getAllDepartments()}");
			result = statement.executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(statement);
		
		String[] allDepartments = new String[countDepartmentMYSQL() - 1];

		try {
			int counter = 0;
			while (result.next()) {
				allDepartments[counter] = result.getString(1);
				counter++;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		for (String i : allDepartments) { // erase this later print option
			System.out.println(i);
		}
		return allDepartments;
	}
	
	// Instructor CALL Procedures
	public int countInstructorMYSQL() {
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return 0;
		}
		CallableStatement statement = null;
		int numOfInstructorRows = 0;
		try {
			statement = (CallableStatement) conn.prepareCall("{call getInstructorCount(?)}");
			statement.registerOutParameter("iCount", java.sql.Types.INTEGER);
			statement.execute();
			numOfInstructorRows = statement.getInt("iCount");
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// System.out.println("OUTPUT COUNT: " + numOfStudentRows);
		return numOfInstructorRows + 1;

	}

	public void insertInstructorMYSQL(Instructor temp) {
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return;
		}
		CallableStatement statement = null;

		try {
			int iID = temp.getIID();
			String iName = temp.getInstructorName();
			int iDID = temp.getInstructorDID();

			statement = (CallableStatement) conn.prepareCall("{call insertInstructor(" + String.valueOf(iID) + ",'"
					+ iName + "'," + String.valueOf(iDID) + ")}");
			System.out.println(statement);

			statement.execute();

			statement.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public void updateInstructorMYSQL(Instructor temp) {
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return;
		}
		CallableStatement statement = null;

		try {
			int IID = temp.getIID();
			String iName = temp.getInstructorName();
			int iDID = temp.getInstructorDID();

			statement = (CallableStatement) conn
					.prepareCall("{call updateInstructor(" + String.valueOf(IID) + ",'" + iName + "'," + iDID + ")}");
			System.out.println(statement);

			statement.execute();

			statement.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public Instructor getInstructorMYSQL(int index) {
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return null;
		}

		CallableStatement statement = null;
		ResultSet result = null;

		try {
			statement = (CallableStatement) conn.prepareCall("{call getInstructor(" + String.valueOf(index) + ")}");
			result = statement.executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(statement);

		Instructor temp = null;

		try {
			result.next();
			int IID = result.getInt(1);
			String iName = result.getString(2);
			int iDID = result.getInt(3);
			
			temp = new Instructor(IID, iName, iDID);
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return temp;
	}
	
	// Course CALL Procedures
	public int countCourseMYSQL() {
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return 0;
		}
		CallableStatement statement = null;
		int numOfCourseRows = 0;
		try {
			statement = (CallableStatement) conn.prepareCall("{call getCourseCount(?)}");
			statement.registerOutParameter("cCount", java.sql.Types.INTEGER);
			statement.execute();
			numOfCourseRows = statement.getInt("cCount");
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// System.out.println("OUTPUT COUNT: " + numOfStudentRows);
		return numOfCourseRows + 1;

	}

	public void insertCourseMYSQL(Course temp) {
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return;
		}
		CallableStatement statement = null;

		try {
			int cNUM = temp.getCNUM();
			String cID = temp.getCID();
			String cNAME = temp.getcName();
			String inst = temp.getInstructor();
			String depart = temp.getDepartment();

			statement = (CallableStatement) conn.prepareCall("{call insertCourse(" + String.valueOf(cNUM) + ",'" + cID
					+ "','" + cNAME + "','" + inst + "','" + depart + "')}");
			System.out.println(statement);

			statement.execute();

			statement.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public void updateCourseMYSQL(Course temp) {
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return;
		}
		CallableStatement statement = null;

		try {
			int CNUM = temp.getCNUM();
			String CID = temp.getCID();
			String CNAME = temp.getcName();
			String instructor = temp.getInstructor();
			String department = temp.getDepartment();

			statement = (CallableStatement) conn.prepareCall("{call updateCourse(" + String.valueOf(CNUM) + ",'" + CID
					+ "','" + CNAME + "','" + instructor + "','" + department + "')}");
			System.out.println(statement);

			statement.execute();

			statement.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public Course getCourseMYSQL(int index) {
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return null;
		}

		CallableStatement statement = null;
		ResultSet result = null;

		try {
			statement = (CallableStatement) conn.prepareCall("{call getCourse(" + String.valueOf(index) + ")}");
			result = statement.executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(statement);

		Course temp = null;

		try {
			result.next();
			int CNUM = result.getInt(1);
			String CID = result.getString(2);
			String cName = result.getString(3);
			String instructor = result.getString(4);
			String department = result.getString(5);
			temp = new Course(CNUM, CID, cName,instructor,department);
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return temp;
	}
	
	public String[] getAllCoursesMYSQL() {
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return null;
		}

		CallableStatement statement = null;
		ResultSet result = null;

		try {
			statement = (CallableStatement) conn.prepareCall("{call getAllCourses()}");
			result = statement.executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(statement);
		
		String[] allCourses = new String[countCourseMYSQL() - 1];

		try {
			int counter = 0;
			while (result.next()) {
				allCourses[counter] = String.valueOf(result.getInt(1));
				counter++;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		for (String i : allCourses) { // erase this later print option
			System.out.println(i);
		}
		return allCourses;
	}
	
	// Enrollment CALL Procedures
	public int countEnrollmentMYSQL() {
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return 0;
		}
		CallableStatement statement = null;
		int numOfEnrollmentRows = 0;
		try {
			statement = (CallableStatement) conn.prepareCall("{call getEnrollmentCount(?)}");
			statement.registerOutParameter("eCount", java.sql.Types.INTEGER);
			statement.execute();
			numOfEnrollmentRows = statement.getInt("eCount");
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// System.out.println("OUTPUT COUNT: " + numOfStudentRows);
		return numOfEnrollmentRows + 1;

	}

	public void insertEnrollmentMYSQL(Enrollment temp) {
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return;
		}
		CallableStatement statement = null;

		try {
			int EIDTemp = temp.getEID();
			int SIDTemp = temp.getStudentID();
			int CNUMTemp = temp.getcNum();
			String yearTemp = temp.getYear();
			String semesterTemp = temp.getSemester();
			String gradeTemp = temp.getGrade();

			statement = (CallableStatement) conn.prepareCall("{call insertEnrollment(" + String.valueOf(EIDTemp) + ","
					+ String.valueOf(SIDTemp) + "," + String.valueOf(CNUMTemp) + ",'" + yearTemp + "','" + semesterTemp
					+ "','" + gradeTemp + "')}");
			System.out.println(statement);

			statement.execute();

			statement.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public void updateEnrollmentMYSQL(Enrollment temp) {
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return;
		}
		CallableStatement statement = null;

		try {
			int EID = temp.getEID();
			int SID = temp.getStudentID();
			int CNUM = temp.getcNum();
			String year = temp.getYear();
			String semester = temp.getSemester();
			String grade = temp.getGrade();

			statement = (CallableStatement) conn
					.prepareCall("{call updateEnrollment(" + String.valueOf(EID) + "," + String.valueOf(SID) + ","
							+ String.valueOf(CNUM) + ",'" + year + "','" + semester + "','" + grade + "')}");
			System.out.println(statement);

			statement.execute();

			statement.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public Enrollment getEnrollmentMYSQL(int index) {
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return null;
		}

		CallableStatement statement = null;
		ResultSet result = null;

		try {
			statement = (CallableStatement) conn.prepareCall("{call getEnrollment(" + String.valueOf(index) + ")}");
			result = statement.executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(statement);

		Enrollment temp = null;

		try {
			result.next();
			int EID = result.getInt(1);
			int SID = result.getInt(2);
			int CNUM = result.getInt(3);
			String year = result.getString(4);
			String semester = result.getString(5);
			String grade = result.getString(6);
			temp = new Enrollment(EID, SID, CNUM, year, semester,grade);
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return temp;
	}
	
	// Student SQL functions
	public int countStudentMYSQLREPLACED() { // replaced with procedure
		int numOfStudentRows = 0;
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return 0;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "SELECT COUNT(*) FROM student;";
		try {
			ResultSet result = stmt.executeQuery(sqlStatement);
			result.next();
			numOfStudentRows = result.getInt("Count(*)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(sqlStatement);
		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		// System.out.println(numOfStudentRows+1);
		return numOfStudentRows + 1; // returns plus 1 to start SID at 1.

	}

	public void insertStudentMYSQLREPLACED(Student temp) { // replaced with procedure
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "INSERT INTO student " + "(studentID, firstName, lastName, address, city, state) "
				+ "VALUES (" + temp.getID() + ", '" + temp.getFirstName() + "', '" + temp.getLastName() + "', '"
				+ temp.getAddress() + "', '" + temp.getCity() + "', '" + temp.getState() + "')";
		System.out.println(sqlStatement);
		int rows = 0;
		try {
			rows = stmt.executeUpdate(sqlStatement);
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		System.out.println(rows + " row(s) added to the table.");
		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}

	public void updateStudentMYSQLREPLACED(Student editedStudent) { // replaced with procedure
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "UPDATE student " + "SET firstName = '" + editedStudent.getFirstName() + "', "
				+ "lastName = '" + editedStudent.getLastName() + "', " + "address = '" + editedStudent.getAddress()
				+ "', " + "city = '" + editedStudent.getCity() + "', " + "state = '" + editedStudent.getState() + "' "
				+ "WHERE studentID = " + String.valueOf(editedStudent.getID()) + ";";
		// editedStudent.getLastName() + "', '" + editedStudent.getAddress() + "', '" +
		// editedStudent.getCity()+ "', '" + editedStudent.getState()+ "')";
		System.out.println(sqlStatement);
		int rows = 0;
		try {
			rows = stmt.executeUpdate(sqlStatement);
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		System.out.println(rows + " row(s) added to the table.");
		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

	}

	public Student getStudentMYSQLREPLACED(int index) { // replaced with procedure
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return null;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "Select *FROM student WHERE studentID = " + index + ";";

		Student temp = null;
		try {
			ResultSet result = stmt.executeQuery(sqlStatement);
			result.next();
			int studentID = result.getInt(1);
			String firstName = result.getString(2);
			String lastName = result.getString(3);
			String address = result.getString(4);
			String city = result.getString(5);
			String state = result.getString(6);
			temp = new Student(studentID, firstName, lastName, address, city, state);
			// numOfStudentRows = result.getInt("Count(*)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return temp;

	}

	public String[] getAllStudentsMYSQLREPLACED() { //replaced with procedure
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return null;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "Select studentID FROM student;";

		String[] allStudents = new String[countStudentMYSQL() - 1];

		try {
			ResultSet result = stmt.executeQuery(sqlStatement);
			int counter = 0;
			while (result.next()) {
				allStudents[counter] = String.valueOf(result.getInt(1));
				counter++;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		for (String i : allStudents) { // erase this later print option
			System.out.println(i);
		}
		return allStudents;

	}

	// Department SQL functions
	public int countDepartmentMYSQLREPLACED() { // replaced with procedure
		int numOfDepartmentRows = 0;
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return 0;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "SELECT COUNT(*) FROM department;";
		try {
			ResultSet result = stmt.executeQuery(sqlStatement);
			result.next();
			numOfDepartmentRows = result.getInt("Count(*)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(sqlStatement);
		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		// System.out.println(numOfDepartmentRows+1);
		return numOfDepartmentRows + 1; // offset //returns plus 1 to start DID at 1.

	}

	public void insertDepartmentMYSQLREPLACED(Department temp) { // replaced with procedure
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "INSERT INTO department " + "(DID, departmentName) " + "VALUES (" + temp.getDID() + ", '"
				+ temp.getDepartmentName() + "')";
		System.out.println(sqlStatement);
		int rows = 0;
		try {
			rows = stmt.executeUpdate(sqlStatement);
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		System.out.println(rows + " row(s) added to the table.");
		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}

	public void updateDepartmentMYSQLREPLACED(Department editedDepartment) { // replaced with procedure
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "UPDATE department " + "SET departmentName = '" + editedDepartment.getDepartmentName()
				+ "' " + "WHERE DID = " + String.valueOf(editedDepartment.getDID()) + ";";
		System.out.println(sqlStatement);
		int rows = 0;
		try {
			rows = stmt.executeUpdate(sqlStatement);
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		System.out.println(rows + " row(s) added to the table.");
		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

	}

	public Department getDepartmentMYSQLREPLACED(int index) { //replaced with procedure
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return null;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "Select *FROM department WHERE DID = " + index + ";";

		Department temp = null;
		try {
			ResultSet result = stmt.executeQuery(sqlStatement);
			result.next();
			int DID = result.getInt(1);
			String departmentName = result.getString(2);
			temp = new Department(DID, departmentName);
			// numOfStudentRows = result.getInt("Count(*)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return temp;

	}

	public int getDepartmentIndexMYSQL(String passedDepartmentName) { // get department index by name for instructor
																		// create
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return 0;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "Select DID FROM department WHERE departmentName = '" + passedDepartmentName + "';";

		int DID = 0;
		try {
			ResultSet result = stmt.executeQuery(sqlStatement);
			result.next();
			DID = result.getInt(1);
			// String departmentName = result.getString(2);
			// temp = new Department(DID, departmentName);
			// numOfStudentRows = result.getInt("Count(*)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return DID;

	}

	public String[] getAllDepartmentsMYSQLREPLACED() { //replaced with procedure
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return null;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "Select departmentName FROM department;";

		String[] allDepartments = new String[countDepartmentMYSQL() - 1];

		try {
			ResultSet result = stmt.executeQuery(sqlStatement);
			int counter = 0;
			while (result.next()) {
				allDepartments[counter] = result.getString(1);
				counter++;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		for (String i : allDepartments) { // erase this later print option
			System.out.println(i);
		}
		return allDepartments;

	}

	// Instructor SQL functions
	public int countInstructorMYSQLREPLACED() { // replaced with procedure
		int numOfInstructorRows = 0;
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return 0;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "SELECT COUNT(*) FROM instructor;";
		try {
			ResultSet result = stmt.executeQuery(sqlStatement);
			result.next();
			numOfInstructorRows = result.getInt("Count(*)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(sqlStatement);
		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		// System.out.println(numOfStudentRows+1);
		return numOfInstructorRows + 1; // returns plus 1 to start SID at 1.

	}

	public int countInstructorByDIDMYSQL(int DID) {
		int numOfInstructorRows = 0;
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return 0;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "SELECT COUNT(*) FROM instructor WHERE instructorDID = " + String.valueOf(DID) + ";";
		try {
			ResultSet result = stmt.executeQuery(sqlStatement);
			result.next();
			numOfInstructorRows = result.getInt("Count(*)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(sqlStatement);
		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.println("Num of instructors by DID: " + String.valueOf(numOfInstructorRows));
		return numOfInstructorRows; // returns plus 1 to start SID at 1.

	}

	public void insertInstructorMYSQLREPLACED(Instructor temp) { // replaced with procedure
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "INSERT INTO instructor " + "(IID, instructorName, instructorDID) " + "VALUES ("
				+ temp.getIID() + ", '" + temp.getInstructorName() + "', " + temp.getInstructorDID() + ")";
		System.out.println(sqlStatement);
		int rows = 0;
		try {
			rows = stmt.executeUpdate(sqlStatement);
		} catch (SQLException e2) {
			// System.out.println("Duplicate Entry found. Data purged.");
			JOptionPane.showMessageDialog(null, "Duplicate Entry found. Data purged.");
			e2.printStackTrace();
			System.exit(0);
		}

		System.out.println(rows + " row(s) added to the table.");
		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}

	public void updateInstructorMYSQLREPLACED(Instructor editedInstructor) { // replaced with procedure
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "UPDATE instructor " + "SET instructorName = '" + editedInstructor.getInstructorName()
				+ "', " + "instructorDID = " + String.valueOf(editedInstructor.getInstructorDID()) + " WHERE IID = "
				+ String.valueOf(editedInstructor.getIID()) + ";";
		// editedStudent.getLastName() + "', '" + editedStudent.getAddress() + "', '" +
		// editedStudent.getCity()+ "', '" + editedStudent.getState()+ "')";
		System.out.println(sqlStatement);
		int rows = 0;
		try {
			rows = stmt.executeUpdate(sqlStatement);
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		System.out.println(rows + " row(s) added to the table.");
		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

	}

	public Instructor getInstructorMYSQLREPLACED(int index) { //replaced with procedure
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return null;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "Select * FROM instructor WHERE IID = " + index + ";";

		Instructor temp = null;
		try {
			ResultSet result = stmt.executeQuery(sqlStatement);
			result.next();
			int IID = result.getInt(1);
			String instructorName = result.getString(2);
			int DID = result.getInt(3);
			temp = new Instructor(IID, instructorName, DID);
			// numOfStudentRows = result.getInt("Count(*)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return temp;

	}

	public String[] getFilteredInstructorsMYSQL(int DID) {
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return null;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		int correctSize = countInstructorByDIDMYSQL(DID);
		String sqlStatement = "SELECT instructorName FROM instructor WHERE instructorDID = " + String.valueOf(DID)
				+ ";";
		System.out.println(sqlStatement);
		String[] filteredInstructors = null;

		/*
		 * old and buggy erase try { ResultSet result = stmt.executeQuery(sqlStatement);
		 * result.next(); System.out.println("ROWS in filtered: " + result.getRow());
		 * int size = result.getRow(); filteredInstructors = new String[size]; // clean
		 * all this later if (size != 0) filteredInstructors[0] = result.getString(1);
		 * int counter = 1; while (result.next()) { filteredInstructors[counter] =
		 * result.getString(1); counter++; }
		 */

		try {
			ResultSet result = stmt.executeQuery(sqlStatement);
			filteredInstructors = new String[correctSize]; // clean all this later

			int counter = 0;
			while (result.next()) {
				filteredInstructors[counter] = result.getString(1);
				counter++;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		System.out.println("------Instructors by DID------"); // erase later
		for (String i : filteredInstructors) { // erase this later print option
			System.out.println(i);
		}
		return filteredInstructors;

	}

	// Course SQL functions
	public int countCourseMYSQLREPLACED() { // replaced with procedure
		int numOfCourseRows = 0;
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return 0;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "SELECT COUNT(*) FROM course;";
		try {
			ResultSet result = stmt.executeQuery(sqlStatement);
			result.next();
			numOfCourseRows = result.getInt("Count(*)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(sqlStatement);
		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		// System.out.println(numOfStudentRows+1);
		return numOfCourseRows + 1; // returns plus 1 to start SID at 1.

	}

	public int countCourseByDepartmentMYSQL(String department) {
		int numOfCourseRows = 0;
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return 0;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "SELECT COUNT(*) FROM course WHERE department = '" + department + "';";
		try {
			ResultSet result = stmt.executeQuery(sqlStatement);
			result.next();
			numOfCourseRows = result.getInt("Count(*)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(sqlStatement);
		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.println("Num of courses by department: " + numOfCourseRows);
		return numOfCourseRows; // returns plus 1 to start SID at 1.

	}

	public void insertCourseMYSQLREPLACED(Course temp) { // replaced with procedure
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "INSERT INTO course " + "(CNUM, CID, cName, instructor, department) " + "VALUES ("
				+ temp.getCNUM() + ", '" + temp.getCID() + "', '" + temp.getcName() + "', '" + temp.getInstructor()
				+ "', '" + temp.getDepartment() + "' " + ")";
		System.out.println(sqlStatement);
		int rows = 0;
		try {
			rows = stmt.executeUpdate(sqlStatement);
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		System.out.println(rows + " row(s) added to the table.");
		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}

	public void updateCourseMYSQLREPLACED(Course editedCourse) { // replaced with procedure
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "UPDATE course " + "SET CID = '" + editedCourse.getCID() + "', " + "cName = '"
				+ editedCourse.getcName() + "', " + "instructor = '" + editedCourse.getInstructor() + "', "
				+ "department = '" + editedCourse.getDepartment() + "' WHERE CNUM = "
				+ String.valueOf(editedCourse.getCNUM()) + ";";
		// editedStudent.getLastName() + "', '" + editedStudent.getAddress() + "', '" +
		// editedStudent.getCity()+ "', '" + editedStudent.getState()+ "')";
		System.out.println(sqlStatement);
		int rows = 0;
		try {
			rows = stmt.executeUpdate(sqlStatement);
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		System.out.println(rows + " row(s) added to the table.");
		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

	}

	public Course getCourseMYSQLREPLACED(int index) { //replaced with procedure
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return null;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "Select * FROM course WHERE CNUM = " + index + ";";

		Course temp = null;
		try {
			ResultSet result = stmt.executeQuery(sqlStatement);
			result.next();
			int CNUM = result.getInt(1);
			String CID = result.getString(2);
			String cNAME = result.getString(3);
			String instructor = result.getString(4);
			String department = result.getString(5);
			temp = new Course(CNUM, CID, cNAME, instructor, department);
			// numOfStudentRows = result.getInt("Count(*)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return temp;

	}

	public String[] getAllCoursesMYSQLREPLACED() { //replaced with procedure // returns array of all CID
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return null;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "Select CNUM FROM course;";

		String[] allCourses = new String[countCourseMYSQL() - 1];

		try {
			ResultSet result = stmt.executeQuery(sqlStatement);
			int counter = 0;
			while (result.next()) {
				allCourses[counter] = String.valueOf(result.getInt(1));
				counter++;
				System.out.println("counter: " + String.valueOf(counter));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		for (String i : allCourses) { // erase this later print option
			System.out.println(i);
		}
		return allCourses;

	}

	public String[] getFilteredCoursesMYSQL(String department) {
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return null;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		int correctSize = countCourseByDepartmentMYSQL(department);
		String sqlStatement = "SELECT * FROM course WHERE department = " + department + ";";
		System.out.println(sqlStatement);
		String[] filteredCourses = null;

		/*
		 * old and buggy erase try { ResultSet result = stmt.executeQuery(sqlStatement);
		 * result.next(); System.out.println("ROWS in filtered: " + result.getRow());
		 * int size = result.getRow(); filteredInstructors = new String[size]; // clean
		 * all this later if (size != 0) filteredInstructors[0] = result.getString(1);
		 * int counter = 1; while (result.next()) { filteredInstructors[counter] =
		 * result.getString(1); counter++; }
		 */

		try {
			ResultSet result = stmt.executeQuery(sqlStatement);
			filteredCourses = new String[correctSize]; // clean all this later

			int counter = 0;
			while (result.next()) {
				filteredCourses[counter] = result.getString(1);
				counter++;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		System.out.println("------Courses by department------"); // erase later
		for (String i : filteredCourses) { // erase this later print option
			System.out.println(i);
		}
		return filteredCourses;

	}

	// Enrollment SQL function
	public int countEnrollmentMYSQLREPLACED() { // replaced with procedure
		int numOfEnrollmentRows = 0;
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return 0;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "SELECT COUNT(*) FROM enrollment;";
		try {
			ResultSet result = stmt.executeQuery(sqlStatement);
			result.next();
			numOfEnrollmentRows = result.getInt("Count(*)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(sqlStatement);
		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		// System.out.println(numOfStudentRows+1);
		return numOfEnrollmentRows + 1; // returns plus 1 to start SID at 1.

	}

	public int countFilteredEnrollmentMYSQL(int SID, String year, String semester) {
		int numOfEnrollmentRows = 0;
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return 0;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "Select Count(*) FROM enrollment WHERE SID = " + String.valueOf(SID) + " AND year = '"
				+ year + "' AND semester = '" + semester + "';";
		System.out.println(sqlStatement);
		try {
			ResultSet result = stmt.executeQuery(sqlStatement);
			result.next();
			numOfEnrollmentRows = result.getInt("Count(*)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(sqlStatement);
		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.println("Num of filtered enrollments by 3 variables: " + numOfEnrollmentRows);
		return numOfEnrollmentRows; // returns plus 1 to start SID at 1.

	}

	public int countFilteredEnrollmentReportMYSQL(int CNUM, String year) {
		int numOfEnrollmentRows = 0;
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return 0;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "Select Count(*) FROM enrollment WHERE CNUM = " + String.valueOf(CNUM) + " AND year = '"
				+ year + "';";
		System.out.println(sqlStatement);
		try {
			ResultSet result = stmt.executeQuery(sqlStatement);
			result.next();
			numOfEnrollmentRows = result.getInt("Count(*)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(sqlStatement);
		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.println("Num of filtered enrollments by 3 variables: " + numOfEnrollmentRows);
		return numOfEnrollmentRows; // returns plus 1 to start SID at 1.

	}

	public int countEnrollmentBySIDMYSQL(int SID) {
		int numOfEnrollmentRows = 0;
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return 0;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "SELECT COUNT(*) FROM enrollment WHERE SID = " + SID + ";";
		try {
			ResultSet result = stmt.executeQuery(sqlStatement);
			result.next();
			numOfEnrollmentRows = result.getInt("Count(*)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(sqlStatement);
		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.println("Num of enrollments by SID: " + numOfEnrollmentRows);
		return numOfEnrollmentRows; // returns plus 1 to start SID at 1.

	}

	public void insertEnrollmentMYSQLREPLACED(Enrollment temp) { // replaced with procedure
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "INSERT INTO enrollment " + "(EID, SID, CNUM, year, semester, grade) " + "VALUES ("
				+ temp.getEID() + ", '" + temp.getStudentID() + "', '" + temp.getcNum() + "', '" + temp.getYear()
				+ "', '" + temp.getSemester() + "', '" + temp.getGrade() + "' " + ");";
		System.out.println(sqlStatement);
		int rows = 0;
		try {
			rows = stmt.executeUpdate(sqlStatement);
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		System.out.println(rows + " row(s) added to the table.");
		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}

	public void updateEnrollmentMYSQLREPLACED(Enrollment editedEnrollment) { // replaced with procedure
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "UPDATE enrollment " + "SET SID = " + String.valueOf(editedEnrollment.getStudentID())
				+ ", " + "CNUM = " + String.valueOf(editedEnrollment.getcNum()) + ", " + "year = '"
				+ editedEnrollment.getYear() + "', " + "semester = '" + editedEnrollment.getSemester() + "', "
				+ "grade = '" + editedEnrollment.getGrade()

				+ "' WHERE EID = " + String.valueOf(editedEnrollment.getEID()) + ";";
		System.out.println(sqlStatement);
		int rows = 0;
		try {
			rows = stmt.executeUpdate(sqlStatement);
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		System.out.println(rows + " row(s) added to the table.");
		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

	}

	public Enrollment getEnrollmentMYSQLREPLACED(int index) { //replaced with procedure
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return null;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "Select * FROM enrollment WHERE EID = " + index + ";";

		Enrollment temp = null;
		try {
			ResultSet result = stmt.executeQuery(sqlStatement);
			result.next();
			int EID = result.getInt(1);
			int SID = result.getInt(2);
			int CNUM = result.getInt(3);
			String year = result.getString(4);
			String semester = result.getString(5);
			String grade = result.getString(6);
			temp = new Enrollment(EID, SID, CNUM, year, semester, grade);
			// numOfStudentRows = result.getInt("Count(*)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return temp;

	}

	// Department Table Frame functions
	public void openDepartmentTable(String loadData[][]) {
		departmentTableFrame = new JFrame("Department Table");
		JPanel panel = new JPanel();
		String data[][] = loadData;
		String column[] = { "DID", "departmentNAME" };
		JTable table = new JTable(data, column);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setPreferredSize(new Dimension(1, 6000));
		JTableHeader header = table.getTableHeader();
		header.setBackground(Color.yellow);
		JScrollPane pane = new JScrollPane(table);
		panel.add(pane);
		departmentTableFrame.add(panel);
		departmentTableFrame.setSize(500, 400);
		// departmentTableFrame.setUndecorated(true); removed causing an error when you
		// cycle it
		departmentTableFrame.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		departmentTableFrame.setVisible(true);
	}

	public String[][] loadDepartmentTable() {
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return null;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "Select * FROM department;";

		int numberOfDepartmentsCountMYSQL = countDepartmentMYSQL() - 1;

		String[][] allDepartmentData = new String[numberOfDepartmentsCountMYSQL][2];

		try {
			ResultSet result = stmt.executeQuery(sqlStatement);
			int i = 0;
			while (result.next()) {
				/*
				 * for (int j = 0; j < 2; j++) { allDepartmentData[i][j] =
				 * result.getString(j+1); }
				 */
				allDepartmentData[i][0] = String.valueOf(result.getInt(1));
				allDepartmentData[i][1] = result.getString(2);
				i++;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		for (int i = 0; i < numberOfDepartmentsCountMYSQL; i++) {
			System.out.println(allDepartmentData[i][0] + ' ' + allDepartmentData[i][1]);
		}
		return allDepartmentData;
	}

	public void closeDepartmentTable() {
		// closes department Jtable when you press cancel
		departmentTableFrame.dispatchEvent(new WindowEvent(departmentTableFrame, WindowEvent.WINDOW_CLOSING));

	}

	// Instructor Table Frame functions
	public void openInstructorTable(String loadData[][]) {
		instructorTableFrame = new JFrame("Instructor Table");
		JPanel panel = new JPanel();
		String data[][] = loadData;
		String column[] = { "IID", "Instructor Name", "Instructor DID" };
		JTable table = new JTable(data, column);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setPreferredSize(new Dimension(100, 6000));
		JTableHeader header = table.getTableHeader();
		header.setBackground(Color.yellow);
		JScrollPane pane = new JScrollPane(table);
		panel.add(pane);
		instructorTableFrame.add(panel);
		instructorTableFrame.setSize(500, 400);
		// departmentTableFrame.setUndecorated(true); removed causing an error when you
		// cycle it
		instructorTableFrame.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		instructorTableFrame.setVisible(true);
	}

	public String[][] loadInstructorTable() {
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return null;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "Select * FROM instructor;";

		int numberOfInstructorsCountMYSQL = countInstructorMYSQL() - 1;

		String[][] allInstructorData = new String[numberOfInstructorsCountMYSQL][3];

		try {
			ResultSet result = stmt.executeQuery(sqlStatement);
			int i = 0;
			while (result.next()) {
				/*
				 * for (int j = 0; j < 2; j++) { allDepartmentData[i][j] =
				 * result.getString(j+1); }
				 */
				allInstructorData[i][0] = String.valueOf(result.getInt(1));
				allInstructorData[i][1] = result.getString(2);
				allInstructorData[i][2] = String.valueOf(result.getInt(3));
				i++;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		for (int i = 0; i < numberOfInstructorsCountMYSQL; i++) {
			System.out.println(allInstructorData[i][0] + ' ' + allInstructorData[i][1]);
		}
		return allInstructorData;
	}

	public void closeInstructorTable() {
		// closes instructor Jtable when you press cancel
		instructorTableFrame.dispatchEvent(new WindowEvent(instructorTableFrame, WindowEvent.WINDOW_CLOSING));

	}

	// Student Table Frame functions
	public void openStudentTable(String loadData[][]) {
		studentTableFrame = new JFrame("Student Table");
		JPanel panel = new JPanel();
		String data[][] = loadData;
		String column[] = { "SID", "First Name", "Last Name", "Address", "City", "State" };
		JTable table = new JTable(data, column);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setPreferredSize(new Dimension(100, 6000));
		JTableHeader header = table.getTableHeader();
		header.setBackground(Color.yellow);
		JScrollPane pane = new JScrollPane(table);
		panel.add(pane);
		studentTableFrame.add(panel);
		studentTableFrame.setSize(500, 400);
		// departmentTableFrame.setUndecorated(true); removed causing an error when you
		// cycle it
		studentTableFrame.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		studentTableFrame.setVisible(true);
	}

	public String[][] loadStudentTable() { // only one with active procedure
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return null;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "Select * FROM student;";

		String[][] allStudentData = new String[countStudentMYSQL() - 1][6];
		// String[][] allStudentData = new String[countStudentProcedure() - 1][6];
		// //switched to procedure

		try {
			ResultSet result = stmt.executeQuery(sqlStatement);
			int i = 0;
			while (result.next()) {
				/*
				 * for (int j = 0; j < 2; j++) { allDepartmentData[i][j] =
				 * result.getString(j+1); }
				 */
				allStudentData[i][0] = String.valueOf(result.getInt(1));
				allStudentData[i][1] = result.getString(2);
				allStudentData[i][2] = result.getString(3);
				allStudentData[i][3] = result.getString(4);
				allStudentData[i][4] = result.getString(5);
				allStudentData[i][5] = result.getString(6);
				i++;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		/*
		 * for (int i = 0; i < countDepartmentMYSQL() - 1; i++) {
		 * System.out.println(allStudentData[i][0] + ' ' + allStudentData[i][1]); }
		 */
		return allStudentData;
	}

	public void closeStudentTable() {
		// closes student Jtable when you press cancel
		studentTableFrame.dispatchEvent(new WindowEvent(studentTableFrame, WindowEvent.WINDOW_CLOSING));

	}

	// Course Table Frame functions
	public void openCourseTable(String loadData[][]) {
		courseTableFrame = new JFrame("Course Table");
		JPanel panel = new JPanel();
		String data[][] = loadData;
		String column[] = { "CNUM", "CID", "Course Name", "Instructor", "Department" };
		JTable table = new JTable(data, column);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setPreferredSize(new Dimension(100, 6000));
		JTableHeader header = table.getTableHeader();
		header.setBackground(Color.yellow);
		JScrollPane pane = new JScrollPane(table);
		panel.add(pane);
		courseTableFrame.add(panel);
		courseTableFrame.setSize(500, 400);
		// departmentTableFrame.setUndecorated(true); removed causing an error when you
		// cycle it
		courseTableFrame.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		courseTableFrame.setVisible(true);
	}

	public String[][] loadCourseTable() {
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return null;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "Select * FROM course;";

		String[][] allCourseData = new String[countCourseMYSQL() - 1][5];

		try {
			ResultSet result = stmt.executeQuery(sqlStatement);
			int i = 0;
			while (result.next()) {
				/*
				 * for (int j = 0; j < 2; j++) { allDepartmentData[i][j] =
				 * result.getString(j+1); }
				 */
				allCourseData[i][0] = String.valueOf(result.getInt(1));
				allCourseData[i][1] = result.getString(2);
				allCourseData[i][2] = result.getString(3);
				allCourseData[i][3] = result.getString(4);
				allCourseData[i][4] = result.getString(5);
				i++;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		/*
		 * for (int i = 0; i < countDepartmentMYSQL() - 1; i++) {
		 * System.out.println(allStudentData[i][0] + ' ' + allStudentData[i][1]); }
		 */
		return allCourseData;
	}

	public String[][] loadFilteredCourseTable(String department) {
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return null;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "Select * FROM course WHERE department = '" + department + "';";
		System.out.println(sqlStatement);
		int filteredCourseCount = countCourseByDepartmentMYSQL(department);

		// String[][] allCourseData = new String[countCourseMYSQL()-1][5];

		String[][] filteredCourseData = new String[filteredCourseCount][5];

		try {
			ResultSet result = stmt.executeQuery(sqlStatement);
			int i = 0;
			while (result.next()) {
				/*
				 * for (int j = 0; j < 2; j++) { allDepartmentData[i][j] =
				 * result.getString(j+1); }
				 */
				filteredCourseData[i][0] = String.valueOf(result.getInt(1));
				filteredCourseData[i][1] = result.getString(2);
				filteredCourseData[i][2] = result.getString(3);
				filteredCourseData[i][3] = result.getString(4);
				filteredCourseData[i][4] = result.getString(5);
				i++;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		for (int i = 0; i < filteredCourseCount - 1; i++) {
			System.out.println(filteredCourseData[i][0] + " " + filteredCourseData[i][1]);
		}
		return filteredCourseData;
	}

	public void closeCourseTable() {
		// closes course Jtable when you press cancel
		courseTableFrame.dispatchEvent(new WindowEvent(courseTableFrame, WindowEvent.WINDOW_CLOSING));

	}

	// Enrollment Table Frame functions
	public void openEnrollmentTable(String loadData[][]) {
		enrollmentTableFrame = new JFrame("Enrollment Table");
		JPanel panel = new JPanel();
		String data[][] = loadData;
		String column[] = { "EID", "SID", "CNUM", "Year", "Semester", "Grade" };
		JTable table = new JTable(data, column);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setPreferredSize(new Dimension(100, 6000));
		JTableHeader header = table.getTableHeader();
		header.setBackground(Color.yellow);
		JScrollPane pane = new JScrollPane(table);
		panel.add(pane);
		enrollmentTableFrame.add(panel);
		enrollmentTableFrame.setSize(500, 400);
		// departmentTableFrame.setUndecorated(true); removed causing an error when you
		// cycle it
		enrollmentTableFrame.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		enrollmentTableFrame.setVisible(true);
	}

	public String[][] loadEnrollmentTable() {
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return null;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "Select * FROM enrollment;";

		String[][] allEnrollmentData = new String[countEnrollmentMYSQL() - 1][6];

		try {
			ResultSet result = stmt.executeQuery(sqlStatement);
			int i = 0;
			while (result.next()) {
				/*
				 * for (int j = 0; j < 2; j++) { allDepartmentData[i][j] =
				 * result.getString(j+1); }
				 */
				allEnrollmentData[i][0] = String.valueOf(result.getInt(1));
				allEnrollmentData[i][1] = String.valueOf(result.getString(2));
				allEnrollmentData[i][2] = String.valueOf(result.getString(3));
				allEnrollmentData[i][3] = result.getString(4);
				allEnrollmentData[i][4] = result.getString(5);
				allEnrollmentData[i][5] = result.getString(6);
				i++;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		/*
		 * for (int i = 0; i < countDepartmentMYSQL() - 1; i++) {
		 * System.out.println(allStudentData[i][0] + ' ' + allStudentData[i][1]); }
		 */
		return allEnrollmentData;
	}

	public String[][] loadFilteredEnrollmentTable(int SID, String year, String semester) { // USED IN GRADES
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return null;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "Select * FROM enrollment WHERE SID = " + String.valueOf(SID) + " AND year = '" + year
				+ "' AND semester = '" + semester + "';";
		System.out.println(sqlStatement);
		// int filteredEnrollmentCount = countEnrollmentBySIDMYSQL(SID);
		int filteredEnrollmentCount = countFilteredEnrollmentMYSQL(SID, year, semester);

		// String[][] allCourseData = new String[countCourseMYSQL()-1][5];

		String[][] filteredEnrollmentData = new String[filteredEnrollmentCount][6];
		/*
		 * if(filteredEnrollmentCount == 0) { return filteredEnrollmentData = new
		 * String[0][0]; }
		 */
		try {
			ResultSet result = stmt.executeQuery(sqlStatement);
			int i = 0;
			while (result.next()) {
				/*
				 * for (int j = 0; j < 2; j++) { allDepartmentData[i][j] =
				 * result.getString(j+1); }
				 */
				filteredEnrollmentData[i][0] = String.valueOf(result.getInt(1));
				filteredEnrollmentData[i][1] = String.valueOf(result.getInt(2));
				filteredEnrollmentData[i][2] = String.valueOf(result.getInt(3));
				filteredEnrollmentData[i][3] = result.getString(4);
				filteredEnrollmentData[i][4] = result.getString(5);
				filteredEnrollmentData[i][5] = result.getString(6);
				i++;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		/*
		 * for (int i = 0; i < filteredEnrollmentCount - 1; i++) {
		 * System.out.println(filteredEnrollmentData[i][0] + " " +
		 * filteredEnrollmentData[i][1]); }
		 */
		return filteredEnrollmentData;
	}

	public String[][] loadFilteredEnrollmentReportTable(int CNUM, String year) { // USED IN GRADES
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e2) {
			System.out.println("ERROR: Could not connect to the database");
			e2.printStackTrace();
			return null;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sqlStatement = "Select * FROM enrollment WHERE CNUM = " + String.valueOf(CNUM) + " AND year = '" + year
				+ "';";
		System.out.println(sqlStatement);
		// int filteredEnrollmentCount = countEnrollmentBySIDMYSQL(SID);
		int filteredEnrollmentCount = countFilteredEnrollmentReportMYSQL(CNUM, year);

		// String[][] allCourseData = new String[countCourseMYSQL()-1][5];

		String[][] filteredEnrollmentData = new String[filteredEnrollmentCount][6];
		/*
		 * if(filteredEnrollmentCount == 0) { return filteredEnrollmentData = new
		 * String[0][0]; }
		 */
		try {
			ResultSet result = stmt.executeQuery(sqlStatement);
			int i = 0;
			while (result.next()) {
				/*
				 * for (int j = 0; j < 2; j++) { allDepartmentData[i][j] =
				 * result.getString(j+1); }
				 */
				filteredEnrollmentData[i][0] = String.valueOf(result.getInt(1));
				filteredEnrollmentData[i][1] = String.valueOf(result.getInt(2));
				filteredEnrollmentData[i][2] = String.valueOf(result.getInt(3));
				filteredEnrollmentData[i][3] = result.getString(4);
				filteredEnrollmentData[i][4] = result.getString(5);
				filteredEnrollmentData[i][5] = result.getString(6);
				i++;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			conn.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		/*
		 * for (int i = 0; i < filteredEnrollmentCount - 1; i++) {
		 * System.out.println(filteredEnrollmentData[i][0] + " " +
		 * filteredEnrollmentData[i][1]); }
		 */
		return filteredEnrollmentData;
	}

	public void closeEnrollmentTable() {
		// closes enrollment Jtable when you press cancel
		enrollmentTableFrame.dispatchEvent(new WindowEvent(enrollmentTableFrame, WindowEvent.WINDOW_CLOSING));

	}

	// Student&Course Table Frame functions
	public void openCourseStudentTable(String loadData[][], String loadData2[][]) {
		courseStudentTableFrame = new JFrame("Course/Student Table");
		JPanel panel = new JPanel();
		JPanel panel2 = new JPanel();

		String dataC[][] = loadData;
		String columnC[] = { "CNUM", "CID", "CNAME", "Instructor", "Department" };

		String dataS[][] = loadData2;
		String columnS[] = { "SID", "First Name", "Last Name", "Address", "City", "State" };

		JTable tableC = new JTable(dataC, columnC);
		tableC.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tableC.setPreferredSize(new Dimension(100, 6000));
		JTableHeader header2 = tableC.getTableHeader();
		header2.setBackground(Color.yellow);
		JScrollPane pane2 = new JScrollPane(tableC);

		JTable tableS = new JTable(dataS, columnS);
		tableS.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tableS.setPreferredSize(new Dimension(100, 6000));
		JTableHeader header = tableS.getTableHeader();
		header.setBackground(Color.yellow);
		JScrollPane pane = new JScrollPane(tableS);

		panel.add(pane2);
		panel2.add(pane);
		courseStudentTableFrame.add(panel, BorderLayout.CENTER);
		courseStudentTableFrame.add(panel2, BorderLayout.SOUTH);

		courseStudentTableFrame.validate();
		courseStudentTableFrame.setSize(500, 1000);
		// departmentTableFrame.setUndecorated(true); removed causing an error when you
		// cycle it
		courseStudentTableFrame.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		courseStudentTableFrame.setVisible(true);
	}

	public void openFilteredStudentCourseTable(String loadData[][], String loadData2[][]) {
		StudentCourseGradeTableFrame = new JFrame("Student/Course Table");
		JPanel panel = new JPanel();
		JPanel panel2 = new JPanel();

		String dataC[][] = loadData;
		String columnC[] = { "CNUM", "CID", "CNAME", "Instructor", "Department" };

		String dataS[][] = loadData2;
		String columnS[] = { "SID", "First Name", "Last Name", "Address", "City", "State" };

		JTable tableC = new JTable(dataC, columnC);
		tableC.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tableC.setPreferredSize(new Dimension(100, 6000));
		JTableHeader header2 = tableC.getTableHeader();
		header2.setBackground(Color.yellow);
		JScrollPane pane2 = new JScrollPane(tableC);

		JTable tableS = new JTable(dataS, columnS);
		tableS.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tableS.setPreferredSize(new Dimension(100, 6000));
		JTableHeader header = tableS.getTableHeader();
		header.setBackground(Color.yellow);
		JScrollPane pane = new JScrollPane(tableS);

		panel.add(pane);
		panel2.add(pane2);
		StudentCourseGradeTableFrame.add(panel, BorderLayout.CENTER);
		StudentCourseGradeTableFrame.add(panel2, BorderLayout.SOUTH);

		StudentCourseGradeTableFrame.validate();
		StudentCourseGradeTableFrame.setSize(500, 1000);
		// departmentTableFrame.setUndecorated(true); removed causing an error when you
		// cycle it
		StudentCourseGradeTableFrame.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		StudentCourseGradeTableFrame.setVisible(true);
	}

	// Student Listeners
	private class studentAddListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			revalidate();
			repaint();

			openStudentTable(loadStudentTable());

			mainPanel = new JPanel();
			try {
				studentAddPanel = new StudentAddPanel(); // Object StudentAddPanel
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			mainPanel.setLayout(new GridLayout(2, 1));
			createButton = new JButton("Create");
			createButton.addActionListener(new CreateButtonListener());
			JPanel buttonPanel = new JPanel();

			buttonPanel.add(createButton);

			cancelButton_AddPanel = new JButton("Cancel");
			cancelButton_AddPanel.addActionListener(new CancelButtonListener());

			buttonPanel.add(cancelButton_AddPanel);
			mainPanel.add(studentAddPanel);
			mainPanel.add(buttonPanel);

			add(mainPanel);
			setVisible(true);
			revalidate();
			repaint();

		}
	}

	private class studentSearchListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			revalidate();
			repaint();

			openStudentTable(loadStudentTable());

			try {
				studentRecord = new StudentFile("student.dat");
				studentSearchPanel = new StudentSearchPanel(); // Object StudentAddPanel
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			searchButton = new JButton("Search");
			searchButton.addActionListener(new SearchButtonListener());
			cancelButton_SearchPanel = new JButton("Cancel");
			cancelButton_SearchPanel.addActionListener(new CancelButton_SearchPanelListener());
			JPanel buttonPanel = new JPanel();
			buttonPanel.add(searchButton);
			buttonPanel.add(cancelButton_SearchPanel);
			searchPanel = new JPanel();
			searchPanel.setLayout(new GridLayout(2, 1));
			searchPanel.add(studentSearchPanel);
			searchPanel.add(buttonPanel);
			add(searchPanel);
			setVisible(true);

			revalidate();
			repaint();

		}
	}

	private class CreateButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// closes student Jtable when you press cancel
			studentTableFrame.dispatchEvent(new WindowEvent(studentTableFrame, WindowEvent.WINDOW_CLOSING));

			if (studentAddPanel.getFirstName().isEmpty() || studentAddPanel.getLastName().isEmpty()
					|| studentAddPanel.getAddress().isEmpty() || studentAddPanel.getCity().isEmpty()
					|| studentAddPanel.getState().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Cannot have an empty field. Input data purged.");
				remove(mainPanel);
				revalidate();
				repaint();
			} else if (!studentAddPanel.getFirstName().matches("[a-zA-Z\\s]+")
					|| !studentAddPanel.getLastName().matches("[a-zA-Z\\s]+")
					|| !studentAddPanel.getAddress().matches("[a-zA-Z0-9\\s]+")
					|| !studentAddPanel.getCity().matches("[a-zA-Z\\s]+")) {
				JOptionPane.showMessageDialog(null,
						"Use only letters except for address which allows alphanumerics. Input data purged.");
				remove(mainPanel);
				revalidate();
				repaint();
			}

			else {
				int nonAdjustedIndex = studentLinkedList.size();
				int sizeLinkedList = studentLinkedList.size() + 1; // LINKED ADD - To offset index 0
				int numOfStudentRows = countStudentMYSQL();
				Student temp = createStudent(numOfStudentRows);

				insertStudentMYSQL(temp);
				openStudentTable(loadStudentTable());
				// This needs to be removed later because database

				try {
					studentRecord.moveFilePointer(nonAdjustedIndex); // No adjustment here
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					studentRecord.writeStudentFile(temp);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				studentLinkedList.add(temp); // LINKED ADD
				printStudentLinkedList(); // LINKED ADD -- TESTING
				remove(mainPanel);
				revalidate();
				repaint();
				displayPanel = new JPanel();
				studentDisplayPanel = new StudentDisplayPanel(temp);
				okButton_DisplayPanel = new JButton("OK");
				okButton_DisplayPanel.addActionListener(new OkButtonListener());

				editButton_DisplayPanel = new JButton("Edit");
				editButton_DisplayPanel.addActionListener(new EditButton_DisplayPanelListener());

				// deleteButton_DisplayPanel = new JButton("Delete");
				// deleteButton_DisplayPanel.addActionListener(new
				// DeleteButton_DisplayPanelListener());

				displayPanel.setLayout(new GridLayout(2, 1));
				buttonPanel = new JPanel();
				buttonPanel.add(okButton_DisplayPanel);
				buttonPanel.add(editButton_DisplayPanel);
				// buttonPanel.add(deleteButton_DisplayPanel);
				displayPanel.add(studentDisplayPanel);
				displayPanel.add(buttonPanel);

				add(displayPanel);
				revalidate();
				repaint();
			}
		}
	}

	private class CancelButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// closes student Jtable when you press cancel
			studentTableFrame.dispatchEvent(new WindowEvent(studentTableFrame, WindowEvent.WINDOW_CLOSING));

			remove(mainPanel);
			revalidate();
			repaint();
		}
	}

	private class CancelButton_SearchPanelListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// closes student Jtable when you press cancel
			studentTableFrame.dispatchEvent(new WindowEvent(studentTableFrame, WindowEvent.WINDOW_CLOSING));

			remove(searchPanel);
			revalidate();
			repaint();
		}
	}

	private class OkButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// closes student Jtable when you press cancel
			studentTableFrame.dispatchEvent(new WindowEvent(studentTableFrame, WindowEvent.WINDOW_CLOSING));

			displayPanel.setVisible(false);
		}
	}

	private class EditButton_DisplayPanelListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			remove(displayPanel);
			revalidate();
			repaint();

			try {
				// studentRecord = new StudentFile("student.dat"); //ERASE THIS
				studentEditPanel = new StudentEditPanel(studentDisplayPanel.student); // pass student object here and
																						// make a constructor
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			saveButton_EditPanel = new JButton("Save");
			saveButton_EditPanel.addActionListener(new SaveButtonListener());
			cancelButton_EditPanel = new JButton("Cancel");
			cancelButton_EditPanel.addActionListener(new CancelButton_EditPanelListener());
			JPanel buttonPanel = new JPanel();
			buttonPanel.add(saveButton_EditPanel);
			buttonPanel.add(cancelButton_EditPanel);
			editPanel = new JPanel();
			editPanel.setLayout(new GridLayout(2, 1));
			editPanel.add(studentEditPanel);
			editPanel.add(buttonPanel);
			add(editPanel);
			setVisible(true);
			revalidate();
			repaint();
		}
	}

	private class CancelButton_EditPanelListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			remove(editPanel);
			revalidate();
			repaint();
		}
	}

	private class SaveButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (studentEditPanel.getStudentEdit_firstNameText2().isEmpty()
					|| studentEditPanel.getStudentEdit_lastNameText2().isEmpty()
					|| studentEditPanel.getStudentEdit_addressText2().isEmpty()
					|| studentEditPanel.getStudentEdit_cityText2().isEmpty()
					|| studentEditPanel.getStudentEdit_stateText2().isEmpty()) {

				JOptionPane.showMessageDialog(null, "Cannot have an empty field. Input data purged.");
				closeStudentTable();
				remove(editPanel);
				revalidate();
				repaint();

			} else if (!studentEditPanel.getStudentEdit_firstNameText2().matches("[a-zA-Z\\s]+")
					|| !studentEditPanel.getStudentEdit_lastNameText2().matches("[a-zA-Z\\s]+")
					|| !studentEditPanel.getStudentEdit_addressText2().matches("[a-zA-Z0-9\\s]+")
					|| !studentEditPanel.getStudentEdit_cityText2().matches("[a-zA-Z\\s]+")) {
				JOptionPane.showMessageDialog(null,
						"Use only letters except for address which allows alphanumerics. Input data purged.");
				closeStudentTable();
				remove(editPanel);
				revalidate();
				repaint();
			}

			else {

				int number = studentEditPanel.student.getID() - 1; // used to pull index from text records
				int noOffsetNumber = studentEditPanel.student.getID(); // Added for MYSQL since no offset is needed
																		// anymore, also used for linkedlist
				System.out.println("Number: " + number);

				String newFirstName = studentEditPanel.getStudentEdit_firstNameText2();
				String newLastName = studentEditPanel.getStudentEdit_lastNameText2();
				String newAddress = studentEditPanel.getStudentEdit_addressText2();
				String newCity = studentEditPanel.getStudentEdit_cityText2();
				String newState = studentEditPanel.getStudentEdit_stateText2();
				Student editedStudent = new Student(noOffsetNumber, newFirstName, newLastName, newAddress, newCity,
						newState);

				// closes student Jtable when you press cancel
				studentTableFrame.dispatchEvent(new WindowEvent(studentTableFrame, WindowEvent.WINDOW_CLOSING));

				updateStudentMYSQL(editedStudent);
				openStudentTable(loadStudentTable());

				try {
					studentRecord.moveFilePointer(number);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					studentRecord.writeStudentFile(editedStudent);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					studentRecord.moveFilePointer(studentRecord.getNumberOfRecords());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				studentLinkedList.overwrite(number, editedStudent);
				remove(editPanel);
				revalidate();
				repaint();
				displayPanel = new JPanel();
				studentDisplayPanel = new StudentDisplayPanel(editedStudent);
				okButton_DisplayPanel = new JButton("OK");
				okButton_DisplayPanel.addActionListener(new OkButtonListener());

				editButton_DisplayPanel = new JButton("Edit");
				editButton_DisplayPanel.addActionListener(new EditButton_DisplayPanelListener());

				// deleteButton_DisplayPanel = new JButton("Delete");
				// deleteButton_DisplayPanel.addActionListener(new
				// DeleteButton_DisplayPanelListener());

				displayPanel.setLayout(new GridLayout(2, 1));
				buttonPanel = new JPanel();
				buttonPanel.add(okButton_DisplayPanel);
				buttonPanel.add(editButton_DisplayPanel);
				// buttonPanel.add(deleteButton_DisplayPanel); removed delete option for now
				displayPanel.add(studentDisplayPanel);
				displayPanel.add(buttonPanel);

				add(displayPanel);
				revalidate();
				repaint();

				printStudentLinkedList();
			}
		}
	}

	private class SearchButtonListener implements ActionListener { // ERASE OLD LINES
		public void actionPerformed(ActionEvent e) {
			closeStudentTable();

			int SIDValue = 0;

			if (studentSearchPanel.getInputStudentIDText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Cannot have an empty field. Input data purged.");
				remove(searchPanel);
				revalidate();
				repaint();
			}

			else if (!studentSearchPanel.getInputStudentIDText().matches("[0-9]+")) {
				JOptionPane.showMessageDialog(null, "Use only numbers and no spaces. Input data purged.");
				remove(searchPanel);
				revalidate();
				repaint();
			}

			else {
				int numberOfStudentsCountMYSQL = countStudentMYSQL() - 1;
				SIDValue = Integer.parseInt(studentSearchPanel.getInputStudentIDText());
				if (SIDValue > numberOfStudentsCountMYSQL || SIDValue <= 0) {
					JOptionPane.showMessageDialog(null,
							"Student ID input value of " + SIDValue + " is higher than the actual stored"
									+ " records value of " + (numberOfStudentsCountMYSQL) // count is 1 ahead because
																							// first
																							// database entry is 1 and
																							// no 0.
									+ ". Value also cannot be 0 or below.");
					closeStudentTable();

					remove(searchPanel);
					revalidate();
					repaint();
				} else {
					Student temp = null;
					try {
						if (numberOfStudentsCountMYSQL != 0) {
							int number = -1;
							while (number < 1 || number > numberOfStudentsCountMYSQL) { // was plus one for count but
																						// left it
								number = Integer.parseInt(studentSearchPanel.getInputStudentIDText());
							}
							int noOffsetNumber = number;
							temp = getStudentMYSQL(noOffsetNumber);
						}
					} catch (NumberFormatException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					// setVisible(false);
					remove(searchPanel);
					revalidate();
					repaint();
					displayPanel = new JPanel();
					studentDisplayPanel = new StudentDisplayPanel(temp);
					okButton_DisplayPanel = new JButton("OK");
					okButton_DisplayPanel.addActionListener(new OkButtonListener());

					editButton_DisplayPanel = new JButton("Edit");
					editButton_DisplayPanel.addActionListener(new EditButton_DisplayPanelListener());

					// deleteButton_DisplayPanel = new JButton("Delete");
					// deleteButton_DisplayPanel.addActionListener(new
					// DeleteButton_DisplayPanelListener());

					displayPanel.setLayout(new GridLayout(2, 1));
					buttonPanel = new JPanel();
					buttonPanel.add(okButton_DisplayPanel);
					buttonPanel.add(editButton_DisplayPanel);
					// buttonPanel.add(deleteButton_DisplayPanel); //Removed delete button feature
					displayPanel.add(studentDisplayPanel);
					displayPanel.add(buttonPanel);

					add(displayPanel);
					revalidate();
					repaint();
				}
			}

		}

	}

	private class SearchButtonListenerOLD implements ActionListener { // ERASE OLD LINES //not used right now
		public void actionPerformed(ActionEvent e) {

			int SIDValue = 0;

			if (!studentSearchPanel.getInputStudentIDText().isEmpty())
				SIDValue = Integer.parseInt(studentSearchPanel.getInputStudentIDText());

			if (studentSearchPanel.getInputStudentIDText().isEmpty()) {

				JOptionPane.showMessageDialog(null, "Cannot have an empty field. Input data purged.");
				remove(searchPanel);
				revalidate();
				repaint();
			}
			// checks size from database
			else if (SIDValue > (countStudentMYSQL() - 1) || SIDValue <= 0) {
				try {
					JOptionPane.showMessageDialog(null,
							"Student ID input value of " + SIDValue + " is higher than the actual stored"
									+ " records value of " + (countStudentMYSQL() - 1) // count is 1 ahead because first
																						// database entry is 1 and no 0.
									+ ". Value also cannot be 0 or below.");
					closeStudentTable();
				} catch (HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				remove(searchPanel);
				revalidate();
				repaint();
			}

			else {
				Student temp = null;
				try {
					if (countStudentMYSQL() != 0) {
						int number = -1;
						while (number < 1 || number > countStudentMYSQL()) {
							number = Integer.parseInt(studentSearchPanel.getInputStudentIDText());
						}
						int noOffsetNumber = number;
						temp = getStudentMYSQL(noOffsetNumber);
					}
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// setVisible(false);
				remove(searchPanel);
				revalidate();
				repaint();
				displayPanel = new JPanel();
				studentDisplayPanel = new StudentDisplayPanel(temp);
				okButton_DisplayPanel = new JButton("OK");
				okButton_DisplayPanel.addActionListener(new OkButtonListener());

				editButton_DisplayPanel = new JButton("Edit");
				editButton_DisplayPanel.addActionListener(new EditButton_DisplayPanelListener());

				// deleteButton_DisplayPanel = new JButton("Delete");
				// deleteButton_DisplayPanel.addActionListener(new
				// DeleteButton_DisplayPanelListener());

				displayPanel.setLayout(new GridLayout(2, 1));
				buttonPanel = new JPanel();
				buttonPanel.add(okButton_DisplayPanel);
				buttonPanel.add(editButton_DisplayPanel);
				// buttonPanel.add(deleteButton_DisplayPanel); //Removed delete button feature
				displayPanel.add(studentDisplayPanel);
				displayPanel.add(buttonPanel);

				add(displayPanel);
				revalidate();
				repaint();

			}

		}
	}

	private class SearchButtonListener2 implements ActionListener { // ERASE OLD LINES
		public void actionPerformed(ActionEvent e) {

			int SIDValue = 0;
			if (!studentSearchPanel.getInputStudentIDText().isEmpty())
				SIDValue = Integer.parseInt(studentSearchPanel.getInputStudentIDText());
			int mappedSIDValue = mappedSIDValue(SIDValue); // Gets correct index thats moves after deletes
			System.out.println("Mapped SID: " + mappedSIDValue);
			if (studentSearchPanel.getInputStudentIDText().isEmpty()) {

				JOptionPane.showMessageDialog(null, "Cannot have an empty field. Input data purged.");
				remove(searchPanel);
				revalidate();
				repaint();
			} else // (SIDValue > studentRecord.getNumberOfRecords() || SIDValue == 0)
			// if (mappedSIDValue >= studentLinkedList.size() || mappedSIDValue < 0) {
			if (studentPosition.indexOf(SIDValue) != -1) {
				try {
					JOptionPane.showMessageDialog(null,
							"Student ID input value of " + mappedSIDValue + " is higher than the actual stored"
									+ " records value of " + studentLinkedList.size()
									+ ". Value also cannot be 0 or below.");
				} catch (HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				remove(searchPanel);
				revalidate();
				repaint();
			} else if (studentPosition.indexOf(SIDValue) != -1) {
				JOptionPane.showMessageDialog(null, "No such Student found");
				remove(searchPanel);
				revalidate();
				repaint();
			}

			else {
				Student temp = null;
				try {
					if (studentLinkedList.size() != 0) {
						int number = -1;
						while (number < 1 || number > studentLinkedList.size()) {
							// number = Integer.parseInt(studentSearchPanel.getInputStudentIDText());
							number = mappedSIDValue;
						}
						// number = number - 1;
						// studentRecord.moveFilePointer(number);
						// temp = studentRecord.readStudentFile();
						temp = studentLinkedList.get(number);
						// temp.printStudent();
						// studentRecord.moveFilePointer(studentRecord.getNumberOfRecords());

					}
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// setVisible(false);
				remove(searchPanel);
				revalidate();
				repaint();
				displayPanel = new JPanel();
				studentDisplayPanel = new StudentDisplayPanel(temp);
				okButton_DisplayPanel = new JButton("OK");
				okButton_DisplayPanel.addActionListener(new OkButtonListener());

				editButton_DisplayPanel = new JButton("Edit");
				editButton_DisplayPanel.addActionListener(new EditButton_DisplayPanelListener());

				deleteButton_DisplayPanel = new JButton("Delete");
				deleteButton_DisplayPanel.addActionListener(new DeleteButton_DisplayPanelListener());

				displayPanel.setLayout(new GridLayout(2, 1));
				buttonPanel = new JPanel();
				buttonPanel.add(okButton_DisplayPanel);
				buttonPanel.add(editButton_DisplayPanel);
				buttonPanel.add(deleteButton_DisplayPanel);
				displayPanel.add(studentDisplayPanel);
				displayPanel.add(buttonPanel);

				add(displayPanel);
				revalidate();
				repaint();

			}

		}
	}

	private class DeleteButton_DisplayPanelListener implements ActionListener { // Alters student ID number for position
																				// NOT PROPER
		public void actionPerformed(ActionEvent e) {
			// int studentIndex = studentDisplayPanel.student.getID()-1; //adjusted for
			// starting at 0;
			loadStudentPositionArrayList();
			int SIDToDelete = studentDisplayPanel.student.getID();
			int posOfDelete = studentPosition.indexOf(SIDToDelete);
			// System.out.println("STUDENT INDEX: " + studentIndex);
			JOptionPane.showMessageDialog(null, studentDisplayPanel.student.getFirstName() + " was erased.");
			studentLinkedList.remove(posOfDelete);

			studentRecord.setFileLength();
			try {
				studentRecord.moveFilePointer(0);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			for (int i = 0; i < studentLinkedList.size(); i++) {
				Student temp = studentLinkedList.get(i);
				// temp.setID(i+1); // make sure that first student starts a 1 and not 0
				try {
					studentRecord.writeStudentFile(temp);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			try {
				studentRecord.moveFilePointer(studentRecord.getNumberOfRecords());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// Ask to erase all enrollments student has.
			// Call function to overwrite the student binary file to reflect new order
			// Call function to
			// Call function to overwrite the enrollment binary file to reflect missing
			// student
			printStudentLinkedList();
			displayPanel.setVisible(false);
		}
	}
	// Course Listeners

	private class preCourseAddListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			revalidate();
			repaint();

			if ((countDepartmentMYSQL() - 1) == 0) {

				JOptionPane.showMessageDialog(null, "Please add at least one department first.");
				revalidate();
				repaint();
			}

			else if ((countInstructorMYSQL() - 1) == 0) {

				JOptionPane.showMessageDialog(null, "Please add at least one instructor first.");
				revalidate();
				repaint();
			}

			else {
				openCourseTable(loadCourseTable());
				try {
					courseRecord = new CourseFile("course.dat");

				} catch (FileNotFoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				preMainPanel_Course = new JPanel();
				// mainPanel.setLayout(new GridLayout(2,1));
				try {
					// courseAddPanel = new CourseAddPanel(departmentLinkedList,
					// instructorLinkedList); // Takes in a linked list for combo box
					preCourseAddPanel = new PreCourseAddPanel(getAllDepartmentsMYSQL()); // Takes in a linked list for
																							// combo
																							// box
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				preMainPanel_Course.setLayout(new GridLayout(2, 1));
				nextButton_Course = new JButton("Next");
				nextButton_Course.addActionListener(new CourseAddListener()); // add new listener
				JPanel buttonPanel = new JPanel();

				buttonPanel.add(nextButton_Course);

				nextCancel_Course = new JButton("Cancel");
				nextCancel_Course.addActionListener(new nextCancelButton_prePanelListener_Course());

				buttonPanel.add(nextCancel_Course);
				preMainPanel_Course.add(preCourseAddPanel);
				preMainPanel_Course.add(buttonPanel);

				add(preMainPanel_Course);
				setVisible(true);
				revalidate();
				repaint();
			}
		}
	}

	private class nextCancelButton_prePanelListener_Course implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// closes course Jtable when you press cancel
			courseTableFrame.dispatchEvent(new WindowEvent(courseTableFrame, WindowEvent.WINDOW_CLOSING));

			remove(preMainPanel_Course);
			revalidate();
			repaint();
		}
	}

	// Next calls course ADD
	private class CourseAddListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// closes course Jtable when you press cancel
			closeCourseTable();
			remove(preMainPanel_Course);
			revalidate();
			repaint();

			try {
				courseRecord = new CourseFile("course.dat");

			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			mainPanel_Course = new JPanel();
			// mainPanel.setLayout(new GridLayout(2,1));

			// courseAddPanel = new CourseAddPanel(departmentLinkedList,
			// instructorLinkedList); // Takes in a linked list for combo box
			String prePanelDepartmentName = preCourseAddPanel.getDepartment();

			// section added to enable combobox string to give proper index of that
			// department.
			// Was getting incorrect index based on array position
			int prePanelDepartmentIndex = getDepartmentIndexMYSQL(prePanelDepartmentName);

			// int prePanelDepartmentIndex = preCourseAddPanel.getDepartmentIndex() + 1;
			System.out.println("Value of DID: " + prePanelDepartmentIndex); // erase later
			String[] filteredInstructors = getFilteredInstructorsMYSQL(prePanelDepartmentIndex);
			int lengthOfFilteredInstructors = filteredInstructors.length;
			System.out.println("Size of filtered Array: " + filteredInstructors.length); // erase later

			openCourseTable(loadFilteredCourseTable(preCourseAddPanel.getDepartment()));

			if (lengthOfFilteredInstructors != 0) {

				try {
					courseAddPanel = new CourseAddPanel(prePanelDepartmentName,
							getFilteredInstructorsMYSQL(prePanelDepartmentIndex));
					mainPanel_Course.setLayout(new GridLayout(2, 1));
					createButton_Course = new JButton("Create");
					createButton_Course.addActionListener(new CreateButtonListener_Course());
					JPanel buttonPanel = new JPanel();

					buttonPanel.add(createButton_Course);

					cancelButton_AddPanel_Course = new JButton("Cancel");
					cancelButton_AddPanel_Course.addActionListener(new CancelButtonListener_Course());

					buttonPanel.add(cancelButton_AddPanel_Course);
					mainPanel_Course.add(courseAddPanel);
					mainPanel_Course.add(buttonPanel);

					add(mainPanel_Course);
					setVisible(true);
					revalidate();
					repaint();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} // Takes in a linked list for combo box
			}

			else {
				JOptionPane.showMessageDialog(null, "No instructors found in the " + prePanelDepartmentName
						+ " department.  Please add before continuing.");
				remove(mainPanel_Course);
				revalidate();
				repaint();
			}

		}
	}

	private class CancelButtonListener_Course implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// closes course Jtable when you press cancel
			courseTableFrame.dispatchEvent(new WindowEvent(courseTableFrame, WindowEvent.WINDOW_CLOSING));

			remove(mainPanel_Course);
			revalidate();
			repaint();
		}
	}

	private class CreateButtonListener_Course implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// closes course Jtable when you press cancel
			courseTableFrame.dispatchEvent(new WindowEvent(courseTableFrame, WindowEvent.WINDOW_CLOSING));

			if (courseAddPanel.getCID().isEmpty() || courseAddPanel.getcName().isEmpty()
					|| courseAddPanel.getInstructor().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Cannot have an empty field. Input data purged.");
				remove(mainPanel_Course);
				revalidate();
				repaint();
			}

			else if (!courseAddPanel.getCID().matches("[a-zA-Z0-9]+")
					|| !courseAddPanel.getcName().matches("[a-zA-Z0-9\\s]+")) {
				JOptionPane.showMessageDialog(null,
						"No space allowed for CID. No special special characters allowed. Input data purged.");
				remove(mainPanel_Course);
				revalidate();
				repaint();
			}

			else {
				int nonAdjustedIndex = courseLinkedList.size();
				int sizeLinkedList = courseLinkedList.size() + 1; // LINKED ADD - To offset index 0

				int numOfDepartmentRows = countCourseMYSQL();

				// Course temp = createCourse(sizeLinkedList); // Index starts at 0 so need to
				// offset by 1
				Course temp = createCourse(numOfDepartmentRows);

				insertCourseMYSQL(temp);
				openCourseTable(loadFilteredCourseTable(preCourseAddPanel.getDepartment()));

				try {
					courseRecord.moveFilePointer(nonAdjustedIndex);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					courseRecord.writeCourseFile(temp);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				courseLinkedList.add(temp); // LINKED ADD
				printCourseLinkedList(); // LINKED ADD -- TESTING

				remove(mainPanel_Course);
				revalidate();
				repaint();

				displayPanel_Course = new JPanel();
				courseDisplayPanel = new CourseDisplayPanel(temp);
				okButton_DisplayPanel_Course = new JButton("OK");
				okButton_DisplayPanel_Course.addActionListener(new OkButtonListener_Course());

				editButton_DisplayPanel_Course = new JButton("Edit");
				// editButton_DisplayPanel_Course.addActionListener(new
				// EditButton_DisplayPanelListener_Course());
				// replaced because needed a pre option now.
				editButton_DisplayPanel_Course.addActionListener(new preEditListener_Course());

				displayPanel_Course.setLayout(new GridLayout(2, 1));
				buttonPanel_Course = new JPanel();
				buttonPanel_Course.add(okButton_DisplayPanel_Course);
				buttonPanel_Course.add(editButton_DisplayPanel_Course);
				displayPanel_Course.add(courseDisplayPanel);
				displayPanel_Course.add(buttonPanel_Course);

				add(displayPanel_Course);
				revalidate();
				repaint();
			}
		}
	}

	private class OkButtonListener_Course implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// closes course Jtable when you press cancel
			courseTableFrame.dispatchEvent(new WindowEvent(courseTableFrame, WindowEvent.WINDOW_CLOSING));

			displayPanel_Course.setVisible(false);
		}
	}

	private class preEditListener_Course implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			remove(displayPanel_Course);
			revalidate();
			repaint();

			try {
				courseRecord = new CourseFile("course.dat");

			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			preEditPanel_Course = new JPanel();
			// mainPanel.setLayout(new GridLayout(2,1));
			try {
				// courseAddPanel = new CourseAddPanel(departmentLinkedList,
				// instructorLinkedList); // Takes in a linked list for combo box
				preEditCoursePanel = new PreEditCoursePanel(getAllDepartmentsMYSQL()); // Takes in a linked list for
																						// combo
																						// box
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			preEditPanel_Course.setLayout(new GridLayout(2, 1));
			nextButton_PreEdit_Course = new JButton("Next");
			nextButton_PreEdit_Course.addActionListener(new EditButton_DisplayPanelListener_Course()); // add new
																										// listener
			JPanel buttonPanel = new JPanel();

			buttonPanel.add(nextButton_PreEdit_Course);

			nextCancel_PreEdit_Course = new JButton("Cancel");
			nextCancel_PreEdit_Course.addActionListener(new nextCancelButton_preEditPanelListener_Course());

			buttonPanel.add(nextCancel_PreEdit_Course);
			preEditPanel_Course.add(preEditCoursePanel);
			preEditPanel_Course.add(buttonPanel);

			add(preEditPanel_Course);
			// setVisible(true);
			revalidate();
			repaint();

		}
	}

	private class nextCancelButton_preEditPanelListener_Course implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// closes course Jtable when you press cancel
			courseTableFrame.dispatchEvent(new WindowEvent(courseTableFrame, WindowEvent.WINDOW_CLOSING));

			remove(preEditPanel_Course);
			revalidate();
			repaint();
		}
	}

	private class EditButton_DisplayPanelListener_Course implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			remove(preEditPanel_Course);
			revalidate();
			repaint();

			String preEditPanelDepartmentName = preEditCoursePanel.getDepartment();
			int preEditPanelDepartmentIndex = getDepartmentIndexMYSQL(preEditPanelDepartmentName);
			System.out.println("Value of DID: " + preEditPanelDepartmentIndex); // erase later
			String[] filteredInstructors = getFilteredInstructorsMYSQL(preEditPanelDepartmentIndex);
			int lengthOfFilteredInstructors = filteredInstructors.length;
			System.out.println("Size of filtered Array: " + filteredInstructors.length); // erase later

			if (lengthOfFilteredInstructors != 0) {

				try {
					courseRecord = new CourseFile("course.dat");
					// courseEditPanel = new CourseEditPanel(courseDisplayPanel.course,
					// departmentLinkedList,
					// instructorLinkedList); // pass student object here and make a
					// constructor
					courseEditPanel = new CourseEditPanel(courseDisplayPanel.course, preEditPanelDepartmentName,
							getFilteredInstructorsMYSQL(preEditPanelDepartmentIndex));

				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				saveButton_EditPanel_Course = new JButton("Save");
				saveButton_EditPanel_Course.addActionListener(new SaveButtonListener_Course());
				cancelButton_EditPanel_Course = new JButton("Cancel");
				cancelButton_EditPanel_Course.addActionListener(new CancelButton_EditPanelListener_Course());
				JPanel buttonPanel = new JPanel();
				buttonPanel.add(saveButton_EditPanel_Course);
				buttonPanel.add(cancelButton_EditPanel_Course);
				editPanel_Course = new JPanel();
				editPanel_Course.setLayout(new GridLayout(2, 1));
				editPanel_Course.add(courseEditPanel);
				editPanel_Course.add(buttonPanel);

				add(editPanel_Course);
				// setVisible(true);
				revalidate();
				repaint();
			} else {
				JOptionPane.showMessageDialog(null, "No instructors found in the " + preEditPanelDepartmentName
						+ " department.  Please add before continuing.");
				remove(editPanel_Course);
				revalidate();
				repaint();
			}

		}
	}

	private class EditButton_DisplayPanelListener_CourseOLD implements ActionListener { // had a bug stopped using -
																						// getting wrong combobox index
																						// in course edit panel
		public void actionPerformed(ActionEvent e) {

			remove(preEditPanel_Course);
			revalidate();
			repaint();

			String preEditPanelDepartmentName = preEditCoursePanel.getDepartment();
			int preEditPanelDepartmentIndex = preEditCoursePanel.getDepartmentIndex() + 1;
			System.out.println("Value of DID: " + preEditPanelDepartmentIndex); // erase later
			String[] filteredInstructors = getFilteredInstructorsMYSQL(preEditPanelDepartmentIndex);
			int lengthOfFilteredInstructors = filteredInstructors.length;
			System.out.println("Size of filtered Array: " + filteredInstructors.length); // erase later

			if (lengthOfFilteredInstructors != 0) {

				try {
					courseRecord = new CourseFile("course.dat");
					// courseEditPanel = new CourseEditPanel(courseDisplayPanel.course,
					// departmentLinkedList,
					// instructorLinkedList); // pass student object here and make a
					// constructor
					courseEditPanel = new CourseEditPanel(courseDisplayPanel.course, preEditPanelDepartmentName,
							getFilteredInstructorsMYSQL(preEditPanelDepartmentIndex));

				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				saveButton_EditPanel_Course = new JButton("Save");
				saveButton_EditPanel_Course.addActionListener(new SaveButtonListener_Course());
				cancelButton_EditPanel_Course = new JButton("Cancel");
				cancelButton_EditPanel_Course.addActionListener(new CancelButton_EditPanelListener_Course());
				JPanel buttonPanel = new JPanel();
				buttonPanel.add(saveButton_EditPanel_Course);
				buttonPanel.add(cancelButton_EditPanel_Course);
				editPanel_Course = new JPanel();
				editPanel_Course.setLayout(new GridLayout(2, 1));
				editPanel_Course.add(courseEditPanel);
				editPanel_Course.add(buttonPanel);

				add(editPanel_Course);
				// setVisible(true);
				revalidate();
				repaint();
			} else {
				JOptionPane.showMessageDialog(null, "No instructors found in the " + preEditPanelDepartmentName
						+ " department.  Please add before continuing.");
				remove(editPanel_Course);
				revalidate();
				repaint();
			}

		}
	}

	private class CancelButton_EditPanelListener_Course implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// closes course Jtable when you press cancel
			courseTableFrame.dispatchEvent(new WindowEvent(courseTableFrame, WindowEvent.WINDOW_CLOSING));

			remove(editPanel_Course);
			revalidate();
			repaint();
		}
	}

	private class SaveButtonListener_Course implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// closes course Jtable when you press cancel
			courseTableFrame.dispatchEvent(new WindowEvent(courseTableFrame, WindowEvent.WINDOW_CLOSING));

			remove(editPanel_Course);
			revalidate();
			repaint();

			if (courseEditPanel.getCourseEdit_CIDText2().isEmpty()
					|| courseEditPanel.getCourseEdit_cNameText2().isEmpty()) {

				JOptionPane.showMessageDialog(null, "Cannot have an empty field. Input data purged.");
				remove(editPanel_Course);
				revalidate();
				repaint();
			}

			else if (!courseEditPanel.getCourseEdit_CIDText2().matches("[a-zA-Z0-9]+")
					|| !courseEditPanel.getCourseEdit_cNameText2().matches("[a-zA-Z0-9\\s]+")) {
				JOptionPane.showMessageDialog(null,
						"No space allowed for CID. No special special characters allowed. Input data purged.");
				remove(mainPanel_Course);
				revalidate();
				repaint();
			}

			else {

				int number = courseEditPanel.course.getCNUM() - 1;
				int noOffsetNumber = courseEditPanel.course.getCNUM(); // Added for MYSQL since no offset is
				// needed anymore, also used for
				// linkedlist

				// Course editedCourse = courseLinkedList.get(noOffsetNumber); // LINKED ADD

				String newCID = courseEditPanel.getCourseEdit_CIDText2();
				String newcName = courseEditPanel.getCourseEdit_cNameText2();
				String newInstructor = courseEditPanel.getInstructor();
				String newDepartment = courseEditPanel.getDepartment();

				Course editedCourse = new Course(noOffsetNumber, newCID, newcName, newInstructor, newDepartment);

				updateCourseMYSQL(editedCourse);
				openCourseTable(loadCourseTable());
				try {
					courseRecord.moveFilePointer(number);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					courseRecord.writeCourseFile(editedCourse);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					courseRecord.moveFilePointer(courseRecord.getNumberOfRecords());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				courseLinkedList.overwrite(number, editedCourse);

				displayPanel_Course = new JPanel();
				courseDisplayPanel = new CourseDisplayPanel(editedCourse);
				okButton_DisplayPanel_Course = new JButton("OK");
				okButton_DisplayPanel_Course.addActionListener(new OkButtonListener_Course());

				editButton_DisplayPanel_Course = new JButton("Edit");
				// editButton_DisplayPanel_Course.addActionListener(new
				// EditButton_DisplayPanelListener_Course());
				editButton_DisplayPanel_Course.addActionListener(new preEditListener_Course());

				displayPanel_Course.setLayout(new GridLayout(2, 1));
				buttonPanel_Course = new JPanel();
				buttonPanel_Course.add(okButton_DisplayPanel_Course);
				buttonPanel_Course.add(editButton_DisplayPanel_Course);
				displayPanel_Course.add(courseDisplayPanel);
				displayPanel_Course.add(buttonPanel_Course);

				add(displayPanel_Course);
				revalidate();
				repaint();

				/*
				 * editDisplayPanel_Course = new JPanel(); courseEditDisplayPanel = new
				 * CourseEditDisplayPanel(editedCourse); okButton_EditDisplayPanel_Course = new
				 * JButton("OK"); okButton_EditDisplayPanel_Course.addActionListener(new
				 * OkButtonListener_Course()); //replace
				 * 
				 * editButton_DisplayPanel_Course = new JButton("Edit");
				 * editButton_DisplayPanel_Course.addActionListener(new
				 * EditButton_DisplayPanelListener_Course());
				 * 
				 * displayPanel_Course.setLayout(new GridLayout(2, 1)); buttonPanel_Course = new
				 * JPanel(); buttonPanel_Course.add(okButton_DisplayPanel_Course);
				 * buttonPanel_Course.add(editButton_DisplayPanel_Course);
				 * displayPanel_Course.add(courseDisplayPanel);
				 * displayPanel_Course.add(buttonPanel_Course);
				 * 
				 * add(displayPanel_Course); revalidate(); repaint();
				 */

				printCourseLinkedList();
			}
		}
	}

	private class CourseSearchListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			openCourseTable(loadCourseTable());
			revalidate();
			repaint();

			try {
				courseRecord = new CourseFile("course.dat");
				courseSearchPanel = new CourseSearchPanel(); // Object StudentAddPanel
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			searchButton_Course = new JButton("Search");
			searchButton_Course.addActionListener(new SearchButtonListener_Course());
			cancelButton_SearchPanel_Course = new JButton("Cancel");
			cancelButton_SearchPanel_Course.addActionListener(new CancelButton_SearchPanelListener_Course());
			JPanel buttonPanel = new JPanel();
			buttonPanel.add(searchButton_Course);
			buttonPanel.add(cancelButton_SearchPanel_Course);
			searchPanel_Course = new JPanel();
			searchPanel_Course.setLayout(new GridLayout(2, 1));
			searchPanel_Course.add(courseSearchPanel);
			searchPanel_Course.add(buttonPanel);
			add(searchPanel_Course);
			setVisible(true);

			revalidate();
			repaint();
		}
	}

	private class SearchButtonListener_Course implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			closeCourseTable();

			int CNUMValue = 0;

			if (courseSearchPanel.getInputCourseIDText().isEmpty()) {

				JOptionPane.showMessageDialog(null, "Cannot have an empty field. Input data purged.");
				remove(searchPanel_Course);
				revalidate();
				repaint();
			}
			/*
			 * else //old version for linked lists try { if (CNUMValue >
			 * courseRecord.getNumberOfRecords() || CNUMValue <= 0) { try {
			 * JOptionPane.showMessageDialog(null, "Student ID input value of " + SIDValue +
			 * " is higher than the actual stored" + " records value of " +
			 * courseRecord.getNumberOfRecords() + ". Value also cannot be 0 or below."); }
			 * catch (HeadlessException e1) { // TODO Auto-generated catch block
			 * e1.printStackTrace(); } catch (IOException e1) { // TODO Auto-generated catch
			 * block e1.printStackTrace(); } remove(searchPanel_Course); revalidate();
			 * repaint(); }
			 * 
			 */
			else if (!courseSearchPanel.getInputCourseIDText().matches("[0-9]+")) {
				JOptionPane.showMessageDialog(null, "Use only numbers and no spaces. Input data purged.");
				remove(searchPanel_Course);
				revalidate();
				repaint();
			}

			else {
				int numberOfCoursesCountMYSQL = countCourseMYSQL() - 1;
				CNUMValue = Integer.parseInt(courseSearchPanel.getInputCourseIDText());
				if (CNUMValue > numberOfCoursesCountMYSQL || CNUMValue <= 0) {
					try {
						JOptionPane.showMessageDialog(null,
								"Student ID input value of " + CNUMValue + " is higher than the actual stored"
										+ " records value of " + (numberOfCoursesCountMYSQL)
										+ ". Value also cannot be 0 or below.");
						closeCourseTable();
					} catch (HeadlessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					remove(searchPanel_Course);
					revalidate();
					repaint();
				}

				/*
				 * //old version else { Course temp = null; try { if
				 * (courseRecord.getNumberOfRecords() != 0) { int number = -1; while (number < 1
				 * || number > courseRecord.getNumberOfRecords()) { number =
				 * Integer.parseInt(courseSearchPanel.getInputCourseIDText()); } number = number
				 * - 1; courseRecord.moveFilePointer(number); temp =
				 * courseRecord.readCourseFile();
				 * courseRecord.moveFilePointer(courseRecord.getNumberOfRecords()); } } catch
				 * (NumberFormatException e1) { // TODO Auto-generated catch block
				 * e1.printStackTrace(); } catch (IOException e1) { // TODO Auto-generated catch
				 * block e1.printStackTrace(); }
				 */

				else {
					Course temp = null;
					try {
						if (numberOfCoursesCountMYSQL != 0) {
							int number = -1;
							while (number < 1 || number > numberOfCoursesCountMYSQL) {
								number = Integer.parseInt(courseSearchPanel.getInputCourseIDText());
							}
							// number = number - 1;
							int noOffsetNumber = number;
							// courseRecord.moveFilePointer(number);
							// temp = courseRecord.readCourseFile();
							temp = getCourseMYSQL(noOffsetNumber);
							// courseRecord.moveFilePointer(courseRecord.getNumberOfRecords());
						}
					} catch (NumberFormatException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					remove(searchPanel_Course);
					revalidate();
					repaint();
					displayPanel_Course = new JPanel();
					courseDisplayPanel = new CourseDisplayPanel(temp);
					okButton_DisplayPanel_Course = new JButton("OK");
					okButton_DisplayPanel_Course.addActionListener(new OkButtonListener_Course());

					editButton_DisplayPanel_Course = new JButton("Edit");
					editButton_DisplayPanel_Course.addActionListener(new preEditListener_Course());

					displayPanel_Course.setLayout(new GridLayout(2, 1));
					buttonPanel_Course = new JPanel();
					buttonPanel_Course.add(okButton_DisplayPanel_Course);
					buttonPanel_Course.add(editButton_DisplayPanel_Course);
					displayPanel_Course.add(courseDisplayPanel);
					displayPanel_Course.add(buttonPanel_Course);

					add(displayPanel_Course);
					revalidate();
					repaint();
				}
			}

		}
	}

	private class SearchButtonListener_CourseOLD implements ActionListener { // not used
		public void actionPerformed(ActionEvent e) {

			int CNUMValue = 0;
			if (!courseSearchPanel.getInputCourseIDText().isEmpty())
				CNUMValue = Integer.parseInt(courseSearchPanel.getInputCourseIDText());
			if (courseSearchPanel.getInputCourseIDText().isEmpty()) {

				JOptionPane.showMessageDialog(null, "Cannot have an empty field. Input data purged.");
				remove(searchPanel_Course);
				revalidate();
				repaint();
			}
			/*
			 * else //old version for linked lists try { if (CNUMValue >
			 * courseRecord.getNumberOfRecords() || CNUMValue <= 0) { try {
			 * JOptionPane.showMessageDialog(null, "Student ID input value of " + SIDValue +
			 * " is higher than the actual stored" + " records value of " +
			 * courseRecord.getNumberOfRecords() + ". Value also cannot be 0 or below."); }
			 * catch (HeadlessException e1) { // TODO Auto-generated catch block
			 * e1.printStackTrace(); } catch (IOException e1) { // TODO Auto-generated catch
			 * block e1.printStackTrace(); } remove(searchPanel_Course); revalidate();
			 * repaint(); }
			 * 
			 */ else if (CNUMValue > (countCourseMYSQL() - 1) || CNUMValue <= 0) {
				try {
					JOptionPane.showMessageDialog(null,
							"Student ID input value of " + CNUMValue + " is higher than the actual stored"
									+ " records value of " + (countCourseMYSQL() - 1)
									+ ". Value also cannot be 0 or below.");
					closeCourseTable();
				} catch (HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				remove(searchPanel_Course);
				revalidate();
				repaint();
			}

			/*
			 * //old version else { Course temp = null; try { if
			 * (courseRecord.getNumberOfRecords() != 0) { int number = -1; while (number < 1
			 * || number > courseRecord.getNumberOfRecords()) { number =
			 * Integer.parseInt(courseSearchPanel.getInputCourseIDText()); } number = number
			 * - 1; courseRecord.moveFilePointer(number); temp =
			 * courseRecord.readCourseFile();
			 * courseRecord.moveFilePointer(courseRecord.getNumberOfRecords()); } } catch
			 * (NumberFormatException e1) { // TODO Auto-generated catch block
			 * e1.printStackTrace(); } catch (IOException e1) { // TODO Auto-generated catch
			 * block e1.printStackTrace(); }
			 */

			else {
				Course temp = null;
				try {
					if ((countCourseMYSQL() - 1) != 0) {
						int number = -1;
						while (number < 1 || number > (countCourseMYSQL() - 1)) {
							number = Integer.parseInt(courseSearchPanel.getInputCourseIDText());
						}
						// number = number - 1;
						int noOffsetNumber = number;
						// courseRecord.moveFilePointer(number);
						// temp = courseRecord.readCourseFile();
						temp = getCourseMYSQL(noOffsetNumber);
						// courseRecord.moveFilePointer(courseRecord.getNumberOfRecords());
					}
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				remove(searchPanel_Course);
				revalidate();
				repaint();
				displayPanel_Course = new JPanel();
				courseDisplayPanel = new CourseDisplayPanel(temp);
				okButton_DisplayPanel_Course = new JButton("OK");
				okButton_DisplayPanel_Course.addActionListener(new OkButtonListener_Course());

				editButton_DisplayPanel_Course = new JButton("Edit");
				editButton_DisplayPanel_Course.addActionListener(new preEditListener_Course());

				displayPanel_Course.setLayout(new GridLayout(2, 1));
				buttonPanel_Course = new JPanel();
				buttonPanel_Course.add(okButton_DisplayPanel_Course);
				buttonPanel_Course.add(editButton_DisplayPanel_Course);
				displayPanel_Course.add(courseDisplayPanel);
				displayPanel_Course.add(buttonPanel_Course);

				add(displayPanel_Course);
				revalidate();
				repaint();

			}

		}
	}

	private class CancelButton_SearchPanelListener_Course implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// closes course Jtable when you press cancel
			courseTableFrame.dispatchEvent(new WindowEvent(courseTableFrame, WindowEvent.WINDOW_CLOSING));

			remove(searchPanel_Course);
			revalidate();
			repaint();
		}
	}

	// Enrollment Listeners
	private class EnrollmentAddListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			revalidate();
			repaint();

			if ((countStudentMYSQL() - 1) == 0) {

				JOptionPane.showMessageDialog(null, "Please add at least one student first.");
				revalidate();
				repaint();
			}

			else if ((countDepartmentMYSQL() - 1) == 0) {

				JOptionPane.showMessageDialog(null, "Please add at least one department first.");
				revalidate();
				repaint();
			}

			else if ((countInstructorMYSQL() - 1) == 0) {

				JOptionPane.showMessageDialog(null, "Please add at least one instructor first.");
				revalidate();
				repaint();
			} else {
				openStudentTable(loadStudentTable());
				firstPanel_Enrollment = new JPanel();
				try {
					enrollmentFirstPanel = new EnrollmentFirstPanel(); // Object StudentAddPanel
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				firstPanel_Enrollment.setLayout(new GridLayout(2, 1));
				searchButton_FirstPanel_Enrollment = new JButton("Search");
				searchButton_FirstPanel_Enrollment.addActionListener(new SearchButton_FirstPanelListener_Enrollment());
				JPanel buttonPanel = new JPanel();

				buttonPanel.add(searchButton_FirstPanel_Enrollment);

				cancelButton_FirstPanel_Enrollment = new JButton("Cancel");
				cancelButton_FirstPanel_Enrollment.addActionListener(new CancelButton_FirstPanelListener_Enrollment());

				buttonPanel.add(cancelButton_FirstPanel_Enrollment);
				firstPanel_Enrollment.add(enrollmentFirstPanel);
				firstPanel_Enrollment.add(buttonPanel);

				add(firstPanel_Enrollment);
				setVisible(true);
				revalidate();
				repaint();
			}
		}
	}

	private class CancelButton_FirstPanelListener_Enrollment implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// closes student Jtable when you press cancel
			studentTableFrame.dispatchEvent(new WindowEvent(studentTableFrame, WindowEvent.WINDOW_CLOSING));

			remove(firstPanel_Enrollment);
			revalidate();
			repaint();
		}
	}

	private class SearchButton_FirstPanelListener_Enrollment implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			closeStudentTable();

			int SIDValue = 0;

			if (enrollmentFirstPanel.getInputStudentIDText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Cannot have an empty field. Input data purged.");
				remove(firstPanel_Enrollment);
				revalidate();
				repaint();
			}

			else if (!enrollmentFirstPanel.getInputStudentIDText().matches("[0-9]+")) {
				JOptionPane.showMessageDialog(null, "Use only numbers and no spaces. Input data purged.");
				remove(firstPanel_Enrollment);
				revalidate();
				repaint();
			}

			// else if (!enrollmentFirstPanel.getInputStudentIDText().isEmpty())
			// SIDValue = Integer.parseInt(enrollmentFirstPanel.getInputStudentIDText());

			else
				try {

					int studentsCount = countStudentMYSQL() - 1;
					SIDValue = Integer.parseInt(enrollmentFirstPanel.getInputStudentIDText());
					if (SIDValue > studentsCount || SIDValue <= 0) { // error here when SIDValue is 1 over
																		// max
						JOptionPane.showMessageDialog(null,
								"Student ID input value of " + SIDValue + " is higher than the actual stored"
										+ " records value of " + (studentsCount)
										+ ". Value also cannot be 0 or below.");
						remove(firstPanel_Enrollment);
						revalidate();
						repaint();
					}

					else {
						openCourseTable(loadCourseTable());
						Student temp = null;
						try {
							if (studentsCount - 1 != 0) {
								int number = -1;
								while (number < 1 || number > studentsCount) {
									number = Integer.parseInt(enrollmentFirstPanel.getInputStudentIDText());
								}
								// number = number - 1;
								int noOffsetNumber = number;
								// studentRecord.moveFilePointer(number);
								// temp = studentLinkedList.get(number);
								temp = getStudentMYSQL(noOffsetNumber);
								// studentRecord.moveFilePointer(studentRecord.getNumberOfRecords());

							}
						} catch (NumberFormatException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						remove(firstPanel_Enrollment);
						revalidate();
						repaint();
						mainPanel_Enrollment = new JPanel();
						// enrollmentSecondPanel = new EnrollmentSecondPanel(temp, courseLinkedList); //
						// not need with databases
						String[] allCourses = getAllCoursesMYSQL();

						enrollmentSecondPanel = new EnrollmentSecondPanel(temp, sortStringArray(allCourses));

						createButton_SecondPanel_Enrollment = new JButton("Create");
						createButton_SecondPanel_Enrollment
								.addActionListener(new CreateButton_SecondPanelListener_Enrollment());

						cancelButton_SecondPanel_Enrollment = new JButton("Cancel");
						cancelButton_SecondPanel_Enrollment
								.addActionListener(new CancelButton_SecondPanelListener_Enrollment());

						mainPanel_Enrollment.setLayout(new GridLayout(2, 1));
						buttonPanel_SecondPanel_Enrollment = new JPanel();
						buttonPanel_SecondPanel_Enrollment.add(createButton_SecondPanel_Enrollment);
						buttonPanel_SecondPanel_Enrollment.add(cancelButton_SecondPanel_Enrollment);
						mainPanel_Enrollment.add(enrollmentSecondPanel);
						mainPanel_Enrollment.add(buttonPanel_SecondPanel_Enrollment);

						add(mainPanel_Enrollment);
						revalidate();
						repaint();
					}
				} catch (HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		}
	}

	private class CancelButton_SecondPanelListener_Enrollment implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// closes course Jtable when you press cancel
			courseTableFrame.dispatchEvent(new WindowEvent(courseTableFrame, WindowEvent.WINDOW_CLOSING));

			remove(mainPanel_Enrollment);
			revalidate();
			repaint();
		}
	}

	private class CreateButton_SecondPanelListener_Enrollment implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// closes course Jtable when you press cancel
			courseTableFrame.dispatchEvent(new WindowEvent(courseTableFrame, WindowEvent.WINDOW_CLOSING));

			int nonAdjustedIndex = enrollmentLinkedList.size();
			int sizeLinkedList = enrollmentLinkedList.size() + 1; // LINKED ADD - To offset index 0

			int numOfInstructorRows = countEnrollmentMYSQL();

			Enrollment temp = createEnrollment(numOfInstructorRows); // made default grade "N"

			insertEnrollmentMYSQL(temp);
			openEnrollmentTable(loadEnrollmentTable());
			// removed due to database addition
			// Enrollment temp = createEnrollment(sizeLinkedList); // Index starts at 0 so
			// need to offset by 1
			// temp.setGrade("N"); // Sets grade to no value instead of Blank
			try {
				enrollmentRecord.moveFilePointer(nonAdjustedIndex);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				enrollmentRecord.writeEnrollmentFile(temp);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			enrollmentLinkedList.add(temp);
			printEnrollmentLinkedList();
			remove(mainPanel_Enrollment);
			revalidate();
			repaint();

			displayPanel_Enrollment = new JPanel();
			enrollmentDisplayPanel = new EnrollmentDisplayPanel(temp);
			okButton_DisplayPanel_Enrollment = new JButton("OK");
			okButton_DisplayPanel_Enrollment.addActionListener(new OkButton_DisplayPanelListener_Enrollment());

			editButton_DisplayPanel_Enrollment = new JButton("Edit");
			editButton_DisplayPanel_Enrollment.addActionListener(new EditButton_DisplayPanelListener_Enrollment());

			displayPanel_Enrollment.setLayout(new GridLayout(2, 1));
			buttonPanel_DisplayPanel_Enrollment = new JPanel();
			buttonPanel_DisplayPanel_Enrollment.add(okButton_DisplayPanel_Enrollment);
			buttonPanel_DisplayPanel_Enrollment.add(editButton_DisplayPanel_Enrollment);
			displayPanel_Enrollment.add(enrollmentDisplayPanel);
			displayPanel_Enrollment.add(buttonPanel_DisplayPanel_Enrollment);

			add(displayPanel_Enrollment);
			revalidate();
			repaint();

		}
	}

	private class OkButton_DisplayPanelListener_Enrollment implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// closes course Jtable when you press cancel
			enrollmentTableFrame.dispatchEvent(new WindowEvent(enrollmentTableFrame, WindowEvent.WINDOW_CLOSING));

			remove(displayPanel_Enrollment);
			revalidate();
			repaint();
		}
	}

	private class CancelButton_EditPanelListener_Enrollment implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// closes course/student Jtable when you press cancel
			courseStudentTableFrame.dispatchEvent(new WindowEvent(courseStudentTableFrame, WindowEvent.WINDOW_CLOSING));

			remove(editPanel_Enrollment);
			revalidate();
			repaint();
		}
	}

	private class EditButton_DisplayPanelListener_Enrollment implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// closes course Jtable when you press cancel
			enrollmentTableFrame.dispatchEvent(new WindowEvent(enrollmentTableFrame, WindowEvent.WINDOW_CLOSING));

			remove(displayPanel_Enrollment);
			revalidate();
			repaint();

			openCourseStudentTable(loadCourseTable(), loadStudentTable());

			String[] allCourses = getAllCoursesMYSQL();
			String[] allStudents = getAllStudentsMYSQL();

			try {
				enrollmentRecord = new EnrollmentFile("enrollment.dat");
				// enrollmentEditPanel = new
				// EnrollmentEditPanel(enrollmentDisplayPanel.enrollment,
				// studentLinkedList,courseLinkedList); // replaced with database
				enrollmentEditPanel = new EnrollmentEditPanel(enrollmentDisplayPanel.enrollment,
						sortStringArray(allCourses), sortStringArray(allStudents));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			saveButton_EditPanel_Enrollment = new JButton("Save");
			saveButton_EditPanel_Enrollment.addActionListener(new SaveButtonListener_Enrollment());
			cancelButton_EditPanel_Enrollment = new JButton("Cancel");
			cancelButton_EditPanel_Enrollment.addActionListener(new CancelButton_EditPanelListener_Enrollment());
			JPanel buttonPanel = new JPanel();
			buttonPanel.add(saveButton_EditPanel_Enrollment);
			buttonPanel.add(cancelButton_EditPanel_Enrollment);
			editPanel_Enrollment = new JPanel();
			editPanel_Enrollment.setLayout(new GridLayout(2, 1));
			editPanel_Enrollment.add(enrollmentEditPanel);
			editPanel_Enrollment.add(buttonPanel);
			add(editPanel_Enrollment);
			setVisible(true);
			revalidate();
			repaint();
		}
	}

	private class SaveButtonListener_Enrollment implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// closes course/student Jtable when you press cancel
			courseStudentTableFrame.dispatchEvent(new WindowEvent(courseStudentTableFrame, WindowEvent.WINDOW_CLOSING));

			int number = enrollmentEditPanel.enrollment.getEID() - 1;
			int noOffsetNumber = enrollmentEditPanel.enrollment.getEID();
			// Enrollment temp = enrollmentLinkedList.get(number);

			int newCID = enrollmentEditPanel.getCourseIndex();
			int newSID = enrollmentEditPanel.getStudentIndex();
			String newYear = enrollmentEditPanel.getEnrollmentDisplay_YearText();
			String newSemester = enrollmentEditPanel.getEnrollmentDisplay_SemesterText();
			// temp.setcNum(newCID);
			// temp.setStudentID(newSID);
			// temp.setYear(newYear);
			// temp.setSemester(newSemester);
			Enrollment editedEnrollment = new Enrollment(noOffsetNumber, newSID, newCID, newYear, newSemester, "N");
			updateEnrollmentMYSQL(editedEnrollment);
			openEnrollmentTable(loadEnrollmentTable());
			try {
				enrollmentRecord.moveFilePointer(number);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				enrollmentRecord.writeEnrollmentFile(editedEnrollment);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				enrollmentRecord.moveFilePointer(enrollmentRecord.getNumberOfRecords());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			enrollmentLinkedList.overwrite(number, editedEnrollment);
			remove(editPanel_Enrollment);
			revalidate();
			repaint();
			displayPanel_Enrollment = new JPanel();
			enrollmentDisplayPanel = new EnrollmentDisplayPanel(editedEnrollment);
			okButton_DisplayPanel_Enrollment = new JButton("OK");
			okButton_DisplayPanel_Enrollment.addActionListener(new OkButton_DisplayPanelListener_Enrollment());

			editButton_DisplayPanel_Enrollment = new JButton("Edit");
			editButton_DisplayPanel_Enrollment.addActionListener(new EditButton_DisplayPanelListener_Enrollment());

			displayPanel_Enrollment.setLayout(new GridLayout(2, 1));
			buttonPanel_DisplayPanel_Enrollment = new JPanel();
			buttonPanel_DisplayPanel_Enrollment.add(okButton_DisplayPanel_Enrollment);
			buttonPanel_DisplayPanel_Enrollment.add(editButton_DisplayPanel_Enrollment);
			displayPanel_Enrollment.add(enrollmentDisplayPanel);
			displayPanel_Enrollment.add(buttonPanel_DisplayPanel_Enrollment);

			add(displayPanel_Enrollment);
			revalidate();
			repaint();

			printEnrollmentLinkedList();
		}
	}

	private class EnrollmentSearchListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			openEnrollmentTable(loadEnrollmentTable());
			revalidate();
			repaint();

			try {
				enrollmentRecord = new EnrollmentFile("enrollment.dat");
				enrollmentSearchPanel = new EnrollmentSearchPanel();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			searchButton_Enrollment = new JButton("Search");
			searchButton_Enrollment.addActionListener(new SearchButtonListener_Enrollment());
			cancelButton_SearchPanel_Enrollment = new JButton("Cancel");
			cancelButton_SearchPanel_Enrollment.addActionListener(new CancelButton_SearchPanelListener_Enrollment());
			JPanel buttonPanel = new JPanel();
			buttonPanel.add(searchButton_Enrollment);
			buttonPanel.add(cancelButton_SearchPanel_Enrollment);
			searchPanel_Enrollment = new JPanel();
			searchPanel_Enrollment.setLayout(new GridLayout(2, 1));
			searchPanel_Enrollment.add(enrollmentSearchPanel);
			searchPanel_Enrollment.add(buttonPanel);
			add(searchPanel_Enrollment);
			setVisible(true);

			revalidate();
			repaint();
		}
	}

	private class CancelButton_SearchPanelListener_Enrollment implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// closes enrollment Jtable when you press cancel
			enrollmentTableFrame.dispatchEvent(new WindowEvent(enrollmentTableFrame, WindowEvent.WINDOW_CLOSING));

			remove(searchPanel_Enrollment);
			revalidate();
			repaint();
		}
	}

	private class SearchButtonListener_Enrollment implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			closeEnrollmentTable();

			int EIDValue = 0;

			if (enrollmentSearchPanel.getInputEnrollmentIDText().isEmpty()) {

				JOptionPane.showMessageDialog(null, "Cannot have an empty field. Input data purged.");
				remove(searchPanel_Enrollment);
				revalidate();
				repaint();
			}

			else if (!enrollmentSearchPanel.getInputEnrollmentIDText().matches("[0-9]+")) {
				JOptionPane.showMessageDialog(null, "Use only numbers and no spaces. Input data purged.");
				remove(searchPanel_Enrollment);
				revalidate();
				repaint();
			}

			else {
				int enrollmentCount = countEnrollmentMYSQL() - 1;
				EIDValue = Integer.parseInt(enrollmentSearchPanel.getInputEnrollmentIDText());
				if (EIDValue > enrollmentCount || EIDValue <= 0) {
					try {
						JOptionPane.showMessageDialog(null,
								"Student ID input value of " + EIDValue + " is higher than the actual stored"
										+ " records value of " + (enrollmentCount)
										+ ". Value also cannot be 0 or below.");
						closeEnrollmentTable();
					} catch (HeadlessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					remove(searchPanel_Enrollment);
					revalidate();
					repaint();
				} else {
					Enrollment temp = null;
					try {
						if (enrollmentCount != 0) {
							int number = -1;
							while (number < 1 || number > enrollmentCount) {
								number = Integer.parseInt(enrollmentSearchPanel.getInputEnrollmentIDText());
							}
							// number = number - 1;
							int noOffsetNumber = number;
							System.out.println("ENROLLMENT index: " + noOffsetNumber);
							temp = getEnrollmentMYSQL(noOffsetNumber);
							// enrollmentRecord.moveFilePointer(number);
							// temp = enrollmentRecord.readEnrollmentFile();
							// enrollmentRecord.moveFilePointer(enrollmentRecord.getNumberOfRecords());
						}
					} catch (NumberFormatException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					remove(searchPanel_Enrollment);
					revalidate();
					repaint();
					displayPanel_Enrollment = new JPanel();
					enrollmentDisplayPanel = new EnrollmentDisplayPanel(temp);
					okButton_DisplayPanel_Enrollment = new JButton("OK");
					okButton_DisplayPanel_Enrollment.addActionListener(new OkButton_DisplayPanelListener_Enrollment());

					editButton_DisplayPanel_Enrollment = new JButton("Edit");
					editButton_DisplayPanel_Enrollment
							.addActionListener(new EditButton_DisplayPanelListener_Enrollment());

					displayPanel_Enrollment.setLayout(new GridLayout(2, 1));
					buttonPanel_DisplayPanel_Enrollment = new JPanel();
					buttonPanel_DisplayPanel_Enrollment.add(okButton_DisplayPanel_Enrollment);
					buttonPanel_DisplayPanel_Enrollment.add(editButton_DisplayPanel_Enrollment);
					displayPanel_Enrollment.add(enrollmentDisplayPanel);
					displayPanel_Enrollment.add(buttonPanel_DisplayPanel_Enrollment);

					add(displayPanel_Enrollment);
					revalidate();
					repaint();
				}
			}
		}
	}

	private class SearchButtonListener_EnrollmentOLD implements ActionListener { // not used
		public void actionPerformed(ActionEvent e) {
			int EIDValue = 0;
			if (!enrollmentSearchPanel.getInputEnrollmentIDText().isEmpty())
				EIDValue = Integer.parseInt(enrollmentSearchPanel.getInputEnrollmentIDText());
			if (enrollmentSearchPanel.getInputEnrollmentIDText().isEmpty()) {

				JOptionPane.showMessageDialog(null, "Cannot have an empty field. Input data purged.");
				remove(searchPanel_Enrollment);
				revalidate();
				repaint();
			} else if (EIDValue > (countEnrollmentMYSQL() - 1) || EIDValue <= 0) {
				try {
					JOptionPane.showMessageDialog(null,
							"Student ID input value of " + EIDValue + " is higher than the actual stored"
									+ " records value of " + (countEnrollmentMYSQL() - 1)
									+ ". Value also cannot be 0 or below.");
					closeEnrollmentTable();
				} catch (HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				remove(searchPanel_Enrollment);
				revalidate();
				repaint();
			} else {
				Enrollment temp = null;
				try {
					if ((countEnrollmentMYSQL() - 1) != 0) {
						int number = -1;
						while (number < 1 || number > (countEnrollmentMYSQL() - 1)) {
							number = Integer.parseInt(enrollmentSearchPanel.getInputEnrollmentIDText());
						}
						// number = number - 1;
						int noOffsetNumber = number;
						System.out.println("ENROLLMENT index: " + noOffsetNumber);
						temp = getEnrollmentMYSQL(noOffsetNumber);
						// enrollmentRecord.moveFilePointer(number);
						// temp = enrollmentRecord.readEnrollmentFile();
						// enrollmentRecord.moveFilePointer(enrollmentRecord.getNumberOfRecords());
					}
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				remove(searchPanel_Enrollment);
				revalidate();
				repaint();
				displayPanel_Enrollment = new JPanel();
				enrollmentDisplayPanel = new EnrollmentDisplayPanel(temp);
				okButton_DisplayPanel_Enrollment = new JButton("OK");
				okButton_DisplayPanel_Enrollment.addActionListener(new OkButton_DisplayPanelListener_Enrollment());

				editButton_DisplayPanel_Enrollment = new JButton("Edit");
				editButton_DisplayPanel_Enrollment.addActionListener(new EditButton_DisplayPanelListener_Enrollment());

				displayPanel_Enrollment.setLayout(new GridLayout(2, 1));
				buttonPanel_DisplayPanel_Enrollment = new JPanel();
				buttonPanel_DisplayPanel_Enrollment.add(okButton_DisplayPanel_Enrollment);
				buttonPanel_DisplayPanel_Enrollment.add(editButton_DisplayPanel_Enrollment);
				displayPanel_Enrollment.add(enrollmentDisplayPanel);
				displayPanel_Enrollment.add(buttonPanel_DisplayPanel_Enrollment);

				add(displayPanel_Enrollment);
				revalidate();
				repaint();

			}
		}
	}

	// Grade Listeners
	private class gradeAddListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			revalidate();
			repaint();
			openStudentTable(loadStudentTable());

			try {
				enrollmentRecord = new EnrollmentFile("enrollment.dat");

			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			introPanel_Grade = new JPanel();
			try {
				gradeAddPanel = new GradeAddPanel();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			introPanel_Grade.setLayout(new GridLayout(2, 1));
			searchButton_IntroPanel_Grade = new JButton("Search");
			searchButton_IntroPanel_Grade.addActionListener(new SearchButton_IntroPanelListener_Grade());
			JPanel buttonPanel = new JPanel();

			buttonPanel.add(searchButton_IntroPanel_Grade);

			cancelButton_IntroPanel_Grade = new JButton("Cancel");
			cancelButton_IntroPanel_Grade.addActionListener(new CancelButton_IntroPanelListener_Grade());

			buttonPanel.add(cancelButton_IntroPanel_Grade);
			introPanel_Grade.add(gradeAddPanel);
			introPanel_Grade.add(buttonPanel);

			add(introPanel_Grade);
			setVisible(true);
			revalidate();
			repaint();

		}
	}

	private class CancelButton_IntroPanelListener_Grade implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			closeStudentTable();
			remove(introPanel_Grade);
			revalidate();
			repaint();
		}
	}

	private class SearchButton_IntroPanelListener_Grade implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			closeStudentTable();

			String year = gradeAddPanel.getIntro_YearText(); // grabs data early to check if record valid
			String semester = gradeAddPanel.getIntro_SemesterText(); // error checking

			int SIDValue = 0;

			if (gradeAddPanel.getGradeIntro_SIDText().isEmpty()) {

				JOptionPane.showMessageDialog(null, "Cannot have an empty field. Input data purged.");

				// closeStudentTable();
				remove(introPanel_Grade);
				revalidate();
				repaint();
			}

			else if (!gradeAddPanel.getGradeIntro_SIDText().matches("[0-9]+")) {
				JOptionPane.showMessageDialog(null, "Use only numbers and no spaces. Input data purged.");
				// closeStudentTable();
				remove(introPanel_Grade);
				revalidate();
				repaint();
			}

			else {
				int studentCount = countStudentMYSQL() - 1;
				SIDValue = Integer.parseInt(gradeAddPanel.getGradeIntro_SIDText());
				try {
					if (SIDValue > studentCount || SIDValue <= 0) {
						JOptionPane.showMessageDialog(null,
								"Student ID input value of " + SIDValue + " is higher than the actual stored"
										+ " records value of " + (studentCount) + ". Value also cannot be 0 or below.");

						closeStudentTable();
						remove(introPanel_Grade);
						revalidate();
						repaint();

					}

					else if (countFilteredEnrollmentMYSQL(SIDValue, year, semester) == 0) { // Checks if any valid
																							// records found
						JOptionPane.showMessageDialog(null,
								"No records found in enrollment. Try again with different values");

						closeStudentTable();
						remove(introPanel_Grade);
						revalidate();
						repaint();
					} else {
						openCourseTable(loadCourseTable());
						remove(introPanel_Grade);
						revalidate();
						repaint();
						postPanel_Grade = new JPanel();
						// insert most of fill function here to pass collected EID nums.
						try {
							year = gradeAddPanel.getIntro_YearText();
							semester = gradeAddPanel.getIntro_SemesterText();
							postGradePanel = new PostGradePanel(loadFilteredEnrollmentTable(SIDValue, year, semester));
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						postButton_PostPanel_Grade = new JButton("Post");
						postButton_PostPanel_Grade.addActionListener(new PostButton_PostPanelListener_Grade());

						cancelButton_PostPanel_Grade = new JButton("Cancel");
						cancelButton_PostPanel_Grade.addActionListener(new CancelButton_PostPanelListener_Grade());

						postPanel_Grade.setLayout(new GridLayout(2, 1));
						buttonPanel_PostPanel_Grade = new JPanel();
						buttonPanel_PostPanel_Grade.add(postButton_PostPanel_Grade);
						buttonPanel_PostPanel_Grade.add(cancelButton_PostPanel_Grade);
						postPanel_Grade.add(postGradePanel);
						postPanel_Grade.add(buttonPanel_PostPanel_Grade);

						add(postPanel_Grade);
						revalidate();
						repaint();
						/*
						 * removed since new jtable try { //fillTextArea(); } catch
						 * (FileNotFoundException e1) { // TODO Auto-generated catch block
						 * e1.printStackTrace(); }
						 */

					}
				} catch (HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	private class SearchButton_IntroPanelListener_GradeOLD implements ActionListener { // not used
		public void actionPerformed(ActionEvent e) {
			closeStudentTable();

			String year = gradeAddPanel.getIntro_YearText(); // grabs data early to check if record valid
			String semester = gradeAddPanel.getIntro_SemesterText(); // error checking

			int SIDValue = 0;
			if (!gradeAddPanel.getGradeIntro_SIDText().isEmpty())

				SIDValue = Integer.parseInt(gradeAddPanel.getGradeIntro_SIDText());

			if (gradeAddPanel.getGradeIntro_SIDText().isEmpty()) {

				JOptionPane.showMessageDialog(null, "Cannot have an empty field. Input data purged.");

				closeStudentTable();
				remove(introPanel_Grade);
				revalidate();
				repaint();
			}

			else
				try {
					if (SIDValue > (countStudentMYSQL() - 1) || SIDValue <= 0) {
						JOptionPane.showMessageDialog(null,
								"Student ID input value of " + SIDValue + " is higher than the actual stored"
										+ " records value of " + (countStudentMYSQL() - 1)
										+ ". Value also cannot be 0 or below.");

						closeStudentTable();
						remove(introPanel_Grade);
						revalidate();
						repaint();

					}

					else if (countFilteredEnrollmentMYSQL(SIDValue, year, semester) == 0) { // Checks if any valid
																							// records found
						JOptionPane.showMessageDialog(null,
								"No records found in enrollment. Try again with different values");

						closeStudentTable();
						remove(introPanel_Grade);
						revalidate();
						repaint();
					} else {
						openCourseTable(loadCourseTable());
						remove(introPanel_Grade);
						revalidate();
						repaint();
						postPanel_Grade = new JPanel();
						// insert most of fill function here to pass collected EID nums.
						try {
							year = gradeAddPanel.getIntro_YearText();
							semester = gradeAddPanel.getIntro_SemesterText();
							postGradePanel = new PostGradePanel(loadFilteredEnrollmentTable(SIDValue, year, semester));
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						postButton_PostPanel_Grade = new JButton("Post");
						postButton_PostPanel_Grade.addActionListener(new PostButton_PostPanelListener_Grade());

						cancelButton_PostPanel_Grade = new JButton("Cancel");
						cancelButton_PostPanel_Grade.addActionListener(new CancelButton_PostPanelListener_Grade());

						postPanel_Grade.setLayout(new GridLayout(2, 1));
						buttonPanel_PostPanel_Grade = new JPanel();
						buttonPanel_PostPanel_Grade.add(postButton_PostPanel_Grade);
						buttonPanel_PostPanel_Grade.add(cancelButton_PostPanel_Grade);
						postPanel_Grade.add(postGradePanel);
						postPanel_Grade.add(buttonPanel_PostPanel_Grade);

						add(postPanel_Grade);
						revalidate();
						repaint();
						/*
						 * removed since new jtable try { //fillTextArea(); } catch
						 * (FileNotFoundException e1) { // TODO Auto-generated catch block
						 * e1.printStackTrace(); }
						 */

					}
				} catch (HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

		}
	}

	private class CancelButton_PostPanelListener_Grade implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			closeCourseTable();
			remove(postPanel_Grade);
			revalidate();
			repaint();
		}
	}

	// linked list and file reading listener - NOT USED
	private class PostButton_PostPanelListener_GradeOLD implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int EIDValue = 0;
			if (!postGradePanel.getPostPanel_EIDText().isEmpty())
				EIDValue = Integer.parseInt(postGradePanel.getPostPanel_EIDText());
			if (postGradePanel.getPostPanel_EIDText().isEmpty()) {

				JOptionPane.showMessageDialog(null, "Cannot have an empty field. Input data purged.");
				remove(postPanel_Grade);
				revalidate();
				repaint();
			} else
				try {
					// enrollmentRecord = new EnrollmentFile("enrollment.dat");
					if (EIDValue > enrollmentLinkedList.size() || EIDValue <= 0) {
						JOptionPane.showMessageDialog(null,
								"Enrollment ID input value of " + EIDValue + " is higher than the actual stored"
										+ " records value of " + enrollmentLinkedList.size()
										+ ". Value also cannot be 0.");
						remove(postPanel_Grade);
						revalidate();
						repaint();
					} else {
						int number = Integer.parseInt(postGradePanel.getPostPanel_EIDText()) - 1;
						try {
							enrollmentRecord.moveFilePointer(number); // dont need this
						} catch (IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						Enrollment temp = null;
						temp = enrollmentLinkedList.get(number);
						String newGrade = postGradePanel.getPostPanel_NewGradeText();
						temp.setGrade(newGrade);
						try {
							enrollmentRecord.moveFilePointer(number);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {
							enrollmentRecord.writeEnrollmentFile(temp);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {
							enrollmentRecord.moveFilePointer(enrollmentRecord.getNumberOfRecords());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						enrollmentLinkedList.overwrite(number, temp);
						remove(postPanel_Grade);
						revalidate();
						repaint();
						postPanel_Grade = new JPanel();
						try {// this was added

							int SIDValue = Integer.parseInt(gradeAddPanel.getGradeIntro_SIDText());
							String year = gradeAddPanel.getIntro_YearText();
							String semester = gradeAddPanel.getIntro_SemesterText();
							postGradePanel = new PostGradePanel(loadFilteredEnrollmentTable(SIDValue, year, semester));
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						postButton_PostPanel_Grade = new JButton("Post");
						postButton_PostPanel_Grade.addActionListener(new PostButton_PostPanelListener_Grade());

						cancelButton_PostPanel_Grade = new JButton("Cancel");
						cancelButton_PostPanel_Grade.addActionListener(new CancelButton_PostPanelListener_Grade());

						postPanel_Grade.setLayout(new GridLayout(2, 1));
						buttonPanel_PostPanel_Grade = new JPanel();
						buttonPanel_PostPanel_Grade.add(postButton_PostPanel_Grade);
						buttonPanel_PostPanel_Grade.add(cancelButton_PostPanel_Grade);
						postPanel_Grade.add(postGradePanel);
						postPanel_Grade.add(buttonPanel_PostPanel_Grade);

						add(postPanel_Grade);
						revalidate();
						repaint();
						/*
						 * removed since new jtable try { //fillTextArea(); } catch
						 * (FileNotFoundException e1) { // TODO Auto-generated catch block
						 * e1.printStackTrace(); }
						 */
						printEnrollmentLinkedList();
					}
				} catch (HeadlessException | NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		}
	}

	// Database version
	private class PostButton_PostPanelListener_Grade implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int EIDValue = 0;
			if (!postGradePanel.getPostPanel_EIDText().isEmpty())
				try {
					EIDValue = Integer.parseInt(postGradePanel.getPostPanel_EIDText());
				} catch (NumberFormatException e2) {
					JOptionPane.showMessageDialog(null, "Invalid EID input format. Please use digits only.");

					closeCourseTable();
					remove(postPanel_Grade);
					revalidate();
					repaint();
					e2.printStackTrace();
					return;
				}
			if (postGradePanel.getPostPanel_EIDText().isEmpty()) {

				JOptionPane.showMessageDialog(null, "Cannot have an empty field. Input data purged.");
				closeCourseTable();
				remove(postPanel_Grade);
				revalidate();
				repaint();
			} else
				try {
					// enrollmentRecord = new EnrollmentFile("enrollment.dat");
					int enrollmentCount = countEnrollmentMYSQL() - 1;
					if (EIDValue > enrollmentCount || EIDValue <= 0) {
						JOptionPane.showMessageDialog(null,
								"Enrollment ID input value of " + EIDValue + " is higher than the actual stored"
										+ " records value of " + (enrollmentCount) + ". Value also cannot be 0.");
						closeCourseTable();
						remove(postPanel_Grade);
						revalidate();
						repaint();
					} else {
						int number = Integer.parseInt(postGradePanel.getPostPanel_EIDText()) - 1;
						try {
							enrollmentRecord.moveFilePointer(number); // dont need this
						} catch (IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}

						// Database addition:
						Enrollment temp = getEnrollmentMYSQL(EIDValue);
						// Enrollment temp = null;
						// temp = enrollmentLinkedList.get(number);
						String newGrade = postGradePanel.getPostPanel_NewGradeText();
						temp.setGrade(newGrade);
						updateEnrollmentMYSQL(temp);

						try {
							enrollmentRecord.moveFilePointer(number);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {
							enrollmentRecord.writeEnrollmentFile(temp);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {
							enrollmentRecord.moveFilePointer(enrollmentRecord.getNumberOfRecords());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						enrollmentLinkedList.overwrite(number, temp);
						remove(postPanel_Grade);
						revalidate();
						repaint();
						postPanel_Grade = new JPanel();
						try {// this was added

							int SIDValue = Integer.parseInt(gradeAddPanel.getGradeIntro_SIDText());
							String year = gradeAddPanel.getIntro_YearText();
							String semester = gradeAddPanel.getIntro_SemesterText();
							postGradePanel = new PostGradePanel(loadFilteredEnrollmentTable(SIDValue, year, semester));
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						postButton_PostPanel_Grade = new JButton("Post");
						postButton_PostPanel_Grade.addActionListener(new PostButton_PostPanelListener_Grade());

						cancelButton_PostPanel_Grade = new JButton("Cancel");
						cancelButton_PostPanel_Grade.addActionListener(new CancelButton_PostPanelListener_Grade());

						postPanel_Grade.setLayout(new GridLayout(2, 1));
						buttonPanel_PostPanel_Grade = new JPanel();
						buttonPanel_PostPanel_Grade.add(postButton_PostPanel_Grade);
						buttonPanel_PostPanel_Grade.add(cancelButton_PostPanel_Grade);
						postPanel_Grade.add(postGradePanel);
						postPanel_Grade.add(buttonPanel_PostPanel_Grade);

						add(postPanel_Grade);
						revalidate();
						repaint();
						/*
						 * removed since new jtable try { //fillTextArea(); } catch
						 * (FileNotFoundException e1) { // TODO Auto-generated catch block
						 * e1.printStackTrace(); }
						 */
						printEnrollmentLinkedList();
					}
				} catch (HeadlessException | NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		}
	}

	// Report Listeners
	private class reportPrintListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			revalidate();
			repaint();
			openCourseTable(loadCourseTable());

			mainPanel_Report = new JPanel();
			// reportPrintPanel = new ReportPrintPanel(courseLinkedList);
			reportPrintPanel = new ReportPrintPanel(sortStringArray(getAllCoursesMYSQL()), new String[0][0]);
			mainPanel_Report.setLayout(new GridLayout(2, 1));
			printButton_MainPanel_Report = new JButton("Print");
			printButton_MainPanel_Report.addActionListener(new printButton_MainPanelListener_Report());
			JPanel buttonPanel = new JPanel();

			buttonPanel.add(printButton_MainPanel_Report);

			cancelButton_MainPanel_Report = new JButton("Cancel");
			cancelButton_MainPanel_Report.addActionListener(new CancelButton_MainPanelListener_Report());

			buttonPanel.add(cancelButton_MainPanel_Report);
			mainPanel_Report.add(reportPrintPanel);
			mainPanel_Report.add(buttonPanel);

			add(mainPanel_Report);
			setVisible(true);
			revalidate();
			repaint();

		}
	}

	private class CancelButton_MainPanelListener_Report implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (courseTableFrame != null)
				closeCourseTable();
			if (studentTableFrame != null)
				closeStudentTable();
			remove(mainPanel_Report);
			revalidate();
			repaint();
		}
	}

	private class printButton_MainPanelListener_Report implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			closeCourseTable();
			if (studentTableFrame != null)
				closeStudentTable();
			openStudentTable(loadStudentTable());
			int CIDValue = 0;
			// CIDValue =
			// Integer.parseInt(reportPrintPanel.getMainPanel_ReportPrint_CourseText());
			CIDValue = reportPrintPanel.getCourseIndex();

			try {
				// courseRecord = new CourseFile("course.dat");
				if (CIDValue == 0) {
					JOptionPane.showMessageDialog(null, "Please add a course first");
					remove(mainPanel_Report);
					revalidate();
					repaint();
				}

				else {
					String year = reportPrintPanel.getMainPanel_ReportPrint_YearText();
					// int cNum =
					// Integer.parseInt(reportPrintPanel.getMainPanel_ReportPrint_CourseText());
					int cNum = reportPrintPanel.getCourseIndex();

					remove(mainPanel_Report);
					revalidate();
					repaint();
					mainPanel_Report = new JPanel();
					// reportPrintPanel = new ReportPrintPanel(courseLinkedList);
					reportPrintPanel = new ReportPrintPanel(sortStringArray(getAllCoursesMYSQL()),
							loadFilteredEnrollmentReportTable(cNum, year));

					mainPanel_Report.setLayout(new GridLayout(2, 1));
					printButton_MainPanel_Report = new JButton("Print");
					printButton_MainPanel_Report.addActionListener(new printButton_MainPanelListener_Report());
					JPanel buttonPanel = new JPanel();

					buttonPanel.add(printButton_MainPanel_Report);

					cancelButton_MainPanel_Report = new JButton("Cancel");
					cancelButton_MainPanel_Report.addActionListener(new CancelButton_MainPanelListener_Report());

					buttonPanel.add(cancelButton_MainPanel_Report);
					mainPanel_Report.add(reportPrintPanel);
					mainPanel_Report.add(buttonPanel);

					add(mainPanel_Report);
					setVisible(true);
					revalidate();
					repaint();
					/*
					 * try { fillReportTextArea(year, cNum); } catch (FileNotFoundException e1) { //
					 * TODO Auto-generated catch block e1.printStackTrace(); }
					 */
				}
			} catch (HeadlessException | NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	// Department listeners
	private class departmentAddListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			revalidate();
			repaint();
			/*
			 * try { studentRecord = new StudentFile("student.dat");
			 * 
			 * } catch (FileNotFoundException e2) { // TODO Auto-generated catch block
			 * e2.printStackTrace(); }
			 */

			openDepartmentTable(loadDepartmentTable()); // open Jtable for department

			mainPanel_Department = new JPanel();
			try {
				departmentAddPanel = new DepartmentAddPanel(); // Object StudentAddPanel
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			mainPanel_Department.setLayout(new GridLayout(2, 1));
			createButton_AddPanel_Department = new JButton("Create");
			createButton_AddPanel_Department.addActionListener(new CreateButtonListener_AddPanel_Department());
			JPanel buttonPanel = new JPanel();

			buttonPanel.add(createButton_AddPanel_Department);

			cancelButton_AddPanel_Department = new JButton("Cancel");
			cancelButton_AddPanel_Department.addActionListener(new CancelButtonListener_AddPanel_Department());

			buttonPanel.add(cancelButton_AddPanel_Department);
			mainPanel_Department.add(departmentAddPanel);
			mainPanel_Department.add(buttonPanel);

			add(mainPanel_Department);
			setVisible(true);
			revalidate();
			repaint();

		}
	}

	private class CreateButtonListener_AddPanel_Department implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			closeDepartmentTable();

			if (departmentAddPanel.getDepartmentName().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Cannot have an empty field. Input data purged.");
				remove(mainPanel_Department);
				revalidate();
				repaint();
			}

			else if (!departmentAddPanel.getDepartmentName().matches("[a-zA-Z\\s]+")) {
				JOptionPane.showMessageDialog(null, "Use only letters and spaces. Input data purged.");
				remove(mainPanel_Department);
				revalidate();
				repaint();
			}

			else {
				int nonAdjustedIndex = departmentLinkedList.size();
				int sizeLinkedList = departmentLinkedList.size() + 1; // LINKED ADD - To offset index 0

				int numOfDepartmentRows = countDepartmentMYSQL();
				Department temp = createDepartment(numOfDepartmentRows);
				// Department temp = createDepartment(sizeLinkedList); //Index starts at 0 so
				// need to offset by 1

				insertDepartmentMYSQL(temp);
				openDepartmentTable(loadDepartmentTable()); // reloads department JTable with updated row

				try {
					departmentRecord.moveFilePointer(nonAdjustedIndex); // No adjustment here
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					departmentRecord.writeDepartmentFile(temp);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				departmentLinkedList.add(temp); // LINKED ADD
				printDepartmentLinkedList(); // LINKED ADD -- TESTING
				remove(mainPanel_Department);
				revalidate();
				repaint();
				displayPanel_Department = new JPanel();
				departmentDisplayPanel = new DepartmentDisplayPanel(temp);
				okButton_DisplayPanel_Department = new JButton("OK");
				okButton_DisplayPanel_Department
						.addActionListener(new OkButtonListener_DisplayPanelListener_Department());

				editButton_DisplayPanel_Department = new JButton("Edit");
				editButton_DisplayPanel_Department.addActionListener(new EditButton_DisplayPanelListener_Department()); // Need
																														// New

				displayPanel_Department.setLayout(new GridLayout(2, 1));
				buttonPanel_DisplayPanel_Department = new JPanel();
				buttonPanel_DisplayPanel_Department.add(okButton_DisplayPanel_Department);
				buttonPanel_DisplayPanel_Department.add(editButton_DisplayPanel_Department);
				displayPanel_Department.add(departmentDisplayPanel);
				displayPanel_Department.add(buttonPanel_DisplayPanel_Department);

				add(displayPanel_Department);
				revalidate();
				repaint();
			}
		}
	}

	private class CancelButtonListener_AddPanel_Department implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// closes department Jtable when you press cancel
			departmentTableFrame.dispatchEvent(new WindowEvent(departmentTableFrame, WindowEvent.WINDOW_CLOSING));
			remove(mainPanel_Department);
			revalidate();
			repaint();
		}
	}

	private class OkButtonListener_DisplayPanelListener_Department implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// departmentTableFrame.dispatchEvent(new WindowEvent(departmentTableFrame,
			// WindowEvent.WINDOW_CLOSING));
			departmentTableFrame.dispose();
			displayPanel_Department.setVisible(false);
		}
	}

	private class EditButton_DisplayPanelListener_Department implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			remove(displayPanel_Department);
			revalidate();
			repaint();

			try {
				// studentRecord = new StudentFile("student.dat"); //ERASE THIS
				departmentEditPanel = new DepartmentEditPanel(departmentDisplayPanel.department); // pass student object
																									// here and
				// make a constructor
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			saveButton_EditPanel_Department = new JButton("Save");
			saveButton_EditPanel_Department.addActionListener(new SaveButtonListener_Department());
			cancelButton_EditPanel_Department = new JButton("Cancel");
			cancelButton_EditPanel_Department.addActionListener(new CancelButton_EditPanelListener_Department());
			JPanel buttonPanel = new JPanel();
			buttonPanel.add(saveButton_EditPanel_Department);
			buttonPanel.add(cancelButton_EditPanel_Department);
			editPanel_Department = new JPanel();
			editPanel_Department.setLayout(new GridLayout(2, 1));
			editPanel_Department.add(departmentEditPanel);
			editPanel_Department.add(buttonPanel);
			add(editPanel_Department);
			setVisible(true);
			revalidate();
			repaint();
		}
	}

	private class CancelButton_EditPanelListener_Department implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// closes department Jtable when you press cancel
			departmentTableFrame.dispatchEvent(new WindowEvent(departmentTableFrame, WindowEvent.WINDOW_CLOSING));

			remove(editPanel_Department);
			revalidate();
			repaint();
		}
	}

	private class departmentSearchListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			revalidate();
			repaint();

			openDepartmentTable(loadDepartmentTable()); // reloads department JTable with updated row

			try {
				// departmentRecord = new DepartmentFile("department.dat");
				departmentSearchPanel = new DepartmentSearchPanel();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			searchButton_Department = new JButton("Search");
			searchButton_Department.addActionListener(new SearchButtonListener_Department());
			cancelButton_SearchPanel_Department = new JButton("Cancel");
			cancelButton_SearchPanel_Department.addActionListener(new CancelButton_SearchPanelListener_Department()); // NEW
																														// LISTENER
																														// HERE
			JPanel buttonPanel = new JPanel();
			buttonPanel.add(searchButton_Department);
			buttonPanel.add(cancelButton_SearchPanel_Department);
			searchPanel_Department = new JPanel();
			searchPanel_Department.setLayout(new GridLayout(2, 1));
			searchPanel_Department.add(departmentSearchPanel);
			searchPanel_Department.add(buttonPanel);
			add(searchPanel_Department);
			setVisible(true);

			revalidate();
			repaint();

		}
	}

	private class SearchButtonListener_Department implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			closeDepartmentTable();

			int DIDValue = 0;

			if (departmentSearchPanel.getInputDepartmentDIDText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Cannot have an empty field. Input data purged.");
				remove(searchPanel_Department);
				revalidate();
				repaint();
			} else if (!departmentSearchPanel.getInputDepartmentDIDText().matches("[0-9]+")) {
				JOptionPane.showMessageDialog(null, "Use only numbers and no spaces. Input data purged.");
				remove(searchPanel_Department);
				revalidate();
				repaint();
			}

			else {

				int numberOfDepartmentsCountMYSQL = countDepartmentMYSQL() - 1;

				DIDValue = Integer.parseInt(departmentSearchPanel.getInputDepartmentDIDText());
				if (DIDValue > numberOfDepartmentsCountMYSQL || DIDValue <= 0) {

					try {
						JOptionPane.showMessageDialog(null,
								"Department DID input value of " + DIDValue + " is higher than the actual stored"
										+ " records value of " + (numberOfDepartmentsCountMYSQL)
										+ ". Value also cannot be 0 or below.");
						closeDepartmentTable();
					} catch (HeadlessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					remove(searchPanel_Department);
					revalidate();
					repaint();
				}

				else {
					Department temp = null;
					try {
						if (numberOfDepartmentsCountMYSQL != 0) {
							int number = -1;
							while (number < 1 || number > (numberOfDepartmentsCountMYSQL)) {
								number = Integer.parseInt(departmentSearchPanel.getInputDepartmentDIDText());
							}
							int noOffsetNumber = number;
							temp = getDepartmentMYSQL(noOffsetNumber);

						}
					} catch (NumberFormatException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					// setVisible(false);
					remove(searchPanel_Department);
					revalidate();
					repaint();
					displayPanel_Department = new JPanel();
					departmentDisplayPanel = new DepartmentDisplayPanel(temp);
					okButton_DisplayPanel_Department = new JButton("OK");
					okButton_DisplayPanel_Department
							.addActionListener(new OkButtonListener_DisplayPanelListener_Department()); // NEW LISTENER

					editButton_DisplayPanel_Department = new JButton("Edit");
					editButton_DisplayPanel_Department
							.addActionListener(new EditButton_DisplayPanelListener_Department()); // NEW
																									// LISTENER

					displayPanel_Department.setLayout(new GridLayout(2, 1));
					buttonPanel_SearchPanel_Department = new JPanel();
					buttonPanel_SearchPanel_Department.add(okButton_DisplayPanel_Department);
					buttonPanel_SearchPanel_Department.add(editButton_DisplayPanel_Department);
					displayPanel_Department.add(departmentDisplayPanel);
					displayPanel_Department.add(buttonPanel_SearchPanel_Department);

					add(displayPanel_Department);
					revalidate();
					repaint();
				}
			}

		}
	}

	private class SearchButtonListener_DepartmentOLD implements ActionListener { // not used right now
		public void actionPerformed(ActionEvent e) {

			int DIDValue = 0;
			if (!departmentSearchPanel.getInputDepartmentDIDText().isEmpty())
				DIDValue = Integer.parseInt(departmentSearchPanel.getInputDepartmentDIDText());
			if (departmentSearchPanel.getInputDepartmentDIDText().isEmpty()) {

				JOptionPane.showMessageDialog(null, "Cannot have an empty field. Input data purged.");
				remove(searchPanel_Department);
				revalidate();
				repaint();
			}

			else if (DIDValue > (countDepartmentMYSQL() - 1) || DIDValue <= 0) {
				try {
					JOptionPane.showMessageDialog(null,
							"Department DID input value of " + DIDValue + " is higher than the actual stored"
									+ " records value of " + (countDepartmentMYSQL() - 1)
									+ ". Value also cannot be 0 or below.");
					closeDepartmentTable();
				} catch (HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				remove(searchPanel_Department);
				revalidate();
				repaint();
			}

			else {
				Department temp = null;
				try {
					if ((countDepartmentMYSQL() - 1) != 0) {
						int number = -1;
						while (number < 1 || number > (countDepartmentMYSQL() - 1)) {
							number = Integer.parseInt(departmentSearchPanel.getInputDepartmentDIDText());
						}
						int noOffsetNumber = number;
						temp = getDepartmentMYSQL(noOffsetNumber);

					}
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// setVisible(false);
				remove(searchPanel_Department);
				revalidate();
				repaint();
				displayPanel_Department = new JPanel();
				departmentDisplayPanel = new DepartmentDisplayPanel(temp);
				okButton_DisplayPanel_Department = new JButton("OK");
				okButton_DisplayPanel_Department
						.addActionListener(new OkButtonListener_DisplayPanelListener_Department()); // NEW LISTENER

				editButton_DisplayPanel_Department = new JButton("Edit");
				editButton_DisplayPanel_Department.addActionListener(new EditButton_DisplayPanelListener_Department()); // NEW
																														// LISTENER

				displayPanel_Department.setLayout(new GridLayout(2, 1));
				buttonPanel_SearchPanel_Department = new JPanel();
				buttonPanel_SearchPanel_Department.add(okButton_DisplayPanel_Department);
				buttonPanel_SearchPanel_Department.add(editButton_DisplayPanel_Department);
				displayPanel_Department.add(departmentDisplayPanel);
				displayPanel_Department.add(buttonPanel_SearchPanel_Department);

				add(displayPanel_Department);
				revalidate();
				repaint();

			}

		}
	}

	private class CancelButton_SearchPanelListener_Department implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// closes department Jtable when you press cancel
			departmentTableFrame.dispatchEvent(new WindowEvent(departmentTableFrame, WindowEvent.WINDOW_CLOSING));

			remove(searchPanel_Department);
			revalidate();
			repaint();
		}
	}

	private class SaveButtonListener_Department implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// closes department JTable
			departmentTableFrame.dispatchEvent(new WindowEvent(departmentTableFrame, WindowEvent.WINDOW_CLOSING));

			if (departmentEditPanel.getDepartmentEdit_nameText2().isEmpty()) {

				JOptionPane.showMessageDialog(null, "Cannot have an empty field. Input data purged.");
				remove(editPanel_Department);
				revalidate();
				repaint();

			}

			else if (!departmentEditPanel.getDepartmentEdit_nameText2().matches("[a-zA-Z\\s]+")) {
				JOptionPane.showMessageDialog(null, "Use only letters and spaces. Input data purged.");
				remove(editPanel_Department);
				revalidate();
				repaint();
			}

			else {

				int number = departmentEditPanel.department.getDID() - 1;
				int noOffsetNumber = departmentEditPanel.department.getDID(); // Added for MYSQL since no offset is
																				// needed anymore

				// Department editedDepartment = departmentLinkedList.get(number); // LINKED ADD

				String departmentName = departmentEditPanel.getDepartmentEdit_nameText2();

				// editedDepartment.setDepartmentName(departmentName);

				// System.out.println("Number: " + number);
				// Student temp = null;

				// Student editedStudent = studentLinkedList.get(number); // LINKED ADD

				Department editedDepartment = new Department(noOffsetNumber, departmentName);

				updateDepartmentMYSQL(editedDepartment);
				openDepartmentTable(loadDepartmentTable()); // reloads department JTable with updated row

				try {
					departmentRecord.moveFilePointer(number);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					departmentRecord.writeDepartmentFile(editedDepartment);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					departmentRecord.moveFilePointer(departmentRecord.getNumberOfRecords());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				departmentLinkedList.overwrite(number, editedDepartment);
				remove(editPanel_Department);
				revalidate();
				repaint();
				displayPanel_Department = new JPanel();
				departmentDisplayPanel = new DepartmentDisplayPanel(editedDepartment);
				okButton_DisplayPanel_Department = new JButton("OK");
				okButton_DisplayPanel_Department
						.addActionListener(new OkButtonListener_DisplayPanelListener_Department()); // needs new

				editButton_DisplayPanel_Department = new JButton("Edit");
				editButton_DisplayPanel_Department.addActionListener(new EditButton_DisplayPanelListener_Department());

				displayPanel_Department.setLayout(new GridLayout(2, 1));
				buttonPanel = new JPanel();
				buttonPanel.add(okButton_DisplayPanel_Department);
				buttonPanel.add(editButton_DisplayPanel_Department);
				displayPanel_Department.add(departmentDisplayPanel);
				displayPanel_Department.add(buttonPanel);

				add(displayPanel_Department);
				revalidate();
				repaint();

				printDepartmentLinkedList();
			}
		}
	}

	// Instructor listeners

	private class instructorAddListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			revalidate();
			repaint();

			if ((countDepartmentMYSQL() - 1) == 0) {

				JOptionPane.showMessageDialog(null, "Please add at least one department first.");
				revalidate();
				repaint();
			}

			else {
				openInstructorTable(loadInstructorTable()); // open Jtable for instructor
				mainPanel_Instructor = new JPanel();
				try {

					// instructorAddPanel = new InstructorAddPanel(departmentLinkedList); //linked
					// list
					instructorAddPanel = new InstructorAddPanel(getAllDepartmentsMYSQL()); // Database
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				mainPanel_Instructor.setLayout(new GridLayout(2, 1));
				createButton_AddPanel_Instructor = new JButton("Create");
				createButton_AddPanel_Instructor.addActionListener(new CreateButtonListener_AddPanel_Instructor()); // new
																													// listener
				JPanel buttonPanel = new JPanel();

				buttonPanel.add(createButton_AddPanel_Instructor);

				cancelButton_AddPanel_Instructor = new JButton("Cancel");
				cancelButton_AddPanel_Instructor.addActionListener(new CancelButtonListener_AddPanel_Instructor());

				buttonPanel.add(cancelButton_AddPanel_Instructor);
				mainPanel_Instructor.add(instructorAddPanel);
				mainPanel_Instructor.add(buttonPanel);

				add(mainPanel_Instructor);
				setVisible(true);
				revalidate();
				repaint();
			}

		}
	}

	private class CancelButtonListener_AddPanel_Instructor implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			instructorTableFrame.dispatchEvent(new WindowEvent(instructorTableFrame, WindowEvent.WINDOW_CLOSING));

			remove(mainPanel_Instructor);
			revalidate();
			repaint();
		}
	}

	private class CreateButtonListener_AddPanel_Instructor implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// closes department Jtable when you press cancel
			instructorTableFrame.dispatchEvent(new WindowEvent(instructorTableFrame, WindowEvent.WINDOW_CLOSING));

			if (instructorAddPanel.getInstructorName().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Cannot have an empty field. Input data purged.");
				remove(mainPanel_Instructor);
				revalidate();
				repaint();
			}

			else if (!instructorAddPanel.getInstructorName().matches("[a-zA-Z\\s]+")) {
				JOptionPane.showMessageDialog(null, "Use only letters and spaces. Input data purged.");
				remove(mainPanel_Instructor);
				revalidate();
				repaint();
			}

			else {
				int nonAdjustedIndex = instructorLinkedList.size();
				int sizeLinkedList = instructorLinkedList.size() + 1; // LINKED ADD - To offset index 0
				int numOfInstructorRows = countInstructorMYSQL();
				Instructor temp = createInstructor(numOfInstructorRows); // Index starts at 0 so need to offset by 1

				insertInstructorMYSQL(temp);
				openInstructorTable(loadInstructorTable()); // open Jtable for instructor

				try {
					instructorRecord.moveFilePointer(nonAdjustedIndex); // No adjustment here
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					instructorRecord.writeInstructorFile(temp);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				instructorLinkedList.add(temp); // LINKED ADD
				printInstructorLinkedList(); // LINKED ADD -- TESTING
				remove(mainPanel_Instructor);
				revalidate();
				repaint();
				displayPanel_Instructor = new JPanel();
				instructorDisplayPanel = new InstructorDisplayPanel(temp);
				okButton_DisplayPanel_Instructor = new JButton("OK");
				okButton_DisplayPanel_Instructor
						.addActionListener(new OkButtonListener_DisplayPanelListener_Instructor()); // NEED NEW

				editButton_DisplayPanel_Instructor = new JButton("Edit");
				editButton_DisplayPanel_Instructor.addActionListener(new EditButton_DisplayPanelListener_Instructor()); // Need
																														// New

				displayPanel_Instructor.setLayout(new GridLayout(2, 1));
				buttonPanel_DisplayPanel_Instructor = new JPanel();
				buttonPanel_DisplayPanel_Instructor.add(okButton_DisplayPanel_Instructor);
				buttonPanel_DisplayPanel_Instructor.add(editButton_DisplayPanel_Instructor);
				displayPanel_Instructor.add(instructorDisplayPanel);
				displayPanel_Instructor.add(buttonPanel_DisplayPanel_Instructor);

				add(displayPanel_Instructor);
				revalidate();
				repaint();
			}
		}
	}

	private class OkButtonListener_DisplayPanelListener_Instructor implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			instructorTableFrame.dispatchEvent(new WindowEvent(instructorTableFrame, WindowEvent.WINDOW_CLOSING));

			displayPanel_Instructor.setVisible(false);
		}
	}

	private class EditButton_DisplayPanelListener_Instructor implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			remove(displayPanel_Instructor);
			revalidate();
			repaint();

			try {
				// instructorEditPanel = new
				// InstructorEditPanel(instructorDisplayPanel.instructor,departmentLinkedList);

				// section created to extract department name from display panel DID and pass it
				// to edit panel.
				// was getting incorrect result in edit panel department name Jtextfield
				int DIDFromInstructorPanel = instructorDisplayPanel.instructor.getInstructorDID();
				Department pulledFromInstructorPanel = getDepartmentMYSQL(DIDFromInstructorPanel);
				String departmentName = pulledFromInstructorPanel.getDepartmentName();

				instructorEditPanel = new InstructorEditPanel(instructorDisplayPanel.instructor, departmentName,
						getAllDepartmentsMYSQL()); // make a constructor
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			saveButton_EditPanel_Instructor = new JButton("Save");
			saveButton_EditPanel_Instructor.addActionListener(new SaveButtonListener_Instructor()); // EDIT
			cancelButton_EditPanel_Instructor = new JButton("Cancel");
			cancelButton_EditPanel_Instructor.addActionListener(new CancelButton_EditPanelListener_Instructor());
			JPanel buttonPanel = new JPanel();
			buttonPanel.add(saveButton_EditPanel_Instructor);
			buttonPanel.add(cancelButton_EditPanel_Instructor);
			editPanel_Instructor = new JPanel();
			editPanel_Instructor.setLayout(new GridLayout(2, 1));
			editPanel_Instructor.add(instructorEditPanel);
			editPanel_Instructor.add(buttonPanel);
			add(editPanel_Instructor);
			setVisible(true);
			revalidate();
			repaint();
		}
	}

	private class CancelButton_EditPanelListener_Instructor implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			instructorTableFrame.dispatchEvent(new WindowEvent(instructorTableFrame, WindowEvent.WINDOW_CLOSING));

			remove(editPanel_Instructor);
			revalidate();
			repaint();
		}
	}

	private class InstructorSearchListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			revalidate();
			repaint();
			openInstructorTable(loadInstructorTable()); // open Jtable for instructor

			try {
				// departmentRecord = new DepartmentFile("department.dat");
				instructorSearchPanel = new InstructorSearchPanel();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			searchButton_Instructor = new JButton("Search");
			searchButton_Instructor.addActionListener(new SearchButtonListener_Instructor());
			cancelButton_SearchPanel_Instructor = new JButton("Cancel");
			cancelButton_SearchPanel_Instructor.addActionListener(new CancelButton_SearchPanelListener_Instructor()); // NEW
																														// LISTENER
																														// HERE
			JPanel buttonPanel = new JPanel();
			buttonPanel.add(searchButton_Instructor);
			buttonPanel.add(cancelButton_SearchPanel_Instructor);
			searchPanel_Instructor = new JPanel();
			searchPanel_Instructor.setLayout(new GridLayout(2, 1));
			searchPanel_Instructor.add(instructorSearchPanel);
			searchPanel_Instructor.add(buttonPanel);
			add(searchPanel_Instructor);
			setVisible(true);

			revalidate();
			repaint();

		}
	}

	private class SaveButtonListener_Instructor implements ActionListener { // Check offsets for DID
		public void actionPerformed(ActionEvent e) {
			instructorTableFrame.dispatchEvent(new WindowEvent(instructorTableFrame, WindowEvent.WINDOW_CLOSING));

			if (instructorEditPanel.getInstructorEdit_nameText2().isEmpty()) {

				JOptionPane.showMessageDialog(null, "Cannot have an empty field. Input data purged.");
				remove(editPanel_Instructor);
				revalidate();
				repaint();

			}

			else if (!instructorEditPanel.getInstructorEdit_nameText2().matches("[a-zA-Z\\s]+")) {
				JOptionPane.showMessageDialog(null, "Use only letters and spaces. Input data purged.");
				remove(editPanel_Instructor);
				revalidate();
				repaint();
			}

			else {

				int indexIID = instructorEditPanel.instructor.getIID() - 1;
				int noOffsetNumber = instructorEditPanel.instructor.getIID(); // Added for MYSQL since no offset is
																				// needed anymore, also used for
																				// linkedlist

				// Instructor editedInstructor = instructorLinkedList.get(indexIID); // LINKED
				// ADD

				String instructorName = instructorEditPanel.getInstructorEdit_nameText2();

				String departmentStringValue = instructorEditPanel.getDepartment();
				int departmentIndex = getDepartmentIndexMYSQL(departmentStringValue);
				int instructorDID = departmentIndex;
				System.out.println("DID in create Instructor: " + departmentIndex);

				int indexDID = departmentIndex;

				Instructor editedInstructor = new Instructor(noOffsetNumber, instructorName, indexDID);

				updateInstructorMYSQL(editedInstructor);
				openInstructorTable(loadInstructorTable()); // open Jtable for instructor

				try {
					instructorRecord.moveFilePointer(indexIID);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					instructorRecord.writeInstructorFile(editedInstructor);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					instructorRecord.moveFilePointer(instructorRecord.getNumberOfRecords());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				instructorLinkedList.overwrite(indexIID, editedInstructor);
				remove(editPanel_Instructor);
				revalidate();
				repaint();
				displayPanel_Instructor = new JPanel();
				instructorDisplayPanel = new InstructorDisplayPanel(editedInstructor);
				okButton_DisplayPanel_Instructor = new JButton("OK");
				okButton_DisplayPanel_Instructor
						.addActionListener(new OkButtonListener_DisplayPanelListener_Instructor()); // needs new

				editButton_DisplayPanel_Instructor = new JButton("Edit");
				editButton_DisplayPanel_Instructor.addActionListener(new EditButton_DisplayPanelListener_Instructor()); // needs
																														// new

				displayPanel_Instructor.setLayout(new GridLayout(2, 1));
				buttonPanel = new JPanel();
				buttonPanel.add(okButton_DisplayPanel_Instructor);
				buttonPanel.add(editButton_DisplayPanel_Instructor);
				displayPanel_Instructor.add(instructorDisplayPanel);
				displayPanel_Instructor.add(buttonPanel);

				add(displayPanel_Instructor);
				revalidate();
				repaint();

				printInstructorLinkedList();
			}
		}
	}

	private class SearchButtonListener_Instructor implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			closeInstructorTable();

			int IIDValue = 0;

			if (instructorSearchPanel.getInputInstructorIIDText().isEmpty()) {

				JOptionPane.showMessageDialog(null, "Cannot have an empty field. Input data purged.");
				remove(searchPanel_Instructor);
				revalidate();
				repaint();
			}
			/*
			 * else if (IIDValue > instructorLinkedList.size() || IIDValue <= 0 ) { try {
			 * JOptionPane.showMessageDialog(null, "Instructor IID input value of " +
			 * IIDValue + " is higher than the actual stored" + " records value of " +
			 * instructorLinkedList.size() + ". Value also cannot be 0 or below."); } catch
			 * (HeadlessException e1) { // TODO Auto-generated catch block
			 * e1.printStackTrace(); } remove(searchPanel_Instructor); revalidate();
			 * repaint();
			 * 
			 * }
			 */
			else if (!instructorSearchPanel.getInputInstructorIIDText().matches("[0-9]+")) {
				JOptionPane.showMessageDialog(null, "Use only numbers and no spaces. Input data purged.");
				remove(searchPanel_Instructor);
				revalidate();
				repaint();
			}

			else {
				int numberOfInstructorsCountMYSQL = countInstructorMYSQL() - 1;
				IIDValue = Integer.parseInt(instructorSearchPanel.getInputInstructorIIDText());
				if (IIDValue > numberOfInstructorsCountMYSQL || IIDValue <= 0) {
					try {
						JOptionPane.showMessageDialog(null,
								"Instructor IID input value of " + IIDValue + " is higher than the actual stored"
										+ " records value of " + (numberOfInstructorsCountMYSQL)
										+ ". Value also cannot be 0 or below.");
						closeInstructorTable();
					} catch (HeadlessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					remove(searchPanel_Instructor);
					revalidate();
					repaint();

				}
				/*
				 * else { Instructor temp = null; try { if (instructorLinkedList.size() != 0) {
				 * int number = -1; while (number < 1 || number > instructorLinkedList.size()) {
				 * number = Integer.parseInt(instructorSearchPanel.getInputInstructorIIDText());
				 * } number = number - 1; //departmentRecord.moveFilePointer(number); //temp =
				 * departmentRecord.readDepartmentFile(); temp =
				 * instructorLinkedList.get(number); // temp.printStudent();
				 * //departmentRecord.moveFilePointer(departmentRecord.getNumberOfRecords());
				 * 
				 * } } catch (NumberFormatException e1) { // TODO Auto-generated catch block
				 * e1.printStackTrace(); }
				 */

				else {
					Instructor temp = null;
					try {
						if (numberOfInstructorsCountMYSQL != 0) {
							int number = -1;
							while (number < 1 || number > numberOfInstructorsCountMYSQL) {
								number = Integer.parseInt(instructorSearchPanel.getInputInstructorIIDText());
							}
							// number = number - 1;
							int noOffsetNumber = number;
							// departmentRecord.moveFilePointer(number);
							// temp = departmentRecord.readDepartmentFile();
							// temp = instructorLinkedList.get(number);
							temp = getInstructorMYSQL(noOffsetNumber);
							// temp.printStudent();
							// departmentRecord.moveFilePointer(departmentRecord.getNumberOfRecords());

						}
					} catch (NumberFormatException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// setVisible(false);
					remove(searchPanel_Instructor);
					revalidate();
					repaint();
					displayPanel_Instructor = new JPanel();
					instructorDisplayPanel = new InstructorDisplayPanel(temp);
					okButton_DisplayPanel_Instructor = new JButton("OK");
					okButton_DisplayPanel_Instructor
							.addActionListener(new OkButtonListener_DisplayPanelListener_Instructor()); // NEW LISTENER

					editButton_DisplayPanel_Instructor = new JButton("Edit");
					editButton_DisplayPanel_Instructor
							.addActionListener(new EditButton_DisplayPanelListener_Instructor()); // NEW
																									// LISTENER

					displayPanel_Instructor.setLayout(new GridLayout(2, 1));
					buttonPanel_SearchPanel_Instructor = new JPanel();
					buttonPanel_SearchPanel_Instructor.add(okButton_DisplayPanel_Instructor);
					buttonPanel_SearchPanel_Instructor.add(editButton_DisplayPanel_Instructor);
					displayPanel_Instructor.add(instructorDisplayPanel);
					displayPanel_Instructor.add(buttonPanel_SearchPanel_Instructor);

					add(displayPanel_Instructor);
					revalidate();
					repaint();
				}
			}

		}
	}

	private class SearchButtonListener_InstructorOLD implements ActionListener {// not used made changes
		public void actionPerformed(ActionEvent e) {

			int IIDValue = 0;
			if (!instructorSearchPanel.getInputInstructorIIDText().isEmpty())
				IIDValue = Integer.parseInt(instructorSearchPanel.getInputInstructorIIDText());
			if (instructorSearchPanel.getInputInstructorIIDText().isEmpty()) {

				JOptionPane.showMessageDialog(null, "Cannot have an empty field. Input data purged.");
				remove(searchPanel_Instructor);
				revalidate();
				repaint();
			}
			/*
			 * else if (IIDValue > instructorLinkedList.size() || IIDValue <= 0 ) { try {
			 * JOptionPane.showMessageDialog(null, "Instructor IID input value of " +
			 * IIDValue + " is higher than the actual stored" + " records value of " +
			 * instructorLinkedList.size() + ". Value also cannot be 0 or below."); } catch
			 * (HeadlessException e1) { // TODO Auto-generated catch block
			 * e1.printStackTrace(); } remove(searchPanel_Instructor); revalidate();
			 * repaint();
			 * 
			 * }
			 */
			else if (IIDValue > (countInstructorMYSQL() - 1) || IIDValue <= 0) {
				try {
					JOptionPane.showMessageDialog(null,
							"Instructor IID input value of " + IIDValue + " is higher than the actual stored"
									+ " records value of " + (countInstructorMYSQL() - 1)
									+ ". Value also cannot be 0 or below.");
					closeInstructorTable();
				} catch (HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				remove(searchPanel_Instructor);
				revalidate();
				repaint();

			}
			/*
			 * else { Instructor temp = null; try { if (instructorLinkedList.size() != 0) {
			 * int number = -1; while (number < 1 || number > instructorLinkedList.size()) {
			 * number = Integer.parseInt(instructorSearchPanel.getInputInstructorIIDText());
			 * } number = number - 1; //departmentRecord.moveFilePointer(number); //temp =
			 * departmentRecord.readDepartmentFile(); temp =
			 * instructorLinkedList.get(number); // temp.printStudent();
			 * //departmentRecord.moveFilePointer(departmentRecord.getNumberOfRecords());
			 * 
			 * } } catch (NumberFormatException e1) { // TODO Auto-generated catch block
			 * e1.printStackTrace(); }
			 */

			else {
				Instructor temp = null;
				try {
					if ((countInstructorMYSQL() - 1) != 0) {
						int number = -1;
						while (number < 1 || number > (countInstructorMYSQL() - 1)) {
							number = Integer.parseInt(instructorSearchPanel.getInputInstructorIIDText());
						}
						// number = number - 1;
						int noOffsetNumber = number;
						// departmentRecord.moveFilePointer(number);
						// temp = departmentRecord.readDepartmentFile();
						// temp = instructorLinkedList.get(number);
						temp = getInstructorMYSQL(noOffsetNumber);
						// temp.printStudent();
						// departmentRecord.moveFilePointer(departmentRecord.getNumberOfRecords());

					}
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// setVisible(false);
				remove(searchPanel_Instructor);
				revalidate();
				repaint();
				displayPanel_Instructor = new JPanel();
				instructorDisplayPanel = new InstructorDisplayPanel(temp);
				okButton_DisplayPanel_Instructor = new JButton("OK");
				okButton_DisplayPanel_Instructor
						.addActionListener(new OkButtonListener_DisplayPanelListener_Instructor()); // NEW LISTENER

				editButton_DisplayPanel_Instructor = new JButton("Edit");
				editButton_DisplayPanel_Instructor.addActionListener(new EditButton_DisplayPanelListener_Instructor()); // NEW
																														// LISTENER

				displayPanel_Instructor.setLayout(new GridLayout(2, 1));
				buttonPanel_SearchPanel_Instructor = new JPanel();
				buttonPanel_SearchPanel_Instructor.add(okButton_DisplayPanel_Instructor);
				buttonPanel_SearchPanel_Instructor.add(editButton_DisplayPanel_Instructor);
				displayPanel_Instructor.add(instructorDisplayPanel);
				displayPanel_Instructor.add(buttonPanel_SearchPanel_Instructor);

				add(displayPanel_Instructor);
				revalidate();
				repaint();

			}

		}
	}

	private class CancelButton_SearchPanelListener_Instructor implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// closes department Jtable when you press cancel
			instructorTableFrame.dispatchEvent(new WindowEvent(instructorTableFrame, WindowEvent.WINDOW_CLOSING));

			remove(searchPanel_Instructor);
			revalidate();
			repaint();
		}
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		StudentManagementSystemAZURE pierce = new StudentManagementSystemAZURE();

	}

}
