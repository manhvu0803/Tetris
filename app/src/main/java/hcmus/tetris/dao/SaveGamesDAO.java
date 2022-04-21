package hcmus.tetris.dao;

import android.content.Context;
import android.util.Xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import hcmus.tetris.dto.SaveGame;
import hcmus.tetris.ults.DAOHelper;

public class SaveGamesDAO {
    public static SaveGamesDAO instance;
    private XmlPullParser parserSaveGame;

    public static SaveGamesDAO getInstance(){
        if (instance == null){
            instance = new SaveGamesDAO();
        }
        return instance;
    }

    public ArrayList<SaveGame> getSaveGames(Context context){
        ArrayList<SaveGame> saveGames = null;
        BufferedReader bufferedReader;
        try {
            String fileName = context.getFilesDir() + "/" + "database.xml";
            bufferedReader = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            bufferedReader = null;
        }

        if (bufferedReader != null){
            //prepare parser to parse database.xml
            try {
                parserSaveGame = Xml.newPullParser();
                parserSaveGame.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parserSaveGame.setInput(bufferedReader);
                parserSaveGame.nextTag();
            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
                parserSaveGame = null;
            }

            if (parserSaveGame != null) {
                //parsing process
                try {
                    //read Database tag (root tag) in database
                    parserSaveGame.require(XmlPullParser.START_TAG, null, "Database");

                    //start reading
                    while (parserSaveGame.next() != XmlPullParser.END_TAG) {
                        if (parserSaveGame.getEventType() == XmlPullParser.START_TAG) {
                            //start by looking for Setting tag
                            if (parserSaveGame.getName().equals("SaveGames")) {
                                //read Setting tag
                                saveGames = readSaveGamesTag();
                            } else {
                                //skip other tag
                                DAOHelper.skipParser(parserSaveGame);
                            }
                        }
                    }
                } catch (XmlPullParserException | IOException e) {
                    e.printStackTrace();
                }
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return saveGames;
    }

    private ArrayList<SaveGame> readSaveGamesTag(){
        ArrayList<SaveGame> saveGames = new ArrayList<>();
        try {
            //variables to store Setting's data
            String timeStamp, otherContent;
            long score;

            //start reading Setting tag
            parserSaveGame.require(XmlPullParser.START_TAG, null, "SaveGames");
            while (parserSaveGame.next() != XmlPullParser.END_TAG) {
                if (parserSaveGame.getEventType() == XmlPullParser.START_TAG) {
                    parserSaveGame.require(XmlPullParser.START_TAG, null, "Game");
                    timeStamp = parserSaveGame.getAttributeValue(null, "timestamp");
                    score = Long.parseLong(parserSaveGame.getAttributeValue(null, "score"));
                    otherContent = "";
                    while (parserSaveGame.next() == XmlPullParser.TEXT){
                        otherContent += parserSaveGame.getText().toString();
                    }
                    parserSaveGame.require(XmlPullParser.END_TAG, null, "Game");
                    saveGames.add(new SaveGame(timeStamp, score, otherContent));
                }
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return saveGames;
    }

    public int saveSaveGameToDTB(Context context, SaveGame newSaveGame){
        int saveResult = 1;
        DocumentBuilderFactory docBuildFac = DocumentBuilderFactory.newInstance();
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        String database = "database.xml";
        try {
            fileInputStream = context.openFileInput(database);
            try {
                //get DOM from database.xml and then get <HighScore> tag
                DocumentBuilder builder = docBuildFac.newDocumentBuilder();
                Document doc = builder.parse(fileInputStream);
                Node saveGamesNode = doc.getElementsByTagName("SaveGames").item(0);

                //add new <Score> to <HighScore>
                saveGamesNode.appendChild(doc.createTextNode("\n"));
                Element newSaveGameNode = doc.createElement("Game");
                newSaveGameNode.setAttribute("timestamp", newSaveGame.getDateTime());
                newSaveGameNode.setAttribute("score", newSaveGame.getScore().toString());
                newSaveGameNode.appendChild(doc.createTextNode(newSaveGame.getOtherContent()));
                saveGamesNode.appendChild(newSaveGameNode);

                //save DOM to database.xml
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource domSource = new DOMSource(doc);
                fileOutputStream = context.openFileOutput(database, Context.MODE_PRIVATE);
                StreamResult result = new StreamResult(fileOutputStream);
                transformer.transform(domSource, result);
            } catch (ParserConfigurationException | IOException | SAXException | TransformerException e) {
                e.printStackTrace();
                saveResult = -2;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            saveResult = -1;
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (fileOutputStream != null){
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                saveResult = 0;
            }
        }
        return saveResult;
    }
}
