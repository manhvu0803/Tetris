package hcmus.tetris.ults;

import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import hcmus.tetris.R;

public class DAOHelper {
    static public void createDatabaseIfNotExists(Context context){
        try {
            File file = new File(context.getFilesDir(), "database.xml");
            if (file.exists()){
                if (file.length() == 0){
                    System.out.println("database.xml is empty. New one will be created.");
                    createDatabase(context);
                }
            }
            else{
                throw new FileNotFoundException();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("database.xml isn't existed yet. New one will be created");
            createDatabase(context);
        }
    }

    static void createDatabase(Context context){
        System.out.println("Creating new database.xml at " + context.getFilesDir());
        File databaseFile = new File(context.getFilesDir(), "database.xml");
        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.database);
            FileOutputStream fileOutputStream = new FileOutputStream(databaseFile);

            byte[] buff = new byte[1024];
            int length;
            while ((length = inputStream.read(buff)) > 0){
                fileOutputStream.write(buff, 0, length);
            }

            fileOutputStream.close();
            inputStream.close();
            System.out.println("Create database.xml successfully");
        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }

    static public void skipParser(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser != null){
            if (parser.getEventType() != XmlPullParser.START_TAG){
                throw new IllegalStateException();
            }
            int depth = 1;
            while (depth != 0) {
                switch (parser.next()){
                    case (XmlPullParser.END_TAG): {
                        depth--;
                        break;
                    }
                    case (XmlPullParser.START_TAG): {
                        depth++;
                        break;
                    }
                }
            }
        }
    }

    static public String formatDateTime(LocalDateTime dateTime){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(dateTimeFormatter);
    }

    static public String twoDArrayIntToString(int[][] twoDArrayInt) {
        StringBuilder stringBuilder = new StringBuilder();
        if (twoDArrayInt != null) {
            int rowNum = twoDArrayInt.length;
            if (rowNum > 0) {
                int colNum = twoDArrayInt[0].length;
                for (int row = 0; row < rowNum; row++) {
                    for (int col = 0; col < colNum; col++) {
                        stringBuilder.append(twoDArrayInt[row][col]);
                        if (col < (colNum - 1)) {
                            stringBuilder.append("-");
                        }
                    }
                    if (row < (rowNum - 1)) {
                        stringBuilder.append("_");
                    }
                }
            }
        }
        return stringBuilder.toString();
    }

    static public int[][] stringTo2DArray(String string) {
        if (string != null) {
            String[] rows = string.split("_");
            int rowNum = rows.length;
            if (rowNum > 0) {
                int[][] twoDArrayInt = new int[rowNum][];
                for (int row = 0; row < rowNum; row++) {
                    String[] cols = rows[row].split("-");
                    int colNum = cols.length;
                    int[] arrayInt = new int[colNum];
                    for (int col = 0; col < colNum; col++) {
                        arrayInt[col] = Integer.parseInt(cols[col]);
                    }
                    twoDArrayInt[row] = arrayInt;
                }
                return twoDArrayInt;
            }
        }
        return null;
    }
}
