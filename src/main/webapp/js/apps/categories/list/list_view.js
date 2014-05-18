Georgina.module("CategoriesApp.List", function(List, Georgina, Backbone, Marionette, $, _) {
    List.Category = Marionette.ItemView.extend({
        tagName: "li",
        template: "#category-item",
        events: {
            "click a.category-js-show": "showClicked"
        },
        showClicked: function(e){
          e.preventDefault();
          e.stopPropagation();
          this.trigger("category:show", this.model);
        }
    });

    List.Categories = Marionette.CollectionView.extend({
        tagName: "ul",
        itemView: List.Category
    });
});
