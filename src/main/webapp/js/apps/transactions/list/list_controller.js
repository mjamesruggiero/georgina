Georgina.module("TransactionsApp.List", function(List, Georgina, Backbone, Marionette, $, _) {
    List.Controller = {
        listTransactions: function() {
            var transactions = Georgina.request("transaction:entities");
            var transactionsListView = new Georgina.TransactionsApp.List.Transactions({
                collection: transactions
            });

            Georgina.mainRegion.show(transactionsListView);
        }
    }
});
