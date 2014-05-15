var transactionItemMarkup = "<td><%- date.substring(0, 10) %></td>"+
"<td><%- category %></td>"+
"<td><%- description.substring(0, 40) %>...</td>"+
"<td><%- amount %></td>"+
"<td><a href='#' class='btn btn-info btn-sm js-show'><i class='icon-zoom-in'></i>More</a></td>";

var transactionItemTemplate = _.template(transactionItemMarkup);

var transactionTableMarkup = "<thead>"+
"<thead>" +
    "<tr>" +
        "<th>Date</th>" +
        "<th>Category</th>" +
        "<th>Description</th>" +
        "<th>Amount</th>" +
        "<th></th>" +
    "</tr>" +
"</thead>" +
"<tbody>" + "</tbody>" + "</thead>";
var transactionTableTemplate = _.template(transactionTableMarkup);

Georgina.module("TransactionsApp.List", function(List, Georgina, Backbone, Marionette, $, _) {
    List.Transaction = Marionette.ItemView.extend({
        tagName: "tr",
        template: transactionItemTemplate,
        events: {
            "click": "highlightRow",
            "click td a.js-show": "showClicked"
        },
        highlightRow: function(){
            this.$el.toggleClass("active");
        },
        showClicked: function(e){
            e.preventDefault();
            e.stopPropagation();
            this.trigger("transaction:show", this.model);
        }
    });

    List.Transactions = Marionette.CompositeView.extend({
        tagName: "table",
        className: "table table-condensed",
        template: transactionTableTemplate,
        itemView: List.Transaction,
        itemViewContainer: "tbody"
    });
});
