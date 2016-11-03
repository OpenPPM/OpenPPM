/*
 * Copyright (C) 2009-2015 SM2 SOFTWARE & SERVICES MANAGEMENT
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program has been created in the hope that it will be useful.
 * It is distributed WITHOUT ANY WARRANTY of any Kind,
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see http://www.gnu.org/licenses/.
 *
 * For more information, please contact SM2 Software & Services Management.
 * Mail: info@talaia-openppm.com
 * Web: http://www.talaia-openppm.com
 *
 * Module: utils
 * File: DocumentUtils.java
 * Create User: javier.hernandez
 * Create Date: 16/03/2015 12:48:20
 */

package es.sm2.openppm.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by javier.hernandez on 16/03/2015.
 */
public class DocumentUtils {

    /**
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static byte[] getBytesFromFile(File file) throws IOException {

        byte[] bytes = null;

        if (file != null) {
            InputStream is = new FileInputStream(file);

            // Get the size of the file
            long length = file.length();

            // You cannot create an array using a long type.
            // It needs to be an int type.
            // Before converting to an int type, check
            // to ensure that file is not larger than Integer.MAX_VALUE.
            if (length > Integer.MAX_VALUE) {

                LogManager.getConsoleLog(DocumentUtils.class).error("File too large");
            }

            // Create the byte array to hold the data
            bytes = new byte[(int) length];

            // Read in the bytes
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length
                    && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }

            is.close();

            // Ensure all the bytes have been read in
            if (offset < bytes.length) {
                throw new IOException("Could not completely read file "
                        + file.getName());
            }
        }

        // Close the input stream and return bytes
        return bytes;
    }

    /**
     * Delete file
     *
     * @param file - file for delete
     */
    public static void deleteFile(File file) {

        if (file == null) {

            LogManager.getConsoleLog(DocumentUtils.class).warn("File not deleted, is null");
        }
        else {

            try {

                LogManager.getConsoleLog(DocumentUtils.class).debug("Delete file: " + file.getName());
                boolean deleted = file.delete();
                if (deleted) {

                    LogManager.getConsoleLog(DocumentUtils.class).info("The file '" + file.getAbsolutePath() + "' has been deleted");
                } else {
                    LogManager.getConsoleLog(DocumentUtils.class).warn("The file " + file.getAbsolutePath() + " not has been deleted");
                }
            } catch (SecurityException e) {
                LogManager.getConsoleLog(DocumentUtils.class).warn("File not removed ", e);
            }
        }
    }

    /**
     *
     * @param files
     * @return
     * @throws java.io.IOException
     */
    public static byte[] zipFiles (List<File> files) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);

        for (File file : files) {

            zos.putNextEntry(new ZipEntry(file.getName()));

            zos.write(DocumentUtils.getBytesFromFile(file.getAbsoluteFile()));

            zos.closeEntry();
        }

        zos.flush();
        baos.flush();
        zos.close();
        baos.close();

        return baos.toByteArray();
    }

}
