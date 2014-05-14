Georgina.SummaryItemView = Marionette.ItemView.extend({
    tagName: "tr",
    template: summaryTemplate
});

Georgina.SummaryItemView = Marionette.ItemView.extend({
    tagName: "table"
});

Georgina.module("Entities", function(Entities, Georgina, Backbone, Marionette, $, _){
    Entities.Summary = Backbone.Model.extend({});

    Entities.SummaryCollection = Backbone.Collection.extend({
        model: Entities.Summary
    });

    var transactions;

    var initializesummary = function() {
        transactions = new Entities.SummaryCollection(getsummary());
    };

    var getsummary = function(category) {
        var endpoint = "/categories/" + category;
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
        getSummaryEntities: function() {
            if (transactions === undefined) {
                initializesummary();
            }
            return transactions;
        }
    };

    Georgina.reqres.setHandler("transaction:entities", function() {
        return API.getSummaryEntities();
    });
}

