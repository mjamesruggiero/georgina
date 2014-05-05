Georgina.module("Entities", function(Entities, Georgina, Backbone, Marionette, $, _){
    Entities.Contact = Backbone.Model.extend({});

    Entities.ContactCollection = Backbone.Collection.extend({
        model: Entities.Contact,
        comparator: "firstName"
    });

    var contacts;

    var initializeContacts = function() {
        contacts = new Entities.ContactCollection([
            { id: 1, firstName: "Bob", lastName: "Brigham", phoneNumber: "555-0163" },
            { id: 2, firstName: "Alice", lastName: "Arten", phoneNumber: "555-5432" },
            { id: 13, firstName: "Charlie", lastName: "Campbell", phoneNumber: "555-8888" }
        ]);
    };

    var API = {
        getContactEntities: function() {
            if (contacts === undefined) {
                initializeContacts();
            }
            return contacts;
        }
    };

    Georgina.reqres.setHandler("contact:entities", function() {
        return API.getContactEntities();
    });
});
