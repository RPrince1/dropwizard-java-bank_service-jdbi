package com.morrisons.base.dropwizard.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.morrisons.base.dropwizard.exceptions.ApplicationException;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.CheckForNull;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.morrisons.base.dropwizard.exceptions.ErrorSequences.JSON_PARSING_ERROR;
import static com.morrisons.base.dropwizard.exceptions.ErrorSequences.XML_PARSING_ERROR;

/**
 * Created by peter on 04/07/17.
 */
@Slf4j
public class JsonXmlUtils {

    public static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }


    @CheckForNull
    public static void writeJsonFile(String path, Object value) throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), value);
    }

    @CheckForNull
    public static void writeFile(String path, String value) throws IOException {
        Path file = Paths.get(path);
        Files.write(file, value.getBytes());
    }

    @CheckForNull
    public static String toJson(Object value) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error(JSON_PARSING_ERROR.getMessage(), e);
            throw new ApplicationException(JSON_PARSING_ERROR);
        }
    }

    public static <T> T fromJson(String json, Class<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            log.error(JSON_PARSING_ERROR.getMessage(), e);
            throw new ApplicationException(JSON_PARSING_ERROR);
        }
    }

    public static <T> List<T> fromJsonList(String json, Class<T> type) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, type));
        } catch (IOException e) {
            log.error(JSON_PARSING_ERROR.getMessage(), e);
            throw new ApplicationException(JSON_PARSING_ERROR);
        }
    }


    @CheckForNull
    public static <T> T readJsonFile(String fileName, Class<T> type) throws IOException {
        return mapper.readValue(readFile(fileName), type);
    }

    public static String readFile(String path) throws IOException {
        if (!path.equals("")) {
            return new String(Files.readAllBytes(Paths.get(path)));
        }
        return null;
    }

    public static <T> String toXml(Object value, Class<T> type) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(type);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            StringWriter sw = new StringWriter();
            jaxbMarshaller.marshal(value, sw);
            return sw.toString();
        } catch (JAXBException e) {
            log.error(XML_PARSING_ERROR.getMessage(), e);
            throw new ApplicationException(XML_PARSING_ERROR);
        }
    }

    @CheckForNull
    public static <T> T fromXml(String xml, Class<T> type) throws IOException, JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(type);
        Unmarshaller u = jaxbContext.createUnmarshaller();
        return (T) u.unmarshal(new StringReader(xml));
    }
}
