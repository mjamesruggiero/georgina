Georgina.module("TransactionsApp.Show", function(Show, Georgina, Backbone, Marionette, $, _){
    Show.Transaction = Marionette.ItemView.extend({
        template: "#transaction-view",

        events: {
            "click a.js-transaction-back": "showTransactions"
        },
        showTransactions: function(e) {
            e.preventDefault();
            e.stopPropagation();
            Georgina.trigger("transactions:list");
        }
    });
});
