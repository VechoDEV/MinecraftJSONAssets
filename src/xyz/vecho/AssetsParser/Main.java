package xyz.vecho.AssetsParser;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.text.ParseException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import org.apache.commons.io.IOUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Main extends JFrame {
	
	JTextArea area;
	
	public Main() {
		this.setSize(600, 350);
		area = new JTextArea(50, 10);
		area.setEnabled(false);
		area.setDisabledTextColor(Color.WHITE);
		area.setBackground(Color.BLACK);
		this.add(area);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void setPrints() {
		System.setErr(new PrintStream(new OutputStream() {
			
			@Override
			public void write(int b) throws IOException {
		        area.append(String.valueOf((char)b));
		        area.setCaretPosition(area.getDocument().getLength());
			}
		}));
		
		System.setOut(new PrintStream(new OutputStream() {
			
			@Override
			public void write(int b) throws IOException {
		        area.append(String.valueOf((char)b));
		        area.setCaretPosition(area.getDocument().getLength());
			}
		}));
	}
	
	public static void main(String[] args) throws IOException, ParseException {
		
		Main main = new Main();
		main.setPrints();
		
		System.out.println("Select a directory for workPath");
		System.out.println("This directory used to download the objects");
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		File workPathFile = null;
		int val = fc.showOpenDialog(null);
		if (val == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			if (file.isDirectory()) {
				workPathFile = file;
			} else {
				System.out.println("You must select a folder. Waiting for close the log area for exiting.");
			}
		} else {
			System.out.println("You must select a folder. Waiting for close the log area for exiting.");
			System.exit(0);
		}
		File jsonDir = new File(workPathFile, "jsons");
		if (!jsonDir.exists()) jsonDir.mkdirs();
		
		File file112 = new File(jsonDir, "1.12.json");
		File file18 = new File(jsonDir, "1.8.json");
		File file188 = new File(jsonDir, "1.8.8.json");
		File file115 = new File(jsonDir, "1.15.json");
		File file116 = new File(jsonDir, "1.16.json");
		
		String url112 = "https://raw.githubusercontent.com/VechoDEV/MinecraftJSONAssets/main/1.12.json";
		String url18 = "https://raw.githubusercontent.com/VechoDEV/MinecraftJSONAssets/main/1.8.json";
		String url188 = "https://raw.githubusercontent.com/VechoDEV/MinecraftJSONAssets/main/1.8.8.json";
		String url115 = "https://raw.githubusercontent.com/VechoDEV/MinecraftJSONAssets/main/1.15.json";
		String url116 = "https://raw.githubusercontent.com/VechoDEV/MinecraftJSONAssets/main/1.16.json";
		
		if (!file112.exists()) {
			file112.createNewFile();
			InputStream stream = new URL(url112).openStream();
			IOUtils.copy(stream, new FileOutputStream(file112));
		}
		
		if (!file18.exists()) {
			file18.createNewFile();
			InputStream stream = new URL(url18).openStream();
			IOUtils.copy(stream, new FileOutputStream(file18));
		}
		
		if (!file188.exists()) {
			file188.createNewFile();
			InputStream stream = new URL(url188).openStream();
			IOUtils.copy(stream, new FileOutputStream(file188));
		}
		
		if (!file115.exists()) {
			file115.createNewFile();
			InputStream stream = new URL(url115).openStream();
			IOUtils.copy(stream, new FileOutputStream(file115));
		}
		
		if (!file116.exists()) {
			file116.createNewFile();
			InputStream stream = new URL(url116).openStream();
			IOUtils.copy(stream, new FileOutputStream(file116));
		}
		
		File objects = new File(workPathFile, "objects");
		System.out.println("Checking/Downloading assets.");
		download(file18, objects);
		download(file188, objects);
		download(file112, objects);
		download(file115, objects);
		download(file116, objects);
		System.out.println("Assets checked/downloaded.");
		
		System.out.println("Doing mappings 1.8...");
		File mapFile = new File(workPathFile, "map_1.8.txt");
		map(file18, mapFile);
		System.out.println("Doing mappings 1.8.8...");
		File mapFile1 = new File(workPathFile, "map_1.8.8.txt");
		map(file188, mapFile1);
		System.out.println("Doing mappings 1.12...");
		File mapFile2 = new File(workPathFile, "map_1.12.txt");
		map(file112, mapFile2);
		System.out.println("Doing mappings 1.15..."); 
		File mapFile3 = new File(workPathFile, "map_1.15.txt"); 
		map(file115, mapFile3); 
		System.out.println("Doing mappings 1.16...");
		File mapFile4 = new File(workPathFile, "map_1.16.txt");
		map(file116, mapFile4);
		System.out.println("Mappings finished. Waiting for close the log area for exiting.");
	}
	
	public static void download(File json, File objects) throws IOException, ParseException {
		FileReader reader = new FileReader(json);
		JsonObject obj = new JsonParser().parseReader(reader).getAsJsonObject();
		
		JsonObject obj2 = obj.get("objects").getAsJsonObject();
		for (String objectsObject : obj2.keySet()) {
			String hash = obj2.get(objectsObject).getAsJsonObject().get("hash").getAsString();
			String downloadLink = "http://resources.download.minecraft.net/" + hash.substring(0, 2) + "/"+ hash;
			File locationCopyFolder = new File(objects, hash.substring(0, 2));
			File locationCopy = new File(locationCopyFolder, hash);
			if (!locationCopyFolder.exists()) locationCopyFolder.mkdirs();
			if (!locationCopy.exists() || locationCopy.length() != obj2.get(objectsObject).getAsJsonObject().get("size").getAsLong()) {
				System.out.println("Downloading: " + objectsObject + " --- "+json.getName());
				locationCopy.createNewFile();
				InputStream stream = new URL(downloadLink).openStream();
				IOUtils.copy(stream, new FileOutputStream(locationCopy));
			}
		}
		
		reader.close();
		
	}
	
	public static void map(File json, File mapFile) throws IOException {
		FileReader reader = new FileReader(json);
		JsonObject obj = new JsonParser().parseReader(reader).getAsJsonObject();
		
		JsonObject obj2 = obj.get("objects").getAsJsonObject();
		if (!mapFile.exists()) mapFile.createNewFile();
		else {
			mapFile.delete();
			mapFile.createNewFile();
		}
		PrintStream fileStream = new PrintStream(mapFile);
		for (String objectsObject : obj2.keySet()) {
			String hash = obj2.get(objectsObject).getAsJsonObject().get("hash").getAsString();
			String folder = hash.substring(0, 2);
			fileStream.println(objectsObject + " : " + folder + "/" + hash);
		}
		
		fileStream.flush();
		fileStream.close();
		
		reader.close();
	}
	
}
