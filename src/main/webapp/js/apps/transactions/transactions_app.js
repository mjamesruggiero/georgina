Georgina.module("TransactionsApp", function(TransactionsApp, Georgina, Backbone, Marionette, $, _) {
    TransactionsApp.Router = Marionette.AppRouter.extend({
        appRoutes: {
            "transactions": "listTransactions",
            "transactions/:id": "showTransaction"
        }
    });

    var API = {
        listTransactions: function() {
            Georgina.TransactionsApp.List.Controller.listTransactions();
        },

        showTransaction: function(id) {
            Georgina.TransactionsApp.Show.Controller.showTransaction(id);
        }
    };

    Georgina.on("transactions:list", function(){
        Georgina.navigate("transactions");
        API.listTransactions();
    });

    Georgina.on("transaction:show", function(id){
        Georgina.navigate("transactions/" + id);
        API.showTransaction(id);
    });

    Georgina.addInitializer(function() {
        new TransactionsApp.Router({
            controller: API
        });
    });
});
