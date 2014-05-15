var transactionShowMarkup = "<div class='panel panel-default'>"+
    "<div class='panel-heading'>"+
        "<h3 class='panel-title'><%- category %></h3>"+
    "</div>"+
    "<div class='panel-body'>"+
        "<p><%- date.substring(0, 10) %></p>"+
        "<p><%- description %></p>"+
        "<p><%- amount %></p>";
    "</div>"+
"</div>";

var transactionShowTemplate = _.template(transactionShowMarkup);

Georgina.module("TransactionsApp.Show", function(Show, Georgina, Backbone, Marionette, $, _){
    Show.Transaction = Marionette.ItemView.extend({
        template: transactionShowTemplate
    });
});
