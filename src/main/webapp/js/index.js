// Templates - note that they are inside the JS, because
// scalatra tries to render the <%- foo %> as
// Scala Server Pages tags
var transactionListTemplate = _.template("<%- description %> <%- amount %>");

Georgina.TransactionItemView = Marionette.ItemView.extend({
    tagName: "li",
    template: transactionListTemplate
});

Georgina.TransactionsView = Marionette.CollectionView.extend({
    tagName: "ul",
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
