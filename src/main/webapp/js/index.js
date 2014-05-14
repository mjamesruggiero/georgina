// Templates -
// rendering here and not on the
// Scala Server Pages - the "<>%%>" tags
// clash with scalatra templating
var summaryTemplateMarkup = "<tr> <td>Start Date</td>" +
        "<td><%- startDate %></td> "+
        "<td>End Date</td>"+
        "<td><%- endDate %></td>"+
        "<td>Category</td><td><%- category %></td>"+
        "<td>Mean</td><td><%- mean %></td>"+
        "<td>Count</td><td><%- count %></td>+"+
        "<td>Standard deviation</td>"+
        "<td><%- standard_deviation %></td></tr>";
var summaryTemplate = _.template(summaryTemplateMarkup);
var summaryModalMarkup = "<button class='btn btn-primary' data-toggle='modal' data-target='.bs-example-modal-lg'>Large modal</button>"+
        " <div class='modal fade bs-example-modal-lg' tabindex='-1' role='dialog' aria-labelledby='myLargeModalLabel' aria-hidden='true'> "+
        "<div class='modal-dialog modal-lg'> "+
        "<div class='modal-content'> ...  </div> </div> </div>";
var summaryModalTemplate = _.template(summaryModalMarkup);

var categoryListTemplate =
    _.template("<a href='categories/<%- category %>'><%-category%></a> (<%- count %>)");

///// categories
Georgina.CategoryItemView = Marionette.ItemView.extend({
    tagName: "li",
    template: categoryListTemplate
});

Georgina.CategoriesView = Marionette.CollectionView.extend({
    tagName: "ul",
    itemView: Georgina.CategoryItemView
});

Georgina.on("initialize:after", function() {
    var transactions = Georgina.request("transaction:entities");
    var categories = Georgina.request("category:entities");
    console.log("categories are ", JSON.stringify(categories));

    var transactionsListView = new Georgina.TransactionsApp.List.Transactions({
        collection: transactions
    });

    var categoryListView = new Georgina.CategoriesView({
        collection: categories
    });

    Georgina.mainRegion.show(transactionsListView);
    Georgina.categoryRegion.show(categoryListView);
});
Georgina.start();
