Georgina.module("CategoriesApp.List", function(List, Georgina, Backbone, Marionette, $, _) {
    List.Category = Marionette.ItemView.extend({
        tagName: "li",
        template: "#category-item",
        events: {
            "click td a.category-js-show": "showClicked"
        },
        showClicked: function(e){
          console.log("you clicked a category");
        }
    });

    List.Categories = Marionette.CollectionView.extend({
        tagName: "ul",
        itemView: List.Category
    });
});
