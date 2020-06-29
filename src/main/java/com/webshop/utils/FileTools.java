package com.webshop.utils;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FileTools {


    public static  void copyFile(MultipartFile file, String location) throws IOException {
        File fileDir = new File(location);
        if (!fileDir.exists()) {
            boolean dirCreated = fileDir.mkdir();
            if (!dirCreated)
                throw new IOException("Directory '" + location + "' could NOT be created");
        }
        String fileName = FilenameUtils.getName(file.getOriginalFilename());
        File squareFile = new File(location + File.separator +fileName);
        ImageIO.write(cropImageSquare(file), "jpg", squareFile);
//        FileCopyUtils.copy(squareFile, new File(UPLOADED_FOLDER + SUB_FOLDER + fileName));
//        FileCopyUtils.copy(file.getBytes(), new File(UPLOADED_FOLDER + SUB_FOLDER + fileName));
    }

    private static BufferedImage cropImageSquare(MultipartFile file) throws IOException {
        // Get a BufferedImage object from a byte array
        InputStream in = new ByteArrayInputStream(file.getBytes());
        BufferedImage originalImage = ImageIO.read(in);

        // Get image dimensions
        int height = originalImage.getHeight();
        int width = originalImage.getWidth();

        // The image is already a square
        if (height == width) {
            return originalImage;
        }

        // Compute the size of the square
        int squareSize = (height > width ? width : height);

        // Coordinates of the image's middle
        int xc = width / 2;
        int yc = height / 2;

        // Crop
        BufferedImage croppedImage = originalImage.getSubimage(
                xc - (squareSize / 2), // x coordinate of the upper-left corner
                yc - (squareSize / 2), // y coordinate of the upper-left corner
                squareSize,            // width
                squareSize             // height
        );

        return croppedImage;
    }

}
