var transactionListTemplate =
    _.template("<td><%- date.substring(0, 10) %></td><td><%- category %></td><td><%- description.substring(0, 48) %>...</td><td><%- amount %></td>");

var transactionTableMarkup = "<thead>"+
"<thead>" +
    "<tr>" +
        "<th>Date</th>" +
        "<th>Category</th>" +
        "<th>Description</th>" +
        "<th>Amount</th>" +
    "</tr>" +
"</thead>" +
"<tbody>" + "</tbody>" + "</thead>";
var transactionTableTemplate = _.template(transactionTableMarkup);

Georgina.module("TransactionsApp.List", function(List, Georgina, Backbone, Marionette, $, _) {
    List.Transaction = Marionette.ItemView.extend({
        tagName: "tr",
        template: transactionListTemplate
    });

    List.Transactions = Marionette.CompositeView.extend({
        tagName: "table",
        className: "table table-condensed",
        template: transactionTableTemplate,
        itemView: List.Transaction,
        itemViewContainer: "tbody"
    });
});
