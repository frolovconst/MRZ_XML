package com;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class Main {

    public static void main(String[] args) {
        try {
            String[] MRZlines = {"", ""};
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            File inputFile = new File("marked_up.xml");
            Document doc = builder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("TextLine");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                NodeList cList = nNode.getChildNodes();
                for (int temp_c = 0; temp_c < cList.getLength(); temp_c++) {
                    Node cNode = cList.item(temp_c);
                    if (cNode.getNodeName() == "Char") {
                        Element eElement = (Element) cNode;
                        MRZlines[temp] = MRZlines[temp] + eElement.getAttribute("Unicode");
                    }
                }
            }

            MRZlines[0] = "P<RUSCHUBANOV<IBN<FARRUKH<<DMITRY<<<<<<<<<<<";

            System.out.println(MRZlines[0]);
            System.out.println(MRZlines[1]);


//            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder dBuilder = factory.newDocumentBuilder();
            Document docO = builder.newDocument();
            // root element
            Element rootElement = docO.createElement("Passport_RU");
//            rootElement.setAttribute("type", "mrz");
            docO.appendChild(rootElement);
            String fieldValue;
            int crntPos;
            ParseResults fieldParseRes;

            //  field element
            Element field;
//            Element value;

            field = docO.createElement("DocumentType");
            rootElement.appendChild(field);
//            attrValue = "";
//            attrValue = attrValue + MRZlines[0].charAt(0);
            field.appendChild(docO.createTextNode(MRZlines[0].substring(0,1)));

            // setting attribute to element
//            Attr DocumentType = docO.createAttribute("type");
//            DocumentType.setValue("DocumentType");
//            field.setAttributeNode(DocumentType);
            // value element
//            value = docO.createElement("value");
//            attrValue = "";
//            attrValue = attrValue + MRZlines[0].charAt(0);
//            Attr attrType = docO.createAttribute("type");
//            attrType.setValue("formula one");
//            value.setAttributeNode(attrType);
//            value.appendChild(docO.createTextNode(attrValue));
//            field.appendChild(value);


            field = docO.createElement("DocumentSubtype");
            rootElement.appendChild(field);
            field.appendChild(docO.createTextNode(MRZlines[0].substring(1,2)));


            field = docO.createElement("IssuingCountry");
            rootElement.appendChild(field);
            field.appendChild(docO.createTextNode(MRZlines[0].substring(2,5)));

//            field = docO.createElement("field");
//            rootElement.appendChild(field);
//            Attr LastName = docO.createAttribute("LastName");
//            LastName.setValue("LastName");
//            field.setAttributeNode(LastName);
//            // value element
//            value = docO.createElement("value");
//            attrValue = "";
//            attrValue = attrValue + MRZlines[0].charAt(0);
//            value.appendChild(docO.createTextNode(attrValue));
//            field.appendChild(value);

//            System.out.println(nextDoubleArrowNumber(MRZlines[0], 5));
//            int nextFieldBorder = nextDoubleArrowNumber(MRZlines[0], 5);
//            System.out.println(MRZlines[0].substring(5, nextDoubleArrowNumber(MRZlines[0], 5)));
//            System.out.println(RunTillNextDoubleArrowNumber(MRZlines[0], 5));
            field = docO.createElement("LastName");
            rootElement.appendChild(field);
            fieldParseRes = RunTillNextDoubleArrowNumber(MRZlines[0], 5);
            crntPos = fieldParseRes.getBorder();
            field.appendChild(docO.createTextNode(fieldParseRes.getResult()));


            field = docO.createElement("GivenName");
            rootElement.appendChild(field);
            fieldParseRes = RunTillNextDoubleArrowNumber(MRZlines[0], crntPos);
            crntPos = fieldParseRes.getBorder();
            field.appendChild(docO.createTextNode(fieldParseRes.getResult()));

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(docO);
            StreamResult result = new StreamResult(new File("marked_up_split.xml"));
            transformer.transform(source, result);
            // Output to console for testing
            StreamResult consoleResult = new StreamResult(System.out);
            transformer.transform(source, consoleResult);
        }
        catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    private static ParseResults RunTillNextDoubleArrowNumber(String input, int start){
        StringBuilder result = new StringBuilder();
        while (true){
            if(input.charAt(start) == '<') {
                if (input.charAt(++start) == '<') {
                    break;
                } else {
                    result.append(' ');
                    result.append(input.charAt(start++));
                }
            }
            else{
                result.append(input.charAt(start++));
            }
        }
        return new ParseResults(result.toString(), ++start);
    }

    private static int nextArrowNumber(String input, int start){
        while (input.charAt(start++) != '<');
        return --start;
    }
}
