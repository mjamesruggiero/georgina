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

///// categories
Georgina.SummaryItemView = Marionette.ItemView.extend({
    tagName: "tr",
    template: summaryTemplate
});

Georgina.SummaryItemView = Marionette.ItemView.extend({
    tagName: "table"
});

Georgina.CategoryItemView = Marionette.ItemView.extend({
    tagName: "li",
    template: "#category-view"
});

Georgina.CategoriesView = Marionette.CollectionView.extend({
    tagName: "ul",
    itemView: Georgina.CategoryItemView
});

Georgina.start();
