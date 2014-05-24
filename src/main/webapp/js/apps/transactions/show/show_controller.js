Georgina.module("TransactionsApp.Show", function(Show, Georgina, Backbone, Marionette, $, _){
    Show.Controller = {
        showTransaction: function(id) {
            var transactions = Georgina.request("transaction:entities");
            var model = transactions.get(id);
            var transactionView = new Show.Transaction({
                model: model
            });

            Georgina.mainRegion.show(transactionView);
        }
    }
});
