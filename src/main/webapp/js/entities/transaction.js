Georgina.module("Entities", function(Entities, Georgina, Backbone, Marionette, $, _){
    Entities.Transaction = Backbone.Model.extend({});

    Entities.TransactionCollection = Backbone.Collection.extend({
        model: Entities.Transaction,
        comparator: "category"
    });

    var transactions;

    var initializeTransactions = function() {
        transactions = new Entities.TransactionCollection([
            { id: 1,  date: "2014-01-01", category: "grocery", description: "Nob Hill", amount: 16.30 },
            { id: 2,  date: "2014-01-01", category: "grocery", description: "Trader Joe's", amount: 100.00 },
            { id: 13, date: "2014-01-01", category: "entertainment", description: "Amazon.com", amount: 25.00 }
        ]);
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
