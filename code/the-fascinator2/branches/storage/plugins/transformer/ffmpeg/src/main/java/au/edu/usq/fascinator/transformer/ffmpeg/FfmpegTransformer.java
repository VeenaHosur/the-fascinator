/*
 * The Fascinator - Plugin - Transformer - FFMPEG
 * Copyright (C) 2010 University of Southern Queensland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package au.edu.usq.fascinator.transformer.ffmpeg;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.edu.usq.fascinator.api.PluginException;
import au.edu.usq.fascinator.api.storage.DigitalObject;
import au.edu.usq.fascinator.api.storage.Payload;
import au.edu.usq.fascinator.api.storage.PayloadType;
import au.edu.usq.fascinator.api.storage.StorageException;
import au.edu.usq.fascinator.api.transformer.Transformer;
import au.edu.usq.fascinator.api.transformer.TransformerException;
import au.edu.usq.fascinator.common.JsonConfig;
import au.edu.usq.fascinator.common.MimeTypeUtil;

/**
 * Converts audio and video media to web friendly versions using the FFMPEG
 * library.
 * 
 * @author Oliver Lucido, Linda Octalina
 */
public class FfmpegTransformer implements Transformer {

    /** Logger **/
    private Logger log = LoggerFactory.getLogger(FfmpegTransformer.class);

    /** json config file **/
    private JsonConfig config;

    /** FFMPEG output file path **/
    private String outputPath;

    /** Ffmpeg class for conversion **/
    private Ffmpeg ffmpeg;

    /**
     * Transforming digital object method
     * 
     * @params object: DigitalObject to be transformed
     * @return transformed digital object
     * @throws TransformerException
     * @throws Exception
     * @throws StorageException
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    @Override
    public DigitalObject transform(DigitalObject object)
            throws TransformerException {
        File file = new File(object.getId());
        ffmpeg = new Ffmpeg(get("executable"));
        if (file.exists() && ffmpeg.isAvailable()) {
            outputPath = get("outputPath");
            File outputDir = new File(outputPath);
            outputDir.mkdirs();
            try {
                FfmpegInfo info = ffmpeg.getInfo(file);
                log.debug("{}: {}", file.getName(), info);
                if (info.isSupported()) {
                    if (info.hasVideo()) {
                        File thumbnail = getThumbnail(file, info.getDuration());
                        object = createFfmpegPayload(object, file);
                    }
                    String ext = FilenameUtils.getExtension(file.getName());
                    List<String> excludeList = Arrays.asList(StringUtils.split(
                            get("excludeExt"), ','));
                    if (!excludeList.contains(ext.toLowerCase())
                            && (info.hasVideo() || info.hasAudio())) {
                        File converted = convert(file);
                        object = createFfmpegPayload(object, converted);
                    }
                }
            } catch (Exception e) {
                log.error("Conversion failed for " + file, e);
                try {
                    object = createFfmpegErrorPayload(object, file, e
                            .getMessage());
                } catch (StorageException e1) {
                    e1.printStackTrace();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return object;
    }

    /**
     * Create ffmpeg error payload
     * 
     * @param object: DigitalObject that store the payload
     * @param file: File to be stored as payload
     * @param message
     * @return transformed DigitalObject
     * @throws StorageException
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public DigitalObject createFfmpegErrorPayload(DigitalObject object,
            File file, String message) throws StorageException,
            FileNotFoundException, UnsupportedEncodingException {
        String name = FilenameUtils.getBaseName(file.getName()) + "_error.htm";
        Payload ffmpegPayload = object.createStoredPayload(name,
                new ByteArrayInputStream(message.getBytes("UTF-8")));
        ffmpegPayload.setType(PayloadType.Enrichment);
        ffmpegPayload.setContentType("text/html");
        ffmpegPayload.setLabel("FFMPEG conversion errors");
        return object;
    }

    /**
     * Create converted ffmpeg payload
     * 
     * @param object: DigitalObject that store the payload
     * @param file: File to be stored as payload
     * @return transformed DigitalObject
     * @throws StorageException
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public DigitalObject createFfmpegPayload(DigitalObject object, File file)
            throws StorageException, FileNotFoundException {
        String name = file.getName();
        Payload ffmpegPayload = object.createStoredPayload(name,
                new FileInputStream(file));
        ffmpegPayload.setType(PayloadType.Enrichment);
        ffmpegPayload.setContentType(MimeTypeUtil.getMimeType(name));
        ffmpegPayload.setLabel(name);
        return object;
    }

    /**
     * Generate thumbnail for the video
     * 
     * @param sourceFile: video file to be generated
     * @param duration: duration for the thumbnail
     * @return generated thumbnail file
     * @throws TransformerException
     */
    private File getThumbnail(File sourceFile, long duration)
            throws TransformerException {
        log.info("Creating thumbnail...");
        String basename = FilenameUtils.getBaseName(sourceFile.getName());
        File outputFile = new File(outputPath, basename + "_thumbnail.jpg");
        if (outputFile.exists()) {
            FileUtils.deleteQuietly(outputFile);
        }
        try {
            List<String> params = new ArrayList<String>();
            params.add("-i");
            params.add(sourceFile.getAbsolutePath()); // input file
            // get random frame from first quarter of video
            params.add("-y"); // overwrite output file
            params.add("-deinterlace");
            params.add("-an"); // disable audio
            params.add("-ss");
            long start = (long) (Math.random() * duration * 0.25);
            params.add(Long.toString(start)); // start time offset
            params.add("-t");
            params.add("00:00:01"); // duration
            params.add("-r");
            params.add("1"); // frame rate
            params.add("-s");
            params.add(get("thumbnailSize")); // size
            params.add("-vcodec");
            params.add("mjpeg");
            params.add("-f");
            params.add("mjpeg"); // mjpeg output format
            params.add(outputFile.getAbsolutePath()); // output file
            String stderr = ffmpeg.executeAndWait(params);
            log.debug(stderr);
            log.info("Thumbnail created: outputFile={}", outputFile);
        } catch (IOException ioe) {
            log.error("Failed to create thumbnail!", ioe);
            throw new TransformerException(ioe);
        }
        return outputFile;
    }

