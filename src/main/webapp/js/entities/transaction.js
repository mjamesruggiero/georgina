Georgina.module("Entities", function(Entities, Georgina, Backbone, Marionette, $, _){
    Entities.Transaction = Backbone.Model.extend({});

    Entities.TransactionCollection = Backbone.Collection.extend({
        model: Entities.Transaction
    });

    var transactions;

    var initializeTransactions = function() {
        transactions = new Entities.TransactionCollection(getTransactions());
    };

    var getTransactions = function() {
        var endpoint = "transactions/";
        var data = getData(endpoint);
        return data["transactions"];
    };

    var getData = function(endpoint) {
        var json = $.ajax({
            url: endpoint,
            async: false
        }).responseText;
        data = JSON.parse(json);
        return data;
    };

    var API = {
        getTransactionEntities: function() {
            if (transactions === undefined) {
                initializeTransactions();
            }
            return transactions;
        }
    };

    Georgina.reqres.setHandler("transaction:entities", function() {
        return API.getTransactionEntities();
    });
});
