// Templates - note that they are inside the JS, because
// scalatra tries to render the <%- foo %> as
// Scala Server Pages tags
var nameTemplate        = _.template("<p><%- firstName %> <%- lastName %></p>");
var contactListTemplate = _.template("<%- firstName %> <%- lastName %>");

Georgina.ContactItemView = Marionette.ItemView.extend({
    tagName: "li",
    template: contactListTemplate
});

Georgina.ContactsView = Marionette.CollectionView.extend({
    tagName: "ul",
    itemView: Georgina.ContactItemView
});

Georgina.on("initialize:after", function() {
    var contacts = Georgina.request("contact:entities");

    var contactsListView = new Georgina.ContactsView({
        collection: contacts
    });

    Georgina.mainRegion.show(contactsListView);
});
Georgina.start();
