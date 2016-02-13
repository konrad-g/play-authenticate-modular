package elements.i18n;

import org.apache.commons.codec.Charsets;
import play.Logger;
import play.api.Configuration;
import play.api.Environment;
import play.api.i18n.DefaultMessagesApi;
import play.api.i18n.Langs;
import scala.Predef;
import scala.Tuple2;
import scala.collection.JavaConverters;
import scala.collection.immutable.Map;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;

/**
 * Created by Konrad Gadzinowski<kgadzinowski@gmail.com> on 06/01/16.
 */
@Singleton
public class ModularMessagesApi extends DefaultMessagesApi {

    private static Environment environment;

    private final String BASE_I18N_FILENAME = "-messages";

    @Inject
    public ModularMessagesApi(Environment environment, Configuration configuration, Langs langs) {
        super(ModularMessagesApi.environment = environment, configuration, langs);
    }

    @Override
    public Map<String, Map<String, String>> loadAllMessages() {

        // Load default messages
        Map<String, Map<String, String>> map = super.loadAllMessages();

        // Convert results to Java map
        java.util.Map<String, Map<String, String>> javaMap = JavaConverters.mapAsJavaMapConverter(map).asJava();
        Set<java.util.Map.Entry<String, Map<String, String>>> javaMapSet = javaMap.entrySet();
        Iterator<java.util.Map.Entry<String, Map<String, String>>> javaMapIterator = javaMapSet.iterator();

        java.util.Map<String, Map<String, String>> javaFinalMap = new LinkedHashMap<>();

        // Go through all languages
        while(javaMapIterator.hasNext()) {
            java.util.Map.Entry<String, Map<String, String>> entry = javaMapIterator.next();

            // Get current dictionaries values for each language
            String lang = entry.getKey();
            java.util.Map<String, String> dictImmutable = JavaConverters.mapAsJavaMapConverter(entry.getValue()).asJava();
            java.util.Map<String, String> dict = new LinkedHashMap<>();
            dict.putAll(dictImmutable);

            // Add custom dictionaries from files
            Set<String> messagesFileNames = I18NModule.getI18nFiles();
            Iterator<String> messagesFileNamesIter = messagesFileNames.iterator();
            while(messagesFileNamesIter.hasNext()) {

                // Get correct language gile
                String fileName = messagesFileNamesIter.next();
                fileName = getFileNameForLanguage(lang, fileName);

                // Import language file
                Map<String, String> moduleDictScala = loadMessages(fileName);
                java.util.Map<String, String> moduleDict = JavaConverters.mapAsJavaMapConverter(moduleDictScala).asJava();
                dict.putAll(moduleDict);
            }

            // Put back data
            Map<String, String> dictScala = toScalaMap(dict);
            javaFinalMap.put(lang, dictScala);
        }

        // Convert map to expected type
        map = toScalaMap(javaFinalMap);
        return map;
    }

    private String getFileNameForLanguage(String lang, String baseFileName) {
        String fileName = baseFileName;

        // Check for default language
        if("default".equals(lang) ||
                "default.play".equals(lang) ||
                I18NModule.getDefaultLangCode().equals(lang)) {
            return fileName;
        }

        // Every other language
        fileName += "." + lang;

        return fileName;
    }

    private <A, B> Map<A, B> toScalaMap(java.util.Map<A, B> m) {
        return JavaConverters.mapAsScalaMapConverter(m).asScala().toMap(
                Predef.<Tuple2<A, B>>conforms()
        );
    }
}
