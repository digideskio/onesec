package com.example.onesec;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import android.os.Environment;
import android.util.Log;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;

public class Oven {

    private static final int MEDIA_TYPE_IMAGE = 1;
	private static final int MEDIA_TYPE_VIDEO = 2;
	
    
    
    private static void makeVideo(List<Movie> inMovies, String cakename) throws IOException{
		if (inMovies.size() == 0){
        	return;
        }
		List<Track> videoTracks = new LinkedList<Track>();
        List<Track> audioTracks = new LinkedList<Track>();
        
        
        for (Movie m : inMovies) {
            for (Track t : m.getTracks()) {
                if (t.getHandler().equals("soun")) {
                    audioTracks.add(t);
                }
                if (t.getHandler().equals("vide")) {
                    videoTracks.add(t);
                }
            }
        }

        Movie result = new Movie();
        if (audioTracks.size() > 0) {
            result.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
            
        }
        if (videoTracks.size() > 0) {
            result.addTrack(new AppendTrack(videoTracks.toArray(new Track[videoTracks.size()])));
        }
        
        Container out = new DefaultMp4Builder().build(result);

        
        File cakeStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES), "OneSec/Cakes");
        
     // Create the storage directory if it does not exist
        if (! cakeStorageDir.exists()){
            if (! cakeStorageDir.mkdirs()){
                Log.d("OneSec", "failed to create directory");
                return;
            }
        }
        
        File output = new File(cakeStorageDir.getPath() + File.separator +
                cakename + ".mp4");
        FileOutputStream fos = new FileOutputStream(output);
        out.writeContainer(fos.getChannel());
        fos.close();
	}
    
//    public static void makeVideoFromChecked(String cakename) throws IOException {
//		List<Movie> inMovies = getCheckedMovies();
//		makeVideo(inMovies, cakename);
//	}
    
//	private static List<Movie> getCheckedMovies() throws FileNotFoundException, IOException {
//		List<Movie> movies = new ArrayList<Movie>();
//		for (Second sec : Cabinet.allSeconds) {
//			if (sec.isChecked()) {
//				System.out.println(sec.getVideoUri().toString() + " isChecked");
//				movies.add(sec.getMovie());
//			}
//		}
//		
//		return movies;
//	}
//	
//	public static void initialize(Context context) {		
//		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//		        Environment.DIRECTORY_MOVIES), "OneSec");
//		
//		if (!mediaStorageDir.isDirectory()){
//			//TODO  The app will probably crash if this isn't taken care of better
//			Toast.makeText(context, "No videos found", Toast.LENGTH_SHORT).show();
//		}
//		
//		for (File movie : mediaStorageDir.listFiles()) {
//			if (movie.toString().endsWith(".mp4")){
//				System.out.println("initializing " + movie.getPath());
//				Cabinet.add(new Second(Uri.fromFile(movie)));
//			}
//		}
//    }
}
