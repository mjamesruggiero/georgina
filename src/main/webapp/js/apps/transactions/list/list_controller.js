Georgina.module("TransactionsApp.List", function(List, Georgina, Backbone, Marionette, $, _) {
    List.Controller = {
        listTransactions: function() {
            var transactions = Georgina.request("transaction:entities");
            var transactionsListView = new Georgina.TransactionsApp.List.Transactions({
                collection: transactions
            });
            transactionsListView.on("itemview:transaction:show", function(childView, model){
                Georgina.TransactionsApp.Show.Controller.showTransaction(model);
            });
            Georgina.mainRegion.show(transactionsListView);
        }
    }
});
