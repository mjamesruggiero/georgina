Georgina.module("Entities", function(Entities, Georgina, Backbone, Marionette, $, _){
    Entities.Transaction = Backbone.Model.extend({
        urlRoot: "transactions"
    });

    Entities.TransactionCollection = Backbone.Collection.extend({
        model: Entities.Transaction,
        url: "transactions"
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
        },

        getTransactionEntity: function(transactionId) {
            var transaction = new Entities.Transaction({id: transactionId});
            var defer = $.Deferred();
            setTimeout(function(){
                transaction.fetch({
                    success: function(data){
                        defer.resolve(data);
                    }
                });
            }, 1000);
            return defer.promise();
        }
    };

    Georgina.reqres.setHandler("transaction:entities", function() {
        return API.getTransactionEntities();
    });
    Georgina.reqres.setHandler("transaction:entity", function(id) {
        return API.getTransactionEntity(id);
    });
});
