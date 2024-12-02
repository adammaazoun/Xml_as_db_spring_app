package projetxml.equipsync.security;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class InstantAdapter extends XmlAdapter<String, Instant> {

    @Override
    public Instant unmarshal(String v) throws Exception {
        return Instant.parse(v);  // Adjust format if necessary
    }

    @Override
    public String marshal(Instant v) throws Exception {
        return v.toString();  // Adjust format if necessary
    }
}
