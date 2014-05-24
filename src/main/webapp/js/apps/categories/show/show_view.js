Georgina.module("CategoriesApp.Show", function(Show, Georgina, Backbone, Marionette, $, _){
    Show.Category = Marionette.ItemView.extend({
        template: "#summary-view",
        events: {
            "click a.js-back": "showTransactions"
        },
        showTransactions: function(e){
          console.log("you triggered a back");
          e.preventDefault();
          e.stopPropagation();
          Georgina.trigger("transactions:list");
        }
    });
});

