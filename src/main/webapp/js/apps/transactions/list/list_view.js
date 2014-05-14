var transactionListTemplate =
    _.template("<td><%- date.substring(0, 10) %></td><td><%- category %></td><td><%- description.substring(0, 48) %>...</td><td><%- amount %></td>");

Georgina.module("TransactionsApp.List", function(List, Georgina, Backbone, Marionette, $, _) {
    List.Transaction = Marionette.ItemView.extend({
        tagName: "tr",
        template: transactionListTemplate
    });

    List.Transactions = Marionette.CollectionView.extend({
        tagName: "table",
        className: "table table-condensed",
        itemView: List.Transaction
    });
});
