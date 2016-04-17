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
                graph.get(node1.getId()).addLinkTo(node2);
            } else {
                graph.put(node2.getId(), node2);
            }

        }

        // Set exit nodes into graph
        for (int i = 0; i < exits; i++) {
            int nodeId = in.nextInt();
            graph.get(nodeId).setExitNode(true);
        }
        System.err.println("graph..." + graph);

        // game loop
        while (true) {
            Node skyNetNode = graph.get(in.nextInt()); // The index of the node on which the Skynet agent is positioned this turn
            System.err.println("agent..." + skyNetNode.getId() + " " + skyNetNode.getLinksTo());
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
    private boolean isExisNode;

    public Node(int id) {
        this.id = id;
        this.linksTo = new HashSet<>();
        this.isExisNode = false;
    }

    public boolean isExitNode() {
        return isExisNode;
    }

    public void setExitNode(boolean exisNode) {
        isExisNode = exisNode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<Node> getLinksTo() {
        return linksTo;
    }

    public void setLinksTo(Set<Node> linksTo) {
        this.linksTo = linksTo;
    }

    public void addLinkTo(Node node) {
        linksTo.add(node);
    }

    @Override
    public String toString() {
        List<Integer> linkToIds = new ArrayList<>(linksTo.size());
        for (Node node : linksTo) {
            linkToIds.add(node.getId());
        }
        return "id: " + id + " isExit: " + isExitNode() + " linksTo: " + Arrays.toString(linkToIds.toArray()) + "\n";
    }
}