var transactionShowMarkup = "<div class='panel panel-default'>"+
    "<div class='panel-body'>"+
        "<table class='table'>"+
            "<tr>" +
                "<td>Date</td><td><%- date.substring(0, 10) %></td>"+
            "</tr>" +
            "<tr>" +
                "<td>Description</td><td><%- description %></td>"+
            "</tr>" +
            "<tr>" +
                "<td>Category</td><td><%- category %></td>"+
            "</tr>" +
            "<tr>" +
                "<td>Amount</td><td><%- amount %></td>";
            "</tr>" +
        "</table>"+
    "</div>"+
"</div>";

var transactionShowTemplate = _.template(transactionShowMarkup);

Georgina.module("TransactionsApp.Show", function(Show, Georgina, Backbone, Marionette, $, _){
    Show.Transaction = Marionette.ItemView.extend({
        template: transactionShowTemplate
    });
});
