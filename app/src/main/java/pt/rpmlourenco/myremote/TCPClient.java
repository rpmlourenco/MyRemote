package pt.rpmlourenco.myremote;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

class TCPClientParams {
    public String ip;
    public int port;
    public int timeout;
    public String content;

    TCPClientParams(String ip, int port, int timeout, String content) {
        this.ip = ip;
        this.port = port;
        this.timeout = timeout;
        this.content = content;
    }
}

class TCPClient extends AsyncTask<TCPClientParams, Void, String>
{
    public Activity activity;

    public TCPClient(Activity a)
    {
        this.activity = a;
    }

    public static String tcpSend(String ip, int port, int timeout, String content)
    {
        String result;
        Socket clientSocket;
        try
        {
            clientSocket = new Socket();
            clientSocket.connect(new InetSocketAddress(ip, port), 1500);
            //clientSocket = new Socket(ip, port);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outToServer.writeBytes(content + '\n');
            outToServer.flush();
            clientSocket.setSoTimeout(timeout);
            result = inFromServer.readLine();
            clientSocket.close();
            outToServer.close();
            inFromServer.close();

        }
        catch (Exception e)
        {
            //e.printStackTrace();
            result = "Could not connect";
        }
        return result;
    }

    @Override
    protected String doInBackground(TCPClientParams... params) {
        return tcpSend(params[0].ip, params[0].port, params[0].timeout, params[0].content);
    }

    @Override
    protected void onPostExecute(String result)
    {
        Toast.makeText(activity.getApplicationContext(), result, Toast.LENGTH_SHORT).show();
    }


}