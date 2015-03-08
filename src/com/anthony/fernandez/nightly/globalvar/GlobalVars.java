package com.anthony.fernandez.nightly.globalvar;

import com.anthony.fernandez.nightly.database.DatabaseHelper;
import com.anthony.fernandez.nightly.imageUtils.ImageDownloaderTask;

import android.graphics.Bitmap;

public class GlobalVars {
	
	/**
	 * @see {@link DatabaseHelper} for token storage
	 * This is use to avoid database IO during processes. 
	 * <b>Initialized</b> during the connection BUT <b>must</b> be destroyed 
	 * during the disconnection. 
	 */
	public static CurrentUserConnected currentUser = null;
	
	public static class CurrentUserConnected{
		public int _idLocal;
		public String _idServer;
		public String token;
		public String email;
		public String gmc;
		public String language;
		public String firstname;
		public String lastname;
		/**
		 * Only use one time, for the first image loading.
		 * The image caching @see {@link ImageDownloaderTask} will store the bitmap file.
		 * So, use profilImg (if not null) then.
		 */
		public String imgURL;
		public Bitmap pofilImg;
		public boolean gender;
	}

}
