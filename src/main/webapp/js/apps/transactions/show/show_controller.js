Georgina.module("TransactionsApp.Show", function(Show, Georgina, Backbone, Marionette, $, _){
    Show.Controller = {
        showTransaction: function(model) {
            var transactionView = new Show.Transaction({
                model: model
            });

            Georgina.mainRegion.show(transactionView);
        }
    }
});
