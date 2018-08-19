package org.lwjglb.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XmlParser
{
  public static List<ThreeDVertex> parseVertices(File f)
  {
    try
    {
      DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document document = documentBuilder.parse(f);

      List<ThreeDVertex> result = new ArrayList<>();
      XPathFactory pathFactory = XPathFactory.newInstance();
      XPath xpath = pathFactory.newXPath();

      XPathExpression expr = xpath
          .compile("/bodyGeometryBlob/bodyGeometries/bodyGeometries[1]/bodyGeometryContent/bodyGeometryIndexedTriangleStrip/vertices/*");

      NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
      for (int i = 0; i < nodes.getLength(); i++)
      {
        Node n = nodes.item(i);
        String longitude = toString(n.getChildNodes().item(1).getTextContent());
        String latitude = toString(n.getChildNodes().item(3).getTextContent());
        String height = toString(n.getChildNodes().item(5).getTextContent());

        result.add(new ThreeDVertex(Float.parseFloat(longitude) / 10000f, Float.parseFloat(latitude) / 10000f,
            Float.parseFloat(height) / 10000f));
      }
      return result;

    }
    catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e)
    {
      e.printStackTrace();
    }

    return null;
  }

  private static String toString(String text)
  {
    return text.trim();
  }

  public static List<TextureCoordinate> parseTextures(File f)
  {
    try
    {
      DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document document = documentBuilder.parse(f);

      List<TextureCoordinate> result = new ArrayList<>();
      XPathFactory pathFactory = XPathFactory.newInstance();
      XPath xpath = pathFactory.newXPath();

      XPathExpression expr = xpath
          .compile("/bodyGeometryBlob/bodyGeometries/bodyGeometries[1]/bodyGeometryContent/bodyGeometryIndexedTriangleStrip/textureCoords/*");

      NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
      for (int i = 0; i < nodes.getLength(); i++)
      {
        Node n = nodes.item(i);
        String u = toString(n.getChildNodes().item(1).getTextContent());
        String v = toString(n.getChildNodes().item(3).getTextContent());

        result.add(new TextureCoordinate(Float.parseFloat(u), Float.parseFloat(v)));
      }
      return result;

    }
    catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e)
    {
      e.printStackTrace();
    }

    return null;
  }

  public static List<Integer> parseVertIndexes(File f)
  {
    try
    {
      DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document document = documentBuilder.parse(f);

      List<Integer> result = new ArrayList<>();
      XPathFactory pathFactory = XPathFactory.newInstance();
      XPath xpath = pathFactory.newXPath();

      XPathExpression expr = xpath
          .compile("/bodyGeometryBlob/bodyGeometries/bodyGeometries[1]/bodyGeometryContent/bodyGeometryIndexedTriangleStrip/renderGroups/renderGroups[1]/vertIndices/*");

      NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
      for (int i = 0; i < nodes.getLength(); i++)
      {
        Node n = nodes.item(i);
        String u = toString(n.getTextContent());
        result.add(Integer.parseInt(u));
      }
      return result;

    }
    catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e)
    {
      e.printStackTrace();
    }

    return null;
  }

  public static List<Integer> parseTextureIndexes(File f)
  {
    try
    {
      DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document document = documentBuilder.parse(f);

      List<Integer> result = new ArrayList<>();
      XPathFactory pathFactory = XPathFactory.newInstance();
      XPath xpath = pathFactory.newXPath();

      XPathExpression expr = xpath
          .compile("/bodyGeometryBlob/bodyGeometries/bodyGeometries[1]/bodyGeometryContent/bodyGeometryIndexedTriangleStrip/renderGroups/renderGroups[1]/uvIndices/*");

      NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
      for (int i = 0; i < nodes.getLength(); i++)
      {
        Node n = nodes.item(i);
        String u = toString(n.getTextContent());
        result.add(Integer.parseInt(u));
      }
      return result;

    }
    catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e)
    {
      e.printStackTrace();
    }

    return null;
  }

  @AllArgsConstructor
  @Data
  public static class TextureCoordinate
  {
    float u;
    float v;
  }

  @AllArgsConstructor
  @Data
  public static class ThreeDVertex
  {
    float longitude;
    float latitude;
    float height;
  }
}
