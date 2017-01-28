package pt.rpmlourenco.myremote;

import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

class WOL extends AsyncTask<String, String, String> {

    /*
    public static void main(String[] args) throws IOException {
        String macAddress = args[0];
        int port = 9;
        wakeUp(macAddress, port);
    }
    */

    @Override
    protected String doInBackground(String... params) {
        String macAddress = params[0];
        int port = 9;
        try {
            wakeUp(macAddress, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void wakeUp(String macAddress, int port) throws IOException {
        byte[] bytes = getMagicBytes(macAddress);
        InetAddress address = getMulticastAddress();
        send(bytes, address, port);
    }

    private static void send(byte[] bytes, InetAddress addr, int port)
            throws IOException {
        DatagramPacket p = new DatagramPacket(bytes, bytes.length, addr, port);
        DatagramSocket socket = new DatagramSocket();
        socket.send(p);
        socket.close();
    }

    private static InetAddress getMulticastAddress()
            throws UnknownHostException {
        return InetAddress.getByAddress(new byte[] {-1, -1, -1, -1});
    }

    private static byte[] getMagicBytes(String macAddress) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        for (int i = 0; i < 6; i++)
            bytes.write(0xff);

        byte[] macAddressBytes = parseHexString(macAddress);
        for (int i = 0; i < 16; i++)
            bytes.write(macAddressBytes);

        bytes.flush();

        return bytes.toByteArray();
    }

    private static byte[] parseHexString(String string) {
        byte[] bytes = new byte[string.length() / 2];
        for (int i = 0, j = 0; i < string.length(); i += 2, j++)
            bytes[j] = (byte) Integer.parseInt(string.substring(i, i + 2), 16);
        return bytes;
    }

}