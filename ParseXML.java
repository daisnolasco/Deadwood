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
                System.out.println("XML parse failure" + filename);
                ex.printStackTrace();
            }
            return doc;
        } // exception handling
    }

    public HashMap<String, Room> readBoardData(Document d) {
        Element root = d.getDocumentElement();

        HashMap<String, Room> rooms = new HashMap<>();
        HashMap<String, ArrayList<String>> adjacentMapping = new HashMap<>();
        NodeList sets = root.getElementsByTagName("set");

        for (int i = 0; i < sets.getLength(); i++) {
            Node roomNode = sets.item(i);
            if (roomNode.getNodeType() == Node.ELEMENT_NODE) {
                Element roomElement = (Element) roomNode;
                String roomName = roomElement.getAttribute("name");
                int shotCounters = roomElement.getElementsByTagName("take").getLength();
                Room room = new Room(roomName, shotCounters, true);
                rooms.put(roomName, room);

                ;
                adjacentMapping.put(roomName, new ArrayList<>());

                // getting adjacent rooms

                NodeList children = roomNode.getChildNodes();
                for (int j = 0; j < children.getLength(); j++) {
                    Node child = children.item(j);
                    if ("neighbors".equals(child.getNodeName())) {
                        NodeList neighborNodes = child.getChildNodes();
                        for (int k = 0; k < neighborNodes.getLength(); k++) {
                            Node neighborNode = neighborNodes.item(k);
                            if ("neighbor".equals(neighborNode.getNodeName())) {
                                String adjacentRoomName = neighborNode.getAttributes().getNamedItem("name")
                                        .getNodeValue();
                                adjacentMapping.get(roomName).add(adjacentRoomName);
                            }

                        }

                    } else if ("parts".equals(child.getNodeName())) {
                        NodeList partNodes = child.getChildNodes();
                        for (int k = 0; k < partNodes.getLength(); k++) {
                            Node partNode = partNodes.item(k);
                            if ("part".equals(partNode.getNodeName())) {
                                String roleName = partNode.getAttributes().getNamedItem("name").getNodeValue();
                                int requiredRank = Integer
                                        .parseInt(partNode.getAttributes().getNamedItem("level").getNodeValue());
                                String line = readLine(partNode);
                                room.addExtraRole(new Role(roleName, line, requiredRank, false));

                            }

                        }

                    }

                }

            }

        } // parsing trailer
        Node trailerNode = root.getElementsByTagName("trailer").item(0);
        if (trailerNode != null && trailerNode.getNodeType() == Node.ELEMENT_NODE) {
            Room trailer = new Room("trailer", false);
            rooms.put("trailer", trailer);
            adjacentMapping.put("trailer", readAdjacentRooms((Element) trailerNode));

        }
        // parsing casting office
        Node castingNode = root.getElementsByTagName("office").item(0);
        if (castingNode != null && castingNode.getNodeType() == Node.ELEMENT_NODE) {
            Room castingRoom = new Room("office", false);
            rooms.put("office", castingRoom);

            adjacentMapping.put("office", readAdjacentRooms((Element) castingNode));

        }
        for (String RoomName : adjacentMapping.keySet()) {
            Room currentRoom = rooms.get(RoomName);
            ArrayList<String> adjacentNames = adjacentMapping.get(RoomName);
            if (currentRoom != null && adjacentNames != null) {
                for (String adjacentRoomName : adjacentNames) {
                    Room neighborRoom = rooms.get(adjacentRoomName);
                    if (neighborRoom != null) {
                        currentRoom.addAdjacentRooms(neighborRoom);
                    }
                }
            }
        }
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
        NodeList lineNodes = roleElement.getElementsByTagName("line");
        if (lineNodes.getLength() > 0) {
            return lineNodes.item(0).getTextContent();
        }

        return "";
    }

    public ArrayList<Scene> readCardData(Document x) {
        ArrayList<Scene> deck = new ArrayList<>();
        Element root = x.getDocumentElement();
        NodeList cards = root.getElementsByTagName("card");
        for (int i = 0; i < cards.getLength(); i++) {
            Node cardNode = cards.item(i);
            if (cardNode.getNodeType() == Node.ELEMENT_NODE) {
                Element cardElement = (Element) cardNode;
                String sceneName = cardElement.getAttribute("name");

                String sceneDescription = "";
                int sceneNumber = 0;
                int movieBudget = Integer.parseInt(cardElement.getAttribute("budget"));
                NodeList sceneNode = cardElement.getElementsByTagName("scene");
                if (sceneNode.getLength() > 0) {
                    Element sceneElement = (Element) sceneNode.item(0);
                    sceneDescription = sceneElement.getTextContent();
                    sceneNumber = Integer.parseInt(sceneElement.getAttribute("number"));
                    ArrayList<Role> starRoles = new ArrayList<>();
                    NodeList partNodes = cardElement.getElementsByTagName("part");
                    for (int j = 0; j < partNodes.getLength(); j++) {
                        Node partNode = partNodes.item(j);

                        if (partNode.getNodeType() == Node.ELEMENT_NODE) {

                            String roleName = partNode.getAttributes().getNamedItem("name").getNodeValue();
                            int requiredRank = Integer
                                    .parseInt(partNode.getAttributes().getNamedItem("level").getNodeValue());
                            String line = readLine(partNode);
                            Role starRole = new Role(roleName, line, requiredRank, true);
                            starRoles.add(starRole);
                        }
                    }
                    Scene sceneCard = new Scene(sceneName, sceneDescription, movieBudget, sceneNumber, starRoles);
                    deck.add(sceneCard);
                }

            }
           

        }
         return deck;
    }
}