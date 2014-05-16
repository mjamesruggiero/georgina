Georgina.module("TransactionsApp.List", function(List, Georgina, Backbone, Marionette, $, _) {
    List.Transaction = Marionette.ItemView.extend({
        tagName: "tr",
        template: "#transaction-item",
        events: {
            "click": "highlightRow",
            "click td a.js-show": "showClicked"
        },
        highlightRow: function(){
            this.$el.toggleClass("active");
        },
        showClicked: function(e){
            e.preventDefault();
            e.stopPropagation();
            this.trigger("transaction:show", this.model);
        }
    });

    List.Transactions = Marionette.CompositeView.extend({
        tagName: "table",
        className: "table table-condensed",
        template: "#transaction-table",
        itemView: List.Transaction,
        itemViewContainer: "tbody"
    });
});
