// Templates - note that they are inside the JS, because
// scalatra tries to render the <%- foo %> as
// Scala Server Pages tags
var transactionListTemplate = _.template("<td><%- date %></td><td><%- category %></td><td><%- description %></td><td><%- amount %></td>");

Georgina.TransactionItemView = Marionette.ItemView.extend({
    tagName: "tr",
    template: transactionListTemplate
});

Georgina.TransactionsView = Marionette.CollectionView.extend({
    tagName: "table",
    className: "table-condensed",
    itemView: Georgina.TransactionItemView
});

Georgina.on("initialize:after", function() {
    var transactions = Georgina.request("transaction:entities");

    var transactionsListView = new Georgina.TransactionsView({
        collection: transactions
    });

    Georgina.mainRegion.show(transactionsListView);
});
Georgina.start();
