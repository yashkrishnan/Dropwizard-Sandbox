package com.test.sandbox.utilities;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import com.test.sandbox.constants.TextConstants;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

/**
 * Utility helper class for handling String based operations
 */
public class StringUtil {

    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    public static synchronized String generateUniqueAlphaNumericId(String previousId) {
        if (previousId != null) {
            previousId = previousId.toUpperCase();
        } else {
            previousId = TextConstants.ALPHA_NUMERIC_FIRST_ID;
        }
        String nextId = null;
        if (!previousId.equals(TextConstants.ALPHA_NUMERIC_LAST_ID)) {
            long nextIdValue = 0;
            String alphaNumerals = TextConstants.ALPHA_NUMERALS;
            for (int i = 0; i < previousId.length(); i++) {
                Character element = previousId.charAt(i);
                int numericPosition = alphaNumerals.indexOf(element);
                nextIdValue = (36 * nextIdValue) + numericPosition;
            }
            nextIdValue += 1;
            if (nextIdValue == 0) {
                nextId = TextConstants.ALPHA_NUMERIC_FIRST_ID;
            } else {
                nextId = TextConstants.STRING_EMPTY_STRING;
                while (nextIdValue > 0) {
                    int digit = (int) (nextIdValue % 36);            // rightmost digit
                    nextId = alphaNumerals.charAt(digit) + nextId;  // string concatenation
                    nextIdValue = nextIdValue / 36;
                }
            }

            if (nextId.length() < TextConstants.ID_LENGTH) {
                int currentLength = nextId.length();
                int balanceLength = TextConstants.ID_LENGTH - currentLength;

                final char[] fillArray = new char[balanceLength];
                Arrays.fill(fillArray, TextConstants.ZERO);
                nextId = new String(fillArray) + nextId;
            }
        }
        return nextId;
    }

    public static String appendStringsWithDelimiter(List<String> stringList, String delimiter) {
        int listSize = stringList.size();
        StringBuilder stringBuilder = new StringBuilder();
        for (String temp : stringList) {
            stringBuilder.append(temp).append(delimiter);
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    public String formatXML(String unformattedXml) {
        try {
            final Document document = parseXmlFile(unformattedXml);
            OutputFormat format = new OutputFormat(document);
            format.setLineWidth(65);
            format.setIndenting(true);
            format.setIndent(2);
            Writer out = new StringWriter();
            XMLSerializer serializer = new XMLSerializer(out, format);
            serializer.serialize(document);

            return out.toString();
        } catch (IOException e) {
            logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
            logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
            throw new RuntimeException(e);
        }
    }

    private Document parseXmlFile(String in) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(in));
            return db.parse(is);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
            logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
            throw new RuntimeException(e);
        }
    }

    public String html2text(String html) {
        return Jsoup.parse(html).text();
    }
}