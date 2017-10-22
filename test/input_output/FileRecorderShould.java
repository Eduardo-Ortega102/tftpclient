package input_output;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.time.Instant;

import static input_output.FileRecorder.BUFFER_LENGTH;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class FileRecorderShould {

    private FileRecorder fileRecorder;
    private String storagePath;
    private String filename;

    @Before
    public void setUp() throws Exception {
        storagePath = "out/";
        filename = "filename.txt";
        fileRecorder = new FileRecorder(storagePath, filename);
    }

    @Test
    public void count_the_amount_of_bytes_received() throws Exception {
        final byte[] data1 = data("Hello World!\n");
        final byte[] data2 = data("Bye!");
        final long expectedLength = data1.length + data2.length;
        fileRecorder.receive(data1);
        fileRecorder.receive(data2);
        assertThat(fileRecorder.countOfBytes(), is(expectedLength));
    }

    @Test
    public void not_store_when_buffer_is_empty() throws Exception {
        final long expectedCount = 0;
        fileRecorder.store();
        assertThat(fileRecorder.countOfBytes(), is(expectedCount));
        assertThat(new File(storagePath + filename).exists(), is(false));
    }

    @Test
    public void store_all_bytes_into_a_file() throws Exception {
        final byte[] data1 = data("Hello World!\n");
        final byte[] data2 = data("Bye! " + Instant.now());
        fileRecorder.receive(data1);
        fileRecorder.receive(data2);
        fileRecorder.store();
        File file = new File(storagePath + filename);
        assertThat(file.exists(), is(true));
        file.delete();
    }

    @Test
    public void store_all_bytes_when_buffer_is_full() throws Exception {
        final byte[] data = data("Hello, this text is going to be repeated a lot of times inside the file =D \n");
        for (int i = 0; i <= BUFFER_LENGTH / data.length; i++) fileRecorder.receive(data);
        File file = new File(storagePath + filename);
        assertThat(file.exists(), is(true));
        assertThat(file.length(), is(fileRecorder.countOfBytes()));
        file.delete();
    }

    private byte[] data(String data) {
        return data.getBytes();
    }

}
