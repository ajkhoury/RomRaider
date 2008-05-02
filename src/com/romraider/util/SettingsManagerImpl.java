package com.romraider.util;

import com.romraider.Settings;
import static com.romraider.Version.VERSION;
import com.romraider.swing.JProgressPane;
import com.romraider.xml.DOMSettingsBuilder;
import com.romraider.xml.DOMSettingsUnmarshaller;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public final class SettingsManagerImpl implements SettingsManager {
    private static final String SETTINGS_FILE = "./settings.xml";

    public Settings load() {
        try {
            InputSource src = new InputSource(new FileInputStream(new File(SETTINGS_FILE)));
            DOMSettingsUnmarshaller domUms = new DOMSettingsUnmarshaller();
            DOMParser parser = new DOMParser();
            parser.parse(src);
            Document doc = parser.getDocument();
            return domUms.unmarshallSettings(doc.getDocumentElement());
        } catch (FileNotFoundException e) {
            showMessageDialog(null, "Settings file not found.\nUsing default settings.",
                    "Error Loading Settings", INFORMATION_MESSAGE);
            return new Settings();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Settings settings) {
        save(settings, new JProgressPane());
    }

    public void save(Settings settings, JProgressPane progress) {
        DOMSettingsBuilder builder = new DOMSettingsBuilder();
        try {
            builder.buildSettings(settings, new File(SETTINGS_FILE), progress, VERSION);
        } catch (Exception e) {
            // ignore
        }
    }
}
