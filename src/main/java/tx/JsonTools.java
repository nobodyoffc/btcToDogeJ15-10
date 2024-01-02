package tx;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class JsonTools {

    private static void sort(JsonElement e) {
        if (e.isJsonNull() || e.isJsonPrimitive()) {
            return;
        }

        if (e.isJsonArray()) {
            JsonArray a = e.getAsJsonArray();
            Iterator<JsonElement> it = a.iterator();
            it.forEachRemaining(i -> sort(i));
            return;
        }

        if (e.isJsonObject()) {
            Map<String, JsonElement> tm = new TreeMap<>(getComparator());
            for (Map.Entry<String, JsonElement> en : e.getAsJsonObject().entrySet()) {
                tm.put(en.getKey(), en.getValue());
            }

            String key;
            JsonElement val;
            for (Map.Entry<String, JsonElement> en : tm.entrySet()) {
                key = en.getKey();
                val = en.getValue();
                e.getAsJsonObject().remove(key);
                e.getAsJsonObject().add(key, val);
                sort(val);
            }
        }
    }
    public static <T,E> Type getMapType(Class<T> t, Class<E> e) {
        return new TypeToken<Map<T, E>>() {}.getType();
    }

    public static String getNiceString(Object ob) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(ob);
    }

    public static String getString(Object ob) {
        return new Gson().toJson(ob);
    }

    public static void  gsonPrint(Object ob) {

        System.out.println("----------\n" + ob.getClass().toString() + ": " + getNiceString(ob) + "\n----------");
    }

    public static String strToJson(String rawStr) {
        if(!rawStr.contains("{")) return null;
        if(!rawStr.contains("}")) return null;
        int begin = rawStr.indexOf("{");
        int end = rawStr.lastIndexOf("}");
        rawStr = rawStr.substring(begin,end+1);
        return rawStr.replaceAll("[\r\n\t]", "");
    }

    private static Comparator<String> getComparator() {
        return (s1, s2) -> s1.compareTo(s2);
    }

    public static <T> T readObjectFromJsonFile(String filePath, String fileName, Class<T> tClass) throws IOException {
        File file = new File(filePath,fileName);
        if(!file.exists()||file.length()==0)return null;
        Gson gson = new Gson();
        FileInputStream fis = new FileInputStream(file);
        byte[] configJsonBytes = new byte[fis.available()];
        fis.read(configJsonBytes);

        String configJson = new String(configJsonBytes);
        T t = gson.fromJson(configJson, tClass);
        fis.close();
        return t;
    }

    public static  <T> void writeObjectToJsonFile(T obj, String fileName, boolean append) {
        Gson gson = new Gson();
        try (Writer writer = new FileWriter(fileName,append)) {
            gson.toJson(obj, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static  <T> void appendObjectsToJsonFile(List<T> objList, String fileName, boolean append) {
        Gson gson = new Gson();
        try (Writer writer = new FileWriter(fileName,append)) {
            objList.forEach(t -> gson.toJson(t, writer));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> T readObjectFromJsonFile(FileInputStream fis, Class<T> tClass) throws IOException {
        Gson gson = new Gson();
        T t;
        byte[] jsonBytes;
        ArrayList<Integer> jsonByteList = new ArrayList<>();

        int tip = 0;

        boolean counting = false;
        boolean ignore;
        int b;
        while(true){
            b = fis.read();
            if(b<0)return null;

            ignore = (char) b == '\\';

            if(ignore){
                jsonByteList.add(b);
                continue;
            }

            if((char)b == '{'){
                counting = true;
                tip++;
            }else {
                if ((char) b == '}'&&counting) tip--;
            }

            jsonByteList.add(b);

            if(counting && tip==0){
                jsonBytes = new byte[jsonByteList.size()];
                int i=0;
                for(int b1 : jsonByteList){
                    jsonBytes[i]= (byte) b1;
                    i++;
                }
                counting=false;
                break;
            }
        }
        if(counting)return null;

        try {
            String json = new String(jsonBytes, StandardCharsets.UTF_8);
            t = gson.fromJson(json, tClass);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return t;
    }

    public static String removeEscapes(String input) {
        StringBuilder result = new StringBuilder();
        boolean escape = false;

        for (char c : input.toCharArray()) {
            if (escape) {
                switch (c) {
                    case 'n' -> result.append('\n');
                    case 't' -> result.append('\t');
                    case 'r' -> result.append('\r');
                    case 'b' -> result.append('\b');
                    case 'f' -> result.append('\f');
                    case '\\' -> result.append('\\');
                    default -> result.append(c);
                }
                escape = false;
            } else {
                if (c == '\\') {
                    escape = true;
                } else {
                    result.append(c);
                }
            }
        }

        return result.toString();
    }



    public static  <T> T readJsonObject(FileInputStream fis, Class<T> clazz) throws IOException {
        Gson gson = new Gson();
        try (JsonReader jsonReader = new JsonReader(new InputStreamReader(fis))) {
            jsonReader.beginObject();
            T object = gson.fromJson(jsonReader, clazz);
            jsonReader.endObject();
            return object;
        }
    }

    public static <T> List<T> readJsonObjects(String fileName, Class<T> clazz) {
        List<T> objects = new ArrayList<>();
        Gson gson = new Gson();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            StringBuilder jsonBuilder = new StringBuilder();
            int braceCount = 0;
            int ch;

            while ((ch = reader.read()) != -1) {
                char c = (char) ch;
                jsonBuilder.append(c);

                if (c == '{') {
                    braceCount++;
                } else if (c == '}') {
                    braceCount--;

                    if (braceCount == 0) {
                        T obj = gson.fromJson(jsonBuilder.toString(), clazz);
                        objects.add(obj);
                        jsonBuilder = new StringBuilder();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return objects;
    }
}

