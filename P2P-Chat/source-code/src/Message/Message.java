package Message;

import java.io.Serializable;

public class Message implements Serializable{

    private final String type;
    private final NodeMetaData nodeMetaData;
    private NodeMetaData successorNodeMetaData;
    private NodeMetaData predecessorNodeMetaData;
    private String context;

    public Message(String type, NodeMetaData nodeMetaData, NodeMetaData successorNodeMetaData, NodeMetaData predecessorNodeMetaData ) {
        this.type = type;
        this.nodeMetaData = nodeMetaData;
        this.successorNodeMetaData = successorNodeMetaData;
        this.predecessorNodeMetaData = predecessorNodeMetaData;
    }

    public void setMessageContext(String context) {
        this.context = context;
    }

    public String getType() {
        return type;
    }

    public String getContext() {
        return context;
    }

    public NodeMetaData getNodeMetaData() {
        return nodeMetaData;
    }

    public NodeMetaData getSuccessorNodeMetaData() {
        return successorNodeMetaData;
    }

    public NodeMetaData getPredecessorNodeMetaData() {
        return predecessorNodeMetaData;
    }

}
