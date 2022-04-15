package hcmus.tetris.dao;

import android.content.Context;
import android.util.Xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import hcmus.tetris.dto.HighScore;
import hcmus.tetris.ults.DAOHelper;

public class HighScoreDAO {
    public static HighScoreDAO instance;
    private XmlPullParser parserHighScore;

    public static HighScoreDAO getInstance(){
        if (instance == null){
            instance = new HighScoreDAO();
        }
        return instance;
    }

    public ArrayList<HighScore> getHighScores(Context context){
        ArrayList<HighScore> highScores = null;
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
                parserHighScore = Xml.newPullParser();
                parserHighScore.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parserHighScore.setInput(bufferedReader);
                parserHighScore.nextTag();
            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
                parserHighScore = null;
            }

            if (parserHighScore != null) {
                //parsing process
                try {
                    //read Database tag (root tag) in database
                    parserHighScore.require(XmlPullParser.START_TAG, null, "Database");

                    //start reading
                    while (parserHighScore.next() != XmlPullParser.END_TAG) {
                        if (parserHighScore.getEventType() == XmlPullParser.START_TAG) {
                            //start by looking for Setting tag
                            if (parserHighScore.getName().equals("HighScore")) {
                                //read Setting tag
                                highScores = readHighScoreTag();
                            } else {
                                //skip other tag
                                DAOHelper.skipParser(parserHighScore);
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

        return highScores;
    }

    private ArrayList<HighScore> readHighScoreTag(){
        ArrayList<HighScore> highScores = new ArrayList<>();
        try {
            //variables to store Setting's data
            long value;
            String timeStamp, player;

            //start reading Setting tag
            parserHighScore.require(XmlPullParser.START_TAG, null, "HighScore");
            while (parserHighScore.next() != XmlPullParser.END_TAG) {
                if (parserHighScore.getEventType() == XmlPullParser.START_TAG) {
                    String childName = parserHighScore.getName();
                    parserHighScore.require(XmlPullParser.START_TAG, null, "Score");
                    value = Long.parseLong(parserHighScore.getAttributeValue(null, "value"));
                    timeStamp = parserHighScore.getAttributeValue(null, "timestamp");
                    player = parserHighScore.getAttributeValue(null, "player");
                    parserHighScore.next();
                    parserHighScore.require(XmlPullParser.END_TAG, null, "Score");
                    highScores.add(new HighScore(player, timeStamp, value));
                }
            }

            int length = highScores.size();
            for (int i = 0; i < length; i++){
                System.out.println(highScores.get(i).getScore());
                System.out.println(highScores.get(i).getName());
                System.out.println(highScores.get(i).getDateTime());
                System.out.println();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return highScores;
    }

    public int saveHighScoreToDTB(Context context, HighScore newHighScore){
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
                Node highScoreNode = doc.getElementsByTagName("HighScore").item(0);

                //add new <Score> to <HighScore>
                highScoreNode.appendChild(doc.createTextNode("\n"));
                Element newHighScoreNode = doc.createElement("Score");
                newHighScoreNode.setAttribute("value", Long.toString(newHighScore.getScore()));
                newHighScoreNode.setAttribute("timestamp", newHighScore.getDateTime());
                newHighScoreNode.setAttribute("player", newHighScore.getName());
                highScoreNode.appendChild(newHighScoreNode);

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
