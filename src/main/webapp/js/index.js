// Templates -
// rendering here and not on the
// Scala Server Pages - the "<>%%>" tags
// clash with scalatra templating

var transactionListTemplate =
    _.template("<td><%- date.substring(0, 10) %></td><td><%- category %></td><td><%- description.substring(0, 48) %>...</td><td><%- amount %></td>");
var categoryListTemplate =
    _.template("<a href='category/<%- category %>'><%-category%></a> (<%- count %>)");

Georgina.CategoryItemView = Marionette.ItemView.extend({
    tagName: "li",
    template: categoryListTemplate
});

Georgina.CategoriesView = Marionette.CollectionView.extend({
    tagName: "ul",
    itemView: Georgina.CategoryItemView
});

Georgina.TransactionItemView = Marionette.ItemView.extend({
    tagName: "tr",
    template: transactionListTemplate
});

Georgina.TransactionsView = Marionette.CollectionView.extend({
    tagName: "table",
    className: "table table-condensed",
    itemView: Georgina.TransactionItemView
});

Georgina.on("initialize:after", function() {
    var transactions = Georgina.request("transaction:entities");
    var categories = Georgina.request("category:entities");
    console.log("categories are ", JSON.stringify(categories));

    var transactionsListView = new Georgina.TransactionsView({
        collection: transactions
    });

    var categoryListView = new Georgina.CategoriesView({
        collection: categories
    });

    Georgina.mainRegion.show(transactionsListView);
    Georgina.categoryRegion.show(categoryListView);
});
Georgina.start();
