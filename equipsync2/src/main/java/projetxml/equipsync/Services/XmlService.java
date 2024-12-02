package projetxml.equipsync.Services;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.transform.stream.StreamSource;
import org.xml.sax.SAXException;

public class XmlService<T> {

    private final Class<T> type;

    public XmlService(Class<T> type) {
        this.type = type;
    }

    /**
     * Serialize the object to an XML string.
     */
    public String serialize(T object) throws Exception {
        JAXBContext context = JAXBContext.newInstance(type);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
        StringWriter writer = new StringWriter();
        marshaller.marshal(object, writer);
        return writer.toString();
    }

    /**
     * Deserialize an XML string to an object.
     */
    public T deserialize(String xml) throws Exception {
        JAXBContext context = JAXBContext.newInstance(type);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        StringReader reader = new StringReader(xml);
        return type.cast(unmarshaller.unmarshal(reader));
    }

    /**
     * Validate the XML string against the given XSD schema.
     */
    public void validate(String xml, String xsdPath) throws Exception {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new File(xsdPath));
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(new StringReader(xml)));
    }
}

