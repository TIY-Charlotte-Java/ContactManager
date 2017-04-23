package com.theironyard.charlotte;

import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import spark.Spark;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    private static ArrayList<Contact> contacts = new ArrayList<>();
    private static HashMap<String, ArrayList<Contact>> events = new HashMap<>();

    public static void main(String[] args) {
        contacts.add(new Contact(1, "Ben", "Sterrett", "12346789", "123 Test Street"));
        contacts.add(new Contact(2, "Ben", "Sterrett", "12346789", "123 Test Street"));
        contacts.add(new Contact(3, "Steve", "Ballmer", "2345552679", "1 Microsoft Exec Way."));
        contacts.add(new Contact(4, "Ann", "Carter", "2552027498", "123 New Bern Street"));
        contacts.add(new Contact(5, "Luke", "Segars", "5556637423", "Suite A, Apartment Street"));

        // the id of the person that is being invited
        // the event name.

        // POST /api/events/:person_id
        // {
        //     "eventId": <string>
        // }

        // POST /api/events/1,
        // POST /api/events/2.
        // POST /api/events/3
        // POST /api/events/513
        // etc.

        // retrieve attendees for an event
        Spark.get("/api/events/:event_id", (req, res) -> {
            // get the event string from the url
            String event = req.params("event_id");

            // get the attendees from the hashmap
            // at key `event`
            ArrayList<Contact> attendees = events.getOrDefault(event, new ArrayList<>());

            // turn that data into json and return it
            return new JsonSerializer().serialize(attendees);
        });


        // WOULD FAIL: /api/events/pants
        Spark.post("/api/events/:contact_id", (req, res) -> {
            // add someone to an event
            // get the person to be added
            int contactId = Integer.valueOf(req.params("contact_id"));

            // going to store the contact that the request
            // is referencing.
            Contact currentContact = null;

            // FIND the one contact in `contacts`
            // whose ID matches the request parameter
            for (Contact c : contacts) {
                if (c.getId() == contactId) {
                    // this is our guy/gal
                    currentContact = c;
                    break;
                }
            }

            // use the jodd library's json parser
            JsonParser parser = new JsonParser();

            // get the body of the request (aka the stuff you POSTed)
            // ..and then turn it into an event.
            Event e = parser.parse(req.body(), Event.class);

            // makes sure that there's at least an arraylist
            // object for that event
            events.putIfAbsent(e.getEventId(), new ArrayList<>());

            // get the arraylist at the `e.getEventId()` key, and add our
            // contact to it.
            events.get(e.getEventId()).add(currentContact);

            // add their name to the arraylist in the event hashmap entry.
            return "";
        });

        // list the people who are attending an event

//        Spark.get("/", (req, res) -> {
//            HashMap<String, Object> model = new HashMap<>();
//
//            model.put("people", contacts);
//
//            return new ModelAndView(model, "index.html");
//        }, new MustacheTemplateEngine());
//
//        Spark.post("/contacts", (req, res) -> {
//            // read in that ol' data
//            String fName = req.queryParams("firstName");
//            String lName = req.queryParams("lastName");
//            String phoneNum = req.queryParams("phoneNumber");
//            String address = req.queryParams("address");
//
//            // craft us a contact
//            // add that contact to our contacts. list.
//            contacts.add(new Contact(fName, lName, phoneNum, address));
//
//            // ye olde redirect here.
//            res.redirect("/");
//            return "";
//        });

        Spark.get("/api/contacts", (req, res) -> {
            JsonSerializer serializer = new JsonSerializer();

            return serializer.serialize(contacts);
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
