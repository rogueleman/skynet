import java.util.*;

class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        int nodes = in.nextInt(); // the total number of nodes in the level, including the gateways
        int links = in.nextInt(); // the number of links
        int exits = in.nextInt(); // the number of exit gateways


        Map<Integer, Node> graph = new HashMap<>(nodes);
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
        List<Node> exitList = new ArrayList<>();
        for (int i = 0; i < exits; i++) {
            int nodeId = in.nextInt();
            Node exitNode = graph.get(nodeId);
            Node tempNode = new Node(exitNode.getId(), exitNode.getLinksTo());

            exitNode.setExitNode(true);

            for (Map.Entry<Integer, Node> integerNodeEntry : graph.entrySet()) {
                Node value = integerNodeEntry.getValue();
                value.setImportance(value.getLinksTo().size());

                if (value.getLinksTo().remove(tempNode)) {
                    value.getLinksTo().add(exitNode);
                }

            }
            exitList.add(exitNode);
        }
        // game loop
        Set<Link> agentPassedLinks = new HashSet<>();
        Integer agentCurrentNode = -1;
        Integer agentBeforeNode = -1;
        while (true) {
            Node skyNetNode = graph.get(in.nextInt()); // The index of the node on which the Skynet agent is positioned this turn
            boolean addToSet = true;

            if (agentBeforeNode != agentCurrentNode) {
                agentBeforeNode = agentCurrentNode;
                agentCurrentNode = skyNetNode.getId();
                if (agentCurrentNode < agentBeforeNode) {
                    Integer tmp = agentCurrentNode;
                    agentCurrentNode = agentBeforeNode;
                    agentBeforeNode = tmp;
                }
                addToSet = agentPassedLinks.add(new Link(agentBeforeNode, agentCurrentNode));
            }

            System.err.println("agentPassedLinks..." + agentPassedLinks);

            if (!addToSet) {
                ArrayList<Link> linksList = new ArrayList<>(agentPassedLinks);
                int i = linksList.indexOf(new Link(agentBeforeNode, agentCurrentNode));
                Link nextLink = linksList.get(3);
                System.out.println(nextLink.getLeft() + " " + nextLink.getRight());
            }
            agentCurrentNode = skyNetNode.getId();

            Integer exitNodeIdFromAgentNode = getExitNodeFromAgentNode(skyNetNode);
            Integer[] ids = deleteLinkBetweenTwoNodesLinkedToExit(exitList, graph);
            if (exitNodeIdFromAgentNode == -1) {
                if (ids == null) {
                    Integer id = skyNetNode.getLinksTo().iterator().next().getId();
                    System.out.println(skyNetNode.getId() + " " + id);
                } else {
                    System.out.println(ids[0] + " " + ids[1]);
                }
            } else if (exitNodeIdFromAgentNode != -1) {
                System.out.println(skyNetNode.getId() + " " + exitNodeIdFromAgentNode);
            } else {
                System.out.println(ids[0] + " " + ids[1]);
            }
        }
    }

    private static Integer getExitNodeFromAgentNode(Node skyNetNode) {
        for (Node node : skyNetNode.getLinksTo()) {
            if (node.isExitNode()) {
                return node.getId();
            }
        }
        return -1;
    }

    private static Integer[] deleteLinkBetweenTwoNodesLinkedToExit(List<Node> exitList, Map<Integer, Node> graph) {
        // if the node where is the agent is not directly linked to exit, so we can remove a link of two nodes of importance 3
        for (Node node : exitList) {

            if (node.getLinksTo().size() < 3) {
                return null;
            }

            Iterator<Node> it = graph.get(node.getId()).getLinksTo().iterator();
            while (it.hasNext()) {
                Node nodeOne = graph.get(it.next().getId());
                if (nodeOne.getImportance() == 3) {
                    //Integer[] ids = {nodeOne.getId(), graph.get(nodeOne.getLinksTo().iterator().next().getId()).getId()};
                    Integer[] ids = {nodeOne.getId(), nodeOne.getId()-1};
                    return ids;
                }
            }
        }
        return null;
    }
}

class Node {
    private Integer id;
    private Set<Node> linksTo;
    private boolean isExitNode;
    private Integer importance;

    public Node(int id) {
        this.id = id;
        this.linksTo = new HashSet<>();
        this.isExitNode = false;
        this.importance = 0;
    }

    public Node(int id, Set<Node> linksTo) {
        this.id = id;
        this.linksTo = linksTo;
        this.isExitNode = false;
        this.importance = 0;
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

    public Integer getImportance() {
        return importance;
    }

    public void setImportance(Integer importance) {
        this.importance = importance;
    }

    @Override
    public String toString() {
        List<String> linkToIds = new ArrayList<>(linksTo.size());
        for (Node node : linksTo) {
            linkToIds.add(node.getId() + " " + node.isExitNode());
        }
        return "id: " + id + " isExit: " + isExitNode() + " linksTo: " + Arrays.toString(linkToIds.toArray()) + " importance: " + importance + "\n";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Node &&
                id.equals(((Node) obj).getId()) &&
                (isExitNode == (((Node) obj).isExitNode()));
    }

    @Override
    public int hashCode() {
        return id * 13;
    }
}

class Link {

    private final Integer left;
    private final Integer right;

    public Link(Integer left, Integer right) {
        this.left = left;
        this.right = right;
    }

    public Integer getLeft() {
        return left;
    }

    public Integer getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "left: " + left + " right: " + right + "\n";
    }

    @Override
    public int hashCode() { return left.hashCode() ^ right.hashCode(); }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Link)) return false;
        Link link = (Link) o;
        return this.left.equals(link.left) &&
                this.right.equals(link.right);
    }
}