import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class JmcAllocationTest
{
    private static final int SIZE = 64;
    private static final int PORT = 8000;
    private static final String HOST = "localhost";

    public static void main(String[] args) throws IOException
    {
        final ByteBuffer readBuffer = ByteBuffer.allocateDirect(SIZE);
        final ByteBuffer writeBuffer = ByteBuffer.allocateDirect(SIZE);

        final ServerSocketChannel server = ServerSocketChannel
            .open()
            .bind(new InetSocketAddress(HOST, PORT));

        final SocketChannel writer = SocketChannel.open();
        System.out.println("Connected: " + writer.connect(new InetSocketAddress(HOST, PORT)));

        final SocketChannel reader = server.accept();

        for (int i = 0; true; i++)
        {
            writeBuffer.putInt(0, i);
            final int written = writer.write(writeBuffer);
            if (written != SIZE)
            {
                System.out.println("written = " + written);
            }

            final int read = reader.read(readBuffer);
            if (read != SIZE)
            {
                System.out.println("read = " + read);
            }

            final int value = readBuffer.getInt(0);
            if (value != i)
            {
                System.out.println("value = " + value);
            }

            readBuffer.clear();
            writeBuffer.clear();
        }
    }
}
