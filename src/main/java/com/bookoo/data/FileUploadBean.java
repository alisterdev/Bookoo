package com.bookoo.data;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

//import org.apache.commons.io.FileUtils;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

// Needed for writing file
import com.google.common.io.Files;

/**
 * Code provided by Ilya Shaikovsky.
 * 
 * 
 * @author CORNEVIN Jolan
 */
@Named("fileUpload")
@SessionScoped
public class FileUploadBean implements Serializable {
	
	private static final long serialVersionUID = 2705433462136755119L;
	private ArrayList<UploadedImage> files = new ArrayList<UploadedImage>();
	private String fileName;

	/**
	 * Send the image to the browser.
	 * 
	 * @param stream
	 * @param object
	 * @throws IOException
	 */
	public void paint(OutputStream stream, Object object) throws IOException {
		stream.write(getFiles().get((Integer) object).getData());
		stream.close();
	}

	/**
	 * This method is called for each file uploaded by rich:fileUpload. It
	 * copies the data from the RichFaces UploadedFile object into an
	 * UploadedImage object. This object is required so that the image can be
	 * displayed in the browser using the stream.write
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void listenerBanner(FileUploadEvent event) throws Exception {

		UploadedFile rawUploadedFile = event.getUploadedFile();
		UploadedImage uploadedImageFile = new UploadedImage();

		fileName = rawUploadedFile.getName();

		uploadedImageFile.setLength(rawUploadedFile.getData().length);
		uploadedImageFile.setName(rawUploadedFile.getName());
		uploadedImageFile.setData(rawUploadedFile.getData());

		files.add(uploadedImageFile);

		// Get the path to this application on the server
		String absoluteWebPath = FacesContext.getCurrentInstance()
				.getExternalContext().getRealPath("/");
		// Create a File pointing to the folder shown
		File file = new File(absoluteWebPath + "/resources/img/banners/"
				+ uploadedImageFile.getName());
		// Using the Google guava commons.io library
		Files.write(uploadedImageFile.getData(), file);
	}

	public void listenerBook(FileUploadEvent event) throws Exception {

		UploadedFile rawUploadedFile = event.getUploadedFile();
		UploadedImage uploadedImageFile = new UploadedImage();

		fileName = rawUploadedFile.getName();

		uploadedImageFile.setLength(rawUploadedFile.getData().length);
		uploadedImageFile.setName(rawUploadedFile.getName());
		uploadedImageFile.setData(rawUploadedFile.getData());

		files.add(uploadedImageFile);

		// Get the path to this application on the server
		String absoluteWebPath = FacesContext.getCurrentInstance()
				.getExternalContext().getRealPath("/");
		// Create a File pointing to the folder shown
		File file = new File(absoluteWebPath + "/resources/img/covers/"
				+ uploadedImageFile.getName());
		// Using the Google guava commons.io library
		Files.write(uploadedImageFile.getData(), file);
	}

	public String clearUploadData() {
		files.clear();
		return null;
	}

	public int getSize() {
		if (getFiles().size() > 0) {
			return getFiles().size();
		} else {
			return 0;
		}
	}

	public long getTimeStamp() {
		return System.currentTimeMillis();
	}

	public ArrayList<UploadedImage> getFiles() {
		return files;
	}

	public void setFiles(ArrayList<UploadedImage> files) {
		this.files = files;
	}

	public String getNameFile() {
		return fileName;
	}

}