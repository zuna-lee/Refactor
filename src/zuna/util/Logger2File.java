package zuna.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;

import zuna.model.MyClass;
import zuna.model.MyMethod;

public class Logger2File {

	public static void print2CSVFileMethods(Hashtable<MyMethod, String> methods, String filename) {
		try {
			FileWriter fw = new FileWriter(new File("C:/Users/Jun/Documents/"+ filename + ".txt"), false);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw);

			for (MyMethod c : methods.keySet()) {
				out.print(filename + "," + c.getID() + "," + methods.get(c));
				out.println();
			}
			
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void print2CSVFile(Hashtable<MyClass, String> classes, String filename) {
		try {
			FileWriter fw = new FileWriter(new File("C:/Users/Jun/Documents/Test/"+ filename + ".csv"), false);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw);

			for (MyClass c : classes.keySet()) {
				out.print(filename + "," + c.getID() + "," + classes.get(c));
				out.println();
			}
			
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void print2CSVFile(ArrayList<String> metric, String filename) {
		try {
			FileWriter fw = new FileWriter(new File("C:/Users/zuna/Documents/Test/"+ filename + ".txt"), false);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw);

			for (String line: metric) {
				out.print(line);
				out.println();
			}
			
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
