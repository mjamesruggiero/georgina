var summaryTemplate = _.template("<tr> <td>Start Date</td><td><%- startDate %></td> <td>End Date</td><td><%- endDate %></td> <td>Category</td><td><%- category %></td> <td>Mean</td><td><%- mean %></td> <td>Count</td><td><%- count %></td> <td>Standard deviation</td><td><%- standard_deviation %></td></tr>");
var summaryModalTemplate = ("<button class='btn btn-primary' data-toggle='modal' data-target='.bs-example-modal-lg'>Large modal</button> <div class='modal fade bs-example-modal-lg' tabindex='-1' role='dialog' aria-labelledby='myLargeModalLabel' aria-hidden='true'> <div class='modal-dialog modal-lg'> <div class='modal-content'> ...  </div> </div> </div>");

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

    var getsummary = function() {
        var endpoint = "/transactions";
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

