package input_output;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static input_output.DigitalUnit.ONE_MEGABYTE;

public class FileRecorder {
    private final String filePath;
    private final ByteArrayOutputStream buffer;
    private final int maximum_size;
    private long amountOfBytes;

    public FileRecorder(String storagePath, String filename) {
        this.filePath = storagePath + filename;
        maximum_size = ONE_MEGABYTE.value();
        buffer = new ByteArrayOutputStream(maximum_size);
        amountOfBytes = 0;
    }

    public void receive(byte[] data) throws IOException {
        buffer.write(data);
        amountOfBytes += data.length;
        if (buffer.size() >= maximum_size) store();
    }

    public long countOfBytes() {
        return amountOfBytes;
    }

    public void store() throws IOException {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(filePath);
            fileOutputStream.write(buffer.toByteArray());
            buffer.reset();
        } finally {
            fileOutputStream.close();
        }
    }
}