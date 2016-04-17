import java.util.*;

class Player {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        int nodes = in.nextInt(); // the total number of nodes in the level, including the gateways
        int links = in.nextInt(); // the number of links
        int exits = in.nextInt(); // the number of exit gateways

        Map<Integer,Node> graph = new HashMap<>(nodes);

        for (int i = 0; i < links; i++) {
            Node node1 = new Node(in.nextInt());
            Node node2 = new Node(in.nextInt());

            // Add links to each node
            node1.addLinkTo(node2);
            node2.addLinkTo(node1);

            // Add nodes into graph
            if (graph.containsKey(node1.getId())) {
                graph.get(node1.getId()).addLinkTo(node2);
            } else {
                graph.put(node1.getId(), node1);
            }

            if (graph.containsKey(node2.getId())) {
                graph.get(node2.getId()).addLinkTo(node1);
            } else {
                graph.put(node2.getId(), node2);
            }
        }

        // Set exit nodes into graph
        for (int i = 0; i < exits; i++) {
            int nodeId = in.nextInt();
            Node exitNodeId = graph.get(nodeId);
            Node tempNode = new Node(exitNodeId.getId(), exitNodeId.getLinksTo());

            exitNodeId.setExitNode(true);

            for (Map.Entry<Integer, Node> integerNodeEntry : graph.entrySet()) {
                Node value = integerNodeEntry.getValue();

                if(value.getLinksTo().remove(tempNode)){
                    value.getLinksTo().add(exitNodeId);
                }
            }
        }

        // game loop
        while (true) {
            Node skyNetNode = graph.get(in.nextInt()); // The index of the node on which the Skynet agent is positioned this turn
            System.out.println(skyNetNode.getId() + " " + getExitNodeFromAgentNode(skyNetNode));
        }
    }

    private static Integer getExitNodeFromAgentNode(Node skyNetNode) {
        for (Node node : skyNetNode.getLinksTo()) {
            if (node.isExitNode()) {
                return node.getId();
            }
        }
        return skyNetNode.getLinksTo().iterator().next().getId();
    }
}

class Node {
    private Integer id;
    private Set<Node> linksTo;
    private boolean isExitNode;

    public Node(int id) {
        this.id = id;
        this.linksTo = new HashSet<>();
        this.isExitNode = false;
    }

    public Node(int id, Set<Node> linksTo) {
        this.id = id;
        this.linksTo = linksTo;
        this.isExitNode = false;
    }

    public boolean isExitNode() {
        return isExitNode;
    }

    public void setExitNode(boolean exisNode) {
        isExitNode = exisNode;
    }

    public Integer getId() {
        return id;
    }

    public Set<Node> getLinksTo() {
        return linksTo;
    }

    public void addLinkTo(Node node) {
        linksTo.add(node);
    }

    @Override
    public String toString() {
        List<String> linkToIds = new ArrayList<>(linksTo.size());
        for (Node node : linksTo) {
            linkToIds.add(node.getId() + " " + node.isExitNode());
        }
        return "id: " + id + " isExit: " + isExitNode() + " linksTo: " + Arrays.toString(linkToIds.toArray()) + "\n";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Node &&
                id.equals(((Node)obj).getId()) &&
                (isExitNode == (((Node)obj).isExitNode()));
    }

    @Override
    public int hashCode() {
        return id * 13;
    }
}