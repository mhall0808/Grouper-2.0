/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BrightspaceManifest;

import DiscussionBoard.DiscussionBoard;
import Group.Group;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Mark Hall
 *
 * Brightspace Manifest - This class creates an imsmanifest.xml file, which is
 * the xml file that links to every other page in the course. This class will
 * also handle the building of different pages by delegating them to the correct
 * classes.
 *
 *
 */
public class BrightspaceManifest {

    private String savePath;
    private String zipName;
    private ArrayList<DiscussionBoard> discussionBoards;
    private ArrayList<String> fileNames;

    /**
     *
     */
    public BrightspaceManifest() {
        savePath = new String();
        discussionBoards = new ArrayList<>();
        fileNames = new ArrayList<>();
    }

    /**
     *
     */
    public void buildManifest() {

        try {
            int itemID = 1;
            int itemIdent = 20000;
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("manifest");

            doc.appendChild(rootElement);

            rootElement.setAttribute("xmlns:d2l_2p0", "http://desire2learn.com/xsd/d2lcp_v2p0");
            rootElement.setAttribute("xmlns:scorm_1p2", "http://www.adlnet.org/xsd/adlcp_rootv1p2");
            rootElement.setAttribute("xmlns", "http://www.imsglobal.org/xsd/imscp_v1p1");

            // Now we create a "Resources" element to house discussion boards
            // and grade book.
            Element resources = doc.createElement("resources");
            rootElement.appendChild(resources);

            // Now we create the link to grades_d2l.xml
            Element grades = doc.createElement("resource");
            grades.setAttribute("title", "Grade Items and Categories");
            grades.setAttribute("href", "grades_d2l.xml");
            grades.setAttribute("d2l_2p0:link_target", "");
            grades.setAttribute("d2l_2p0:material_type", "d2lgrades");
            grades.setAttribute("type", "webcontent");
            grades.setAttribute("identifier", "res_grades");
            resources.appendChild(grades);

            // Next, we actually create the gradebook.  Since we are attaching
            // grade items to topics, we have to take this into account.
            Gradebook gradebook = new Gradebook();
            gradebook.setDiscussionBoards(discussionBoards);
            gradebook.setSavePath(savePath);
            gradebook.createGradebook();

            // Because the gradebook added information, now we need to 
            // copy the groups back over.
            discussionBoards = gradebook.getDiscussionBoards();

            // Creating something to append to the resources.
            String resString = new String();

            // Next, we create discussion boards and topics, while 
            // attaching grade items to each TOPIC.  At the same time, we will
            // create a link to it in the resources area in the manifest
            for (DiscussionBoard board : discussionBoards) {
                board.setBoardID(itemID);
                board.setBoardIdent(itemIdent);
                board.setSavePath(savePath);
                board.buildDBoard();
                itemID = board.getBoardID();
                itemIdent = board.getBoardIdent() + 1;

                Element resource = doc.createElement("resource");
                resource.setAttribute("identifier", "res_discuss_" + board.getSaveIdentity());
                resource.setAttribute("type", "webcontent");
                resource.setAttribute("d2l_2p0:material_type", "d2ldiscussion");
                resource.setAttribute("d2l_2p0:link_target", "");
                resource.setAttribute("href", "discussion_d2l_" + board.getSaveIdentity() + ".xml");
                resources.appendChild(resource);

                fileNames.add(savePath + "\\discussion_d2l_" + board.getSaveIdentity() + ".xml");

            }

            // Lastly, we save the file, then take care of zipping them into
            // folders.
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(savePath) + "\\imsmanifest.xml");
            fileNames.add(savePath + "\\imsmanifest.xml");
            fileNames.add(savePath + "\\grades_d2l.xml");

            // Save the file to the opened file.
            transformer.transform(source, result);

        } catch (ParserConfigurationException ex) {
        } catch (TransformerConfigurationException ex) {
        } catch (TransformerException ex) {
        }
        zipAndDelete();
    }

    /**
     *
     * @return
     */
    public String getSavePath() {
        return savePath;
    }

    /**
     *
     * @param savePath
     */
    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getZipName() {
        return zipName;
    }

    public void setZipName(String zipName) {
        this.zipName = zipName;
    }

    public ArrayList<DiscussionBoard> getDiscussionBoards() {
        return discussionBoards;
    }

    public void setDiscussionBoards(ArrayList<DiscussionBoard> discussionBoards) {
        this.discussionBoards = discussionBoards;
    }

    public ArrayList<String> getFileNames() {
        return fileNames;
    }

    public void setFileNames(ArrayList<String> fileNames) {
        this.fileNames = fileNames;
    }

    public void zipAndDelete() {
        byte[] buffer = new byte[1024];
        try {

            FileOutputStream fos = new FileOutputStream(savePath + "\\" + zipName);
            ZipOutputStream zos = new ZipOutputStream(fos);

            for (String fileName : fileNames) {

                System.out.println("zipping file: " + fileName);
                File srcFile = new File(fileName);
                FileInputStream fis;
                fis = new FileInputStream(srcFile);
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
                fis.close();

                boolean success = (new File(fileName)).delete();
            }
            zos.close();

        } catch (FileNotFoundException ex) {
            System.out.println("File not found!");
        } catch (IOException ex) {
        }
    }

}
