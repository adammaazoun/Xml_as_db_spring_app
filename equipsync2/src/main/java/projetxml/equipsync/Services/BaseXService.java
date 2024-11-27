package projetxml.equipsync.Services;

import org.basex.core.BaseXException;
import org.basex.api.client.ClientSession;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

@Service
public class BaseXService {
    private final ClientSession session;

    public BaseXService(ClientSession session) {
        this.session = session;
    }

    public String executeXQuery(String query) throws BaseXException, IOException {
        return session.execute("xquery " + query);
    }
    public void openDatabase(String dbName) throws BaseXException, IOException {
        session.execute("OPEN " + dbName);  // Open the specified database
    }


}