    /**
     * Convert audio/video to flv format
     * 
     * @param sourceFile: to be converted
     * @return converted file
     * @throws TransformerException
     */
    private File convert(File sourceFile) throws TransformerException {
        String outputExt = get("outputExt");
        log.info("Converting to {}: {}", outputExt, sourceFile);
        String filename = sourceFile.getName();
        String basename = FilenameUtils.getBaseName(filename);
        File outputFile = new File(outputPath, basename + "." + outputExt);
        if (outputFile.exists()) {
            FileUtils.deleteQuietly(outputFile);
        }
        try {
            List<String> params = new ArrayList<String>();
            params.add("-i");
            params.add(sourceFile.getAbsolutePath()); // input file
            params.add("-y"); // overwrite output file
            // load extension specific parameters or use defaults if not found
            String configParams = get("params/default");
            String ext = FilenameUtils.getExtension(filename);
            if (!"".equals(ext)) {
                log.debug("Loading params for {}...", ext);
                configParams = get("params/" + ext);
            }
            params.addAll(Arrays.asList(StringUtils.split(configParams, ' ')));
            params.add(outputFile.getAbsolutePath()); // output file
            String stderr = ffmpeg.executeAndWait(params);
            log.debug(stderr);
            log.info("Conversion successful: outputFile={}", outputFile);
        } catch (IOException ioe) {
            log.error("Failed to convert!", ioe);
            throw new TransformerException(ioe);
        }
        return outputFile;
    }

    /**
     * Get Transformer id
     * 
     * @return id
     */
    @Override
    public String getId() {
        return "ffmpeg";
    }

    /**
     * Get Transformer name
     * 
     * @return name
     */
    @Override
    public String getName() {
        return "FFMPEG Transformer";
    }

    /**
     * Init method to initialise Ffmpeg transformer
     * 
     * @param jsonFile
     * @throws IOException
     * @throws PluginException
     */
    @Override
    public void init(File jsonFile) throws PluginException {
        try {
            config = new JsonConfig(jsonFile);
        } catch (IOException ioe) {
            throw new PluginException(ioe);
        }
    }

    /**
     * Init method to initialise Ffmpeg transformer
     * 
     * @param jsonString
     * @throws IOException
     * @throws PluginException
     */
    @Override
    public void init(String jsonString) throws PluginException {
        try {
            config = new JsonConfig(jsonString);
        } catch (IOException ioe) {
            throw new PluginException(ioe);
        }
    }

    /**
     * Get configuration value from json file
     * 
     * @param key
     * @return value of the configuration
     */
    private String get(String key) {
        return config.get("transformer/ffmpeg/" + key);
    }

    /**
     * Shut down the transformer plugin
     */
    @Override
    public void shutdown() throws PluginException {
    }
}
