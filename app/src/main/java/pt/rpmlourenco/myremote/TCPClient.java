package pt.rpmlourenco.myremote;

import android.os.AsyncTask;

import org.xbill.DNS.DClass;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.Type;

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
    public MainActivity activity;

    public TCPClient(MainActivity a)
    {
        this.activity = a;
    }

    public static String tcpSend(String hostname, int port, int timeout, String content)
    {
        String result;
        Socket clientSocket;
        try
        {
            // Get IP Address of hostname
            int type = Type.A, dclass = DClass.IN;
            Name name = null;
            name = Name.fromString(hostname, Name.root);
            Message query, response;
            Record rec;
            SimpleResolver res = new SimpleResolver("192.168.1.1");
            rec = Record.newRecord(name, type, dclass);
            query = Message.newQuery(rec);
            response = res.send(query);
            String ip = response.getSectionRRsets(1)[0].first().rdataToString();

            clientSocket = new Socket();
            clientSocket.connect(new InetSocketAddress(ip, port), 1500);

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
            e.printStackTrace();
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
        //Toast.makeText(activity.getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        activity.showAToast(result);
    }

}