package hcmus.tetris.dao;

import android.content.Context;
import android.util.Xml;
import android.widget.EditText;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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

import hcmus.tetris.dto.Setting;
import hcmus.tetris.ults.DAOHelper;

public class SettingDAO {
    public static SettingDAO instance;
    private XmlPullParser parserSetting;

    public static SettingDAO getInstance(){
        if (instance == null){
            instance = new SettingDAO();
        }
        return instance;
    }

    public Setting getSetting(Context context) {
        Setting setting = null;
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
                parserSetting = Xml.newPullParser();
                parserSetting.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parserSetting.setInput(bufferedReader);
                parserSetting.nextTag();
            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
                parserSetting = null;
            }

            if (parserSetting != null) {
                //parsing process
                try {
                    //read Database tag (root tag) in database
                    parserSetting.require(XmlPullParser.START_TAG, null, "Database");

                    //start reading
                    while (parserSetting.next() != XmlPullParser.END_TAG) {
                        if (parserSetting.getEventType() == XmlPullParser.START_TAG) {
                            //start by looking for Setting tag
                            if (parserSetting.getName().equals("Setting")) {
                                //read Setting tag
                                setting = readSettingTag();
                            } else {
                                //skip other tag
                                DAOHelper.skipParser(parserSetting);
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

        return setting;
    }

    Setting readSettingTag() {
        Setting setting = null;
        try {
            //variables to store Setting's data
            long scoreLimit = -1, lineScore = -1;
            String controlScheme = null;
            ArrayList<ArrayList<Integer>> timeLevels = null;
            int width = -1, height  = -1;

            //start reading Setting tag
            parserSetting.require(XmlPullParser.START_TAG, null, "Setting");
            while (parserSetting.next() != XmlPullParser.END_TAG) {
                if (parserSetting.getEventType() == XmlPullParser.START_TAG) {
                    String childName = parserSetting.getName();
                    switch (childName) {
                        case "ScoreLimit": {
                            parserSetting.require(XmlPullParser.START_TAG, null, "ScoreLimit");
                            scoreLimit = Long.parseLong(parserSetting.getAttributeValue(null, "value"));
                            parserSetting.next();
                            parserSetting.require(XmlPullParser.END_TAG, null, "ScoreLimit");
                            break;
                        }
                        case "LineScore": {
                            parserSetting.require(XmlPullParser.START_TAG, null, "LineScore");
                            lineScore = Long.parseLong(parserSetting.getAttributeValue(null, "value"));
                            parserSetting.next();
                            parserSetting.require(XmlPullParser.END_TAG, null, "LineScore");
                            break;
                        }
                        case "TimeLevel": {
                            parserSetting.require(XmlPullParser.START_TAG, null, "TimeLevel");
                            timeLevels = readTimeLevel();
                            parserSetting.require(XmlPullParser.END_TAG, null, "TimeLevel");
                            break;
                        }
                        case "ControlScheme": {
                            parserSetting.require(XmlPullParser.START_TAG, null, "ControlScheme");
                            controlScheme = parserSetting.getAttributeValue(null, "value");
                            parserSetting.next();
                            parserSetting.require(XmlPullParser.END_TAG, null, "ControlScheme");
                            break;
                        }
                        case "BoardSize": {
                            parserSetting.require(XmlPullParser.START_TAG, null, "BoardSize");
                            width = Integer.parseInt(parserSetting.getAttributeValue(null, "width"));
                            height = Integer.parseInt(parserSetting.getAttributeValue(null, "height"));
                            parserSetting.next();
                            parserSetting.require(XmlPullParser.END_TAG, null, "BoardSize");
                            break;
                        }
                    }
                }
            }
            System.out.println(scoreLimit);
            System.out.println(lineScore);
            System.out.println(controlScheme);
            System.out.println(timeLevels);
            System.out.println(width);
            System.out.println(height);
            if (scoreLimit != -1 && lineScore != -1 && controlScheme != null && timeLevels != null
                && width != -1 && height != -1){
                setting = new Setting(scoreLimit, lineScore, controlScheme, timeLevels, width, height);
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return setting;
    }

    private ArrayList<ArrayList<Integer>> readTimeLevel() {
        ArrayList<ArrayList<Integer>> timeLevel = new ArrayList<>();
        try {
            while (parserSetting.next() != XmlPullParser.END_TAG) {
                if (parserSetting.getEventType() == XmlPullParser.START_TAG) {
                    ArrayList<Integer> level = new ArrayList<>();
                    level.add(Integer.parseInt(parserSetting.getAttributeValue(null, "timestamp")));
                    level.add(Integer.parseInt(parserSetting.getAttributeValue(null, "dropspeed")));
                    timeLevel.add(level);
                    parserSetting.next();
                }
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
            timeLevel = null;
        }
        return timeLevel;
    }

    public int saveSettingToDTB(Context context, ArrayList<String> otherValuesToSave,
                            ArrayList<EditText> timeLevelEditTextList){
        int saveResult = 1;
        DocumentBuilderFactory docBuildFac = DocumentBuilderFactory.newInstance();
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        String database = "database.xml";
        try {
            fileInputStream = context.openFileInput(database);
            try {
                //get DOM from database.xml
                DocumentBuilder builder = docBuildFac.newDocumentBuilder();
                Document doc = builder.parse(fileInputStream);
                Node settingNode = doc.getElementsByTagName("Setting").item(0);
                NodeList settingChildren = settingNode.getChildNodes();
                int numChildren = settingChildren.getLength();
                for (int i = 0; i < numChildren; i++){
                    Node child = settingChildren.item(i);
                    NamedNodeMap att = child.getAttributes();
                    switch (child.getNodeName()) {
                        case "ScoreLimit": {
                            Node value = att.item(0);
                            value.setNodeValue(otherValuesToSave.get(0));
                            break;
                        }
                        case "LineScore": {
                            Node value = att.item(0);
                            value.setNodeValue(otherValuesToSave.get(1));
                            break;
                        }
                        case "TimeLevel": {
                            NodeList levelList = child.getChildNodes();
                            int numLevels = levelList.getLength(), k = 0;
                            for (int j = 0; j < numLevels; j++) {
                                Node level = levelList.item(j);
                                if (level.getNodeName().equals("#text")) continue;
                                NamedNodeMap attLevel = level.getAttributes();
                                Node timestamp = attLevel.item(1);
                                Node dropSpeed = attLevel.item(2);
                                timestamp.setNodeValue(timeLevelEditTextList.get(k++).getText().toString());
                                dropSpeed.setNodeValue(timeLevelEditTextList.get(k++).getText().toString());
                            }
                            break;
                        }
                        case "ControlScheme": {
                            Node value = att.item(0);
                            value.setNodeValue(otherValuesToSave.get(2));
                            break;
                        }
                        case "BoardSize": {
                            Node widthValue = att.item(0);
                            Node heightValue = att.item(1);
                            widthValue.setNodeValue(otherValuesToSave.get(3));
                            heightValue.setNodeValue(otherValuesToSave.get(4));
                            break;
                        }
                    }
                }

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