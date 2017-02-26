//package pt.me.microm.tools.levelloader;
//
//import org.w3c.dom.NodeList;
//import org.xml.sax.SAXException;
//import pt.me.microm.model.AbstractModel;
//import pt.me.microm.model.dev.DebugModel;
//import pt.me.microm.model.stuff.DaBoxModel;
//
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.xpath.XPathConstants;
//import javax.xml.xpath.XPathExpression;
//import javax.xml.xpath.XPathExpressionException;
//import java.io.IOException;
//import java.util.ArrayList;
//
///**
// * Created by Marcio on 21/02/2017.
// */
//public class LevLoader {
//
//
//    public static void LoadLevel(){
//
//    }
//
//
//    public ArrayList<BasicShape> LoadLevel() {
//        int nrElements = 0;
//        ArrayList<BasicShape> modelBag = new ArrayList<BasicShape>();
//
//
//
//
//        try {
//
//
//            XPathExpression expr;
//            // Get Camera
//            if (logger.isInfoEnabled()) logger.info("Camera...");
//            expr = xpath.compile("//svg/g/path[contains(@id,'camera')]");
//            NodeList camera = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
//            for (int i = 0; i < camera.getLength(); i++) {
//                String d = camera.item(i).getAttributes().getNamedItem("d").getNodeValue();
//                String style = camera.item(i).getAttributes().getNamedItem("style").getNodeValue();
//                if (logger.isInfoEnabled()) logger.info("d= " + d + "; style= " + style + ";");
//
//                BasicShape s = new BasicShape(d, style, ObjectType.CAMERA);
//
//                String camera_name = camera.item(i).getAttributes().getNamedItem("id").getNodeValue();
//
//                nrElements+=1;
//            }
//
//
//            // Get Board
//            if (logger.isInfoEnabled()) logger.info("Board...");
//            expr = xpath.compile("//svg/g/path[contains(@id,'board')]");
//            NodeList board = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
//            for (int i = 0; i < board.getLength(); i++) {
//                String d = board.item(i).getAttributes().getNamedItem("d").getNodeValue();
//                String style = board.item(i).getAttributes().getNamedItem("style").getNodeValue();
//                if (logger.isInfoEnabled()) logger.info("d= " + d + "; style= " + style + ";");
//
//                BasicShape s = new BasicShape(d, style, ObjectType.BOARD);
//                String board_name = board.item(i).getAttributes().getNamedItem("id").getNodeValue();
//                addBoardToWorld(modelBag, wm, s, board_name);
//
//                nrElements+=1;
//            }
//
//            // Get DaBox
//            DaBoxModel daBoxRef = null;
//            if (logger.isInfoEnabled()) logger.info("DaBox...");
//            expr = xpath.compile("//svg/g/path[contains(@id,'daBox')]");
//            NodeList dabox = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
//            for (int i = 0; i < dabox.getLength(); i++) {
//                String d = dabox.item(i).getAttributes().getNamedItem("d").getNodeValue();
//                String style = dabox.item(i).getAttributes().getNamedItem("style").getNodeValue();
//                if (logger.isInfoEnabled()) logger.info("d= " + d + "; style= " + style + ";");
//
//                BasicShape s = new BasicShape(d, style, ObjectType.DABOX);
//                String dabox_name = dabox.item(i).getAttributes().getNamedItem("id").getNodeValue();
//                daBoxRef = addDaBoxToWorld(modelBag, wm, s, dabox_name);
//
//                nrElements+=1;
//            }
//
//            // Get Spawn
//            if (logger.isInfoEnabled()) logger.info("Spawn...");
//            expr = xpath.compile("//svg/g/path[contains(@id,'spawn')]");
//            NodeList spawn = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
//            for (int i = 0; i < spawn.getLength(); i++) {
//                String d = spawn.item(i).getAttributes().getNamedItem("d").getNodeValue();
//                String style = spawn.item(i).getAttributes().getNamedItem("style").getNodeValue();
//                if (logger.isInfoEnabled()) logger.info("d= " + d + "; style= " + style + ";");
//
//                BasicShape s = new BasicShape(d, style, ObjectType.SPAWN);
//                String spawn_name = spawn.item(i).getAttributes().getNamedItem("id").getNodeValue();
//                addSpawnToWorld(modelBag, wm, daBoxRef, s, spawn_name);
//
//                nrElements+=1;
//            }
//
//            // Get Goals
//            if (logger.isInfoEnabled()) logger.info("Goals...");
//            expr = xpath.compile("//svg/g/path[contains(@id,'goal')]");
//            NodeList goals = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
//            for (int i = 0; i < goals.getLength(); i++) {
//                String d = goals.item(i).getAttributes().getNamedItem("d").getNodeValue();
//                String style = goals.item(i).getAttributes().getNamedItem("style").getNodeValue();
//                if (logger.isInfoEnabled()) logger.info("d= " + d + "; style= " + style + ";");
//
//                BasicShape s = new BasicShape(d, style, ObjectType.GOAL);
//                String goal_name = goals.item(i).getAttributes().getNamedItem("id").getNodeValue();
//                addGoalToWorld(modelBag, wm, s, goal_name);
//
//                nrElements+=1;
//            }
//
//            // Get Grounds
//            if (logger.isInfoEnabled()) logger.info("Grounds...");
//            expr = xpath.compile("//svg/g/path[contains(@id,'ground')]");
//            NodeList grounds = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
//            for (int i = 0; i < grounds.getLength(); i++) {
//                String d = grounds.item(i).getAttributes().getNamedItem("d").getNodeValue();
//                String style = grounds.item(i).getAttributes().getNamedItem("style").getNodeValue();
//                if (logger.isInfoEnabled()) logger.info("d= " + d + "; style= " + style + ";");
//
//                BasicShape s = new BasicShape(d, style, ObjectType.GROUND);
//                String ground_name = grounds.item(i).getAttributes().getNamedItem("id").getNodeValue();
//                addGroundToWorld(modelBag, wm, s, ground_name);
//
//                nrElements+=1;
//            }
//
//            // Get portals
//            if (logger.isInfoEnabled()) logger.info("Portals...");
//            //expr = xpath.compile("//svg/g/path[contains(@id,'portal')]/@d");
//            expr = xpath.compile("//svg/g/path[contains(@id,'portal')]");
//            NodeList portals = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
//            for (int i = 0; i < portals.getLength(); i++) {
//                String d = portals.item(i).getAttributes().getNamedItem("d").getNodeValue();
//                String style = portals.item(i).getAttributes().getNamedItem("style").getNodeValue();
//                if (logger.isInfoEnabled()) logger.info("d= " + d + "; style= " + style + ";");
//
//                BasicShape s = new BasicShape(d, style, ObjectType.PORTAL);
//                String portal_name = portals.item(i).getAttributes().getNamedItem("id").getNodeValue();
//                addPortalToWorld(modelBag, wm, s, portal_name);
//
//                nrElements+=1;
//            }
//
//            // Get walls
//            if (logger.isInfoEnabled()) logger.info("Walls...");
//            //expr = xpath.compile("//svg/g/path[contains(@id,'portal')]/@d");
//            expr = xpath.compile("//svg/g/path[contains(@id,'wall')]");
//            NodeList walls = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
//            for (int i = 0; i < walls.getLength(); i++) {
//                String d = walls.item(i).getAttributes().getNamedItem("d").getNodeValue();
//                String style = walls.item(i).getAttributes().getNamedItem("style").getNodeValue();
//                if (logger.isInfoEnabled()) logger.info("d= " + d + "; style= " + style + ";");
//
//                BasicShape s = new BasicShape(d, style, ObjectType.WALL);
//                String wall_name = walls.item(i).getAttributes().getNamedItem("id").getNodeValue();
//                addWallToWorld(modelBag, wm, s, wall_name);
//
//                nrElements+=1;
//            }
//
//            // Get stars
//            if (logger.isInfoEnabled()) logger.info("Stars...");
//            //expr = xpath.compile("//svg/g/path[contains(@id,'portal')]/@d");
//            expr = xpath.compile("//svg/g/path[contains(@id,'star')]");
//            NodeList stars = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
//            for (int i = 0; i < stars.getLength(); i++) {
//                String d = stars.item(i).getAttributes().getNamedItem("d").getNodeValue();
//                String style = stars.item(i).getAttributes().getNamedItem("style").getNodeValue();
//                if (logger.isInfoEnabled()) logger.info("d= " + d + "; style= " + style + ";");
//
//                BasicShape s = new BasicShape(d, style, ObjectType.STAR);
//                String star_name = stars.item(i).getAttributes().getNamedItem("id").getNodeValue();
//                addStarToWorld(modelBag, wm, s, star_name);
//
//                nrElements+=1;
//            }
//
//
//            // Get text
//            if (logger.isInfoEnabled()) logger.info("Text...");
//            expr = xpath.compile("//svg/g/text[contains(@id,'text')]/tspan");
//            NodeList text = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
//            for (int i = 0; i < text.getLength(); i++) {
//                String id = text.item(i).getAttributes().getNamedItem("id").getNodeValue();
//                if (logger.isInfoEnabled()) logger.info("[" + id + "]");
//
//                String x = text.item(i).getAttributes().getNamedItem("x").getNodeValue();
//                String y = text.item(i).getAttributes().getNamedItem("y").getNodeValue();
//                String s = text.item(i).getTextContent();
//
//                if (logger.isInfoEnabled()) logger.info("[" + id + "] = x: " + x + "; y: " + y + "; ==> '" + s + "'" );
//                BasicShape sh = new BasicShape("m " + x + "," + y, "", ObjectType.TEXT);
//                if (logger.isInfoEnabled()) logger.info(".:.:.:. " + sh.getCentroid() + " .:.:.:.");
//
//                addTextToWorld(modelBag, wm, sh, id, s);
//
//                nrElements+=1;
//            }
//
//
//            // Get triggers
//            if (logger.isInfoEnabled()) logger.info("Triggers...");
//            expr = xpath.compile("//svg/g/path[contains(@id,'trigger')]");
//            NodeList triggers = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
//            for (int i = 0; i < triggers.getLength(); i++) {
//                String d = triggers.item(i).getAttributes().getNamedItem("d").getNodeValue();
//                String style = triggers.item(i).getAttributes().getNamedItem("style").getNodeValue();
//                if (logger.isInfoEnabled()) logger.info("d= " + d + "; style= " + style + ";");
//
//                BasicShape s = new BasicShape(d, style, ObjectType.TRIGGER);
//                String trigger_name = triggers.item(i).getAttributes().getNamedItem("id").getNodeValue();
//                String script = triggers.item(i).getAttributes().getNamedItem("custom-script").getNodeValue();
//                addTriggerToWorld(modelBag, wm, s, trigger_name, script);
//
//                nrElements+=1;
//            }
//
//            if (logger.isInfoEnabled()) logger.info("Finished Loading level: " + h.name());
//
//        } catch (ParserConfigurationException e) {
//            if (logger.isErrorEnabled()) logger.error(e.getMessage());
//            e.printStackTrace();
//        } catch (SAXException e) {
//            if (logger.isErrorEnabled()) logger.error(e.getMessage());
//            e.printStackTrace();
//        } catch (IOException e) {
//            if (logger.isErrorEnabled()) logger.error(e.getMessage());
//            e.printStackTrace();
//        } catch (XPathExpressionException e) {
//            if (logger.isErrorEnabled()) logger.error(e.getMessage());
//            e.printStackTrace();
//        }
//
//        int check = 0;
//        for (AbstractModel am : modelBag) {
//            if (am instanceof DebugModel) check+=1;
//        }
//        // the (-1) is because the camera loading parameters doesn't create a new model
//        if (logger.isDebugEnabled()) logger.debug("size of bag is {}; # of non debug point models is {}; # of debug points model is {}", modelBag.size(), nrElements-1, check);
//
//        return modelBag;
//    }
//}
