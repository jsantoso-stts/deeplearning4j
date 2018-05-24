package org.datavec.api.resources;

import lombok.NonNull;
import org.nd4j.base.Preconditions;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * DataVecResources controls the local storage locations for resources and datasets that are downloaded and stored locally.<br>
 * The storage location is customizable in 2 ways:<br>
 * (a) via the {@link #DATAVEC_RESOURCES_DIR_PROPERTY} system property, org.deeplearning4j.resources.directory<br>
 * (b) By calling {@link #setBaseDirectory(File)} at runtime<br>
 *
 * @author Alex Black
 */
public class DataVecResources {

    public static final String DATAVEC_RESOURCES_DIR_PROPERTY = "org.datavec.resources.directory";
    public static final String DATAVEC_BASE_URL_PROPERTY = "org.datavec.resources.baseurl";
    private static final String DATAVEC_DEFAULT_URL = "http://blob.deeplearning4j.org/";

    private static File baseDirectory;
    private static String baseURL;

    static {
        resetBaseDirectoryLocation();

        String property = System.getProperty(DATAVEC_BASE_URL_PROPERTY);
        if(property != null){
            baseURL = property;
        } else {
            baseURL = DATAVEC_DEFAULT_URL;
        }
    }

    /**
     * Set the base download URL for (most) DATAVEC datasets and resources.<br>
     * This usually doesn't need to be set manually unless there is some issue with the default location
     * @param baseDownloadURL Base download URL to set. For example, http://blob.deeplearning4j.org/
     */
    public static void setBaseDownloadURL(@NonNull String baseDownloadURL){
        baseURL = baseDownloadURL;
    }

    /**
     * @return The base URL hosting DATAVEC datasets and resources
     */
    public static String getBaseDownloadURL(){
        return baseURL;
    }

    /**
     * Get the URL relative to the base URL.<br>
     * For example, if baseURL is "http://blob.deeplearning4j.org/", and relativeToBase is "/datasets/iris.dat"
     * this simply returns "http://blob.deeplearning4j.org/datasets/iris.dat"
     *
     * @param relativeToBase Relative URL
     * @return URL
     * @throws MalformedURLException For bad URL
     */
    public static URL getURL(String relativeToBase) throws MalformedURLException {
        return new URL(getURLString(relativeToBase));
    }

    /**
     * Get the URL relative to the base URL as a String.<br>
     * For example, if baseURL is "http://blob.deeplearning4j.org/", and relativeToBase is "/datasets/iris.dat"
     * this simply returns "http://blob.deeplearning4j.org/datasets/iris.dat"
     *
     * @param relativeToBase Relative URL
     * @return URL
     * @throws MalformedURLException For bad URL
     */
    public static String getURLString(String relativeToBase){
        if(relativeToBase.startsWith("/")){
            relativeToBase = relativeToBase.substring(1);
        }
        return baseURL + relativeToBase;
    }

    /**
     * Reset to the default directory, or the directory set via the {@link #DATAVEC_RESOURCES_DIR_PROPERTY} system property,
     * org.deeplearning4j.resources.directory
     */
    public static void resetBaseDirectoryLocation(){
        String property = System.getProperty(DATAVEC_RESOURCES_DIR_PROPERTY);
        if(property != null){
            baseDirectory = new File(property);
        } else {
            baseDirectory = new File(System.getProperty("user.home"), ".deeplearning4j");
        }

        if(!baseDirectory.exists()){
            baseDirectory.mkdirs();
        }
    }

    /**
     * Set the base directory for local storage of files. Default is: {@code new File(System.getProperty("user.home"), ".deeplearning4j")}
     * @param f Base directory to use for resources
     */
    public static void setBaseDirectory(@NonNull File f){
        Preconditions.checkState(f.exists() && f.isDirectory(), "Specified base directory does not exist and/or is not a directory: %s", f.getAbsolutePath());
        baseDirectory = f;
    }

    /**
     * @return The base storage directory for DATAVEC resources
     */
    public static File getBaseDirectory(){
        return baseDirectory;
    }

    /**
     * Get the storage location for the specified resource type and resource name
     * @param resourceType Type of resource
     * @param resourceName Name of the resource
     * @return The root directory. Creates the directory and any parent directories, if required
     */
    public static File getDirectory(ResourceType resourceType, String resourceName){
        File f = new File(baseDirectory, resourceType.resourceName());
        f = new File(f, resourceName);
        f.mkdirs();
        return f;
    }
}
