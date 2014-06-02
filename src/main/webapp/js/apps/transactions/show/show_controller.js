Georgina.module("TransactionsApp.Show", function(Show, Georgina, Backbone, Marionette, $, _){
    Show.Controller = {
        showTransaction: function(id) {
            var fetchingTransaction = Georgina.request("transaction:entity", id);
            $.when(fetchingTransaction).done(function(transaction) {
                var transactionView;
                if (transaction !== undefined) {
                    transactionView = new Show.Transaction({
                        model: transaction
                    });
                }
                else {
                    transactionView = new Show.MissingTransaction();
                }
                Georgina.mainRegion.show(transactionView);
            });
        }
    };
});
