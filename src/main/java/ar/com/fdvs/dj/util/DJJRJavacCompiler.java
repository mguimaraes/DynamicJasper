package ar.com.fdvs.dj.util;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRJavacCompiler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 *         Date: Oct 8, 2007
 *         Time: 11:07:07 AM
 */
public class DJJRJavacCompiler extends JRJavacCompiler {

    public String compileClasses(File[] sourceFiles, String classpath) throws JRException {
        String[] source = new String[sourceFiles.length + 5];
        source[0] = "javac";
        source[1] = "-classpath";
        source[2] = classpath;
        source[3] = "-encoding";
        source[4] = System.getProperty("file.encoding");
        for (int i = 0; i < sourceFiles.length; i++) {
            source[i + 5] = sourceFiles[i].getPath();
        }

        try {
            // Compile the source file and arrange to read the errors if any.
            Process compile = Runtime.getRuntime().exec(source);
            InputStream errFile = compile.getErrorStream();

            // Read the error messages (if any) into the ByteArrayOutputStream
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int count = 0;
            do {
                count = errFile.read(buffer);
                if (count > 0) {
                    baos.write(buffer, 0, count);
                }
            } while (count >= 0);

            if (baos.toString().indexOf("error") != -1) {
                return baos.toString();
            }

            return null;
        }
        catch (Exception e) {
            StringBuffer files = new StringBuffer();
            for (int i = 0; i < sourceFiles.length; ++i) {
                files.append(sourceFiles[i].getPath());
                files.append(' ');
            }
            throw new JRException("Error compiling report java source files : " + files, e);
        }
    }
}