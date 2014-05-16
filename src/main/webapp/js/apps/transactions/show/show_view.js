Georgina.module("TransactionsApp.Show", function(Show, Georgina, Backbone, Marionette, $, _){
    Show.Transaction = Marionette.ItemView.extend({
        template: "#transaction-view"
    });
});
