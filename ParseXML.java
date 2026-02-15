import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class ParseXML {

    // building a document from the XML file

    public Document getDocFromFile(String filename)
            throws ParserConfigurationException {
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = null;
            try {
                doc = db.parse(filename);
            } catch (Exception ex) {
                System.out.println("XML parse failure");
                ex.printStackTrace();
            }
            return doc;
        } // exception handling
    }

    public HashMap<String, Room> readBoardData(Document d) {
        Element root = d.getDocumentElement();

        HashMap<String, Room> rooms = new HashMap<>();
        HashMap<String, ArrayList<String>> adjacentRooms = new HashMap<>();
        NodeList sets = root.getElementsByTagName("set");

        for (int i = 0; i < sets.getLength(); i++) {
            Node roomNode = sets.item(i);
            if (roomNode.getNodeType() == Node.ELEMENT_NODE) {
                Element roomElement = (Element) roomNode;
                String roomName = roomElement.getAttribute("name");
                int shotCounters = roomElement.getElementsByTagName("take").getLength();
                Room room = new Room(roomName, shotCounters, true);
                rooms.put(roomName, room);

                ArrayList<String> adjacentRoomNames = new ArrayList<>();
                adjacentRooms.put(roomName, adjacentRoomNames);

                // getting adjacent rooms

                NodeList children = roomNode.getChildNodes();
                for (int j = 0; j < children.getLength(); j++) {
                    Node neigbhor = children.item(j);
                    if ("neighbors".equals(neigbhor.getNodeName())) {
                        NodeList neighborNodes = neigbhor.getChildNodes();
                        for (int k = 0; k < neighborNodes.getLength(); k++) {
                            Node neighborNode = neighborNodes.item(k);
                            if ("neighbor".equals(neighborNode.getNodeName())) {
                                String adjacentRoomName = neighborNode.getAttributes().getNamedItem("name")
                                        .getNodeValue();
                                adjacentRooms.get(roomName).add(adjacentRoomName);
                            }

                        }

                    } else if ("parts".equals(neigbhor.getNodeName())) {
                        NodeList partNodes = neigbhor.getChildNodes();
                        for (int k = 0; k < partNodes.getLength(); k++) {
                            Node partChildNode = partNodes.item(k);
                            if ("part".equals(partChildNode.getNodeName())) {

                                String roleName = partChildNode.getAttributes().getNamedItem("name").getNodeValue();
                                int requiredRank = Integer
                                        .parseInt(partChildNode.getAttributes().getNamedItem("level").getNodeValue());
                                String line = readLine(partChildNode);
                                Role extra = new Role(roleName, line, requiredRank, false);
                                room.addExtraRole(extra);

                            }

                        }

                    }

                }

            }

        }
         Node trailerNode = root.getElementsByTagName("trailer").item(0);
                if (trailerNode != null && trailerNode.getNodeType() == Node.ELEMENT_NODE) {
                    Room trailer = new Room("trailer", false);

                    rooms.put("trailer", trailer);
                    adjacentRooms.put("trailer", readAdjacentRooms((Element) trailerNode));

                }
                Node castingNode = root.getElementsByTagName("office").item(0);
                if (castingNode != null && castingNode.getNodeType() == Node.ELEMENT_NODE) {
                    Room castingRoom = new Room("office", false);
                    rooms.put("office", castingRoom);

                    adjacentRooms.put("office", readAdjacentRooms((Element) castingNode));

                }
                for (String RoomName : adjacentRooms.keySet()) {
                    Room currentRoom = rooms.get(RoomName);
                    ArrayList<String> adjacentNames = adjacentRooms.get(RoomName);
                    if(currentRoom!=null && adjacentNames!=null){
                    for (String adjacentRoomName : adjacentNames) {
                        Room neighborRoom = rooms.get(adjacentRoomName);
                        if (neighborRoom != null) {
                            currentRoom.addAdjacentRooms(neighborRoom);
                        }
                    }
                }}
                return rooms;


    }

    private ArrayList<String> readAdjacentRooms(Element roomElement) {
        ArrayList<String> adjacentNames = new ArrayList<>();
        NodeList adjacentNodes = roomElement.getElementsByTagName("neighbor");
        for (int j = 0; j < adjacentNodes.getLength(); j++) {
            Node adjacentNode = adjacentNodes.item(j);
            if (adjacentNode.getNodeType() == Node.ELEMENT_NODE) {
                String adjacentRoomName = adjacentNode.getAttributes().getNamedItem("name").getNodeValue();
                adjacentNames.add(adjacentRoomName);
            }
        }
        return adjacentNames;

    }

    private String readLine(Node roleNode) {

        Element roleElement = (Element) roleNode;
        if (roleElement.getElementsByTagName("line").getLength() > 0) {
            return roleElement.getElementsByTagName("line").item(0).getTextContent();
        }

        return "";
    }

    public ArrayList<Scene> readCardData(Document d) {
        ArrayList<Scene> deck = new ArrayList<>();
        Element root = d.getDocumentElement();
        NodeList cards = root.getElementsByTagName("card");
        for (int i = 0; i < cards.getLength(); i++) {
            Node cardNode = cards.item(i);
            if (cardNode.getNodeType() == Node.ELEMENT_NODE) {
                Element cardElement = (Element) cardNode;
                String sceneName = cardElement.getAttribute("name");

                String sceneDescription = cardElement.getElementsByTagName("scene").item(0).getTextContent();
                int movieBudget = Integer.parseInt(cardElement.getAttribute("budget"));
                ArrayList<Role> starRoles = new ArrayList<>();
                NodeList partNode = cardElement.getElementsByTagName("part");
                for (int j = 0; j < partNode.getLength(); j++) {
                    Node partNodes = partNode.item(j);

                    if (partNodes.getNodeType() == Node.ELEMENT_NODE) {
                        Element partElement = (Element) partNodes;
                        String roleName = partElement.getAttribute("name");
                        int requiredRank = Integer.parseInt(partElement.getAttribute("level"));
                        String line = readLine(partNodes);
                        Role starRole = new Role(roleName, line, requiredRank, true);
                        starRoles.add(starRole);
                    }
                }
                Scene sceneCard = new Scene(sceneName, sceneDescription, movieBudget, starRoles);
                deck.add(sceneCard);
            }

        }
        return deck;

    }
}
