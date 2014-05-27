Georgina.module("TransactionsApp.Show", function(Show, Georgina, Backbone, Marionette, $, _){
    Show.Controller = {
        showTransaction: function(id) {
            var transactions = Georgina.request("transaction:entities");
            var model = transactions.get(id);
            var transactionView;

            if (model !== undefined) {
                transactionView = new Show.Transaction({
                    model: model
                });
            } else  {
                transactionView = new Show.MissingTransaction();
            }

            Georgina.mainRegion.show(transactionView);
        }
    }
});
