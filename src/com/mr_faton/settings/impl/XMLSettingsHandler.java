package com.mr_faton.settings.impl;

import com.mr_faton.settings.SettingsAPI;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by root on 08.06.2015.
 */
public class XMLSettingsHandler implements SettingsAPI{
    private File settingsFile;
    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;
    private Document document;

    public XMLSettingsHandler() {
        settingsFile = new File("settings.xml");
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            System.err.println("Произошла ошибка: при конструировании файла настроек");
            e.printStackTrace();
        }

        if (!settingsFile.exists()) {
            System.err.println("Произошла ошибка: Файл настроек не существует... Но он был создан");
            createSettingsFile();
        } else {
            try {
                document = documentBuilder.parse(settingsFile);
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createSettingsFile() {
        Document doc = documentBuilder.newDocument();

        Element settings = doc.createElement("settings");

        Element program_settings = doc.createElement("program_settings");
        Element wallets = doc.createElement("wallets");

        //for program settings

        Element url = doc.createElement("url");
        Text urlText = doc.createTextNode("url");
        url.appendChild(urlText);

        Element email_from = doc.createElement("email_from");

        Element email_from_login = doc.createElement("email_from_login");
        Text email_from_loginText = doc.createTextNode("email_from_login");
        email_from_login.appendChild(email_from_loginText);

        Element email_from_password = doc.createElement("email_from_password");
        Text email_from_passwordText = doc.createTextNode("email_from_password");
        email_from_password.appendChild(email_from_passwordText);

        email_from.appendChild(email_from_login);
        email_from.appendChild(email_from_password);


        Element email_to = doc.createElement("email_to");
        Text email_toText = doc.createTextNode("email_to");
        email_to.appendChild(email_toText);

        program_settings.appendChild(url);
        program_settings.appendChild(email_from);
        program_settings.appendChild(email_to);

        //wallets

        Element wallet = doc.createElement("wallet");

        Element wallet_login = doc.createElement("wallet_login");
        Text wallet_loginText = doc.createTextNode("wallet_login");
        wallet_login.appendChild(wallet_loginText);

        Element wallet_password = doc.createElement("wallet_password");
        Text wallet_passwordText = doc.createTextNode("wallet_password");
        wallet_password.appendChild(wallet_passwordText);

        wallet.appendChild(wallet_login);
        wallet.appendChild(wallet_password);

        wallets.appendChild(wallet);

        settings.appendChild(program_settings);
        settings.appendChild(wallets);

        doc.appendChild(settings);

        document = doc;

        saveSettingsFile();
    }

    private void saveSettingsFile() {
        try (FileOutputStream fileOutput = new FileOutputStream(settingsFile)) {
            //построить холостое преобразование
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            //установить отступ
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            //выполнить холостое преобразование и вывести узел в файл
            transformer.transform(new DOMSource(document), new StreamResult(fileOutput));

        } catch (IOException | TransformerException ex) {
            System.err.println("Ошибка при сохранении файла настроек");
        }
    }

    @Override
    public Map<String, String> getWalletsMap() {
        LinkedHashMap<String, String> walletsMap = new LinkedHashMap<>();
        NodeList walletList = document.getElementsByTagName("wallet");

        Element wallet;
        Node walletLoginNode;
        Node walletPasswordNode;

        for (int i = 0; i < walletList.getLength(); i++) {
            wallet = (Element) walletList.item(i);
            walletLoginNode = wallet.getElementsByTagName("wallet_login").item(0);
            walletPasswordNode = wallet.getElementsByTagName("wallet_password").item(0);

            walletsMap.put(walletLoginNode.getTextContent(), walletPasswordNode.getTextContent());
        }

        return walletsMap;
    }

    @Override
    public String getUrl() {
        Node urlNode = document.getElementsByTagName("url").item(0);
        return urlNode.getTextContent();
    }

    @Override
    public Map<String, String> getMapMailFrom() {
        Map<String, String> mapMailFrom = new HashMap<>();
        String mailLogin;
        String mailPassword;

        Node mailFromLogin = document.getElementsByTagName("email_from_login").item(0);
        mailLogin = mailFromLogin.getTextContent();

        Node mailFromPassword =document.getElementsByTagName("email_from_password").item(0);
        mailPassword = mailFromPassword.getTextContent();

        mapMailFrom.put("login", mailLogin);
        mapMailFrom.put("password", mailPassword);
        return mapMailFrom;
    }

    @Override
    public String getMailTo() {
        Node mailToNode = document.getElementsByTagName("email_to").item(0);
        return mailToNode.getTextContent();
    }
}