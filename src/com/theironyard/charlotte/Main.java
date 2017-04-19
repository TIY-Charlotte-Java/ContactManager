package com.theironyard.charlotte;

import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    private static ArrayList<Contact> contacts = new ArrayList<>();

    public static void main(String[] args) {
        Spark.get("/", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();

            model.put("people", contacts);

            return new ModelAndView(model, "index.html");
        }, new MustacheTemplateEngine());

        Spark.post("/contacts", (req, res) -> {
            // read in that ol' data
            String fName = req.queryParams("firstName");
            String lName = req.queryParams("lastName");
            String phoneNum = req.queryParams("phoneNumber");
            String address = req.queryParams("address");

            // craft us a contact
            // add that contact to our contacts. list.
            contacts.add(new Contact(fName, lName, phoneNum, address));

            // ye olde redirect here.
            res.redirect("/");
            return "";
        });

        Spark.get("/api/contacts", (req, res) -> {
            return new JsonSerializer().serialize(contacts);
        });

        Spark.post("/api/contacts", (req, res) -> {
            // get the json string posted in the request
            String json = req.body();

            // turn that json into an object (a Contact object)
            Contact c = new JsonParser().parse(json, Contact.class);

            // add that object to my contacts list
            contacts.add(c);

            // celebrate yay.
            return "";
        });
    }
}
