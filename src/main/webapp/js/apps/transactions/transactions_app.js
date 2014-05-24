Georgina.module("TransactionsApp", function(TransactionsApp, Georgina, Backbone, Marionette, $, _) {
    TransactionsApp.Router = Marionette.AppRouter.extend({
        appRoutes: {
            "transactions": "listTransactions"
        }
    });

    var API = {
        listTransactions: function() {
            Georgina.TransactionsApp.List.Controller.listTransactions();
        }
    };

    Georgina.on("transactions:list", function(){
        Georgina.navigate("transactions");
        API.listTransactions();
    });

    Georgina.addInitializer(function() {
        new TransactionsApp.Router({
            controller: API
        });
    });
});
