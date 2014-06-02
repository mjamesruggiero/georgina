Georgina.module("TransactionsApp.List", function(List, Georgina, Backbone, Marionette, $, _) {
    List.Controller = {
        listTransactions: function() {
            var transactions = Georgina.request("transaction:entities");
            var transactionsListView = new Georgina.TransactionsApp.List.Transactions({
                collection: transactions
            });
            transactionsListView.on("itemview:transaction:show", function(childView, model){
                Georgina.trigger("transaction:show", model.get("id"));
            });
            Georgina.mainRegion.show(transactionsListView);
        }
    };
});
