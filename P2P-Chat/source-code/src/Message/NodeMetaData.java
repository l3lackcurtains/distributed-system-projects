package Message;

import java.io.Serializable;

public class NodeMetaData implements Serializable {

    private String ipAddress;
    private String username;
    private int port;

    public NodeMetaData(String username, String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.username = username;
    }


    public String getIpAddress() {
        return ipAddress;
    }

    public String getUsername() {
        return username;
    }

    public int getPort() {
        return port;
    }

}
